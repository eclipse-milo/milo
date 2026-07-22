/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an AlarmCondition's optional ShelvedStateMachine component (Part 9 §5.8.10,
 * §5.8.11): the TimedShelve/OneShotShelve/Unshelve method semantics with their status codes,
 * CurrentState/LastTransition coherence, UnshelveTime computed on read, timed expiry with a lazy
 * fallback, one-shot release on the active→inactive transition, and MaxTimeShelved enforcement.
 *
 * <p>The unshelve deadline is stored as an absolute time and re-checked on every touch (any
 * shelving method call or ActiveState transition), so a late or lost expiry timer degrades to
 * late-but-correct, never stuck: the timer is only a prompt.
 *
 * <p>Shelving transitions update ShelvingState and SuppressedOrShelved and generate condition
 * events; live alarm events keep flowing while shelved with SuppressedOrShelved={@code true} in
 * their fields.
 */
final class ShelvingRuntime {

  /** Immutable {@code (state, deadline)} pair for the lock-free UnshelveTime read. */
  private record ReadSnapshot(ShelvedState state, @Nullable DateTime deadline) {}

  private final AlarmCondition alarm;
  private final ShelvedStateMachineTypeNode node;

  private final @Nullable FiniteStateVariableTypeNode currentState;
  private final @Nullable FiniteTransitionVariableTypeNode lastTransition;

  private volatile ShelvedState state = ShelvedState.UNSHELVED;

  /** Absolute time at which shelving expires, or {@code null} if no deadline applies. */
  private volatile @Nullable DateTime unshelveDeadline;

  /**
   * Lock-free mirror of {@code (state, unshelveDeadline)} as one immutable pair, so the
   * UnshelveTime read filter — which runs off the read path without the Condition's lock — always
   * observes a consistent state/deadline combination rather than a torn read straddling a
   * transition. Updated under the lock wherever {@code state}/{@code unshelveDeadline} change.
   */
  private volatile ReadSnapshot readSnapshot;

  // Guarded by the owning Condition's lock.
  private long shelveGeneration = 0;
  private @Nullable ScheduledFuture<?> expiryTimer;
  private boolean expiryTimerSuppressed = false;
  private boolean shutdown = false;

  ShelvingRuntime(AlarmCondition alarm, ShelvedStateMachineTypeNode node) {
    this.alarm = alarm;
    this.node = node;

    currentState = node.getCurrentStateNode();
    lastTransition = node.getLastTransitionNode();

    NodeId currentStateId = currentState != null ? currentState.getId() : null;
    for (ShelvedState shelvedState : ShelvedState.values()) {
      if (shelvedState.stateId().equals(currentStateId)) {
        state = shelvedState;
      }
    }

    readSnapshot = new ReadSnapshot(state, unshelveDeadline);

    if (currentStateId == null) {
      setCurrentState(ShelvedState.UNSHELVED);
    }

    // Seed LastTransition so its property nodes exist before read filters are installed.
    if (lastTransition != null && lastTransition.getTransitionTime() == null) {
      lastTransition.setValue(new DataValue(new Variant(LocalizedText.NULL_VALUE)));
      lastTransition.setId(NodeId.NULL_VALUE);
      lastTransition.setTransitionTime(DateTime.NULL_VALUE);
    }

    node.setUnshelveTime(0.0);
  }

  ShelvedStateMachineTypeNode getNode() {
    return node;
  }

  Optional<UaMethodNode> findMethodNode(NodeId methodId) {
    return Optional.ofNullable(node.findMethodNode(methodId));
  }

  /**
   * Install the filter computing UnshelveTime on read. Installed after the disabled-read filter so
   * disabled gating applies before the computed value.
   */
  void installUnshelveTimeFilter() {
    PropertyTypeNode unshelveTime = node.getUnshelveTimeNode();
    if (unshelveTime != null) {
      unshelveTime.getFilterChain().addLast(new UnshelveTimeFilter());
    }
  }

