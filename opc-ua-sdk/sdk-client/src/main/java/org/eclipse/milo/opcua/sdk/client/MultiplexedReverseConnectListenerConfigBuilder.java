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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.jspecify.annotations.Nullable;

/**
 * Builder for {@link MultiplexedReverseConnectListenerConfig}.
 *
 * @see MultiplexedReverseConnectListenerConfig#newBuilder()
 */
public class MultiplexedReverseConnectListenerConfigBuilder {

  private @Nullable InetSocketAddress listenAddress;
  private @Nullable OpcClientTransportConfig transportConfig;
  private boolean rateLimitingEnabled = true;
  private int maxConnections = 0;
  private int maxPendingConnections = 16;
  private long resolverTimeout = 60_000;
  private long reverseHelloTimeout = 5_000;
  private Consumer<ServerBootstrap> serverBootstrapCustomizer = b -> {};
  private Consumer<ChannelPipeline> channelPipelineCustomizer = p -> {};
  private @Nullable EndpointResolver endpointResolver;
  private @Nullable ClientCustomizer clientCustomizer;
  private @Nullable ClientListener clientListener;

  /**
   * Set the address the listener binds to for accepting incoming server connections. Required.
   *
   * @param listenAddress the listen address.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setListenAddress(
      InetSocketAddress listenAddress) {
    this.listenAddress = listenAddress;
    return this;
  }

  /**
   * Set the transport config providing shared executor, event loop, and wheel timer resources.
   * Required.
   *
   * @param transportConfig the transport config.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setTransportConfig(
      OpcClientTransportConfig transportConfig) {

    this.transportConfig = transportConfig;
    return this;
  }

  /**
   * Set whether rate limiting is enabled for incoming connections. Default: {@code true}.
   *
   * @param rateLimitingEnabled {@code true} to enable rate limiting.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setRateLimitingEnabled(
      boolean rateLimitingEnabled) {
    this.rateLimitingEnabled = rateLimitingEnabled;
    return this;
  }

  /**
   * Set the maximum number of concurrent active connections. A value of {@code 0} means unlimited.
   * Default: {@code 0}.
   *
   * @param maxConnections the maximum number of connections.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
    return this;
  }

  /**
   * Set the maximum number of in-flight unmatched connections (pending {@link EndpointResolver}
   * calls). Default: {@code 16}.
   *
   * @param maxPendingConnections the maximum number of pending connections.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setMaxPendingConnections(
      int maxPendingConnections) {
    this.maxPendingConnections = maxPendingConnections;
    return this;
  }

  /**
   * Set the timeout in milliseconds for resolving an unknown server endpoint. Default: {@code
   * 60000} ms.
   *
   * @param resolverTimeout the timeout in milliseconds.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setResolverTimeout(long resolverTimeout) {
    if (resolverTimeout <= 0) {
      throw new IllegalArgumentException("resolverTimeout must be greater than 0");
    }

    this.resolverTimeout = resolverTimeout;
    return this;
  }

  /**
   * Set the timeout in milliseconds for receiving a ReverseHello message after accepting a TCP
   * connection. Default: {@code 5000} ms.
   *
   * @param reverseHelloTimeout the timeout in milliseconds.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setReverseHelloTimeout(
      long reverseHelloTimeout) {
    this.reverseHelloTimeout = reverseHelloTimeout;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   * used by the listener. Default: no-op.
   *
   * @param serverBootstrapCustomizer a {@link Consumer} for customizing the {@link
   *     ServerBootstrap}.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setServerBootstrapCustomizer(
      Consumer<ServerBootstrap> serverBootstrapCustomizer) {

    this.serverBootstrapCustomizer = serverBootstrapCustomizer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline} of
   * each accepted child channel. Default: no-op.
   *
   * @param channelPipelineCustomizer a {@link Consumer} for customizing the {@link
   *     ChannelPipeline}.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setChannelPipelineCustomizer(
      Consumer<ChannelPipeline> channelPipelineCustomizer) {

    this.channelPipelineCustomizer = channelPipelineCustomizer;
    return this;
  }

  /**
   * Set the {@link EndpointResolver} for resolving endpoints for unknown servers. Resolver
   * invocation and completion handling run on the configured transport executor. When set, a {@link
   * ClientListener} should also be set to receive on-demand clients. A {@link ClientCustomizer} may
   * optionally be set to configure each client before it is created.
   *
   * @param endpointResolver the endpoint resolver.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setEndpointResolver(
      EndpointResolver endpointResolver) {

    this.endpointResolver = endpointResolver;
    return this;
  }

  /**
   * Set the {@link ClientCustomizer} for configuring on-demand clients. When set, the customizer is
   * called on the configured transport executor with the {@code OpcUaClientConfigBuilder} before
   * the client is created, allowing the application to set properties such as application name and
   * URI.
   *
   * @param clientCustomizer the client customizer.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setClientCustomizer(
      ClientCustomizer clientCustomizer) {

    this.clientCustomizer = clientCustomizer;
    return this;
  }

  /**
   * Set the {@link ClientListener} notified on the configured transport executor when on-demand
   * clients are created.
   *
   * @param clientListener the client listener.
   * @return this {@link MultiplexedReverseConnectListenerConfigBuilder}.
   */
  public MultiplexedReverseConnectListenerConfigBuilder setClientListener(
      ClientListener clientListener) {
    this.clientListener = clientListener;
    return this;
  }

