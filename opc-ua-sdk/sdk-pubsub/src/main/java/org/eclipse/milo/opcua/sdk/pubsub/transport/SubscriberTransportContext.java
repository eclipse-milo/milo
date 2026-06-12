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

import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoopGroup;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.jspecify.annotations.Nullable;

/**
 * Context for {@link TransportProvider#openSubscriber(SubscriberTransportContext)}.
 *
 * <p>Future versions will add components to this record via new {@code of(...)} factory overloads;
 * the canonical constructor may change incompatibly when they do.
 *
 * @param connection the config of the connection the channel is opened for.
 * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
 * @param messageConsumer the consumer every received message is delivered to. The buffer is only
 *     valid for the duration of the callback; the consumer must {@code retain()} it to keep it
 *     longer, and the channel releases it after the callback returns.
 * @param topicMessageConsumer the topic-aware consumer for transports that know the source queue
 *     (topic) of a received message, or {@code null} when the engine did not supply one. A broker
 *     transport channel should deliver via this consumer when it is present, passing the source
 *     topic, and via {@code messageConsumer} otherwise; transports without topics (e.g. UDP) always
 *     use {@code messageConsumer}. A message is delivered to exactly one of the two consumers. The
 *     buffer validity rules of {@code messageConsumer} apply unchanged.
 * @apiNote Create instances via {@link #of(PubSubConnectionConfig, EventLoopGroup, Consumer,
 *     BiConsumer)} rather than the canonical constructor; the factory methods are stable while the
 *     canonical constructor is not.
 */
public record SubscriberTransportContext(
    PubSubConnectionConfig connection,
    EventLoopGroup eventLoopGroup,
    Consumer<ByteBuf> messageConsumer,
    @Nullable BiConsumer<String, ByteBuf> topicMessageConsumer) {

  /**
   * Create a {@link SubscriberTransportContext} without a topic-aware consumer.
   *
   * @param connection the config of the connection the channel is opened for.
   * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
   * @param messageConsumer the consumer every received message is delivered to. The buffer is only
   *     valid for the duration of the callback; the consumer must {@code retain()} it to keep it
   *     longer, and the channel releases it after the callback returns.
   * @return a new {@link SubscriberTransportContext}.
   */
  public static SubscriberTransportContext of(
      PubSubConnectionConfig connection,
      EventLoopGroup eventLoopGroup,
      Consumer<ByteBuf> messageConsumer) {

    return new SubscriberTransportContext(connection, eventLoopGroup, messageConsumer, null);
  }

  /**
   * Create a {@link SubscriberTransportContext}.
   *
   * @param connection the config of the connection the channel is opened for.
   * @param eventLoopGroup the Netty {@link EventLoopGroup} the channel must use for I/O.
   * @param messageConsumer the consumer every received message is delivered to. The buffer is only
   *     valid for the duration of the callback; the consumer must {@code retain()} it to keep it
   *     longer, and the channel releases it after the callback returns.
   * @param topicMessageConsumer the topic-aware consumer for transports that know the source queue
   *     (topic) of a received message, or {@code null} to supply none.
   * @return a new {@link SubscriberTransportContext}.
   */
  public static SubscriberTransportContext of(
      PubSubConnectionConfig connection,
      EventLoopGroup eventLoopGroup,
      Consumer<ByteBuf> messageConsumer,
      @Nullable BiConsumer<String, ByteBuf> topicMessageConsumer) {

    return new SubscriberTransportContext(
        connection, eventLoopGroup, messageConsumer, topicMessageConsumer);
  }
}
