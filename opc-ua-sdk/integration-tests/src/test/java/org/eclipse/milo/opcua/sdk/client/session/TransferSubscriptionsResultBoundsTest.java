/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * What {@code SessionFsmFactory.transferSubscriptions} does with a TransferSubscriptionsResponse
 * carrying more TransferResults than the request carried SubscriptionIds.
 *
 * <p>Part 4 §5.14.7.2 defines results as the "list of results for the subscriptions to transfer",
 * one per requested SubscriptionId, so a longer list is a Server defect — but the client is the
 * party that has to survive it, and it is the only shape of that defect the client cannot detect
 * before it indexes with it.
 *
 * <p>The loop that records availableSequenceNumbers has always been bounded by both the results and
 * the requested ids ({@code SessionFsmFactory.transferSubscriptions}, {@code i < results.length &&
 * i < subscriptionIdsArray.length}). The loop that reports failures was bounded only by {@code
 * results.length} and indexed the unfiltered Subscription snapshot rather than the ids the request
 * was built from ({@code subscriptions.get(i)}), so a surplus result indexed past the end of that
 * snapshot. The IndexOutOfBoundsException escaped into the transport executor's task, where it
 * killed the worker thread and abandoned every remaining notification in the loop.
 *
 * <p>Pins that: with one Subscription and a Server answering the transfer with two results, nothing
 * may be thrown out of the transport executor, and the surplus result — which belongs to no
 * Subscription the client asked about — must not be reported to one.
 */
class TransferSubscriptionsResultBoundsTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for a reconnect. The Session FSM waits one second in {@code ReactivatingWait}
   * before its first re-activation attempt and another second in {@code CreatingWait} before
   * creating a replacement Session, and doubles each wait on every failure.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * How long the transport executor is given to run the task the transfer response dispatched. The
   * task is submitted before the FSM leaves {@code Transferring}, so by the time the Session is
   * {@code Active} again it has almost certainly already run; this is the margin, not the
   * expectation.
   */
  private static final long SETTLE_TIMEOUT_MILLIS = 2_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request and no Session
   * keep-alive. Every stall asserted against below is therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  @Test
  void surplusTransferResultIsNotIndexedAgainstTheSubscriptionSnapshot() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.awaitPublishPipelineFull();
      fixture.refuseNextReactivation();
      fixture.faultSession();
      fixture.awaitReactivation();

      assertTrue(
          fixture.awaitTrue(() -> fixture.transferCount() >= 1, AWAIT_TIMEOUT_MILLIS),
          "precondition: the reconnect did not go through TransferSubscriptions, so this test is"
              + " not exercising the transfer path at all");

      // Returns as soon as something is thrown, so only a client that survives the surplus result
      // waits out the full timeout.
      fixture.awaitTrue(() -> !fixture.uncaughtFailures().isEmpty(), SETTLE_TIMEOUT_MILLIS);

      List<Throwable> failures = fixture.uncaughtFailures();

      assertTrue(
          failures.isEmpty(),
          "a TransferResult the client has no SubscriptionId for was indexed against the"
              + " Subscription snapshot: "
              + failures.stream().map(Throwable::toString).toList());
      assertEquals(
          0,
          fixture.transferFailedCount(),
          "the surplus TransferResult corresponds to no Subscription the client asked to transfer,"
              + " so no Subscription may be reset and reported as transfer-failed because of it");
    }
  }

  // region Fixture

  /**
   * A {@link ScriptableSubscriptionServiceSet} that answers TransferSubscriptions with one Good
   * result per requested SubscriptionId plus one surplus Bad result.
   *
   * <p>Publish is left to the script, i.e. parked, so the Server-side Subscription's own transfer
   * state never participates in the test.
   */
  private static final class SurplusResultSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger transferCount = new AtomicInteger(0);

    SurplusResultSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public TransferSubscriptionsResponse onTransferSubscriptions(
        ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {

      transferCount.incrementAndGet();

      UInteger[] subscriptionIds = request.getSubscriptionIds();
      int requested = subscriptionIds != null ? subscriptionIds.length : 0;

      var results = new TransferResult[requested + 1];
      for (int i = 0; i < requested; i++) {
        results[i] = new TransferResult(StatusCode.GOOD, null);
      }
      results[requested] =
          new TransferResult(new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid), null);

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
   * A running Server whose TransferSubscriptions and ActivateSession responses are scripted, and a
   * connected client with one Subscription.
   *
   * <p>The client's transport executor is its own pool rather than {@code Stack.sharedExecutor()},
   * so anything thrown out of a task the SDK dispatched onto it is attributable to this test
   * instead of being logged and lost among the other tests sharing that pool.
   */
  private static final class Fixture implements AutoCloseable {

    private final List<Throwable> uncaught = Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger transferFailedCount = new AtomicInteger(0);

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    private final ExecutorService executor;
    private final OpcUaServer server;
    private final OpcUaClient client;
    private final SurplusResultSubscriptionServiceSet scriptable;
    private final RefusingSessionServiceSet sessionServiceSet;

    @SuppressWarnings("unused")
    private final OpcUaSubscription subscription;

    Fixture() throws Exception {
      executor = recordingThreadPool(uncaught);

      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new SurplusResultSubscriptionServiceSet(server);
      sessionServiceSet = new RefusingSessionServiceSet(server);

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

      subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onTransferFailed(OpcUaSubscription s, StatusCode status) {
              transferFailedCount.incrementAndGet();
            }
          });
      subscription.create();
    }

    /**
     * Wait until the client's single PublishRequest is parked at the Server, which is what {@link
     * #faultSession()} answers.
     */
    void awaitPublishPipelineFull() throws Exception {
      assertTrue(
          awaitTrue(() -> scriptable.getParkedRequestCount() == 1, AWAIT_TIMEOUT_MILLIS),
          "the client never filled its Publish pipeline, so there is no request to fault");
    }

    /**
     * Refuse the next ActivateSession with a ServiceFault, which sends the Session FSM to {@code
     * CreatingWait} and from there onto the create-a-new-Session-and-transfer path.
     */
    void refuseNextReactivation() {
      sessionServiceSet.refuseNext.set(true);
    }

    /**
     * Answer the one parked PublishRequest with a Bad_SessionIdInvalid ServiceFault, which {@code
     * SessionFsmFactory}'s SessionFaultListener classifies as a Session error and turns into a
     * reconnect.
     */
    void faultSession() {
      scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
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

    int transferCount() {
      return scriptable.transferCount.get();
    }

    int transferFailedCount() {
      return transferFailedCount.get();
    }

    List<Throwable> uncaughtFailures() {
      return List.copyOf(uncaught);
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
        try {
          server.shutdown().get(10, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  /**
   * The shape of {@code Stack.sharedExecutor()} — an unbounded cached pool of daemon threads — with
   * an uncaught-exception handler that records instead of logging. {@code ThreadPoolExecutor}
   * rethrows whatever a task passed to {@code execute} threw, so the handler sees it.
   */
  private static ExecutorService recordingThreadPool(List<Throwable> uncaught) {
    return new ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        recordingThreadFactory(uncaught));
  }

  private static ThreadFactory recordingThreadFactory(List<Throwable> uncaught) {
    var counter = new AtomicInteger(0);

    return runnable -> {
      var thread = new Thread(runnable, "transfer-results-" + counter.incrementAndGet());
      thread.setDaemon(true);
      thread.setUncaughtExceptionHandler((t, ex) -> uncaught.add(ex));

      return thread;
    };
  }

  // endregion
}
