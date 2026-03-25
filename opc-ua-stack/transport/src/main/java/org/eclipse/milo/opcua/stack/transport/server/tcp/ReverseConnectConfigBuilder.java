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
 * Builder for {@link ReverseConnectConfig}.
 *
 * @see ReverseConnectConfig#newBuilder()
 */
public class ReverseConnectConfigBuilder {

  private Duration connectInterval = Duration.ofSeconds(5);
  private Duration connectTimeout = Duration.ofSeconds(5);
  private Duration rejectBackoff = Duration.ofSeconds(60);
  private Duration maxReconnectDelay = Duration.ofSeconds(30);

  /**
   * Set the interval between reconnection attempts after a connection failure. Default: 5s.
   *
   * @param connectInterval the connect interval.
   * @return this {@link ReverseConnectConfigBuilder}.
   */
  public ReverseConnectConfigBuilder setConnectInterval(Duration connectInterval) {
    this.connectInterval = connectInterval;
    return this;
  }

  /**
   * Set the TCP connect timeout per attempt. Default: 5s.
   *
   * @param connectTimeout the connect timeout.
   * @return this {@link ReverseConnectConfigBuilder}.
   */
  public ReverseConnectConfigBuilder setConnectTimeout(Duration connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  /**
   * Set the backoff duration after a client explicitly rejects the connection (sends an Error
   * message). Default: 60s.
   *
   * @param rejectBackoff the reject backoff duration.
   * @return this {@link ReverseConnectConfigBuilder}.
   */
  public ReverseConnectConfigBuilder setRejectBackoff(Duration rejectBackoff) {
    this.rejectBackoff = rejectBackoff;
    return this;
  }

  /**
   * Set the maximum reconnection delay when using exponential backoff. Default: 30s.
   *
   * @param maxReconnectDelay the maximum reconnect delay.
   * @return this {@link ReverseConnectConfigBuilder}.
   */
  public ReverseConnectConfigBuilder setMaxReconnectDelay(Duration maxReconnectDelay) {
    this.maxReconnectDelay = maxReconnectDelay;
    return this;
  }

  /**
   * Build the {@link ReverseConnectConfig}.
   *
   * @return a new {@link ReverseConnectConfig}.
   */
  public ReverseConnectConfig build() {
    return new ReverseConnectConfigImpl(
        connectInterval, connectTimeout, rejectBackoff, maxReconnectDelay);
  }

  static class ReverseConnectConfigImpl implements ReverseConnectConfig {

    private final Duration connectInterval;
    private final Duration connectTimeout;
    private final Duration rejectBackoff;
    private final Duration maxReconnectDelay;

    ReverseConnectConfigImpl(
        Duration connectInterval,
        Duration connectTimeout,
        Duration rejectBackoff,
        Duration maxReconnectDelay) {

      this.connectInterval = connectInterval;
      this.connectTimeout = connectTimeout;
      this.rejectBackoff = rejectBackoff;
      this.maxReconnectDelay = maxReconnectDelay;
    }

    @Override
    public Duration getConnectInterval() {
      return connectInterval;
    }

    @Override
    public Duration getConnectTimeout() {
      return connectTimeout;
    }

    @Override
    public Duration getRejectBackoff() {
      return rejectBackoff;
    }

    @Override
    public Duration getMaxReconnectDelay() {
      return maxReconnectDelay;
    }
  }
}
