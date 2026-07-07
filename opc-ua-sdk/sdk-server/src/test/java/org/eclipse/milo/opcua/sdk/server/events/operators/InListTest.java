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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class InListTest {

  @Test
  public void testMatchesFirstCandidate() throws Exception {
    assertTrue(inList("a", "a", "b", "c"));
  }

  @Test
  public void testMatchesMiddleCandidate() throws Exception {
    assertTrue(inList("b", "a", "b", "c"));
  }

  @Test
  public void testMatchesLastCandidate() throws Exception {
    assertTrue(inList("c", "a", "b", "c"));
  }

  @Test
  public void testNoMatchReturnsFalse() throws Exception {
    assertFalse(inList("d", "a", "b", "c"));
  }

  @Test
  public void testMixedNumericImplicitConversion() throws Exception {
    assertTrue(inList(42, 1L, 42L, 100L));
  }

  @Test
  public void testSingleElementArrayUsesEqualsSemantics() throws Exception {
    assertTrue(inList(new int[] {2}, 1, 2, 3));
  }

  @Test
  public void testNullTestedValueReturnsNullWhenNothingMatches() throws Exception {
    assertNull(inList(null, 1, 2, 3));
  }

  @Test
  public void testMoreThanTwoListOperands() throws Exception {
    assertTrue(inList(4, 1, 2, 3, 4, 5));
  }

  @Test
  public void testNodeIdMatchesExpandedNodeIdCandidate() throws Exception {
    assertTrue(inList(NodeIds.ExclusiveLimitAlarmType, ExpandedNodeId.parse("i=9341")));
  }

  private static Boolean inList(Object value, Object... candidates) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand[] operands = new FilterOperand[candidates.length + 1];
    Object[] values = new Object[candidates.length + 1];

    operands[0] = new LiteralOperand(new Variant(value));
    values[0] = value;

    for (int i = 0; i < candidates.length; i++) {
      operands[i + 1] = new LiteralOperand(new Variant(candidates[i]));
      values[i + 1] = candidates[i];
    }

    for (int i = 0; i < operands.length; i++) {
      when(context.resolve(operands[i], eventNode)).thenReturn(values[i]);
    }

    return Operators.IN_LIST.apply(context, eventNode, operands);
  }
}
