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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.ServerState;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link OpcUaSubscription#setMonitoringMode(MonitoringMode, List)} updates {@link
 * OpcUaMonitoredItem#getServerState()} atomically with {@link
 * OpcUaMonitoredItem#getMonitoringMode()} on Good operation results and leaves it unchanged on
 * failures.
 */
public class MonitoredItemServerStateMonitoringModeTest {

  /**
   * Part 4 §5.12.4: after a successful SetMonitoringMode call, ServerState.getMonitoringMode() must
   * immediately match both the Server and item.getMonitoringMode(), without waiting for a later
   * ModifyMonitoredItems call to rebuild ServerState.
   */
  @Test
  void setMonitoringModeUpdatesServerStateImmediatelyWithoutSubsequentModify() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      item.setSamplingInterval(500.0);
      item.setQueueSize(uint(3));
      item.setDiscardOldest(false);

      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      ServerState initial = item.getServerState().orElseThrow();
      assertEquals(MonitoringMode.Reporting, initial.getMonitoringMode());
      assertEquals(MonitoringMode.Reporting, fixture.serverMonitoringMode(subscription, item));

      for (MonitoringMode nextMode :
          List.of(MonitoringMode.Sampling, MonitoringMode.Disabled, MonitoringMode.Reporting)) {
        List<MonitoredItemServiceOperationResult> results =
            subscription.setMonitoringMode(nextMode, List.of(item));

        assertEquals(1, results.size());
        assertTrue(results.get(0).isGood());
        assertEquals(StatusCode.GOOD, item.getSetMonitoringModeResult().orElseThrow());
        assertEquals(nextMode, fixture.serverMonitoringMode(subscription, item));
        assertEquals(nextMode, item.getMonitoringMode());

        ServerState updated = item.getServerState().orElseThrow();
        assertEquals(
            nextMode,
            updated.getMonitoringMode(),
            "ServerState.getMonitoringMode() must reflect the mode accepted by SetMonitoringMode");
        assertEquals(initial.getMonitoredItemId(), updated.getMonitoredItemId());
        assertEquals(initial.getSamplingInterval(), updated.getSamplingInterval());
        assertEquals(initial.getQueueSize(), updated.getQueueSize());
        assertEquals(initial.getDiscardOldest(), updated.getDiscardOldest());
      }

