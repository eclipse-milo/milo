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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OpcUaMonitoredItemTest extends AbstractClientServerTest {

  private OpcUaSubscription subscription;

  @BeforeEach
  void setUp() throws UaException {
    subscription = new OpcUaSubscription(client);
    subscription.create();
  }

  @AfterEach
  void tearDown() throws UaException {
    subscription.delete();
  }

  @Test
  void createMonitoredItem() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    subscription.addMonitoredItem(monitoredItem);
    List<MonitoredItemServiceOperationResult> created = subscription.createMonitoredItems();

    assertEquals(1, created.size());
    assertEquals(monitoredItem, created.get(0).monitoredItem());
    assertTrue(created.get(0).serviceResult().isGood());
    assertTrue(created.get(0).operationResult().orElseThrow().isGood());
    assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, monitoredItem.getSyncState());
  }

  @Test
  void createMonitoredItemsIgnoresDataEncoding() {
    NodeId nodeId = new NodeId(2, "TestInt32");

    var valueItem =
        new OpcUaMonitoredItem(
            new ReadValueId(
                nodeId, AttributeId.Value.uid(), null, new QualifiedName(9999, "Default Binary")));
    var browseNameItem =
        new OpcUaMonitoredItem(
            new ReadValueId(
                nodeId,
                AttributeId.BrowseName.uid(),
                null,
                new QualifiedName(0, "Default Binary")));

    subscription.addMonitoredItem(valueItem);
    subscription.addMonitoredItem(browseNameItem);
    List<MonitoredItemServiceOperationResult> created = subscription.createMonitoredItems();

    assertEquals(2, created.size());
    assertTrue(created.get(0).serviceResult().isGood());
    assertTrue(created.get(0).operationResult().orElseThrow().isGood());
    assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, valueItem.getSyncState());
    assertTrue(created.get(1).serviceResult().isGood());
    assertTrue(created.get(1).operationResult().orElseThrow().isGood());
    assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, browseNameItem.getSyncState());
  }

  @Test
  void createMonitoredItemFiltered() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeId.parse("ns=9999;s=DoesNotExist"));

    subscription.addMonitoredItem(monitoredItem);
    List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();
    assertTrue(results.get(0).operationResult().orElseThrow().isBad());

    List<MonitoredItemServiceOperationResult> results2 =
        subscription.createMonitoredItems(item -> item.getCreateResult().isEmpty());

    assertTrue(results2.isEmpty());
  }

  @Test
  void deleteMonitoredItem() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    subscription.addMonitoredItem(monitoredItem);
    List<MonitoredItemServiceOperationResult> created = subscription.createMonitoredItems();
    assertEquals(1, created.size());
    assertEquals(monitoredItem, created.get(0).monitoredItem());
    assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, monitoredItem.getSyncState());

    subscription.removeMonitoredItem(monitoredItem);
    List<MonitoredItemServiceOperationResult> deleted = subscription.deleteMonitoredItems();
    assertEquals(1, deleted.size());
    assertEquals(monitoredItem, deleted.get(0).monitoredItem());
    assertTrue(deleted.get(0).serviceResult().isGood());
    assertTrue(deleted.get(0).operationResult().orElseThrow().isGood());
    assertEquals(OpcUaMonitoredItem.SyncState.INITIAL, monitoredItem.getSyncState());
  }

  @Test
  void deleteMonitoredItemsHandlesClearedItemServerState() {
    var validItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    var resetItem = new ResetOnMonitoredItemIdRead();

    subscription.addMonitoredItems(List.of(validItem, resetItem));
    subscription.createMonitoredItems();

    subscription.removeMonitoredItems(List.of(validItem, resetItem));
    resetItem.resetOnNextMonitoredItemIdRead();

    List<MonitoredItemServiceOperationResult> results = subscription.deleteMonitoredItems();

    assertEquals(2, results.size());

    MonitoredItemServiceOperationResult validResult =
        results.stream()
            .filter(result -> result.monitoredItem() == validItem)
            .findFirst()
            .orElseThrow();
    assertEquals(StatusCode.GOOD, validResult.serviceResult());
    assertTrue(validResult.operationResult().orElseThrow().isGood());

    MonitoredItemServiceOperationResult resetResult =
        results.stream()
            .filter(result -> result.monitoredItem() == resetItem)
            .findFirst()
            .orElseThrow();
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), resetResult.serviceResult());
    assertTrue(resetResult.operationResult().isEmpty());
  }

  @Test
  void setMonitoringModeHandlesClearedItemServerState() {
    var validItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    var resetBeforeCallItem =
        OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    var resetOnReadItem = new ResetOnMonitoredItemIdRead();

    subscription.addMonitoredItems(List.of(validItem, resetBeforeCallItem, resetOnReadItem));
    subscription.createMonitoredItems();

    resetBeforeCallItem.reset();
    resetOnReadItem.resetOnNextMonitoredItemIdRead();

    List<MonitoredItemServiceOperationResult> results =
        subscription.setMonitoringMode(
            MonitoringMode.Disabled, List.of(validItem, resetBeforeCallItem, resetOnReadItem));

    assertEquals(3, results.size());

    MonitoredItemServiceOperationResult validResult = results.get(0);
    assertEquals(validItem, validResult.monitoredItem());
    assertEquals(StatusCode.GOOD, validResult.serviceResult());
    assertTrue(validResult.operationResult().orElseThrow().isGood());
    assertEquals(MonitoringMode.Disabled, validItem.getMonitoringMode());

    MonitoredItemServiceOperationResult resetBeforeCallResult = results.get(1);
    assertEquals(resetBeforeCallItem, resetBeforeCallResult.monitoredItem());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidState), resetBeforeCallResult.serviceResult());
    assertTrue(resetBeforeCallResult.operationResult().isEmpty());
    assertEquals(MonitoringMode.Reporting, resetBeforeCallItem.getMonitoringMode());

    MonitoredItemServiceOperationResult resetOnReadResult = results.get(2);
    assertEquals(resetOnReadItem, resetOnReadResult.monitoredItem());
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), resetOnReadResult.serviceResult());
    assertTrue(resetOnReadResult.operationResult().isEmpty());
    assertEquals(MonitoringMode.Reporting, resetOnReadItem.getMonitoringMode());
  }

  @Test
  void setMonitoringModeReturnsInvalidStateForNeverCreatedItem() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    List<MonitoredItemServiceOperationResult> results =
        subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(monitoredItem));

    assertEquals(1, results.size());
    assertEquals(monitoredItem, results.get(0).monitoredItem());
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), results.get(0).serviceResult());
    assertTrue(results.get(0).operationResult().isEmpty());
  }

  @Test
  void modifyMonitoredItem_SamplingInterval() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    subscription.addMonitoredItem(monitoredItem);
    subscription.createMonitoredItems();

    monitoredItem.setSamplingInterval(5000.0);
    List<MonitoredItemServiceOperationResult> modified = subscription.modifyMonitoredItems();

    assertEquals(1, modified.size());
    assertEquals(monitoredItem, modified.get(0).monitoredItem());
    assertTrue(modified.get(0).serviceResult().isGood());
    assertTrue(modified.get(0).operationResult().orElseThrow().isGood());
    assertEquals(5000.0, monitoredItem.getSamplingInterval());
    assertEquals(5000.0, monitoredItem.getRevisedSamplingInterval().orElseThrow());
  }

  @Test
  void modifyMonitoredItem_QueueSize() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    subscription.addMonitoredItem(monitoredItem);
    subscription.createMonitoredItems();

    monitoredItem.setQueueSize(uint(10));
    List<MonitoredItemServiceOperationResult> modified = subscription.modifyMonitoredItems();

    assertEquals(1, modified.size());
    assertEquals(monitoredItem, modified.get(0).monitoredItem());
    assertTrue(modified.get(0).serviceResult().isGood());
    assertTrue(modified.get(0).operationResult().orElseThrow().isGood());
    assertEquals(uint(10), monitoredItem.getQueueSize());
    assertEquals(uint(10), monitoredItem.getRevisedQueueSize().orElseThrow());
  }

  @Test
  void modifyMonitoredItem_DiscardOldest() {
    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    subscription.addMonitoredItem(monitoredItem);
    subscription.createMonitoredItems();

    monitoredItem.setDiscardOldest(false);
    List<MonitoredItemServiceOperationResult> modified = subscription.modifyMonitoredItems();

    assertEquals(1, modified.size());
    assertTrue(modified.get(0).serviceResult().isGood());
    assertTrue(modified.get(0).operationResult().orElseThrow().isGood());
    assertEquals(monitoredItem, modified.get(0).monitoredItem());
    assertFalse(monitoredItem.getDiscardOldest());
  }

  @Test
  void synchronizeMonitoredItems() throws MonitoredItemSynchronizationException {
    var monitoredItems = new LinkedList<OpcUaMonitoredItem>();

    // Create 10 and expect 10 affected during synchronization
    for (int i = 0; i < 10; i++) {
      var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

      subscription.addMonitoredItem(monitoredItem);

      monitoredItems.add(monitoredItem);
    }
    subscription.synchronizeMonitoredItems();

    // Modify 2 and expect 2 affected during synchronization
    monitoredItems.get(0).setSamplingInterval(100.0);
    monitoredItems.get(1).setSamplingInterval(100.0);
    subscription.synchronizeMonitoredItems();

    // Delete 2, modify 2, and create 3. Expect 7 affected during synchronization
    subscription.removeMonitoredItem(monitoredItems.removeLast());
    subscription.removeMonitoredItem(monitoredItems.removeLast());
    monitoredItems.get(0).setSamplingInterval(1000.0);
    monitoredItems.get(1).setSamplingInterval(1000.0);
    for (int i = 0; i < 3; i++) {
      var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

      subscription.addMonitoredItem(monitoredItem);

      monitoredItems.add(monitoredItem);
    }
    subscription.synchronizeMonitoredItems();

    // Nothing changed, expect 0 affected during synchronization
    subscription.synchronizeMonitoredItems();

    // Remove the same item 3 times, expect only 1 affected during synchronization
    OpcUaMonitoredItem toRemove = monitoredItems.removeLast();
    subscription.removeMonitoredItem(toRemove);
    subscription.removeMonitoredItem(toRemove);
    subscription.removeMonitoredItem(toRemove);
    subscription.synchronizeMonitoredItems();
  }

  @Test
  void createEventItemWithMonitoringMode() {
    var monitoredItem =
        OpcUaMonitoredItem.newEventItem(NodeIds.Server, MonitoringMode.Sampling, null);

    // Verify the MonitoringMode is set correctly
    assertEquals(MonitoringMode.Sampling, monitoredItem.getMonitoringMode());
    // Verify the sampling interval is set to 0.0 for event items
    assertEquals(0.0, monitoredItem.getSamplingInterval());
    // Verify the filter is null as specified
    assertNull(monitoredItem.getFilter());
  }

  private static final class ResetOnMonitoredItemIdRead extends OpcUaMonitoredItem {

    private boolean resetOnNextMonitoredItemIdRead;

    private ResetOnMonitoredItemIdRead() {
      super(
          new ReadValueId(
              NodeIds.Server_ServerStatus_CurrentTime,
              AttributeId.Value.uid(),
              null,
              QualifiedName.NULL_VALUE));
    }

    private void resetOnNextMonitoredItemIdRead() {
      resetOnNextMonitoredItemIdRead = true;
    }

    @Override
    public Optional<UInteger> getMonitoredItemId() {
      if (resetOnNextMonitoredItemIdRead) {
        resetOnNextMonitoredItemIdRead = false;
        reset();
      }

      return super.getMonitoredItemId();
    }
  }
}
