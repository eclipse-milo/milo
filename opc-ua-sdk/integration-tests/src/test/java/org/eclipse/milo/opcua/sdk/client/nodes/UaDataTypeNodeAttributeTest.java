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

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaDataTypeNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getDataTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestDataType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var dataTypeNode = (UaDataTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, dataTypeNode.getNodeId());
    assertEquals(newQualifiedName("TestDataType"), dataTypeNode.getBrowseName());
    assertEquals(LocalizedText.english("TestDataType"), dataTypeNode.getDisplayName());
    assertEquals(LocalizedText.english("TestDataType Description"), dataTypeNode.getDescription());
    assertEquals(uint(0), dataTypeNode.getWriteMask());
    assertEquals(uint(0), dataTypeNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, dataTypeNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, dataTypeNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, dataTypeNode.getAccessRestrictions());
    assertFalse(dataTypeNode.getIsAbstract());
    assertEquals(AttributeTestHelper.DATA_TYPE_DEFINITION, dataTypeNode.getDataTypeDefinition());
  }

  @Test
  public void readDataTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestDataType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var dataTypeNode = (UaDataTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, dataTypeNode.readNodeId());
    assertEquals(newQualifiedName("TestDataType"), dataTypeNode.readBrowseName());
    assertEquals(LocalizedText.english("TestDataType"), dataTypeNode.readDisplayName());
    assertEquals(LocalizedText.english("TestDataType Description"), dataTypeNode.readDescription());
    assertEquals(uint(0), dataTypeNode.readWriteMask());
    assertEquals(uint(0), dataTypeNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, dataTypeNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, dataTypeNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, dataTypeNode.readAccessRestrictions());
    assertFalse(dataTypeNode.readIsAbstract());
    assertEquals(AttributeTestHelper.DATA_TYPE_DEFINITION, dataTypeNode.readDataTypeDefinition());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureDataTypeNode(testNamespace);
  }
}
