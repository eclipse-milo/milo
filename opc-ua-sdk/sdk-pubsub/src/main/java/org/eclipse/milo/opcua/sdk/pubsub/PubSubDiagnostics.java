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
      @Nullable StatusCode lastError) {}
}
