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

import org.jspecify.annotations.Nullable;

/**
 * A registry that dispatches reverse-connected channels to registered consumers.
 *
 * <p>Transports call {@link #register} during connect and {@link #deregister} during disconnect.
 */
public interface ChannelConsumerRegistry {

  /**
   * A registered consumer that receives dispatched channels.
   *
   * @param channelFsm the FSM managing the channel lifecycle.
   * @param transport the transport that owns the FSM, or {@code null} for test-only consumers.
   */
  record ChannelConsumer(
      ReverseConnectChannelFsm channelFsm,
      @Nullable OpcTcpMultiplexedReverseConnectTransport transport) {}

  /**
   * Register a consumer for the given {@code ServerUri}. Channels whose {@code ReverseHello}
   * contains this URI will be dispatched to the consumer's FSM.
   *
   * @param serverUri the server's ApplicationUri.
   * @param consumer the consumer to register.
   */
  void register(String serverUri, ChannelConsumer consumer);

  /**
   * Deregister a previously registered consumer.
   *
   * @param serverUri the server's ApplicationUri.
   * @param consumer the consumer to deregister.
   */
  void deregister(String serverUri, ChannelConsumer consumer);
}
