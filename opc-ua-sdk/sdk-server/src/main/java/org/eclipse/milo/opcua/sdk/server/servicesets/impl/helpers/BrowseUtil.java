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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference.ServerIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
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

  /**
   * Resolve and normalize an {@link ExpandedNodeId} against {@code serverTable} and {@code
   * namespaceTable} per OPC 10000-4 §7.16 and §7.29.
   *
   * <p>The target's server reference is resolved to a {@link ServerIndex} via {@code serverTable}
   * first so a target addressed by this server's own URI (index {@code 0}) is classified as local,
   * while an unregistered remote server URI or server index returns empty instead of failing binary
   * encoding or emitting an invalid target. Local targets (server index {@code 0}) are normalized
   * to relative namespace-index form and validated against {@code namespaceTable}; remote targets
   * (server index {@code > 0}) are normalized to absolute namespace-URI form. If the required
   * namespace entry or mapping is absent from {@code namespaceTable}, empty is returned.
   *
   * @param nodeId the ExpandedNodeId to resolve and normalize.
   * @param serverTable the ServerTable used to resolve server URIs and validate remote indices.
   * @param namespaceTable the NamespaceTable used to map namespace indices and URIs.
   * @return the normalized ExpandedNodeId (always using a {@link ServerIndex}), or empty if the
   *     target cannot be validly represented.
   */
  public static Optional<ExpandedNodeId> normalize(
      ExpandedNodeId nodeId, ServerTable serverTable, NamespaceTable namespaceTable) {

    UInteger serverIndex = nodeId.getServerIndex(serverTable);
    if (serverIndex == null) {
      return Optional.empty();
    }

    if (serverIndex.longValue() > 0L && serverTable.get(serverIndex) == null) {
      return Optional.empty();
    }

    ExpandedNodeId withServerIndex =
        nodeId.server() instanceof ServerIndex
            ? nodeId
            : new ExpandedNodeId(
                ServerReference.of(serverIndex), nodeId.namespace(), nodeId.identifier());

    if (serverIndex.longValue() == 0L) {
      if (withServerIndex.isAbsolute()) {
        return withServerIndex.relative(namespaceTable);
      } else {
        UShort namespaceIndex = withServerIndex.getNamespaceIndex();
        return namespaceIndex != null && namespaceTable.get(namespaceIndex) != null
            ? Optional.of(withServerIndex)
            : Optional.empty();
      }
    } else {
      return withServerIndex.isRelative()
          ? withServerIndex.absolute(namespaceTable)
          : Optional.of(withServerIndex);
    }
  }
}
