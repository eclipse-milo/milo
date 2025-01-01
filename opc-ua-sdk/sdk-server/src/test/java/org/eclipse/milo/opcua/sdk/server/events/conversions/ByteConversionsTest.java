/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToBoolean;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToDouble;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToFloat;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToInt16;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToInt32;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToInt64;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToSByte;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToString;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToUInt16;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToUInt32;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.byteToUInt64;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ByteConversions.explicitConversion;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.junit.jupiter.api.Test;

public class ByteConversionsTest {

  public static final Object[][] CONVERSIONS =
      new Object[][] {
        {ubyte(0), Boolean.FALSE},
        {ubyte(1), Boolean.TRUE},
        {ubyte(0), 0.0d},
        {ubyte(1), 1.0d},
        {ubyte(0), 0.0f},
        {ubyte(1), 1.0f}
      };

  @Test
  public void testConversions() {
    for (Object[] conversion : CONVERSIONS) {
      UByte b = (UByte) conversion[0];
      Object expected = conversion[1];
      BuiltinDataType targetType = BuiltinDataType.fromBackingClass(expected.getClass());

      assertNotNull(targetType);

      assertEquals(expected, explicitConversion(b, targetType));
    }
  }

  @Test
  public void testByteToBoolean() {
    assertEquals(Boolean.FALSE, byteToBoolean(ubyte(0)));
    assertEquals(Boolean.TRUE, byteToBoolean(ubyte(1)));
  }

  @Test
  public void testByteToDouble() {
    assertEquals(0.0d, byteToDouble(ubyte(0)));
    assertEquals(1.0d, byteToDouble(ubyte(1)));
  }

  @Test
  public void testByteToFloat() {
    assertEquals(0.0f, byteToFloat(ubyte(0)));
    assertEquals(1.0f, byteToFloat(ubyte(1)));
  }

  @Test
  public void testByteToInt16() {
    assertEquals(Short.valueOf((short) 0), byteToInt16(ubyte(0)));
    assertEquals(Short.valueOf((short) 1), byteToInt16(ubyte(1)));
  }

  @Test
  public void testByteToInt32() {
    assertEquals(Integer.valueOf(0), byteToInt32(ubyte(0)));
    assertEquals(Integer.valueOf(1), byteToInt32(ubyte(1)));
  }

  @Test
  public void testByteToInt64() {
    assertEquals(Long.valueOf(0), byteToInt64(ubyte(0)));
    assertEquals(Long.valueOf(1), byteToInt64(ubyte(1)));
  }

  @Test
  public void testByteToSByte() {
    assertEquals(Byte.valueOf((byte) 0), byteToSByte(ubyte(0)));
    assertEquals(Byte.valueOf(Byte.MAX_VALUE), byteToSByte(ubyte(Byte.MAX_VALUE)));
    assertNull(byteToSByte(ubyte(Byte.MAX_VALUE + 1)));
  }

  @Test
  public void testByteToString() {
    assertEquals("0", byteToString(ubyte(0)));
    assertEquals("1", byteToString(ubyte(1)));
  }

  @Test
  public void testByteToUInt16() {
    assertEquals(ushort(UByte.MIN_VALUE), byteToUInt16(UByte.MIN));
    assertEquals(ushort(UByte.MAX_VALUE), byteToUInt16(UByte.MAX));
  }

  @Test
  public void testByteToUInt32() {
    assertEquals(uint(UByte.MIN_VALUE), byteToUInt32(UByte.MIN));
    assertEquals(uint(UByte.MAX_VALUE), byteToUInt32(UByte.MAX));
  }

  @Test
  public void testByteToUInt64() {
    assertEquals(ulong(UByte.MIN_VALUE), byteToUInt64(UByte.MIN));
    assertEquals(ulong(UByte.MAX_VALUE), byteToUInt64(UByte.MAX));
  }
}
