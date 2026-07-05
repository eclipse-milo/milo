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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProvider;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Runtime behavior of the core event publish path on positive-interval writer groups (Part 14
 * §6.2.6.2): {@code publishEvent} buffers events into an event-mode {@link DataSetWriterRuntime},
 * whose per-cycle drain emits one {@code EVENT} DataSetMessage per event, each in its OWN
 * NetworkMessage (per-event partition), carrying the pushed field values as Variants; suppressed
 * (no-event) cycles consume no §7.2.3 sequence number while an idle event writer with a configured
 * keepAliveTime still emits header-only keep-alives (§6.2.6.3) that peek the next-expected sequence
 * without consuming it; a mixed group runs a cyclic data writer and an event writer independently,
 * and an oversize event NetworkMessage dropped at the {@code maxNetworkMessageSize} gate leaves the
 * data writer's delta baseline intact (EVENT drafts have none — {@code
 * WriterGroupRuntime#invalidateDeltaBaselines} skips them); a secured group gates publishing on key
 * availability; and an event decodes end-to-end as {@link DataSetMessageKind#EVENT} at a
 * subscriber.
 *
 * <p>Follows the {@code DeltaFrameCadenceTest} harness: real publish scheduling at short intervals
 * with a stub transport whose sent queue is decoded and classified — assertions are on frame
 * content and sequence, never on wall-clock timing. Connections with writer groups open real UDP
 * discovery sockets, so every stub-backed test pins the discoveryAddress to a unique loopback
 * multicast group instead of the {@code 224.0.2.14:4840} default; the end-to-end test uses unicast
 * loopback with ephemeral ports.
 */
class EventPublishingTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);
  private static final Duration PUBLISHING_INTERVAL = Duration.ofMillis(25);

  private static final PublishedDataSetRef EVENT_REF = new PublishedDataSetRef("evt-ds");
  private static final PublishedDataSetRef DATA_REF = new PublishedDataSetRef("data-ds");

  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 1L);
  private static final UUID CONSTANT_FIELD_ID = new UUID(0L, 2L);
  private static final UUID MESSAGE_FIELD_ID = new UUID(0L, 0x11L);
  private static final UUID SEVERITY_FIELD_ID = new UUID(0L, 0x12L);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(99));

  private @Nullable PubSubService service;
  private final List<PubSubService> tracked = new ArrayList<>();

  @AfterEach
  void shutdownServices() {
    if (service != null) {
      service.close();
      service = null;
    }
    for (PubSubService s : tracked) {
      s.close();
    }
    tracked.clear();
  }

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

  // region core event publish path

  /**
   * {@code publishEvent} delivers zero, one, or many events per publishing interval, each event
   * becoming its OWN NetworkMessage (Part 14 §6.2.6.2, per-event partition): every sent frame
   * decodes to exactly one DataSetMessage of kind {@code EVENT} carrying the pushed field values as
   * Variants, in publish (FIFO) order, and the DataSetMessage sequence numbers increment by exactly
   * one per event from 0 (§7.2.3). An idle event writer with no keepAliveTime sends nothing, so the
   * three events are the only frames on the wire.
   */
  @Test
  void publishEventEmitsOneEventNetworkMessagePerEventWithPushedValues() throws Exception {
    StubTransport transport =
        startEventPublisher(
            eventGroup(null, uint(0), null),
            eventDataSet("message", "severity"),
            multicast("239.255.75.1", 24801));

    for (int i = 0; i < 3; i++) {
      service.publishEvent(
          EVENT_REF, List.of(Variant.ofString("evt-" + i), Variant.ofUInt16(ushort(i))));
    }

    for (int i = 0; i < 3; i++) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(DataSetMessageKind.EVENT, message.kind(), "frame " + i);
      assertEquals(2, message.fields().size(), "event " + i + " carries both fields");
      assertEquals("evt-" + i, message.fields().get(0).value().value().value(), "event " + i);
      assertEquals(
          ushort(i), message.fields().get(1).value().value().value(), "severity of event " + i);
      assertEquals(i, message.sequenceNumber().intValue(), "sequence number of event " + i);
    }
  }

  /**
   * {@code publishEvent} validates its arguments best-effort against the current config (Part 14
   * D-4): an unknown dataset ref, a non-event (data-items) dataset, and a field-count mismatch each
   * raise {@link IllegalArgumentException} without offering anything to a writer.
   */
  @Test
  void publishEventRejectsInvalidArguments() throws Exception {
    var counter = new AtomicInteger(0);
    startMixedPublisher(uint(0), multicast("239.255.75.2", 24802), counter, "message", "severity");

    IllegalArgumentException unknown =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                service.publishEvent(
                    new PublishedDataSetRef("nope"), List.of(Variant.ofString("x"))));
    assertTrue(unknown.getMessage().contains("unknown"), unknown.getMessage());

    IllegalArgumentException notEvent =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.publishEvent(DATA_REF, List.of(Variant.ofInt32(1), Variant.ofInt32(2))));
    assertTrue(notEvent.getMessage().contains("not an event source"), notEvent.getMessage());

    IllegalArgumentException arity =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.publishEvent(EVENT_REF, List.of(Variant.ofString("only-one"))));
    assertTrue(arity.getMessage().contains("expected 2"), arity.getMessage());
  }

  /**
   * Event DataSetMessage sequence numbers increment once per event and never for a suppressed
   * (no-event) interval: an idle event writer with a keepAliveTime emits keep-alives (§6.2.6.3)
   * that carry the next-expected sequence number 0 without consuming it (§7.2.3), the first
   * published event consumes sequence 0, and the following keep-alive carries the next-expected 1 —
   * so every idle interval before and between events consumed nothing.
   */
  @Test
  void keepAlivePeeksTheSequenceAndOnlyEventsConsumeIt() throws Exception {
    StubTransport transport =
        startEventPublisher(
            eventGroup(Duration.ofMillis(50), uint(0), null),
            eventDataSet("payload"),
            multicast("239.255.75.3", 24803));

    // idle: keep-alives peek the next-expected sequence (0) without consuming it
    for (int i = 0; i < 2; i++) {
      DecodedDataSetMessage keepAlive =
          awaitFrame(transport, m -> m.kind() == DataSetMessageKind.KEEP_ALIVE);
      assertEquals(0, keepAlive.sequenceNumber().intValue(), "idle keep-alive " + i);
    }

    service.publishEvent(EVENT_REF, List.of(Variant.ofString("first")));

    // the event consumes sequence 0
    DecodedDataSetMessage event = awaitFrame(transport, m -> m.kind() == DataSetMessageKind.EVENT);
    assertEquals(0, event.sequenceNumber().intValue());
    assertEquals("first", event.fields().get(0).value().value().value());

    // the next keep-alive now peeks the next-expected 1: exactly one sequence number was consumed
    DecodedDataSetMessage afterEvent =
        awaitFrame(
            transport,
            m -> m.kind() == DataSetMessageKind.KEEP_ALIVE && m.sequenceNumber().intValue() == 1);
    assertEquals(1, afterEvent.sequenceNumber().intValue());
  }

  // endregion

  // region mixed data + event group

  /**
   * A data-items writer and an event writer in the same group run independently: the data writer's
   * key/delta cadence (keyFrameCount 3, a field changing every cycle) continues uninterrupted while
   * events flow, and the pushed events decode as {@code EVENT} DataSetMessages in their own
   * NetworkMessages with their field values, interleaved with the cyclic data.
   */
  @Test
  void mixedGroupRunsDataAndEventsIndependently() throws Exception {
    var counter = new AtomicInteger(0);
    StubTransport transport =
        startMixedPublisher(uint(0), multicast("239.255.75.4", 24804), counter, "payload");

    var dataFrames = new ArrayList<DecodedDataSetMessage>();
    var eventFrames = new ArrayList<DecodedDataSetMessage>();
    boolean published = false;
    while (eventFrames.size() < 2) {
      DecodedDataSetMessage message = nextFrame(transport);
      if (ushort(1).equals(message.dataSetWriterId())) {
        dataFrames.add(message);
      } else if (ushort(2).equals(message.dataSetWriterId())) {
        eventFrames.add(message);
      }
      if (!published && dataFrames.size() >= 3) {
        service.publishEvent(EVENT_REF, List.of(Variant.ofString("evt-a")));
        service.publishEvent(EVENT_REF, List.of(Variant.ofString("evt-b")));
        published = true;
      }
    }

    // the events flowed as their own EVENT NetworkMessages with the pushed values, in publish order
    assertEquals(DataSetMessageKind.EVENT, eventFrames.get(0).kind());
    assertEquals(DataSetMessageKind.EVENT, eventFrames.get(1).kind());
    assertEquals("evt-a", eventFrames.get(0).fields().get(0).value().value().value());
    assertEquals("evt-b", eventFrames.get(1).fields().get(0).value().value().value());

    // the data writer's key/delta cadence continued unbroken across the whole window
    assertTrue(dataFrames.size() >= 4, "expected several data frames");
    assertDataCadence(dataFrames);
  }

  /**
   * Isolation (Part 14 D-6): an oversize event whose single-event NetworkMessage is dropped at the
   * {@code maxNetworkMessageSize} gate ({@code Bad_EncodingLimitsExceeded}) does NOT disturb the
   * sibling data writer's delta baseline — EVENT drafts have no baseline, so {@code
   * WriterGroupRuntime#invalidateDeltaBaselines} skips the {@code invalidateDeltaBaseline()} call
   * for them. The data writer's key/delta cadence therefore stays unbroken across the drop; had the
   * event drop wrongly invalidated the data baseline, a delta cycle would have been forced to a key
   * frame.
   */
  @Test
  void oversizeEventDropLeavesDataWriterBaselineIntact() throws Exception {
    var counter = new AtomicInteger(0);
    // budget admits the tiny two-Int32 data frames but not a single event carrying an 8 KB blob
    StubTransport transport =
        startMixedPublisher(uint(1024), multicast("239.255.75.5", 24805), counter, "payload");

    var drops = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    service.addDiagnosticsListener(
        event -> {
          if (event.statusCode().value() == StatusCodes.Bad_EncodingLimitsExceeded) {
            drops.add(event);
          }
        });

    // every frame on the wire is a data frame (the oversize event is dropped before transmission)
    var dataFrames = new ArrayList<DecodedDataSetMessage>();
    while (dataFrames.size() < 9) {
      DecodedDataSetMessage message = nextFrame(transport);
      assertEquals(ushort(1), message.dataSetWriterId(), "only the data writer reaches the wire");
      dataFrames.add(message);
      if (dataFrames.size() == 3) {
        service.publishEvent(
            EVENT_REF, List.of(Variant.ofByteString(ByteString.of(new byte[8000]))));
      }
    }

    // the oversize event NetworkMessage was dropped at the group's size gate
    PubSubDiagnosticsEvent drop = drops.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(drop, "expected an oversize-event drop within the deadline");
    assertEquals("conn/WG", drop.path());

    // and the data writer's cadence is unbroken across the drop (baseline never invalidated)
    assertDataCadence(dataFrames);
  }

  // endregion

  // region secured group

  /**
   * A secured writer group gates publishing on key availability: while its first key fetch is
   * pending the group stays {@code PreOperational} and nothing is published; once the keys arrive
   * it becomes {@code Operational} and pushed events are drained as (secured) event
   * NetworkMessages, one per event.
   *
   * <p>Secured NetworkMessages are not decoded here (the plaintext codec has no security context),
   * so the assertion is on frame count — one NetworkMessage per event — plus the state transition.
   */
  @Test
  void securedGroupGatesEventPublishingOnKeys() throws Exception {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;
    var gate = new GatedKeyProvider(StaticSecurityKeyProvider.of(policy, keyData(policy)));

    SecurityGroupRef securityGroupRef = new SecurityGroupRef("sg");
    MessageSecurityConfig security =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(securityGroupRef)
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet("payload"))
            .securityGroup(
                SecurityGroupConfig.builder("sg").securityPolicyUri(policy.getUri()).build())
            .connection(
                connection(eventGroup(null, uint(0), security), multicast("239.255.75.6", 24806)))
            .build();

    PubSubBindings bindings = PubSubBindings.builder().securityKeys(securityGroupRef, gate).build();

    StubTransport transport = startEventPublisher(config, bindings);

    // startup returns without awaiting the (gated) key fetch: the secured group stays
    // PreOperational
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.PreOperational, service.state(group), "unkeyed secured group");

    // release the keys: the group completes its deferred startup and goes Operational
    gate.open();
    awaitTrue(
        () -> service.state(group) == PubSubState.Operational, "secured group reaches Operational");

    // now-operational: three events publish as three (secured) NetworkMessages
    for (int i = 0; i < 3; i++) {
      service.publishEvent(EVENT_REF, List.of(Variant.ofString("evt-" + i)));
    }
    for (int i = 0; i < 3; i++) {
      byte[] frame = transport.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      assertNotNull(frame, "secured event NetworkMessage " + i);
    }
  }

  // endregion

  // region end-to-end reader reception

  /**
   * A published event is decoded and delivered to a DataSetReader as a {@link DataSetReceivedEvent}
   * whose {@code kind} is {@link DataSetMessageKind#EVENT}, carrying the pushed field values, over
   * unicast loopback UDP.
   */
  @Test
  void publishedEventArrivesAtReaderAsEventKind() throws Exception {
    int port = freeUdpPort();

    PublishedDataSetConfig dataSet = eventDataSet("message", "severity");

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(50))
                            .messageSettings(
                                UadpWriterGroupSettings.builder()
                                    .networkMessageContentMask(
                                        UadpNetworkMessageContentMask.of(
                                            UadpNetworkMessageContentMask.Field.PublisherId,
                                            UadpNetworkMessageContentMask.Field.GroupHeader,
                                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                                            UadpNetworkMessageContentMask.Field.SequenceNumber,
                                            UadpNetworkMessageContentMask.Field.PayloadHeader))
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(
                                        UadpDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(
                                                UadpDataSetMessageContentMask.of(
                                                    UadpDataSetMessageContentMask.Field
                                                        .SequenceNumber))
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("evt-ds")
            .field("message", NodeIds.String, MESSAGE_FIELD_ID)
            .field("severity", NodeIds.UInt16, SEVERITY_FIELD_ID)
            .build();

    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("sub-conn")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .publisherId(PUBLISHER_ID)
                                    .writerGroupId(ushort(1))
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService publisher = track(PubSubService.create(publisherConfig));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    publisher.publishEvent(
        dataSet.ref(), List.of(Variant.ofString("boom"), Variant.ofUInt16(ushort(700))));

    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(DataSetMessageKind.EVENT, event.kind());
    assertEquals("boom", event.fieldsByName().get("message").value().value());
    assertEquals(ushort(700), event.fieldsByName().get("severity").value().value());
  }

  // endregion

  // region fixtures

  private static PublishedDataSetConfig eventDataSet(String... fieldNames) {
    PublishedEventsConfig.Builder events =
        PublishedEventsConfig.builder(ExpandedNodeId.of("urn:test:notifier", "Notifier"));
    for (String fieldName : fieldNames) {
      events.field(
          EventFieldDefinition.builder(fieldName).selectedField(select(fieldName)).build());
    }
    return PublishedDataSetConfig.builder("evt-ds").source(events.build()).build();
  }

  /** A select clause for the BaseEventType field named {@code browseName}. */
  private static SimpleAttributeOperand select(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  private static PublishedDataSetConfig dataItemsDataSet() {
    return PublishedDataSetConfig.builder("data-ds")
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

  /** Field 0 changes on every read (the delta driver); field 1 is constant. */
  private static PublishedDataSetSource countingSource(AtomicInteger counter) {
    return context ->
        DataSetSnapshot.builder(context)
            .field("counter", new DataValue(Variant.ofInt32(counter.incrementAndGet())))
            .field("constant", new DataValue(Variant.ofInt32(100)))
            .build();
  }

  /** An event-only writer group (writer id 1) at the standard interval. */
  private static WriterGroupConfig eventGroup(
      @Nullable Duration keepAliveTime,
      UInteger maxNetworkMessageSize,
      @Nullable MessageSecurityConfig security) {

    WriterGroupConfig.Builder group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(PUBLISHING_INTERVAL)
            .maxNetworkMessageSize(maxNetworkMessageSize)
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    // 0x41: PublisherId | PayloadHeader, so frames carry the writer id
                    .networkMessageContentMask(new UadpNetworkMessageContentMask(uint(0x41)))
                    .build())
            .dataSetWriter(eventWriter("W1", ushort(1), EVENT_REF));
    if (keepAliveTime != null) {
      group.keepAliveTime(keepAliveTime);
    }
    if (security != null) {
      group.messageSecurity(security);
    }
    return group.build();
  }

  private static DataSetWriterConfig eventWriter(String name, UShort id, PublishedDataSetRef ref) {
    return DataSetWriterConfig.builder(name)
        .dataSet(ref)
        .dataSetWriterId(id)
        .settings(
            UadpDataSetWriterSettings.builder()
                // 0x20: SequenceNumber
                .dataSetMessageContentMask(new UadpDataSetMessageContentMask(uint(0x20)))
                .build())
        .build();
  }

  private static UdpConnectionConfig connection(
      WriterGroupConfig group, UdpDatagramAddress discoveryAddress) {

    return UdpConnectionConfig.builder("conn")
        .publisherId(PUBLISHER_ID)
        .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
        // a connection with writer groups opens real UDP discovery sockets: keep them on a unique
        // loopback multicast group, never the 224.0.2.14:4840 default
        .discoveryAddress(discoveryAddress)
        .writerGroup(group)
        .build();
  }

  /** Start an event-only stub publisher from a group + event dataset. */
  private StubTransport startEventPublisher(
      WriterGroupConfig group,
      PublishedDataSetConfig eventDataSet,
      UdpDatagramAddress discoveryAddress)
      throws Exception {

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet)
            .connection(connection(group, discoveryAddress))
            .build();

    return startEventPublisher(config, PubSubBindings.builder().build());
  }

  private StubTransport startEventPublisher(PubSubConfig config, PubSubBindings bindings)
      throws Exception {

    var transport = new StubTransport();
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(transport).build();

    service = PubSubService.create(config, bindings, serviceConfig);
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return transport;
  }

  /**
   * Start a stub publisher whose one group carries a cyclic data writer (id 1, keyFrameCount 3, a
   * counting source) alongside an event writer (id 2).
   */
  private StubTransport startMixedPublisher(
      UInteger maxNetworkMessageSize,
      UdpDatagramAddress discoveryAddress,
      AtomicInteger counter,
      String... eventFields)
      throws Exception {

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(PUBLISHING_INTERVAL)
            .maxNetworkMessageSize(maxNetworkMessageSize)
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(new UadpNetworkMessageContentMask(uint(0x41)))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("Wdata")
                    .dataSet(DATA_REF)
                    .dataSetWriterId(ushort(1))
                    .keyFrameCount(uint(3))
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(
                                new UadpDataSetMessageContentMask(uint(0x20)))
                            .build())
                    .build())
            .dataSetWriter(eventWriter("Wevent", ushort(2), EVENT_REF))
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataItemsDataSet())
            .publishedDataSet(eventDataSet(eventFields))
            .connection(connection(group, discoveryAddress))
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder().source(DATA_REF, countingSource(counter)).build();

    return startEventPublisher(config, bindings);
  }

  private static UdpDatagramAddress multicast(String group, int port) {
    return UdpDatagramAddress.multicast(group, port).networkInterface("127.0.0.1");
  }

  private static ByteString keyData(PubSubSecurityPolicy policy) {
    byte[] bytes = new byte[policy.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (i + 1);
    }
    return ByteString.of(bytes);
  }

  /** A key provider whose fetch is held pending until {@link #open()} is called. */
  private static final class GatedKeyProvider implements SecurityKeyProvider {

    private final SecurityKeyProvider delegate;
    private final CompletableFuture<Void> gate = new CompletableFuture<>();

    GatedKeyProvider(SecurityKeyProvider delegate) {
      this.delegate = delegate;
    }

    void open() {
      gate.complete(null);
    }

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      return gate.thenCompose(
          v -> delegate.getKeys(securityGroupId, startingTokenId, requestedKeyCount));
    }
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    tracked.add(service);
    return service;
  }

  private DecodedDataSetMessage nextFrame(StubTransport transport) throws Exception {
    byte[] frame = transport.sent.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(frame, "no frame published within the deadline");

    List<DecodedDataSetMessage> messages = decode(frame).messages();
    assertEquals(1, messages.size(), "one DataSetMessage per NetworkMessage");
    return messages.get(0);
  }

  /** Poll frames until one message matches, discarding the rest. */
  private DecodedDataSetMessage awaitFrame(
      StubTransport transport, Predicate<DecodedDataSetMessage> predicate) throws Exception {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return fail("timed out waiting for a matching frame");
      }
      byte[] frame = transport.sent.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (frame == null) {
        continue;
      }
      for (DecodedDataSetMessage message : decode(frame).messages()) {
        if (predicate.test(message)) {
          return message;
        }
      }
    }
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

  /** The consecutive data frames follow the keyFrameCount-3 cadence K, D, D, K, D, D, ... */
  private static void assertDataCadence(List<DecodedDataSetMessage> dataFrames) {
    for (int i = 0; i < dataFrames.size(); i++) {
      DataSetMessageKind expected =
          i % 3 == 0 ? DataSetMessageKind.KEY_FRAME : DataSetMessageKind.DELTA_FRAME;
      assertEquals(expected, dataFrames.get(i).kind(), "data frame " + i);
    }
  }

  private static void awaitTrue(BooleanSupplier condition, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(10);
    }
  }

  private static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events, Predicate<DataSetReceivedEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return fail("timed out waiting for a matching DataSetReceivedEvent");
      }
      DataSetReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
