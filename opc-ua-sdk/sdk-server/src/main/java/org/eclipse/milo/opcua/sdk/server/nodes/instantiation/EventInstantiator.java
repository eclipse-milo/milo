/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 * Creates transient Event instances through the {@link NodeInstantiator}, replacing the legacy
 * {@link org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory}.
 *
 * <p>Event nodes are created with every optional member included, into a private {@link
 * UaNodeManager} that is registered with the server's {@link
 * org.eclipse.milo.opcua.sdk.server.AddressSpaceManager} while this component is running, so event
 * fields are resolvable during filter evaluation without the events being reachable from the
 * server's hierarchy. The EventType Property is set as part of the instantiation rather than by
 * mutating the created node.
 *
 * <p>Unlike the legacy factory — which cast the created node to {@link BaseEventTypeNode} after its
 * nodes were already stored, throwing {@link ClassCastException} and leaving residue for custom
 * event types with no registered Java class — the expected class here is validated at plan time,
 * before any node is created.
 */
public class EventInstantiator extends AbstractLifecycle {

  private static final BrowsePath EVENT_TYPE_PATH =
      BrowsePath.of(new QualifiedName(0, "EventType"));

  private final UaNodeManager nodeManager = new UaNodeManager();

  private final OpcUaServer server;

  public EventInstantiator(OpcUaServer server) {
    this.server = server;
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
   * <p>Event nodes must be deleted by the caller ({@link UaNode#delete()}) once they have been
   * posted to the event bus or their lifetime has otherwise expired.
   *
   * @param nodeId the {@link NodeId} to use for the Event instance root.
   * @param typeDefinitionId the {@link NodeId} of the ObjectType node representing the type
   *     definition.
   * @return a {@link BaseEventTypeNode} instance; an unregistered custom event type resolves to its
   *     nearest registered ancestor's class.
   * @throws UaException if the type cannot be instantiated, or its resolved Java class is not a
   *     {@link BaseEventTypeNode} — found at plan time, before any node is created.
   */
  public BaseEventTypeNode createEvent(NodeId nodeId, NodeId typeDefinitionId) throws UaException {
    return createEvent(BaseEventTypeNode.class, nodeId, typeDefinitionId);
  }

  /**
   * Create an Event instance of the type identified by {@code typeDefinitionId}, expecting {@code
   * eventClass} as the root's Java class.
   *
   * <p>Event nodes must be deleted by the caller ({@link UaNode#delete()}) once they have been
   * posted to the event bus or their lifetime has otherwise expired.
   *
   * @param eventClass the expected Java class of the Event instance root.
   * @param nodeId the {@link NodeId} to use for the Event instance root.
   * @param typeDefinitionId the {@link NodeId} of the ObjectType node representing the type
   *     definition.
   * @param <T> the expected root class.
   * @return an Event instance of {@code eventClass}.
   * @throws UaException if the type cannot be instantiated, or its resolved Java class is not an
   *     {@code eventClass} — found at plan time, before any node is created.
   */
  public <T extends BaseEventTypeNode> T createEvent(
      Class<T> eventClass, NodeId nodeId, NodeId typeDefinitionId) throws UaException {

    InstantiationRequest<T> request =
        InstantiationRequest.of(eventClass, typeDefinitionId)
            .nodeId(nodeId)
            .target(nodeManager)
            .includeAllOptionals()
            // Event instances are transient values, never part of the server's hierarchy, so
            // abstract EventTypes (BaseEventType itself is abstract) are legitimate here.
            .allowAbstractType()
            .onNode(
                (declaration, node, parent, graph) -> {
                  if (declaration != null
                      && EVENT_TYPE_PATH.equals(declaration.browsePath())
                      && node instanceof UaVariableNode variableNode) {

                    variableNode.setValue(new DataValue(new Variant(typeDefinitionId)));
                  }
                })
            .build();

    return server.getNodeInstantiator().instantiate(request).root();
  }

  /**
   * @return the private {@link UaNodeManager} Event instances are created in.
   */
  UaNodeManager getNodeManager() {
    return nodeManager;
  }
}
