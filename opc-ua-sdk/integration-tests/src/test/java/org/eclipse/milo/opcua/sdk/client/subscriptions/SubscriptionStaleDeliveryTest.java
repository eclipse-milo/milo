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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What happens to a NotificationMessage whose Subscription is discarded while the message is still
 * on its way to the application.
 *
 * <p>The delivery queue belongs to the {@link OpcUaSubscription} object, not to any one
 * Subscription it has represented: {@link OpcUaSubscription#reset()} neither drains nor replaces
 * it. An application callback that has not returned therefore holds behind it work belonging to a
 * Subscription that may be gone — and gone Subscriptions are replaced, by a {@link
 * OpcUaSubscription#create()} on the same object.
 *
 * <p>{@link SubscriptionIdentityTest} covers the binding that makes such work recognisable as
 * stale: a {@code PublishingManager} entry is bound to the SubscriptionId it was registered under,
 * so a message received on one Subscription is never mistaken for a message on the Subscription
 * that replaced it. The two cases here are what must then <i>happen</i> to it: notifications the
 * application will never be given have to be reported as lost, and a Bad_Timeout that goes stale
 * <i>while its own NotificationMessage is being delivered</i> — the guard in {@code
 * deliverNotificationMessage} is evaluated once, before the first callback, and application
 * callbacks take as long as they take — must not tear down the replacement.
 */
public class SubscriptionStaleDeliveryTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to watch for stale work that must not take effect. Every task involved is already
   * queued when the window opens and runs as soon as the delivery queue is released.
   */
  private static final long STALE_WORK_WINDOW_MILLIS = 3_000;

  /**
   * Long enough that nothing times out on its own: the PublishRequests parked at the Server must
   * stay parked until the test decides how they end.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** The client keeps one more PublishRequest in flight than it has Subscriptions. */
  private static final int PIPELINE_DEPTH = 2;

  private static final DataValue CURRENT_VALUE = new DataValue(Variant.ofInt32(1));
  private static final DataValue STALE_VALUE = new DataValue(Variant.ofInt32(2));

  /**
   * Part 4 §5.14.5.2 lets a Server "delete the Message with this sequence number from its
   * retransmission queue" once the client acknowledges it, and the client acknowledges a
   * NotificationMessage when it is <i>received</i>, not when it is delivered. A message discarded
   * on its way to the application is therefore data the client asked the Server to forget and then
   * dropped: the application is never given it, and no Republish can ever get it back.
   *
   * <p>{@code onNotificationDataLost} is the one way an application can find out. Part 4 §5.14.1.1
   * makes the client's sequence-number accounting the detector of everything else that goes
   * missing; this is the one loss the accounting cannot see, because the message did arrive and was
   * accounted for.
   */
  @Nested
  class NotificationDataThatCanNoLongerBeAttributed {

    /**
     * Control: the identical script with nothing discarded. It proves the fixture does not report
     * lost data of its own accord, so the test below fails because of the discard.
     */
    @Test
    void nothingIsReportedLostWhenTheQueuedNotificationMessageIsStillAttributable()
        throws Exception {

      try (var fixture = new Fixture()) {
        fixture.awaitPipelineFilled();

        fixture.enqueueDataChange(1, CURRENT_VALUE);
        assertTrue(
            fixture.listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered, so the delivery queue is not held open");

        fixture.enqueueDataChange(2, STALE_VALUE);
        fixture.awaitQueuedDelivery();

        fixture.listener.release();

        assertTrue(
            fixture.awaitTrue(() -> fixture.listener.received(STALE_VALUE)),
            "control: a DataChangeNotification queued behind a blocked delivery must be delivered"
                + " once the delivery queue drains");
        assertEquals(
            0,
            fixture.listener.notificationDataLostCount(),
            "control: nothing was discarded, so nothing may be reported as lost");
      }
    }

    @Test
    void aDiscardedNotificationMessageIsReportedAsLostData() throws Exception {
      try (var fixture = new Fixture()) {
        UInteger idA = fixture.subscriptionId();
        fixture.awaitPipelineFilled();

        // Hold the delivery queue open with a notification the application never finishes handling.
        fixture.enqueueDataChange(1, CURRENT_VALUE);
        assertTrue(
            fixture.listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the first notification was never delivered, so the delivery queue is not held open");

        // Queued behind it: received, acknowledged, and not yet delivered.
        fixture.enqueueDataChange(2, STALE_VALUE);
        fixture.awaitQueuedDelivery();

        // The application discards the Subscription the queued message belongs to.
        fixture.subscription.reset();
        fixture.subscription.create();

        assertNotEquals(
            idA,
            fixture.subscriptionId(),
            "the Server reused the SubscriptionId, so nothing distinguishes the two");

        fixture.listener.release();

        assertTrue(
            fixture.awaitTrue(() -> fixture.listener.notificationDataLostCount() >= 1),
            "a NotificationMessage received on Subscription "
                + idA
                + " was discarded, correctly, because that Subscription no longer exists — but the"
                + " application was never told. The client had already acknowledged it, so the"
                + " Server may have deleted its only copy: the notifications in it are gone and"
                + " nothing reports them lost");
      }
    }
  }

  /**
   * Part 4 §5.13.1.1 makes the SubscriptionId "the Server-assigned identifier for the
   * Subscription", so a Bad_Timeout StatusChangeNotification is a statement about the Subscription
   * it was received on and about no other. Applied to a Subscription created since, it discards a
   * Subscription that is alive on the Server and leaves it unreachable: {@link
   * OpcUaSubscription#delete()} needs the ServerState that {@link OpcUaSubscription#reset()} throws
   * away.
   *
   * <p>The window is inside a single NotificationMessage. {@code deliverNotificationMessage} checks
   * that the message's Subscription still exists once, before it hands anything to the application,
   * and then walks the NotificationData in order. A DataChangeNotification ahead of the
   * StatusChangeNotification puts an application callback of arbitrary duration between the check
   * and the teardown, which is time enough for the application to discard the Subscription and
   * create another.
   */
  @Nested
  class BadTimeoutThatGoesStaleMidMessage {

    /**
     * Control: the identical NotificationMessage with no reset in the middle of it. It proves the
     * Bad_Timeout in the second half of the message is still acted on after the blocking callback
     * returns — Part 4 §5.13.1.1 requires the teardown when the Subscription really is the one that
     * timed out — so the test below fails because the teardown landed on the wrong Subscription and
     * not because it stopped happening.
     */
    @Test
    void badTimeoutBehindABlockingCallbackResetsTheSubscriptionItWasReceivedFor() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.awaitPipelineFilled();

        fixture.enqueueDataChangeThenBadTimeout(1, CURRENT_VALUE);
        assertTrue(
            fixture.listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the DataChangeNotification at the head of the message was never delivered");

        fixture.listener.release();

        // Wait on the report, not on the reset: notifyStatusChanged() resets the Subscription
        // before it tells the application, so syncState reaching INITIAL does not mean the
        // StatusChangeNotification has been delivered yet.
        assertTrue(
            fixture.awaitTrue(() -> !fixture.listener.statusChanges().isEmpty()),
            "control: the Bad_Timeout must be reported to the application, even when an application"
                + " callback in the same NotificationMessage ran first");
        assertEquals(
            List.of(new StatusCode(StatusCodes.Bad_Timeout)),
            fixture.listener.statusChanges(),
            "control: the Bad_Timeout, and nothing else, must be reported to the application");
        assertEquals(
            OpcUaSubscription.SyncState.INITIAL,
            fixture.subscription.getSyncState(),
            "control: a Bad_Timeout StatusChangeNotification must reset the Subscription it was"
                + " received for; the reset happens before the report, so it has already happened"
                + " by the time the report arrives");
      }
    }

    @Test
    void aStaleBadTimeoutDoesNotTearDownTheSubscriptionCreatedWhileItWasBeingDelivered()
        throws Exception {

      try (var fixture = new Fixture()) {
        UInteger idA = fixture.subscriptionId();
        fixture.awaitPipelineFilled();

        // One NotificationMessage carrying a value and then the Subscription's death notice.
        fixture.enqueueDataChangeThenBadTimeout(1, CURRENT_VALUE);
        assertTrue(
            fixture.listener.awaitDeliveryStarted(AWAIT_TIMEOUT_MILLIS),
            "the DataChangeNotification at the head of the message was never delivered, so the"
                + " Bad_Timeout behind it is not held up by an application callback");

        // The application discards the timed-out Subscription and creates another, which is what it
        // is expected to do — from onDataReceived's point of view the Subscription is simply gone.
        fixture.subscription.reset();
        fixture.subscription.create();

        UInteger idB = fixture.subscriptionId();
        assertNotEquals(
            idA, idB, "the Server reused the SubscriptionId, so nothing distinguishes the two");

        fixture.listener.release();

        assertFalse(
            fixture.awaitTrue(
                () -> fixture.subscription.getSyncState() == OpcUaSubscription.SyncState.INITIAL,
                STALE_WORK_WINDOW_MILLIS),
            "a Bad_Timeout StatusChangeNotification received on Subscription "
                + idA
                + " tore down Subscription "
                + idB
                + ", which was created while that message was being delivered and has never timed"
                + " out: the guard at the head of the delivery is stale by the time the"
                + " StatusChangeNotification behind the application callback is reached");
        assertEquals(
            Optional.of(idB),
            fixture.subscription.getSubscriptionId(),
            "the Subscription created while the stale notification was being delivered lost its"
                + " ServerState, so delete() can no longer name it and the Server-side"
                + " Subscription is unreachable");
      }
    }
  }

  // region helpers

  /**
   * A listener that suspends the Subscription's delivery queue inside the first {@code
   * onDataReceived} callback, so a test can let work pile up behind it — or interrupt the delivery
   * of the very NotificationMessage it belongs to — and choose when that work continues.
   */
  private static final class BlockingDeliveryListener
      implements OpcUaSubscription.SubscriptionListener {

    private final List<DataValue> received = Collections.synchronizedList(new ArrayList<>());
    private final List<StatusCode> statusChanges = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger notificationDataLost = new AtomicInteger(0);
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

    @Override
    public void onStatusChanged(OpcUaSubscription subscription, StatusCode status) {
      statusChanges.add(status);
    }

    @Override
    public void onNotificationDataLost(OpcUaSubscription subscription) {
      notificationDataLost.incrementAndGet();
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

    List<StatusCode> statusChanges() {
      return List.copyOf(statusChanges);
    }

    int notificationDataLostCount() {
      return notificationDataLost.get();
    }
  }

  /**
   * A running Server whose Publish responses are scripted, plus a connected client holding one
   * Subscription with one MonitoredItem and a {@link BlockingDeliveryListener}.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    private final OpcUaSubscription subscription;
    private final OpcUaMonitoredItem item;
    private final BlockingDeliveryListener listener = new BlockingDeliveryListener();

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg
                      // Long request timeout so parked Publish requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();

      subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(listener);
      subscription.create();

      // The MonitoredItem only has to exist on the client: addMonitoredItem assigns the
      // ClientHandle
      // the notification fan-out looks scripted notifications up by, and no Server-side item
      // participates in delivering one.
      item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
    }

    UInteger subscriptionId() {
      return subscription.getSubscriptionId().orElseThrow();
    }

    void awaitPipelineFilled() throws Exception {
      assertTrue(
          awaitTrue(() -> scriptable.getParkedRequestCount() >= PIPELINE_DEPTH),
          "the client did not fill its Publish pipeline");
    }

    /** Wait until exactly one delivery is waiting behind the blocked one. */
    void awaitQueuedDelivery() throws Exception {
      assertTrue(
          awaitTrue(() -> subscription.getDeliveryQueue().getQueueSize() == 1),
          "the NotificationMessage was never queued behind the blocked delivery, so the scenario"
              + " under test did not happen");
    }

    /** Script a PublishResponse carrying {@code value} for the current Subscription. */
    void enqueueDataChange(long sequenceNumber, DataValue value) {
      scriptable.enqueueDataChange(
          subscriptionId(), sequenceNumber, List.of(notification(value)), uint(sequenceNumber));
    }

    /**
     * Script a PublishResponse whose single NotificationMessage carries {@code value} and then a
     * Bad_Timeout StatusChangeNotification, in that order.
     *
     * <p>Part 4 §5.14.1.1 makes a NotificationMessage's notificationData a list of Notifications,
     * so this is one message the client delivers in two callbacks — which is what puts an
     * application callback between the Subscription's death notice and the check that it is still
     * the current Subscription.
     */
    void enqueueDataChangeThenBadTimeout(long sequenceNumber, DataValue value) {
      ExtensionObject[] notificationData = {
        scriptable.encode(
            new DataChangeNotification(
                new MonitoredItemNotification[] {notification(value)}, null)),
        scriptable.encode(
            new StatusChangeNotification(
                new StatusCode(StatusCodes.Bad_Timeout), DiagnosticInfo.NULL_VALUE))
      };

      scriptable.enqueueNotification(
          subscriptionId(), sequenceNumber, notificationData, uint(sequenceNumber));
    }

    private MonitoredItemNotification notification(DataValue value) {
      return new MonitoredItemNotification(item.getClientHandle().orElseThrow(), value);
    }

    boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
      return awaitTrue(condition, AWAIT_TIMEOUT_MILLIS);
    }

    /** Polls {@code condition} until it holds or {@code timeoutMillis} elapses. */
    boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis) throws Exception {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      while (System.nanoTime() < deadline) {
        if (condition.get()) {
          return true;
        }
        Thread.sleep(25);
      }

      return condition.get();
    }

    @Override
    public void close() throws Exception {
      listener.release();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
