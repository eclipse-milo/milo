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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.OffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.ConflictPolicy;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationResult;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.MethodInstantiation;
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
 * Builds Condition instances: instantiates the typed instance node via the server's node
 * instantiator (including opted-in optional components), wires HasCondition from the condition
 * source and ensures the source participates in the notifier hierarchy, sets the initial state, and
 * installs method handlers on the instance's method nodes iff their backing state exists.
 *
 * <p>Consumed through each behavior class's {@code create(context, builder -> ...)} entry point,
 * e.g. {@link AcknowledgeableCondition#create}.
 */
public class ConditionBuilder {

  private static final BrowsePath ACTIVE_STATE_EFFECTIVE_DISPLAY_NAME =
      BrowsePath.of(
          new QualifiedName(0, "ActiveState"), new QualifiedName(0, "EffectiveDisplayName"));

  private static final BrowsePath SHELVING_STATE_LAST_TRANSITION =
      BrowsePath.of(new QualifiedName(0, "ShelvingState"), new QualifiedName(0, "LastTransition"));

  private static final BrowsePath LIMIT_STATE_LAST_TRANSITION =
      BrowsePath.of(new QualifiedName(0, "LimitState"), new QualifiedName(0, "LastTransition"));

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
  private @Nullable NodeId normalState;
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
  private final @Nullable ConditionTypeNode adoptedNode;
  private final @Nullable NodeId adoptedTypeDefinitionId;

  private boolean displayNameConfigured;
  private boolean conditionNameConfigured;
  private boolean conditionClassConfigured;
  private boolean severityConfigured;
  private boolean inputNodeConfigured;
  private boolean normalStateConfigured;
  private boolean maxTimeShelvedConfigured;

  ConditionBuilder(UaNodeContext context) {
    this.context = context;
    adoptedNode = null;
    adoptedTypeDefinitionId = null;
  }

  private ConditionBuilder(
      UaNodeContext context, ConditionTypeNode adoptedNode, NodeId adoptedTypeDefinitionId) {
    this.context = context;
    this.adoptedNode = adoptedNode;
    this.adoptedTypeDefinitionId = adoptedTypeDefinitionId;
    nodeId = adoptedNode.getNodeId();
    browseName = adoptedNode.getBrowseName();
  }

  static ConditionBuilder forAdoption(
      UaNodeContext context, NodeId nodeId, Class<? extends ConditionTypeNode> expectedNodeClass)
      throws UaException {

    UaNode node =
        context
            .getNodeManager()
            .getNode(nodeId)
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown,
                        "condition node does not exist in the supplied NodeManager: " + nodeId));

    if (!expectedNodeClass.isInstance(node)) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch,
          "condition node "
              + nodeId
              + " is "
              + node.getClass().getName()
              + ", expected "
              + expectedNodeClass.getName()
              + " or a generated subtype");
    }

    Set<NodeId> typeDefinitions = new HashSet<>();
    for (var reference : node.getReferences()) {
      if (reference.isForward()
          && NodeIds.HasTypeDefinition.equals(reference.getReferenceTypeId())) {
        reference
            .getTargetNodeId()
            .toNodeId(context.getNamespaceTable())
            .ifPresent(typeDefinitions::add);
      }
    }

    if (typeDefinitions.size() != 1) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch,
          "condition node "
              + nodeId
              + " must have exactly one resolvable HasTypeDefinition reference");
    }

    ConditionTypeNode conditionNode = expectedNodeClass.cast(node);
    ConditionNodeTraversal.discoverMethodSurface(conditionNode);
    ConditionNodeTraversal.discoverAssignedNodeIds(conditionNode);

    return new ConditionBuilder(context, conditionNode, typeDefinitions.iterator().next());
  }

  /**
   * Set the {@link NodeId} of the Condition instance (the ConditionId).
   *
   * @param nodeId the {@link NodeId} of the Condition instance.
   * @return this builder.
   */
  public ConditionBuilder nodeId(NodeId nodeId) {
    if (adoptedNode != null && !adoptedNode.getNodeId().equals(nodeId)) {
      throw new IllegalArgumentException(
          "adopt cannot replace NodeId " + adoptedNode.getNodeId() + " with " + nodeId);
    }
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
    if (adoptedNode != null && !adoptedNode.getBrowseName().equals(browseName)) {
      throw new IllegalArgumentException(
          "adopt cannot replace BrowseName " + adoptedNode.getBrowseName() + " with " + browseName);
    }
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
    displayNameConfigured = true;
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
    conditionNameConfigured = true;
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
    conditionClassConfigured = true;
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
    severityConfigured = true;
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
    inputNodeConfigured = true;
    return this;
  }

  /**
   * Set the NormalState Property value: the {@link NodeId} of the Variable whose value is the
   * normal state of the Variable identified by InputNode.
   *
   * <p>Only meaningful for OffNormalAlarmType and its subtypes. The SDK does not read or subscribe
   * to either Variable; the application compares them and drives {@link AlarmCondition#setActive}
   * or {@link OffNormalAlarm#evaluate}.
   *
   * @param normalState the {@link NodeId} of the Variable holding the normal state value.
   * @return this builder.
   */
  public ConditionBuilder normalState(NodeId normalState) {
    this.normalState = normalState;
    normalStateConfigured = true;
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
    maxTimeShelvedConfigured = true;
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
   * Configure the SeverityHighHigh Property without replacing the HighHighLimit value.
   *
   * <p>During adoption, an existing HighHighLimit satisfies the corresponding-limit requirement.
   * During creation, {@link #highHighLimit(double)} must also be configured.
   *
   * @param severity the SeverityHighHigh Property value, in the range 1-1000.
   * @return this builder.
   */
  public ConditionBuilder highHighSeverity(UShort severity) {
    limitConfig(ExclusiveLimitState.HIGH_HIGH).severity = severity;
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
   * Configure the SeverityHigh Property without replacing the HighLimit value.
   *
   * <p>During adoption, an existing HighLimit satisfies the corresponding-limit requirement. During
   * creation, {@link #highLimit(double)} must also be configured.
   *
   * @param severity the SeverityHigh Property value, in the range 1-1000.
   * @return this builder.
   */
  public ConditionBuilder highSeverity(UShort severity) {
    limitConfig(ExclusiveLimitState.HIGH).severity = severity;
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
   * Configure the SeverityLow Property without replacing the LowLimit value.
   *
   * <p>During adoption, an existing LowLimit satisfies the corresponding-limit requirement. During
   * creation, {@link #lowLimit(double)} must also be configured.
   *
   * @param severity the SeverityLow Property value, in the range 1-1000.
   * @return this builder.
   */
  public ConditionBuilder lowSeverity(UShort severity) {
    limitConfig(ExclusiveLimitState.LOW).severity = severity;
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
   * Configure the SeverityLowLow Property without replacing the LowLowLimit value.
   *
   * <p>During adoption, an existing LowLowLimit satisfies the corresponding-limit requirement.
   * During creation, {@link #lowLowLimit(double)} must also be configured.
   *
   * @param severity the SeverityLowLow Property value, in the range 1-1000.
   * @return this builder.
   */
  public ConditionBuilder lowLowSeverity(UShort severity) {
    limitConfig(ExclusiveLimitState.LOW_LOW).severity = severity;
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
    boolean anyLimit = false;
    for (ExclusiveLimitState limit : ExclusiveLimitState.values()) {
      anyLimit |= configuredLimit(limit) != null;
    }
    if (!anyLimit) {
      throw new IllegalArgumentException("at least one limit must be configured");
    }

    for (ExclusiveLimitState limit : ExclusiveLimitState.values()) {
      String name = limit.stateName();
      Double value = configuredLimit(limit);
      UShort configuredSeverity = configuredSeverity(limit);
      Double configuredDeadband = configuredDeadband(limit);

      if (value == null && (configuredSeverity != null || configuredDeadband != null)) {
        throw new IllegalArgumentException(
            "Severity" + name + "/" + name + "Deadband configured without " + name + "Limit");
      }
      if (value != null && !Double.isFinite(value)) {
        throw new IllegalArgumentException(name + "Limit must be finite: " + value);
      }
      if (configuredDeadband != null
          && (!Double.isFinite(configuredDeadband) || configuredDeadband < 0.0)) {
        throw new IllegalArgumentException(
            name + "Deadband must be finite and non-negative: " + configuredDeadband);
      }
      if (configuredSeverity != null
          && (configuredSeverity.intValue() < 1 || configuredSeverity.intValue() > 1000)) {
        throw new IllegalArgumentException(
            "Severity" + name + " must be in the range 1..1000: " + configuredSeverity);
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
    if (config != null && config.limit != null) {
      return config.limit;
    }

    if (adoptedNode instanceof LimitAlarmTypeNode limitNode) {
      return switch (limit) {
        case HIGH_HIGH -> limitNode.getHighHighLimit();
        case HIGH -> limitNode.getHighLimit();
        case LOW -> limitNode.getLowLimit();
        case LOW_LOW -> limitNode.getLowLowLimit();
      };
    }

    return null;
  }

  private @Nullable UShort configuredSeverity(ExclusiveLimitState limit) {
    LimitConfig config = limits.get(limit);
    if (config != null && config.severity != null) {
      return config.severity;
    }

    if (adoptedNode instanceof LimitAlarmTypeNode limitNode) {
      return switch (limit) {
        case HIGH_HIGH -> limitNode.getSeverityHighHigh();
        case HIGH -> limitNode.getSeverityHigh();
        case LOW -> limitNode.getSeverityLow();
        case LOW_LOW -> limitNode.getSeverityLowLow();
      };
    }

    return null;
  }

  private @Nullable Double configuredDeadband(ExclusiveLimitState limit) {
    LimitConfig config = limits.get(limit);
    if (config != null && config.deadband != null) {
      return config.deadband;
    }

    if (adoptedNode instanceof LimitAlarmTypeNode limitNode) {
      return switch (limit) {
        case HIGH_HIGH -> limitNode.getHighHighDeadband();
        case HIGH -> limitNode.getHighDeadband();
        case LOW -> limitNode.getLowDeadband();
        case LOW_LOW -> limitNode.getLowLowDeadband();
      };
    }

    return null;
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
    NodeId configuredSetpoint = setpointNode;
    if (configuredSetpoint == null
        && adoptedNode instanceof ExclusiveDeviationAlarmTypeNode deviationNode) {
      configuredSetpoint = deviationNode.getSetpointNode();
    }
    if (configuredSetpoint == null
        && adoptedNode instanceof NonExclusiveDeviationAlarmTypeNode deviationNode) {
      configuredSetpoint = deviationNode.getSetpointNode();
    }

    if (configuredSetpoint == null) {
      throw new IllegalArgumentException("setpointNode must be set for a deviation alarm");
    }
  }

  /** Instantiate or complete the Condition node, initialize it, and wire its condition source. */
  ConditionTypeNode buildNode(NodeId typeDefinitionId) throws UaException {
    NodeId nodeId = requireNonNull(this.nodeId, "nodeId must be set");
    QualifiedName browseName = requireNonNull(this.browseName, "browseName must be set");
    boolean adopting = adoptedNode != null;
    NodeId effectiveTypeDefinitionId =
        adopting ? requireNonNull(adoptedTypeDefinitionId) : typeDefinitionId;
    Map<BrowsePath, NodeId> assignedNodeIds =
        adopting
            ? ConditionNodeTraversal.discoverAssignedNodeIds(requireNonNull(adoptedNode))
            : Map.of();
    ConditionNodeTraversal.MethodSurface adoptedMethodSurface =
        adopting ? ConditionNodeTraversal.discoverMethodSurface(requireNonNull(adoptedNode)) : null;

    Set<String> optionalIncludes = new HashSet<>();
    optionalIncludes.add("TransitionTime");
    optionalIncludes.add("SupportsFilteredRetain");

    if (adopting
        && (assignedNodeIds.containsKey(BrowsePath.of(new QualifiedName(0, "ConfirmedState")))
            || assignedNodeIds.containsKey(BrowsePath.of(new QualifiedName(0, "Confirm"))))) {
      withConfirm = true;
    }
    if (withConfirm) {
      optionalIncludes.add("ConfirmedState");
      optionalIncludes.add("Confirm");
    }

    if (adopting
        && (assignedNodeIds.containsKey(BrowsePath.of(new QualifiedName(0, "ShelvingState")))
            || assignedNodeIds.containsKey(
                BrowsePath.of(new QualifiedName(0, "MaxTimeShelved"))))) {
      withShelving = true;
    }
    if (withShelving) {
      optionalIncludes.add("ShelvingState");
      if (maxTimeShelved != null
          || (adopting
              && assignedNodeIds.containsKey(
                  BrowsePath.of(new QualifiedName(0, "MaxTimeShelved"))))) {
        optionalIncludes.add("MaxTimeShelved");
      }
    }

    for (ExclusiveLimitState limit : ExclusiveLimitState.values()) {
      String name = limit.stateName();
      if (configuredLimit(limit) != null) {
        optionalIncludes.add(name + "Limit");
        optionalIncludes.add(name + "State");
      }
      if (configuredSeverity(limit) != null) {
        optionalIncludes.add("Severity" + name);
      }
      if (configuredDeadband(limit) != null) {
        optionalIncludes.add(name + "Deadband");
      }
    }

    if (engineeringUnits != null || storedEngineeringUnits() != null) {
      optionalIncludes.add("EngineeringUnits");
    }

    boolean includeLimitMembers =
        adoptedNode instanceof LimitAlarmTypeNode || hasConfiguredLimits();

    InstantiationRequest.Builder<ConditionTypeNode> requestBuilder =
        InstantiationRequest.of(rootClassForRequest(), effectiveTypeDefinitionId)
            .nodeId(nodeId)
            .browseName(browseName)
            .displayName(
                displayName != null ? displayName : LocalizedText.english(browseName.name()))
            .target(context.getNodeManager())
            .includeOptionals(
                declaration ->
                    assignedNodeIds.containsKey(declaration.browsePath())
                        || optionalIncludes.contains(declaration.browseName().name())
                        || (includeLimitMembers
                            && ACTIVE_STATE_EFFECTIVE_DISPLAY_NAME.equals(declaration.browsePath()))
                        || (withShelving
                            && SHELVING_STATE_LAST_TRANSITION.equals(declaration.browsePath()))
                        || (includeLimitMembers
                            && LIMIT_STATE_LAST_TRANSITION.equals(declaration.browsePath())));

    if (adopting) {
      requestBuilder.conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE);
      ConditionNodeTraversal.DiscoveredMethod enableMethod =
          requireNonNull(adoptedMethodSurface).get("Enable");
      if (enableMethod != null && !enableMethod.exclusivelyOwned()) {
        requestBuilder.methodInstantiation(MethodInstantiation.SHARE);
      }
      assignedNodeIds.forEach(
          (path, assignedNodeId) -> {
            if (!path.isRoot()) {
              requestBuilder.assignNodeId(path, assignedNodeId);
            }
          });
    }

    InstantiationRequest<ConditionTypeNode> request = requestBuilder.build();

    InstantiationResult<ConditionTypeNode> result =
        context.getServer().getNodeInstantiator().instantiate(request);
    ConditionTypeNode node = result.root();

    if (adopting) {
      initializeAdoptedNode(node, effectiveTypeDefinitionId);
    } else {
      initializeCreatedNode(node, effectiveTypeDefinitionId);
    }
    ConditionWiring.wire(node, conditionSource);

    return node;
  }

  @SuppressWarnings("unchecked")
  private Class<ConditionTypeNode> rootClassForRequest() {
    return adoptedNode != null
        ? (Class<ConditionTypeNode>) adoptedNode.getClass()
        : ConditionTypeNode.class;
  }

  private void initializeCreatedNode(ConditionTypeNode node, NodeId typeDefinitionId) {
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

    if (node instanceof AlarmConditionTypeNode alarmNode) {
      initializeAlarmNode(alarmNode);
    }

    if (node instanceof OffNormalAlarmTypeNode offNormalNode) {
      offNormalNode.setNormalState(normalState != null ? normalState : NodeId.NULL_VALUE);
    }

    if (node instanceof LimitAlarmTypeNode limitNode) {
      initializeLimitNode(limitNode);
    }
  }

  private void initializeAdoptedNode(ConditionTypeNode node, NodeId typeDefinitionId) {
    QualifiedName browseName = requireNonNull(this.browseName);

    if (displayNameConfigured) {
      node.setDisplayName(requireNonNull(displayName));
    }

    if (node.getEventType() == null) {
      node.setEventType(typeDefinitionId);
    }
    if (node.getEventId() == null) {
      node.setEventId(ByteString.NULL_VALUE);
    }
    if (node.getTime() == null) {
      node.setTime(DateTime.now());
    }
    if (node.getReceiveTime() == null) {
      node.setReceiveTime(DateTime.NULL_VALUE);
    }
    if (node.getMessage() == null) {
      node.setMessage(LocalizedText.NULL_VALUE);
    }
    if (severityConfigured || node.getSeverity() == null) {
      node.setSeverity(severity);
    }

    if (conditionNameConfigured || node.getConditionName() == null) {
      node.setConditionName(conditionName != null ? conditionName : browseName.name());
    }
    if (conditionClassConfigured || node.getConditionClassId() == null) {
      node.setConditionClassId(conditionClassId);
    }
    if (conditionClassConfigured || node.getConditionClassName() == null) {
      node.setConditionClassName(conditionClassName);
    }
    if (node.getBranchId() == null) {
      node.setBranchId(NodeId.NULL_VALUE);
    }
    if (node.getRetain() == null) {
      node.setRetain(false);
    }
    if (node.getSupportsFilteredRetain() == null) {
      node.setSupportsFilteredRetain(false);
    }

    if (node instanceof AlarmConditionTypeNode alarmNode) {
      if (inputNodeConfigured || alarmNode.getInputNode() == null) {
        alarmNode.setInputNode(inputNode != null ? inputNode : NodeId.NULL_VALUE);
      }
      if (alarmNode.getSuppressedOrShelved() == null) {
        alarmNode.setSuppressedOrShelved(false);
      }
      if (maxTimeShelvedConfigured) {
        alarmNode.setMaxTimeShelved((double) requireNonNull(maxTimeShelved).toMillis());
      }
    }

    if (node instanceof OffNormalAlarmTypeNode offNormalNode
        && (normalStateConfigured || offNormalNode.getNormalState() == null)) {
      offNormalNode.setNormalState(normalState != null ? normalState : NodeId.NULL_VALUE);
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
  }

  private boolean hasConfiguredLimits() {
    for (ExclusiveLimitState limit : ExclusiveLimitState.values()) {
      if (configuredLimit(limit) != null) {
        return true;
      }
    }
    return false;
  }

  private @Nullable EUInformation storedEngineeringUnits() {
    if (adoptedNode instanceof ExclusiveRateOfChangeAlarmTypeNode rateOfChangeNode) {
      return rateOfChangeNode.getEngineeringUnits();
    }
    if (adoptedNode instanceof NonExclusiveRateOfChangeAlarmTypeNode rateOfChangeNode) {
      return rateOfChangeNode.getEngineeringUnits();
    }
    return null;
  }
}
