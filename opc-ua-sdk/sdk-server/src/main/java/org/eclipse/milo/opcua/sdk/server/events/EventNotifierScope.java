/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Resolves the notifier hierarchy that associates an Event with notifier MonitoredItems. */
public final class EventNotifierScope {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventNotifierScope.class);

  private EventNotifierScope() {}

  /**
   * Collect the Event's SourceNode plus every node that can reach it via forward
   * HasEventSource/HasNotifier References, by walking inverse References up from the SourceNode.
   *
   * <p>Reference pairs must be present in both directions. Resolution tolerates concurrent address
   * space mutation: a node whose References cannot be read is omitted from the result.
   *
   * @param server the {@link OpcUaServer} whose notifier hierarchy is resolved.
   * @param event the Event whose notifier scope is resolved.
   * @return the Event's SourceNode and its notifier ancestors.
   */
  public static Set<NodeId> resolve(OpcUaServer server, BaseEventTypeNode event) {
    NodeId sourceNodeId = event != null ? event.getSourceNode() : null;

    if (sourceNodeId == null || sourceNodeId.isNull()) {
      return Set.of();
    }

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();

    var scope = new HashSet<NodeId>();
    var queue = new ArrayDeque<NodeId>();
    scope.add(sourceNodeId);
    queue.add(sourceNodeId);

    while (!queue.isEmpty()) {
      NodeId nodeId = queue.poll();

      List<Reference> references;
      try {
        references =
            server
                .getAddressSpaceManager()
                .getManagedReferences(
                    nodeId,
                    r ->
                        r.isInverse()
                            && (NodeIds.HasEventSource.equals(r.getReferenceTypeId())
                                || referenceTypeTree.isSubtypeOf(
                                    r.getReferenceTypeId(), NodeIds.HasEventSource)));
      } catch (Throwable t) {
        LOGGER.debug("Error resolving notifier scope at {}", nodeId, t);
        continue;
      }

      for (Reference reference : references) {
        reference
            .getTargetNodeId()
            .toNodeId(server.getNamespaceTable())
            .ifPresent(
                targetNodeId -> {
                  if (scope.add(targetNodeId)) {
                    queue.add(targetNodeId);
                  }
                });
      }
    }

    return Set.copyOf(scope);
  }

  /**
   * Check whether {@code item} is associated with an Event whose resolved notifier ancestors are
   * {@code scope}. The Server Object is the root notifier and therefore matches every Event.
   *
   * @param item the notifier MonitoredItem.
   * @param scope an Event notifier scope returned by {@link #resolve}.
   * @return {@code true} if the Event is in the item's notifier hierarchy.
   */
  public static boolean contains(MonitoredItem item, Set<NodeId> scope) {
    NodeId monitoredNodeId = item.getReadValueId().getNodeId();

    return NodeIds.Server.equals(monitoredNodeId) || scope.contains(monitoredNodeId);
  }
}
