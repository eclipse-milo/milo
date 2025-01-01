/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription.SyncState;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.junit.jupiter.api.Test;

public class OpcUaSubscriptionTest extends AbstractClientServerTest {

  @Test
  void createSubscription() throws UaException {
    var subscription = new OpcUaSubscription(client);

    subscription.create();

    try {
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      assertTrue(subscription.getSubscriptionId().isPresent());
      assertTrue(subscription.getRevisedLifetimeCount().isPresent());
      assertTrue(subscription.getRevisedMaxKeepAliveCount().isPresent());
      assertTrue(subscription.getRevisedPublishingInterval().isPresent());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void deleteSubscription() throws UaException {
    var subscription = new OpcUaSubscription(client);

    assertEquals(SyncState.INITIAL, subscription.getSyncState());
    subscription.delete(); // no-op if in INITIAL state
    assertEquals(SyncState.INITIAL, subscription.getSyncState());

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void createDeleteSubscriptionAsync() throws ExecutionException, InterruptedException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.createAsync().toCompletableFuture().get();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
    } finally {
      subscription.deleteAsync().toCompletableFuture().get();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyPublishingInterval() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      Double newPublishingInterval =
          subscription.getRevisedPublishingInterval().map(v -> v * 2.0).orElseThrow();
      subscription.setPublishingInterval(newPublishingInterval);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      subscription.modify();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(newPublishingInterval, subscription.getPublishingInterval());
      assertEquals(newPublishingInterval, subscription.getRevisedPublishingInterval().get());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyLifetimeCount() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      UInteger newLifetimeCount =
          subscription
              .getRevisedLifetimeCount()
              .map(v -> v.add(UInteger.valueOf(10)))
              .orElseThrow();
      subscription.setLifetimeCount(newLifetimeCount);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      subscription.modify();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(newLifetimeCount, subscription.getLifetimeCount());
      assertEquals(newLifetimeCount, subscription.getRevisedLifetimeCount().get());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyMaxKeepAliveCount() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      UInteger newMaxKeepAliveCount =
          subscription
              .getRevisedMaxKeepAliveCount()
              .map(v -> v.add(UInteger.valueOf(10)))
              .orElseThrow();
      subscription.setMaxKeepAliveCount(newMaxKeepAliveCount);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      subscription.modify();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(newMaxKeepAliveCount, subscription.getMaxKeepAliveCount());
      assertEquals(newMaxKeepAliveCount, subscription.getRevisedMaxKeepAliveCount().get());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyMaxNotificationsPerPublish() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      UInteger newMaxNotificationsPerPublish =
          subscription.getMaxNotificationsPerPublish().add(UInteger.valueOf(10));
      subscription.setMaxNotificationsPerPublish(newMaxNotificationsPerPublish);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      subscription.modify();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(newMaxNotificationsPerPublish, subscription.getMaxNotificationsPerPublish());
      assertEquals(
          newMaxNotificationsPerPublish,
          subscription.getServerState().orElseThrow().getMaxNotificationsPerPublish());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyPriority() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      UByte newPriority = subscription.getPriority().add(UByte.valueOf(1));
      subscription.setPriority(newPriority);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      subscription.modify();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(newPriority, subscription.getPriority());
      assertEquals(newPriority, subscription.getServerState().orElseThrow().getPriority());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void modifyAsync() throws UaException, ExecutionException, InterruptedException {
    var subscription = new OpcUaSubscription(client);

    subscription.create();
    subscription.setPublishingInterval(100.0);
    subscription.modifyAsync().toCompletableFuture().get();

    assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
    assertEquals(100.0, subscription.getPublishingInterval());
    assertEquals(100.0, subscription.getRevisedPublishingInterval().orElseThrow());

    subscription.delete();
  }

  @Test
  void lifetimeAndKeepAliveCalculation() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      // lifetime and keep alive are calculated
      {
        UInteger previousRequestedLifetimeCount = subscription.getLifetimeCount();
        UInteger previousMaxKeepAliveCount = subscription.getMaxKeepAliveCount();

        subscription.setPublishingInterval(60_000.0);

        subscription.modify();
        assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
        assertNotEquals(previousRequestedLifetimeCount, subscription.getLifetimeCount());
        assertNotEquals(previousMaxKeepAliveCount, subscription.getMaxKeepAliveCount());

        UInteger revisedLifetimeCount = subscription.getRevisedLifetimeCount().orElseThrow();
        UInteger revisedMaxKeepAliveCount =
            subscription.getRevisedMaxKeepAliveCount().orElseThrow();
        assertEquals(revisedLifetimeCount, subscription.getLifetimeCount());
        assertEquals(revisedMaxKeepAliveCount, subscription.getMaxKeepAliveCount());
      }

      // lifetime and keep alive are not calculated
      {
        subscription.setLifetimeAndKeepAliveCalculated(false);
        assertFalse(subscription.isLifetimeAndKeepAliveCalculated());

        UInteger requestedLifetimeCount = subscription.getLifetimeCount();
        UInteger requestedMaxKeepAliveCount = subscription.getMaxKeepAliveCount();
        subscription.setPublishingInterval(1000.0);

        subscription.modify();
        assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
        assertEquals(requestedLifetimeCount, subscription.getLifetimeCount());
        assertEquals(requestedMaxKeepAliveCount, subscription.getMaxKeepAliveCount());
      }
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void setMonitoringMode() throws UaException {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    for (int i = 0; i < 3; i++) {
      var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(monitoredItem);
    }

    subscription.synchronizeMonitoredItems();

    Consumer<List<MonitoredItemServiceOperationResult>> assertGood =
        results -> {
          assertEquals(3, results.size());
          assertTrue(results.stream().allMatch(r -> r.serviceResult().isGood()));
          assertTrue(results.stream().allMatch(r -> r.operationResult().orElseThrow().isGood()));
        };

    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Disabled, subscription.getMonitoredItems()));
    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Sampling, subscription.getMonitoredItems()));
    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Reporting, subscription.getMonitoredItems()));

    subscription.setMaxMonitoredItemsPerCall(uint(2));

    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Disabled, subscription.getMonitoredItems()));
    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Sampling, subscription.getMonitoredItems()));
    assertGood.accept(
        subscription.setMonitoringMode(MonitoringMode.Reporting, subscription.getMonitoredItems()));

    subscription.delete();
  }

  @Test
  void setPublishingMode() throws UaException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      subscription.setPublishingMode(false);
      assertFalse(subscription.isPublishingEnabled().orElseThrow());

      subscription.setPublishingMode(true);
      assertTrue(subscription.isPublishingEnabled().orElseThrow());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void setPublishingModeAsync() throws UaException, ExecutionException, InterruptedException {
    var subscription = new OpcUaSubscription(client);

    try {
      subscription.create();
      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());

      subscription.setPublishingModeAsync(false).toCompletableFuture().get();
      assertFalse(subscription.isPublishingEnabled().orElseThrow());

      subscription.setPublishingModeAsync(true).toCompletableFuture().get();
      assertTrue(subscription.isPublishingEnabled().orElseThrow());
    } finally {
      subscription.delete();
      assertEquals(SyncState.INITIAL, subscription.getSyncState());
    }
  }

  @Test
  void maxMonitoredItemsPerCall() throws UaException {
    var subscription = new OpcUaSubscription(client);
    subscription.setMaxMonitoredItemsPerCall(uint(3));
    subscription.create();

    var monitoredItems = new ArrayList<OpcUaMonitoredItem>();

    for (int i = 0; i < 10; i++) {
      var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      monitoredItems.add(monitoredItem);
    }

    subscription.addMonitoredItems(monitoredItems);

    List<MonitoredItemServiceOperationResult> createResults = subscription.createMonitoredItems();
    assertEquals(10, createResults.size());
    assertTrue(createResults.stream().allMatch(r -> r.serviceResult().isGood()));
    assertTrue(createResults.stream().allMatch(r -> r.operationResult().orElseThrow().isGood()));

    subscription
        .getMonitoredItems()
        .forEach(
            item -> {
              item.setSamplingInterval(100.0);
              item.setQueueSize(uint(2));
            });

    List<MonitoredItemServiceOperationResult> modifyResults = subscription.modifyMonitoredItems();
    assertEquals(10, modifyResults.size());
    assertTrue(modifyResults.stream().allMatch(r -> r.serviceResult().isGood()));
    assertTrue(modifyResults.stream().allMatch(r -> r.operationResult().orElseThrow().isGood()));

    subscription.removeMonitoredItems(monitoredItems);
    List<MonitoredItemServiceOperationResult> deleteResults = subscription.deleteMonitoredItems();
    assertEquals(10, deleteResults.size());
    assertTrue(deleteResults.stream().allMatch(r -> r.serviceResult().isGood()));
    assertTrue(deleteResults.stream().allMatch(r -> r.operationResult().orElseThrow().isGood()));

    subscription.delete();
  }
}
