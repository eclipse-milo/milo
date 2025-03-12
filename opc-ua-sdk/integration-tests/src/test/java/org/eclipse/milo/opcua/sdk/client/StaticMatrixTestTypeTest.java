/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.MatrixTestType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

public class StaticMatrixTestTypeTest extends AbstractClientServerTest {

  @Test
  public void read() throws Exception {
    // Register Codecs for each supported encoding with DataTypeManager
    client
        .getStaticDataTypeManager()
        .registerType(
            MatrixTestType.TYPE_ID.toNodeIdOrThrow(client.getNamespaceTable()),
            new MatrixTestType.Codec(),
            MatrixTestType.BINARY_ENCODING_ID.toNodeIdOrThrow(client.getNamespaceTable()),
            null,
            MatrixTestType.JSON_ENCODING_ID.toNodeIdOrThrow(client.getNamespaceTable()));

    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode testNode =
        (UaVariableNode) addressSpace.getNode(new NodeId(2, "MatrixTestTypeValue"));

    DataValue value = testNode.readValue();
    assertNotNull(value);

    ExtensionObject xo = (ExtensionObject) value.value().value();
    assert xo != null;

    MatrixTestType decoded = (MatrixTestType) xo.decode(client.getStaticEncodingContext());
    System.out.println(decoded);
  }
}
