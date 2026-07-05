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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttClientSession.SubscriptionEntry;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerQualityOfService;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SubscriberChannel} that subscribes the connection's reader queues on the shared MQTT
 * client and delivers received payloads to the engine.
 *
 * <p>At open, the channel subscribes the de-duplicated set of every reader's data queue, configured
 * metadata queue, and JSON status topic filter, taking the highest resolved QoS when readers share
 * a queue or filter. The session re-issues these subscriptions on every reconnect. Decode is
 * content-based in the engine, so data, metadata, and status payloads flow through the same
 * topic-aware consumer; the topic is preserved for publisher-status events.
 *
 * <p>Delivery: payloads are wrapped in unpooled buffers and handed to the context's topic-aware
 * consumer (the source topic is always known on MQTT), on a single Netty event loop of the
 * context's group so arrival order is preserved — matching the UDP transport's delivery threading.
 * The buffer is released after the callback returns, per the {@link
 * SubscriberTransportContext#messageConsumer()} contract.
 *
 * <p>The context's {@code TransportStateListener}, when present, is registered with the shared
 * session at open and removed at close, so the engine observes broker liveness edges for the
 * channel's lifetime.
 */
final class MqttSubscriberChannel implements SubscriberChannel {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubscriberChannel.class);

  private final AtomicBoolean closed = new AtomicBoolean(false);

  private final MqttTransportProvider provider;
  private final MqttClientSession session;
  private final List<SubscriptionEntry> entries;
  private final @Nullable TransportStateListener transportStateListener;

  private MqttSubscriberChannel(
      MqttTransportProvider provider,
      MqttClientSession session,
      List<SubscriptionEntry> entries,
      @Nullable TransportStateListener transportStateListener) {

    this.provider = provider;
    this.session = session;
    this.entries = entries;
    this.transportStateListener = transportStateListener;
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

    var channel =
        new MqttSubscriberChannel(
            provider, session, new ArrayList<>(), context.transportStateListener());
    channel.entries.addAll(channel.buildSubscriptions(connection, context));

    if (channel.transportStateListener != null) {
      session.addTransportStateListener(channel.transportStateListener);
    }

    session.addSubscriptions(channel.entries);

    return channel;
  }

  /**
   * The de-duplicated reader data, metadata, and JSON status subscriptions of {@code connection},
   * merged at the highest QoS per topic filter.
   */
  private List<SubscriptionEntry> buildSubscriptions(
      MqttConnectionConfig connection, SubscriberTransportContext context) {

    var topicQos = new LinkedHashMap<String, MqttQos>();
    var exactStatusTopicFilters = new LinkedHashSet<String>();
    String wildcardStatusTopicFilter = null;

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

        StatusSubscription statusSubscription = statusSubscription(connection, reader);
        if (statusSubscription != null) {
          if (statusSubscription.wildcard()) {
            wildcardStatusTopicFilter = statusSubscription.topicFilter();
          } else {
            exactStatusTopicFilters.add(statusSubscription.topicFilter());
          }
        }
      }
    }

    if (wildcardStatusTopicFilter != null) {
      mergeQos(topicQos, wildcardStatusTopicFilter, MqttQos.AT_LEAST_ONCE);
    } else {
      mergeStatusQos(topicQos, exactStatusTopicFilters);
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
    BiConsumer<String, ByteBuf> topicMessageConsumer = context.topicMessageConsumer();
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

  private static void mergeStatusQos(Map<String, MqttQos> topicQos, Set<String> topicFilters) {
    for (String topicFilter : topicFilters) {
      mergeQos(topicQos, topicFilter, MqttQos.AT_LEAST_ONCE);
    }
  }

  private static @Nullable StatusSubscription statusSubscription(
      MqttConnectionConfig connection, DataSetReaderConfig reader) {

    String mappingName = mappingNameOf(reader.getSettings());
    if (!"json".equals(mappingName)) {
      return null;
    }

    PublisherId publisherId = reader.getPublisherId();
    String prefix = BrokerTopics.topicPrefix(connection);
    if (publisherId != null) {
      return new StatusSubscription(
          BrokerTopics.statusTopic(prefix, mappingName, publisherId), false);
    } else {
      return new StatusSubscription(prefix + "/" + mappingName + "/status/+", true);
    }
  }

  private static String mappingNameOf(DataSetReaderMessageSettings settings) {
    if (settings instanceof UadpDataSetReaderSettings) {
      return "uadp";
    } else if (settings instanceof JsonDataSetReaderSettings) {
      return "json";
    } else {
      return settings.getClass().getSimpleName();
    }
  }

  private record StatusSubscription(String topicFilter, boolean wildcard) {}

  @Override
  public CompletableFuture<Void> closeAsync() {
    if (closed.compareAndSet(false, true)) {
      if (transportStateListener != null) {
        session.removeTransportStateListener(transportStateListener);
      }
      session.removeSubscriptions(entries);
      return provider.releaseSession(session);
    } else {
      return CompletableFuture.completedFuture(null);
    }
  }
}
