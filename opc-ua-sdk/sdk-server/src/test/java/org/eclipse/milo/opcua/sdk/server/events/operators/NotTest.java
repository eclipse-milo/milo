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

public class NotTest {

  @Test
  public void testBooleanOperands() throws Exception {
    assertEquals(Boolean.FALSE, not(true));
    assertEquals(Boolean.TRUE, not(false));
  }

  @Test
  public void testImplicitStringBooleanConversion() throws Exception {
    assertEquals(Boolean.FALSE, not("true"));
    assertEquals(Boolean.TRUE, not("false"));
    assertNull(not("not boolean"));
  }

  @Test
  public void testSingleElementBooleanArray() throws Exception {
    assertEquals(Boolean.FALSE, not(new boolean[] {true}));
  }

  private static Boolean not(Object value) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(value));

    when(context.resolve(op0, eventNode)).thenReturn(value);

    return Operators.NOT.apply(context, eventNode, new FilterOperand[] {op0});
  }
}
