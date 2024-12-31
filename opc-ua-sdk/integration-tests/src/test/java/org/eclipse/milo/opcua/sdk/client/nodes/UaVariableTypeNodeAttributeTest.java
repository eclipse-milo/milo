/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.nodes;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaVariableTypeNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getVariableTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestVariableType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var variableTypeNode = (UaVariableTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, variableTypeNode.getNodeId());
    assertEquals(newQualifiedName("TestVariableType"), variableTypeNode.getBrowseName());
    assertEquals(LocalizedText.english("TestVariableType"), variableTypeNode.getDisplayName());
    assertEquals(
        LocalizedText.english("TestVariableType Description"), variableTypeNode.getDescription());
    assertEquals(uint(0), variableTypeNode.getWriteMask());
    assertEquals(uint(0), variableTypeNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, variableTypeNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, variableTypeNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, variableTypeNode.getAccessRestrictions());

    assertEquals(new Variant(42), variableTypeNode.getValue().getValue());
    assertEquals(BuiltinDataType.Int32.getNodeId(), variableTypeNode.getDataType());
    assertEquals(-1, variableTypeNode.getValueRank());
    assertNull(variableTypeNode.getArrayDimensions());
    assertFalse(variableTypeNode.getIsAbstract());
  }

  @Test
  public void readVariableTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestVariableType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var variableTypeNode = (UaVariableTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, variableTypeNode.readNodeId());
    assertEquals(newQualifiedName("TestVariableType"), variableTypeNode.readBrowseName());
    assertEquals(LocalizedText.english("TestVariableType"), variableTypeNode.readDisplayName());
    assertEquals(
        LocalizedText.english("TestVariableType Description"), variableTypeNode.readDescription());
    assertEquals(uint(0), variableTypeNode.readWriteMask());
    assertEquals(uint(0), variableTypeNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, variableTypeNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, variableTypeNode.readUserRolePermissions());
    assertEquals(
        AttributeTestHelper.ACCESS_RESTRICTIONS, variableTypeNode.readAccessRestrictions());

    assertEquals(new Variant(42), variableTypeNode.readValue().getValue());
    assertEquals(BuiltinDataType.Int32.getNodeId(), variableTypeNode.readDataType());
    assertEquals(-1, variableTypeNode.readValueRank());
    assertNull(variableTypeNode.readArrayDimensions());
    assertFalse(variableTypeNode.readIsAbstract());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureVariableTypeNode(testNamespace);
  }
}
