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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
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
import org.junit.jupiter.api.Test;

/**
 * The window between the Session future completing and the {@code onSessionActive} callbacks
 * running, measured against the Part 4 §6.7 ordering the Publish suspension gate exists to impose.
 *
 * <p>Part 4 §6.7: "After re-establishing the connection the Client shall call Republish in a
 * loop... After the Republish returns Bad_MessageNotAvailable the Client shall start sending
 * Publish requests with the normal Publish handling." {@link PublishReconnectRecoveryTest} asserts
 * that order for the ordinary caller — one that asks for a Session after the reconnect is over.
 * This class covers the caller that was already there.
 *
 * <p>{@code SessionFsmFactory} completes the Session future in one executor task and fans the
 * {@code onSessionActive} callbacks out in another, submitted after it. Every continuation parked
 * on {@code getSessionAsync()} therefore runs <i>before</i> the {@code PublishingManager} has been
 * told the Session is Active. A suspension gate that asks only "has every Session activation
 * counted so far had its recovery run?" is open in that window, because the new activation has not
 * been counted yet and the answer still describes the previous one — and a PublishRequest goes out
 * on the new Session ahead of the Republish loop, which is precisely what the ordering forbids.
 * Counting the activation earlier does not close it either: a re-activation hands back the same
 * {@link UaSession} object, so the gate has to know both which activation was recovered and which
 * Session that recovery ran on, and it has to stop trusting the latter the moment that Session goes
 * away.
 *
 * <p>The interleaving is forced rather than raced. The client's transport executor has a single
 * thread, so the task that completes the Session future runs to completion — the continuations
 * parked on it included — before the task that delivers the activation callbacks starts. The parked
 * caller is a real one: a PublishRequest returned with a failure while the Session is down makes
 * {@code PublishingManager} try to replace it, find no Session, and wait for the one being
 * established.
 */
public class PublishSuspensionGateRaceTest {

  /** Log entry written when a PublishRequest reaches the Server. */
  private static final String PUBLISH = "Publish";

  /** Prefix of the log entry written when a RepublishRequest reaches the Server. */
  private static final String REPUBLISH = "Republish:";

  /** Recorded when the Session future completes, by a caller parked on it. */
  private static final String SESSION_FUTURE = "SessionFuture";

  /** Recorded when the {@code onSessionActive} callbacks run. */
  private static final String SESSION_ACTIVE = "SessionActive";

  /**
   * The sequence number of the last NotificationMessage the client accounts for before the Session
   * fault.
   */
  private static final long LAST_SEQUENCE_NUMBER_BEFORE_FAULT = 2;

  /** The sequence number the Republish loop of Part 4 §6.7 has to start from. */
  private static final long NEXT_EXPECTED_SEQUENCE_NUMBER = LAST_SEQUENCE_NUMBER_BEFORE_FAULT + 1;

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for a reconnect. The Session FSM waits one second in {@code ReactivatingWait}
   * before its first re-activation attempt, another second in {@code CreatingWait} before creating
   * a replacement Session, and doubles each wait on every failure.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request, no Republish, and no
   * Session keep-alive. Every ordering asserted against below is therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /** Upper bound on how long a held ActivateSession is held, so nothing hangs indefinitely. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /**
   * The precondition the two tests below rest on, asserted on its own so that a failure there is
   * unambiguous: a caller that parks on {@code getSessionAsync()} while the Session is down is
   * released before any {@code onSessionActive} callback runs, and therefore before {@code
   * PublishingManager} has counted the activation or started its Republish loop.
   */
  @Test
  void aCallerParkedOnTheSessionFutureRunsBeforeTheActivationCallbacks() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.deliverInitialNotifications();

      fixture.faultSession();
      fixture.awaitSessionInactive();

      fixture.parkAnObserverOnTheSessionFuture();

      fixture.awaitReactivation();

