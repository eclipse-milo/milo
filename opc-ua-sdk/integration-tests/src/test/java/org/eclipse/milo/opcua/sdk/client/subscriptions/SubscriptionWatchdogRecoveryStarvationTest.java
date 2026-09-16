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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.junit.jupiter.api.Test;

/**
 * What the Subscription watchdog is allowed to conclude while the client is still recovering from a
 * reconnect.
 *
 * <p>The watchdog exists to report a Server that has stopped honouring the keep-alive interval it
 * promised in its CreateSubscription response (Part 4 §5.13.2), and the only event that feeds it is
 * a PublishResponse. Part 4 §6.7 makes the client run a Republish loop before Publish handling
 * resumes — "After the Republish returns Bad_MessageNotAvailable the Client shall start sending
 * Publish requests with the normal Publish handling" — so for as long as that loop runs no
 * PublishResponse <i>can</i> arrive: the client is holding its own Publish traffic shut.
 *
 * <p>Arming the watchdog when the Session becomes Active therefore starts a countdown that nothing
 * is able to stop. A recovery that takes longer than the watchdog delay reports {@code
 * onWatchdogTimerElapsed} for a Subscription that is recovering exactly as the specification says
 * it should, which is the opposite of what the callback means: it tells the application its
 * Subscription is dead when the client is in the middle of bringing it back. The watchdog has to be
 * armed when Publish traffic is allowed to flow again, not when the Session becomes Active.
 *
 * <p>The recovery is held by the test rather than slowed down by a sleep: the Server does not
 * answer the Republish that begins the drain until the test lets it, so the length of the recovery
 * is a property of the test and not of anyone's timing.
 */
public class SubscriptionWatchdogRecoveryStarvationTest {

  /** Made explicit rather than relying on the default, because the delay below depends on it. */
  private static final double WATCHDOG_MULTIPLIER = 1.5;

  private static final double PUBLISHING_INTERVAL = 111.0;

  /** ceil(111 / 111) derives a MaxKeepAliveCount of 1. */
  private static final double TARGET_KEEP_ALIVE_INTERVAL = 111.0;

  private static final UInteger EXPECTED_MAX_KEEP_ALIVE_COUNT = uint(1);

  /** 111 * (1 + 1) * 1.5 — the watchdog delay implied by the revised parameters. */
  private static final long WATCHDOG_DELAY_MILLIS = 333;

  /**
   * How long the Republish drain is held, and therefore how long the recovery lasts: ~9x {@link
   * #WATCHDOG_DELAY_MILLIS}, so a watchdog armed anywhere in the recovery has ample time to fire.
   */
  private static final long RECOVERY_HOLD_MILLIS = 3_000;

  /**
   * The Session FSM waits one second in {@code ReactivatingWait} before its first re-activation
   * attempt, and doubles the wait on each failure; this window allows for several attempts.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 20_000;

  /** How long to wait for an expiry that must happen. */
  private static final long WATCHDOG_TIMEOUT_MILLIS = 10_000;

  /** How long to wait for something else that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /** Upper bound on how long the Republish drain is held, so nothing hangs indefinitely. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /**
   * A recovery that outlasts the watchdog delay must not fire the watchdog. Nothing has gone wrong:
   * the Subscription is alive, the Server is answering, and the reason no PublishResponse has
   * arrived is that the client is running the Republish loop Part 4 §6.7 puts ahead of resumed
   * Publish handling.
   */
  @Test
  void watchdogDoesNotElapseWhileTheReconnectRepublishDrainHoldsPublishSuspended()
      throws Exception {

    try (var fixture = new Fixture()) {
      // Answers the first Publish the client sends once the Subscription is created; every
      // subsequent Publish is parked.
      fixture.faultSession();

      var elapsedAfterReactivation = new CountDownLatch(1);
      OpcUaSubscription subscription = fixture.createSubscription(elapsedAfterReactivation);
      assertRevisedParameters(subscription);

      fixture.awaitReactivation();
      fixture.awaitRepublishDrainStarted();

      assertFalse(
          elapsedAfterReactivation.await(RECOVERY_HOLD_MILLIS, TimeUnit.MILLISECONDS),
          "the watchdog elapsed while the Part 4 §6.7 Republish drain was still running: no"
              + " PublishResponse can arrive while the client is holding Publish traffic shut for"
              + " that drain, so a watchdog armed when the Session became Active counts down"
              + " against a Subscription that is recovering normally and reports it dead after "
              + WATCHDOG_DELAY_MILLIS
              + "ms");
    }
  }

