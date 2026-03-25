/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import java.time.Duration;

/**
 * Configuration for the server-side Reverse Connect behavior, controlling connection timing, retry
 * backoff, and rejection handling.
 *
 * @see ReverseConnectConfigBuilder
 */
public interface ReverseConnectConfig {

  /**
   * @return the interval between reconnection attempts after a connection failure. default: 5s.
   */
  Duration getConnectInterval();

  /**
   * @return the TCP connect timeout per attempt. default: 5s.
   */
  Duration getConnectTimeout();

  /**
   * @return the backoff duration after a client explicitly rejects the connection (sends an Error
   *     message). default: 60s.
   */
  Duration getRejectBackoff();

  /**
   * @return the maximum reconnection delay when using exponential backoff. default: 30s.
   */
  Duration getMaxReconnectDelay();

  /**
   * @return a new {@link ReverseConnectConfigBuilder}.
   */
  static ReverseConnectConfigBuilder newBuilder() {
    return new ReverseConnectConfigBuilder();
  }
}
