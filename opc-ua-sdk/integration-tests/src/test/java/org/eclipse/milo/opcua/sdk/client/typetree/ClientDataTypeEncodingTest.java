/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClientDataTypeEncodingTest {

  // The encoding ID used for codec registration must also survive in decoded value metadata.
  @ParameterizedTest
  @MethodSource("encodingCases")
  void decodedValuesCanBeReencoded(
      StructureType structureType, NodeId advertisedEncodingId, NodeId effectiveEncodingId) {
    var manager = new DefaultDataTypeManager();
    var context =
        new DefaultEncodingContext() {
          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    context.getNamespaceTable().add("urn:test:encoding-fallback");

    var definition =
        new StructureDefinition(
            new NodeId(1, 5001),
            NodeIds.Structure,
            structureType,
            new StructureField[] {
              new StructureField(
                  "Value", LocalizedText.NULL_VALUE, NodeIds.Int32, -1, null, uint(0), false)
            });
    var dataType =
        new ClientDataType(
            new QualifiedName(1, "TestType"),
            new NodeId(1, 3001),
            advertisedEncodingId,
            null,
            null,
            definition,
            false);
    var root =
        new Tree<DataType>(
            null,
            new ClientDataType(
                new QualifiedName(0, "Structure"),
                NodeIds.Structure,
                null,
                null,
                null,
                null,
                true));
    root.addChild(dataType);
    var tree = new DataTypeTree(root);
    new DataTypeManagerFactory.DefaultInitializer()
        .initialize(context.getNamespaceTable(), tree, manager);

    byte[] body =
        structureType == StructureType.Union
            ? new byte[] {1, 0, 0, 0, 7, 0, 0, 0}
            : new byte[] {7, 0, 0, 0};
    var encoded = ExtensionObject.of(ByteString.of(body), effectiveEncodingId);
    UaStructuredType decoded = encoded.decode(context);

    if (decoded instanceof DynamicStructType struct) {
      assertEquals(7, struct.getMembers().get("Value"));
    } else {
      assertEquals(
          new DynamicUnionType.UnionValue("Value", 7),
          ((DynamicUnionType) decoded).getValue().orElseThrow());
    }
    assertEquals(encoded, ExtensionObject.encode(context, decoded));
    assertEquals(effectiveEncodingId, tree.getBinaryEncodingId(dataType.getNodeId()));
  }

  static Stream<Arguments> encodingCases() {
    return Stream.of(StructureType.Structure, StructureType.Union)
        .flatMap(
            type ->
                Stream.of(
                    Arguments.of(type, null, new NodeId(1, 5001)),
                    Arguments.of(type, NodeId.NULL_VALUE, new NodeId(1, 5001)),
                    Arguments.of(type, new NodeId(1, 5002), new NodeId(1, 5002))));
  }
}