  /**
   * The control that keeps the assertion above from being vacuous: the same held recovery,
   * released. Once the drain is over the client may send PublishRequests again, the Server's
   * keep-alive promise applies again, and a Server that then stays silent must be reported — so the
   * watchdog has to be armed at that point, and this is the assertion that it is.
   */
  @Test
  void watchdogElapsesOnceTheHeldRepublishDrainLetsPublishResume() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.faultSession();

      var elapsedAfterReactivation = new CountDownLatch(1);
      OpcUaSubscription subscription = fixture.createSubscription(elapsedAfterReactivation);
      assertRevisedParameters(subscription);

      fixture.awaitReactivation();
      fixture.awaitRepublishDrainStarted();

      fixture.releaseRepublishDrain();

      assertTrue(
          elapsedAfterReactivation.await(WATCHDOG_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "control: the watchdog never elapsed although the Republish drain had finished, Publish"
              + " traffic was allowed to resume, and no PublishResponse followed; a watchdog that"
              + " is not armed when publishing resumes leaves the Subscription unsupervised for the"
              + " rest of its life");
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

  // region fixture

  /**
   * A running Server that parks every Publish request it is not scripted to answer and holds the
   * first Republish request until the test releases it, plus a connected client.
   */
  private static final class Fixture implements AutoCloseable {

    /** Counted down when the Republish that begins the reconnect drain reaches the Server. */
    private final CountDownLatch republishStarted = new CountDownLatch(1);

    /** Releases the held Republish; always counted down by {@link #close()}. */
    private final CountDownLatch republishGate = new CountDownLatch(1);

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    /**
     * {@code true} once the Session has become Active again. Only expiries that follow the return
     * to Active are counted: an expiry armed before the fault proves nothing about recovery, and
     * these tests must not be able to pass or fail on one.
     */
    private final AtomicBoolean reactivated = new AtomicBoolean(false);

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

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg.setRequestTimeout(uint(60_000))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are
                      // the ones it scripts.
                      .setKeepAliveInterval(uint(60_000)));
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
                reactivated.set(true);
                sessionReactivated.countDown();
              }
            }
          });

      scriptable.setRepublishResponder(this::respondToRepublish);
    }

    /**
     * Answer the next Publish request with a Bad_SessionIdInvalid ServiceFault, which {@code
     * SessionFsmFactory}'s SessionFaultListener classifies as a Session error and turns into a
     * reconnect. The Server-side Session is untouched, so re-activation succeeds and the
     * Subscription survives it.
     */
    void faultSession() {
      scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
    }

    /**
     * Create a Subscription whose watchdog delay is {@value #WATCHDOG_DELAY_MILLIS}ms and whose
     * listener counts down {@code elapsedAfterReactivation} for every expiry reported after the
     * Session has become Active again.
     *
     * <p>The listener is installed before {@code create()} so that no expiry can be missed by
     * having been reported before it was there.
     */
    OpcUaSubscription createSubscription(CountDownLatch elapsedAfterReactivation)
        throws UaException {

      var subscription = new OpcUaSubscription(client);
      subscription.setWatchdogMultiplier(WATCHDOG_MULTIPLIER);
      subscription.setPublishingInterval(PUBLISHING_INTERVAL);
      subscription.setTargetKeepAliveInterval(TARGET_KEEP_ALIVE_INTERVAL);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onWatchdogTimerElapsed(OpcUaSubscription s) {
              if (reactivated.get()) {
                elapsedAfterReactivation.countDown();
              }
            }
          });
      subscription.create();

      return subscription;
    }

    /**
     * Hold the Republish that begins the reconnect drain until {@link #releaseRepublishDrain()},
     * then answer it Bad_MessageNotAvailable — the termination condition of the Part 4 §6.7 loop,
     * since the Server is holding nothing for retransmission.
     */
    private RepublishResponse respondToRepublish(RepublishRequest request) throws UaException {
      republishStarted.countDown();

      try {
        if (!republishGate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
          throw new UaException(StatusCodes.Bad_Timeout);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }

      throw new UaException(StatusCodes.Bad_MessageNotAvailable);
    }

    void awaitReactivation() throws Exception {
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
      assertTrue(
          sessionReactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session never became Active again");
    }

    void awaitRepublishDrainStarted() throws Exception {
      assertTrue(
          republishStarted.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "precondition: the Republish drain Part 4 §6.7 requires never reached the Server, so"
              + " nothing is holding Publish traffic suspended and there is no recovery to"
              + " outlast");
    }

    void releaseRepublishDrain() {
      republishGate.countDown();
    }

    @Override
    public void close() throws Exception {
      republishGate.countDown();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }

  // endregion
}
