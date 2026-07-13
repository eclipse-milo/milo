/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Engine-level end-to-end test of the JSON mapping over a stub broker transport: a publisher
 * service produces {@code ua-data} and retained {@code ua-metadata} wire JSON through the addressed
 * send path; the captured documents are fed into a subscriber service's consumer, where a
 * REQUIRE_CONFIGURED reader delivers fields with configured names and goes Operational on the JSON
 * key frame, and an ACCEPT_DISCOVERED reader applies the {@code ua-metadata} message via the
 * handleMetaData path ({@link MetaDataReceivedEvent}).
 *
 * <p>No sockets are opened: the connections are MQTT configs served by an in-memory {@link
 * TransportProvider}, so the test is CI-safe and cannot reach the live multicast traffic on the
 * build host.
 */
class JsonEngineEndToEndTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.string("line-7");

  /** The publisher-side dataSetFieldIds, carried by the derived (discovered) metadata. */
  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xA1L);

  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xA2L);

  /** Distinct configured ids so configured-vs-discovered metadata attribution is observable. */
  private static final UUID CONFIGURED_TEMPERATURE_FIELD_ID = new UUID(0L, 0xC1L);

  private static final UUID CONFIGURED_STATUS_FIELD_ID = new UUID(0L, 0xC2L);

  private static final UUID POSITION_FIELD_ID = new UUID(0L, 0xD1L);

  private record Sent(MessageAddress address, String json) {}

  /** An in-memory broker transport: captures addressed sends, exposes the receive consumers. */
  private static final class StubBrokerTransport implements TransportProvider {

    final BlockingQueue<Sent> sent = new LinkedBlockingQueue<>();
    final AtomicInteger unaddressedSends = new AtomicInteger(0);
    final AtomicReference<@Nullable BiConsumer<String, ByteBuf>> topicConsumer =
        new AtomicReference<>();
    final AtomicReference<@Nullable Consumer<ByteBuf>> consumer = new AtomicReference<>();

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:stub-broker";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return connection instanceof MqttConnectionConfig;
    }

    @Override
    public PublisherChannel openPublisher(PublisherTransportContext context) {
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          // the engine always uses the addressed form on data and metadata paths
          unaddressedSends.incrementAndGet();
          message.release();
          return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> send(ByteBuf message, MessageAddress address) {
          String json = message.toString(StandardCharsets.UTF_8);
          message.release();
          sent.add(new Sent(address, json));
          return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }

    @Override
    public SubscriberChannel openSubscriber(SubscriberTransportContext context) {
      topicConsumer.set(context.topicMessageConsumer());
      consumer.set(context.messageConsumer());
      return () -> CompletableFuture.completedFuture(null);
    }

    /** Deliver one wire document the way a broker subscription would. */
    void inject(String topic, String json) {
      BiConsumer<String, ByteBuf> topicMessageConsumer = topicConsumer.get();
      Consumer<ByteBuf> messageConsumer = consumer.get();
      assertTrue(
          topicMessageConsumer != null || messageConsumer != null,
          "subscriber channel was never opened");

      ByteBuf buffer = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));
      try {
        if (topicMessageConsumer != null) {
          topicMessageConsumer.accept(topic, buffer);
        } else {
          messageConsumer.accept(buffer);
        }
      } finally {
        buffer.release();
      }
    }
  }

  private @Nullable PubSubService publisher;
  private @Nullable PubSubService subscriber;
  private @Nullable ExecutorService subscriberExecutor;

  @AfterEach
  void tearDown() throws Exception {
    if (publisher != null) {
      publisher.close();
      publisher = null;
    }
    if (subscriber != null) {
      subscriber.close();
      subscriber = null;
    }
    if (subscriberExecutor != null) {
      subscriberExecutor.shutdown();
      assertTrue(subscriberExecutor.awaitTermination(10, TimeUnit.SECONDS));
      subscriberExecutor = null;
    }
  }

  @Test
  void jsonOverBrokerEndToEnd() throws Exception {
    var publisherTransport = new StubBrokerTransport();
    var subscriberTransport = new StubBrokerTransport();
    subscriberExecutor = Executors.newSingleThreadExecutor();

    // region publisher service

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds")
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
            .build(); // default ConfigurationVersion (1, 1)

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(JsonWriterGroupSettings.builder().build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(JsonDataSetWriterSettings.builder().build())
                                    .build())
                            .build())
                    .build())
            .build();

    PublishedDataSetSource source =
        context -> {
          DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
          builder.field("temperature", new DataValue(Variant.ofDouble(21.5)));
          builder.field("status", new DataValue(Variant.ofString("running")));
          return builder.build();
        };

    publisher =
        PubSubService.create(
            publisherConfig,
            PubSubBindings.builder().source(dataSet.ref(), source).build(),
            PubSubServiceConfig.builder().transportProvider(publisherTransport).build());

    // endregion

    // region subscriber service

    DataSetMetaDataConfig configuredMetaData =
        DataSetMetaDataConfig.builder("ds")
            .field("temperature", NodeIds.Double, CONFIGURED_TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, CONFIGURED_STATUS_FIELD_ID)
            .build(); // default ConfigurationVersion (1, 1), matching the published dataset

    // a reader on a broker connection requires an explicit data queueName (M7 validation)
    BrokerTransportSettings readerQueue =
        BrokerTransportSettings.builder().queueName("opcua/json/data/line-7/grp").build();

    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader-cfg")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(configuredMetaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .settings(JsonDataSetReaderSettings.builder().build())
                                    .brokerTransport(readerQueue)
                                    .build())
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader-disc")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
                                    .settings(JsonDataSetReaderSettings.builder().build())
                                    .brokerTransport(readerQueue)
                                    .build())
                            .build())
                    .build())
            .build();

    var configuredEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var discoveredEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();

    subscriber =
        PubSubService.create(
            subscriberConfig,
            PubSubBindings.builder()
                .listener(
                    new DataSetReaderRef("sub-conn", "rgrp", "reader-cfg"), configuredEvents::add)
                .listener(
                    new DataSetReaderRef("sub-conn", "rgrp", "reader-disc"), discoveredEvents::add)
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(subscriberTransport)
                .transportExecutor(subscriberExecutor)
                .build());
    subscriber.addMetaDataListener(metaDataEvents::add);

    // endregion

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle configuredReader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-cfg").orElseThrow();
    PubSubHandle discoveredReader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-disc").orElseThrow();
    assertEquals(PubSubState.PreOperational, subscriber.state(configuredReader));
    assertEquals(PubSubState.PreOperational, subscriber.state(discoveredReader));

    // region captured wire messages

    // metadata is published retained at writer activation (broker connection), data every cycle
    Sent metaDataSent = awaitSent(publisherTransport, MessageAddress.Kind.METADATA);
    Sent dataSent = awaitSent(publisherTransport, MessageAddress.Kind.DATA);

    // the metadata address is the derived per-writer metadata topic, retained, QoS1
    assertEquals("opcua/json/metadata/line-7/grp/writer", metaDataSent.address().queueName());
    assertTrue(metaDataSent.address().retain());
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce, metaDataSent.address().deliveryGuarantee());
    assertEquals(MessageAddress.CONTENT_TYPE_JSON, metaDataSent.address().contentTypeHint());

    JsonObject metaDataWire = JsonParser.parseString(metaDataSent.json()).getAsJsonObject();
    assertEquals("ua-metadata", metaDataWire.get("MessageType").getAsString());
    assertEquals("line-7", metaDataWire.get("PublisherId").getAsString());
    assertEquals(1, metaDataWire.get("DataSetWriterId").getAsInt());
    assertEquals("grp", metaDataWire.get("WriterGroupName").getAsString());
    assertEquals("writer", metaDataWire.get("DataSetWriterName").getAsString());
    assertEquals("ds", metaDataWire.get("MetaData").getAsJsonObject().get("Name").getAsString());

    // the data address is the derived group data topic, not retained, QoS0
    assertEquals("opcua/json/data/line-7/grp", dataSent.address().queueName());
    assertTrue(!dataSent.address().retain());
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce, dataSent.address().deliveryGuarantee());
    assertEquals(MessageAddress.CONTENT_TYPE_JSON, dataSent.address().contentTypeHint());

    JsonObject dataWire = JsonParser.parseString(dataSent.json()).getAsJsonObject();
    assertEquals("ua-data", dataWire.get("MessageType").getAsString());
    assertEquals("line-7", dataWire.get("PublisherId").getAsString());
    JsonObject payload =
        dataWire
            .get("Messages")
            .getAsJsonArray()
            .get(0)
            .getAsJsonObject()
            .get("Payload")
            .getAsJsonObject();
    assertEquals(21.5, payload.get("temperature").getAsDouble());
    assertEquals("running", payload.get("status").getAsString());

    // endregion

    // region ua-metadata wire message feeds handleMetaData

    subscriberTransport.inject("opcua/json/metadata/line-7/grp/writer", metaDataSent.json());
    flushSubscriber();

    // the ACCEPT_DISCOVERED reader receives (and applies) the discovered metadata
    MetaDataReceivedEvent metaDataEvent =
        awaitMetaDataEvent(metaDataEvents, "sub-conn/rgrp/reader-disc");
    assertEquals("ds", metaDataEvent.dataSetName());
    assertEquals("ds", metaDataEvent.metaData().getName());
    assertEquals(uint(1), metaDataEvent.configurationVersion().getMajorVersion());
    assertEquals(uint(1), metaDataEvent.configurationVersion().getMinorVersion());

    // metadata alone does not complete reader startup
    assertEquals(PubSubState.PreOperational, subscriber.state(configuredReader));
    assertEquals(PubSubState.PreOperational, subscriber.state(discoveredReader));

    // endregion

    // region the JSON key frame completes startup and delivers named fields

    subscriberTransport.inject("opcua/json/data/line-7/grp", dataSent.json());
    flushSubscriber();

    // both readers transition PreOperational -> Operational on the JSON key frame
    assertEquals(PubSubState.Operational, subscriber.state(configuredReader));
    assertEquals(PubSubState.Operational, subscriber.state(discoveredReader));

    // the REQUIRE_CONFIGURED reader names fields via its CONFIGURED metadata
    DataSetReceivedEvent configuredEvent = configuredEvents.poll(10, TimeUnit.SECONDS);
    assertNotNull(configuredEvent);
    assertEquals(PUBLISHER_ID, configuredEvent.publisherId());
    assertEquals(ushort(1), configuredEvent.dataSetWriterId());
    assertEquals("ds", configuredEvent.dataSetName());
    assertNotNull(configuredEvent.metaData());

    List<DataSetFieldValue> configuredFields = configuredEvent.fields();
    assertEquals(2, configuredFields.size());
    assertEquals("temperature", configuredFields.get(0).name());
    assertEquals(0, configuredFields.get(0).index());
    assertEquals(CONFIGURED_TEMPERATURE_FIELD_ID, configuredFields.get(0).dataSetFieldId());
    assertEquals(21.5, configuredFields.get(0).value().value().value());
    assertEquals("status", configuredFields.get(1).name());
    assertEquals(CONFIGURED_STATUS_FIELD_ID, configuredFields.get(1).dataSetFieldId());
    assertEquals("running", configuredFields.get(1).value().value().value());

    // the ACCEPT_DISCOVERED reader names fields via the DISCOVERED (wire) metadata
    DataSetReceivedEvent discoveredEvent = discoveredEvents.poll(10, TimeUnit.SECONDS);
    assertNotNull(discoveredEvent);
    List<DataSetFieldValue> discoveredFields = discoveredEvent.fields();
    assertEquals(2, discoveredFields.size());
    assertEquals("temperature", discoveredFields.get(0).name());
    assertEquals(TEMPERATURE_FIELD_ID, discoveredFields.get(0).dataSetFieldId());
    assertEquals("status", discoveredFields.get(1).name());
    assertEquals(STATUS_FIELD_ID, discoveredFields.get(1).dataSetFieldId());

    // endregion

    // region sequence numbers: surfaced on the event, duplicate documents dropped

    // the default JSON DataSetMessage mask puts the UInt32 SequenceNumber on the wire; the JSON
    // NetworkMessage has no sequence number at all (Part 14 §7.2.5.3 Table 184)
    assertNotNull(configuredEvent.dataSetMessageSequenceNumber());
    assertNull(configuredEvent.networkMessageSequenceNumber());
    assertEquals(
        configuredEvent.dataSetMessageSequenceNumber(),
        discoveredEvent.dataSetMessageSequenceNumber());

    // re-injecting the same captured document is the broker QoS AtLeastOnce duplicate shape
    // (§6.4.2.1: "Readers shall de-duplicate based on message id or sequence number"): the
    // §7.2.3 window drops it for both readers, one staleSequenceMessages tick each, no delivery
    subscriberTransport.inject("opcua/json/data/line-7/grp", dataSent.json());
    flushSubscriber();

    assertNull(configuredEvents.poll());
    assertNull(discoveredEvents.poll());
    assertEquals(
        1,
        subscriber
            .diagnostics()
            .snapshot()
            .get("sub-conn/rgrp/reader-cfg")
            .staleSequenceMessages());
    assertEquals(
        1,
        subscriber
            .diagnostics()
            .snapshot()
            .get("sub-conn/rgrp/reader-disc")
            .staleSequenceMessages());

    // endregion

    // the engine never used the unaddressed send path
    assertEquals(0, publisherTransport.unaddressedSends.get());
    assertEquals(0, subscriberTransport.unaddressedSends.get());
  }

  /**
   * A JSON writer with {@code keyFrameCount} = 3 (and the MessageType member the Annex A.3.3.4 rule
   * requires) publishes {@code ua-keyframe} and {@code ua-deltaframe} documents through the real
   * engine; the delta Payload carries only the changed field. The subscriber decodes both and the
   * reader delivers the delta's field name-matched against its configured metadata, so listeners
   * can merge it onto the key frame state they already hold.
   */
  @Test
  void jsonDeltaFramesEndToEnd() throws Exception {
    var publisherTransport = new StubBrokerTransport();
    var subscriberTransport = new StubBrokerTransport();
    subscriberExecutor = Executors.newSingleThreadExecutor();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds")
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
            .build();

    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.PublisherId);

    // delta frames require the MessageType member (Part 14 Annex A.3.3.4)
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.SequenceNumber,
            JsonDataSetMessageContentMask.Field.MetaDataVersion,
            JsonDataSetMessageContentMask.Field.MessageType,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(
                                JsonWriterGroupSettings.builder()
                                    .networkMessageContentMask(nmMask)
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .keyFrameCount(uint(3))
                                    .settings(
                                        JsonDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(dsmMask)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    // temperature changes on every read; status never does
    var reads = new AtomicInteger(0);
    PublishedDataSetSource source =
        context -> {
          DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
          builder.field(
              "temperature", new DataValue(Variant.ofDouble(20.0 + reads.incrementAndGet())));
          builder.field("status", new DataValue(Variant.ofString("running")));
          return builder.build();
        };

    publisher =
        PubSubService.create(
            publisherConfig,
            PubSubBindings.builder().source(dataSet.ref(), source).build(),
            PubSubServiceConfig.builder().transportProvider(publisherTransport).build());

    DataSetMetaDataConfig configuredMetaData =
        DataSetMetaDataConfig.builder("ds")
            .field("temperature", NodeIds.Double, CONFIGURED_TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, CONFIGURED_STATUS_FIELD_ID)
            .build();

    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(configuredMetaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .settings(JsonDataSetReaderSettings.builder().build())
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName("opcua/json/data/line-7/grp")
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    subscriber =
        PubSubService.create(
            subscriberConfig,
            PubSubBindings.builder()
                .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(subscriberTransport)
                .transportExecutor(subscriberExecutor)
                .build());

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader").orElseThrow();
    assertEquals(PubSubState.PreOperational, subscriber.state(reader));

    // the first data document of the cadence is a key frame, the second a delta frame whose
    // Payload carries only the changed field
    Sent keyFrameSent = awaitSent(publisherTransport, MessageAddress.Kind.DATA);
    Sent deltaFrameSent = awaitSent(publisherTransport, MessageAddress.Kind.DATA);

    JsonObject keyFrame = firstDataSetMessage(keyFrameSent);
    assertEquals("ua-keyframe", keyFrame.get("MessageType").getAsString());
    JsonObject keyFramePayload = keyFrame.get("Payload").getAsJsonObject();
    assertEquals(2, keyFramePayload.keySet().size());

    JsonObject deltaFrame = firstDataSetMessage(deltaFrameSent);
    assertEquals("ua-deltaframe", deltaFrame.get("MessageType").getAsString());
    JsonObject deltaFramePayload = deltaFrame.get("Payload").getAsJsonObject();
    assertEquals(List.of("temperature"), List.copyOf(deltaFramePayload.keySet()));
    assertEquals(
        keyFrame.get("SequenceNumber").getAsLong() + 1,
        deltaFrame.get("SequenceNumber").getAsLong());

    // the key frame completes reader startup and delivers both fields
    subscriberTransport.inject("opcua/json/data/line-7/grp", keyFrameSent.json());
    flushSubscriber();

    assertEquals(PubSubState.Operational, subscriber.state(reader));

    DataSetReceivedEvent keyFrameEvent = events.poll(10, TimeUnit.SECONDS);
    assertNotNull(keyFrameEvent);
    assertEquals(2, keyFrameEvent.fields().size());

    // the delta delivers only the changed field, name-matched against the configured metadata,
    // so the listener can merge it onto the state the key frame established
    subscriberTransport.inject("opcua/json/data/line-7/grp", deltaFrameSent.json());
    flushSubscriber();

    DataSetReceivedEvent deltaFrameEvent = events.poll(10, TimeUnit.SECONDS);
    assertNotNull(deltaFrameEvent);
    assertEquals(1, deltaFrameEvent.fields().size());

    DataSetFieldValue changed = deltaFrameEvent.fields().get(0);
    assertEquals("temperature", changed.name());
    assertEquals(0, changed.index());
    assertEquals(CONFIGURED_TEMPERATURE_FIELD_ID, changed.dataSetFieldId());
    assertEquals(
        deltaFramePayload.get("temperature").getAsDouble(), changed.value().value().value());

    // delta frames consume sequence numbers like key frames (Part 14 §7.2.3)
    assertNotNull(keyFrameEvent.dataSetMessageSequenceNumber());
    assertNotNull(deltaFrameEvent.dataSetMessageSequenceNumber());
    assertEquals(
        keyFrameEvent.dataSetMessageSequenceNumber().longValue() + 1,
        deltaFrameEvent.dataSetMessageSequenceNumber().longValue());
  }

  /**
   * The acceptance path for publisher-side custom-DataType metadata: a standalone publisher whose
   * EncodingContext registers a custom structure codec (DataType and encoding NodeIds in a non-zero
   * namespace) and whose dataset declares the matching {@code StructureDescription} announces
   * {@code ua-metadata} from which a remote JSON-only subscriber — running a default
   * EncodingContext with no access to the publisher's namespace table or codecs — can (a) resolve
   * the field's DataType NodeId via the metadata Namespaces array (OPC 10000-5 §12.31) and (b) find
   * the StructureDescription needed to interpret the field's inline JSON struct members.
   */
  @Test
  void customStructTypeMetaDataEndToEnd() throws Exception {
    var publisherTransport = new StubBrokerTransport();
    var subscriberTransport = new StubBrokerTransport();
    subscriberExecutor = Executors.newSingleThreadExecutor();

    // region publisher service with a custom-type EncodingContext

    // CUSTOM_TYPES_URI deliberately sits at publisher-local index 2, so the metadata-local
    // remapping (to index 1) is observable on the wire.
    var publisherNamespaces = new NamespaceTable("urn:milo:test:other", Position3D.NAMESPACE_URI);

    NodeId positionDataTypeId =
        Position3D.TYPE_ID.toNodeId(publisherNamespaces).orElseThrow(); // ns=2;i=3001
    NodeId positionBinaryEncodingId =
        Position3D.BINARY_ENCODING_ID.toNodeId(publisherNamespaces).orElseThrow();
    NodeId positionJsonEncodingId =
        Position3D.JSON_ENCODING_ID.toNodeId(publisherNamespaces).orElseThrow();

    DataTypeManager publisherDataTypeManager =
        DefaultDataTypeManager.createAndInitialize(publisherNamespaces);
    publisherDataTypeManager.registerType(
        positionDataTypeId,
        new Position3D.Codec(),
        positionBinaryEncodingId,
        null,
        positionJsonEncodingId);

    EncodingContext publisherContext =
        new DefaultEncodingContext() {
          @Override
          public NamespaceTable getNamespaceTable() {
            return publisherNamespaces;
          }

          @Override
          public DataTypeManager getDataTypeManager() {
            return publisherDataTypeManager;
          }
        };

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("custom-ds")
            .field(
                FieldDefinition.builder("position")
                    .dataType(positionDataTypeId)
                    .dataSetFieldId(POSITION_FIELD_ID)
                    .build())
            .structureDataType(
                positionDataTypeId,
                new QualifiedName(positionDataTypeId.getNamespaceIndex(), "Position3D"),
                Position3D.definition(publisherNamespaces))
            .build();

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(JsonWriterGroupSettings.builder().build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(JsonDataSetWriterSettings.builder().build())
                                    .build())
                            .build())
                    .build())
            .build();

    PublishedDataSetSource source =
        context -> {
          DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
          builder.field("position", new DataValue(Variant.of(new Position3D(1.5, 2.5))));
          return builder.build();
        };

    publisher =
        PubSubService.create(
            publisherConfig,
            PubSubBindings.builder().source(dataSet.ref(), source).build(),
            PubSubServiceConfig.builder()
                .transportProvider(publisherTransport)
                .encodingContext(publisherContext)
                .build());

    // endregion

    // region JSON-only subscriber service: default EncodingContext, no codecs, no metadata

    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
                                    .settings(JsonDataSetReaderSettings.builder().build())
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName("opcua/json/data/line-7/grp")
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();

    subscriber =
        PubSubService.create(
            subscriberConfig,
            PubSubBindings.builder()
                .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(subscriberTransport)
                .transportExecutor(subscriberExecutor)
                .build());
    subscriber.addMetaDataListener(metaDataEvents::add);

    // endregion

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // region announced ua-metadata carries the DataTypeSchemaHeader content

    Sent metaDataSent = awaitSent(publisherTransport, MessageAddress.Kind.METADATA);
    Sent dataSent = awaitSent(publisherTransport, MessageAddress.Kind.DATA);

    JsonObject metaDataWire =
        JsonParser.parseString(metaDataSent.json())
            .getAsJsonObject()
            .get("MetaData")
            .getAsJsonObject();

    // Only the referenced custom namespace is announced — the publisher-local table (where the
    // custom URI sits at index 2, behind an unreferenced URI) never leaks: the metadata-local
    // index 1 is backed by the Namespaces array, and the JSON codec renders the metadata NodeIds
    // in their URI-qualified string form.
    var namespacesWire = metaDataWire.get("Namespaces").getAsJsonArray();
    assertEquals(1, namespacesWire.size());
    assertEquals(Position3D.NAMESPACE_URI, namespacesWire.get(0).getAsString());
    String wireDataTypeId = "nsu=" + Position3D.NAMESPACE_URI + ";i=3001";
    assertEquals(
        wireDataTypeId,
        metaDataWire
            .get("Fields")
            .getAsJsonArray()
            .get(0)
            .getAsJsonObject()
            .get("DataType")
            .getAsString());
    assertEquals(
        wireDataTypeId,
        metaDataWire
            .get("StructureDataTypes")
            .getAsJsonArray()
            .get(0)
            .getAsJsonObject()
            .get("DataTypeId")
            .getAsString());

    // endregion

    // region the subscriber resolves the DataType and finds the StructureDescription

    subscriberTransport.inject("opcua/json/metadata/line-7/grp/writer", metaDataSent.json());
    flushSubscriber();

    MetaDataReceivedEvent metaDataEvent =
        awaitMetaDataEvent(metaDataEvents, "sub-conn/rgrp/reader");
    DataSetMetaDataType metaData = metaDataEvent.metaData();

    FieldMetaData positionField = metaData.getFields()[0];
    assertEquals(POSITION_FIELD_ID, positionField.getDataSetFieldId());

    // (a) the field's DataType NodeId is resolvable without the publisher's namespace table: the
    // announced Namespaces array carries the custom URI, and the subscriber — whose table does
    // not contain it — decodes the URI-qualified NodeId to the self-describing namespace-0 String
    // fallback of the OPC 10000-6 unknown-namespace rule.
    String[] namespaces = metaData.getNamespaces();
    assertNotNull(namespaces);
    assertEquals(Position3D.NAMESPACE_URI, namespaces[0]);
    NodeId fallbackDataTypeId = new NodeId(0, "nsu=" + Position3D.NAMESPACE_URI + ";i=3001");
    assertEquals(fallbackDataTypeId, positionField.getDataType());

    // (b) the matching StructureDescription describes the inline struct members
    StructureDescription description = metaData.getStructureDataTypes()[0];
    assertEquals(positionField.getDataType(), description.getDataTypeId());
    assertEquals("nsu=" + Position3D.NAMESPACE_URI + ";Position3D", description.getName().name());
    StructureField[] structureFields = description.getStructureDefinition().getFields();
    assertEquals("X", structureFields[0].getName());
    assertEquals(NodeIds.Double, structureFields[0].getDataType());
    assertEquals("Y", structureFields[1].getName());
    assertEquals(NodeIds.Double, structureFields[1].getDataType());

    // endregion

    // region the data message's inline struct members match the description

    subscriberTransport.inject("opcua/json/data/line-7/grp", dataSent.json());
    flushSubscriber();

    DataSetReceivedEvent dataEvent = events.poll(10, TimeUnit.SECONDS);
    assertNotNull(dataEvent);
    assertEquals("custom-ds", dataEvent.dataSetName());

    DataSetFieldValue field = dataEvent.fields().get(0);
    assertEquals("position", field.name());
    assertEquals(POSITION_FIELD_ID, field.dataSetFieldId());

    // The codec-less subscriber surfaces the struct as a raw JSON ExtensionObject whose members
    // are exactly those the announced StructureDescription names.
    ExtensionObject.Json json =
        assertInstanceOf(ExtensionObject.Json.class, field.value().value().value());
    JsonObject body = JsonParser.parseString(json.getBody()).getAsJsonObject();
    assertEquals(1.5, body.get("X").getAsDouble());
    assertEquals(2.5, body.get("Y").getAsDouble());

    // endregion
  }

  /** The first DataSetMessage object of a captured {@code ua-data} document. */
  private static JsonObject firstDataSetMessage(Sent sent) {
    JsonObject networkMessage = JsonParser.parseString(sent.json()).getAsJsonObject();
    return networkMessage.get("Messages").getAsJsonArray().get(0).getAsJsonObject();
  }

  private void flushSubscriber() throws Exception {
    assertNotNull(subscriberExecutor);
    subscriberExecutor.submit(() -> {}).get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  /** Poll captured sends until one of the requested kind appears. */
  private static Sent awaitSent(StubBrokerTransport transport, MessageAddress.Kind kind)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      Sent sent = transport.sent.poll(100, TimeUnit.MILLISECONDS);
      if (sent != null && sent.address().kind() == kind) {
        return sent;
      }
    }
    fail("timed out waiting for a " + kind + " send");
    throw new AssertionError("unreachable");
  }

  /**
   * Poll metadata events until the one for the given reader path appears (handleMetaData notifies
   * every matched reader; only ACCEPT_DISCOVERED readers apply the metadata).
   */
  private static MetaDataReceivedEvent awaitMetaDataEvent(
      BlockingQueue<MetaDataReceivedEvent> events, String readerPath) throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      MetaDataReceivedEvent event = events.poll(100, TimeUnit.MILLISECONDS);
      if (event != null && readerPath.equals(event.reader().path())) {
        return event;
      }
    }
    fail("timed out waiting for a MetaDataReceivedEvent for " + readerPath);
    throw new AssertionError("unreachable");
  }

  /**
   * A hand-written custom structure following the Milo code-generated type conventions (static
   * {@code TYPE_ID} / encoding ids as namespace-URI-qualified ExpandedNodeIds, and a static {@code
   * definition(NamespaceTable)} method), registered only in the publisher's EncodingContext.
   */
  public static class Position3D implements UaStructuredType {

    static final String NAMESPACE_URI = "urn:milo:test:custom-types";

    public static final ExpandedNodeId TYPE_ID =
        ExpandedNodeId.parse("nsu=" + NAMESPACE_URI + ";i=3001");

    public static final ExpandedNodeId JSON_ENCODING_ID =
        ExpandedNodeId.parse("nsu=" + NAMESPACE_URI + ";i=3002");

    public static final ExpandedNodeId BINARY_ENCODING_ID =
        ExpandedNodeId.parse("nsu=" + NAMESPACE_URI + ";i=3003");

    private final double x;
    private final double y;

    Position3D(double x, double y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return TYPE_ID;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return JSON_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return BINARY_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
      return new StructureDefinition(
          BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow(),
          NodeIds.Structure,
          StructureType.Structure,
          new StructureField[] {
            new StructureField(
                "X", LocalizedText.NULL_VALUE, NodeIds.Double, -1, null, uint(0), false),
            new StructureField(
                "Y", LocalizedText.NULL_VALUE, NodeIds.Double, -1, null, uint(0), false)
          });
    }

    static class Codec extends GenericDataTypeCodec<Position3D> {
      @Override
      public Class<Position3D> getType() {
        return Position3D.class;
      }

      @Override
      public Position3D decodeType(EncodingContext context, UaDecoder decoder) {
        double x = decoder.decodeDouble("X");
        double y = decoder.decodeDouble("Y");
        return new Position3D(x, y);
      }

      @Override
      public void encodeType(EncodingContext context, UaEncoder encoder, Position3D value) {
        encoder.encodeDouble("X", value.x);
        encoder.encodeDouble("Y", value.y);
      }
    }
  }
}
