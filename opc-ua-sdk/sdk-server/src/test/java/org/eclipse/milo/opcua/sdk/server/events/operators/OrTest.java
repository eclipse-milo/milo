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

public class OrTest {

  @Test
  public void testTruthTable() throws Exception {
    assertEquals(Boolean.TRUE, or(true, true));
    assertEquals(Boolean.TRUE, or(true, false));
    assertEquals(Boolean.TRUE, or(true, null));

    assertEquals(Boolean.TRUE, or(false, true));
    assertEquals(Boolean.FALSE, or(false, false));
    assertNull(or(false, null));

    assertEquals(Boolean.TRUE, or(null, true));
    assertNull(or(null, false));
    assertNull(or(null, null));
  }

  @Test
  public void testNonBooleanOperandsAreNull() throws Exception {
    assertEquals(Boolean.TRUE, or(true, "not boolean"));
    assertNull(or(false, "not boolean"));
  }

  @Test
  public void testSingleElementBooleanArray() throws Exception {
    assertEquals(Boolean.TRUE, or(new boolean[] {true}, false));
  }

  @Test
  public void testImplicitStringBooleanConversion() throws Exception {
    assertEquals(Boolean.TRUE, or("true", false));
    assertEquals(Boolean.FALSE, or("false", false));
    assertNull(or("not boolean", false));
  }

  private static Boolean or(Object lhs, Object rhs) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(lhs));
    FilterOperand op1 = new LiteralOperand(new Variant(rhs));

    when(context.resolve(op0, eventNode)).thenReturn(lhs);
    when(context.resolve(op1, eventNode)).thenReturn(rhs);

    return Operators.OR.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
