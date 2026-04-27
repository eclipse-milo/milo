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

/**
 * A callback for customizing the configuration of on-demand clients created for a {@code
 * MultiplexedReverseConnectListener} when an unknown server connects via ReverseHello.
 */
@FunctionalInterface
public interface ClientCustomizer {

  /**
   * Configure the client config builder for a server that connected via ReverseHello. The callback
   * is invoked on the listener's configured transport executor.
   *
   * @param serverUri the server's ApplicationUri from the ReverseHello.
   * @param builder the client config builder to configure.
   */
  void configure(String serverUri, OpcUaClientConfigBuilder builder);
}
