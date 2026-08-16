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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

/**
 * The Subscription watchdog is re-armed and cancelled from two unrelated threads: {@code
 * PublishingManager} re-arms it from the Publish completion handler (dispatched on the transport
 * executor), and the Session FSM cancels it from {@code onSessionInactive()} (dispatched on the
 * same executor, which is an unbounded cached pool, so the two run concurrently).
 *
 * <p>Those two entry points do not share a lock: {@code resetWatchdogTimer()} and {@code
 * cancelWatchdogTimer()} are {@code synchronized} on the Subscription, but the Session callbacks
 * call the {@code WatchdogTimer}'s own {@code reset()}/{@code cancel()} directly, and the {@code
 * AtomicReference} holding the scheduled future provides no mutual exclusion across the
 * read-cancel-schedule-store sequence a re-arm performs.
 *
 * <p>The invariant under test is the one an application depends on: once the watchdog has been
 * cancelled it must not fire. A spurious {@code onWatchdogTimerElapsed()} tells the application its
 * Subscription is dead when it is not, which is exactly what the callback exists to rule out.
 *
 * <p>The interleaving is <b>forced</b>, not raced: the client is given a {@link
 * GateScheduledExecutor} that suspends the re-arming thread inside {@code schedule()} — after it
 * has cancelled the current future but before it stores the new one — and the cancelling thread is
 * released into that window. This makes the failure deterministic instead of probabilistic.
 */
public class SubscriptionWatchdogCancelRaceTest {

  /** Made explicit rather than relying on the default, because the delay below depends on it. */
  private static final double WATCHDOG_MULTIPLIER = 1.5;

  private static final double PUBLISHING_INTERVAL = 111.0;

  /** ceil(111 / 111) derives a MaxKeepAliveCount of 1. */
  private static final double TARGET_KEEP_ALIVE_INTERVAL = 111.0;

  private static final UInteger EXPECTED_MAX_KEEP_ALIVE_COUNT = uint(1);

  /**
   * 111 * (1 + 1) * 1.5 — the watchdog delay implied by the revised parameters. Deliberately not a
   * round number: {@link GateScheduledExecutor} identifies watchdog tasks by their delay, and no
   * other client task is scheduled 333ms out.
   */
  private static final long WATCHDOG_DELAY_MILLIS = 333;

  /** How long to watch for an expiry that must not happen. ~9x {@link #WATCHDOG_DELAY_MILLIS}. */
  private static final long SPURIOUS_EXPIRY_WINDOW_MILLIS = 3_000;

  /** How long to wait for an expiry that must happen. */
  private static final long EXPIRY_TIMEOUT_MILLIS = 10_000;

  private static final long THREAD_TIMEOUT_MILLIS = 10_000;

