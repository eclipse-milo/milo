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

/** Shared reference matching for Method ownership and declaration lookup. */
final class MethodReferences {
  private MethodReferences() {}

  static boolean isForwardComponent(UaNodeContext context, Reference reference) {
    return reference.isForward()
        && (reference.getReferenceTypeId().equals(NodeIds.HasComponent)
            || context
                .getServer()
                .getReferenceTypeTree()
                .isSubtypeOf(reference.getReferenceTypeId(), NodeIds.HasComponent));
  }
}
