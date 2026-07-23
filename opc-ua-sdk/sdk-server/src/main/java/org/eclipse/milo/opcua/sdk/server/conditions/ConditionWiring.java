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

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatch;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatchException;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/** Idempotent source and notifier wiring shared by create, attach, and adopt. */
final class ConditionWiring {

  private ConditionWiring() {}

  static void wire(ConditionTypeNode condition, @Nullable UaNode source) {
    if (source == null) {
      return;
    }

    condition.setSourceNode(source.getNodeId());

    LocalizedText sourceDisplayName = source.getDisplayName();
    String sourceName = sourceDisplayName != null ? sourceDisplayName.text() : null;
    condition.setSourceName(sourceName != null ? sourceName : source.getBrowseName().name());

    Reference hasCondition =
        new Reference(
            source.getNodeId(), NodeIds.HasCondition, condition.getNodeId().expanded(), true);

    addReferenceIfAbsent(source, hasCondition);

    ensureEventSourceWiring(condition, source);
  }

  private static void ensureEventSourceWiring(ConditionTypeNode condition, UaNode source) {
    var context = condition.getNodeContext();
    ReferenceTypeTree referenceTypeTree = context.getServer().getReferenceTypeTree();

    boolean hasEventSourceParent =
        context
            .getServer()
            .getAddressSpaceManager()
            .getManagedReferences(source.getNodeId())
            .stream()
            .anyMatch(
                reference ->
                    reference.isInverse()
                        && (NodeIds.HasEventSource.equals(reference.getReferenceTypeId())
                            || (referenceTypeTree != null
                                && referenceTypeTree.isSubtypeOf(
                                    reference.getReferenceTypeId(), NodeIds.HasEventSource))));

    if (hasEventSourceParent) {
      return;
    }

    Reference hasEventSource =
        new Reference(NodeIds.Server, NodeIds.HasEventSource, source.getNodeId().expanded(), true);

    context
        .getServer()
        .getAddressSpaceManager()
        .getManagedNode(NodeIds.Server)
        .ifPresent(
            serverNode -> {
              addReferenceIfAbsent(serverNode, hasEventSource);
            });
  }

  /**
   * Atomically add a Reference and its inverse if absent. NodeManager reference storage is a
   * multiset, so the deduplicating commit is the point at which concurrent wiring calls must
   * converge; a contains-then-add sequence cannot repair duplicate occurrences afterwards.
   */
  private static void addReferenceIfAbsent(UaNode owner, Reference reference) {
    NodeManager<UaNode> nodeManager = owner.getNodeManager();
    NodeManagerBatch.Builder<UaNode> batch = NodeManagerBatch.builder();
    batch.addReference(reference);
    reference.invert(owner.getNodeContext().getNamespaceTable()).ifPresent(batch::addReference);

    try {
      nodeManager.commit(batch.build());
    } catch (NodeManagerBatchException e) {
      throw new UaRuntimeException(e.getStatusCode().getValue(), e);
    }
  }
}
