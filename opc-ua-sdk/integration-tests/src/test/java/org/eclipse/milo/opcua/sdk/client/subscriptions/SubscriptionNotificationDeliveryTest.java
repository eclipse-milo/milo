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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.EventNotificationList;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Delivery guarantees of the client-side notification fan-out: {@code
 * OpcUaSubscription.notifyDataReceived}, {@code notifyEventsReceived}, {@code
 * notifyKeepAliveReceived} and {@code notifyStatusChanged}, driven through {@code
 * PublishingManager.deliverNotificationMessage}.
 *
 * <p>Publish responses are scripted with {@link ScriptableSubscriptionServiceSet}, so the
 * MonitoredItems used here only need to exist on the client: {@link
 * OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)} assigns the ClientHandle the fan-out
 * looks notifications up by, and no Server-side item participates in delivering a scripted
 * notification.
 */
public class SubscriptionNotificationDeliveryTest {

  private static final DataValue VALUE_1 = new DataValue(Variant.ofInt32(1));
  private static final DataValue VALUE_2 = new DataValue(Variant.ofInt32(2));

  private static final String DATA_CALLBACK = "onDataReceived";
  private static final String KEEP_ALIVE_CALLBACK = "onKeepAliveReceived";

  /**
   * The application's {@link OpcUaSubscription.SubscriptionListener} and the per-item {@link
   * OpcUaMonitoredItem.DataValueListener}s are independent sinks for the same
   * DataChangeNotification. A failure in one must not cost the others their notification, and must
   * not consume the rest of the NotificationMessage.
   */
  @Nested
  class ListenerFailureIsolation {

    @Test
    void monitoredItemListenerIsNotifiedWhenSubscriptionListenerThrows() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);

