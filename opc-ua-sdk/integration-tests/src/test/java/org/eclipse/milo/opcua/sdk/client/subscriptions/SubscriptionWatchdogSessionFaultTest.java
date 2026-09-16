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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

/**
 * A Publish that fails with {@code Bad_SessionIdInvalid} or {@code Bad_SessionClosed} means the
 * <i>Session</i> is gone, not the Subscription. {@code SessionFsmFactory.SessionFaultListener}
 * classifies both as Session errors and drives the Session FSM out of {@code Active} and back
 * through re-activation, after which the Subscription is still alive on the Server and Publish
 * traffic resumes on it.
 *
 * <p>The watchdog is the only mechanism that tells an application the Server has stopped honouring
 * the keep-alive interval it promised in its CreateSubscription response (Part 4 §5.13.2 — the
 * Server must send a keep-alive Publish response when MaxKeepAliveCount publishing cycles pass with
 * no notifications). A transient Session fault must therefore <i>suspend</i> the watchdog, not
 * destroy it: if it is destroyed, the Subscription runs unsupervised for the rest of its life and a
 * Server that later goes silent is never reported, even though Publish traffic itself recovered.
 *
 * <p>Every Publish is parked by {@link ScriptableSubscriptionServiceSet} except the first, which is
 * answered with the Session fault, so no Publish response can re-arm the watchdog and the only
 * possible re-arming is the one the Session's return to {@code Active} is supposed to perform.
 */
public class SubscriptionWatchdogSessionFaultTest {

  /** Made explicit rather than relying on the default, because the delay below depends on it. */
  private static final double WATCHDOG_MULTIPLIER = 1.5;

  private static final double PUBLISHING_INTERVAL = 111.0;

  /** ceil(111 / 111) derives a MaxKeepAliveCount of 1. */
  private static final double TARGET_KEEP_ALIVE_INTERVAL = 111.0;

  private static final UInteger EXPECTED_MAX_KEEP_ALIVE_COUNT = uint(1);

  /** 111 * (1 + 1) * 1.5 — the watchdog delay implied by the revised parameters. */
  private static final long WATCHDOG_DELAY_MILLIS = 333;

  /**
   * The Session FSM waits one second in {@code ReactivatingWait} before its first re-activation
   * attempt, and doubles the wait on each failure; this window allows for several attempts.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 20_000;

  /** Two orders of magnitude more than {@link #WATCHDOG_DELAY_MILLIS}. */
  private static final long WATCHDOG_TIMEOUT_MILLIS = 10_000;

  /**
   * A Publish failing with {@code Bad_SessionIdInvalid} takes the Session down and brings it back;
   * it says nothing about the Subscription. Once the Session is {@code Active} again the Server's
   * keep-alive promise applies again, so the watchdog must be armed again and must elapse when the
   * Server stays silent — exactly as it would have before the fault.
   */
  @Test
  void watchdogElapsesAfterSessionFaultAndReactivation() throws Exception {
    try (var fixture = new Fixture()) {
      var sessionInactive = new CountDownLatch(1);
      var sessionReactivated = new CountDownLatch(1);
      var reactivated = new AtomicBoolean(false);

      fixture.client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionInactive(UaSession session) {
              sessionInactive.countDown();
            }

            @Override
            public void onSessionActive(UaSession session) {
              if (sessionInactive.getCount() == 0) {
                reactivated.set(true);
                sessionReactivated.countDown();
              }
            }
          });

      // Answers the first Publish the client sends once the Subscription is created; every
      // subsequent Publish is parked.
      fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);

      OpcUaSubscription subscription = fixture.createSubscription();
      assertRevisedParameters(subscription);

      // Only expiries that follow the return to Active are counted. An expiry armed before the
      // fault proves nothing about recovery, and this test must not be able to pass on one.
      var elapsedAfterReactivation = new CountDownLatch(1);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onWatchdogTimerElapsed(OpcUaSubscription subscription) {
              if (reactivated.get()) {
                elapsedAfterReactivation.countDown();
              }
            }
          });

      // Preconditions: the fault really did drive the Session FSM out of Active and back, which is
      // what makes the watchdog assertion below meaningful.
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
      assertTrue(
          sessionReactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session was never re-activated");

      assertTrue(
          elapsedAfterReactivation.await(WATCHDOG_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog never elapsed after the Session was re-activated, although no Publish response"
              + " has been received and the expiry was due "
              + WATCHDOG_DELAY_MILLIS
              + "ms later; the Session fault destroyed the watchdog instead of suspending it");
    }
  }

  /**
   * The control that proves {@link #watchdogElapsesAfterSessionFaultAndReactivation()} is not
   * vacuous: in this fixture, with no Session fault at all, a Subscription whose Publish requests
   * go unanswered does report the expiry, and reports it on the schedule the revised parameters
   * imply.
   */
  @Test
  void watchdogElapsesWhenPublishResponsesStop() throws Exception {
    try (var fixture = new Fixture()) {
      var elapsed = new CountDownLatch(1);

      OpcUaSubscription subscription = fixture.newSubscription();
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onWatchdogTimerElapsed(OpcUaSubscription subscription) {
              elapsed.countDown();
            }
          });
      subscription.create();

      assertRevisedParameters(subscription);

      assertTrue(
          elapsed.await(WATCHDOG_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog did not elapse within "
              + WATCHDOG_TIMEOUT_MILLIS
              + "ms although no Publish response was ever delivered");
    }
  }

  /**
   * The watchdog delay is derived from the revised parameters, so the timings above are only
   * meaningful if the Server returned what was requested. Asserted up front so a Server that
   * revises them fails loudly instead of producing a mystery timeout.
   */
  private static void assertRevisedParameters(OpcUaSubscription subscription) {
    assertAll(
        () ->
            assertEquals(
                PUBLISHING_INTERVAL,
                subscription.getRevisedPublishingInterval().orElseThrow(),
                "revised PublishingInterval"),
        () ->
            assertEquals(
                EXPECTED_MAX_KEEP_ALIVE_COUNT,
                subscription.getRevisedMaxKeepAliveCount().orElseThrow(),
                "revised MaxKeepAliveCount"));
  }

  /** A running Server whose Publish requests are all parked, plus a connected client. */
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

      // Long request timeout so the parked Publish requests do not time out during the test.
      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(60_000)));
      client.connect();
    }

    OpcUaSubscription newSubscription() {
      var subscription = new OpcUaSubscription(client);
      subscription.setWatchdogMultiplier(WATCHDOG_MULTIPLIER);
      subscription.setPublishingInterval(PUBLISHING_INTERVAL);
      subscription.setTargetKeepAliveInterval(TARGET_KEEP_ALIVE_INTERVAL);

      return subscription;
    }

    OpcUaSubscription createSubscription() throws UaException {
      OpcUaSubscription subscription = newSubscription();
      subscription.create();

      return subscription;
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