  /**
   * Build the {@link MultiplexedReverseConnectListenerConfig}.
   *
   * @return a new {@link MultiplexedReverseConnectListenerConfig}.
   * @throws NullPointerException if {@code listenAddress} or {@code transportConfig} have not been
   *     set.
   */
  public MultiplexedReverseConnectListenerConfig build() {
    Objects.requireNonNull(listenAddress, "listenAddress must be set");
    Objects.requireNonNull(transportConfig, "transportConfig must be set");

    return new MultiplexedReverseConnectListenerConfigImpl(
        listenAddress,
        transportConfig,
        rateLimitingEnabled,
        maxConnections,
        maxPendingConnections,
        resolverTimeout,
        reverseHelloTimeout,
        serverBootstrapCustomizer,
        channelPipelineCustomizer,
        endpointResolver,
        clientCustomizer,
        clientListener);
  }

  record MultiplexedReverseConnectListenerConfigImpl(
      InetSocketAddress listenAddress,
      OpcClientTransportConfig transportConfig,
      boolean rateLimitingEnabled,
      int maxConnections,
      int maxPendingConnections,
      long resolverTimeout,
      long reverseHelloTimeout,
      Consumer<ServerBootstrap> serverBootstrapCustomizer,
      Consumer<ChannelPipeline> channelPipelineCustomizer,
      @Nullable EndpointResolver endpointResolver,
      @Nullable ClientCustomizer clientCustomizer,
      @Nullable ClientListener clientListener)
      implements MultiplexedReverseConnectListenerConfig {

    @Override
    public InetSocketAddress getListenAddress() {
      return listenAddress;
    }

    @Override
    public OpcClientTransportConfig getTransportConfig() {
      return transportConfig;
    }

    @Override
    public boolean isRateLimitingEnabled() {
      return rateLimitingEnabled;
    }

    @Override
    public int getMaxConnections() {
      return maxConnections;
    }

    @Override
    public int getMaxPendingConnections() {
      return maxPendingConnections;
    }

    @Override
    public long getResolverTimeout() {
      return resolverTimeout;
    }

    @Override
    public long getReverseHelloTimeout() {
      return reverseHelloTimeout;
    }

    @Override
    public Consumer<ServerBootstrap> getServerBootstrapCustomizer() {
      return serverBootstrapCustomizer;
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return channelPipelineCustomizer;
    }

    @Override
    public @Nullable EndpointResolver getEndpointResolver() {
      return endpointResolver;
    }

    @Override
    public @Nullable ClientCustomizer getClientCustomizer() {
      return clientCustomizer;
    }

    @Override
    public @Nullable ClientListener getClientListener() {
      return clientListener;
    }
  }
}
