/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Verifies that parameter changes made to an {@link OpcUaMonitoredItem} while its
 * CreateMonitoredItems or ModifyMonitoredItems service call is in flight are not silently dropped
 * when the Good result is applied, and are sent by the next {@link
 * OpcUaSubscription#synchronizeMonitoredItems()} pass.
 */
public class MonitoredItemInFlightModificationTest {

  private static final long TIMEOUT_SECONDS = 10;

  /**
   * Changing samplingInterval, queueSize, discardOldest, and filter while ModifyMonitoredItems is
   * in flight must leave the item UNSYNCHRONIZED so a follow-up synchronizeMonitoredItems() sends
   * the latest values to the Server.
   */
  @Test
  void parameterChangesDuringInFlightModifyAreSentOnNextSynchronization() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      item.setSamplingInterval(250.0);
      item.setQueueSize(uint(2));

      Hold hold = fixture.serviceSet.holdNextModify();
      CompletableFuture<Void> firstSync = synchronizeAsync(subscription);
      hold.awaitArrived();

      // Update all four modifiable parameters while the first ModifyMonitoredItems is in flight.
      var filter =
          new DataChangeFilter(
              DataChangeTrigger.StatusValueTimestamp, uint(DeadbandType.None.getValue()), 0.0);
      item.setSamplingInterval(500.0);
      item.setQueueSize(uint(5));
      item.setDiscardOldest(false);
      item.setFilter(filter);

      hold.release();
      firstSync.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertEquals(
          SyncState.UNSYNCHRONIZED,
          item.getSyncState(),
          "parameter changes made during an in-flight ModifyMonitoredItems must not be dropped");
      assertFalse(subscription.isMonitoredItemsSynchronized());
      assertEquals(250.0, item.getRevisedSamplingInterval().orElseThrow());
      assertEquals(uint(2), item.getRevisedQueueSize().orElseThrow());
      assertTrue(
          item.getServerState().orElseThrow().getDiscardOldest(),
          "ServerState must reflect the discardOldest accepted by the Server");

      BaseMonitoredItem<?> serverItemBefore = fixture.serverItem(subscription, item);
      assertEquals(250.0, serverItemBefore.getSamplingInterval());
      assertEquals(2, serverItemBefore.getQueueSize());
      assertTrue(serverItemBefore.isDiscardOldest());

      int modifyCallsBefore = fixture.serviceSet.modifyCalls.get();
      subscription.synchronizeMonitoredItems();

      assertEquals(
          modifyCallsBefore + 1,
          fixture.serviceSet.modifyCalls.get(),
          "second synchronizeMonitoredItems() must send the in-flight changes");
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertEquals(500.0, item.getRevisedSamplingInterval().orElseThrow());
      assertEquals(uint(5), item.getRevisedQueueSize().orElseThrow());
      assertFalse(item.getServerState().orElseThrow().getDiscardOldest());

      BaseMonitoredItem<?> serverItemAfter = fixture.serverItem(subscription, item);
      assertEquals(500.0, serverItemAfter.getSamplingInterval());
      assertEquals(5, serverItemAfter.getQueueSize());
      assertFalse(serverItemAfter.isDiscardOldest());

      subscription.delete();
    }
  }

  /**
   * Changing parameters on an INITIAL item while CreateMonitoredItems is in flight must transition
   * the item to UNSYNCHRONIZED when the Good Create result is applied so the next
   * synchronizeMonitoredItems() sends ModifyMonitoredItems with the updated parameters.
   */
  @Test
  void parameterChangesDuringInFlightCreateAreSentOnNextSynchronization() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      Hold hold = fixture.serviceSet.holdNextCreate();
      CompletableFuture<Void> firstSync = synchronizeAsync(subscription);
      hold.awaitArrived();

      // Mutate parameters while CreateMonitoredItems is in flight.
      var filter =
          new DataChangeFilter(
              DataChangeTrigger.StatusValueTimestamp, uint(DeadbandType.None.getValue()), 0.0);
      item.setSamplingInterval(500.0);
      item.setQueueSize(uint(4));
      item.setDiscardOldest(false);
      item.setFilter(filter);

      hold.release();
      firstSync.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertEquals(
          SyncState.UNSYNCHRONIZED,
          item.getSyncState(),
          "parameter changes made during an in-flight CreateMonitoredItems must leave the item"
              + " UNSYNCHRONIZED");
      assertFalse(subscription.isMonitoredItemsSynchronized());
      assertTrue(item.getCreateResult().orElseThrow().isGood());
      assertEquals(1000.0, item.getRevisedSamplingInterval().orElseThrow());
      assertEquals(uint(1), item.getRevisedQueueSize().orElseThrow());
      assertTrue(item.getServerState().orElseThrow().getDiscardOldest());

      int modifyCallsBefore = fixture.serviceSet.modifyCalls.get();
      subscription.synchronizeMonitoredItems();

      assertEquals(
          modifyCallsBefore + 1,
          fixture.serviceSet.modifyCalls.get(),
          "second synchronizeMonitoredItems() must send ModifyMonitoredItems for the in-flight"
              + " create changes");
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertEquals(500.0, item.getRevisedSamplingInterval().orElseThrow());
      assertEquals(uint(4), item.getRevisedQueueSize().orElseThrow());
      assertFalse(item.getServerState().orElseThrow().getDiscardOldest());

      BaseMonitoredItem<?> serverItem = fixture.serverItem(subscription, item);
      assertEquals(500.0, serverItem.getSamplingInterval());
      assertEquals(4, serverItem.getQueueSize());
      assertFalse(serverItem.isDiscardOldest());

      subscription.delete();
    }
  }

  /**
   * Control: CreateMonitoredItems and ModifyMonitoredItems with no concurrent parameter changes
   * leave the item SYNCHRONIZED and a follow-up synchronizeMonitoredItems() makes no redundant
   * ModifyMonitoredItems calls.
   */
  @Test
  void unchangedCreateAndModifyDoNotTriggerExtraModifyCall() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setSamplingInterval(250.0);
      subscription.addMonitoredItem(item);

      subscription.synchronizeMonitoredItems();
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(0, fixture.serviceSet.modifyCalls.get());

      subscription.synchronizeMonitoredItems();
      assertEquals(
          0,
          fixture.serviceSet.modifyCalls.get(),
          "control: an unchanged item must not trigger ModifyMonitoredItems after Create");

      item.setSamplingInterval(500.0);
      subscription.synchronizeMonitoredItems();
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(1, fixture.serviceSet.modifyCalls.get());

      subscription.synchronizeMonitoredItems();
      assertEquals(
          1,
          fixture.serviceSet.modifyCalls.get(),
          "control: an unchanged item must not trigger a second ModifyMonitoredItems");

      subscription.delete();
    }
  }

  /**
   * Two overlapping ModifyMonitoredItems calls reordered across the wire and response path: Caller
   * A prepares queueSize=2 and pauses before server execution; setter updates queueSize=3; Caller B
   * executes queueSize=3 on the Server first and pauses before replying; Caller A resumes, executes
   * queueSize=2 on the Server second (leaving the Server at 2), and applies locally first; Caller B
   * then replies and applies locally second. The item must remain UNSYNCHRONIZED until a final
   * synchronization reconciles the Server back to 3.
   */
  @Test
  void overlappingModifiesReorderedOnWireStayUnsynchronizedUntilReconciled() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      // Caller A prepares queueSize=2 and pauses BEFORE the Server applies it.
      item.setQueueSize(uint(2));
      Hold holdABeforeServer = fixture.serviceSet.holdNextModifyBeforeDelegate();
      CompletableFuture<Void> syncA = synchronizeAsync(subscription);
      holdABeforeServer.awaitArrived();

      // Setter advances desired queueSize to 3; Caller B executes on the Server FIRST, then pauses
      // BEFORE returning its response so A can overtake B on the Server and apply locally first.
      item.setQueueSize(uint(3));
      Hold holdBAfterServer = fixture.serviceSet.holdNextModify();
      CompletableFuture<Void> syncB = synchronizeAsync(subscription);
      holdBAfterServer.awaitArrived();

      // Server currently holds B's value (3). Release A so A overwrites the Server with 2 and
      // applies its Good response locally before B replies.
      assertEquals(3, fixture.serverItem(subscription, item).getQueueSize());
      holdABeforeServer.release();
      syncA.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertEquals(
          2,
          fixture.serverItem(subscription, item).getQueueSize(),
          "control: A executed on the Server after B, so the Server now holds queueSize=2");

      // Now let B's Good response apply locally AFTER A finished.
      holdBAfterServer.release();
      syncB.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertEquals(
          SyncState.UNSYNCHRONIZED,
          item.getSyncState(),
          "item must stay UNSYNCHRONIZED because overlapping modifies reordered on the wire while"
              + " the Server holds queueSize=2");
      assertFalse(subscription.isMonitoredItemsSynchronized());

      // Final non-overlapping synchronization reconciles the Server to 3.
      int modifyCallsBefore = fixture.serviceSet.modifyCalls.get();
      subscription.synchronizeMonitoredItems();

      assertEquals(modifyCallsBefore + 1, fixture.serviceSet.modifyCalls.get());
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertEquals(uint(3), item.getRevisedQueueSize().orElseThrow());
      assertEquals(3, fixture.serverItem(subscription, item).getQueueSize());

      subscription.delete();
    }
  }

  /**
   * When Subscription A selects a dirty item and blocks on an earlier partition while the item is
   * deleted from A, recreated on Subscription B, and modified again, A's next partition must verify
   * membership under monitoredItemsLock, skip sending ModifyMonitoredItems against A's
   * SubscriptionId, report Bad_InvalidState, and leave B's item UNSYNCHRONIZED.
   */
  @Test
  void modifySkipsItemMovedToAnotherSubscriptionBetweenPartitions() throws Exception {
    try (var fixture = new Fixture()) {
      var subscriptionA = new OpcUaSubscription(fixture.client);
      subscriptionA.setMaxMonitoredItemsPerCall(uint(1));
      subscriptionA.create();

      var subscriptionB = new OpcUaSubscription(fixture.client);
      subscriptionB.create();

      OpcUaMonitoredItem item1 =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      OpcUaMonitoredItem item2 = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
      subscriptionA.addMonitoredItems(List.of(item1, item2));
      subscriptionA.synchronizeMonitoredItems();

      item1.setSamplingInterval(250.0);
      item2.setSamplingInterval(250.0);

      Hold firstPartitionHold = fixture.serviceSet.holdNextModify();
      CompletableFuture<List<MonitoredItemServiceOperationResult>> modifyAFuture =
          CompletableFuture.supplyAsync(subscriptionA::modifyMonitoredItems);

      try {
        firstPartitionHold.awaitArrived();

        UInteger firstPartitionItemId = fixture.serviceSet.lastModifiedItemId.get();
        OpcUaMonitoredItem stayedInA =
            firstPartitionItemId.equals(item1.getMonitoredItemId().orElseThrow()) ? item1 : item2;
        OpcUaMonitoredItem movedToB = stayedInA == item1 ? item2 : item1;

        // Move the second-partition item from A to B and make it dirty in B.
        subscriptionA.removeMonitoredItem(movedToB);
        List<MonitoredItemServiceOperationResult> deletedFromA =
            subscriptionA.deleteMonitoredItems();
        assertEquals(1, deletedFromA.size());
        assertTrue(deletedFromA.get(0).isGood());

        subscriptionB.addMonitoredItem(movedToB);
        List<MonitoredItemServiceOperationResult> createdInB = subscriptionB.createMonitoredItems();
        assertEquals(1, createdInB.size());
        assertTrue(createdInB.get(0).isGood());
        assertEquals(
            uint(1),
            movedToB.getMonitoredItemId().orElseThrow(),
            "pins id overlap: Subscription B assigns MonitoredItemId 1");

        movedToB.setSamplingInterval(400.0);
        assertEquals(SyncState.UNSYNCHRONIZED, movedToB.getSyncState());

        firstPartitionHold.release();

        List<MonitoredItemServiceOperationResult> modifyAResults =
            modifyAFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertEquals(
            1,
            fixture.serviceSet.modifyCalls.get(),
            "Subscription A must not send ModifyMonitoredItems for an item moved to B");
        assertEquals(2, modifyAResults.size());

        MonitoredItemServiceOperationResult stayedResult =
            modifyAResults.stream()
                .filter(r -> r.monitoredItem() == stayedInA)
                .findFirst()
                .orElseThrow();
        assertTrue(stayedResult.isGood());
        assertEquals(SyncState.SYNCHRONIZED, stayedInA.getSyncState());
        assertEquals(250.0, fixture.serverItem(subscriptionA, stayedInA).getSamplingInterval());

        MonitoredItemServiceOperationResult movedResult =
            modifyAResults.stream()
                .filter(r -> r.monitoredItem() == movedToB)
                .findFirst()
                .orElseThrow();
        assertEquals(StatusCodes.Bad_InvalidState, movedResult.serviceResult().value());
        assertTrue(movedResult.operationResult().isEmpty());

        // Subscription B's item must remain UNSYNCHRONIZED until B synchronizes it.
        assertEquals(SyncState.UNSYNCHRONIZED, movedToB.getSyncState());
        assertEquals(250.0, fixture.serverItem(subscriptionB, movedToB).getSamplingInterval());

        subscriptionB.synchronizeMonitoredItems();
        assertEquals(SyncState.SYNCHRONIZED, movedToB.getSyncState());
        assertEquals(400.0, fixture.serverItem(subscriptionB, movedToB).getSamplingInterval());
      } finally {
        firstPartitionHold.release();
      }

      subscriptionA.delete();
      subscriptionB.delete();
    }
  }

  /**
   * If filter encoding throws an Error during modifyMonitoredItems(), activeModifies must still be
   * released for every item in the partition so the next modify transitions cleanly to
   * SYNCHRONIZED.
   */
  @Test
  void errorDuringModifyFilterEncodingReleasesActiveModifiesForPartition() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem healthyItem =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      OpcUaMonitoredItem failingItem =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
      subscription.addMonitoredItems(List.of(healthyItem, failingItem));
      subscription.synchronizeMonitoredItems();

      healthyItem.setQueueSize(uint(3));
      failingItem.setFilter(new ErrorThrowingDataChangeFilter());

      assertThrows(AssertionError.class, subscription::modifyMonitoredItems);

      failingItem.setFilter(null);
      failingItem.setQueueSize(uint(4));

      List<MonitoredItemServiceOperationResult> results = subscription.modifyMonitoredItems();
      assertEquals(2, results.size());
      assertTrue(results.get(0).isGood());
      assertTrue(results.get(1).isGood());
      assertEquals(
          SyncState.SYNCHRONIZED,
          healthyItem.getSyncState(),
          "healthyItem must not have a stranded activeModifies count");
      assertEquals(
          SyncState.SYNCHRONIZED,
          failingItem.getSyncState(),
          "failingItem must not have a stranded activeModifies count");
      assertTrue(subscription.isMonitoredItemsSynchronized());

      subscription.delete();
    }
  }

  /**
   * Interrupting the thread waiting on ModifyMonitoredItems(queueSize=2) while the server request
   * is still active must NOT release activeModifies early: a concurrent modify(queueSize=3) must
   * still see activeModifies > 0 and leave the item UNSYNCHRONIZED so a final reconciliation pass
   * repairs the Server after the delayed first modify completes.
   */
  @Test
  void interruptingModifyKeepsActiveModifiesHeldUntilAsyncResponseCompletes() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      item.setQueueSize(uint(2));
      Hold holdABeforeServer = fixture.serviceSet.holdNextModifyBeforeDelegate();

      var callerAResults = new AtomicReference<List<MonitoredItemServiceOperationResult>>();
      var callerAInterrupted = new AtomicBoolean(false);
      Thread callerA =
          new Thread(
              () -> {
                callerAResults.set(subscription.modifyMonitoredItems());
                callerAInterrupted.set(Thread.currentThread().isInterrupted());
              });
      callerA.start();

      try {
        holdABeforeServer.awaitArrived();

        callerA.interrupt();
        callerA.join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
        assertFalse(callerA.isAlive());
        assertTrue(callerAInterrupted.get());
        assertEquals(1, callerAResults.get().size());
        assertEquals(
            StatusCodes.Bad_UnexpectedError, callerAResults.get().get(0).serviceResult().value());

        // While Modify A is still active on the Server, Modify B updates queueSize=3.
        item.setQueueSize(uint(3));
        List<MonitoredItemServiceOperationResult> resultsB = subscription.modifyMonitoredItems();
        assertEquals(1, resultsB.size());
        assertTrue(resultsB.get(0).isGood());
        assertEquals(3, fixture.serverItem(subscription, item).getQueueSize());
        assertEquals(
            SyncState.UNSYNCHRONIZED,
            item.getSyncState(),
            "Modify A's activeModifies reservation must remain held after caller interrupt so"
                + " Modify B cannot prematurely mark the item SYNCHRONIZED");
      } finally {
        holdABeforeServer.release();
      }

      // Wait until the delayed Modify A executes on the Server and applies asynchronously.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
      while (item.getRevisedQueueSize().map(q -> q.intValue() != 2).orElse(true)
          && System.nanoTime() < deadline) {
        Thread.yield();
      }
      assertEquals(uint(2), item.getRevisedQueueSize().orElseThrow());
      assertEquals(2, fixture.serverItem(subscription, item).getQueueSize());
      assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());

      // Final non-overlapping sync reconciles the Server to 3 cleanly.
      subscription.synchronizeMonitoredItems();
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(uint(3), item.getRevisedQueueSize().orElseThrow());
      assertEquals(3, fixture.serverItem(subscription, item).getQueueSize());

      subscription.delete();
    }
  }

  /**
   * Removing an item and calling deleteMonitoredItems() or synchronizeMonitoredItems() while a
   * ModifyMonitoredItems request for that item is paused (for example during filter encoding) must
   * defer deleting that item until the modify finishes, while still deleting other removed items in
   * the same batch cleanly and leaving the deferred item pending deletion.
   */
  @Test
  void deleteIsDeferredWhileModifyIsInFlight() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      OpcUaMonitoredItem otherItem =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
      subscription.addMonitoredItems(List.of(item, otherItem));
      subscription.synchronizeMonitoredItems();
      assertEquals(2, fixture.serverMonitoredItemCount(subscriptionId));

      var blockingFilter = new BlockingDataChangeFilter();
      item.setFilter(blockingFilter);

      CompletableFuture<List<MonitoredItemServiceOperationResult>> modifyFuture =
          CompletableFuture.supplyAsync(subscription::modifyMonitoredItems);

      try {
        blockingFilter.awaitEncodingStarted();

        // Remove both items while item's modify is paused in filter encoding.
        subscription.removeMonitoredItems(List.of(item, otherItem));

        List<MonitoredItemServiceOperationResult> batchDelete = subscription.deleteMonitoredItems();
        assertEquals(2, batchDelete.size());

        MonitoredItemServiceOperationResult otherDeleteResult =
            batchDelete.stream()
                .filter(r -> r.monitoredItem() == otherItem)
                .findFirst()
                .orElseThrow();
        assertTrue(otherDeleteResult.isGood());

        MonitoredItemServiceOperationResult deferredDeleteResult =
            batchDelete.stream().filter(r -> r.monitoredItem() == item).findFirst().orElseThrow();
        assertEquals(StatusCodes.Bad_InvalidState, deferredDeleteResult.serviceResult().value());
        assertTrue(deferredDeleteResult.operationResult().isEmpty());

        assertFalse(subscription.isMonitoredItemsSynchronized());
        assertEquals(1, fixture.serverMonitoredItemCount(subscriptionId));
        assertTrue(item.getClientHandle().isPresent());

        // synchronizeMonitoredItems() must also defer without sending a DeleteMonitoredItems call.
        int deleteCallsBefore = fixture.serviceSet.deleteCalls.get();
        MonitoredItemSynchronizationException ex =
            assertThrows(
                MonitoredItemSynchronizationException.class,
                subscription::synchronizeMonitoredItems);
        assertEquals(deleteCallsBefore, fixture.serviceSet.deleteCalls.get());
        assertEquals(1, ex.getDeleteResults().size());
        assertEquals(
            StatusCodes.Bad_InvalidState, ex.getDeleteResults().get(0).serviceResult().value());
      } finally {
        blockingFilter.releaseEncoding();
      }

      List<MonitoredItemServiceOperationResult> modifyResults =
          modifyFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      assertEquals(1, modifyResults.size());
      assertTrue(modifyResults.get(0).isGood());

      // Now that the in-flight modify finished, synchronizeMonitoredItems() deletes item cleanly.
      subscription.synchronizeMonitoredItems();
      assertEquals(0, fixture.serverMonitoredItemCount(subscriptionId));
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertTrue(item.getClientHandle().isEmpty());

      subscription.delete();
    }
  }

  private static final class BlockingDataChangeFilter extends DataChangeFilter {

    private final CountDownLatch encodingStarted = new CountDownLatch(1);
    private final CountDownLatch releaseEncoding = new CountDownLatch(1);

    BlockingDataChangeFilter() {
      super(DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);
    }

    void awaitEncodingStarted() throws InterruptedException {
      assertTrue(
          encodingStarted.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
          "filter encoding did not start");
    }

    void releaseEncoding() {
      releaseEncoding.countDown();
    }

    @Override
    public Double getDeadbandValue() {
      encodingStarted.countDown();
      try {
        assertTrue(
            releaseEncoding.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
            "filter encoding gate was never released");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new AssertionError(e);
      }
      return super.getDeadbandValue();
    }
  }

  private static final class ErrorThrowingDataChangeFilter extends DataChangeFilter {

    ErrorThrowingDataChangeFilter() {
      super(DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);
    }

    @Override
    public Double getDeadbandValue() {
      throw new AssertionError("simulated Error during modify filter encoding");
    }
  }

  private static CompletableFuture<Void> synchronizeAsync(OpcUaSubscription subscription) {
    return CompletableFuture.runAsync(
        () -> {
          try {
            subscription.synchronizeMonitoredItems();
          } catch (MonitoredItemSynchronizationException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private static final class Hold {

    private final CountDownLatch arrived = new CountDownLatch(1);
    private final CountDownLatch released = new CountDownLatch(1);

    void awaitArrived() throws InterruptedException {
      assertTrue(
          arrived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
          "the gated MonitoredItem request never reached the Server");
    }

    void release() {
      released.countDown();
    }

    void hold() throws UaException {
      arrived.countDown();

      try {
        if (!released.await(30, TimeUnit.SECONDS)) {
          throw new UaException(StatusCodes.Bad_Timeout, "gate was never released");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }
    }
  }

  private static final class HoldingMonitoredItemServiceSet
      extends DelegatingMonitoredItemServiceSet {

    final AtomicInteger modifyCalls = new AtomicInteger();
    final AtomicInteger deleteCalls = new AtomicInteger();
    final AtomicReference<UInteger> lastModifiedItemId = new AtomicReference<>();

    private final AtomicReference<Hold> nextCreate = new AtomicReference<>();
    private final AtomicReference<Hold> nextModifyBeforeDelegate = new AtomicReference<>();
    private final AtomicReference<Hold> nextModify = new AtomicReference<>();

    HoldingMonitoredItemServiceSet(OpcUaServer server) {
      super(server);
    }

    Hold holdNextCreate() {
      var hold = new Hold();
      nextCreate.set(hold);
      return hold;
    }

    Hold holdNextModifyBeforeDelegate() {
      var hold = new Hold();
      nextModifyBeforeDelegate.set(hold);
      return hold;
    }

    Hold holdNextModify() {
      var hold = new Hold();
      nextModify.set(hold);
      return hold;
    }

    void releaseAll() {
      Hold createHold = nextCreate.getAndSet(null);
      if (createHold != null) {
        createHold.release();
      }

      Hold beforeModifyHold = nextModifyBeforeDelegate.getAndSet(null);
      if (beforeModifyHold != null) {
        beforeModifyHold.release();
      }

      Hold modifyHold = nextModify.getAndSet(null);
      if (modifyHold != null) {
        modifyHold.release();
      }
    }

    @Override
    public CreateMonitoredItemsResponse onCreateMonitoredItems(
        ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {

      CreateMonitoredItemsResponse response = super.onCreateMonitoredItems(context, request);

      Hold hold = nextCreate.getAndSet(null);
      if (hold != null) {
        hold.hold();
      }

      return response;
    }

    @Override
    public ModifyMonitoredItemsResponse onModifyMonitoredItems(
        ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

      modifyCalls.incrementAndGet();
      if (request.getItemsToModify() != null && request.getItemsToModify().length > 0) {
        lastModifiedItemId.set(request.getItemsToModify()[0].getMonitoredItemId());
      }

      Hold beforeHold = nextModifyBeforeDelegate.getAndSet(null);
      if (beforeHold != null) {
        beforeHold.hold();
      }

      ModifyMonitoredItemsResponse response = super.onModifyMonitoredItems(context, request);

      Hold hold = nextModify.getAndSet(null);
      if (hold != null) {
        hold.hold();
      }

      return response;
    }

    @Override
    public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
        ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {

      deleteCalls.incrementAndGet();
      return super.onDeleteMonitoredItems(context, request);
    }
  }

  private static final class Fixture implements AutoCloseable {

    final OpcUaServer server;
    final OpcUaClient client;
    final HoldingMonitoredItemServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new HoldingMonitoredItemServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(10_000)));
      client.connect();
    }

    BaseMonitoredItem<?> serverItem(OpcUaSubscription subscription, OpcUaMonitoredItem item) {
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();
      UInteger monitoredItemId = item.getMonitoredItemId().orElseThrow();

      return server.getSubscriptions().get(subscriptionId).getMonitoredItems().get(monitoredItemId);
    }

    int serverMonitoredItemCount(UInteger subscriptionId) {
      return server.getSubscriptions().get(subscriptionId).getMonitoredItemCount().intValue();
    }

    @Override
    public void close() throws Exception {
      serviceSet.releaseAll();
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
