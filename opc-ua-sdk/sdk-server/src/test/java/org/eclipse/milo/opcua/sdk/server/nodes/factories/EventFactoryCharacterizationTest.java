/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.namespaces.loader.NodeLoader;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.typetree.ReferenceTypeTreeBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * Characterization tests pinning the observable behavior of {@link EventFactory} exactly as
 * shipped, before the replacement node-instantiation engine lands. This fixture is the assertion
 * source for the event-path replacement: {@code createEvent} forces all optional members on, casts
 * the result to {@link BaseEventTypeNode} (so a custom, unregistered event type throws {@link
 * ClassCastException} — a hazard pinned as-is), applies {@code setEventType}, and stores the
 * created nodes in the factory's private, AddressSpaceManager-registered {@link UaNodeManager}
 * rather than in any caller-visible NodeManager.
 *
 * <p>A test failure here means legacy behavior drifted; a deliberate, reviewed legacy fix must
 * update this fixture in the same commit. See {@link NodeFactoryCharacterizationTest} for the
 * shared nondeterminism and static-IDH-cache isolation notes; synthetic type NodeIds here are
 * likewise unique per test method.
 */
public class EventFactoryCharacterizationTest {

  private static final AtomicInteger TEST_COUNTER = new AtomicInteger();

  private static final String TEST_NAMESPACE_URI = "urn:eclipse:milo:test:characterization:events";

  /** Unique per test method; prefixes every synthetic NodeId to defeat the static IDH cache. */
  private String testPrefix;

  private OpcUaServer server;
  private NamespaceTable namespaceTable;
  private UaNodeManager nodeManager;
  private UaNodeContext context;
  private EventFactory eventFactory;

  /** The private NodeManager EventFactory registered with the AddressSpaceManager on startup. */
  private NodeManager<UaNode> privateNodeManager;

  @BeforeEach
  public void setup() throws Exception {
    testPrefix = "EventFactoryCharacterization" + TEST_COUNTER.incrementAndGet();

    server = Mockito.mock(OpcUaServer.class);

    namespaceTable = new NamespaceTable();
    namespaceTable.add(TEST_NAMESPACE_URI);
    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);

    nodeManager = new UaNodeManager();

    AddressSpaceManager addressSpaceManager = Mockito.mock(AddressSpaceManager.class);

