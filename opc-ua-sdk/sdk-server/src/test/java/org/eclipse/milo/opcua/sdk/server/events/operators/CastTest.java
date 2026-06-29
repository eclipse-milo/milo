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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class CastTest {

  @Test
  public void testNodeIdTargetDataType() throws Exception {
    assertEquals(42, cast("42", NodeIds.Int32));
  }

  @Test
  public void testSameTypeCastReturnsSourceValue() throws Exception {
    assertEquals(42, cast(42, NodeIds.Int32));
    assertEquals("event message", cast("event message", NodeIds.String));
    assertEquals(ushort(2), cast(ushort(2), NodeIds.UInt16));
  }

  @Test
  public void testExpandedNodeIdTargetDataType() throws Exception {
    assertEquals(Boolean.TRUE, cast("true", NodeIds.Boolean.expanded()));
  }

  @Test
  public void testExplicitConversion() throws Exception {
    assertEquals("1", cast(true, NodeIds.String));
  }

  @Test
  public void testUInt64Above32BitsCastsToBooleanTrue() throws Exception {
    assertEquals(Boolean.TRUE, cast(ulong(0x1_0000_0000L), NodeIds.Boolean));
  }

  @Test
  public void testConversionFailureReturnsNull() throws Exception {
    assertNull(cast("not an int", NodeIds.Int32));
  }

  @Test
  public void testNullSourceReturnsNull() throws Exception {
    assertNull(cast(null, NodeIds.Int32));
  }

  @Test
  public void testUnknownTargetDataTypeReturnsNull() throws Exception {
    assertNull(cast("42", new NodeId(1, "UnknownDataType")));
  }

  @Test
  public void testNonNodeIdTargetDataTypeReturnsNull() throws Exception {
    assertNull(cast("42", "Int32"));
  }

  private static Object cast(Object value, Object targetDataType) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

    FilterOperand op0 = new LiteralOperand(new Variant(value));
    FilterOperand op1 = new LiteralOperand(new Variant(targetDataType));

    when(context.resolve(op0, eventNode)).thenReturn(value);
    when(context.resolve(op1, eventNode)).thenReturn(targetDataType);

    return Operators.CAST.apply(context, eventNode, new FilterOperand[] {op0, op1});
  }
}
