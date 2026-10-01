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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedChunk;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.ReceivedSecurity;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.SecurityOutcome;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reassembles the payloads of Chunk NetworkMessages (OPC UA Part 14 §7.2.4.4.4) received on one
 * connection.
 *
 * <p>Sits AFTER the codec's verify/decrypt step: each chunk NetworkMessage is secured individually,
 * so the {@link DecodedChunk} pieces fed here are already verified plaintext (and already fresh
 * copies — they alias neither the datagram nor a decrypted buffer). The caller feeds only chunks
 * that at least one reader of the connection accepted; chunks no reader accepts never touch
 * reassembly state.
 *
 * <p>Streams are keyed by (PublisherId, WriterGroupId, DataSetWriterId) and by the received
 * security mode (None, Sign, or SignAndEncrypt), so a reassembled payload only ever contains bytes
 * from chunks received with the same security mode as the chunk that completed it, and chunks
 * received with another mode cannot change, reset, or complete it. One payload is in progress per
 * stream: the spec blesses a single-payload subscriber, and a chunk whose MessageSequenceNumber
 * classifies NEWER (§7.2.3, N=16) discards the incomplete predecessor, while OLDER/invalid chunks
 * are dropped. Chunks of the same payload may arrive out of order; duplicate ranges overwrite and
 * are not double-counted.
 *
 * <p>Each stream also remembers the MessageSequenceNumber of its last completed payload,
 * independently of any payload in progress, so a second copy of an already completed chunk set, or
 * an older payload whose chunks arrive after a newer one completed, is dropped instead of
 * reassembled again. Only a chunk whose MessageSequenceNumber classifies NEWER than the completed
 * one can start or continue the next payload. A stream whose record is idle for {@link
 * #IDLE_EVICTION_NANOS} is forgotten, so a publisher that restarts its numbering recovers after
 * that period of its chunks being dropped.
 *
 * <p>Resource caps (hard constants): a payload larger than {@link #MAX_TOTAL_SIZE} (4 MiB) is
 * dropped, at most {@link #MAX_STREAMS} (64) streams are tracked concurrently, in progress or
 * completed, and streams idle longer than {@link #IDLE_EVICTION_NANOS} (10 s) are evicted by a
 * sweep run from {@link #accept(DecodedNetworkMessage, long)}. When the cap is reached, a new
 * stream evicts a stream whose received security mode is the same as or lower than its own,
 * preferring the least recently active completed record over any payload in progress; a chunk that
 * finds no such stream is dropped, so chunks received with mode None never evict a signed stream.
 * Dropped chunks do not count as activity.
 *
 * <p>Not thread safe: confined to the connection's dispatch queue, like all subscriber dispatch
 * state.
 */
final class ChunkReassembler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChunkReassembler.class);

  /** The maximum reassembled payload size (Table 159 TotalSize) accepted, in bytes: 4 MiB. */
  static final int MAX_TOTAL_SIZE = 4 * 1024 * 1024;

  /** The maximum number of concurrently tracked streams, in progress or completed. */
  static final int MAX_STREAMS = 64;

  /** Streams idle longer than this are evicted, in progress or completed: 10 seconds. */
  static final long IDLE_EVICTION_NANOS = TimeUnit.SECONDS.toNanos(10);

  /** Idle eviction sweeps are rate-limited to one per interval. */
  private static final long SWEEP_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(1);

  private final Map<StreamKey, StreamState> streams = new HashMap<>();

  private boolean sweepArmed = false;
  private long lastSweepNanos;

  /**
   * Accept one decoded chunk NetworkMessage that at least one reader of the connection accepted.
   *
   * @param chunkMessage a decoded NetworkMessage whose {@link DecodedNetworkMessage#chunk()} is
   *     non-null and whose security, when present, is {@link SecurityOutcome#VERIFIED}.
   * @param nowNanos the current {@link System#nanoTime()} (injectable for tests).
   * @return the reassembled payload when this chunk completes it, else {@code null}.
   */
  @Nullable ReassembledMessage accept(DecodedNetworkMessage chunkMessage, long nowNanos) {
    evictStale(nowNanos);

    DecodedChunk chunk = chunkMessage.chunk();
    if (chunk == null) {
      return null;
    }

    ReceivedSecurity security = chunkMessage.security();
    if (security != null && security.outcome() != SecurityOutcome.VERIFIED) {
      // the decoder surfaces no chunk for a non-VERIFIED outcome; never let one reach a stream
      return null;
    }
    MessageSecurityMode securityMode =
        security != null ? security.mode() : MessageSecurityMode.None;

    var key =
        new StreamKey(
            chunkMessage.publisherId() != null
                ? chunkMessage.publisherId().toCanonicalString()
                : null,
            chunkMessage.writerGroupId(),
            chunk.dataSetWriterId(),
            securityMode);

    long totalSize = chunk.totalSize().longValue();
    long offset = chunk.chunkOffset().longValue();
    int length = chunk.chunkData().length;

    if (totalSize > MAX_TOTAL_SIZE) {
      // the cap also protects the in-progress payload of the stream: an unusable TotalSize
      // abandons it rather than letting a mixed stream complete with stale bytes. The stream's
      // completed record is kept, so the chunk cannot reopen a completed payload either.
      LOGGER.debug(
          "chunk TotalSize {} exceeds the {} byte cap; dropping", totalSize, MAX_TOTAL_SIZE);
      abandonInProgress(key);
      return null;
    }
    if (offset + length > totalSize) {
      LOGGER.debug(
          "chunk range [{}, {}) exceeds TotalSize {}; dropping",
          offset,
          offset + length,
          totalSize);
      abandonInProgress(key);
      return null;
    }

    int sequenceNumber = chunk.messageSequenceNumber().intValue();

    StreamState state = streams.get(key);

    if (state != null
        && state.hasCompleted
        && !isNewer(state.completedSequenceNumber, sequenceNumber)) {
      // a second copy of the completed payload, or an older one: drop it without activity
      LOGGER.debug(
          "chunk MessageSequenceNumber {} is not newer than completed {}; dropping",
          sequenceNumber,
          state.completedSequenceNumber);
      return null;
    }

    Payload payload = state != null ? state.payload : null;

    if (payload != null
        && (payload.sequenceNumber != sequenceNumber || payload.totalSize != (int) totalSize)) {
      if (payload.sequenceNumber != sequenceNumber
          && !isNewer(payload.sequenceNumber, sequenceNumber)) {
        // a chunk of an older (or unprovably newer) payload: drop it, keep the in-progress one
        LOGGER.debug(
            "chunk MessageSequenceNumber {} is not newer than in-progress {}; dropping",
            sequenceNumber,
            payload.sequenceNumber);
        return null;
      }
      // a NEWER payload started (§7.2.4.4.4: a single-payload subscriber may skip an incomplete
      // payload when a chunk for a newer MessageSequenceNumber arrives) — or the same payload
      // changed its declared TotalSize, which can never complete consistently. Start over.
      payload = null;
    }

    if (state == null) {
      if (streams.size() >= MAX_STREAMS && !evictLeastRecentlyActive(securityMode)) {
        LOGGER.debug(
            "stream cap of {} reached and no stream of mode {} or lower to evict; dropping",
            MAX_STREAMS,
            securityMode);
        return null;
      }
      state = new StreamState();
      streams.put(key, state);
    }

    if (payload == null) {
      payload = new Payload(sequenceNumber, (int) totalSize);
      state.payload = payload;
    }

    state.lastActivityNanos = nowNanos;

    // duplicate ranges overwrite; coverage intervals are merged, so they are not double-counted
    System.arraycopy(chunk.chunkData(), 0, payload.buffer, (int) offset, length);
    payload.addCoverage((int) offset, (int) offset + length);

    if (payload.isComplete()) {
      // keep the stream as a record of the completed MessageSequenceNumber, without the buffer,
      // so a repeated or older chunk set is not reassembled again
      state.payload = null;
      state.hasCompleted = true;
      state.completedSequenceNumber = sequenceNumber;
      return new ReassembledMessage(chunkMessage, chunk.dataSetWriterId(), payload.buffer);
    }

    return null;
  }

  /**
   * Evict streams idle longer than {@link #IDLE_EVICTION_NANOS}, in progress or completed. Called
   * from {@link #accept(DecodedNetworkMessage, long)}; sweeps are rate-limited to one per second.
   */
  void evictStale(long nowNanos) {
    if (sweepArmed && nowNanos - lastSweepNanos < SWEEP_INTERVAL_NANOS) {
      return;
    }
    sweepArmed = true;
    lastSweepNanos = nowNanos;

    streams.values().removeIf(state -> nowNanos - state.lastActivityNanos > IDLE_EVICTION_NANOS);
  }

  /** The number of streams with an in-progress payload; test surface. */
  int streamCount() {
    int count = 0;
    for (StreamState state : streams.values()) {
      if (state.payload != null) {
        count++;
      }
    }
    return count;
  }

  /** The number of tracked streams, in progress or completed; test surface. */
  int recordCount() {
    return streams.size();
  }

  /** Whether {@code candidate} classifies NEW against {@code reference} in the §7.2.3 window. */
  private static boolean isNewer(int reference, int candidate) {
    return SequenceNumberWindow.classify(reference, candidate, 16)
        == SequenceNumberWindow.Classification.NEW;
  }

  /**
   * Drop the in-progress payload of {@code key}, if any. The stream's completed record, if it has
   * one, stays; a stream with neither is forgotten.
   */
  private void abandonInProgress(StreamKey key) {
    StreamState state = streams.get(key);
    if (state == null) {
      return;
    }
    state.payload = null;
    if (!state.hasCompleted) {
      streams.remove(key);
    }
  }

  /**
   * Evict one stream whose received security mode is the same as or lower than {@code
   * securityMode}: the least recently active completed record if there is one, else the least
   * recently active payload in progress.
   *
   * @return whether a stream was evicted.
   */
  private boolean evictLeastRecentlyActive(MessageSecurityMode securityMode) {
    int rank = modeRank(securityMode);
    StreamKey oldestKey = null;
    long oldestNanos = Long.MAX_VALUE;
    boolean oldestIsRecord = false;

    for (Map.Entry<StreamKey, StreamState> entry : streams.entrySet()) {
      if (modeRank(entry.getKey().securityMode()) > rank) {
        continue;
      }
      boolean isRecord = entry.getValue().payload == null;
      if (oldestKey == null
          || (isRecord && !oldestIsRecord)
          || (isRecord == oldestIsRecord && entry.getValue().lastActivityNanos - oldestNanos < 0)) {
        oldestKey = entry.getKey();
        oldestNanos = entry.getValue().lastActivityNanos;
        oldestIsRecord = isRecord;
      }
    }

    if (oldestKey == null) {
      return false;
    }
    LOGGER.debug("evicting chunk stream at the stream cap: {}", oldestKey);
    streams.remove(oldestKey);
    return true;
  }

  /** The §7.2.4.3 numeric mode order; Invalid ranks like None. */
  private static int modeRank(MessageSecurityMode mode) {
    return switch (mode) {
      case Invalid, None -> 1;
      case Sign -> 2;
      case SignAndEncrypt -> 3;
    };
  }

  /**
   * A reassembled chunk payload: one complete DataSetMessage (header and body).
   *
   * @param header the completing chunk's NetworkMessage, for the header values the dispatcher
   *     routes on; its security is the security every contributing chunk was received with.
   * @param dataSetWriterId the DataSetWriterId of the chunked stream, or {@code null} if the chunk
   *     NetworkMessages carried no PayloadHeader.
   * @param payload the reassembled payload bytes; owned by the receiver.
   */
  record ReassembledMessage(
      DecodedNetworkMessage header, @Nullable UShort dataSetWriterId, byte[] payload) {}

  /**
   * The identity of one chunked stream: PublisherId (canonical form), WriterGroupId, and
   * DataSetWriterId, each {@code null} when absent from the wire, plus the received security mode
   * ({@link MessageSecurityMode#None} for an unsecured chunk).
   */
  private record StreamKey(
      @Nullable String publisherId,
      @Nullable UShort writerGroupId,
      @Nullable UShort dataSetWriterId,
      MessageSecurityMode securityMode) {}

  /**
   * One stream: the payload in progress, if any, and the record of the last completed
   * MessageSequenceNumber, if any. At least one of the two is present while the stream is tracked.
   */
  private static final class StreamState {

    @Nullable Payload payload;

    boolean hasCompleted = false;
    int completedSequenceNumber;

    long lastActivityNanos;
  }

  /** The in-progress payload of one stream. */
  private static final class Payload {

    final int sequenceNumber;
    final int totalSize;
    final byte[] buffer;

    /** Merged, sorted, disjoint covered ranges as {@code [start, end)} pairs. */
    final List<int[]> coverage = new ArrayList<>();

    Payload(int sequenceNumber, int totalSize) {
      this.sequenceNumber = sequenceNumber;
      this.totalSize = totalSize;
      this.buffer = new byte[totalSize];
    }

    void addCoverage(int start, int end) {
      if (start == end) {
        return;
      }

      var merged = new ArrayList<int[]>(coverage.size() + 1);
      int newStart = start;
      int newEnd = end;

      for (int[] range : coverage) {
        if (range[1] < newStart || range[0] > newEnd) {
          merged.add(range);
        } else {
          newStart = Math.min(newStart, range[0]);
          newEnd = Math.max(newEnd, range[1]);
        }
      }

      merged.add(new int[] {newStart, newEnd});
      merged.sort((a, b) -> Integer.compare(a[0], b[0]));

      coverage.clear();
      coverage.addAll(merged);
    }

    boolean isComplete() {
      if (totalSize == 0) {
        return true;
      }
      return coverage.size() == 1 && coverage.get(0)[0] == 0 && coverage.get(0)[1] == totalSize;
    }
  }
}
