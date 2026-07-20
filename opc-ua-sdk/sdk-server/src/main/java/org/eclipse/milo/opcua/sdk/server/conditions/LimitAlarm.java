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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * Behavior common to the limit-alarm family (Part 9 §5.8.18): the limit, per-limit severity, and
 * per-limit deadband configuration surface, and the {@code evaluate(value)} helper deriving alarm
 * state from a sampled value with deadband hysteresis.
 *
 * <p>Limits, severities, and deadbands are read from the instance's Property nodes on every
 * evaluation, so runtime writes to the configuration Properties affect subsequent evaluation; the
 * limit-order validation (HighHigh &gt; High &gt; Low &gt; LowLow, at least one limit) applies at
 * build time only.
 *
 * <p>The SDK never samples the alarm's input: the application observes its process value and calls
 * {@link #evaluate}, or drives the state directly via the {@code setActive} variants.
 */
public abstract class LimitAlarm extends AlarmCondition {

  /** The Property nodes configuring one limit, resolved once at construction. */
  private record LimitProperties(
      @Nullable PropertyTypeNode limit,
      @Nullable PropertyTypeNode severity,
      @Nullable PropertyTypeNode deadband) {}

  private final Map<ExclusiveLimitState, LimitProperties> limitProperties =
      new EnumMap<>(ExclusiveLimitState.class);

  private final @Nullable PropertyTypeNode activeStateEffectiveDisplayName;

  /**
   * The Condition's current base Severity: reported while active without a per-limit override and
   * restored on return-to-normal. Updated by {@link #setSeverity} so later transitions do not
   * resurrect the construction-time value.
   */
  private @Nullable UShort baseSeverity;

  /**
   * Create LimitAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link LimitAlarmTypeNode} to wrap.
   */
  public LimitAlarm(LimitAlarmTypeNode node) {
    super(node);

    baseSeverity = node.getSeverity();

    limitProperties.put(
        ExclusiveLimitState.HIGH_HIGH,
        new LimitProperties(
            node.getHighHighLimitNode(),
            node.getSeverityHighHighNode(),
            node.getHighHighDeadbandNode()));
    limitProperties.put(
        ExclusiveLimitState.HIGH,
        new LimitProperties(
            node.getHighLimitNode(), node.getSeverityHighNode(), node.getHighDeadbandNode()));
    limitProperties.put(
        ExclusiveLimitState.LOW,
        new LimitProperties(
            node.getLowLimitNode(), node.getSeverityLowNode(), node.getLowDeadbandNode()));
    limitProperties.put(
        ExclusiveLimitState.LOW_LOW,
        new LimitProperties(
            node.getLowLowLimitNode(), node.getSeverityLowLowNode(), node.getLowLowDeadbandNode()));

    TwoStateVariableTypeNode activeState = node.getActiveStateNode();
    activeStateEffectiveDisplayName =
        activeState != null ? activeState.getEffectiveDisplayNameNode() : null;
  }

  @Override
  public LimitAlarmTypeNode getNode() {
    return (LimitAlarmTypeNode) super.getNode();
  }

  /**
   * Set and remember the Condition's base Severity. Per-limit severities may temporarily replace it
   * during evaluation, but later states without an override and deactivation restore this value.
   */
  @Override
  public void setSeverity(UShort severity) {
    runLocked(
        () -> {
          baseSeverity = severity;
          super.setSeverity(severity);
        });
  }

  /**
   * Evaluate the alarm against a sampled value: derive the active/limit state from the configured
   * limits with deadband hysteresis, and apply per-limit severity — one event per resulting
   * transition.
   *
   * <p>What the value means is type-specific: the process value for level alarms, the deviation
   * from the setpoint for deviation alarms, and the rate of change for rate-of-change alarms — the
   * SDK does not compute these from the input.
   *
   * @param value the sampled value to evaluate.
   */
  public abstract void evaluate(double value);

  /**
   * Get the configured value of {@code limit}, read from the instance's Property node.
   *
   * @return the configured limit value, or {@code null} if the limit is not configured.
   */
  @Nullable Double limitValue(ExclusiveLimitState limit) {
    LimitProperties properties = limitProperties.get(limit);
    return doubleValue(properties.limit());
  }

  /** Whether the instance has the limit Property that configures {@code limit}. */
  boolean hasConfiguredLimit(ExclusiveLimitState limit) {
    return limitProperties.get(limit).limit() != null;
  }

  /** The configured deadband of {@code limit}; 0 when absent or invalid (negative/non-finite). */
  double deadbandValue(ExclusiveLimitState limit) {
    LimitProperties properties = limitProperties.get(limit);
    Double deadband = doubleValue(properties.deadband());
    return deadband != null && Double.isFinite(deadband) && deadband > 0.0 ? deadband : 0.0;
  }

  /** The configured per-limit Severity of {@code limit}, or {@code null} if not configured. */
  @Nullable UShort severityValue(ExclusiveLimitState limit) {
    PropertyTypeNode severity = limitProperties.get(limit).severity();

    Object object = severity != null ? currentValue(severity) : null;
    return object instanceof UShort severityValue ? severityValue : null;
  }

  /** The effective Severity for {@code mostSevere}, falling back to the current base Severity. */
  private @Nullable UShort effectiveSeverity(@Nullable ExclusiveLimitState mostSevere) {
    UShort severity = mostSevere != null ? severityValue(mostSevere) : null;
    return severity != null ? severity : baseSeverity;
  }

  /** Whether runtime configuration changed the effective Severity of the current target state. */
  boolean effectiveSeverityChanged(@Nullable ExclusiveLimitState mostSevere) {
    return !Objects.equals(getNode().getSeverity(), effectiveSeverity(mostSevere));
  }

  /** Apply only a runtime-configuration Severity change, without touching alarm state. */
  void applyEffectiveSeverity(@Nullable ExclusiveLimitState mostSevere, DateTime time) {
    UShort severity = effectiveSeverity(mostSevere);
    if (severity != null) {
      applySeverity(severity, time);
    }
  }

  /**
   * Check whether {@code limit} is — or, applying its deadband, remains — violated at {@code
   * value}: entry requires crossing the limit itself; leaving additionally requires retreating
   * within the limit by the deadband (§5.8.18: hysteresis against chattering).
   */
  boolean limitViolated(ExclusiveLimitState limit, boolean wasViolated, double value) {
    Double limitValue = limitValue(limit);
    if (limitValue == null || !Double.isFinite(limitValue)) {
      // A null limit is unconfigured; a non-finite one (a runtime write of NaN/Infinity, which
      // build-time validation rejects) cannot be meaningfully compared — treat both as "not
      // violated" rather than letting a NaN relational comparison silently read false.
      return false;
    }

    double deadband = wasViolated ? deadbandValue(limit) : 0.0;

    if (limit.isHighSide()) {
      return value >= limitValue - deadband;
    } else {
      return value <= limitValue + deadband;
    }
  }

  /**
   * The most severe configured limit whose plain threshold {@code value} violates, ignoring
   * deadbands; {@code null} if none. The limit ordering makes the two sides mutually exclusive.
   */
  @Nullable ExclusiveLimitState enteredLimit(double value) {
    for (ExclusiveLimitState limit : ExclusiveLimitState.BY_EXCURSION) {
      if (limitViolated(limit, false, value)) {
        return limit;
      }
    }

    return null;
  }

  /**
   * Maintain ActiveState's optional EffectiveDisplayName, where present, to reflect the limit
   * sub-state (§5.2): "Active/HighHigh" style while a limit state is active, the plain state text
   * otherwise. Runs after {@link #applyActive}, whose generic maintenance writes the plain text.
   */
  void setActiveEffectiveDisplayName(@Nullable ExclusiveLimitState subState) {
    if (activeStateEffectiveDisplayName == null) {
      return;
    }

    String text =
        subState != null
            ? ACTIVE_TEXTS.whenTrue() + "/" + subState.stateName()
            : ACTIVE_TEXTS.forState(isActive());

    activeStateEffectiveDisplayName.setValue(
        new DataValue(new Variant(LocalizedText.english(text))));
  }

  /**
   * Apply the activation tail shared by both concrete limit-alarm types, after the subclass has
   * written its own limit-state shape: ActiveState (with the acknowledgement-needed rule),
   * ActiveState's EffectiveDisplayName sub-state text, and the per-limit Severity of the most
   * severe violated limit.
   *
   * <p>Must be called while holding the Condition's lock, inside a {@link StateMutation}.
   *
   * @param mostSevere the most severe violated limit, or {@code null} if the alarm is now inactive.
   * @param limitStateChanged whether one or more limit states changed and therefore start a new
   *     acknowledgement cycle while the alarm remains active.
   * @param time the transition time.
   */
  void applyActiveTransition(
      @Nullable ExclusiveLimitState mostSevere, boolean limitStateChanged, DateTime time) {
    boolean targetActive = mostSevere != null;

    if (isActive() != targetActive) {
      applyActive(targetActive, time);
    } else if (targetActive && limitStateChanged) {
      // A change in the violated limit(s) while already active is a state needing acknowledgement.
      applyAcked(currentBranch(), false, time);
    }

    setActiveEffectiveDisplayName(mostSevere);
    applyEffectiveSeverity(mostSevere, time);
  }

  /** The event Message for a transition into {@code state} ({@code null} = inactive). */
  static String transitionMessage(@Nullable ExclusiveLimitState state) {
    return state != null
        ? ACTIVE_TEXTS.whenTrue() + "/" + state.stateName()
        : ACTIVE_TEXTS.whenFalse();
  }

  /**
   * Shared tail of the limit-alarm {@code create} entry points: instantiate the typed instance
   * node, wrap it in its behavior class, and install method handlers.
   */
  static <T extends LimitAlarm> T build(
      ConditionBuilder builder, NodeId typeDefinitionId, Function<LimitAlarmTypeNode, T> behavior)
      throws UaException {

    LimitAlarmTypeNode node = (LimitAlarmTypeNode) builder.buildNode(typeDefinitionId);

    T alarm = behavior.apply(node);
    alarm.installMethodHandlers(builder.getMethodNodes());

    return alarm;
  }

  private static @Nullable Double doubleValue(@Nullable PropertyTypeNode property) {
    Object object = property != null ? currentValue(property) : null;
    return object instanceof Number number ? number.doubleValue() : null;
  }
}
