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

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
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
  private volatile EndpointConfiguration current;
  private boolean active;
  private long generation;
  private long nextRefresh;
  private long cooldownNanos;
  private @Nullable CompletableFuture<EndpointConfiguration> inFlight;

  EndpointRefresh(EndpointConfiguration initial, @Nullable EndpointResolver resolver) {
    this(initial, resolver, System::nanoTime);
  }

  EndpointRefresh(
      EndpointConfiguration initial, @Nullable EndpointResolver resolver, LongSupplier nanoTime) {
    current = Objects.requireNonNull(initial);
    this.resolver = resolver;
    this.nanoTime = nanoTime;
  }

  EndpointConfiguration current() {
    return current;
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
  synchronized void stop() {
    active = false;
    generation++;
    if (inFlight != null) {
      inFlight.cancel(true);
      inFlight = null;
    }
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

  private synchronized void refresh() {
    if (!active || resolver == null || inFlight != null || nanoTime.getAsLong() - nextRefresh < 0) {
      return;
    }
    nextRefresh = nanoTime.getAsLong() + cooldownNanos;
    cooldownNanos = 2 * resolver.getRefreshCooldown().toNanos();
    long epoch = generation;
    CompletableFuture<EndpointConfiguration> source;
    try {
      source = resolver.resolve();
    } catch (Exception e) {
      LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", e);
      return;
    }
    inFlight = source;
    // The deadline applies to a copy so it never completes the resolver's own future.
    source
        .copy()
        .orTimeout(resolver.getTimeout().toMillis(), TimeUnit.MILLISECONDS)
        .whenComplete((value, error) -> complete(source, epoch, value, error));
  }

  private synchronized void complete(
      CompletableFuture<EndpointConfiguration> source,
      long epoch,
      @Nullable EndpointConfiguration value,
      @Nullable Throwable error) {

    if (epoch != generation || !active) return;
    inFlight = null;
    if (!source.isDone()) source.cancel(true);
    try {
      if (error != null) throw new UaException(error);
      validateEquivalent(current, value);
      // Compare the selected endpoint only; a change elsewhere in the discovery list is not a
      // refresh of the endpoint this client connects with.
      if (!current.endpoint().equals(value.endpoint())) {
        LOGGER.info("Endpoint refreshed: {}", value.endpoint().getEndpointUrl());
      }
      current = value;
    } catch (Exception e) {
      LOGGER.warn("Endpoint refresh failed; retaining cached endpoint", e);
    }
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
