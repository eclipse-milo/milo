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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * A Server is free to revise the publishing interval and max keep-alive count it returns from
 * CreateSubscription (Part 4 §5.13.2); nothing bounds their product.
 *
 * <p>{@code PublishingManager} derives the Publish request's {@code timeoutHint} from {@code
 * revisedPublishingInterval * revisedMaxKeepAliveCount * maxPendingPublishes * 1.5}, and {@code
 * timeoutHint} is encoded as a UInt32. When the product exceeds {@code UInteger.MAX_VALUE} the
 * client must clamp it. If it does not, building the request throws — and because {@code
 * maybeSendPublishRequests()} takes a pending-publish permit <i>before</i> calling {@code
 * sendPublishRequest()}, the permit is never returned. The permits ratchet up to the maximum and
 * the client stops sending Publish requests for the rest of the Session.
 */
public class PublishTimeoutHintOverflowTest {

  /** Revised parameters a Server would plausibly return; their product is far below UInt32. */
  private static final double ORDINARY_PUBLISHING_INTERVAL = 1_000.0;

  private static final UInteger ORDINARY_MAX_KEEP_ALIVE_COUNT = uint(10);

  /**
   * A one-hour publishing interval with a max keep-alive count of 1000: both legal, and their
   * product (3.6e9 ms) multiplied by the two in-flight Publish requests and the 1.5 safety factor
   * yields ~1.08e10, well beyond the UInt32 range {@code timeoutHint} is carried in.
   */
  private static final double LARGE_PUBLISHING_INTERVAL = 3_600_000.0;

  private static final UInteger LARGE_MAX_KEEP_ALIVE_COUNT = uint(1_000);

  /**
   * Control for {@link #publishRequestIsSentWhenRevisedParametersOverflowTheTimeoutHint()}: with
   * ordinary revised parameters the client pipelines a Publish request as soon as the Subscription
   * is created. If this one fails, the fixture is broken rather than the timeout hint.
   */
  @Test
  void publishRequestIsSentWithOrdinaryRevisedParameters() throws Exception {
    try (var fixture = new Fixture(ORDINARY_PUBLISHING_INTERVAL, ORDINARY_MAX_KEEP_ALIVE_COUNT)) {
      fixture.createSubscription();

      assertTrue(
          fixture.awaitPublishRequest(),
          "no Publish request was sent after the Subscription was created");
    }
  }

  /**
   * Creating a Subscription whose revised parameters overflow the computed timeout hint must still
   * start Publish traffic. Under the defect the overflow throws out of {@code sendPublishRequest()}
   * after the pending-publish permit was taken, so no Publish request is ever sent.
   */
  @Test
  void publishRequestIsSentWhenRevisedParametersOverflowTheTimeoutHint() throws Exception {
    try (var fixture = new Fixture(LARGE_PUBLISHING_INTERVAL, LARGE_MAX_KEEP_ALIVE_COUNT)) {
      fixture.createSubscription();

      assertTrue(
          fixture.awaitPublishRequest(),
          "no Publish request was sent: the timeout hint overflowed UInt32 and the"
              + " pending-publish permit was leaked");
    }
  }

  /**
   * The same defect observed at its source. Anything thrown while building or sending the Publish
   * request escapes with the caller's pending-publish permit already taken and no completion
   * handler registered to return it.
   */
  @Test
  void sendPublishRequestDoesNotThrowWhenRevisedParametersOverflowTheTimeoutHint()
      throws Exception {
    try (var fixture = new Fixture(LARGE_PUBLISHING_INTERVAL, LARGE_MAX_KEEP_ALIVE_COUNT)) {
      fixture.createSubscription();

      PublishingManager publishingManager = fixture.client.getPublishingManager();
      OpcUaSession session = fixture.client.getSession();

      assertDoesNotThrow(() -> publishingManager.sendPublishRequest(session, new AtomicLong(1)));
    }
  }

  /**
   * A running Server whose CreateSubscription response is rewritten to advertise the given revised
   * publishing interval and max keep-alive count, plus a connected client. Publish requests are
   * counted and then parked by {@link ScriptableSubscriptionServiceSet}.
   */
  private static final class Fixture implements AutoCloseable {

    private final CountDownLatch publishRequestReceived = new CountDownLatch(1);

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    Fixture(double revisedPublishingInterval, UInteger revisedMaxKeepAliveCount) throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable =
          new ScriptableSubscriptionServiceSet(server) {
            @Override
            public CreateSubscriptionResponse onCreateSubscription(
                ServiceRequestContext context, CreateSubscriptionRequest request)
                throws UaException {

              CreateSubscriptionResponse response = super.onCreateSubscription(context, request);

              return new CreateSubscriptionResponse(
                  response.getResponseHeader(),
                  response.getSubscriptionId(),
                  revisedPublishingInterval,
                  response.getRevisedLifetimeCount(),
                  revisedMaxKeepAliveCount);
            }

            @Override
            public CompletableFuture<PublishResponse> onPublish(
                ServiceRequestContext context, PublishRequest request) {

              publishRequestReceived.countDown();

              return super.onPublish(context, request);
            }
          };

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      // Long request timeout so parked Publish requests do not time out during the test.
      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(60_000)));
      client.connect();
    }

    OpcUaSubscription createSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();
      return subscription;
    }

    boolean awaitPublishRequest() throws InterruptedException {
      return publishRequestReceived.await(5, TimeUnit.SECONDS);
    }

    @Override
    public void close() throws Exception {
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
