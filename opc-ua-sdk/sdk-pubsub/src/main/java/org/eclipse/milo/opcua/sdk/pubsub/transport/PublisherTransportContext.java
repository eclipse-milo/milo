/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import io.netty.channel.EventLoopGroup;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.jspecify.annotations.Nullable;

/**
 * Context for {@link TransportProvider#openPublisher(PublisherTransportContext)}.
 *
 * <p>Future versions will add components to this record via new {@code of(...)} factory overloads;
 * the canonical constructor may change incompatibly when they do.
 *
 * @param connection the config of the connection the channel is opened for.
 * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
 * @param transportStateListener the listener the channel should notify about the liveness of its
 *     underlying connection, or {@code null} when the engine did not supply one. Connectionless
 *     transports (e.g. UDP) ignore it.
 * @apiNote Create instances via {@link #of(PubSubConnectionConfig, EventLoopGroup)} rather than the
 *     canonical constructor; the factory methods are stable while the canonical constructor is not.
 */
public record PublisherTransportContext(
    PubSubConnectionConfig connection,
    EventLoopGroup eventLoopGroup,
    @Nullable TransportStateListener transportStateListener) {

  /**
   * Create a {@link PublisherTransportContext} without a transport state listener.
   *
   * @param connection the config of the connection the channel is opened for.
   * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
   * @return a new {@link PublisherTransportContext}.
   */
  public static PublisherTransportContext of(
      PubSubConnectionConfig connection, EventLoopGroup eventLoopGroup) {

    return new PublisherTransportContext(connection, eventLoopGroup, null);
  }

  /**
   * Create a {@link PublisherTransportContext}.
   *
   * @param connection the config of the connection the channel is opened for.
   * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
   * @param transportStateListener the listener the channel should notify about the liveness of its
   *     underlying connection, or {@code null} to supply none.
   * @return a new {@link PublisherTransportContext}.
   */
  public static PublisherTransportContext of(
      PubSubConnectionConfig connection,
      EventLoopGroup eventLoopGroup,
      @Nullable TransportStateListener transportStateListener) {

    return new PublisherTransportContext(connection, eventLoopGroup, transportStateListener);
  }
}
