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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
import org.junit.jupiter.api.Test;

/**
 * What the Part 4 §6.7 Republish drain is allowed to do on the client's <i>first</i> Session
 * activation.
 *
 * <p>The drain exists to collect the NotificationMessages a Server generated while a Session was
 * unusable. On the first activation there is no such window: the Session and every Subscription
 * registered against it were created moments earlier, on that Session. Running the drain anyway
 * spends a Republish round trip per Subscription to be told Bad_MessageNotAvailable, and because
 * the drain holds Publish traffic suspended until it ends, it also delays every Subscription's
 * first PublishRequest by that much.
 *
 * <p>The window this guards is reachable rather than theoretical: {@code connect()} returns when
 * the Session future completes, and that happens in a task submitted before the {@code
 * onSessionActive} fan-out — so a Subscription created immediately afterwards can register itself
 * before the callbacks run, and be included in a recovery snapshot that has nothing to recover.
 *
 * <p>Note on what these tests do and do not prove. They are a guard, not a reproduction: entering
 * that window requires the calling thread to beat the fan-out task, which it loses on an idle
 * machine, so this class passes against the code from before the drain was made conditional. What
 * demonstrated the defect was the whole integration-tests module in one JVM fork on two CPUs, where
 * the window opens often enough to produce a spurious Republish in {@code
 * PublishSequenceRecoveryTest} and {@code PublishResponseOrderingTest}, a reset publish ceiling in
 * {@code PublishCeilingRecoveryTest}, and — because a drain that is held holds the publish gate
 * with it — a Publish that is never sent in {@code SubscriptionWatchdogRecoveryStarvationTest}.
 * These assertions pin the intended behaviour so that a future change making recovery unconditional
 * again fails here deterministically instead of as a flake somewhere else.
 */
public class PublishFirstActivationRecoveryTest {

  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to watch for a Republish that must not happen. Generous relative to the round trip it
   * would take against a loopback Server.
   */
  private static final long QUIET_PERIOD_MILLIS = 2_000;

  /**
   * A Subscription created in the window between {@code connect()} returning and the activation
   * callbacks running must not be dragged through a drain that cannot find anything.
   */
  @Test
  void noRepublishIsRequestedOnTheFirstSessionActivation() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      // The client pipelines PublishRequests as soon as publishing is allowed, so their arrival is
      // the signal that the gate opened and any recovery is over.
      assertTrue(
          fixture.awaitPublishRequests(2),
          "no PublishRequest was sent, so the publish gate never opened: either recovery is still"
              + " running or it never finished");

      Thread.sleep(QUIET_PERIOD_MILLIS);

      assertEquals(
          List.of(),
          fixture.republishes(),
          "the Subscription was created on the Session that has just become Active for the first"
              + " time, so the Server cannot be holding a NotificationMessage the client has not"
              + " collected and the §6.7 drain must not ask for one");
    }
  }

  /**
   * The control that keeps the assertion above from being vacuous: once a Session has actually been
   * lost, the drain is exactly what §6.7 requires and must run.
   */
  @Test
  void republishIsRequestedAfterTheSessionIsReactivated() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      assertTrue(fixture.awaitPublishRequests(2), "the publish gate never opened");
      assertEquals(List.of(), fixture.republishes(), "premise: no drain on the first activation");

      // Bad_SessionIdInvalid is classified as a Session error and turned into a reconnect. The
      // Server-side Session is untouched, so re-activation succeeds and the Subscription survives.
      fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);

      assertTrue(
          fixture.sessionInactive.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
      assertTrue(
          fixture.sessionReactivated.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session never became Active again");

      assertTrue(
          fixture.awaitRepublish(),
          "the Session was lost and re-activated, so Part 4 §6.7 requires the client to Republish"
              + " from the next expected sequence number before resuming Publish handling, and it"
              + " asked for nothing");
    }
  }

  private static class Fixture implements AutoCloseable {

    final CountDownLatch sessionInactive = new CountDownLatch(1);
    final CountDownLatch sessionReactivated = new CountDownLatch(1);

    private final List<Long> republishes = new CopyOnWriteArrayList<>();
    private final CountDownLatch republishRequested = new CountDownLatch(1);

    private final OpcUaServer server;
    final OpcUaClient client;
    final ScriptableSubscriptionServiceSet scriptable;

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
                  // Long enough that a parked PublishRequest does not time out, and no Session
                  // keep-alive traffic competes with the requests these tests script.
                  cfg.setRequestTimeout(uint(60_000)).setKeepAliveInterval(uint(60_000)));

      scriptable.setRepublishResponder(
          request -> {
            republishes.add(request.getRetransmitSequenceNumber().longValue());
            republishRequested.countDown();

            // The termination condition of the §6.7 loop: the Server holds nothing to retransmit.
            throw new UaException(StatusCodes.Bad_MessageNotAvailable);
          });

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
    }

    List<Long> republishes() {
      return List.copyOf(republishes);
    }

    boolean awaitRepublish() throws InterruptedException {
      return republishRequested.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }

    boolean awaitPublishRequests(int count) throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

      while (System.nanoTime() < deadline) {
        if (scriptable.getPublishRequestCount() >= count) {
          return true;
        }
        Thread.sleep(25);
      }

      return scriptable.getPublishRequestCount() >= count;
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
