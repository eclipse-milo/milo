/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.testng.annotations.Test;

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

      assertEquals(explicitConversion(b, targetType), expected);
    }
  }

  @Test
  public void testByteToBoolean() {
    assertEquals(byteToBoolean(ubyte(0)), Boolean.FALSE);
    assertEquals(byteToBoolean(ubyte(1)), Boolean.TRUE);
  }

  @Test
  public void testByteToDouble() {
    assertEquals(byteToDouble(ubyte(0)), 0.0d);
    assertEquals(byteToDouble(ubyte(1)), 1.0d);
  }

  @Test
  public void testByteToFloat() {
    assertEquals(byteToFloat(ubyte(0)), 0.0f);
    assertEquals(byteToFloat(ubyte(1)), 1.0f);
  }

  @Test
  public void testByteToInt16() {
    assertEquals(byteToInt16(ubyte(0)), Short.valueOf((short) 0));
    assertEquals(byteToInt16(ubyte(1)), Short.valueOf((short) 1));
  }

  @Test
  public void testByteToInt32() {
    assertEquals(byteToInt32(ubyte(0)), Integer.valueOf(0));
    assertEquals(byteToInt32(ubyte(1)), Integer.valueOf(1));
  }

  @Test
  public void testByteToInt64() {
    assertEquals(byteToInt64(ubyte(0)), Long.valueOf(0));
    assertEquals(byteToInt64(ubyte(1)), Long.valueOf(1));
  }

  @Test
  public void testByteToSByte() {
    assertEquals(byteToSByte(ubyte(0)), Byte.valueOf((byte) 0));
    assertEquals(byteToSByte(ubyte(Byte.MAX_VALUE)), Byte.valueOf(Byte.MAX_VALUE));
    assertNull(byteToSByte(ubyte(Byte.MAX_VALUE + 1)));
  }

  @Test
  public void testByteToString() {
    assertEquals(byteToString(ubyte(0)), "0");
    assertEquals(byteToString(ubyte(1)), "1");
  }

  @Test
  public void testByteToUInt16() {
    assertEquals(byteToUInt16(UByte.MIN), ushort(UByte.MIN_VALUE));
    assertEquals(byteToUInt16(UByte.MAX), ushort(UByte.MAX_VALUE));
  }

  @Test
  public void testByteToUInt32() {
    assertEquals(byteToUInt32(UByte.MIN), uint(UByte.MIN_VALUE));
    assertEquals(byteToUInt32(UByte.MAX), uint(UByte.MAX_VALUE));
  }

  @Test
  public void testByteToUInt64() {
    assertEquals(byteToUInt64(UByte.MIN), ulong(UByte.MIN_VALUE));
    assertEquals(byteToUInt64(UByte.MAX), ulong(UByte.MAX_VALUE));
  }
}
