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
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitTrue;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * End-to-end JSON-over-MQTT event publishing against an embedded HiveMQ CE broker: the standalone
 * core push API ({@link PubSubService#publishEvent}) feeds an event-source dataset whose writer
 * emits {@code ua-event} DataSetMessages through the built-in "json" mapping over real broker
 * topics, and a DataSetReader on the derived topic decodes them back to {@link
 * DataSetReceivedEvent}s of kind {@link DataSetMessageKind#EVENT} (Part 14 §6.2.6.2, §7.2.5.4.1).
 *
 * <p>No server is involved: the event dataset needs no source binding and the field values are
 * pushed directly as Variants through {@code publishEvent}, keeping this module's dependency set
 * unchanged (the server adapter's notifier-driven path is exercised in sdk-pubsub-server).
 *
 * <p>Topic pins (Part 14 §7.3.4.7): data = {@code opcua/json/data/<publisherId>/<writerGroupName>},
 * metadata = {@code opcua/json/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>}. The
 * writer group's masks are chosen so events are wire-expressible (Annex A.3.3.4): the
 * DataSetMessageHeader in the NetworkMessage mask and the MessageType member in the DataSetMessage
 * mask, without which a {@code ua-event} payload is indistinguishable from a key frame.
 */
class MqttJsonEventEndToEndTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4343));

  private static final String DATA_TOPIC = "opcua/json/data/4343/evtgrp";
  private static final String META_TOPIC = "opcua/json/metadata/4343/evtgrp/evt-writer";

  private static final PublishedDataSetRef EVENT_REF = new PublishedDataSetRef("evt-ds");

  private static final UUID MESSAGE_FIELD_ID = new UUID(0L, 0xE1L);
  private static final UUID VALUE_FIELD_ID = new UUID(0L, 0xE2L);

  /** One event's field values, in the dataset's configured event-field (wire) order. */
  private static final List<Variant> EVENT_FIELDS =
      List.of(Variant.ofString("boom"), Variant.ofDouble(42.5));

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
  void tearDown() {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException | InterruptedException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  /**
   * {@code publishEvent} pushes an event into an event-source writer that emits it as a {@code
   * ua-event} DataSetMessage over MQTT; a DataSetReader on the derived data topic decodes it
   * against its configured metadata and delivers a {@link DataSetReceivedEvent} of kind {@link
   * DataSetMessageKind#EVENT} carrying the pushed field values.
   */
  @Test
  void publishedEventArrivesAtReaderAsEventKind() throws Exception {
    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    startSubscriber(events);

    PubSubService publisher = startPublisher();
    awaitPublisherOperational(publisher);

    DataSetReceivedEvent event = awaitEventDelivery(publisher, events);

    assertEquals(DataSetMessageKind.EVENT, event.kind());
    assertEquals(ushort(1), event.dataSetWriterId());
    assertEquals("evt-ds", event.dataSetName());
    assertNotNull(event.metaData());

    // pushed Variants round-trip through the ua-event Payload, name-matched via configured metadata
    assertEquals("boom", event.fieldsByName().get("message").value().value());
    assertEquals(42.5, event.fieldsByName().get("value").value().value());
  }

  /**
   * The event lands on the §7.3.4.7 data topic as a {@code ua-data} NetworkMessage whose single
   * DataSetMessage carries the {@code ua-event} MessageType member and a Variant-represented
   * Payload of the pushed field values (per-event partition: one event per NetworkMessage, D-6).
   */
  @Test
  void jsonEventIsUaEventDocumentOnDerivedTopic() throws Exception {
    PubSubService publisher = startPublisher();
    awaitPublisherOperational(publisher);

    try (RawMqtt5Probe probe = RawMqtt5Probe.connect(broker.port())) {
      probe.subscribe(DATA_TOPIC, MqttQos.EXACTLY_ONCE);

      // the QoS-0 data stream is at-most-once; a small burst tolerates any single dropped publish
      for (int i = 0; i < 5; i++) {
        publisher.publishEvent(EVENT_REF, EVENT_FIELDS);
      }

      Mqtt5Publish publish = probe.awaitMessage(TIMEOUT);

      assertEquals(DATA_TOPIC, publish.getTopic().toString());
      assertEquals("application/json", publish.getContentType().map(Object::toString).orElse(null));

      JsonObject document = parse(publish);
      // the NetworkMessage envelope is ua-data; the event lives in the DataSetMessage
      assertEquals("ua-data", document.get("MessageType").getAsString());
      assertEquals("4343", document.get("PublisherId").getAsString());

      JsonArray messages = document.getAsJsonArray("Messages");
      assertEquals(1, messages.size(), "one event per NetworkMessage (per-event partition)");

      JsonObject dataSetMessage = messages.get(0).getAsJsonObject();
      assertEquals(1, dataSetMessage.get("DataSetWriterId").getAsInt());
      assertEquals("ua-event", dataSetMessage.get("MessageType").getAsString());

      JsonObject payload = dataSetMessage.getAsJsonObject("Payload");
      assertEquals("boom", payload.get("message").getAsString());
      assertEquals(42.5, payload.get("value").getAsDouble());
    }
  }

  /**
   * The event writer publishes a retained {@code ua-metadata} document naming the event fields; a
   * late subscriber to the metadata topic receives it via retained-message replay (the derived
   * §7.3.4.7 metadata topic).
   */
  @Test
  void jsonEventMetaDataIsRetainedUaMetadataDocument() throws Exception {
    PubSubService publisher = startPublisher();
    awaitPublisherOperational(publisher);

    // drive a few events (the writer publishes its retained ua-metadata at activation, independent
    // of events) so the writer is exercised past activation
    for (int i = 0; i < 5; i++) {
      publisher.publishEvent(EVENT_REF, EVENT_FIELDS);
    }

    // Confirm the retained ua-metadata is actually on the broker before the late subscriber
    // connects. The writer's activation-time metadata publish runs synchronously during startup,
    // right after the broker connect is *initiated* asynchronously, so it can lose that race and
    // fail fast (Bad_ServerNotConnected); MetaDataPublisher then retries it with a backoff. The
    // writer being Operational (events flowing) therefore does NOT guarantee the retained publish
    // has landed yet. Waiting for the metadata to be observed here does: it is always published
    // with retain=true, so once any observation of it has occurred the broker's retained store is
    // populated and a subsequent fresh subscription replays it retained.
    try (RawMqtt5Probe warmup = RawMqtt5Probe.connect(broker.port())) {
      warmup.subscribe(META_TOPIC, MqttQos.AT_LEAST_ONCE);
      warmup.awaitMessage(TIMEOUT);
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
      assertEquals("evt-ds", metaData.get("Name").getAsString());

      JsonArray metaDataFields = metaData.getAsJsonArray("Fields");
      assertEquals(2, metaDataFields.size());

      List<String> names =
          metaDataFields.asList().stream()
              .map(element -> element.getAsJsonObject().get("Name").getAsString())
              .toList();
      assertTrue(names.contains("message"), "field names in metadata: " + names);
      assertTrue(names.contains("value"), "field names in metadata: " + names);
    }
  }

  // region fixtures

  private static URI brokerUri() {
    return URI.create("mqtt://127.0.0.1:" + broker.port());
  }

  /** An event-source dataset selecting a String "message" field and a Double "value" field. */
  private static PublishedDataSetConfig eventDataSet() {
    PublishedEventsConfig events =
        PublishedEventsConfig.builder(ExpandedNodeId.of("urn:test:notifier", "Notifier"))
            .field(eventField("message", NodeIds.String, MESSAGE_FIELD_ID))
            .field(eventField("value", NodeIds.Double, VALUE_FIELD_ID))
            .build();

    return PublishedDataSetConfig.builder("evt-ds").source(events).build();
  }

  private static EventFieldDefinition eventField(String name, NodeId dataType, UUID fieldId) {
    return EventFieldDefinition.builder(name)
        .dataType(dataType)
        .dataSetFieldId(fieldId)
        .selectedField(select(name))
        .build();
  }

  /** A select clause for the BaseEventType field named {@code browseName}. */
  private static SimpleAttributeOperand select(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  /**
   * Start a publisher whose writer group "evtgrp" carries a single event writer "evt-writer" on the
   * event dataset, using the "json" mapping with masks that make {@code ua-event} wire-expressible.
   */
  private PubSubService startPublisher() throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet())
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(brokerUri())
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(
                        WriterGroupConfig.builder("evtgrp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(50))
                            .messageSettings(
                                JsonWriterGroupSettings.builder()
                                    .networkMessageContentMask(
                                        JsonNetworkMessageContentMask.of(
                                            JsonNetworkMessageContentMask.Field
                                                .NetworkMessageHeader,
                                            JsonNetworkMessageContentMask.Field
                                                .DataSetMessageHeader,
                                            JsonNetworkMessageContentMask.Field.PublisherId))
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("evt-writer")
                                    .dataSet(EVENT_REF)
                                    .dataSetWriterId(ushort(1))
                                    .settings(
                                        JsonDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(
                                                JsonDataSetMessageContentMask.of(
                                                    JsonDataSetMessageContentMask.Field
                                                        .DataSetWriterId,
                                                    JsonDataSetMessageContentMask.Field
                                                        .SequenceNumber,
                                                    JsonDataSetMessageContentMask.Field
                                                        .MetaDataVersion,
                                                    JsonDataSetMessageContentMask.Field.MessageType,
                                                    JsonDataSetMessageContentMask.Field
                                                        .FieldEncoding2))
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    // event datasets need no source binding: field values arrive via publishEvent
    PubSubService publisher =
        track(PubSubService.create(config, PubSubBindings.builder().build(), mqttServiceConfig()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return publisher;
  }

  /** A DataSetReader on the derived data topic with configured metadata for the event fields. */
  private void startSubscriber(LinkedBlockingQueue<DataSetReceivedEvent> events) throws Exception {

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("evt-ds")
            .field("message", NodeIds.String, MESSAGE_FIELD_ID)
            .field("value", NodeIds.Double, VALUE_FIELD_ID)
            .build();

    // no writerGroupId filter: the default JSON NetworkMessage mask does not put the WriterGroup
    // on the wire
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .publisherId(PUBLISHER_ID)
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetReaderSettings.builder().build())
            .dataSetMetaData(metaData)
            .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
            .brokerTransport(BrokerTransportSettings.builder().queueName(DATA_TOPIC).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(brokerUri())
                    .readerGroup(ReaderGroupConfig.builder("rgrp").dataSetReader(reader).build())
                    .build())
            .build();

    PubSubService subscriber =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                    .build(),
                mqttServiceConfig()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private void awaitPublisherOperational(PubSubService publisher) throws InterruptedException {
    PubSubHandle group = publisher.components().writerGroup("pub-conn", "evtgrp").orElseThrow();
    awaitTrue(
        () -> publisher.state(group) == PubSubState.Operational, "publisher group Operational");
  }

  /**
   * Publish the event repeatedly until the reader delivers one: the reader's MQTT SUBSCRIBE is
   * issued asynchronously and not awaited by startup, so a single one-shot event could be published
   * into the subscribe gap and lost. Each retry pushes another event; the first delivered one is
   * returned.
   */
  private DataSetReceivedEvent awaitEventDelivery(
      PubSubService publisher, LinkedBlockingQueue<DataSetReceivedEvent> events)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      publisher.publishEvent(EVENT_REF, EVENT_FIELDS);
      DataSetReceivedEvent event = events.poll(300, TimeUnit.MILLISECONDS);
      if (event != null) {
        return event;
      }
    }
    return fail("timed out waiting for the event to be delivered to the reader");
  }

  private static JsonObject parse(Mqtt5Publish publish) {
    String json = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
    return JsonParser.parseString(json).getAsJsonObject();
  }

  // endregion
}
