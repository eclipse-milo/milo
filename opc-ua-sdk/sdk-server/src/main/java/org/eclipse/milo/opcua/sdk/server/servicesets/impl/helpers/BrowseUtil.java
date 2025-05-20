/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;

class BrowseUtil {

  private BrowseUtil() {}

  /**
   * "Normalize" an {@link ExpandedNodeId} to be relative (index-based) if it is local and absolute
   * (uri-based) if it is not.
   *
   * <p>This is required for ExpandedNodeId values returned in {@link ReferenceDescription} and
   * {@link BrowsePathTarget}. See <a
   * href="https://reference.opcfoundation.org/Core/Part4/v105/docs/7.30">ReferenceDescription</a>.
   *
   * @param nodeId the ExpandedNodeId to normalize.
   * @param namespaceTable the NamespaceTable to use.
   * @return the normalized ExpandedNodeId.
   */
  public static ExpandedNodeId normalize(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    if (nodeId.isLocal()) {
      if (nodeId.isAbsolute()) {
        nodeId = nodeId.relative(namespaceTable).orElseThrow();
      }
    } else {
      if (nodeId.isRelative()) {
        nodeId = nodeId.absolute(namespaceTable).orElseThrow();
      }
    }
    return nodeId;
  }
}
