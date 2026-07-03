/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Companion to {@link SecurityKeyManagerTest} covering the reschedule/retry/threading rows of the
 * key lifecycle (Part 14 §8.3.2, §6.2.12.2; plan pins K6, D15): re-learned
 * TimeToNextKey/KeyLifetime durations rescheduling the one-shot tasks, the min(KeyLifetime/2, 10 s)
 * retry cap, persistent refresh failure expiring held keys into Error — with the publisher snapshot
 * nulled BEFORE the failure transition (observed from inside the state change listener), so no
 * further sends happen, and the material zeroized only after the retirement grace so no in-flight
 * borrow races the wipe — a synchronous provider throw as a failed fetch, the provider-thread hop
 * (a future completing on a foreign thread mutates nothing until the pinned scheduler runs), and
 * shutdown.
 *
 * <p>Deterministic: cadence is driven through the injectable clock + one-shot scheduler seams — no
 * sleeping, no real timers, and the only real thread used is join()ed before any assertion.
 */
class SecurityKeyManagerRescheduleAndRetryTest {

  private static final String AES128_URI = PubSubSecurityPolicy.PubSubAes128Ctr.getUri();

  private static final SecurityGroupRef REF = new SecurityGroupRef("SG");
  private static final String GROUP_PATH = "conn/WG";

  private final AtomicLong clock = new AtomicLong(0);
  private final TestScheduler scheduler = new TestScheduler(clock);

  /** Key material a test wants observed at Error-transition time; see {@link #onStateChange}. */
  private SecurityKeyManager.@Nullable PublisherKeys watchedKeys;

  private final List<ErrorObservation> errorObservations = new ArrayList<>();

  private final PubSubStateMachine stateMachine =
      new PubSubStateMachine(new Object(), this::onStateChange);

  private final EventDispatcher eventDispatcher = new EventDispatcher(Runnable::run);
  private final DiagnosticsCollector diagnostics = new DiagnosticsCollector(eventDispatcher);

  private final ScriptedProvider provider = new ScriptedProvider();

  private final SecurityKeyManager manager =
      new SecurityKeyManager(
          diagnostics,
          stateMachine,
          Map.of(REF, provider),
          scheduler,
          clock::get,
          () -> new byte[] {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD});

  // region fixtures

  /** What was observable at the instant an Error transition ran (listener fires inside fail()). */
  private record ErrorObservation(
      StatusCode statusCode,
      boolean publisherSnapshotWasNull,
      boolean subscriberWindowWasNull,
      boolean watchedKeysWereDestroyed) {}

  private void onStateChange(
      AbstractComponentRuntime component,
      PubSubState oldState,
      PubSubState newState,
      StatusCode statusCode) {

    if (newState == PubSubState.Error) {
      SecurityKeyManager.PublisherKeys watched = watchedKeys;
      errorObservations.add(
          new ErrorObservation(
              statusCode,
              manager.currentPublisherKeys(REF) == null,
              manager.subscriberKeyWindow(REF) == null,
              watched != null && watched.keys().isDestroyed()));
    }
  }

  private static final class TestComponent extends AbstractComponentRuntime {

    TestComponent(String path) {
      super(ComponentType.WRITER_GROUP, path, null, true);
    }

    @Override
    List<? extends AbstractComponentRuntime> children() {
      return List.of();
    }

    @Override
    boolean startupCompletesImmediately() {
      // secured groups defer startup completion to the key manager's first fetch
      return false;
    }
  }

  private static final class ScriptedProvider implements SecurityKeyProvider {

    record Request(String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {}

    final List<Request> requests = new ArrayList<>();
    final List<CompletableFuture<SecurityKeySet>> futures = new ArrayList<>();

    @Nullable UaException autoFail;
    @Nullable RuntimeException throwSynchronously;

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      requests.add(new Request(securityGroupId, startingTokenId, requestedKeyCount));
      if (throwSynchronously != null) {
        throw throwSynchronously;
      }
      var future = new CompletableFuture<SecurityKeySet>();
      if (autoFail != null) {
        future.completeExceptionally(autoFail);
      }
      futures.add(future);
      return future;
    }
  }

