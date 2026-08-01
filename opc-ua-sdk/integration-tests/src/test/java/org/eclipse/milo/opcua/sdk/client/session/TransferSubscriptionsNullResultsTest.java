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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * What {@code SessionFsmFactory.transferSubscriptions} does when its own response handling throws.
 *
 * <p>{@code transferSubscriptions} returns a {@code transferFuture} it completes from inside a
 * {@code whenComplete} whose own result future is discarded. Anything thrown in that callback is
 * therefore captured by a future nobody looks at, and {@code transferFuture} is never completed at
 * all — which leaves the FSM parked in {@code Transferring} with no event pending and no timeout,
 * so the Session never comes back and {@code closeSession()} is shelved forever.
 *
 * <p>{@code requireNonNull(tsr.getResults())} is the reachable throw site: Part 4 §5.14.7.2 defines
 * results as the "list of results for the subscriptions to transfer", but a Server that answers
 * Good with no results at all is a defect the client is the party that has to survive.
 *
 * <p>Pins that a transfer response the client cannot handle takes the FSM out of {@code
 * Transferring} the way any other transfer failure does, rather than wedging it: the Server here
 * carries out the first TransferSubscriptions but hands the Client a copy of the response with a
 * null results array, and answers every subsequent one normally. A client that treats the first as
 * a failure and starts over must preserve the transferred Subscription for the retry; only a wedged
 * or destructive cleanup path fails to recover it.
 */
class TransferSubscriptionsNullResultsTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for a reconnect. The Session FSM waits one second in {@code ReactivatingWait}
   * before its first re-activation attempt and another second in {@code CreatingWait} before
   * creating a replacement Session, and doubles each wait on every failure — and this test spends
   * two of those failures before it can succeed.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request and no Session
   * keep-alive. Every stall asserted against below is therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  @Test
  void unhandleableTransferResponseRetriesWithoutDeletingSubscription() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.awaitPublishPipelineFull();
      fixture.refuseNextReactivation();
      fixture.faultSession();

      assertTrue(
          fixture.sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");

      assertTrue(
          fixture.awaitTrue(() -> fixture.transferCount() >= 1, AWAIT_TIMEOUT_MILLIS),
          "precondition: the reconnect did not go through TransferSubscriptions, so this test is"
              + " not exercising the transfer path at all");

      assertTrue(
          fixture.sessionReactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session never became Active again: the throw out of the TransferSubscriptions"
              + " response handling left transferFuture uncompleted, and the FSM parked in"
              + " Transferring with nothing pending");

      assertTrue(
          fixture.transferCount() >= 2,
          "the Session became Active without retrying the unhandleable transfer response");

      assertTrue(
          fixture.subscriptionExistsOnServer(),
          "automatic Session cleanup deleted the Subscription before the replacement Session"
              + " could transfer it");
    }
  }

  // region Fixture

  /**
   * A {@link ScriptableSubscriptionServiceSet} that performs the first TransferSubscriptions but
   * replaces its results with {@code null} before returning it, and answers every subsequent one
   * normally.
   *
   * <p>Publish is left to the script, i.e. parked, so nothing else completes or removes the
   * Server-side Subscription while the Session FSM retries.
   */
  private static final class NullResultsSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger transferCount = new AtomicInteger(0);

    NullResultsSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public TransferSubscriptionsResponse onTransferSubscriptions(
        ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {

      boolean first = transferCount.getAndIncrement() == 0;

      TransferSubscriptionsResponse response = super.onTransferSubscriptions(context, request);

      if (first) {
        return new TransferSubscriptionsResponse(
            response.getResponseHeader(), null, response.getDiagnosticInfos());
      } else {
        return response;
      }
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
   */
  private static final class Fixture implements AutoCloseable {

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final NullResultsSubscriptionServiceSet scriptable;
    private final RefusingSessionServiceSet sessionServiceSet;

    private final OpcUaSubscription subscription;
    private final UInteger subscriptionId;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new NullResultsSubscriptionServiceSet(server);
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

      subscription = new OpcUaSubscription(client);
      subscription.create();
      subscriptionId = subscription.getSubscriptionId().orElseThrow();
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

    int transferCount() {
      return scriptable.transferCount.get();
    }

    boolean subscriptionExistsOnServer() {
      return server.getSubscriptions().containsKey(subscriptionId);
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
        // Bounded: a wedged FSM shelves the CloseSession event forever.
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
