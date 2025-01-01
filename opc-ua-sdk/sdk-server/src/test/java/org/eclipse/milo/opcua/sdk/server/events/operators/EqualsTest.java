/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class EqualsTest {

  @Test
  public void testArray() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(new int[] {1, 2, 3}));
    FilterOperand op1 = new LiteralOperand(new Variant(new int[] {1, 2, 3}));

    when(context.resolve(op0, eventNode)).thenReturn(new int[] {1, 2, 3});

    when(context.resolve(op1, eventNode)).thenReturn(new int[] {1, 2, 3});

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testScalar() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(42));
    FilterOperand op1 = new LiteralOperand(new Variant(42));

    when(context.resolve(op0, eventNode)).thenReturn(42);

    when(context.resolve(op1, eventNode)).thenReturn(42);

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testArrayWithImplicitConversion() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(new int[] {1, 2, 3}));
    FilterOperand op1 = new LiteralOperand(new Variant(new long[] {1L, 2L, 3L}));

    when(context.resolve(op0, eventNode)).thenReturn(new int[] {1, 2, 3});

    when(context.resolve(op1, eventNode)).thenReturn(new Long[] {1L, 2L, 3L});

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testScalarWithImplicitConversion() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(42));
    FilterOperand op1 = new LiteralOperand(new Variant(42L));

    when(context.resolve(op0, eventNode)).thenReturn(42);
    when(context.resolve(op1, eventNode)).thenReturn(42L);

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testArrayNotEqual() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(new int[] {1, 2, 3}));
    FilterOperand op1 = new LiteralOperand(new Variant(new int[] {3, 2, 1}));

    when(context.resolve(op0, eventNode)).thenReturn(new int[] {1, 2, 3});

    when(context.resolve(op1, eventNode)).thenReturn(new int[] {3, 2, 1});

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  public void testScalarNotEqual() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(1));
    FilterOperand op1 = new LiteralOperand(new Variant(2));

    when(context.resolve(op0, eventNode)).thenReturn(1);

    when(context.resolve(op1, eventNode)).thenReturn(2);

    Boolean result = Operators.EQUALS.apply(context, eventNode, new FilterOperand[] {op0, op1});

    assertNotNull(result);
    assertFalse(result);
  }
}
