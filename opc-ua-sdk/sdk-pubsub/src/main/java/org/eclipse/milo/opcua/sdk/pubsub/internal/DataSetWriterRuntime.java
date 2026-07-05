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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.json.JsonContentMasks;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * Runtime for one DataSetWriter: pulls its source for a snapshot and produces at most one
 * DataSetMessage draft per publish cycle.
 *
 * <p>Source failures never cause a missed cycle: the draft is produced with every field set to
 * {@code Bad_InternalError} and the failure is recorded in diagnostics. (A source failure flips
 * every field's status, so the failing cycle and the recovery cycle each emit a key frame via the
 * all-fields-changed rule below.)
 *
 * <p>Key frame cadence (Part 14 §6.2.4.3): {@code keyFrameCount} is "the multiplier of the
 * PublishingInterval that defines the maximum number of times the PublishingInterval expires before
 * a key frame message with values for all published Variables is sent". A per-writer cycle counter
 * advances on every publishing-interval tick — including ticks that were suppressed or covered by a
 * keep-alive — and a key frame is emitted on the first cycle after writer start/group (re)activate
 * and whenever the counter reaches {@code keyFrameCount}; the cycles in between emit delta frames
 * of the changed fields. A {@code keyFrameCount} of 0 (the spec's non-cyclic/event value, which
 * Milo does not model) is clamped to 1 at runtime, so 0 and 1 both mean every message is a key
 * frame; configs round-trip unmodified.
 *
 * <p>Change detection (§5.3.3: a delta frame "only contains the subset that changed since the
 * previous DataSetMessage") diffs against the last-transmitted per-field values, comparing the
 * mask-projected wire representation of each field — the {@link DataSetFieldContentMask}-filtered
 * DataValue members, or the Table 34 Variant projection — rather than raw {@code DataValue.equals},
 * so e.g. a timestamp-only change counts only when timestamps are actually transmitted. A change of
 * the raw field StatusCode always counts, even when the status is not on the wire for the
 * configured masks.
 *
 * <p>If no fields changed on a delta cycle, nothing is sent (§6.2.4.3: "If no changes exist, the
 * delta frame DataSetMessage shall not be sent"): no sequence number is consumed and {@code
 * lastSentNanos} does not advance, so the group's keep-alive emission (§6.2.6.3) takes over. If
 * every field changed, a key frame is emitted instead and the cadence counter resets — a
 * full-coverage delta is strictly larger than the key frame under both mappings (§7.2.4.5.6: "The
 * Publisher shall send a key frame message if the delta frame message is larger than a key frame
 * message"). Partial deltas are not byte-compared against the key frame (that would require
 * double-encoding every cycle), so a delta of n-1 tiny changed fields may occasionally exceed the
 * key frame size; this residue is accepted.
 *
 * <p>If the configuration cannot express (or cannot safely carry) a delta frame — a JSON writer
 * without the DataSetMessageHeader or the MessageType member (Annex A.3.3.4/A.3.4.4), or a UADP
 * writer with a non-zero ConfiguredSize (the fixed-size layout is key-frame-only per Annex A.2.1.7,
 * and a DataSetMessage exceeding its ConfiguredSize is re-encoded header-only with the valid bit
 * clear per §6.3.1.3.3 yet still SENDS, so a delta lost that way would bypass the not-transmitted
 * baseline invalidation below and leave subscribers silently stale) — the writer degrades safely to
 * emitting a key frame every cycle, e.g. when such a writer is enabled after startup validation
 * ran. A ConfiguredSize key frame that overflows its fixed size is valid-cleared on the wire but
 * self-heals: the next cycle emits a full key frame again (the pre-delta posture). These
 * expressibility rules belong to the built-in mappings and are not applied when a custom provider
 * is resolved for the group's mapping name (see {@link #resolveDeltaCapable}).
 *
 * <p>The baseline tracks the last TRANSMITTED values (§5.3.3), not merely the last drafted ones:
 * when {@code WriterGroupRuntime#publishPartition} fails to put a NetworkMessage on the wire
 * (encode failure, oversize skip, send failure), it calls {@link #invalidateDeltaBaseline()} for
 * the writers whose data DataSetMessages the message carried, and the next data draft is a key
 * frame — otherwise the next delta would diff against values the subscriber never received and a
 * field changed only in the dropped cycle would stay silently stale until the next cadence key
 * frame. The dropped message's consumed sequence number is not rolled back: the resulting gap is
 * wire-indistinguishable from network loss, which subscribers must already tolerate (§7.2.3). Two
 * bounded residues remain: an asynchronous send-failure callback may land only after the next cycle
 * drafted (and possibly transmitted) one more delta against the untransmitted baseline, healed by
 * the key frame one cycle later; and a key frame that itself exceeds the group's
 * maxNetworkMessageSize is dropped and re-forced every cycle, so such a misconfigured writer
 * transmits no data (an honest failure with per-cycle diagnostics) instead of emitting deltas
 * against a baseline that can never be transmitted — though it still emits keep-alives when the
 * group has a keepAliveTime, because {@code lastSentNanos} only advances when a message is actually
 * handed to the transport channel (see {@link #lastSentNanos()} and {@code
 * WriterGroupRuntime#publishPartition}).
 *
 * <p>The DataSetMessage sequence counter (§7.2.3) wraps at the wire DataType maximum of the group's
 * mapping — 2^16 for UADP's UInt16, 2^32 for JSON's UInt32 and for custom mappings (see {@link
 * DataSetMessageSequenceCounter}). The counter is instance state, but path-stable reconfigure
 * restarts reseed the replacement runtime from a quiesced snapshot of the replaced one ({@link
 * #sequenceSnapshot()}/{@link #seedSequenceNumber(long)}), so the wire stream continues seamlessly
 * — unless the restart changes the mapping's counter width (16 ↔ 32), which restarts the sequence
 * at 0. A writer removed and re-added by later reconfigurations also restarts at 0; subscribers
 * recover from that backward jump by whichever comes first: the recreated writer's first
 * keep-alive, whose carried next-expected value reseeds the subscriber's window in both timeout
 * modes (§7.2.4.5.8 — the publisher is authoritative about its own counter; requires the group to
 * emit keep-alives), the §7.2.3 two-times-receive-timeout record discard when a
 * messageReceiveTimeout is configured, or the 16-consecutive-rejection restart heuristic of {@link
 * ReaderSequenceTracker} when it is not.
 *
 * <p>Draft creation, the DataSetMessage sequence counter, the cadence counter, and the delta
 * baseline are confined to the owning group's publish task thread; the two cross-thread inputs are
 * the baseline-invalidated flag (written from transport callbacks) and the frame-state reset
 * generation (written from {@link WriterGroupRuntime#activate()} on the engine thread), both
 * volatile and both consumed at the start of each data draft. The reset in particular must NOT be
 * applied on the engine thread: a stale publish cycle that passed its activation-generation check
 * before the (re)activation may still be drafting, and would otherwise observe half-reset state and
 * emit a delta against the wrong baseline.
 *
 * <p><b>Event mode.</b> A writer whose PublishedDataSet is a {@link PublishedEventsConfig} source
 * runs in event mode instead: it produces zero-to-many {@code EVENT} DataSetMessages per cycle
 * (Part 14 §6.2.6.2), not one snapshot-pulled data DataSetMessage. Events are pushed from arbitrary
 * threads into a bounded lock-free buffer ({@link #offerEvent}, a {@link ConcurrentLinkedQueue}
 * bounded by an {@link AtomicInteger} against {@code eventQueueCapacity}) and drained on the
 * publish task thread AFTER the group's per-cycle security gate ({@link #createEventDrafts}),
 * mirroring the baseline-invalidated handoff seam: producers take neither the engine lock nor the
 * group publish lock. On overflow the first dropped event of an episode enqueues a synthesized
 * {@code EventQueueOverflowEventType} marker as the buffer's last entry (§6.2.6.2) and records one
 * edge-triggered diagnostics error; the drain clears the latch when it dequeues the marker. Event
 * mode has no delta baseline, no cadence counter, no read context, and ignores {@code
 * keyFrameCount}; it still owns a §7.2.3 DataSetMessage sequence counter, consumed by {@link
 * #createEventDrafts} on the publish thread (one {@link DataSetMessageSequenceCounter#next()} per
 * transmitted event, none for events skipped by the drain-side arity guard), so the keep-alive
 * {@link DataSetMessageSequenceCounter#peek()} contract holds unchanged. Buffered events, the
 * overflow latch, and the sequence counter survive path-stable reconfigure restarts via {@link
 * #snapshotRestartState()}/{@link #seedEvents} taken under the group publish lock; {@link #dispose}
 * leaves the buffer intact (buffer lifetime = object lifetime) so a group/connection-level restart
 * can snapshot it after disposal.
 */
final class DataSetWriterRuntime extends AbstractComponentRuntime {

  /** Per-cycle event drain budget, bounding publish-thread work regardless of buffer capacity. */
  private static final int EVENT_DRAIN_LIMIT = 256;

  private final PubSubServiceImpl service;
  private final WriterGroupRuntime group;
  private final DataSetWriterConfig config;
  private final PublishedDataSetConfig dataSet;

  /** Data mode only; {@code null} in event mode (events carry no snapshot read context). */
  private final @Nullable PublishedDataSetReadContext readContext;

  private final ConfigurationVersionDataType configurationVersion;
  private final DataSetMetaDataType metaData;

  /** Whether this writer publishes events ({@link PublishedEventsConfig}) instead of data items. */
  private final boolean eventMode;

  /** Event mode only: the dataset's event field select clauses, for overflow-marker synthesis. */
  private final List<EventFieldDefinition> eventFields;

  /** Event mode only: the dataset's event field count, the drain-side arity guard target. */
  private final int eventFieldCount;

  /**
   * Event mode only: the bounded lock-free event buffer (Part 14 §6.2.6.2). Producers enqueue from
   * arbitrary threads via {@link #offerEvent}; the publish task drains via {@link
   * #createEventDrafts}. {@code null} in data mode.
   */
  private final @Nullable ConcurrentLinkedQueue<QueuedEvent> eventQueue;

  /**
   * Event mode only: the current buffer size, held exactly at or below {@code eventQueueCapacity}
   * (plus one admitted overflow marker) under concurrent producers via increment/decrement.
   */
  private final AtomicInteger eventQueueSize = new AtomicInteger(0);

  /** Event mode only: the configured buffer capacity (Part 14 §6.2.6.2). */
  private final int eventQueueCapacity;

  /**
   * Event mode only: the per-episode overflow latch. CAS false→true on the first drop of an episode
   * synthesizes and enqueues the overflow marker and records the diagnostics error exactly once;
   * the drain resets it to false when it dequeues the marker. Any thread.
   */
  private final AtomicBoolean overflowMarked = new AtomicBoolean(false);

  /** The effective key frame cadence: {@code keyFrameCount} clamped to at least 1. */
  private final long keyFrameCount;

  /** Whether the configuration can express (and safely carry) a delta frame on the wire. */
  private final boolean deltaCapable;

  /**
   * Wraps at the wire width of the group's mapping (§7.2.3 Table 152). Only touched by the owning
   * group's publish task.
   */
  private final DataSetMessageSequenceCounter sequenceCounter;

  /**
   * Publishing-interval ticks since the last key frame; suppressed and keep-alive cycles advance it
   * too (§6.2.4.3 counts interval expirations, not emitted messages). Only touched by the owning
   * group's publish task; group (re)activation resets it via the {@link #requestFrameStateReset()}
   * handoff.
   */
  private long cyclesSinceKeyFrame = 0;

  /**
   * The mask-projected wire values last transmitted per field, or {@code null} before the first key
   * frame; the §5.3.3 delta baseline. Only touched by the owning group's publish task.
   */
  private DataValue @Nullable [] baselineProjected;

  /** The raw per-field statuses last transmitted ("status transitions always count"). */
  private StatusCode @Nullable [] baselineStatuses;

  /**
   * Set by {@link #invalidateDeltaBaseline()} when a NetworkMessage carrying this writer's data
   * DataSetMessage was not transmitted, consumed (and cleared) by {@link #createDataDraft} on the
   * publish task thread. Volatile: send-failure callbacks write it from transport threads.
   */
  private volatile boolean baselineInvalidated = false;

  /**
   * Bumped by {@link #requestFrameStateReset()} on group (re)activation (engine thread, single
   * writer under the engine lock, so the non-atomic increment is safe); compared against {@link
   * #appliedResetGeneration} at the top of each data draft, which performs the actual reset on the
   * publish task thread.
   */
  private volatile long resetGeneration = 0;

  /**
   * The value of {@link #resetGeneration} whose reset has been applied. Only touched by the owning
   * group's publish task.
   */
  private long appliedResetGeneration = 0;

  private volatile long lastSentNanos = System.nanoTime();

  DataSetWriterRuntime(
      PubSubServiceImpl service, WriterGroupRuntime group, DataSetWriterConfig config) {

    super(
        ComponentType.DATA_SET_WRITER,
        group.path() + "/" + config.getName(),
        group,
        config.isEnabled());

    this.service = service;
    this.group = group;
    this.config = config;

    this.dataSet = service.requirePublishedDataSet(config.getDataSet());

    // event mode vs data mode is fixed at construction from the resolved source kind; a config
    // change to the source flows through reconfigure, which recreates this runtime
    if (dataSet.getSource() instanceof PublishedEventsConfig events) {
      this.eventMode = true;
      this.eventFields = events.getFields();
      this.eventFieldCount = events.getFields().size();
      this.readContext = null; // events are pushed, never snapshot-pulled
      this.eventQueue = new ConcurrentLinkedQueue<>();
      this.eventQueueCapacity = config.getEventQueueCapacity();
    } else {
      this.eventMode = false;
      this.eventFields = List.of();
      this.eventFieldCount = 0;
      this.readContext =
          new PublishedDataSetReadContext(config.getDataSet(), dataSet.getFields(), null);
      this.eventQueue = null;
      this.eventQueueCapacity = config.getEventQueueCapacity();
    }

    this.configurationVersion =
        new ConfigurationVersionDataType(
            dataSet.getConfigurationVersionMajor(), dataSet.getConfigurationVersionMinor());
    this.metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true);

    this.keyFrameCount = Math.max(1L, config.getKeyFrameCount().longValue());
    this.deltaCapable =
        resolveDeltaCapable(service.isBuiltinMapping(group.mappingName()), group.config(), config);

    // the §7.2.3 counter wraps at the wire DataType maximum of the group's mapping: UInt16 for
    // the UADP mapping; UInt32 for JSON and for custom mappings, mirroring the reader-side
    // window width choice in DataSetReaderRuntime
    int sequenceNumberBitWidth =
        PubSubServiceImpl.MAPPING_UADP.equals(group.mappingName()) ? 16 : 32;
    this.sequenceCounter = new DataSetMessageSequenceCounter(sequenceNumberBitWidth);
  }

  /**
   * Whether the configuration can express (and safely carry) a delta frame on the wire.
   *
   * <p>The rules below are properties of the BUILT-IN mappings, so they apply only when the group's
   * mapping name resolves to the built-in UADP or JSON provider. A custom {@link
   * org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider} — including one registered under
   * "uadp" or "json", shadowing the built-in — owns its wire format and must not be second-guessed
   * (the same posture {@code PubSubServiceImpl#deltaFrameConfigError} takes at validation): such a
   * writer is delta-capable, the draft model carries the frame kind, and the provider decides how
   * to express it.
   *
   * <p>JSON: requires the DataSetMessageHeader in the effective network message mask and the
   * MessageType member in the effective DataSetMessage mask (Part 14 Annex A.3.3.4/A.3.4.4: "If the
   * KeyFrameCount is not 1, the MessageType bit shall be true") — without them a delta payload is
   * indistinguishable from a key frame.
   *
   * <p>UADP: requires ConfiguredSize 0 (dynamic size). The fixed-size layout is key-frame-only
   * (Annex A.2.1.7: "Only data key frame DataSetMessages are supported", with KeyFrameCount fixed
   * to 1), and operationally a DataSetMessage exceeding its ConfiguredSize is re-encoded
   * header-only with the valid bit clear (§6.3.1.3.3) and still sends successfully — bypassing the
   * not-transmitted baseline invalidation — so a delta lost that way (Table 164 adds 2 bytes per
   * field over the key frame, so a busy partial delta can exceed a key-frame-sized budget) would
   * leave every subscriber silently stale against a baseline they never received. Every-cycle key
   * frames restore the self-healing posture: a valid-cleared key frame is simply superseded by the
   * next cycle's key frame. The message-type bits themselves are always expressible — DataSetFlags2
   * is part of the unconditional UADP DataSetMessage header (Table 158 defines no mask bit that
   * could disable it).
   *
   * <p>Writer settings classes a built-in mapping does not own (rejected per cycle by the mapping)
   * are likewise treated as capable.
   */
  private static boolean resolveDeltaCapable(
      boolean builtinMapping, WriterGroupConfig groupConfig, DataSetWriterConfig config) {

    if (!builtinMapping) {
      return true;
    }

    if (config.getSettings() instanceof UadpDataSetWriterSettings writerSettings) {
      return writerSettings.getConfiguredSize().intValue() == 0;
    }

    if (config.getSettings() instanceof JsonDataSetWriterSettings writerSettings) {
      if (!(groupConfig.getMessageSettings() instanceof JsonWriterGroupSettings groupSettings)) {
        // mixed settings are rejected per cycle by the mapping; never attempt deltas
        return false;
      }
      boolean dataSetMessageHeader =
          JsonContentMasks.effectiveNetworkMessageMask(groupSettings.getNetworkMessageContentMask())
              .getDataSetMessageHeader();
      boolean messageType =
          JsonContentMasks.effectiveDataSetMessageMask(
                  writerSettings.getDataSetMessageContentMask())
              .getMessageType();
      return dataSetMessageHeader && messageType;
    }

    return true;
  }

  DataSetWriterConfig config() {
    return config;
  }

  /** The group this writer belongs to; used by the service to fan out event-publish triggers. */
  WriterGroupRuntime group() {
    return group;
  }

  /** Whether this writer publishes events ({@link #createEventDrafts}) rather than data drafts. */
  boolean eventMode() {
    return eventMode;
  }

  /**
   * The resolved metadata of this writer's PublishedDataSet, the wire-bound surface with
   * Milo-internal field properties stripped: carried on every draft and published to broker
   * metadata queues.
   */
  DataSetMetaDataType metaData() {
    return metaData;
  }

  /**
   * The instant a NetworkMessage carrying one of this writer's DataSetMessages — data or keep-alive
   * — was last handed to the transport channel, the §6.2.6.3 keep-alive reference. Drafting alone
   * never advances it: a draft dropped at the size gate (or never encoded, or whose synchronous
   * send failed) does not count as "sent", so keep-alive emission stays reachable for a writer
   * whose data never reaches the wire.
   */
  long lastSentNanos() {
    return lastSentNanos;
  }

  /**
   * Set the keep-alive reference: called by {@link WriterGroupRuntime} on group activation (engine
   * thread — activation starts a fresh keep-alive period) and whenever a NetworkMessage carrying
   * one of this writer's DataSetMessages is handed to the transport channel (publish task thread).
   * The field is volatile; no further synchronization between the two writers is needed (group
   * activation and the publish task it schedules are ordered).
   */
  void resetLastSent(long nanos) {
    lastSentNanos = nanos;
  }

  /**
   * Request that the key frame cadence counter be reset and the delta baseline discarded before the
   * next data draft, forcing it to be a key frame. Called from {@link
   * WriterGroupRuntime#activate()} (engine thread, under the engine lock) so the first message
   * after a group (re)activation is always a key frame; writer start and reconfigure/metadata
   * changes are covered by runtime recreation (constructor state). Milo policy, stricter than Part
   * 14 requires: a Table 164 FieldIndex baseline is meaningless across metadata changes, and a
   * fresh subscriber completes startup only on a key frame or event (§6.2.1 Table 2).
   *
   * <p>The reset itself is deferred to the publish task thread: a publish cycle scheduled before
   * the (re)activation may still be executing (it passed its activation-generation check before the
   * bump), and resetting the publish-thread-confined frame state from the engine thread could hand
   * that cycle a half-reset baseline to diff against — a wrong-baseline delta. Instead this bumps a
   * volatile generation that {@link #createDataDraft} consumes at the top of the next draft, before
   * any frame decision; whichever cycle consumes it first (the stale one or the new task's) emits a
   * key frame, and cycles serialize on the group's publish lock, so the frame state never tears.
   * There is no deterministic unit seam for this interleaving (the runtime requires a full
   * service); the behavioral contract — first frame after a re-activation is a key frame — is
   * pinned by the cadence tests.
   */
  void requestFrameStateReset() {
    resetGeneration++;
  }

  /**
   * The DataSetMessage sequence state of this writer: the next value to consume and its wire width.
   * Exact only while the owning group's publish cycles are quiescent (the counter value is a
   * volatile read); {@code WriterGroupRuntime} snapshots under its publish lock.
   */
  WriterSequenceState sequenceSnapshot() {
    return new WriterSequenceState(sequenceCounter.currentValue(), sequenceCounter.bitWidth());
  }

  /** The wire width of this writer's sequence counter (16 or 32). */
  int sequenceBitWidth() {
    return sequenceCounter.bitWidth();
  }

  /**
   * Seed this writer's sequence counter with the next value to consume; only called before the
   * runtime's first publish cycle (restart preservation).
   */
  void seedSequenceNumber(long value) {
    sequenceCounter.seed(value);
  }

  /** A quiesced snapshot of one writer's DataSetMessage sequence state. */
  record WriterSequenceState(long nextValue, int bitWidth) {}

  /**
   * A quiesced restart snapshot of this writer: its DataSetMessage sequence state plus, in event
   * mode, a drained copy of its buffered events and its overflow latch (the counterpart of the
   * non-draining {@link #sequenceSnapshot()}, which feeds the lock-free diagnostics API and must
   * not disturb the buffer). Taken ONLY under the group publish lock ({@link WriterGroupRuntime}),
   * so no publish cycle drains concurrently; producers may still offer (lock-free), and events
   * racing this window may be lost across the swap (best-effort reconfigure semantics). Draining
   * the buffer here is safe because the snapshot is only taken on a runtime already removed from
   * the tree and about to be discarded.
   */
  WriterRestartState snapshotRestartState() {
    var events = new ArrayList<QueuedEvent>();
    if (eventQueue != null) {
      QueuedEvent event;
      while ((event = eventQueue.poll()) != null) {
        eventQueueSize.decrementAndGet();
        events.add(event);
      }
    }
    return new WriterRestartState(
        sequenceCounter.currentValue(),
        sequenceCounter.bitWidth(),
        List.copyOf(events),
        overflowMarked.get());
  }

  /**
   * Re-enqueue a predecessor's buffered events into this event-mode successor before its first
   * publish cycle (restart preservation); a data-mode successor drops them. Called under the group
   * publish lock. Independent of sequence-counter seeding, which a bit-width mismatch (mapping
   * switch) may skip: events transfer regardless.
   *
   * <p>The overflow latch is carried over only when a marker actually transferred with the events:
   * {@link #snapshotRestartState()} runs under the publish lock but {@link #offerEvent} is
   * lock-free, so a snapshot interleaving between the predecessor's {@code overflowMarked} CAS and
   * its marker enqueue would capture {@code overflowMarked == true} while the marker lands in the
   * discarded predecessor's buffer. Seeding the raw latch then would leave this successor latched
   * with no marker to reset it, permanently suppressing its next overflow marker and diagnostic.
   * Gating on a transferred marker resets the latch in that case so a fresh episode can synthesize
   * one.
   */
  void seedEvents(WriterRestartState state) {
    if (!eventMode || eventQueue == null) {
      return; // a data-mode successor drops buffered events
    }
    boolean markerTransferred = false;
    for (QueuedEvent event : state.bufferedEvents()) {
      eventQueue.add(event);
      eventQueueSize.incrementAndGet();
      markerTransferred |= event.overflowMarker();
    }
    overflowMarked.set(state.overflowMarked() && markerTransferred);
  }

  /**
   * A quiesced restart snapshot of one writer: its DataSetMessage sequence state, its buffered
   * events (empty in data mode), and its overflow latch.
   */
  record WriterRestartState(
      long nextValue, int bitWidth, List<QueuedEvent> bufferedEvents, boolean overflowMarked) {}

  /**
   * Reset the key frame cadence counter and discard the delta baseline, forcing the next data draft
   * to be a key frame. Publish task thread only; see {@link #requestFrameStateReset()} for the
   * activation handoff.
   */
  private void resetFrameState() {
    cyclesSinceKeyFrame = 0;
    baselineProjected = null;
    baselineStatuses = null;
    baselineInvalidated = false;
  }

  /**
   * Mark the delta baseline as not reflecting what subscribers received, forcing the next data
   * draft to be a key frame: called by {@code WriterGroupRuntime#publishPartition} when a
   * NetworkMessage carrying this writer's data DataSetMessage was not transmitted (encode failure,
   * oversize skip, send failure). The §5.3.3 baseline is the last TRANSMITTED per-field values;
   * without this, the next delta would diff against values no subscriber ever saw. Safe to call
   * from any thread (volatile flag); the publish task consumes it at the start of the next data
   * draft, so an invalidation arriving from an asynchronous send-failure callback takes effect at
   * the latest one cycle later.
   */
  void invalidateDeltaBaseline() {
    baselineInvalidated = true;
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return List.of();
  }

  /**
   * Drain this writer's buffered events when it enters Operational within an event-triggered
   * (PublishingInterval 0) group. An interval-0 group has no fixed-rate cycle, so events buffered
   * while this writer was PreOperational, Error, or Disabled — including events preserved across a
   * leaf-level path-stable restart and re-seeded into this runtime — would otherwise wait for the
   * next {@code publishEvent} or keep-alive tick. Triggering on the writer's OWN Operational entry
   * (rather than only the group's) covers leaf restarts and per-writer Error/Disabled recovery,
   * which do not transition the already-Operational group; {@link PubSubStateMachine} sets this
   * writer Operational before invoking this hook, so the coalesced cycle is guaranteed to observe
   * it Operational and drain it. Coalesced with the group-level trigger via {@code triggerPending};
   * a no-op for positive-interval groups (which drain on their next tick) and data-mode writers.
   */
  @Override
  void onEnterOperational() {
    if (eventMode && group.eventTriggered()) {
      group.triggerEventPublish();
    }
  }

  /**
   * Activate the writer: re-run the writer-level subset of the startup validation and publish
   * broker metadata. A throw is mapped by {@link PubSubStateMachine} to {@code PubSubState.Error}
   * with the exception's status code, leaving the group Operational.
   */
  @Override
  void activate() throws UaException {
    checkUnsupportedUadpFeatures();
    checkEventTriggeredGroupCompatibility();

    // broker connections publish this writer's (retained) metadata when the writer activates;
    // failures are recorded in diagnostics, never thrown: metadata publication is auxiliary
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterActivated(group, this);
    }
  }

  /**
   * Re-check at activation that a data-items writer is not enabled into an event-triggered
   * (PublishingInterval 0) writer group: such a group has no fixed-rate cycle to pull the writer's
   * snapshot, so the writer could never publish. Startup/reconfigure validation rejects the whole
   * group when any writer is data-items, but a writer disabled at that point and enabled after its
   * group activated is first caught here — it fails into {@code PubSubState.Error} with {@code
   * Bad_ConfigurationError} (mirroring {@link #checkUnsupportedUadpFeatures}). Event-mode writers
   * are the intended members of such groups and pass.
   */
  private void checkEventTriggeredGroupCompatibility() throws UaException {
    if (!eventMode && group.eventTriggered()) {
      var e =
          new UaException(
              StatusCodes.Bad_ConfigurationError,
              ("data-items DataSetWriter '%s' cannot belong to an event-triggered"
                      + " (PublishingInterval 0) writer group")
                  .formatted(path()));
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }
  }

  /**
   * Re-check the writer-level unsupported-UADP-feature rule ({@link
   * PubSubServiceImpl#unsupportedUadpFeatureError}) at activation: this writer's RawData
   * field-content-mask bit, applied only when the group's "uadp" mapping resolves to the built-in
   * provider — a custom provider shadowing "uadp" owns its wire format and is never second-guessed.
   * Startup/reconfigure validation and {@link WriterGroupRuntime#activate} only ever see enabled
   * writers, so a writer disabled at startup (tolerated, config round-trip posture) and enabled
   * after its group activated is first checked here — it fails into {@code PubSubState.Error} with
   * {@code Bad_NotSupported} instead of going Operational while the encoder backstop rejects every
   * publish cycle. The group-level PromotedFields bit is not re-checked here: a writer only
   * activates under an Operational group, and a PromotedFields group fails its own activation.
   */
  private void checkUnsupportedUadpFeatures() throws UaException {
    String error =
        service.unsupportedUadpFeatureError(group.config(), List.of(config), group.path());
    if (error != null) {
      var e = new UaException(StatusCodes.Bad_NotSupported, error);
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }
  }

  @Override
  void deactivate() {
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterDeactivated(this);
    }
  }

  /**
   * Release all resources of this runtime. The event buffer is intentionally left intact (buffer
   * lifetime = object lifetime): a group- or connection-level path-stable restart snapshots the
   * buffered events via {@link #snapshotRestartState()} AFTER disposal (the CHANGED apply disposes
   * the predecessor before snapshotting), so clearing here would lose them.
   */
  void dispose() {
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterDeactivated(this);
    }
  }

  /**
   * Create the data draft for this publish cycle from the current source snapshot: a key frame, a
   * delta frame of the changed fields, or {@code null} when this is a no-change delta cycle and
   * nothing is sent (Part 14 §6.2.4.3). A suppressed cycle consumes no sequence number, and {@code
   * lastSentNanos} is never advanced here — only when an encoded NetworkMessage carrying the draft
   * is actually handed to the transport channel ({@code WriterGroupRuntime#publishPartition}) — so
   * the keep-alive emission of §6.2.6.3 remains reachable for suppressed cycles and for drafts that
   * die before transmission (oversize skip, encode failure, synchronous send failure). Publish task
   * thread only.
   */
  @Nullable DataSetMessageDraft createDataDraft(DateTime timestamp) {
    long resetGeneration = this.resetGeneration;
    if (resetGeneration != appliedResetGeneration) {
      // a group (re)activation requested a frame-state reset; apply it here on the publish task
      // thread, before any frame decision, so an in-flight stale cycle can never observe (or
      // emit a delta against) half-reset state
      appliedResetGeneration = resetGeneration;
      resetFrameState();
    }

    if (baselineInvalidated) {
      // a NetworkMessage carrying this writer's previous data DataSetMessage was never
      // transmitted: the baseline no longer matches what subscribers received, so discard it
      // and emit a key frame (resetFrameState clears the flag)
      resetFrameState();
    }

    SnapshotRead read = readSnapshot();
    List<DataValue> values = read.values();
    int fieldCount = values.size();

    // §6.2.4.3 counts PublishingInterval expirations, so every cycle advances the cadence,
    // including cycles that end up suppressed or covered by a keep-alive
    cyclesSinceKeyFrame++;

    DataValue[] projected = new DataValue[fieldCount];
    for (int i = 0; i < fieldCount; i++) {
      projected[i] = projectFieldValue(values.get(i));
    }

    DataValue[] baselineProjected = this.baselineProjected;
    StatusCode[] baselineStatuses = this.baselineStatuses;

    boolean keyFrame =
        !deltaCapable
            || baselineProjected == null
            || baselineStatuses == null
            || baselineProjected.length != fieldCount
            || cyclesSinceKeyFrame >= keyFrameCount;

    List<DataSetMessageDraft.DeltaField> changed = List.of();
    if (!keyFrame) {
      changed = diffAgainstBaseline(values, projected, baselineProjected, baselineStatuses);

      if (changed.isEmpty()) {
        // §6.2.4.3: "If no changes exist, the delta frame DataSetMessage shall not be sent."
        return null;
      }
      if (changed.size() == fieldCount) {
        // §7.2.4.5.6: a full-coverage delta is strictly larger than the key frame
        keyFrame = true;
      }
    }

    // the baseline is the per-field state just transmitted: changed fields carry the new
    // projection, unchanged fields had an equal projection already
    this.baselineProjected = projected;
    var statuses = new StatusCode[fieldCount];
    for (int i = 0; i < fieldCount; i++) {
      statuses[i] = values.get(i).statusCode();
    }
    this.baselineStatuses = statuses;

    if (keyFrame) {
      cyclesSinceKeyFrame = 0;
      return DataSetMessageDraft.of(
          config,
          sequenceCounter.next(),
          includeTimestamp() ? timestamp : null,
          read.status(),
          configurationVersion,
          false,
          values,
          metaData);
    } else {
      return DataSetMessageDraft.ofDeltaFrame(
          config,
          sequenceCounter.next(),
          includeTimestamp() ? timestamp : null,
          read.status(),
          configurationVersion,
          changed,
          metaData);
    }
  }

  /**
   * The changed fields relative to the baseline: a field changed when its mask-projected wire value
   * differs, or when its raw status differs (status transitions always count, even when the masks
   * keep the status off the wire).
   */
  private static List<DataSetMessageDraft.DeltaField> diffAgainstBaseline(
      List<DataValue> values,
      DataValue[] projected,
      DataValue[] baselineProjected,
      StatusCode[] baselineStatuses) {

    var changed = new ArrayList<DataSetMessageDraft.DeltaField>();
    for (int i = 0; i < projected.length; i++) {
      if (!projected[i].equals(baselineProjected[i])
          || !values.get(i).statusCode().equals(baselineStatuses[i])) {
        changed.add(new DataSetMessageDraft.DeltaField(i, values.get(i)));
      }
    }
    return changed;
  }

  /**
   * Project a field value to its wire representation for change detection, mirroring the per-field
   * encoding both mappings apply: with the DataValue representation only the {@link
   * DataSetFieldContentMask}-selected members are transmitted (§6.2.4.2); with the Variant
   * representation the Table 34 status propagation applies — Bad transmits the status instead of
   * the value, Uncertain transmits value + status, Good transmits the bare value.
   */
  private DataValue projectFieldValue(DataValue value) {
    DataSetFieldContentMask mask = config.getFieldContentMask();

    boolean dataValueRepresentation =
        mask.getStatusCode()
            || mask.getSourceTimestamp()
            || mask.getServerTimestamp()
            || mask.getSourcePicoSeconds()
            || mask.getServerPicoSeconds();

    if (dataValueRepresentation) {
      boolean sourceTimestamp = mask.getSourceTimestamp();
      boolean serverTimestamp = mask.getServerTimestamp();

      return new DataValue(
          value.value(),
          mask.getStatusCode() ? value.statusCode() : StatusCode.GOOD,
          sourceTimestamp ? value.sourceTime() : null,
          sourceTimestamp && mask.getSourcePicoSeconds() ? value.sourcePicoseconds() : null,
          serverTimestamp ? value.serverTime() : null,
          serverTimestamp && mask.getServerPicoSeconds() ? value.serverPicoseconds() : null);
    } else {
      StatusCode status = value.statusCode();
      if (status.isBad()) {
        return new DataValue(Variant.ofStatusCode(status), StatusCode.GOOD, null, null, null, null);
      } else if (status.isUncertain()) {
        return new DataValue(value.value(), status, null, null, null, null);
      } else {
        return new DataValue(value.value(), StatusCode.GOOD, null, null, null, null);
      }
    }
  }

  /**
   * Create a keep-alive draft. Carries the next expected sequence number without incrementing the
   * counter, per Part 14 §7.2.3. Does not advance {@code lastSentNanos}: like data drafts, the
   * keep-alive counts as "sent" (§6.2.6.3) only when its NetworkMessage is handed to the transport
   * channel — a keep-alive lost at the size gate or to a synchronous send failure is simply drafted
   * again on the next cycle. Publish task thread only.
   */
  DataSetMessageDraft createKeepAliveDraft(DateTime timestamp) {
    return DataSetMessageDraft.of(
        config,
        sequenceCounter.peek(),
        includeTimestamp() ? timestamp : null,
        StatusCode.GOOD,
        configurationVersion,
        true,
        List.of(),
        metaData);
  }

  /**
   * Offer one event to this writer's bounded buffer (Part 14 §6.2.6.2). Called from arbitrary
   * producer threads ({@code PubSubServiceImpl#publishEvent}); takes neither the engine lock nor
   * the group publish lock, and never blocks — the producer/consumer handoff mirrors the
   * baseline-invalidated flag.
   *
   * <p>The capacity bound is exact under concurrent producers: {@code incrementAndGet() > capacity}
   * decrements back and drops. On the FIRST drop of an overflow episode ({@link #overflowMarked}
   * CAS false→true) a synthesized {@code EventQueueOverflowEventType} marker is enqueued as the
   * buffer's last entry — admitted beyond capacity, so the buffer may briefly hold {@code capacity
   * + 1} — and one edge-triggered diagnostics error is recorded; further drops in the same episode
   * are silent. The drain resets the latch when it dequeues the marker.
   */
  void offerEvent(QueuedEvent event) {
    ConcurrentLinkedQueue<QueuedEvent> queue = requireNonNull(eventQueue);

    if (eventQueueSize.incrementAndGet() > eventQueueCapacity) {
      // over capacity: this event does not fit
      eventQueueSize.decrementAndGet();

      if (overflowMarked.compareAndSet(false, true)) {
        // first drop of the episode: insert the overflow marker as the last entry, at overflow
        // time (§6.2.6.2), admitting it beyond capacity (the buffer may hold capacity + 1)
        eventQueueSize.incrementAndGet();
        queue.add(
            QueuedEvent.overflowMarker(OverflowEventFields.forFields(eventFields), DateTime.now()));
        service
            .getDiagnostics()
            .error(
                path(),
                new StatusCode(StatusCodes.Bad_ResourceUnavailable),
                "event queue overflow: capacity %d exceeded for DataSetWriter '%s'"
                    .formatted(eventQueueCapacity, path()),
                null,
                PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      }
      return;
    }

    queue.add(event);
  }

  /**
   * Drain up to {@link #EVENT_DRAIN_LIMIT} buffered events into {@code EVENT} DataSetMessage drafts
   * (Part 14 §6.2.6.2). Publish task thread only; called from the group publish loop AFTER the
   * per-cycle security gate, so a secured cycle skipped for want of keys leaves events buffered for
   * the next cycle rather than losing them.
   *
   * <p>Each drained entry decrements the buffer size. An overflow marker additionally resets the
   * overflow latch (a new episode may begin). An event whose field count disagrees with the
   * writer's metadata is skipped with a diagnostics source error and consumes NO sequence number —
   * the drain-side arity guard, the correctness backstop that also covers reconfigure swap-window
   * offers and seeded events whose dataset shape changed. Every other entry consumes one §7.2.3
   * sequence number and becomes a draft. Any remainder beyond the drain limit is left for the next
   * cycle.
   */
  List<DataSetMessageDraft> createEventDrafts(DateTime now) {
    ConcurrentLinkedQueue<QueuedEvent> queue = requireNonNull(eventQueue);

    var drafts = new ArrayList<DataSetMessageDraft>();
    for (int drained = 0; drained < EVENT_DRAIN_LIMIT; drained++) {
      QueuedEvent event = queue.poll();
      if (event == null) {
        break;
      }
      eventQueueSize.decrementAndGet();

      if (event.overflowMarker()) {
        // the marker has been dequeued: a subsequent drop starts a fresh overflow episode
        overflowMarked.set(false);
      }

      if (event.fields().size() != eventFieldCount) {
        service
            .getDiagnostics()
            .sourceError(
                path(),
                new StatusCode(StatusCodes.Bad_InternalError),
                "event for DataSetWriter '%s' has %d fields, expected %d"
                    .formatted(path(), event.fields().size(), eventFieldCount),
                null);
        continue; // arity guard: no sequence number consumed
      }

      drafts.add(
          DataSetMessageDraft.ofEvent(
              config,
              sequenceCounter.next(),
              includeTimestamp() ? event.timestamp() : null,
              StatusCode.GOOD,
              configurationVersion,
              event.fields(),
              metaData));
    }
    return drafts;
  }

  private SnapshotRead readSnapshot() {
    // data mode only: createDataDraft (its sole caller) is never invoked for event-mode writers
    PublishedDataSetReadContext readContext = requireNonNull(this.readContext);
    int fieldCount = readContext.fields().size();

    PublishedDataSetSource source = service.getSource(config.getDataSet());

    if (source == null) {
      var status = new StatusCode(StatusCodes.Bad_ConfigurationError);
      service
          .getDiagnostics()
          .sourceError(
              path(),
              status,
              "no PublishedDataSetSource bound for PublishedDataSet '%s'"
                  .formatted(config.getDataSet().name()),
              null);

      return SnapshotRead.failed(fieldCount, status);
    }

    try {
      DataSetSnapshot snapshot = source.read(readContext);
      List<DataValue> values = snapshot.values();

      if (values.size() != fieldCount) {
        var status = new StatusCode(StatusCodes.Bad_InternalError);
        service
            .getDiagnostics()
            .sourceError(
                path(),
                status,
                "snapshot for PublishedDataSet '%s' has %d values, expected %d"
                    .formatted(config.getDataSet().name(), values.size(), fieldCount),
                null);

        return SnapshotRead.failed(fieldCount, status);
      }

      return new SnapshotRead(values, StatusCode.GOOD);
    } catch (Exception e) {
      var status =
          UaException.extractStatusCode(e).orElse(new StatusCode(StatusCodes.Bad_InternalError));
      service
          .getDiagnostics()
          .sourceError(
              path(),
              status,
              "PublishedDataSetSource for '%s' failed: %s"
                  .formatted(config.getDataSet().name(), e.getMessage()),
              e);

      return SnapshotRead.failed(fieldCount, status);
    }
  }

  /**
   * The outcome of one source read: the per-field values plus the DataSetMessage-level status.
   *
   * <p>On a fatal read failure (no bound source, a snapshot whose arity disagrees with the
   * metadata, or a source exception) every field is set to {@code Bad_InternalError} and {@code
   * status} carries the failure's StatusCode, which the mappings surface as the DataSetMessage
   * header status (Part 14 §6.2.4.2: the header status is set to a bad code in a fatal error
   * situation). A successful read carries {@link StatusCode#GOOD}; per-field quality then rides on
   * the fields themselves per the Table 34/35 status propagation rules, so the header stays Good
   * for the Variant and DataValue field representations.
   *
   * @param values the per-field values to publish, one per metadata field.
   * @param status the DataSetMessage-level status.
   */
  private record SnapshotRead(List<DataValue> values, StatusCode status) {

    static SnapshotRead failed(int fieldCount, StatusCode status) {
      return new SnapshotRead(
          Collections.nCopies(fieldCount, new DataValue(StatusCodes.Bad_InternalError)), status);
    }
  }

  private boolean includeTimestamp() {
    return config.getSettings() instanceof UadpDataSetWriterSettings settings
        && settings.getDataSetMessageContentMask().getTimestamp();
  }
}
