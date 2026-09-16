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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class BitwiseAndTest {

  @Test
  public void testSignedIntegers() throws Exception {
    assertEquals(0b1000, bitwiseAnd(0b1100, 0b1010));
  }

  @Test
  public void testSingleElementIntegerArray() throws Exception {
    assertEquals(0b1000, bitwiseAnd(new int[] {0b1100}, 0b1010));
  }

  @Test
  public void testUnsignedWrappers() throws Exception {
    Object result = bitwiseAnd(ushort(0x00ff), ushort(0x0f0f));

    assertInstanceOf(UShort.class, result);
    assertEquals(ushort(0x000f), result);
  }

  @Test
  public void testMixedIntegerWidths() throws Exception {
    Object result = bitwiseAnd(ushort(0x00ff), uint(0x0f0fL));

    assertInstanceOf(UInteger.class, result);
    assertEquals(uint(0x000fL), result);
  }

  @Test
  public void testSameWidthSignedAndUnsignedUseUnsignedResultType() throws Exception {
    Object result = bitwiseAnd(uint(0x8000_0000L), -1);

    assertInstanceOf(UInteger.class, result);
    assertEquals(uint(0x8000_0000L), result);
  }

  @Test
  public void testTargetResultType() throws Exception {
    Object result = bitwiseAnd(ulong(0x00ffL), ulong(0x0f0fL));

    assertInstanceOf(ULong.class, result);
    assertEquals(ulong(0x000fL), result);
  }

  @Test
  public void testUInt64HighBitWithSignedInt64() throws Exception {
    Object result = bitwiseAnd(ulong(ULong.MAX_VALUE_LONG), -1L);

    assertInstanceOf(ULong.class, result);
    assertEquals(ulong(ULong.MAX_VALUE_LONG), result);
  }

  @Test
  public void testNullOperandsReturnNull() throws Exception {
    assertNull(bitwiseAnd(null, 1));
    assertNull(bitwiseAnd(1, null));
  }

  @Test
  public void testNonIntegerOperandsReturnNull() throws Exception {
    assertNull(bitwiseAnd(1.0d, 1.0d));
    assertNull(bitwiseAnd(true, 1));
    assertNull(bitwiseAnd("1", 1));
  }

  private static Object bitwiseAnd(Object lhs, Object rhs) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(lhs));
    FilterOperand op1 = new LiteralOperand(new Variant(rhs));

    when(context.resolve(op0, eventNode)).thenReturn(lhs);
    when(context.resolve(op1, eventNode)).thenReturn(rhs);

    return Operators.BITWISE_AND.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
