/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

/**
 * Arithmetic on NotificationMessage sequence numbers.
 *
 * <p>OPC UA Part 4 §5.14.1.1: "The value 0 is never used for the sequence number. The first
 * NotificationMessage sent on a Subscription has a sequence number of 1. If the sequence number
 * rolls over, it rolls over to 1."
 *
 * <p>Sequence numbers are therefore a cycle over the values {@code 1..0xFFFFFFFF} — <i>not</i> the
 * plain UInt32 Counter of Part 4 §7.8, whose successor of {@code 0xFFFFFFFF} is 0. Arithmetic that
 * uses the §7.8 rule, or that does not wrap at all, is wrong by one at every rollover.
 */
final class SequenceNumbers {

  /** The sequence number of the first NotificationMessage sent on a Subscription. */
  static final long FIRST = 1L;

  /** The largest legal sequence number. Its successor is {@link #FIRST}. */
  static final long LAST = 0xFFFF_FFFFL;

  /**
   * Not a legal sequence number, and therefore usable as a distinguished "no NotificationMessage
   * has been accounted for yet" marker. {@link #successor(long)} maps it to {@link #FIRST}, which
   * is the sequence number of the first NotificationMessage a Subscription sends; no other
   * operation accepts it.
   */
  static final long NONE = 0L;

  /** The number of distinct legal sequence numbers, i.e. the length of the cycle. */
  private static final long CYCLE = LAST - FIRST + 1;

  /**
   * The largest forward distance still interpreted as "ahead". Beyond the half-way point of the
   * cycle a sequence number is nearer going backwards, and is treated as stale rather than as the
   * far end of an enormous gap.
   */
  private static final long AHEAD_LIMIT = CYCLE / 2;

  private SequenceNumbers() {}

  /**
   * @param sequenceNumber the value to test.
   * @return {@code true} if {@code sequenceNumber} is a legal NotificationMessage sequence number.
   */
  static boolean isLegal(long sequenceNumber) {
    return sequenceNumber >= FIRST && sequenceNumber <= LAST;
  }

  /**
   * Returns the sequence number that follows {@code sequenceNumber}, wrapping to {@link #FIRST} and
   * skipping {@link #NONE}.
   *
   * @param sequenceNumber a legal sequence number, or {@link #NONE} for "nothing accounted for
   *     yet".
   * @return the next sequence number; never {@link #NONE}.
   */
  static long successor(long sequenceNumber) {
    if (sequenceNumber == NONE || sequenceNumber == LAST) {
      return FIRST;
    }
    checkLegal(sequenceNumber);

    return sequenceNumber + 1;
  }

  /**
   * Returns the sequence number that precedes {@code sequenceNumber}, wrapping to {@link #LAST} and
   * skipping {@link #NONE}.
   *
   * @param sequenceNumber a legal sequence number. {@link #NONE} is rejected: nothing precedes "no
   *     messages yet".
   * @return the previous sequence number; never {@link #NONE}.
   */
  static long predecessor(long sequenceNumber) {
    checkLegal(sequenceNumber);

    return sequenceNumber == FIRST ? LAST : sequenceNumber - 1;
  }

  /**
   * Returns the number of steps from {@code from} to {@code to} in the direction sequence numbers
   * advance, i.e. the number of NotificationMessages in the half-open range {@code [from, to)}.
   *
   * @param from a legal sequence number.
   * @param to a legal sequence number.
   * @return the forward distance, in {@code [0, CYCLE)}.
   */
  static long forwardDistance(long from, long to) {
    checkLegal(from);
    checkLegal(to);

    return Math.floorMod(to - from, CYCLE);
  }

  /**
   * Returns whether {@code received} is ahead of {@code expected}, i.e. whether
   * NotificationMessages are missing between them.
   *
   * <p>A sequence number that is neither {@code expected} nor ahead of it is behind it: a stale,
   * duplicated, or reordered message rather than a gap.
   *
   * @param received the sequence number of the NotificationMessage that arrived.
   * @param expected the sequence number of the NotificationMessage that was expected.
   * @return {@code true} if at least one NotificationMessage is missing between them.
   */
  static boolean isAhead(long received, long expected) {
    long distance = forwardDistance(expected, received);

    return distance > 0 && distance <= AHEAD_LIMIT;
  }

  private static void checkLegal(long sequenceNumber) {
    if (!isLegal(sequenceNumber)) {
      throw new IllegalArgumentException("illegal sequenceNumber: " + sequenceNumber);
    }
  }
}
