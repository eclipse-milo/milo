/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/** Builder for {@link ReverseConnectManager}. */
public final class ReverseConnectManagerBuilder {

  private final List<InetSocketAddress> bindAddresses = new ArrayList<>();

  private @Nullable Executor executor;
  private @Nullable ScheduledExecutorService scheduler;
  private @Nullable EventLoopGroup eventLoop;
  private Duration firstMessageTimeout = Duration.ofSeconds(5);
  private Duration pendingConnectionHoldTime = Duration.ofSeconds(30);
  private int maxPendingCandidates = 64;
  private int maxRetainedCandidateSnapshots = 1024;
  private ReverseHelloVerifier reverseHelloVerifier = ReverseHelloVerifier.acceptAll();
  private Consumer<ServerBootstrap> bootstrapCustomizer = bootstrap -> {};

  ReverseConnectManagerBuilder() {}

  /**
   * Add an address the manager should bind when it starts.
   *
   * @param bindAddress the listener bind address.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder addBindAddress(InetSocketAddress bindAddress) {
    bindAddresses.add(bindAddress);
    return this;
  }

  /**
   * Use the executor for serialized listener callbacks.
   *
   * @param executor the callback executor.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setExecutor(Executor executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Use the scheduler for pending-candidate expiry.
   *
   * @param scheduler the scheduled executor.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setScheduler(ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
    return this;
  }

  /**
   * Use the Netty event loop for listener channels.
   *
   * @param eventLoop the Netty event loop group.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setEventLoop(EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
    return this;
  }

  /**
   * Set the first-message timeout.
   *
   * @param firstMessageTimeout how long accepted sockets may wait for {@code ReverseHello}.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setFirstMessageTimeout(Duration firstMessageTimeout) {
    this.firstMessageTimeout = requireNonNegative(firstMessageTimeout, "firstMessageTimeout");
    return this;
  }

  /**
   * Set the first-message timeout in milliseconds.
   *
   * @param firstMessageTimeout the timeout in milliseconds.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setFirstMessageTimeout(UInteger firstMessageTimeout) {
    return setFirstMessageTimeout(Duration.ofMillis(firstMessageTimeout.longValue()));
  }

  /**
   * Set how long unclaimed decoded candidates remain pending.
   *
   * @param pendingConnectionHoldTime the pending hold time.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setPendingConnectionHoldTime(
      Duration pendingConnectionHoldTime) {

    this.pendingConnectionHoldTime =
        requireNonNegative(pendingConnectionHoldTime, "pendingConnectionHoldTime");
    return this;
  }

  /**
   * Set how long unclaimed decoded candidates remain pending, in milliseconds.
   *
   * @param pendingConnectionHoldTime the pending hold time in milliseconds.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setPendingConnectionHoldTime(
      UInteger pendingConnectionHoldTime) {

    return setPendingConnectionHoldTime(Duration.ofMillis(pendingConnectionHoldTime.longValue()));
  }

  /**
   * Set the maximum number of candidates that can be parked awaiting a selector.
   *
   * @param maxPendingCandidates the maximum pending candidates.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setMaxPendingCandidates(int maxPendingCandidates) {
    if (maxPendingCandidates < 0) {
      throw new IllegalArgumentException("maxPendingCandidates must be non-negative");
    }

    this.maxPendingCandidates = maxPendingCandidates;
    return this;
  }

  /**
   * Set how many terminal candidate snapshots are retained for each history bucket.
   *
   * <p>Pending candidates are tracked independently until they are claimed or rejected. This
   * setting only bounds the historical {@code acceptedCandidates} and {@code rejectedCandidates}
   * lists returned by {@link ReverseConnectManager#snapshot()}.
   *
   * @param maxRetainedCandidateSnapshots the maximum retained snapshots per history bucket.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setMaxRetainedCandidateSnapshots(
      int maxRetainedCandidateSnapshots) {

    if (maxRetainedCandidateSnapshots < 0) {
      throw new IllegalArgumentException("maxRetainedCandidateSnapshots must be non-negative");
    }

    this.maxRetainedCandidateSnapshots = maxRetainedCandidateSnapshots;
    return this;
  }

  /**
   * Set the synchronous pre-SecureChannel verifier.
   *
   * @param reverseHelloVerifier the verifier.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setReverseHelloVerifier(
      ReverseHelloVerifier reverseHelloVerifier) {

    this.reverseHelloVerifier = reverseHelloVerifier;
    return this;
  }

  /**
   * Customize each Netty {@link ServerBootstrap} before it binds.
   *
   * @param bootstrapCustomizer the customizer.
   * @return this builder.
   */
  public ReverseConnectManagerBuilder setBootstrapCustomizer(
      Consumer<ServerBootstrap> bootstrapCustomizer) {

    this.bootstrapCustomizer = bootstrapCustomizer;
    return this;
  }

  /**
   * Build a manager.
   *
   * @return a new manager.
   */
  public ReverseConnectManager build() {
    if (bindAddresses.isEmpty()) {
      throw new IllegalStateException("at least one bind address is required");
    }

    Executor actualExecutor = executor != null ? executor : Stack.sharedExecutor();
    ScheduledExecutorService actualScheduler =
        scheduler != null ? scheduler : Stack.sharedScheduledExecutor();
    EventLoopGroup actualEventLoop = eventLoop != null ? eventLoop : Stack.sharedEventLoop();

    return new ReverseConnectManager(
        bindAddresses,
        actualExecutor,
        actualScheduler,
        actualEventLoop,
        firstMessageTimeout,
        pendingConnectionHoldTime,
        maxPendingCandidates,
        maxRetainedCandidateSnapshots,
        reverseHelloVerifier,
        bootstrapCustomizer);
  }

  private static Duration requireNonNegative(Duration duration, String name) {
    if (duration.isNegative()) {
      throw new IllegalArgumentException(name + " must be non-negative");
    }

    return duration;
  }
}
