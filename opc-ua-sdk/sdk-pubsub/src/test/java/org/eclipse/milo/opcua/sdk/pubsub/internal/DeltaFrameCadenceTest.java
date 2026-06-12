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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
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
 * degrades to every-cycle key frames.
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
      return new SubscriberChannel() {
        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
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

    var transport = new StubTransport();

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

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(99)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            // a connection with writer groups opens real UDP discovery sockets: keep them on a
            // unique loopback multicast group, never the 224.0.2.14:4840 default
            .discoveryAddress(discoveryAddress)
            .writerGroup(group.build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(publishedDataSet()).connection(connection).build();

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
          .decode(new DecodeContext(new DefaultEncodingContext()), buffer);
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
}
