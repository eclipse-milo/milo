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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.Test;

/**
 * Tests of the publisher nonce state exposed through {@link SecurityKeyManager.PublisherKeys}: the
 * MessageNonce wire form ({@code Random[4] || NonceSequenceNumber} little-endian, Part 14 Table
 * 156), counter reset to 1 exactly on token switch, no reset on a refresh that keeps the token, one
 * shared counter per SecurityGroupRef across all reads, and the hard stop (throw plus dropped
 * publisher snapshot) instead of a sequence wrap.
 */
class PublisherNonceStateTest {

  private static final String AES128_URI = PubSubSecurityPolicy.PubSubAes128Ctr.getUri();
  private static final SecurityGroupRef REF = new SecurityGroupRef("SG");

  private final AtomicLong clock = new AtomicLong(0);
  private final TestScheduler scheduler = new TestScheduler(clock);

  private final PubSubStateMachine stateMachine =
      new PubSubStateMachine(
          new Object(), (component, oldState, newState, statusCode, cause) -> {});

  private final EventDispatcher eventDispatcher = new EventDispatcher(Runnable::run);
  private final DiagnosticsCollector diagnostics = new DiagnosticsCollector(eventDispatcher);

  private final ScriptedProvider provider = new ScriptedProvider();

  private byte[] nonceRandom = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};

  private final SecurityKeyManager manager =
      new SecurityKeyManager(
          diagnostics,
          stateMachine,
          Map.of(REF, provider),
          scheduler,
          clock::get,
          () -> nonceRandom.clone());

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
      return false;
    }
  }

  private static final class ScriptedProvider implements SecurityKeyProvider {

    final List<CompletableFuture<SecurityKeySet>> futures = new ArrayList<>();

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      var future = new CompletableFuture<SecurityKeySet>();
      futures.add(future);
      return future;
    }
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

  /** Attach a publisher group and install keys: 3 tokens from 1, TTNK 30 s, KeyLifetime 60 s. */
  private TestComponent attachWithKeys() throws Exception {
    var component = new TestComponent("conn/WG");
    diagnostics.register(component.path());
    stateMachine.setRootOperational(true, List.of(component));

    manager.attachPublisher(
        component,
        MessageSecurityConfig.builder().mode(MessageSecurityMode.Sign).securityGroup(REF).build(),
        SecurityGroupConfig.builder("SG").keyLifeTime(Duration.ofSeconds(60)).build());

    scheduler.runPending();
    provider.futures.get(0).complete(keySet(1, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    assertEquals(PubSubState.Operational, component.state());
    return component;
  }

  private SecurityKeyManager.PublisherKeys currentKeys() {
    SecurityKeyManager.PublisherKeys keys = manager.currentPublisherKeys(REF);
    assertNotNull(keys);
    return keys;
  }

  // endregion

  @Test
  void nonceIsRandomFourBytesThenLittleEndianSequenceFromOne() throws Exception {
    attachWithKeys();

    // Random[4] || NonceSequenceNumber (UInt32, little-endian), sequence starting at 1
    assertArrayEquals(
        new byte[] {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, 0x01, 0x00, 0x00, 0x00},
        currentKeys().nonceSupplier().nextNonce());

    // the 4 random bytes are drawn fresh per call
    nonceRandom = new byte[] {0x01, 0x02, 0x03, 0x04};
    assertArrayEquals(
        new byte[] {0x01, 0x02, 0x03, 0x04, 0x02, 0x00, 0x00, 0x00},
        currentKeys().nonceSupplier().nextNonce());
  }

  @Test
  void counterIsSharedAcrossSnapshotReads() throws Exception {
    attachWithKeys();

    // every read of the same token returns the same snapshot: several writer groups publishing
    // on one SecurityGroupRef share ONE nonce counter — (key, nonce) uniqueness is global per key
    SecurityKeyManager.PublisherKeys first = currentKeys();
    SecurityKeyManager.PublisherKeys second = currentKeys();
    assertSame(first, second);

    first.nonceSupplier().nextNonce();
    second.nonceSupplier().nextNonce();
    assertEquals(3, first.nonceCounter().get());
  }

  @Test
  void refreshWithoutTokenSwitchPreservesTheCounter() throws Exception {
    attachWithKeys();

    currentKeys().nonceSupplier().nextNonce(); // sequence 1 consumed
    SecurityKeyManager.PublisherKeys before = currentKeys();

    // refresh at KeyLifetime/2 = 30 s... complete it with the SAME current token
    scheduler.advanceTo(TimeUnit.SECONDS.toNanos(29));
    scheduler.advanceTo(TimeUnit.SECONDS.toNanos(30) - 1);
    assertEquals(1, provider.futures.size());
    scheduler.advanceTo(TimeUnit.SECONDS.toNanos(30));
    assertEquals(2, provider.futures.size());

    // the switch also fired at t=30 (TTNK), rotating to token 2 — so re-read expectations:
    // completing the refresh with FirstTokenId 2 keeps the post-switch token
    provider.futures.get(1).complete(keySet(2, 3, Duration.ofSeconds(30), Duration.ofSeconds(60)));
    scheduler.runPending();

    SecurityKeyManager.PublisherKeys after = currentKeys();
    assertEquals(2, after.tokenId());

    // the refresh did not rebuild the post-switch snapshot: the counter was NOT reset
    after.nonceSupplier().nextNonce();
    assertEquals(2, after.nonceCounter().get());
    assertSame(manager.currentPublisherKeys(REF), after);

    // 'before' belonged to token 1 and is a different snapshot
    assertEquals(1, before.tokenId());
  }

  @Test
  void tokenSwitchResetsTheCounterToOne() throws Exception {
    attachWithKeys();

    SecurityKeyManager.PublisherKeys token1 = currentKeys();
    token1.nonceSupplier().nextNonce();
    token1.nonceSupplier().nextNonce();
    assertEquals(3, token1.nonceCounter().get());

    // switch at TimeToNextKey (30 s): a NEW snapshot with the counter reset to 1
    scheduler.advanceTo(TimeUnit.SECONDS.toNanos(30));

    SecurityKeyManager.PublisherKeys token2 = currentKeys();
    assertEquals(2, token2.tokenId());
    assertEquals(1, token2.nonceCounter().get());
    assertArrayEquals(
        new byte[] {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, 0x01, 0x00, 0x00, 0x00},
        token2.nonceSupplier().nextNonce());
  }

  @Test
  void sequenceExhaustionStopsPublishingAndFailsTheGroupInsteadOfWrapping() throws Exception {
    TestComponent component = attachWithKeys();

    SecurityKeyManager.PublisherKeys keys = currentKeys();
    // simulate 2^32 - 1 nonces consumed: the next getAndIncrement crosses the UInt32 range
    keys.nonceCounter().set(0xFFFF_FFFFL + 1);

    UaException e = assertThrows(UaException.class, () -> keys.nonceSupplier().nextNonce());
    assertEquals(StatusCodes.Bad_InternalError, e.getStatusCode().value());

    // stop first: the publisher snapshot is gone, so the next cycle skips
    assertNull(manager.currentPublisherKeys(REF));

    // then fail: the attached publisher group goes to Error via the scheduler
    scheduler.runPending();
    assertEquals(PubSubState.Error, component.state());

    // the subscriber side is unaffected (decode keys remain valid)
    assertNotNull(manager.subscriberKeyWindow(REF));
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

    void runPending() {
      advanceTo(clock.get());
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
      throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException();
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
