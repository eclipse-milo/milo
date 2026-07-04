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
 * Deterministic tests of the {@link SecurityKeyManager} key lifecycle (Part 14 §8.3.2, §6.2.12.2):
 * fetch-at-attach with deferred startup, refresh at KeyLifetime/2, switch at TimeToNextKey then
 * KeyLifetime, 2×KeyLifetime expiry into Error with recovery on the next success, FirstTokenId
 * merge/dedup/discard, policy-authority handling, the static ZERO/ZERO sentinel, unknown-token
 * single-flight refresh with its 1 s floor, and material retirement on detach (zeroization deferred
 * by the destroy grace so no in-flight borrow races the wipe).
 *
 * <p>Cadence is driven through the injectable clock + one-shot scheduler seams — no sleeping, no
 * real timers. The fake scheduler rejects {@code scheduleAtFixedRate}, implicitly pinning the
 * one-shot-reschedule requirement (TimeToNextKey/KeyLifetime are re-learned per response).
 */
class SecurityKeyManagerTest {

  private static final String AES128_URI = PubSubSecurityPolicy.PubSubAes128Ctr.getUri();
  private static final String AES256_URI = PubSubSecurityPolicy.PubSubAes256Ctr.getUri();

  private static final SecurityGroupRef REF = new SecurityGroupRef("SG");
  private static final String GROUP_PATH = "conn/WG";

  private final AtomicLong clock = new AtomicLong(0);
  private final TestScheduler scheduler = new TestScheduler(clock);

  private final List<StatusCode> transitionStatuses = new ArrayList<>();

  private final PubSubStateMachine stateMachine =
      new PubSubStateMachine(
          new Object(),
          (component, oldState, newState, statusCode, cause) -> transitionStatuses.add(statusCode));

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

    @Nullable UaException autoFail;

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      requests.add(new Request(securityGroupId, startingTokenId, requestedKeyCount));
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

