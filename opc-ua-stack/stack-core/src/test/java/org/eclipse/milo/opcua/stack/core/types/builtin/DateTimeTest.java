/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DateTimeTest {

  private static final long EPOCH_DELTA = 116_444_736_000_000_000L;

  @Test
  void getJavaInstant() {
    ZonedDateTime inputZdt =
        LocalDateTime.of(2020, Month.JANUARY, 10, 20, 30, 40, 123_456_789)
            .atZone(ZoneId.systemDefault());

    Instant endInstant = inputZdt.toInstant();

    DateTime dateTime = new DateTime(endInstant);
    Instant outputInstant = dateTime.getJavaInstant();

    ZonedDateTime outputZdt = ZonedDateTime.ofInstant(outputInstant, ZoneId.systemDefault());

    assertEquals(inputZdt.getYear(), outputZdt.getYear());
    assertEquals(inputZdt.getMonth(), outputZdt.getMonth());
    assertEquals(inputZdt.getDayOfMonth(), outputZdt.getDayOfMonth());
    assertEquals(inputZdt.getHour(), outputZdt.getHour());
    assertEquals(inputZdt.getMinute(), outputZdt.getMinute());
    assertEquals(inputZdt.getSecond(), outputZdt.getSecond());
    // we lose precision here because DateTime uses 100-nanosecond intervals
    assertEquals(123_456_700, outputZdt.getNano());
  }

  @Test
  void isNull() {
    DateTime nullDateTime = DateTime.NULL_VALUE;
    DateTime zeroDateTime = new DateTime(0L);

    assertTrue(nullDateTime.isNull());
    assertTrue(zeroDateTime.isNull());
    assertEquals(nullDateTime, zeroDateTime);
  }

  // Part 6 §5.2.2.5: DateTime counts 100-ns ticks since 1601-01-01T00:00:00Z. Converting to Java's
  // 1970 epoch must floor toward negative infinity and avoid signed long underflow at extremes so
  // pre-1970 sub-second values are not shifted +1 s and getJavaTime() always matches Instant.
  @ParameterizedTest
  @MethodSource("boundaryConversionArguments")
  void conversionsAtBoundaryTicksMatchExpectedInstantAndFlooredMillis(
      long utcTime, Instant expectedInstant) {
    DateTime dateTime = new DateTime(utcTime);
    long expectedEpochMilli = expectedInstant.toEpochMilli();

    assertEquals(expectedInstant, dateTime.getJavaInstant());
    assertEquals(DateTimeFormatter.ISO_INSTANT.format(expectedInstant), dateTime.toIso8601String());
    assertEquals(expectedEpochMilli, dateTime.getJavaTime());
    assertEquals(dateTime.getJavaInstant().toEpochMilli(), dateTime.getJavaTime());
    assertEquals(new Date(expectedEpochMilli), dateTime.getJavaDate());
  }

  static Stream<Arguments> boundaryConversionArguments() {
    return Stream.of(
        Arguments.of(Long.MIN_VALUE, Instant.ofEpochSecond(-933_981_677_286L, 522_419_200L)),
        Arguments.of(-1L, Instant.parse("1600-12-31T23:59:59.999999900Z")),
        Arguments.of(0L, Instant.parse("1601-01-01T00:00:00Z")),
        Arguments.of(1L, Instant.parse("1601-01-01T00:00:00.000000100Z")),
        Arguments.of(9_999L, Instant.parse("1601-01-01T00:00:00.000999900Z")),
        Arguments.of(10_000L, Instant.parse("1601-01-01T00:00:00.001Z")),
        Arguments.of(9_999_999L, Instant.parse("1601-01-01T00:00:00.999999900Z")),
        Arguments.of(10_000_000L, Instant.parse("1601-01-01T00:00:01Z")),
        Arguments.of(EPOCH_DELTA - 5_000_000L, Instant.parse("1969-12-31T23:59:59.500Z")),
        Arguments.of(EPOCH_DELTA - 9_999L, Instant.parse("1969-12-31T23:59:59.999000100Z")),
        Arguments.of(EPOCH_DELTA - 1L, Instant.parse("1969-12-31T23:59:59.999999900Z")),
        Arguments.of(EPOCH_DELTA, Instant.parse("1970-01-01T00:00:00Z")),
        Arguments.of(EPOCH_DELTA + 1L, Instant.parse("1970-01-01T00:00:00.000000100Z")),
        Arguments.of(EPOCH_DELTA + 5_000_000L, Instant.parse("1970-01-01T00:00:00.500Z")),
        Arguments.of(Long.MAX_VALUE, Instant.ofEpochSecond(910_692_730_085L, 477_580_700L)));
  }

  // Round-tripping pre-1970 sub-second Instants through DateTime must preserve the exact tick and
  // ISO-8601 text rather than advancing by 1 second on each pass.
  @ParameterizedTest
  @MethodSource("instantRoundTripArguments")
  void instantConstructorRoundTripsAcrossEpochBoundaries(Instant instant, long expectedUtcTime) {
    DateTime dateTime = new DateTime(instant);

    assertEquals(expectedUtcTime, dateTime.getUtcTime());
    assertEquals(instant, dateTime.getJavaInstant());
    assertEquals(DateTimeFormatter.ISO_INSTANT.format(instant), dateTime.toIso8601String());
    assertEquals(instant.toEpochMilli(), dateTime.getJavaTime());
  }

  static Stream<Arguments> instantRoundTripArguments() {
    return Stream.of(
        Arguments.of(Instant.parse("1600-12-31T23:59:59.999999900Z"), -1L),
        Arguments.of(Instant.parse("1601-01-01T00:00:00Z"), 0L),
        Arguments.of(Instant.parse("1601-01-01T00:00:00.000000100Z"), 1L),
        Arguments.of(Instant.parse("1601-01-01T00:00:00.000999900Z"), 9_999L),
        Arguments.of(Instant.parse("1601-01-01T00:00:00.001Z"), 10_000L),
        Arguments.of(Instant.parse("1601-01-01T00:00:00.999999900Z"), 9_999_999L),
        Arguments.of(Instant.parse("1601-01-01T00:00:01Z"), 10_000_000L),
        Arguments.of(Instant.parse("1969-12-31T23:59:59.500Z"), EPOCH_DELTA - 5_000_000L),
        Arguments.of(Instant.parse("1969-12-31T23:59:59.999000100Z"), EPOCH_DELTA - 9_999L),
        Arguments.of(Instant.parse("1969-12-31T23:59:59.999999900Z"), EPOCH_DELTA - 1L),
        Arguments.of(Instant.parse("1970-01-01T00:00:00Z"), EPOCH_DELTA),
        Arguments.of(Instant.parse("1970-01-01T00:00:00.500Z"), EPOCH_DELTA + 5_000_000L));
  }

  // Constructing a DateTime from a pre-1970 Date must keep getJavaDate(), getJavaTime(), and
  // getJavaInstant() consistent with the input timestamp.
  @ParameterizedTest
  @MethodSource("dateRoundTripArguments")
  void dateConstructorRoundTripsAcrossEpochBoundaries(long epochMillis) {
    Date input = new Date(epochMillis);
    DateTime dateTime = new DateTime(input);

    assertEquals(input, dateTime.getJavaDate());
    assertEquals(epochMillis, dateTime.getJavaTime());
    assertEquals(input.toInstant(), dateTime.getJavaInstant());
  }

  static Stream<Arguments> dateRoundTripArguments() {
    return Stream.of(
        Arguments.of(-11_644_473_600_001L),
        Arguments.of(-11_644_473_600_000L),
        Arguments.of(-11_644_473_599_999L),
        Arguments.of(-500L),
        Arguments.of(-1L),
        Arguments.of(0L),
        Arguments.of(500L));
  }

  // Whole-second and millisecond transitions between 1601 and 1970 must remain strictly ordered;
  // truncating division previously caused utcTime = 9_999_999 to sort after utcTime = 10_000_000.
  @ParameterizedTest
  @MethodSource("secondBoundaryTicks")
  void conversionsAreMonotonicAcrossWholeSecondBoundaries(long boundaryUtcTime) {
    DateTime before = new DateTime(boundaryUtcTime - 1L);
    DateTime at = new DateTime(boundaryUtcTime);
    DateTime after = new DateTime(boundaryUtcTime + 1L);

    assertTrue(before.getJavaInstant().isBefore(at.getJavaInstant()));
    assertTrue(at.getJavaInstant().isBefore(after.getJavaInstant()));
    assertEquals(at.getJavaTime() - 1L, before.getJavaTime());
    assertEquals(at.getJavaTime(), after.getJavaTime());
  }

  static Stream<Arguments> secondBoundaryTicks() {
    return Stream.of(
        Arguments.of(0L),
        Arguments.of(10_000_000L),
        Arguments.of(EPOCH_DELTA - 10_000_000L),
        Arguments.of(EPOCH_DELTA));
  }

  // Extreme negative ticks like Long.MIN_VALUE must stay invalid and before MIN_ISO_8601_INSTANT
  // rather than underflowing past MAX_ISO_8601_INSTANT.
  @Test
  void isValidAtRangeBoundaries() {
    DateTime minLong = new DateTime(Long.MIN_VALUE);
    DateTime maxLong = new DateTime(Long.MAX_VALUE);

    assertFalse(minLong.isValid());
    assertTrue(minLong.getJavaInstant().isBefore(DateTime.MIN_ISO_8601_INSTANT));

    assertFalse(DateTime.MIN_DATE_TIME.isValid());
    assertTrue(new DateTime(DateTime.MIN_DATE_TIME.getUtcTime() + 1L).isValid());
    assertTrue(DateTime.NULL_VALUE.isValid());
    assertTrue(new DateTime(1L).isValid());
    assertTrue(new DateTime(EPOCH_DELTA - 1L).isValid());
    assertTrue(new DateTime(DateTime.MAX_DATE_TIME.getUtcTime() - 1L).isValid());
    assertFalse(DateTime.MAX_DATE_TIME.isValid());

    assertFalse(maxLong.isValid());
    assertTrue(maxLong.getJavaInstant().isAfter(DateTime.MAX_ISO_8601_INSTANT));
  }
}
