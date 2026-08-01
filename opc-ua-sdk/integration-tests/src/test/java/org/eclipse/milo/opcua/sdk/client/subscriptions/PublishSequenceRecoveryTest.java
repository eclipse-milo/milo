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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet.RepublishResponder;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Sequence-number accounting in {@code PublishingManager.processPublishResponse}.
 *
 * <p>Part 4 §5.14.1.1 defines the NotificationMessage sequence number: "The value 0 is never used
 * for the sequence number. The first NotificationMessage sent on a Subscription has a sequence
 * number of 1. If the sequence number rolls over, it rolls over to 1." The same clause defines a
 * keep-alive as a message that "does not contain any Notifications and ... contains the sequence
 * number of the next NotificationMessage that is to be sent" — so a keep-alive carrying sequence
 * {@code n} is positive evidence that {@code n} has <i>not</i> been sent yet, and is <i>not</i>
 * evidence that {@code n - 1} was received.
 *
 * <p>{@code lastSequenceNumber} is the client's record of the last NotificationMessage actually
 * accounted for; it is the sole input to gap detection and therefore to Republish recovery. Every
 * test here drives real PublishResponses through the real client stack via {@link
 * ScriptableSubscriptionServiceSet} and observes the Republish requests, {@code
 * onNotificationDataLost} callbacks, and SubscriptionAcknowledgements that result.
 */
public class PublishSequenceRecoveryTest {

  /** The largest legal sequence number; the successor of this value is 1, never 0 (§5.14.1.1). */
  private static final long MAX_SEQUENCE_NUMBER = 0xFFFF_FFFFL;

  private TestServer testServer;
  private OpcUaServer server;
  private OpcUaClient client;
  private ScriptableSubscriptionServiceSet scriptable;
  private OpcUaSubscription subscription;
  private UInteger subscriptionId;

  /** Every {@code retransmitSequenceNumber} the client has asked the Server to Republish. */
  private final List<Long> republishRequests = Collections.synchronizedList(new ArrayList<>());

  private final AtomicInteger keepAliveCount = new AtomicInteger();
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

    server.startup().get();

