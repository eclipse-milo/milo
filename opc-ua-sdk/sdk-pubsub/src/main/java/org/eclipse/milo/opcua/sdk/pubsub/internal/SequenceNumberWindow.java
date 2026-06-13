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

/**
 * A Part 14 §7.2.3 sequence-number recency window over one stream of monotonically increasing,
 * wrapping sequence numbers of bit width N.
 *
 * <p>§7.2.3: "To determine whether a received message is newer than the last processed message the
 * following formula shall be used: (received sequence number - 1 - last processed sequence number)
 * modulo 2^N". "Results below the lower bound indicate that the received message is newer than the
 * last processed message and it shall be processed. Results above the upper bound indicate that the
 * received message is older than (or same as) than the last processed message and it shall be
 * ignored unless reordering of messages is required. Other results are invalid and the message
 * shall be ignored." The lower bound is 2^(N-2), the upper bound 2^N - 2^(N-2) (Table 152: 16384
 * and 49152 for UInt16, 2^30 and 2^32-2^30 for UInt32); results equal to either bound fall in
 * "other results" and are invalid. Milo does not implement the optional reordering, so both non-new
 * buckets are dropped by callers.
 *
 * <p>The classification itself is the pure function {@link #classify(long, long, int)} over (last
 * processed, received, N); an instance adds only the small resettable per-stream record: the window
 * position (seeded flag, last processed value) and a separate liveness clock ({@link
 * #lastSeenNanos()}) for the §7.2.3 record-discard rule. It carries no reader- or
 * transport-specific state, so the same class serves UADP NetworkMessage and DataSetMessage numbers
 * (N=16), JSON DataSetMessage numbers (N=32), and — in a later phase, after signature verification
 * — the AES-CTR MessageNonce sequence (N=32, explicitly re-seeded on key change via {@link
 * #reset()} and {@link #accept(long, long)}).
 *
 * <p>An unseeded window classifies every value as {@link Classification#NEW}: the spec defines no
 * rule for the first message of a stream (the publisher may have started long before the
 * subscriber), so the first observation is accepted and seeds the window. A keep-alive carries the
 * <em>next expected</em> sequence number without consuming it (§7.2.4.5.8, §7.2.5.4.1): {@link
 * #observeKeepAlive(long, long)} seeds an unseeded window to {@code carried - 1}, and on a seeded
 * window it never advances on consistent ({@link Classification#NEW}-classifying) keep-alives —
 * advancing would make the next data message, which carries the same number, classify as a
 * duplicate — but reseeds to {@code carried - 1} on inconsistent (stale- or invalid-classifying)
 * ones, per §7.2.4.5.8: the carried value is the publisher's authoritative next expected sequence
 * number, so a carried value the window would reject means the window, not the keep-alive, is wrong
 * (typically a restarted publisher). It always refreshes the liveness clock: §7.2.3 discards
 * records when the subscriber does "not receive messages", and a keep-alive is a received message.
 *
 * <p>Not thread safe; confine each instance to one dispatch thread.
 */
final class SequenceNumberWindow {

  /** The §7.2.3 recency buckets of a received sequence number. */
  enum Classification {
    /** Newer than the last processed message: "it shall be processed". */
    NEW,
    /** "Older than (or same as)" the last processed message: dropped (no reordering support). */
    STALE,
    /** Neither provably newer nor older: "the message shall be ignored". */
    INVALID
  }

  private final int bitWidth;
  private final long valueMask;

  private boolean seeded = false;
  private long lastProcessed = 0L;
  private long lastSeenNanos;
  private int consecutiveRejections = 0;

  /**
   * @param bitWidth the wire width N of the sequence number (16 or 32).
   * @param nowNanos the current {@link System#nanoTime()}, the liveness clock's initial value.
   */
  SequenceNumberWindow(int bitWidth, long nowNanos) {
    this.bitWidth = bitWidth;
    this.valueMask = (1L << bitWidth) - 1;
    this.lastSeenNanos = nowNanos;
  }

  /**
   * Classify {@code received} against the last processed sequence number without advancing the
   * window. An unseeded window classifies everything as {@link Classification#NEW}.
   */
  Classification classify(long received) {
    if (!seeded) {
      return Classification.NEW;
    }
    return classify(lastProcessed, received, bitWidth);
  }

