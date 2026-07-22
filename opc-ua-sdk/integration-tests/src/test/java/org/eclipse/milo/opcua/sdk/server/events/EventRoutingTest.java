/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemServiceOperationResult;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for SDK-owned event item registration, notifier-hierarchy-scoped delivery, and {@link
 * TransientEvent} lifecycle.
 *
 * <p>The test namespace has no {@code onEventItems*} code; registration must be SDK-owned for any
 * of these tests to pass.
 */
public class EventRoutingTest extends AbstractClientServerTest {

  /** Severity used by all events fired by this test; the where clause matches on it. */
  private static final int TEST_SEVERITY = 42;

  private @Nullable UaObjectNode areaA;
  private @Nullable UaObjectNode areaB;
  private @Nullable UaObjectNode machineA;
  private @Nullable UaObjectNode machineB;

  private OpcUaSubscription subscription;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    areaA = addObjectNode(namespace, "EventRoutingTest/AreaA");
    areaB = addObjectNode(namespace, "EventRoutingTest/AreaB");
    machineA = addObjectNode(namespace, "EventRoutingTest/MachineA");
    machineB = addObjectNode(namespace, "EventRoutingTest/MachineB");

    UaNodeManager nodeManager = namespace.getNodeManager();

    nodeManager.addReferences(
        new Reference(
            areaA.getNodeId(), NodeIds.HasEventSource, machineA.getNodeId().expanded(), true),
        server.getNamespaceTable());

    nodeManager.addReferences(
        new Reference(
            areaB.getNodeId(), NodeIds.HasEventSource, machineB.getNodeId().expanded(), true),
        server.getNamespaceTable());

