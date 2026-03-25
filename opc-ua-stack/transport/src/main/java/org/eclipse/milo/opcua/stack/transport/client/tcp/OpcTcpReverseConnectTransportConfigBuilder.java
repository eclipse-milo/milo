/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Builder for {@link OpcTcpReverseConnectTransportConfig}.
 *
 * @see OpcTcpReverseConnectTransportConfig#newBuilder()
 */
public class OpcTcpReverseConnectTransportConfigBuilder {

  private InetSocketAddress listenAddress;
  private Set<String> allowedServerUris = new LinkedHashSet<>();
  private long reverseHelloTimeout = 30_000;
  private long connectTimeout = 60_000;
  private UInteger acknowledgeTimeout = uint(5_000);
  private UInteger channelLifetime = uint(60 * 60 * 1000);

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private EventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private Consumer<ServerBootstrap> serverBootstrapCustomizer = b -> {};
  private Consumer<ChannelPipeline> channelPipelineCustomizer = p -> {};

  /**
   * Set the address the client listens on for incoming server connections. Required.
   *
   * @param listenAddress the listen address.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setListenAddress(
      InetSocketAddress listenAddress) {

    this.listenAddress = listenAddress;
    return this;
  }

  /**
   * Add a ServerUri to the set of accepted values. Only servers whose ReverseHello carries a
   * matching ServerUri will be accepted. If no URIs are added, all servers are accepted.
   *
   * @param serverUri the server URI to allow.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder addAllowedServerUri(String serverUri) {
    this.allowedServerUris.add(serverUri);
    return this;
  }

  /**
   * Replace the entire set of allowed ServerUri values.
   *
   * @param allowedServerUris the set of allowed server URIs.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   * @see #addAllowedServerUri(String)
   */
  public OpcTcpReverseConnectTransportConfigBuilder setAllowedServerUris(
      Set<String> allowedServerUris) {

    this.allowedServerUris = new LinkedHashSet<>(allowedServerUris);
    return this;
  }

  /**
   * Set the timeout in milliseconds for receiving a ReverseHello after accepting a TCP connection.
   * Default: 30,000 ms.
   *
   * @param reverseHelloTimeout the timeout in milliseconds.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setReverseHelloTimeout(
      long reverseHelloTimeout) {

    this.reverseHelloTimeout = reverseHelloTimeout;
    return this;
  }

  /**
   * Set the maximum time in milliseconds to wait for a server to connect inbound when used with
   * one-shot discovery methods. Default: 60,000 ms.
   *
   * @param connectTimeout the connect timeout in milliseconds.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setConnectTimeout(long connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  /**
   * Set the timeout for the Hello/Acknowledge exchange. Default: 5,000 ms.
   *
   * @param acknowledgeTimeout the timeout.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setAcknowledgeTimeout(
      UInteger acknowledgeTimeout) {

    this.acknowledgeTimeout = acknowledgeTimeout;
    return this;
  }

  /**
   * Set the requested SecureChannel lifetime. Default: 3,600,000 ms (1 hour).
   *
   * @param channelLifetime the channel lifetime.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setChannelLifetime(UInteger channelLifetime) {
    this.channelLifetime = channelLifetime;
    return this;
  }

  /**
   * Set the {@link ExecutorService} for general-purpose work. If not set, the shared Stack executor
   * is used.
   *
   * @param executor the executor service.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Set the {@link ScheduledExecutorService} for scheduled tasks. If not set, the shared Stack
   * scheduled executor is used.
   *
   * @param scheduledExecutor the scheduled executor service.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setScheduledExecutor(
      ScheduledExecutorService scheduledExecutor) {

    this.scheduledExecutor = scheduledExecutor;
    return this;
  }

  /**
   * Set the Netty {@link EventLoopGroup} for I/O. If not set, the shared Stack event loop is used.
   *
   * @param eventLoop the event loop group.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setEventLoop(EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
    return this;
  }

  /**
   * Set the {@link HashedWheelTimer} for timeout scheduling. If not set, the shared Stack wheel
   * timer is used.
   *
   * @param wheelTimer the wheel timer.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setWheelTimer(HashedWheelTimer wheelTimer) {
    this.wheelTimer = wheelTimer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   * used by this transport.
   *
   * <p>This transport uses a {@link ServerBootstrap} (not a client {@link Bootstrap}) because the
   * client listens for incoming server connections in Reverse Connect mode.
   *
   * @param serverBootstrapCustomizer a {@link Consumer} that will be given a chance to customize
   *     the {@link ServerBootstrap} used by this transport.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setServerBootstrapCustomizer(
      Consumer<ServerBootstrap> serverBootstrapCustomizer) {

    this.serverBootstrapCustomizer = serverBootstrapCustomizer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @param channelPipelineCustomizer a {@link Consumer} that will be given a chance to customize
   *     the {@link ChannelPipeline} used by this transport.
   * @return this {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  public OpcTcpReverseConnectTransportConfigBuilder setChannelPipelineCustomizer(
      Consumer<ChannelPipeline> channelPipelineCustomizer) {

    this.channelPipelineCustomizer = channelPipelineCustomizer;
    return this;
  }

  /**
   * Build the {@link OpcTcpReverseConnectTransportConfig}.
   *
   * @return a new {@link OpcTcpReverseConnectTransportConfig}.
   * @throws NullPointerException if {@code listenAddress} has not been set.
   */
  public OpcTcpReverseConnectTransportConfig build() {
    Objects.requireNonNull(listenAddress, "listenAddress must be set");

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

    return new OpcTcpReverseConnectTransportConfigImpl(
        listenAddress,
        Set.copyOf(allowedServerUris),
        reverseHelloTimeout,
        connectTimeout,
        acknowledgeTimeout,
        channelLifetime,
        executor,
        scheduledExecutor,
        eventLoop,
        wheelTimer,
        serverBootstrapCustomizer,
        channelPipelineCustomizer);
  }

