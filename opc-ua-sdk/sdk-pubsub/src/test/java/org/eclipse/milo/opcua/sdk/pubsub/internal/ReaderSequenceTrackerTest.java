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

import static org.eclipse.milo.opcua.sdk.pubsub.internal.SequenceNumberWindow.Classification.INVALID;
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
 * discard, the 16-consecutive-rejection restart-recovery discard that replaces it when the timeout
 * is disabled (and is inert otherwise), the §7.2.4.5.8 keep-alive reseed of a window inconsistent
 * with the carried next-expected value, and the fixed stream cap with least-recently-used eviction.
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
   * steady state of a quiet publisher) must keep a seeded stream record alive: each consistent
   * keep-alive refreshes the discard clock while the window position never moves, so a
   * then-replayed old data message still classifies STALE.
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

    // ... and the consistent keep-alives never advanced the window or counted as accepts: the
    // carried next-expected number is still NEW
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

  /**
   * Restart recovery with the timeout disabled (Milo extension): with no time-based discard armed
   * ({@code discardNanos == 0}), 16 consecutive STALE/INVALID classifications on one stream discard
   * its record, and the 17th message — the first after the discard — re-seeds and classifies NEW. A
   * publisher that restarted its numbering at 0 recovers instead of staying wedged forever (§7.2.3
   * defines recovery only via the two-times-timeout silence discard).
   */
  @Test
  void sixteenConsecutiveRejectionsReseedTheStreamWhenTheTimeoutIsDisabled() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    // the publisher reached 30000, then restarted at 0
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(30_000), 0L);

    // the restarted publisher's messages 0..15 land in the invalid band and are rejected; the
    // 16th rejection trips the threshold and discards the record
    for (int seq = 0; seq < 16; seq++) {
      assertEquals(
          INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(seq), 1_000L + seq));
    }

    // the 17th message — the first after the discard — accepts and re-seeds the stream
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(16), 2_000L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(16), 2_000L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(17), 2_001L));
  }

  /** An accepted message resets the rejection streak: 15 rejects then an accept start over. */
  @Test
  void anAcceptResetsTheConsecutiveRejectionStreak() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(30_000), 0L);

    // 15 rejections: one short of the threshold
    for (int seq = 0; seq < 15; seq++) {
      assertEquals(INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(seq), 1_000L));
    }

    // an in-window message accepts and clears the streak ...
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(30_001), 1_100L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(30_001), 1_100L);

    // ... so 15 further rejections still leave the record intact (a 16th would discard it)
    for (int seq = 0; seq < 15; seq++) {
      assertEquals(INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(seq), 1_200L));
    }
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(30_001), 1_300L));
  }

  /**
   * With a time-based discard armed ({@code discardNanos > 0}) the restart heuristic is INERT:
   * rejections keep dropping past 16 for as long as the record lives — the pure §7.2.3 semantics —
   * so the replay-rejection posture of configured deployments is not weakened.
   */
  @Test
  void restartHeuristicIsInertWhenATimeBasedDiscardIsArmed() {
    long discardNanos = 1_000_000L;
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, discardNanos);

    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(30_000), 0L);

    // far past 16 consecutive rejections, all within the discard period: every one still drops
    for (int round = 0; round < 40; round++) {
      assertEquals(
          INVALID,
          tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(round % 16), 1_000L + round));
    }

    // the record is still alive and positioned at 30000
    assertEquals(STALE, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(30_000), 2_000L));
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(30_001), 2_001L));
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
  void keepAliveSeedsButAConsistentOneNeverAdvancesAndLastAcceptedTracksAcceptsOnly() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);
    assertNull(tracker.lastAcceptedDataSetMessageSequenceNumber());

    // keep-alive on an unseeded stream seeds to carried-1; it is not an accepted DataSetMessage
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(7), 0L);
    assertNull(tracker.lastAcceptedDataSetMessageSequenceNumber());

    // the data message carrying the keep-alive's next-expected number is NEW
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(7), 1L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(7), 1L);
    assertEquals(uint(7), tracker.lastAcceptedDataSetMessageSequenceNumber());

    // a consistent (NEW-classifying) keep-alive never advances a seeded window: the next data
    // number is still NEW after one
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(8), 2L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(8), 3L));
    assertEquals(uint(7), tracker.lastAcceptedDataSetMessageSequenceNumber());
  }

  /**
   * The §7.2.4.5.8 keep-alive reseed: the carried next-expected value is the publisher's
   * authoritative word on its own counter, so a keep-alive whose carried value classifies STALE or
   * INVALID against the current window reseeds it to {@code carried - 1}. A restarted publisher
   * that emits keep-alives recovers its stream immediately — even with a time-based discard armed,
   * where pre-fix each keep-alive refreshed the discard clock (and never moved the window), wedging
   * the stream forever.
   */
  @Test
  void inconsistentKeepAliveReseedsTheWindowSoARestartedPublisherRecovers() {
    long discardNanos = 1_000_000L;
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, discardNanos);

    // the publisher reached 5000, then restarted; its first keep-alive carries next-expected 1,
    // which classifies STALE against the window — the window, not the keep-alive, is wrong, so
    // the window reseeds to 0
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5_000), 0L);
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(1), 100L);

    // the restarted publisher's first data message, sequence number 1, is accepted (pre-fix it
    // classified STALE) and the stream continues normally
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(1), 200L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(1), 200L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(2), 300L));
  }

  /**
   * The accepted trade-off of the keep-alive reseed: a reordered keep-alive from long ago causes a
   * transient backward reseed, the live data in flight drops as INVALID, and the next current
   * keep-alive — itself inconsistent with the regressed window — reseeds forward, so the stream
   * self-heals at a bounded cost: only the data messages between the two keep-alives are lost.
   */
  @Test
  void reorderedOldKeepAliveCausesABoundedTransientThatSelfHeals() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    // the live stream is at 40000
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(40_000), 0L);

    // a reordered keep-alive from long ago carries next-expected 50 (INVALID against the
    // window): the publisher's word is authoritative, so the window reseeds backward to 49
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(50), 1L);

    // the transient cost: the live data in flight classifies INVALID against the regressed
    // window and drops
    assertEquals(INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(40_001), 2L));
    assertEquals(INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(40_002), 3L));

    // the next current keep-alive (next-expected 40003) is inconsistent with the regressed
    // window and reseeds it forward to 40002 ...
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(40_003), 4L);

    // ... so the live stream is accepted again: the transient cost was two dropped messages
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(40_003), 5L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(40_003), 5L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(40_004), 6L));
  }

  /**
   * A restarted publisher that interleaves keep-alives with data (the post-HG3 steady state)
   * recovers via the keep-alive reseed directly: pre-fix each keep-alive merely cleared the
   * 16-rejection streak — blocking the restart heuristic — and refreshed the discard clock, so with
   * the timeout disabled (and with it armed alike) the stream stayed wedged forever.
   */
  @Test
  void interleavedKeepAlivesRecoverARestartedPublisherViaTheReseedDirectly() {
    var tracker = new ReaderSequenceTracker("conn/RG/R1", 16, 0L);

    // the publisher reached 30000, then restarted at 0
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(30_000), 0L);

    // a few of the restarted publisher's data messages drop, far short of the 16-rejection
    // threshold
    for (int seq = 1; seq <= 4; seq++) {
      assertEquals(INVALID, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(seq), seq));
    }

    // its first interleaved keep-alive (next-expected 5) used to clear the rejection streak —
    // keeping the heuristic forever out of reach — but now reseeds the window directly
    tracker.observeKeepAlive(PUB_A, ushort(10), uint(5), 10L);

    // recovery is immediate: the next data message is accepted
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(5), 11L));
    tracker.acceptDataSetMessage(PUB_A, ushort(10), uint(5), 11L);
    assertEquals(NEW, tracker.classifyDataSetMessage(PUB_A, ushort(10), uint(6), 12L));
  }
}
