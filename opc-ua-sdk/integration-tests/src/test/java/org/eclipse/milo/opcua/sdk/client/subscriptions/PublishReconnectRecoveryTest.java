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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What the client does with a Subscription the first time its Session becomes {@code Active} again
 * after a connection or Session fault.
 *
 * <p>Part 4 §6.7 "Re-establishing connections": "After re-establishing the connection the Client
 * shall call Republish in a loop, starting with the next expected sequence number and incrementing
 * the sequence number until the Server returns the status Bad_MessageNotAvailable." The loop comes
 * <i>before</i> normal Publish handling resumes, and the reason is mechanical: the Server's
 * retransmission queue is finite, and Part 4 §5.14.1.1 says that "in the case of a retransmission
 * queue overflow, the oldest sent NotificationMessage gets deleted". Every NotificationMessage the
 * Server sends in answer to a resumed Publish request is therefore capable of evicting one the
 * client has not collected yet.
 *
 * <p>Milo already performs the rest of §6.7 — it re-establishes the SecureChannel, re-activates or
 * re-creates the Session, and calls TransferSubscriptions with {@code sendInitialValues} — and it
 * repairs a sequence gap <i>reactively</i>, once a PublishResponse has revealed one. What it does
 * not do is drain the retransmission queue before letting that first PublishResponse arrive: {@code
 * PublishingManager}'s {@code SessionActivityListener} goes straight to {@code
 * maybeSendPublishRequests()}. The tests below drive both reconnect paths the Session FSM has and
 * assert on the order in which requests reach the Server, and on what the application ends up
 * seeing when the retransmission queue overflows in the window the missing order opens.
 *
 * <p>They also cover the second half of the same clause set. A successful TransferSubscriptions
 * returns, per Part 4 §5.14.7.1, the sequence numbers of the NotificationMessages the Server is
 * still holding for the transferred Subscription — "The Client should acknowledge all Messages in
 * this list for which it will not request retransmission" — which is exactly the input a Republish
 * drain needs. {@code SessionFsmFactory} reads only the TransferResult's StatusCode.
 */
public class PublishReconnectRecoveryTest {

  /** Log entry written when a PublishRequest reaches the Server. */
  private static final String PUBLISH = "Publish";

  /** Prefix of the log entry written when a RepublishRequest reaches the Server. */
  private static final String REPUBLISH = "Republish:";

  /**
   * The sequence number of the last NotificationMessage the client accounts for before the Session
   * fault, and therefore the sequence number §6.7's Republish loop has to start from.
   */
  private static final long LAST_SEQUENCE_NUMBER_BEFORE_FAULT = 2;

  private static final long NEXT_EXPECTED_SEQUENCE_NUMBER = LAST_SEQUENCE_NUMBER_BEFORE_FAULT + 1;

  /**
   * The two NotificationMessages the Server generates while the client is not connected, i.e. the
   * ones §6.7's Republish loop exists to collect.
   */
  private static final long[] GENERATED_WHILE_DISCONNECTED = {3, 4};

  /** The sequence number of the first NotificationMessage the Server sends after the reconnect. */
  private static final long FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT = 5;

  /**
   * A retransmission queue with room for exactly the two NotificationMessages generated while the
   * client was disconnected. Sending one more overflows it and deletes the oldest, which is the
   * loss §6.7's ordering requirement is there to prevent. Real queues are larger — Part 4 §5.14.5.1
   * makes the Server hold "at least two times the number of Publish requests per Session" — but
   * every queue has a size, and the number here only decides how many new NotificationMessages it
   * takes to lose an old one.
   */
  private static final int OVERFLOWING_RETRANSMISSION_QUEUE = 2;

