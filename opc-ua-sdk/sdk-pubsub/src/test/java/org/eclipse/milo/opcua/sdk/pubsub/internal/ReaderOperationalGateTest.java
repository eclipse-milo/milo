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
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the DataSetReader PreOperational → Operational gate (OPC UA Part 14 §6.2.1 Table 2):
 * the reader changes to Operational only after the first <em>key frame or event</em>
 * DataSetMessage. Delta frames received before the first key frame are still delivered to
 * listeners, still counted in diagnostics, and still reset the receive timeout, but do not complete
 * startup; keep-alives never complete startup.
 *
 * <p>Exercised through {@link PubSubService} with a stub in-memory transport (no sockets, CI-safe;
 * a reader-only service opens no discovery channels, and the explicit loopback discoveryAddress is
 * defense in depth). Delta and event frames are hand-built bytes so the gate behavior is pinned
 * independent of the encoder (which emits deltas, but never events).
 */
class ReaderOperationalGateTest {

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;

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

  /** A transport that never touches the network: exposes datagram injection. */
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

  private @Nullable StubTransport transport;
  private final BlockingQueue<DataSetReceivedEvent> events = new LinkedBlockingQueue<>();

  private PubSubHandle startReaderService(DataSetReaderConfig reader) throws Exception {
    transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    // a reader-only service never opens discovery channels; the explicit loopback
    // discoveryAddress guards against any future fallback to the 224.0.2.14:4840 default
    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.73.1", 24843).networkInterface("127.0.0.1"))
            .readerGroup(ReaderGroupConfig.builder("RG").dataSetReader(reader).build())
            .build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service =
        PubSubService.create(
            PubSubConfig.builder().connection(connection).build(),
            PubSubBindings.builder().build(),
            serviceConfig);

    service.addDataSetListener(new DataSetReaderRef("conn", "RG", reader.getName()), events::add);

    service.startup().get(10, TimeUnit.SECONDS);

    return service.components().dataSetReader("conn", "RG", reader.getName()).orElseThrow();
  }

