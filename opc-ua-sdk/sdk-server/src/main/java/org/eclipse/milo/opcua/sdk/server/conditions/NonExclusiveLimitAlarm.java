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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for a NonExclusiveLimitAlarm instance (Part 9 §5.8.20): one independent TwoStateVariable
 * per configured limit (HighHighState/HighState/LowState/LowLowState), each a sub-state of
 * ActiveState, so multiple limits can be violated simultaneously (a value above HighHigh violates
 * High too).
 *
 * <p>{@link #evaluate} applies each limit's threshold and deadband hysteresis independently and
 * uses Milo's scalar precedence to select the effective state. Applications that compute a compound
 * result themselves use {@link #setLimitStates} to replace the complete active set and select its
 * effective state without imposing scalar ordering. Each reduced-state change is applied
 * atomically; activation and changes while the alarm remains active start a new acknowledgement
 * cycle.
 */
public class NonExclusiveLimitAlarm extends LimitAlarm {

  private final Map<ExclusiveLimitState, TwoStateVariableTypeNode> limitStates =
      new EnumMap<>(ExclusiveLimitState.class);

  private volatile @Nullable ExclusiveLimitState effectiveLimitState;

  /**
   * Create NonExclusiveLimitAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link NonExclusiveLimitAlarmTypeNode} to wrap.
   */
  public NonExclusiveLimitAlarm(NonExclusiveLimitAlarmTypeNode node) {
    super(node);

    putLimitState(ExclusiveLimitState.HIGH_HIGH, node.getHighHighStateNode());
    putLimitState(ExclusiveLimitState.HIGH, node.getHighStateNode());
    putLimitState(ExclusiveLimitState.LOW, node.getLowStateNode());
    putLimitState(ExclusiveLimitState.LOW_LOW, node.getLowLowStateNode());

    effectiveLimitState = mostSevereViolated(currentActiveLimitStates());
  }

  private void putLimitState(ExclusiveLimitState limit, @Nullable TwoStateVariableTypeNode state) {
    if (state != null) {
      limitStates.put(limit, state);

      ensureTwoStateDefaults(state, false, stateTexts(limit));
      installDisabledReadFilter(state);
    }
  }

  /**
   * Create a NonExclusiveLimitAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least a High or Low
   *     limit must be configured, obeying HighHigh &gt; High &gt; Low &gt; LowLow.
   * @return the created {@link NonExclusiveLimitAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static NonExclusiveLimitAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateLimits(true);

    return build(
        builder,
        NodeIds.NonExclusiveLimitAlarmType,
        node -> new NonExclusiveLimitAlarm((NonExclusiveLimitAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveLimitAlarm attach(NonExclusiveLimitAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveLimitAlarm attach(
      NonExclusiveLimitAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached -> new NonExclusiveLimitAlarm((NonExclusiveLimitAlarmTypeNode) attached));
  }

  /**
   * Complete and attach behavior to a pre-existing instance without replacing its identity.
   *
   * @param context the context whose NodeManager owns the instance.
   * @param nodeId the existing ConditionId.
   * @param configure receives the adopt-mode builder.
   * @return the adopted, unregistered behavior.
   * @throws UaException if validation or in-place completion fails.
   */
  public static NonExclusiveLimitAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, NonExclusiveLimitAlarmTypeNode.class);
    configure.accept(builder);
    builder.validateLimits(true);
    return build(
        builder,
        NodeIds.NonExclusiveLimitAlarmType,
        node -> new NonExclusiveLimitAlarm((NonExclusiveLimitAlarmTypeNode) node));
  }

  @Override
  public NonExclusiveLimitAlarmTypeNode getNode() {
    return (NonExclusiveLimitAlarmTypeNode) super.getNode();
  }

  /**
   * Check whether {@code limit} is currently violated.
   *
   * @param limit the limit to check.
   * @return {@code true} if the limit's state variable exists and reads active.
   */
  public boolean isLimitActive(ExclusiveLimitState limit) {
    return booleanId(limitStates.get(limit), false);
  }

  /**
   * Atomically replace the complete set of active limit states with an application-computed result.
   * An empty set requires a null effective state; a non-empty set requires an effective state that
   * belongs to it. The effective state controls Message, ActiveState's EffectiveDisplayName, and
   * Severity without restricting which configured states may coexist.
   *
   * <pre>{@code
   * alarm.setLimitStates(
   *     Set.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW),
   *     ExclusiveLimitState.LOW);
   * }</pre>
   *
   * <p>The input set is copied before the Condition is touched. Activation and changing either
   * membership or the effective state while the alarm remains active start a new acknowledgement
   * cycle. Reapplying an identical tuple is a no-op after any due lazy shelving expiry when
   * ActiveState is coherent and the effective Severity is unchanged; an incoherent loaded
   * ActiveState is repaired, and a Severity correction is applied as a configuration event without
   * starting a new acknowledgement cycle.
   *
   * <p>All modeled state writes in one call share one transition time. Changed per-limit states
   * receive that time, ActiveState.TransitionTime changes only when Active changes, and membership
   * changes advance ActiveState.EffectiveTransitionTime. Changing only {@code effectiveState}
   * renews acknowledgement and updates presentation without changing any of those modeled-state
   * transition times.
   *
   * @param activeStates the complete set of limit states that should be active.
   * @param effectiveState the active state selected for Message, EffectiveDisplayName, and
   *     Severity, or {@code null} when {@code activeStates} is empty.
   * @throws IllegalArgumentException if the set and effective state do not form a valid tuple, or
   *     if a requested state lacks either its modeled state variable or configured limit Property.
   */
  public void setLimitStates(
      Set<ExclusiveLimitState> activeStates, @Nullable ExclusiveLimitState effectiveState) {

    Set<ExclusiveLimitState> normalizedStates = Set.copyOf(activeStates);
    validateLimitStates(normalizedStates, effectiveState);

    runLocked(
        () -> {
          applyShelvingExpiryIfDue();
          applyLimitStates(normalizedStates, effectiveState);
        });
  }

  /**
   * Get the active limit state currently selected for Message, EffectiveDisplayName, and Severity.
   *
   * @return the selected effective state, or {@code null} while no limit state is active.
   */
  public @Nullable ExclusiveLimitState getEffectiveLimitState() {
    return effectiveLimitState;
  }

  @Override
  public void evaluate(double value) {
    runLocked(
        () -> {
          applyShelvingExpiryIfDue();

          if (!Double.isFinite(value)) {
            // A non-finite sampled value (e.g. a sensor fault) is bad data, not a return to normal:
            // hold the limit/active states after applying any due lazy shelving expiry.
            return;
          }

          EnumSet<ExclusiveLimitState> targets = EnumSet.noneOf(ExclusiveLimitState.class);

          for (ExclusiveLimitState limit : limitStates.keySet()) {
            boolean wasViolated = isLimitActive(limit);
            boolean violated = limitViolated(limit, wasViolated, value);

            if (violated) {
              targets.add(limit);
            }
          }

          applyLimitStates(targets, mostSevereViolated(targets));
        });
  }

  /**
   * {@inheritDoc}
   *
   * <p>A non-exclusive limit alarm activates into one or more independent limit states, so it
   * cannot be activated without naming them: {@link #setActive(boolean) setActive(true)} throws,
   * and applications drive activation through {@link #setLimitStates} or {@link #evaluate}. {@code
   * setActive(false)} clears every violated limit state and deactivates the alarm as one
   * transition.
   *
   * @throws IllegalArgumentException if {@code active} is {@code true}.
   */
  @Override
  public void setActive(boolean active) {
    if (active) {
      throw new IllegalArgumentException(
          "a non-exclusive limit alarm activates into one or more limit states;"
              + " use setLimitStates(activeStates, effectiveState) or evaluate(value)");
    }

    runLocked(
        () -> {
          applyShelvingExpiryIfDue();
          applyLimitStates(Set.of(), null);
        });
  }

  @Override
  Set<ExclusiveLimitState> captureActiveLimits() {
    return currentActiveLimitStates();
  }

  @Override
  @Nullable ExclusiveLimitState captureEffectiveLimitState() {
    return effectiveLimitState;
  }

  @Override
  void applySnapshot(
      ConditionSnapshot snapshot,
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      DateTime time) {

    boolean wasActive = isActive();
    Set<ExclusiveLimitState> previousStates = currentActiveLimitStates();
    Set<ExclusiveLimitState> activeLimits = restoredActiveLimits(trunkSnapshot);
    ExclusiveLimitState restoredEffectiveState =
        restoredEffectiveLimitState(trunkSnapshot, activeLimits);

    super.applySnapshot(snapshot, trunkSnapshot, !activeLimits.isEmpty(), time);

    // Restore replaces every configured limit state: limits the snapshot omits go inactive, so an
    // earlier restore's violations cannot linger.
    for (Map.Entry<ExclusiveLimitState, TwoStateVariableTypeNode> entry : limitStates.entrySet()) {
      ExclusiveLimitState limit = entry.getKey();
      boolean violated = activeLimits.contains(limit);
      if (isLimitActive(limit) != violated) {
        setTwoState(entry.getValue(), violated, stateTexts(limit), time);
      }
    }

    effectiveLimitState = restoredEffectiveState;

    applyRestoredPresentation(
        restoredEffectiveState,
        wasActive == activeLimits.isEmpty() || !previousStates.equals(activeLimits),
        snapshot,
        time);
  }

  /**
   * Apply a complete reduced state as one Condition mutation. The caller holds the Condition lock
   * and has already applied any due shelving expiry.
   *
   * @param activeStates the complete target membership.
   * @param selectedState the target effective state, or {@code null} for an empty membership.
   */
  private void applyLimitStates(
      Set<ExclusiveLimitState> activeStates, @Nullable ExclusiveLimitState selectedState) {

    Set<ExclusiveLimitState> currentStates = currentActiveLimitStates();
    boolean membershipChanged = !currentStates.equals(activeStates);
    boolean selectionChanged = effectiveLimitState != selectedState;
    boolean activeStateChanged = isActive() == activeStates.isEmpty();
    boolean severityChanged = effectiveSeverityChanged(selectedState);

    if (!membershipChanged && !selectionChanged && !activeStateChanged && !severityChanged) {
      return;
    }

    boolean reducedStateChanged = membershipChanged || selectionChanged;

    withStateChange(
        transitionMessage(selectedState),
        now -> {
          for (Map.Entry<ExclusiveLimitState, TwoStateVariableTypeNode> entry :
              limitStates.entrySet()) {
            ExclusiveLimitState limit = entry.getKey();
            boolean targetActive = activeStates.contains(limit);
            if (isLimitActive(limit) != targetActive) {
              setTwoState(entry.getValue(), targetActive, stateTexts(limit), now);
            }
          }

          effectiveLimitState = selectedState;

          if (reducedStateChanged || activeStateChanged) {
            applyActiveTransition(selectedState, reducedStateChanged, membershipChanged, now);
          } else {
            applyEffectiveSeverity(selectedState, now);
          }
        });
  }

  /** Validate an application-computed tuple before shelving or Condition state is touched. */
  private void validateLimitStates(
      Set<ExclusiveLimitState> activeStates, @Nullable ExclusiveLimitState selectedState) {

    if (activeStates.isEmpty() && selectedState != null) {
      throw new IllegalArgumentException(
          "an inactive non-exclusive limit alarm cannot have an effective limit state");
    }
    if (!activeStates.isEmpty() && selectedState == null) {
      throw new IllegalArgumentException(
          "an active non-exclusive limit alarm requires an effective limit state");
    }
    if (selectedState != null && !activeStates.contains(selectedState)) {
      throw new IllegalArgumentException("the effective limit state must belong to active states");
    }

    for (ExclusiveLimitState state : activeStates) {
      if (!limitStates.containsKey(state) || !hasConfiguredLimit(state)) {
        throw new IllegalArgumentException(
            "cannot activate an unsupported " + state.stateName() + " limit state");
      }
    }
  }

  /** Read the currently active modeled limit states into an unshared set. */
  private Set<ExclusiveLimitState> currentActiveLimitStates() {
    EnumSet<ExclusiveLimitState> activeStates = EnumSet.noneOf(ExclusiveLimitState.class);

    for (ExclusiveLimitState limit : limitStates.keySet()) {
      if (isLimitActive(limit)) {
        activeStates.add(limit);
      }
    }

    return activeStates;
  }

  /** Select the captured active states representable by this destination instance. */
  private Set<ExclusiveLimitState> restoredActiveLimits(
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot) {

    EnumSet<ExclusiveLimitState> activeStates = EnumSet.noneOf(ExclusiveLimitState.class);

    if (trunkSnapshot == null || !Boolean.TRUE.equals(trunkSnapshot.active())) {
      return activeStates;
    }

    for (ExclusiveLimitState state : trunkSnapshot.activeLimits()) {
      if (limitStates.containsKey(state) && hasConfiguredLimit(state)) {
        activeStates.add(state);
      }
    }

    return activeStates;
  }

  /** Use a valid persisted selection, or the scalar selector for a legacy or partial snapshot. */
  private static @Nullable ExclusiveLimitState restoredEffectiveLimitState(
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      Set<ExclusiveLimitState> activeStates) {

    if (activeStates.isEmpty()) {
      return null;
    }

    ExclusiveLimitState persistedState =
        trunkSnapshot != null ? trunkSnapshot.effectiveLimitState() : null;
    return persistedState != null && activeStates.contains(persistedState)
        ? persistedState
        : mostSevereViolated(activeStates);
  }

  /**
   * The most severe violated limit in {@code targets}, or {@code null} if none is violated: a
   * first-match scan in {@link ExclusiveLimitState#BY_EXCURSION excursion order}, so a value above
   * HighHigh (which also violates High) reports HighHigh and below LowLow reports LowLow.
   */
  private static @Nullable ExclusiveLimitState mostSevereViolated(
      Set<ExclusiveLimitState> targets) {

    for (ExclusiveLimitState limit : ExclusiveLimitState.BY_EXCURSION) {
      if (targets.contains(limit)) {
        return limit;
      }
    }

    return null;
  }

  /** The state texts of a limit's TwoStateVariable, matching the ns=0 TrueState/FalseState. */
  private static StateTexts stateTexts(ExclusiveLimitState limit) {
    return new StateTexts(limit.stateName() + " active", limit.stateName() + " inactive");
  }
}
