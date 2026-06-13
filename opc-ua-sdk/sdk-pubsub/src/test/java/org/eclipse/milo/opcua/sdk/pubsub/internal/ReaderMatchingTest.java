/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the subscriber-side DataSetReader matching chain (Part 14 §6.2.9.x / §6.3.1.4.x):
 * PublisherId (exact type + value), WriterGroupId, GroupVersion, and DataSetWriterId filters, each
 * as wildcard (null/0) and as mismatch, plus the payload-header-present vs absent paths.
 *
 * <p>{@link ReaderDispatcher} needs the full engine plumbing, so the chain is exercised through
 * {@link PubSubService} with a stub in-memory transport: crafted UADP NetworkMessages are injected
 * into the subscriber channel consumer and reader listeners observe what matched. A single-threaded
 * transport executor makes dispatch completion observable via a barrier task.
 */
class ReaderMatchingTest {

  private static final UUID META_FIELD_ID = new UUID(0L, 7L);

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;
  private @Nullable StubTransport transport;

  private final Map<String, BlockingQueue<DataSetReceivedEvent>> eventsByReader =
      new ConcurrentHashMap<>();

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
    if (transportExecutor != null) {
      transportExecutor.shutdown();
      assertTrue(transportExecutor.awaitTermination(10, TimeUnit.SECONDS));
      transportExecutor = null;
    }
  }

  // region fixture

  private static final class StubTransport implements TransportProvider {

    final AtomicReference<Consumer<ByteBuf>> consumer = new AtomicReference<>();

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:stub-transport";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return true;
    }

    @Override
    public PublisherChannel openPublisher(PublisherTransportContext context) {
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          message.release();
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
      consumer.set(context.messageConsumer());
      return () -> CompletableFuture.completedFuture(null);
    }

    void inject(byte[] datagram) {
      Consumer<ByteBuf> messageConsumer = consumer.get();
      assertNotNull(messageConsumer, "subscriber channel was never opened");
      ByteBuf buffer = Unpooled.wrappedBuffer(datagram);
      try {
        messageConsumer.accept(buffer);
      } finally {
        buffer.release();
      }
    }
  }

  private void startService(DataSetReaderConfig... readers) throws Exception {
    startService(uint(0), readers);
  }

  private void startService(UInteger maxNetworkMessageSize, DataSetReaderConfig... readers)
      throws Exception {
    transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    ReaderGroupConfig.Builder group =
        ReaderGroupConfig.builder("RG").maxNetworkMessageSize(maxNetworkMessageSize);
    for (DataSetReaderConfig reader : readers) {
      group.dataSetReader(reader);
    }

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .readerGroup(group.build())
            .build();

    PubSubConfig config = PubSubConfig.builder().connection(connection).build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, PubSubBindings.builder().build(), serviceConfig);

    for (DataSetReaderConfig reader : readers) {
      var queue = new LinkedBlockingQueue<DataSetReceivedEvent>();
      eventsByReader.put(reader.getName(), queue);
      service.addDataSetListener(new DataSetReaderRef("conn", "RG", reader.getName()), queue::add);
    }

    service.startup().get(10, TimeUnit.SECONDS);
  }

  /**
   * Start a service with one MQTT (broker) connection "conn" carrying a uadp reader "U1" and a json
   * reader "J1" in group "RG": mixed mappings are a broker-connection shape (JSON message settings
   * are not valid on a UDP connection), and every injected message is offered to both mappings.
   */
  private void startMixedMappingService() throws Exception {
    transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    DataSetReaderConfig uadpReader =
        DataSetReaderConfig.builder("U1")
            .brokerTransport(BrokerTransportSettings.builder().queueName("topic/uadp").build())
            .build();
    DataSetReaderConfig jsonReader =
        DataSetReaderConfig.builder("J1")
            .settings(JsonDataSetReaderSettings.builder().build())
            .brokerTransport(BrokerTransportSettings.builder().queueName("topic/json").build())
            .build();

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .readerGroup(
                ReaderGroupConfig.builder("RG")
                    .dataSetReader(uadpReader)
                    .dataSetReader(jsonReader)
                    .build())
            .build();

    PubSubConfig config = PubSubConfig.builder().connection(connection).build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, PubSubBindings.builder().build(), serviceConfig);

    for (String name : List.of("U1", "J1")) {
      var queue = new LinkedBlockingQueue<DataSetReceivedEvent>();
      eventsByReader.put(name, queue);
      service.addDataSetListener(new DataSetReaderRef("conn", "RG", name), queue::add);
    }

    service.startup().get(10, TimeUnit.SECONDS);
  }

  private void injectAndFlush(byte[] frame) throws Exception {
    assertNotNull(transport);
    assertNotNull(transportExecutor);
    transport.inject(frame);
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);
  }

  private int eventCount(String readerName) {
    return eventsByReader.get(readerName).size();
  }

  private DataSetReceivedEvent takeEvent(String readerName) {
    DataSetReceivedEvent event = eventsByReader.get(readerName).poll();
    assertNotNull(event, "no event for reader " + readerName);
    return event;
  }

  private void clearEvents() {
    eventsByReader.values().forEach(BlockingQueue::clear);
  }

  /** Encode one NetworkMessage from crafted, standalone publisher-side configs. */
  private static byte[] encode(
      PublisherId publisherId,
      UadpNetworkMessageContentMask networkMessageContentMask,
      int writerGroupId,
      UInteger groupVersion,
      DataSetMessageDraft... drafts)
      throws UaException {

    return encode(publisherId, networkMessageContentMask, writerGroupId, groupVersion, 0, drafts);
  }

  /** Encode one NetworkMessage with an explicit NetworkMessage SequenceNumber header value. */
  private static byte[] encode(
      PublisherId publisherId,
      UadpNetworkMessageContentMask networkMessageContentMask,
      int writerGroupId,
      UInteger groupVersion,
      int networkMessageSequenceNumber,
      DataSetMessageDraft... drafts)
      throws UaException {

    WriterGroupConfig group =
        WriterGroupConfig.builder("EncodeWG")
            .writerGroupId(ushort(writerGroupId))
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(networkMessageContentMask)
                    .build())
            .build();

    List<EncodedNetworkMessage> encoded =
        new UadpMessageMapping()
            .encode(
                new EncodeContext(
                    new DefaultEncodingContext(),
                    publisherId,
                    group,
                    groupVersion,
                    ushort(1),
                    ushort(networkMessageSequenceNumber),
                    null,
                    List.of(drafts)));

    ByteBuf data = encoded.get(0).data();
    try {
      byte[] bytes = new byte[data.readableBytes()];
      data.readBytes(bytes);
      return bytes;
    } finally {
      data.release();
    }
  }

  private static DataSetMessageDraft draft(int dataSetWriterId, int value) {
    return draft(
        dataSetWriterId,
        UadpDataSetMessageContentMask.of(),
        new ConfigurationVersionDataType(uint(0), uint(0)),
        value);
  }

  private static DataSetMessageDraft draft(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      ConfigurationVersionDataType configurationVersion,
      int value) {

    return draft(
        dataSetWriterId,
        dataSetMessageContentMask,
        configurationVersion,
        0,
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  /** A key-frame draft that puts the DataSetMessage SequenceNumber on the wire. */
  private static DataSetMessageDraft sequencedDraft(
      int dataSetWriterId, int sequenceNumber, int value) {

    return draft(
        dataSetWriterId,
        UadpDataSetMessageContentMask.of(UadpDataSetMessageContentMask.Field.SequenceNumber),
        new ConfigurationVersionDataType(uint(0), uint(0)),
        sequenceNumber,
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  /** A keep-alive draft carrying the next expected sequence number (Part 14 §7.2.4.5.8). */
  private static DataSetMessageDraft keepAliveDraft(int dataSetWriterId, int sequenceNumber) {
    return draft(
        dataSetWriterId,
        UadpDataSetMessageContentMask.of(UadpDataSetMessageContentMask.Field.SequenceNumber),
        new ConfigurationVersionDataType(uint(0), uint(0)),
        sequenceNumber,
        true,
        List.of());
  }

  private static DataSetMessageDraft draft(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      ConfigurationVersionDataType configurationVersion,
      int sequenceNumber,
      boolean keepAlive,
      List<DataValue> values) {

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("EncodeW" + dataSetWriterId)
            .dataSet(new PublishedDataSetRef("EncodePDS"))
            .dataSetWriterId(ushort(dataSetWriterId))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(dataSetMessageContentMask)
                    .build())
            .build();

    return DataSetMessageDraft.of(
        writer, uint(sequenceNumber), null, null, configurationVersion, keepAlive, values);
  }

  private static UadpNetworkMessageContentMask nmMask(
      UadpNetworkMessageContentMask.Field... fields) {
    return UadpNetworkMessageContentMask.of(fields);
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private PubSubDiagnostics.ComponentDiagnostics diagnostics(String path) {
    PubSubDiagnostics.ComponentDiagnostics diagnostics = service.diagnostics().snapshot().get(path);
    assertNotNull(diagnostics, "no diagnostics for " + path);
    return diagnostics;
  }

  // endregion

  @Test
  void publisherIdFilterMatchesExactTypeOrCanonicalStringForm() throws Exception {
    startService(
        DataSetReaderConfig.builder("any").build(),
        DataSetReaderConfig.builder("byte5").publisherId(PublisherId.ubyte(ubyte(5))).build(),
        DataSetReaderConfig.builder("u16-5").publisherId(PublisherId.uint16(ushort(5))).build(),
        DataSetReaderConfig.builder("u32-5").publisherId(PublisherId.uint32(uint(5))).build(),
        DataSetReaderConfig.builder("u64-5").publisherId(PublisherId.uint64(ulong(5))).build(),
        DataSetReaderConfig.builder("str").publisherId(PublisherId.string("pub")).build());

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    // UInt16 5 matches every filter whose canonical string form is "5" — ids of differing types
    // compare by canonical string form — plus the wildcard
    injectAndFlush(encode(PublisherId.uint16(ushort(5)), mask, 1, uint(0), draft(10, 1)));
    assertEquals(1, eventCount("any"));
    assertEquals(1, eventCount("u16-5"));
    assertEquals(1, eventCount("byte5"));
    assertEquals(1, eventCount("u32-5"));
    assertEquals(1, eventCount("u64-5"));
    assertEquals(0, eventCount("str"));
    assertEquals(PublisherId.uint16(ushort(5)), takeEvent("u16-5").publisherId());
    clearEvents();

    // a String id matches numeric filters by canonical string form (the JSON mapping carries
    // every PublisherId as a String)
    injectAndFlush(encode(PublisherId.string("5"), mask, 1, uint(0), draft(10, 1)));
    assertEquals(1, eventCount("byte5"));
    assertEquals(1, eventCount("u16-5"));
    assertEquals(1, eventCount("u32-5"));
    assertEquals(1, eventCount("u64-5"));
    assertEquals(0, eventCount("str"));
    assertEquals(1, eventCount("any"));
    clearEvents();

    // a String id with a different canonical form matches only its own filter
    injectAndFlush(encode(PublisherId.string("pub"), mask, 1, uint(0), draft(10, 1)));
    assertEquals(1, eventCount("str"));
    assertEquals(0, eventCount("u16-5"));
    assertEquals(0, eventCount("byte5"));
    assertEquals(1, eventCount("any"));

    // a different value does not match, regardless of type
    clearEvents();
    injectAndFlush(encode(PublisherId.uint16(ushort(6)), mask, 1, uint(0), draft(10, 1)));
    assertEquals(0, eventCount("u16-5"));
    assertEquals(0, eventCount("byte5"));
    assertEquals(1, eventCount("any"));
  }

  @Test
  void messageWithoutPublisherIdMatchesOnlyWildcardReaders() throws Exception {
    startService(
        DataSetReaderConfig.builder("any").build(),
        DataSetReaderConfig.builder("u16-5").publisherId(PublisherId.uint16(ushort(5))).build());

    // PublisherId bit clear: the id is not on the wire even though the encoder was given one
    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(5)),
            nmMask(UadpNetworkMessageContentMask.Field.PayloadHeader),
            1,
            uint(0),
            draft(10, 1)));

    assertEquals(0, eventCount("u16-5"));
    assertEquals(1, eventCount("any"));

    // no wire id and no filter: the event falls back to PublisherId.string("")
    assertEquals(PublisherId.string(""), takeEvent("any").publisherId());
  }

  @Test
  void writerGroupIdFilterMatchesGroupHeaderValue() throws Exception {
    startService(
        DataSetReaderConfig.builder("wg7").writerGroupId(ushort(7)).build(),
        DataSetReaderConfig.builder("wg0").writerGroupId(ushort(0)).build(),
        DataSetReaderConfig.builder("wild").build());

    var groupHeaderMask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.GroupHeader,
            UadpNetworkMessageContentMask.Field.WriterGroupId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    injectAndFlush(
        encode(PublisherId.uint16(ushort(1)), groupHeaderMask, 7, uint(0), draft(10, 1)));
    assertEquals(1, eventCount("wg7"));
    assertEquals(1, eventCount("wg0"));
    assertEquals(1, eventCount("wild"));
    assertEquals(ushort(7), takeEvent("wg7").writerGroupId());
    clearEvents();

    // mismatching WriterGroupId
    injectAndFlush(
        encode(PublisherId.uint16(ushort(1)), groupHeaderMask, 8, uint(0), draft(10, 1)));
    assertEquals(0, eventCount("wg7"));
    assertEquals(1, eventCount("wg0"));
    assertEquals(1, eventCount("wild"));
    clearEvents();

    // no GroupHeader on the wire: the identifier is absent so the filter cannot be applied and
    // the message matches by configuration (Part 14 6.2.9; open62541-compatible)
    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(1)),
            nmMask(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.PayloadHeader),
            7,
            uint(0),
            draft(10, 1)));
    assertEquals(1, eventCount("wg7"));
    assertEquals(1, eventCount("wg0"));
    assertEquals(1, eventCount("wild"));
  }

  @Test
  void dataSetWriterIdFilterSelectsMessagesFromPayloadHeader() throws Exception {
    startService(
        DataSetReaderConfig.builder("w10").dataSetWriterId(ushort(10)).build(),
        DataSetReaderConfig.builder("w0").dataSetWriterId(ushort(0)).build(),
        DataSetReaderConfig.builder("w12").dataSetWriterId(ushort(12)).build(),
        DataSetReaderConfig.builder("wild").build());

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    injectAndFlush(
        encode(PublisherId.uint16(ushort(1)), mask, 1, uint(0), draft(10, 1), draft(11, 2)));

    // the filter selects individual DataSetMessages within the NetworkMessage
    assertEquals(1, eventCount("w10"));
    assertEquals(ushort(10), takeEvent("w10").dataSetWriterId());

    // wildcard filters (0 and unset) receive both messages
    assertEquals(2, eventCount("w0"));
    assertEquals(2, eventCount("wild"));

    // mismatching filter receives nothing
    assertEquals(0, eventCount("w12"));
  }

  @Test
  void absentPayloadHeaderTreatsPayloadAsSingleMessageForAllReaders() throws Exception {
    startService(
        DataSetReaderConfig.builder("w10").dataSetWriterId(ushort(10)).build(),
        DataSetReaderConfig.builder("wild").build());

    // no PayloadHeader: the payload is one DataSetMessage with no DataSetWriterId. The id is
    // absent from the wire, so a configured filter cannot be applied and the reader identifies
    // the message by its configuration (fixed-layout publishers, Part 14 6.2.9).
    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(1)),
            nmMask(UadpNetworkMessageContentMask.Field.PublisherId),
            1,
            uint(0),
            draft(10, 5)));

    assertEquals(1, eventCount("w10"));
    assertEquals(1, eventCount("wild"));

    // no wire id: the event id falls back to the configured filter, then 0
    assertEquals(ushort(10), takeEvent("w10").dataSetWriterId());
    DataSetReceivedEvent event = takeEvent("wild");
    assertEquals(ushort(0), event.dataSetWriterId());
    assertEquals(Variant.ofInt32(5), event.fields().get(0).value().getValue());
  }

  @Test
  void groupVersionFilterMatchesGroupHeaderValue() throws Exception {
    startService(
        DataSetReaderConfig.builder("gv42")
            .settings(UadpDataSetReaderSettings.builder().groupVersion(uint(42)).build())
            .build(),
        DataSetReaderConfig.builder("wild").build());

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.GroupHeader,
            UadpNetworkMessageContentMask.Field.GroupVersion,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    injectAndFlush(encode(PublisherId.uint16(ushort(1)), mask, 1, uint(42), draft(10, 1)));
    assertEquals(1, eventCount("gv42"));
    assertEquals(1, eventCount("wild"));
    clearEvents();

    injectAndFlush(encode(PublisherId.uint16(ushort(1)), mask, 1, uint(43), draft(10, 1)));
    assertEquals(0, eventCount("gv42"));
    assertEquals(1, eventCount("wild"));
  }

  @Test
  void majorVersionMismatchIsDroppedWithDiagnostics() throws Exception {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("MD")
            .field("F1", NodeIds.Int32, META_FIELD_ID)
            .configurationVersion(uint(2), uint(2))
            .build();

    startService(DataSetReaderConfig.builder("R1").dataSetMetaData(metaData).build());

    var versionMask =
        UadpDataSetMessageContentMask.of(
            UadpDataSetMessageContentMask.Field.MajorVersion,
            UadpDataSetMessageContentMask.Field.MinorVersion);
    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    // message major version 1 != configured major version 2: dropped, with diagnostics
    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(1)),
            mask,
            1,
            uint(0),
            draft(10, versionMask, new ConfigurationVersionDataType(uint(1), uint(1)), 1)));

    assertEquals(0, eventCount("R1"));

    var diagnostics = service.diagnostics().snapshot().get("conn/RG/R1");
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_ConfigurationError), diagnostics.lastError());

    // a message with the matching major version is delivered, named per the metadata
    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(1)),
            mask,
            1,
            uint(0),
            draft(10, versionMask, new ConfigurationVersionDataType(uint(2), uint(2)), 9)));

    assertEquals(1, eventCount("R1"));
    DataSetReceivedEvent event = takeEvent("R1");
    assertEquals("MD", event.dataSetName());
    assertEquals(metaData, event.metaData());
    assertEquals("F1", event.fields().get(0).name());
    assertEquals(META_FIELD_ID, event.fields().get(0).dataSetFieldId());
    assertEquals(Variant.ofInt32(9), event.fields().get(0).value().getValue());
  }

  /** A truncated UADP NetworkMessage ticks decodeErrors and lastError at the connection path. */
  @Test
  void truncatedNetworkMessageTicksConnectionDecodeErrors() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);
    byte[] full = encode(PublisherId.uint16(ushort(1)), mask, 1, uint(0), draft(10, 1));

    // cut mid-DataSetMessage: the tolerant UADP decode surfaces a failure instead of throwing
    injectAndFlush(Arrays.copyOf(full, full.length - 2));

    assertEquals(0, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics connection = diagnostics("conn");
    assertEquals(1, connection.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), connection.lastError());

    // an intact message still flows afterwards
    injectAndFlush(full);
    assertEquals(1, eventCount("R1"));
  }

  /** An inbound chunked NetworkMessage is no longer silently skipped (Part 14 §7.2.4.4.4). */
  @Test
  void chunkedNetworkMessageTicksConnectionDecodeErrorsWithBadNotSupported() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    injectAndFlush(
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x01, 0x00, 0x05, 0x00, 0x00, 0x00)); // (chunk data, skipped)

    assertEquals(0, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics connection = diagnostics("conn");
    assertEquals(1, connection.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_NotSupported), connection.lastError());
  }

  /**
   * A reader group with a non-zero maxNetworkMessageSize does not accept larger NetworkMessages:
   * the drop is attributed to the GROUP path with Bad_EncodingLimitsExceeded (a Milo extension;
   * Part 14 gives the parameter no receive-side semantics).
   */
  @Test
  void readerGroupMaxNetworkMessageSizeDropsOversizeAtTheGroup() throws Exception {
    byte[] small = encode(PublisherId.uint16(ushort(1)), nmMask(), 1, uint(0), draft(10, 1));
    byte[] big =
        encode(
            PublisherId.uint16(ushort(1)),
            nmMask(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.PayloadHeader),
            1,
            uint(0),
            draft(10, 1),
            draft(11, 2));
    assertTrue(big.length > small.length);

    startService(uint(small.length), DataSetReaderConfig.builder("R1").build());

    injectAndFlush(big);
    assertEquals(0, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics group = diagnostics("conn/RG");
    assertEquals(1, group.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded), group.lastError());

    // the message decoded fine; the drop belongs to the group, not the connection
    assertEquals(0, diagnostics("conn").decodeErrors());

    // a message within the limit is still delivered
    injectAndFlush(small);
    assertEquals(1, eventCount("R1"));
  }

  /** networkMessagesReceived counts arrivals: once per injected datagram, whatever the outcome. */
  @Test
  void networkMessagesReceivedTicksOncePerArrival() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);
    byte[] valid = encode(PublisherId.uint16(ushort(1)), mask, 1, uint(0), draft(10, 1));

    // a datagram that decodes and delivers
    injectAndFlush(valid);
    assertEquals(1, diagnostics("conn").networkMessagesReceived());

    // a datagram whose decode fails
    injectAndFlush(Arrays.copyOf(valid, valid.length - 2));
    assertEquals(2, diagnostics("conn").networkMessagesReceived());

    // a datagram that is not UADP at all (version nibble 15): tolerated skip
    injectAndFlush(bytes(0xFF, 0xFF, 0xFF));
    assertEquals(3, diagnostics("conn").networkMessagesReceived());

    // only the truncated datagram counted as a decode error
    assertEquals(1, diagnostics("conn").decodeErrors());
  }

  /**
   * On a mixed-mapping connection every message is offered to every mapping, so the "wrong"
   * mapping's failure on a message the right mapping decoded is expected and suppressed: valid
   * traffic of either mapping ticks no decodeErrors.
   */
  @Test
  void validTrafficOnMixedMappingConnectionTicksNoDecodeErrors() throws Exception {
    startMixedMappingService();

    var mask =
        nmMask(
            UadpNetworkMessageContentMask.Field.PublisherId,
            UadpNetworkMessageContentMask.Field.PayloadHeader);

    // a valid UADP message: the JSON mapping's parse failure on UADP bytes is suppressed
    injectAndFlush(encode(PublisherId.uint16(ushort(1)), mask, 1, uint(0), draft(10, 1)));
    assertEquals(1, eventCount("U1"));
    assertEquals(0, eventCount("J1"));
    assertEquals(0, diagnostics("conn").decodeErrors());

    // a valid JSON message: the UADP mapping skips the foreign version nibble without failure
    injectAndFlush("{\"x\": 42}".getBytes(StandardCharsets.UTF_8));
    assertEquals(1, eventCount("J1"));
    assertEquals(1, eventCount("U1"));
    assertEquals(0, diagnostics("conn").decodeErrors());
  }

  /**
   * The mixed-mapping suppression applies only when some mapping decoded content from the buffer: a
   * malformed JSON payload is foreign input to the UADP decoder (the version nibble of '{' is not
   * 1), and its empty tolerated skip decoded nothing, so the JSON mapping's genuine failure still
   * ticks decodeErrors at the connection path.
   */
  @Test
  void malformedJsonOnMixedMappingConnectionTicksConnectionDecodeErrors() throws Exception {
    startMixedMappingService();

    // a truncated JSON document: no mapping can decode it
    injectAndFlush("{\"Messages\":".getBytes(StandardCharsets.UTF_8));

    assertEquals(0, eventCount("U1"));
    assertEquals(0, eventCount("J1"));

    PubSubDiagnostics.ComponentDiagnostics connection = diagnostics("conn");
    assertEquals(1, connection.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), connection.lastError());
  }

  /** A buffer no mapping can decode ticks decodeErrors once per failed mapping. */
  @Test
  void bufferFailingEveryMappingTicksDecodeErrorsPerMapping() throws Exception {
    startMixedMappingService();

    // version nibble 1 but truncated before the payload: the UADP decode surfaces a failure, and
    // the bytes are not a JSON document either
    injectAndFlush(
        bytes(
            0x11, // byte 0: version 1 | PublisherId enabled (Byte type)
            0x07)); // PublisherId: Byte = 7; the payload is missing entirely

    assertEquals(0, eventCount("U1"));
    assertEquals(0, eventCount("J1"));

    PubSubDiagnostics.ComponentDiagnostics connection = diagnostics("conn");
    assertEquals(2, connection.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), connection.lastError());
  }

  /**
   * A failing mapping's own partial content does not suppress its own failure but does suppress the
   * other mappings': a truncated UADP message that still delivered a decodable prefix ticks
   * decodeErrors exactly once on a mixed connection — the UADP Bad_DecodingError, not also the JSON
   * mapping's parse failure on the same bytes.
   */
  @Test
  void truncatedUadpWithDecodedPrefixOnMixedMappingConnectionTicksOnce() throws Exception {
    startMixedMappingService();

    // PayloadHeader promises two sized DataSetMessages but only the first arrived: the UADP
    // decode delivers DSM 1 and surfaces a failure for the missing DSM 2 (Sizes[1] exceeds the
    // zero remaining bytes); the bytes are not a JSON document either
    injectAndFlush(
        bytes(
            0x41, // byte 0: version 1 | PayloadHeader 0x40
            0x02, // PayloadHeader: Count = 2
            0x01, 0x00, // DataSetWriterIds[0] = 1
            0x02, 0x00, // DataSetWriterIds[1] = 2
            0x05, 0x00, // Sizes[0] = 5
            0x05, 0x00, // Sizes[1] = 5
            0x01, 0x01, 0x00, 0x01, 0x01)); // DSM 1: valid key frame, Boolean true; DSM 2 missing

    // the decoded prefix was still delivered (partial-delivery posture)
    assertEquals(1, eventCount("U1"));
    assertEquals(0, eventCount("J1"));

    // exactly one decodeError: the UADP failure stays observable, the JSON failure is suppressed
    PubSubDiagnostics.ComponentDiagnostics connection = diagnostics("conn");
    assertEquals(1, connection.decodeErrors());
    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), connection.lastError());
  }

  // region Part 14 §7.2.3 sequence-number window

  private static final UadpNetworkMessageContentMask SEQ_TEST_MASK =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  /** PublisherId | GroupHeader | WriterGroupId | SequenceNumber | PayloadHeader. */
  private static final UadpNetworkMessageContentMask GROUP_SEQ_MASK =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.GroupHeader,
          UadpNetworkMessageContentMask.Field.WriterGroupId,
          UadpNetworkMessageContentMask.Field.SequenceNumber,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  @Test
  void duplicateDataSetMessageIsDroppedAndCounted() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));
    byte[] seq5 = encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 1));

    injectAndFlush(seq5);
    assertEquals(1, eventCount("R1"));
    DataSetReceivedEvent event = takeEvent("R1");
    assertEquals(uint(5), event.dataSetMessageSequenceNumber());
    // no GroupHeader on the wire: no NetworkMessage sequence number
    assertNull(event.networkMessageSequenceNumber());

    // the exact same DataSetMessage again: dropped as stale, listener sees nothing
    injectAndFlush(seq5);
    assertEquals(0, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics reader = diagnostics("conn/RG/R1");
    assertEquals(1, reader.staleSequenceMessages());
    assertEquals(0, reader.invalidSequenceMessages());
    // sequence drops are not "received" DataSetMessages and are not error-class events
    assertEquals(1, reader.dataSetMessagesReceived());
    assertEquals(0, reader.decodeErrors());
    assertNull(reader.lastError());

    // the next number still flows
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 6, 2)));
    assertEquals(1, eventCount("R1"));
    assertEquals(uint(6), takeEvent("R1").dataSetMessageSequenceNumber());
  }

  @Test
  void reorderedOlderDataSetMessageIsDropped() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 1)));
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 6, 2)));
    assertEquals(2, eventCount("R1"));
    clearEvents();

    // older-than-last-processed arrivals are "older than (or same as)" and dropped
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 1)));
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 4, 1)));
    assertEquals(0, eventCount("R1"));
    assertEquals(2, diagnostics("conn/RG/R1").staleSequenceMessages());

    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 7, 3)));
    assertEquals(1, eventCount("R1"));
  }

  @Test
  void largeForwardJumpIsInvalidAndDoesNotAdvanceTheWindow() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 100, 1)));
    assertEquals(1, eventCount("R1"));
    clearEvents();

    // (received - 1 - last) mod 2^16 = 16384 = the lower bound: "other results are invalid"
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 100 + 16385, 2)));
    assertEquals(0, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics reader = diagnostics("conn/RG/R1");
    assertEquals(1, reader.invalidSequenceMessages());
    assertEquals(0, reader.staleSequenceMessages());

    // the invalid message did not advance the window: the genuine next number is still NEW
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 101, 3)));
    assertEquals(1, eventCount("R1"));
  }

  @Test
  void wildcardReaderTracksEachPublisherStreamIndependently() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pubA = PublisherId.uint16(ushort(1));
    PublisherId pubB = PublisherId.uint16(ushort(2));

    // interleaved streams from two publishers, both at sequence number 5: independent windows
    injectAndFlush(encode(pubA, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 1)));
    injectAndFlush(encode(pubB, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 2)));
    assertEquals(2, eventCount("R1"));
    clearEvents();

    // a duplicate from A is dropped while B's stream advances normally
    injectAndFlush(encode(pubA, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 5, 1)));
    injectAndFlush(encode(pubB, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 6, 3)));

    assertEquals(1, eventCount("R1"));
    assertEquals(PublisherId.uint16(ushort(2)), takeEvent("R1").publisherId());
    assertEquals(1, diagnostics("conn/RG/R1").staleSequenceMessages());
  }

  /**
   * The off-by-one trap (Part 14 §7.2.4.5.8): a keep-alive carries the NEXT EXPECTED sequence
   * number without consuming it, so the data DataSetMessage that follows carries the SAME number
   * and must be accepted; repeated keep-alives must not count as duplicates.
   */
  @Test
  void keepAliveSeedsTheWindowAndTheDataMessageWithTheSameNumberIsAccepted() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    // a keep-alive seeds the unseeded stream (next expected = 7); not delivered to listeners
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), keepAliveDraft(10, 7)));
    assertEquals(0, eventCount("R1"));

    // the data message carrying the same number 7 is NEW
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 7, 1)));
    assertEquals(1, eventCount("R1"));
    assertEquals(uint(7), takeEvent("R1").dataSetMessageSequenceNumber());

    // repeated keep-alives (all carrying next expected 8) never advance the window and never
    // tick the sequence-drop counters
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), keepAliveDraft(10, 8)));
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), keepAliveDraft(10, 8)));
    injectAndFlush(encode(pub, SEQ_TEST_MASK, 1, uint(0), sequencedDraft(10, 8, 2)));
    assertEquals(1, eventCount("R1"));

    PubSubDiagnostics.ComponentDiagnostics reader = diagnostics("conn/RG/R1");
    assertEquals(0, reader.staleSequenceMessages());
    assertEquals(0, reader.invalidSequenceMessages());
    // 2 data messages + 3 keep-alives were received
    assertEquals(5, reader.dataSetMessagesReceived());
  }

  /**
   * The NetworkMessage window is checked first: a duplicated NetworkMessage drops every matched
   * DataSetMessage it carries, ticking the stale counter once per suppressed DataSetMessage.
   */
  @Test
  void duplicateNetworkMessageDropsAllItsDataSetMessages() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));
    byte[] nmSeq3 = encode(pub, GROUP_SEQ_MASK, 7, uint(0), 3, draft(10, 1), draft(11, 2));

    injectAndFlush(nmSeq3);
    assertEquals(2, eventCount("R1"));
    DataSetReceivedEvent event = takeEvent("R1");
    assertEquals(ushort(3), event.networkMessageSequenceNumber());
    // the drafts carry no DataSetMessage sequence number
    assertNull(event.dataSetMessageSequenceNumber());
    clearEvents();

    // the same NetworkMessage again: both DataSetMessages suppressed, one tick each
    injectAndFlush(nmSeq3);
    assertEquals(0, eventCount("R1"));
    assertEquals(2, diagnostics("conn/RG/R1").staleSequenceMessages());

    // the next NetworkMessage number flows, both DataSetMessages delivered
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 4, draft(10, 3), draft(11, 4)));
    assertEquals(2, eventCount("R1"));
  }

  /**
   * The NetworkMessage carrying a keep-alive consumes a NetworkMessage sequence number like any
   * other (Part 14 §7.2.3; §7.2.4.5.8's next-expected rule applies to the DataSetMessage number
   * only), so keep-alive-only NetworkMessages must advance the NetworkMessage window — otherwise a
   * long keep-alive-only period freezes the window while the publisher's counter advances, and
   * resumed data is wrongly dropped as invalid, then stale. The keep-alive itself is never dropped
   * on the NetworkMessage verdict.
   */
  @Test
  void keepAliveNetworkMessagesAdvanceTheNetworkMessageWindow() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    // data seeds the NetworkMessage window at 10
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 10, draft(10, 1)));
    assertEquals(1, eventCount("R1"));
    clearEvents();

    // a keep-alive-only NetworkMessage advances the window to 11; not delivered, no drop ticks
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 11, keepAliveDraft(10, 5)));
    assertEquals(0, eventCount("R1"));
    assertEquals(0, diagnostics("conn/RG/R1").staleSequenceMessages());
    assertEquals(0, diagnostics("conn/RG/R1").invalidSequenceMessages());

    // a data NetworkMessage replaying the keep-alive's number is "older than (or same as)" the
    // last processed NetworkMessage and dropped
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 11, draft(10, 2)));
    assertEquals(0, eventCount("R1"));
    assertEquals(1, diagnostics("conn/RG/R1").staleSequenceMessages());

    // a keep-alive in a stale NetworkMessage is itself never dropped: still received (it must
    // keep resetting the receive timeout per §6.2.9.6), and it ticks no drop counters
    long received = diagnostics("conn/RG/R1").dataSetMessagesReceived();
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 11, keepAliveDraft(10, 5)));
    assertEquals(received + 1, diagnostics("conn/RG/R1").dataSetMessagesReceived());
    assertEquals(1, diagnostics("conn/RG/R1").staleSequenceMessages());
    assertEquals(0, diagnostics("conn/RG/R1").invalidSequenceMessages());

    // the next NetworkMessage number flows
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 12, draft(10, 3)));
    assertEquals(1, eventCount("R1"));
  }

  /**
   * The NetworkMessage window advances on every NEW NetworkMessage, even when its matched
   * DataSetMessages are all dropped at the DataSetMessage window: the publisher consumed the number
   * (§7.2.3), so deferring the advance to delivery would open the same window-freeze gap as
   * keep-alive-only periods.
   */
  @Test
  void networkMessageWindowAdvancesWhenAllDataSetMessagesAreDroppedAtTheDataSetMessageWindow()
      throws Exception {

    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 0, sequencedDraft(10, 5, 1)));
    assertEquals(1, eventCount("R1"));
    clearEvents();

    // NetworkMessage 1 is NEW and advances its window even though its only DataSetMessage is a
    // duplicate dropped at the DataSetMessage window
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 1, sequencedDraft(10, 5, 1)));
    assertEquals(0, eventCount("R1"));
    assertEquals(1, diagnostics("conn/RG/R1").staleSequenceMessages());

    // so a replay of NetworkMessage 1 is dropped at the NetworkMessage window, fresh payload
    // notwithstanding ...
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 1, sequencedDraft(10, 6, 2)));
    assertEquals(0, eventCount("R1"));
    assertEquals(2, diagnostics("conn/RG/R1").staleSequenceMessages());

    // ... and the genuine next NetworkMessage flows, its DataSetMessage still NEW
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 2, sequencedDraft(10, 6, 3)));
    assertEquals(1, eventCount("R1"));
    assertEquals(uint(6), takeEvent("R1").dataSetMessageSequenceNumber());
  }

  @Test
  void bothSequenceNumbersSurfaceOnTheEvent() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    injectAndFlush(
        encode(
            PublisherId.uint16(ushort(1)),
            GROUP_SEQ_MASK,
            7,
            uint(0),
            9,
            sequencedDraft(10, 5, 1)));

    assertEquals(1, eventCount("R1"));
    DataSetReceivedEvent event = takeEvent("R1");
    assertEquals(ushort(9), event.networkMessageSequenceNumber());
    assertEquals(uint(5), event.dataSetMessageSequenceNumber());
  }

  /**
   * The NetworkMessage window is observed once per (reader, NetworkMessage) for every
   * NetworkMessage that passes the NetworkMessage-level filters and carries a sequence number —
   * even when ZERO contained DataSetMessages match the reader's dataSetWriterId filter: the
   * (PublisherId, WriterGroupId) stream consumed those numbers either way. A reader filtered to a
   * quiet writer A must not have its NetworkMessage window starved while writer B of the same group
   * consumes thousands of NetworkMessage sequence numbers — before this, A's next data
   * NetworkMessage computed a huge forward jump and was wrongly dropped as INVALID.
   */
  @Test
  void networkMessageWindowAdvancesOnNetworkMessagesWithNoMatchedDataSetMessages()
      throws Exception {

    startService(DataSetReaderConfig.builder("R1").dataSetWriterId(ushort(10)).build());

    PublisherId pub = PublisherId.uint16(ushort(1));

    // writer A's data NetworkMessage seeds R1's NetworkMessage window at 0
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 0, draft(10, 1)));
    assertEquals(1, eventCount("R1"));
    clearEvents();

    // writer B consumes 20000 NetworkMessage sequence numbers of the same group while A is
    // silent; none of these DataSetMessages match R1's filter, so nothing is delivered and no
    // drop counters tick — but each NetworkMessage must advance R1's window
    assertNotNull(transport);
    assertNotNull(transportExecutor);
    DataSetMessageDraft writerBDraft = draft(11, 2);
    for (int sequenceNumber = 1; sequenceNumber <= 20000; sequenceNumber++) {
      transport.inject(encode(pub, GROUP_SEQ_MASK, 7, uint(0), sequenceNumber, writerBDraft));
    }
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);

    assertEquals(0, eventCount("R1"));
    assertEquals(0, diagnostics("conn/RG/R1").staleSequenceMessages());
    assertEquals(0, diagnostics("conn/RG/R1").invalidSequenceMessages());

    // A's next data NetworkMessage: (20001 - 1 - 20000) mod 2^16 = 0 → NEW and delivered. With
    // the window stuck at 0 it would compute 20000 ≥ 2^14 and classify INVALID.
    injectAndFlush(encode(pub, GROUP_SEQ_MASK, 7, uint(0), 20001, draft(10, 3)));
    assertEquals(1, eventCount("R1"));
    assertEquals(0, diagnostics("conn/RG/R1").invalidSequenceMessages());
  }

  /** §6.2.9.6: without a sequence number "each received DataSetMessage is considered new". */
  @Test
  void dataSetMessagesWithoutSequenceNumbersBypassTheWindow() throws Exception {
    startService(DataSetReaderConfig.builder("R1").build());

    PublisherId pub = PublisherId.uint16(ushort(1));
    byte[] frame = encode(pub, SEQ_TEST_MASK, 1, uint(0), draft(10, 1));

    injectAndFlush(frame);
    injectAndFlush(frame);
    injectAndFlush(frame);

    assertEquals(3, eventCount("R1"));
    assertNull(takeEvent("R1").dataSetMessageSequenceNumber());

    PubSubDiagnostics.ComponentDiagnostics reader = diagnostics("conn/RG/R1");
    assertEquals(0, reader.staleSequenceMessages());
    assertEquals(0, reader.invalidSequenceMessages());
    assertEquals(3, reader.dataSetMessagesReceived());
  }

  // endregion
}
