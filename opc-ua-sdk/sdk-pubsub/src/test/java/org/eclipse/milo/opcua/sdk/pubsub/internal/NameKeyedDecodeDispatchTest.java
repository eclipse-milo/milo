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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
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
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the E6 dispatch evolution: hand-decoded DataSetMessages with name-keyed {@link
 * DecodedField}s (as the JSON mapping produces) are matched against the reader's effective metadata
 * by NAME — the metadata position becomes the {@link DataSetFieldValue} index and the metadata
 * dataSetFieldId is resolved; unknown wire names are kept with the null fieldId — and PublisherIds
 * of differing types compare by canonical string form through a real reader filter.
 *
 * <p>A stub {@link MessageMappingProvider} registered under the name {@code "uadp"} (configured
 * providers shadow the built-in) returns the hand-built {@link DecodedNetworkMessage}, so the wire
 * format is irrelevant: dispatch semantics are exercised in isolation.
 */
class NameKeyedDecodeDispatchTest {

  private static final UUID FIELD_ID_A = new UUID(0L, 0xAL);
  private static final UUID FIELD_ID_B = new UUID(0L, 0xBL);
  private static final UUID NULL_FIELD_ID = new UUID(0L, 0L);

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;
  private @Nullable StubTransport transport;
  private @Nullable StubMapping mapping;

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

  /** Shadows the built-in "uadp" mapping and decodes everything to a canned result. */
  private static final class StubMapping implements MessageMappingProvider {

    final AtomicReference<DecodedNetworkMessage> next = new AtomicReference<>();

    @Override
    public String mappingName() {
      return "uadp";
    }

    @Override
    public List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
      throw new UaException(StatusCodes.Bad_NotSupported, "decode-only stub");
    }

