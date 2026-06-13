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
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitEvent;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitTrue;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.counter;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mapSource;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * End-to-end UADP-over-MQTT against an embedded HiveMQ CE broker: a publisher service and a
 * subscriber service exchanging UADP NetworkMessages through real broker topics, with a raw MQTT 5
 * probe client asserting the broker-side view (derived topics, QoS, retain, content type).
 *
 * <p>Topic pins (Part 14 §7.3.4.7, prefix "opcua"): data = {@code
 * opcua/uadp/data/<publisherId>/<writerGroupName>}, metadata = {@code
 * opcua/uadp/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>}; configured queue names
 * always win.
 */
class MqttUadpEndToEndTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4711));

  private static final String DATA_TOPIC = "opcua/uadp/data/4711/grp";
  private static final String META_TOPIC = "opcua/uadp/metadata/4711/grp/writer-a";

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xA1L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xA2L);
  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 0xB1L);

  private static final UadpWriterGroupSettings GROUP_SETTINGS =
      UadpWriterGroupSettings.builder()
          .networkMessageContentMask(
              UadpNetworkMessageContentMask.of(
                  UadpNetworkMessageContentMask.Field.PublisherId,
                  UadpNetworkMessageContentMask.Field.GroupHeader,
                  UadpNetworkMessageContentMask.Field.WriterGroupId,
                  UadpNetworkMessageContentMask.Field.SequenceNumber,
                  UadpNetworkMessageContentMask.Field.PayloadHeader))
          .build();

  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.Status,
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  @TempDir static Path tempDir;

  private static EmbeddedTestBroker broker;

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startBroker() throws Exception {
    broker = EmbeddedTestBroker.start(tempDir);
  }

  @AfterAll
  static void stopBroker() {
    broker.stop();
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  @Test
  void endToEndPublishSubscribeOverMqtt() throws Exception {
    PubSubService publisher = startPublisher(null, null);
    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = startSubscriber(events);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);

    assertEquals(ushort(1), event.dataSetWriterId());
    assertEquals(ushort(1), event.writerGroupId());
    assertEquals("ds-a", event.dataSetName());
    assertNotNull(event.metaData());
    assertEquals(uint(7), event.metaData().getConfigurationVersionMajor());
    assertEquals(uint(3), event.metaData().getConfigurationVersionMinor());

    List<DataSetFieldValue> fields = event.fields();
    assertEquals(2, fields.size());
    assertEquals("temperature", fields.get(0).name());
    assertEquals(TEMPERATURE_FIELD_ID, fields.get(0).dataSetFieldId());
    assertEquals(21.5, fields.get(0).value().value().value());
    assertEquals("status", fields.get(1).name());
    assertEquals(STATUS_FIELD_ID, fields.get(1).dataSetFieldId());
    assertEquals("running", fields.get(1).value().value().value());

    // the reader completed startup on its first decoded DataSetMessage
    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-a").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(reader));

    // diagnostics tick on both sides
    awaitTrue(
        () ->
            counter(
                    publisher,
                    "pub-conn/grp",
                    PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
                > 0,
        "publisher writer group networkMessagesSent > 0");
    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp/reader-a",
                    PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived)
                > 0,
        "subscriber reader dataSetMessagesReceived > 0");
  }

  @Test
  void dataPublishesOnDerivedTopicWithUadpContentType() throws Exception {
    startPublisher(null, null);

    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(DATA_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish = probe.awaitMessage(TIMEOUT);

      assertEquals(DATA_TOPIC, publish.getTopic().toString());
      assertTrue(publish.getPayloadAsBytes().length > 0);

      // data defaults (Part 14 Table 204): QoS 0 after NotSpecified resolution, retain false
      assertEquals(MqttQos.AT_MOST_ONCE, publish.getQos());
      assertFalse(publish.isRetain());

      // MQTT 5 publishes carry the §7.3.4.9 Content Type
      assertEquals(
          "application/opcua+uadp", publish.getContentType().map(Object::toString).orElse(null));
    }
  }

  @Test
  void retainedMetaDataReplaysToLateSubscriber() throws Exception {
    startPublisher(null, null);

    // wait until the publisher is demonstrably connected and publishing (metadata is
    // published at writer activation, before/alongside the first data publishes)
    try (RawMqtt5Probe dataProbe = RawMqtt5Probe.connect(broker.port())) {
      dataProbe.subscribe(DATA_TOPIC, MqttQos.AT_MOST_ONCE);
      dataProbe.awaitMessage(TIMEOUT);
    }

    // a LATE subscriber receives the metadata announcement via retained-message replay
    try (RawMqtt5Probe lateProbe = RawMqtt5Probe.connect(broker.port())) {
      lateProbe.subscribe(META_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish = lateProbe.awaitMessage(TIMEOUT);

      assertTrue(publish.isRetain(), "metadata must be retained");
      assertEquals(META_TOPIC, publish.getTopic().toString());

      // UADP discovery announcement NetworkMessage header byte (version 1, PublisherId,
      // ExtendedFlags1)
      byte[] payload = publish.getPayloadAsBytes();
      assertTrue(payload.length > 0);
      assertEquals((byte) 0x91, payload[0]);

      // metadata defaults (Part 14 Table 204): QoS 1 after NotSpecified resolution
      assertEquals(MqttQos.AT_LEAST_ONCE, publish.getQos());
      assertEquals(
          "application/opcua+uadp", publish.getContentType().map(Object::toString).orElse(null));
    }
  }

  @Test
  void metaDataRepublishesPeriodicallyWhenUpdateTimeIsSet() throws Exception {
    // writer-level broker settings are authoritative for the update time
    startPublisher(
        null, BrokerTransportSettings.builder().metaDataUpdateTime(Duration.ofMillis(250)).build());

    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(META_TOPIC, MqttQos.AT_LEAST_ONCE);

      // activation publish + periodic republishes; NB: metadata sequence numbers share the
      // per-PublisherId counter, so per-stream sequences are NOT asserted gap-free
      int count = probe.countMessages(3, TIMEOUT);
      assertTrue(count >= 3, "expected at least 3 metadata publications, got " + count);
    }
  }

  @Test
  void writerQueueNameOverrideRoutesThatWritersMessages() throws Exception {
    String customTopic = "milo/custom/ds-b";

    PublishedDataSetConfig dataSetA = dataSetA();
    PublishedDataSetConfig dataSetB =
        PublishedDataSetConfig.builder("ds-b")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .build();

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSetA)
            .publishedDataSet(dataSetB)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(brokerUri())
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(100))
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer-a")
                                    .dataSet(dataSetA.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            // §6.4.2.5.1: a writer-level queueName override puts this
                            // writer's DataSetMessages into their own NetworkMessages on
                            // the overridden topic
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer-b")
                                    .dataSet(dataSetB.ref())
                                    .dataSetWriterId(ushort(2))
                                    .settings(WRITER_SETTINGS)
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName(customTopic)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    var valuesA =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));
    var valuesB = new AtomicReference<>(Map.of("counter", new DataValue(Variant.ofInt32(42))));

    track(
            PubSubService.create(
                publisherConfig,
                PubSubBindings.builder()
                    .source(dataSetA.ref(), mapSource(valuesA))
                    .source(dataSetB.ref(), mapSource(valuesB))
                    .build(),
                mqttServiceConfig()))
        .startup()
        .get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // broker-side: the override topic actually carries traffic
    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(customTopic, MqttQos.AT_MOST_ONCE);
      Mqtt5Publish publish = probe.awaitMessage(TIMEOUT);
      assertEquals(customTopic, publish.getTopic().toString());
      assertTrue(publish.getPayloadAsBytes().length > 0);
    }

    // subscriber-side: reader-a subscribes only the derived group topic and receives ds-a;
    // reader-b subscribes only the override topic and receives ds-b
    DataSetMetaDataConfig metaDataB =
        DataSetMetaDataConfig.builder("ds-b")
            .field("counter", NodeIds.Int32, COUNTER_FIELD_ID)
            .build();

    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(brokerUri())
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                readerBuilder("reader-a", ushort(1), dataSetMetaDataA(), DATA_TOPIC)
                                    .build())
                            .dataSetReader(
                                readerBuilder("reader-b", ushort(2), metaDataB, customTopic)
                                    .build())
                            .build())
                    .build())
            .build();

    var eventsA = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var eventsB = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader-a"), eventsA::add)
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader-b"), eventsB::add)
                    .build(),
                mqttServiceConfig()));
    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent eventA = awaitEvent(eventsA, e -> true);
    assertEquals(ushort(1), eventA.dataSetWriterId());
    assertEquals("ds-a", eventA.dataSetName());

    DataSetReceivedEvent eventB = awaitEvent(eventsB, e -> true);
    assertEquals(ushort(2), eventB.dataSetWriterId());
    assertEquals("ds-b", eventB.dataSetName());
    assertEquals(42, eventB.fieldsByName().get("counter").value().value());
  }

  @Test
  void groupQosOverrideAppliesToDataPublishes() throws Exception {
    startPublisher(
        BrokerTransportSettings.builder()
            .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
            .build(),
        null);

    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(DATA_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish = probe.awaitMessage(TIMEOUT);
      assertEquals(MqttQos.AT_LEAST_ONCE, publish.getQos());
    }
  }

  // region fixtures

  private static URI brokerUri() {
    return URI.create("mqtt://127.0.0.1:" + broker.port());
  }

  private static PublishedDataSetConfig dataSetA() {
    return PublishedDataSetConfig.builder("ds-a")
        .field(
            FieldDefinition.builder("temperature")
                .dataType(NodeIds.Double)
                .dataSetFieldId(TEMPERATURE_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("status")
                .dataType(NodeIds.String)
                .dataSetFieldId(STATUS_FIELD_ID)
                .build())
        .configurationVersion(uint(7), uint(3))
        .build();
  }

  private static DataSetMetaDataConfig dataSetMetaDataA() {
    return DataSetMetaDataConfig.builder("ds-a")
        .field("temperature", NodeIds.Double, TEMPERATURE_FIELD_ID)
        .field("status", NodeIds.String, STATUS_FIELD_ID)
        .configurationVersion(uint(7), uint(3))
        .build();
  }

  private static DataSetReaderConfig.Builder readerBuilder(
      String name, UShort writerId, DataSetMetaDataConfig metaData, String queueName) {

    return DataSetReaderConfig.builder(name)
        .publisherId(PUBLISHER_ID)
        .writerGroupId(ushort(1))
        .dataSetWriterId(writerId)
        .dataSetMetaData(metaData)
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .brokerTransport(BrokerTransportSettings.builder().queueName(queueName).build());
  }

  /**
   * Start a publisher service: one MQTT connection, writer group "grp" at 100 ms with writer
   * "writer-a" publishing "ds-a".
   */
  private PubSubService startPublisher(
      @Nullable BrokerTransportSettings groupBrokerTransport,
      @Nullable BrokerTransportSettings writerBrokerTransport)
      throws Exception {

    PublishedDataSetConfig dataSet = dataSetA();

    DataSetWriterConfig.Builder writer =
        DataSetWriterConfig.builder("writer-a")
            .dataSet(dataSet.ref())
            .dataSetWriterId(ushort(1))
            .settings(WRITER_SETTINGS);
    if (writerBrokerTransport != null) {
      writer.brokerTransport(writerBrokerTransport);
    }

    WriterGroupConfig.Builder group =
        WriterGroupConfig.builder("grp")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(100))
            .messageSettings(GROUP_SETTINGS)
            .dataSetWriter(writer.build());
    if (groupBrokerTransport != null) {
      group.brokerTransport(groupBrokerTransport);
    }

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(brokerUri())
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(group.build())
                    .build())
            .build();

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    PubSubService publisher =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build(),
                mqttServiceConfig()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return publisher;
  }

  /** Start a subscriber service with reader "reader-a" on the derived data topic. */
  private PubSubService startSubscriber(LinkedBlockingQueue<DataSetReceivedEvent> events)
      throws Exception {

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(brokerUri())
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                readerBuilder("reader-a", ushort(1), dataSetMetaDataA(), DATA_TOPIC)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService subscriber =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader-a"), events::add)
                    .build(),
                mqttServiceConfig()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return subscriber;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  // endregion
}