    // Long request timeout so parked Publish requests do not time out during the test.
    client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(60_000)));
    client.connect();

    subscription = new OpcUaSubscription(client);
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onKeepAliveReceived(OpcUaSubscription subscription) {
            keepAliveCount.incrementAndGet();
          }

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
   * Positive control for every "was Republish called for X?" assertion below: an ordinary gap
   * between two <i>data</i> messages is detected today, proving the recording Republish responder
   * and the Republish observation are wired up. If this test fails, the fixture is broken rather
   * than the sequence accounting.
   */
  @Test
  void gapBetweenDataMessagesIsRepublished() throws Exception {
    scriptable.setRepublishResponder(recordingRepublishResponder());

    // Sequence 1 is received normally, sequence 2 is lost, sequence 3 arrives.
    scriptable.enqueueDataChange(subscriptionId, 1, dataNotifications(), uint(1));
    assertTrue(awaitAcknowledgement(1), "sequence 1 was never acknowledged");

    scriptable.enqueueDataChange(subscriptionId, 3, dataNotifications(), uint(1), uint(3));
    assertTrue(awaitAtLeast(dataReceivedCount, 2), "sequence 3 was never delivered");

    assertEquals(
        List.of(2L),
        List.copyOf(republishRequests),
        "the missing NotificationMessage 2 must be requested via Republish");
  }

  /**
   * Finding 1: an initial keep-alive carrying sequence 1 is recorded as if NotificationMessage 1
   * had been received.
   *
   * <p>Part 4 §5.14.1.1: a keep-alive carries "the sequence number of the next NotificationMessage
   * that is to be sent", so an initial keep-alive with sequence 1 means the first
   * NotificationMessage has <i>not</i> yet been sent. Recording it as received means the loss of
   * the real NotificationMessage 1 is never detected, and the client goes on to acknowledge it —
   * which purges it from the Server's retransmission queue and makes it unrecoverable.
   *
   * <p>These tests deliberately re-create the interop scenario from #1401 (keep-alive 1 followed by
   * first data 2). That commit added the {@code receivedSequenceNumber == 1} disjunct to suppress a
   * Republish(1) it considered doomed; per §5.14.1.1 NotificationMessage 1 is genuinely missing in
   * that trace and Republish(1) is the correct behavior.
   */
  @Nested
  class InitialKeepAlive {

    /**
     * Keep-alive 1, then data 2: NotificationMessage 1 was sent and lost, so the client must try to
     * recover it.
     */
    @Test
    void notificationMessageLostAfterAnInitialKeepAliveIsRepublished() throws Exception {
      scriptable.setRepublishResponder(recordingRepublishResponder());

      scriptable.enqueueKeepAlive(subscriptionId, 1);
      assertTrue(awaitAtLeast(keepAliveCount, 1), "the initial keep-alive was never delivered");

      // Sequence 1 was sent by the Server (it is still available for Republish) but never reached
      // the client; sequence 2 is the next message to arrive.
      scriptable.enqueueDataChange(subscriptionId, 2, dataNotifications(), uint(1), uint(2));
      assertTrue(awaitAtLeast(dataReceivedCount, 1), "sequence 2 was never delivered");

      assertEquals(
          List.of(1L),
          List.copyOf(republishRequests),
          "NotificationMessage 1 was lost and must be requested via Republish; an initial"
              + " keep-alive carrying sequence 1 is not evidence that message 1 was received");
    }

    /**
     * The aggravating half of finding 1: because the acknowledgement set is refilled verbatim from
     * {@code availableSequenceNumbers}, the client acknowledges sequence 1 — a NotificationMessage
     * it never received and never recovered. The Server then purges it from the retransmission
     * queue and the data is gone for good.
     *
     * <p>The Republish attempt is scripted to fail with {@code Bad_MessageNotAvailable}, so nothing
     * in this trace ever recovers sequence 1: acknowledging it is unconditionally wrong.
     */
    @Test
    void unreceivedNotificationMessageIsNotAcknowledged() throws Exception {
      scriptable.setRepublishResponder(recordingRepublishResponder());

      scriptable.enqueueKeepAlive(subscriptionId, 1);
      assertTrue(awaitAtLeast(keepAliveCount, 1), "the initial keep-alive was never delivered");

      scriptable.enqueueDataChange(subscriptionId, 2, dataNotifications(), uint(1), uint(2));

      // Sequences 1 and 2 are both advertised as available, so both are acknowledged in the same
      // PublishRequest: observing the acknowledgement of 2 is enough to decide about 1.
      assertTrue(awaitAcknowledgement(2), "sequence 2 was never acknowledged");

      assertFalse(
          acknowledged(1),
          "the client acknowledged NotificationMessage 1, which it never received and never"
              + " recovered; the Server will purge it from the retransmission queue");
    }
  }

  /**
   * Finding 2: a gap that ends at a keep-alive advances {@code lastSequenceNumber} to the first
   * missing sequence instead of to the last one, so the next keep-alive re-detects the tail of the
   * same gap.
   *
   * <p>The gap loop bounds are correct, but the recovered range is not recorded: {@code
   * lastSequenceNumber = expectedSequenceNumber} sets it to the <i>first</i> missing sequence, and
   * the trailing {@code == 1 || !isKeepAlive} guard declines to correct it because this is a
   * keep-alive. Each subsequent keep-alive therefore repeats the recovery for the remaining tail.
   * Meanwhile the client has already acknowledged the whole range, so the Server has purged it and
   * the repeat cycles fail — producing spurious {@code onNotificationDataLost} callbacks (and, if
   * the acknowledgements have not landed yet, duplicate delivery to the application).
   */
  @Nested
  class GapEndingInAKeepAlive {

    /** The number of keep-alives carrying sequence 10 that the Server sends after the gap. */
    private static final int KEEP_ALIVE_ROUNDS = 4;

    private final List<Long> expectedAttempts = List.of(6L, 7L, 8L, 9L);

    /**
     * Every missing NotificationMessage must be requested exactly once. Repeating the request is
     * not merely wasteful: the client has already acknowledged 6-9, so the Server no longer holds
     * them and every repeat is a blocking round-trip that can only fail.
     */
    @Test
    void eachMissingNotificationMessageIsRepublishedExactlyOnce() throws Exception {
      driveGapEndingInKeepAlives();

      assertEquals(
          expectedAttempts,
          List.copyOf(republishRequests),
          "each NotificationMessage missing before the keep-alive must be requested exactly once");
    }

    /**
     * {@code onNotificationDataLost} is the SDK's "your data is gone" signal. Recovering 6-9
     * successfully and then re-requesting them from a Server that has (correctly) purged them
     * raises that alarm for data the application already received.
     */
    @Test
    void fullyRecoveredGapDoesNotReportNotificationDataLost() throws Exception {
      driveGapEndingInKeepAlives();

      assertEquals(
          0,
          notificationDataLostCount.get(),
          "every missing NotificationMessage was recovered, so no data was lost");
    }

    /**
     * Drives {@code lastSequenceNumber} to 5, then delivers {@link #KEEP_ALIVE_ROUNDS} keep-alives
     * carrying sequence 10 with 6-9 advertised as available.
     *
     * <p>The Republish responder models the Server's retransmission queue: each sequence can be
     * republished once, after which it is gone (the client's own acknowledgements purge it).
     */
    private void driveGapEndingInKeepAlives() throws Exception {
      // Establish lastSequenceNumber = 5 with five in-order data messages, staying in lockstep with
      // the responder queue by waiting for each acknowledgement before enqueueing the next.
      for (long sequenceNumber = 1; sequenceNumber <= 5; sequenceNumber++) {
        scriptable.enqueueDataChange(
            subscriptionId, sequenceNumber, dataNotifications(), uint(sequenceNumber));

        assertTrue(
            awaitAcknowledgement(sequenceNumber),
            "sequence " + sequenceNumber + " was never acknowledged");
      }

      assertEquals(
          List.<Long>of(),
          List.copyOf(republishRequests),
          "control: five in-order data messages must not trigger any Republish");

      scriptable.setRepublishResponder(retransmissionQueueResponder(Set.of(6L, 7L, 8L, 9L)));

      // Sequences 6-9 were sent and lost; the keep-alive announces that 10 is next.
      for (int round = 1; round <= KEEP_ALIVE_ROUNDS; round++) {
        scriptable.enqueueKeepAlive(subscriptionId, 10, uint(6), uint(7), uint(8), uint(9));

        assertTrue(
            awaitAtLeast(keepAliveCount, round),
            "keep-alive round " + round + " was not delivered");
      }
    }
  }

  /**
   * Finding 12: the sequence arithmetic is plain unwrapped {@code long} math, so it breaks at the
   * UInt32 rollover boundary defined by Part 4 §5.14.1.1 ("If the sequence number rolls over, it
   * rolls over to 1").
   *
   * <p><b>White-box seeding.</b> There is no wire path to a near-maximum {@code
   * lastSequenceNumber}: it only ever advances one message at a time, and any forward jump large
   * enough to reach the boundary would spin the (unbounded) Republish loop billions of times. These
   * tests therefore seed {@code lastSequenceNumber} reflectively and then drive <i>real</i>
   * PublishResponses through the real client stack, so everything after the seed — gap detection,
   * Republish recovery, delivery — is the production code path.
   */
  @Nested
  class SequenceNumberRollover {

    /**
     * The message before the rollover is lost. {@code expected = last + 1} is the last legal
     * sequence number, the wrapped sequence 1 compares as "behind" it, and the gap is silently
     * swallowed.
     */
    @Test
    void notificationMessageLostAtTheRolloverBoundaryIsRepublished() throws Exception {
      scriptable.setRepublishResponder(recordingRepublishResponder());
      seedLastSequenceNumber(MAX_SEQUENCE_NUMBER - 1);

      // MAX_SEQUENCE_NUMBER was sent and lost; the sequence rolls over to 1.
      scriptable.enqueueDataChange(
          subscriptionId, 1, dataNotifications(), uint(MAX_SEQUENCE_NUMBER), uint(1));
      assertTrue(awaitAtLeast(dataReceivedCount, 1), "the wrapped sequence 1 was never delivered");

      assertEquals(
          List.of(MAX_SEQUENCE_NUMBER),
          List.copyOf(republishRequests),
          "the NotificationMessage lost at the rollover boundary must be requested via Republish");
    }

    /**
     * A keep-alive arriving immediately after the rollover. {@code expected = last + 1} is
     * 4294967296 — a value no NotificationMessage can carry — so no received sequence number can
     * ever again compare as "ahead" and gap detection is dead from here on.
     */
    @Test
    void keepAliveAfterRolloverDetectsTheMissingNotificationMessage() throws Exception {
      scriptable.setRepublishResponder(recordingRepublishResponder());
      seedLastSequenceNumber(MAX_SEQUENCE_NUMBER);

      // The wrapped sequence 1 was sent and lost; the keep-alive announces that 2 is next.
      scriptable.enqueueKeepAlive(subscriptionId, 2, uint(1));
      assertTrue(awaitAtLeast(keepAliveCount, 1), "the keep-alive was never delivered");

      assertEquals(
          List.of(1L),
          List.copyOf(republishRequests),
          "the wrapped NotificationMessage 1 was lost and must be requested via Republish");
    }
  }

  // region fixture helpers

  /** Records the requested sequence number and reports that the Server no longer holds it. */
  private RepublishResponder recordingRepublishResponder() {
    return request -> {
      republishRequests.add(request.getRetransmitSequenceNumber().longValue());

      throw new UaException(StatusCodes.Bad_MessageNotAvailable);
    };
  }

  /**
   * Models a Server retransmission queue: each sequence in {@code held} can be republished once,
   * after which it is gone — the same effect the client's own acknowledgements have.
   */
  private RepublishResponder retransmissionQueueResponder(Set<Long> held) {
    Set<Long> remaining = Collections.synchronizedSet(new HashSet<>(held));

    return request -> {
      long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
      republishRequests.add(sequenceNumber);

      if (remaining.remove(sequenceNumber)) {
        return scriptable.buildRepublishResponse(
            request, sequenceNumber, encodedDataNotifications());
      } else {
        throw new UaException(StatusCodes.Bad_MessageNotAvailable);
      }
    };
  }

  /**
   * A single MonitoredItemNotification. The ClientHandle is not registered with the Subscription,
   * which is irrelevant here: what matters is that the NotificationMessage carries notification
   * data, making it a data message rather than a keep-alive, and that delivery reaches {@code
   * onDataReceived}.
   */
  private static List<MonitoredItemNotification> dataNotifications() {
    return List.of(new MonitoredItemNotification(uint(1), new DataValue(new Variant(0))));
  }

  private ExtensionObject[] encodedDataNotifications() {
    return new ExtensionObject[] {
      scriptable.encode(
          new DataChangeNotification(
              dataNotifications().toArray(MonitoredItemNotification[]::new), null))
    };
  }

  /**
   * Seeds the {@code lastSequenceNumber} the {@code PublishingManager} tracks for this
   * Subscription. Called before any PublishResponse has been processed, so nothing races with it.
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

  private boolean acknowledged(long sequenceNumber) {
    return scriptable.getReceivedAcknowledgements().stream()
        .anyMatch(
            ack ->
                ack.getSubscriptionId().equals(subscriptionId)
                    && ack.getSequenceNumber().longValue() == sequenceNumber);
  }

  private boolean awaitAcknowledgement(long sequenceNumber) throws Exception {
    return awaitTrue(() -> acknowledged(sequenceNumber));
  }

  private static boolean awaitAtLeast(AtomicInteger counter, int count) throws Exception {
    return awaitTrue(() -> counter.get() >= count);
  }

  /** Polls {@code condition} until it holds or the (generous) timeout elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
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
