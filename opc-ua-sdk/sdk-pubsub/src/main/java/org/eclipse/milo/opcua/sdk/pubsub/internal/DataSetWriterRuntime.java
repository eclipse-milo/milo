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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
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
 * DataSetMessageSequenceCounter}). The counter is instance state, so writer recreation —
 * reconfigure, and the metadata/ConfigurationVersion changes that imply it — restarts the
 * DataSetMessage sequence at 0; subscribers recover from the apparent backward jump by whichever
 * comes first: the recreated writer's first keep-alive, whose carried next-expected value reseeds
 * the subscriber's window in both timeout modes (§7.2.4.5.8 — the publisher is authoritative about
 * its own counter; requires the group to emit keep-alives), the §7.2.3 two-times-receive-timeout
 * record discard when a messageReceiveTimeout is configured, or the 16-consecutive-rejection
 * restart heuristic of {@link ReaderSequenceTracker} when it is not.
 *
 * <p>Draft creation, the DataSetMessage sequence counter, the cadence counter, and the delta
 * baseline are confined to the owning group's publish task thread; the two cross-thread inputs are
 * the baseline-invalidated flag (written from transport callbacks) and the frame-state reset
 * generation (written from {@link WriterGroupRuntime#activate()} on the engine thread), both
 * volatile and both consumed at the start of each data draft. The reset in particular must NOT be
 * applied on the engine thread: a stale publish cycle that passed its activation-generation check
 * before the (re)activation may still be drafting, and would otherwise observe half-reset state and
 * emit a delta against the wrong baseline.
 */
final class DataSetWriterRuntime extends AbstractComponentRuntime {

  private final PubSubServiceImpl service;
  private final WriterGroupRuntime group;
  private final DataSetWriterConfig config;
  private final PublishedDataSetConfig dataSet;
  private final PublishedDataSetReadContext readContext;
  private final ConfigurationVersionDataType configurationVersion;
  private final DataSetMetaDataType metaData;

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
    this.readContext =
        new PublishedDataSetReadContext(config.getDataSet(), dataSet.getFields(), null);
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
   * Activate the writer: re-run the writer-level subset of the startup validation and publish
   * broker metadata. A throw is mapped by {@link PubSubStateMachine} to {@code PubSubState.Error}
   * with the exception's status code, leaving the group Operational.
   */
  @Override
  void activate() throws UaException {
    checkUnsupportedUadpFeatures();

    // broker connections publish this writer's (retained) metadata when the writer activates;
    // failures are recorded in diagnostics, never thrown: metadata publication is auxiliary
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterActivated(group, this);
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
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
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

  /** Release all resources of this runtime. */
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

  private SnapshotRead readSnapshot() {
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
