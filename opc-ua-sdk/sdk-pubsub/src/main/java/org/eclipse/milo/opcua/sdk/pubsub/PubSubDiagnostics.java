/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.Map;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Diagnostics view of a {@link PubSubService}: immutable per-component counter snapshots, keyed by
 * component path.
 *
 * <p>Counters that do not apply to a component type, e.g. {@code networkMessagesReceived} on a
 * WriterGroup, remain zero.
 */
public interface PubSubDiagnostics {

  /**
   * Get an immutable snapshot of the diagnostics of every component, keyed by component path, e.g.
   * {@code "conn/group/writer"}.
   *
   * @return an immutable snapshot of the diagnostics of every component.
   */
  Map<String, ComponentDiagnostics> snapshot();

  /**
   * Get an immutable snapshot of the diagnostics of the component at {@code path}.
   *
   * @param path the path of the component, e.g. {@code "conn/group/writer"}.
   * @return the component diagnostics, or empty if no component exists at {@code path}.
   */
  default Optional<ComponentDiagnostics> component(String path) {
    return Optional.ofNullable(snapshot().get(path));
  }

  /**
   * Reset the diagnostic counters of the component at {@code path} to zero, clearing their
   * TimeFirstChange baselines. Applies to exactly that path (per-object, Part 14 §9.1.11.3):
   * resetting a group does not touch its writers/readers. All counters reset, spec-mapped and
   * vendor alike; {@code lastError} is untouched. Unknown paths are a no-op.
   *
   * <p>Server-side vendor counters kept outside this view (e.g. {@code
   * ServerPubSub.targetWriteErrors()} in the server integration module) are not affected.
   *
   * <p>Counter increments concurrent with a reset may be lost (the underlying adders reset
   * lock-free); an explicit reset tolerates that imprecision.
   *
   * @param path the path of the component to reset, e.g. {@code "conn/group/writer"}.
   */
  void reset(String path);

