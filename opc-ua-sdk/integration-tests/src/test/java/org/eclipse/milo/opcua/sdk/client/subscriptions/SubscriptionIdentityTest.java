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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Which Subscription a piece of {@code PublishingManager} work belongs to.
 *
 * <p>A {@code SubscriptionDetails} entry is registered under the SubscriptionId the {@link
 * OpcUaSubscription} had at registration time, but it carries only the mutable Subscription object.
 * Everything that later needs an id — removing the entry when the Server reports Bad_Timeout,
 * building SubscriptionAcknowledgements, delivering a NotificationMessage — asks that object for
 * its <i>current</i> id. The object's id is not stable: {@link OpcUaSubscription#reset()} clears it
 * and {@link OpcUaSubscription#create()} installs a new one, and neither is serialized against work
 * already queued for the previous incarnation.
 *
 * <p>The tests below drive the two ways an entry's id and its Subscription's current id can
 * disagree, and assert the invariant each case breaks: work belonging to one incarnation of a
 * Subscription must never be applied to another.
 */
public class SubscriptionIdentityTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to watch for stale work that must not take effect. Every task involved is already
   * queued when the window opens and runs as soon as the delivery queue is released, so this is
   * generous by three orders of magnitude.
   */
  private static final long STALE_WORK_WINDOW_MILLIS = 3_000;

  /**
   * Long enough that nothing times out on its own: a parked Publish request, and a
   * CreateSubscription held at the gate below, must stay held until the test releases them.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** The client keeps one more PublishRequest in flight than it has Subscriptions. */
  private static final int PIPELINE_DEPTH = 2;

  private static final DataValue CURRENT_VALUE = new DataValue(Variant.ofInt32(1));
  private static final DataValue STALE_VALUE = new DataValue(Variant.ofInt32(2));

  /**
   * A NotificationMessage is processed on the Subscription's processing queue and then handed to
   * its delivery queue, which is a {@code final} field of {@link OpcUaSubscription} that {@link
   * OpcUaSubscription#reset()} neither drains nor replaces. An application callback that has not
   * yet returned therefore holds behind it work belonging to a Subscription incarnation that may be
   * gone by the time it runs — and that work addresses the live object, so it acts on whatever
   * incarnation exists then.
   *
   * <p>Part 4 §5.13.1.1 makes the SubscriptionId "the Server-assigned identifier for the
   * Subscription": a NotificationMessage received for one is not a statement about any other, and
   * two Subscriptions created in sequence are two different Subscriptions even when the same client
   * object represents them.
   */
  @Nested
  class StaleWorkFromAPreviousIncarnation {

    /**
     * The traced failure: a Bad_Timeout StatusChangeNotification received for the previous
     * Subscription removes the <i>new</i> Subscription's registration (it asks the live object for
     * its current id) and then resets it (Bad_Timeout resets the Subscription the notification is
     * delivered on). The application is left holding a Subscription object that reports INITIAL and
     * has no SubscriptionId, while the Subscription it just created is still alive on the Server —
     * and unreachable, because {@link OpcUaSubscription#delete()} needs the ServerState that reset
     * discarded.
     */
    @Test
    void staleStatusChangeDoesNotTearDownTheRecreatedSubscription() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        var listener = new BlockingDeliveryListener();
        subscription.setSubscriptionListener(listener);

        UInteger idA = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        // Hold the delivery queue open with a notification the application never finishes handling.
        fixture.scriptable.enqueueDataChange(idA, 1, notifications(item, CURRENT_VALUE), uint(1));
        assertTrue(
            listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered, so the delivery queue is not held open");

        // Queue the Bad_Timeout status change behind it. It is processed now, delivered later.
        fixture.scriptable.enqueueNotification(idA, 2, badTimeout(fixture.scriptable), uint(2));
        assertTrue(
            awaitTrue(
                () -> subscription.getDeliveryQueue().getQueueSize() == 1, AWAIT_TIMEOUT_MILLIS),
            "the StatusChangeNotification was never queued behind the blocked delivery, so the"
                + " scenario under test did not happen");

        // The application discards the Subscription and creates another one.
        subscription.reset();
        subscription.create();

        UInteger idB = subscriptionId(subscription);
        assertNotEquals(
            idA, idB, "the Server reused the SubscriptionId, so nothing distinguishes the two");

        listener.release();

        assertFalse(
            awaitTrue(
                () -> subscription.getSyncState() == OpcUaSubscription.SyncState.INITIAL,
                STALE_WORK_WINDOW_MILLIS),
            "a Bad_Timeout StatusChangeNotification received for Subscription "
                + idA
                + " reset Subscription "
                + idB
                + ", which was created after it and has never timed out: the queued delivery"
                + " task asks the live Subscription object for its current SubscriptionId, so it"
                + " de-registers and tears down whatever incarnation exists when it finally runs");

        assertEquals(
            Optional.of(idB),
            subscription.getSubscriptionId(),
            "the Subscription created after the stale notification lost its ServerState, so"
                + " delete() can no longer name it and the Server-side Subscription is"
                + " unreachable");
      }
    }

    /**
     * The control that isolates the stale StatusChangeNotification as the cause: the identical
     * script with nothing queued behind the blocked delivery leaves the recreated Subscription
     * intact.
     */
    @Test
    void recreatedSubscriptionSurvivesWhenNoStaleWorkIsQueued() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        var listener = new BlockingDeliveryListener();
        subscription.setSubscriptionListener(listener);

        UInteger idA = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueDataChange(idA, 1, notifications(item, CURRENT_VALUE), uint(1));
        assertTrue(
            listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered");

        subscription.reset();
        subscription.create();

        UInteger idB = subscriptionId(subscription);

        listener.release();

        assertFalse(
            awaitTrue(
                () -> subscription.getSyncState() == OpcUaSubscription.SyncState.INITIAL,
                STALE_WORK_WINDOW_MILLIS),
            "control: reset() followed by create() must leave a usable Subscription");
        assertEquals(
            Optional.of(idB),
            subscription.getSubscriptionId(),
            "control: reset() followed by create() must leave a usable Subscription");
      }
    }

    /**
     * The control that keeps the assertion above from being vacuous in the other direction: a
     * Bad_Timeout StatusChangeNotification delivered to the Subscription it was received for does
     * reset it. Part 4 §5.13.1.1 — the Subscription no longer exists on the Server — so the reset
     * is required, and the defect is only that the reset lands on the wrong incarnation.
     */
    @Test
    void statusChangeResetsTheSubscriptionItWasReceivedFor() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onStatusChanged(OpcUaSubscription s, StatusCode status) {
                statusChanged.countDown();
              }
            });

        UInteger idA = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueNotification(idA, 1, badTimeout(fixture.scriptable), uint(1));

        assertTrue(
            statusChanged.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
            "control: the Bad_Timeout StatusChangeNotification was never delivered");
        assertEquals(
            OpcUaSubscription.SyncState.INITIAL,
            subscription.getSyncState(),
            "control: a Bad_Timeout StatusChangeNotification must reset the Subscription it was"
                + " received for");
      }
    }

    /**
     * The same interleaving with a DataChangeNotification instead of a status change. {@link
     * OpcUaSubscription#reset()} leaves the MonitoredItem map and every ClientHandle in it
     * untouched, so a value received for the discarded Subscription still resolves to an item and
     * is handed to the application as though it had arrived for the Subscription created since.
     */
    @Test
    void staleDataChangeIsNotDeliveredAfterTheSubscriptionIsRecreated() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        var listener = new BlockingDeliveryListener();
        subscription.setSubscriptionListener(listener);

        UInteger idA = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueDataChange(idA, 1, notifications(item, CURRENT_VALUE), uint(1));
        assertTrue(
            listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered, so the delivery queue is not held open");

        fixture.scriptable.enqueueDataChange(idA, 2, notifications(item, STALE_VALUE), uint(2));
        assertTrue(
            awaitTrue(
                () -> subscription.getDeliveryQueue().getQueueSize() == 1, AWAIT_TIMEOUT_MILLIS),
            "the second DataChangeNotification was never queued behind the blocked delivery, so"
                + " the scenario under test did not happen");

        subscription.reset();
        subscription.create();

        UInteger idB = subscriptionId(subscription);
        assertNotEquals(
            idA, idB, "the Server reused the SubscriptionId, so nothing distinguishes the two");

        listener.release();

        assertFalse(
            awaitTrue(() -> listener.received(STALE_VALUE), STALE_WORK_WINDOW_MILLIS),
            "a DataChangeNotification received for Subscription "
                + idA
                + " was delivered to the application after that Subscription had been discarded"
                + " and Subscription "
                + idB
                + " created in its place: the application is told a MonitoredItem of the new"
                + " Subscription has a value the new Subscription has never reported");
      }
    }

    /**
     * The control for the test above: with no reset and no recreate, the second
     * DataChangeNotification is delivered. Without it the assertion there could hold simply because
     * the script never produced a second delivery.
     */
    @Test
    void queuedDataChangeIsDeliveredWhenTheSubscriptionIsNotRecreated() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        var listener = new BlockingDeliveryListener();
        subscription.setSubscriptionListener(listener);

        UInteger idA = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueDataChange(idA, 1, notifications(item, CURRENT_VALUE), uint(1));
        assertTrue(
            listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered");

        fixture.scriptable.enqueueDataChange(idA, 2, notifications(item, STALE_VALUE), uint(2));

        listener.release();

        assertTrue(
            awaitTrue(() -> listener.received(STALE_VALUE), AWAIT_TIMEOUT_MILLIS),
            "control: a DataChangeNotification queued behind a blocked delivery must be delivered"
                + " once the delivery queue drains");
      }
    }
  }

  /**
   * {@link OpcUaSubscription#create()} is a check-then-act around a blocking service call: it tests
   * {@code syncState == INITIAL}, calls CreateSubscription, and only then publishes the
   * ServerState. Nothing serializes it, so two concurrent calls both pass the check and both create
   * a Subscription on the Server, while the object keeps only the ServerState written last.
   *
   * <p>The Subscription whose id was overwritten is not forgotten everywhere: {@code
   * PublishingManager} still holds an entry registered under it, and that entry answers questions
   * about its id with the live object's current one.
   */
  @Nested
  class ConcurrentCreate {

    /**
     * Part 4 §5.14.5.2 defines a SubscriptionAcknowledgement as a subscriptionId and the
     * sequenceNumber of a NotificationMessage "received on the Subscription", and lets the Server
     * "delete the Message with this sequence number from its retransmission queue". Sending the
     * sequence number of one Subscription's NotificationMessage under another Subscription's id
     * therefore acknowledges a message that was never received on that Subscription, while the
     * message that <i>was</i> received is never acknowledged and stays in the Server's
     * retransmission queue.
     */
    @Test
    void acknowledgementCarriesTheIdItsNotificationWasReceivedUnder() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);

        UInteger idA = fixture.createConcurrently(subscription);
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        fixture.scriptable.enqueueDataChange(idA, 1, notifications(item, CURRENT_VALUE), uint(1));

        assertTrue(
            awaitTrue(() -> fixture.acknowledgementOfSequence(1).isPresent(), AWAIT_TIMEOUT_MILLIS),
            "the client never acknowledged sequence 1, so there is no acknowledgement to inspect");

        assertEquals(
            idA,
            fixture.acknowledgementOfSequence(1).orElseThrow().getSubscriptionId(),
            "the NotificationMessage was received on Subscription "
                + idA
                + " but acknowledged under Subscription "
                + subscription.getSubscriptionId().orElse(null)
                + ": the acknowledgement is built from the live Subscription object's current id"
                + " rather than the id the PublishingManager entry is registered under");
      }
    }

    /** The control: after a single create() the acknowledgement carries that Subscription's id. */
    @Test
    void acknowledgementCarriesTheSubscriptionIdAfterASingleCreate() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        OpcUaMonitoredItem item = fixture.addDataItem(subscription);

        UInteger id = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueDataChange(id, 1, notifications(item, CURRENT_VALUE), uint(1));

        assertTrue(
            awaitTrue(() -> fixture.acknowledgementOfSequence(1).isPresent(), AWAIT_TIMEOUT_MILLIS),
            "control: the client never acknowledged sequence 1");
        assertEquals(
            id,
            fixture.acknowledgementOfSequence(1).orElseThrow().getSubscriptionId(),
            "control: the acknowledgement must carry the id of the Subscription the"
                + " NotificationMessage was received on");
      }
    }

    /**
     * Whatever an {@link OpcUaSubscription} created on the Server, {@link
     * OpcUaSubscription#delete()} has to be able to delete: a Server-side Subscription no client
     * object can name is a resource leak that survives until its lifetime expires, and Part 4
     * §5.13.8 gives DeleteSubscriptions the SubscriptionIds as its only handle on them.
     *
     * <p>A second, concurrent create() overwrites the ServerState holding the first SubscriptionId,
     * so delete() names only the second and the first is left running.
     */
    @Test
    void deleteRemovesEveryServerSideSubscriptionTheClientCreated() throws Exception {
      try (var fixture = new Fixture()) {
        var subscription = new OpcUaSubscription(fixture.client);

        UInteger idA = fixture.createConcurrently(subscription);
        UInteger idB = subscriptionId(subscription);

        subscription.delete();

        assertEquals(
            Set.of(),
            Set.copyOf(fixture.server.getSubscriptions().keySet()),
            "delete() left a Subscription running on the Server. Two concurrent create() calls"
                + " each created one ("
                + idA
                + " and "
                + idB
                + "), the second overwrote the ServerState holding the first, and nothing the"
                + " client can reach still names it");
      }
    }

    /** The control: a Subscription created once is removed from the Server by delete(). */
    @Test
    void deleteRemovesTheServerSideSubscriptionAfterASingleCreate() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        subscription.delete();

        assertEquals(
            Set.of(),
            Set.copyOf(fixture.server.getSubscriptions().keySet()),
            "control: delete() must remove the Subscription from the Server");
      }
    }
  }

  // region helpers

  private static UInteger subscriptionId(OpcUaSubscription subscription) {
    return subscription.getSubscriptionId().orElseThrow();
  }

  private static List<MonitoredItemNotification> notifications(
      OpcUaMonitoredItem item, DataValue value) {

    return List.of(new MonitoredItemNotification(item.getClientHandle().orElseThrow(), value));
  }

  /** A NotificationMessage body reporting that the Subscription has timed out. */
  private static ExtensionObject[] badTimeout(ScriptableSubscriptionServiceSet scriptable) {
    return new ExtensionObject[] {
      scriptable.encode(
          new StatusChangeNotification(
              new StatusCode(StatusCodes.Bad_Timeout), DiagnosticInfo.NULL_VALUE))
    };
  }

  /**
   * A listener that suspends the Subscription's delivery queue inside the first {@code
   * onDataReceived} callback, so a test can queue further work behind it and choose when that work
   * runs. Every DataValue handed to the callback is recorded, including the one delivered while it
   * is suspended.
   */
  private static final class BlockingDeliveryListener
      implements OpcUaSubscription.SubscriptionListener {

    private final List<DataValue> received = Collections.synchronizedList(new ArrayList<>());
    private final CountDownLatch deliveryStarted = new CountDownLatch(1);
    private final CountDownLatch release = new CountDownLatch(1);

    @Override
    public void onDataReceived(
        OpcUaSubscription subscription, List<OpcUaMonitoredItem> items, List<DataValue> values) {

      received.addAll(values);

      if (deliveryStarted.getCount() > 0) {
        deliveryStarted.countDown();

        try {
          if (!release.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            throw new IllegalStateException("the delivery queue was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException(e);
        }
      }
    }

    boolean awaitDeliveryStarted(long timeoutMillis) throws InterruptedException {
      return deliveryStarted.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    void release() {
      release.countDown();
    }

    boolean received(DataValue value) {
      synchronized (received) {
        return received.stream().anyMatch(v -> v.getValue().equals(value.getValue()));
      }
    }
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that can hold CreateSubscription requests at the
   * Server and release them one at a time.
   *
   * <p>Holding them <i>inside</i> the Server handler is what makes the overlap real: the two client
   * threads have each sent their request, so each has already passed {@code create()}'s {@code
   * syncState == INITIAL} check, and the order in which their ServerStates are published is the
   * order the test releases them in.
   */
  private static final class GatedCreateServiceSet extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger arrivals = new AtomicInteger(0);
    private final CountDownLatch releaseFirst = new CountDownLatch(1);
    private final CountDownLatch releaseSecond = new CountDownLatch(1);

    private volatile boolean gated = false;

    GatedCreateServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateCreates() {
      gated = true;
    }

    int arrivedCreateCount() {
      return arrivals.get();
    }

    void releaseFirstCreate() {
      releaseFirst.countDown();
    }

    void releaseSecondCreate() {
      releaseSecond.countDown();
    }

    void releaseAllCreates() {
      releaseFirst.countDown();
      releaseSecond.countDown();
    }

    @Override
    public CreateSubscriptionResponse onCreateSubscription(
        ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {

      if (gated) {
        int index = arrivals.getAndIncrement();

        await(index == 0 ? releaseFirst : releaseSecond);
      }

      return super.onCreateSubscription(context, request);
    }

    private static void await(CountDownLatch gate) throws UaException {
      try {
        if (!gate.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
          throw new UaException(
              StatusCodes.Bad_Timeout, "the CreateSubscription gate was never released");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }
    }
  }

  /**
   * A running Server whose Publish responses are scripted and whose CreateSubscription responses
   * can be gated, plus a connected client.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final GatedCreateServiceSet scriptable;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new GatedCreateServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg
                      // Long request timeout so parked or gated requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();
    }

    OpcUaSubscription createSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      return subscription;
    }

    /**
     * Add a MonitoredItem to {@code subscription}, client-side only: {@link
     * OpcUaSubscription#addMonitoredItem} assigns the ClientHandle a scripted notification is
     * looked up by, and no Server-side item participates in delivering one.
     */
    OpcUaMonitoredItem addDataItem(OpcUaSubscription subscription) {
      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      return item;
    }

    void awaitPipelineFilled() throws Exception {
      assertTrue(
          awaitTrue(
              () -> scriptable.getParkedRequestCount() >= PIPELINE_DEPTH, AWAIT_TIMEOUT_MILLIS),
          "the client did not fill its Publish pipeline");
    }

    /**
     * Run two overlapping {@code create()} calls on {@code subscription} and return the
     * SubscriptionId the Server assigned the first of them, i.e. the one whose ServerState the
     * second overwrites.
     *
     * <p>The second call is started only once the first is inside the Server's CreateSubscription
     * handler, so the overlap is fixed rather than raced. Neither call is required to reach the
     * Server: an implementation that serializes {@code create()} rejects the second before it sends
     * anything, and the invariants asserted by the callers hold just as well then.
     */
    UInteger createConcurrently(OpcUaSubscription subscription) throws Exception {
      scriptable.gateCreates();

      var failures = new CopyOnWriteArrayList<Exception>();
      Runnable create =
          () -> {
            try {
              subscription.create();
            } catch (Exception e) {
              failures.add(e);
            }
          };

      var first = new Thread(create, "create-first");
      var second = new Thread(create, "create-second");

      first.start();
      assertTrue(
          awaitTrue(() -> scriptable.arrivedCreateCount() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the first create() never reached the Server");

      second.start();
      awaitTrue(
          () -> scriptable.arrivedCreateCount() >= 2 || !second.isAlive(),
          STALE_WORK_WINDOW_MILLIS);

      scriptable.releaseFirstCreate();
      assertTrue(
          awaitTrue(() -> subscription.getSubscriptionId().isPresent(), AWAIT_TIMEOUT_MILLIS),
          "the first create() never completed: " + failures);

      UInteger firstId = subscriptionId(subscription);

      // Publish traffic starts only once a Subscription is registered with the PublishingManager,
      // so a PublishRequest is proof that the first create() finished registering under firstId.
      assertTrue(
          awaitTrue(() -> scriptable.getPublishRequestCount() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the first create() never registered its Subscription with the PublishingManager");

      scriptable.releaseSecondCreate();

      join(first);
      join(second);

      return firstId;
    }

    /** The first acknowledgement of {@code sequenceNumber} the Server received, if any. */
    Optional<SubscriptionAcknowledgement> acknowledgementOfSequence(long sequenceNumber) {
      return scriptable.getReceivedAcknowledgements().stream()
          .filter(ack -> ack.getSequenceNumber().longValue() == sequenceNumber)
          .findFirst();
    }

    @Override
    public void close() throws Exception {
      scriptable.releaseAllCreates();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  private static void join(Thread thread) throws InterruptedException {
    thread.join(AWAIT_TIMEOUT_MILLIS);

    assertFalse(thread.isAlive(), thread.getName() + " did not finish");
  }

  /** Polls {@code condition} until it holds or {@code timeoutMillis} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis)
      throws Exception {

    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

    while (System.nanoTime() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }

    return condition.get();
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
