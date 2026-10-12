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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
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

  /**
   * Register {@code area} as an event notifier node in the Server's notifier hierarchy.
   *
   * <p>Sets the SubscribeToEvents bit of the node's EventNotifier attribute and, unless the node is
   * already the target of a HasNotifier Reference from a parent area, wires a HasNotifier Reference
   * pair from the Server Object to the node.
   *
   * <p>Events fired for a source node reachable from {@code area} via forward
   * HasEventSource/HasNotifier References are delivered to event monitored items on {@code area}.
   * This method is idempotent.
   *
   * @param area the {@link UaObjectNode} to register as an event notifier.
   */
  protected void registerEventNotifier(UaObjectNode area) {
    UByte eventNotifier = area.getEventNotifier();

    int bits = eventNotifier != null ? eventNotifier.intValue() : 0;
    if ((bits & 0x01) == 0) {
      area.setEventNotifier(ubyte(bits | 0x01));
    }

    boolean hasNotifierParent =
        getServer().getAddressSpaceManager().getManagedReferences(area.getNodeId()).stream()
            .anyMatch(r -> r.isInverse() && NodeIds.HasNotifier.equals(r.getReferenceTypeId()));

    if (!hasNotifierParent) {
      UaNode serverNode =
          getServer()
              .getAddressSpaceManager()
              .getManagedNode(NodeIds.Server)
              .orElseThrow(() -> new IllegalStateException("Server Object Node not found"));

      serverNode
          .getNodeManager()
          .addReferences(
              new Reference(NodeIds.Server, NodeIds.HasNotifier, area.getNodeId().expanded(), true),
              getServer().getNamespaceTable());
    }
  }
}
