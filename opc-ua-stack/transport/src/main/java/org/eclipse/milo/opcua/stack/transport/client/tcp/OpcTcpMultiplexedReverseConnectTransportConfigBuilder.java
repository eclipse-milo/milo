/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Builder for {@link OpcTcpMultiplexedReverseConnectTransportConfig}.
 *
 * @see OpcTcpMultiplexedReverseConnectTransportConfig#newBuilder()
 */
public class OpcTcpMultiplexedReverseConnectTransportConfigBuilder {

  private Set<String> allowedServerUris = new LinkedHashSet<>();
  private long reverseHelloTimeout = 30_000;
  private long connectTimeout = 60_000;
  private UInteger acknowledgeTimeout = uint(5_000);
  private UInteger channelLifetime = uint(60 * 60 * 1000);

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private EventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private Consumer<ChannelPipeline> channelPipelineCustomizer = p -> {};

  /**
   * Add a ServerUri to the set of accepted values. Only servers whose ReverseHello carries a
   * matching ServerUri will be accepted. If no URIs are added, all servers are accepted.
   *
   * @param serverUri the server URI to allow.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder addAllowedServerUri(
      String serverUri) {
    this.allowedServerUris.add(serverUri);
    return this;
  }

  /**
   * Replace the entire set of allowed ServerUri values.
   *
   * @param allowedServerUris the set of allowed server URIs.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   * @see #addAllowedServerUri(String)
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setAllowedServerUris(
      Set<String> allowedServerUris) {

    this.allowedServerUris = new LinkedHashSet<>(allowedServerUris);
    return this;
  }

  /**
   * Set the per-client timeout in milliseconds for the ReverseHello handshake. Default: 30,000 ms.
   *
   * @param reverseHelloTimeout the timeout in milliseconds.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setReverseHelloTimeout(
      long reverseHelloTimeout) {
    this.reverseHelloTimeout = reverseHelloTimeout;
    return this;
  }

  /**
   * Set the maximum time in milliseconds to wait for a reverse-connected channel or pending
   * accepted channel. Default: 60,000 ms.
   *
   * @param connectTimeout the connect timeout in milliseconds.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setConnectTimeout(
      long connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  /**
   * Set the timeout for the Hello/Acknowledge exchange. Default: 5,000 ms.
   *
   * @param acknowledgeTimeout the timeout.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setAcknowledgeTimeout(
      UInteger acknowledgeTimeout) {
    this.acknowledgeTimeout = acknowledgeTimeout;
    return this;
  }

  /**
   * Set the requested SecureChannel lifetime. Default: 3,600,000 ms (1 hour).
   *
   * @param channelLifetime the channel lifetime.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setChannelLifetime(
      UInteger channelLifetime) {
    this.channelLifetime = channelLifetime;
    return this;
  }

  /**
   * Set the {@link ExecutorService} for general-purpose work. If not set, the shared Stack executor
   * is used.
   *
   * @param executor the executor service.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setExecutor(
      ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Set the {@link ScheduledExecutorService} for scheduled tasks. If not set, the shared Stack
   * scheduled executor is used.
   *
   * @param scheduledExecutor the scheduled executor service.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setScheduledExecutor(
      ScheduledExecutorService scheduledExecutor) {

    this.scheduledExecutor = scheduledExecutor;
    return this;
  }

  /**
   * Set the Netty {@link EventLoopGroup} for I/O. If not set, the shared Stack event loop is used.
   *
   * @param eventLoop the event loop group.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setEventLoop(
      EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
    return this;
  }

  /**
   * Set the {@link HashedWheelTimer} for timeout scheduling. If not set, the shared Stack wheel
   * timer is used.
   *
   * @param wheelTimer the wheel timer.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setWheelTimer(
      HashedWheelTimer wheelTimer) {
    this.wheelTimer = wheelTimer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @param channelPipelineCustomizer a {@link Consumer} for customizing the {@link
   *     ChannelPipeline}.
   * @return this {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfigBuilder setChannelPipelineCustomizer(
      Consumer<ChannelPipeline> channelPipelineCustomizer) {

    this.channelPipelineCustomizer = channelPipelineCustomizer;
    return this;
  }

  /**
   * Build the {@link OpcTcpMultiplexedReverseConnectTransportConfig}.
   *
   * @return a new {@link OpcTcpMultiplexedReverseConnectTransportConfig}.
   */
  public OpcTcpMultiplexedReverseConnectTransportConfig build() {
    if (executor == null) {
      executor = Stack.sharedExecutor();
    }
    if (scheduledExecutor == null) {
      scheduledExecutor = Stack.sharedScheduledExecutor();
    }
    if (eventLoop == null) {
      eventLoop = Stack.sharedEventLoop();
    }
    if (wheelTimer == null) {
      wheelTimer = Stack.sharedWheelTimer();
    }

    return new OpcTcpMultiplexedReverseConnectTransportConfigImpl(
        Set.copyOf(allowedServerUris),
        reverseHelloTimeout,
        connectTimeout,
        acknowledgeTimeout,
        channelLifetime,
        executor,
        scheduledExecutor,
        eventLoop,
        wheelTimer,
        channelPipelineCustomizer);
  }

  record OpcTcpMultiplexedReverseConnectTransportConfigImpl(
      Set<String> allowedServerUris,
      long reverseHelloTimeout,
      long connectTimeout,
      UInteger acknowledgeTimeout,
      UInteger channelLifetime,
      ExecutorService executor,
      ScheduledExecutorService scheduledExecutor,
      EventLoopGroup eventLoop,
      HashedWheelTimer wheelTimer,
      Consumer<ChannelPipeline> channelPipelineCustomizer)
      implements OpcTcpMultiplexedReverseConnectTransportConfig {

    @Override
    public Set<String> getAllowedServerUris() {
      return allowedServerUris;
    }

    @Override
    public long getReverseHelloTimeout() {
      return reverseHelloTimeout;
    }

    @Override
    public long getConnectTimeout() {
      return connectTimeout;
    }

    @Override
    public UInteger getAcknowledgeTimeout() {
      return acknowledgeTimeout;
    }

    @Override
    public UInteger getChannelLifetime() {
      return channelLifetime;
    }

    @Override
    public ExecutorService getExecutor() {
      return executor;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutor() {
      return scheduledExecutor;
    }

    @Override
    public EventLoopGroup getEventLoop() {
      return eventLoop;
    }

    @Override
    public HashedWheelTimer getWheelTimer() {
      return wheelTimer;
    }

    @Override
    public Consumer<Bootstrap> getBootstrapCustomizer() {
      return b -> {};
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return channelPipelineCustomizer;
    }
  }
}
