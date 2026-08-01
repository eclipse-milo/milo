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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UShortTest {

  // Namespace indexes are UShorts and are almost always small, so the low values are allocated
  // constantly during decoding. Sharing instances is the whole point of the cache.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 255})
  void valueOfSharesInstancesWithinTheCachedRange(int value) {
    assertSame(UShort.valueOf(value), UShort.valueOf(value));
    assertSame(UShort.valueOf(value), UShort.valueOf((long) value));
    assertSame(UShort.valueOf(value), UShort.valueOf((short) value));
  }

  // The cache is initialized before MIN, so MIN must be the shared zero instance rather than a
  // separate one. Reordering the static fields would silently break that.
  @Test
  void minIsTheCachedZeroInstance() {
    assertSame(UShort.valueOf(0), UShort.MIN);
  }

  // Values beyond the cache are still correct, just not shared; equality must stay value-based.
  @Test
  void valueOfOutsideTheCachedRangeStillComparesByValue() {
    UShort a = UShort.valueOf(4096);
    UShort b = UShort.valueOf(4096);

    assertNotSame(a, b);
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  // valueOf(short) masks rather than range checks, so (short) -1 is 65535 and not an error.
  @Test
  void valueOfShortMasksNegativeValues() {
    assertEquals(UShort.MAX_VALUE, UShort.valueOf((short) -1).intValue());
    assertEquals(UShort.MAX, UShort.valueOf((short) -1));
  }

  @Test
  void valueOfRejectsValuesOutsideUnsignedShortRange() {
    assertThrows(NumberFormatException.class, () -> UShort.valueOf(-1));
    assertThrows(NumberFormatException.class, () -> UShort.valueOf(UShort.MAX_VALUE + 1));
    assertThrows(NumberFormatException.class, () -> UShort.valueOf(-1L));
    assertThrows(NumberFormatException.class, () -> UShort.valueOf(UShort.MAX_VALUE + 1L));
    assertThrows(NumberFormatException.class, () -> UShort.valueOf("65536"));
    assertThrows(NumberFormatException.class, () -> UShort.valueOf("not a number"));
  }

  @Test
  void valueOfStringParsesInRangeValues() {
    assertEquals(UShort.MIN, UShort.valueOf("0"));
    assertEquals(UShort.MAX, UShort.valueOf("65535"));
  }

  // readResolve keeps deserialization from smuggling in a duplicate of a cached value, which would
  // make reference comparisons behave differently depending on how the instance was obtained.
  @Test
  void deserializationResolvesToTheCachedInstance() throws Exception {
    UShort original = UShort.valueOf(7);

    var bytes = new ByteArrayOutputStream();
    try (var out = new ObjectOutputStream(bytes)) {
      out.writeObject(original);
    }

    Object restored;
    try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
      restored = in.readObject();
    }

    assertSame(original, restored);
  }
}
