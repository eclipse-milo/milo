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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * When the Publish pipeline is refilled after a fault.
 *
 * <p>{@code PublishingManager} keeps {@code min(subscriptionCount + 1, maxPendingPublishRequests)}
 * Publish requests outstanding per Session and refills the pipeline whenever one of them is dealt
 * with. Two of the fault paths get the refill wrong, and both are exercised here through {@link
 * ScriptableSubscriptionServiceSet}, which parks every Publish request until the test decides how
 * it ends — so the timing belongs to the test rather than to a Server timer.
 */
public class PublishPipelineRefillTest {

  /** One Subscription targets min(1 + 1, UInteger.MAX_VALUE) = 2 outstanding Publish requests. */
  private static final int PIPELINE_TARGET = 2;

  private static final long PIPELINE_FILL_TIMEOUT_MILLIS = 10_000;

  private static final long REFILL_TIMEOUT_MILLIS = 10_000;

  /** How long to watch for a Publish request that must not be sent. */
  private static final long QUIET_WINDOW_MILLIS = 2_000;

  private static final long DELIVERY_TIMEOUT_MILLIS = 10_000;

  /**
   * Part 4 §5.14.8.1: when the last Subscription of a Session is deleted, "all Publish requests
   * still queued for that Session are de-queued and shall be returned with Bad_NoSubscription".
   * Milo's own Server does exactly that in {@code SubscriptionManager}.
   *
   * <p>An application that deletes its last Subscription and immediately creates another therefore
   * takes that whole burst of Bad_NoSubscription failures <i>after</i> its Subscription set has
   * become non-empty again. {@code addSubscription()} cannot start the new Subscription's Publish
   * traffic, because the permits taken by the de-queued requests are still held when it runs, so
   * the refill has to come from the failure handler — and the failure handler suppresses it
   * unconditionally for Bad_NoSubscription.
   */
  @Nested
  class DeletingTheLastSubscription {

    /**
     * Control: the same delete/create/fail sequence, but with a status code the failure handler
     * does not suppress the refill for. It proves the fixture can observe a refill at all, so the
     * two tests below fail because of what Bad_NoSubscription does and not because a refill is
     * unobservable here.
     */
    @Test
    void publishRequestsResumeWhenTheDeQueuedRequestsFailWithAnotherStatusCode() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription first = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        first.delete();
        fixture.createSubscription();

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.failParkedRequests(StatusCodes.Bad_InternalError);

