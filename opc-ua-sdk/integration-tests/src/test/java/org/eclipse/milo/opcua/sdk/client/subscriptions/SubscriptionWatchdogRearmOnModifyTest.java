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
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
 * The Subscription watchdog fires {@code onWatchdogTimerElapsed()} when no Publish response has
 * been seen for {@code revisedPublishingInterval * (revisedMaxKeepAliveCount + 1) *
 * watchdogMultiplier} milliseconds — i.e. when the Server has missed the keep-alive it promised in
 * its ModifySubscription/CreateSubscription response.
 *
 * <p>ModifySubscription is the only place besides create where those two parameters can change
 * (Part 4 §5.14.3), so {@code modify()} must re-arm the watchdog from the <i>revised</i> parameters
 * the Server just returned. Re-arming from the pre-modify parameters breaks the watchdog in both
 * directions: slowing the Subscription down makes it cry wolf long before the Server's next
 * keep-alive is due, and speeding it up leaves it armed for the old, longer delay so a genuinely
 * dead Subscription goes unreported.
 *
 * <p>Every Publish request is parked by {@link ScriptableSubscriptionServiceSet} and never
 * answered, because a Publish response is the other thing that re-arms the watchdog and would mask
 * the arming performed by {@code modify()}.
 */
public class SubscriptionWatchdogRearmOnModifyTest {

  /** Made explicit rather than relying on the default, because the delays below depend on it. */
  private static final double WATCHDOG_MULTIPLIER = 1.5;

  private static final double FAST_PUBLISHING_INTERVAL = 100.0;
  private static final double FAST_TARGET_KEEP_ALIVE_INTERVAL = 1_000.0;

  private static final double SLOW_PUBLISHING_INTERVAL = 2_000.0;
  private static final double SLOW_TARGET_KEEP_ALIVE_INTERVAL = 20_000.0;

  /**
   * ceil(1000 / 100) and ceil(20000 / 2000) both derive a MaxKeepAliveCount of 10, so the two
   * configurations below differ only in their PublishingInterval and the Server revises neither
   * count.
   */
  private static final UInteger EXPECTED_MAX_KEEP_ALIVE_COUNT = uint(10);

  /** 100 * (10 + 1) * 1.5 — the watchdog delay implied by the fast configuration. */
  private static final long FAST_WATCHDOG_DELAY_MILLIS = 1_650;

  /** 2000 * (10 + 1) * 1.5 — the watchdog delay implied by the slow configuration. */
  private static final long SLOW_WATCHDOG_DELAY_MILLIS = 33_000;

  /**
   * How long to watch for a watchdog expiry. Comfortably longer than {@link
   * #FAST_WATCHDOG_DELAY_MILLIS} and far shorter than {@link #SLOW_WATCHDOG_DELAY_MILLIS}, so the
   * two are never confused.
   */
  private static final long OBSERVATION_WINDOW_MILLIS = 6_000;