      subscription.delete();
    }
  }

  /**
   * Control: when SetMonitoringMode returns a Bad operation-level StatusCode, ServerState and
   * getMonitoringMode() must retain the previous mode.
   */
  @Test
  void refusedSetMonitoringModeLeavesServerStateUnchanged() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.refuseNextOperation.set(true);

      List<MonitoredItemServiceOperationResult> results =
          subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item));

      assertEquals(1, results.size());
      assertEquals(StatusCode.GOOD, results.get(0).serviceResult());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied, results.get(0).operationResult().orElseThrow().value());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          item.getSetMonitoringModeResult().orElseThrow().value());
      assertEquals(MonitoringMode.Reporting, fixture.serverMonitoringMode(subscription, item));
      assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());

      subscription.delete();
    }
  }

  /**
   * Control: when SetMonitoringMode fails with a service-level fault, ServerState and
   * getMonitoringMode() must retain the previous mode.
   */
  @Test
  void serviceFaultSetMonitoringModeLeavesServerStateUnchanged() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.failNextService.set(true);

      List<MonitoredItemServiceOperationResult> results =
          subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item));

      assertEquals(1, results.size());
      assertEquals(StatusCodes.Bad_UnexpectedError, results.get(0).serviceResult().value());
      assertTrue(results.get(0).operationResult().isEmpty());
      assertEquals(
          StatusCodes.Bad_UnexpectedError, item.getSetMonitoringModeResult().orElseThrow().value());
      assertEquals(MonitoringMode.Reporting, fixture.serverMonitoringMode(subscription, item));
      assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());

      subscription.delete();
    }
  }

  /**
   * Deleting an item while SetMonitoringMode is in flight must be deferred until the mode request
   * finishes: deleteMonitoredItems() and synchronizeMonitoredItems() report Bad_InvalidState with
   * no server delete call and leave the item pending deletion, and the next delete succeeds cleanly
   * once SetMonitoringMode completes.
   */
  @Test
  void deleteIsDeferredWhileSetMonitoringModeIsInFlightAndSucceedsAfterGoodResponse()
      throws Exception {

    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.gateNextSetMonitoringMode(false);
      CompletableFuture<List<MonitoredItemServiceOperationResult>> modeCall =
          CompletableFuture.supplyAsync(
              () -> subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item)));

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        subscription.removeMonitoredItem(item);

        int deleteCallsBefore = fixture.serviceSet.deleteCalls.get();
        List<MonitoredItemServiceOperationResult> deferredDelete =
            subscription.deleteMonitoredItems();
        assertEquals(1, deferredDelete.size());
        assertEquals(StatusCodes.Bad_InvalidState, deferredDelete.get(0).serviceResult().value());
        assertTrue(deferredDelete.get(0).operationResult().isEmpty());
        assertEquals(deleteCallsBefore, fixture.serviceSet.deleteCalls.get());
        assertFalse(subscription.isMonitoredItemsSynchronized());
        assertEquals(1, fixture.serverMonitoredItemCount(subscriptionId));

        MonitoredItemSynchronizationException ex =
            assertThrows(
                MonitoredItemSynchronizationException.class,
                subscription::synchronizeMonitoredItems);
        assertEquals(deleteCallsBefore, fixture.serviceSet.deleteCalls.get());
        assertEquals(1, ex.getDeleteResults().size());
        assertEquals(
            StatusCodes.Bad_InvalidState, ex.getDeleteResults().get(0).serviceResult().value());
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      List<MonitoredItemServiceOperationResult> modeResults = modeCall.get(10, TimeUnit.SECONDS);
      assertEquals(1, modeResults.size());
      assertTrue(modeResults.get(0).isGood());
      assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());

      // Once SetMonitoringMode finishes, deletion proceeds cleanly.
      subscription.synchronizeMonitoredItems();
      assertEquals(0, fixture.serverMonitoredItemCount(subscriptionId));
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertTrue(item.getClientHandle().isEmpty());

      subscription.delete();
    }
  }

  /**
   * Deleting an item while a failing SetMonitoringMode is in flight must stay deferred until the
   * service fault releases the reservation, after which the next delete cleans up the item.
   */
  @Test
  void deleteIsDeferredWhileSetMonitoringModeIsInFlightAndSucceedsAfterServiceFault()
      throws Exception {

    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.gateNextSetMonitoringMode(true);
      CompletableFuture<List<MonitoredItemServiceOperationResult>> modeCall =
          CompletableFuture.supplyAsync(
              () -> subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item)));

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        subscription.removeMonitoredItem(item);

        int deleteCallsBefore = fixture.serviceSet.deleteCalls.get();
        List<MonitoredItemServiceOperationResult> deferredDelete =
            subscription.deleteMonitoredItems();
        assertEquals(1, deferredDelete.size());
        assertEquals(StatusCodes.Bad_InvalidState, deferredDelete.get(0).serviceResult().value());
        assertEquals(deleteCallsBefore, fixture.serviceSet.deleteCalls.get());
        assertFalse(subscription.isMonitoredItemsSynchronized());
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      List<MonitoredItemServiceOperationResult> modeResults = modeCall.get(10, TimeUnit.SECONDS);
      assertEquals(1, modeResults.size());
      assertEquals(StatusCodes.Bad_UnexpectedError, modeResults.get(0).serviceResult().value());
      assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());

      // Once the service fault releases setMonitoringModeInFlight, deletion succeeds cleanly.
      subscription.synchronizeMonitoredItems();
      assertEquals(0, fixture.serverMonitoredItemCount(subscriptionId));
      assertTrue(subscription.isMonitoredItemsSynchronized());
      assertTrue(item.getClientHandle().isEmpty());

      subscription.delete();
    }
  }

  /**
   * Overlapping SetMonitoringMode calls for the same item must not both execute concurrently and
   * risk applying responses out of order: while the first call is in flight, a second call must be
   * rejected with Bad_InvalidState without sending a competing server request, leaving client,
   * ServerState, and Server consistent after the first call completes and ready for a follow-up
   * call.
   */
  @Test
  void overlappingSetMonitoringModeIsRejectedAndLeavesModeConsistent() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.gateNextSetMonitoringMode(false);
      CompletableFuture<List<MonitoredItemServiceOperationResult>> firstCall =
          CompletableFuture.supplyAsync(
              () -> subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item)));

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        List<MonitoredItemServiceOperationResult> overlapping =
            subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item));
        assertEquals(1, overlapping.size());
        assertEquals(StatusCodes.Bad_InvalidState, overlapping.get(0).serviceResult().value());
        assertTrue(overlapping.get(0).operationResult().isEmpty());
        assertEquals(
            1,
            fixture.serviceSet.setMonitoringModeCalls.get(),
            "overlapping SetMonitoringMode must not send a competing request on the wire");

        fixture.serviceSet.releaseSetMonitoringMode();

        List<MonitoredItemServiceOperationResult> firstResults =
            firstCall.get(10, TimeUnit.SECONDS);
        assertEquals(1, firstResults.size());
        assertTrue(firstResults.get(0).isGood());

        assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
        assertEquals(
            MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
        assertEquals(MonitoringMode.Sampling, fixture.serverMonitoringMode(subscription, item));

        // After the first call finishes and releases its reservation, Disabled succeeds cleanly.
        List<MonitoredItemServiceOperationResult> secondResults =
            subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item));
        assertEquals(1, secondResults.size());
        assertTrue(secondResults.get(0).isGood());
        assertEquals(MonitoringMode.Disabled, item.getMonitoringMode());
        assertEquals(
            MonitoringMode.Disabled, item.getServerState().orElseThrow().getMonitoringMode());
        assertEquals(MonitoringMode.Disabled, fixture.serverMonitoringMode(subscription, item));
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      subscription.delete();
    }
  }

  /**
   * When Subscription A invokes setMonitoringMode() across partitions and blocks on Partition 1
   * while an item from Partition 2 is deleted from A and recreated on Subscription B, A must
   * revalidate membership under monitoredItemsLock, report Bad_InvalidState for the moved item,
   * send no SetMonitoringMode request for it on A, and leave B's item untouched.
   */
  @Test
  void setMonitoringModeSkipsItemMovedToAnotherSubscriptionBetweenPartitions() throws Exception {
    try (var fixture = new Fixture()) {
      var subscriptionA = new OpcUaSubscription(fixture.client);
      subscriptionA.setMaxMonitoredItemsPerCall(uint(1));
      subscriptionA.create();

      var subscriptionB = new OpcUaSubscription(fixture.client);
      subscriptionB.create();

      OpcUaMonitoredItem item1 =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      OpcUaMonitoredItem item2 =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_State, MonitoringMode.Reporting);
      subscriptionA.addMonitoredItems(List.of(item1, item2));
      subscriptionA.synchronizeMonitoredItems();

      fixture.serviceSet.gateNextSetMonitoringMode(false);
      CompletableFuture<List<MonitoredItemServiceOperationResult>> modeCallA =
          CompletableFuture.supplyAsync(
              () ->
                  subscriptionA.setMonitoringMode(MonitoringMode.Disabled, List.of(item1, item2)));

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        // While Partition 1 (item1) is in flight on A, move item2 from A to B.
        subscriptionA.removeMonitoredItem(item2);
        List<MonitoredItemServiceOperationResult> deletedFromA =
            subscriptionA.deleteMonitoredItems();
        assertEquals(1, deletedFromA.size());
        assertTrue(deletedFromA.get(0).isGood());

        subscriptionB.addMonitoredItem(item2);
        List<MonitoredItemServiceOperationResult> createdInB = subscriptionB.createMonitoredItems();
        assertEquals(1, createdInB.size());
        assertTrue(createdInB.get(0).isGood());
        assertEquals(
            uint(1),
            item2.getMonitoredItemId().orElseThrow(),
            "pins id overlap: Subscription B assigns MonitoredItemId 1");

        fixture.serviceSet.releaseSetMonitoringMode();

        List<MonitoredItemServiceOperationResult> resultsA = modeCallA.get(10, TimeUnit.SECONDS);

        assertEquals(
            1,
            fixture.serviceSet.setMonitoringModeCalls.get(),
            "Subscription A must not send SetMonitoringMode for an item moved to B");
        assertEquals(2, resultsA.size());
        assertTrue(resultsA.get(0).isGood());
        assertEquals(StatusCodes.Bad_InvalidState, resultsA.get(1).serviceResult().value());
        assertTrue(resultsA.get(1).operationResult().isEmpty());

        assertEquals(MonitoringMode.Disabled, item1.getMonitoringMode());
        assertEquals(
            MonitoringMode.Disabled, item1.getServerState().orElseThrow().getMonitoringMode());
        assertEquals(MonitoringMode.Disabled, fixture.serverMonitoringMode(subscriptionA, item1));

        // Subscription B's item must remain Reporting on client, ServerState, and Server.
        assertEquals(MonitoringMode.Reporting, item2.getMonitoringMode());
        assertEquals(
            MonitoringMode.Reporting, item2.getServerState().orElseThrow().getMonitoringMode());
        assertEquals(MonitoringMode.Reporting, fixture.serverMonitoringMode(subscriptionB, item2));
        assertTrue(item2.getSetMonitoringModeResult().isEmpty());
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      subscriptionA.delete();
      subscriptionB.delete();
    }
  }

  /**
   * Mutating the caller-supplied List while setMonitoringMode() is in flight must not redirect
   * result application or reservation release to another item or strand setMonitoringModeInFlight
   * on the original item.
   */
  @Test
  void mutatingCallerListDuringSetMonitoringModeDoesNotCorruptStateOrStrandReservation()
      throws Exception {

    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem itemA =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      OpcUaMonitoredItem itemB =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_State, MonitoringMode.Reporting);
      subscription.addMonitoredItems(List.of(itemA, itemB));
      subscription.synchronizeMonitoredItems();

      var callerList = new ArrayList<>(List.of(itemA));

      fixture.serviceSet.gateNextSetMonitoringMode(false);
      CompletableFuture<List<MonitoredItemServiceOperationResult>> modeCall =
          CompletableFuture.supplyAsync(
              () -> subscription.setMonitoringMode(MonitoringMode.Sampling, callerList));

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        // Mutate the caller's list in place while the service call is in flight.
        callerList.set(0, itemB);
        callerList.clear();

        fixture.serviceSet.releaseSetMonitoringMode();

        List<MonitoredItemServiceOperationResult> results = modeCall.get(10, TimeUnit.SECONDS);
        assertEquals(1, results.size());
        assertEquals(itemA, results.get(0).monitoredItem());
        assertTrue(results.get(0).isGood());

        // itemA updated and its reservation released; itemB untouched.
        assertEquals(MonitoringMode.Sampling, itemA.getMonitoringMode());
        assertEquals(
            MonitoringMode.Sampling, itemA.getServerState().orElseThrow().getMonitoringMode());
        assertEquals(MonitoringMode.Reporting, itemB.getMonitoringMode());
        assertEquals(
            MonitoringMode.Reporting, itemB.getServerState().orElseThrow().getMonitoringMode());
        assertTrue(itemB.getSetMonitoringModeResult().isEmpty());

        // Confirm itemA's mode reservation was released cleanly.
        List<MonitoredItemServiceOperationResult> followUp =
            subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(itemA));
        assertEquals(1, followUp.size());
        assertTrue(followUp.get(0).isGood());
        assertEquals(MonitoringMode.Disabled, itemA.getMonitoringMode());
        assertEquals(
            MonitoringMode.Disabled, itemA.getServerState().orElseThrow().getMonitoringMode());
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      subscription.delete();
    }
  }

  /**
   * Interrupting the thread waiting on setMonitoringMode() while the server request is still active
   * must keep setMonitoringModeInFlight held until the async response completes and applies, so an
   * overlapping mode change cannot race on the wire or leave client and Server diverged.
   */
  @Test
  void interruptingSetMonitoringModeKeepsReservationHeldUntilAsyncResponseCompletes()
      throws Exception {

    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.gateNextSetMonitoringMode(false);

      var callerResults = new AtomicReference<List<MonitoredItemServiceOperationResult>>();
      var callerInterrupted = new AtomicBoolean(false);
      Thread caller =
          new Thread(
              () -> {
                callerResults.set(
                    subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item)));
                callerInterrupted.set(Thread.currentThread().isInterrupted());
              });
      caller.start();

      try {
        fixture.serviceSet.awaitSetMonitoringModeArrived();

        caller.interrupt();
        caller.join(TimeUnit.SECONDS.toMillis(10));
        assertFalse(caller.isAlive());
        assertTrue(callerInterrupted.get());
        assertEquals(1, callerResults.get().size());
        assertEquals(
            StatusCodes.Bad_UnexpectedError, callerResults.get().get(0).serviceResult().value());

        // Reservation must still be held while the server request is active.
        List<MonitoredItemServiceOperationResult> overlapping =
            subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item));
        assertEquals(1, overlapping.size());
        assertEquals(StatusCodes.Bad_InvalidState, overlapping.get(0).serviceResult().value());
        assertEquals(1, fixture.serviceSet.setMonitoringModeCalls.get());
      } finally {
        fixture.serviceSet.releaseSetMonitoringMode();
      }

      // Async completion handler must apply Sampling and release setMonitoringModeInFlight; wait
      // on ServerState since it is the final volatile write in applySetMonitoringModeResultLocked.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
      while (item.getServerState()
                  .map(OpcUaMonitoredItem.ServerState::getMonitoringMode)
                  .orElse(null)
              != MonitoringMode.Sampling
          && System.nanoTime() < deadline) {
        Thread.yield();
      }
      assertEquals(
          MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
      assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
      assertEquals(MonitoringMode.Sampling, fixture.serverMonitoringMode(subscription, item));

      // Follow-up mode change succeeds cleanly once the async completion releases the reservation.
      List<MonitoredItemServiceOperationResult> followUp =
          subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item));
      assertEquals(1, followUp.size());
      assertTrue(followUp.get(0).isGood());
      assertEquals(MonitoringMode.Disabled, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Disabled, item.getServerState().orElseThrow().getMonitoringMode());

      subscription.delete();
    }
  }

  /**
   * Passing the same MonitoredItem twice in setMonitoringMode() must produce identical results and
   * side effects regardless of partition size, both on success and when a one-shot service fault
   * occurs.
   */
  @Test
  void duplicateItemsInSetMonitoringModeProduceIdenticalResultsAcrossPartitionSizes()
      throws Exception {

    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      // Partition size 1: deduplication runs before partitioning, sending a single server call.
      int callsBeforeSizeOne = fixture.serviceSet.setMonitoringModeCalls.get();
      subscription.setMaxMonitoredItemsPerCall(uint(1));
      List<MonitoredItemServiceOperationResult> sizeOneResults =
          subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item, item));

      assertEquals(callsBeforeSizeOne + 1, fixture.serviceSet.setMonitoringModeCalls.get());
      assertEquals(2, sizeOneResults.size());
      for (MonitoredItemServiceOperationResult result : sizeOneResults) {
        assertEquals(item, result.monitoredItem());
        assertTrue(result.isGood());
        assertEquals(StatusCode.GOOD, result.operationResult().orElseThrow());
      }
      assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
      assertEquals(MonitoringMode.Sampling, fixture.serverMonitoringMode(subscription, item));

      // One-shot service fault at partition size 1 must return [Bad, Bad] and leave mode unchanged.
      int callsBeforeFaultOne = fixture.serviceSet.setMonitoringModeCalls.get();
      fixture.serviceSet.failNextService.set(true);
      List<MonitoredItemServiceOperationResult> faultSizeOne =
          subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item, item));

      assertEquals(callsBeforeFaultOne + 1, fixture.serviceSet.setMonitoringModeCalls.get());
      assertEquals(2, faultSizeOne.size());
      for (MonitoredItemServiceOperationResult result : faultSizeOne) {
        assertEquals(item, result.monitoredItem());
        assertEquals(StatusCodes.Bad_UnexpectedError, result.serviceResult().value());
        assertTrue(result.operationResult().isEmpty());
      }
      assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
      assertEquals(MonitoringMode.Sampling, fixture.serverMonitoringMode(subscription, item));

      // One-shot service fault at partition size 2 must match partition size 1 identically.
      int callsBeforeFaultTwo = fixture.serviceSet.setMonitoringModeCalls.get();
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      fixture.serviceSet.failNextService.set(true);
      List<MonitoredItemServiceOperationResult> faultSizeTwo =
          subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item, item));

      assertEquals(callsBeforeFaultTwo + 1, fixture.serviceSet.setMonitoringModeCalls.get());
      assertEquals(2, faultSizeTwo.size());
      for (MonitoredItemServiceOperationResult result : faultSizeTwo) {
        assertEquals(item, result.monitoredItem());
        assertEquals(StatusCodes.Bad_UnexpectedError, result.serviceResult().value());
        assertTrue(result.operationResult().isEmpty());
      }
      assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
      assertEquals(MonitoringMode.Sampling, fixture.serverMonitoringMode(subscription, item));

      // Partition size 2 on success: identical (Good, Good) results with a single server call.
      int callsBeforeSizeTwo = fixture.serviceSet.setMonitoringModeCalls.get();
      List<MonitoredItemServiceOperationResult> sizeTwoResults =
          subscription.setMonitoringMode(MonitoringMode.Disabled, List.of(item, item));

      assertEquals(callsBeforeSizeTwo + 1, fixture.serviceSet.setMonitoringModeCalls.get());
      assertEquals(2, sizeTwoResults.size());
      for (MonitoredItemServiceOperationResult result : sizeTwoResults) {
        assertEquals(item, result.monitoredItem());
        assertTrue(result.isGood());
        assertEquals(StatusCode.GOOD, result.operationResult().orElseThrow());
      }
      assertEquals(MonitoringMode.Disabled, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Disabled, item.getServerState().orElseThrow().getMonitoringMode());
      assertEquals(MonitoringMode.Disabled, fixture.serverMonitoringMode(subscription, item));

      // Confirm the reservation was released cleanly after the deduplicated call.
      List<MonitoredItemServiceOperationResult> followUp =
          subscription.setMonitoringMode(MonitoringMode.Reporting, List.of(item));
      assertEquals(1, followUp.size());
      assertTrue(followUp.get(0).isGood());
      assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
      assertEquals(
          MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());

      subscription.delete();
    }
  }

  private static final class ScriptableMonitoringModeServiceSet
      extends DelegatingMonitoredItemServiceSet {

    final AtomicBoolean refuseNextOperation = new AtomicBoolean(false);
    final AtomicBoolean failNextService = new AtomicBoolean(false);
    final AtomicInteger setMonitoringModeCalls = new AtomicInteger();
    final AtomicInteger deleteCalls = new AtomicInteger();
    private final AtomicBoolean gateNextSetMonitoringMode = new AtomicBoolean(false);
    private final AtomicBoolean failGatedSetMonitoringMode = new AtomicBoolean(false);
    private final CountDownLatch modeArrived = new CountDownLatch(1);
    private final CountDownLatch modeGate = new CountDownLatch(1);

    ScriptableMonitoringModeServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateNextSetMonitoringMode(boolean failAfterRelease) {
      failGatedSetMonitoringMode.set(failAfterRelease);
      gateNextSetMonitoringMode.set(true);
    }

    void awaitSetMonitoringModeArrived() throws InterruptedException {
      assertTrue(
          modeArrived.await(10, TimeUnit.SECONDS), "gated SetMonitoringMode request never arrived");
    }

    void releaseSetMonitoringMode() {
      modeGate.countDown();
    }

    @Override
    public SetMonitoringModeResponse onSetMonitoringMode(
        ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException {

      setMonitoringModeCalls.incrementAndGet();

      if (gateNextSetMonitoringMode.compareAndSet(true, false)) {
        SetMonitoringModeResponse response =
            failGatedSetMonitoringMode.get() ? null : super.onSetMonitoringMode(context, request);

        modeArrived.countDown();
        try {
          if (!modeGate.await(30, TimeUnit.SECONDS)) {
            throw new UaException(StatusCodes.Bad_Timeout, "mode gate was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        }

        if (failGatedSetMonitoringMode.get()) {
          throw new UaException(
              StatusCodes.Bad_UnexpectedError, "scripted late SetMonitoringMode service fault");
        }

        return response;
      }

      if (failNextService.compareAndSet(true, false)) {
        throw new UaException(
            StatusCodes.Bad_UnexpectedError, "scripted SetMonitoringMode service fault");
      }

      if (refuseNextOperation.compareAndSet(true, false)) {
        UInteger[] ids = request.getMonitoredItemIds();
        var results = new StatusCode[ids == null ? 0 : ids.length];
        Arrays.fill(results, new StatusCode(StatusCodes.Bad_UserAccessDenied));

        var header =
            new ResponseHeader(
                DateTime.now(),
                request.getRequestHeader().getRequestHandle(),
                StatusCode.GOOD,
                null,
                null,
                null);

        return new SetMonitoringModeResponse(header, results, null);
      }

      return super.onSetMonitoringMode(context, request);
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
    final ScriptableMonitoringModeServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new ScriptableMonitoringModeServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(10_000)));
      client.connect();
    }

    MonitoringMode serverMonitoringMode(OpcUaSubscription subscription, OpcUaMonitoredItem item) {
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();
      UInteger monitoredItemId = item.getMonitoredItemId().orElseThrow();

      return server
          .getSubscriptions()
          .get(subscriptionId)
          .getMonitoredItems()
          .get(monitoredItemId)
          .getMonitoringMode();
    }

    int serverMonitoredItemCount(UInteger subscriptionId) {
      return server.getSubscriptions().get(subscriptionId).getMonitoredItemCount().intValue();
    }

    @Override
    public void close() throws Exception {
      serviceSet.releaseSetMonitoringMode();
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
