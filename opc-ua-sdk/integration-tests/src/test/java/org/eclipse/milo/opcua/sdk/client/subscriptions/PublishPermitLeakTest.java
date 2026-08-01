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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The pending-publish permits {@code PublishingManager} uses to keep a bounded number of
 * PublishRequests in flight, and the answers that must not cost it one.
 *
 * <p>A permit is taken before a PublishRequest is sent and released when its answer has been dealt
 * with; the release is also what sends the replacement request. A permit that is never released is
 * therefore not merely a leak: the pipeline it belonged to permanently shrinks by one, and a client
 * that loses its last permit stops asking the Server for notifications altogether, silently and for
 * good — no error is reported, and the Subscription stays alive on the Server.
 *
 * <p>Every test here configures maxPendingPublishRequests = 1, so that the client's single
 * Subscription targets min(1 + 1, 1) = 1 outstanding request. A leaked permit is then the whole
 * pipeline, and the symptom is exactly the one the application would see: no further
 * PublishRequest, ever.
 *
 * @see PublishPipelineRefillTest for the refill rules the permits feed.
 */
public class PublishPermitLeakTest {

  /** min(subscriptionCount + 1, maxPendingPublishRequests) = min(2, 1) with one Subscription. */
  private static final int PIPELINE_TARGET = 1;

  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * Long enough that nothing times out on its own: the PublishRequests this test parks at the
   * Server must stay parked until the test decides how they end.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /**
   * Part 4 §5.14.5.2 gives a PublishRequest a PublishResponse, and the client dispatches on the
   * type it gets: anything else goes down the failure path. That path had no failure to inspect —
   * the request completed <i>successfully</i>, just not with a PublishResponse — so extracting a
   * StatusCode from the (null) exception threw, and the permit the request held was never released.
   *
   * <p>The answer is fabricated by the transport rather than by the Server, because the type of a
   * Publish answer is fixed by {@code SubscriptionServiceSet.onPublish}: a Server cannot express
   * one through the service set, and a ServiceFault — the one wrong-typed message a Server can send
   * — is turned into an exception by the client's channel before the SDK ever sees it. What is
   * under test is the client's accounting of the answer, in which the Server's copy of the request
   * plays no part, so the intercepted request is not sent at all.
   */
  @Nested
  class AnswerThatIsNotAPublishResponse {

    /**
     * Control: the same interception answering with a service failure instead, i.e. the same path
     * with an exception to extract a StatusCode from. It proves the fixture observes the pipeline
     * being refilled after an intercepted answer at all, so the test below fails because the answer
     * carried no exception and not because a refill is unobservable here.
     */
    @Test
    void publishRequestsResumeAfterAFailedPublishRequest() throws Exception {
      try (var fixture = new Fixture(PublishAnswer.FAILURE)) {
        fixture.createSubscription();

        assertTrue(fixture.awaitInterceptedRequest(), "the client never sent a PublishRequest");

        assertTrue(
            fixture.awaitPublishRequestAtServer(),
            "the failed PublishRequest was not replaced: its permit was never released");
      }
    }

    @Test
    void publishRequestsResumeAfterAnAnswerThatIsNotAPublishResponse() throws Exception {
      try (var fixture = new Fixture(PublishAnswer.WRONG_TYPE)) {
        fixture.createSubscription();

        assertTrue(fixture.awaitInterceptedRequest(), "the client never sent a PublishRequest");

        assertTrue(
            fixture.awaitPublishRequestAtServer(),
            "the client sent no further PublishRequest after being answered with a message that"
                + " was not a PublishResponse: the answer completed the request successfully, so"
                + " there was no exception to take a StatusCode from, and the pending-publish"
                + " permit the request held was lost along with the whole pipeline");
      }
    }
  }

  /**
   * A NotificationMessage is handed to the Subscription's delivery queue and the permit is released
   * when the application callbacks have run, which is the backpressure the Server is held to. A
   * delivery queue that cannot accept the task — {@code TaskQueue.submit()} returns {@code null}
   * once it is shut down, or when its max queue size would be exceeded — has no callbacks to wait
   * for, so nothing will ever release the permit on the message's behalf.
   *
   * <p>{@code recoverSubscription} already treated the same rejection from a Subscription's
   * processing queue as work that will not happen and completed in its place; the delivery queue's
   * rejection was simply dropped.
   */
  @Nested
  class DeliveryQueueThatCannotAcceptTheMessage {

    /**
     * Control: the identical script with a delivery queue that can accept the message. It proves
     * the fixture observes the pipeline being refilled after a PublishResponse at all, so the test
     * below fails because the message could not be queued and not because a refill is unobservable
     * here.
     */
    @Test
    void publishRequestsResumeAfterADeliveredNotificationMessage() throws Exception {
      try (var fixture = new Fixture(PublishAnswer.NONE)) {
        OpcUaSubscription subscription = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueKeepAlive(subscription.getSubscriptionId().orElseThrow(), 1);

        assertTrue(
            fixture.awaitPublishRequestsAtServer(before + 1),
            "the answered PublishRequest was not replaced once its NotificationMessage had been"
                + " delivered");
      }
    }