  /**
   * Modifying to a slower PublishingInterval must push the watchdog out. If the timer is re-armed
   * before the revised parameters are installed it stays armed for the old 1650 ms delay and
   * reports the Subscription dead ~31 seconds before the Server's next keep-alive is even due.
   */
  @Test
  void modifyToSlowerIntervalDoesNotElapseWatchdogAtThePreModifyInterval() throws Exception {
    try (var fixture = new Fixture()) {
      OpcUaSubscription subscription =
          fixture.createSubscription(FAST_PUBLISHING_INTERVAL, FAST_TARGET_KEEP_ALIVE_INTERVAL);

      assertRevisedParameters(subscription, FAST_PUBLISHING_INTERVAL);

      subscription.setPublishingInterval(SLOW_PUBLISHING_INTERVAL);
      subscription.setTargetKeepAliveInterval(SLOW_TARGET_KEEP_ALIVE_INTERVAL);
      subscription.modify();

      assertRevisedParameters(subscription, SLOW_PUBLISHING_INTERVAL);

      CountDownLatch elapsed = watchdogElapsedLatch(subscription);

      assertFalse(
          elapsed.await(OBSERVATION_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog elapsed within "
              + OBSERVATION_WINDOW_MILLIS
              + "ms, but the revised parameters put the next expiry "
              + SLOW_WATCHDOG_DELAY_MILLIS
              + "ms out; it was re-armed with the pre-modify delay of "
              + FAST_WATCHDOG_DELAY_MILLIS
              + "ms");
    }
  }

  /**
   * The mirror case, and the control that proves {@link
   * #modifyToSlowerIntervalDoesNotElapseWatchdogAtThePreModifyInterval()} is not vacuous: with the
   * watchdog correctly re-armed the expiry does arrive, and it arrives on the revised schedule.
   * Re-arming from the pre-modify parameters leaves it armed for the old 33 second delay, so a
   * Subscription that has gone silent is not reported for another half minute.
   */
  @Test
  void modifyToFasterIntervalElapsesWatchdogAtThePostModifyInterval() throws Exception {
    try (var fixture = new Fixture()) {
      OpcUaSubscription subscription =
          fixture.createSubscription(SLOW_PUBLISHING_INTERVAL, SLOW_TARGET_KEEP_ALIVE_INTERVAL);

      assertRevisedParameters(subscription, SLOW_PUBLISHING_INTERVAL);

      subscription.setPublishingInterval(FAST_PUBLISHING_INTERVAL);
      subscription.setTargetKeepAliveInterval(FAST_TARGET_KEEP_ALIVE_INTERVAL);
      subscription.modify();

      assertRevisedParameters(subscription, FAST_PUBLISHING_INTERVAL);

      CountDownLatch elapsed = watchdogElapsedLatch(subscription);

      assertTrue(
          elapsed.await(OBSERVATION_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog did not elapse within "
              + OBSERVATION_WINDOW_MILLIS
              + "ms, but the revised parameters put the expiry "
              + FAST_WATCHDOG_DELAY_MILLIS
              + "ms out; it is still armed for the pre-modify delay of "
              + SLOW_WATCHDOG_DELAY_MILLIS
              + "ms");
    }
  }

  /**
   * The watchdog delay is computed from the revised parameters, so the test's arithmetic is only
   * meaningful if the Server returned the parameters that were requested. Asserted before the
   * timing assertions so a Server that revises them fails loudly instead of producing a mystery
   * timeout.
   */
  private static void assertRevisedParameters(
      OpcUaSubscription subscription, double expectedPublishingInterval) {

    assertAll(
        () ->
            assertEquals(
                expectedPublishingInterval,
                subscription.getRevisedPublishingInterval().orElseThrow(),
                "revised PublishingInterval"),
        () ->
            assertEquals(
                EXPECTED_MAX_KEEP_ALIVE_COUNT,
                subscription.getRevisedMaxKeepAliveCount().orElseThrow(),
                "revised MaxKeepAliveCount"));
  }

  /**
   * Install a listener that counts down when the watchdog elapses.
   *
   * <p>Deliberately installed <i>after</i> {@code modify()}: the arming performed when the
   * Subscription was created may already have expired while the ModifySubscription round trip was
   * in flight, and that expiry is not the one under test. {@code notifyWatchdogTimerElapsed()}
   * reads the listener when the timer fires, so an expiry that predates this call cannot be
   * observed.
   */
  private static CountDownLatch watchdogElapsedLatch(OpcUaSubscription subscription) {
    var latch = new CountDownLatch(1);

    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onWatchdogTimerElapsed(OpcUaSubscription subscription) {
            latch.countDown();
          }
        });

    return latch;
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

    OpcUaSubscription createSubscription(double publishingInterval, double targetKeepAliveInterval)
        throws UaException {

      var subscription = new OpcUaSubscription(client);
      subscription.setWatchdogMultiplier(WATCHDOG_MULTIPLIER);
      subscription.setPublishingInterval(publishingInterval);
      subscription.setTargetKeepAliveInterval(targetKeepAliveInterval);
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
