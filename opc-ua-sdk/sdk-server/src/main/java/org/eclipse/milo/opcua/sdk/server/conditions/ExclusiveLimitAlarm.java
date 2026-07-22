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

import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an ExclusiveLimitAlarm instance (Part 9 §5.8.19.3): at most one limit is violated at
 * a time, tracked by the LimitState ExclusiveLimitStateMachine as a sub-state of ActiveState.
 *
 * <p>{@link #evaluate} derives the state from the configured limits with deadband hysteresis:
 * escalation (or a violation on the other side) applies immediately, while de-escalation and
 * return-to-normal are held until the value retreats within the current limit by its deadband.
 * {@link #setActive(boolean, ExclusiveLimitState)} is the app-driven alternative. Each transition
 * generates one event carrying the new limit state and its per-limit severity, and is a new state
 * needing acknowledgement.
 */
public class ExclusiveLimitAlarm extends LimitAlarm {

  private final ExclusiveLimitStateMachine limitStateMachine;

  /**
   * Create ExclusiveLimitAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link ExclusiveLimitAlarmTypeNode} to wrap.
   */
  public ExclusiveLimitAlarm(ExclusiveLimitAlarmTypeNode node) {
    super(node);

    // The machine tracks limit state internally, so it is created even if the Mandatory LimitState
    // node is somehow absent (a node wrapped outside create()); otherwise getLimitState() would be
    // pinned null and applyExclusiveState's change-guard would refire on every evaluate at a stable
    // limit. When present, the machine seeds CurrentState/LastTransition before the disabled-read
    // filter walks the subtree, so their property nodes are covered.
    ExclusiveLimitStateMachineTypeNode limitStateNode = node.getLimitStateNode();
    limitStateMachine = new ExclusiveLimitStateMachine(limitStateNode);
    if (limitStateNode != null) {
      installDisabledReadFilter(limitStateNode);
    }
  }

  /**
   * Create an ExclusiveLimitAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least one limit must be
   *     configured, obeying HighHigh &gt; High &gt; Low &gt; LowLow.
   * @return the created {@link ExclusiveLimitAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static ExclusiveLimitAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateLimits(false);

    return build(
        builder,
        NodeIds.ExclusiveLimitAlarmType,
        node -> new ExclusiveLimitAlarm((ExclusiveLimitAlarmTypeNode) node));
  }

  @Override
  public ExclusiveLimitAlarmTypeNode getNode() {
    return (ExclusiveLimitAlarmTypeNode) super.getNode();
  }

  /**
   * Get the current limit state.
   *
   * @return the current {@link ExclusiveLimitState}, or {@code null} while the alarm is inactive.
   */
  public @Nullable ExclusiveLimitState getLimitState() {
    return limitStateMachine.getState();
  }

  @Override
  public void evaluate(double value) {
    runLocked(
        () -> {
          applyShelvingExpiryIfDue();

          if (!Double.isFinite(value)) {
            // A non-finite sampled value (e.g. a sensor fault) is bad data, not a return to normal:
            // hold the limit/active state after applying any due lazy shelving expiry.
            return;
          }

          applyExclusiveState(computeTarget(getLimitState(), value));
        });
  }

  /**
   * Set the active and limit state of the alarm directly — the fully app-driven alternative to
   * {@link #evaluate}. A call that does not change the state is a no-op.
   *
   * @param active the new active state.
   * @param limitState the violated limit; required when activating, must be {@code null} when
   *     deactivating.
   * @throws IllegalArgumentException if {@code active} and {@code limitState} disagree.
   */
  public void setActive(boolean active, @Nullable ExclusiveLimitState limitState) {
    if (active && limitState == null) {
      throw new IllegalArgumentException("an active exclusive limit alarm requires a limit state");
    }
    if (!active && limitState != null) {
      throw new IllegalArgumentException(
          "an inactive exclusive limit alarm cannot hold a limit state");
    }
    if (active && !hasConfiguredLimit(limitState)) {
      throw new IllegalArgumentException(
          "cannot activate an unconfigured " + limitState.stateName() + " limit state");
    }

    runLocked(
        () -> {
          applyShelvingExpiryIfDue();
          applyExclusiveState(limitState);
        });
  }

  /**
   * {@inheritDoc}
   *
   * <p>An exclusive limit alarm activates into a limit state: deactivation clears the LimitState
   * machine, activation must go through {@link #setActive(boolean, ExclusiveLimitState)} or {@link
   * #evaluate}.
   *
   * @throws IllegalArgumentException if {@code active} is {@code true}.
   */
  @Override
  public void setActive(boolean active) {
    if (active) {
      throw new IllegalArgumentException(
          "an exclusive limit alarm activates into a limit state;"
              + " use setActive(true, ExclusiveLimitState) or evaluate(value)");
    }

    setActive(false, null);
  }

  @Override
  Set<ExclusiveLimitState> captureActiveLimits() {
    ExclusiveLimitState limitState = getLimitState();
    return limitState != null ? Set.of(limitState) : Set.of();
  }

  @Override
  void applySnapshot(
      ConditionSnapshot snapshot,
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      DateTime time) {

    super.applySnapshot(snapshot, trunkSnapshot, time);

    ExclusiveLimitState target =
        isActive() && trunkSnapshot != null
            ? ExclusiveLimitState.mostSevere(trunkSnapshot.activeLimits())
            : null;

    // Restore replaces the machine state: an inactive snapshot clears a previous restore's limit.
    limitStateMachine.setState(target, time);
    setActiveEffectiveDisplayName(target);
  }

  /**
   * The target state for {@code value}: the most severe violated limit, except that the current
   * state's deadband holds off de-escalation on the same side and return-to-normal.
   */
  private @Nullable ExclusiveLimitState computeTarget(
      @Nullable ExclusiveLimitState current, double value) {

    ExclusiveLimitState entered = enteredLimit(value);
    if (entered == current) {
      return current;
    }

    if (current != null && limitViolated(current, true, value)) {
      boolean escalation =
          entered != null
              && entered.isHighSide() == current.isHighSide()
              && entered.severityRank() > current.severityRank();
      boolean crossSide = entered != null && entered.isHighSide() != current.isHighSide();

      if (!escalation && !crossSide) {
        return current;
      }
    }

    return entered;
  }

  /**
   * Apply a transition to {@code target} ({@code null} = inactive) as one state change: the
   * LimitState machine, then the shared activation tail (ActiveState with the
   * acknowledgement-needed rule, EffectiveDisplayName, and the per-limit severity) — one event per
   * transition.
   */
  private void applyExclusiveState(@Nullable ExclusiveLimitState target) {
    boolean targetActive = target != null;
    boolean limitStateChanged = getLimitState() != target || isActive() != targetActive;
    boolean severityChanged = effectiveSeverityChanged(target);

    withStateChange(
        transitionMessage(target),
        () -> limitStateChanged || severityChanged,
        now -> {
          if (limitStateChanged) {
            limitStateMachine.setState(target, now);
            applyActiveTransition(target, true, now);
          } else {
            applyEffectiveSeverity(target, now);
          }
        });
  }
}