    @Test
    void publishRequestsResumeWhenTheDeliveryQueueCannotAcceptTheNotificationMessage()
        throws Exception {

      try (var fixture = new Fixture(PublishAnswer.NONE)) {
        OpcUaSubscription subscription = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        // Shut down before the response arrives, so the rejection is a property of the fixture
        // rather than a race with the delivery.
        subscription.getDeliveryQueue().shutdown(false);

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueKeepAlive(subscription.getSubscriptionId().orElseThrow(), 1);

        assertTrue(
            fixture.awaitPublishRequestsAtServer(before + 1),
            "the client sent no further PublishRequest after a NotificationMessage its delivery"
                + " queue could not accept: nothing was waiting to deliver it, so nothing released"
                + " the pending-publish permit the message's PublishRequest held, and the pipeline"
                + " is empty for good");
      }
    }
  }

  // region helpers

  /** What the transport does with the client's first PublishRequest. */
  private enum PublishAnswer {

    /** Nothing: every PublishRequest is sent to the Server. */
    NONE,

    /** Answers it, without sending it, with a response that is not a PublishResponse. */
    WRONG_TYPE,

    /** Answers it, without sending it, with a service failure. */
    FAILURE
  }

  /**
   * An {@link OpcTcpClientTransport} that answers the client's first PublishRequest itself.
   *
   * <p>The interception is at the transport rather than at the Server because the answer being
   * scripted is one no Server can express through a {@code SubscriptionServiceSet}, whose {@code
   * onPublish} returns a {@code PublishResponse} and nothing else.
   */
  private static final class MisansweringTransport extends OpcTcpClientTransport {

    private final AtomicInteger intercepted = new AtomicInteger(0);

    private final PublishAnswer answer;

    MisansweringTransport(OpcTcpClientTransportConfig config, PublishAnswer answer) {
      super(config);

      this.answer = answer;
    }

    int interceptedCount() {
      return intercepted.get();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      if (answer != PublishAnswer.NONE
          && requestMessage instanceof PublishRequest publishRequest
          && intercepted.incrementAndGet() == 1) {

        var future = new CompletableFuture<UaResponseMessageType>();

        // Completed on the transport's executor, which is where AbstractUascClientTransport
        // completes any response that is not a PublishResponse: only PublishResponses go through
        // the serial PublishResponse queue.
        getConfig()
            .getExecutor()
            .execute(
                () -> {
                  if (answer == PublishAnswer.FAILURE) {
                    future.completeExceptionally(new UaException(StatusCodes.Bad_InternalError));
                  } else {
                    future.complete(notAPublishResponse(publishRequest));
                  }
                });

        return future;
      }

      return super.sendRequestMessage(requestMessage);
    }

    /**
     * A well-formed response of the wrong type, as a Server that mixed up its handlers would send.
     */
    private static UaResponseMessageType notAPublishResponse(PublishRequest request) {
      var responseHeader =
          new ResponseHeader(
              DateTime.now(),
              request.getRequestHeader().getRequestHandle(),
              StatusCode.GOOD,
              DiagnosticInfo.NULL_VALUE,
              null,
              null);

      return new ReadResponse(responseHeader, null, null);
    }
  }

  /**
   * A running Server whose Publish requests are all parked by {@link
   * ScriptableSubscriptionServiceSet} — nothing completes one unless the test says so — plus a
   * client whose transport answers its first PublishRequest according to {@code answer}.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;
    private final MisansweringTransport transport;

    Fixture(PublishAnswer answer) throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      var transportHolder = new MisansweringTransport[1];

      client =
          TestClient.createWithTransport(
              server,
              transportConfig -> {
                transportHolder[0] = new MisansweringTransport(transportConfig, answer);
                return transportHolder[0];
              },
              cfg ->
                  cfg
                      // Long request timeout so parked Publish requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      .setMaxPendingPublishRequests(uint(PIPELINE_TARGET)));

      transport = transportHolder[0];

      client.connect();
    }

    OpcUaSubscription createSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      return subscription;
    }

    /** Wait until the transport has answered a PublishRequest on the Server's behalf. */
    boolean awaitInterceptedRequest() throws Exception {
      return awaitTrue(() -> transport.interceptedCount() >= 1, AWAIT_TIMEOUT_MILLIS);
    }

    /**
     * Wait until the client has its whole pipeline outstanding, parked at the Server exactly as a
     * Server holding queued Publish requests for a Session would hold it.
     */
    boolean awaitFullPipeline() throws Exception {
      return awaitTrue(
          () -> scriptable.getParkedRequestCount() >= PIPELINE_TARGET, AWAIT_TIMEOUT_MILLIS);
    }

    boolean awaitPublishRequestAtServer() throws Exception {
      return awaitPublishRequestsAtServer(1);
    }

    boolean awaitPublishRequestsAtServer(int count) throws Exception {
      return awaitTrue(() -> scriptable.getPublishRequestCount() >= count, AWAIT_TIMEOUT_MILLIS);
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

  /** Polls {@code condition} until it holds or {@code timeoutMillis} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis)
      throws Exception {

    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

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
