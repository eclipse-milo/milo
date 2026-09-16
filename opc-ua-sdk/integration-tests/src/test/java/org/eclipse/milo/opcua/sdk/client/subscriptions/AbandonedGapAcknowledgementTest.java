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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * What the client acknowledges when it gives a gap up as lost data.
 *
 * <p>Part 4 §5.14.7.1, of availableSequenceNumbers: "The Client should acknowledge all Messages in
 * this list for which it will not request retransmission." An unacknowledged NotificationMessage
 * stays in the Server's retransmission queue, is re-advertised in the availableSequenceNumbers of
 * every subsequent PublishResponse, and holds its memory for the life of the Subscription. So when
 * the client decides a gap is too large to recover — {@code missingSequenceNumbers} reports it as
 * lost data and never asks for any of it — the messages it is abandoning have to be acknowledged,
 * or the Server holds them forever for a retransmission that will never be requested. That
 * acknowledgement is what 93bedb32c added.
 *
 * <p><b>This deliberately acknowledges NotificationMessages the client never received</b>, which is
 * the opposite of what {@link PublishAcknowledgementTest} and {@code
 * PublishSequenceRecoveryTest.InitialKeepAlive} pin. Both rules are correct, and the distinction is
 * whether the client might still want the message:
 *
 * <ul>
 *   <li>While a message is still <i>recoverable</i> — the client will, or might, Republish it — an
 *       acknowledgement destroys the Server's only copy of data the client still wants. §5.14.5.2:
 *       "the Server may delete the Message with this sequence number from its retransmission
 *       queue". Never acknowledge those.
 *   <li>Once the client has <i>decided never to ask</i>, the Server holding the message serves
 *       nobody. Acknowledge those.
 * </ul>
 *
 * <p>The two tests below are that pair: one drives a gap the client abandons and asserts the
 * abandoned sequence numbers are acknowledged, the other drives a gap the client does try to
 * recover and asserts that a message it failed to recover is <i>not</i> acknowledged.
 */
public class AbandonedGapAcknowledgementTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 5_000;

  /**
   * Long enough that nothing times out on its own, so a parked Publish request stays parked and any
   * failure observed below is scripted rather than incidental.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  private TestServer testServer;
  private OpcUaServer server;
  private OpcUaClient client;
  private ScriptableSubscriptionServiceSet scriptable;
  private OpcUaSubscription subscription;
  private UInteger subscriptionId;
  private UInteger clientHandle;

  /** Every {@code retransmitSequenceNumber} the client has asked the Server to Republish. */
  private final List<Long> republishRequests = Collections.synchronizedList(new ArrayList<>());

  private final AtomicInteger dataReceivedCount = new AtomicInteger();
  private final AtomicInteger notificationDataLostCount = new AtomicInteger();

  @BeforeEach
  void startClientAndServerAndCreateSubscription() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    scriptable = new ScriptableSubscriptionServiceSet(server);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), scriptable);
    }

    // Models a Server that has already evicted or cannot produce the requested message, and records
    // that it was asked at all.
    scriptable.setRepublishResponder(
        request -> {
          republishRequests.add(request.getRetransmitSequenceNumber().longValue());

          throw new UaException(StatusCodes.Bad_MessageNotAvailable);
        });

    server.startup().get();

    client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS)));
    client.connect();

    subscription = new OpcUaSubscription(client);
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onDataReceived(
              OpcUaSubscription subscription,
              List<OpcUaMonitoredItem> items,
              List<DataValue> values) {

            dataReceivedCount.incrementAndGet();
          }

          @Override
          public void onNotificationDataLost(OpcUaSubscription subscription) {
            notificationDataLostCount.incrementAndGet();
          }
        });
    subscription.create();

    subscriptionId = subscription.getSubscriptionId().orElseThrow();

    // Client-side only: addMonitoredItem assigns the ClientHandle the notification fan-out looks
    // notifications up by, and no Server-side item takes part in delivering a scripted one.
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    subscription.addMonitoredItem(item);
    clientHandle = item.getClientHandle().orElseThrow();
  }

  @AfterEach
  void stopClientAndServer() throws Exception {
    scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
    try {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } finally {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  /**
   * The defect. NotificationMessage 1 is received, then 10 arrives with only 2 and 3 still in the
   * Server's retransmission queue. The gap 2..9 is eight messages and the Server is advertising
   * two, so it is not recoverable and {@code missingSequenceNumbers} gives it up as lost data
   * without requesting any of it. The two the Server is still holding must be acknowledged, because
   * nothing will ever ask for them.
   */
  @Test
  void sequenceNumbersAbandonedWithAnUnrecoverableGapAreAcknowledged() throws Exception {
    sendDataChange(1, uint(1));
    assertTrue(awaitAcknowledged(1), "sequence 1 was never acknowledged");

    // Sequences 2..9 were sent and lost; the Server still holds 2 and 3 and has evicted the rest.
    sendDataChange(10, uint(2), uint(3), uint(10));

    assertTrue(
        awaitTrue(() -> dataReceivedCount.get() >= 2),
        "the NotificationMessage that revealed the gap was never delivered");
    assertTrue(awaitAcknowledged(10), "sequence 10 was never acknowledged");
    assertTrue(
        awaitTrue(() -> notificationDataLostCount.get() >= 1),
        "the gap was not given up on as lost data, so the scenario under test did not happen and"
            + " the assertions below prove nothing");
    assertEquals(
        List.<Long>of(),
        List.copyOf(republishRequests),
        "a gap given up on as lost data must not be requested via Republish; if it was, this is not"
            + " the abandoned-gap path");

    assertTrue(
        awaitAcknowledged(2),
        "sequence 2 was advertised as available for retransmission and the client has decided never"
            + " to request it, so Part 4 §5.14.7.1 requires it to be acknowledged. Unacknowledged,"
            + " it sits in the Server's retransmission queue — re-advertised in every"
            + " PublishResponse — for the life of the Subscription");
    assertTrue(
        awaitAcknowledged(3),
        "sequence 3 was advertised as available for retransmission and the client has decided never"
            + " to request it, so Part 4 §5.14.7.1 requires it to be acknowledged");
  }

  /**
   * The companion rule, which the test above must not be read as overturning: while a gap is small
   * enough for the client to try to recover it, a message the recovery <i>failed</i> to bring back
   * is still not acknowledged. The client never had it, and the Server's copy — if the failure was
   * transient — is the only one there is.
   *
   * <p>NotificationMessage 1 is received, then 3 arrives with 2 and 3 advertised: a one-message gap
   * inside a two-message retransmission queue, so it is recoverable and Republish(2) is attempted.
   * The Republish fails, the data is reported lost, and 2 must remain unacknowledged.
   */
  @Test
  void aSequenceNumberThatFailedToRepublishIsNotAcknowledged() throws Exception {
    sendDataChange(1, uint(1));
    assertTrue(awaitAcknowledged(1), "sequence 1 was never acknowledged");

    // Sequence 2 was sent and lost, and the Server still holds it: a recoverable gap.
    sendDataChange(3, uint(2), uint(3));

    assertEquals(
        List.of(2L),
        awaitRepublishRequests(),
        "the missing NotificationMessage 2 was recoverable and must be requested via Republish; if"
            + " it was not, this is not the recoverable-gap path and the assertion below proves"
            + " nothing");

    assertTrue(awaitAcknowledged(3), "sequence 3 was never acknowledged");

    assertFalse(
        acknowledged(2),
        "NotificationMessage 2 was requested via Republish and not recovered, so the client never"
            + " had it: acknowledging it would let the Server delete the only copy of data the"
            + " client asked for. Only a gap the client has decided never to request may be"
            + " acknowledged");
  }

  // region fixture helpers

  /**
   * Enqueue a PublishResponse carrying a DataChangeNotification at {@code sequenceNumber}, so the
   * message is a data message rather than a keep-alive and reaches {@code onDataReceived}.
   */
  private void sendDataChange(long sequenceNumber, UInteger... available) {
    scriptable.enqueueDataChange(
        subscriptionId,
        sequenceNumber,
        List.of(
            new MonitoredItemNotification(
                clientHandle, new DataValue(Variant.ofInt32((int) sequenceNumber)))),
        available);
  }

  private boolean acknowledged(long sequenceNumber) {
    return scriptable.getReceivedAcknowledgements().stream()
        .anyMatch(
            ack ->
                ack.getSubscriptionId().equals(subscriptionId)
                    && ack.getSequenceNumber().longValue() == sequenceNumber);
  }

  private boolean awaitAcknowledged(long sequenceNumber) throws Exception {
    return awaitTrue(() -> acknowledged(sequenceNumber));
  }

  /** The recorded Republish requests, once at least one has arrived or the timeout has elapsed. */
  private List<Long> awaitRepublishRequests() throws Exception {
    awaitTrue(() -> !republishRequests.isEmpty());

    return List.copyOf(republishRequests);
  }

  /** Polls {@code condition} until it holds or {@link #AWAIT_TIMEOUT_MILLIS} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

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
