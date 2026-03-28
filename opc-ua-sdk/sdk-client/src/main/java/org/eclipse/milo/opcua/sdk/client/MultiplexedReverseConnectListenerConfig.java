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
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.jspecify.annotations.Nullable;

/**
 * Configuration for a {@link MultiplexedReverseConnectListener}.
 *
 * @see #newBuilder()
 */
public interface MultiplexedReverseConnectListenerConfig {

  /**
   * Get the address the listener binds to for accepting incoming server connections.
   *
   * @return the listen address.
   */
  InetSocketAddress getListenAddress();

  /**
   * Get the transport config providing shared executor, event loop, and wheel timer resources.
   *
   * @return the transport config.
   */
  OpcClientTransportConfig getTransportConfig();

  /**
   * Return whether rate limiting is enabled for incoming connections.
   *
   * @return {@code true} if rate limiting is enabled.
   */
  boolean isRateLimitingEnabled();

  /**
   * Get the maximum number of concurrent active connections. A value of {@code 0} means unlimited.
   *
   * @return the maximum number of connections.
   */
  int getMaxConnections();

  /**
   * Get the maximum number of in-flight unmatched connections (pending {@link EndpointResolver}
   * calls).
   *
   * @return the maximum number of pending connections.
   */
  int getMaxPendingConnections();

  /**
   * Get the timeout in milliseconds for receiving a ReverseHello message after accepting a TCP
   * connection.
   *
   * @return the timeout in milliseconds.
   */
  long getReverseHelloTimeout();

  /**
   * Get the {@link Consumer} for customizing the {@link ServerBootstrap}.
   *
   * @return the server bootstrap customizer.
   */
  Consumer<ServerBootstrap> getServerBootstrapCustomizer();

  /**
   * Get the {@link Consumer} for customizing the {@link ChannelPipeline} of each accepted child
   * channel.
   *
   * @return the channel pipeline customizer.
   */
  Consumer<ChannelPipeline> getChannelPipelineCustomizer();

  /**
   * Get the {@link EndpointResolver} for resolving endpoints for unknown servers. If {@code null},
   * connections from unregistered servers are closed.
   *
   * @return the endpoint resolver, or {@code null} if on-demand client creation is not enabled.
   */
  @Nullable EndpointResolver getEndpointResolver();

  /**
   * Get the {@link ClientCustomizer} for configuring on-demand clients. When set, the customizer is
   * called with the {@code OpcUaClientConfigBuilder} before the client is created, allowing the
   * application to set properties such as application name and URI.
   *
   * @return the client customizer, or {@code null}.
   */
  @Nullable ClientCustomizer getClientCustomizer();

  /**
   * Get the {@link ClientListener} notified when on-demand clients are created.
   *
   * @return the client listener, or {@code null}.
   */
  @Nullable ClientListener getClientListener();

  /**
   * Create a new {@link MultiplexedReverseConnectListenerConfigBuilder}.
   *
   * @return a new builder.
   */
  static MultiplexedReverseConnectListenerConfigBuilder newBuilder() {
    return new MultiplexedReverseConnectListenerConfigBuilder();
  }
}