  /**
   * A cancel that lands while a re-arm is in flight must still win: the watchdog was cancelled, so
   * nothing may fire afterwards.
   *
   * <p>Today the re-arming thread stores its new future after the cancelling thread has already
   * taken the old one out of the {@code AtomicReference}, so the new future survives a cancel that
   * happened after it was created and fires ~{@value #WATCHDOG_DELAY_MILLIS}ms later. When the
   * cancel comes from {@code cancelWatchdogTimer()} — the Session-fault path — the {@code
   * WatchdogTimer} is also unreachable by then, so nothing can ever cancel that future.
   */
  @Test
  void watchdogDoesNotElapseWhenCancelRacesWithReset() throws Exception {
    try (var fixture = new Fixture()) {
      OpcUaSubscription subscription = fixture.createSubscription();
      assertRevisedParameters(subscription);

      SessionActivityListener watchdog = watchdogListener(subscription);
      UaSession session = fixture.client.getSession();

      RecordingScheduledFuture armed = fixture.gate.onlyWatchdogFuture();

      // Thread 1: a Publish response arrived, so PublishingManager re-arms the watchdog. It is
      // suspended inside schedule(), after cancelling the armed future and before storing the new
      // one.
      var rearm = new Thread(subscription::resetWatchdogTimer, "watchdog-rearm");
      fixture.gate.gateNextScheduleFrom(rearm);
      rearm.start();

      assertTrue(
          fixture.gate.awaitGated(THREAD_TIMEOUT_MILLIS),
          "the re-arming thread never reached schedule()");

      // Thread 2: the Session left Active. This is the callback SessionFsmFactory dispatches, on
      // the object it dispatches it to, and it takes no lock on the Subscription.
      var cancel = new Thread(() -> watchdog.onSessionInactive(session), "watchdog-cancel");
      cancel.start();

      // The interleaving point, observed rather than slept for: the second cancel() of the armed
      // future is the cancelling thread having already emptied the AtomicReference. Bounded,
      // because an implementation that serializes the two threads will not get there until the
      // re-arm is released — and then there is no race and the assertion below simply holds.
      armed.awaitCancelCount(2, SPURIOUS_EXPIRY_WINDOW_MILLIS);

      // Installed now: the future armed at create() has been cancelled twice over and the re-arm's
      // future does not exist yet, so the only expiry this listener can observe is the one the
      // race leaks.
      var elapsed = new CountDownLatch(1);
      subscription.setSubscriptionListener(watchdogElapsedListener(elapsed));

      fixture.gate.release();
      joinAll(rearm, cancel);

      assertFalse(
          elapsed.await(SPURIOUS_EXPIRY_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog elapsed although it had been cancelled; the re-arm that was in flight stored a"
              + " future the cancel could no longer see, and the application is now told a healthy"
              + " Subscription is dead");
    }
  }

  /**
   * The control that proves {@link #watchdogDoesNotElapseWhenCancelRacesWithReset()} is not
   * vacuous: with the same gated re-arm and no cancel at all, the future the re-arm creates does
   * fire and the listener does see it.
   */
  @Test
  void watchdogElapsesAfterResetWhenItIsNotCancelled() throws Exception {
    try (var fixture = new Fixture()) {
      OpcUaSubscription subscription = fixture.createSubscription();
      assertRevisedParameters(subscription);

      var rearm = new Thread(subscription::resetWatchdogTimer, "watchdog-rearm");
      fixture.gate.gateNextScheduleFrom(rearm);
      rearm.start();

      assertTrue(
          fixture.gate.awaitGated(THREAD_TIMEOUT_MILLIS),
          "the re-arming thread never reached schedule()");

      var elapsed = new CountDownLatch(1);
      subscription.setSubscriptionListener(watchdogElapsedListener(elapsed));

      fixture.gate.release();
      joinAll(rearm);

      assertTrue(
          elapsed.await(EXPIRY_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog did not elapse although it was re-armed and no Publish response was ever"
              + " delivered");
    }
  }

  /**
   * The second control, and the one that isolates the race as the cause: the same two operations in
   * a defined order — the re-arm completes, then the Session goes inactive — do cancel the
   * watchdog. Only overlapping them loses the future.
   */
  @Test
  void watchdogDoesNotElapseWhenCancelFollowsACompletedReset() throws Exception {
    try (var fixture = new Fixture()) {
      OpcUaSubscription subscription = fixture.createSubscription();
      assertRevisedParameters(subscription);

      SessionActivityListener watchdog = watchdogListener(subscription);
      UaSession session = fixture.client.getSession();

      var rearm = new Thread(subscription::resetWatchdogTimer, "watchdog-rearm");
      fixture.gate.gateNextScheduleFrom(rearm);
      rearm.start();

      assertTrue(
          fixture.gate.awaitGated(THREAD_TIMEOUT_MILLIS),
          "the re-arming thread never reached schedule()");

      var elapsed = new CountDownLatch(1);
      subscription.setSubscriptionListener(watchdogElapsedListener(elapsed));

      fixture.gate.release();
      joinAll(rearm);

      watchdog.onSessionInactive(session);

      assertFalse(
          elapsed.await(SPURIOUS_EXPIRY_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
          "watchdog elapsed although it was cancelled after the re-arm had completed");
    }
  }

  private static OpcUaSubscription.SubscriptionListener watchdogElapsedListener(
      CountDownLatch elapsed) {

    return new OpcUaSubscription.SubscriptionListener() {
      @Override
      public void onWatchdogTimerElapsed(OpcUaSubscription subscription) {
        elapsed.countDown();
      }
    };
  }

  private static void joinAll(Thread... threads) throws InterruptedException {
    for (Thread thread : threads) {
      thread.join(THREAD_TIMEOUT_MILLIS);
      assertFalse(thread.isAlive(), thread.getName() + " did not finish");
    }
  }

  /**
   * The {@code WatchdogTimer} is a {@link SessionActivityListener} registered with the client and
   * is not reachable through any API. Reflection obtains the same reference the Session FSM holds
   * so the test can deliver the callback the FSM delivers, to the object it delivers it to;
   * everything exercised from there is production code on production state.
   */
  private static SessionActivityListener watchdogListener(OpcUaSubscription subscription)
      throws Exception {

    Field field = OpcUaSubscription.class.getDeclaredField("watchdogTimer");
    field.setAccessible(true);

    Object watchdogTimer = field.get(subscription);
    assertNotNull(watchdogTimer, "the Subscription has no watchdog timer");

    return (SessionActivityListener) watchdogTimer;
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

  /**
   * The client's {@code ScheduledExecutorService}, with two test affordances:
   *
   * <ul>
   *   <li>one nominated thread is suspended on its next {@code schedule()} call until the test
   *       releases it, which is what turns the race into a fixed interleaving;
   *   <li>futures scheduled at the watchdog delay are handed back wrapped so the test can observe
   *       cancellation of a specific future, and the first of them is announced so the fixture can
   *       wait for the timer to be armed.
   * </ul>
   *
   * Every other scheduling the client does — Session keep-alives, reconnect back-off — passes
   * straight through.
   */
  private static final class GateScheduledExecutor extends ScheduledThreadPoolExecutor {

    private final AtomicReference<Thread> gatedThread = new AtomicReference<>();
    private final CountDownLatch gated = new CountDownLatch(1);
    private final CountDownLatch released = new CountDownLatch(1);

    private final List<RecordingScheduledFuture> watchdogFutures = new CopyOnWriteArrayList<>();
    private final CountDownLatch watchdogArmed = new CountDownLatch(1);

    GateScheduledExecutor() {
      super(4);
    }

    void gateNextScheduleFrom(Thread thread) {
      gatedThread.set(thread);
    }

    boolean awaitGated(long timeoutMillis) throws InterruptedException {
      return gated.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /** Wait until a watchdog expiry has been scheduled, i.e. until the timer is armed. */
    boolean awaitWatchdogArmed(long timeoutMillis) throws InterruptedException {
      return watchdogArmed.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    void release() {
      released.countDown();
    }

    /** The single watchdog future armed when the Subscription was created. */
    RecordingScheduledFuture onlyWatchdogFuture() {
      assertEquals(1, watchdogFutures.size(), "expected exactly one armed watchdog future");

      return watchdogFutures.get(0);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      if (gatedThread.compareAndSet(Thread.currentThread(), null)) {
        gated.countDown();
        try {
          if (!released.await(THREAD_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            throw new IllegalStateException("gate was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      ScheduledFuture<?> future = super.schedule(command, delay, unit);

      if (unit.toMillis(delay) == WATCHDOG_DELAY_MILLIS) {
        var recording = new RecordingScheduledFuture(future);
        watchdogFutures.add(recording);
        watchdogArmed.countDown();
        return recording;
      }

      return future;
    }
  }

  /** A {@link ScheduledFuture} that counts the cancellation attempts made against it. */
  private static final class RecordingScheduledFuture implements ScheduledFuture<Object> {

    private final AtomicInteger cancelCount = new AtomicInteger(0);

    private final ScheduledFuture<?> delegate;

    private RecordingScheduledFuture(ScheduledFuture<?> delegate) {
      this.delegate = delegate;
    }

    /**
     * Wait until this future has been cancelled at least {@code count} times, or the timeout
     * expires. Returning early on timeout is intentional: it keeps a test that no longer produces
     * the interleaving from hanging.
     */
    void awaitCancelCount(int count, long timeoutMillis) throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      while (cancelCount.get() < count && System.nanoTime() < deadline) {
        TimeUnit.MILLISECONDS.sleep(1);
      }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      cancelCount.incrementAndGet();

      return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return delegate.getDelay(unit);
    }

    @Override
    public int compareTo(Delayed o) {
      return delegate.compareTo(o);
    }

    @Override
    public boolean isCancelled() {
      return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
      return delegate.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
      return delegate.get();
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {

      return delegate.get(timeout, unit);
    }
  }

  /**
   * A running Server whose Publish requests are all parked — no Publish response may re-arm the
   * watchdog behind the test's back — plus a client driven by a {@link GateScheduledExecutor}.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;
    private final GateScheduledExecutor gate = new GateScheduledExecutor();

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      // Long request timeout so the parked Publish requests do not time out during the test.
      client =
          TestClient.create(
              server,
              transportConfig -> transportConfig.setScheduledExecutor(gate),
              cfg -> cfg.setRequestTimeout(uint(60_000)));
      client.connect();
    }

    /**
     * Create the Subscription and return only once its watchdog timer is armed.
     *
     * <p>The barrier is not incidental. {@code resetWatchdogTimer()} is a no-op while {@code
     * PublishingManager.isPublishingSuspended()} holds, and it holds until the reconnect recovery
     * for the initial Session activation has resumed publishing. That recovery is an {@code
     * onSessionActive()} callback the Session FSM dispatches to the transport executor, on a
     * different thread from the one that completes the future {@code connect()} waits on, so {@code
     * connect()} can — and on a loaded machine occasionally does — return before it has run. {@code
     * create()} then defers its arm and the recovery arms the timer instead, a few milliseconds
     * later and from a thread the gate is not watching. Every test below needs the timer armed
     * before it nominates a thread, so wait for the arm itself rather than for the state that
     * permits it.
     */
    OpcUaSubscription createSubscription() throws Exception {
      var subscription = new OpcUaSubscription(client);
      subscription.setWatchdogMultiplier(WATCHDOG_MULTIPLIER);
      subscription.setPublishingInterval(PUBLISHING_INTERVAL);
      subscription.setTargetKeepAliveInterval(TARGET_KEEP_ALIVE_INTERVAL);
      subscription.create();

      assertTrue(
          gate.awaitWatchdogArmed(THREAD_TIMEOUT_MILLIS),
          "the watchdog timer was never armed after create()");

      return subscription;
    }

    @Override
    public void close() throws Exception {
      gate.release();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        try {
          server.shutdown().get(5, TimeUnit.SECONDS);
        } finally {
          gate.shutdownNow();
        }
      }
    }
  }
}