  void installMethodHandlers(Map<QualifiedName, UaMethodNode> methodNodes) {
    UaMethodNode timedShelve = methodNodes.get(new QualifiedName(0, "TimedShelve"));
    if (timedShelve != null) {
      timedShelve.setInvocationHandler(
          new ShelvedStateMachineType.TimedShelveMethod(timedShelve) {
            @Override
            protected void invoke(InvocationContext context, Double shelvingTime)
                throws UaException {
              handleTimedShelve(context, shelvingTime);
            }
          });
    }

    UaMethodNode oneShotShelve = methodNodes.get(new QualifiedName(0, "OneShotShelve"));
    if (oneShotShelve != null) {
      oneShotShelve.setInvocationHandler(
          new ShelvedStateMachineType.OneShotShelveMethod(oneShotShelve) {
            @Override
            protected void invoke(InvocationContext context) throws UaException {
              handleOneShotShelve(context);
            }
          });
    }

    UaMethodNode unshelve = methodNodes.get(new QualifiedName(0, "Unshelve"));
    if (unshelve != null) {
      unshelve.setInvocationHandler(
          new ShelvedStateMachineType.UnshelveMethod(unshelve) {
            @Override
            protected void invoke(InvocationContext context) throws UaException {
              handleUnshelve(context);
            }
          });
    }
  }

  void handleTimedShelve(InvocationContext context, @Nullable Double shelvingTime)
      throws UaException {

    double shelvingTimeMillis = shelvingTime != null ? shelvingTime : 0.0;

    handleShelveMethod(
        () -> {
          if (state == ShelvedState.TIMED_SHELVED) {
            throw new UaException(StatusCodes.Bad_ConditionAlreadyShelved);
          }

          // Reject non-finite (NaN, ±Infinity) as well as non-positive and over-limit times: a NaN
          // slips past both magnitude comparisons (IEEE-754), and +Infinity past them when there is
          // no MaxTimeShelved, otherwise reaching deadline() and shelving with a bogus expiry.
          Double maxTimeShelved = alarm.getNode().getMaxTimeShelved();
          if (!Double.isFinite(shelvingTimeMillis)
              || shelvingTimeMillis <= 0.0
              || (maxTimeShelved != null && shelvingTimeMillis > maxTimeShelved)) {
            throw new UaException(StatusCodes.Bad_ShelvingTimeOutOfRange);
          }
        },
        () -> alarm.getInterceptor().onTimedShelve(context, shelvingTimeMillis),
        () ->
            shelveTransition(ShelvedState.TIMED_SHELVED, now -> deadline(now, shelvingTimeMillis)));
  }

  void handleOneShotShelve(InvocationContext context) throws UaException {
    handleShelveMethod(
        () -> {
          if (state == ShelvedState.ONE_SHOT_SHELVED) {
            throw new UaException(StatusCodes.Bad_ConditionAlreadyShelved);
          }
        },
        () -> alarm.getInterceptor().onOneShotShelve(context),
        () -> {
          // One-shot shelving lasts until the active→inactive transition, bounded by
          // MaxTimeShelved where the Property is present (§5.8.11). A non-finite MaxTimeShelved
          // (NaN/±Infinity written at runtime) is not a usable bound: treat it as unbounded rather
          // than letting deadline()'s cast turn it into 0 and immediately unshelve, mirroring the
          // finiteness guard handleTimedShelve applies to its own argument.
          Double maxTimeShelved = alarm.getNode().getMaxTimeShelved();

          if (maxTimeShelved != null && Double.isFinite(maxTimeShelved)) {
            double maxMillis = maxTimeShelved;
            shelveTransition(ShelvedState.ONE_SHOT_SHELVED, now -> deadline(now, maxMillis));
          } else {
            shelveTransition(ShelvedState.ONE_SHOT_SHELVED, null);
          }
        });
  }

  void handleUnshelve(InvocationContext context) throws UaException {
    handleShelveMethod(
        () -> {
          if (state == ShelvedState.UNSHELVED) {
            throw new UaException(StatusCodes.Bad_ConditionNotShelved);
          }
        },
        () -> alarm.getInterceptor().onUnshelve(context),
        () -> shelveTransition(ShelvedState.UNSHELVED, null));
  }

