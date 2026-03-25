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

import io.netty.bootstrap.ServerBootstrap;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;

/**
 * Configuration for the client-side OPC UA Reverse Connect transport.
 *
 * <p>In Reverse Connect mode the client opens a listening socket and waits for the server to
 * connect inbound. This config controls the listen address, which servers are accepted, and
 * handshake timeouts.
 *
 * @see OpcTcpReverseConnectTransportConfigBuilder
 * @see OpcTcpReverseConnectTransport
 */
public interface OpcTcpReverseConnectTransportConfig
    extends OpcClientTransportConfig, UascClientConfig {

  /**
   * Get the address the client listens on for incoming server connections.
   *
   * @return the listen address.
   */
  InetSocketAddress getListenAddress();

  /**
   * Get the set of ServerUri values the client will accept ReverseHello from. If empty, all
   * ServerUris are accepted (permissive mode).
   *
   * @return the allowed server URIs.
   */
  Set<String> getAllowedServerUris();

  /**
   * Get the timeout in milliseconds for receiving a ReverseHello after accepting a TCP connection.
   *
   * @return the reverse hello timeout in milliseconds.
   */
  long getReverseHelloTimeout();

  /**
   * Get a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   * used by this transport.
   *
   * <p>This transport uses a {@link ServerBootstrap} (not a client {@code Bootstrap}) because the
   * client listens for incoming server connections in Reverse Connect mode.
   *
   * @return a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   *     used by this transport.
   */
  Consumer<ServerBootstrap> getServerBootstrapCustomizer();

  /**
   * Create a new {@link OpcTcpReverseConnectTransportConfigBuilder}.
   *
   * @return a new {@link OpcTcpReverseConnectTransportConfigBuilder}.
   */
  static OpcTcpReverseConnectTransportConfigBuilder newBuilder() {
    return new OpcTcpReverseConnectTransportConfigBuilder();
  }
}
