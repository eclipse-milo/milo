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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
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
  }

  private void initializeAlarmNode(AlarmConditionTypeNode node) {
    node.setInputNode(inputNode != null ? inputNode : NodeId.NULL_VALUE);
    node.setSuppressedOrShelved(false);

    if (maxTimeShelved != null) {
      node.setMaxTimeShelved((double) maxTimeShelved.toMillis());
    }
  }

  /**
   * Wire the present sub-states of EnabledState per §5.4 in a single pass: AckedState and
   * ConfirmedState for every AcknowledgeableCondition, plus ActiveState and the ShelvingState
   * sub-state machine (§5.8.10) for an AlarmCondition. The required inverse Reference from
   * sub-state to super-state is added automatically by {@code UaNode.addReference}.
   */
  private void wireSubStates(AcknowledgeableConditionTypeNode node) {
    TwoStateVariableTypeNode enabledState = node.getEnabledStateNode();
    if (enabledState == null) {
      return;
    }

    wireSubState(enabledState, node.getAckedStateNode());
    wireSubState(enabledState, node.getConfirmedStateNode());

    if (node instanceof AlarmConditionTypeNode alarmNode) {
      wireSubState(enabledState, alarmNode.getActiveStateNode());
      wireSubState(enabledState, alarmNode.getShelvingStateNode());
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
