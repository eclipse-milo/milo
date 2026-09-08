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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class LikeTest {

  @Test
  public void testPercentWildcard() throws Exception {
    assertTrue(like("event message", "event%"));
    assertTrue(like("event", "event%"));
    assertFalse(like("message event", "event%"));
  }

  @Test
  public void testLocalizedTextUsesImplicitStringConversion() throws Exception {
    assertTrue(like(LocalizedText.english("event message"), "event%"));
  }

  @Test
  public void testSingleElementStringArrayMatchesAsScalar() throws Exception {
    assertTrue(like(new String[] {"event message"}, "event%"));
  }

  @Test
  public void testMalformedPatternsReturnFalse() throws Exception {
    assertFalse(like("abc", "a[b"));
    assertFalse(like("abc", "abc\\"));
    assertFalse(like("abc", "a[]c"));
  }

  @Test
  public void testNonStringOperandsReturnFalse() throws Exception {
    assertFalse(like(42, "%"));
    assertFalse(like("42", 42));
  }

  @Test
  public void testNullOperandsAreIndeterminate() throws Exception {
    // A null operand is indeterminate (null), consistent with the comparison operators, rather than
    // a non-match (false).
    assertNull(like(null, "event%"));
    assertNull(like("event message", null));
  }

  private static Boolean like(Object value, Object pattern) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(value));
    FilterOperand op1 = new LiteralOperand(new Variant(pattern));

    when(context.resolve(op0, eventNode)).thenReturn(value);
    when(context.resolve(op1, eventNode)).thenReturn(pattern);

    return Operators.LIKE.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
