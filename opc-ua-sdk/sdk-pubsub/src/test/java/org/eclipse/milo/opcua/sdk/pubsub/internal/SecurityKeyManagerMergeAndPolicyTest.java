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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
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
 * Companion to {@link SecurityKeyManagerTest} covering the remaining FirstTokenId merge rows (Part
 * 14 §8.3.2 / K6), the K8 policy-precedence rows, and the unknown-token trigger edges: a connected
 * response ahead of the local current token advances the window (evicted material retired and
 * zeroized after the destroy grace, C4), a response still reporting the pre-switch token dedups
 * without discarding, a disconnected FirstTokenId behind the window discards and replaces, a null
 * configured policy URI accepts any supported policy, {@code MessageSecurityConfig} takes
 * precedence over {@code SecurityGroupConfig} and conflicting pins fail every fetch, a zero
 * KeyLifetime with a non-zero TimeToNextKey is malformed, and an unknown-token burst collapses into
 * ONE StartingTokenId-0 fetch.
 *
 * <p>Deterministic: cadence is driven through the injectable clock + one-shot scheduler seams — no
 * sleeping, no real timers.
 */
class SecurityKeyManagerMergeAndPolicyTest {

  private static final String AES128_URI = PubSubSecurityPolicy.PubSubAes128Ctr.getUri();
  private static final String AES256_URI = PubSubSecurityPolicy.PubSubAes256Ctr.getUri();

  private static final SecurityGroupRef REF = new SecurityGroupRef("SG");
  private static final String GROUP_PATH = "conn/WG";

  private final AtomicLong clock = new AtomicLong(0);
  private final TestScheduler scheduler = new TestScheduler(clock);