  private static SecurityGroupConfig securityGroup(Duration keyLifeTime) {
    return SecurityGroupConfig.builder("SG").keyLifeTime(keyLifeTime).build();
  }

  private static MessageSecurityConfig signConfig() {
    return MessageSecurityConfig.builder()
        .mode(MessageSecurityMode.Sign)
        .securityGroup(REF)
        .build();
  }

  private TestComponent attachPublisher(SecurityGroupConfig securityGroup) throws UaException {
    var component = new TestComponent(GROUP_PATH);
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));
    assertEquals(PubSubState.PreOperational, component.state());

    manager.attachPublisher(component, signConfig(), securityGroup);
    return component;
  }

  private static ByteString keyData(int seed) {
    byte[] bytes = new byte[52]; // PubSub-Aes128-CTR key data length
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (seed + i);
    }
    return ByteString.of(bytes);
  }

  private static SecurityKeySet keySet(
      long firstTokenId, int count, Duration timeToNextKey, Duration keyLifetime) {

    var keys = new ArrayList<ByteString>();
    for (int i = 0; i < count; i++) {
      keys.add(keyData((int) (firstTokenId + i) * 7));
    }
    return new SecurityKeySet(AES128_URI, uint(firstTokenId), keys, timeToNextKey, keyLifetime);
  }

  private static long seconds(long value) {
    return TimeUnit.SECONDS.toNanos(value);
  }

  private static long millis(long value) {
    return TimeUnit.MILLISECONDS.toNanos(value);
  }

  private long currentTokenId() {
    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);
    return keys.tokenId();
  }

  private ComponentDiagnostics groupDiagnostics() {
    ComponentDiagnostics component = diagnostics.snapshot().get(GROUP_PATH);
    assertNotNull(component);
    return component;
  }

  // endregion

  @Test
  void relearnedDurationsRescheduleRefreshSwitchAndLifetime() throws Exception {
    attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    // t=30: the refresh (KeyLifetime/2) fires and the TimeToNextKey switch rotates to token 2
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());
    assertEquals(2, currentTokenId());

    // the response re-learns much shorter durations: TimeToNextKey 10 s, KeyLifetime 20 s
    provider.futures.get(1).complete(keySet(2, 3, Duration.ofSeconds(10), Duration.ofSeconds(20)));
    scheduler.runPending();

    // the pending switch (was t=90) and refresh (was t=60) were rescheduled from the response:
    // nothing fires at the old cadence...
    scheduler.advanceTo(seconds(39));
    assertEquals(2, provider.requests.size());
    assertEquals(2, currentTokenId());

    // ...the switch fires at the re-learned TimeToNextKey (30+10) and the refresh at the
    // re-learned KeyLifetime/2 (30+10)
    scheduler.advanceTo(seconds(40));
    assertEquals(3, provider.requests.size());
    assertEquals(3, currentTokenId());

    // subsequent switches every re-learned KeyLifetime (20 s)
    scheduler.advanceTo(seconds(59));
    assertEquals(3, currentTokenId());
    scheduler.advanceTo(seconds(60));
    assertEquals(4, currentTokenId());
  }

  @Test
  void refreshRetryDelayIsCappedAtTenSeconds() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    provider.autoFail = new UaException(StatusCodes.Bad_CommunicationError, "SKS unreachable");

    // the refresh at KeyLifetime/2 (30 s) fails; retries follow at min(KeyLifetime/2, 10 s) = 10 s
    // — faster than the 60 s key lifetime (§5.4.5.3)
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());
    scheduler.advanceTo(seconds(39));
    assertEquals(2, provider.requests.size());
    scheduler.advanceTo(seconds(40));
    assertEquals(3, provider.requests.size());
    scheduler.advanceTo(seconds(50));
    assertEquals(4, provider.requests.size());

    // fetch failures alone never fail a group that still holds valid keys
    assertEquals(PubSubState.Operational, component.state());
    assertNotNull(manager.currentPublisherKeys(REF));
    assertEquals(
        new StatusCode(StatusCodes.Bad_CommunicationError), groupDiagnostics().lastError());
  }

  @Test
  void persistentRefreshFailureExpiresHeldKeysIntoErrorThenRecovers() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    // a single key, no futures: the window ends when TimeToNextKey elapses at t=30
    provider.futures.get(0).complete(keySet(1, 1, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());

    watchedKeys = manager.currentPublisherKeys(REF);
    assertNotNull(watchedKeys);

    provider.autoFail = new UaException(StatusCodes.Bad_CommunicationError, "SKS unreachable");

    // retries run from t=30 every 10 s; expiry = window end (30 s) + 2×KeyLifetime (120 s) = 150 s
    scheduler.advanceTo(seconds(149));
    assertEquals(PubSubState.Operational, component.state());
    assertTrue(provider.requests.size() > 2, "expected 10 s retries before the expiry");

    scheduler.advanceTo(seconds(150));
    assertEquals(PubSubState.Error, component.state());

    // D15: expiry fails with Bad_SecurityChecksFailed...
    assertEquals(1, errorObservations.size());
    ErrorObservation observation = errorObservations.get(0);
    assertEquals(new StatusCode(StatusCodes.Bad_SecurityChecksFailed), observation.statusCode());
    // ...and by the time the transition ran, the snapshots were ALREADY nulled — that is what
    // stops sends: an in-flight publish cycle resolves null and skips. The material itself is
    // only retired at that point (zeroized after the destroy grace), so a cycle that resolved
    // its context just before the expiry never races the wipe (S3)
    assertTrue(observation.publisherSnapshotWasNull());
    assertTrue(observation.subscriberWindowWasNull());
    assertFalse(observation.watchedKeysWereDestroyed());
    assertNull(manager.currentPublisherKeys(REF));

    // the fetch loop keeps retrying after the failure
    int requestsAtExpiry = provider.requests.size();
    scheduler.advanceTo(seconds(170));
    assertTrue(provider.requests.size() > requestsAtExpiry);

    // the next success reinstalls and recovers the group; by t=180 the destroy grace of the
    // material retired at expiry (t=150) has also elapsed: it is zeroized now
    provider.autoFail = null;
    scheduler.advanceTo(seconds(180));
    SecurityKeyManager.PublisherKeys watched = watchedKeys;
    assertNotNull(watched);
    assertTrue(watched.keys().isDestroyed());
    provider
        .futures
        .get(provider.futures.size() - 1)
        .complete(keySet(9, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());
    assertEquals(9, currentTokenId());
    assertNotNull(manager.subscriberKeyWindow(REF));
  }

  @Test
  void synchronousProviderThrowIsAFailedFetchWithRetries() throws Exception {
    provider.throwSynchronously = new IllegalStateException("provider exploded");

    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(1)));
    scheduler.runPending();
    assertEquals(1, provider.requests.size());
    assertEquals(PubSubState.PreOperational, component.state());

    // the throw is a failed fetch: retries at configuredKeyLifetime/2 = 500 ms until the
    // 2×KeyLifetime startup deadline fails the group (D15: a plain RuntimeException carries no
    // status, so the extracted fetch status defaults to Bad_InternalError)
    scheduler.advanceTo(millis(1_999));
    assertEquals(PubSubState.PreOperational, component.state());
    assertTrue(provider.requests.size() > 1, "expected retries before the startup deadline");

    scheduler.advanceTo(millis(2_000));
    assertEquals(PubSubState.Error, component.state());
    assertEquals(1, errorObservations.size());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), errorObservations.get(0).statusCode());

    // the loop is still retrying; the first success recovers from the manager-imposed failure
    provider.throwSynchronously = null;
    scheduler.advanceTo(millis(2_500));
    provider
        .futures
        .get(provider.futures.size() - 1)
        .complete(keySet(1, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());
  }

  @Test
  void foreignThreadCompletionMutatesNothingUntilThePinnedSchedulerRuns() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(1, provider.futures.size());

    // the provider completes on its own thread: the manager must hop to the pinned scheduler
    // (whenCompleteAsync) instead of running state mutation on the foreign thread
    var thread =
        new Thread(
            () ->
                provider
                    .futures
                    .get(0)
                    .complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60))));
    thread.start();
    thread.join();

    // nothing observable changed yet: the completion only enqueued the scheduler hop
    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.currentPublisherKeys(REF));
    assertNull(manager.subscriberKeyWindow(REF));
    assertTrue(scheduler.hasPendingTasks());

    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());
    assertNotNull(manager.currentPublisherKeys(REF));
  }

  @Test
  void shutdownRetiresMaterialCancelsTasksAndIgnoresLaterEvents() throws Exception {
    attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);

    manager.shutdown();

    assertNull(manager.currentPublisherKeys(REF));
    assertNull(manager.subscriberKeyWindow(REF));
    assertNull(manager.view(REF));

    // the material is retired at shutdown and zeroized once the destroy grace elapses: an
    // in-flight cycle that resolved its context before shutdown never encodes with wiped keys
    assertFalse(keys.keys().isDestroyed());
    scheduler.advanceTo(SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(keys.keys().isDestroyed());

    // the deferred destroys were the only tasks left: every lifecycle task was cancelled
    assertFalse(scheduler.hasPendingTasks());

    // no fetch loop survives, and no event resurrects one
    manager.onUnknownToken(REF, 99);
    manager.onForceKeyReset(REF);
    scheduler.advanceTo(seconds(1_000));
    assertEquals(1, provider.requests.size());

    // attach after shutdown is a no-op
    var late = new TestComponent("conn/late");
    diagnostics.register(late.path());
    stateMachine.setRootOperational(true, List.of(late));
    manager.attachPublisher(late, signConfig(), securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(1, provider.requests.size());
    assertFalse(scheduler.hasPendingTasks());
  }

  // region deterministic scheduler

  /** See {@link SecurityKeyManagerTest}: a manually advanced one-shot-only scheduler. */
  private static final class TestScheduler extends AbstractExecutorService
      implements ScheduledExecutorService {

    private final AtomicLong clock;
    private final PriorityQueue<Task> tasks =
        new PriorityQueue<>(
            Comparator.comparingLong((Task task) -> task.timeNanos)
                .thenComparingLong(task -> task.sequence));

    private long nextSequence = 0;

    TestScheduler(AtomicLong clock) {
      this.clock = clock;
    }

    /** Advance the clock to {@code nanos}, running every due task (including tasks they spawn). */
    void advanceTo(long nanos) {
      while (true) {
        Task next = tasks.peek();
        if (next == null || next.timeNanos > nanos) {
          break;
        }
        tasks.poll();
        if (next.cancelled) {
          continue;
        }
        clock.set(Math.max(clock.get(), next.timeNanos));
        next.command.run();
      }
      clock.set(Math.max(clock.get(), nanos));
    }

    /** Run everything already due at the current clock. */
    void runPending() {
      advanceTo(clock.get());
    }

    boolean hasPendingTasks() {
      return tasks.stream().anyMatch(task -> !task.cancelled);
    }

    @Override
    public void execute(Runnable command) {
      schedule(command, 0, TimeUnit.NANOSECONDS);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      var task = new Task(clock.get() + unit.toNanos(delay), nextSequence++, command);
      tasks.add(task);
      return task;
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(
        Runnable command, long initialDelay, long period, TimeUnit unit) {
      throw new UnsupportedOperationException(
          "the key manager must not use fixed-rate scheduling (K6: one-shot + reschedule)");
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException(
          "the key manager must not use fixed-delay scheduling (K6: one-shot + reschedule)");
    }

    @Override
    public void shutdown() {}

    @Override
    public List<Runnable> shutdownNow() {
      return List.of();
    }

    @Override
    public boolean isShutdown() {
      return false;
    }

    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return true;
    }

    private static final class Task implements ScheduledFuture<Object> {

      final long timeNanos;
      final long sequence;
      final Runnable command;

      volatile boolean cancelled = false;

      private Task(long timeNanos, long sequence, Runnable command) {
        this.timeNanos = timeNanos;
        this.sequence = sequence;
        this.command = command;
      }

      @Override
      public long getDelay(TimeUnit unit) {
        return unit.convert(timeNanos, TimeUnit.NANOSECONDS);
      }

      @Override
      public int compareTo(Delayed other) {
        return Long.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        cancelled = true;
        return true;
      }

      @Override
      public boolean isCancelled() {
        return cancelled;
      }

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public Object get() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object get(long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
      }
    }
  }

  // endregion
}
