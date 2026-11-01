/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Field;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReturnDiagnosticsTest {

  // Golden values from the returnDiagnostics table in Part 4 §7.32.
  static Stream<Arguments> bits() {
    return Stream.of(
        Arguments.of(0x001, Level.SERVICE, Field.SYMBOLIC_ID),
        Arguments.of(0x002, Level.SERVICE, Field.LOCALIZED_TEXT),
        Arguments.of(0x004, Level.SERVICE, Field.ADDITIONAL_INFO),
        Arguments.of(0x008, Level.SERVICE, Field.INNER_STATUS_CODE),
        Arguments.of(0x010, Level.SERVICE, Field.INNER_DIAGNOSTICS),
        Arguments.of(0x020, Level.OPERATION, Field.SYMBOLIC_ID),
        Arguments.of(0x040, Level.OPERATION, Field.LOCALIZED_TEXT),
        Arguments.of(0x080, Level.OPERATION, Field.ADDITIONAL_INFO),
        Arguments.of(0x100, Level.OPERATION, Field.INNER_STATUS_CODE),
        Arguments.of(0x200, Level.OPERATION, Field.INNER_DIAGNOSTICS));
  }

  @ParameterizedTest
  @MethodSource("bits")
  void eachBitDecodesToOneFieldAtOneLevel(int bit, Level level, Field field) {
    ReturnDiagnostics decoded = ReturnDiagnostics.decode(uint(bit));

    Level other = level == Level.SERVICE ? Level.OPERATION : Level.SERVICE;

    assertEquals(Set.of(field), decoded.fields(level));
    assertEquals(
        Set.of(), decoded.fields(other), "the bit must not set a field at the other level");
  }

  @ParameterizedTest
  @MethodSource("bits")
  void eachFieldEncodesToItsBit(int bit, Level level, Field field) {
    ReturnDiagnostics single =
        level == Level.SERVICE
            ? new ReturnDiagnostics(Set.of(field), Set.of())
            : new ReturnDiagnostics(Set.of(), Set.of(field));

    assertEquals(uint(bit), single.encode());
  }

  @Test
  void everyMaskRoundTrips() {
    for (int mask = 0; mask < 1024; mask++) {
      assertEquals(
          uint(mask), ReturnDiagnostics.decode(uint(mask)).encode(), "mask 0x%03X".formatted(mask));
    }
  }

  // Part 4 §7.32 defines only the low ten bits. A client that sets others must still get the
  // diagnostics it asked for, not an error.
  @Test
  void decodeIgnoresReservedBits() {
    assertEquals(
        ReturnDiagnostics.decode(uint(0x041)), ReturnDiagnostics.decode(uint(0xFFFF_FC41L)));
    assertSame(ReturnDiagnostics.NONE, ReturnDiagnostics.decode(uint(0xFFFF_FC00L)));
  }

  // A request built by hand may leave the mask null; that means no diagnostics.
  @Test
  void decodeTreatsNullAsNone() {
    assertSame(ReturnDiagnostics.NONE, ReturnDiagnostics.decode(null));
  }

  @Test
  void noneEncodesToZeroAndAllToEveryBit() {
    assertEquals(uint(0), ReturnDiagnostics.NONE.encode());
    assertEquals(uint(0x3FF), ReturnDiagnostics.ALL.encode());
    assertTrue(ReturnDiagnostics.NONE.isEmpty());
  }

  @Test
  void ofRequestsTheSameFieldsAtBothLevels() {
    ReturnDiagnostics rd = ReturnDiagnostics.of(Field.SYMBOLIC_ID, Field.LOCALIZED_TEXT);

    assertEquals(uint(0x063), rd.encode());
  }

  // The record holds immutable copies, so a caller cannot change a mask after handing it over.
  @Test
  void fieldSetsAreImmutableCopies() {
    EnumSet<Field> fields = EnumSet.of(Field.SYMBOLIC_ID);
    var rd = new ReturnDiagnostics(fields, fields);

    fields.add(Field.ADDITIONAL_INFO);

    assertEquals(uint(0x021), rd.encode(), "a later change to the caller's set has no effect");
    assertThrows(
        UnsupportedOperationException.class, () -> rd.serviceLevel().add(Field.ADDITIONAL_INFO));
  }

  @Test
  void ofAcceptsRepeatedFields() {
    assertEquals(
        ReturnDiagnostics.of(Field.SYMBOLIC_ID),
        ReturnDiagnostics.of(Field.SYMBOLIC_ID, Field.SYMBOLIC_ID));
  }
}