  /** A retransmission queue with room to spare, so nothing is evicted during the test. */
  private static final int ROOMY_RETRANSMISSION_QUEUE = 4;

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for a reconnect. The Session FSM waits one second in {@code ReactivatingWait}
   * before its first re-activation attempt and another second in {@code CreatingWait} before
   * creating a replacement Session, and doubles each wait on every failure.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request, no Republish, and no
   * Session keep-alive. Every stall asserted against below is therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /**
   * The same-session reconnect path: {@code Active -> ReactivatingWait -> Reactivating ->
   * Initializing -> Active}. No TransferSubscriptions happens on it, so the only thing that can
   * tell the client what the Server still holds is the Republish loop itself.
   */
  @Nested
  class ReactivatedSession {

    /**
     * Part 4 §6.7: "After re-establishing the connection the Client shall call Republish in a loop,
     * starting with the next expected sequence number and incrementing the sequence number until
     * the Server returns the status Bad_MessageNotAvailable."
     *
     * <p>The very first request the client sends for a Subscription once its Session is {@code
     * Active} again must therefore be a Republish for the next expected sequence number, not a
     * Publish.
     */
    @Test
    void firstRequestAfterReactivationIsRepublishFromTheNextExpectedSequenceNumber()
        throws Exception {

      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitRequestLogEntries(1),
            "no request at all reached the Server after the Session was re-activated");

