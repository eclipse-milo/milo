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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class AndTest {

  @Test
  public void testTruthTable() throws Exception {
    assertEquals(Boolean.TRUE, and(true, true));
    assertEquals(Boolean.FALSE, and(true, false));
    assertNull(and(true, null));

    assertEquals(Boolean.FALSE, and(false, true));
    assertEquals(Boolean.FALSE, and(false, false));
    assertEquals(Boolean.FALSE, and(false, null));

    assertNull(and(null, true));
    assertEquals(Boolean.FALSE, and(null, false));
    assertNull(and(null, null));
  }

  @Test
  public void testNonBooleanOperandsAreNull() throws Exception {
    assertNull(and(true, "not boolean"));
    assertEquals(Boolean.FALSE, and(false, "not boolean"));
  }

  @Test
  public void testSingleElementBooleanArray() throws Exception {
    assertEquals(Boolean.TRUE, and(new boolean[] {true}, true));
  }

  @Test
  public void testImplicitStringBooleanConversion() throws Exception {
    assertEquals(Boolean.TRUE, and("true", true));
    assertEquals(Boolean.FALSE, and("false", true));
    assertNull(and("not boolean", true));
  }

  private static Boolean and(Object lhs, Object rhs) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(lhs));
    FilterOperand op1 = new LiteralOperand(new Variant(rhs));

    when(context.resolve(op0, eventNode)).thenReturn(lhs);
    when(context.resolve(op1, eventNode)).thenReturn(rhs);

    return Operators.AND.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
