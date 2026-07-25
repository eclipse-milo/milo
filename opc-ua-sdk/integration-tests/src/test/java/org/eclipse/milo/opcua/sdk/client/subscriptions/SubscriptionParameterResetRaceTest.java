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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription.SyncState;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A parameter setter racing {@code OpcUaSubscription.reset()}.
 *
 * <p>The setters — {@code setPublishingInterval} and the rest — do a check-then-act: if the
 * Subscription exists on the Server they record the new value as a pending modification and mark
 * the Subscription {@code UNSYNCHRONIZED} so the next {@code modify()} sends it. Before 93bedb32c
 * they did that without {@code lifecycleLock}, while {@code reset()} — which clears the pending
 * modifications, drops the {@code ServerState} and returns the Subscription to {@code INITIAL} —
 * holds it throughout.
 *
 * <p>A setter that read the state before the reset and wrote it afterwards therefore left the
 * object in a state no legitimate sequence can produce: {@code UNSYNCHRONIZED} with no {@code
 * ServerState} at all. Every transition answers {@code Bad_InvalidState} there — {@code create()}
 * because the state is not {@code INITIAL}, {@code modify()} and {@code delete()} because there is
 * no {@code ServerState} to name a Subscription with — so the object is permanently unusable, and
 * the application's only recourse is to throw it away. {@code reset()} is not only an application
 * call: {@code PublishingManager} makes it when the Server reports Bad_Timeout for a Subscription
 * and the Session FSM makes it when a Subscription could not be transferred, so the losing side of
 * this race is ordinary SDK behavior racing an ordinary application call.
 *
 * <p>The setters now take the lock for exactly that check-then-act, so a setter either runs
 * entirely before the reset — and has its work discarded, which is correct — or entirely after it,
 * and finds the Subscription {@code INITIAL} and leaves it alone.
 *
 * <p><b>Why this is a stress loop and not a deterministic interleaving.</b> The window is the
 * setter's own read-modify-write, and a setter performs no I/O and takes no callback, so there is
 * no seam a test could park it in: the interleaving cannot be forced, only provoked. It is provoked
 * very efficiently, though, because the window is nearly the whole of the setter's body — a thread
 * calling it in a loop is inside the window a large fraction of the time — so a single {@code
 * reset()} lands in it with high probability, and {@link #ROUNDS} rounds make missing it {@link
 * #ROUNDS} times over the only way this test can pass without the fix. The bad state is also
 * sticky: once reached, every subsequent setter call re-marks it {@code UNSYNCHRONIZED}, so there
 * is no chance of the evidence being tidied away before the assertion runs. Everything is bounded —
 * {@link #ROUNDS} rounds, two round trips each, and daemon setter threads that are stopped and
 * joined before anything is asserted.
 */
public class SubscriptionParameterResetRaceTest {

  /**
   * How many create/race/reset rounds to run. Each is two round trips against a loopback Server, so
   * the whole test is a fraction of a second of network time.
   */
  private static final int ROUNDS = 40;

  /**
   * How many threads call the setter while the reset runs. More than one so that a reset landing in
   * a gap between one thread's iterations still lands inside another's window.
   */
  private static final int SETTER_THREADS = 2;

  /** How long to wait for the setter threads to start, and for them to stop when asked. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  private TestServer testServer;
  private OpcUaServer server;
  private OpcUaClient client;
  private OpcUaSubscription subscription;

  @BeforeEach
  void startClientAndServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();
    server.startup().get();

    client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(10_000)));
    client.connect();

    subscription = new OpcUaSubscription(client);
  }

  @AfterEach
  void stopClientAndServer() throws Exception {
    try {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } finally {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  /**
   * The defect: {@code reset()} racing {@code setPublishingInterval} must never leave the
   * Subscription {@code UNSYNCHRONIZED} with no SubscriptionId, and must always leave it usable.
   *
   * <p>Each round creates the Subscription, starts the setter threads, resets, stops the setter
   * threads, and asserts. The {@code create()} at the head of the next round is itself the
   * assertion that the previous round left the object usable rather than answering {@code
   * Bad_InvalidState} forever.
   */
  @Test
  void aParameterSetterRacingResetLeavesTheSubscriptionUsable() throws Exception {
    for (int round = 1; round <= ROUNDS; round++) {
      subscription.create();

      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      SetterThreads setters = SetterThreads.start(subscription);
      try {
        subscription.reset();
      } finally {
        setters.stopAndJoin();
      }

      assertEquals(
          SyncState.INITIAL,
          subscription.getSyncState(),
          "round "
              + round
              + ": reset() returned the Subscription to INITIAL and every setter call after it"
              + " found it there, so nothing may have re-marked it. A setter that read the state"
              + " before the reset and wrote it after has left it "
              + subscription.getSyncState()
              + " with SubscriptionId "
              + subscription.getSubscriptionId()
              + " — a state in which create(), modify() and delete() all answer Bad_InvalidState"
              + " and the object can never be used again");

      // Distinct from the SyncState assertion above: this is the combination that makes the object
      // unusable rather than merely mislabelled, and it is what every transition trips over.
      assertTrue(
          subscription.getSyncState() != SyncState.UNSYNCHRONIZED
              || subscription.getSubscriptionId().isPresent(),
          "round "
              + round
              + ": the Subscription reports UNSYNCHRONIZED with no SubscriptionId. There is no"
              + " Subscription for the pending modifications to be applied to, so modify() and"
              + " delete() answer Bad_InvalidState for want of a ServerState and create() answers"
              + " it because the state is not INITIAL");

      // reset() only discards the client's knowledge of the Subscription; the Server is still
      // running it and nothing the client object can do names it any more.
      client.deleteSubscriptions(List.of(subscriptionId));
    }

    // The Subscription must still be usable after every round, by the transitions themselves and
    // not
    // only by what getSyncState() reports.
    subscription.create();

    assertEquals(
        SyncState.SYNCHRONIZED,
        subscription.getSyncState(),
        "the Subscription must still be creatable after the races above");

    subscription.setPublishingInterval(500.0);
    subscription.modify();

    assertEquals(
        SyncState.SYNCHRONIZED,
        subscription.getSyncState(),
        "the Subscription must still be modifiable after the races above");

    subscription.delete();
  }

  // region fixture helpers

  /**
   * Daemon threads calling {@code setPublishingInterval} in a tight loop, so that a {@code reset()}
   * made while they run has a good chance of landing inside one of their check-then-act windows.
   *
   * <p>{@code setPublishingInterval} is used because it is the widest of the setters: with the
   * LifetimeCount and MaxKeepAliveCount derived from the interval (the default), it performs three
   * separate check-then-acts, one of its own and one in each of the setters it delegates to.
   */
  private static final class SetterThreads {

    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicLong iterations = new AtomicLong(0);
    private final List<Thread> threads = new ArrayList<>(SETTER_THREADS);

    static SetterThreads start(OpcUaSubscription subscription) throws Exception {
      var setters = new SetterThreads();

      for (int i = 0; i < SETTER_THREADS; i++) {
        // A different interval per thread, so no call is a no-op the JIT could hoist away.
        double publishingInterval = 100.0 + i;

        var thread =
            new Thread(
                () -> {
                  while (!setters.stopped.get()) {
                    subscription.setPublishingInterval(publishingInterval);
                    setters.iterations.incrementAndGet();
                  }
                },
                "parameter-setter-" + i);
        thread.setDaemon(true);

        setters.threads.add(thread);
        thread.start();
      }

      // The reset has to race a setter that is already running, not one that is still starting.
      assertTrue(
          setters.awaitIterations(SETTER_THREADS),
          "the setter threads never called setPublishingInterval, so nothing raced the reset()");

      return setters;
    }

    void stopAndJoin() throws InterruptedException {
      stopped.set(true);

      for (Thread thread : threads) {
        thread.join(AWAIT_TIMEOUT_MILLIS);
      }
    }

    private boolean awaitIterations(long count) throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

      while (System.nanoTime() < deadline) {
        if (iterations.get() >= count) {
          return true;
        }
        Thread.sleep(1);
      }

      return iterations.get() >= count;
    }
  }

  // endregion
}
