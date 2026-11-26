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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.PreparedCreateRequest;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.PreparedModifyRequest;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link OpcUaMonitoredItem} parameter setters and lifecycle/result transitions
 * serialize cleanly and preserve invariants between {@link SyncState}, {@code ServerState}, and
 * pending modifications.
 */
public class OpcUaMonitoredItemStateConcurrencyTest {

  private static final int RACE_ROUNDS = 2_000;
  private static final long TIMEOUT_SECONDS = 10;

  private final ExecutorService workers = Executors.newFixedThreadPool(2);

  @AfterEach
  void stopWorkers() throws InterruptedException {
    workers.shutdownNow();
    assertTrue(workers.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS));
  }

  @Nested
  class SetterRaces {

    /**
     * Parameter setters racing {@link OpcUaMonitoredItem#reset()} must neither throw a
     * NullPointerException on cleared pending modifications nor overwrite INITIAL back to
     * UNSYNCHRONIZED when ServerState has already been cleared.
     */
    @Test
    void settersRacingResetNeverThrowOrLeaveUnsynchronizedWithoutServerState() throws Exception {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));

      var filter =
          new DataChangeFilter(
              DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

      for (int round = 0; round < RACE_ROUNDS; round++) {
        item.applyCreateResult(createdResult(round + 1));

        var start = new CyclicBarrier(2);
        var setterFailure = new AtomicReference<Throwable>();
        int step = round;

        Future<?> setterTask =
            workers.submit(
                () -> {
                  try {
                    start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    applySetter(item, step, filter);
                  } catch (Throwable t) {
                    setterFailure.compareAndSet(null, t);
                  }
                });

        start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        item.reset();

        setterTask.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNull(
            setterFailure.get(),
            "round " + round + ": parameter setter threw while racing reset()");
        assertEquals(
            SyncState.INITIAL,
            item.getSyncState(),
            "round " + round + ": reset() must not leave item UNSYNCHRONIZED without ServerState");
        assertTrue(
            item.getServerState().isEmpty(), "round " + round + ": reset() must clear ServerState");
      }
    }

    /**
     * Parameter setters racing a Good {@link OpcUaMonitoredItem#applyModifyResult} must neither
     * throw a NullPointerException when modifications are cleared nor strand the item
     * UNSYNCHRONIZED with no pending Modifications.
     */
    @Test
    void settersRacingApplyModifyResultNeverThrowOrStrandWithoutModifications() throws Exception {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));
      item.applyCreateResult(createdResult(1));

      var filter =
          new DataChangeFilter(
              DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

      for (int round = 0; round < RACE_ROUNDS; round++) {
        // Ensure a pending modification exists before the modify result is applied.
        item.setQueueSize(uint(2 + (round % 5)));

        var start = new CyclicBarrier(2);
        var setterFailure = new AtomicReference<Throwable>();
        int step = round;

        Future<?> setterTask =
            workers.submit(
                () -> {
                  try {
                    start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    applySetter(item, step, filter);
                  } catch (Throwable t) {
                    setterFailure.compareAndSet(null, t);
                  }
                });

        start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        item.applyModifyResult(
            new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(2), null));

        setterTask.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNull(
            setterFailure.get(),
            "round " + round + ": parameter setter threw while racing applyModifyResult()");

        if (item.getSyncState() == SyncState.UNSYNCHRONIZED) {
          MonitoredItemModifyRequest request = item.tryBeginModify().orElseThrow().toRequest();
          assertNotNull(
              request,
              "round "
                  + round
                  + ": an UNSYNCHRONIZED item must always have pending modifications to build a"
                  + " ModifyMonitoredItems request");
        }
      }
    }

    /**
     * Parameter setters racing {@link OpcUaMonitoredItem#applyDeleteResult(StatusCode)} must
     * neither throw a NullPointerException nor leave the deleted item UNSYNCHRONIZED with no
     * ServerState.
     */
    @Test
    void settersRacingApplyDeleteResultNeverThrowOrStrandDeletedItem() throws Exception {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      var filter =
          new DataChangeFilter(
              DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

      for (int round = 0; round < RACE_ROUNDS; round++) {
        item.setClientHandle(uint(1));
        item.applyCreateResult(createdResult(round + 1));

        var start = new CyclicBarrier(2);
        var setterFailure = new AtomicReference<Throwable>();
        int step = round;

        Future<?> setterTask =
            workers.submit(
                () -> {
                  try {
                    start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    applySetter(item, step, filter);
                  } catch (Throwable t) {
                    setterFailure.compareAndSet(null, t);
                  }
                });

        start.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        item.applyDeleteResult(StatusCode.GOOD);

        setterTask.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNull(
            setterFailure.get(),
            "round " + round + ": parameter setter threw while racing applyDeleteResult()");
        assertEquals(
            SyncState.INITIAL,
            item.getSyncState(),
            "round " + round + ": deleted item must stay INITIAL");
        assertTrue(item.getServerState().isEmpty());
        assertTrue(item.getClientHandle().isEmpty());
      }
    }
  }

  @Nested
  class DefensiveStateTransitions {

    /**
     * If a MonitoredItem is reset or deleted while a ModifyMonitoredItems response is still in
     * flight, applying that late Good or Bad result must not throw a NullPointerException on the
     * cleared ServerState or flip the INITIAL item back to UNSYNCHRONIZED.
     */
    @Test
    void applyModifyResultAfterResetDoesNotThrowOrMarkUnsynchronized() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));
      item.applyCreateResult(createdResult(1));
      item.setSamplingInterval(250.0);
      item.reset();

      item.applyModifyResult(new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(1), null));
      assertEquals(SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getServerState().isEmpty());

      item.applyModifyResult(
          new MonitoredItemModifyResult(
              new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid), 0.0, uint(0), null));
      assertEquals(SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getServerState().isEmpty());
    }

    /**
     * A failed ModifyMonitoredItems result must keep pending modifications present so a subsequent
     * synchronization pass can retry rather than failing with "no pending modification".
     */
    @Test
    void badModifyResultWithoutPendingModificationsStillBuildsModifyRequest() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));
      item.applyCreateResult(createdResult(7));

      item.applyModifyResult(
          new MonitoredItemModifyResult(
              new StatusCode(StatusCodes.Bad_UserAccessDenied), 0.0, uint(0), null));

      assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());
      MonitoredItemModifyRequest request = item.tryBeginModify().orElseThrow().toRequest();
      assertEquals(uint(7), request.getMonitoredItemId());
      assertEquals(
          item.getSamplingInterval(), request.getRequestedParameters().getSamplingInterval());
    }

    /**
     * Request builders used during synchronization must report empty rather than throwing when an
     * item's handle, ServerState, or pending modifications are absent.
     */
    @Test
    void tryBeginRequestBuildersReturnEmptyWhenPreconditionsAreMissing() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

      assertTrue(
          item.tryBeginCreate().isEmpty(),
          "cannot build CreateMonitoredItems request without a ClientHandle");
      assertTrue(
          item.tryBeginModify().isEmpty(),
          "cannot build ModifyMonitoredItems request without ServerState");

      item.setClientHandle(uint(1));
      PreparedCreateRequest preparedCreate = item.tryBeginCreate().orElseThrow();
      assertNotNull(
          preparedCreate.toRequest(),
          "control: INITIAL item with ClientHandle builds CreateMonitoredItems request");

      item.applyCreateResult(createdResult(10), preparedCreate);
      assertTrue(
          item.tryBeginCreate().isEmpty(),
          "already-created item must not build a duplicate CreateMonitoredItems request");
      assertTrue(
          item.tryBeginModify().isEmpty(),
          "SYNCHRONIZED item without pending modifications returns empty");

      item.setSamplingInterval(500.0);
      PreparedModifyRequest preparedModify = item.tryBeginModify().orElseThrow();
      assertNotNull(
          preparedModify.toRequest(),
          "control: UNSYNCHRONIZED item with ServerState builds ModifyMonitoredItems request");
    }

    /**
     * While a CreateMonitoredItems request is in flight, a second concurrent create must not claim
     * the same item and detachIfNotCreated() must keep a pending removal queued until the create
     * finishes or its reservation is released.
     */
    @Test
    void createInFlightBlocksDuplicateCreateAndPrematureDetachUntilReleased() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));

      PreparedCreateRequest first = item.tryBeginCreate().orElseThrow();

      assertTrue(
          item.tryBeginCreate().isEmpty(),
          "a second concurrent create must not claim an item whose create is already in flight");
      assertFalse(
          item.detachIfNotCreated(),
          "detachIfNotCreated() must not clear the handle while a create is in flight");
      assertEquals(uint(1), item.getClientHandle().orElseThrow());
      assertTrue(
          item.needsServerDeletion(),
          "an item removed while create is in flight cannot be treated as synchronized yet");

      // Releasing the reservation (e.g. after a service fault) allows retry or detach.
      item.releaseCreate(first);
      assertFalse(item.needsServerDeletion());

      PreparedCreateRequest retry = item.tryBeginCreate().orElseThrow();
      item.releaseCreate(retry);

      assertTrue(item.detachIfNotCreated());
      assertTrue(item.getClientHandle().isEmpty());
    }

    /**
     * A CreateMonitoredItems response captured before reset() must not install a ServerState on the
     * reset item.
     */
    @Test
    void applyCreateResultForStaleIncarnationIsIgnored() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));

      PreparedCreateRequest prepared = item.tryBeginCreate().orElseThrow();
      item.reset();

      item.applyCreateResult(createdResult(99), prepared);

      assertEquals(SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getServerState().isEmpty());
      assertTrue(item.getCreateResult().isEmpty());
    }

    /**
     * Detaching uncreated items during deleteMonitoredItems must drop INITIAL items and preserve
     * items that already exist on the Server.
     */
    @Test
    void detachIfNotCreatedDetachesInitialItemAndPreservesCreatedItem() {
      var uncreated = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      uncreated.setClientHandle(uint(1));

      assertTrue(uncreated.detachIfNotCreated());
      assertTrue(uncreated.getClientHandle().isEmpty());
      assertEquals(SyncState.INITIAL, uncreated.getSyncState());

      var created = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      created.setClientHandle(uint(2));
      created.applyCreateResult(createdResult(11));

      assertFalse(created.detachIfNotCreated());
      assertEquals(uint(2), created.getClientHandle().orElseThrow());
      assertEquals(SyncState.SYNCHRONIZED, created.getSyncState());
    }

    /**
     * A ModifyMonitoredItems response captured for an earlier item incarnation must be ignored
     * after the item is deleted and recreated, even when the Server reuses the same MonitoredItemId
     * in another Subscription.
     */
    @Test
    void applyModifyResultForStaleIncarnationIsIgnored() {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setClientHandle(uint(1));
      item.applyCreateResult(createdResult(1));
      item.setSamplingInterval(250.0);

      PreparedModifyRequest staleModify = item.tryBeginModify().orElseThrow();

      // Delete and recreate on a new lifecycle with the same numeric MonitoredItemId=1.
      item.applyDeleteResult(StatusCode.GOOD);
      item.setClientHandle(uint(1));
      item.setSamplingInterval(500.0);
      item.applyCreateResult(
          new MonitoredItemCreateResult(StatusCode.GOOD, uint(1), 500.0, uint(1), null));
      item.setSamplingInterval(750.0);
      assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());

      item.applyModifyResult(
          new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(1), null), staleModify);

      assertEquals(
          SyncState.UNSYNCHRONIZED,
          item.getSyncState(),
          "a stale modify response from an earlier incarnation must not mark the recreated item"
              + " SYNCHRONIZED");
      assertEquals(500.0, item.getRevisedSamplingInterval().orElseThrow());
    }
  }

  private static void applySetter(OpcUaMonitoredItem item, int step, DataChangeFilter filter) {

    switch (step % 4) {
      case 0 -> item.setSamplingInterval(100.0 + (step % 10));
      case 1 -> item.setQueueSize(uint(1 + (step % 10)));
      case 2 -> item.setDiscardOldest((step & 1) == 0);
      case 3 -> item.setFilter((step & 1) == 0 ? filter : null);
      default -> throw new AssertionError();
    }
  }

  private static MonitoredItemCreateResult createdResult(int id) {
    return new MonitoredItemCreateResult(StatusCode.GOOD, uint(id), 1000.0, uint(1), null);
  }
}
