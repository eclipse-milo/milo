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
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttClientSession.SubscriptionEntry;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerQualityOfService;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SubscriberChannel} that subscribes the connection's reader queues on the shared MQTT
 * client and delivers received payloads to the engine.
 *
 * <p>At open, the channel subscribes the de-duplicated set of every reader's data queue and (when
 * configured) metadata queue, taking the highest resolved QoS when readers share a queue. The
 * session re-issues these subscriptions on every reconnect. Decode is content-based in the engine,
 * so data and metadata payloads flow through the same consumer.
 *
 * <p>Delivery: payloads are wrapped in unpooled buffers and handed to the context's topic-aware
 * consumer (the source topic is always known on MQTT), on a single Netty event loop of the
 * context's group so arrival order is preserved — matching the UDP transport's delivery threading.
 * The buffer is released after the callback returns, per the {@link
 * SubscriberTransportContext#messageConsumer()} contract.
 */
final class MqttSubscriberChannel implements SubscriberChannel {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubscriberChannel.class);

  private final AtomicBoolean closed = new AtomicBoolean(false);

  private final MqttTransportProvider provider;
  private final MqttClientSession session;
  private final List<SubscriptionEntry> entries;

  private MqttSubscriberChannel(
      MqttTransportProvider provider, MqttClientSession session, List<SubscriptionEntry> entries) {

    this.provider = provider;
    this.session = session;
    this.entries = entries;
  }

  /**
   * Open a subscriber channel: register the connection's reader queue subscriptions with the
   * session (issued immediately if connected, and on every (re)connect).
   *
   * @param provider the owning provider (for session release).
   * @param session the connection's shared client session.
   * @param connection the connection config the subscriptions derive from.
   * @param context the transport context supplying the consumers and event loop group.
   * @return an open {@link MqttSubscriberChannel}.
   */
  static MqttSubscriberChannel open(
      MqttTransportProvider provider,
      MqttClientSession session,
      MqttConnectionConfig connection,
      SubscriberTransportContext context) {

    var channel = new MqttSubscriberChannel(provider, session, new ArrayList<>());
    channel.entries.addAll(channel.buildSubscriptions(connection, context));

    session.addSubscriptions(channel.entries);

    return channel;
  }

  /**
   * The de-duplicated reader data + metadata queue subscriptions of {@code connection}, merged at
   * the highest QoS per topic filter.
   */
  private List<SubscriptionEntry> buildSubscriptions(
      MqttConnectionConfig connection, SubscriberTransportContext context) {

    var topicQos = new LinkedHashMap<String, MqttQos>();

    for (ReaderGroupConfig group : connection.readerGroups()) {
      for (DataSetReaderConfig reader : group.getDataSetReaders()) {
        BrokerTransportSettings settings = reader.getBrokerTransport();
        if (settings == null) {
          // tolerated for disabled readers; the engine rejects enabled readers without a
          // data queueName at startup
          continue;
        }

        String dataQueue = settings.getQueueName();
        if (dataQueue != null && !dataQueue.isEmpty()) {
          // the ReaderGroup has no broker transport parameters (§6.4.2.4): group settings null
          mergeQos(
              topicQos,
              dataQueue,
              MqttClientSession.toMqttQos(BrokerQualityOfService.resolveData(null, settings)));
        }

        String metaDataQueue = settings.getMetaDataQueueName();
        if (metaDataQueue != null && !metaDataQueue.isEmpty()) {
          mergeQos(
              topicQos,
              metaDataQueue,
              MqttClientSession.toMqttQos(BrokerQualityOfService.resolveMetaData(null, settings)));
        }
      }
    }

    // one event loop (not the group) so delivery preserves arrival order, as UDP channels do
    Executor callbackExecutor = context.eventLoopGroup().next();

    BiConsumer<String, byte[]> consumer = deliveryConsumer(context);

    var entries = new ArrayList<SubscriptionEntry>(topicQos.size());
    topicQos.forEach(
        (topicFilter, qos) ->
            entries.add(new SubscriptionEntry(topicFilter, qos, consumer, callbackExecutor)));
    return entries;
  }

  private BiConsumer<String, byte[]> deliveryConsumer(SubscriberTransportContext context) {
    @Nullable BiConsumer<String, ByteBuf> topicMessageConsumer = context.topicMessageConsumer();
    Consumer<ByteBuf> messageConsumer = context.messageConsumer();

    return (topic, payload) -> {
      if (closed.get()) {
        return;
      }

      ByteBuf buffer = Unpooled.wrappedBuffer(payload);
      try {
        if (topicMessageConsumer != null) {
          topicMessageConsumer.accept(topic, buffer);
        } else {
          messageConsumer.accept(buffer);
        }
      } catch (Throwable t) {
        LOGGER.warn(
            "connection '{}': message consumer failed for topic '{}'",
            session.config().name(),
            topic,
            t);
      } finally {
        buffer.release();
      }
    };
  }

  private static void mergeQos(Map<String, MqttQos> topicQos, String topicFilter, MqttQos qos) {
    topicQos.merge(topicFilter, qos, (a, b) -> a.getCode() >= b.getCode() ? a : b);
  }

  @Override
  public CompletableFuture<Void> closeAsync() {
    if (closed.compareAndSet(false, true)) {
      session.removeSubscriptions(entries);
      return provider.releaseSession(session);
    } else {
      return CompletableFuture.completedFuture(null);
    }
  }
}
