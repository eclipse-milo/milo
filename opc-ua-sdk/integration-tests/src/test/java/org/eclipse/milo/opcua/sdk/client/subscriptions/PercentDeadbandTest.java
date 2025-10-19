/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PercentDeadbandTest extends AbstractClientServerTest {

  private static final String NODE_ID_RANGE_100 = "PercentDeadbandTest_Range100";
  private static final String NODE_ID_RANGE_1000 = "PercentDeadbandTest_Range1000";
  private static final String NODE_ID_NEGATIVE_RANGE = "PercentDeadbandTest_NegativeRange";

  private OpcUaSubscription subscription;

  @BeforeEach
  void setUp() throws Exception {
    subscription = new OpcUaSubscription(client);
    subscription.create();
    resetNodeValues();
  }

  @AfterEach
  void tearDown() throws UaException {
    subscription.delete();
  }

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    try {
      createAnalogItemNode(namespace, NODE_ID_RANGE_100, new Range(0.0, 100.0), 50.0);

      createAnalogItemNode(namespace, NODE_ID_RANGE_1000, new Range(0.0, 1000.0), 500.0);

      createAnalogItemNode(namespace, NODE_ID_NEGATIVE_RANGE, new Range(-50.0, 50.0), 0.0);
    } catch (UaException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testPercentDeadband_ExistingTestAnalogValue() throws Exception {
    // Test with the existing TestAnalogValue node from TestNamespace
    var nodeId = new NodeId(testNamespace.getNamespaceIndex(), "TestAnalogValue");

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);

    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
  }

  @Test
  void testPercentDeadband_WithinDeadband_NoNotification() throws Exception {
    NodeId nodeId = newNodeId(NODE_ID_RANGE_100);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Write a value within deadband (9% change from 50.0 to 59.0)
    // Range is 100, so 10% deadband = 10 units
    var latch2 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch2.countDown();
        });

    AddressSpace addressSpace = client.getAddressSpace();
    UaVariableNode variableNode = (UaVariableNode) addressSpace.getNode(nodeId);
    variableNode.writeValue(new Variant(59.0));

    // Wait a bit to ensure no notification is sent
    boolean notified = latch2.await(1, TimeUnit.SECONDS);

    assertFalse(notified, "Should not receive notification for change within deadband");
    assertEquals(
        0, receivedValues.size(), "Should not receive notification for change within deadband");
  }

  @Test
  void testPercentDeadband_ExceedsDeadband_Notification() throws Exception {
    NodeId nodeId = newNodeId(NODE_ID_RANGE_100);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Write a value exceeding deadband (11% change from 50.0 to 61.0)
    var latch2 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch2.countDown();
        });

    AddressSpace addressSpace = client.getAddressSpace();
    UaVariableNode variableNode = (UaVariableNode) addressSpace.getNode(nodeId);
    variableNode.writeValue(new Variant(61.0));

    // Wait for notification
    assertTrue(
        latch2.await(5, TimeUnit.SECONDS),
        "Should receive notification for change exceeding deadband");
    assertEquals(1, receivedValues.size());
    assertEquals(61.0, receivedValues.get(0).value().value());
  }

  @Test
  void testPercentDeadband_ExactBoundary_NoNotification() throws Exception {
    NodeId nodeId = newNodeId(NODE_ID_RANGE_100);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Write a value exactly at deadband boundary (10% change from 50.0 to 60.0)
    var latch2 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch2.countDown();
        });

    AddressSpace addressSpace = client.getAddressSpace();
    UaVariableNode variableNode = (UaVariableNode) addressSpace.getNode(nodeId);
    variableNode.writeValue(new Variant(60.0));

    // Wait a bit to ensure no notification is sent
    boolean notified = latch2.await(1, TimeUnit.SECONDS);

    assertFalse(
        notified, "Should not receive notification for change exactly at deadband boundary");
    assertEquals(
        0,
        receivedValues.size(),
        "Should not receive notification for change exactly at deadband boundary");
  }

  @Test
  void testPercentDeadband_DifferentRanges() throws Exception {
    NodeId nodeId = newNodeId(NODE_ID_RANGE_1000);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 5.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Test within deadband: 5% of 1000 = 50 units
    // Change from 500.0 to 549.0 is 49 units = 4.9%
    var latch2 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch2.countDown();
        });

    AddressSpace addressSpace = client.getAddressSpace();
    UaVariableNode variableNode = (UaVariableNode) addressSpace.getNode(nodeId);
    variableNode.writeValue(new Variant(549.0));

    boolean notified = latch2.await(1, TimeUnit.SECONDS);
    assertFalse(notified, "Should not receive notification for 4.9% change");
    assertEquals(0, receivedValues.size(), "Should not receive notification for 4.9% change");

    // Test exceeding deadband: change from 549.0 to 600.0 = 51 units from original 500.0 = 5.1%
    // But we need to exceed from last sent value (500.0)
    receivedValues.clear();
    var latch3 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch3.countDown();
        });

    variableNode.writeValue(new Variant(551.0));

    assertTrue(latch3.await(5, TimeUnit.SECONDS), "Should receive notification for 5.1% change");
    assertEquals(1, receivedValues.size());
    assertEquals(551.0, receivedValues.get(0).value().value());
  }

  @Test
  void testPercentDeadband_MultipleChanges() throws Exception {
    NodeId nodeId = newNodeId(NODE_ID_NEGATIVE_RANGE);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);
    monitoredItem.setFilter(filter);

    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for initial value
    assertTrue(latch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    assertEquals(0.0, receivedValues.get(0).value().value());
    receivedValues.clear();

    // Range is -50 to 50, total span = 100
    // 10% deadband = 10 units
    // Initial value is 0.0

    AddressSpace addressSpace = client.getAddressSpace();
    UaVariableNode variableNode = (UaVariableNode) addressSpace.getNode(nodeId);

    // Change 1: within deadband (9 units)
    variableNode.writeValue(new Variant(9.0));
    Thread.sleep(500);
    assertEquals(0, receivedValues.size(), "Change 1 should not trigger notification");

    // Change 2: exceeds deadband (11 units from original 0.0)
    var latch2 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch2.countDown();
        });

    variableNode.writeValue(new Variant(11.0));
    assertTrue(latch2.await(5, TimeUnit.SECONDS), "Change 2 should trigger notification");
    assertEquals(1, receivedValues.size());
    assertEquals(11.0, receivedValues.get(0).value().value());
    receivedValues.clear();

    // Change 3: within deadband from new baseline (11.0 to 20.0 = 9 units)
    variableNode.writeValue(new Variant(20.0));
    Thread.sleep(500);
    assertEquals(0, receivedValues.size(), "Change 3 should not trigger notification");

    // Change 4: exceeds deadband from baseline (11.0 to 22.0 = 11 units)
    var latch3 = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          latch3.countDown();
        });

    variableNode.writeValue(new Variant(22.0));
    assertTrue(latch3.await(5, TimeUnit.SECONDS), "Change 4 should trigger notification");
    assertEquals(1, receivedValues.size());
    assertEquals(22.0, receivedValues.get(0).value().value());
  }

  private void createAnalogItemNode(
      TestNamespace namespace, String identifier, Range euRange, double initialValue)
      throws UaException {

    var nodeId = new NodeId(namespace.getNamespaceIndex(), identifier);

    AnalogItemTypeNode node =
        (AnalogItemTypeNode)
            namespace
                .getNodeFactory()
                .createNode(
                    nodeId,
                    NodeIds.AnalogItemType,
                    new NodeFactory.InstantiationCallback() {
                      @Override
                      public boolean includeOptionalNode(
                          NodeId typeDefinitionId, QualifiedName browseName) {
                        return true;
                      }
                    });

    node.setBrowseName(new QualifiedName(namespace.getNamespaceIndex(), identifier));
    node.setDisplayName(LocalizedText.english(identifier));
    node.setDataType(NodeIds.Double);
    node.setValue(new DataValue(new Variant(initialValue)));
    node.setAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    node.setEuRange(euRange);

    node.addReference(
        new Reference(
            node.getNodeId(),
            NodeIds.HasComponent,
            NodeIds.ObjectsFolder.expanded(),
            Reference.Direction.INVERSE));

    namespace.getNodeManager().addNode(node);
  }

  private void resetNodeValues() throws Exception {
    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode node1 = (UaVariableNode) addressSpace.getNode(newNodeId(NODE_ID_RANGE_100));
    node1.writeValue(new Variant(50.0));

    UaVariableNode node2 = (UaVariableNode) addressSpace.getNode(newNodeId(NODE_ID_RANGE_1000));
    node2.writeValue(new Variant(500.0));

    UaVariableNode node3 = (UaVariableNode) addressSpace.getNode(newNodeId(NODE_ID_NEGATIVE_RANGE));
    node3.writeValue(new Variant(0.0));
  }
}