    namespace.registerEventNotifier(areaA);
    namespace.registerEventNotifier(areaB);
  }

  private UaObjectNode addObjectNode(TestNamespace namespace, String name) {
    var node =
        new UaObjectNode(
            namespace.getNodeContext(),
            newNodeId(name),
            newQualifiedName(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            ubyte(0));

    node.addReference(
        new Reference(
            node.getNodeId(), NodeIds.HasTypeDefinition, NodeIds.BaseObjectType.expanded(), true));

    node.addReference(
        new Reference(
            node.getNodeId(),
            NodeIds.HasComponent,
            NodeIds.ObjectsFolder.expanded(),
            Reference.Direction.INVERSE));

    namespace.getNodeManager().addNode(node);

    return node;
  }

  @BeforeEach
  void setUp() throws Exception {
    subscription = new OpcUaSubscription(client);
    subscription.create();
  }

  @AfterEach
  void tearDown() throws Exception {
    subscription.delete();
  }

  @Test
  void eventItemOnManagedNamespaceAreaReceivesSubtreeEvents() throws Exception {
    List<Variant[]> areaEvents = monitorEvents(requireNonNull(areaA).getNodeId());

    fireEvent(requireNonNull(machineA).getNodeId(), "subtree-delivery");

    Variant[] eventFields = awaitEvent(areaEvents, "subtree-delivery");
    assertEquals(ushort(TEST_SEVERITY), eventFields[1].value());
  }

  @Test
  void deliveryIsScopedToNotifierHierarchy() throws Exception {
    List<Variant[]> areaAEvents = monitorEvents(requireNonNull(areaA).getNodeId());
    List<Variant[]> areaBEvents = monitorEvents(requireNonNull(areaB).getNodeId());
    List<Variant[]> serverEvents = monitorEvents(NodeIds.Server);

    fireEvent(requireNonNull(machineA).getNodeId(), "scoped-to-a");
    fireEvent(requireNonNull(machineB).getNodeId(), "scoped-to-b");

    awaitEvent(areaAEvents, "scoped-to-a");
    awaitEvent(areaBEvents, "scoped-to-b");
    awaitEvent(serverEvents, "scoped-to-a");
    awaitEvent(serverEvents, "scoped-to-b");

    assertNoEvent(areaAEvents, "scoped-to-b");
    assertNoEvent(areaBEvents, "scoped-to-a");
  }

  @Test
  void unwiredSourceReachesOnlyServerItem() throws Exception {
    List<Variant[]> areaAEvents = monitorEvents(requireNonNull(areaA).getNodeId());
    List<Variant[]> serverEvents = monitorEvents(NodeIds.Server);

    fireEvent(newNodeId("EventRoutingTest/Unwired"), "unwired-source");

    awaitEvent(serverEvents, "unwired-source");
    assertNoEvent(areaAEvents, "unwired-source");
  }

  @Test
  void monitoringModeTogglesDelivery() throws Exception {
    var events = new CopyOnWriteArrayList<Variant[]>();
    OpcUaMonitoredItem item = createEventItem(requireNonNull(areaA).getNodeId(), events);

    fireEvent(requireNonNull(machineA).getNodeId(), "before-disable");
    awaitEvent(events, "before-disable");

    setMonitoringMode(item, MonitoringMode.Disabled);

    fireEvent(machineA.getNodeId(), "while-disabled");
    assertNoEvent(events, "while-disabled");

    setMonitoringMode(item, MonitoringMode.Reporting);

    fireEvent(machineA.getNodeId(), "after-enable");
    awaitEvent(events, "after-enable");
    assertNoEvent(events, "while-disabled");
  }

  @Test
  void transientEventNodesAreDeletedOnClose() throws Exception {
    List<Variant[]> serverEvents = monitorEvents(NodeIds.Server);

    UaNodeManager transientNodeManager;

    try (TransientEvent event = server.newEvent(NodeIds.BaseEventType)) {
      transientNodeManager = (UaNodeManager) event.getNode().getNodeManager();
      assertFalse(transientNodeManager.getNodes().isEmpty());

      populateEvent(event.getNode(), NodeIds.Server, "transient-lifecycle");
      event.fire();
    }

    // Field extraction resolved Message/Severity against the EventInstantiator's private tree.
    Variant[] eventFields = awaitEvent(serverEvents, "transient-lifecycle");
    assertEquals(ushort(TEST_SEVERITY), eventFields[1].value());

    assertTrue(transientNodeManager.getNodes().isEmpty());
  }

  @Test
  void transientEventFireAfterCloseThrows() throws Exception {
    TransientEvent event = server.newEvent(NodeIds.BaseEventType);
    populateEvent(event.getNode(), NodeIds.Server, "fire-after-close");

    event.close();
    event.close(); // double-close is a no-op

    assertThrows(IllegalStateException.class, event::fire);
  }

  private List<Variant[]> monitorEvents(NodeId nodeId) throws Exception {
    return EventTestSupport.monitorEvents(subscription, nodeId, eventFilter());
  }

  private OpcUaMonitoredItem createEventItem(NodeId nodeId, List<Variant[]> events)
      throws Exception {
    return EventTestSupport.createEventItem(subscription, nodeId, eventFilter(), events);
  }

  private void setMonitoringMode(OpcUaMonitoredItem item, MonitoringMode monitoringMode) {
    List<MonitoredItemServiceOperationResult> results =
        subscription.setMonitoringMode(monitoringMode, List.of(item));

    assertTrue(results.get(0).isGood());
  }

  private void fireEvent(NodeId sourceNodeId, String message) throws UaException {
    try (TransientEvent event = server.newEvent(NodeIds.BaseEventType)) {
      populateEvent(event.getNode(), sourceNodeId, message);
      event.fire();
    }
  }

  private void populateEvent(BaseEventTypeNode eventNode, NodeId sourceNodeId, String message) {
    eventNode.setBrowseName(newQualifiedName("EventRoutingTest"));
    eventNode.setDisplayName(LocalizedText.english("EventRoutingTest"));
    eventNode.setSourceNode(sourceNodeId);
    eventNode.setSourceName("EventRoutingTest");
    eventNode.setMessage(LocalizedText.english(message));
    eventNode.setSeverity(ushort(TEST_SEVERITY));
  }

  /** Wait until an event with Message {@code message} arrives, returning its fields. */
  private static Variant[] awaitEvent(List<Variant[]> events, String message)
      throws InterruptedException {
    return EventTestSupport.awaitEvent(
        events, "message \"" + message + "\"", eventFields -> messageMatches(eventFields, message));
  }

  /** Assert an event with Message {@code message} does not arrive within a settle period. */
  private static void assertNoEvent(List<Variant[]> events, String message)
      throws InterruptedException {
    EventTestSupport.assertNoEvent(
        events, "message \"" + message + "\"", eventFields -> messageMatches(eventFields, message));
  }

  private static boolean messageMatches(Variant[] eventFields, String message) {
    return eventFields[0].value() instanceof LocalizedText text && message.equals(text.text());
  }

  /** Select Message and Severity; match only events with Severity == {@link #TEST_SEVERITY}. */
  private static EventFilter eventFilter() {
    return EventTestSupport.severityEventFilter(
        TEST_SEVERITY,
        EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
        EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"));
  }
}