  /**
   * An immutable snapshot of the diagnostic counters of one PubSub component.
   *
   * @param path the path of the component, e.g. {@code "conn/group/writer"}.
   * @param networkMessagesSent the number of NetworkMessages sent.
   * @param networkMessagesReceived the number of NetworkMessages received. On a connection this
   *     counts arrivals on the data path — one tick per received datagram or broker message, before
   *     decoding and regardless of the decode outcome (a climbing connection counter with flat
   *     reader counters therefore indicates traffic that does not decode or match) — plus, for UADP
   *     connections with a discovery endpoint, one tick per discovery-socket datagram that decoded
   *     to a discovery probe or metadata announcement (other discovery-socket traffic, including
   *     undecodable input, is not counted). On a reader group it counts NetworkMessages that
   *     carried at least one DataSetMessage matching one of the group's readers, whether or not
   *     delivery followed (a matched message may still be dropped by the valid/metadata gates or
   *     the sequence-number windows).
   * @param dataSetMessagesSent the number of DataSetMessages sent.
   * @param dataSetMessagesReceived the number of DataSetMessages received.
   * @param decodeErrors the number of messages dropped because they could not be decoded or were
   *     not accepted: undecodable or truncated input and unsupported chunked NetworkMessages at the
   *     connection; NetworkMessages exceeding a reader group's non-zero {@code
   *     maxNetworkMessageSize} at the group; version mismatches, invalid DataSetMessages, and
   *     DataSetMetaData announcements that fail conversion at the reader.
   * @param sourceErrors the number of {@link PublishedDataSetSource} read failures.
   * @param staleSequenceMessages the number of DataSetMessages dropped by a DataSetReader's Part 14
   *     §7.2.3 sequence-number window as older than — or duplicating — the last processed message.
   *     The unit is dropped DataSetMessages: a NetworkMessage-level drop counts once per matched
   *     DataSetMessage it suppressed. A per-reader counter: both sequence-drop counters tick at
   *     DataSetReader paths only, where {@code dataSetMessagesReceived} plus the two sequence-drop
   *     counters equals the total matched DataSetMessages; at group and connection paths the
   *     sequence-drop counters stay zero and {@code dataSetMessagesReceived} does not count
   *     window-dropped DataSetMessages, so no such equality holds there. A normal-operation counter
   *     (the spec says such messages "shall be ignored"): drops do not set {@code lastError}. A
   *     Milo extension; Part 14 defines no counter for sequence-window drops.
   * @param invalidSequenceMessages the number of DataSetMessages dropped by a DataSetReader's Part
   *     14 §7.2.3 sequence-number window with a recency result in the invalid band — neither
   *     provably newer nor older than the last processed message, e.g. a huge forward jump after a
   *     publisher restarted its numbering. Same unit and posture as {@code staleSequenceMessages}.
   * @param encryptionErrors the number of publish cycles of a secured WriterGroup skipped because
   *     no usable key material was available (keys expired without replacement, a failed key
   *     source, an exhausted MessageNonce sequence). Ticks at WriterGroup paths only, once per
   *     skipped cycle; {@code lastError} and a diagnostics event are recorded only on the first
   *     skip after a successful cycle (edge-triggered), so a dead key source does not storm the
   *     event queue at the publishing interval.
   * @param decryptionErrors the number of received secured NetworkMessages whose payload failed to
   *     decrypt after the signature verified. Ticks at ReaderGroup paths — attributed to the first
   *     secured group, in declaration order, matching the message identity — once per
   *     NetworkMessage, with {@code lastError} and a diagnostics event (the same error class as
   *     {@code decodeErrors}).
   * @param invalidSignatureMessages the number of received secured NetworkMessages whose signature
   *     did not verify (or that were too short to carry the promised signature). Ticks at
   *     ReaderGroup paths, once per NetworkMessage, attributed like {@code decryptionErrors};
   *     {@code lastError} is set but NO diagnostics event is emitted — forged traffic must not
   *     become event-queue pressure — and a rate-limited WARN is logged instead.
   * @param unknownTokenMessages the number of received secured NetworkMessages dropped because
   *     their SecurityTokenId is outside the receiver's token window. Each drop may trigger at most
   *     one single-flight key refresh; messages are never buffered while it runs. Ticks at
   *     ReaderGroup paths, once per NetworkMessage; a normal-operation counter (no {@code
   *     lastError}, no event).
   * @param staleKeyMessages the number of received NetworkMessages dropped for want of usable keys
   *     — no or expired key window (Part 14 §6.2.12.2) — or by the §7.2.4.3 receive-mode gate: a
   *     received security mode below the group's configured mode, or a secured message to a group
   *     configured with mode None. Ticks at ReaderGroup paths, once per (group, NetworkMessage); a
   *     normal-operation counter (no {@code lastError}, no event). Security drops never count in
   *     {@code decodeErrors}.
   * @param failedTransmissions the number of NetworkMessages that were not transmitted: Part 14
   *     Table 322 "Error on NetworkMessage transmission". Ticks at WriterGroup paths only, for the
   *     group's data NetworkMessages: one per asynchronous send failure, one for the message whose
   *     synchronous send threw plus one per never-handed remainder message of that cycle, and one
   *     per oversize message skipped at the {@code maxNetworkMessageSize} gate ({@code
   *     Bad_EncodingLimitsExceeded} — such a skip ticks both this counter and {@code
   *     failedDataSetMessages}). Encode failures do not tick it (no NetworkMessage existed);
   *     metadata and discovery sends never tick it (connection-scope traffic, not group data);
   *     failures suppressed as shutdown/restart teardown noise never tick it.
   * @param failedDataSetMessages the number of DataSetMessages never sent due to an encode or send
   *     failure, attributed per contributing writer (Part 14 Table 329 FailedDataSetMessages).
   *     Ticks at DataSetWriter paths: per contributing writer of an untransmitted NetworkMessage
   *     (asynchronous send failure, synchronous send throw, oversize skip — attribution via the
   *     encoded message's writer list), and per non-keep-alive draft of a partition whose encode
   *     failed (an encode failure ticks this counter only, never {@code failedTransmissions}).
   *     Source-read failures stay in {@code sourceErrors}; suppressed teardown noise never ticks.
   * @param stateError the number of transitions into {@code PubSubState.Error} (Part 14 Table 311
   *     StateError). Ticks whenever the component enters Error, whatever the cause.
   * @param stateOperationalByMethod the number of transitions into {@code Operational} caused by an
   *     explicit enable — the owner API or a remote Enable Method (Table 311 "Enable Method call").
   *     Deferred startups tick on the final hop with the remembered enabling cause. Service
   *     startup/reconfigure transitions ({@code STARTUP} cause) tick neither this counter nor
   *     {@code stateOperationalByParent} on the initiating component.
   * @param stateOperationalByParent the number of transitions into {@code Operational} cascaded
   *     from a parent's state change (Table 311 "an operational parent"). Descendants of a starting
   *     or recovering component transition via child cascades and tick here.
   * @param stateOperationalFromError the number of transitions into {@code Operational} whose
   *     previous state was {@code Error} (Table 311 StateOperationalFromError), whatever cleared
   *     the error.
   * @param statePausedByParent the number of transitions into {@code Paused}. A Paused entry is
   *     definitionally parent-caused (Table 311 "triggered by a paused or disabled parent"); an
   *     enable of a component under a non-operational parent lands here too.
   * @param stateDisabledByMethod the number of transitions into {@code Disabled} caused by an
   *     explicit disable — the owner API or a remote Disable Method. Dispose-caused Disabled
   *     transitions (shutdown, removal by reconfiguration) tick nothing.
   * @param timeFirstChange the first time each counter left zero, keyed by {@link PubSubCounter};
   *     an entry exists only for counters that have left zero (the Part 14 §9.1.11.5
   *     TimeFirstChange contract: null while the value is 0). Cleared by {@link #reset(String)};
   *     preserved, like the counters, across path-stable reconfigure restarts. Unmodifiable.
   * @param lastError the status code of the most recent error, or {@code null} if no error has
   *     occurred.
   */
  record ComponentDiagnostics(
      String path,
      long networkMessagesSent,
      long networkMessagesReceived,
      long dataSetMessagesSent,
      long dataSetMessagesReceived,
      long decodeErrors,
      long sourceErrors,
      long staleSequenceMessages,
      long invalidSequenceMessages,
      long encryptionErrors,
      long decryptionErrors,
      long invalidSignatureMessages,
      long unknownTokenMessages,
      long staleKeyMessages,
      long failedTransmissions,
      long failedDataSetMessages,
      long stateError,
      long stateOperationalByMethod,
      long stateOperationalByParent,
      long stateOperationalFromError,
      long statePausedByParent,
      long stateDisabledByMethod,
      Map<PubSubCounter, DateTime> timeFirstChange,
      @Nullable StatusCode lastError) {

    /**
     * Get the first time {@code counter} left zero, or {@code null} if it has never left zero (Part
     * 14 §9.1.11.5 TimeFirstChange).
     *
     * @param counter the counter identity.
     * @return the first-change time, or {@code null} while the counter is 0.
     */
    public @Nullable DateTime timeFirstChange(PubSubCounter counter) {
      return timeFirstChange.get(counter);
    }
  }
}
