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
 * <p>{@link #evaluate} applies each limit's threshold and deadband hysteresis independently;
 * ActiveState is the disjunction of the limit states. Each evaluation that changes any state
 * generates one event carrying the per-limit severity of the most severe violated limit, and is a
 * new state needing acknowledgement.
 */
public class NonExclusiveLimitAlarm extends LimitAlarm {

  private final Map<ExclusiveLimitState, TwoStateVariableTypeNode> limitStates =
      new EnumMap<>(ExclusiveLimitState.class);

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

          var targets = new EnumMap<ExclusiveLimitState, Boolean>(ExclusiveLimitState.class);
          var changed = new EnumMap<ExclusiveLimitState, Boolean>(ExclusiveLimitState.class);

          for (ExclusiveLimitState limit : limitStates.keySet()) {
            boolean wasViolated = isLimitActive(limit);
            boolean violated = limitViolated(limit, wasViolated, value);

            targets.put(limit, violated);
            if (violated != wasViolated) {
              changed.put(limit, violated);
            }
          }

          ExclusiveLimitState mostSevere = mostSevereViolated(targets);
          if (!changed.isEmpty() || effectiveSeverityChanged(mostSevere)) {
            applyLimitStates(targets, changed);
          }
        });
  }

  /**
   * {@inheritDoc}
   *
   * <p>A non-exclusive limit alarm activates into one or more independent limit states, so it
   * cannot be activated without naming them: {@link #setActive(boolean) setActive(true)} throws,
   * and applications drive activation through {@link #evaluate}. {@code setActive(false)} clears
   * every violated limit state and deactivates the alarm as one transition.
   *
   * @throws IllegalArgumentException if {@code active} is {@code true}.
   */
  @Override
  public void setActive(boolean active) {
    if (active) {
      throw new IllegalArgumentException(
          "a non-exclusive limit alarm activates into one or more limit states;"
              + " use evaluate(value)");
    }

    runLocked(
        () -> {
          applyShelvingExpiryIfDue();

          var targets = new EnumMap<ExclusiveLimitState, Boolean>(ExclusiveLimitState.class);
          var changed = new EnumMap<ExclusiveLimitState, Boolean>(ExclusiveLimitState.class);

          for (ExclusiveLimitState limit : limitStates.keySet()) {
            targets.put(limit, false);
            if (isLimitActive(limit)) {
              changed.put(limit, false);
            }
          }

          if (!changed.isEmpty()) {
            applyLimitStates(targets, changed);
          }
        });
  }

  @Override
  Set<ExclusiveLimitState> captureActiveLimits() {
    var activeLimits = EnumSet.noneOf(ExclusiveLimitState.class);
    for (ExclusiveLimitState limit : limitStates.keySet()) {
      if (isLimitActive(limit)) {
        activeLimits.add(limit);
      }
    }

    return activeLimits;
  }

  @Override
  void applySnapshot(
      ConditionSnapshot snapshot,
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      DateTime time) {

    super.applySnapshot(snapshot, trunkSnapshot, time);

    // Restore replaces every configured limit state: limits the snapshot omits go inactive, so an
    // earlier restore's violations cannot linger.
    Set<ExclusiveLimitState> activeLimits =
        isActive() && trunkSnapshot != null ? trunkSnapshot.activeLimits() : Set.of();

    for (Map.Entry<ExclusiveLimitState, TwoStateVariableTypeNode> entry : limitStates.entrySet()) {
      ExclusiveLimitState limit = entry.getKey();
      boolean violated = activeLimits.contains(limit);
      if (isLimitActive(limit) != violated) {
        setTwoState(entry.getValue(), violated, stateTexts(limit), time);
      }
    }

    setActiveEffectiveDisplayName(ExclusiveLimitState.mostSevere(activeLimits));
  }

  /**
   * Apply the per-limit target states as one state change: write the {@code changed} limit state
   * variables, then the activation tail shared by all limit alarms (ActiveState with the
   * acknowledgement-needed rule, EffectiveDisplayName, and the per-limit severity of the most
   * severe violated limit) — one event per evaluation that changes anything.
   *
   * @param targets every configured limit's target violation state, for the most-severe decision.
   * @param changed only the limits whose state changed, to be written; empty for a
   *     runtime-configuration-only Severity change.
   */
  private void applyLimitStates(
      Map<ExclusiveLimitState, Boolean> targets, Map<ExclusiveLimitState, Boolean> changed) {

    ExclusiveLimitState mostSevere = mostSevereViolated(targets);
    boolean limitStateChanged = !changed.isEmpty();

    withStateChange(
        transitionMessage(mostSevere),
        now -> {
          for (Map.Entry<ExclusiveLimitState, Boolean> entry : changed.entrySet()) {
            ExclusiveLimitState limit = entry.getKey();
            setTwoState(limitStates.get(limit), entry.getValue(), stateTexts(limit), now);
          }

          if (limitStateChanged) {
            applyActiveTransition(mostSevere, true, now);
          } else {
            applyEffectiveSeverity(mostSevere, now);
          }
        });
  }

  /**
   * The most severe violated limit in {@code targets}, or {@code null} if none is violated: a
   * first-match scan in {@link ExclusiveLimitState#BY_EXCURSION excursion order}, so a value above
   * HighHigh (which also violates High) reports HighHigh and below LowLow reports LowLow.
   */
  private static @Nullable ExclusiveLimitState mostSevereViolated(
      Map<ExclusiveLimitState, Boolean> targets) {

    for (ExclusiveLimitState limit : ExclusiveLimitState.BY_EXCURSION) {
      if (Boolean.TRUE.equals(targets.get(limit))) {
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
