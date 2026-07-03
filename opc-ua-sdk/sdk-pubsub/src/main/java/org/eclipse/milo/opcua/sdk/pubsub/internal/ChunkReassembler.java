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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reassembles the payloads of Chunk NetworkMessages (OPC UA Part 14 §7.2.4.4.4) received on one
 * connection.
 *
 * <p>Sits AFTER the codec's verify/decrypt step: each chunk NetworkMessage is secured individually,
 * so the {@link DecodedChunk} pieces fed here are already verified plaintext (and already fresh
 * copies — they alias neither the datagram nor a decrypted buffer). One payload is in progress per
 * stream, keyed by (PublisherId, WriterGroupId, DataSetWriterId): the spec blesses a single-payload
 * subscriber, and a chunk whose MessageSequenceNumber classifies NEWER (§7.2.3, N=16) discards the
 * incomplete predecessor, while OLDER/invalid chunks are dropped. Chunks of the same payload may
 * arrive out of order; duplicate ranges overwrite and are not double-counted.
 *
 * <p>DoS caps (hard constants; TotalSize is attacker-controlled): a payload larger than {@link
 * #MAX_TOTAL_SIZE} (4 MiB) is dropped, at most {@link #MAX_STREAMS} (64) streams are reassembled
 * concurrently (the least recently active is evicted), and in-progress payloads idle longer than
 * {@link #IDLE_EVICTION_NANOS} (10 s) are evicted by a sweep run from {@link
 * #accept(DecodedNetworkMessage, long)}.
 *
 * <p>Not thread safe: confined to the connection's dispatch queue, like all subscriber dispatch
 * state.
 */
final class ChunkReassembler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChunkReassembler.class);

  /** The maximum reassembled payload size (Table 159 TotalSize) accepted, in bytes: 4 MiB. */
  static final int MAX_TOTAL_SIZE = 4 * 1024 * 1024;

  /** The maximum number of concurrently reassembling streams. */
  static final int MAX_STREAMS = 64;

  /** In-progress payloads idle longer than this are evicted: 10 seconds. */
  static final long IDLE_EVICTION_NANOS = TimeUnit.SECONDS.toNanos(10);

  /** Idle eviction sweeps are rate-limited to one per interval. */
  private static final long SWEEP_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(1);

  private final Map<StreamKey, StreamState> streams = new HashMap<>();

  private boolean sweepArmed = false;
  private long lastSweepNanos;

  /**
   * Accept one decoded chunk NetworkMessage.
   *
   * @param chunkMessage a decoded NetworkMessage whose {@link DecodedNetworkMessage#chunk()} is
   *     non-null.
   * @param nowNanos the current {@link System#nanoTime()} (injectable for tests).
   * @return the reassembled payload when this chunk completes it, else {@code null}.
   */
  @Nullable ReassembledMessage accept(DecodedNetworkMessage chunkMessage, long nowNanos) {
    evictStale(nowNanos);

    DecodedChunk chunk = chunkMessage.chunk();
    if (chunk == null) {
      return null;
    }

    var key =
        new StreamKey(
            chunkMessage.publisherId() != null
                ? chunkMessage.publisherId().toCanonicalString()
                : null,
            chunkMessage.writerGroupId(),
            chunk.dataSetWriterId());

    long totalSize = chunk.totalSize().longValue();
    long offset = chunk.chunkOffset().longValue();
    int length = chunk.chunkData().length;

    if (totalSize > MAX_TOTAL_SIZE) {
      // the cap also protects the in-progress payload of the stream: a poisoned TotalSize
      // abandons it rather than letting a mixed stream complete with stale bytes
      LOGGER.debug(
          "chunk TotalSize {} exceeds the {} byte cap; dropping", totalSize, MAX_TOTAL_SIZE);
      streams.remove(key);
      return null;
    }
    if (offset + length > totalSize) {
      LOGGER.debug(
          "chunk range [{}, {}) exceeds TotalSize {}; dropping",
          offset,
          offset + length,
          totalSize);
      streams.remove(key);
      return null;
    }

    int sequenceNumber = chunk.messageSequenceNumber().intValue();

    StreamState state = streams.get(key);

    if (state != null
        && (state.sequenceNumber != sequenceNumber || state.totalSize != (int) totalSize)) {
      if (state.sequenceNumber != sequenceNumber
          && SequenceNumberWindow.classify(state.sequenceNumber, sequenceNumber, 16)
              != SequenceNumberWindow.Classification.NEW) {
        // a chunk of an older (or unprovably newer) payload: drop it, keep the in-progress one
        LOGGER.debug(
            "chunk MessageSequenceNumber {} is not newer than in-progress {}; dropping",
            sequenceNumber,
            state.sequenceNumber);
        return null;
      }
      // a NEWER payload started (§7.2.4.4.4: a single-payload subscriber may skip an incomplete
      // payload when a chunk for a newer MessageSequenceNumber arrives) — or the same payload
      // changed its declared TotalSize, which can never complete consistently. Start over.
      streams.remove(key);
      state = null;
    }

    if (state == null) {
      if (streams.size() >= MAX_STREAMS) {
        evictLeastRecentlyActive();
      }
      state = new StreamState(sequenceNumber, (int) totalSize);
      streams.put(key, state);
    }

    state.lastActivityNanos = nowNanos;

    // duplicate ranges overwrite; coverage intervals are merged, so they are not double-counted
    System.arraycopy(chunk.chunkData(), 0, state.payload, (int) offset, length);
    state.addCoverage((int) offset, (int) offset + length);

    if (state.isComplete()) {
      streams.remove(key);
      return new ReassembledMessage(chunkMessage, chunk.dataSetWriterId(), state.payload);
    }

    return null;
  }

  /**
   * Evict in-progress payloads idle longer than {@link #IDLE_EVICTION_NANOS}. Called from {@link
   * #accept(DecodedNetworkMessage, long)}; sweeps are rate-limited to one per second.
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
    return streams.size();
  }

  private void evictLeastRecentlyActive() {
    StreamKey oldestKey = null;
    long oldestNanos = Long.MAX_VALUE;

    for (Map.Entry<StreamKey, StreamState> entry : streams.entrySet()) {
      if (oldestKey == null || entry.getValue().lastActivityNanos - oldestNanos < 0) {
        oldestKey = entry.getKey();
        oldestNanos = entry.getValue().lastActivityNanos;
      }
    }

    if (oldestKey != null) {
      LOGGER.debug("evicting least recently active chunk stream: {}", oldestKey);
      streams.remove(oldestKey);
    }
  }

  /**
   * A reassembled chunk payload: one complete DataSetMessage (header and body).
   *
   * @param header the completing chunk's NetworkMessage, for the header values the dispatcher
   *     routes on.
   * @param dataSetWriterId the DataSetWriterId of the chunked stream, or {@code null} if the chunk
   *     NetworkMessages carried no PayloadHeader.
   * @param payload the reassembled payload bytes; owned by the receiver.
   */
  record ReassembledMessage(
      DecodedNetworkMessage header, @Nullable UShort dataSetWriterId, byte[] payload) {}

  /**
   * The identity of one chunked stream: PublisherId (canonical form), WriterGroupId, and
   * DataSetWriterId, each {@code null} when absent from the wire.
   */
  private record StreamKey(
      @Nullable String publisherId,
      @Nullable UShort writerGroupId,
      @Nullable UShort dataSetWriterId) {}

  /** The in-progress payload of one stream. */
  private static final class StreamState {

    final int sequenceNumber;
    final int totalSize;
    final byte[] payload;

    /** Merged, sorted, disjoint covered ranges as {@code [start, end)} pairs. */
    final List<int[]> coverage = new ArrayList<>();

    long lastActivityNanos;

    StreamState(int sequenceNumber, int totalSize) {
      this.sequenceNumber = sequenceNumber;
      this.totalSize = totalSize;
      this.payload = new byte[totalSize];
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
