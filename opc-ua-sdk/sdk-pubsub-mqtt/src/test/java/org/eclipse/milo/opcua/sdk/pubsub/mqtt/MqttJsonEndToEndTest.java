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
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mapSource;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherStatusMode;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.json.JsonStatusCodec;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * End-to-end JSON-over-MQTT against an embedded HiveMQ CE broker: the built-in "json" message
 * mapping publishing ua-data and ua-metadata documents through real broker topics.
 *
 * <p>Topic pins (Part 14 §7.3.4.7): data = {@code opcua/json/data/<publisherId>/<writerGroupName>},
 * metadata = {@code opcua/json/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>}.
 *
 * <p>The publisher uses a UInt16 PublisherId while JSON carries publisher ids as strings; the
 * subscriber's UInt16 filter matches via the canonical string form, exercised end-to-end here.
 */
class MqttJsonEndToEndTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4242));

  private static final String DATA_TOPIC = "opcua/json/data/4242/grp";
  private static final String META_TOPIC = "opcua/json/metadata/4242/grp/writer-a";
  private static final String STATUS_TOPIC = "opcua/json/status/4242";
  private static final String DEDUPE_STATUS_TOPIC = "opcua/json/status/status-dedupe";

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xC1L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xC2L);

  private static final Duration SILENCE = Duration.ofMillis(300);

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
  void endToEndJsonPublishSubscribe() throws Exception {
    startPublisher();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = startSubscriber(configuredMetaDataReader(), events);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);

    assertEquals(ushort(1), event.dataSetWriterId());
    assertEquals("ds-a", event.dataSetName());
    assertNotNull(event.metaData());
    assertEquals(uint(7), event.metaData().getConfigurationVersionMajor());
    assertEquals(uint(3), event.metaData().getConfigurationVersionMinor());

    // name-keyed JSON payload fields are matched against the configured metadata
    List<DataSetFieldValue> fields = event.fields();
    assertEquals(2, fields.size());
    assertEquals("temperature", fields.get(0).name());
    assertEquals(TEMPERATURE_FIELD_ID, fields.get(0).dataSetFieldId());
    assertEquals(21.5, fields.get(0).value().value().value());
    assertEquals("status", fields.get(1).name());
    assertEquals(STATUS_FIELD_ID, fields.get(1).dataSetFieldId());
    assertEquals("running", fields.get(1).value().value().value());

    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-a").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(reader));
  }

  @Test
  void jsonDataIsUaDataDocumentOnDerivedTopic() throws Exception {
    startPublisher();

    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(DATA_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish = probe.awaitMessage(TIMEOUT);

      assertEquals(DATA_TOPIC, publish.getTopic().toString());
      assertEquals(MqttQos.AT_MOST_ONCE, publish.getQos());
      assertEquals("application/json", publish.getContentType().map(Object::toString).orElse(null));

      JsonObject document = parse(publish);
      assertEquals("ua-data", document.get("MessageType").getAsString());
      assertTrue(document.has("MessageId"));
      assertEquals("4242", document.get("PublisherId").getAsString());

      JsonArray messages = document.getAsJsonArray("Messages");
      assertEquals(1, messages.size());

      JsonObject dataSetMessage = messages.get(0).getAsJsonObject();
      assertEquals(1, dataSetMessage.get("DataSetWriterId").getAsInt());
      assertTrue(dataSetMessage.has("SequenceNumber"));

      JsonObject metaDataVersion = dataSetMessage.getAsJsonObject("MetaDataVersion");
      assertEquals(7, metaDataVersion.get("MajorVersion").getAsInt());
      assertEquals(3, metaDataVersion.get("MinorVersion").getAsInt());

      // default field encoding is Verbose: concrete scalar fields collapse to bare values
      JsonObject payload = dataSetMessage.getAsJsonObject("Payload");
      assertEquals(21.5, payload.get("temperature").getAsDouble());
      assertEquals("running", payload.get("status").getAsString());
    }
  }

  @Test
  void jsonMetaDataIsRetainedUaMetadataDocument() throws Exception {
    startPublisher();

    // ensure the publisher is connected and has activated (metadata publishes at activation)
    try (RawMqtt5Probe dataProbe = RawMqtt5Probe.connect(broker.port())) {
      dataProbe.subscribe(DATA_TOPIC, MqttQos.AT_MOST_ONCE);
      dataProbe.awaitMessage(TIMEOUT);
    }

    // a late subscriber receives the ua-metadata document via retained-message replay
    try (RawMqtt5Probe lateProbe = RawMqtt5Probe.connect(broker.port())) {
      lateProbe.subscribe(META_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish = lateProbe.awaitMessage(TIMEOUT);

      assertTrue(publish.isRetain(), "metadata must be retained");
      assertEquals(MqttQos.AT_LEAST_ONCE, publish.getQos());
      assertEquals("application/json", publish.getContentType().map(Object::toString).orElse(null));

      JsonObject document = parse(publish);
      assertEquals("ua-metadata", document.get("MessageType").getAsString());
      assertEquals(1, document.get("DataSetWriterId").getAsInt());

      JsonObject metaData = document.getAsJsonObject("MetaData");
      assertNotNull(metaData, "ua-metadata document carries the MetaData struct");
      assertEquals("ds-a", metaData.get("Name").getAsString());

      JsonArray metaDataFields = metaData.getAsJsonArray("Fields");
      assertEquals(2, metaDataFields.size());

      List<String> names =
          metaDataFields.asList().stream()
              .map(element -> element.getAsJsonObject().get("Name").getAsString())
              .toList();
      assertTrue(names.contains("temperature"), "field names in metadata: " + names);
      assertTrue(names.contains("status"), "field names in metadata: " + names);
    }
  }

  @Test
  void jsonStatusIsRetainedUaStatusDocument() throws Exception {
    startPublisher();

    try (RawMqtt5Probe watcher = RawMqtt5Probe.connect(broker.port())) {
      watcher.subscribe(STATUS_TOPIC, MqttQos.AT_LEAST_ONCE);
      watcher.awaitMessage(
          publish -> parse(publish).get("Status").getAsInt() == PubSubState.Operational.getValue(),
          TIMEOUT);
    }

    try (RawMqtt5Probe lateProbe = RawMqtt5Probe.connect(broker.port())) {
      lateProbe.subscribe(STATUS_TOPIC, MqttQos.EXACTLY_ONCE);

      Mqtt5Publish publish =
          lateProbe.awaitMessage(
              message ->
                  parse(message).get("Status").getAsInt() == PubSubState.Operational.getValue(),
              TIMEOUT);

      assertTrue(publish.isRetain(), "status must be retained");
      assertEquals(MqttQos.AT_LEAST_ONCE, publish.getQos());
      assertEquals("application/json", publish.getContentType().map(Object::toString).orElse(null));

      JsonObject document = parse(publish);
      assertEquals("ua-status", document.get("MessageType").getAsString());
      assertEquals("4242", document.get("PublisherId").getAsString());
      assertFalse(document.get("IsCyclic").getAsBoolean());
      assertEquals(PubSubState.Operational.getValue(), document.get("Status").getAsInt());
      assertFalse(document.has("Timestamp"));
      assertFalse(document.has("NextReportTime"));
    }
  }

  @Test
  void subscriberConsumesJsonStatusAndCorrelatesReader() throws Exception {
    var dataEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = startSubscriber(configuredMetaDataReader(), dataEvents);

    var statusEvents = new LinkedBlockingQueue<PublisherStatusReceivedEvent>();
    subscriber.addPublisherStatusListener(statusEvents::add);

    try (RawMqtt5Probe publisher = RawMqtt5Probe.connect(broker.port())) {
      publisher.publish(
          STATUS_TOPIC,
          MqttQos.AT_LEAST_ONCE,
          true,
          "application/json",
          JsonStatusCodec.encode(PUBLISHER_ID, PubSubState.Error, false, null, null));
    }

    PublisherStatusReceivedEvent event =
        awaitStatus(statusEvents, e -> e.status() == PubSubState.Error);
    assertEquals("sub-conn", event.connection().path());
    assertEquals("json", event.mappingName());
    assertEquals(STATUS_TOPIC, event.topic());
    assertEquals(PublisherId.string("4242"), event.publisherId());
    assertEquals(PubSubState.Error, event.status());
    assertFalse(event.timeout());

    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-a").orElseThrow();
    assertTrue(event.readers().contains(reader));
    assertTrue(dataEvents.isEmpty());
  }

  @Test
  void overlappingStatusSubscriptionsDeliverOneEvent() throws Exception {
    try (RawMqtt5Probe publisher = RawMqtt5Probe.connect(broker.port())) {
      publisher.publish(
          DEDUPE_STATUS_TOPIC,
          MqttQos.AT_LEAST_ONCE,
          true,
          "application/json",
          JsonStatusCodec.encode(
              "dedupe", PublisherId.string("status-dedupe"), PubSubState.Error, false, null, null));
    }

    var dataEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber =
        newSubscriber(List.of(statusExactReader(), statusWildcardReader()), dataEvents);

    var statusEvents = new LinkedBlockingQueue<PublisherStatusReceivedEvent>();
    subscriber.addPublisherStatusListener(statusEvents::add);
    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    Predicate<PublisherStatusReceivedEvent> matching = event -> "dedupe".equals(event.messageId());
    PublisherStatusReceivedEvent event = awaitStatus(statusEvents, matching);
    assertEquals(PublisherId.string("status-dedupe"), event.publisherId());

    assertNoStatus(statusEvents, matching, SILENCE);
  }

  @Test
  void mqttWillPublishesRetainedErrorStatusOnClientTakeover() throws Exception {
    startPublisher();

    try (RawMqtt5Probe watcher = RawMqtt5Probe.connect(broker.port())) {
      watcher.subscribe(STATUS_TOPIC, MqttQos.AT_LEAST_ONCE);
      watcher.awaitMessage(
          publish -> parse(publish).get("Status").getAsInt() == PubSubState.Operational.getValue(),
          TIMEOUT);

      try (RawMqtt5Probe takeover = RawMqtt5Probe.connect(broker.port(), "4242")) {
        watcher.awaitMessage(
            publish -> parse(publish).get("Status").getAsInt() == PubSubState.Error.getValue(),
            TIMEOUT);

        try (RawMqtt5Probe lateProbe = RawMqtt5Probe.connect(broker.port())) {
          lateProbe.subscribe(STATUS_TOPIC, MqttQos.AT_LEAST_ONCE);

          Mqtt5Publish retainedWill =
              lateProbe.awaitMessage(
                  publish ->
                      parse(publish).get("Status").getAsInt() == PubSubState.Error.getValue(),
                  TIMEOUT);

          assertTrue(retainedWill.isRetain(), "Will status must be retained for late subscribers");
          JsonObject document = parse(retainedWill);
          assertEquals("ua-status", document.get("MessageType").getAsString());
          assertFalse(document.get("IsCyclic").getAsBoolean());
          assertEquals(PubSubState.Error.getValue(), document.get("Status").getAsInt());
        }
      }
    }
  }

  @Test
  void publisherStatusDisabledSuppressesRetainedStatusAndWill() throws Exception {
    PublisherId publisherId =
        PublisherId.string("status-disabled-" + UUID.randomUUID().toString().substring(0, 8));
    String dataTopic = dataTopic(publisherId);
    String statusTopic = statusTopic(publisherId);

    try (RawMqtt5Probe statusProbe = RawMqtt5Probe.connect(broker.port())) {
      statusProbe.subscribe(statusTopic, MqttQos.AT_LEAST_ONCE);

      startPublisher(publisherId, PublisherStatusMode.DISABLED);

      try (RawMqtt5Probe dataProbe = RawMqtt5Probe.connect(broker.port())) {
        dataProbe.subscribe(dataTopic, MqttQos.AT_MOST_ONCE);
        dataProbe.awaitMessage(TIMEOUT);
      }

      assertEquals(0, statusProbe.countMessages(1, SILENCE));

      try (RawMqtt5Probe takeover =
          RawMqtt5Probe.connect(broker.port(), publisherId.toCanonicalString())) {
        assertEquals(0, statusProbe.countMessages(1, SILENCE));
      }
    }
  }

  @Test
  void publisherStatusCyclicPublishesRetainedStatusWithNextReportTime() throws Exception {
    PublisherId publisherId =
        PublisherId.string("status-cyclic-" + UUID.randomUUID().toString().substring(0, 8));
    String statusTopic = statusTopic(publisherId);

    try (RawMqtt5Probe statusProbe = RawMqtt5Probe.connect(broker.port())) {
      statusProbe.subscribe(statusTopic, MqttQos.AT_LEAST_ONCE);

      startPublisher(publisherId, PublisherStatusMode.CYCLIC);

      Mqtt5Publish publish =
          statusProbe.awaitMessage(
              message -> parse(message).get("IsCyclic").getAsBoolean(), TIMEOUT);

      assertEquals(MqttQos.AT_LEAST_ONCE, publish.getQos());

      JsonObject document = parse(publish);
      assertEquals("ua-status", document.get("MessageType").getAsString());
      assertEquals(publisherId.toCanonicalString(), document.get("PublisherId").getAsString());
      assertTrue(document.get("IsCyclic").getAsBoolean());
      assertEquals(PubSubState.Operational.getValue(), document.get("Status").getAsInt());
      assertTrue(document.has("Timestamp"));
      assertTrue(document.has("NextReportTime"));

      try (RawMqtt5Probe lateProbe = RawMqtt5Probe.connect(broker.port())) {
        lateProbe.subscribe(statusTopic, MqttQos.AT_LEAST_ONCE);

        Mqtt5Publish retained =
            lateProbe.awaitMessage(
                message -> message.isRetain() && parse(message).get("IsCyclic").getAsBoolean(),
                TIMEOUT);

        assertEquals(MqttQos.AT_LEAST_ONCE, retained.getQos());
        assertEquals(
            publisherId.toCanonicalString(), parse(retained).get("PublisherId").getAsString());
      }
    }
  }

  @Test
  void discoveredMetaDataNamesFieldsForAcceptDiscoveredReader() throws Exception {
    startPublisher();

    // REQUEST_IF_MISSING-equivalent on MQTT: no configured metadata, ACCEPT_DISCOVERED, and a
    // metadata queue subscription; the retained ua-metadata replay supplies the metadata
    // through the normal decode path
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader-a")
            .publisherId(PUBLISHER_ID)
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetReaderSettings.builder().build())
            .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
            .brokerTransport(
                BrokerTransportSettings.builder()
                    .queueName(DATA_TOPIC)
                    .metaDataQueueName(META_TOPIC)
                    .build())
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();

    PubSubService subscriber = startSubscriber(reader, events);
    subscriber.addMetaDataListener(metaDataEvents::add);

    // the metadata flows through the reader's metadata queue and is applied
    MetaDataReceivedEvent metaDataEvent = awaitMetaData(metaDataEvents);
    assertEquals("ds-a", metaDataEvent.dataSetName());
    assertEquals(uint(7), metaDataEvent.configurationVersion().getMajorVersion());
    assertEquals(uint(3), metaDataEvent.configurationVersion().getMinorVersion());

    // events decoded against the discovered metadata: dataset name and field ids are only
    // known from metadata (JSON wire names alone cannot supply them)
    DataSetReceivedEvent event = awaitEvent(events, e -> e.metaData() != null);
    assertEquals("ds-a", event.dataSetName());

    Map<String, DataValue> byName = event.fieldsByName();
    assertEquals(21.5, byName.get("temperature").value().value());
    assertEquals("running", byName.get("status").value().value());

    DataSetFieldValue temperature =
        event.fields().stream()
            .filter(field -> "temperature".equals(field.name()))
            .findFirst()
            .orElseThrow();
    assertEquals(TEMPERATURE_FIELD_ID, temperature.dataSetFieldId());
  }

  // region fixtures

  private static URI brokerUri() {
    return URI.create("mqtt://127.0.0.1:" + broker.port());
  }

  /** A reader with configured metadata matching the published dataset, REQUIRE_CONFIGURED. */
  private static DataSetReaderConfig configuredMetaDataReader() {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-a")
            .field("temperature", NodeIds.Double, TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, STATUS_FIELD_ID)
            .configurationVersion(uint(7), uint(3))
            .build();

    // no writerGroupId filter: the default JSON NetworkMessage mask does not put the
    // WriterGroup on the wire
    return DataSetReaderConfig.builder("reader-a")
        .publisherId(PUBLISHER_ID)
        .dataSetWriterId(ushort(1))
        .settings(JsonDataSetReaderSettings.builder().build())
        .dataSetMetaData(metaData)
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .brokerTransport(BrokerTransportSettings.builder().queueName(DATA_TOPIC).build())
        .build();
  }

  private static DataSetReaderConfig statusExactReader() {
    return DataSetReaderConfig.builder("reader-exact-status")
        .publisherId(PublisherId.string("status-dedupe"))
        .dataSetWriterId(ushort(1))
        .settings(JsonDataSetReaderSettings.builder().build())
        .brokerTransport(
            BrokerTransportSettings.builder()
                .queueName("opcua/json/data/status-dedupe/grp")
                .build())
        .build();
  }

  private static DataSetReaderConfig statusWildcardReader() {
    return DataSetReaderConfig.builder("reader-wild-status")
        .dataSetWriterId(ushort(1))
        .settings(JsonDataSetReaderSettings.builder().build())
        .brokerTransport(
            BrokerTransportSettings.builder()
                .queueName("opcua/json/data/status-dedupe/grp")
                .build())
        .build();
  }

  /**
   * Start a publisher service: writer group "grp" at 100 ms with writer "writer-a" publishing
   * "ds-a" using the built-in "json" mapping with all-default (empty) content masks.
   */
  private PubSubService startPublisher() throws Exception {
    return startPublisher(PUBLISHER_ID, PublisherStatusMode.AUTO);
  }

  private PubSubService startPublisher(PublisherId publisherId, PublisherStatusMode statusMode)
      throws Exception {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-a")
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

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(brokerUri())
                    .publisherId(publisherId)
                    .publisherStatusMode(statusMode)
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(100))
                            .messageSettings(JsonWriterGroupSettings.builder().build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer-a")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(JsonDataSetWriterSettings.builder().build())
                                    .build())
                            .build())
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

  private PubSubService startSubscriber(
      DataSetReaderConfig reader, LinkedBlockingQueue<DataSetReceivedEvent> events)
      throws Exception {

    return startSubscriber(List.of(reader), events);
  }

  private PubSubService startSubscriber(
      List<DataSetReaderConfig> readers, LinkedBlockingQueue<DataSetReceivedEvent> events)
      throws Exception {

    PubSubService subscriber = newSubscriber(readers, events);
    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return subscriber;
  }

  private PubSubService newSubscriber(
      List<DataSetReaderConfig> readers, LinkedBlockingQueue<DataSetReceivedEvent> events) {

    ReaderGroupConfig.Builder readerGroupBuilder = ReaderGroupConfig.builder("rgrp");
    PubSubBindings.Builder bindingsBuilder = PubSubBindings.builder();

    for (DataSetReaderConfig reader : readers) {
      readerGroupBuilder.dataSetReader(reader);
      bindingsBuilder.listener(
          new DataSetReaderRef("sub-conn", "rgrp", reader.getName()), events::add);
    }

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(brokerUri())
                    .readerGroup(readerGroupBuilder.build())
                    .build())
            .build();

    PubSubService subscriber =
        track(PubSubService.create(config, bindingsBuilder.build(), mqttServiceConfig()));

    return subscriber;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private static String dataTopic(PublisherId publisherId) {
    return "opcua/json/data/" + publisherId.toCanonicalString() + "/grp";
  }

  private static String statusTopic(PublisherId publisherId) {
    return "opcua/json/status/" + publisherId.toCanonicalString();
  }

  private static JsonObject parse(Mqtt5Publish publish) {
    String json = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
    return JsonParser.parseString(json).getAsJsonObject();
  }

  private static MetaDataReceivedEvent awaitMetaData(
      LinkedBlockingQueue<MetaDataReceivedEvent> events) throws InterruptedException {

    MetaDataReceivedEvent event = events.poll(TIMEOUT.toNanos(), TimeUnit.NANOSECONDS);
    if (event == null) {
      fail("timed out waiting for a MetaDataReceivedEvent");
    }
    return event;
  }

  private static PublisherStatusReceivedEvent awaitStatus(
      LinkedBlockingQueue<PublisherStatusReceivedEvent> events) throws InterruptedException {

    return awaitStatus(events, event -> true);
  }

  private static PublisherStatusReceivedEvent awaitStatus(
      LinkedBlockingQueue<PublisherStatusReceivedEvent> events,
      Predicate<PublisherStatusReceivedEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        fail("timed out waiting for a PublisherStatusReceivedEvent");
      }
      PublisherStatusReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  private static void assertNoStatus(
      LinkedBlockingQueue<PublisherStatusReceivedEvent> events,
      Predicate<PublisherStatusReceivedEvent> predicate,
      Duration window)
      throws InterruptedException {

    long deadline = System.nanoTime() + window.toNanos();
    while (System.nanoTime() < deadline) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        break;
      }
      PublisherStatusReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        fail("unexpected duplicate PublisherStatusReceivedEvent: " + event);
      }
    }
  }

  // endregion
}
