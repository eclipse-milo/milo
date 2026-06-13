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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.internal.SequenceNumberWindow.Classification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the Part 14 §7.2.3 sequence-number recency classification: {@code result =
 * (received - 1 - lastProcessed) mod 2^N}, NEW below the lower bound 2^(N-2), STALE above the upper
 * bound 2^N - 2^(N-2), INVALID otherwise (both bounds included; Table 152: 16384/49152 for UInt16,
 * 2^30 / 2^32-2^30 for UInt32) — plus the window-record behaviors layered on it: accept-and-seed on
 * first observation, keep-alive seeding to carried-1, the keep-alive contract on a seeded window —
 * never advance on a consistent carried value, reseed to carried-1 on an inconsistent one
 * (§7.2.4.5.8, §7.2.5.4.1: the carried value is the publisher's authoritative next expected
 * sequence number) — always refreshing the record-discard liveness clock, and explicit reset.
 */
class SequenceNumberWindowTest {

  // region pure classification, N=16 (UADP NetworkMessage and DataSetMessage numbers)

  /**
   * Boundary table around last=100 for N=16. The largest accepted forward jump is 16384 (result
   * 16383); results of exactly 16384 (lower bound) and 49152 (upper bound) are INVALID; an exact
   * duplicate computes 65535.
   */
  static Stream<Arguments> uint16Boundaries() {
    return Stream.of(
        // label, lastProcessed, received, expected
        Arguments.of("next in order (result 0)", 100L, 101L, NEW),
        Arguments.of("small gap (loss tolerated)", 100L, 110L, NEW),
        Arguments.of("largest accepted jump, result 16383", 100L, 100L + 16384, NEW),
        Arguments.of("result 16384 = lower bound -> invalid", 100L, 100L + 16385, INVALID),
        Arguments.of("mid invalid band", 100L, 100L + 30000, INVALID),
        Arguments.of("result 49151, still invalid band", 100L, 100L + 49152, INVALID),
        Arguments.of("result 49152 = upper bound -> invalid", 100L, 100L + 49153, INVALID),
        Arguments.of("result 49153, just above upper bound", 100L, 100L + 49154, STALE),
        Arguments.of("exact duplicate (result 65535)", 100L, 100L, STALE),
        Arguments.of("reordered older", 100L, 99L, STALE),
        Arguments.of("much older but within stale band", 100L, 100L - 16382, STALE),
        // values widened beyond 16 bits alias modulo 2^16 (UADP numbers decode into UInteger;
        // N=16 math must still apply)
        Arguments.of("received aliases mod 2^16 (+2^16)", 100L, 101L + 65536, NEW),
        Arguments.of("received aliases mod 2^16 (+2*2^16)", 100L, 101L + 2 * 65536, NEW),
        // rollover (§7.2.3: "Receivers need to be aware of sequence numbers roll over")
        Arguments.of("rollover 0xFFFF -> 0", 0xFFFFL, 0L, NEW),
        Arguments.of("rollover straddling: 0xFFFE -> 1", 0xFFFEL, 1L, NEW),
        Arguments.of("stale across rollover", 2L, 0xFFFFL, STALE));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("uint16Boundaries")
  void classifyUInt16(String label, long lastProcessed, long received, Classification expected) {
    assertEquals(expected, SequenceNumberWindow.classify(lastProcessed, received, 16));
  }

  // endregion

  // region pure classification, N=32 (JSON DataSetMessage numbers; spot checks)

  static Stream<Arguments> uint32Boundaries() {
    long lower = 1L << 30; // 1_073_741_824, Table 152
    long upper = (1L << 32) - lower; // 3_221_225_472, Table 152
    return Stream.of(
        Arguments.of("next in order", 100L, 101L, NEW),
        Arguments.of("largest accepted jump, result lower-1", 100L, 100L + lower, NEW),
        Arguments.of("result = lower bound -> invalid", 100L, 100L + lower + 1, INVALID),
        Arguments.of("result = upper bound -> invalid", 100L, 100L + upper + 1, INVALID),
        Arguments.of("just above upper bound", 100L, 100L + upper + 2, STALE),
        Arguments.of("exact duplicate", 100L, 100L, STALE),
        Arguments.of("rollover 0xFFFFFFFF -> 0", 0xFFFF_FFFFL, 0L, NEW),
        Arguments.of("stale across rollover", 2L, 0xFFFF_FFFFL, STALE));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("uint32Boundaries")
  void classifyUInt32(String label, long lastProcessed, long received, Classification expected) {
    assertEquals(expected, SequenceNumberWindow.classify(lastProcessed, received, 32));
  }

  // endregion

  // region per-stream record: seeding, keep-alives, reset

  @Test
  void unseededWindowClassifiesEverythingAsNewAndAcceptSeeds() {
    var window = new SequenceNumberWindow(16, 0L);
    assertFalse(window.isSeeded());

    // the spec has no rule for the first message of a stream: anything is NEW until seeded
    assertEquals(NEW, window.classify(0));
    assertEquals(NEW, window.classify(12345));
    assertEquals(NEW, window.classify(0xFFFF));

    window.accept(12345, 1L);
    assertTrue(window.isSeeded());
    assertEquals(12345, window.lastProcessed());
    assertEquals(1L, window.lastSeenNanos());

    assertEquals(STALE, window.classify(12345));
    assertEquals(NEW, window.classify(12346));
  }

  @Test
  void acceptMasksValuesToTheBitWidth() {
    var window = new SequenceNumberWindow(16, 0L);
    window.accept(100 + 65536, 0L); // an aliased widened value
    assertEquals(100, window.lastProcessed());
    assertEquals(NEW, window.classify(101));
  }

  @Test
  void keepAliveSeedsAnUnseededWindowToCarriedMinusOne() {
    var window = new SequenceNumberWindow(16, 0L);

    // §7.2.4.5.8: "the sequence number provides the next expected sequence number for the
    // DataSetWriter" — the publisher literally told us next-expected, so seed to carried-1
    // (seeding an unseeded window is not reported as a reseed)
    assertFalse(window.observeKeepAlive(7, 1L));
    assertTrue(window.isSeeded());
    assertEquals(6, window.lastProcessed());
    assertEquals(1L, window.lastSeenNanos());

    // the next data message carries the SAME number the keep-alive carried, and must be NEW
    assertEquals(NEW, window.classify(7));
    assertEquals(STALE, window.classify(6));
  }

  @Test
  void keepAliveCarryingZeroSeedsAcrossTheRollover() {
    var window = new SequenceNumberWindow(16, 0L);
    window.observeKeepAlive(0, 0L);
    assertEquals(0xFFFF, window.lastProcessed());
    assertEquals(NEW, window.classify(0));
  }

  @Test
  void consistentKeepAliveNeverAdvancesASeededWindow() {
    var window = new SequenceNumberWindow(16, 0L);
    window.accept(7, 0L);

    // the off-by-one trap: if the keep-alive (carrying next-expected 8) advanced the window to 8,
    // the next data message — which carries 8 — would compute 2^N-1 and be dropped as a duplicate
    assertFalse(window.observeKeepAlive(8, 1L));
    assertEquals(7, window.lastProcessed());
    assertEquals(NEW, window.classify(8));

    // repeated keep-alives all carry the same next-expected value; none of them move the window,
    // but each refreshes the record-discard liveness clock — a keep-alive is a received message
    // (§7.2.3 discards records when the subscriber does "not receive messages")
    assertFalse(window.observeKeepAlive(8, 2L));
    assertFalse(window.observeKeepAlive(8, 3L));
    assertEquals(7, window.lastProcessed());
    assertEquals(3L, window.lastSeenNanos());
    assertEquals(NEW, window.classify(8));
  }

  @Test
  void inconsistentKeepAliveReseedsASeededWindowToCarriedMinusOne() {
    var window = new SequenceNumberWindow(16, 0L);
    window.accept(5_000, 0L);

    // a keep-alive carrying a STALE-classifying next-expected value (a restarted publisher):
    // §7.2.4.5.8 makes the carried value authoritative, so the window — not the keep-alive — is
    // wrong and reseeds to carried-1, refreshing the liveness clock
    assertTrue(window.observeKeepAlive(1, 1L));
    assertEquals(0, window.lastProcessed());
    assertEquals(1L, window.lastSeenNanos());
    assertEquals(NEW, window.classify(1));

    // an INVALID-classifying carried value reseeds the same way
    assertTrue(window.observeKeepAlive(30_000, 2L));
    assertEquals(29_999, window.lastProcessed());
    assertEquals(NEW, window.classify(30_000));
  }

  @Test
  void resetForgetsTheStreamAndSupportsExplicitReseed() {
    var window = new SequenceNumberWindow(32, 0L);
    window.accept(5000, 0L);
    assertEquals(STALE, window.classify(5000));

    window.reset();
    assertFalse(window.isSeeded());
    assertEquals(NEW, window.classify(5000));

    // the Phase 4 AES-CTR nonce stream shape (§7.2.4.4.3.2): reset, then expect 1 — re-seed via
    // an explicit accept of 0 so the next observation of 1 is NEW
    window.accept(0, 1L);
    assertEquals(NEW, window.classify(1));
    assertEquals(STALE, window.classify(0));
  }

  // endregion
}
