/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

public class OperatorUtilTest {

  @Test
  public void testCommonTypeConversionMixedSignedAndUnsignedNumericOperands() {
    OperatorUtil.BinaryOperands operands = OperatorUtil.convertToCommonType(42, ushort(7));

    assertEquals(OpcUaDataType.Int32, operands.dataType());
    assertEquals(42, operands.operand0());
    assertEquals(7, operands.operand1());
    assertInstanceOf(Integer.class, operands.operand1());
  }

  @Test
  public void testOrderedComparison() {
    assertTrue(OperatorUtil.compareOrdered((byte) -1, (byte) 1) < 0);
    assertTrue(OperatorUtil.compareOrdered(uint(4_000_000_000L), uint(3L)) > 0);
    assertTrue(OperatorUtil.compareOrdered(1.25f, 1.0d) > 0);
    assertTrue(OperatorUtil.compareOrdered(new DateTime(200L), new DateTime(100L)) > 0);
  }

  @Test
  public void testSingleElementArrayConvertsToScalarForCommonTypeOperations() {
    OperatorUtil.BinaryOperands operands = OperatorUtil.convertToCommonType(new int[] {42}, 42);

    assertEquals(OpcUaDataType.Int32, operands.dataType());
    assertEquals(42, operands.operand0());
    assertEquals(42, operands.operand1());

    assertTrue(OperatorUtil.compareOrdered(new int[] {2}, 1) > 0);
  }

  @Test
  public void testArrayConversionFailsWhenAnyElementConversionFails() {
    assertArrayEquals(
        new String[] {"a", "b"},
        (String[]) OperatorUtil.convert(new String[] {"a", "b"}, OpcUaDataType.String));

    assertArrayEquals(
        new Integer[] {1, 2},
        (Integer[]) OperatorUtil.convert(new String[] {"1", "2"}, OpcUaDataType.Int32));

    assertNull(OperatorUtil.convert(new String[] {"1", "not an int"}, OpcUaDataType.Int32));
  }

  @Test
  public void testOrderedComparisonReturnsNullForNonOrderedValues() {
    assertNull(OperatorUtil.compareOrdered("a", "b"));
  }

  @Test
  public void testOrderedComparisonReturnsNullForNaN() {
    assertNull(OperatorUtil.compareOrdered(Float.NaN, 1.0f));
    assertNull(OperatorUtil.compareOrdered(1.0d, Double.NaN));
  }

  @Test
  public void testBitwiseHelperKeepsUnsignedBackingTypes() {
    Object u16 = OperatorUtil.bitwise(ushort(0x00ff), ushort(0x0f0f), (a, b) -> a & b);
    Object u32 = OperatorUtil.bitwise(uint(0x00ffL), uint(0x0f0fL), (a, b) -> a | b);
    Object u64 = OperatorUtil.bitwise(ulong(0x00ffL), ulong(0x0f0fL), (a, b) -> a | b);

    assertInstanceOf(UShort.class, u16);
    assertEquals(ushort(0x000f), u16);

    assertInstanceOf(UInteger.class, u32);
    assertEquals(uint(0x0fffL), u32);

    assertInstanceOf(ULong.class, u64);
    assertEquals(ulong(0x0fffL), u64);
  }

  @Test
  public void testBitwiseHelperRejectsNonIntegerValues() {
    assertNull(OperatorUtil.bitwise(1.0d, 1.0d, (a, b) -> a & b));
  }

  @Test
  public void testBitwiseHelperAcceptsSingleElementIntegerArrays() {
    assertEquals(0b1000, OperatorUtil.bitwise(new int[] {0b1100}, 0b1010, (a, b) -> a & b));
  }
}