  /**
   * Shared template for the three shelving methods: under the Condition's lock, apply any due lazy
   * expiry, gate on enabled, run the per-method precondition, consult the interceptor
   * (short-circuit on {@code HANDLED}), then apply the default transition. Parallels {@link
   * Condition#handleBranchMethod} so the lazy-expiry fallback and interceptor contract cannot drift
   * between the three methods.
   */
  private void handleShelveMethod(
      ShelvePrecondition precondition,
      Condition.InterceptorCall interceptorCall,
      Runnable transition)
      throws UaException {

    alarm.runLocked(
        () -> {
          applyExpiryIfDue();

          if (!alarm.isEnabled()) {
            throw new UaException(StatusCodes.Bad_ConditionDisabled);
          }

          precondition.check();

          if (alarm.consultInterceptor(interceptorCall)
              == ConditionMethodInterceptor.Outcome.HANDLED) {
            return;
          }

          transition.run();

          // Dormant audit emission point: AuditConditionShelvingEventType.
        });
  }

  @FunctionalInterface
  private interface ShelvePrecondition {
    void check() throws UaException;
  }

  /**
   * React to an ActiveState transition, applied inside the transition's state-change mutation (the
   * Condition's lock is held): one-shot shelving releases when the alarm goes inactive, so the same
   * event reports both changes.
   */
  void onActiveTransition(boolean active, DateTime time) {
    if (!active && state == ShelvedState.ONE_SHOT_SHELVED) {
      applyTransition(ShelvedState.UNSHELVED, null, time);
    }
  }

  /** Capture the shelving state for a {@link ConditionSnapshot}; the Condition's lock is held. */
  ConditionSnapshot.ShelvingSnapshot captureState() {
    return new ConditionSnapshot.ShelvingSnapshot(state, unshelveDeadline);
  }

  /**
   * Restore shelving state from a snapshot, silently: CurrentState and SuppressedOrShelved are
   * written but no transition is modelled (LastTransition keeps its seeded NULL value) and no event
   * is generated. A deadline already in the past arms no timer — the restored alarm unshelves
   * lazily on its first touch, composing with {@link #applyExpiryIfDue()}. TimedShelved without a
   * deadline restores as Unshelved.
   *
   * <p>Must be called while holding the owning Condition's lock, before the Condition has generated
   * events.
   */
  void restoreState(ShelvedState target, @Nullable DateTime deadline) {
    if (target == ShelvedState.TIMED_SHELVED && deadline == null) {
      // TimedShelved without its deadline could never expire — both the timer and the lazy
      // fallback key on the deadline — so the incoherent pair normalizes to the Unshelved
      // recovery default instead of shelving forever.
      target = ShelvedState.UNSHELVED;
    }

    DateTime effectiveDeadline = target != ShelvedState.UNSHELVED ? deadline : null;

    state = target;
    unshelveDeadline = effectiveDeadline;
    readSnapshot = new ReadSnapshot(target, effectiveDeadline);
    shelveGeneration++;
    cancelExpiryTimer();

    setCurrentState(target);

    alarm.getNode().setSuppressedOrShelved(target != ShelvedState.UNSHELVED);

    if (effectiveDeadline != null
        && !expiryTimerSuppressed
        && effectiveDeadline.getJavaTime() > System.currentTimeMillis()) {
      scheduleExpiryTimer(effectiveDeadline);
    }
  }

  /**
   * Apply expiry if the unshelve deadline has passed — the lazy fallback consulted on every touch,
   * making a lost or late timer degrade to late-but-correct.
   *
   * <p>Must be called while holding the Condition's lock.
   */
  void applyExpiryIfDue() {
    DateTime deadline = unshelveDeadline;

    if (!shutdown
        && state != ShelvedState.UNSHELVED
        && deadline != null
        && System.currentTimeMillis() >= deadline.getJavaTime()) {
      shelveTransition(ShelvedState.UNSHELVED, null);
    }
  }

  private void shelveTransition(
      ShelvedState target, @Nullable Function<DateTime, DateTime> deadline) {

    alarm.withStateChange(
        target.stateName(),
        now -> applyTransition(target, deadline != null ? deadline.apply(now) : null, now));
  }

