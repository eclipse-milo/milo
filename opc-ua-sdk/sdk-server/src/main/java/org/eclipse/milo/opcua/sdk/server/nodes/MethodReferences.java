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

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/** Shared reference matching for Method ownership and declaration lookup. */
final class MethodReferences {
  private MethodReferences() {}

  /**
   * Whether {@code reference} is a forward HasComponent reference or a forward reference of a
   * HasComponent subtype, as recorded in the server's ReferenceTypeTree.
   */
  static boolean isForwardComponent(UaNodeContext context, Reference reference) {
    if (!reference.isForward()) return false;
    NodeId referenceTypeId = reference.getReferenceTypeId();
    // TypeTree.isSubtypeOf() does not consider a type to be a subtype of itself.
    return referenceTypeId.equals(NodeIds.HasComponent)
        || context
            .getServer()
            .getReferenceTypeTree()
            .isSubtypeOf(referenceTypeId, NodeIds.HasComponent);
  }
}
