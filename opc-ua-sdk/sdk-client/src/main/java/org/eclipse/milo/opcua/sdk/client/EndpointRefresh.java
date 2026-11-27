/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.LongSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the endpoint the client currently connects with and refreshes it through the configured
 * {@link EndpointResolver} after a SecureChannel establishment failure.
 *
 * <p>The transport reads {@link #current()} at the start of every connection attempt and keeps
 * reconnecting on its own schedule. A refresh runs concurrently with that schedule, so the attempt
 * after a successful refresh connects with the replacement server certificate.
 *
 * <p>Refreshes are rate limited by the resolver's cooldown. The cooldown resets each time a channel
 * becomes ready, so the first failure after a working connection refreshes immediately while a
 * server that keeps failing the handshake sees a bounded discovery rate.
 */
final class EndpointRefresh {
  private static final Logger LOGGER = LoggerFactory.getLogger(EndpointRefresh.class);

  private final @Nullable EndpointResolver resolver;
  private final LongSupplier nanoTime;
  private final ExecutionQueue notificationQueue;
  private final List<EndpointRefreshListener> listeners = new CopyOnWriteArrayList<>();
  private final ArrayDeque<Runnable> pendingNotifications = new ArrayDeque<>();
  private volatile EndpointConfiguration current;
  private boolean active;
  private boolean refreshing;
  private int pendingCancels;
  private long generation;
  private long nextRefresh;
  private long cooldownNanos;
  private @Nullable CompletableFuture<EndpointConfiguration> inFlight;

  EndpointRefresh(EndpointConfiguration initial, @Nullable EndpointResolver resolver) {
    this(initial, resolver, System::nanoTime, Runnable::run);
  }

  EndpointRefresh(
      EndpointConfiguration initial, @Nullable EndpointResolver resolver, Executor executor) {
    this(initial, resolver, System::nanoTime, executor);
  }

  EndpointRefresh(
      EndpointConfiguration initial, @Nullable EndpointResolver resolver, LongSupplier nanoTime) {
    this(initial, resolver, nanoTime, Runnable::run);
  }

  EndpointRefresh(
      EndpointConfiguration initial,
      @Nullable EndpointResolver resolver,
      LongSupplier nanoTime,
      Executor executor) {
    current = Objects.requireNonNull(initial, "initial");
    this.resolver = resolver;
    this.nanoTime = Objects.requireNonNull(nanoTime, "nanoTime");
    this.notificationQueue = new ExecutionQueue(Objects.requireNonNull(executor, "executor"));
  }

  EndpointConfiguration current() {
    return current;
  }

  void addListener(EndpointRefreshListener listener) {
    listeners.add(Objects.requireNonNull(listener, "listener"));
  }

  void removeListener(EndpointRefreshListener listener) {
    listeners.remove(Objects.requireNonNull(listener, "listener"));
  }

  /** Enable refreshes for a new connection and allow the first one immediately. */
  synchronized void start() {
    if (!active) {
      active = true;
      resetCooldown();
    }
  }

  /**
   * Allow the next qualifying failure to refresh immediately.
   *
   * <p>Called when a channel becomes ready. Without this, the cooldown doubled by an earlier outage
   * would delay recovery from a second certificate rotation for the client's whole lifetime, since
   * the transport reconnects without going through {@link #start()}.
   */
  synchronized void onConnected() {
    if (active) {
      resetCooldown();
    }
  }

  private void resetCooldown() {
    nextRefresh = nanoTime.getAsLong();
    cooldownNanos = resolver != null ? resolver.getRefreshCooldown().toNanos() : 0;
  }

  /** Disable refreshes, cancel any in flight, and discard its result if it completes late. */
  void stop() {
    CompletableFuture<EndpointConfiguration> toCancel;
    synchronized (this) {
      active = false;
      generation++;
      refreshing = false;
      toCancel = inFlight;
      inFlight = null;
      if (toCancel != null) {
        pendingCancels++;
      }
    }
    cancelOutsideLock(toCancel);
  }

  /**
   * Refresh after a failed SecureChannel establishment, which may indicate stale discovery data.
   *
   * <p>Unsecured endpoints carry no certificate binding and do not trigger refresh.
   */
  void onConnectFailure(Throwable failure) {
    if (current.endpoint().getSecurityMode() != MessageSecurityMode.None
        && isRefreshTrigger(failure)) {
      refresh();
    }
  }

  private void refresh() {
    final EndpointResolver currentResolver;
    final long epoch;
    synchronized (this) {
      if (!active
          || resolver == null
          || refreshing
          || pendingCancels > 0
          || nanoTime.getAsLong() - nextRefresh < 0) {
        return;
      }
      nextRefresh = nanoTime.getAsLong() + cooldownNanos;
      cooldownNanos = 2 * resolver.getRefreshCooldown().toNanos();
      epoch = generation;
      refreshing = true;
      currentResolver = resolver;
    }

    CompletableFuture<EndpointConfiguration> source;
    long timeoutMillis;
    try {
      timeoutMillis =
          Objects.requireNonNull(
                  currentResolver.getTimeout(), "EndpointResolver.getTimeout() returned null")
              .toMillis();
      source =
          Objects.requireNonNull(
              currentResolver.resolve(), "EndpointResolver.resolve() returned null");
    } catch (Throwable t) {
      boolean shouldNotify = false;
      synchronized (this) {
        if (epoch == generation && active) {
          refreshing = false;
          if (!(t instanceof Error)) {
            Throwable failure = normalizeFailure(t);
            LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", failure);
            shouldNotify = enqueueNotification(() -> notifyRefreshFailed(failure));
          }
        }
      }
      if (t instanceof Error err) {
        throw err;
      }
      if (shouldNotify) {
        notificationQueue.submit(this::drainNotifications);
      }
      return;
    }

    boolean cancelledBeforeAttach = false;
    synchronized (this) {
      if (epoch == generation && active) {
        inFlight = source;
      } else {
        cancelledBeforeAttach = true;
        pendingCancels++;
      }
    }

    if (cancelledBeforeAttach) {
      cancelOutsideLock(source);
      return;
    }

    // The deadline applies to a copy so it never completes the resolver's own future.
    source
        .copy()
        .orTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
        .whenComplete((value, error) -> complete(source, epoch, value, error));
  }

  private void complete(
      CompletableFuture<EndpointConfiguration> source,
      long epoch,
      @Nullable EndpointConfiguration value,
      @Nullable Throwable error) {

    boolean cancelSource;
    boolean shouldNotify = false;
    synchronized (this) {
      if (epoch != generation || !active) {
        return;
      }
      refreshing = false;
      inFlight = null;
      cancelSource = !source.isDone();
      if (cancelSource) {
        pendingCancels++;
      }

      if (error != null) {
        Throwable failure = normalizeFailure(error);
        LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", failure);
        shouldNotify = enqueueNotification(() -> notifyRefreshFailed(failure));
      } else if (value == null) {
        Throwable failure = new NullPointerException("EndpointResolver resolved to null");
        LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", failure);
        shouldNotify = enqueueNotification(() -> notifyRefreshFailed(failure));
      } else {
        EndpointConfiguration previous = current;
        try {
          validateEquivalent(previous, value);
          // Compare the selected endpoint only; a change elsewhere in the discovery list is not a
          // refresh of the endpoint this client connects with.
          boolean endpointChanged = !previous.endpoint().equals(value.endpoint());
          current = value;
          if (endpointChanged) {
            LOGGER.info("Endpoint refreshed: {}", value.endpoint().getEndpointUrl());
            shouldNotify = enqueueNotification(() -> notifyRefreshed(previous, value));
          }
        } catch (Exception e) {
          LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", e);
          shouldNotify = enqueueNotification(() -> notifyRefreshFailed(e));
        }
      }
    }

    try {
      if (cancelSource) {
        cancelOutsideLock(source);
      }
    } finally {
      if (shouldNotify) {
        notificationQueue.submit(this::drainNotifications);
      }
    }
  }

  private void cancelOutsideLock(@Nullable CompletableFuture<EndpointConfiguration> toCancel) {
    if (toCancel == null) {
      return;
    }
    try {
      toCancel.cancel(true);
    } finally {
      synchronized (this) {
        pendingCancels--;
      }
    }
  }

  private boolean enqueueNotification(Runnable notification) {
    if (listeners.isEmpty()) {
      return false;
    }
    pendingNotifications.add(notification);
    return true;
  }

  private void drainNotifications() {
    while (true) {
      Runnable next;
      synchronized (this) {
        next = pendingNotifications.poll();
      }
      if (next == null) {
        return;
      }
      next.run();
    }
  }

  private void notifyRefreshed(EndpointConfiguration previous, EndpointConfiguration current) {
    for (EndpointRefreshListener listener : listeners) {
      try {
        listener.onEndpointRefreshed(previous, current);
      } catch (Throwable t) {
        LOGGER.warn("EndpointRefreshListener failed: {}", listener, t);
      }
    }
  }

  private void notifyRefreshFailed(Throwable failure) {
    for (EndpointRefreshListener listener : listeners) {
      try {
        listener.onEndpointRefreshFailed(failure);
      } catch (Throwable t) {
        LOGGER.warn("EndpointRefreshListener failed: {}", listener, t);
      }
    }
  }

  private static Throwable normalizeFailure(Throwable failure) {
    Throwable cause = failure;
    while ((cause instanceof CompletionException || cause instanceof ExecutionException)
        && cause.getCause() != null
        && cause.getCause() != cause) {
      cause = cause.getCause();
    }
    if (cause instanceof TimeoutException) {
      return new UaException(StatusCodes.Bad_Timeout, cause);
    }
    return cause;
  }

  /** Whether the transport identifies a failed SecureChannel establishment. */
  static boolean isRefreshTrigger(Throwable failure) {
    boolean handshakeFailure = false;
    for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
      if (cause instanceof CancellationException) return false;
      if (cause instanceof SecureChannelHandshakeException) handshakeFailure = true;
      if (cause.getCause() == cause) break;
    }
    return handshakeFailure;
  }

  /**
   * Require the replacement to describe the same endpoint as the one it replaces, so that only the
   * server certificate can change.
   *
   * <p>The user token policies are pinned as well. GetEndpoints is unauthenticated and unsecured,
   * and a refresh is reachable by anyone able to disrupt the OPN handshake. An attacker who could
   * swap in a weaker user token policy, such as a {@code None} policy on a Sign-only channel, would
   * otherwise obtain the user's credentials in the clear on the next ActivateSession.
   */
  static void validateEquivalent(EndpointConfiguration previous, EndpointConfiguration replacement)
      throws UaException {
    var a = previous.endpoint();
    var b = replacement.endpoint();
    if (!Objects.equals(a.getEndpointUrl(), b.getEndpointUrl())
        || !Objects.equals(a.getServer().getApplicationUri(), b.getServer().getApplicationUri())
        || !Objects.equals(a.getServer().getGatewayServerUri(), b.getServer().getGatewayServerUri())
        || !Objects.equals(a.getTransportProfileUri(), b.getTransportProfileUri())
        || !Objects.equals(a.getSecurityPolicyUri(), b.getSecurityPolicyUri())
        || a.getSecurityMode() != b.getSecurityMode()
        || !Arrays.equals(a.getUserIdentityTokens(), b.getUserIdentityTokens())) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "discovery did not select an equivalent server endpoint");
    }
  }
}