        var received = new AtomicReference<DataValue>();
        var itemNotified = new CountDownLatch(1);
        item.setDataValueListener(
            (i, value) -> {
              received.set(value);
              itemNotified.countDown();
            });

        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onDataReceived(
                  OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {
                throw new IllegalStateException("application SubscriptionListener failure");
              }
            });

        fixture.scriptable.enqueueDataChange(
            subscriptionId(subscription),
            1,
            List.of(new MonitoredItemNotification(clientHandle(item), VALUE_1)));

        assertTrue(
            itemNotified.await(5, TimeUnit.SECONDS),
            "the MonitoredItem's DataValueListener was never notified: the throwing"
                + " SubscriptionListener aborted the fan-out");
        assertEquals(VALUE_1.getValue(), received.get().getValue());
      }
    }

    @Test
    void laterMonitoredItemListenersAreNotifiedWhenAnEarlierOneThrows() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var throwingItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        var observedItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_StartTime);
        subscription.addMonitoredItem(throwingItem);
        subscription.addMonitoredItem(observedItem);

        throwingItem.setDataValueListener(
            (i, value) -> {
              throw new IllegalStateException("application DataValueListener failure");
            });

        var received = new AtomicReference<DataValue>();
        var itemNotified = new CountDownLatch(1);
        observedItem.setDataValueListener(
            (i, value) -> {
              received.set(value);
              itemNotified.countDown();
            });

        fixture.scriptable.enqueueDataChange(
            subscriptionId(subscription),
            1,
            List.of(
                new MonitoredItemNotification(clientHandle(throwingItem), VALUE_1),
                new MonitoredItemNotification(clientHandle(observedItem), VALUE_2)));

        assertTrue(
            itemNotified.await(5, TimeUnit.SECONDS),
            "the second MonitoredItem's DataValueListener was never notified: the first item's"
                + " listener threw and abandoned the rest of the DataChangeNotification");
        assertEquals(VALUE_2.getValue(), received.get().getValue());
      }
    }

    // A NotificationMessage carries a sequence of NotificationData elements. A listener failure in
    // one element must not discard the elements behind it: here the StatusChangeNotification that
    // tells the application the Subscription was transferred away.
    @Test
    void remainingNotificationDataIsDeliveredWhenAMonitoredItemListenerThrows() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);
        item.setDataValueListener(
            (i, value) -> {
              throw new IllegalStateException("application DataValueListener failure");
            });

        var received = new AtomicReference<StatusCode>();
        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onStatusChanged(OpcUaSubscription s, StatusCode status) {
                received.set(status);
                statusChanged.countDown();
              }
            });

        ExtensionObject[] notificationData = {
          fixture.scriptable.encode(
              new DataChangeNotification(
                  new MonitoredItemNotification[] {
                    new MonitoredItemNotification(clientHandle(item), VALUE_1)
                  },
                  null)),
          fixture.scriptable.encode(
              new StatusChangeNotification(
                  new StatusCode(StatusCodes.Good_SubscriptionTransferred),
                  DiagnosticInfo.NULL_VALUE))
        };

        fixture.scriptable.enqueueNotification(subscriptionId(subscription), 1, notificationData);

        assertTrue(
            statusChanged.await(5, TimeUnit.SECONDS),
            "onStatusChanged was never called: the throwing DataValueListener escaped the"
                + " NotificationData loop and the StatusChangeNotification was dropped");
        assertEquals(StatusCodes.Good_SubscriptionTransferred, received.get().value());
      }
    }

    // The event fan-out has the same structure as the data fan-out and needs the same isolation.
    @Test
    void monitoredItemEventListenerIsNotifiedWhenSubscriptionListenerThrows() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var item = OpcUaMonitoredItem.newEventItem(NodeIds.Server);
        subscription.addMonitoredItem(item);

        var received = new AtomicReference<Variant[]>();
        var itemNotified = new CountDownLatch(1);
        item.setEventValueListener(
            (i, eventValues) -> {
              received.set(eventValues);
              itemNotified.countDown();
            });

        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onEventReceived(
                  OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<Variant[]> fields) {
                throw new IllegalStateException("application SubscriptionListener failure");
              }
            });

        ExtensionObject[] notificationData = {
          fixture.scriptable.encode(
              new EventNotificationList(
                  new EventFieldList[] {
                    new EventFieldList(
                        clientHandle(item), new Variant[] {Variant.ofString("event")})
                  }))
        };

        fixture.scriptable.enqueueNotification(subscriptionId(subscription), 1, notificationData);

        assertTrue(
            itemNotified.await(5, TimeUnit.SECONDS),
            "the MonitoredItem's EventValueListener was never notified: the throwing"
                + " SubscriptionListener aborted the fan-out");
        assertEquals(Variant.ofString("event"), received.get()[0]);
      }
    }

    // The item and value Lists handed to onDataReceived are the very instances the per-item fan-out
    // then iterates in lockstep. If they are not defensively copied, an application that mutates
    // them silently corrupts delivery: removing an entry shifts the items while the values keep
    // their positions, pairing every remaining item with the wrong DataValue.
    @Test
    void monitoredItemFanOutIsUnaffectedByListenerMutatingTheNotificationLists() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var firstItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        var secondItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_StartTime);
        subscription.addMonitoredItem(firstItem);
        subscription.addMonitoredItem(secondItem);

        var firstReceived = new AtomicReference<DataValue>();
        var secondReceived = new AtomicReference<DataValue>();
        var itemsNotified = new CountDownLatch(2);
        firstItem.setDataValueListener(
            (i, value) -> {
              firstReceived.set(value);
              itemsNotified.countDown();
            });
        secondItem.setDataValueListener(
            (i, value) -> {
              secondReceived.set(value);
              itemsNotified.countDown();
            });

        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onDataReceived(
                  OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {
                try {
                  items.remove(0);
                } catch (UnsupportedOperationException expected) {
                  // An immutable List is one correct answer; a defensive copy is the other. Either
                  // way the fan-out that follows must be unaffected.
                }
              }
            });

        fixture.scriptable.enqueueDataChange(
            subscriptionId(subscription),
            1,
            List.of(
                new MonitoredItemNotification(clientHandle(firstItem), VALUE_1),
                new MonitoredItemNotification(clientHandle(secondItem), VALUE_2)));

        assertTrue(
            itemsNotified.await(5, TimeUnit.SECONDS),
            "not every MonitoredItem was notified: the SubscriptionListener mutated the List the"
                + " fan-out iterates");
        assertEquals(
            VALUE_1.getValue(),
            firstReceived.get().getValue(),
            "first item was paired with the wrong DataValue");
        assertEquals(
            VALUE_2.getValue(),
            secondReceived.get().getValue(),
            "second item was paired with the wrong DataValue");
      }
    }
  }

  /**
   * The SDK's own delivery contract: notifications are handed to the application in the order they
   * were received, and the next PublishRequest is withheld until the application has finished
   * processing the current one — the backpressure mechanism documented on {@code
   * SubscriptionListener.onDataReceived}. Both properties hold only if every callback for a
   * NotificationMessage runs inside the delivery task submitted for that message.
   */
  @Nested
  class CallbackOrdering {

    @Test
    void keepAliveIsDeliveredBeforeTheNotificationThatFollowedIt() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        // A second Subscription raises the in-flight Publish limit to min(2 + 1, max) = 3 so all
        // three scripted responses can be outstanding at once.
        fixture.createSubscription();

        var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        subscription.addMonitoredItem(item);

        List<String> observed = Collections.synchronizedList(new ArrayList<String>());
        var firstDeliveryStarted = new CountDownLatch(1);
        var release = new CountDownLatch(1);
        var allDelivered = new CountDownLatch(3);

        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onDataReceived(
                  OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {
                observed.add(DATA_CALLBACK);
                allDelivered.countDown();

                if (firstDeliveryStarted.getCount() > 0) {
                  firstDeliveryStarted.countDown();
                  // Hold the delivery queue open so the two responses that follow queue up behind
                  // this one instead of racing it.
                  awaitRelease(release);
                }
              }

              @Override
              public void onKeepAliveReceived(OpcUaSubscription s) {
                observed.add(KEEP_ALIVE_CALLBACK);
                allDelivered.countDown();
              }
            });

        UInteger subscriptionId = subscriptionId(subscription);

        assertTrue(
            awaitTrue(() -> fixture.scriptable.getPublishRequestCount() >= 3, 5000),
            "fewer than three Publish requests were in flight");

        try {
          // Sequence numbers 1, 2, 2: contiguous, so no Republish is triggered.
          fixture.scriptable.enqueueDataChange(
              subscriptionId,
              1,
              List.of(new MonitoredItemNotification(clientHandle(item), VALUE_1)));
          assertTrue(
              firstDeliveryStarted.await(5, TimeUnit.SECONDS),
              "the first data notification was not delivered");

          fixture.scriptable.enqueueKeepAlive(subscriptionId, 2);
          assertTrue(
              awaitTrue(() -> subscription.getDeliveryQueue().getQueueSize() == 1, 5000),
              "the keep-alive was not queued for delivery");

          fixture.scriptable.enqueueDataChange(
              subscriptionId,
              2,
              List.of(new MonitoredItemNotification(clientHandle(item), VALUE_2)));
          assertTrue(
              awaitTrue(() -> subscription.getDeliveryQueue().getQueueSize() == 2, 5000),
              "the second data notification was not queued for delivery");
        } finally {
          release.countDown();
        }

        assertTrue(
            allDelivered.await(5, TimeUnit.SECONDS),
            "not every scripted notification was delivered");

        assertEquals(
            List.of(DATA_CALLBACK, KEEP_ALIVE_CALLBACK, DATA_CALLBACK),
            List.copyOf(observed),
            "callbacks were delivered out of order: the keep-alive was re-queued behind the data"
                + " notification that arrived after it");
      }
    }

    @Test
    void publishRequestIsNotSentUntilTheKeepAliveCallbackReturns() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var inKeepAlive = new CountDownLatch(1);
        var release = new CountDownLatch(1);

        subscription.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onKeepAliveReceived(OpcUaSubscription s) {
                inKeepAlive.countDown();
                awaitRelease(release);
              }
            });

        assertTrue(
            awaitTrue(() -> fixture.scriptable.getPublishRequestCount() >= 2, 5000),
            "fewer than two Publish requests were in flight");

        int publishRequestsBefore = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueKeepAlive(subscriptionId(subscription), 1);

        boolean publishRequestSent;
        try {
          assertTrue(inKeepAlive.await(5, TimeUnit.SECONDS), "the keep-alive was not delivered");

          publishRequestSent =
              awaitTrue(
                  () -> fixture.scriptable.getPublishRequestCount() > publishRequestsBefore, 2000);
        } finally {
          release.countDown();
        }

        assertFalse(
            publishRequestSent,
            "a Publish request was sent while onKeepAliveReceived was still running: the"
                + " pending-publish permit was released when the callback was queued rather than"
                + " when it ran");
      }
    }
  }

  private static UInteger subscriptionId(OpcUaSubscription subscription) {
    return subscription.getSubscriptionId().orElseThrow();
  }

  private static UInteger clientHandle(OpcUaMonitoredItem item) {
    return item.getClientHandle().orElseThrow();
  }

  /** Block the calling delivery thread until {@code release} is counted down. */
  private static void awaitRelease(CountDownLatch release) {
    try {
      if (!release.await(10, TimeUnit.SECONDS)) {
        throw new IllegalStateException("timed out waiting to be released");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(e);
    }
  }

  private static boolean awaitTrue(BooleanSupplierThrowing condition, long timeoutMillis)
      throws Exception {
    long deadline = System.currentTimeMillis() + timeoutMillis;
    while (System.currentTimeMillis() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }
    return condition.get();
  }

  @FunctionalInterface
  private interface BooleanSupplierThrowing {
    boolean get() throws Exception;
  }

  /** A running Server whose Publish responses are scripted, plus a connected client. */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      // Long request timeout so parked Publish requests do not time out during the test.
      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(60_000)));
      client.connect();
    }

    OpcUaSubscription createSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();
      return subscription;
    }

    @Override
    public void close() throws Exception {
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