        assertTrue(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > before, REFILL_TIMEOUT_MILLIS),
            "no Publish request was sent for the newly created Subscription");
      }
    }

    @Test
    void publishRequestsResumeAfterTheLastSubscriptionIsDeletedAndAnotherIsCreated()
        throws Exception {

      try (var fixture = new Fixture()) {
        OpcUaSubscription first = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        first.delete();
        fixture.createSubscription();

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);

        assertTrue(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > before, REFILL_TIMEOUT_MILLIS),
            "no Publish request was sent for the newly created Subscription: the de-queued requests"
                + " released their permits without refilling the pipeline, and nothing else will");
      }
    }

    /**
     * The same defect stated as what the application sees. A Subscription that was created
     * successfully, and that the Server is happily publishing for, never delivers anything.
     */
    @Test
    void recreatedSubscriptionReceivesNotificationsAfterTheLastOneWasDeleted() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription first = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        first.delete();

        OpcUaSubscription second = fixture.createSubscription();

        var keepAliveReceived = new CountDownLatch(1);
        second.setSubscriptionListener(
            new OpcUaSubscription.SubscriptionListener() {
              @Override
              public void onKeepAliveReceived(OpcUaSubscription subscription) {
                keepAliveReceived.countDown();
              }
            });

        fixture.scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);

        // Held by the harness until a Publish request arrives to carry it.
        fixture.scriptable.enqueueKeepAlive(second.getSubscriptionId().orElseThrow(), 1);

        assertTrue(
            keepAliveReceived.await(DELIVERY_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
            "the re-created Subscription never received a NotificationMessage: no Publish request"
                + " was ever sent for it");
      }
    }
  }

  /**
   * Part 4 §5.14.5.1: a Client "shall not issue another Publish request before one of its
   * outstanding Publish requests is returned" once the Server has answered
   * Bad_TooManyPublishRequests — one returned request buys one replacement.
   *
   * <p>The immediate half of that is implemented: the failed request is not replaced on the spot.
   * But the target is left at subscriptionCount + 1, so the deficit the fault opened is still
   * there, and the next PublishResponse that is delivered refills all of it at once. One returned
   * request causes two to be issued, restoring exactly the outstanding count that drew the fault.
   *
   * <p>The same clause also requires a Server to accept at least subscriptionCount + 1 queued
   * Publish requests, which is precisely what the Client aims for, so a conformant Server never
   * answers Bad_TooManyPublishRequests here. This is about what the Client does when a
   * non-conformant one does.
   */
  @Nested
  class TooManyPublishRequests {

    /**
     * Control: the immediate suppression, which already works. It also establishes that the quiet
     * window below is long enough for the failure to have been accounted for.
     */
    @Test
    void noPublishRequestIsSentImmediatelyAfterBadTooManyPublishRequests() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_TooManyPublishRequests);

        assertFalse(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > before, QUIET_WINDOW_MILLIS),
            "a Publish request was sent straight back at a Server that had just refused one for"
                + " holding too many");
      }
    }

    @Test
    void atMostOnePublishRequestReplacesTheOneThatReturnedAfterTheFault() throws Exception {
      try (var fixture = new Fixture()) {
        OpcUaSubscription subscription = fixture.createSubscription();
        assertTrue(fixture.awaitFullPipeline(), "the Publish pipeline never filled");

        fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_TooManyPublishRequests);

        // The suppression asserted by the control test, repeated here because it is also the
        // barrier: it is what makes the failure accounted for before the response below is
        // delivered, so the number of requests that follow is a property of the client and not of
        // which of the two completions won.
        assertFalse(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > PIPELINE_TARGET,
                QUIET_WINDOW_MILLIS),
            "a Publish request was sent straight back at a Server that had just refused one for"
                + " holding too many");

        int before = fixture.scriptable.getPublishRequestCount();

        fixture.scriptable.enqueueKeepAlive(subscription.getSubscriptionId().orElseThrow(), 1);

        assertTrue(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > before, REFILL_TIMEOUT_MILLIS),
            "the returned Publish request was not replaced at all");

        assertFalse(
            awaitTrue(
                () -> fixture.scriptable.getPublishRequestCount() > before + 1,
                QUIET_WINDOW_MILLIS),
            "two Publish requests were issued in response to one returning, restoring the very"
                + " outstanding count that had just drawn Bad_TooManyPublishRequests");
      }
    }
  }

  private static boolean awaitTrue(BooleanSupplierThrowing condition, long timeoutMillis)
      throws Exception {

    long deadline = System.currentTimeMillis() + timeoutMillis;
    while (System.currentTimeMillis() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }
    return condition.get();
  }

  @FunctionalInterface
  private interface BooleanSupplierThrowing {
    boolean get() throws Exception;
  }

  /**
   * A running Server whose Publish requests are all parked by {@link
   * ScriptableSubscriptionServiceSet} — nothing completes one unless the test says so — plus a
   * connected client.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
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

    /**
     * Wait until the client has {@value #PIPELINE_TARGET} Publish requests outstanding, all of them
     * parked by the harness. The Server therefore holds them exactly as a Server holding queued
     * Publish requests for a Session would.
     */
    boolean awaitFullPipeline() throws Exception {
      return awaitTrue(
          () -> scriptable.getParkedRequestCount() >= PIPELINE_TARGET,
          PIPELINE_FILL_TIMEOUT_MILLIS);
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
