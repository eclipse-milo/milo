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

import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.util.Unit;

/**
 * A registry that dispatches reverse-connected channels to registered consumers.
 *
 * <p>Transports call {@link #register} during connect and {@link #deregister} during disconnect.
 */
public interface ChannelConsumerRegistry {

  /** A registered consumer that receives dispatched channels and listener lifecycle signals. */
  interface ChannelConsumer {

    /**
     * Return {@code true} when this consumer is waiting for a new inbound channel.
     *
     * @return {@code true} if the consumer should receive the next matching channel.
     */
    boolean needsChannel();

    /**
     * Return {@code true} when this consumer can receive a duplicate inbound channel.
     *
     * @return {@code true} if dispatch may offer a duplicate channel to this consumer.
     */
    boolean acceptsDuplicateChannel();

    /**
     * Try to reserve and accept a decoded reverse-connected channel for this consumer.
     *
     * <p>The default implementation preserves the legacy {@link #needsChannel()} / {@link #accept}
     * behavior. Consumers that update their channel-needing state asynchronously should override
     * this method to reserve the next channel synchronously before returning {@code true}.
     *
     * @param channel the accepted channel.
     * @param reverseHello the decoded {@code ReverseHello}.
     * @return {@code true} if the channel was accepted by this consumer.
     */
    default boolean tryAccept(Channel channel, ReverseHelloMessage reverseHello) {
      if (!needsChannel()) {
        return false;
      }

      accept(channel, reverseHello);

      return true;
    }

    /**
     * Offer a decoded reverse-connected channel to this consumer.
     *
     * @param channel the accepted channel.
     * @param reverseHello the decoded {@code ReverseHello}.
     */
    void accept(Channel channel, ReverseHelloMessage reverseHello);

    /**
     * Notify this consumer that its listener has stopped.
     *
     * @param cause the shutdown cause to use for pending and future channel waiters.
     * @return a future completed when the consumer has processed the notification.
     */
    CompletableFuture<Unit> listenerStopped(Throwable cause);
  }

  /**
   * Register a consumer for the given {@code ServerUri}. Channels whose {@code ReverseHello}
   * contains this URI will be dispatched to the consumer.
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
