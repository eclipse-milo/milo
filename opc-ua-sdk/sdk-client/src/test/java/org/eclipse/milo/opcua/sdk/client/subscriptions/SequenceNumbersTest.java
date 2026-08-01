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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * NotificationMessage sequence number arithmetic.
 *
 * <p>Part 4 §5.14.1.1: "The value 0 is never used for the sequence number. The first
 * NotificationMessage sent on a Subscription has a sequence number of 1. If the sequence number
 * rolls over, it rolls over to 1." Sequence numbers are therefore a cycle over {@code
 * 1..0xFFFFFFFF}, and <i>not</i> the plain UInt32 Counter of §7.8 whose successor of {@code
 * 0xFFFFFFFF} is 0 — an implementation that uses the §7.8 rule is wrong by one at every rollover,
 * and one that does not wrap at all stops detecting gaps entirely once it reaches the boundary.
 *
 * <p>The rollover cannot be reached over the wire (a sequence number advances one message at a
 * time), so these unit tests are the only place the boundary behavior can be pinned down.
 */
class SequenceNumbersTest {

  private static final long LAST = 0xFFFF_FFFFL;

  /** The rollover rule of §5.14.1.1: the successor of the largest sequence number is 1, not 0. */
  @Test
  void successorOfTheLargestSequenceNumberIsOne() {
    assertEquals(1L, SequenceNumbers.successor(LAST));
  }

  /** The mirror image of the rollover rule, used to derive the message before a keep-alive. */
  @Test
  void predecessorOfOneIsTheLargestSequenceNumber() {
    assertEquals(LAST, SequenceNumbers.predecessor(1L));
  }

  /**
   * 0 is not a legal sequence number, so it is used as the "nothing accounted for yet" marker; the
   * first NotificationMessage a Subscription sends has sequence number 1, so that is what follows
   * it.
   */
  @Test
  void successorOfNoneIsTheFirstSequenceNumber() {
    assertEquals(SequenceNumbers.FIRST, SequenceNumbers.successor(SequenceNumbers.NONE));
  }

  /**
   * 0 doubles as the "nothing accounted for yet" marker, so producing it from real arithmetic would
   * silently reset a Subscription's gap detection.
   */
  @ParameterizedTest
  @ValueSource(longs = {1L, 2L, 0x8000_0000L, 0xFFFF_FFFEL, LAST})
  void neitherSuccessorNorPredecessorEverProducesZero(long sequenceNumber) {
    assertNotEquals(0L, SequenceNumbers.successor(sequenceNumber));
    assertNotEquals(0L, SequenceNumbers.predecessor(sequenceNumber));
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L, 0x8000_0000L, 0xFFFF_FFFEL, LAST})
  void predecessorUndoesSuccessor(long sequenceNumber) {
    assertEquals(
        sequenceNumber, SequenceNumbers.predecessor(SequenceNumbers.successor(sequenceNumber)));
    assertEquals(
        sequenceNumber, SequenceNumbers.successor(SequenceNumbers.predecessor(sequenceNumber)));
  }

  /**
   * The number of NotificationMessages between two sequence numbers is what bounds Republish
   * recovery; counting one too many at the rollover would request a message that cannot exist.
   */
  @ParameterizedTest(name = "forwardDistance({0}, {1}) == {2}")
  @CsvSource({
    "1, 1, 0",
    "1, 2, 1",
    "5, 10, 5",
    "4294967295, 1, 1", // the rollover is a single step
    "4294967295, 2, 2",
    "4294967294, 1, 2",
    "1, 4294967295, 4294967294", // all the way around, one short of the full cycle
    "2, 1, 4294967294" // backwards by one is almost a full cycle forwards
  })
  void forwardDistanceCountsStepsInTransmissionOrder(long from, long to, long expected) {
    assertEquals(expected, SequenceNumbers.forwardDistance(from, to));
  }

  /**
   * The gap-detection predicate. The wrapped sequence number 1 arriving when {@code 0xFFFFFFFF} was
   * expected means {@code 0xFFFFFFFF} is missing — with unwrapped arithmetic this compares as
   * "behind" and the loss is never noticed.
   */
  @Test
  void aWrappedSequenceNumberIsAheadOfTheExpectedOne() {
    assertTrue(SequenceNumbers.isAhead(1L, LAST));
  }

  @ParameterizedTest(name = "isAhead({0}, {1}) == {2}")
  @CsvSource({
    "1, 1, false", // in sequence
    "2, 1, true", // one message missing
    "10, 6, true", // four messages missing
    "1, 4294967295, true", // the message at the rollover boundary is missing
    "4294967295, 1, false", // stale: 1 was expected, 0xFFFFFFFF is a full cycle behind it
    "6, 10, false", // a duplicate of an already-accounted-for message
    "2147483648, 1, true", // half the cycle ahead is still a gap
    "2147483649, 1, false" // more than half the cycle ahead is nearer going backwards: stale
  })
  void isAheadDistinguishesGapsFromStaleSequenceNumbers(
      long received, long expected, boolean ahead) {

    assertEquals(ahead, SequenceNumbers.isAhead(received, expected));
  }

  /**
   * The composition the Republish recovery loop relies on: {@code forwardDistance} says how many
   * NotificationMessages are missing and {@code successor} enumerates them, so a gap that spans the
   * rollover must yield {@code 0xFFFFFFFF} and 1 — never 0, and never 4294967296.
   */
  @Test
  void aGapSpanningTheRolloverEnumeratesEveryMissingSequenceNumber() {
    long expected = LAST;
    long received = 2L;

    long missingCount = SequenceNumbers.forwardDistance(expected, received);
    var missing = new ArrayList<Long>();

    long sequenceNumber = expected;
    for (long i = 0; i < missingCount; i++) {
      missing.add(sequenceNumber);
      sequenceNumber = SequenceNumbers.successor(sequenceNumber);
    }

    assertEquals(List.of(LAST, 1L), missing);
    assertEquals(
        received,
        sequenceNumber,
        "walking forwardDistance() successors must land exactly on the received sequence number");
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, 1L, 2L, LAST, 0x1_0000_0000L})
  void onlyValuesInOneThroughTheLargestSequenceNumberAreLegal(long sequenceNumber) {
    assertEquals(
        sequenceNumber >= 1L && sequenceNumber <= LAST, SequenceNumbers.isLegal(sequenceNumber));
  }

  /**
   * The "nothing accounted for yet" marker and out-of-range values have no defined arithmetic;
   * failing loudly keeps a bad value from being laundered into a plausible-looking sequence number.
   */
  @Test
  void illegalSequenceNumbersAreRejected() {
    assertThrows(IllegalArgumentException.class, () -> SequenceNumbers.predecessor(0L));
    assertThrows(IllegalArgumentException.class, () -> SequenceNumbers.successor(0x1_0000_0000L));
    assertThrows(IllegalArgumentException.class, () -> SequenceNumbers.forwardDistance(0L, 1L));
    assertThrows(IllegalArgumentException.class, () -> SequenceNumbers.isAhead(1L, 0L));
  }

  @Test
  void theNothingAccountedForYetMarkerIsNotALegalSequenceNumber() {
    assertFalse(SequenceNumbers.isLegal(SequenceNumbers.NONE));
  }
}
