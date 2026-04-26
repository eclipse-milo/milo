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

import java.util.Set;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;

/**
 * Configuration for a {@link OpcTcpMultiplexedReverseConnectTransport} — an {@link
 * OpcClientTransport} that receives its channels from a shared {@code
 * MultiplexedReverseConnectListener} instead of owning a {@code ServerBootstrap}.
 *
 * @see OpcTcpMultiplexedReverseConnectTransportConfigBuilder
 */
public interface OpcTcpMultiplexedReverseConnectTransportConfig
    extends OpcClientTransportConfig, UascClientConfig {

  /**
   * Get the set of ServerUri values accepted by this transport. Only servers whose ReverseHello
   * carries a matching ServerUri will be accepted. An empty set means accept all servers.
   *
   * @return the set of allowed server URIs.
   */
  Set<String> getAllowedServerUris();

  /**
   * Get the per-client timeout in milliseconds for the ReverseHello handshake inside {@link
   * org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientReverseHelloHandler}.
   *
   * <p>This is distinct from the listener-level ReverseHello timeout, which governs the raw TCP →
   * RHE decode window.
   *
   * @return the reverse hello timeout in milliseconds.
   */
  long getReverseHelloTimeout();

  /**
   * Get the maximum time in milliseconds to wait for a reverse-connected channel or pending
   * accepted channel before failing the current lifecycle. Default: 60,000 ms.
   *
   * @return the connect timeout in milliseconds.
   */
  long getConnectTimeout();

  /**
   * Create a new {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   *
   * @return a new {@link OpcTcpMultiplexedReverseConnectTransportConfigBuilder}.
   */
  static OpcTcpMultiplexedReverseConnectTransportConfigBuilder newBuilder() {
    return new OpcTcpMultiplexedReverseConnectTransportConfigBuilder();
  }
}
