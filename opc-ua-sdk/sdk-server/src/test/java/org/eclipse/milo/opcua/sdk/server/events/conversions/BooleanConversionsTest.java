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

import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToByte;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToDouble;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToFloat;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToInt16;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToInt32;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToInt64;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToSByte;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToString;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToUInt16;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToUInt32;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.BooleanConversions.booleanToUInt64;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BooleanConversionsTest {

  @Test
  public void testBooleanToByte() {
    assertEquals(ubyte(0), booleanToByte(false));
    assertEquals(ubyte(1), booleanToByte(true));
  }

  @Test
  public void testBooleanToDouble() {
    assertEquals(0.0, booleanToDouble(false));
    assertEquals(1.0, booleanToDouble(true));
  }

  @Test
  public void testBooleanToFloat() {
    assertEquals(0.0f, booleanToFloat(false));
    assertEquals(1.0f, booleanToFloat(true));
  }

  @Test
  public void testBooleanToInt16() {
    assertEquals(0, (short) booleanToInt16(false));
    assertEquals(1, (short) booleanToInt16(true));
  }

  @Test
  public void testBooleanToInt32() {
    assertEquals(0, (int) booleanToInt32(false));
    assertEquals(1, (int) booleanToInt32(true));
  }

  @Test
  public void testBooleanToInt64() {
    assertEquals(0, (long) booleanToInt64(false));
    assertEquals(1, (long) booleanToInt64(true));
  }

  @Test
  public void testBooleanToSByte() {
    assertEquals(0, (byte) booleanToSByte(false));
    assertEquals(1, (byte) booleanToSByte(true));
  }

  @Test
  public void testBooleanToString() {
    assertEquals("1", booleanToString(true));
    assertEquals("0", booleanToString(false));
  }

  @Test
  public void testBooleanToUInt16() {
    assertEquals(ushort(0), booleanToUInt16(false));
    assertEquals(ushort(1), booleanToUInt16(true));
  }

  @Test
  public void testBooleanToUInt32() {
    assertEquals(uint(0), booleanToUInt32(false));
    assertEquals(uint(1), booleanToUInt32(true));
  }

  @Test
  public void testBooleanToUInt64() {
    assertEquals(ulong(0), booleanToUInt64(false));
    assertEquals(ulong(1), booleanToUInt64(true));
  }
}
