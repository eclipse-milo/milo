/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class UaObjectNodeTest extends AbstractClientServerTest {

  @Test
  public void getObjectComponent() throws UaException {
    UaObjectNode objectNode = client.getAddressSpace().getObjectNode(NodeIds.Server);

    assertNotNull(objectNode.getObjectComponent("ServerCapabilities"));
  }

  @Test
  public void getVariableComponent() throws UaException {
    UaObjectNode objectNode = client.getAddressSpace().getObjectNode(NodeIds.Server);

    assertNotNull(objectNode.getVariableComponent("ServerStatus"));
  }

  @Test
  public void getTypeDefinition() throws UaException {
    UaObjectNode objectNode = client.getAddressSpace().getObjectNode(NodeIds.Server);

    UaObjectTypeNode objectTypeNode = objectNode.getTypeDefinition();

    assertEquals(NodeIds.ServerType, objectTypeNode.getNodeId());
  }
}
