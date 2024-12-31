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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaObjectNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getObjectAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestObject");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var objectNode = (UaObjectNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, objectNode.getNodeId());
    assertEquals(newQualifiedName("TestObject"), objectNode.getBrowseName());
    assertEquals(LocalizedText.english("TestObject"), objectNode.getDisplayName());
    assertEquals(LocalizedText.english("TestObject Description"), objectNode.getDescription());
    assertEquals(uint(0), objectNode.getWriteMask());
    assertEquals(uint(0), objectNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, objectNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, objectNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, objectNode.getAccessRestrictions());

    assertEquals(ubyte(0), objectNode.getEventNotifier());
  }

  @Test
  public void readObjectAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestObject");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var objectNode = (UaObjectNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, objectNode.readNodeId());
    assertEquals(newQualifiedName("TestObject"), objectNode.readBrowseName());
    assertEquals(LocalizedText.english("TestObject"), objectNode.readDisplayName());
    assertEquals(LocalizedText.english("TestObject Description"), objectNode.readDescription());
    assertEquals(uint(0), objectNode.readWriteMask());
    assertEquals(uint(0), objectNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, objectNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, objectNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, objectNode.readAccessRestrictions());

    assertEquals(ubyte(0), objectNode.readEventNotifier());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureObjectNode(testNamespace);
  }
}
