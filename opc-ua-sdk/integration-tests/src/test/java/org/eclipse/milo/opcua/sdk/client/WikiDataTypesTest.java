/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.MatrixTestType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.Test;

/** Checks the Wiki's dynamic discovery and explicit codec recipes on a real local server. */
public class WikiDataTypesTest extends AbstractClientServerTest {

  // The client has no application Java class registered on the dynamic path.
  @Test
  void discoveredStructurePreservesMatrixMembers() throws Exception {
    NodeId variableId = newNodeId("MatrixTestTypeValue");
    UaStructuredType decoded = readStructure(client, variableId);
    DynamicStructType structure = assertInstanceOf(DynamicStructType.class, decoded);
    Matrix builtinMatrix =
        assertInstanceOf(Matrix.class, structure.getMembers().get("BuiltinMatrix"));
    assertArrayEquals(new int[] {2, 2}, builtinMatrix.getDimensions());
    assertArrayEquals(new Integer[] {0, 1, 2, 3}, (Integer[]) builtinMatrix.getElements());
    assertEquals(
        MatrixTestType.TYPE_ID,
        structure.getTypeId().absolute(client.getNamespaceTable()).orElseThrow());
  }

  @Test
  void dynamicReadReportsBadNodeInsteadOfTryingToDecodeItsEmptyValue() {
    UaException failure =
        assertThrows(
            UaException.class, () -> readStructure(client, newNodeId("missing-type-example")));
    assertEquals(StatusCodes.Bad_NodeIdUnknown, failure.getStatusCode().getValue());
  }

  // Explicit registration must change the decoded representation to the caller's Java type.
  @Test
  void registeredCodecDecodesTheSameStructureIntoAnApplicationClass() throws Exception {
    NodeId dataTypeId = MatrixTestType.TYPE_ID.toNodeIdOrThrow(client.getNamespaceTable());
    NodeId binaryEncodingId =
        MatrixTestType.BINARY_ENCODING_ID.toNodeIdOrThrow(client.getNamespaceTable());
    NodeId xmlEncodingId = null;
    NodeId jsonEncodingId =
        MatrixTestType.JSON_ENCODING_ID.toNodeIdOrThrow(client.getNamespaceTable());
    var codec = new MatrixTestType.Codec();
    // wiki:register-codec:start
    client
        .getStaticDataTypeManager()
        .registerType(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
    // wiki:register-codec:end
    DataValue value =
        client.readValue(0, TimestampsToReturn.Neither, newNodeId("MatrixTestTypeValue"));
    assertEquals(0L, value.statusCode().getValue());
    ExtensionObject encoded = assertInstanceOf(ExtensionObject.class, value.value().value());
    MatrixTestType decoded =
        assertInstanceOf(MatrixTestType.class, encoded.decode(client.getStaticEncodingContext()));
    assertArrayEquals(new Integer[] {0, 1}, decoded.getBuiltinMatrix()[0]);
    assertArrayEquals(new Integer[] {2, 3}, decoded.getBuiltinMatrix()[1]);
  }

  // wiki:dynamic-structure:start
  static UaStructuredType readStructure(OpcUaClient client, NodeId variableId) throws UaException {
    DataValue value = client.readValue(0, TimestampsToReturn.Both, variableId);
    if (!value.statusCode().isGood()) {
      throw new UaException(value.statusCode());
    }
    if (!(value.value().value() instanceof ExtensionObject encoded)) {
      throw new IllegalArgumentException("Expected a structured value");
    }
    return encoded.decode(client.getDynamicEncodingContext());
  }
  // wiki:dynamic-structure:end
}
