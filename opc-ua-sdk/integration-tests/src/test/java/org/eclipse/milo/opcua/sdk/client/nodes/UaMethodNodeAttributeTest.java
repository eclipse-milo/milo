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

public class UaMethodNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getMethodAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestMethod");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var methodNode = (UaMethodNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, methodNode.getNodeId());
    assertEquals(newQualifiedName("TestMethod"), methodNode.getBrowseName());
    assertEquals(LocalizedText.english("TestMethod"), methodNode.getDisplayName());
    assertEquals(LocalizedText.english("TestMethod Description"), methodNode.getDescription());
    assertEquals(uint(0), methodNode.getWriteMask());
    assertEquals(uint(0), methodNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, methodNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, methodNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, methodNode.getAccessRestrictions());

    assertTrue(methodNode.isExecutable());
    assertTrue(methodNode.isUserExecutable());
  }

  @Test
  public void readMethodAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestMethod");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var methodNode = (UaMethodNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, methodNode.readNodeId());
    assertEquals(newQualifiedName("TestMethod"), methodNode.readBrowseName());
    assertEquals(LocalizedText.english("TestMethod"), methodNode.readDisplayName());
    assertEquals(LocalizedText.english("TestMethod Description"), methodNode.readDescription());
    assertEquals(uint(0), methodNode.readWriteMask());
    assertEquals(uint(0), methodNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, methodNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, methodNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, methodNode.readAccessRestrictions());

    assertTrue(methodNode.isExecutable());
    assertTrue(methodNode.isUserExecutable());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureMethodNode(testNamespace);
  }
}
