/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A shared, stack-wide, one-per-application-regardless-of-how-many-server-instances-you-have
 * handler that is added to the beginning of every server pipeline to handle rate limiting and
 * connection limits.
 *
 * <p>Any configuration changes must be made before an {@link OpcTcpServerTransport} is bound the
 * first time, application-wide. Once the instance has been created further configuration changes
 * will have no effect.
 */
@ChannelHandler.Sharable
public class RateLimitingHandler extends AbstractRemoteAddressFilter<InetSocketAddress> {

  /** Cumulative count of all connection rejections for the lifetime of the server. */
  @SuppressWarnings("WeakerAccess")
  public static final AtomicLong CUMULATIVE_CONNECTIONS_REJECTED = new AtomicLong(0L);

  /**
   * Get the shared {@link RateLimitingHandler} instance.
   *
   * <p>The values of {@link Stack.ConnectionLimits#RATE_LIMIT_ENABLED}, {@link
   * Stack.ConnectionLimits#RATE_LIMIT_MAX_ATTEMPTS}, {@link
   * Stack.ConnectionLimits#RATE_LIMIT_WINDOW_MS}, {@link
   * Stack.ConnectionLimits#RATE_LIMIT_MAX_CONNECTIONS}, and {@link
   * Stack.ConnectionLimits#RATE_LIMIT_MAX_CONNECTIONS_PER_ADDRESS} will be locked in whenever the
   * first invocation of this method occurs.
   *
   * @return the shared {@link RateLimitingHandler} instance.
   */
  public static RateLimitingHandler getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final RateLimitingHandler INSTANCE =
        new RateLimitingHandler(
            Stack.ConnectionLimits.RATE_LIMIT_ENABLED,
            Stack.ConnectionLimits.RATE_LIMIT_MAX_ATTEMPTS,
            Stack.ConnectionLimits.RATE_LIMIT_WINDOW_MS,
            Stack.ConnectionLimits.RATE_LIMIT_MAX_CONNECTIONS,
            Stack.ConnectionLimits.RATE_LIMIT_MAX_CONNECTIONS_PER_ADDRESS);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ConcurrentMap<InetAddress, LinkedList<Long>> timestamps = new ConcurrentHashMap<>();

  private final boolean enabled;
  private final int maxAttempts;
  private final int rateLimitWindowMs;
  private final int maxConnections;
  private final int maxConnectionsPerAddress;

  private final Multiset<InetAddress> connections = ConcurrentHashMultiset.create();

  private RateLimitingHandler(
      boolean enabled,
      int maxAttempts,
      int rateLimitWindowMs,
      int maxConnections,
      int maxConnectionsPerAddress) {

    this.enabled = enabled;
    this.maxAttempts = maxAttempts;
    this.rateLimitWindowMs = rateLimitWindowMs;
    this.maxConnections = maxConnections;
    this.maxConnectionsPerAddress = maxConnectionsPerAddress;

    logger.debug(
        String.format(
            "enabled=%s, maxAttempts=%s, rateLimitWindowMs=%s, maxConnections=%s,"
                + " maxConnectionsPerAddress=%s",
            enabled, maxAttempts, rateLimitWindowMs, maxConnections, maxConnectionsPerAddress));
  }

  @Override
  protected synchronized boolean accept(ChannelHandlerContext ctx, InetSocketAddress isa) {
    final InetAddress address = isa.getAddress();

    if (!enabled || address.isLoopbackAddress()) {
      return true;
    }

    LinkedList<Long> attempts = timestamps.computeIfAbsent(address, ia -> new LinkedList<>());

    long now = System.currentTimeMillis();

    if (attempts.size() >= maxAttempts) {
      // count the number of previous connections from this address
      // that have occurred within the rate limit window.
      int attemptsInWindow = 0;
      for (Long ts : attempts) {
        if (now - ts < rateLimitWindowMs) {
          attemptsInWindow++;
        }
      }

      attempts.addLast(now);
      while (attempts.size() > maxAttempts) {
        attempts.removeFirst();
      }

      int connectionsTotal = connections.size();
      int connectionsFromAddress = connections.count(address);

      boolean accept =
          attemptsInWindow < maxAttempts
              && connectionsTotal < maxConnections
              && connectionsFromAddress < maxConnectionsPerAddress;

      if (accept) {
        logger.debug(
            String.format(
                "Accepting connection from %s. window=%sms, attemptsInWindow=%s,"
                    + " connectionsTotal=%s, connectionsFromAddress=%s",
                isa,
                rateLimitWindowMs,
                attemptsInWindow,
                connectionsTotal,
                connectionsFromAddress));
      } else {
        logger.debug(
            String.format(
                "Rejecting connection from %s. window=%sms, attemptsInWindow=%s,"
                    + " connectionsTotal=%s, connectionsFromAddress=%s",
                isa,
                rateLimitWindowMs,
                attemptsInWindow,
                connectionsTotal,
                connectionsFromAddress));

        long cumulativeConnectionsRejected = CUMULATIVE_CONNECTIONS_REJECTED.incrementAndGet();
        logger.debug("cumulativeConnectionsRejected=" + cumulativeConnectionsRejected);
      }

      return accept;
    } else {
      attempts.addLast(now);

      return true;
    }
  }

  @Override
  protected void channelAccepted(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
    final InetAddress address = remoteAddress.getAddress();

    if (!enabled || address.isLoopbackAddress()) {
      return;
    }

    connections.add(address);

    ctx.channel()
        .closeFuture()
        .addListener(
            (ChannelFutureListener)
                future -> {
                  connections.remove(address);

                  if (connections.count(address) == 0) {
                    logger.debug("Scheduling timestamp removal for " + address);

                    ctx.executor()
                        .schedule(
                            () -> {
                              // If there's still no connections from the remote address after the
                              // rate limit window remove
                              // the timestamps.
                              // Removing them before the window elapses would allow a remote
                              // address to connect/disconnect
                              // at an effectively unlimited rate.
                              if (connections.count(address) == 0) {
                                timestamps.remove(address);
                                logger.debug("Removed timestamps for " + address);
                              }
                            },
                            rateLimitWindowMs,
                            TimeUnit.MILLISECONDS);
                  }
                });
  }
}
