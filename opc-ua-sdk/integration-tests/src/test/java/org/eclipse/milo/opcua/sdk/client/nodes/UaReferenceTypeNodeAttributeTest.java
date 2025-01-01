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

public class UaReferenceTypeNodeAttributeTest extends AbstractClientServerTest {

  @Test
  public void getReferenceTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestReferenceType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var referenceTypeNode = (UaReferenceTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, referenceTypeNode.getNodeId());
    assertEquals(newQualifiedName("TestReferenceType"), referenceTypeNode.getBrowseName());
    assertEquals(LocalizedText.english("TestReferenceType"), referenceTypeNode.getDisplayName());
    assertEquals(
        LocalizedText.english("TestReferenceType Description"), referenceTypeNode.getDescription());
    assertEquals(uint(0), referenceTypeNode.getWriteMask());
    assertEquals(uint(0), referenceTypeNode.getUserWriteMask());
    assertArrayEquals(AttributeTestHelper.ROLE_PERMISSIONS, referenceTypeNode.getRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, referenceTypeNode.getUserRolePermissions());
    assertEquals(
        AttributeTestHelper.ACCESS_RESTRICTIONS, referenceTypeNode.getAccessRestrictions());

    assertTrue(referenceTypeNode.getIsAbstract());
    assertTrue(referenceTypeNode.getSymmetric());
    assertEquals(LocalizedText.english("Inverse"), referenceTypeNode.getInverseName());
  }

  @Test
  public void readReferenceTypeAttributes() throws UaException {
    NodeId nodeId = newNodeId("TestReferenceType");
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    var referenceTypeNode = (UaReferenceTypeNode) client.getAddressSpace().getNode(nodeId);

    assertEquals(nodeId, referenceTypeNode.readNodeId());
    assertEquals(newQualifiedName("TestReferenceType"), referenceTypeNode.readBrowseName());
    assertEquals(LocalizedText.english("TestReferenceType"), referenceTypeNode.readDisplayName());
    assertEquals(
        LocalizedText.english("TestReferenceType Description"),
        referenceTypeNode.readDescription());
    assertEquals(uint(0), referenceTypeNode.readWriteMask());
    assertEquals(uint(0), referenceTypeNode.readUserWriteMask());
    assertArrayEquals(
        AttributeTestHelper.ROLE_PERMISSIONS, referenceTypeNode.readRolePermissions());
    assertArrayEquals(
        AttributeTestHelper.USER_ROLE_PERMISSIONS, referenceTypeNode.readUserRolePermissions());
    assertEquals(
        AttributeTestHelper.ACCESS_RESTRICTIONS, referenceTypeNode.readAccessRestrictions());

    assertTrue(referenceTypeNode.readIsAbstract());
    assertTrue(referenceTypeNode.readSymmetric());
    assertEquals(LocalizedText.english("Inverse"), referenceTypeNode.readInverseName());
  }

  @BeforeAll
  public void configureTestNode() {
    AttributeTestHelper.configureReferenceTypeNode(testNamespace);
  }
}