    @Override
    public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException {
      DecodedNetworkMessage decoded = next.get();
      if (decoded == null) {
        throw new UaException(StatusCodes.Bad_DecodingError, "no canned message");
      }
      return decoded;
    }
  }

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
    mapping = new StubMapping();
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
            .messageMappingProvider(mapping)
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

  private void injectAndFlush(DecodedNetworkMessage decoded) throws Exception {
    assertNotNull(transport);
    assertNotNull(mapping);
    assertNotNull(transportExecutor);
    mapping.next.set(decoded);
    transport.inject(new byte[] {0x00});
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

  private static DecodedNetworkMessage networkMessage(
      @Nullable PublisherId publisherId, List<DecodedField> fields) {
    return networkMessage(publisherId, 1, fields);
  }

  /**
   * A canned message with an explicit DataSetMessage sequence number: repeated injections on the
   * same (PublisherId, DataSetWriterId) stream must increment it, or the §7.2.3 window drops the
   * repeat as a duplicate.
   */
  private static DecodedNetworkMessage networkMessage(
      @Nullable PublisherId publisherId, int sequenceNumber, List<DecodedField> fields) {

    var message =
        new DecodedDataSetMessage(
            ushort(10),
            DataSetMessageKind.KEY_FRAME,
            true,
            uint(sequenceNumber),
            null,
            null,
            null,
            fields);

    return DecodedNetworkMessage.of(
        publisherId, null, null, null, null, null, List.of(message), List.of());
  }

  private static DataSetMetaDataConfig metaDataAB() {
    return DataSetMetaDataConfig.builder("MD")
        .field("A", NodeIds.Int32, FIELD_ID_A)
        .field("B", NodeIds.Int32, FIELD_ID_B)
        .build();
  }

  // endregion

  @Test
  void nameKeyedFieldsMatchMetadataByNameAndAdoptMetadataPosition() throws Exception {
    startService(DataSetReaderConfig.builder("R1").dataSetMetaData(metaDataAB()).build());

    // wire order is B then A: name matching must resolve each field to its metadata position
    injectAndFlush(
        networkMessage(
            PublisherId.string("pub"),
            List.of(
                new DecodedField(0, "B", new DataValue(Variant.ofInt32(22))),
                new DecodedField(1, "A", new DataValue(Variant.ofInt32(11))))));

    assertEquals(1, eventCount("R1"));
    DataSetReceivedEvent event = takeEvent("R1");
    assertEquals("MD", event.dataSetName());
    assertEquals(2, event.fields().size());

    // fields surface in wire order, with name/fieldId/index resolved from the metadata
    DataSetFieldValue b = event.fields().get(0);
    assertEquals("B", b.name());
    assertEquals(FIELD_ID_B, b.dataSetFieldId());
    assertEquals(1, b.index());
    assertEquals(Variant.ofInt32(22), b.value().getValue());

    DataSetFieldValue a = event.fields().get(1);
    assertEquals("A", a.name());
    assertEquals(FIELD_ID_A, a.dataSetFieldId());
    assertEquals(0, a.index());
    assertEquals(Variant.ofInt32(11), a.value().getValue());

    // the key frame completed reader startup
    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(reader));
  }

  @Test
  void unknownWireNameIsKeptWithNullFieldIdAndWireIndex() throws Exception {
    startService(DataSetReaderConfig.builder("R1").dataSetMetaData(metaDataAB()).build());

    injectAndFlush(
        networkMessage(
            PublisherId.string("pub"),
            List.of(new DecodedField(2, "X", new DataValue(Variant.ofInt32(7))))));

    assertEquals(1, eventCount("R1"));
    DataSetFieldValue field = takeEvent("R1").fields().get(0);
    assertEquals("X", field.name());
    assertEquals(NULL_FIELD_ID, field.dataSetFieldId());
    assertEquals(2, field.index());
    assertEquals(Variant.ofInt32(7), field.value().getValue());
  }

  @Test
  void nameKeyedFieldsWithoutMetadataSurfaceWireNamesAndIndices() throws Exception {
    // a reader without configured metadata still receives name-keyed fields: wire names are
    // kept (no synthetic Field_<n> names), with the null fieldId
    startService(DataSetReaderConfig.builder("R1").build());

    injectAndFlush(
        networkMessage(
            PublisherId.string("pub"),
            List.of(new DecodedField(0, "Flow", new DataValue(Variant.ofInt32(3))))));

    assertEquals(1, eventCount("R1"));
    DataSetFieldValue field = takeEvent("R1").fields().get(0);
    assertEquals("Flow", field.name());
    assertEquals(NULL_FIELD_ID, field.dataSetFieldId());
    assertEquals(0, field.index());
  }

  @Test
  void publisherIdFilterMatchesCrossTypeByCanonicalStringForm() throws Exception {
    startService(
        DataSetReaderConfig.builder("u16-5").publisherId(PublisherId.uint16(ushort(5))).build(),
        DataSetReaderConfig.builder("u16-6").publisherId(PublisherId.uint16(ushort(6))).build(),
        DataSetReaderConfig.builder("str-pub").publisherId(PublisherId.string("pub")).build(),
        DataSetReaderConfig.builder("wild").build());

    // the JSON mapping carries every PublisherId as a String: string "5" matches the uint16(5)
    // filter by canonical string form
    injectAndFlush(networkMessage(PublisherId.string("5"), List.of()));
    assertEquals(1, eventCount("u16-5"));
    assertEquals(0, eventCount("u16-6"));
    assertEquals(0, eventCount("str-pub"));
    assertEquals(1, eventCount("wild"));
    assertEquals(PublisherId.string("5"), takeEvent("u16-5").publisherId());
    clearEvents();

    // canonical numeric form has no leading zeros: string "05" matches nothing but the wildcard
    injectAndFlush(networkMessage(PublisherId.string("05"), 2, List.of()));
    assertEquals(0, eventCount("u16-5"));
    assertEquals(0, eventCount("u16-6"));
    assertEquals(1, eventCount("wild"));
    clearEvents();

    // exact-type matches keep working through the same path; uint16(5) canonicalizes to the
    // same "5" stream the first injection seeded, so the sequence number must increment
    injectAndFlush(networkMessage(PublisherId.uint16(ushort(5)), 2, List.of()));
    assertEquals(1, eventCount("u16-5"));
    assertEquals(0, eventCount("u16-6"));
    assertEquals(0, eventCount("str-pub"));
    assertEquals(1, eventCount("wild"));
  }
}
