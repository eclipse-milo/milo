/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * A {@link FieldAddress} backed by a Node in an OPC UA address space.
 *
 * <p>The Node is identified by an {@link ExpandedNodeId}, preferably in namespace-URI form, so the
 * address remains durable across processes with differing namespace tables.
 *
 * @param nodeId the {@link ExpandedNodeId} identifying the source Node.
 * @param attributeId the attribute to publish, typically {@link AttributeId#Value}.
 */
public record NodeFieldAddress(ExpandedNodeId nodeId, AttributeId attributeId)
    implements FieldAddress {

  /**
   * Create a {@link NodeFieldAddress} from a {@link NodeId}, converting it to a namespace-URI
   * {@link ExpandedNodeId} using {@code namespaceTable}.
   *
   * @param nodeId the {@link NodeId} identifying the source Node.
   * @param attributeId the attribute to publish.
   * @param namespaceTable the {@link NamespaceTable} used to look up the namespace URI.
   * @return a new {@link NodeFieldAddress}.
   * @throws IllegalStateException if the namespace index of {@code nodeId} is not present in {@code
   *     namespaceTable}.
   */
  public static NodeFieldAddress of(
      NodeId nodeId, AttributeId attributeId, NamespaceTable namespaceTable) {

    return new NodeFieldAddress(nodeId.expanded(namespaceTable), attributeId);
  }

  /**
   * Create a {@link NodeFieldAddress} by parsing {@code expandedNodeIdText}.
   *
   * @param expandedNodeIdText an ExpandedNodeId in its parseable string form, e.g. {@code
   *     "nsu=urn:example:namespace;s=Sensor.Temperature"}.
   * @param attributeId the attribute to publish.
   * @return a new {@link NodeFieldAddress}.
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if {@code expandedNodeIdText} is
   *     not a parseable ExpandedNodeId.
   */
  public static NodeFieldAddress parse(String expandedNodeIdText, AttributeId attributeId) {
    return new NodeFieldAddress(ExpandedNodeId.parse(expandedNodeIdText), attributeId);
  }
}
