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

import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.nio.NioEventLoopGroup;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Provider and channel lifecycle behavior against a live embedded broker: MQTT version modes,
 * ClientId resolution observable at the broker, and the reference-counted shared client session.
 */
class MqttChannelLifecycleTest {

  @TempDir static Path tempDir;

  private static EmbeddedTestBroker broker;
  private static NioEventLoopGroup eventLoopGroup;

  @BeforeAll
  static void startBroker() throws Exception {
    broker = EmbeddedTestBroker.start(tempDir);
    eventLoopGroup = new NioEventLoopGroup(1);
  }

  @AfterAll
  static void stopBroker() throws InterruptedException {
    eventLoopGroup.shutdownGracefully(0, 1, TimeUnit.SECONDS).await(5, TimeUnit.SECONDS);
    broker.stop();
  }

  @Test
  void bestAvailableConnectsAndRoundTrips() throws Exception {
    // CE is MQTT 5 capable: BestAvailable connects with MQTT 5 and works end to end
    roundTripLeg("ba-conn", null);
  }

  @Test
  void pinnedMqtt311ConnectsAndRoundTrips() throws Exception {
    roundTripLeg("v3-conn", MqttTransportProvider.MQTT_VERSION_3_1_1);
  }

  @Test
  void clientIdPropertyOverrideIsObservableAtTheBroker() throws Exception {
    String clientId = "milo-takeover-test";

    // connect a raw client with the same ClientId first; when the channel's session connects
    // with the overridden id, the broker MUST take over and disconnect the raw client
    try (RawMqtt5Probe victim = RawMqtt5Probe.connect(broker.port(), clientId)) {
      MqttConnectionConfig connection =
          PubSubConnectionConfig.mqtt("clientid-conn")
              .brokerUri(brokerUri())
              .publisherId(PublisherId.string("clientid-conn"))
              .property(MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY, Variant.ofString(clientId))
              .build();

      MqttTransportProvider provider = MqttTransportProvider.create();
      PublisherChannel channel =
          provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup));
      try {
        assertTrue(
            victim.awaitDisconnected(TIMEOUT),
            "the broker must disconnect the raw client holding the overridden ClientId");
      } finally {
        channel.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      }
    }
  }

  @Test
  void channelCloseIsReferenceCounted() throws Exception {
    String dataTopic = "milo/test/refcount/data";
    MqttConnectionConfig connection = connectionConfig("refcount-conn", dataTopic, null);
    MessageAddress address = dataAddress(dataTopic);

    MqttTransportProvider provider = MqttTransportProvider.create();

    var received = new LinkedBlockingQueue<String>();

    PublisherChannel publisher =
        provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup));
    SubscriberChannel subscriber =
        provider.openSubscriber(
            SubscriberTransportContext.of(
                connection,
                eventLoopGroup,
                buf -> {},
                (topic, buf) ->
                    received.add(
                        topic
                            + "|"
                            + new String(ByteBufUtil.getBytes(buf), StandardCharsets.UTF_8))));

    // both channels share one connected session
    publishUntilDelivered(publisher, address, dataTopic, "before-close", received);

    // closing the subscriber releases one reference; the publisher keeps working
    subscriber.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher
        .send(Unpooled.wrappedBuffer("after-sub-close".getBytes(StandardCharsets.UTF_8)), address)
        .get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // closing the publisher releases the last reference and disconnects the session
    publisher.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // reopening after a full close creates a fresh client that connects again
    var receivedAfterReopen = new LinkedBlockingQueue<String>();
    PublisherChannel reopened =
        provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup));
    SubscriberChannel reopenedSubscriber =
        provider.openSubscriber(
            SubscriberTransportContext.of(
                connection,
                eventLoopGroup,
                buf -> {},
                (topic, buf) ->
                    receivedAfterReopen.add(
                        topic
                            + "|"
                            + new String(ByteBufUtil.getBytes(buf), StandardCharsets.UTF_8))));
    try {
      publishUntilDelivered(reopened, address, dataTopic, "after-reopen", receivedAfterReopen);
    } finally {
      reopenedSubscriber.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      reopened.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    }
  }

  // region helpers

  /**
   * One full channel-level round trip: open a publisher and a subscriber channel on the same
   * connection, publish to the reader's data queue until the topic-aware consumer delivers it.
   */
  private void roundTripLeg(String name, String version) throws Exception {
    String dataTopic = "milo/test/" + name + "/data";
    MqttConnectionConfig connection = connectionConfig(name, dataTopic, version);
    MessageAddress address = dataAddress(dataTopic);

    MqttTransportProvider provider = MqttTransportProvider.create();

    var received = new LinkedBlockingQueue<String>();
    var plainReceived = new LinkedBlockingQueue<byte[]>();

    PublisherChannel publisher =
        provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup));
    SubscriberChannel subscriber =
        provider.openSubscriber(
            SubscriberTransportContext.of(
                connection,
                eventLoopGroup,
                buf -> plainReceived.add(ByteBufUtil.getBytes(buf)),
                (topic, buf) ->
                    received.add(
                        topic
                            + "|"
                            + new String(ByteBufUtil.getBytes(buf), StandardCharsets.UTF_8))));

    try {
      publishUntilDelivered(publisher, address, dataTopic, "hello-" + name, received);

      // the topic-aware consumer takes precedence over the plain consumer
      assertTrue(plainReceived.isEmpty(), "plain consumer must not be used");
    } finally {
      subscriber.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      publisher.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    }
  }

  /**
   * Publish {@code payload} repeatedly until the subscriber delivers it: the initial connect and
   * the SUBACK are both asynchronous, so sends are retried while the session connects and early QoS
   * 1 publishes may precede the subscription.
   */
  private static void publishUntilDelivered(
      PublisherChannel publisher,
      MessageAddress address,
      String expectedTopic,
      String payload,
      LinkedBlockingQueue<String> received)
      throws Exception {

    String expected = expectedTopic + "|" + payload;
    byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);

    long deadline = System.nanoTime() + Duration.ofSeconds(20).toNanos();
    while (System.nanoTime() < deadline) {
      try {
        publisher.send(Unpooled.wrappedBuffer(payloadBytes), address).get(2, TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // not yet connected; retry
        Thread.sleep(150);
        continue;
      }

      String delivery = received.poll(500, TimeUnit.MILLISECONDS);
      if (delivery != null) {
        assertEquals(expected, delivery);
        return;
      }
    }

    fail("timed out waiting for the round trip of '" + expected + "'");
  }

  private static URI brokerUri() {
    return URI.create("mqtt://127.0.0.1:" + broker.port());
  }

  private static MessageAddress dataAddress(String topic) {
    return MessageAddress.of(
        topic,
        BrokerTransportQualityOfService.AtLeastOnce,
        false,
        MessageAddress.Kind.DATA,
        MessageAddress.CONTENT_TYPE_UADP);
  }

  /**
   * A connection with one reader whose data queue is {@code dataTopic} (the subscriber channel
   * subscribes the reader queues at open), optionally pinning an MQTT version.
   */
  private static MqttConnectionConfig connectionConfig(
      String name, String dataTopic, String version) {

    MqttConnectionConfig.Builder builder =
        PubSubConnectionConfig.mqtt(name)
            .brokerUri(brokerUri())
            .publisherId(PublisherId.string(name))
            .readerGroup(
                ReaderGroupConfig.builder("rg")
                    .dataSetReader(
                        DataSetReaderConfig.builder("r1")
                            .publisherId(PublisherId.string(name))
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .queueName(dataTopic)
                                    .requestedDeliveryGuarantee(
                                        BrokerTransportQualityOfService.AtLeastOnce)
                                    .build())
                            .build())
                    .build());

    if (version != null) {
      builder.property(MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofString(version));
    }

    return builder.build();
  }

  // endregion
}