  private void applyTransition(ShelvedState target, @Nullable DateTime deadline, DateTime time) {
    ShelvedState from = state;
    if (from == target) {
      return;
    }

    state = target;
    unshelveDeadline = deadline;
    readSnapshot = new ReadSnapshot(target, deadline);
    shelveGeneration++;
    cancelExpiryTimer();

    setCurrentState(target);
    setLastTransition(from, target, time);

    // In v1 SuppressedOrShelved aggregates shelving only; suppression and out-of-service join
    // the formula when their states ship.
    alarm.getNode().setSuppressedOrShelved(target != ShelvedState.UNSHELVED);

    if (deadline != null && !expiryTimerSuppressed) {
      scheduleExpiryTimer(deadline);
    }
  }

  private void setCurrentState(ShelvedState target) {
    if (currentState == null) {
      return;
    }

    currentState.setValue(new DataValue(new Variant(LocalizedText.english(target.stateName()))));
    currentState.setId(target.stateId());
  }

  private void setLastTransition(ShelvedState from, ShelvedState to, DateTime time) {
    if (lastTransition == null) {
      return;
    }

    String transitionName = from.stateName() + "To" + to.stateName();

    lastTransition.setValue(new DataValue(new Variant(LocalizedText.english(transitionName))));
    lastTransition.setId(transitionId(from, to));
    lastTransition.setTransitionTime(time);
  }

  private static NodeId transitionId(ShelvedState from, ShelvedState to) {
    return switch (from) {
      case UNSHELVED ->
          to == ShelvedState.TIMED_SHELVED
              ? NodeIds.ShelvedStateMachineType_UnshelvedToTimedShelved
              : NodeIds.ShelvedStateMachineType_UnshelvedToOneShotShelved;
      case TIMED_SHELVED ->
          to == ShelvedState.UNSHELVED
              ? NodeIds.ShelvedStateMachineType_TimedShelvedToUnshelved
              : NodeIds.ShelvedStateMachineType_TimedShelvedToOneShotShelved;
      case ONE_SHOT_SHELVED ->
          to == ShelvedState.UNSHELVED
              ? NodeIds.ShelvedStateMachineType_OneShotShelvedToUnshelved
              : NodeIds.ShelvedStateMachineType_OneShotShelvedToTimedShelved;
    };
  }

  private static DateTime deadline(DateTime now, double millisFromNow) {
    long javaTime;
    try {
      // Round up: a positive but sub-millisecond duration (e.g. a TimedShelve of 0.5) must still
      // yield a deadline strictly after now rather than truncating to 0 and expiring immediately.
      javaTime = Math.addExact(now.getJavaTime(), (long) Math.ceil(millisFromNow));
    } catch (ArithmeticException e) {
      // Saturate instead of wrapping to a past instant for an unbounded (very large) shelving time.
      javaTime = Long.MAX_VALUE;
    }
    return new DateTime(Instant.ofEpochMilli(javaTime));
  }

  private void scheduleExpiryTimer(DateTime deadline) {
    if (shutdown) {
      return;
    }

    long generation = shelveGeneration;
    long delayMillis = Math.max(0L, deadline.getJavaTime() - System.currentTimeMillis());

    try {
      expiryTimer =
          alarm
              .getServer()
              .getScheduledExecutorService()
              .schedule(() -> dispatchExpiry(generation), delayMillis, TimeUnit.MILLISECONDS);
    } catch (RejectedExecutionException e) {
      // The deadline remains authoritative. Complete the shelving transition and rely on the
      // lock-serialized lazy fallback rather than returning an error after partially changing
      // CurrentState, LastTransition, and SuppressedOrShelved.
      expiryTimer = null;
    }
  }

  private void dispatchExpiry(long generation) {
    try {
      alarm.getServer().getExecutorService().execute(() -> onExpiryTimer(generation));
    } catch (RejectedExecutionException e) {
      // The scheduler is only a prompt and the deadline remains authoritative. If the regular
      // executor is shutting down or otherwise rejects the work, the next read, evaluation, or
      // shelving method touch applies expiry lazily.
    }
  }

