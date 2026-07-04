/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link PublisherChannel} that publishes encoded NetworkMessages to MQTT topics.
 *
 * <p>Sends are addressed: the engine resolves the topic, QoS, retain flag, and content type into
 * the {@link MessageAddress} and this channel applies them verbatim. The payload is copied to a
 * {@code byte[]} (the HiveMQ API is array-based) and the {@link ByteBuf} released immediately. On
 * MQTT 5 connections the address's content type hint is carried in the Content Type property (Part
 * 14 §7.3.4.9); MQTT 3.1.1 has no message properties, so it is discarded.
 *
 * <p>Send failures (including sends attempted while the broker is unreachable) complete the
 * returned future exceptionally, which the engine records in diagnostics.
 *
 * <p>The context's {@code TransportStateListener}, when present, is registered with the shared
 * session at open and removed at close, so the engine observes broker liveness edges for the
 * channel's lifetime.
 */
final class MqttPublisherChannel implements PublisherChannel {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqttPublisherChannel.class);

  private final AtomicBoolean closed = new AtomicBoolean(false);

  private final MqttTransportProvider provider;
  private final MqttClientSession session;
  private final @Nullable TransportStateListener transportStateListener;

  MqttPublisherChannel(
      MqttTransportProvider provider,
      MqttClientSession session,
      @Nullable TransportStateListener transportStateListener) {

    this.provider = provider;
    this.session = session;
    this.transportStateListener = transportStateListener;

    if (transportStateListener != null) {
      session.addTransportStateListener(transportStateListener);
    }
  }

  @Override
  public CompletableFuture<Void> send(ByteBuf message) {
    message.release();

    return CompletableFuture.failedFuture(
        new UaException(
            StatusCodes.Bad_InvalidArgument,
            "the MQTT publisher channel requires an addressed send"));
  }

  @Override
  public CompletableFuture<Void> send(ByteBuf message, MessageAddress address) {
    byte[] payload;
    try {
      payload = ByteBufUtil.getBytes(message);
    } finally {
      message.release();
    }

    if (closed.get()) {
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_InvalidState, "the MQTT publisher channel is closed"));
    }

    String topic = address.queueName();
    if (topic == null) {
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_ConfigurationError, "message address has no queue name"));
    }

    MqttQos qos = MqttClientSession.toMqttQos(address.deliveryGuarantee());

    return session
        .publish(topic, qos, address.retain(), address.contentTypeHint(), payload)
        .whenComplete(
            (v, ex) -> {
              if (ex != null) {
                LOGGER.debug(
                    "connection '{}': publish to '{}' failed: {}",
                    session.config().name(),
                    topic,
                    ex.getMessage());
              }
            });
  }

  @Override
  public CompletableFuture<Void> closeAsync() {
    if (closed.compareAndSet(false, true)) {
      if (transportStateListener != null) {
        session.removeTransportStateListener(transportStateListener);
      }
      return provider.releaseSession(session);
    } else {
      return CompletableFuture.completedFuture(null);
    }
  }
}