  private void injectAndFlush(byte[] frame) throws Exception {
    assertNotNull(transport);
    assertNotNull(transportExecutor);
    transport.inject(frame);
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);
  }

  private static void awaitTrue(String description, BooleanSupplier condition)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for: " + description);
  }

  private long readerCounter(
      String name, ToLongFunction<PubSubDiagnostics.ComponentDiagnostics> counter) {
    var diagnostics = service.diagnostics().component("conn/RG/" + name).orElse(null);
    return diagnostics == null ? 0L : counter.applyAsLong(diagnostics);
  }

  private static byte[] bytes(int... values) {
    byte[] result = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      result[i] = (byte) values[i];
    }
    return result;
  }

  /** Encode one NetworkMessage with the default UADP-Dynamic masks (PublisherId+PayloadHeader). */
  private static byte[] encodeKeyFrameMessage(int dataSetWriterId, int value) throws UaException {
    return encode(
        dataSetWriterId,
        UadpDataSetMessageContentMask.of(),
        0,
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  /** Encode a key frame whose DataSetMessage SequenceNumber is on the wire. */
  private static byte[] encodeSequencedKeyFrameMessage(
      int dataSetWriterId, int sequenceNumber, int value) throws UaException {

    return encode(
        dataSetWriterId,
        UadpDataSetMessageContentMask.of(UadpDataSetMessageContentMask.Field.SequenceNumber),
        sequenceNumber,
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  private static byte[] encodeKeepAliveMessage(int dataSetWriterId) throws UaException {
    return encode(dataSetWriterId, UadpDataSetMessageContentMask.of(), 0, true, List.of());
  }

  private static byte[] encode(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      int sequenceNumber,
      boolean keepAlive,
      List<DataValue> values)
      throws UaException {

    WriterGroupConfig group =
        WriterGroupConfig.builder("EncodeWG").writerGroupId(ushort(1)).build();

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("EncodeW")
            .dataSet(new PublishedDataSetRef("EncodePDS"))
            .dataSetWriterId(ushort(dataSetWriterId))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(dataSetMessageContentMask)
                    .build())
            .build();

    var draft =
        DataSetMessageDraft.of(
            writer,
            uint(sequenceNumber),
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            keepAlive,
            values);

    List<EncodedNetworkMessage> encoded =
        new UadpMessageMapping()
            .encode(
                new EncodeContext(
                    new DefaultEncodingContext(),
                    PublisherId.uint16(ushort(1)),
                    group,
                    uint(0),
                    ushort(1),
                    ushort(0),
                    null,
                    List.of(draft),
                    null));

    ByteBuf data = encoded.get(0).data();
    try {
      byte[] result = new byte[data.readableBytes()];
      data.readBytes(result);
      return result;
    } finally {
      data.release();
    }
  }

  /**
   * A Data Delta Frame with one Variant Int32 field, hand-built per Part 14 §7.2.4.5.6 Table 164
   * (built by hand so these gate tests stay independent of the encoder's delta emission). No
   * optional NetworkMessage headers; a payload without a PayloadHeader is one DataSetMessage with
   * no DataSetWriterId on the wire.
   */
  private static byte[] deltaFrameWithInt32Field(int fieldIndex, int value) {
    return bytes(
        0x01, // byte 0: version 1, all optional NetworkMessage headers off
        0x81, // DataSetFlags1: valid 0x01 | Variant encoding | DataSetFlags2 0x80
        0x01, // DataSetFlags2: type 0001 = Data Delta Frame
        0x01,
        0x00, // FieldCount = 1
        fieldIndex & 0xFF,
        (fieldIndex >>> 8) & 0xFF, // FieldIndex
        0x06, // Variant type id 6 = Int32
        value & 0xFF,
        (value >>> 8) & 0xFF,
        (value >>> 16) & 0xFF,
        (value >>> 24) & 0xFF);
  }

  /**
   * Like {@link #deltaFrameWithInt32Field(int, int)} but with the DataSetMessageSequenceNumber on
   * the wire (DataSetFlags1 bit 3, §7.2.4.5.4 Table 162). Headerless like the other hand-built
   * frames, so it shares the (null, null) stream identity with {@link
   * #keyFrameWithSequenceNumber(int, int)}.
   */
  private static byte[] deltaFrameWithSequenceNumber(int sequenceNumber, int value) {
    return bytes(
        0x01, // byte 0: version 1, all optional NetworkMessage headers off
        0x89, // DataSetFlags1: valid 0x01 | Variant encoding | SequenceNumber 0x08 | Flags2 0x80
        0x01, // DataSetFlags2: type 0001 = Data Delta Frame
        sequenceNumber & 0xFF,
        (sequenceNumber >>> 8) & 0xFF, // DataSetMessageSequenceNumber (UInt16 LE)
        0x01,
        0x00, // FieldCount = 1
        0x01,
        0x00, // FieldIndex = 1
        0x06, // Variant type id 6 = Int32
        value & 0xFF,
        (value >>> 8) & 0xFF,
        (value >>> 16) & 0xFF,
        (value >>> 24) & 0xFF);
  }

  /**
   * A key frame with the DataSetMessageSequenceNumber on the wire (no DataSetFlags2: message type
   * defaults to key frame) and one Variant Int32 field, hand-built per §7.2.4.5.4 Table 162.
   */
  private static byte[] keyFrameWithSequenceNumber(int sequenceNumber, int value) {
    return bytes(
        0x01, // byte 0: version 1, all optional NetworkMessage headers off
        0x09, // DataSetFlags1: valid 0x01 | Variant encoding | SequenceNumber 0x08
        sequenceNumber & 0xFF,
        (sequenceNumber >>> 8) & 0xFF, // DataSetMessageSequenceNumber (UInt16 LE)
        0x01,
        0x00, // FieldCount = 1
        0x06, // Variant type id 6 = Int32
        value & 0xFF,
        (value >>> 8) & 0xFF,
        (value >>> 16) & 0xFF,
        (value >>> 24) & 0xFF);
  }

  /** An Event DataSetMessage with one Variant Int32 field (§7.2.4.5.7 Table 165), hand-built. */
  private static byte[] eventFrameWithInt32Field(int value) {
    return bytes(
        0x01, // byte 0: version 1
        0x81, // DataSetFlags1: valid | Variant encoding | DataSetFlags2 present
        0x02, // DataSetFlags2: type 0010 = Event
        0x01,
        0x00, // FieldCount = 1
        0x06, // Variant type id 6 = Int32
        value & 0xFF,
        (value >>> 8) & 0xFF,
        (value >>> 16) & 0xFF,
        (value >>> 24) & 0xFF);
  }

  /**
   * A NetworkMessage with PayloadHeader whose first DataSetMessage (writer id 1) is a header-only
   * key frame, i.e. a heartbeat (§7.2.4.5.5), and whose second (writer id 2) carries one field.
   */
  private static byte[] heartbeatMessage() {
    return bytes(
        0x41, // byte 0: version 1 | PayloadHeader 0x40
        0x02, // PayloadHeader: Count = 2
        0x01, 0x00, // DataSetWriterIds[0] = 1
        0x02, 0x00, // DataSetWriterIds[1] = 2
        0x01, 0x00, // Sizes[0] = 1: header-only DataSetMessage (heartbeat)
        0x05, 0x00, // Sizes[1] = 5
        0x01, // DSM 1: DataSetFlags1 valid; no body at all
        0x01, // DSM 2: DataSetFlags1 valid
        0x01, 0x00, // DSM 2: FieldCount = 1
        0x01, 0x01); // DSM 2: Variant Boolean = true
  }

  // endregion

  @Test
  void keyFrameCompletesReaderStartup() throws Exception {
    PubSubHandle reader = startReaderService(DataSetReaderConfig.builder("R1").build());
    assertEquals(PubSubState.PreOperational, service.state(reader));

    injectAndFlush(encodeKeyFrameMessage(10, 7));

    assertEquals(PubSubState.Operational, service.state(reader));

    DataSetReceivedEvent event = events.poll();
    assertNotNull(event);
    assertEquals("Field_0", event.fields().get(0).name());
    assertEquals(Variant.ofInt32(7), event.fields().get(0).value().getValue());
  }

  @Test
  void zeroFieldKeyFrameHeartbeatCompletesReaderStartup() throws Exception {
    // the reader's filter selects only the header-only key frame in the two-message payload
    PubSubHandle reader =
        startReaderService(DataSetReaderConfig.builder("R1").dataSetWriterId(ushort(1)).build());
    assertEquals(PubSubState.PreOperational, service.state(reader));

    injectAndFlush(heartbeatMessage());

    assertEquals(PubSubState.Operational, service.state(reader));

    DataSetReceivedEvent event = events.poll();
    assertNotNull(event);
    assertEquals(ushort(1), event.dataSetWriterId());
    assertTrue(event.fields().isEmpty());
  }

  @Test
  void eventMessageCompletesReaderStartup() throws Exception {
    PubSubHandle reader = startReaderService(DataSetReaderConfig.builder("R1").build());
    assertEquals(PubSubState.PreOperational, service.state(reader));

    injectAndFlush(eventFrameWithInt32Field(11));

    assertEquals(PubSubState.Operational, service.state(reader));

    DataSetReceivedEvent event = events.poll();
    assertNotNull(event);
    assertEquals("Field_0", event.fields().get(0).name());
    assertEquals(Variant.ofInt32(11), event.fields().get(0).value().getValue());
  }

  /**
   * The DataSetMessage kind decoded from the wire (§7.2.4.5.4 Table 162 DataSetFlags2 type bits) is
   * surfaced on the delivered {@link DataSetReceivedEvent#kind()}: a key frame delivers {@link
   * DataSetMessageKind#KEY_FRAME}, an event DataSetMessage delivers {@link
   * DataSetMessageKind#EVENT}. Both are driven through the real decode-and-dispatch path (the codec
   * round-trip tests pin the byte-level kind decode; this pins the dispatcher wiring {@code
   * message.kind()} into the event).
   */
  @Test
  void deliveredEventCarriesDecodedDataSetMessageKind() throws Exception {
    startReaderService(DataSetReaderConfig.builder("R1").build());

    // a key frame (default masks: no sequence number, so it bypasses the §7.2.3 window)
    injectAndFlush(encodeKeyFrameMessage(10, 7));
    DataSetReceivedEvent keyFrame = events.poll();
    assertNotNull(keyFrame);
    assertEquals(DataSetMessageKind.KEY_FRAME, keyFrame.kind());

    // an event DataSetMessage (DataSetFlags2 type bits = 0x02) on the same reader
    injectAndFlush(eventFrameWithInt32Field(11));
    DataSetReceivedEvent event = events.poll();
    assertNotNull(event);
    assertEquals(DataSetMessageKind.EVENT, event.kind());
  }

  @Test
  void keepAliveDoesNotCompleteReaderStartup() throws Exception {
    PubSubHandle reader = startReaderService(DataSetReaderConfig.builder("R1").build());

    injectAndFlush(encodeKeepAliveMessage(10));

    assertEquals(PubSubState.PreOperational, service.state(reader));
    assertEquals(
        1, readerCounter("R1", PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
  }

  /**
   * Part 14 §6.2.1 Table 2 (SHALL): a delta frame received before the first key frame does not
   * complete startup — but it is DELIBERATELY still delivered to listeners, still counted in
   * diagnostics, and the reader does not enter Error from its receive timeout while deltas keep
   * flowing. §7.2.4.3 leaves the pre-baseline delivery policy to the application: listeners receive
   * honest partial state while the PreOperational state signals "no full baseline seen yet", and
   * the publisher-side keyFrameCount cadence bounds the wait for a key frame.
   */
  @Test
  void preKeyDeltaFramesAreDeliveredAndCountedButDoNotCompleteStartup() throws Exception {
    PubSubHandle reader =
        startReaderService(
            DataSetReaderConfig.builder("R1")
                .messageReceiveTimeout(Duration.ofMillis(250))
                .build());
    assertEquals(PubSubState.PreOperational, service.state(reader));

    // inject deltas across a window longer than 2x the receive timeout
    for (int i = 0; i < 5; i++) {
      injectAndFlush(deltaFrameWithInt32Field(1, 100 + i));
      assertEquals(PubSubState.PreOperational, service.state(reader), "after delta " + i);
      Thread.sleep(120);
    }

    // still PreOperational: deltas never completed startup, and no receive-timeout Error
    assertEquals(PubSubState.PreOperational, service.state(reader));

    // every delta was delivered, with positional names from the explicit wire indices
    assertEquals(5, events.size());
    for (int i = 0; i < 5; i++) {
      DataSetReceivedEvent event = events.poll();
      assertNotNull(event);
      assertEquals(1, event.fields().size());
      assertEquals("Field_1", event.fields().get(0).name());
      assertEquals(1, event.fields().get(0).index());
      assertEquals(Variant.ofInt32(100 + i), event.fields().get(0).value().getValue());
    }

    // ... and counted in diagnostics, without decode errors
    assertEquals(
        5, readerCounter("R1", PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
    assertEquals(0, readerCounter("R1", PubSubDiagnostics.ComponentDiagnostics::decodeErrors));

    // the first key frame completes startup
    injectAndFlush(encodeKeyFrameMessage(10, 1));
    assertEquals(PubSubState.Operational, service.state(reader));
  }

  /**
   * Once Operational, delta frames keep resetting the receive timeout; when they stop the watchdog
   * fires, and the next delta recovers the reader from Error.
   */
  @Test
  void deltaFramesResetReceiveTimeoutAndRecoverErrorWhileOperational() throws Exception {
    PubSubHandle reader =
        startReaderService(
            DataSetReaderConfig.builder("R1")
                .messageReceiveTimeout(Duration.ofMillis(400))
                .build());

    injectAndFlush(encodeKeyFrameMessage(10, 1));
    assertEquals(PubSubState.Operational, service.state(reader));

    // deltas at a cadence well below the timeout keep the reader Operational for > 2x timeout
    for (int i = 0; i < 12; i++) {
      Thread.sleep(80);
      injectAndFlush(deltaFrameWithInt32Field(0, i));
      assertEquals(PubSubState.Operational, service.state(reader), "after delta " + i);
    }

    // no more messages: the watchdog expires and moves the reader to Error
    awaitTrue(
        "reader to enter Error after messageReceiveTimeout",
        () -> service.state(reader) == PubSubState.Error);
    assertEquals(
        new StatusCode(StatusCodes.Bad_Timeout),
        service.diagnostics().snapshot().get("conn/RG/R1").lastError());

    // a reader that has already been Operational recovers from Error on any accepted message,
    // including a delta frame
    injectAndFlush(deltaFrameWithInt32Field(0, 99));
    awaitTrue(
        "reader to recover to Operational on the next delta",
        () -> service.state(reader) == PubSubState.Operational);
  }

  /**
   * Part 14 §6.2.9.6: "A DataSetMessage is considered new if the sequence number increments" — a
   * stream of pure duplicates does not reset the receive timeout, so the watchdog still moves the
   * reader to Error; dropped duplicates do not recover it either, but the next new message does.
   */
  @Test
  void duplicateDataMessagesDoNotResetTheReceiveTimeout() throws Exception {
    PubSubHandle reader =
        startReaderService(
            DataSetReaderConfig.builder("R1").messageReceiveTimeout(Duration.ofSeconds(1)).build());

    byte[] seq5 = encodeSequencedKeyFrameMessage(10, 5, 1);

    injectAndFlush(seq5);
    assertEquals(PubSubState.Operational, service.state(reader));
    assertEquals(1, events.size());

    // keep injecting the SAME message at a cadence well below the timeout: the drops do not
    // reset the receive timeout, so the watchdog expires despite the steady traffic
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (service.state(reader) != PubSubState.Error && System.nanoTime() < deadline) {
      injectAndFlush(seq5);
      Thread.sleep(60);
    }
    assertEquals(PubSubState.Error, service.state(reader));

    // a dropped duplicate does not recover the reader from Error — injected immediately upon
    // observing Error: the watchdog fired at 1x the timeout while the §7.2.3 record-discard
    // fires only at 2x, so a full timeout of real-time headroom remains before a discarded
    // record would make this duplicate re-seed the stream and wrongly recover the reader
    injectAndFlush(seq5);
    assertEquals(PubSubState.Error, service.state(reader));

    assertEquals(
        new StatusCode(StatusCodes.Bad_Timeout),
        service.diagnostics().snapshot().get("conn/RG/R1").lastError());

    // none of the duplicates was delivered, all were counted
    assertEquals(1, events.size());
    assertTrue(
        readerCounter("R1", PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages) > 0);

    // the next new sequence number does recover the reader
    injectAndFlush(encodeSequencedKeyFrameMessage(10, 6, 2));
    awaitTrue(
        "reader to recover to Operational on the next new message",
        () -> service.state(reader) == PubSubState.Operational);
  }

  /**
   * A duplicate key frame dropped by the §7.2.3 window does not complete startup: the window is
   * seeded by a pre-key delta frame (delivered, reader still PreOperational per §6.2.1 Table 2),
   * then a key frame re-using the delta's sequence number is dropped without a state change.
   */
  @Test
  void droppedDuplicateKeyFrameDoesNotCompleteStartup() throws Exception {
    PubSubHandle reader = startReaderService(DataSetReaderConfig.builder("R1").build());
    assertEquals(PubSubState.PreOperational, service.state(reader));

    // a delta with sequence number 5 seeds the (null, null) stream window; delivered, no startup
    injectAndFlush(deltaFrameWithSequenceNumber(5, 100));
    assertEquals(PubSubState.PreOperational, service.state(reader));
    assertEquals(1, events.size());

    // a key frame duplicating sequence number 5 is dropped: no delivery, no startup completion
    injectAndFlush(keyFrameWithSequenceNumber(5, 7));
    assertEquals(PubSubState.PreOperational, service.state(reader));
    assertEquals(1, events.size());
    assertEquals(
        1, readerCounter("R1", PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages));

    // the next new key frame completes startup
    injectAndFlush(keyFrameWithSequenceNumber(6, 8));
    assertEquals(PubSubState.Operational, service.state(reader));
    assertEquals(2, events.size());
  }
}