  static class OpcTcpReverseConnectTransportConfigImpl
      implements OpcTcpReverseConnectTransportConfig {

    private final InetSocketAddress listenAddress;
    private final Set<String> allowedServerUris;
    private final long reverseHelloTimeout;
    private final long connectTimeout;
    private final UInteger acknowledgeTimeout;
    private final UInteger channelLifetime;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final EventLoopGroup eventLoop;
    private final HashedWheelTimer wheelTimer;
    private final Consumer<ServerBootstrap> serverBootstrapCustomizer;
    private final Consumer<ChannelPipeline> channelPipelineCustomizer;

    public OpcTcpReverseConnectTransportConfigImpl(
        InetSocketAddress listenAddress,
        Set<String> allowedServerUris,
        long reverseHelloTimeout,
        long connectTimeout,
        UInteger acknowledgeTimeout,
        UInteger channelLifetime,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutor,
        EventLoopGroup eventLoop,
        HashedWheelTimer wheelTimer,
        Consumer<ServerBootstrap> serverBootstrapCustomizer,
        Consumer<ChannelPipeline> channelPipelineCustomizer) {

      this.listenAddress = listenAddress;
      this.allowedServerUris = allowedServerUris;
      this.reverseHelloTimeout = reverseHelloTimeout;
      this.connectTimeout = connectTimeout;
      this.acknowledgeTimeout = acknowledgeTimeout;
      this.channelLifetime = channelLifetime;
      this.executor = executor;
      this.scheduledExecutor = scheduledExecutor;
      this.eventLoop = eventLoop;
      this.wheelTimer = wheelTimer;
      this.serverBootstrapCustomizer = serverBootstrapCustomizer;
      this.channelPipelineCustomizer = channelPipelineCustomizer;
    }

    @Override
    public InetSocketAddress getListenAddress() {
      return listenAddress;
    }

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
    public Consumer<ServerBootstrap> getServerBootstrapCustomizer() {
      return serverBootstrapCustomizer;
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return channelPipelineCustomizer;
    }
  }
}
