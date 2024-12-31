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

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStruct;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.MatrixTestType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

public class DynamicMatrixTestTypeTest extends AbstractClientServerTest {

  @Test
  public void read() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode testNode =
        (UaVariableNode) addressSpace.getNode(new NodeId(2, "MatrixTestTypeValue"));

    DataValue value = testNode.readValue();
    assertNotNull(value);

    ExtensionObject xo = (ExtensionObject) value.getValue().getValue();
    assert xo != null;

    DynamicStruct decoded = (DynamicStruct) xo.decode(client.getDynamicEncodingContext());
    assertEquals(
        MatrixTestType.TYPE_ID,
        decoded.getTypeId().absolute(client.getNamespaceTable()).orElseThrow());
    System.out.println(decoded);
  }
}
