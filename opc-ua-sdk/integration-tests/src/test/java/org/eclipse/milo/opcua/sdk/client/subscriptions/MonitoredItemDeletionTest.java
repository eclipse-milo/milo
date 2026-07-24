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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Verifies the bookkeeping {@link OpcUaSubscription} keeps for MonitoredItems that have been
 * removed from the Subscription but not yet deleted from the Server.
 *
 * <p>Removing a MonitoredItem only queues it for deletion; the DeleteMonitoredItems service call
 * happens later, in {@link OpcUaSubscription#deleteMonitoredItems()}. Two things must hold for that
 * queue: a deletion that did not happen must stay queued, and a deletion that can no longer refer
 * to anything on the Server must be discarded. Neither costs anything when everything works, and
 * both are the difference between a leaked item and a wrongly deleted one when it doesn't.
 */
public class MonitoredItemDeletionTest {

  /** The queue of pending deletions after a DeleteMonitoredItems service fault. */
  @Nested
  class ServiceFault {

    /**
     * A DeleteMonitoredItems service fault leaves the MonitoredItem alive on the Server, so it must
     * stay queued for deletion: a service fault is precisely the transient failure a caller is
     * expected to retry, and the retry has to send the deletion the failed attempt did not perform.
     * Dropping it strands the item on the Server, publishing notifications that no longer route to
     * any client-side item, with no way for the application to delete it short of deleting the
     * whole Subscription.
     */
    @Test
    void failedDeleteStaysQueuedForTheNextDelete() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem item = fixture.createItem(subscription);
        UInteger monitoredItemId = item.getMonitoredItemId().orElseThrow();

        subscription.removeMonitoredItem(item);

        fixture.serviceSet.failNextDeleteMonitoredItems(1);

        List<MonitoredItemServiceOperationResult> failedResults =
            subscription.deleteMonitoredItems();

        assertEquals(1, failedResults.size());
        assertEquals(item, failedResults.get(0).monitoredItem());
        assertEquals(
            StatusCodes.Bad_TooManyOperations,
            failedResults.get(0).serviceResult().value(),
            "control: the scripted service fault reached the caller");
        assertTrue(
            failedResults.get(0).operationResult().isEmpty(),
            "control: a service fault means there is no operation-level result");

        assertFalse(
            subscription.isMonitoredItemsSynchronized(),
            "the MonitoredItem is still alive on the Server after the failed delete, so the"
                + " Subscription's MonitoredItems are not synchronized");

        // The retry must send the deletion the first attempt failed to perform.
        List<MonitoredItemServiceOperationResult> retryResults =
            subscription.deleteMonitoredItems();

        assertEquals(
            1, retryResults.size(), "the failed deletion must be retried by the next delete");
        assertEquals(item, retryResults.get(0).monitoredItem());
        assertTrue(
            retryResults.get(0).isGood(),
            "the retried deletion should succeed: " + retryResults.get(0).serviceResult());

        List<List<UInteger>> deleteRequests = fixture.serviceSet.getDeletedMonitoredItemIds();
        assertEquals(
            List.of(List.of(monitoredItemId), List.of(monitoredItemId)),
            deleteRequests,
            "the Server should have seen the deletion attempted twice");
        assertTrue(subscription.isMonitoredItemsSynchronized());
      }
    }

    /**
     * Control for {@link #failedDeleteStaysQueuedForTheNextDelete()}: when the delete succeeds the
     * item must leave the pending-deletion queue, so a later deleteMonitoredItems() is a no-op.
     * Without this, "keep failed deletions queued" could be satisfied by never dequeuing anything.
     */
    @Test
    void successfulDeleteLeavesNothingQueued() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem item = fixture.createItem(subscription);
        UInteger monitoredItemId = item.getMonitoredItemId().orElseThrow();

        subscription.removeMonitoredItem(item);

        List<MonitoredItemServiceOperationResult> results = subscription.deleteMonitoredItems();

        assertEquals(1, results.size());
        assertTrue(results.get(0).isGood());
        assertTrue(subscription.isMonitoredItemsSynchronized());

        assertEquals(List.of(), subscription.deleteMonitoredItems());
        assertEquals(
            List.of(List.of(monitoredItemId)),
            fixture.serviceSet.getDeletedMonitoredItemIds(),
            "a MonitoredItem that was deleted must not be deleted a second time");
      }
    }

    /**
     * A MonitoredItem whose deletion failed is still owned by the Subscription, so adding it back
     * has to restore it. If the failed deletion is dropped instead, the item keeps a ClientHandle
     * that no longer maps to anything, and {@code addMonitoredItem} silently ignores it: the
     * application is left holding an item it can neither delete nor use.
     */
    @Test
    void itemWhoseDeleteFailedCanBeAddedBack() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem item = fixture.createItem(subscription);

        subscription.removeMonitoredItem(item);

        fixture.serviceSet.failNextDeleteMonitoredItems(1);
        assertTrue(
            subscription.deleteMonitoredItems().get(0).serviceResult().isBad(),
            "control: the delete attempt failed");

        subscription.addMonitoredItem(item);

        assertEquals(
            List.of(item),
            subscription.getMonitoredItems(),
            "an item whose deletion failed is still alive on the Server and must be restored to"
                + " the Subscription when it is added back");
        assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, item.getSyncState());
      }
    }

    /**
     * Control for {@link #itemWhoseDeleteFailedCanBeAddedBack()}: adding back an item that is still
     * queued for deletion cancels the pending deletion. This is the path the failed-delete case has
     * to end up on, so it must work independently of any delete attempt.
     */
    @Test
    void itemQueuedForDeletionCanBeAddedBack() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem item = fixture.createItem(subscription);

        subscription.removeMonitoredItem(item);
        subscription.addMonitoredItem(item);

        assertEquals(List.of(item), subscription.getMonitoredItems());
        assertEquals(List.of(), subscription.deleteMonitoredItems());
        assertEquals(
            List.of(),
            fixture.serviceSet.getDeletedMonitoredItemIds(),
            "an item that was added back must no longer be pending deletion");
      }
    }
  }

  /** The queue of pending deletions across a Subscription's deletion and re-creation. */
  @Nested
  class SubscriptionRecreated {

    /**
     * MonitoredItemIds are scoped to a Subscription, and Milo's Server assigns them from 1 for
     * every new Subscription. A deletion queued against a Subscription that no longer exists must
     * therefore be discarded when the Subscription is reset: replaying that MonitoredItemId against
     * the replacement Subscription does not fail, it deletes whichever item happens to hold the id
     * now.
     */
    @Test
    void staleMonitoredItemIdIsNotDeletedFromTheNewSubscription() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();
        UInteger firstSubscriptionId = subscription.getSubscriptionId().orElseThrow();

        OpcUaMonitoredItem itemA = fixture.createItem(subscription);
        assertEquals(
            uint(1),
            itemA.getMonitoredItemId().orElseThrow(),
            "control: the Server assigns MonitoredItemIds from 1 within a Subscription");

        // Queue A for deletion, then destroy the Subscription it belongs to before the
        // deletion is sent.
        subscription.removeMonitoredItem(itemA);
        subscription.delete();

        subscription.create();
        UInteger secondSubscriptionId = subscription.getSubscriptionId().orElseThrow();
        assertNotEquals(firstSubscriptionId, secondSubscriptionId);

        OpcUaMonitoredItem itemB = fixture.createItem(subscription);
        assertEquals(
            uint(1),
            itemB.getMonitoredItemId().orElseThrow(),
            "pins the collision: B holds the MonitoredItemId A held in the previous Subscription");

        fixture.serviceSet.clearDeletedMonitoredItemIds();

        List<MonitoredItemServiceOperationResult> results = subscription.deleteMonitoredItems();

        assertEquals(
            List.of(),
            fixture.serviceSet.getDeletedMonitoredItemIds(),
            "a MonitoredItem belonging to a Subscription that no longer exists must not be"
                + " deleted from its replacement");
        assertEquals(
            List.of(),
            results.stream().map(r -> r.monitoredItem().getReadValueId().getNodeId()).toList(),
            "there is nothing left to delete after the Subscription was deleted");

        // B, the item that held the recycled MonitoredItemId, must still exist on the Server.
        subscription.removeMonitoredItem(itemB);
        List<MonitoredItemServiceOperationResult> deleteB = subscription.deleteMonitoredItems();

        assertEquals(1, deleteB.size());
        assertTrue(
            deleteB.get(0).isGood(),
            "B should still exist on the Server, but deleting it returned "
                + deleteB.get(0).operationResult().orElse(deleteB.get(0).serviceResult()));
      }
    }

    /**
     * A MonitoredItem queued for deletion when the Subscription is reset keeps its SyncState, so
     * the Subscription reports itself as never synchronized: {@code isMonitoredItemsSynchronized()}
     * stays {@code false} for a Subscription whose items are all created, which is a state no
     * caller can act on.
     */
    @Test
    void newSubscriptionIsSynchronizedAfterAnItemWasQueuedForDeletion() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        OpcUaMonitoredItem itemA = fixture.createItem(subscription);

        subscription.removeMonitoredItem(itemA);
        subscription.delete();
        subscription.create();

        assertTrue(
            subscription.isMonitoredItemsSynchronized(),
            "the new Subscription has no MonitoredItems, and the item queued for deletion no"
                + " longer exists on the Server, so there is nothing left to synchronize");
      }
    }

    /**
     * Control for {@link #newSubscriptionIsSynchronizedAfterAnItemWasQueuedForDeletion()}: a
     * delete/create cycle with no pending deletion leaves the Subscription synchronized once its
     * items are created again.
     */
    @Test
    void newSubscriptionIsSynchronizedAfterItemsAreCreatedAgain() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);
        subscription.create();

        fixture.createItem(subscription);

        subscription.delete();
        subscription.create();

        assertFalse(
            subscription.isMonitoredItemsSynchronized(),
            "control: the item still has to be created on the new Subscription");
        assertTrue(subscription.createMonitoredItems().get(0).isGood());
        assertTrue(subscription.isMonitoredItemsSynchronized());
      }
    }
  }

  // region fixture

  /**
   * A {@link DelegatingMonitoredItemServiceSet} that records the MonitoredItemIds of every
   * DeleteMonitoredItems request it receives and can fail the next {@code n} of them with a service
   * fault.
   */
  private static final class RecordingMonitoredItemServiceSet
      extends DelegatingMonitoredItemServiceSet {

    private final List<List<UInteger>> deletedMonitoredItemIds =
        Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger deleteFailuresRemaining = new AtomicInteger(0);

    RecordingMonitoredItemServiceSet(OpcUaServer server) {
      super(server);
    }

    /** The MonitoredItemIds of each DeleteMonitoredItems request, in the order received. */
    List<List<UInteger>> getDeletedMonitoredItemIds() {
      synchronized (deletedMonitoredItemIds) {
        return List.copyOf(deletedMonitoredItemIds);
      }
    }

    void clearDeletedMonitoredItemIds() {
      deletedMonitoredItemIds.clear();
    }

    void failNextDeleteMonitoredItems(int count) {
      deleteFailuresRemaining.set(count);
    }

    @Override
    public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
        ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {

      UInteger[] ids = request.getMonitoredItemIds();
      deletedMonitoredItemIds.add(
          ids == null ? List.of() : Arrays.stream(ids).collect(Collectors.toList()));

      if (deleteFailuresRemaining.getAndDecrement() > 0) {
        throw new UaException(
            StatusCodes.Bad_TooManyOperations, "scripted DeleteMonitoredItems failure");
      }

      return super.onDeleteMonitoredItems(context, request);
    }
  }

  /** A running Server whose MonitoredItem service calls are recorded, and a connected client. */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final RecordingMonitoredItemServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new RecordingMonitoredItemServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, cfg -> {});
      client.connect();
    }

    /** Add a MonitoredItem to {@code subscription} and create it on the Server. */
    OpcUaMonitoredItem createItem(OpcUaSubscription subscription) {
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();
      assertEquals(1, results.size());
      assertTrue(results.get(0).isGood(), "failed to create the MonitoredItem");

      return item;
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }

  // endregion
}
