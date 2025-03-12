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

import com.google.common.base.MoreObjects;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public record DateTime(long utcTime) {

  /** Minimum ISO-8601 formatted string. */
  public static final String MIN_ISO_8601_STRING = "0001-01-01T00:00:00Z";

  /** Maximum ISO-8601 formatted string. */
  public static final String MAX_ISO_8601_STRING = "9999-12-31T23:59:59Z";

  /** Minimum {@link Instant} that can be formatted in ISO-8601, i.e. "0001-01-01T00:00:00Z". */
  public static final Instant MIN_ISO_8601_INSTANT = Instant.parse(MIN_ISO_8601_STRING);

  /** Minimum {@link Instant} that can be formatted in ISO-8601, i.e. "0001-01-01T00:00:00Z". */
  public static final Instant MAX_ISO_8601_INSTANT = Instant.parse(MAX_ISO_8601_STRING);

  /** Minimum DateTime that can be represented on this "DevelopmentPlatform". */
  public static final DateTime MIN_DATE_TIME = new DateTime(MIN_ISO_8601_INSTANT);

  /** Maximum DateTime that can be represented on this "DevelopmentPlatform". */
  public static final DateTime MAX_DATE_TIME = new DateTime(MAX_ISO_8601_INSTANT);

  /** When {@code true}, {@link #now()} will use nanosecond precision. */
  public static volatile boolean USE_NANOS = true;

  public static final DateTime MIN_VALUE = new DateTime(0L);

  public static final DateTime NULL_VALUE = MIN_VALUE;

  /**
   * The delta in 100 nanosecond intervals between Java epoch (January 1, 1970) and UTC epoch (Jan
   * 1, 1601).
   */
  private static final long EPOCH_DELTA = 116444736000000000L;

  public DateTime() {
    this(Instant.now());
  }

  public DateTime(Date date) {
    this(date.toInstant());
  }

  public DateTime(Instant instant) {
    this(javaToUtc(instant.toEpochMilli()) + (instant.getNano() % 1_000_000) / 100);
  }

  /**
   * @return this time as 100 nanosecond intervals since UTC epoch.
   */
  public long getUtcTime() {
    return utcTime;
  }

  /**
   * @return this time as milliseconds since Java epoch.
   */
  public long getJavaTime() {
    return getJavaDate().getTime();
  }

  /**
   * @return this time as a {@link Date}.
   */
  public Date getJavaDate() {
    return new Date(utcToJava(utcTime));
  }

  /**
   * @return this time as an {@link Instant}.
   */
  public Instant getJavaInstant() {
    return Instant.ofEpochSecond(utcToJava(utcTime) / 1_000, (utcTime % 10_000_000) * 100);
  }

  public boolean isValid() {
    Instant instant = getJavaInstant();
    return instant.isAfter(MIN_ISO_8601_INSTANT) && instant.isBefore(MAX_ISO_8601_INSTANT);
  }

  public boolean isInvalid() {
    return !isValid();
  }

  @Deprecated
  public boolean isNull() {
    return utcTime == 0;
  }

  @Deprecated
  public boolean isNotNull() {
    return !isNull();
  }

  public String toIso8601String() {
    return DateTimeFormatter.ISO_INSTANT.format(getJavaInstant());
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DateTime dateTime = (DateTime) o;
    return utcTime == dateTime.utcTime;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(utcTime);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("date", getJavaDate())
        .add("instant", getJavaInstant())
        .toString();
  }

  /**
   * Get a {@link DateTime} initialized to now.
   *
   * <p>The precision depends on the value of {@link #USE_NANOS}.
   *
   * @return a {@link DateTime} initialized to now.
   */
  public static DateTime now() {
    if (USE_NANOS) {
      return nowNanos();
    } else {
      return nowMillis();
    }
  }

  /**
   * @return a {@link DateTime} initialized to now with millisecond precision.
   */
  public static DateTime nowMillis() {
    return new DateTime(new Date());
  }

  /**
   * @return a {@link DateTime} initialized to now with nanosecond precision.
   */
  public static DateTime nowNanos() {
    return new DateTime(Instant.now());
  }

  private static long javaToUtc(long javaTime) {
    return (javaTime * 10_000) + EPOCH_DELTA;
  }

  private static long utcToJava(long utcTime) {
    return (utcTime - EPOCH_DELTA) / 10_000;
  }
}
