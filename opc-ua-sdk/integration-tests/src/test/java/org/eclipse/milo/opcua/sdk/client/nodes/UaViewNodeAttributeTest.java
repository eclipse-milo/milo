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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UaViewNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getViewAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestView");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var viewNode = (UaViewNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, viewNode.getNodeId());
    assertEquals(newQualifiedName("TestView"), viewNode.getBrowseName());
    assertEquals(LocalizedText.english("TestView"), viewNode.getDisplayName());
    assertEquals(LocalizedText.english("TestView Description"), viewNode.getDescription());
    assertEquals(uint(0), viewNode.getWriteMask());
    assertEquals(uint(0), viewNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, viewNode.getRolePermissions());
    assertArrayEquals(AttributeTestHelper.USER_ROLE_PERMISSIONS, viewNode.getUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, viewNode.getAccessRestrictions());

    assertTrue(viewNode.getContainsNoLoops());
    assertEquals(ubyte(0), viewNode.getEventNotifier());
  }

  @Test
  public void readViewAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestView");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var viewNode = (UaViewNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, viewNode.readNodeId());
    assertEquals(newQualifiedName("TestView"), viewNode.readBrowseName());
    assertEquals(LocalizedText.english("TestView"), viewNode.readDisplayName());
    assertEquals(LocalizedText.english("TestView Description"), viewNode.readDescription());
    assertEquals(uint(0), viewNode.readWriteMask());
    assertEquals(uint(0), viewNode.readUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, viewNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, viewNode.readUserRolePermissions());
    assertEquals(AttributeTestHelper.ACCESS_RESTRICTIONS, viewNode.readAccessRestrictions());

    assertTrue(viewNode.readContainsNoLoops());
    assertEquals(ubyte(0), viewNode.readEventNotifier());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureViewNode(testNamespace);
  }
}
