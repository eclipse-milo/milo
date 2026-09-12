/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.server.nodes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;

/** Shared reference matching for Method ownership and declaration lookup. */
final class MethodReferences {
  private MethodReferences() {}

  static boolean isForwardComponent(UaNodeContext context, Reference reference) {
    if (!reference.isForward()) return false;
    NodeId current = reference.getReferenceTypeId();
    if (current.equals(NodeIds.HasComponent)) return true;

    var visited = new HashSet<NodeId>();
    boolean component = false;
    while (visited.add(current)) {
      NodeId id = current;
      var definition =
          context
              .getNodeManager()
              .getNode(id)
              .or(() -> context.getServer().getAddressSpaceManager().getManagedNode(id));
      if (definition.isEmpty()) {
        // Keep cached-tree compatibility only where no managed definition is authoritative.
        // Start at the unresolved ancestor so a private prefix cannot be replaced by stale
        // ancestry.
        return component
            || id.equals(NodeIds.HasComponent)
            || context.getServer().getReferenceTypeTree().isSubtypeOf(id, NodeIds.HasComponent);
      }
      UaNode node = definition.orElseThrow();
      if (!node.getNodeId().equals(id)
          || node.getNodeContext().getServer() != context.getServer()
          || node.getNodeClass() != NodeClass.ReferenceType) {
        return false;
      }
      component |= id.equals(NodeIds.HasComponent);
      var parents = new LinkedHashSet<NodeId>();
      for (Reference parent : node.getReferences()) {
        if (!parent.isForward() && parent.getReferenceTypeId().equals(NodeIds.HasSubtype)) {
          var target = parent.getTargetNodeId();
          if (!target.isLocal()) return false;
          var parentId = target.toNodeId(context.getNamespaceTable());
          if (parentId.isEmpty()) return false;
          parents.add(parentId.orElseThrow());
        }
      }
      if (id.equals(NodeIds.References)) return parents.isEmpty() && component;
      if (parents.size() != 1) return false;
      current = parents.iterator().next();
    }
    return false;
  }
}
