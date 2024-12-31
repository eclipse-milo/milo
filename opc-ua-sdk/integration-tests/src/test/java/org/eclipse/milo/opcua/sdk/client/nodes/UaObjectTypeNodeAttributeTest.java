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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaObjectTypeNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getObjectTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestObjectType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var objectTypeNode = (UaObjectTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, objectTypeNode.getNodeId());
    assertEquals(newQualifiedName("TestObjectType"), objectTypeNode.getBrowseName());
    assertEquals(LocalizedText.english("TestObjectType"), objectTypeNode.getDisplayName());
    assertEquals(
        LocalizedText.english("TestObjectType Description"), objectTypeNode.getDescription());
    assertEquals(uint(0), objectTypeNode.getWriteMask());
    assertEquals(uint(0), objectTypeNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, objectTypeNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, objectTypeNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, objectTypeNode.getAccessRestrictions());

    assertTrue(objectTypeNode.getIsAbstract());
  }

  @Test
  public void readObjectTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestObjectType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var objectTypeNode = (UaObjectTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, objectTypeNode.readNodeId());
    assertEquals(newQualifiedName("TestObjectType"), objectTypeNode.readBrowseName());
    assertEquals(LocalizedText.english("TestObjectType"), objectTypeNode.readDisplayName());
    assertEquals(
        LocalizedText.english("TestObjectType Description"), objectTypeNode.readDescription());
    assertEquals(uint(0), objectTypeNode.readWriteMask());
    assertEquals(uint(0), objectTypeNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, objectTypeNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, objectTypeNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, objectTypeNode.readAccessRestrictions());

    assertTrue(objectTypeNode.readIsAbstract());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureObjectTypeNode(testNamespace);
  }
}
