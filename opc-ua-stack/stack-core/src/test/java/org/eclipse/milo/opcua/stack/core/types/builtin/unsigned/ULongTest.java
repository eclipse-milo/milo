/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin.unsigned;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ULongTest {

  private static final BigInteger TWO_POW_64 = BigInteger.ONE.shiftLeft(64);

  // Values at or above 2^63 have the sign bit set in the backing long. Subtracting from one of them
  // used to throw whenever the result dropped below 2^63, which is a valid result.
  @ParameterizedTest
  @CsvSource({
    "18446744073709551615, 18446744073709551615, 0",
    "9223372036854775808, 9223372036854775808, 0",
    "9223372036854775808, 1, 9223372036854775807",
    "18446744073709551615, 9223372036854775808, 9223372036854775807",
  })
  void subtractReturnsResultsBelowTheSignedBoundary(
      String minuend, String subtrahend, String expected) {
    assertEquals(u(expected), u(minuend).subtract(u(subtrahend)));
  }

  @ParameterizedTest
  @CsvSource({
    "9223372036854775808, 1, 9223372036854775807",
    "18446744073709551615, 9223372036854775807, 9223372036854775808",
  })
  void subtractLongReturnsResultsBelowTheSignedBoundary(
      String minuend, long subtrahend, String expected) {
    assertEquals(u(expected), u(minuend).subtract(subtrahend));
  }

  // add(long) with a negative argument is a subtraction, so it hit the same bug.
  @Test
  void addNegativeLongSubtracts() {
    assertEquals(u("9223372036854775807"), u("9223372036854775808").add(-1L));
    assertEquals(u("0"), u("9223372036854775807").add(Long.MIN_VALUE + 1));
  }

  // Math.abs(Long.MIN_VALUE) is still negative, so add(long) and subtract(long) used to hand
  // Long.MIN_VALUE back and forth until the stack overflowed.
  @Test
  void addAndSubtractAcceptLongMinValue() {
    assertEquals(u("9223372036854775807"), ULong.MAX.add(Long.MIN_VALUE));
    assertEquals(u("9223372036854775808"), ULong.MIN.subtract(Long.MIN_VALUE));
    assertEquals(u("18446744073709551615"), u("9223372036854775807").subtract(Long.MIN_VALUE));
  }

  @Test
  void addRejectsOverflow() {
    assertThrows(NumberFormatException.class, () -> ULong.MAX.add(1));
    assertThrows(NumberFormatException.class, () -> ULong.MAX.add(u("1")));
    assertThrows(
        NumberFormatException.class, () -> u("9223372036854775808").add(u("9223372036854775808")));
    assertThrows(NumberFormatException.class, () -> ULong.MAX.subtract(Long.MIN_VALUE));
  }

  @Test
  void subtractRejectsUnderflow() {
    assertThrows(NumberFormatException.class, () -> ULong.MIN.subtract(1));
    assertThrows(NumberFormatException.class, () -> u("1").subtract(u("2")));
    assertThrows(NumberFormatException.class, () -> ULong.MIN.add(Long.MIN_VALUE));
    assertThrows(NumberFormatException.class, () -> u("1").add(-2L));
  }

  // The named cases above pin the boundary; this checks the whole value space against BigInteger
  // so the sign-bit handling cannot regress somewhere the named cases do not reach.
  @Test
  void arithmeticMatchesBigInteger() {
    var random = new Random(1);

    for (int i = 0; i < 10_000; i++) {
      long a = random.nextLong();
      long b = random.nextLong();
      ULong ua = ULong.valueOf(a);
      ULong ub = ULong.valueOf(b);

      assertMatches(ua.toBigInteger().add(ub.toBigInteger()), () -> ua.add(ub));
      assertMatches(ua.toBigInteger().subtract(ub.toBigInteger()), () -> ua.subtract(ub));
      assertMatches(ua.toBigInteger().add(BigInteger.valueOf(b)), () -> ua.add(b));
      assertMatches(ua.toBigInteger().subtract(BigInteger.valueOf(b)), () -> ua.subtract(b));
    }
  }

  private static void assertMatches(BigInteger expected, Supplier<ULong> operation) {
    if (expected.signum() >= 0 && expected.compareTo(TWO_POW_64) < 0) {
      assertEquals(expected, operation.get().toBigInteger());
    } else {
      assertThrows(NumberFormatException.class, operation::get, "expected " + expected);
    }
  }

  private static ULong u(String value) {
    return ULong.valueOf(value);
  }
}
