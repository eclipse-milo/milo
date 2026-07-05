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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Event-mode {@link DataSetWriterRuntime} buffer mechanics (Part 14 §6.2.6.2): the bounded event
 * queue, overflow-marker synthesis and ordering, the per-cycle drain budget, the drain-side arity
 * guard, restart preservation, and the non-draining diagnostics reads.
 *
 * <p>These tests drive the writer runtime's producer/consumer seam directly ({@link
 * DataSetWriterRuntime#offerEvent}/{@code createEventDrafts}) on the test thread rather than
 * through a running publish scheduler: the service is created but never started, so no group
 * activates and no publish cycle races the queue. Every assertion is therefore a deterministic
 * statement about buffered CONTENT and sequence — the drained {@link DataSetMessageDraft} list, its
 * FIFO order, the synthesized overflow marker, the consumed §7.2.3 sequence numbers — with nothing
 * depending on wall-clock timing. The runtime objects are reached via the same package-private
 * registry the service itself uses.
 *
 * <p>Restart preservation drives the exact snapshot/seed seam a path-stable reconfigure restart
 * uses, at both leaf scope ({@code snapshotWriterSequenceState}/{@code seedWriterSequenceState})
 * and group scope ({@code snapshotSequenceState}/{@code seedSequenceState}), and drains the seeded
 * successor to observe the preserved events. {@link OverflowEventFields} is unit-tested standalone.
 */
class EventQueueTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  /** A non-overflow EventType NodeId, distinct from EventQueueOverflowEventType (i=3035). */
  private static final NodeId NORMAL_EVENT_TYPE = NodeIds.BaseEventType; // i=2041

  /** The dataset event fields, in wire order: index 0 is EventType, index 1 is Severity. */
  private static final List<String> FIELD_NAMES =
      List.of("EventType", "Severity", "SourceName", "Message", "EventId", "Time");

  private @Nullable PubSubService service;

  @AfterEach
  void closeService() {
    if (service != null) {
      try {
        service.close();
      } catch (RuntimeException ignored) {
        // best-effort teardown; the tests themselves report real failures
      }
      service = null;
    }
  }

  // region overflow

  /**
   * Filling the queue past {@code eventQueueCapacity} synthesizes exactly one {@code
   * EventQueueOverflowEventType} marker, enqueued as the buffer's LAST entry at overflow time (Part
   * 14 §6.2.6.2); the capacity worth of admitted events precede it in FIFO order, every excess
   * event is discarded, and the overflow diagnostics error is edge-triggered (one per episode
   * regardless of how many events were dropped).
   */
  @Test
  void overflowMarkerIsLastEntryExcessDiscardedAndErrorIsEdgeTriggered() {
    var errors = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    DataSetWriterRuntime writer =
        writerRuntime(config(4), StatusCodes.Bad_ResourceUnavailable, errors);

    // capacity 4: the first four events are admitted (FIFO)
    for (int sev = 1; sev <= 4; sev++) {
      writer.offerEvent(normalEvent(sev));
    }
    // ten excess events: the first drop synthesizes the marker, the other nine are silent
    for (int sev = 5; sev <= 14; sev++) {
      writer.offerEvent(normalEvent(sev));
    }

    List<DataSetMessageDraft> drafts = writer.createEventDrafts(DateTime.now());

    assertEquals(5, drafts.size(), "four admitted events plus one overflow marker");
    drafts.forEach(d -> assertEquals(DataSetMessageKind.EVENT, d.kind()));

    // the four admitted events come out first, in FIFO order; the excess (5..14) is gone
    assertEquals(List.of(1, 2, 3, 4), severities(drafts.subList(0, 4)));
    assertTrue(drafts.subList(0, 4).stream().noneMatch(EventQueueTest::isOverflowMarker));

    // the marker is the LAST buffered entry
    assertTrue(isOverflowMarker(drafts.get(4)), "overflow marker is the last drained entry");
    assertEquals(1, drafts.stream().filter(EventQueueTest::isOverflowMarker).count());

    // the overflow error fired exactly once for the episode despite ten dropped events
    PubSubDiagnosticsEvent error = poll(errors);
    assertNotNull(error, "an overflow diagnostics error was recorded");
    assertEquals("conn/WG/W1", error.path());
    assertTrue(error.message().contains("overflow"), "message: " + error.message());
    assertNull(
        pollBriefly(errors), "the overflow error is edge-triggered: exactly one per episode");
  }

  /**
   * The overflow latch clears when the drain dequeues the marker, so events buffered AFTER an
   * overflow episode follow the marker (never merge into it) and a fresh overflow gets its own
   * marker — one marker per episode across two consecutive episodes.
   */
  @Test
  void overflowLatchResetsSoNextEpisodeGetsItsOwnMarker() {
    DataSetWriterRuntime writer = writerRuntime(config(4));

    // episode 1: fill to capacity, then overflow
    for (int sev = 1; sev <= 4; sev++) {
      writer.offerEvent(normalEvent(sev));
    }
    for (int sev = 5; sev <= 7; sev++) {
      writer.offerEvent(normalEvent(sev));
    }

    List<DataSetMessageDraft> firstEpisode = writer.createEventDrafts(DateTime.now());
    assertEquals(List.of(1, 2, 3, 4), severities(firstEpisode.subList(0, 4)));
    assertTrue(isOverflowMarker(firstEpisode.get(4)));
    assertEquals(1, firstEpisode.stream().filter(EventQueueTest::isOverflowMarker).count());

    // events pushed after the episode drained follow it: a fresh, empty buffer with the latch reset
    for (int sev = 8; sev <= 11; sev++) {
      writer.offerEvent(normalEvent(sev));
    }
    writer.offerEvent(normalEvent(12)); // overflows again -> a second, distinct marker

    List<DataSetMessageDraft> secondEpisode = writer.createEventDrafts(DateTime.now());
    assertEquals(List.of(8, 9, 10, 11), severities(secondEpisode.subList(0, 4)));
    assertTrue(isOverflowMarker(secondEpisode.get(4)), "the reset latch produced a second marker");
    assertEquals(1, secondEpisode.stream().filter(EventQueueTest::isOverflowMarker).count());
  }

  /**
   * {@link OverflowEventFields} micro-selects the standard BaseEventType fields against the
   * dataset's operands: the synthesized values match {@code MonitoredEventItem} (EventType i=3035,
   * SourceNode Server, SourceName "Server", Severity 0, a non-null 16-byte EventId, a real Time); a
   * field the overflow event does not populate yields {@link Variant#NULL_VALUE}; and an operand
   * whose {@code typeDefinitionId} is {@link NodeId#NULL_VALUE} is rejected (also NULL).
   */
  @Test
  void overflowFieldsMatchStandardBaseEventTypeSelectClauses() {
    List<EventFieldDefinition> fields =
        List.of(
            selectField("f0", "EventType"),
            selectField("f1", "SourceNode"),
            selectField("f2", "SourceName"),
            selectField("f3", "Severity"),
            selectField("f4", "Message"),
            selectField("f5", "EventId"),
            selectField("f6", "Time"),
            // a BaseEventType field the overflow event does not populate -> NULL_VALUE
            selectField("f7", "LocalTime"),
            // a NULL_VALUE typeDefinitionId is rejected by the micro-select -> NULL_VALUE
            field(
                "f8",
                new SimpleAttributeOperand(
                    NodeId.NULL_VALUE,
                    new QualifiedName[] {new QualifiedName(0, "Severity")},
                    AttributeId.Value.uid(),
                    null)));

    List<DataValue> values = OverflowEventFields.forFields(fields);
    assertEquals(fields.size(), values.size(), "one value per configured field, in wire order");

    assertEquals(NodeIds.EventQueueOverflowEventType, inner(values.get(0)));
    assertEquals(NodeIds.Server, inner(values.get(1)));
    assertEquals("Server", inner(values.get(2)));
    assertEquals(ushort(0), inner(values.get(3)));

    Object message = inner(values.get(4));
    assertInstanceOf(LocalizedText.class, message);
    assertEquals("Event queue overflow", ((LocalizedText) message).text());

    Object eventId = inner(values.get(5));
    assertInstanceOf(ByteString.class, eventId);
    assertEquals(16, ((ByteString) eventId).length());

    Object time = inner(values.get(6));
    assertInstanceOf(DateTime.class, time);
    assertNotEquals(DateTime.NULL_VALUE, time);

    assertEquals(Variant.NULL_VALUE, values.get(7).value(), "an unpopulated field is NULL");
    assertEquals(
        Variant.NULL_VALUE, values.get(8).value(), "a NULL_VALUE typeDefinitionId is NULL");
  }

  // endregion

  // region drain budget and arity guard

  /**
   * More than the 256-event per-cycle drain budget drains across multiple cycles: each cycle yields
   * at most 256 drafts and the events emerge in strict FIFO order across the cycle boundary, with
   * the remainder carried to the next cycle.
   */
  @Test
  void drainBudgetCapsEachCycleAndPreservesFifoAcrossCycles() {
    DataSetWriterRuntime writer = writerRuntime(config(600));

    for (int sev = 0; sev < 300; sev++) {
      writer.offerEvent(normalEvent(sev));
    }

    List<DataSetMessageDraft> cycle1 = writer.createEventDrafts(DateTime.now());
    assertEquals(256, cycle1.size(), "the first cycle drains the 256-event budget");

    List<DataSetMessageDraft> cycle2 = writer.createEventDrafts(DateTime.now());
    assertEquals(44, cycle2.size(), "the remaining 44 events drain on the next cycle");

    assertTrue(writer.createEventDrafts(DateTime.now()).isEmpty(), "the buffer is now empty");

    var drained = new ArrayList<DataSetMessageDraft>(cycle1);
    drained.addAll(cycle2);
    var expected = new ArrayList<Integer>();
    for (int sev = 0; sev < 300; sev++) {
      expected.add(sev);
    }
    assertEquals(expected, severities(drained), "FIFO order preserved across the cycle boundary");
  }

  /**
   * An event whose field count disagrees with the writer's metadata is skipped by the drain-side
   * arity guard with a {@code sourceError} and consumes NO §7.2.3 sequence number; the valid events
   * around it drain normally with consecutive sequence numbers.
   */
  @Test
  void mismatchedEventIsSkippedWithSourceErrorAndConsumesNoSequenceNumber() {
    var errors = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    DataSetWriterRuntime writer = writerRuntime(config(100), StatusCodes.Bad_InternalError, errors);

    assertEquals(0, sequenceOf(writer), "fresh writer starts its counter at 0");

    // a two-field event does not match the six-field metadata: offered directly so it bypasses the
    // best-effort arity check in PubSubServiceImpl#publishEvent (the swap-window / seed backstop)
    QueuedEvent mismatched =
        QueuedEvent.of(
            List.of(new DataValue(Variant.ofInt32(1)), new DataValue(Variant.ofInt32(2))),
            DateTime.now());

    writer.offerEvent(normalEvent(10));
    writer.offerEvent(mismatched);
    writer.offerEvent(normalEvent(11));

    List<DataSetMessageDraft> drafts = writer.createEventDrafts(DateTime.now());

    assertEquals(2, drafts.size(), "the mismatched event is dropped, the two valid events remain");
    assertEquals(List.of(10, 11), severities(drafts));
    // the mismatched event consumed no sequence number: 0 and 1, then next is 2
    assertEquals(0, drafts.get(0).sequenceNumber().intValue());
    assertEquals(1, drafts.get(1).sequenceNumber().intValue());
    assertEquals(2, sequenceOf(writer));

    PubSubDiagnosticsEvent error = poll(errors);
    assertNotNull(error, "the arity mismatch was recorded as a source error");
    assertEquals("conn/WG/W1", error.path());
    assertTrue(error.message().contains("expected 6"), "message: " + error.message());
  }

  // endregion

  // region non-draining diagnostics reads

  /**
   * The public {@code nextDataSetMessageSequenceNumber} diagnostics read (and the underlying {@code
   * sequenceSnapshot()}) is a cheap non-draining read: querying it repeatedly leaves every buffered
   * event in place, and it reflects the counter without advancing it.
   */
  @Test
  void sequenceDiagnosticsReadDoesNotDrainTheQueue() {
    DataSetWriterRuntime writer = writerRuntime(config(100));
    PubSubHandle handle = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    writer.offerEvent(normalEvent(1));
    writer.offerEvent(normalEvent(2));
    writer.offerEvent(normalEvent(3));

    // repeated reads never consume the counter and never drain the buffer
    for (int i = 0; i < 3; i++) {
      assertEquals(0, service.nextDataSetMessageSequenceNumber(handle).intValue());
      assertEquals(0L, writer.sequenceSnapshot().nextValue());
    }

    List<DataSetMessageDraft> drafts = writer.createEventDrafts(DateTime.now());
    assertEquals(List.of(1, 2, 3), severities(drafts), "all events survived the diagnostics reads");

    assertEquals(
        3, service.nextDataSetMessageSequenceNumber(handle).intValue(), "then it advances");
  }

  // endregion

  // region restart preservation

  /**
   * Buffered events survive a path-stable LEAF-level restart (the writer's config changed): the
   * disposed predecessor's buffer is snapshotted and seeded into the successor via the exact seam
   * the reconfigure leaf-CHANGED path uses, and the successor drains the preserved events in FIFO
   * order.
   */
  @Test
  void bufferedEventsSurviveLeafLevelRestart() {
    create(config(100));
    PubSubServiceImpl impl = (PubSubServiceImpl) requireNonNull(service);
    WriterGroupRuntime group = groupRuntime("conn", "WG");
    DataSetWriterRuntime predecessor = requireNonNull(group.findWriterRuntime("W1"));

    predecessor.offerEvent(normalEvent(10));
    predecessor.offerEvent(normalEvent(20));
    predecessor.offerEvent(normalEvent(30));

    // snapshot drains the (already removed, about-to-be-discarded) predecessor; seed re-enqueues
    var state = group.snapshotWriterSequenceState(predecessor);
    var successor = new DataSetWriterRuntime(impl, group, predecessor.config());
    group.seedWriterSequenceState(successor, state);

    assertNotSame(predecessor, successor, "a fresh runtime received the preserved buffer");
    assertEquals(
        List.of(10, 20, 30),
        severities(successor.createEventDrafts(DateTime.now())),
        "buffered events preserved across the leaf restart");
  }

  /**
   * Buffered events also survive a path-stable GROUP-level restart (the whole group is rebuilt):
   * the group snapshot preserves each writer's buffer keyed by path and the rebuilt writer drains
   * the preserved events in FIFO order — the group-scope counterpart of the leaf seam.
   */
  @Test
  void bufferedEventsSurviveGroupLevelRestart() {
    create(config(100));
    PubSubServiceImpl impl = (PubSubServiceImpl) requireNonNull(service);
    WriterGroupRuntime group = groupRuntime("conn", "WG");
    ConnectionRuntime connection = group.connectionRuntime();
    DataSetWriterRuntime predecessor = requireNonNull(group.findWriterRuntime("W1"));

    predecessor.offerEvent(normalEvent(40));
    predecessor.offerEvent(normalEvent(50));
    predecessor.offerEvent(normalEvent(60));

    // whole-group snapshot/seed: the successor group is rebuilt from the same config and each
    // writer's buffer transfers by path (conn/WG/W1)
    var groupState = group.snapshotSequenceState();
    var successorGroup = new WriterGroupRuntime(impl, connection, group.config());
    successorGroup.seedSequenceState(groupState);

    DataSetWriterRuntime successor = requireNonNull(successorGroup.findWriterRuntime("W1"));
    assertNotSame(predecessor, successor, "the group rebuild replaced the writer runtime");
    assertEquals(
        List.of(40, 50, 60),
        severities(successor.createEventDrafts(DateTime.now())),
        "buffered events preserved across the group restart");
  }

  // endregion

  // region concurrency

  /**
   * Under concurrent producers the bounded buffer holds at exactly {@code eventQueueCapacity}
   * admitted events plus the single overflow marker (capacity + 1): hammering {@link
   * DataSetWriterRuntime#offerEvent} from several threads far past capacity and then draining
   * everything yields exactly {@code capacity} normal events and one marker, never more.
   */
  @Test
  void concurrentOffersHoldTheBufferAtCapacityPlusOneMarker() throws Exception {
    int capacity = 64;
    DataSetWriterRuntime writer = writerRuntime(config(capacity));

    int threads = 6;
    int perThread = 1000;
    var workers = new ArrayList<Thread>();
    for (int t = 0; t < threads; t++) {
      var worker =
          new Thread(
              () -> {
                for (int i = 0; i < perThread; i++) {
                  writer.offerEvent(normalEvent(1));
                }
              });
      workers.add(worker);
      worker.start();
    }
    for (Thread worker : workers) {
      worker.join(TIMEOUT.toMillis());
    }

    List<DataSetMessageDraft> drafts = drainAll(writer);

    long markers = drafts.stream().filter(EventQueueTest::isOverflowMarker).count();
    long normal = drafts.size() - markers;
    assertEquals(capacity, normal, "exactly capacity admitted events under concurrent producers");
    assertEquals(1, markers, "exactly one overflow marker for the single episode");
  }

  // endregion

  // region fixtures and helpers

  private DataSetWriterRuntime writerRuntime(PubSubConfig config) {
    return writerRuntime(config, null, null);
  }

  private DataSetWriterRuntime writerRuntime(
      PubSubConfig config,
      @Nullable Long errorStatusFilter,
      @Nullable LinkedBlockingQueue<PubSubDiagnosticsEvent> errorSink) {

    create(config);
    if (errorSink != null) {
      long filter = requireNonNull(errorStatusFilter);
      service.addDiagnosticsListener(
          event -> {
            if (event.statusCode().value() == filter) {
              errorSink.add(event);
            }
          });
    }
    return (DataSetWriterRuntime) runtime("conn", "WG", "W1");
  }

  private WriterGroupRuntime groupRuntime(String connection, String group) {
    return (WriterGroupRuntime)
        runtimeHandle(connection + "/" + group, () -> handle(connection, group));
  }

  private AbstractComponentRuntime runtime(String connection, String group, String writer) {
    return runtimeHandle(
        connection + "/" + group + "/" + writer,
        () -> service.components().dataSetWriter(connection, group, writer).orElseThrow());
  }

  private AbstractComponentRuntime runtimeHandle(
      String description, java.util.function.Supplier<PubSubHandle> handleSupplier) {

    HandleRegistry registry = (HandleRegistry) service.components();
    PubSubHandle handle = handleSupplier.get();
    return requireNonNull(registry.get(handle), () -> "no runtime registered for " + description);
  }

  private PubSubHandle handle(String connection, String group) {
    return service.components().writerGroup(connection, group).orElseThrow();
  }

  /** Create (never start) the service for {@code config}, storing it for teardown. */
  private PubSubService create(PubSubConfig config) {
    PubSubService created = PubSubService.create(config, null, null);
    service = created;
    return created;
  }

  /** A UDP/UADP config: one event dataset, one positive-interval writer group, one event writer. */
  private static PubSubConfig config(int eventQueueCapacity) {
    return PubSubConfig.builder()
        .publishedDataSet(eventDataSet())
        .connection(
            UdpConnectionConfig.builder("conn")
                .publisherId(PublisherId.uint16(ushort(42)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 14841))
                .writerGroup(
                    WriterGroupConfig.builder("WG")
                        .writerGroupId(ushort(1))
                        // positive interval: the group is never event-triggered, and the service is
                        // never started, so no publish cycle races the direct queue drives below
                        .publishingInterval(Duration.ofSeconds(1))
                        .messageSettings(
                            UadpWriterGroupSettings.builder()
                                .networkMessageContentMask(
                                    new UadpNetworkMessageContentMask(uint(0x41)))
                                .build())
                        .dataSetWriter(
                            DataSetWriterConfig.builder("W1")
                                .dataSet(new PublishedDataSetRef("EventPDS"))
                                .dataSetWriterId(ushort(1))
                                .eventQueueCapacity(eventQueueCapacity)
                                .settings(
                                    UadpDataSetWriterSettings.builder()
                                        .dataSetMessageContentMask(
                                            new UadpDataSetMessageContentMask(uint(0x20)))
                                        .build())
                                .build())
                        .build())
                .build())
        .build();
  }

  private static PublishedDataSetConfig eventDataSet() {
    PublishedEventsConfig.Builder events = PublishedEventsConfig.builder(NodeIds.Server.expanded());
    for (String name : FIELD_NAMES) {
      events.field(selectField(name, name));
    }
    return PublishedDataSetConfig.builder("EventPDS").source(events.build()).build();
  }

  /** An {@link EventFieldDefinition} selecting the {@code Value} of a BaseEventType field. */
  private static EventFieldDefinition selectField(String fieldName, String browseName) {
    return field(
        fieldName,
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, browseName)},
            AttributeId.Value.uid(),
            null));
  }

  private static EventFieldDefinition field(String fieldName, SimpleAttributeOperand operand) {
    return EventFieldDefinition.builder(fieldName)
        .selectedField(operand)
        .dataSetFieldId(new UUID(0L, fieldName.hashCode()))
        .build();
  }

  /**
   * A normal (non-marker) event carrying six fields in wire order: index 0 is a non-overflow
   * EventType NodeId, index 1 is {@code severity} (the FIFO/identity sentinel used by assertions).
   */
  private static QueuedEvent normalEvent(int severity) {
    List<DataValue> fields =
        List.of(
            new DataValue(Variant.ofNodeId(NORMAL_EVENT_TYPE)),
            new DataValue(Variant.ofUInt16(ushort(severity))),
            new DataValue(Variant.ofString("src")),
            new DataValue(Variant.ofLocalizedText(LocalizedText.english("m"))),
            new DataValue(Variant.ofByteString(ByteString.of(new byte[] {0x0A}))),
            new DataValue(Variant.ofDateTime(DateTime.now())));
    return QueuedEvent.of(fields, DateTime.now());
  }

  private static boolean isOverflowMarker(DataSetMessageDraft draft) {
    return NodeIds.EventQueueOverflowEventType.equals(inner(draft.fields().get(0)));
  }

  private static List<Integer> severities(List<DataSetMessageDraft> drafts) {
    var out = new ArrayList<Integer>(drafts.size());
    for (DataSetMessageDraft draft : drafts) {
      out.add(((UShort) inner(draft.fields().get(1))).intValue());
    }
    return out;
  }

  private static @Nullable Object inner(DataValue value) {
    return value.value().value();
  }

  private static long sequenceOf(DataSetWriterRuntime writer) {
    return writer.sequenceSnapshot().nextValue();
  }

  private static List<DataSetMessageDraft> drainAll(DataSetWriterRuntime writer) {
    var all = new ArrayList<DataSetMessageDraft>();
    while (true) {
      List<DataSetMessageDraft> batch = writer.createEventDrafts(DateTime.now());
      if (batch.isEmpty()) {
        return all;
      }
      all.addAll(batch);
    }
  }

  private static @Nullable PubSubDiagnosticsEvent poll(
      LinkedBlockingQueue<PubSubDiagnosticsEvent> queue) {
    try {
      return queue.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  private static @Nullable PubSubDiagnosticsEvent pollBriefly(
      LinkedBlockingQueue<PubSubDiagnosticsEvent> queue) {
    try {
      return queue.poll(400, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  // endregion
}