  /**
   * Record that the message carrying {@code received} was accepted: seeds or advances the window
   * and refreshes the liveness clock used by the §7.2.3 record-discard rule.
   */
  void accept(long received, long nowNanos) {
    seeded = true;
    lastProcessed = received & valueMask;
    lastSeenNanos = nowNanos;
    consecutiveRejections = 0;
  }

  /**
   * Observe a keep-alive carrying the next expected sequence number (§7.2.4.5.8, §7.2.5.4.1): seeds
   * an unseeded window to {@code nextExpected - 1}; never advances a seeded window on a consistent
   * ({@link Classification#NEW}-classifying) carried value, but reseeds it to {@code nextExpected -
   * 1} when the carried value classifies stale or invalid — the publisher is authoritative about
   * its own counter, so an inconsistent keep-alive means the window is wrong (typically a restarted
   * publisher). Always refreshes the liveness clock — a keep-alive is a received message for the
   * §7.2.3 record-discard rule, which fires only when the subscriber does "not receive messages".
   *
   * @return {@code true} if a seeded window was reseeded from an inconsistent carried value, so the
   *     caller can surface the occurrence.
   */
  boolean observeKeepAlive(long nextExpected, long nowNanos) {
    boolean reseeded = false;
    if (!seeded) {
      seeded = true;
      lastProcessed = (nextExpected - 1) & valueMask;
    } else if (classify(lastProcessed, nextExpected, bitWidth) != Classification.NEW) {
      // §7.2.4.5.8: the keep-alive "provides the next expected sequence number for the
      // DataSetWriter" — a carried value this window would reject means the window, not the
      // keep-alive, is wrong (restarted publisher); reseed so the stream recovers
      lastProcessed = (nextExpected - 1) & valueMask;
      reseeded = true;
    }
    lastSeenNanos = nowNanos;
    consecutiveRejections = 0;
    return reseeded;
  }

  /** Forget the stream state: the next observation re-seeds the window. */
  void reset() {
    seeded = false;
    lastProcessed = 0L;
    consecutiveRejections = 0;
  }

  /**
   * Record that the message just classified STALE/INVALID against this window was rejected, and
   * return the length of the current uninterrupted rejection streak. The streak is cleared by any
   * {@link #accept(long, long)}, {@link #observeKeepAlive(long, long)}, or {@link #reset()}; it
   * feeds the {@code ReaderSequenceTracker} restart-recovery heuristic, which applies only when no
   * time-based record discard is armed.
   */
  int recordRejection() {
    return ++consecutiveRejections;
  }

  /** Whether the window has processed (or been seeded by) an observation since the last reset. */
  boolean isSeeded() {
    return seeded;
  }

  /** The last processed sequence number; meaningful only while {@link #isSeeded()}. */
  long lastProcessed() {
    return lastProcessed;
  }

  /**
   * The §7.2.3 record-discard liveness clock: the {@link System#nanoTime()} of the last accepted
   * message or keep-alive observation. Rejected (stale/invalid) observations do not refresh it.
   */
  long lastSeenNanos() {
    return lastSeenNanos;
  }

  /**
   * The pure §7.2.3 recency classification: {@code result = (received - 1 - lastProcessed) mod
   * 2^N}; {@code result < 2^(N-2)} is {@link Classification#NEW}, {@code result > 2^N - 2^(N-2)} is
   * {@link Classification#STALE}, anything else — both bounds included — is {@link
   * Classification#INVALID}.
   *
   * <p>Inputs are taken modulo 2^N, so UADP UInt16 values widened into wider Java types classify
   * with N=16 math (e.g. the 0xFFFF → 0 rollover is NEW).
   *
   * @param lastProcessed the last processed sequence number of the stream.
   * @param received the received sequence number.
   * @param bitWidth the wire width N of the sequence number (16 or 32).
   * @return the classification of {@code received}.
   */
  static Classification classify(long lastProcessed, long received, int bitWidth) {
    long valueMask = (1L << bitWidth) - 1;
    long result = (received - 1 - lastProcessed) & valueMask;

    long lowerBound = 1L << (bitWidth - 2);
    long upperBound = (valueMask + 1) - lowerBound;

    if (result < lowerBound) {
      return Classification.NEW;
    }
    if (result > upperBound) {
      return Classification.STALE;
    }
    return Classification.INVALID;
  }
}