    // EventFactory registers a private NodeManager with the AddressSpaceManager; lookups must
    // aggregate the ns0 manager and everything registered, like the real composite does.
    List<NodeManager<UaNode>> managers = new java.util.concurrent.CopyOnWriteArrayList<>();
    managers.add(nodeManager);

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(NodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation -> {
                  NodeId nodeId = invocation.getArgument(0);
                  return managers.stream().flatMap(m -> m.getNode(nodeId).stream()).findFirst();
                });

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(ExpandedNodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation -> {
                  ExpandedNodeId nodeId = invocation.getArgument(0);
                  return managers.stream()
                      .flatMap(m -> m.getNode(nodeId, namespaceTable).stream())
                      .findFirst();
                });

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class)))
        .then(
            (Answer<List<Reference>>)
                invocation -> {
                  NodeId nodeId = invocation.getArgument(0);
                  return managers.stream().flatMap(m -> m.getReferences(nodeId).stream()).toList();
                });

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class), Mockito.any()))
        .then(
            (Answer<List<Reference>>)
                invocation -> {
                  NodeId nodeId = invocation.getArgument(0);
                  java.util.function.Predicate<Reference> filter = invocation.getArgument(1);
                  return managers.stream()
                      .flatMap(m -> m.getReferences(nodeId, filter).stream())
                      .toList();
                });

    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    Mockito.when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    Mockito.when(server.getEncodingManager()).thenReturn(OpcUaEncodingManager.getInstance());

    context =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };

    new NodeLoader(context, nodeManager).loadNodes();

    // Built outside the when() call because building the tree interacts with the same mock.
    ReferenceTypeTree referenceTypeTree = ReferenceTypeTreeBuilder.build(server);
    Mockito.when(server.getReferenceTypeTree()).thenReturn(referenceTypeTree);

    ObjectTypeManager objectTypeManager = new ObjectTypeManager();
    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);

    VariableTypeManager variableTypeManager = new VariableTypeManager();
    VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);

    eventFactory = new EventFactory(server, objectTypeManager, variableTypeManager);
    eventFactory.startup();

    @SuppressWarnings("unchecked")
    ArgumentCaptor<NodeManager<UaNode>> captor = ArgumentCaptor.forClass(NodeManager.class);
    Mockito.verify(addressSpaceManager).register(captor.capture());
    privateNodeManager = captor.getValue();
    managers.add(privateNodeManager);
  }

  /**
   * {@code createEvent}'s happy-path contract, pinned as shipped: all optional members are forced
   * on (BaseEventType's Optional {@code LocalTime} property is instantiated), the result is a
   * {@link BaseEventTypeNode} from the registered constructor, {@code setEventType} is applied, and
   * every created node lands in the factory's private, ASM-registered NodeManager — not in any
   * other NodeManager.
   */
  @Test
  public void createEventForcesOptionalsAppliesEventTypeAndUsesPrivateNodeManager()
      throws Exception {
    assertNotSame(nodeManager, privateNodeManager, "EventFactory registered its own NodeManager");

    NodeId eventNodeId = new NodeId(1, testPrefix + ":TestEvent");

    BaseEventTypeNode event = eventFactory.createEvent(eventNodeId, NodeIds.BaseEventType);

    assertNotNull(event);
    assertEquals(eventNodeId, event.getNodeId());

    // setEventType was applied.
    assertEquals(NodeIds.BaseEventType, event.getEventType());

    // Mandatory member present, with the child-id formula over the ns0 browse name.
    NodeId eventIdPropertyId = new NodeId(1, testPrefix + ":TestEvent/0:EventId");
    assertTrue(privateNodeManager.containsNode(eventIdPropertyId));

    // Optional member (LocalTime, ModellingRule Optional in ns0) forced on.
    NodeId localTimePropertyId = new NodeId(1, testPrefix + ":TestEvent/0:LocalTime");
    assertTrue(
        privateNodeManager.containsNode(localTimePropertyId),
        "optional members are unconditionally included by createEvent");

    // Nodes land in the private manager only.
    assertTrue(privateNodeManager.containsNode(eventNodeId));
    assertFalse(nodeManager.containsNode(eventNodeId));
    assertFalse(nodeManager.containsNode(eventIdPropertyId));
  }

  /**
   * Pinned as shipped: an event type with no registered constructor falls back to a plain {@link
   * UaObjectNode}, and {@code createEvent}'s cast throws {@link ClassCastException} instead of a
   * {@code UaException}. The instantiated nodes have already been stored in the private NodeManager
   * by the time the cast fails, and are left behind.
   */
  @Test
  public void createEventWithUnregisteredCustomTypeThrowsClassCastException() {
    NodeId customTypeId = new NodeId(1, testPrefix + ":CustomEventType");

    UaObjectTypeNode customEventType =
        new UaObjectTypeNode(
            context,
            customTypeId,
            new QualifiedName(1, "CustomEventType"),
            LocalizedText.english("CustomEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            false);
    customEventType.addReference(
        new Reference(customTypeId, NodeIds.HasSubtype, NodeIds.BaseEventType.expanded(), false));
    nodeManager.addNode(customEventType);

    NodeId eventNodeId = new NodeId(1, testPrefix + ":CustomEvent");

    assertThrows(
        ClassCastException.class, () -> eventFactory.createEvent(eventNodeId, customTypeId));

    // Residue: the root instance (a plain UaObjectNode) and its members were stored before the
    // cast failed, and remain in the private NodeManager.
    UaNode residue = privateNodeManager.getNode(eventNodeId).orElse(null);
    assertNotNull(residue, "nodes created before the failed cast are left behind");
    assertInstanceOf(UaObjectNode.class, residue);
    assertFalse(residue instanceof BaseEventTypeNode);
    assertTrue(
        privateNodeManager.containsNode(new NodeId(1, testPrefix + ":CustomEvent/0:Message")),
        "inherited BaseEventType members were created and left behind too");
  }
}