  private TestComponent attachSubscriber(SecurityGroupConfig securityGroup) throws UaException {
    var component = new TestComponent(GROUP_PATH);
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));
    assertEquals(PubSubState.PreOperational, component.state());

    manager.attachSubscriber(component, signConfig(), securityGroup);
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

  private ComponentDiagnostics groupDiagnostics() {
    ComponentDiagnostics component = diagnostics.snapshot().get(GROUP_PATH);
    assertNotNull(component);
    return component;
  }

  // endregion

  @Test
  void firstFetchCompletesStartup() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));

    // the fetch is initiated asynchronously (never under the engine lock)
    assertEquals(0, provider.requests.size());
    scheduler.runPending();
    assertEquals(1, provider.requests.size());

    // StartingTokenId 0 (the current token); RequestedKeyCount defaults to 2.
    assertEquals(new ScriptedProvider.Request("SG", uint(0), uint(2)), provider.requests.get(0));

    // still PreOperational until the fetch completes
    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.currentPublisherKeys(REF));

    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());

    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);
    assertEquals(1, keys.tokenId());

    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    assertEquals(1, window.currentTokenId());
    assertNotNull(window.keyFor(1));
    assertNotNull(window.keyFor(2));
    assertNotNull(window.keyFor(3));
    assertNull(window.keyFor(4));

    SecurityKeyManager.SecurityGroupKeyView view = manager.view(REF);
    assertNotNull(view);
    assertEquals("SG", view.securityGroupId());
    assertEquals(AES128_URI, view.securityPolicyUri());
    assertEquals(1, view.currentTokenId());
    assertEquals(Duration.ofSeconds(30), view.timeToNextKey());
  }

  @Test
  void requestedKeyCountUsesConfiguredMaxFutureKeyCount() throws Exception {
    SecurityGroupConfig securityGroup =
        SecurityGroupConfig.builder("SG")
            .keyLifeTime(Duration.ofSeconds(60))
            .maxFutureKeyCount(uint(5))
            .build();

    attachPublisher(securityGroup);
    scheduler.runPending();

    assertEquals(uint(5), provider.requests.get(0).requestedKeyCount());
  }

  @Test
  void refreshRunsAtHalfKeyLifetime() throws Exception {
    attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(1, provider.requests.size());

    // KeyLifetime 60 s: the refresh fires at 30 s
    scheduler.advanceTo(seconds(29));
    assertEquals(1, provider.requests.size());
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());
    assertEquals(uint(0), provider.requests.get(1).startingTokenId());
  }

  @Test
  void switchesAtTimeToNextKeyThenEveryKeyLifetime() throws Exception {
    attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.PublisherKeys token1 = manager.currentPublisherKeys(REF);
    assertNotNull(token1);
    assertEquals(1, token1.tokenId());

    // first switch at TimeToNextKey (30 s)
    scheduler.advanceTo(seconds(30));
    SecurityKeyManager.PublisherKeys token2 = manager.currentPublisherKeys(REF);
    assertNotNull(token2);
    assertEquals(2, token2.tokenId());
    assertNotSame(token1, token2);

    // the previous key is retained for the §8.3.2 overlap tolerance
    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    assertEquals(1, window.previousTokenId());
    assertNotNull(window.keyFor(1));

    // subsequent switches every KeyLifetime (90 s)
    scheduler.advanceTo(seconds(90));
    SecurityKeyManager.PublisherKeys token3 = manager.currentPublisherKeys(REF);
    assertNotNull(token3);
    assertEquals(3, token3.tokenId());

    // token 1 left the {prev, current, futures} window: retired immediately, zeroized only
    // after the destroy grace — an in-flight encode/decode still borrowing it drains meanwhile
    SecurityKeyManager.SubscriberKeyWindow window3 = manager.subscriberKeyWindow(REF);
    assertNotNull(window3);
    assertNull(window3.keyFor(1));
    assertFalse(token1.keys().isDestroyed());
    scheduler.advanceTo(seconds(90) + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(token1.keys().isDestroyed());
  }

  @Test
  void expiryNullsSnapshotsAndFailsGroupThenRecoversOnNextSuccess() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    // a single key, no futures: the window ends when TimeToNextKey elapses at t=30
    provider.futures.get(0).complete(keySet(1, 1, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());

    // t=30: refresh fires (fetch #2, left pending) and the switch finds no future key
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());
    assertNotNull(manager.currentPublisherKeys(REF));

    // expiry at window end (30 s) + 2×KeyLifetime (120 s) = 150 s
    scheduler.advanceTo(seconds(149));
    assertEquals(PubSubState.Operational, component.state());
    scheduler.advanceTo(seconds(150));

    assertNull(manager.currentPublisherKeys(REF));
    assertNull(manager.subscriberKeyWindow(REF));
    assertEquals(PubSubState.Error, component.state());
    assertEquals(
        new StatusCode(StatusCodes.Bad_SecurityChecksFailed),
        transitionStatuses.get(transitionStatuses.size() - 1));
    assertEquals(
        new StatusCode(StatusCodes.Bad_SecurityChecksFailed), groupDiagnostics().lastError());

    // the fetch loop kept running: the pending fetch completing recovers the group
    provider.futures.get(1).complete(keySet(9, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());
    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);
    assertEquals(9, keys.tokenId());
  }

  @Test
  void policyMismatchWithConfiguredUriFailsFetch() throws Exception {
    // the SecurityGroup pins Aes256; the provider returns Aes128 keys and must not downgrade
    SecurityGroupConfig securityGroup =
        SecurityGroupConfig.builder("SG")
            .keyLifeTime(Duration.ofSeconds(60))
            .securityPolicyUri(AES256_URI)
            .build();

    TestComponent component = attachPublisher(securityGroup);
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.currentPublisherKeys(REF));
    assertNull(manager.subscriberKeyWindow(REF));
    assertEquals(
        new StatusCode(StatusCodes.Bad_ConfigurationError), groupDiagnostics().lastError());
  }

  @Test
  void unsupportedPolicyUriFailsFetch() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider
        .futures
        .get(0)
        .complete(
            new SecurityKeySet(
                "http://example.com/bogus-policy",
                uint(1),
                List.of(keyData(7)),
                Duration.ofSeconds(30),
                Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.subscriberKeyWindow(REF));
  }

  @Test
  void staticSentinelInstallsWithoutAnyScheduling() throws Exception {
    TestComponent component = attachSubscriber(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ZERO, Duration.ZERO));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());
    assertNotNull(manager.subscriberKeyWindow(REF));

    // no refresh, no switch, no expiry: nothing is scheduled and no further fetch ever happens
    assertFalse(scheduler.hasPendingTasks());
    scheduler.advanceTo(seconds(100_000));
    assertEquals(1, provider.requests.size());
    assertEquals(PubSubState.Operational, component.state());

    // static keys never rotate: unknown tokens do not trigger refetches either
    manager.onUnknownToken(REF, 99);
    scheduler.runPending();
    assertEquals(1, provider.requests.size());

    SecurityKeyManager.SecurityGroupKeyView view = manager.view(REF);
    assertNotNull(view);
    assertNull(view.timeToNextKey());
  }

  @Test
  void exactlyOneZeroDurationIsMalformed() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ZERO, Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.PreOperational, component.state());
    assertNull(manager.subscriberKeyWindow(REF));
    assertEquals(
        new StatusCode(StatusCodes.Bad_ConfigurationError), groupDiagnostics().lastError());
  }

  @Test
  void overlappingFirstTokenIdKeepsExistingEntriesAndAppends() throws Exception {
    attachSubscriber(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    SecurityKeyMaterial token2Before = window.keyFor(2);
    SecurityKeyMaterial token3Before = window.keyFor(3);
    assertNotNull(token2Before);
    assertNotNull(token3Before);

    // t=30: the switch rotates to token 2 and the refresh fetches; the SKS responds with
    // FirstTokenId 2 (its current token) and keys for tokens 2..4
    scheduler.advanceTo(seconds(30));
    assertEquals(2, provider.requests.size());
    provider.futures.get(1).complete(keySet(2, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow merged = manager.subscriberKeyWindow(REF);
    assertNotNull(merged);
    assertEquals(2, merged.currentTokenId());
    // dedup on FirstTokenId keeps the EXISTING entries for overlapping tokens
    assertSame(token2Before, merged.keyFor(2));
    assertSame(token3Before, merged.keyFor(3));
    // and appends the new tail beyond the window end
    assertNotNull(merged.keyFor(4));
    assertNull(merged.keyFor(5));
  }

  @Test
  void disconnectedFirstTokenIdDiscardsAndReplacesWindow() throws Exception {
    attachSubscriber(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(REF);
    assertNotNull(window);
    SecurityKeyMaterial token1Before = window.keyFor(1);
    assertNotNull(token1Before);

    scheduler.advanceTo(seconds(30));
    provider.futures.get(1).complete(keySet(10, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.SubscriberKeyWindow replaced = manager.subscriberKeyWindow(REF);
    assertNotNull(replaced);
    assertEquals(10, replaced.currentTokenId());
    assertNull(replaced.keyFor(2));
    assertNull(replaced.keyFor(3));
    assertNotNull(replaced.keyFor(10));
    assertNotNull(replaced.keyFor(12));

    // the discarded window's material was retired: zeroized only after the destroy grace, so a
    // decode that borrowed it just before the discard never races the wipe
    assertFalse(token1Before.isDestroyed());
    scheduler.advanceTo(clock.get() + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(token1Before.isDestroyed());
  }

  @Test
  void unknownTokenTriggersSingleFlightRefreshWithFloor() throws Exception {
    // long lifetime so no refresh/switch task interferes with the timeline below
    attachSubscriber(securityGroup(Duration.ofHours(2)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();
    assertEquals(1, provider.requests.size());

    // unknown token: one refresh
    manager.onUnknownToken(REF, 99);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());

    // single-flight: no second fetch while one is pending
    manager.onUnknownToken(REF, 99);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());

    provider.futures.get(1).complete(keySet(1, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();

    // floor: another unknown token within 1 s of the last trigger does not refetch
    manager.onUnknownToken(REF, 100);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());

    // past the floor it does
    scheduler.advanceTo(clock.get() + seconds(2));
    manager.onUnknownToken(REF, 100);
    scheduler.runPending();
    assertEquals(3, provider.requests.size());
  }

  @Test
  void forceKeyResetTriggersRefresh() throws Exception {
    attachSubscriber(securityGroup(Duration.ofHours(2)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 2, Duration.ofHours(1), Duration.ofHours(2)));
    scheduler.runPending();
    assertEquals(1, provider.requests.size());

    scheduler.advanceTo(clock.get() + seconds(2));
    manager.onForceKeyReset(REF);
    scheduler.runPending();
    assertEquals(2, provider.requests.size());
  }

  @Test
  void persistentFetchFailureFailsGroupAfterTwiceConfiguredKeyLifetime() throws Exception {
    provider.autoFail =
        new UaException(StatusCodes.Bad_CommunicationError, "key service unreachable");

    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(1)));
    scheduler.runPending();
    assertEquals(PubSubState.PreOperational, component.state());

    // retries run at min(KeyLifetime/2, 10 s) = 500 ms; the startup deadline is 2×KeyLifetime
    scheduler.advanceTo(seconds(2));

    assertTrue(provider.requests.size() > 1, "expected retries before the startup deadline");
    assertEquals(PubSubState.Error, component.state());
    // the extracted fetch status, not a generic internal error
    assertEquals(
        new StatusCode(StatusCodes.Bad_CommunicationError),
        transitionStatuses.get(transitionStatuses.size() - 1));

    // a later success recovers
    provider.autoFail = null;
    scheduler.advanceTo(seconds(3));
    provider
        .futures
        .get(provider.futures.size() - 1)
        .complete(keySet(1, 2, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, component.state());
  }

  @Test
  void lastDetachCancelsTasksAndRetiresMaterial() throws Exception {
    TestComponent component = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);

    manager.detach(component, REF);

    assertNull(manager.currentPublisherKeys(REF));
    assertNull(manager.subscriberKeyWindow(REF));
    assertNull(manager.view(REF));

    // the window material is retired on the last detach and zeroized once the destroy grace
    // elapses: a publish cycle still mid-encode when deactivate detached never races the wipe
    assertFalse(keys.keys().isDestroyed());
    scheduler.advanceTo(clock.get() + SecurityKeyManager.RETIRED_KEY_DESTROY_DELAY_NANOS);
    assertTrue(keys.keys().isDestroyed());

    // the deferred destroys were the only tasks left: every lifecycle task was cancelled
    assertFalse(scheduler.hasPendingTasks());

    scheduler.advanceTo(seconds(1_000));
    assertEquals(1, provider.requests.size());
  }

  @Test
  void secondAttachSharesTheFetchAndCompletesStartup() throws Exception {
    TestComponent first = attachPublisher(securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();
    assertEquals(PubSubState.Operational, first.state());

    // a second group attaching to the same ref completes startup from the existing window,
    // without another fetch
    var second = new TestComponent("conn/RG");
    diagnostics.register(second.path());
    stateMachine.setRootOperational(true, List.of(first, second));
    assertEquals(PubSubState.PreOperational, second.state());

    manager.attachSubscriber(second, signConfig(), securityGroup(Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, second.state());
    assertEquals(1, provider.requests.size());
  }

  // region deterministic scheduler

  /**
   * A single-threaded, manually advanced {@link ScheduledExecutorService}: tasks run in time order
   * (submission order within one instant) when {@link #advanceTo} moves the shared clock past their
   * deadline. {@code scheduleAtFixedRate}/{@code scheduleWithFixedDelay} are rejected — the key
   * manager must only use one-shot scheduling.
   */
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
      throw new UnsupportedOperationException("the key manager must not use fixed-rate scheduling");
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException(
          "the key manager must not use fixed-delay scheduling");
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
