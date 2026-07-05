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
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.assertStartupFails;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.assertUaFailure;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitTrue;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.freeTcpPort;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.lastError;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mapSource;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.nio.NioEventLoopGroup;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherStatusMode;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Provider and channel behavior that does not require a running broker: connection property
 * parsing, startup validation, the publish fail-fast guard, and channel argument/state guards.
 *
 * <p>All broker URIs in this class point at loopback ports that are guaranteed unbound, so the
 * HiveMQ client never connects; its automatic reconnect retries quietly until the channels are
 * closed.
 */
class MqttTransportConfigTest {

  private static final MessageAddress DATA_ADDRESS =
      MessageAddress.of(
          "milo/test/data",
          BrokerTransportQualityOfService.AtMostOnce,
          false,
          MessageAddress.Kind.DATA,
          MessageAddress.CONTENT_TYPE_UADP);

  private static NioEventLoopGroup eventLoopGroup;

  private final List<PublisherChannel> channels = new CopyOnWriteArrayList<>();
  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void createEventLoopGroup() {
    eventLoopGroup = new NioEventLoopGroup(1);
  }

  @AfterAll
  static void shutdownEventLoopGroup() throws InterruptedException {
    eventLoopGroup.shutdownGracefully(0, 1, TimeUnit.SECONDS).await(5, TimeUnit.SECONDS);
  }

