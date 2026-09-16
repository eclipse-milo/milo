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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.Reference.Direction;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.junit.jupiter.api.Test;

public class OfTypeTest {

  @Test
  public void testExactTypeDefinitionMatches() throws Exception {
    assertTrue(ofType(NodeIds.BaseEventType, NodeIds.BaseEventType));
  }

  @Test
  public void testSubtypeMatches() throws Exception {
    NodeId eventTypeId = NodeIds.SystemEventType;
    NodeId targetTypeId = NodeIds.BaseEventType;

    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mockEventNode(eventTypeId);
    FilterOperand operand = new LiteralOperand(new Variant(targetTypeId));
    OpcUaServer server = mock(OpcUaServer.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    UaObjectTypeNode eventTypeNode = mockObjectTypeNode(eventTypeId);
    UaObjectTypeNode targetTypeNode = mockObjectTypeNode(targetTypeId);
    Reference subtypeOfTarget =
        new Reference(eventTypeId, NodeIds.HasSubtype, targetTypeId.expanded(), Direction.INVERSE);

    when(context.resolve(operand, eventNode)).thenReturn(targetTypeId);
    when(context.getServer()).thenReturn(server);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getNamespaceTable()).thenReturn(new NamespaceTable());
    when(addressSpaceManager.getManagedNode(eventTypeId)).thenReturn(Optional.of(eventTypeNode));
    when(addressSpaceManager.getManagedReferences(eventTypeId))
        .thenReturn(List.of(subtypeOfTarget));
    when(addressSpaceManager.getManagedNode(targetTypeId)).thenReturn(Optional.of(targetTypeNode));

    assertEquals(
        Boolean.TRUE, Operators.OF_TYPE.apply(context, eventNode, new FilterOperand[] {operand}));
  }

  @Test
  public void testUnrelatedTypeReturnsFalse() throws Exception {
    assertFalse(ofType(NodeIds.BaseEventType, NodeIds.ConditionType));
  }

  @Test
  public void testNonNodeIdOperandReturnsFalse() throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);
    FilterOperand operand = new LiteralOperand(new Variant("BaseEventType"));

    when(context.resolve(operand, eventNode)).thenReturn("BaseEventType");

    assertNotEquals(
        Boolean.TRUE, Operators.OF_TYPE.apply(context, eventNode, new FilterOperand[] {operand}));
  }

  private static Boolean ofType(NodeId eventTypeId, NodeId targetTypeId) throws Exception {
    OperatorContext context = mock(OperatorContext.class);
    BaseEventTypeNode eventNode = mockEventNode(eventTypeId);
    FilterOperand operand = new LiteralOperand(new Variant(targetTypeId));
    OpcUaServer server = mock(OpcUaServer.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);

    when(context.resolve(operand, eventNode)).thenReturn(targetTypeId);
    when(context.getServer()).thenReturn(server);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(addressSpaceManager.getManagedNode(eventTypeId)).thenReturn(Optional.empty());

    return Operators.OF_TYPE.apply(context, eventNode, new FilterOperand[] {operand});
  }

  private static BaseEventTypeNode mockEventNode(NodeId eventTypeId) {
    BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);
    ObjectTypeNode typeDefinitionNode = mock(ObjectTypeNode.class);

    when(eventNode.getTypeDefinitionNode()).thenReturn(typeDefinitionNode);
    when(typeDefinitionNode.getNodeId()).thenReturn(eventTypeId);

    return eventNode;
  }

  private static UaObjectTypeNode mockObjectTypeNode(NodeId nodeId) {
    UaObjectTypeNode node = mock(UaObjectTypeNode.class);

    when(node.getNodeId()).thenReturn(nodeId);

    return node;
  }
}
