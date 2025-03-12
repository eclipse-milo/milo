/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaVariableNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getVariableAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestVariable");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var variableNode = (UaVariableNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, variableNode.getNodeId());
    assertEquals(newQualifiedName("TestVariable"), variableNode.getBrowseName());
    assertEquals(LocalizedText.english("TestVariable"), variableNode.getDisplayName());
    assertEquals(LocalizedText.english("TestVariable Description"), variableNode.getDescription());
    assertEquals(uint(0), variableNode.getWriteMask());
    assertEquals(uint(0), variableNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, variableNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, variableNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, variableNode.getAccessRestrictions());

    assertEquals(new Variant(42), variableNode.getValue().value());
    assertEquals(OpcUaDataType.Int32.getNodeId(), variableNode.getDataType());
    assertEquals(-1, variableNode.getValueRank());
    assertNull(variableNode.getArrayDimensions());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), variableNode.getAccessLevel());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), variableNode.getUserAccessLevel());
    assertEquals(-1.0, variableNode.getMinimumSamplingInterval());
    assertFalse(variableNode.getHistorizing());
  }

  @Test
  public void readVariableAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestVariable");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var variableNode = (UaVariableNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, variableNode.readNodeId());
    assertEquals(newQualifiedName("TestVariable"), variableNode.readBrowseName());
    assertEquals(LocalizedText.english("TestVariable"), variableNode.readDisplayName());
    assertEquals(LocalizedText.english("TestVariable Description"), variableNode.readDescription());
    assertEquals(uint(0), variableNode.readWriteMask());
    assertEquals(uint(0), variableNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, variableNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, variableNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, variableNode.readAccessRestrictions());

    assertEquals(new Variant(42), variableNode.readValue().value());
    assertEquals(OpcUaDataType.Int32.getNodeId(), variableNode.readDataType());
    assertEquals(-1, variableNode.readValueRank());
    assertNull(variableNode.readArrayDimensions());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), variableNode.readAccessLevel());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), variableNode.readUserAccessLevel());
    assertEquals(-1.0, variableNode.readMinimumSamplingInterval());
    assertFalse(variableNode.readHistorizing());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureVariableNode(testNamespace);
  }
}
