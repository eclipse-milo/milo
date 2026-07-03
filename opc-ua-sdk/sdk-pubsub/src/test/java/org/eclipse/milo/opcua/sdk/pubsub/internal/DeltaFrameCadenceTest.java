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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
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
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Engine-level key-frame cadence and delta suppression behavior (Part 14 §6.2.4.3): a writer with
 * {@code keyFrameCount} N emits a key frame every N-th publishing interval and delta frames of the
 * changed fields in between; a cycle where every field changed emits a key frame instead
 * (§7.2.4.5.6) and resets the cadence; a no-change cycle sends nothing and the group keep-alive
 * (§6.2.6.3) takes over, carrying the next expected sequence number (§7.2.4.5.8); a group
 * disable/enable forces a fresh key frame; a NetworkMessage that is not transmitted (send failure,
 * oversize skip) invalidates its writers' delta baselines, forcing a key frame on the next
 * transmitted message (§5.3.3: the baseline is the last TRANSMITTED values); and a UADP writer with
 * a non-zero ConfiguredSize never emits deltas — fixed-size layouts are key-frame-only (Annex
 * A.2.1.7), and an overflowing DataSetMessage would be valid-bit cleared yet still sent
 * (§6.3.1.3.3), bypassing the not-transmitted invalidation — so one that slips past validation
 * degrades to every-cycle key frames. Keep-alive emission tracks actual transmission, not drafting:
 * a writer whose data is dropped at the size gate every cycle still emits (budget-fitting,
 * header-only) keep-alives once due, and a writer in {@code PubSubState.Error} emits none at all —
 * a keep-alive asserts the writer is still active (§6.2.6.3), and would otherwise mask an
 * activation failure from subscribers.
 *
 * <p>Following the PubSubStateMachineTest publish-cycle pattern: real publish scheduling with short
 * intervals and a stub transport whose sent queue is decoded and classified — assertions are on the
 * frame sequence, never on timing. Connections with writer groups open real UDP discovery sockets,
 * so every test pins the discoveryAddress to a unique loopback multicast group instead of the
 * 224.0.2.14:4840 default.
 */
class DeltaFrameCadenceTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 1L);
  private static final UUID CONSTANT_FIELD_ID = new UUID(0L, 2L);
  private static final UUID BLOB_FIELD_ID = new UUID(0L, 3L);

  private @Nullable PubSubService service;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  /**
   * A transport that never touches the network: captures publisher sends, and can inject send
   * failures in either of the shapes the engine handles (an exceptionally completed future, or a
   * synchronous throw).
   */
  private static final class StubTransport implements TransportProvider {

    enum SendFailureMode {
      /** The send future completes exceptionally (the asynchronous transport failure shape). */
      FAIL_FUTURE,
      /** The send throws synchronously. */
      THROW
    }

    final BlockingQueue<byte[]> sent = new LinkedBlockingQueue<>();
    final AtomicReference<SendFailureMode> sendFailureMode = new AtomicReference<>();
    final AtomicInteger failedSends = new AtomicInteger();

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
          SendFailureMode failureMode = sendFailureMode.get();
          if (failureMode != null) {
            // a conforming channel owns (and releases) the buffer on every path, including a
            // synchronous throw (PublisherChannel#send contract)
            message.release();
            failedSends.incrementAndGet();
            if (failureMode == SendFailureMode.THROW) {
              throw new RuntimeException("injected synchronous send failure");
            }
            return CompletableFuture.failedFuture(new RuntimeException("injected send failure"));
          }
          try {
            byte[] bytes = new byte[message.readableBytes()];
            message.readBytes(bytes);
            sent.add(bytes);
          } finally {
            message.release();
          }
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
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  // region fixtures

  private static PublishedDataSetConfig publishedDataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("counter")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(COUNTER_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("constant")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(CONSTANT_FIELD_ID)
                .build())
        .build();
  }

  /** Field 0 changes on every read; field 1 only when {@code constant} is mutated. */
  private static PublishedDataSetSource countingSource(
      AtomicInteger counter, AtomicReference<Integer> constant) {

    return context ->
        DataSetSnapshot.builder(context)
            .field("counter", new DataValue(Variant.ofInt32(counter.incrementAndGet())))
            .field("constant", new DataValue(Variant.ofInt32(constant.get())))
            .build();
  }

  /** Three fields, one of variable wire size: counter (Int32), blob (ByteString), constant. */
  private static PublishedDataSetConfig blobPublishedDataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("counter")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(COUNTER_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("blob")
                .dataType(NodeIds.ByteString)
                .dataSetFieldId(BLOB_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("constant")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(CONSTANT_FIELD_ID)
                .build())
        .build();
  }

  /** Field 0 changes on every read; field 1 only when {@code blob} is mutated; field 2 never. */
  private static PublishedDataSetSource blobSource(
      AtomicInteger counter, AtomicReference<ByteString> blob) {

    return context ->
        DataSetSnapshot.builder(context)
            .field("counter", new DataValue(Variant.ofInt32(counter.incrementAndGet())))
            .field("blob", new DataValue(Variant.ofByteString(blob.get())))
            .field("constant", new DataValue(Variant.ofInt32(100)))
            .build();
  }

  /** The {@link #startPublisher} writer group shape, for tests that bring their own dataset. */
  private static WriterGroupConfig blobWriterGroup(
      int keyFrameCount, UInteger maxNetworkMessageSize) {

    return WriterGroupConfig.builder("WG")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(25))
        .maxNetworkMessageSize(maxNetworkMessageSize)
        .messageSettings(
            UadpWriterGroupSettings.builder()
                .networkMessageContentMask(
                    // 0x41: PublisherId | PayloadHeader, so frames carry the writer id
                    new UadpNetworkMessageContentMask(uint(0x41)))
                .build())
        .dataSetWriter(
            DataSetWriterConfig.builder("W1")
                .dataSet(new PublishedDataSetRef("PDS"))
                .dataSetWriterId(ushort(1))
                .keyFrameCount(uint(keyFrameCount))
                .settings(
                    UadpDataSetWriterSettings.builder()
                        .dataSetMessageContentMask(
                            // 0x20: SequenceNumber
                            new UadpDataSetMessageContentMask(uint(0x20)))
                        .build())
                .build())
        .build();
  }

  private StubTransport startPublisher(
      int keyFrameCount,
      @Nullable Duration keepAliveTime,
      PublishedDataSetSource source,
      UdpDatagramAddress discoveryAddress)
      throws Exception {

    return startPublisher(keyFrameCount, keepAliveTime, source, discoveryAddress, uint(0));
  }

  private StubTransport startPublisher(
      int keyFrameCount,
      @Nullable Duration keepAliveTime,
      PublishedDataSetSource source,
      UdpDatagramAddress discoveryAddress,
      UInteger maxNetworkMessageSize)
      throws Exception {

    return startPublisher(
        keyFrameCount,
        keepAliveTime,
        source,
        discoveryAddress,
        maxNetworkMessageSize,
        ushort(0),
        true);
  }

  private StubTransport startPublisher(
      int keyFrameCount,
      @Nullable Duration keepAliveTime,
      PublishedDataSetSource source,
      UdpDatagramAddress discoveryAddress,
      UInteger maxNetworkMessageSize,
      UShort configuredSize,
      boolean writerEnabled)
      throws Exception {

    WriterGroupConfig.Builder group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(25))
            .maxNetworkMessageSize(maxNetworkMessageSize)
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(
                        // 0x41: PublisherId | PayloadHeader, so frames carry the writer id
                        new UadpNetworkMessageContentMask(uint(0x41)))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .enabled(writerEnabled)
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .keyFrameCount(uint(keyFrameCount))
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(
                                // 0x20: SequenceNumber
                                new UadpDataSetMessageContentMask(uint(0x20)))
                            .configuredSize(configuredSize)
                            .build())
                    .build());
    if (keepAliveTime != null) {
      group.keepAliveTime(keepAliveTime);
    }

    return startPublisher(publishedDataSet(), group.build(), source, discoveryAddress);
  }

  private StubTransport startPublisher(
      PublishedDataSetConfig dataSet,
      WriterGroupConfig group,
      PublishedDataSetSource source,
      UdpDatagramAddress discoveryAddress)
      throws Exception {

    var transport = new StubTransport();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(99)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            // a connection with writer groups opens real UDP discovery sockets: keep them on a
            // unique loopback multicast group, never the 224.0.2.14:4840 default
            .discoveryAddress(discoveryAddress)
            .writerGroup(group)
            .build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();

    PubSubBindings bindings =
        PubSubBindings.builder().source(new PublishedDataSetRef("PDS"), source).build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(transport).build();

    service = PubSubService.create(config, bindings, serviceConfig);
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return transport;
  }

  private DecodedDataSetMessage nextFrame(StubTransport transport) throws Exception {
    byte[] frame = transport.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(frame, "no frame published within the deadline");

    List<DecodedDataSetMessage> messages = decode(frame).messages();
    assertEquals(1, messages.size());
    return messages.get(0);
  }

  private static org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage decode(byte[] frame)
      throws UaException {

    ByteBuf buffer = Unpooled.wrappedBuffer(frame);
    try {
      return new UadpMessageMapping()
          .decode(DecodeContext.of(new DefaultEncodingContext()), buffer);
    } finally {
      buffer.release();
    }
  }

  private static int counterValueOf(DecodedDataSetMessage message) {
    return message.fields().stream()
        .filter(f -> f.index() == 0)
        .findFirst()
        .map(f -> (Integer) f.value().value().value())
        .orElseThrow();
  }

  // endregion

  /**
   * keyFrameCount = 3 with one field changing every cycle: the sent sequence classifies as K, D, D,
   * K, D, D, ...; deltas carry ONLY the changed field with its explicit index, key frames carry all
   * fields; and key, delta, and key-again frames consume the sequence number uniformly (§7.2.3
   * "incremented by exactly one for each message").
   */
  @Test
  void keyFrameCadenceFollowsKeyFrameCount() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    StubTransport transport =
        startPublisher(
            3,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.21", 24751).networkInterface("127.0.0.1"));

    int previousCounterValue = 0;
    for (int i = 0; i < 9; i++) {
      DecodedDataSetMessage message = nextFrame(transport);

      assertEquals(i, message.sequenceNumber().intValue(), "sequence number of frame " + i);

      if (i % 3 == 0) {
        assertEquals(DataSetMessageKind.KEY_FRAME, message.kind(), "frame " + i);
        assertEquals(2, message.fields().size(), "key frame " + i + " carries all fields");
        assertEquals(100, message.fields().get(1).value().value().value());
      } else {
        assertEquals(DataSetMessageKind.DELTA_FRAME, message.kind(), "frame " + i);
        assertEquals(1, message.fields().size(), "delta frame " + i + " carries one changed field");
        assertEquals(0, message.fields().get(0).index());
      }

      int counterValue = counterValueOf(message);
      assertTrue(counterValue > previousCounterValue, "counter value advances every frame");
      previousCounterValue = counterValue;
    }
  }

  /**
   * A cycle where every field changed emits a key frame instead of the (strictly larger) delta
   * (§7.2.4.5.6) and resets the cadence: no delta ever carries the rarely-changing field, the first
   * frame carrying its new value is a key frame, and the next key frame follows a full cadence
   * later.
   */
  @Test
  void allFieldsChangedEmitsKeyFrameAndResetsCadence() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    StubTransport transport =
        startPublisher(
            4,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.22", 24752).networkInterface("127.0.0.1"));

    var frames = new ArrayList<DecodedDataSetMessage>();
    for (int i = 0; i < 6; i++) {
      frames.add(nextFrame(transport));
    }

    constant.set(200);

    // collect until the new constant value has been on the wire for a full cadence
    int forcedKeyIndex = -1;
    while (forcedKeyIndex < 0 || frames.size() < forcedKeyIndex + 6) {
      frames.add(nextFrame(transport));
      if (forcedKeyIndex < 0) {
        DecodedDataSetMessage last = frames.get(frames.size() - 1);
        boolean carriesNewConstant =
            last.fields().stream()
                .anyMatch(
                    f -> f.index() == 1 && Integer.valueOf(200).equals(f.value().value().value()));
        if (carriesNewConstant) {
          forcedKeyIndex = frames.size() - 1;
        }
      }
    }

    // the first frame carrying the changed second field is a key frame (all fields changed on
    // that cycle: the always-changing counter plus the constant)
    assertEquals(DataSetMessageKind.KEY_FRAME, frames.get(forcedKeyIndex).kind());

    // no delta frame ever carries the second field: a cycle that changes it changes all fields
    for (DecodedDataSetMessage frame : frames) {
      if (frame.kind() == DataSetMessageKind.DELTA_FRAME) {
        assertEquals(1, frame.fields().size());
        assertEquals(0, frame.fields().get(0).index());
      }
    }

    // the forced key frame reset the cadence: the next 3 frames are deltas, then a key frame
    for (int i = 1; i <= 3; i++) {
      assertEquals(
          DataSetMessageKind.DELTA_FRAME,
          frames.get(forcedKeyIndex + i).kind(),
          "frame " + i + " after the forced key frame");
    }
    assertEquals(DataSetMessageKind.KEY_FRAME, frames.get(forcedKeyIndex + 4).kind());

    // sequence numbers increment by exactly one across every emitted frame (§7.2.3)
    for (int i = 1; i < frames.size(); i++) {
      assertEquals(
          (frames.get(i - 1).sequenceNumber().intValue() + 1) & 0xFFFF,
          frames.get(i).sequenceNumber().intValue());
    }
  }

  /**
   * No-change delta cycles send nothing (§6.2.4.3: "If no changes exist, the delta frame
   * DataSetMessage shall not be sent"): after the initial key frame an all-constant source produces
   * only keep-alives (§6.2.6.3), each carrying the next expected sequence number without consuming
   * it (§7.2.4.5.8) — the suppressed cycles consumed none.
   */
  @Test
  void noChangeCyclesSendNothingAndKeepAliveCarriesNextExpected() throws Exception {
    var constant = new AtomicReference<>(100);
    PublishedDataSetSource constantSource =
        context ->
            DataSetSnapshot.builder(context)
                .field("counter", new DataValue(Variant.ofInt32(7)))
                .field("constant", new DataValue(Variant.ofInt32(constant.get())))
                .build();

    StubTransport transport =
        startPublisher(
            100,
            Duration.ofMillis(100),
            constantSource,
            UdpDatagramAddress.multicast("239.255.74.23", 24753).networkInterface("127.0.0.1"));

    DecodedDataSetMessage first = nextFrame(transport);
    assertEquals(DataSetMessageKind.KEY_FRAME, first.kind());
    int keyFrameSequence = first.sequenceNumber().intValue();

    // nothing but keep-alives follows, all carrying the next expected sequence number
    for (int i = 0; i < 3; i++) {
      DecodedDataSetMessage frame = nextFrame(transport);
      assertEquals(DataSetMessageKind.KEEP_ALIVE, frame.kind(), "frame " + i + " after key frame");
      assertEquals((keyFrameSequence + 1) & 0xFFFF, frame.sequenceNumber().intValue());
    }
  }

  /** The first frame after a writer group disable/enable is a key frame (baseline reset). */
  @Test
  void firstFrameAfterGroupDisableEnableIsKeyFrame() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    StubTransport transport =
        startPublisher(
            50,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.24", 24754).networkInterface("127.0.0.1"));

    assertEquals(DataSetMessageKind.KEY_FRAME, nextFrame(transport).kind());
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(transport).kind());

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    service.disable(group);
    assertEquals(PubSubState.Disabled, service.state(group));

    // drain frames already in flight: once 150 ms pass without a frame the publisher is idle
    while (transport.sent.poll(150, TimeUnit.MILLISECONDS) != null) {
      // keep draining
    }

    service.enable(group);

    // with keyFrameCount = 50 the cadence alone could not force a key frame here: this is the
    // group re-activation reset
    DecodedDataSetMessage afterEnable = nextFrame(transport);
    assertEquals(DataSetMessageKind.KEY_FRAME, afterEnable.kind());
    assertEquals(2, afterEnable.fields().size());

    // and the baseline was rebuilt: the following frame is a delta again
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(transport).kind());
  }

  // region untransmitted-baseline protection: send failures, oversize drops, ConfiguredSize

  static Stream<Arguments> sendFailureModes() {
    return Stream.of(
        Arguments.of("future completes exceptionally", StubTransport.SendFailureMode.FAIL_FUTURE),
        Arguments.of("send throws synchronously", StubTransport.SendFailureMode.THROW));
  }

  /**
   * A NetworkMessage whose send fails was never transmitted, so the writer's delta baseline is
   * invalidated and the first frame transmitted after the failure window is a key frame (§5.3.3:
   * the baseline is the last TRANSMITTED values). Without the invalidation the first post-failure
   * delta would diff against values no subscriber received, leaving any field that changed only
   * during the failure window silently stale until the next cadence key frame.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("sendFailureModes")
  void sendFailureForcesKeyFrameOnNextTransmittedMessage(
      String label, StubTransport.SendFailureMode failureMode) throws Exception {

    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    // keyFrameCount = 100: within this test the cadence alone never forces a key frame
    StubTransport transport =
        startPublisher(
            100,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.25", 24755).networkInterface("127.0.0.1"));

    assertEquals(DataSetMessageKind.KEY_FRAME, nextFrame(transport).kind());
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(transport).kind());

    transport.sendFailureMode.set(failureMode);

    // wait for at least two failed sends: the first dropped message was a delta of the
    // always-changing counter field (drafted against a then-valid baseline), so its values were
    // lost in flight and the baseline no longer matches anything a subscriber received
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (transport.failedSends.get() < 2 && System.nanoTime() < deadline) {
      Thread.sleep(10);
    }
    assertTrue(transport.failedSends.get() >= 2, "expected at least two failed sends");

    // anything still queued was captured before the failure window began
    transport.sent.clear();

    transport.sendFailureMode.set(null);

    // the first frame transmitted after the failure window is a key frame carrying all fields
    DecodedDataSetMessage healed = nextFrame(transport);
    assertEquals(DataSetMessageKind.KEY_FRAME, healed.kind());
    assertEquals(2, healed.fields().size());

    // and delta emission resumes against the freshly transmitted baseline
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(transport).kind());
  }

  /**
   * The worst case of the not-transmitted heal: a writer whose KEY frame exceeds the group's
   * maxNetworkMessageSize — while its one-field deltas would fit — transmits NOTHING. Every cycle
   * re-forces a key frame that is dropped again with {@code Bad_EncodingLimitsExceeded}
   * diagnostics, because the baseline is never transmitted. The alternative would be an
   * alive-looking delta stream diffed against a baseline no subscriber ever received, which could
   * also never complete a fresh reader's startup (§6.2.1 Table 2: Operational requires a first key
   * frame or event).
   */
  @Test
  void untransmittableKeyFrameProducesNoDeltas() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    // measure the (constant-sized: two Int32 Variants, fixed-width headers) encoded key frame
    // with an unrestricted publisher
    StubTransport unrestricted =
        startPublisher(
            3,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.26", 24756).networkInterface("127.0.0.1"));

    byte[] keyFrame = unrestricted.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(keyFrame, "no frame published within the deadline");
    assertEquals(DataSetMessageKind.KEY_FRAME, decode(keyFrame).messages().get(0).kind());

    service.close();
    service = null;

    // restart with a budget one byte below the key frame size: a one-field delta is 3 bytes
    // smaller (one 2-byte FieldIndex + one 5-byte Int32 Variant instead of two 5-byte Int32
    // Variants, Table 163/164) and would fit
    StubTransport restricted =
        startPublisher(
            3,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.27", 24757).networkInterface("127.0.0.1"),
            uint(keyFrame.length - 1));

    var drops = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    service.addDiagnosticsListener(
        event -> {
          if (event.statusCode().value() == StatusCodes.Bad_EncodingLimitsExceeded) {
            drops.add(event);
          }
        });

    // at least three publish cycles each dropped an oversize NetworkMessage at the group path...
    for (int i = 0; i < 3; i++) {
      PubSubDiagnosticsEvent drop = drops.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      assertNotNull(drop, "no oversize-drop diagnostics event " + i + " within the deadline");
      assertEquals("conn/WG", drop.path());
    }

    // ...and no delta sneaked out in between: every cycle re-forced an (oversize, dropped) key
    // frame, so nothing reached the wire
    assertTrue(restricted.sent.isEmpty(), "expected nothing to be transmitted");
  }

  /**
   * An oversize-skipped DELTA never becomes the baseline: with a budget that admits the key frame
   * and one-field counter deltas but not a frame carrying a grown blob field, the cycle whose delta
   * carries the big blob is dropped at the size gate, every following big-blob cycle re-forces (and
   * re-drops) a key frame, and the first frame transmitted after the blob shrinks again is a KEY
   * frame carrying all fields. Without the invalidation that cycle would emit a small,
   * budget-fitting delta diffed against the never-transmitted big-blob values (§5.3.3: the baseline
   * is the last TRANSMITTED values) — a subscriber would conclude the blob changed big→small having
   * never seen "big".
   */
  @Test
  void oversizeSkippedDeltaForcesKeyFrameOnNextTransmittedMessage() throws Exception {
    var counter = new AtomicInteger(0);
    var blob = new AtomicReference<>(ByteString.of(new byte[4]));

    // measure the key frame with an unrestricted publisher: all three fields are fixed-width on
    // the wire while the blob keeps its 4-byte length, so every key frame has exactly this size
    StubTransport unrestricted =
        startPublisher(
            blobPublishedDataSet(),
            blobWriterGroup(100, uint(0)),
            blobSource(counter, blob),
            UdpDatagramAddress.multicast("239.255.74.29", 24759).networkInterface("127.0.0.1"));

    byte[] keyFrame = unrestricted.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(keyFrame, "no frame published within the deadline");
    assertEquals(DataSetMessageKind.KEY_FRAME, decode(keyFrame).messages().get(0).kind());

    service.close();
    service = null;

    // budget = the small-blob key frame size: key frames and counter-only deltas fit, any frame
    // carrying the 200-byte blob exceeds it; keyFrameCount 100 keeps the cadence out of the way
    StubTransport restricted =
        startPublisher(
            blobPublishedDataSet(),
            blobWriterGroup(100, uint(keyFrame.length)),
            blobSource(counter, blob),
            UdpDatagramAddress.multicast("239.255.74.30", 24760).networkInterface("127.0.0.1"));

    var drops = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    service.addDiagnosticsListener(
        event -> {
          if (event.statusCode().value() == StatusCodes.Bad_EncodingLimitsExceeded) {
            drops.add(event);
          }
        });

    assertEquals(DataSetMessageKind.KEY_FRAME, nextFrame(restricted).kind());
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(restricted).kind());

    // the blob grows: that cycle's delta {counter, blob} exceeds the budget and is dropped
    blob.set(ByteString.of(new byte[200]));

    PubSubDiagnosticsEvent drop = drops.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(drop, "no oversize-drop diagnostics event within the deadline");
    assertEquals("conn/WG", drop.path());

    // every frame in the queue predates the drop (sends enqueue on the publish thread, cycles
    // before the one whose drop we just observed); while the blob stays big, every cycle drafts
    // a key frame carrying it, which is dropped too, so nothing further arrives
    restricted.sent.clear();

    // the blob shrinks: the first transmitted frame is the forced key frame with all fields,
    // never a delta against the unsent big-blob baseline
    blob.set(ByteString.of(new byte[] {1, 2, 3, 4}));

    DecodedDataSetMessage healed = nextFrame(restricted);
    assertEquals(DataSetMessageKind.KEY_FRAME, healed.kind());
    assertEquals(3, healed.fields().size());

    // and delta emission resumes against the freshly transmitted baseline
    assertEquals(DataSetMessageKind.DELTA_FRAME, nextFrame(restricted).kind());
  }

  /**
   * A writer whose data DataSetMessages all die at the size gate still emits keep-alives when a
   * keepAliveTime is configured: {@code lastSentNanos} reflects NetworkMessages actually handed to
   * the transport channel, never drafts, so the keep-alive emission (§6.2.6.3 "no DataSetMessage
   * was sent in this period") stays reachable even though a (doomed) data draft exists every cycle
   * — and the header-only keep-alive (§7.2.4.5.8) fits the budget the data does not. Subscribers
   * keep learning the writer is alive instead of timing out while the publisher silently drops
   * every cycle.
   */
  @Test
  void keepAliveFiresWhenDataIsOversizeSkipped() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    // measure the (constant-sized) encoded key frame with an unrestricted publisher
    StubTransport unrestricted =
        startPublisher(
            100,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.31", 24761).networkInterface("127.0.0.1"));

    byte[] keyFrame = unrestricted.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(keyFrame, "no frame published within the deadline");
    assertEquals(DataSetMessageKind.KEY_FRAME, decode(keyFrame).messages().get(0).kind());

    service.close();
    service = null;

    // restart with a budget one byte below the key frame size and a keep-alive time: every data
    // frame (each cycle's re-forced key frame, the counter changes every read) is dropped
    StubTransport restricted =
        startPublisher(
            100,
            Duration.ofMillis(100),
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.32", 24762).networkInterface("127.0.0.1"),
            uint(keyFrame.length - 1));

    // everything reaching the wire is a keep-alive
    for (int i = 0; i < 3; i++) {
      DecodedDataSetMessage frame = nextFrame(restricted);
      assertEquals(DataSetMessageKind.KEEP_ALIVE, frame.kind(), "frame " + i);
    }
  }

  /**
   * A UADP writer with a non-zero ConfiguredSize never emits delta frames: fixed-size layouts are
   * key-frame-only (Annex A.2.1.7), and a delta exceeding the ConfiguredSize would be valid-bit
   * cleared (§6.3.1.3.3) yet still SEND — bypassing the not-transmitted baseline invalidation and
   * leaving subscribers silently stale. Startup validation rejects the enabled combination, so the
   * degradation path is exercised the only way it can be reached: the writer is disabled at startup
   * (tolerated) and enabled at runtime. With keyFrameCount 3 and a field changing every cycle, a
   * delta-capable writer would emit deltas on two of every three cycles; instead every transmitted
   * frame is a key frame carrying all fields (a valid-cleared key frame would simply be superseded
   * by the next cycle's, the self-healing pre-delta posture).
   */
  @Test
  void configuredSizeWriterEnabledAfterStartupDegradesToEveryCycleKeyFrames() throws Exception {
    var counter = new AtomicInteger(0);
    var constant = new AtomicReference<>(100);

    StubTransport transport =
        startPublisher(
            3,
            null,
            countingSource(counter, constant),
            UdpDatagramAddress.multicast("239.255.74.28", 24758).networkInterface("127.0.0.1"),
            uint(0),
            // generous fixed size: the ~15-byte encoded key frame is zero-padded up to it
            ushort(64),
            false);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    service.enable(writer);
    assertEquals(PubSubState.Operational, service.state(writer));

    for (int i = 0; i < 6; i++) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.KEY_FRAME, message.kind(), "frame " + i);
      assertEquals(2, message.fields().size(), "frame " + i + " carries all fields");
    }
  }

  // endregion

  // region keep-alive gating by writer state

  /**
   * A writer in {@code PubSubState.Error} emits NO keep-alives: a keep-alive asserts the writer is
   * still active (§6.2.6.3), which an Error writer is not — emitting one would mask its failure
   * from subscribers, who would keep resetting their receive timeouts against a writer that can
   * never produce data. Reached the HG4 way (the only path to a writer-level Error under an
   * Operational group: the group-level activation re-check rejects any enabled RawData writer
   * outright): a RawData writer disabled at startup (tolerated) is enabled at runtime and fails the
   * writer-level activation re-check into Error, while its sibling keeps publishing. The sibling's
   * source never changes, so after its initial key frame the wire carries nothing but its
   * keep-alives; pre-gate, the Error writer's keep-alives (due from group activation on) joined the
   * same shared partition — and since its RawData mask is rejected by the encoder backstop, they
   * poisoned the whole NetworkMessage, silencing the healthy sibling too (with a feature the
   * encoder could express, they would instead have masked the Error from subscribers).
   * Disabled/Paused keep-alive emission is unchanged (pinned by
   * PubSubStateMachineTest#keepAliveEmittedPerNonOperationalWriterWithoutSequenceIncrement).
   */
  @Test
  void errorWriterEmitsNoKeepAlives() throws Exception {
    PublishedDataSetSource constantSource =
        context ->
            DataSetSnapshot.builder(context)
                .field("counter", new DataValue(Variant.ofInt32(7)))
                .field("constant", new DataValue(Variant.ofInt32(100)))
                .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(25))
            .keepAliveTime(Duration.ofMillis(200))
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(
                        // 0x41: PublisherId | PayloadHeader, so frames carry the writer id
                        new UadpNetworkMessageContentMask(uint(0x41)))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .keyFrameCount(uint(100))
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(
                                // 0x20: SequenceNumber
                                new UadpDataSetMessageContentMask(uint(0x20)))
                            .build())
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W2")
                    // disabled at startup (tolerated): the group activates without it
                    .enabled(false)
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(2))
                    // unsupported under the built-in UADP mapping: activation fails into Error
                    .fieldContentMask(
                        DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData))
                    .settings(UadpDataSetWriterSettings.builder().build())
                    .build())
            .build();

    StubTransport transport =
        startPublisher(
            publishedDataSet(),
            group,
            constantSource,
            UdpDatagramAddress.multicast("239.255.74.33", 24763).networkInterface("127.0.0.1"));

    // enabling W2 is first seen by the writer-level activation re-check, which fails it into
    // Error (the HG4 backstop); the group and W1 stay Operational
    PubSubHandle w2 = service.components().dataSetWriter("conn", "WG", "W2").orElseThrow();
    service.enable(w2);

    PubSubHandle groupHandle = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle w1 = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(groupHandle));
    assertEquals(PubSubState.Operational, service.state(w1));
    assertEquals(PubSubState.Error, service.state(w2));

    // drop everything captured before W2 entered Error (W1's initial key frame, typically); a
    // W2 keep-alive cannot have been emitted in that window — its keep-alive reference was
    // reset at group activation, moments ago at machine timescale, against a 200 ms period
    transport.sent.clear();

    // W1's constant source suppresses every delta cycle, so it emits a keep-alive every period;
    // scan until three have passed — a window spanning several periods in which W2's
    // keep-alives were due — asserting every wire DataSetMessage is W1's
    int keepAlives = 0;
    while (keepAlives < 3) {
      byte[] frame = transport.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      assertNotNull(frame, "no frame published within the deadline");
      for (DecodedDataSetMessage message : decode(frame).messages()) {
        assertEquals(ushort(1), message.dataSetWriterId(), "only the Operational writer emits");
        if (message.kind() == DataSetMessageKind.KEEP_ALIVE) {
          keepAlives++;
        }
      }
    }
  }

  // endregion
}
