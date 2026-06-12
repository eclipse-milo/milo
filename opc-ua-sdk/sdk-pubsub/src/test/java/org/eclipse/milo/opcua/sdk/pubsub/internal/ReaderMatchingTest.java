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
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
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
      return new SubscriberChannel() {
        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
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
    transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    ReaderGroupConfig.Builder group = ReaderGroupConfig.builder("RG");
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
                    ushort(0),
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

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("EncodeW" + dataSetWriterId)
            .dataSet(new PublishedDataSetRef("EncodePDS"))
            .dataSetWriterId(ushort(dataSetWriterId))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(dataSetMessageContentMask)
                    .build())
            .build();

    return new DataSetMessageDraft(
        writer,
        ushort(0),
        null,
        null,
        configurationVersion,
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  private static UadpNetworkMessageContentMask nmMask(
      UadpNetworkMessageContentMask.Field... fields) {
    return UadpNetworkMessageContentMask.of(fields);
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
}