        assertEquals(
            REPUBLISH + NEXT_EXPECTED_SEQUENCE_NUMBER,
            fixture.requestLog().get(0),
            "the first request sent after re-activation must be the Republish that §6.7 requires;"
                + " resuming Publish first lets the Server answer with a NotificationMessage that"
                + " overwrites one still sitting in its retransmission queue");
      }
    }

    /**
     * The control that keeps the assertion above from passing vacuously: in this fixture the
     * re-activated Session really does resume Publish traffic and deliver notifications, so a
     * failure above is about <i>what</i> the client sends first, not about the client sending
     * nothing at all.
     */
    @Test
    void publishResumesAfterReactivation() throws Exception {
      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(NEXT_EXPECTED_SEQUENCE_NUMBER);
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitDeliveredValues(List.of(1, 2, (int) NEXT_EXPECTED_SEQUENCE_NUMBER)),
            "control: the NotificationMessage scripted for the first Publish after re-activation"
                + " was never delivered, actual deliveries: "
                + fixture.deliveredValues());
      }
    }
  }

  /**
   * What the ordering requirement is worth, measured at the application. The Server generates two
   * NotificationMessages while the client is away and holds them in a retransmission queue that has
   * no room for a third. Whether the client collects them depends entirely on whether it asks
   * before or after it lets the Server publish something new.
   */
  @Nested
  class NotificationMessagesGeneratedWhileTheSessionWasDown {

    /**
     * The two NotificationMessages the Server generated while the client was away are in its
     * retransmission queue when the Session becomes {@code Active} again, so §6.7's Republish loop
     * would collect both. Resuming Publish first spends the queue's last slot on a new
     * NotificationMessage instead, and the oldest of the two is deleted before the client ever asks
     * for it — reported to the application only as {@code onNotificationDataLost}.
     */
    @Test
    void areRecoveredEvenWhenTheNextPublishResponseOverflowsTheRetransmissionQueue()
        throws Exception {

      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        fixture.awaitDeliveredValues(List.of(1, 2, 3, 4, 5));

        assertAll(
            () ->
                assertEquals(
                    List.of(1, 2, 3, 4, 5),
                    fixture.deliveredValues(),
                    "every NotificationMessage the Server generated while the client was away was"
                        + " still in its retransmission queue when the Session became Active"
                        + " again, so all of them must reach the application exactly once and in"
                        + " sequence order"),
            () ->
                assertEquals(
                    0,
                    fixture.notificationDataLostCount(),
                    "no NotificationMessage was unrecoverable at the moment the Session became"
                        + " Active again; reporting data lost means the client let the Server"
                        + " overwrite one before asking for it"));
      }
    }

    /**
     * The control that isolates the retransmission queue overflow as the cause: the identical
     * script, against a Server whose retransmission queue has room for the new NotificationMessage
     * as well, loses nothing today. The reactive gap repair recovers both missing messages because
     * the Server still happens to hold them when the first PublishResponse reveals the gap — which
     * is luck, not a guarantee, and is exactly what §6.7's ordering removes the need for.
     */
    @Test
    void areRecoveredWhenTheRetransmissionQueueDoesNotOverflow() throws Exception {
      try (var fixture = new Fixture(ROOMY_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        fixture.awaitDeliveredValues(List.of(1, 2, 3, 4, 5));

        assertAll(
            () ->
                assertEquals(
                    List.of(1, 2, 3, 4, 5),
                    fixture.deliveredValues(),
                    "control: with a retransmission queue that does not overflow, every"
                        + " NotificationMessage must reach the application"),
            () ->
                assertEquals(
                    0,
                    fixture.notificationDataLostCount(),
                    "control: nothing was ever evicted, so nothing can be reported lost"));
      }
    }
  }

  /**
   * The new-session reconnect path: re-activation is refused, so the client creates a replacement
   * Session and runs {@code Active -> ... -> Transferring -> Initializing -> Active}. Here the
   * Server does tell the client what it is holding — Part 4 §5.14.7.1 gives each successful
   * TransferResult the "sequence numbers of the NotificationMessages available for retransmission"
   * — and the client throws that list away.
   */
  @Nested
  class TransferredSubscription {

    /**
     * §6.7's Republish loop applies to this path too, and the TransferResult has just named the
     * sequence numbers it should ask for. Every one of them must be requested before the client
     * lets the Server answer a Publish request, because answering one is what overflows the
     * retransmission queue holding them.
     */
    @Test
    void everySequenceNumberTheTransferAdvertisedIsRepublishedBeforePublishResumes()
        throws Exception {

      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);
        fixture.advertiseOnTransfer(GENERATED_WHILE_DISCONNECTED);
        fixture.refuseNextReactivation();

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitRequestLogContains(PUBLISH),
            "Publish never resumed after the Subscription was transferred to the new Session");
        assertTrue(
            fixture.transferCount() >= 1,
            "precondition: the reconnect did not go through TransferSubscriptions, so this test"
                + " is not exercising the transfer path at all");

        List<String> log = fixture.requestLog();
        int firstPublish = log.indexOf(PUBLISH);

        assertAll(
            () ->
                assertEquals(
                    REPUBLISH + GENERATED_WHILE_DISCONNECTED[0],
                    log.get(0),
                    "the first request sent after the transfer must be the Republish §6.7"
                        + " requires, for the oldest NotificationMessage the TransferResult said"
                        + " the Server still holds; request log: "
                        + log),
            () ->
                assertTrue(
                    isBefore(log, REPUBLISH + GENERATED_WHILE_DISCONNECTED[1], firstPublish),
                    "the TransferResult advertised sequence number "
                        + GENERATED_WHILE_DISCONNECTED[1]
                        + " as available for retransmission, so it must be requested before the"
                        + " first Publish; request log: "
                        + log));
      }
    }

    /**
     * The control for the test above: it proves the fixture really does drive the client onto the
     * new-session path, that TransferSubscriptions is reached, and that Publish traffic resumes
     * afterwards — so a failure above is about ordering rather than about a reconnect that never
     * happened.
     */
    @Test
    void isTransferredToAReplacementSessionAndPublishResumes() throws Exception {
      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);
        fixture.advertiseOnTransfer(GENERATED_WHILE_DISCONNECTED);
        fixture.refuseNextReactivation();

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitTrue(
                () ->
                    fixture.deliveredValues().contains((int) FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT),
                AWAIT_TIMEOUT_MILLIS),
            "control: the NotificationMessage scripted for the first Publish after the transfer"
                + " was never delivered, actual deliveries: "
                + fixture.deliveredValues());

        assertTrue(
            fixture.transferCount() >= 1,
            "control: the reconnect did not go through TransferSubscriptions");
      }
    }
  }

  /**
   * Gating Publish on a recovery step trades a loss window for a permanent stall if any branch of
   * that step can fail without releasing the gate. These are the guards for that: whatever the
   * Republish loop runs into, Publish traffic has to resume.
   */
  @Nested
  class RecoveryFailureMustNotStallPublish {

    /** A Republish that fails with a service error, rather than Bad_MessageNotAvailable. */
    @Test
    void publishResumesWhenEveryRepublishFailsWithAServiceError() throws Exception {
      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);
        fixture.failEveryRepublishWith(StatusCodes.Bad_UnexpectedError);

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitTrue(
                () ->
                    fixture.deliveredValues().contains((int) FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT),
                AWAIT_TIMEOUT_MILLIS),
            "Publish did not resume after every Republish failed: recovery that cannot complete"
                + " must still hand the pipeline back, actual deliveries: "
                + fixture.deliveredValues());
      }
    }

    /**
     * The Subscription no longer exists on the Server. Part 4 §6.7: "If the Republish returns
     * Bad_SubscriptionIdInvalid, then the Client needs to create a new Subscription" — which is a
     * statement about the Subscription, not a reason for the client's Publish pipeline to stop.
     */
    @Test
    void publishResumesWhenRepublishReportsTheSubscriptionIsGone() throws Exception {
      try (var fixture = new Fixture(OVERFLOWING_RETRANSMISSION_QUEUE)) {
        fixture.deliverInitialNotifications();
        fixture.generateWhileDisconnected(GENERATED_WHILE_DISCONNECTED);
        fixture.failEveryRepublishWith(StatusCodes.Bad_SubscriptionIdInvalid);

        fixture.armRequestLog();
        fixture.faultSession();
        fixture.enqueueDataChange(FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT);
        fixture.awaitReactivation();

        assertTrue(
            fixture.awaitTrue(
                () ->
                    fixture.deliveredValues().contains((int) FIRST_SEQUENCE_NUMBER_AFTER_RECONNECT),
                AWAIT_TIMEOUT_MILLIS),
            "Publish did not resume after Republish reported Bad_SubscriptionIdInvalid, actual"
                + " deliveries: "
                + fixture.deliveredValues());
      }
    }
  }

  /**
   * @return {@code true} if {@code entry} appears in {@code log} before index {@code limit}.
   */
  private static boolean isBefore(List<String> log, String entry, int limit) {
    int index = log.indexOf(entry);

    return index >= 0 && index < limit;
  }

  // region fixture

  /**
   * The order in which Publish and Republish requests reach the Server.
   *
   * <p>Recording starts only when the log is {@linkplain #arm() armed}, which a test does once its
   * Publish pipeline is quiescent — one request parked at the Server and no responder scripted for
   * it. From that moment nothing can reach the Server until the scripted Session fault has been
   * answered and the Session has become {@code Active} again, so the first entry recorded is
   * exactly the first request the client sends after the reconnect.
   */
  private static final class RequestLog {

    private final List<String> entries = Collections.synchronizedList(new ArrayList<>());

    private volatile boolean armed = false;

    void arm() {
      entries.clear();
      armed = true;
    }

    void record(String entry) {
      if (armed) {
        entries.add(entry);
      }
    }

    List<String> entries() {
      return List.copyOf(entries);
    }
  }

  /**
   * A Server-side retransmission queue of bounded size.
   *
   * <p>Part 4 §5.14.1.1: "In the case of a retransmission queue overflow, the oldest sent
   * NotificationMessage gets deleted." A NotificationMessage is available for Republish while it is
   * in the queue, and the queue's contents are what a PublishResponse advertises in
   * availableSequenceNumbers.
   */
  private static final class RetransmissionQueue {

    private final Deque<Long> queue = new ArrayDeque<>();

    private final int capacity;

    RetransmissionQueue(int capacity) {
      this.capacity = capacity;
    }

    /** Record that the Server has sent the NotificationMessage with this sequence number. */
    synchronized void send(long sequenceNumber) {
      queue.addLast(sequenceNumber);

      while (queue.size() > capacity) {
        queue.removeFirst();
      }
    }

    synchronized boolean holds(long sequenceNumber) {
      return queue.contains(sequenceNumber);
    }

    synchronized UInteger[] available() {
      return queue.stream().map(sequenceNumber -> uint(sequenceNumber)).toArray(UInteger[]::new);
    }
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that records the arrival of every PublishRequest and
   * can answer TransferSubscriptions with a scripted availableSequenceNumbers list.
   */
  private static final class LoggingSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger transferCount = new AtomicInteger(0);

    /**
     * {@code null} until a test scripts the transfer, at which point the Server delegate is no
     * longer consulted.
     */
    private volatile UInteger[] transferAvailableSequenceNumbers = null;

    private final RequestLog requestLog;

    LoggingSubscriptionServiceSet(OpcUaServer server, RequestLog requestLog) {
      super(server);

      this.requestLog = requestLog;
    }

    @Override
    public CompletableFuture<PublishResponse> onPublish(
        ServiceRequestContext context, PublishRequest request) {

      requestLog.record(PUBLISH);

      return super.onPublish(context, request);
    }

    @Override
    public TransferSubscriptionsResponse onTransferSubscriptions(
        ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {

      UInteger[] available = transferAvailableSequenceNumbers;

      if (available == null) {
        return super.onTransferSubscriptions(context, request);
      }

      transferCount.incrementAndGet();

      UInteger[] subscriptionIds = request.getSubscriptionIds();
      int count = subscriptionIds != null ? subscriptionIds.length : 0;

      var results = new TransferResult[count];
      for (int i = 0; i < count; i++) {
        results[i] = new TransferResult(StatusCode.GOOD, available);
      }

      var responseHeader =
          new ResponseHeader(
              DateTime.now(),
              request.getRequestHeader().getRequestHandle(),
              StatusCode.GOOD,
              null,
              null,
              null);

      return new TransferSubscriptionsResponse(responseHeader, results, null);
    }
  }

  /**
   * A {@link DelegatingSessionServiceSet} that can refuse a single ActivateSession with a
   * ServiceFault, which is what drives the Session FSM off the re-activation path and onto the
   * create-a-new-Session-and-transfer path.
   */
  private static final class RefusingSessionServiceSet extends DelegatingSessionServiceSet {

    private final AtomicBoolean refuseNext = new AtomicBoolean(false);

    RefusingSessionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public ActivateSessionResponse onActivateSession(
        ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

      if (refuseNext.compareAndSet(true, false)) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      }

      return super.onActivateSession(context, request);
    }
  }

  /**
   * A running Server whose Publish, Republish and TransferSubscriptions responses are scripted, and
   * a connected client with one Subscription carrying one MonitoredItem.
   *
   * <p>The client is configured for a single pending PublishRequest. That makes the pipeline state
   * unambiguous at the moment the Session fault is scripted: exactly one request is parked at the
   * Server, the fault answers it, and nothing is left outstanding to muddle what the client sends
   * once the Session is {@code Active} again.
   */
  private static final class Fixture implements AutoCloseable {

    private final RequestLog requestLog = new RequestLog();

    private final RetransmissionQueue retransmissionQueue;

    private final List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger notificationDataLostCount = new AtomicInteger(0);

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    /** Non-zero while every Republish is scripted to fail with that StatusCode. */
    private volatile long republishFailure = 0L;

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final LoggingSubscriptionServiceSet scriptable;
    private final RefusingSessionServiceSet sessionServiceSet;

    private final OpcUaSubscription subscription;
    private final UInteger subscriptionId;
    private final UInteger clientHandle;

    Fixture(int retransmissionQueueCapacity) throws Exception {
      retransmissionQueue = new RetransmissionQueue(retransmissionQueueCapacity);

      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new LoggingSubscriptionServiceSet(server, requestLog);
      sessionServiceSet = new RefusingSessionServiceSet(server);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
        server.addServiceSet(endpoint.getPath(), sessionServiceSet);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      .setMaxPendingPublishRequests(uint(1)));
      client.connect();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionInactive(UaSession session) {
              sessionInactive.countDown();
            }

            @Override
            public void onSessionActive(UaSession session) {
              if (sessionInactive.getCount() == 0) {
                sessionReactivated.countDown();
              }
            }
          });

      scriptable.setRepublishResponder(this::respondToRepublish);

      subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {

              for (DataValue value : values) {
                deliveredValues.add((Integer) value.getValue().getValue());
              }
            }

            @Override
            public void onNotificationDataLost(OpcUaSubscription s) {
              notificationDataLostCount.incrementAndGet();
            }
          });
      subscription.create();

      subscriptionId = subscription.getSubscriptionId().orElseThrow();

      // The MonitoredItem only has to exist on the client: addMonitoredItem assigns the
      // ClientHandle the notification fan-out looks scripted notifications up by, and no
      // Server-side item participates in delivering one.
      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      clientHandle = item.getClientHandle().orElseThrow();
    }

    /**
     * Deliver NotificationMessages 1 and 2, leaving the client's last accounted-for sequence number
     * at {@value #LAST_SEQUENCE_NUMBER_BEFORE_FAULT} and its Publish pipeline quiescent: one
     * request parked at the Server with no responder scripted for it.
     */
    void deliverInitialNotifications() throws Exception {
      enqueueDataChange(1);
      assertTrue(
          awaitTrue(() -> deliveredValues().size() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the first NotificationMessage was never delivered");

      enqueueDataChange(LAST_SEQUENCE_NUMBER_BEFORE_FAULT);
      assertTrue(
          awaitTrue(() -> deliveredValues().size() >= 2, AWAIT_TIMEOUT_MILLIS),
          "the second NotificationMessage was never delivered");

      assertTrue(
          awaitTrue(() -> scriptable.getParkedRequestCount() == 1, AWAIT_TIMEOUT_MILLIS),
          "the client did not refill its Publish pipeline");

      assertEquals(
          List.of(1, (int) LAST_SEQUENCE_NUMBER_BEFORE_FAULT),
          deliveredValues(),
          "precondition: the client must have accounted for NotificationMessages 1 and 2 before"
              + " the Session fault");
      assertEquals(
          0,
          notificationDataLostCount(),
          "precondition: nothing may be lost before the Session fault");
    }

    /**
     * Record that the Server generated these NotificationMessages, and is therefore holding them in
     * its retransmission queue, while the client was not connected.
     */
    void generateWhileDisconnected(long... sequenceNumbers) {
      for (long sequenceNumber : sequenceNumbers) {
        retransmissionQueue.send(sequenceNumber);
      }
    }

    /** Answer TransferSubscriptions with these availableSequenceNumbers (Part 4 §5.14.7.1). */
    void advertiseOnTransfer(long... sequenceNumbers) {
      var available = new UInteger[sequenceNumbers.length];
      for (int i = 0; i < sequenceNumbers.length; i++) {
        available[i] = uint(sequenceNumbers[i]);
      }

      scriptable.transferAvailableSequenceNumbers = available;
    }

    /**
     * Refuse the next ActivateSession with a ServiceFault, which sends the Session FSM to {@code
     * CreatingWait} and from there onto the create-a-new-Session-and-transfer path.
     */
    void refuseNextReactivation() {
      sessionServiceSet.refuseNext.set(true);
    }

    /** Script every Republish to fail with {@code statusCode} rather than model the queue. */
    void failEveryRepublishWith(long statusCode) {
      republishFailure = statusCode;
    }

    void armRequestLog() {
      requestLog.arm();
    }

    /**
     * Answer the one parked PublishRequest with a Bad_SessionIdInvalid ServiceFault, which {@code
     * SessionFsmFactory}'s SessionFaultListener classifies as a Session error and turns into a
     * reconnect. The Server-side Session is untouched, so re-activation succeeds unless {@link
     * #refuseNextReactivation()} was called.
     */
    void faultSession() {
      scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
    }

    /**
     * Script the next PublishResponse as a data message carrying {@code sequenceNumber}, whose
     * value identifies the NotificationMessage it came from.
     *
     * <p>Sending it puts the sequence number in the Server's retransmission queue, evicting the
     * oldest entry if the queue is full, and the queue's contents become the response's
     * availableSequenceNumbers. That is the causal link the ordering requirement is about: the
     * eviction happens when the Server answers a Publish request, so whether a Republish finds a
     * NotificationMessage depends on whether it was sent before or after Publish resumed.
     *
     * <p>Called immediately after {@link #faultSession()}, when the Server has no parked
     * PublishRequest left, so this responder waits in the script rather than being applied at once.
     * The Session FSM waits a whole second in {@code ReactivatingWait} before it even attempts to
     * reconnect, so the client cannot have sent its next PublishRequest by then.
     */
    void enqueueDataChange(long sequenceNumber) {
      scriptable.enqueue(
          request -> {
            retransmissionQueue.send(sequenceNumber);

            return CompletableFuture.completedFuture(
                scriptable.buildPublishResponse(
                    request,
                    subscriptionId,
                    sequenceNumber,
                    notificationData(sequenceNumber),
                    retransmissionQueue.available(),
                    false));
          });
    }

    /**
     * Answer a Republish the way a Server holding a bounded retransmission queue would: with the
     * NotificationMessage if it is still in the queue, and Bad_MessageNotAvailable if it is not —
     * which is also what terminates the Republish loop Part 4 §6.7 describes.
     */
    private RepublishResponse respondToRepublish(RepublishRequest request) throws UaException {
      long sequenceNumber = request.getRetransmitSequenceNumber().longValue();

      requestLog.record(REPUBLISH + sequenceNumber);

      long failure = republishFailure;
      if (failure != 0L) {
        throw new UaException(failure);
      }

      if (!retransmissionQueue.holds(sequenceNumber)) {
        throw new UaException(StatusCodes.Bad_MessageNotAvailable);
      }

      return scriptable.buildRepublishResponse(
          request, sequenceNumber, notificationData(sequenceNumber));
    }

    private ExtensionObject[] notificationData(long sequenceNumber) {
      var notification =
          new MonitoredItemNotification(
              clientHandle, new DataValue(Variant.ofInt32((int) sequenceNumber)));

      return new ExtensionObject[] {
        scriptable.encode(
            new DataChangeNotification(new MonitoredItemNotification[] {notification}, null))
      };
    }

    /** Wait for the Session to leave {@code Active} and come back to it. */
    void awaitReactivation() throws Exception {
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
      assertTrue(
          sessionReactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session never became Active again");
    }

    List<String> requestLog() {
      return requestLog.entries();
    }

    boolean awaitRequestLogEntries(int count) throws Exception {
      return awaitTrue(() -> requestLog.entries().size() >= count, AWAIT_TIMEOUT_MILLIS);
    }

    boolean awaitRequestLogContains(String entry) throws Exception {
      return awaitTrue(() -> requestLog.entries().contains(entry), AWAIT_TIMEOUT_MILLIS);
    }

    List<Integer> deliveredValues() {
      return List.copyOf(deliveredValues);
    }

    boolean awaitDeliveredValues(List<Integer> expected) throws Exception {
      return awaitTrue(() -> deliveredValues().equals(expected), AWAIT_TIMEOUT_MILLIS);
    }

    int notificationDataLostCount() {
      return notificationDataLostCount.get();
    }

    int transferCount() {
      return scriptable.transferCount.get();
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
