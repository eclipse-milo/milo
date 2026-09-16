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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
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
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * The ordering between failed-Subscription cleanup and reconnect initialization.
 *
 * <p>A replacement Session is forced by refusing re-activation. Its TransferSubscriptions answer
 * then either contains mixed Good/Bad operation results or reports an expected unsupported-service
 * failure. The failed Subscription holds its overridable callback, widening the scheduling window
 * deterministically: cleanup must already be complete and the multi-threaded Session FSM must be
 * free to enter Initializing while application notification remains held.
 */
public class TransferFailureOrderingTest {

  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;
  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /**
   * A failed TransferResult and an expected service-level transfer failure both mean the affected
   * Subscription does not exist on the replacement Session. It must therefore be absent before
   * Initializing can lead to onSessionActive recovery, while a successfully transferred peer is
   * retained.
   */
  @Test
  void mixedTransferResultsAreRemovedBeforeReconnectInitialization() throws Exception {
    transferFailureCallbacksDoNotDelayReconnectInitialization(TransferResponse.MIXED_RESULTS);
  }

  @Test
  void unsupportedTransferIsRemovedBeforeReconnectInitialization() throws Exception {
    transferFailureCallbacksDoNotDelayReconnectInitialization(TransferResponse.SERVICE_UNSUPPORTED);
  }

  private void transferFailureCallbacksDoNotDelayReconnectInitialization(TransferResponse response)
      throws Exception {

    try (var fixture = new Fixture(response, FailureNotification.HOLD)) {
      fixture.beginReplacementSessionReconnect();
      fixture.awaitTransferFailureNotification();

      assertTrue(
          fixture.awaitReconnectInitialization(RECONNECT_TIMEOUT_MILLIS),
          "a blocking transfer-failure callback kept the replacement Session in Transferring");

      List<OpcUaSubscription> subscriptionsAtActive = fixture.awaitSubscriptionsAtSessionActive();
      List<OpcUaSubscription> expected =
          response == TransferResponse.MIXED_RESULTS
              ? List.of(fixture.successfulSubscription)
              : List.of();

      assertEquals(
          expected,
          subscriptionsAtActive,
          "onSessionActive observed Subscriptions that were not transferred to the replacement"
              + " Session");
      assertTrue(
          fixture.failedSubscription.getSubscriptionId().isEmpty(),
          "the failed Subscription was not reset before the Session became Active");

      fixture.releaseTransferFailureNotification();
    }
  }

  /**
   * Application callbacks must not be able to strand the Session FSM in Transferring. If an
   * overridden notification fails before delegating, the local reset is still required before the
   * reconnect proceeds.
   */
  @Test
  void exceptionFromTransferFailureNotificationDoesNotWedgeReconnect() throws Exception {
    try (var fixture = new Fixture(TransferResponse.MIXED_RESULTS, FailureNotification.THROW)) {

      fixture.beginReplacementSessionReconnect();
      fixture.awaitTransferFailureNotification();

      assertTrue(
          fixture.awaitReconnectInitialization(RECONNECT_TIMEOUT_MILLIS),
          "an exception from transfer-failure notification wedged the Session FSM");
      assertEquals(
          List.of(fixture.successfulSubscription),
          fixture.awaitSubscriptionsAtSessionActive(),
          "the notification failed before delegating, but internal cleanup did not reset the"
              + " failed Subscription");
      assertTrue(
          fixture.failedSubscription.getSubscriptionId().isEmpty(),
          "internal cleanup did not clear the failed SubscriptionId");
    }
  }

  private enum TransferResponse {
    MIXED_RESULTS,
    SERVICE_UNSUPPORTED
  }

  private enum FailureNotification {
    HOLD,
    THROW
  }

  /** Refuses one re-activation, forcing the FSM to create a replacement Session. */
  private static final class RefusingSessionServiceSet extends DelegatingSessionServiceSet {

    private final AtomicBoolean refuseNextActivation = new AtomicBoolean(false);

    RefusingSessionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public ActivateSessionResponse onActivateSession(
        ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

      if (refuseNextActivation.compareAndSet(true, false)) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      }