  private void onExpiryTimer(long generation) {
    alarm.runLocked(
        () -> {
          if (shutdown || shelveGeneration != generation) {
            // Superseded: a later transition replaced or cleared the deadline this timer was armed
            // for; that transition's own timer (if any) is authoritative.
            return;
          }

          // A matching generation proves the armed deadline is still current and its delay has
          // fully elapsed, so expiry applies even if a coarse wall clock still reads a fraction
          // before the stored deadline.
          shelveTransition(ShelvedState.UNSHELVED, null);
        });
  }

  private void cancelExpiryTimer() {
    ScheduledFuture<?> timer = expiryTimer;
    if (timer != null) {
      timer.cancel(false);
      expiryTimer = null;
    }
  }

  /**
   * Test seam: suppress (or restore) the expiry timer without touching the stored deadline, so
   * tests can exercise the lazy-expiry fallback deterministically.
   */
  void setExpiryTimerSuppressedForTesting(boolean suppressed) {
    alarm.runLocked(
        () -> {
          expiryTimerSuppressed = suppressed;

          if (suppressed) {
            shelveGeneration++;
            cancelExpiryTimer();
          } else {
            DateTime deadline = unshelveDeadline;
            if (!shutdown && deadline != null && state != ShelvedState.UNSHELVED) {
              scheduleExpiryTimer(deadline);
            }
          }
        });
  }

  /** Test seam: make the current shelving deadline due without waiting for wall-clock time. */
  void makeExpiryDueForTesting() {
    alarm.runLocked(
        () -> {
          if (state != ShelvedState.UNSHELVED && unshelveDeadline != null) {
            DateTime due = new DateTime(Instant.ofEpochMilli(System.currentTimeMillis() - 1L));
            unshelveDeadline = due;
            readSnapshot = new ReadSnapshot(state, due);
            shelveGeneration++;
            cancelExpiryTimer();
          }
        });
  }

  /** Test seam: report whether an expiry timer is currently armed. */
  boolean hasExpiryTimerForTesting() {
    boolean[] armed = new boolean[1];
    alarm.runLocked(() -> armed[0] = expiryTimer != null);
    return armed[0];
  }

  /** Test seam: report whether {@code timer} is the expiry timer currently owned by this alarm. */
  boolean ownsExpiryTimerForTesting(ScheduledFuture<?> timer) {
    boolean[] owned = new boolean[1];
    alarm.runLocked(() -> owned[0] = expiryTimer == timer);
    return owned[0];
  }

  /** Cancel the armed expiry prompt and prevent any queued prompt from acting after shutdown. */
  void shutdown() {
    alarm.runLocked(
        () -> {
          shutdown = true;
          shelveGeneration++;
          cancelExpiryTimer();
        });
  }

  private double computeUnshelveTime() {
    // A read is a shelving touch. Apply due expiry under the Condition lock before producing the
    // value; event-field extraction may recursively read UnshelveTime, but by then the state is
    // already Unshelved and this check is a no-op.
    alarm.runLocked(this::applyExpiryIfDue);

    ReadSnapshot snapshot = readSnapshot;
    ShelvedState state = snapshot.state();
    DateTime deadline = snapshot.deadline();

    if (state == ShelvedState.UNSHELVED) {
      return 0.0;
    } else if (deadline != null) {
      return Math.max(0.0, deadline.getJavaTime() - System.currentTimeMillis());
    } else {
      // OneShotShelved with no MaxTimeShelved: shelved until explicitly released (§5.8.11).
      return Double.MAX_VALUE;
    }
  }

  /** Computes the remaining UnshelveTime on read (§5.8.11: the value counts down). */
  private class UnshelveTimeFilter implements AttributeFilter {
    @Override
    public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
      if (attributeId == AttributeId.Value) {
        return new DataValue(new Variant(computeUnshelveTime()), StatusCode.GOOD, DateTime.now());
      }

      return ctx.getAttribute(attributeId);
    }
  }
}
