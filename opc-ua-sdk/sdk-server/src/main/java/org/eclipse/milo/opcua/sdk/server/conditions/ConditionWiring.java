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

import java.util.ArrayList;
import java.util.List;
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

  static WiringJournal wire(ConditionTypeNode condition, @Nullable UaNode source) {
    WiringJournal journal = new WiringJournal();
    if (source == null) {
      return journal;
    }

    try {
      condition.setSourceNode(source.getNodeId());

      LocalizedText sourceDisplayName = source.getDisplayName();
      String sourceName = sourceDisplayName != null ? sourceDisplayName.text() : null;
      condition.setSourceName(sourceName != null ? sourceName : source.getBrowseName().name());

      Reference hasCondition =
          new Reference(
              source.getNodeId(), NodeIds.HasCondition, condition.getNodeId().expanded(), true);

      addReferenceIfAbsent(source, hasCondition, journal);

      ensureEventSourceWiring(condition, source, journal);

      return journal;
    } catch (RuntimeException e) {
      try {
        journal.rollBack();
      } catch (RuntimeException rollbackFailure) {
        e.addSuppressed(rollbackFailure);
      }
      throw e;
    }
  }

  private static void ensureEventSourceWiring(
      ConditionTypeNode condition, UaNode source, WiringJournal journal) {
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
              addReferenceIfAbsent(serverNode, hasEventSource, journal);
            });
  }

  /**
   * Atomically add a Reference and its inverse if absent. NodeManager reference storage is a
   * multiset, so the deduplicating commit is the point at which concurrent wiring calls must
   * converge; a contains-then-add sequence cannot repair duplicate occurrences afterwards.
   */
  private static void addReferenceIfAbsent(
      UaNode owner, Reference reference, WiringJournal journal) {
    NodeManager<UaNode> nodeManager = owner.getNodeManager();
    NodeManagerBatch.Builder<UaNode> batch = NodeManagerBatch.builder();
    batch.addReference(reference);
    reference.invert(owner.getNodeContext().getNamespaceTable()).ifPresent(batch::addReference);

    try {
      journal.record(nodeManager, nodeManager.commit(batch.build()));
    } catch (NodeManagerBatchException e) {
      journal.record(nodeManager, e.getApplied());
      throw new UaRuntimeException(e.getStatusCode().getValue(), e);
    }
  }

  /** Exact per-NodeManager journal of references added by one source-wiring operation. */
  static final class WiringJournal {

    private final List<AppliedReferences> appliedReferences = new ArrayList<>();
    private boolean rolledBack;

    private void record(NodeManager<UaNode> nodeManager, NodeManager.CommitResult applied) {
      if (!applied.addedReferences().isEmpty()) {
        appliedReferences.add(new AppliedReferences(nodeManager, applied.addedReferences()));
      }
    }

    /** Remove only the reference occurrences that this wiring operation added. */
    void rollBack() {
      if (rolledBack) {
        return;
      }
      rolledBack = true;

      RuntimeException failure = null;
      for (int i = appliedReferences.size() - 1; i >= 0; i--) {
        AppliedReferences applied = appliedReferences.get(i);
        List<Reference> references = applied.references();
        for (int j = references.size() - 1; j >= 0; j--) {
          try {
            applied.nodeManager().removeReference(references.get(j));
          } catch (RuntimeException e) {
            if (failure == null) {
              failure = e;
            } else {
              failure.addSuppressed(e);
            }
          }
        }
      }

      if (failure != null) {
        throw failure;
      }
    }
  }

  private record AppliedReferences(NodeManager<UaNode> nodeManager, List<Reference> references) {}
}
