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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Republish recovery in {@code PublishingManager.recoverMissingNotificationMessages}.
 *
 * <p>When a PublishResponse reveals a gap in the NotificationMessage sequence, the client recovers
 * the missing messages with the Republish service (Part 4 §5.14.6). Recovery runs on {@code
 * PublishingManager}'s processing queue — one {@link TaskQueue} with a concurrency limit of 1,
 * shared by every Subscription on the client — and calls the synchronous {@code
 * OpcUaClient.republish}, which is {@code republishAsync(...).get()}: an unbounded blocking wait
 * for a network round trip, taken on the queue that is processing PublishResponses.
 *
 * <p>Two consequences follow, and the first two nested classes assert against them:
 *
 * <ol>
 *   <li>The thread that blocks belongs to the transport executor, and the RepublishResponse's
 *       completion is dispatched <i>onto that same executor</i>: {@code
 *       AbstractUascClientTransport.handleResponse} completes everything that is not a
 *       PublishResponse via {@code config.getExecutor().execute(...)}. A client whose executor has
 *       one thread therefore self-deadlocks — the only thread that could complete the Republish
 *       response is the one waiting for it — and the deadlock is permanent, not merely slow: {@code
 *       handleResponse} cancels the request timeout the moment the response arrives, so the wheel
 *       timer that rescues a Server that never answers cannot rescue a Server that does. A
 *       single-threaded executor is an ordinary application choice; {@code
 *       OpcTcpClientTransportConfigBuilder.setExecutor} exists for it.
 *   <li>Even with threads to spare, recovery holds the single processing queue for a network round
 *       trip per missing message, so PublishResponses for <i>every other Subscription</i> on that
 *       client wait behind it.
 * </ol>
 *
 * <p>The third nested class covers what recovery asks for rather than how it waits. Part 4
 * §5.14.5.2 defines the Publish response's availableSequenceNumbers as "a list of sequence number
 * ranges that identify unacknowledged NotificationMessages that are available for retransmission
 * from the Subscription's retransmission queue". Recovery walks the numeric gap instead of that
 * list, so it spends blocking round trips asking for messages the Server has just said it no longer
 * holds.
 */
