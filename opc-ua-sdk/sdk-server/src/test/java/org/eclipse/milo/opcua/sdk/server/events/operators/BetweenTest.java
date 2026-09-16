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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class BetweenTest {

  @Test
  public void testInclusiveBounds() throws Exception {
    assertTrue(between(1, 1, 3));
    assertTrue(between(3, 1, 3));
  }

  @Test
  public void testBelowAndAboveRange() throws Exception {
    assertFalse(between(0, 1, 3));
    assertFalse(between(4, 1, 3));
  }

  @Test
  public void testMixedNumericImplicitConversion() throws Exception {
    assertTrue(between(ushort(2), 1, 3));
  }

  @Test
  public void testSingleElementArraysConvertToScalars() throws Exception {
    assertTrue(between(new int[] {2}, new int[] {1}, new int[] {3}));
  }

  @Test
  public void testAllOperandsMustConvertToOneCommonType() throws Exception {
    assertFalse(between(ulong(5L), -1L, ulong(ULong.MAX_VALUE_LONG)));
  }

  @Test
  public void testUInt32AndUInt64() throws Exception {
    assertTrue(between(uint(4_000_000_000L), uint(3_000_000_000L), uint(4_200_000_000L)));
    assertTrue(between(ulong(4_000_000_000L), ulong(3_000_000_000L), ulong(4_200_000_000L)));
  }

  @Test
  public void testDateTime() throws Exception {
    assertTrue(between(new DateTime(200L), new DateTime(100L), new DateTime(300L)));
  }

  @Test
  public void testNullOperandReturnsNull() throws Exception {
    assertNull(between(null, 1, 3));
    assertNull(between(2, null, 3));
    assertNull(between(2, 1, null));
  }

  @Test
  public void testNonOrderedValuesReturnFalse() throws Exception {
    assertFalse(between("b", "a", "c"));
  }

  @Test
  public void testNaNReturnsFalse() throws Exception {
    assertFalse(between(Double.NaN, 1.0d, 3.0d));
    assertFalse(between(2.0d, 1.0d, Double.NaN));
  }

  private static Boolean between(Object value, Object lowerBound, Object upperBound)
      throws Exception {

    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(value));
    FilterOperand op1 = new LiteralOperand(new Variant(lowerBound));
    FilterOperand op2 = new LiteralOperand(new Variant(upperBound));

    when(context.resolve(op0, eventNode)).thenReturn(value);
    when(context.resolve(op1, eventNode)).thenReturn(lowerBound);
    when(context.resolve(op2, eventNode)).thenReturn(upperBound);

    return Operators.BETWEEN.apply(context, eventNode, new FilterOperand[] {op0, op1, op2});
  }
}
