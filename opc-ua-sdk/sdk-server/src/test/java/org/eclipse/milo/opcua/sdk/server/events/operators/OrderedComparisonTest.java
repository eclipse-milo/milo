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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class OrderedComparisonTest {

  @Test
  public void testDateTimeComparisons() throws Exception {
    assertTrue(apply(Operators.GREATER_THAN, new DateTime(200L), new DateTime(100L)));
    assertTrue(apply(Operators.GREATER_THAN_OR_EQUAL, new DateTime(200L), new DateTime(200L)));
    assertTrue(apply(Operators.LESS_THAN, new DateTime(100L), new DateTime(200L)));
    assertTrue(apply(Operators.LESS_THAN_OR_EQUAL, new DateTime(200L), new DateTime(200L)));
  }

  @Test
  public void testSingleElementArrayComparisons() throws Exception {
    assertTrue(apply(Operators.GREATER_THAN, new int[] {2}, 1));
    assertTrue(apply(Operators.GREATER_THAN_OR_EQUAL, new int[] {1}, 1));
    assertTrue(apply(Operators.LESS_THAN, new int[] {1}, 2));
    assertTrue(apply(Operators.LESS_THAN_OR_EQUAL, new int[] {1}, 1));
  }

  @Test
  public void testNonOrderedValuesReturnFalse() throws Exception {
    assertFalse(apply(Operators.GREATER_THAN, "b", "a"));
  }

  @Test
  public void testNaNReturnsFalse() throws Exception {
    assertFalse(apply(Operators.GREATER_THAN, Double.NaN, 1.0d));
    assertFalse(apply(Operators.LESS_THAN, 1.0d, Double.NaN));
  }

  @Test
  public void testSignedZerosCompareEqual() throws Exception {
    // IEEE semantics: +0.0 and -0.0 are numerically equal, so neither is strictly greater/less,
    // and both "or equal" variants hold (unlike Float.compare/Double.compare, which order -0.0
    // below +0.0).
    assertFalse(apply(Operators.GREATER_THAN, 0.0f, -0.0f));
    assertFalse(apply(Operators.LESS_THAN, -0.0f, 0.0f));
    assertTrue(apply(Operators.GREATER_THAN_OR_EQUAL, -0.0f, 0.0f));
    assertTrue(apply(Operators.LESS_THAN_OR_EQUAL, 0.0f, -0.0f));

    assertFalse(apply(Operators.GREATER_THAN, 0.0d, -0.0d));
    assertFalse(apply(Operators.LESS_THAN, -0.0d, 0.0d));
    assertTrue(apply(Operators.GREATER_THAN_OR_EQUAL, -0.0d, 0.0d));
    assertTrue(apply(Operators.LESS_THAN_OR_EQUAL, 0.0d, -0.0d));
  }

  private static Boolean apply(Operator<Boolean> operator, Object lhs, Object rhs)
      throws Exception {

    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(lhs));
    FilterOperand op1 = new LiteralOperand(new Variant(rhs));

    when(context.resolve(op0, eventNode)).thenReturn(lhs);
    when(context.resolve(op1, eventNode)).thenReturn(rhs);

    return operator.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
