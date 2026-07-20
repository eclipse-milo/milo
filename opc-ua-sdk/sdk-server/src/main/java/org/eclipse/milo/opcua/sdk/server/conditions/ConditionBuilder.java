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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.jspecify.annotations.Nullable;

/**
 * Builds Condition instances: instantiates the typed instance node via {@link NodeFactory}
 * (including opted-in optional components), wires HasCondition from the condition source and
 * ensures the source participates in the notifier hierarchy, sets the initial state, and installs
 * method handlers on the instance's method nodes iff their backing state exists.
 *
 * <p>Consumed through each behavior class's {@code create(context, builder -> ...)} entry point,
 * e.g. {@link AcknowledgeableCondition#create}.
 */
public class ConditionBuilder {

  private final Map<QualifiedName, UaMethodNode> methodNodes = new HashMap<>();

  private @Nullable NodeId nodeId;
  private @Nullable QualifiedName browseName;
  private @Nullable LocalizedText displayName;
  private @Nullable UaNode conditionSource;
  private @Nullable String conditionName;
  private NodeId conditionClassId = NodeIds.BaseConditionClassType;
  private LocalizedText conditionClassName = LocalizedText.english("BaseConditionClass");
  private UShort severity = ushort(1);
  private boolean withConfirm = false;
  private @Nullable NodeId inputNode;
  private boolean withShelving = false;
  private @Nullable Duration maxTimeShelved;

  /** The configured value, per-limit severity, and deadband of one limit. */
  private static final class LimitConfig {
    @Nullable Double limit;
    @Nullable UShort severity;
    @Nullable Double deadband;
  }

  private final Map<ExclusiveLimitState, LimitConfig> limits =
      new EnumMap<>(ExclusiveLimitState.class);

  private @Nullable NodeId setpointNode;
  private @Nullable EUInformation engineeringUnits;

  private final UaNodeContext context;

  ConditionBuilder(UaNodeContext context) {
    this.context = context;
  }

