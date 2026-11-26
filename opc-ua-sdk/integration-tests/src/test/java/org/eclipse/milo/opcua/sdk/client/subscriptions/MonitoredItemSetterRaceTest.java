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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Verifies {@link OpcUaSubscription} behavior when {@link OpcUaMonitoredItem} parameter setters
 * race {@link OpcUaSubscription#reset()} or modify result application, and verifies that
 * synchronization passes report per-item state anomalies instead of aborting with an unchecked
 * {@link IllegalStateException}.
 */
public class MonitoredItemSetterRaceTest {

  private static final int RESET_RACE_ROUNDS = 25;
  private static final long TIMEOUT_SECONDS = 10;

  private final ExecutorService workers = Executors.newSingleThreadExecutor();

  @AfterEach
  void stopWorkers() throws InterruptedException {
    workers.shutdownNow();
    assertTrue(workers.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS));
  }

  @Nested
  class DefensiveSynchronization {

    /**
     * When one item loses its ServerState between selection and ModifyMonitoredItems request
     * building, synchronizeMonitoredItems() must not abort with an unchecked IllegalStateException:
     * it must still modify other valid items, proceed to createMonitoredItems(), and report
     * Bad_InvalidState for the affected item in MonitoredItemSynchronizationException.
     */
    @Test
    void synchronizeReportsItemThatLostServerStateWithoutBlockingOtherItems() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var resetItem = new ResetOnModifyRequestItem(NodeIds.Server_ServerStatus_CurrentTime);
        var validModified = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);

        subscription.addMonitoredItems(List.of(resetItem, validModified));
        subscription.synchronizeMonitoredItems();

        resetItem.setSamplingInterval(250.0);
        validModified.setSamplingInterval(250.0);
        resetItem.armResetOnNextModifyRequest();

        var newInitial = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_BuildInfo);
        subscription.addMonitoredItem(newInitial);

        MonitoredItemSynchronizationException ex =
            assertThrows(
                MonitoredItemSynchronizationException.class,
                subscription::synchronizeMonitoredItems,
                "an item that lost ServerState during modify must be reported via"
                    + " MonitoredItemSynchronizationException, not IllegalStateException");

        MonitoredItemServiceOperationResult resetModifyResult =
            resultFor(ex.getModifyResults(), resetItem);
        assertEquals(StatusCodes.Bad_InvalidState, resetModifyResult.serviceResult().value());
        assertTrue(resetModifyResult.operationResult().isEmpty());

        MonitoredItemServiceOperationResult validModifyResult =
            resultFor(ex.getModifyResults(), validModified);
        assertTrue(validModifyResult.isGood());
        assertEquals(250.0, validModified.getRevisedSamplingInterval().orElseThrow());
        assertEquals(SyncState.SYNCHRONIZED, validModified.getSyncState());

        // The create phase must still have run after modifyMonitoredItems().
        MonitoredItemServiceOperationResult newCreateResult =
            resultFor(ex.getCreateResults(), newInitial);
        assertTrue(
            newCreateResult.isGood(),
            "INITIAL items must still be created when another item fails modify request building");
        assertEquals(SyncState.SYNCHRONIZED, newInitial.getSyncState());

        // Because resetItem became INITIAL during modify, the create step also recreated it.
        MonitoredItemServiceOperationResult recreatedResult =
            resultFor(ex.getCreateResults(), resetItem);
        assertTrue(recreatedResult.isGood());
        assertEquals(SyncState.SYNCHRONIZED, resetItem.getSyncState());
      }
    }

    /**
     * When one item has its pending modification cleared concurrently before request building,
     * synchronizeMonitoredItems() must not throw IllegalStateException("no pending modification"),
     * must still modify other items and create new INITIAL items, and must report Bad_InvalidState
     * for the already-synchronized item.
     */
    @Test
    void synchronizeReportsItemThatLostPendingModificationWithoutBlockingOtherItems()
        throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var clearedItem =
            new ClearModificationOnModifyRequestItem(NodeIds.Server_ServerStatus_CurrentTime);
        var validModified = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);

        subscription.addMonitoredItems(List.of(clearedItem, validModified));
        subscription.synchronizeMonitoredItems();

        clearedItem.setSamplingInterval(250.0);
        validModified.setSamplingInterval(250.0);
        clearedItem.armClearOnNextModifyRequest();

        var newInitial = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_BuildInfo);
        subscription.addMonitoredItem(newInitial);

        MonitoredItemSynchronizationException ex =
            assertThrows(
                MonitoredItemSynchronizationException.class,
                subscription::synchronizeMonitoredItems);

        MonitoredItemServiceOperationResult clearedModifyResult =
            resultFor(ex.getModifyResults(), clearedItem);
        assertEquals(StatusCodes.Bad_InvalidState, clearedModifyResult.serviceResult().value());
        assertTrue(clearedModifyResult.operationResult().isEmpty());

        assertTrue(resultFor(ex.getModifyResults(), validModified).isGood());
        assertTrue(resultFor(ex.getCreateResults(), newInitial).isGood());
        assertEquals(SyncState.SYNCHRONIZED, newInitial.getSyncState());
      }
    }

    /**
     * When every item selected for modification loses its pending modification before the request
     * is built, modifyMonitoredItems() must report Bad_InvalidState and skip sending an empty
     * ModifyMonitoredItems service call.
     */
    @Test
    void modifyMonitoredItemsSkipsServiceCallWhenNoItemCanBuildARequest() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var resetItem = new ResetOnModifyRequestItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(resetItem);
        subscription.synchronizeMonitoredItems();

        resetItem.setSamplingInterval(250.0);
        resetItem.armResetOnNextModifyRequest();

        int callsBefore = fixture.serviceSet.modifyCalls.get();
        List<MonitoredItemServiceOperationResult> results = subscription.modifyMonitoredItems();

        assertEquals(1, results.size());
        assertEquals(StatusCodes.Bad_InvalidState, results.get(0).serviceResult().value());
        assertTrue(results.get(0).operationResult().isEmpty());
        assertEquals(
            callsBefore,
            fixture.serviceSet.modifyCalls.get(),
            "no ModifyMonitoredItems service call should be sent when no valid requests remain");
      }
    }

    /**
     * When one INITIAL item loses its ClientHandle between selection and CreateMonitoredItems
     * request building, createMonitoredItems() must report Bad_InvalidState for it while still
     * creating the remaining items in the partition.
     */
    @Test
    void createMonitoredItemsReportsDetachedItemWithoutBlockingOtherItems() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var detachedItem = new DetachOnCreateRequestItem(NodeIds.Server_ServerStatus_CurrentTime);
        var validItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);

        subscription.addMonitoredItems(List.of(detachedItem, validItem));
        detachedItem.armDetachOnNextCreateRequest();

        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();

        assertEquals(2, results.size());

        MonitoredItemServiceOperationResult detachedResult = resultFor(results, detachedItem);
        assertEquals(StatusCodes.Bad_InvalidState, detachedResult.serviceResult().value());
        assertTrue(detachedResult.operationResult().isEmpty());

        MonitoredItemServiceOperationResult validResult = resultFor(results, validItem);
        assertTrue(validResult.isGood());
        assertEquals(SyncState.SYNCHRONIZED, validItem.getSyncState());
      }
    }
  }

  @Nested
  class SubscriptionResetRaces {

    /**
     * Parameter setters racing OpcUaSubscription.reset() must never leave an item UNSYNCHRONIZED
     * with no ServerState; recreating the Subscription and synchronizing must create every item on
     * the new Subscription cleanly.
     */
    @Test
    void settersRacingSubscriptionResetLeaveAllItemsSynchronizableAfterRecreate() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        var itemA = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        var itemB = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
        subscription.addMonitoredItems(List.of(itemA, itemB));

        var filter =
            new DataChangeFilter(
                DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

        for (int round = 0; round < RESET_RACE_ROUNDS; round++) {
          subscription.create();
          var subscriptionId = subscription.getSubscriptionId().orElseThrow();
          subscription.synchronizeMonitoredItems();

          var start = new CyclicBarrier(2);
          var setterFailure = new AtomicReference<Throwable>();
          int step = round;

          Future<?> setterTask =
              workers.submit(
                  () -> {
                    try {
                      start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                      switch (step % 4) {
                        case 0 -> itemA.setSamplingInterval(100.0 + step);
                        case 1 -> itemA.setQueueSize(uint(2 + (step % 5)));
                        case 2 -> itemA.setDiscardOldest((step & 1) == 0);
                        case 3 -> itemA.setFilter((step & 1) == 0 ? filter : null);
                        default -> throw new AssertionError();
                      }
                    } catch (Throwable t) {
                      setterFailure.compareAndSet(null, t);
                    }
                  });

          start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
          subscription.reset();

          setterTask.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

          assertNull(
              setterFailure.get(),
              "round " + round + ": parameter setter threw while racing subscription.reset()");
          assertEquals(
              SyncState.INITIAL,
              itemA.getSyncState(),
              "round " + round + ": reset must leave itemA INITIAL");
          assertTrue(itemA.getServerState().isEmpty());
          assertEquals(SyncState.INITIAL, itemB.getSyncState());

          fixture.client.deleteSubscriptions(List.of(subscriptionId));
        }

        // Verify the final recreated Subscription creates both items cleanly.
        subscription.create();
        subscription.synchronizeMonitoredItems();

        assertEquals(SyncState.SYNCHRONIZED, itemA.getSyncState());
        assertEquals(SyncState.SYNCHRONIZED, itemB.getSyncState());
        assertTrue(itemA.getMonitoredItemId().isPresent());
        assertTrue(itemB.getMonitoredItemId().isPresent());

        subscription.delete();
      }
    }
  }

  @Nested
  class InFlightCreateLifecycle {

    /**
     * Removing an item and calling deleteMonitoredItems() while CreateMonitoredItems is in flight
     * must not detach the item or drop its pending removal, and a second concurrent create must not
     * send a duplicate request. Once the in-flight create succeeds, the next deleteMonitoredItems()
     * must delete the created server-side item with no server leak.
     */
    @Test
    void itemRemovedAndDeletedWhileCreateIsInFlightIsDeletedOnceCreateCompletes() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();
        UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

        OpcUaMonitoredItem item =
            OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);

        fixture.serviceSet.gateNextCreate(false);
        CompletableFuture<List<MonitoredItemServiceOperationResult>> createFuture =
            CompletableFuture.supplyAsync(subscription::createMonitoredItems);
        fixture.serviceSet.awaitCreateArrived();

        // A second concurrent createMonitoredItems() must not send a duplicate server call.
        List<MonitoredItemServiceOperationResult> duplicateCreate =
            subscription.createMonitoredItems();
        assertEquals(1, duplicateCreate.size());
        assertEquals(StatusCodes.Bad_InvalidState, duplicateCreate.get(0).serviceResult().value());
        assertEquals(
            1,
            fixture.serviceSet.createCalls.get(),
            "concurrent createMonitoredItems() must not send a duplicate CreateMonitoredItems");

        // Removing and calling deleteMonitoredItems() while Create is in flight must keep the
        // pending removal queued until the create finishes.
        subscription.removeMonitoredItem(item);
        List<MonitoredItemServiceOperationResult> earlyDelete = subscription.deleteMonitoredItems();
        assertEquals(1, earlyDelete.size());
        assertEquals(StatusCodes.Bad_InvalidState, earlyDelete.get(0).serviceResult().value());
        assertFalse(
            subscription.isMonitoredItemsSynchronized(),
            "a removed item whose create is in flight must remain unsynchronized");
        assertTrue(item.getClientHandle().isPresent());

        // Release the in-flight create; it succeeds on the server.
        fixture.serviceSet.releaseCreate();
        List<MonitoredItemServiceOperationResult> createResults =
            createFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(1, createResults.size());
        assertTrue(createResults.get(0).isGood());
        assertEquals(1, fixture.serverMonitoredItemCount(subscriptionId));
        assertFalse(
            subscription.isMonitoredItemsSynchronized(),
            "the newly created item is still pending deletion");

        // The next deleteMonitoredItems() deletes the server-side item cleanly.
        List<MonitoredItemServiceOperationResult> finalDelete = subscription.deleteMonitoredItems();
        assertEquals(1, finalDelete.size());
        assertTrue(finalDelete.get(0).isGood());
        assertEquals(
            0,
            fixture.serverMonitoredItemCount(subscriptionId),
            "the created item must be deleted on the Server with no leak");
        assertTrue(subscription.isMonitoredItemsSynchronized());
        assertEquals(SyncState.INITIAL, item.getSyncState());
        assertTrue(item.getClientHandle().isEmpty());

        subscription.delete();
      }
    }

    /**
     * When a CreateMonitoredItems service call fails while a removal is pending, its in-flight
     * reservation must be released so the next deleteMonitoredItems() cleanly detaches the item
     * without making a server DeleteMonitoredItems call.
     */
    @Test
    void itemRemovedWhileCreateFailsIsCleanlyDetachedOnNextDelete() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem item =
            OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);

        fixture.serviceSet.gateNextCreate(true);
        CompletableFuture<List<MonitoredItemServiceOperationResult>> createFuture =
            CompletableFuture.supplyAsync(subscription::createMonitoredItems);
        fixture.serviceSet.awaitCreateArrived();

        subscription.removeMonitoredItem(item);
        List<MonitoredItemServiceOperationResult> earlyDelete = subscription.deleteMonitoredItems();
        assertEquals(1, earlyDelete.size());
        assertEquals(StatusCodes.Bad_InvalidState, earlyDelete.get(0).serviceResult().value());

        fixture.serviceSet.releaseCreate();
        List<MonitoredItemServiceOperationResult> createResults =
            createFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(1, createResults.size());
        assertEquals(StatusCodes.Bad_UnexpectedError, createResults.get(0).serviceResult().value());

        int deleteCallsBefore = fixture.serviceSet.deleteCalls.get();
        List<MonitoredItemServiceOperationResult> finalDelete = subscription.deleteMonitoredItems();
        assertTrue(finalDelete.isEmpty());
        assertEquals(deleteCallsBefore, fixture.serviceSet.deleteCalls.get());
        assertTrue(subscription.isMonitoredItemsSynchronized());
        assertTrue(item.getClientHandle().isEmpty());

        subscription.delete();
      }
    }
  }

  @Nested
  class FilterEncodingSafety {

    /**
     * Filter encoding during CreateMonitoredItems and ModifyMonitoredItems must run outside
     * OpcUaSubscription's lifecycleLock and monitoredItemsLock so a custom filter waiting on
     * another thread that mutates the Subscription cannot deadlock.
     */
    @Test
    void filterEncodingDoesNotHoldSubscriptionLocksDuringCreateOrModify() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var createFilter = new BlockingDataChangeFilter();
        var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        item.setFilter(createFilter);
        subscription.addMonitoredItem(item);

        CompletableFuture<List<MonitoredItemServiceOperationResult>> createFuture =
            CompletableFuture.supplyAsync(subscription::createMonitoredItems);

        try {
          createFilter.awaitEncodingStarted();

          // Both monitoredItemsLock (addMonitoredItem) and lifecycleLock (modify) must be free.
          var otherItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
          subscription.addMonitoredItem(otherItem);
          subscription.setPublishingInterval(500.0);
          subscription.modify();
        } finally {
          createFilter.releaseEncoding();
        }

        List<MonitoredItemServiceOperationResult> createResults =
            createFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(1, createResults.size());
        assertTrue(createResults.get(0).isGood());
        assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());

        // Repeat during ModifyMonitoredItems.
        var modifyFilter = new BlockingDataChangeFilter();
        item.setFilter(modifyFilter);

        CompletableFuture<List<MonitoredItemServiceOperationResult>> modifyFuture =
            CompletableFuture.supplyAsync(subscription::modifyMonitoredItems);

        try {
          modifyFilter.awaitEncodingStarted();

          var thirdItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_BuildInfo);
          subscription.addMonitoredItem(thirdItem);
          subscription.setPublishingInterval(750.0);
          subscription.modify();
        } finally {
          modifyFilter.releaseEncoding();
        }

        List<MonitoredItemServiceOperationResult> modifyResults =
            modifyFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(1, modifyResults.size());
        assertTrue(modifyResults.get(0).isGood());
        assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());

        subscription.delete();
      }
    }

    /**
     * If filter encoding throws an Error during createMonitoredItems(), every create reservation in
     * the partition must still be released so subsequent createMonitoredItems() calls succeed.
     */
    @Test
    void errorDuringCreateFilterEncodingReleasesAllCreateReservationsInPartition()
        throws Exception {

      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var healthyItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        var failingItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
        failingItem.setFilter(new ErrorThrowingDataChangeFilter());

        subscription.addMonitoredItems(List.of(healthyItem, failingItem));

        assertThrows(AssertionError.class, subscription::createMonitoredItems);

        // Neither item's createInFlight reservation may be stranded.
        failingItem.setFilter(null);
        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();
        assertEquals(2, results.size());
        assertTrue(resultFor(results, healthyItem).isGood());
        assertTrue(resultFor(results, failingItem).isGood());
        assertEquals(SyncState.SYNCHRONIZED, healthyItem.getSyncState());
        assertEquals(SyncState.SYNCHRONIZED, failingItem.getSyncState());

        subscription.delete();
      }
    }

    /**
     * Interrupting the thread waiting on createMonitoredItems() while the server request is still
     * active must NOT release createInFlight prematurely: duplicate creates and premature detaches
     * must stay blocked until the async response completes and applies, after which removing and
     * deleting the item cleans up the server-side MonitoredItem with no leak.
     */
    @Test
    void interruptingCreateKeepsReservationHeldUntilAsyncResponseCompletes() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();
        UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

        OpcUaMonitoredItem item =
            OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);

        fixture.serviceSet.gateNextCreate(false);

        var callerResults = new AtomicReference<List<MonitoredItemServiceOperationResult>>();
        var callerInterrupted = new AtomicBoolean(false);
        Thread caller =
            new Thread(
                () -> {
                  callerResults.set(subscription.createMonitoredItems());
                  callerInterrupted.set(Thread.currentThread().isInterrupted());
                });
        caller.start();

        try {
          fixture.serviceSet.awaitCreateArrived();

          caller.interrupt();
          caller.join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
          assertFalse(caller.isAlive(), "interrupted createMonitoredItems() caller must return");
          assertTrue(callerInterrupted.get(), "interrupt status must be preserved on caller");
          assertEquals(1, callerResults.get().size());
          assertEquals(
              StatusCodes.Bad_UnexpectedError, callerResults.get().get(0).serviceResult().value());

          // While the server request is still active, createInFlight must remain held.
          List<MonitoredItemServiceOperationResult> duplicateCreate =
              subscription.createMonitoredItems();
          assertEquals(1, duplicateCreate.size());
          assertEquals(
              StatusCodes.Bad_InvalidState, duplicateCreate.get(0).serviceResult().value());
          assertEquals(
              1,
              fixture.serviceSet.createCalls.get(),
              "interruption must not release createInFlight and allow a duplicate server create");

          // Removing and deleting while the async create is still active must not detach the item.
          subscription.removeMonitoredItem(item);
          List<MonitoredItemServiceOperationResult> earlyDelete =
              subscription.deleteMonitoredItems();
          assertEquals(1, earlyDelete.size());
          assertEquals(StatusCodes.Bad_InvalidState, earlyDelete.get(0).serviceResult().value());
          assertFalse(subscription.isMonitoredItemsSynchronized());
          assertTrue(item.getClientHandle().isPresent());
        } finally {
          fixture.serviceSet.releaseCreate();
        }

        // Wait for the async completion handler to apply the CreateMonitoredItemsResponse (the
        // createResult volatile write is the final field updated inside applyCreateResultLocked).
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
        while (item.getCreateResult().isEmpty() && System.nanoTime() < deadline) {
          Thread.yield();
        }
        assertTrue(
            item.getServerState().isPresent(),
            "async completion handler must apply the create response even after caller interrupt");
        assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
        assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
        assertEquals(1, fixture.serverMonitoredItemCount(subscriptionId));

        // Now deleteMonitoredItems() cleans up the created server-side item with zero leak.
        List<MonitoredItemServiceOperationResult> finalDelete = subscription.deleteMonitoredItems();
        assertEquals(1, finalDelete.size());
        assertTrue(finalDelete.get(0).isGood());
        assertEquals(0, fixture.serverMonitoredItemCount(subscriptionId));
        assertTrue(subscription.isMonitoredItemsSynchronized());
        assertTrue(item.getClientHandle().isEmpty());

        subscription.delete();
      }
    }

    /**
     * Racing addMonitoredItem(sameItem) on two fresh Subscriptions must allow at most one
     * Subscription to claim the detached item; the losing Subscription must leave the item out of
     * its map so the same object is never mapped in two Subscriptions at once.
     */
    @Test
    void concurrentAddMonitoredItemOnTwoSubscriptionsClaimsItemForOnlyOne() throws Exception {
      try (var fixture = new Fixture()) {
        var subA = new OpcUaSubscription(fixture.client);
        var subB = new OpcUaSubscription(fixture.client);

        for (int round = 0; round < 500; round++) {
          var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
          var start = new CyclicBarrier(2);
          var failure = new AtomicReference<Throwable>();

          Future<?> addB =
              workers.submit(
                  () -> {
                    try {
                      start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                      subB.addMonitoredItem(item);
                    } catch (Throwable t) {
                      failure.compareAndSet(null, t);
                    }
                  });

          start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
          subA.addMonitoredItem(item);
          addB.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

          assertNull(failure.get(), "round " + round + ": addMonitoredItem threw");

          boolean inA = subA.getMonitoredItems().contains(item);
          boolean inB = subB.getMonitoredItems().contains(item);
          assertTrue(
              inA ^ inB,
              "round "
                  + round
                  + ": exactly one Subscription must claim the detached item (inA="
                  + inA
                  + ", inB="
                  + inB
                  + ")");

          // Detach from whichever Subscription won so we also exercise repeated rounds cleanly.
          OpcUaSubscription winner = inA ? subA : subB;
          winner.removeMonitoredItem(item);
          winner.deleteMonitoredItems();
          assertTrue(item.getClientHandle().isEmpty());
        }
      }
    }

    /**
     * OpcUaSubscription must use non-overridable internal accessors rather than calling public
     * overridable getters on OpcUaMonitoredItem while holding subscription locks.
     */
    @Test
    void overridablePublicGettersAreNeverInvokedBySubscriptionOperations() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        var item = new ThrowingPublicGettersItem(NodeIds.Server_ServerStatus_CurrentTime);

        subscription.addMonitoredItem(item);
        assertFalse(subscription.isMonitoredItemsSynchronized());

        List<MonitoredItemServiceOperationResult> created = subscription.createMonitoredItems();
        assertEquals(1, created.size());
        assertTrue(created.get(0).isGood());
        assertTrue(subscription.isMonitoredItemsSynchronized());

        item.setSamplingInterval(250.0);
        List<MonitoredItemServiceOperationResult> modified = subscription.modifyMonitoredItems();
        assertEquals(1, modified.size());
        assertTrue(modified.get(0).isGood());

        List<MonitoredItemServiceOperationResult> modeChanged =
            subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item));
        assertEquals(1, modeChanged.size());
        assertTrue(modeChanged.get(0).isGood());

        subscription.removeMonitoredItem(item);
        List<MonitoredItemServiceOperationResult> deleted = subscription.deleteMonitoredItems();
        assertEquals(1, deleted.size());
        assertTrue(deleted.get(0).isGood());
        assertTrue(subscription.isMonitoredItemsSynchronized());

        subscription.delete();
      }
    }
  }

  private static final class ThrowingPublicGettersItem extends OpcUaMonitoredItem {

    ThrowingPublicGettersItem(NodeId nodeId) {
      super(valueReadId(nodeId));
    }

    @Override
    public Optional<UInteger> getClientHandle() {
      throw new AssertionError("overridable getClientHandle() must not be invoked internally");
    }

    @Override
    public Optional<UInteger> getMonitoredItemId() {
      throw new AssertionError("overridable getMonitoredItemId() must not be invoked internally");
    }

    @Override
    public SyncState getSyncState() {
      throw new AssertionError("overridable getSyncState() must not be invoked internally");
    }

    @Override
    public Optional<ServerState> getServerState() {
      throw new AssertionError("overridable getServerState() must not be invoked internally");
    }
  }

  private static MonitoredItemServiceOperationResult resultFor(
      List<MonitoredItemServiceOperationResult> results, OpcUaMonitoredItem item) {

    return results.stream()
        .filter(r -> r.monitoredItem() == item)
        .findFirst()
        .orElseThrow(() -> new AssertionError("missing result for item " + item));
  }

  private static ReadValueId valueReadId(NodeId nodeId) {
    return new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
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
          "filter encoding never started");
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
        throw new AssertionError("interrupted while encoding filter", e);
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
      throw new AssertionError("simulated Error during filter encoding");
    }
  }

  private static final class ResetOnModifyRequestItem extends OpcUaMonitoredItem {

    private boolean arm;

    ResetOnModifyRequestItem(NodeId nodeId) {
      super(valueReadId(nodeId));
    }

    void armResetOnNextModifyRequest() {
      arm = true;
    }

    @Override
    Optional<PreparedModifyRequest> tryBeginModify() {
      if (arm) {
        arm = false;
        reset();
      }
      return super.tryBeginModify();
    }
  }

  private static final class ClearModificationOnModifyRequestItem extends OpcUaMonitoredItem {

    private boolean arm;

    ClearModificationOnModifyRequestItem(NodeId nodeId) {
      super(valueReadId(nodeId));
    }

    void armClearOnNextModifyRequest() {
      arm = true;
    }

    @Override
    Optional<PreparedModifyRequest> tryBeginModify() {
      if (arm) {
        arm = false;
        applyModifyResult(new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(1), null));
      }
      return super.tryBeginModify();
    }
  }

  private static final class DetachOnCreateRequestItem extends OpcUaMonitoredItem {

    private boolean arm;

    DetachOnCreateRequestItem(NodeId nodeId) {
      super(valueReadId(nodeId));
    }

    void armDetachOnNextCreateRequest() {
      arm = true;
    }

    @Override
    Optional<PreparedCreateRequest> tryBeginCreate() {
      if (arm) {
        arm = false;
        setClientHandle(null);
      }
      return super.tryBeginCreate();
    }
  }

  private static final class CountingMonitoredItemServiceSet
      extends DelegatingMonitoredItemServiceSet {

    final AtomicInteger createCalls = new AtomicInteger();
    final AtomicInteger modifyCalls = new AtomicInteger();
    final AtomicInteger deleteCalls = new AtomicInteger();

    private final AtomicBoolean gateNextCreate = new AtomicBoolean(false);
    private final AtomicBoolean failGatedCreate = new AtomicBoolean(false);
    private final CountDownLatch createArrived = new CountDownLatch(1);
    private final CountDownLatch createGate = new CountDownLatch(1);

    CountingMonitoredItemServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateNextCreate(boolean failAfterRelease) {
      failGatedCreate.set(failAfterRelease);
      gateNextCreate.set(true);
    }

    void awaitCreateArrived() throws InterruptedException {
      assertTrue(
          createArrived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
          "gated CreateMonitoredItems request never arrived");
    }

    void releaseCreate() {
      createGate.countDown();
    }

    @Override
    public CreateMonitoredItemsResponse onCreateMonitoredItems(
        ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {

      createCalls.incrementAndGet();

      if (gateNextCreate.compareAndSet(true, false)) {
        createArrived.countDown();

        try {
          if (!createGate.await(30, TimeUnit.SECONDS)) {
            throw new UaException(StatusCodes.Bad_Timeout, "create gate was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        }

        if (failGatedCreate.get()) {
          throw new UaException(
              StatusCodes.Bad_UnexpectedError, "scripted CreateMonitoredItems failure");
        }
      }

      return super.onCreateMonitoredItems(context, request);
    }

    @Override
    public ModifyMonitoredItemsResponse onModifyMonitoredItems(
        ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

      modifyCalls.incrementAndGet();
      return super.onModifyMonitoredItems(context, request);
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
    final CountingMonitoredItemServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new CountingMonitoredItemServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(10_000)));
      client.connect();
    }

    int serverMonitoredItemCount(UInteger subscriptionId) {
      return server.getSubscriptions().get(subscriptionId).getMonitoredItemCount().intValue();
    }

    @Override
    public void close() throws Exception {
      serviceSet.releaseCreate();
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
