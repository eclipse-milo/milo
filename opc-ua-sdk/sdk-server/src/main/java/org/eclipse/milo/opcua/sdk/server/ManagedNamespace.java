/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/** A {@link ManagedAddressSpaceFragment} for all Nodes belonging to a namespace index / URI. */
public abstract class ManagedNamespace extends ManagedAddressSpaceFragment implements Namespace {

  private final AddressSpaceFilter filter;

  private final String namespaceUri;
  private final UShort namespaceIndex;

  /**
   * Create a {@link ManagedNamespace} at {@code namespaceUri}.
   *
   * <p>The URI will be registered with the Server's {@link NamespaceTable} and assigned a namespace
   * index.
   *
   * @param server the {@link OpcUaServer}.
   * @param namespaceUri the URI assigned to this namespace.
   */
  public ManagedNamespace(OpcUaServer server, String namespaceUri) {
    super(server);

    this.namespaceUri = namespaceUri;
    this.namespaceIndex = server.getNamespaceTable().add(namespaceUri);

    filter =
        SimpleAddressSpaceFilter.create(
            nodeId -> nodeId.getNamespaceIndex().equals(getNamespaceIndex()));
  }

  @Override
  public AddressSpaceFilter getFilter() {
    return filter;
  }

  @Override
  public final String getNamespaceUri() {
    return namespaceUri;
  }

  @Override
  public final UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  /**
   * Create a new {@link NodeId} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param id the id of the {@link NodeId}.
   * @return a {@link NodeId} belonging to this namespace.
   */
  protected final NodeId newNodeId(long id) {
    return new NodeId(namespaceIndex, uint(id));
  }

  /**
   * Create a new {@link NodeId} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param id the id of the {@link NodeId}.
   * @return a {@link NodeId} belonging to this namespace.
   */
  protected final NodeId newNodeId(UInteger id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Create a new {@link NodeId} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param id the id of the {@link NodeId}.
   * @return a {@link NodeId} belonging to this namespace.
   */
  protected final NodeId newNodeId(String id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Create a new {@link NodeId} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param id the id of the {@link NodeId}.
   * @return a {@link NodeId} belonging to this namespace.
   */
  protected final NodeId newNodeId(UUID id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Create a new {@link NodeId} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param id the id of the {@link NodeId}.
   * @return a {@link NodeId} belonging to this namespace.
   */
  protected final NodeId newNodeId(ByteString id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Create a new {@link QualifiedName} using the namespace index for this {@link ManagedNamespace}.
   *
   * @param name the name component of the {@link QualifiedName}.
   * @return a {@link QualifiedName} belonging to this namespace.
   */
  protected final QualifiedName newQualifiedName(String name) {
    return new QualifiedName(namespaceIndex, name);
  }
}