      assertEquals(
          List.of(SESSION_FUTURE, SESSION_ACTIVE),
          fixture.activationOrder(),
          "the Session future must complete before the activation callbacks run; if it does not,"
              + " there is no window for a parked caller to send a PublishRequest in and the"
              + " ordering assertions in this class prove nothing");
    }
  }

  /**
   * The same-Session reconnect path: {@code Active -> ReactivatingWait -> Reactivating ->
   * Initializing -> Active}. The FSM hands back the very same {@link UaSession} object, so a gate
   * that remembers which Session the last finished recovery ran on still matches it — unless that
   * record is revoked when the Session becomes inactive.
   *
   * <p>Whatever the gate is keyed on, the first request the client sends for this Subscription once
   * the Session is Active again must be the Republish that Part 4 §6.7 requires, even though a
   * caller was already parked on the Session future when it completed.
   */
  @Test
  void noPublishPrecedesTheRepublishDrainWhenACallerIsParkedOnAReactivatedSession()
      throws Exception {

    try (var fixture = new Fixture()) {
      fixture.deliverInitialNotifications();

      fixture.armRequestLog();
      fixture.faultSession();
      fixture.awaitSessionInactive();
      fixture.parkAPublishRefillOnTheSessionFuture();
      fixture.awaitReactivation();

      assertTrue(
          fixture.awaitRequestLogEntries(1),
          "no request at all reached the Server after the Session was re-activated");

      assertEquals(
          REPUBLISH + NEXT_EXPECTED_SEQUENCE_NUMBER,
          fixture.requestLog().get(0),
          "the first request sent after re-activation must be the Republish Part 4 §6.7 requires,"
              + " but a PublishRequest overtook it: the caller parked on the Session future was"
              + " released before the activation was counted, and the suspension gate answered it"
              + " with the previous activation's recovery; request log: "
              + fixture.requestLog());
    }
  }

  /**
   * The replacement-Session reconnect path: re-activation is refused, so the client creates a new
   * Session and transfers the Subscription to it. Here the {@link UaSession} handed to the parked
   * caller is a different object from the one the last finished recovery ran on, so this is the
   * half of the gate that the Session identity answers rather than the revocation.
   *
   * <p>The caller is parked while the replacement Session's ActivateSession is held at the Server,
   * because the Session future a caller parks on during {@code ReactivatingWait} is discarded —
   * never completed — when re-activation is refused, and a caller parked on that one is never
   * released at all.
   */
  @Test
  void noPublishPrecedesTheRepublishDrainWhenACallerIsParkedOnAReplacementSession()
      throws Exception {

    try (var fixture = new Fixture()) {
      fixture.deliverInitialNotifications();
      fixture.advertiseNothingOnTransfer();
      fixture.refuseNextReactivationAndHoldTheReplacement();

      fixture.armRequestLog();
      fixture.faultSession();
      fixture.awaitReplacementActivateSessionHeld();
      fixture.parkAPublishRefillOnTheSessionFuture();
      fixture.releaseReplacementActivateSession();
      fixture.awaitReactivation();

      assertTrue(
          fixture.awaitRequestLogEntries(1),
          "no request at all reached the Server after the Subscription was transferred");
      assertTrue(
          fixture.transferCount() >= 1,
          "precondition: the reconnect did not go through TransferSubscriptions, so this test is"
              + " not exercising the replacement-Session path at all");

      assertEquals(
          REPUBLISH + NEXT_EXPECTED_SEQUENCE_NUMBER,
          fixture.requestLog().get(0),
          "the first request sent after the transfer must be the Republish Part 4 §6.7 requires,"
              + " but a PublishRequest overtook it: the caller parked on the Session future was"
              + " released before the activation was counted, and the suspension gate answered it"
              + " with the previous Session's recovery; request log: "
              + fixture.requestLog());
    }
  }

  // region fixture

  /**
   * The order in which Publish and Republish requests reach the Server.
   *
   * <p>Recording starts only when the log is {@linkplain #arm() armed}, which a test does once its
   * Publish pipeline is quiescent — every request parked at the Server and no responder scripted
   * for any of them. From that moment the only requests that can reach the Server are the ones the
   * client sends after the reconnect.
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
   * A {@link ScriptableSubscriptionServiceSet} that records the arrival of every PublishRequest and
   * can answer TransferSubscriptions with a scripted availableSequenceNumbers list.
   */
  private static final class LoggingSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    private final AtomicBoolean transferScripted = new AtomicBoolean(false);
    private volatile int transferCount = 0;

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

      if (!transferScripted.get()) {
        return super.onTransferSubscriptions(context, request);
      }

      transferCount++;

      UInteger[] subscriptionIds = request.getSubscriptionIds();
      int count = subscriptionIds != null ? subscriptionIds.length : 0;

      var results = new TransferResult[count];
      for (int i = 0; i < count; i++) {
        // Part 4 §5.14.7.1: the Server is holding nothing for retransmission, so the Republish loop
        // of §6.7 is the increment-until-Bad_MessageNotAvailable form.
        results[i] = new TransferResult(StatusCode.GOOD, new UInteger[0]);
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
   * ServiceFault — which is what drives the Session FSM off the re-activation path and onto the
   * create-a-new-Session-and-transfer path — and then hold the next one until the test releases it.
   */
  private static final class ScriptedSessionServiceSet extends DelegatingSessionServiceSet {

    private final AtomicBoolean refuseNext = new AtomicBoolean(false);
    private final AtomicBoolean holdNext = new AtomicBoolean(false);

    private final CountDownLatch heldActivateSession = new CountDownLatch(1);
    private final CountDownLatch activateSessionGate = new CountDownLatch(1);

    ScriptedSessionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public ActivateSessionResponse onActivateSession(
        ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

      if (refuseNext.compareAndSet(true, false)) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      }

      if (holdNext.compareAndSet(true, false)) {
        heldActivateSession.countDown();

        try {
          if (!activateSessionGate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            throw new UaException(StatusCodes.Bad_Timeout);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        }
      }

      return super.onActivateSession(context, request);
    }
  }

  /**
   * A running Server whose Publish, Republish and TransferSubscriptions responses are scripted, and
   * a connected client with one Subscription carrying one MonitoredItem.
   *
   * <p>The client's transport executor has a single thread. That is what makes the window this
   * class is about deterministic: the Session FSM submits the task that completes the Session
   * future before the task that fans the activation callbacks out, so on one thread the first runs
   * to completion — parked continuations included — before the second begins.
   *
   * <p>The client is configured for two pending PublishRequests, which is one per role: one to be
   * answered with the Session fault, and one to be returned, once the Session is down, with a
   * failure the client answers by trying to send a replacement.
   */
  private static final class Fixture implements AutoCloseable {

    private static final long MAX_PENDING_PUBLISH_REQUESTS = 2;

    private final RequestLog requestLog = new RequestLog();

    private final List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

    /** The order in which the Session future completed and the activation callbacks ran. */
    private final List<String> activationOrder = Collections.synchronizedList(new ArrayList<>());

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    /** Counted down when the client observes the ServiceFault that provokes the parked refill. */
    private final CountDownLatch refillFaultObserved = new CountDownLatch(1);

    private final ExecutorService executor =
        Executors.newSingleThreadExecutor(daemonThreadFactory("publish-suspension-gate-race"));

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final LoggingSubscriptionServiceSet scriptable;
    private final ScriptedSessionServiceSet sessionServiceSet;

    private final UInteger subscriptionId;
    private final UInteger clientHandle;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new LoggingSubscriptionServiceSet(server, requestLog);
      sessionServiceSet = new ScriptedSessionServiceSet(server);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
        server.addServiceSet(endpoint.getPath(), sessionServiceSet);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              transportConfig -> transportConfig.setExecutor(executor),
              cfg ->
                  cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      .setMaxPendingPublishRequests(uint(MAX_PENDING_PUBLISH_REQUESTS)));
      client.connect();

      client.addFaultListener(
          serviceFault -> {
            if (serviceFault.getResponseHeader().getServiceResult().value()
                == StatusCodes.Bad_UnexpectedError) {

              refillFaultObserved.countDown();
            }
          });

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionInactive(UaSession session) {
              sessionInactive.countDown();
            }

            @Override
            public void onSessionActive(UaSession session) {
              if (sessionInactive.getCount() == 0) {
                activationOrder.add(SESSION_ACTIVE);
                sessionReactivated.countDown();
              }
            }
          });

      scriptable.setRepublishResponder(this::respondToRepublish);

      var subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {

              for (DataValue value : values) {
                deliveredValues.add((Integer) value.getValue().getValue());
              }
            }
          });
      subscription.create();

      subscriptionId = subscription.getSubscriptionId().orElseThrow();

      // The MonitoredItem only has to exist on the client: addMonitoredItem assigns the
      // ClientHandle
      // the notification fan-out looks scripted notifications up by, and no Server-side item
      // participates in delivering one.
      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      clientHandle = item.getClientHandle().orElseThrow();
    }

    /**
     * Deliver NotificationMessages 1 and 2, leaving the client's last accounted-for sequence number
     * at {@value #LAST_SEQUENCE_NUMBER_BEFORE_FAULT} and its Publish pipeline quiescent: every
     * request parked at the Server with no responder scripted for any of them.
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
          awaitTrue(
              () -> scriptable.getParkedRequestCount() == MAX_PENDING_PUBLISH_REQUESTS,
              AWAIT_TIMEOUT_MILLIS),
          "the client did not refill its Publish pipeline");

      assertEquals(
          List.of(1, (int) LAST_SEQUENCE_NUMBER_BEFORE_FAULT),
          deliveredValues(),
          "precondition: the client must have accounted for NotificationMessages 1 and 2 before the"
              + " Session fault");
    }

    /** Answer TransferSubscriptions with an empty availableSequenceNumbers (Part 4 §5.14.7.1). */
    void advertiseNothingOnTransfer() {
      scriptable.transferScripted.set(true);
    }

    /**
     * Refuse the next ActivateSession with a ServiceFault, which sends the Session FSM to {@code
     * CreatingWait} and from there onto the create-a-new-Session-and-transfer path, and hold the
     * ActivateSession of the replacement Session so that a caller can be parked on the Session
     * future the FSM will complete for it.
     */
    void refuseNextReactivationAndHoldTheReplacement() {
      sessionServiceSet.refuseNext.set(true);
      sessionServiceSet.holdNext.set(true);
    }

    void awaitReplacementActivateSessionHeld() throws Exception {
      assertTrue(
          sessionServiceSet.heldActivateSession.await(
              RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the client never got as far as activating a replacement Session");
    }

    void releaseReplacementActivateSession() {
      sessionServiceSet.activateSessionGate.countDown();
    }

    void armRequestLog() {
      requestLog.arm();
    }

    /**
     * Answer one parked PublishRequest with a Bad_SessionIdInvalid ServiceFault, which {@code
     * SessionFsmFactory}'s SessionFaultListener classifies as a Session error and turns into a
     * reconnect. The Server-side Session is untouched, so re-activation succeeds unless {@link
     * #refuseNextReactivation()} was called.
     */
    void faultSession() {
      scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
    }

    /**
     * Return the remaining outstanding PublishRequest with a failure that is a statement about that
     * one request rather than about the Session or the Subscription set, so {@code
     * PublishingManager} answers it by trying to send a replacement. The Session is gone by now, so
     * that replacement's caller finds no Session and parks on the one being established: exactly
     * the caller the suspension gate has to hold back when it arrives.
     *
     * <p>Returns only once the client has run the failure handler, so the caller is provably parked
     * before the Session future can complete. The fault listener fires from a task the failing
     * request's completion handler submits, and the failure handler is another such task, so a
     * barrier queued once the listener has fired can still be ahead of it — but the second barrier
     * cannot, because the first only runs after that completion handler has returned, by which time
     * the failure handler is queued.
     */
    void parkAPublishRefillOnTheSessionFuture() throws Exception {
      scriptable.enqueueServiceFault(StatusCodes.Bad_UnexpectedError);

      assertTrue(
          refillFaultObserved.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the client never observed the ServiceFault that provokes the parked Publish refill");

      awaitTransportExecutorDrained();
      awaitTransportExecutorDrained();
    }

    /** Wait until the transport executor has run everything queued before this call. */
    private void awaitTransportExecutorDrained() throws Exception {
      var drained = new CompletableFuture<Void>();
      executor.execute(() -> drained.complete(null));

      drained.get(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }

    /** Park a caller on the Session future that records when it is released. */
    void parkAnObserverOnTheSessionFuture() {
      client.getSessionAsync().whenComplete((session, ex) -> activationOrder.add(SESSION_FUTURE));
    }

    /**
     * Script the next PublishResponse as a data message carrying {@code sequenceNumber}, whose
     * value identifies the NotificationMessage it came from.
     */
    void enqueueDataChange(long sequenceNumber) {
      scriptable.enqueue(
          request ->
              CompletableFuture.completedFuture(
                  scriptable.buildPublishResponse(
                      request,
                      subscriptionId,
                      sequenceNumber,
                      notificationData(sequenceNumber),
                      new UInteger[] {uint(sequenceNumber)},
                      false)));
    }

    /**
     * The Server holds nothing for retransmission, so every Republish is answered
     * Bad_MessageNotAvailable — which is also what terminates the Republish loop Part 4 §6.7
     * describes.
     */
    private RepublishResponse respondToRepublish(RepublishRequest request) throws UaException {
      requestLog.record(REPUBLISH + request.getRetransmitSequenceNumber().longValue());

      throw new UaException(StatusCodes.Bad_MessageNotAvailable);
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

    void awaitSessionInactive() throws Exception {
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
    }

    void awaitReactivation() throws Exception {
      awaitSessionInactive();

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

    List<String> activationOrder() {
      return List.copyOf(activationOrder);
    }

    List<Integer> deliveredValues() {
      return List.copyOf(deliveredValues);
    }

    int transferCount() {
      return scriptable.transferCount;
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
      sessionServiceSet.activateSessionGate.countDown();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // A client whose single transport thread is occupied cannot run the disconnect it is asked
        // for; shutting the Server and the executor down below is what releases it. Tolerated here
        // so teardown does not mask the assertion that detected the stall.
      } finally {
        try {
          server.shutdown().get(10, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  /** Daemon threads, so a test that leaves work behind cannot keep the JVM alive. */
  private static ThreadFactory daemonThreadFactory(String name) {
    return runnable -> {
      var thread = new Thread(runnable, name);
      thread.setDaemon(true);

      return thread;
    };
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