  private final PubSubStateMachine stateMachine =
      new PubSubStateMachine(new Object(), (component, oldState, newState, statusCode) -> {});

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

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      requests.add(new Request(securityGroupId, startingTokenId, requestedKeyCount));
      var future = new CompletableFuture<SecurityKeySet>();
      futures.add(future);
      return future;
    }
  }

  private static SecurityGroupConfig securityGroup(Duration keyLifeTime) {
    return SecurityGroupConfig.builder("SG").keyLifeTime(keyLifeTime).build();
  }

  private static MessageSecurityConfig securityConfig(@Nullable String policyUri) {
    MessageSecurityConfig.Builder builder =
        MessageSecurityConfig.builder().mode(MessageSecurityMode.Sign).securityGroup(REF);
    if (policyUri != null) {
      builder.securityPolicyUri(policyUri);
    }
    return builder.build();
  }

  private TestComponent attachPublisher(SecurityGroupConfig securityGroup) throws UaException {
    var component = new TestComponent(GROUP_PATH);
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));
    assertEquals(PubSubState.PreOperational, component.state());

    manager.attachPublisher(component, securityConfig(null), securityGroup);
    return component;
  }

  private TestComponent attachSubscriber(SecurityGroupConfig securityGroup) throws UaException {
    var component = new TestComponent(GROUP_PATH);
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));
    assertEquals(PubSubState.PreOperational, component.state());

    manager.attachSubscriber(component, securityConfig(null), securityGroup);
    return component;
  }

  private static ByteString keyData(int seed, int length) {
    byte[] bytes = new byte[length];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (seed + i);
    }
    return ByteString.of(bytes);
  }

  private static SecurityKeySet keySet(
      long firstTokenId, int count, Duration timeToNextKey, Duration keyLifetime) {

    return keySet(AES128_URI, firstTokenId, count, timeToNextKey, keyLifetime);
  }

  private static SecurityKeySet keySet(
      String policyUri,
      long firstTokenId,
      int count,
      Duration timeToNextKey,
      Duration keyLifetime) {

    // 52 B for PubSub-Aes128-CTR, 68 B for PubSub-Aes256-CTR (Part 14 Table 155)
    int keyDataLength = AES256_URI.equals(policyUri) ? 68 : 52;
    var keys = new ArrayList<ByteString>();
    for (int i = 0; i < count; i++) {
      keys.add(keyData((int) (firstTokenId + i) * 7, keyDataLength));
    }
    return new SecurityKeySet(policyUri, uint(firstTokenId), keys, timeToNextKey, keyLifetime);
  }

  private static long seconds(long value) {
    return TimeUnit.SECONDS.toNanos(value);
  }

  private ComponentDiagnostics groupDiagnostics() {
    ComponentDiagnostics component = diagnostics.snapshot().get(GROUP_PATH);
    assertNotNull(component);
    return component;
  }

  // endregion

  @Test
  void connectedResponseAheadOfTheCurrentTokenAdvancesTheWindow() throws Exception {
    attachPublisher(securityGroup(Duration.ofHours(2)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    SecurityKeyManager.PublisherKeys token1 = manager.currentPublisherKeys(REF);
    assertNotNull(token1);
    token1.nonceSupplier().nextNonce(); // consume sequence 1 on token 1

    SecurityKeyManager.SubscriberKeyWindow before = manager.subscriberKeyWindow(REF);
    assertNotNull(before);
    SecurityKeyMaterial key1 = before.keyFor(1);
    SecurityKeyMaterial key2 = before.keyFor(2);
    SecurityKeyMaterial key3 = before.keyFor(3);
    assertNotNull(key1);
    assertNotNull(key2);
    assertNotNull(key3);

    // the SKS moved on: its current token is 3 — still connected (inside the local window)
    manager.onUnknownToken(REF, 5);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());
    provider.futures.get(1).complete(keySet(3, 3, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    // the window advanced to the provider's current token: {prev=2, current=3, futures=[4, 5]}
    SecurityKeyManager.SubscriberKeyWindow after = manager.subscriberKeyWindow(REF);
    assertNotNull(after);
    assertEquals(3, after.currentTokenId());
    assertEquals(2, after.previousTokenId());
    assertNull(after.keyFor(1));
    assertSame(key2, after.keyFor(2));
    assertSame(key3, after.keyFor(3)); // dedup kept the EXISTING token-3 entry
    assertNotNull(after.keyFor(4));
    assertNotNull(after.keyFor(5));
    assertNull(after.keyFor(6));

    // token 1 left the {prev, current, futures} window: retired (C4), zeroized only after the
    // destroy grace so an in-flight decode never races the wipe; the rest is live
    assertFalse(key1.isDestroyed());
    scheduler.advanceTo(clock.get() + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(key1.isDestroyed());
    assertFalse(key2.isDestroyed());
    assertFalse(key3.isDestroyed());

    // the merge-induced switch rebuilt the publisher snapshot: token 3, nonce counter reset to 1
    SecurityKeyManager.PublisherKeys token3 = manager.currentPublisherKeys(REF);
    assertNotNull(token3);
    assertNotSame(token1, token3);
    assertEquals(3, token3.tokenId());
    assertEquals(1, token3.nonceCounter().get());
  }

  @Test
  void responseReportingThePreSwitchTokenMergesWithoutDiscarding() throws Exception {
    attachSubscriber(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow initial = manager.subscriberKeyWindow(REF);
    assertNotNull(initial);
    SecurityKeyMaterial key1 = initial.keyFor(1);
    SecurityKeyMaterial key2 = initial.keyFor(2);
    SecurityKeyMaterial key3 = initial.keyFor(3);
    assertNotNull(key1);
    assertNotNull(key2);
    assertNotNull(key3);

    // t=30: the refresh fetch goes out just before the local switch rotates to token 2
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());

    // the response still reports the pre-switch token 1 as current: it connects through the
    // retained previous token and must NOT discard the window (§8.3.2 overlap tolerance)
    provider.futures.get(1).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow merged = manager.subscriberKeyWindow(REF);
    assertNotNull(merged);
    assertEquals(2, merged.currentTokenId());
    assertEquals(1, merged.previousTokenId());
    assertSame(key1, merged.keyFor(1));
    assertSame(key2, merged.keyFor(2));
    assertSame(key3, merged.keyFor(3));
    assertFalse(key1.isDestroyed());

    // the re-learned TimeToNextKey (30 s from the response at t=30) drives the next switch
    scheduler.advanceTo(seconds(59));
    SecurityKeyManager.SubscriberKeyWindow beforeSwitch = manager.subscriberKeyWindow(REF);
    assertNotNull(beforeSwitch);
    assertEquals(2, beforeSwitch.currentTokenId());

    scheduler.advanceTo(seconds(60));
    SecurityKeyManager.SubscriberKeyWindow afterSwitch = manager.subscriberKeyWindow(REF);
    assertNotNull(afterSwitch);
    assertEquals(3, afterSwitch.currentTokenId());

    // token 1 finally left the window: retired, zeroized after the destroy grace
    assertFalse(key1.isDestroyed());
    scheduler.advanceTo(seconds(60) + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(key1.isDestroyed());
  }

  @Test
  void responseBehindTheWindowDiscardsAndReplacesIt() throws Exception {
    attachSubscriber(securityGroup(Duration.ofHours(2)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(5, 3, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow before = manager.subscriberKeyWindow(REF);
    assertNotNull(before);
    SecurityKeyMaterial key5 = before.keyFor(5);
    SecurityKeyMaterial key6 = before.keyFor(6);
    SecurityKeyMaterial key7 = before.keyFor(7);
    assertNotNull(key5);
    assertNotNull(key6);
    assertNotNull(key7);

    // FirstTokenId 2 does not connect to {5, 6, 7}: discard and replace (K6/§8.3.2)
    manager.onUnknownToken(REF, 2);
    scheduler.runPending();
    provider.futures.get(1).complete(keySet(2, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow replaced = manager.subscriberKeyWindow(REF);
    assertNotNull(replaced);
    assertEquals(2, replaced.currentTokenId());
    assertNotNull(replaced.keyFor(2));
    assertNotNull(replaced.keyFor(3));
    assertNull(replaced.keyFor(5));

    // every entry of the discarded window was retired (C4): zeroized after the destroy grace
    assertFalse(key5.isDestroyed());
    assertFalse(key6.isDestroyed());
    assertFalse(key7.isDestroyed());
    scheduler.advanceTo(clock.get() + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(key5.isDestroyed());
    assertTrue(key6.isDestroyed());
    assertTrue(key7.isDestroyed());
  }

  @Test
  void nullConfiguredPolicyUriAcceptsAnySupportedPolicy() throws Exception {
    // neither the group MessageSecurityConfig nor the SecurityGroup pins a policy URI (K8)
    TestComponent component = attachSubscriber(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider
        .futures
        .get(0)
        .complete(keySet(AES256_URI, 1, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());

    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr, window.currentKey().getPolicy());

    SecurityKeyManager.SecurityGroupKeyView view = manager.view(REF);
    assertNotNull(view);
    assertEquals(AES256_URI, view.securityPolicyUri());
  }

  @Test
  void messageSecurityPolicyUriTakesPrecedenceOverTheSecurityGroupUri() throws Exception {
    // the group's MessageSecurityConfig pins Aes128; the SecurityGroup itself says Aes256
    SecurityGroupConfig securityGroup =
        SecurityGroupConfig.builder("SG")
            .keyLifeTime(Duration.ofSeconds(60))
            .securityPolicyUri(AES256_URI)
            .build();

    var component = new TestComponent(GROUP_PATH);
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));
    manager.attachPublisher(component, securityConfig(AES128_URI), securityGroup);
    scheduler.runPending();

    // a response matching the MessageSecurityConfig pin is accepted: it overrides the group URI
    provider
        .futures
        .get(0)
        .complete(keySet(AES128_URI, 1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());

    SecurityKeyManager.SecurityGroupKeyView view = manager.view(REF);
    assertNotNull(view);
    assertEquals(AES128_URI, view.securityPolicyUri());

    // a refresh response that switches to another policy is a failed fetch (K8: never swap)
    scheduler.advanceTo(seconds(2));
    manager.onUnknownToken(REF, 99);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());
    provider
        .futures
        .get(1)
        .complete(keySet(AES256_URI, 1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());
    assertEquals(
        new StatusCode(StatusCodes.Bad_ConfigurationError), groupDiagnostics().lastError());
    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr, window.currentKey().getPolicy());
  }

  @Test
  void conflictingPinnedUrisAcrossAttachmentsFailEveryFetch() throws Exception {
    SecurityGroupConfig securityGroup = securityGroup(Duration.ofSeconds(60));

    var publisher = new TestComponent("conn/WG");
    var subscriber = new TestComponent("conn/RG");
    diagnostics.register(publisher.path());
    diagnostics.register(subscriber.path());
    stateMachine.setRootOperational(true, List.of(publisher, subscriber));

    manager.attachPublisher(publisher, securityConfig(AES128_URI), securityGroup);
    manager.attachSubscriber(subscriber, securityConfig(AES256_URI), securityGroup);
    scheduler.runPending();
    assertEquals(1, provider.requests.size()); // one shared fetch loop per SecurityGroupRef

    // an Aes128 response mismatches the subscriber's pin: FAILED for everyone (K8, no downgrade)
    provider
        .futures
        .get(0)
        .complete(keySet(AES128_URI, 1, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.PreOperational, publisher.state());
    assertEquals(PubSubState.PreOperational, subscriber.state());
    assertNull(manager.subscriberKeyWindow(REF));

    // ...and the Aes256 retry response mismatches the publisher's pin: FAILED again
    scheduler.advanceTo(seconds(10)); // retry at min(configuredKeyLifetime/2, 10 s)
    assertEquals(2, provider.requests.size());
    provider
        .futures
        .get(1)
        .complete(keySet(AES256_URI, 1, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.PreOperational, publisher.state());
    assertEquals(PubSubState.PreOperational, subscriber.state());
    assertNull(manager.subscriberKeyWindow(REF));
  }

  @Test
  void zeroKeyLifetimeWithNonZeroTimeToNextKeyIsMalformed() throws Exception {
    // the other half of the static sentinel rule: exactly one zero duration fails the fetch
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ofSeconds(30), Duration.ZERO));
    scheduler.runPending();

    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.subscriberKeyWindow(REF));
    assertEquals(
        new StatusCode(StatusCodes.Bad_ConfigurationError), groupDiagnostics().lastError());
  }

  @Test
  void unknownTokenBurstTriggersOneFetchAndNeverRequestsPastTokens() throws Exception {
    attachSubscriber(securityGroup(Duration.ofHours(2)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();
    assertEquals(1, provider.requests.size());

    // a burst of unknown-token hits before the scheduler runs: single-flight ⇒ ONE provider call
    manager.onUnknownToken(REF, 42);
    manager.onUnknownToken(REF, 43);
    manager.onUnknownToken(REF, 44);
    manager.onUnknownToken(REF, 45);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());

    provider.futures.get(1).complete(keySet(1, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    // within the 1 s floor a further hit does not even schedule work (dispatch-thread pre-check)
    int pendingTasks = scheduler.pendingTaskCount();
    manager.onUnknownToken(REF, 46);
    assertEquals(pendingTasks, scheduler.pendingTaskCount());
    assertEquals(2, provider.requests.size());

    // StartingTokenId is pinned to 0 on every fetch: past tokens are never re-fetched (K6)
    for (ScriptedProvider.Request request : provider.requests) {
      assertEquals(uint(0), request.startingTokenId());
    }
  }

  @Test
  void eventsForAnUnknownRefAreIgnored() {
    var unknown = new SecurityGroupRef("unbound");

    manager.onUnknownToken(unknown, 1);
    manager.onForceKeyReset(unknown);

    assertFalse(scheduler.hasPendingTasks());
    assertEquals(0, provider.requests.size());
    assertNull(manager.view(unknown));
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

    int pendingTaskCount() {
      return (int) tasks.stream().filter(task -> !task.cancelled).count();
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