  @AfterEach
  void tearDown() throws Exception {
    for (PublisherChannel channel : channels) {
      try {
        channel.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup
      }
    }
    channels.clear();

    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup
      }
    }
    services.clear();
  }

  // region 0:MqttVersion property

  @Test
  void versionModeDefaultsToBestAvailableAndAcceptsPinnedValues() throws Exception {
    assertEquals(
        MqttClientSession.VersionMode.BEST_AVAILABLE,
        MqttClientSession.versionMode(connBuilder("c", 1883).build()));

    assertEquals(
        MqttClientSession.VersionMode.MQTT_5,
        MqttClientSession.versionMode(
            connBuilder("c", 1883)
                .property(
                    MqttTransportProvider.MQTT_VERSION_PROPERTY,
                    Variant.ofString(MqttTransportProvider.MQTT_VERSION_5_0))
                .build()));

    assertEquals(
        MqttClientSession.VersionMode.MQTT_3_1_1,
        MqttClientSession.versionMode(
            connBuilder("c", 1883)
                .property(
                    MqttTransportProvider.MQTT_VERSION_PROPERTY,
                    Variant.ofString(MqttTransportProvider.MQTT_VERSION_3_1_1))
                .build()));

    assertEquals(
        MqttClientSession.VersionMode.BEST_AVAILABLE,
        MqttClientSession.versionMode(
            connBuilder("c", 1883)
                .property(
                    MqttTransportProvider.MQTT_VERSION_PROPERTY,
                    Variant.ofString(MqttTransportProvider.MQTT_VERSION_BEST_AVAILABLE))
                .build()));
  }

  @Test
  void invalidVersionPropertyValueIsBadConfigurationError() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MqttClientSession.versionMode(
                    connBuilder("c", 1883)
                        .property(
                            MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofString("4.0"))
                        .build()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());

    UaException e2 =
        assertThrows(
            UaException.class,
            () ->
                MqttClientSession.versionMode(
                    connBuilder("c", 1883)
                        .property(MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofInt32(5))
                        .build()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e2.getStatusCode().value());
  }

  @Test
  void invalidVersionPropertyFailsChannelOpen() throws Exception {
    MqttConnectionConfig connection =
        connBuilder("bad-version", freeTcpPort())
            .property(MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofString("MQTTv9"))
            .build();

    MqttTransportProvider provider = MqttTransportProvider.create();

    UaException e =
        assertThrows(
            UaException.class,
            () -> provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup)));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void invalidVersionPropertyFailsServiceStartup() throws Exception {
    // the reader has a data queueName, so M7 validation passes and the failure is the
    // channel open at connection startup
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                connBuilder("sub-conn", freeTcpPort())
                    .property(MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofString("9.9"))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName("milo/test/data")
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService service = track(PubSubService.create(config, null, mqttServiceConfig()));

    assertEquals(StatusCodes.Bad_ConfigurationError, assertStartupFails(service).value());
  }

  // endregion

  // region ClientId resolution

  @Test
  void clientIdPropertyOverrideWins() throws Exception {
    MqttConnectionConfig connection =
        connBuilder("c", 1883)
            .property(MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY, Variant.ofString("my-client"))
            .build();

    assertEquals("my-client", MqttClientSession.clientId(connection));
  }

  @Test
  void clientIdFallsBackToCanonicalPublisherId() throws Exception {
    assertEquals(
        "7",
        MqttClientSession.clientId(
            connBuilder("c", 1883).publisherId(PublisherId.uint16(ushort(7))).build()));

    assertEquals(
        "pub",
        MqttClientSession.clientId(
            connBuilder("c", 1883).publisherId(PublisherId.string("pub")).build()));
  }

  @Test
  void clientIdIsRandomForPublisherLessConnections() throws Exception {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("c").brokerUri(URI.create("mqtt://127.0.0.1:1883")).build();

    String clientId = MqttClientSession.clientId(connection);
    assertTrue(clientId.startsWith("milo-"), "random ClientId starts with 'milo-': " + clientId);
    assertNotEquals(clientId, MqttClientSession.clientId(connection));
  }

  @Test
  void invalidClientIdPropertyIsBadConfigurationError() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MqttClientSession.clientId(
                    connBuilder("c", 1883)
                        .property(
                            MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY, Variant.ofString(""))
                        .build()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());

    UaException e2 =
        assertThrows(
            UaException.class,
            () ->
                MqttClientSession.clientId(
                    connBuilder("c", 1883)
                        .property(MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY, Variant.ofInt32(1))
                        .build()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e2.getStatusCode().value());
  }

  // endregion

  // region QoS mapping

  @Test
  void qosMappingFollowsPart14Table204() {
    assertEquals(
        MqttQos.AT_LEAST_ONCE,
        MqttClientSession.toMqttQos(BrokerTransportQualityOfService.AtLeastOnce));
    assertEquals(
        MqttQos.EXACTLY_ONCE,
        MqttClientSession.toMqttQos(BrokerTransportQualityOfService.ExactlyOnce));
    assertEquals(
        MqttQos.AT_MOST_ONCE,
        MqttClientSession.toMqttQos(BrokerTransportQualityOfService.AtMostOnce));
    assertEquals(
        MqttQos.AT_MOST_ONCE,
        MqttClientSession.toMqttQos(BrokerTransportQualityOfService.BestEffort));
    assertEquals(
        MqttQos.AT_MOST_ONCE,
        MqttClientSession.toMqttQos(BrokerTransportQualityOfService.NotSpecified));
  }

  // endregion

  // region provider selection

  @Test
  void providerSupportsOnlyMqttConnections() throws Exception {
    MqttTransportProvider provider = MqttTransportProvider.create();

    assertTrue(provider.supports(connBuilder("c", 1883).build()));

    UdpConnectionConfig udpConnection =
        PubSubConnectionConfig.udp("u")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 47001))
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 47002))
            .build();

    assertFalse(provider.supports(udpConnection));

    // nothing is started here, so the UDP addresses are never bound
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                provider.openPublisher(
                    PublisherTransportContext.of(udpConnection, eventLoopGroup)));
    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
  }

  // endregion

  // region publisher status mode

  @Test
  void willPublisherStatusModeRejectsMixedMappings() throws Exception {
    MqttConnectionConfig connection =
        connBuilder("will-mixed", freeTcpPort())
            .publisherStatusMode(PublisherStatusMode.WILL)
            .writerGroup(
                WriterGroupConfig.builder("json")
                    .writerGroupId(ushort(1))
                    .messageSettings(JsonWriterGroupSettings.builder().build())
                    .build())
            .writerGroup(
                WriterGroupConfig.builder("uadp")
                    .writerGroupId(ushort(2))
                    .messageSettings(UadpWriterGroupSettings.builder().build())
                    .build())
            .build();

    MqttTransportProvider provider = MqttTransportProvider.create();

    UaException e =
        assertThrows(
            UaException.class,
            () -> provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup)));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("publisherStatusMode WILL"), e.getMessage());
  }

  // endregion

  // region channel guards

  @Test
  void plainSendIsRejectedAndReleasesTheBuffer() throws Exception {
    PublisherChannel channel = openPublisherToUnboundPort();

    ByteBuf message = Unpooled.wrappedBuffer(new byte[] {1, 2, 3});
    UaException e = assertUaFailure(channel.send(message));

    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
    assertEquals(0, message.refCnt(), "plain send must release the buffer");
  }

  @Test
  void sendWhileNotConnectedFailsFastWithBadServerNotConnected() throws Exception {
    PublisherChannel channel = openPublisherToUnboundPort();

    // load-bearing fail-fast: HiveMQ's "async" publish would block the calling thread until
    // the first successful connect, so the channel must fail without touching the publish API
    ByteBuf message = Unpooled.wrappedBuffer(new byte[] {1, 2, 3});
    UaException e = assertUaFailure(channel.send(message, DATA_ADDRESS));

    assertEquals(StatusCodes.Bad_ServerNotConnected, e.getStatusCode().value());
    assertEquals(0, message.refCnt(), "addressed send must release the buffer");
  }

  @Test
  void addressWithoutQueueNameIsBadConfigurationError() throws Exception {
    PublisherChannel channel = openPublisherToUnboundPort();

    MessageAddress noQueue =
        MessageAddress.of(
            null,
            BrokerTransportQualityOfService.AtMostOnce,
            false,
            MessageAddress.Kind.DATA,
            MessageAddress.CONTENT_TYPE_UADP);

    UaException e = assertUaFailure(channel.send(Unpooled.wrappedBuffer(new byte[] {1}), noQueue));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void sendOnClosedChannelIsBadInvalidState() throws Exception {
    PublisherChannel channel = openPublisherToUnboundPort();
    channel.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    ByteBuf message = Unpooled.wrappedBuffer(new byte[] {1, 2, 3});
    UaException e = assertUaFailure(channel.send(message, DATA_ADDRESS));

    assertEquals(StatusCodes.Bad_InvalidState, e.getStatusCode().value());
    assertEquals(0, message.refCnt());

    // repeat close is a no-op
    channel.closeAsync().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  // endregion

  // region engine validation and diagnostics

  @Test
  void enabledReaderWithoutDataQueueNameFailsStartup() throws Exception {
    // M7: a broker reader cannot derive its data topic (it does not know the publisher's
    // WriterGroup name), so a missing queueName is a configuration error at startup
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                connBuilder("sub-conn", freeTcpPort())
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(DataSetReaderConfig.builder("reader").build())
                            .build())
                    .build())
            .build();

    PubSubService service = track(PubSubService.create(config, null, mqttServiceConfig()));

    assertEquals(StatusCodes.Bad_ConfigurationError, assertStartupFails(service).value());
  }

  @Test
  void unreachableBrokerSurfacesSendFailuresInDiagnosticsWithoutHanging() throws Exception {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds")
            .field(FieldDefinition.builder("value").dataType(NodeIds.Int32).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                connBuilder("pub-conn", freeTcpPort())
                    .publisherId(PublisherId.string("unreachable"))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(100))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w1")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    var values = new AtomicReference<>(Map.of("value", new DataValue(Variant.ofInt32(1))));

    PubSubService service =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build(),
                mqttServiceConfig()));

    // channel open is non-blocking and the connect retries in the background, so startup
    // completes even though the broker is unreachable
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // every publish fails fast (Bad_ServerNotConnected from the channel) and is recorded by
    // the engine as a send failure on the writer group path; the real channel code surfaces
    // un-flattened. The session never connected, so no transport-down edge fires and the
    // connection is never failed to Error.
    awaitTrue(
        () -> {
          var error = lastError(service, "pub-conn/grp");
          return error != null && error.value() == StatusCodes.Bad_ServerNotConnected;
        },
        "Bad_ServerNotConnected diagnostics on the writer group");

    // and shutdown completes promptly despite the broker never having been reachable
    service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private PublisherChannel openPublisherToUnboundPort() throws Exception {
    MqttConnectionConfig connection = connBuilder("unbound", freeTcpPort()).build();

    MqttTransportProvider provider = MqttTransportProvider.create();
    PublisherChannel channel =
        provider.openPublisher(PublisherTransportContext.of(connection, eventLoopGroup));
    channels.add(channel);
    return channel;
  }

  private static MqttConnectionConfig.Builder connBuilder(String name, int port) {
    return PubSubConnectionConfig.mqtt(name)
        .brokerUri(URI.create("mqtt://127.0.0.1:" + port))
        .publisherId(PublisherId.string(name));
  }

  // endregion
}
