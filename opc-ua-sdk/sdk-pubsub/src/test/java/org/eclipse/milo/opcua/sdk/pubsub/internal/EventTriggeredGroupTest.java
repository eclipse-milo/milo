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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
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
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Runtime behavior of event-triggered (Part 14 PublishingInterval 0) writer groups (D-4..D-7): a
 * group with an event-source dataset runs no fixed-rate publish cycle — {@code publishEvent} pushes
 * events into the writer's bounded buffer and triggers one coalesced publish that drains them into
 * EVENT NetworkMessages ({@link org.eclipse.milo.opcua.sdk.pubsub.PubSubService#publishEvent}); a
 * configured keepAliveTime still emits keep-alives when idle; buffered events survive a path-stable
 * group restart and are drained on the successor's Operational entry (the {@code
 * onEnterOperational} self-trigger); per-event NetworkMessages within one cycle carry sequential
 * NetworkMessageNumbers (§7.2.4.4.2); and the engine's startup pre-validation (D-10) rejects event
 * writers the built-in mappings cannot carry (a JSON writer without the MessageType member, a UADP
 * writer with a non-zero ConfiguredSize).
 *
 * <p>Following the {@link DeltaFrameCadenceTest} pattern: real publish scheduling with a stub
 * transport whose sent queue is decoded and classified — assertions are on the frame kind, count,
 * and NetworkMessageNumber, never on wall-clock cadence. Where a test needs a deterministic single
 * drain cycle (coalescing, restart preservation), a {@link HoldingScheduledExecutor} holds the
 * event-trigger submissions until the test releases them, so the outcome does not depend on timing.
 * Connections with writer groups open real UDP discovery sockets, so every publishing test pins the
 * discoveryAddress to a unique loopback multicast group instead of the 224.0.2.14:4840 default.
 */
class EventTriggeredGroupTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublishedDataSetRef EVENTS_REF = new PublishedDataSetRef("events");

  /**
   * Stable dataSetFieldIds for the two {@link #eventDataSet()} fields. {@link #eventDataSet()} is
   * rebuilt on every {@code eventConfig} construction, and an unset dataSetFieldId is randomized
   * per build ({@link EventFieldDefinition}), so leaving them unset makes successive builds of the
   * "events" dataset non-equal by field GUID. Fixing them keeps rebuilds value-equal, so a
   * group-shell reconfigure (e.g. maxNetworkMessageSize) restarts only the writer group and does
   * not mutate the dataset's DataSetMetaData — which would otherwise fire an unsolicited §7.2.4.6.4
   * DataSetMetaData announcement onto the data channel and defeat the "nothing drained" assertions
   * of {@link #bufferedEventsSurviveGroupRestartAndDrainOnOperationalEntry}.
   */
  private static final UUID SEVERITY_FIELD_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000001");

  private static final UUID MESSAGE_FIELD_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000002");

  /** PublisherId | PayloadHeader: frames carry the writer id (mirrors DeltaFrameCadenceTest). */
  private static final UadpNetworkMessageContentMask NM_MASK =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  /**
   * PublisherId | GroupHeader | NetworkMessageNumber | PayloadHeader: the GroupHeader carries the
   * NetworkMessageNumber field (§7.2.4.4.2), decodable on the wire.
   */
  private static final UadpNetworkMessageContentMask NM_MASK_WITH_NUMBER =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.GroupHeader,
          UadpNetworkMessageContentMask.Field.NetworkMessageNumber,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  /** SequenceNumber, so decoded DataSetMessages carry a sequence number. */
  private static final UadpDataSetMessageContentMask DSM_MASK =
      UadpDataSetMessageContentMask.of(UadpDataSetMessageContentMask.Field.SequenceNumber);

  private @Nullable PubSubService service;
  private @Nullable HoldingScheduledExecutor holdingExecutor;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
    if (holdingExecutor != null) {
      holdingExecutor.shutdownDelegate();
      holdingExecutor = null;
    }
  }

  // region fixtures

  /** A transport that never touches the network: captures publisher sends into a queue. */
  private static final class StubTransport implements TransportProvider {

    final BlockingQueue<byte[]> sent = new LinkedBlockingQueue<>();

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

  /** A two-field event dataset (Severity, Message) sourced from the Server notifier. */
  private static PublishedDataSetConfig eventDataSet() {
    SimpleAttributeOperand severity =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Severity")},
            AttributeId.Value.uid(),
            null);
    SimpleAttributeOperand message =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);

    return PublishedDataSetConfig.builder("events")
        .source(
            PublishedEventsConfig.builder(NodeIds.Server.expanded())
                .field(
                    EventFieldDefinition.builder("Severity")
                        .selectedField(severity)
                        .dataType(NodeIds.UInt16)
                        .dataSetFieldId(SEVERITY_FIELD_ID)
                        .build())
                .field(
                    EventFieldDefinition.builder("Message")
                        .selectedField(message)
                        .dataType(NodeIds.String)
                        .dataSetFieldId(MESSAGE_FIELD_ID)
                        .build())
                .build())
        .build();
  }

  /** Two Variant field values matching the {@link #eventDataSet()} arity, tagged by {@code n}. */
  private static List<Variant> eventFields(int n) {
    return List.of(Variant.ofUInt16(ushort(n)), Variant.ofString("evt-" + n));
  }

  /**
   * A UDP {@link PubSubConfig} whose single interval-0 writer group publishes the event dataset.
   *
   * @param keepAliveTime the group keepAliveTime, or {@code null} for none.
   * @param maxNetworkMessageSize the group maxNetworkMessageSize (0 = unlimited).
   * @param nmMask the group NetworkMessage content mask.
   * @param configuredSize the writer's UADP ConfiguredSize (0 = dynamic).
   */
  private static PubSubConfig eventConfig(
      @Nullable Duration keepAliveTime,
      UInteger maxNetworkMessageSize,
      UadpNetworkMessageContentMask nmMask,
      UShort configuredSize,
      UdpDatagramAddress discoveryAddress) {

    WriterGroupConfig.Builder group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ZERO)
            .maxNetworkMessageSize(maxNetworkMessageSize)
            .messageSettings(
                UadpWriterGroupSettings.builder().networkMessageContentMask(nmMask).build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(EVENTS_REF)
                    .dataSetWriterId(ushort(1))
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(DSM_MASK)
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
            .discoveryAddress(discoveryAddress)
            .writerGroup(group.build())
            .build();

    return PubSubConfig.builder().publishedDataSet(eventDataSet()).connection(connection).build();
  }

  /**
   * An interval-0 event {@link PubSubConfig} whose single writer carries the given {@code
   * eventQueueCapacity}. Changing only the capacity between two otherwise-identical configs yields
   * a LEAF-level (DataSetWriter) path-stable restart that leaves the group Operational — the case
   * the writer-level {@code onEnterOperational} self-trigger covers.
   */
  private static PubSubConfig eventConfigWithWriterCapacity(
      int eventQueueCapacity, UdpDatagramAddress discoveryAddress) {

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ZERO)
            .messageSettings(
                UadpWriterGroupSettings.builder().networkMessageContentMask(NM_MASK).build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(EVENTS_REF)
                    .dataSetWriterId(ushort(1))
                    .eventQueueCapacity(eventQueueCapacity)
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(DSM_MASK)
                            .build())
                    .build())
            .build();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(99)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .discoveryAddress(discoveryAddress)
            .writerGroup(group)
            .build();

    return PubSubConfig.builder().publishedDataSet(eventDataSet()).connection(connection).build();
  }

  private static UdpDatagramAddress discovery(String group, int port) {
    return UdpDatagramAddress.multicast(group, port).networkInterface("127.0.0.1");
  }

  private StubTransport startService(PubSubConfig config, @Nullable ScheduledExecutorService sched)
      throws Exception {

    var transport = new StubTransport();

    PubSubServiceConfig.Builder serviceConfig =
        PubSubServiceConfig.builder().transportProvider(transport);
    if (sched != null) {
      serviceConfig.scheduledExecutor(sched);
    }

    service = PubSubService.create(config, PubSubBindings.builder().build(), serviceConfig.build());
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return transport;
  }

  private static DecodedNetworkMessage decode(byte[] frame) throws UaException {
    ByteBuf buffer = Unpooled.wrappedBuffer(frame);
    try {
      return new UadpMessageMapping()
          .decode(DecodeContext.of(new DefaultEncodingContext()), buffer);
    } finally {
      buffer.release();
    }
  }

  private static DecodedNetworkMessage nextNetworkMessage(StubTransport transport)
      throws Exception {
    byte[] frame = transport.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(frame, "no frame published within the deadline");
    return decode(frame);
  }

  private static DecodedDataSetMessage nextFrame(StubTransport transport) throws Exception {
    List<DecodedDataSetMessage> messages = nextNetworkMessage(transport).messages();
    assertEquals(
        1, messages.size(), "each event NetworkMessage carries exactly one DataSetMessage");
    return messages.get(0);
  }

  // endregion

  /**
   * An interval-0 all-event group with no keepAliveTime runs no fixed-rate publish task: nothing is
   * on the wire until an event is pushed, and then {@code publishEvent} triggers a prompt publish
   * that emits the event as an EVENT NetworkMessage carrying the dataset's fields.
   */
  @Test
  void publishEventEmitsEventNetworkMessagePromptly() throws Exception {
    StubTransport transport =
        startService(
            eventConfig(null, uint(0), NM_MASK, ushort(0), discovery("239.255.75.1", 24801)), null);

    // no fixed-rate cycle: an idle interval-0 group with no keepAliveTime never publishes on its
    // own
    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS),
        "an interval-0 group with no events and no keepAliveTime must not publish on a fixed rate");

    service.publishEvent(EVENTS_REF, eventFields(7));

    DecodedDataSetMessage message = nextFrame(transport);
    assertEquals(DataSetMessageKind.EVENT, message.kind());
    assertEquals(2, message.fields().size(), "the event carries both dataset fields");
    assertEquals(ushort(7), message.fields().get(0).value().value().value());
  }

  /**
   * Multiple rapid {@code publishEvent} calls coalesce onto the event-trigger latch, and every
   * buffered event is drained: five events pushed back to back all reach the wire as EVENT
   * DataSetMessages, and nothing extra follows (no fixed-rate task).
   */
  @Test
  void rapidPublishEventsCoalesceAndAllDrain() throws Exception {
    StubTransport transport =
        startService(
            eventConfig(null, uint(0), NM_MASK, ushort(0), discovery("239.255.75.2", 24802)), null);

    Set<Integer> expected = new HashSet<>();
    for (int i = 1; i <= 5; i++) {
      expected.add(i);
      service.publishEvent(EVENTS_REF, eventFields(i));
    }

    Set<Integer> observed = new HashSet<>();
    while (observed.size() < 5) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.EVENT, message.kind());
      observed.add(((UShort) message.fields().get(0).value().value().value()).intValue());
    }
    assertEquals(expected, observed, "every buffered event is drained exactly once");

    // the whole burst drained; with no keepAliveTime nothing else is emitted
    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS), "no frame beyond the drained events");
  }

  /**
   * An interval-0 group with a keepAliveTime emits keep-alives when idle: the keepAliveTime timer
   * runs the normal publish cycle and, finding no events, emits keep-alive DataSetMessages. Only
   * that a keep-alive arrives is asserted, never the exact cadence (the ~2x keepAliveTime silence
   * bound is documented, not pinned).
   */
  @Test
  void keepAlivesEmittedWhenIdleUnderKeepAliveTime() throws Exception {
    StubTransport transport =
        startService(
            eventConfig(
                Duration.ofMillis(100),
                uint(0),
                NM_MASK,
                ushort(0),
                discovery("239.255.75.3", 24803)),
            null);

    for (int i = 0; i < 2; i++) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.KEEP_ALIVE, message.kind(), "keep-alive " + i);
    }
  }

  /**
   * Engine startup pre-validation (D-10): an enabled event writer under the JSON mapping whose
   * effective DataSetMessage content mask lacks the MessageType member is rejected at startup with
   * {@code Bad_ConfigurationError} — a JSON event ({@code ua-event}) without it is wire-ambiguous
   * with a key frame (Annex A.3.3.4).
   */
  @Test
  void jsonEventWriterWithoutMessageTypeRejectedAtStartup() {
    JsonNetworkMessageContentMask nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader);
    // DataSetWriterId | SequenceNumber, deliberately WITHOUT MessageType
    JsonDataSetMessageContentMask dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.SequenceNumber);

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet())
            .connection(
                PubSubConnectionConfig.mqtt("conn")
                    .publisherId(PublisherId.string("pub"))
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .writerGroup(
                        WriterGroupConfig.builder("WG")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ZERO)
                            .messageSettings(
                                JsonWriterGroupSettings.builder()
                                    .networkMessageContentMask(nmMask)
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("W1")
                                    .dataSet(EVENTS_REF)
                                    .dataSetWriterId(ushort(1))
                                    .settings(
                                        JsonDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(dsmMask)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    UaException cause =
        assertStartupFailsWith(config, StatusCodes.Bad_ConfigurationError, "MessageType");
    assertNotNull(cause);
  }

  /**
   * Engine startup pre-validation (D-10): an enabled event writer under the UADP mapping with a
   * non-zero ConfiguredSize is rejected at startup with {@code Bad_ConfigurationError} — a
   * fixed-size UADP layout cannot carry events (§7.2.4.5.7).
   */
  @Test
  void uadpEventWriterWithConfiguredSizeRejectedAtStartup() {
    PubSubConfig config =
        eventConfig(null, uint(0), NM_MASK, ushort(64), discovery("239.255.75.5", 24805));

    assertStartupFailsWith(config, StatusCodes.Bad_ConfigurationError, "ConfiguredSize");
  }

  /**
   * Per-event NetworkMessages produced within one publish cycle carry sequential
   * NetworkMessageNumbers 1..N (§7.2.4.4.2) when the UADP network mask enables the field. The
   * event-trigger submissions are held so all four events drain in a single cycle, making the
   * numbering deterministic.
   */
  @Test
  void perEventNetworkMessagesCarrySequentialNumbers() throws Exception {
    var executor = new HoldingScheduledExecutor();
    holdingExecutor = executor;

    // Hold every event-trigger submission — including the group's Operational-entry self-trigger
    // (D-7), which fires during startup and would otherwise escape a hold engaged only afterwards —
    // so the whole burst drains in ONE publish cycle when released.
    executor.hold();

    StubTransport transport =
        startService(
            eventConfig(
                null, uint(0), NM_MASK_WITH_NUMBER, ushort(0), discovery("239.255.75.6", 24806)),
            executor);

    for (int i = 1; i <= 4; i++) {
      service.publishEvent(EVENTS_REF, eventFields(i));
    }
    assertNull(transport.sent.poll(300, TimeUnit.MILLISECONDS), "the drain is held until released");

    executor.release();

    for (int i = 1; i <= 4; i++) {
      DecodedNetworkMessage nm = nextNetworkMessage(transport);
      assertNotNull(
          nm.networkMessageNumber(), "the NetworkMessageNumber field is present on the wire");
      assertEquals(
          i,
          nm.networkMessageNumber().intValue(),
          "per-event NetworkMessages are numbered sequentially within the cycle");
      assertEquals(1, nm.messages().size());
      assertEquals(DataSetMessageKind.EVENT, nm.messages().get(0).kind());
    }
  }

  /**
   * The {@code onEnterOperational} self-trigger drains events that were accepted while the runtime
   * was not yet Operational (D-7). Events pushed to the group (their drain held) are preserved
   * across a path-stable GROUP-level restart — snapshotted from the disposed predecessor and seeded
   * into the fresh successor while it is still Disabled — and drained when the successor enters
   * Operational, so a deactivate/reactivate window strands nothing already accepted.
   */
  @Test
  void bufferedEventsSurviveGroupRestartAndDrainOnOperationalEntry() throws Exception {
    var executor = new HoldingScheduledExecutor();
    holdingExecutor = executor;

    // Engage the hold BEFORE startup: the group fires its Operational-entry self-trigger (D-7) the
    // moment it activates during startup, submitting a coalesced publish to this executor. Holding
    // only after startup would let that startup submission escape the latch — it drains nothing at
    // startup (no events buffered yet) but lingers on the single-threaded delegate and, if delayed,
    // can run later while the predecessor group is still Operational and drain the events buffered
    // below, defeating the "held until released" assertions. Held from the outset, the startup
    // self-trigger sits on the latch too, so the only drains are the ones this test controls.
    executor.hold();

    UdpDatagramAddress discovery = discovery("239.255.75.7", 24807);
    StubTransport transport =
        startService(eventConfig(null, uint(0), NM_MASK, ushort(0), discovery), executor);

    // accept two events but hold their drain, so they are still buffered at restart time
    service.publishEvent(EVENTS_REF, eventFields(1));
    service.publishEvent(EVENTS_REF, eventFields(2));
    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS),
        "accepted events are held, not yet drained");

    // path-stable group restart (maxNetworkMessageSize shell change): the predecessor is disposed
    // and its buffered events transfer to the successor, whose onEnterOperational drains them
    ReconfigureResult result =
        service.reconfigure(
            eventConfig(null, uint(60000), NM_MASK, ushort(0), discovery),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertTrue(
        result.restartedPaths().contains("conn/WG"),
        "the group restarted path-stably: " + result.restartedPaths());

    // the successor's self-trigger publish is also held: nothing has drained yet
    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS),
        "the successor's Operational-entry drain is held until released");

    executor.release();

    Set<Integer> observed = new HashSet<>();
    while (observed.size() < 2) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.EVENT, message.kind());
      observed.add(((UShort) message.fields().get(0).value().value().value()).intValue());
    }
    assertEquals(Set.of(1, 2), observed, "both preserved events drain after the restart");
  }

  /**
   * The writer-level {@code onEnterOperational} self-trigger drains events preserved across a
   * LEAF-level (DataSetWriter) path-stable restart. Unlike a group restart, a writer-only change
   * leaves the group Operational, so the group's own {@code onEnterOperational} never fires; only
   * the writer entering Operational triggers the drain. Without the writer-level hook the preserved
   * events would strand in an interval-0 group with no keepAliveTime until the next {@code
   * publishEvent}.
   */
  @Test
  void bufferedEventsSurviveLeafRestartAndDrainOnWriterOperationalEntry() throws Exception {
    var executor = new HoldingScheduledExecutor();
    holdingExecutor = executor;

    // Hold from the outset so the startup self-trigger sits on the latch and the only drains are
    // the
    // ones this test releases (see bufferedEventsSurviveGroupRestartAndDrainOnOperationalEntry).
    executor.hold();

    UdpDatagramAddress discovery = discovery("239.255.75.8", 24808);
    StubTransport transport = startService(eventConfigWithWriterCapacity(100, discovery), executor);

    service.publishEvent(EVENTS_REF, eventFields(1));
    service.publishEvent(EVENTS_REF, eventFields(2));
    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS),
        "accepted events are held, not yet drained");

    // leaf-only change (writer eventQueueCapacity): the writer restarts path-stably while the group
    // stays Operational, so the group's onEnterOperational does not fire — only the writer's does
    ReconfigureResult result =
        service.reconfigure(
            eventConfigWithWriterCapacity(50, discovery),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertTrue(
        result.restartedPaths().contains("conn/WG/W1"),
        "the writer restarted path-stably: " + result.restartedPaths());
    assertTrue(
        result.restartedPaths().stream().noneMatch(p -> p.equals("conn/WG")),
        "the group did NOT restart (leaf-only change): " + result.restartedPaths());

    assertNull(
        transport.sent.poll(300, TimeUnit.MILLISECONDS),
        "the successor writer's Operational-entry drain is held until released");

    executor.release();

    Set<Integer> observed = new HashSet<>();
    while (observed.size() < 2) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.EVENT, message.kind());
      observed.add(((UShort) message.fields().get(0).value().value().value()).intValue());
    }
    assertEquals(
        Set.of(1, 2), observed, "both events preserved across the leaf restart drain after it");
  }

  private UaException assertStartupFailsWith(
      PubSubConfig config, long expectedStatus, String messageSubstring) {

    var transport = new StubTransport();
    service =
        PubSubService.create(
            config,
            PubSubBindings.builder().build(),
            PubSubServiceConfig.builder().transportProvider(transport).build());

    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(expectedStatus, cause.getStatusCode().value(), cause.getMessage());
    assertTrue(
        cause.getMessage() != null && cause.getMessage().contains(messageSubstring),
        "expected message to mention '" + messageSubstring + "' but was: " + cause.getMessage());
    return cause;
  }

  /**
   * A {@link ScheduledExecutorService} that can hold {@link #execute(Runnable)} submissions — the
   * event-trigger path ({@code WriterGroupRuntime#triggerEventPublish}) — until {@link #release()},
   * delegating everything else to an owned single-thread scheduled executor. Lets a test decide
   * exactly when queued events drain, so coalescing and restart-preservation outcomes do not depend
   * on timing.
   */
  private static final class HoldingScheduledExecutor implements ScheduledExecutorService {

    private final ScheduledExecutorService delegate = Executors.newSingleThreadScheduledExecutor();
    private final Object gate = new Object();
    private boolean holding = false;
    private final List<Runnable> held = new ArrayList<>();

    void hold() {
      synchronized (gate) {
        holding = true;
      }
    }

    void release() {
      List<Runnable> toRun;
      synchronized (gate) {
        holding = false;
        toRun = new ArrayList<>(held);
        held.clear();
      }
      for (Runnable r : toRun) {
        delegate.execute(r);
      }
    }

    void shutdownDelegate() throws InterruptedException {
      release();
      delegate.shutdownNow();
      delegate.awaitTermination(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void execute(Runnable command) {
      synchronized (gate) {
        if (holding) {
          held.add(command);
          return;
        }
      }
      delegate.execute(command);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      return delegate.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      return delegate.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(
        Runnable command, long initialDelay, long period, TimeUnit unit) {
      return delegate.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {
      return delegate.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override
    public void shutdown() {
      delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
      return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
      return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
      return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
      return delegate.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
      return delegate.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
      return delegate.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException {
      return delegate.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
      return delegate.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
      return delegate.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      return delegate.invokeAny(tasks, timeout, unit);
    }
  }
}