  /**
   * Set the {@link NodeId} of the Condition instance (the ConditionId).
   *
   * @param nodeId the {@link NodeId} of the Condition instance.
   * @return this builder.
   */
  public ConditionBuilder nodeId(NodeId nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Set the BrowseName of the Condition instance.
   *
   * @param browseName the BrowseName of the Condition instance.
   * @return this builder.
   */
  public ConditionBuilder browseName(QualifiedName browseName) {
    this.browseName = browseName;
    return this;
  }

  /**
   * Set the DisplayName of the Condition instance.
   *
   * <p>Defaults to the name component of the BrowseName.
   *
   * @param displayName the DisplayName of the Condition instance.
   * @return this builder.
   */
  public ConditionBuilder displayName(LocalizedText displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Set the condition source, i.e. the Node the Condition is associated with.
   *
   * <p>A HasCondition Reference is wired from the source to the Condition instance, the source
   * becomes the SourceNode/SourceName of generated events, and, unless the source already
   * participates in the notifier hierarchy, a HasEventSource Reference is wired from the Server
   * Object to the source.
   *
   * @param conditionSource the condition source Node.
   * @return this builder.
   */
  public ConditionBuilder conditionSource(UaNode conditionSource) {
    this.conditionSource = conditionSource;
    return this;
  }

  /**
   * Set the ConditionName Property value.
   *
   * <p>Defaults to the name component of the BrowseName.
   *
   * @param conditionName the ConditionName Property value.
   * @return this builder.
   */
  public ConditionBuilder conditionName(String conditionName) {
    this.conditionName = conditionName;
    return this;
  }

  /**
   * Set the ConditionClassId and ConditionClassName Property values.
   *
   * <p>Defaults to BaseConditionClassType.
   *
   * @param conditionClassId the {@link NodeId} of a ConditionClassType.
   * @param conditionClassName the name of the condition class.
   * @return this builder.
   */
  public ConditionBuilder conditionClass(
      NodeId conditionClassId, LocalizedText conditionClassName) {
    this.conditionClassId = conditionClassId;
    this.conditionClassName = conditionClassName;
    return this;
  }

  /**
   * Set the initial Severity of events generated by the Condition.
   *
   * <p>Defaults to 1; {@link Condition#setSeverity} changes it later.
   *
   * @param severity the initial Severity, in the range 1-1000.
   * @return this builder.
   */
  public ConditionBuilder severity(UShort severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Opt in to the optional ConfirmedState component and its Confirm method.
   *
   * @return this builder.
   */
  public ConditionBuilder withConfirm() {
    this.withConfirm = true;
    return this;
  }

  /**
   * Set the InputNode Property value: the {@link NodeId} of the Variable the alarm monitors.
   *
   * <p>Only meaningful for AlarmConditionType and its subtypes. The SDK does not subscribe to the
   * input; the application drives {@link AlarmCondition#setActive}.
   *
   * @param inputNode the {@link NodeId} of the alarm's input Variable.
   * @return this builder.
   */
  public ConditionBuilder inputNode(NodeId inputNode) {
    this.inputNode = inputNode;
    return this;
  }

  /**
   * Opt in to the optional ShelvingState component and its shelving methods, with no MaxTimeShelved
   * limit.
   *
   * <p>Only meaningful for AlarmConditionType and its subtypes.
   *
   * @return this builder.
   */
  public ConditionBuilder withShelving() {
    this.withShelving = true;
    return this;
  }

  /**
   * Opt in to the optional ShelvingState component and its shelving methods, limited by the
   * optional MaxTimeShelved Property: TimedShelve may not exceed it and OneShotShelve automatically
   * releases after it.
   *
   * <p>Only meaningful for AlarmConditionType and its subtypes.
   *
   * @param maxTimeShelved the MaxTimeShelved Property value.
   * @return this builder.
   */
  public ConditionBuilder withShelving(Duration maxTimeShelved) {
    this.withShelving = true;
    this.maxTimeShelved = maxTimeShelved;
    return this;
  }

  /**
   * Configure the HighHighLimit Property value.
   *
   * <p>Only meaningful for LimitAlarmType and its subtypes. Configured limits must obey HighHigh
   * &gt; High &gt; Low &gt; LowLow, and the limit-alarm {@code create} entry points require at
   * least one limit.
   *
   * @param limit the HighHighLimit Property value.
   * @return this builder.
   */
  public ConditionBuilder highHighLimit(double limit) {
    limitConfig(ExclusiveLimitState.HIGH_HIGH).limit = limit;
    return this;
  }

  /**
   * Configure the HighHighLimit Property value and the SeverityHighHigh Property, the Severity of
   * events reported while that limit is the most severe one violated.
   *
   * @param limit the HighHighLimit Property value.
   * @param severity the SeverityHighHigh Property value, in the range 1-1000.
   * @return this builder.
   * @see #highHighLimit(double)
   */
  public ConditionBuilder highHighLimit(double limit, UShort severity) {
    LimitConfig config = limitConfig(ExclusiveLimitState.HIGH_HIGH);
    config.limit = limit;
    config.severity = severity;
    return this;
  }

  /**
   * Configure the HighLimit Property value.
   *
   * @param limit the HighLimit Property value.
   * @return this builder.
   * @see #highHighLimit(double)
   */
  public ConditionBuilder highLimit(double limit) {
    limitConfig(ExclusiveLimitState.HIGH).limit = limit;
    return this;
  }

  /**
   * Configure the HighLimit Property value and the SeverityHigh Property.
   *
   * @param limit the HighLimit Property value.
   * @param severity the SeverityHigh Property value, in the range 1-1000.
   * @return this builder.
   * @see #highHighLimit(double, UShort)
   */
  public ConditionBuilder highLimit(double limit, UShort severity) {
    LimitConfig config = limitConfig(ExclusiveLimitState.HIGH);
    config.limit = limit;
    config.severity = severity;
    return this;
  }

  /**
   * Configure the LowLimit Property value.
   *
   * @param limit the LowLimit Property value.
   * @return this builder.
   * @see #highHighLimit(double)
   */
  public ConditionBuilder lowLimit(double limit) {
    limitConfig(ExclusiveLimitState.LOW).limit = limit;
    return this;
  }

  /**
   * Configure the LowLimit Property value and the SeverityLow Property.
   *
   * @param limit the LowLimit Property value.
   * @param severity the SeverityLow Property value, in the range 1-1000.
   * @return this builder.
   * @see #highHighLimit(double, UShort)
   */
  public ConditionBuilder lowLimit(double limit, UShort severity) {
    LimitConfig config = limitConfig(ExclusiveLimitState.LOW);
    config.limit = limit;
    config.severity = severity;
    return this;
  }

  /**
   * Configure the LowLowLimit Property value.
   *
   * @param limit the LowLowLimit Property value.
   * @return this builder.
   * @see #highHighLimit(double)
   */
  public ConditionBuilder lowLowLimit(double limit) {
    limitConfig(ExclusiveLimitState.LOW_LOW).limit = limit;
    return this;
  }

  /**
   * Configure the LowLowLimit Property value and the SeverityLowLow Property.
   *
   * @param limit the LowLowLimit Property value.
   * @param severity the SeverityLowLow Property value, in the range 1-1000.
   * @return this builder.
   * @see #highHighLimit(double, UShort)
   */
  public ConditionBuilder lowLowLimit(double limit, UShort severity) {
    LimitConfig config = limitConfig(ExclusiveLimitState.LOW_LOW);
    config.limit = limit;
    config.severity = severity;
    return this;
  }

  /**
   * Configure the HighHighDeadband Property: leaving the HighHigh state requires the value to
   * retreat within the limit by the deadband (hysteresis against chattering, §5.8.18).
   *
   * <p>Requires the corresponding limit to be configured.
   *
   * @param deadband the HighHighDeadband Property value.
   * @return this builder.
   */
  public ConditionBuilder highHighDeadband(double deadband) {
    limitConfig(ExclusiveLimitState.HIGH_HIGH).deadband = deadband;
    return this;
  }

  /**
   * Configure the HighDeadband Property.
   *
   * @param deadband the HighDeadband Property value.
   * @return this builder.
   * @see #highHighDeadband(double)
   */
  public ConditionBuilder highDeadband(double deadband) {
    limitConfig(ExclusiveLimitState.HIGH).deadband = deadband;
    return this;
  }

  /**
   * Configure the LowDeadband Property.
   *
   * @param deadband the LowDeadband Property value.
   * @return this builder.
   * @see #highHighDeadband(double)
   */
  public ConditionBuilder lowDeadband(double deadband) {
    limitConfig(ExclusiveLimitState.LOW).deadband = deadband;
    return this;
  }

  /**
   * Configure the LowLowDeadband Property.
   *
   * @param deadband the LowLowDeadband Property value.
   * @return this builder.
   * @see #highHighDeadband(double)
   */
  public ConditionBuilder lowLowDeadband(double deadband) {
    limitConfig(ExclusiveLimitState.LOW_LOW).deadband = deadband;
    return this;
  }

  /**
   * Set the SetpointNode Property value: the {@link NodeId} of the Variable holding the setpoint a
   * deviation alarm's deviation is measured from.
   *
   * <p>Required by the deviation alarm types; the SDK does not read or subscribe to the setpoint —
   * the application computes the deviation it passes to {@code evaluate}.
   *
   * @param setpointNode the {@link NodeId} of the setpoint Variable.
   * @return this builder.
   */
  public ConditionBuilder setpointNode(NodeId setpointNode) {
    this.setpointNode = setpointNode;
    return this;
  }

  /**
   * Set the optional EngineeringUnits Property value of a rate-of-change alarm: the unit of the
   * rate passed to {@code evaluate} (absent: the input's unit per second).
   *
   * @param engineeringUnits the EngineeringUnits Property value.
   * @return this builder.
   */
  public ConditionBuilder engineeringUnits(EUInformation engineeringUnits) {
    this.engineeringUnits = engineeringUnits;
    return this;
  }

  private LimitConfig limitConfig(ExclusiveLimitState limit) {
    return limits.computeIfAbsent(limit, k -> new LimitConfig());
  }

  /**
   * Validate the limit configuration for a limit-alarm {@code create} entry point: at least one
   * limit (for non-exclusive types at least High or Low), finite values obeying HighHigh &gt; High
   * &gt; Low &gt; LowLow, and no per-limit severity or deadband without its limit.
   *
   * @throws IllegalArgumentException if the configuration is invalid.
   */
  void validateLimits(boolean requireHighOrLow) {
    if (limits.values().stream().allMatch(config -> config.limit == null)) {
      throw new IllegalArgumentException("at least one limit must be configured");
    }

    for (Map.Entry<ExclusiveLimitState, LimitConfig> entry : limits.entrySet()) {
      String name = entry.getKey().stateName();
      LimitConfig config = entry.getValue();

      if (config.limit == null) {
        throw new IllegalArgumentException(
            "Severity" + name + "/" + name + "Deadband configured without " + name + "Limit");
      }
      if (!Double.isFinite(config.limit)) {
        throw new IllegalArgumentException(name + "Limit must be finite: " + config.limit);
      }
      if (config.deadband != null && (!Double.isFinite(config.deadband) || config.deadband < 0.0)) {
        throw new IllegalArgumentException(
            name + "Deadband must be finite and non-negative: " + config.deadband);
      }
      if (config.severity != null
          && (config.severity.intValue() < 1 || config.severity.intValue() > 1000)) {
        throw new IllegalArgumentException(
            "Severity" + name + " must be in the range 1..1000: " + config.severity);
      }
    }

    if (requireHighOrLow
        && configuredLimit(ExclusiveLimitState.HIGH) == null
        && configuredLimit(ExclusiveLimitState.LOW) == null) {
      throw new IllegalArgumentException(
          "a non-exclusive limit alarm requires at least a High or Low limit");
    }

    Double previous = null;
    for (ExclusiveLimitState limit :
        new ExclusiveLimitState[] {
          ExclusiveLimitState.HIGH_HIGH,
          ExclusiveLimitState.HIGH,
          ExclusiveLimitState.LOW,
          ExclusiveLimitState.LOW_LOW
        }) {

      Double value = configuredLimit(limit);
      if (value != null) {
        if (previous != null && previous <= value) {
          throw new IllegalArgumentException("limits must satisfy HighHigh > High > Low > LowLow");
        }
        previous = value;
      }
    }

    validateAdjacentDeadbands();
  }

  /**
   * Validate each configured pair of neighboring limits after absent optional limits have been
   * skipped. A limit's deadband boundary must remain strictly on its own side of the neighboring
   * limit, otherwise the adjacent limit regions overlap (§5.8.18).
   */
  private void validateAdjacentDeadbands() {
    ExclusiveLimitState[] orderedLimits = {
      ExclusiveLimitState.HIGH_HIGH,
      ExclusiveLimitState.HIGH,
      ExclusiveLimitState.LOW,
      ExclusiveLimitState.LOW_LOW
    };

    for (int upperIndex = 0; upperIndex < orderedLimits.length; upperIndex++) {
      ExclusiveLimitState upper = orderedLimits[upperIndex];
      Double upperValue = configuredLimit(upper);
      if (upperValue == null) {
        continue;
      }

      for (int lowerIndex = upperIndex + 1; lowerIndex < orderedLimits.length; lowerIndex++) {
        ExclusiveLimitState lower = orderedLimits[lowerIndex];
        Double lowerValue = configuredLimit(lower);
        if (lowerValue == null) {
          continue;
        }

        Double upperDeadband = configuredDeadband(upper);
        if (upperDeadband != null && upperValue - upperDeadband <= lowerValue) {
          throw new IllegalArgumentException(
              upper.stateName()
                  + "Limit - "
                  + upper.stateName()
                  + "Deadband must be > "
                  + lower.stateName()
                  + "Limit");
        }

        Double lowerDeadband = configuredDeadband(lower);
        if (lowerDeadband != null && lowerValue + lowerDeadband >= upperValue) {
          throw new IllegalArgumentException(
              lower.stateName()
                  + "Limit + "
                  + lower.stateName()
                  + "Deadband must be < "
                  + upper.stateName()
                  + "Limit");
        }

        break;
      }
    }
  }

  private @Nullable Double configuredLimit(ExclusiveLimitState limit) {
    LimitConfig config = limits.get(limit);
    return config != null ? config.limit : null;
  }

  private @Nullable Double configuredDeadband(ExclusiveLimitState limit) {
    LimitConfig config = limits.get(limit);
    return config != null ? config.deadband : null;
  }

  /**
   * Validate the signed deviation limits required by Part 9 §5.8.22: high-side deviations are
   * positive and non-zero, while low-side deviations are negative and non-zero.
   *
   * @param requireHighOrLow whether the non-exclusive High-or-Low requirement applies.
   * @throws IllegalArgumentException if the generic or deviation-specific configuration is invalid.
   */
  void validateDeviationLimits(boolean requireHighOrLow) {
    validateLimits(requireHighOrLow);

    for (ExclusiveLimitState limit : ExclusiveLimitState.values()) {
      Double value = configuredLimit(limit);
      if (value == null) {
        continue;
      }

      if (limit.isHighSide() && value <= 0.0) {
        throw new IllegalArgumentException(
            limit.stateName() + " deviation must be positive and non-zero: " + value);
      }
      if (!limit.isHighSide() && value >= 0.0) {
        throw new IllegalArgumentException(
            limit.stateName() + " deviation must be negative and non-zero: " + value);
      }
    }
  }

  /**
   * Require {@link #setpointNode} to be configured, for the deviation-alarm {@code create} entry
   * points (SetpointNode is Mandatory on the deviation types).
   *
   * @throws IllegalArgumentException if no setpointNode is configured.
   */
  void requireSetpointNode() {
    if (setpointNode == null) {
      throw new IllegalArgumentException("setpointNode must be set for a deviation alarm");
    }
  }

  /**
   * Get the instance method nodes recorded during {@link #buildNode}, keyed by BrowseName, for
   * {@link Condition#installMethodHandlers}.
   */
  Map<QualifiedName, UaMethodNode> getMethodNodes() {
    return Map.copyOf(methodNodes);
  }

  /**
   * Instantiate the Condition instance node of {@code typeDefinitionId}, set its initial state per
   * §4.12, and wire the condition source.
   *
   * <p>Method nodes created during instantiation are recorded for {@link #getMethodNodes()}.
   */
  ConditionTypeNode buildNode(NodeId typeDefinitionId) throws UaException {
    NodeId nodeId = requireNonNull(this.nodeId, "nodeId must be set");
    QualifiedName browseName = requireNonNull(this.browseName, "browseName must be set");

    Set<String> optionalIncludes = new HashSet<>();
    optionalIncludes.add("TransitionTime");
    optionalIncludes.add("SupportsFilteredRetain");
    if (withConfirm) {
      optionalIncludes.add("ConfirmedState");
    }
    if (withShelving) {
      optionalIncludes.add("ShelvingState");
      // The ShelvedStateMachine's LastTransition (FiniteStateMachineType) is Optional.
      optionalIncludes.add("LastTransition");
      if (maxTimeShelved != null) {
        optionalIncludes.add("MaxTimeShelved");
      }
    }

    for (Map.Entry<ExclusiveLimitState, LimitConfig> entry : limits.entrySet()) {
      String name = entry.getKey().stateName();
      LimitConfig config = entry.getValue();

      if (config.limit != null) {
        optionalIncludes.add(name + "Limit");
        // The per-limit TwoStateVariables of the non-exclusive types ("HighHighState", ...);
        // no-ops on the exclusive types, whose LimitState machine is Mandatory.
        optionalIncludes.add(name + "State");
      }
      if (config.severity != null) {
        optionalIncludes.add("Severity" + name);
      }
      if (config.deadband != null) {
        optionalIncludes.add(name + "Deadband");
      }
    }

    if (!limits.isEmpty()) {
      // ActiveState's EffectiveDisplayName reflects the limit sub-state (§5.2), and the exclusive
      // LimitState machine's LastTransition records the modelled intra-side transitions.
      optionalIncludes.add("EffectiveDisplayName");
      optionalIncludes.add("LastTransition");
    }

    if (engineeringUnits != null) {
      optionalIncludes.add("EngineeringUnits");
    }

    var nodeFactory = new NodeFactory(context);

    UaNode instance =
        nodeFactory.createNode(
            nodeId,
            typeDefinitionId,
            new NodeFactory.InstantiationCallback() {
              @Override
              public boolean includeOptionalNode(
                  NodeId typeDefinitionId, QualifiedName browseName) {
                return optionalIncludes.contains(browseName.name());
              }

              @Override
              public void onMethodAdded(@Nullable UaObjectNode parent, UaMethodNode methodNode) {
                methodNodes.put(methodNode.getBrowseName(), methodNode);
              }
            });

    if (!(instance instanceof ConditionTypeNode node)) {
      // NodeFactory already registered the instance tree; remove it so the failure doesn't leak
      // orphaned nodes into the address space.
      instance.delete();

      throw new UaException(
          StatusCodes.Bad_InternalError,
          "expected a ConditionTypeNode instance for " + typeDefinitionId + ", got " + instance);
    }

    node.setBrowseName(browseName);
    node.setDisplayName(
        displayName != null ? displayName : LocalizedText.english(browseName.name()));

    initializeNode(node, typeDefinitionId);
    wireConditionSource(node);

    return node;
  }

  private void initializeNode(ConditionTypeNode node, NodeId typeDefinitionId) {
    DateTime now = DateTime.now();
    QualifiedName browseName = requireNonNull(this.browseName);

    node.setEventType(typeDefinitionId);
    node.setEventId(ByteString.NULL_VALUE);
    node.setTime(now);
    node.setReceiveTime(DateTime.NULL_VALUE);
    node.setMessage(LocalizedText.NULL_VALUE);
    node.setSeverity(severity);

    node.setConditionName(conditionName != null ? conditionName : browseName.name());
    node.setConditionClassId(conditionClassId);
    node.setConditionClassName(conditionClassName);
    node.setBranchId(NodeId.NULL_VALUE);
    node.setRetain(false);
    node.setSupportsFilteredRetain(false);

    if (node instanceof AcknowledgeableConditionTypeNode ackNode) {
      wireSubStates(ackNode);
    }

    if (node instanceof AlarmConditionTypeNode alarmNode) {
      initializeAlarmNode(alarmNode);
    }

    if (node instanceof LimitAlarmTypeNode limitNode) {
      initializeLimitNode(limitNode);
    }
  }

  private void initializeAlarmNode(AlarmConditionTypeNode node) {
    node.setInputNode(inputNode != null ? inputNode : NodeId.NULL_VALUE);
    node.setSuppressedOrShelved(false);

    if (maxTimeShelved != null) {
      node.setMaxTimeShelved((double) maxTimeShelved.toMillis());
    }
  }

  private void initializeLimitNode(LimitAlarmTypeNode node) {
    LimitConfig highHigh = limits.get(ExclusiveLimitState.HIGH_HIGH);
    if (highHigh != null) {
      if (highHigh.limit != null) {
        node.setHighHighLimit(highHigh.limit);
      }
      if (highHigh.severity != null) {
        node.setSeverityHighHigh(highHigh.severity);
      }
      if (highHigh.deadband != null) {
        node.setHighHighDeadband(highHigh.deadband);
      }
    }

    LimitConfig high = limits.get(ExclusiveLimitState.HIGH);
    if (high != null) {
      if (high.limit != null) {
        node.setHighLimit(high.limit);
      }
      if (high.severity != null) {
        node.setSeverityHigh(high.severity);
      }
      if (high.deadband != null) {
        node.setHighDeadband(high.deadband);
      }
    }

    LimitConfig low = limits.get(ExclusiveLimitState.LOW);
    if (low != null) {
      if (low.limit != null) {
        node.setLowLimit(low.limit);
      }
      if (low.severity != null) {
        node.setSeverityLow(low.severity);
      }
      if (low.deadband != null) {
        node.setLowDeadband(low.deadband);
      }
    }

    LimitConfig lowLow = limits.get(ExclusiveLimitState.LOW_LOW);
    if (lowLow != null) {
      if (lowLow.limit != null) {
        node.setLowLowLimit(lowLow.limit);
      }
      if (lowLow.severity != null) {
        node.setSeverityLowLow(lowLow.severity);
      }
      if (lowLow.deadband != null) {
        node.setLowLowDeadband(lowLow.deadband);
      }
    }

    if (setpointNode != null) {
      if (node instanceof ExclusiveDeviationAlarmTypeNode deviationNode) {
        deviationNode.setSetpointNode(setpointNode);
      }
      if (node instanceof NonExclusiveDeviationAlarmTypeNode deviationNode) {
        deviationNode.setSetpointNode(setpointNode);
      }
    }

    if (engineeringUnits != null) {
      if (node instanceof ExclusiveRateOfChangeAlarmTypeNode rateOfChangeNode) {
        rateOfChangeNode.setEngineeringUnits(engineeringUnits);
      }
      if (node instanceof NonExclusiveRateOfChangeAlarmTypeNode rateOfChangeNode) {
        rateOfChangeNode.setEngineeringUnits(engineeringUnits);
      }
    }

    // "EffectiveDisplayName" is opted in (buildNode) so ActiveState carries the limit sub-state
    // text (§5.2), but the browse-name-only optional filter also matches AlarmConditionType's
    // optional EnabledState EffectiveDisplayName. That is not wanted — prune it so a limit alarm's
    // address-space shape matches a plain alarm, which never includes it.
    TwoStateVariableTypeNode enabledState = node.getEnabledStateNode();
    if (enabledState != null) {
      PropertyTypeNode effectiveDisplayName = enabledState.getEffectiveDisplayNameNode();
      if (effectiveDisplayName != null) {
        effectiveDisplayName.delete();
      }
    }
  }

  /**
   * Wire the present sub-states of EnabledState per §5.4 in a single pass: AckedState and
   * ConfirmedState for every AcknowledgeableCondition, plus ActiveState and the ShelvingState
   * sub-state machine (§5.8.10) for an AlarmCondition, plus the limit sub-states of ActiveState —
   * the exclusive LimitState machine (§5.8.19.3) or the independent non-exclusive limit states
   * (§5.8.20). The required inverse Reference from sub-state to super-state is added automatically
   * by {@code UaNode.addReference}.
   */
  private void wireSubStates(AcknowledgeableConditionTypeNode node) {
    TwoStateVariableTypeNode enabledState = node.getEnabledStateNode();
    if (enabledState == null) {
      return;
    }

    wireSubState(enabledState, node.getAckedStateNode());
    wireSubState(enabledState, node.getConfirmedStateNode());

    if (node instanceof AlarmConditionTypeNode alarmNode) {
      TwoStateVariableTypeNode activeState = alarmNode.getActiveStateNode();

      wireSubState(enabledState, activeState);
      wireSubState(enabledState, alarmNode.getShelvingStateNode());

      if (activeState != null) {
        if (alarmNode instanceof ExclusiveLimitAlarmTypeNode exclusiveNode) {
          wireSubState(activeState, exclusiveNode.getLimitStateNode());
        }

        if (alarmNode instanceof NonExclusiveLimitAlarmTypeNode nonExclusiveNode) {
          wireSubState(activeState, nonExclusiveNode.getHighHighStateNode());
          wireSubState(activeState, nonExclusiveNode.getHighStateNode());
          wireSubState(activeState, nonExclusiveNode.getLowStateNode());
          wireSubState(activeState, nonExclusiveNode.getLowLowStateNode());
        }
      }
    }
  }

  private void wireSubState(UaNode superState, @Nullable UaNode subState) {
    if (subState == null) {
      return;
    }

    superState.addReference(
        new Reference(
            superState.getNodeId(),
            NodeIds.HasTrueSubState,
            subState.getNodeId().expanded(),
            true));
  }

  private void wireConditionSource(ConditionTypeNode node) {
    UaNode source = conditionSource;
    if (source == null) {
      return;
    }

    node.setSourceNode(source.getNodeId());

    LocalizedText sourceDisplayName = source.getDisplayName();
    String sourceName = sourceDisplayName != null ? sourceDisplayName.text() : null;
    node.setSourceName(sourceName != null ? sourceName : source.getBrowseName().name());

    source.addReference(
        new Reference(source.getNodeId(), NodeIds.HasCondition, node.getNodeId().expanded(), true));

    ensureEventSourceWiring(source);
  }

  /**
   * Ensure the condition source is the target of a HasEventSource (or subtype) Reference so events
   * fired for it resolve into the notifier hierarchy; if it is not, wire it from the Server Object.
   */
  private void ensureEventSourceWiring(UaNode source) {
    ReferenceTypeTree referenceTypeTree = context.getServer().getReferenceTypeTree();

    boolean hasEventSourceParent =
        context
            .getServer()
            .getAddressSpaceManager()
            .getManagedReferences(source.getNodeId())
            .stream()
            .anyMatch(
                r ->
                    r.isInverse()
                        // isSubtypeOf is strict — it does not match the type itself.
                        && (NodeIds.HasEventSource.equals(r.getReferenceTypeId())
                            || referenceTypeTree.isSubtypeOf(
                                r.getReferenceTypeId(), NodeIds.HasEventSource)));

    if (hasEventSourceParent) {
      return;
    }

    UaNode serverNode =
        context.getServer().getAddressSpaceManager().getManagedNode(NodeIds.Server).orElse(null);

    if (serverNode != null) {
      serverNode
          .getNodeManager()
          .addReferences(
              new Reference(
                  NodeIds.Server, NodeIds.HasEventSource, source.getNodeId().expanded(), true),
              context.getNamespaceTable());
    }
  }
}