      return super.onActivateSession(context, request);
    }
  }

  /** Supplies either mixed operation results or an expected service-level failure. */
  private static final class TransferSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    private volatile TransferResponse response;
    private volatile UInteger failedSubscriptionId;

    TransferSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public TransferSubscriptionsResponse onTransferSubscriptions(
        ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {

      TransferResponse response = this.response;

      if (response == null) {
        return super.onTransferSubscriptions(context, request);
      } else if (response == TransferResponse.SERVICE_UNSUPPORTED) {
        throw new UaException(StatusCodes.Bad_ServiceUnsupported);
      }

      UInteger[] subscriptionIds = request.getSubscriptionIds();
      int count = subscriptionIds != null ? subscriptionIds.length : 0;
      var results = new TransferResult[count];

      for (int i = 0; i < count; i++) {
        StatusCode status =
            subscriptionIds[i].equals(failedSubscriptionId)
                ? new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid)
                : StatusCode.GOOD;

        results[i] = new TransferResult(status, new UInteger[0]);
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

  /** Holds or rejects the overridable notification after internal transfer cleanup has run. */
  private static final class ControllableSubscription extends OpcUaSubscription {

    private final FailureNotification behavior;
    private final CountDownLatch notificationEntered = new CountDownLatch(1);
    private final CountDownLatch notificationGate = new CountDownLatch(1);

    ControllableSubscription(OpcUaClient client, FailureNotification behavior) {
      super(client);
      this.behavior = behavior;
    }

    @Override
    public void notifyTransferFailed(StatusCode status) {
      notificationEntered.countDown();

      if (behavior == FailureNotification.THROW) {
        throw new IllegalStateException("scripted transfer-failure notification exception");
      }

      try {
        if (!notificationGate.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
          throw new IllegalStateException("timed out waiting to release transfer-failure cleanup");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException(e);
      }

      super.notifyTransferFailed(status);
    }
  }

  private static final class Fixture implements AutoCloseable {

    private final ExecutorService executor =
        Executors.newFixedThreadPool(4, daemonThreadFactory("transfer-failure-ordering"));

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch reconnectInitializationStarted = new CountDownLatch(1);
    private final CompletableFuture<List<OpcUaSubscription>> subscriptionsAtSessionActive =
        new CompletableFuture<>();

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final TransferSubscriptionServiceSet subscriptionServiceSet;
    private final RefusingSessionServiceSet sessionServiceSet;

    private final ControllableSubscription failedSubscription;
    private final OpcUaSubscription successfulSubscription;

    Fixture(TransferResponse response, FailureNotification notification) throws Exception {
      server = TestServer.create().getServer();
      subscriptionServiceSet = new TransferSubscriptionServiceSet(server);
      sessionServiceSet = new RefusingSessionServiceSet(server);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), subscriptionServiceSet);
        server.addServiceSet(endpoint.getPath(), sessionServiceSet);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              transportConfig -> transportConfig.setExecutor(executor),
              config ->
                  config
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      .setMaxPendingPublishRequests(uint(3)));
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
                subscriptionsAtSessionActive.complete(client.getSubscriptions());
              }
            }
          });
      client.addSessionInitializer(
          (OpcUaClient ignoredClient, OpcUaSession ignoredSession) -> {
            reconnectInitializationStarted.countDown();
            return CompletableFuture.completedFuture(Unit.VALUE);
          });

      failedSubscription = new ControllableSubscription(client, notification);
      failedSubscription.create();

      successfulSubscription = new OpcUaSubscription(client);
      successfulSubscription.create();

      subscriptionServiceSet.failedSubscriptionId =
          failedSubscription.getSubscriptionId().orElseThrow();
      subscriptionServiceSet.response = response;

      assertTrue(
          awaitTrue(() -> subscriptionServiceSet.getParkedRequestCount() > 0, AWAIT_TIMEOUT_MILLIS),
          "the client did not establish a Publish pipeline before the reconnect");
    }

    void beginReplacementSessionReconnect() {
      sessionServiceSet.refuseNextActivation.set(true);
      subscriptionServiceSet.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
    }

    void awaitTransferFailureNotification() throws Exception {
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Session fault did not make the Session inactive");
      assertTrue(
          failedSubscription.notificationEntered.await(
              RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the replacement Session never reached failed-Subscription cleanup");
    }

    boolean awaitReconnectInitialization(long timeoutMillis) throws InterruptedException {
      return reconnectInitializationStarted.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    void releaseTransferFailureNotification() {
      failedSubscription.notificationGate.countDown();
    }

    List<OpcUaSubscription> awaitSubscriptionsAtSessionActive() throws Exception {
      return subscriptionsAtSessionActive.get(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws Exception {
      releaseTransferFailureNotification();
      subscriptionServiceSet.failParkedRequests(StatusCodes.Bad_NoSubscription);

      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // An assertion can leave reconnect work in flight. Server and executor teardown below
        // releases it without obscuring the assertion that failed.
      } finally {
        try {
          server.shutdown().get(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  private static ThreadFactory daemonThreadFactory(String prefix) {
    var sequence = new AtomicInteger();

    return runnable -> {
      var thread = new Thread(runnable, prefix + "-" + sequence.incrementAndGet());
      thread.setDaemon(true);
      return thread;
    };
  }

  private static boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis)
      throws Exception {

    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

    while (System.nanoTime() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(10);
    }

    return condition.get();
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }
}
