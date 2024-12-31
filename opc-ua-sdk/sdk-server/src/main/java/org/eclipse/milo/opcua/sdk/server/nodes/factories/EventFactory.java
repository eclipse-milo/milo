/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.factories;

import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public class EventFactory extends AbstractLifecycle {

  private final NodeManager<UaNode> nodeManager = new UaNodeManager();

  private final OpcUaServer server;
  private final NodeFactory nodeFactory;

  public EventFactory(OpcUaServer server) {
    this(server, server.getObjectTypeManager(), server.getVariableTypeManager());
  }

  public EventFactory(
      OpcUaServer server,
      ObjectTypeManager objectTypeManager,
      VariableTypeManager variableTypeManager) {

    this.server = server;

    nodeFactory =
        new NodeFactory(
            new EventNodeContext(server, nodeManager), objectTypeManager, variableTypeManager);
  }

  @Override
  protected void onStartup() {
    server.getAddressSpaceManager().register(nodeManager);
  }

  @Override
  protected void onShutdown() {
    server.getAddressSpaceManager().unregister(nodeManager);
  }

  /**
   * Create an Event instance of the type identified by {@code typeDefinitionId}.
   *
   * <p>Event Nodes must be deleted by the caller when once they have been posted to the event bus
   * or their lifetime has otherwise expired.
   *
   * @param nodeId the {@link NodeId} to use for the Event {@link ObjectNode}.
   * @param typeDefinitionId the {@link NodeId} of the {@link ObjectTypeNode} representing the type
   *     definition.
   * @return an Event {@link ObjectNode} instance.
   * @throws UaException if an error occurs creating the Event instance.
   */
  public BaseEventTypeNode createEvent(NodeId nodeId, NodeId typeDefinitionId) throws UaException {
    return (BaseEventTypeNode)
        nodeFactory.createNode(
            nodeId,
            typeDefinitionId,
            new NodeFactory.InstantiationCallback() {
              @Override
              public boolean includeOptionalNode(
                  NodeId typeDefinitionId, QualifiedName browseName) {
                return true;
              }
            });
  }

  private static class EventNodeContext implements UaNodeContext {

    private final OpcUaServer server;
    private final NodeManager<UaNode> nodeManager;

    EventNodeContext(OpcUaServer server, NodeManager<UaNode> nodeManager) {
      this.server = server;
      this.nodeManager = nodeManager;
    }

    @Override
    public OpcUaServer getServer() {
      return server;
    }

    @Override
    public NodeManager<UaNode> getNodeManager() {
      return nodeManager;
    }
  }
}
