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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
 * What {@code PublishingManager.processPublishResponse} does with a NotificationMessage whose
 * sequence number is <i>behind</i> the one it expects next.
 *
 * <p>Two different things can produce such a message, and they need opposite handling:
 *
 * <ul>
 *   <li>A <b>duplicate</b>: a retransmission, or a copy of a message the client already recovered
 *       via Republish. Accounting must only ever move forwards, so it has to be discarded — see
 *       {@link PublishSequenceRecoveryTest} for the accounting it would otherwise corrupt.
 *   <li>The Server's <b>numbering having regressed</b>: Part 4 §5.14.1.1 numbers
 *       NotificationMessages per Subscription starting at 1, so a Server that restarts and restores
 *       a Subscription — or that otherwise renumbers one — begins sending sequence numbers far
 *       below the ones the client has already accounted for. Discarding those never ends: the
 *       numbering does not "catch up" to where it was, so every NotificationMessage for the rest of
 *       the Subscription's life is dropped without a trace.
 * </ul>
 *
 * <p>The two are told apart by distance. A message no further behind than the larger of the
 * retransmission queue the Server is advertising and {@code
 * PublishingManager.DEFAULT_MAX_RECOVERABLE_GAP} ({@value #DUPLICATE_WINDOW}) is within
 * retransmission range and is treated as a duplicate; anything further behind than that cannot be a
 * duplicate of anything the Server still holds, and is delivered and resynchronized to.
 *
 * <p>This is the boundary 93bedb32c introduced. Before it, <i>everything</i> behind the expected
 * sequence number was discarded, which for a genuine renumbering meant silently and indefinitely.
 * The tests below drive real PublishResponses through the real client stack via {@link
 * ScriptableSubscriptionServiceSet} and observe which NotificationMessages reach the application.
 */
public class PublishSequenceRegressionTest {

  /**
   * {@code PublishingManager.DEFAULT_MAX_RECOVERABLE_GAP}: the floor on how far behind the expected
   * sequence number a NotificationMessage may be and still be taken for a duplicate.
   */
  private static final int DUPLICATE_WINDOW = 64;

  /** How long to wait for a NotificationMessage that must be delivered. */
  private static final long DELIVERY_WINDOW_MILLIS = 5_000;

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

  /**
   * The Int32 payload of every DataChangeNotification the application has been handed, in delivery
   * order. Each scripted NotificationMessage carries its own sequence number as its value, so this
   * says exactly which messages were delivered and in which order — a count alone could not
   * distinguish "the regressed message was delivered" from "a later one was".
   */
  private final List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

  /** Every {@code retransmitSequenceNumber} the client has asked the Server to Republish. */
  private final List<Long> republishRequests = Collections.synchronizedList(new ArrayList<>());

  private final AtomicInteger notificationDataLostCount = new AtomicInteger();

  @BeforeEach
  void startClientAndServerAndCreateSubscription() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    scriptable = new ScriptableSubscriptionServiceSet(server);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), scriptable);
    }

    // Records the requested sequence number and reports that the Server no longer holds it: no test
    // here expects a Republish, so any request one of them provokes is recorded and asserted about.
    scriptable.setRepublishResponder(
        request -> {
          republishRequests.add(request.getRetransmitSequenceNumber().longValue());

          throw new UaException(StatusCodes.Bad_MessageNotAvailable);
        });

    server.startup().get();

    // One outstanding Publish request preserves scripted response order across server dispatch.
    client =
        TestClient.create(
            server,
            cfg ->
                cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                    .setMaxPendingPublishRequests(uint(1)));
    client.connect();

    subscription = new OpcUaSubscription(client);
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onDataReceived(
              OpcUaSubscription subscription,
              List<OpcUaMonitoredItem> items,
              List<DataValue> values) {

            values.forEach(value -> deliveredValues.add((Integer) value.value().value()));
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
   * The defect. A NotificationMessage far behind the expected sequence number is the Server's
   * numbering having regressed, so it must be delivered and resynchronized to — and the messages
   * that follow it in the <i>new</i> numbering must be delivered as well, which is the part that
   * makes the difference between one dropped message and a Subscription that never delivers
   * anything again.
   *
   * <p>Nothing here should provoke a Republish: the received sequence number is not ahead of the
   * expected one, so there is no gap to repair, and a resynchronization that reported a gap would
   * ask the Server to retransmit billions of NotificationMessages.
   */
  @Test
  void regressedNumberingIsDeliveredAndTheNewNumberingContinues() throws Exception {
    seedLastSequenceNumber(1000);

    // 1000 behind the expected 1001, so far outside the duplicate window that no retransmission
    // queue could hold the messages in between: the Server has started numbering again from 1.
    sendDataChange(1, uint(1));

    assertTrue(
        awaitDelivered(1),
        "the NotificationMessage carrying sequence 1 was never delivered. It is 1000 behind the"
            + " expected 1001 — far beyond the "
            + DUPLICATE_WINDOW
            + " that could be a duplicate — so it is the Server's numbering having regressed, and"
            + " discarding it drops every NotificationMessage until the numbering catches back up,"
            + " which for a renumbered Subscription is never");

    sendDataChange(2, uint(1), uint(2));
    sendDataChange(3, uint(1), uint(2), uint(3));

    assertTrue(
        awaitTrue(() -> deliveredValues.size() >= 3),
        "the NotificationMessages that followed the regressed one were not delivered: the"
            + " accounting was not resynchronized to it, so they are all behind"
            + " lastSequenceNumber too");

    assertEquals(
        List.of(1, 2, 3),
        List.copyOf(deliveredValues),
        "every NotificationMessage of the Server's new numbering must be delivered exactly once and"
            + " in order");

    assertEquals(
        List.<Long>of(),
        List.copyOf(republishRequests),
        "resynchronizing to a regressed sequence number must not report a gap: the received"
            + " sequence number is behind the expected one, not ahead of it");

    assertEquals(
        0,
        notificationDataLostCount.get(),
        "no NotificationMessage was lost: the Server renumbered, it did not drop anything");
  }

  /**
   * Control for the test above, at the exact boundary: a NotificationMessage {@value
   * #DUPLICATE_WINDOW} behind the expected one is still within retransmission range, so it is still
   * a duplicate and must still be discarded. Without this the fix could have turned every duplicate
   * into a resynchronization, which rolls {@code lastSequenceNumber} backwards and makes every
   * message already received after it look missing.
   */
  @Test
  void duplicateAtTheEdgeOfTheDuplicateWindowIsStillDiscarded() throws Exception {
    seedLastSequenceNumber(100);

    // Exactly DUPLICATE_WINDOW behind the expected 101.
    long duplicate = 101 - DUPLICATE_WINDOW;
    sendDataChange(duplicate, uint(duplicate));

    // The next message in the established numbering, which is delivered: the serial processing
    // queue means observing this one is proof the duplicate ahead of it has been dealt with.
    sendDataChange(101, uint(duplicate), uint(101));

    assertTrue(awaitDelivered(101), "the expected NotificationMessage 101 was never delivered");

    assertEquals(
        List.of(101),
        List.copyOf(deliveredValues),
        "the NotificationMessage "
            + DUPLICATE_WINDOW
            + " behind the expected one is within the Server's retransmission range, so it is a"
            + " duplicate and must not be delivered a second time");
  }

  /**
   * The other side of the same boundary: one step further behind than a duplicate can be, so it is
   * the Server's numbering having regressed and is delivered. Together with the test above this
   * pins where the boundary is, not merely that there is one.
   */
  @Test
  void numberingRegressedJustBeyondTheDuplicateWindowIsDelivered() throws Exception {
    seedLastSequenceNumber(100);

    long regressed = 101 - (DUPLICATE_WINDOW + 1);
    sendDataChange(regressed, uint(regressed));

    assertTrue(
        awaitDelivered((int) regressed),
        "a NotificationMessage "
            + (DUPLICATE_WINDOW + 1)
            + " behind the expected 101 is further behind than any duplicate can be — beyond both"
            + " the advertised retransmission queue and DEFAULT_MAX_RECOVERABLE_GAP — so it is a"
            + " regression in the Server's numbering and must be delivered");

    assertEquals(
        List.of((int) regressed),
        List.copyOf(deliveredValues),
        "exactly the regressed NotificationMessage must be delivered");
  }

  /**
   * The same control as {@link #duplicateAtTheEdgeOfTheDuplicateWindowIsStillDiscarded} on an
   * ordinary trace with no seeded state: a retransmitted copy of a message received a moment ago is
   * one step behind, and is discarded. This is also the positive control for the fixture — it
   * proves the scripted notifications, the ClientHandle and the delivery observation are wired up
   * without any reflection involved.
   */
  @Test
  void duplicateInAnOrdinaryTraceIsStillDiscarded() throws Exception {
    for (long sequenceNumber = 1; sequenceNumber <= 3; sequenceNumber++) {
      sendDataChange(sequenceNumber, uint(sequenceNumber));

      assertTrue(
          awaitDelivered((int) sequenceNumber),
          "sequence " + sequenceNumber + " was never delivered");
    }

    // A retransmitted copy of NotificationMessage 2, which the client has already accounted for.
    sendDataChange(2, uint(1), uint(2), uint(3));
    sendDataChange(4, uint(1), uint(2), uint(3), uint(4));

    assertTrue(awaitDelivered(4), "sequence 4 was never delivered");

    assertEquals(
        List.of(1, 2, 3, 4),
        List.copyOf(deliveredValues),
        "the retransmitted copy of NotificationMessage 2 must not be delivered a second time");
  }

  // region fixture helpers

  /**
   * Enqueue a PublishResponse carrying a DataChangeNotification whose value is {@code
   * sequenceNumber}, so the delivered value identifies the NotificationMessage it came from.
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

  /**
   * Seeds the {@code lastSequenceNumber} the {@code PublishingManager} tracks for this
   * Subscription.
   *
   * <p><b>White-box seeding.</b> {@code lastSequenceNumber} only ever advances one
   * NotificationMessage at a time, so there is no wire path to a value hundreds ahead of where the
   * Server's numbering will restart: reaching it legitimately would mean scripting that many
   * responses, and reaching it by a forward jump would spin the Republish loop instead. The same
   * technique {@code PublishSequenceRecoveryTest} uses for the rollover boundary. Everything after
   * the seed is the production code path: real PublishResponses over a real connection, and the
   * real accounting, gap detection and delivery. Called before any PublishResponse has been
   * processed, so nothing races with it.
   */
  private void seedLastSequenceNumber(long sequenceNumber) throws Exception {
    PublishingManager publishingManager = client.getPublishingManager();

    Field subscriptionDetailsField =
        PublishingManager.class.getDeclaredField("subscriptionDetails");
    subscriptionDetailsField.setAccessible(true);

    Map<?, ?> subscriptionDetails = (Map<?, ?>) subscriptionDetailsField.get(publishingManager);
    Object details = subscriptionDetails.get(subscriptionId);
    assertNotNull(details, "the Subscription is not registered with the PublishingManager");

    Field lastSequenceNumberField = details.getClass().getDeclaredField("lastSequenceNumber");
    lastSequenceNumberField.setAccessible(true);
    lastSequenceNumberField.setLong(details, sequenceNumber);
  }

  private boolean awaitDelivered(int value) throws Exception {
    return awaitTrue(() -> deliveredValues.contains(value));
  }

  /** Polls {@code condition} until it holds or {@link #DELIVERY_WINDOW_MILLIS} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(DELIVERY_WINDOW_MILLIS);

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
