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

import static org.eclipse.milo.opcua.sdk.pubsub.internal.SequenceNumberWindow.Classification.NEW;
import static org.eclipse.milo.opcua.sdk.pubsub.internal.SequenceNumberWindow.Classification.STALE;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the per-reader stream keying and record lifecycle around {@link
 * SequenceNumberWindow}: wire-derived stream tuples with null components participating in the key,
 * cross-type PublisherId canonicalization, the lazy Part 14 §7.2.3 two-times-receive-timeout record
 * discard, and the fixed stream cap with least-recently-used eviction when the timeout is disabled.
 */
class ReaderSequenceTrackerTest {

  private static final PublisherId PUB_A = PublisherId.uint16(ushort(1));
  private static final PublisherId PUB_B = PublisherId.uint16(ushort(2));

  @Test
  void dataSetMessageStreamsAreTrackedPerPublisherAndWriter() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    // pub A / writer 10 seeds its own stream
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 0L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5), 0L);

    // the same number from pub B / writer 10 and pub A / writer 11 is NEW: independent streams
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_B, ushort(10), uint(5), 0L));
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(11), uint(5), 0L));

    // but a repeat on pub A / writer 10 is a duplicate
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 0L));
  }

  @Test
  void nullKeyComponentsParticipateInTheStreamKey() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    tracker.acceptDataSetMessage(null, null, uint(5), 0L);
    assertEquals(STALE, tracker.classifyDataSetMessage(null, null, uint(5), 0L));

    // a stream that puts identifiers on the wire is distinct from the headerless stream
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, null, uint(5), 0L));
    assertEquals(NEW, tracker.classifyDataSetMessage(null, ushort(10), uint(5), 0L));
  }

  @Test
  void publisherIdsCompareByCanonicalStringAcrossWireTypes() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 32, 0L);

    // the JSON mapping carries every PublisherId as a String: "1" and UInt16 1 are one stream
    tracker.acceptDataSetMessage(PublisherId.uint16(ushort(1)), ushort(10), uint(5), 0L);
    assertEquals(
        STALE, tracker.classifyDataSetMessage(PublisherId.string("1"), ushort(10), uint(5), 0L));
  }

  @Test
  void networkMessageWindowsAlwaysUseUInt16Math() {
    // DataSetMessage width 32 (a JSON-mapped reader) must not leak into the NetworkMessage
    // window: the UADP NetworkMessage sequence number is UInt16 and 0xFFFF -> 0 is a rollover
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 32, 0L);

    tracker.acceptNetworkMessage(PUB_A, ushort(1), ushort(0xFFFF), 0L);
    assertEquals(NEW, tracker.classifyNetworkMessage(PUB_A, ushort(1), ushort(0), 0L));

    // and NetworkMessage streams are independent of DataSetMessage streams
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(1), uint(0xFFFF), 0L));
  }

  @Test
  void recordsAreDiscardedAfterTwoTimesTheReceiveTimeoutOfStreamSilence() {
    long discardNanos = 1_000L;
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, discardNanos);

    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5), 0L);

    // within the discard period the window holds: a duplicate is stale
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 500L));

    // rejected observations do not refresh the discard clock (a restarted publisher that keeps
    // sending must not keep its own stream wedged): past the period the record is discarded
    // lazily and the same number re-seeds the stream
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 900L));
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 1_001L + 1));
  }

  /**
   * §7.2.3 discards records when the subscriber does "not receive messages" for the discard period
   * — and keep-alives are received messages. A keep-alive-only period of any length (the post-HG3
   * steady state of a quiet publisher) must keep a seeded stream record alive: each keep-alive
   * refreshes the discard clock while the window position never moves, so a then-replayed old data
   * message still classifies STALE.
   */
  @Test
  void keepAlivesOnASeededStreamRefreshTheRecordDiscardClock() {
    long discardNanos = 1_000L;
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, discardNanos);

    // a data message accepted at t=0 seeds the stream
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5), 0L);

    // a keep-alive-only period: gaps below the discard period, total span well past it. With an
    // accepts-only discard clock the record would die of "silence" along the way — the replay at
    // t=3_600 lands more than the discard period after anything such a clock counts — and would
    // wrongly re-seed the stream as NEW.
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(6), 900L);
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(6), 1_800L);
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(6), 2_700L);

    // the record survived: a replayed old data message still classifies STALE ...
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 3_600L));

    // ... and the keep-alives never advanced the window or counted as accepts: the carried
    // next-expected number is still NEW
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(6), 3_600L));
    assertEquals(uint(5), tracker.lastAcceptedDataSetMessageSequenceNumber());
  }

  @Test
  void zeroDiscardPeriodMeansNoTimeBasedDiscard() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5), 0L);
    assertEquals(
        STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), Long.MAX_VALUE / 2));
  }

  @Test
  void streamCapEvictsTheLeastRecentlyUsedStream() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    // fill the map to the cap with distinct headerless-writer streams (distinct writer ids)
    for (int i = 0; i < ReaderSequenceTracker.MAX_TRACKED_STREAMS; i++) {
      tracker.acceptDataSetMessage(PUB_A, ushort(i), uint(5), i);
    }

    // every stream is still tracked: duplicates are stale
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(0), uint(5), 5_000L));
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(4095), uint(5), 5_001L));

    // one stream over the cap evicts the least recently used (writer 1: writers 0 and 4095 were
    // just touched above) ...
    tracker.acceptDataSetMessage(PUB_B, ushort(9999), uint(1), 6_000L);

    // ... so writer 1 re-seeds while a recently used stream still detects its duplicate
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(1), uint(5), 6_001L));
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(0), uint(5), 6_002L));
  }

  @Test
  void keepAliveSeedsButNeverAdvancesAndLastAcceptedTracksAcceptsOnly() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);
    assertNull(tracker.lastAcceptedDataSetMessageSequenceNumber());

    // keep-alive on an unseeded stream seeds to carried-1; it is not an accepted DataSetMessage
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(7), 0L);
    assertNull(tracker.lastAcceptedDataSetMessageSequenceNumber());

    // the data message carrying the keep-alive's next-expected number is NEW
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(7), 1L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(7), 1L);
    assertEquals(uint(7), tracker.lastAcceptedDataSetMessageSequenceNumber());

    // keep-alives never advance a seeded window: the next data number is still NEW after one
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(8), 2L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(8), 3L));
    assertEquals(uint(7), tracker.lastAcceptedDataSetMessageSequenceNumber());
  }
}