public class RepublishRecoveryTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for work that a blocked processing queue is holding up. A client that does not
   * block on the processing queue gets through a loopback Republish round trip in single-digit
   * milliseconds, so this is generous by three orders of magnitude — but the stalls below do not
   * end at all, so no larger value would change an outcome.
   */
  private static final long STALL_WINDOW_MILLIS = 5_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request, and no Republish. The
   * stalls asserted against below are therefore the client's own doing and not a request timeout.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** NotificationMessages 2, 3 and 4 are missing when 5 arrives. */
  private static final int MISSING_COUNT = 3;

  private static final long GAP_REVEALED_BY = 5;

  /** Upper bound on how long a gated Republish response is held, so nothing hangs indefinitely. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /** How long teardown waits for a client that may be deadlocked to disconnect. */
  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /**
   * Recovery must not require a second thread to make progress. The blocking {@code republish()}
   * call is made from a transport-executor thread and the response it waits for is completed by the
   * transport executor, so a client whose executor has a single thread cannot complete its own
   * Republish.
   *
   * <p>The Server here is entirely healthy: it answers every Republish request immediately.
   */
  @Nested
  class BlockedByItsOwnRecovery {

    @Test
    void everyMissingNotificationMessageIsRequestedWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(Executors.newSingleThreadExecutor())) {
        Sub sub = fixture.createSubscription(1);
        fixture.revealGap(sub);

        assertTrue(
            fixture.awaitRepublishRequests(1, AWAIT_TIMEOUT_MILLIS),
            "the gap was never detected: no Republish request reached the Server");

        assertTrue(
            fixture.awaitRepublishRequests(MISSING_COUNT, STALL_WINDOW_MILLIS),
            () ->
                "only "
                    + fixture.republishRequests.size()
                    + " of "
                    + MISSING_COUNT
                    + " missing NotificationMessages were requested; the Server answered the"
                    + " first Republish immediately, but completing that response is a task for"
                    + " the executor thread that is blocked waiting for it");
      }
    }

    /**
     * The same stall as the application sees it. Until recovery finishes, the NotificationMessage
     * that revealed the gap has not been delivered either — it is delivered after the recovered
     * ones — so the Subscription simply stops producing data.
     */
    @Test
    void notificationMessageThatRevealedTheGapIsDeliveredWhenTheExecutorHasOneThread()
        throws Exception {

      try (var fixture = new Fixture(Executors.newSingleThreadExecutor())) {
        Sub sub = fixture.createSubscription(1);
        int deliveriesBefore = sub.dataReceivedCount().get();

        fixture.revealGap(sub);

        assertTrue(
            fixture.awaitRepublishRequests(1, AWAIT_TIMEOUT_MILLIS),
            "the gap was never detected: no Republish request reached the Server");

        assertTrue(
            fixture.awaitTrue(
                () -> sub.dataReceivedCount().get() > deliveriesBefore, STALL_WINDOW_MILLIS),
            "no further NotificationMessage was delivered: the recovery of the "
                + MISSING_COUNT
                + " missing messages is blocking the queue that processes PublishResponses");
      }
    }

    /**
     * The control that proves the two tests above are about the executor and not about the fixture:
     * the identical script, driven by a client whose executor has threads to spare, recovers the
     * whole gap and delivers.
     */
    @Test
    void everyMissingNotificationMessageIsRequestedWhenTheExecutorHasThreadsToSpare()
        throws Exception {

      try (var fixture = new Fixture(cachedThreadPool())) {
        Sub sub = fixture.createSubscription(1);
        int deliveriesBefore = sub.dataReceivedCount().get();

        fixture.revealGap(sub);

        assertTrue(
            fixture.awaitRepublishRequests(MISSING_COUNT, STALL_WINDOW_MILLIS),
            () ->
                "control: with a multi-threaded executor all "
                    + MISSING_COUNT
                    + " missing NotificationMessages must be requested, but only "
                    + fixture.republishRequests.size()
                    + " were");

        assertTrue(
            fixture.awaitTrue(
                () -> sub.dataReceivedCount().get() > deliveriesBefore, STALL_WINDOW_MILLIS),
            "control: with a multi-threaded executor the recovered NotificationMessages and the"
                + " one that revealed the gap must be delivered");
      }
    }
  }

  /**
   * There is one {@code PublishingManager} per client and one processing queue for all of its
   * Subscriptions, so a Subscription recovering a gap spends that queue on a synchronous network
   * round trip per missing NotificationMessage. Nothing else the client received is processed
   * meanwhile: an unrelated, healthy Subscription stops receiving its data because a different one
   * lost a message.
   *
   * <p>The Republish response is gated by the test rather than delayed by a sleep, so the stall is
   * a property of the code under test and not of the fixture's timing.
   */
  @Nested
  class RecoveryOnTheSharedProcessingQueue {

    @Test
    void publishResponseForAnotherSubscriptionIsProcessedWhileRecoveryIsInFlight()
        throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        Sub recovering = fixture.createSubscription(2);
        Sub healthy = fixture.createSubscription(3);

        fixture.gateRepublishResponses();
        fixture.revealGap(recovering);

        assertTrue(
            fixture.republishStarted.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
            "the gap was never detected: no Republish request reached the Server");

        int deliveriesBefore = healthy.dataReceivedCount().get();
        fixture.scriptable.enqueueDataChange(
            healthy.subscriptionId(), 2, dataNotifications(), uint(2));

        assertTrue(
            fixture.awaitTrue(
                () -> healthy.dataReceivedCount().get() > deliveriesBefore, STALL_WINDOW_MILLIS),
            "a PublishResponse for a Subscription with nothing missing was not processed while"
                + " another Subscription's Republish recovery was in flight: recovery is blocking"
                + " the processing queue that every Subscription on this client shares");
      }
    }

    /**
     * The control: the same two Subscriptions and the same gap, with the Republish answered
     * immediately. It isolates the blocking wait as the cause — the PublishResponse for the second
     * Subscription reaches the client identically in both cases.
     */
    @Test
    void publishResponseForAnotherSubscriptionIsProcessedWhenRecoveryDoesNotBlock()
        throws Exception {

      try (var fixture = new Fixture(cachedThreadPool())) {
        Sub recovering = fixture.createSubscription(2);
        Sub healthy = fixture.createSubscription(3);

        fixture.revealGap(recovering);

        assertTrue(
            fixture.awaitRepublishRequests(MISSING_COUNT, AWAIT_TIMEOUT_MILLIS),
            "control: the gap was never recovered");

        int deliveriesBefore = healthy.dataReceivedCount().get();
        fixture.scriptable.enqueueDataChange(
            healthy.subscriptionId(), 2, dataNotifications(), uint(2));

        assertTrue(
            fixture.awaitTrue(
                () -> healthy.dataReceivedCount().get() > deliveriesBefore, STALL_WINDOW_MILLIS),
            "control: a PublishResponse for a second Subscription must be processed once no"
                + " recovery is in flight");
      }
    }
  }

  /**
   * Part 4 §5.14.5.2: availableSequenceNumbers is "a list of sequence number ranges that identify
   * unacknowledged NotificationMessages that are available for retransmission from the
   * Subscription's retransmission queue". A sequence number missing from that list is one the
   * Server has just told the client it cannot retransmit — the retransmission queue overflowed and
   * dropped it, or it was acknowledged — so Republishing it can only be answered
   * Bad_MessageNotAvailable.
   *
   * <p>Recovery instead walks the numeric gap and asks for every sequence number in it. Each of
   * those requests is a blocking round trip on the shared processing queue (see the nested classes
   * above), spent on an answer the client was given before it asked.
   */
  @Nested
  class AvailableSequenceNumbers {

    @Test
    void republishIsNotRequestedForNotificationMessagesTheServerIsNotHolding() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        Sub sub = fixture.createSubscription(1);
        fixture.useRetransmissionQueueResponder(Set.of(3L, 4L, 5L));

        int publishRequestsBefore = fixture.scriptable.getPublishRequestCount();

        // The Server is holding 3, 4 and 5: NotificationMessage 2 is no longer in the
        // retransmission queue.
        fixture.scriptable.enqueueDataChange(
            sub.subscriptionId(), GAP_REVEALED_BY, dataNotifications(), uint(3), uint(4), uint(5));

        assertTrue(
            fixture.awaitResponsesProcessed(publishRequestsBefore, 1, AWAIT_TIMEOUT_MILLIS),
            "the PublishResponse that revealed the gap was never fully processed");

        assertEquals(
            List.of(3L, 4L),
            List.copyOf(fixture.republishRequests),
            "the Server advertised only 3, 4 and 5 as available for retransmission, so"
                + " NotificationMessage 2 is gone; Republishing it is a blocking round trip that"
                + " can only be answered Bad_MessageNotAvailable");
      }
    }

    /**
     * The control for the assertion above: when the Server is still holding every missing
     * NotificationMessage, every one of them is requested. Without it, restricting recovery to the
     * advertised set could pass by never Republishing anything at all.
     */
    @Test
    void republishIsRequestedForEveryNotificationMessageTheServerIsHolding() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        Sub sub = fixture.createSubscription(1);
        fixture.useRetransmissionQueueResponder(Set.of(2L, 3L, 4L, 5L));

        int publishRequestsBefore = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueDataChange(
            sub.subscriptionId(),
            GAP_REVEALED_BY,
            dataNotifications(),
            uint(2),
            uint(3),
            uint(4),
            uint(5));

        assertTrue(
            fixture.awaitResponsesProcessed(publishRequestsBefore, 1, AWAIT_TIMEOUT_MILLIS),
            "the PublishResponse that revealed the gap was never fully processed");

        assertEquals(
            List.of(2L, 3L, 4L),
            List.copyOf(fixture.republishRequests),
            "control: every missing NotificationMessage the Server is still holding must be"
                + " requested via Republish");
      }
    }
  }

  // region fixture

  /** A Subscription's Server-assigned id and the number of deliveries its listener has observed. */
  private record Sub(UInteger subscriptionId, AtomicInteger dataReceivedCount) {}

  /**
   * A running Server whose Publish and Republish responses are scripted, plus a client driven by a
   * caller-supplied {@link ExecutorService}.
   */
  private static final class Fixture implements AutoCloseable {

    /** Every {@code retransmitSequenceNumber} the client has asked the Server to Republish. */
    private final List<Long> republishRequests = Collections.synchronizedList(new ArrayList<>());

    /** Counted down when the Server receives a gated Republish request. */
    private final CountDownLatch republishStarted = new CountDownLatch(1);

    /** Releases a gated Republish response; always counted down by {@link #close()}. */
    private final CountDownLatch republishGate = new CountDownLatch(1);

    private final ExecutorService executor;
    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    Fixture(ExecutorService executor) throws Exception {
      this.executor = executor;

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
              transportConfig -> transportConfig.setExecutor(executor),
              cfg ->
                  cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are
                      // the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();

      scriptable.setRepublishResponder(recordingRepublishResponder());
    }

    /**
     * Create a Subscription, drive its {@code lastSequenceNumber} to 1, and wait for the client's
     * Publish pipeline to refill so that responses scripted afterwards land on requests already
     * parked at the Server.
     *
     * @param parkedRequests the number of parked Publish requests to wait for; the client keeps one
     *     more in flight than it has Subscriptions.
     */
    Sub createSubscription(int parkedRequests) throws Exception {
      var dataReceivedCount = new AtomicInteger();

      var subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription subscription,
                List<OpcUaMonitoredItem> items,
                List<DataValue> values) {

              dataReceivedCount.incrementAndGet();
            }
          });
      subscription.create();

      var sub = new Sub(subscription.getSubscriptionId().orElseThrow(), dataReceivedCount);

      scriptable.enqueueDataChange(sub.subscriptionId(), 1, dataNotifications(), uint(1));

      assertTrue(
          awaitTrue(() -> sub.dataReceivedCount().get() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the first NotificationMessage was never delivered");
      assertTrue(
          awaitTrue(
              () -> scriptable.getParkedRequestCount() >= parkedRequests, AWAIT_TIMEOUT_MILLIS),
          "the client did not refill its Publish pipeline");

      return sub;
    }

    /**
     * Send a NotificationMessage carrying sequence {@value #GAP_REVEALED_BY} to a Subscription
     * whose last accounted-for sequence number is 1, leaving {@value #MISSING_COUNT} missing. Every
     * missing message is advertised as available for retransmission, so the whole gap is
     * recoverable.
     */
    void revealGap(Sub sub) {
      scriptable.enqueueDataChange(
          sub.subscriptionId(),
          GAP_REVEALED_BY,
          dataNotifications(),
          uint(2),
          uint(3),
          uint(4),
          uint(5));
    }

    /** Records the requested sequence number and answers with a NotificationMessage. */
    private RepublishResponder recordingRepublishResponder() {
      return request -> {
        long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
        republishRequests.add(sequenceNumber);

        return scriptable.buildRepublishResponse(
            request, sequenceNumber, encodedDataNotifications());
      };
    }

    /**
     * Install a Republish responder that suspends the caller until {@link #close()} releases it.
     * The Server dispatches every service request on its own executor, so only the thread handling
     * this one request waits; the Server keeps answering everything else.
     */
    void gateRepublishResponses() {
      scriptable.setRepublishResponder(
          request -> {
            long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
            republishRequests.add(sequenceNumber);
            republishStarted.countDown();

            try {
              if (!republishGate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                throw new UaException(StatusCodes.Bad_Timeout);
              }
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              throw new UaException(StatusCodes.Bad_UnexpectedError, e);
            }

            return scriptable.buildRepublishResponse(
                request, sequenceNumber, encodedDataNotifications());
          });
    }

    /**
     * Install a Republish responder that behaves like a real retransmission queue: it answers for
     * the sequence numbers it is holding and reports Bad_MessageNotAvailable for anything else.
     *
     * @param held the sequence numbers still in the Server's retransmission queue, i.e. the ones
     *     its PublishResponse advertises in availableSequenceNumbers.
     */
    void useRetransmissionQueueResponder(Set<Long> held) {
      scriptable.setRepublishResponder(
          request -> {
            long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
            republishRequests.add(sequenceNumber);

            if (held.contains(sequenceNumber)) {
              return scriptable.buildRepublishResponse(
                  request, sequenceNumber, encodedDataNotifications());
            } else {
              throw new UaException(StatusCodes.Bad_MessageNotAvailable);
            }
          });
    }

    boolean awaitRepublishRequests(int count, long timeoutMillis) throws Exception {
      return awaitTrue(() -> republishRequests.size() >= count, timeoutMillis);
    }

    /**
     * Wait until the client has finished processing and delivering {@code count} more
     * PublishResponses.
     *
     * <p>The client replaces a Publish request only once the notifications it carried have been
     * delivered — that is the SDK's backpressure mechanism — so {@code count} further Publish
     * requests arriving at the Server is exactly that signal.
     */
    boolean awaitResponsesProcessed(int publishRequestsBefore, int count, long timeoutMillis)
        throws Exception {

      return awaitTrue(
          () -> scriptable.getPublishRequestCount() >= publishRequestsBefore + count,
          timeoutMillis);
    }

    private ExtensionObject[] encodedDataNotifications() {
      return new ExtensionObject[] {
        scriptable.encode(
            new DataChangeNotification(
                dataNotifications().toArray(MonitoredItemNotification[]::new), null))
      };
    }

    /** Polls {@code condition} until it holds or the timeout elapses. */
    boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis) throws Exception {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      while (System.nanoTime() < deadline) {
        if (condition.get()) {
          return true;
        }
        Thread.sleep(10);
      }

      return condition.get();
    }

    @Override
    public void close() throws Exception {
      republishGate.countDown();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // A client deadlocked inside a blocking Republish recovery cannot run the disconnect it is
        // asked for; shutting the Server and the executor down below is what releases it. Tolerated
        // here so teardown does not mask the assertion that detected the deadlock.
      } finally {
        try {
          server.shutdown().get(10, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
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

  /** The shape of {@code Stack.sharedExecutor()}: an unbounded cached pool. */
  private static ExecutorService cachedThreadPool() {
    return new ThreadPoolExecutor(
        0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
