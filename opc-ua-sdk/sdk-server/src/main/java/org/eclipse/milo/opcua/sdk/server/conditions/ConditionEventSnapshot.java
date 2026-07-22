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

import java.util.HashSet;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationResult;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * An immutable per-branch event snapshot used by ConditionRefresh/ConditionRefresh2 replay.
 *
 * <p>The snapshot materializes an event node tree in its own private {@link NodeManager} that is
 * never registered with the Server's AddressSpaceManager: the tree carries a copy of the Condition
 * instance's variable values, the Condition instance's own NodeId (so a ConditionId select clause
 * resolves to the ConditionId, as it does for live events), and the branch's last EventId and Time.
 * A private manager per snapshot means concurrent refreshes replaying the same Condition cannot
 * collide, and the tree is invisible to the Server's services.
 *
 * <p>Snapshots must be {@link #delete() deleted} once replay has delivered them.
 */
public final class ConditionEventSnapshot {

  private final BaseEventTypeNode eventNode;
  private final Runnable cleanup;

  ConditionEventSnapshot(BaseEventTypeNode eventNode) {
    this(eventNode, eventNode::delete);
  }

  private ConditionEventSnapshot(BaseEventTypeNode eventNode, Runnable cleanup) {
    this.eventNode = eventNode;
    this.cleanup = cleanup;
  }

  /**
   * Get the snapshot's event node, delivered to monitored items through their normal filtered
   * delivery path.
   *
   * @return the snapshot's {@link BaseEventTypeNode}.
   */
  public BaseEventTypeNode getEventNode() {
    return eventNode;
  }

  /** Delete the snapshot's event node tree from its private manager. */
  public void delete() {
    cleanup.run();
  }

  /**
   * Create a snapshot of {@code branch}'s state from {@code conditionNode}'s current variable
   * values.
   *
   * <p>Must be called under the owning Condition's lock so the copied values are mutually
   * consistent, and only for branches whose last recorded state the node currently reflects (for
   * the trunk that is always the case: every state change while Retained generates an event).
   *
   * @param server the {@link OpcUaServer} the Condition belongs to.
   * @param conditionNode the Condition instance node to snapshot.
   * @param branch the {@link ConditionBranch} whose last EventId/Time the snapshot carries.
   * @return the created {@link ConditionEventSnapshot}.
   * @throws UaException if materializing the snapshot's event node tree fails.
   */
  static ConditionEventSnapshot create(
      OpcUaServer server, ConditionTypeNode conditionNode, ConditionBranch branch)
      throws UaException {

    NodeId typeDefinitionId = conditionNode.getTypeDefinitionNode().getNodeId();

    var nodeManager = new UaNodeManager();

    var nodeContext =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };

    InstantiationRequest<BaseEventTypeNode> request =
        InstantiationRequest.of(BaseEventTypeNode.class, typeDefinitionId)
            .nodeId(conditionNode.getNodeId())
            .target(nodeManager)
            .includeAllOptionals()
            .build();

    InstantiationResult<BaseEventTypeNode> result =
        server.getNodeInstantiator().instantiate(request);
    BaseEventTypeNode snapshotNode = result.root();

    copyVariableValues(server, conditionNode, snapshotNode, new HashSet<>());

    snapshotNode.setEventType(typeDefinitionId);
    snapshotNode.setEventId(branch.getLastEventId());
    snapshotNode.setTime(branch.getLastEventTime());

    return new ConditionEventSnapshot(snapshotNode, result::deleteCreated);
  }

  /**
   * Copy the Value of every variable in {@code source}'s hierarchical subtree to the node at the
   * same browse path under {@code target}.
   *
   * <p>Source nodes with no counterpart in the target tree (non-modelled children added by the
   * application) are skipped; target nodes with no counterpart in the source (optional components
   * the instance was built without) keep their unset values and extract as null, as they do for
   * live events.
   */
  private static void copyVariableValues(
      OpcUaServer server, UaNode source, UaNode target, Set<NodeId> visited) {

    if (!visited.add(source.getNodeId())) {
      return;
    }

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();

    for (Reference reference : source.getReferences()) {
      if (!reference.isForward()
          || !referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), NodeIds.HierarchicalReferences)) {
        continue;
      }

      UaNode sourceChild =
          source
              .getNodeManager()
              .getNode(reference.getTargetNodeId(), server.getNamespaceTable())
              .or(() -> server.getAddressSpaceManager().getManagedNode(reference.getTargetNodeId()))
              .orElse(null);

      if (!(sourceChild instanceof UaVariableNode || sourceChild instanceof UaObjectNode)) {
        continue;
      }

      UaNode targetChild =
          target
              .findNode(
                  sourceChild.getBrowseName(),
                  n -> n.getNodeClass() == sourceChild.getNodeClass(),
                  r ->
                      r.isForward()
                          && referenceTypeTree.isSubtypeOf(
                              r.getReferenceTypeId(), NodeIds.HierarchicalReferences))
              .orElse(null);

      if (targetChild == null) {
        continue;
      }

      if (sourceChild instanceof UaVariableNode sourceVariable
          && targetChild instanceof UaVariableNode targetVariable) {
        targetVariable.setValue(sourceVariable.getValue());
      }

      copyVariableValues(server, sourceChild, targetChild, visited);
    }
  }
}
