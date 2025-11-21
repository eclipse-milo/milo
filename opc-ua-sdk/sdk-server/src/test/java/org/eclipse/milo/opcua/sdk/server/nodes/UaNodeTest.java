/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UaNodeTest {

  private OpcUaServer server;
  private UaNodeContext nodeContext;
  private UaNodeManager nodeManager;

  @BeforeEach
  public void setup() throws Exception {
    server = Mockito.mock(OpcUaServer.class);

    AddressSpaceManager addressSpaceManager = new AddressSpaceManager(server);
    NamespaceTable namespaceTable = new NamespaceTable();
    ObjectTypeManager objectTypeManager = new ObjectTypeManager();
    VariableTypeManager variableTypeManager = new VariableTypeManager();

    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);
    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    Mockito.when(server.getObjectTypeManager()).thenReturn(objectTypeManager);
    Mockito.when(server.getVariableTypeManager()).thenReturn(variableTypeManager);
    Mockito.when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    Mockito.when(server.getEncodingManager()).thenReturn(OpcUaEncodingManager.getInstance());

    nodeManager = new UaNodeManager();
    addressSpaceManager.register(nodeManager);

    nodeContext =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };
  }

  @Test
  public void testGetPropertyWithExtensionObjects() {
    // Create a UaObjectNode to test with
    NodeId objectNodeId = new NodeId(1, "TestObject");

    UaObjectNode objectNode =
        UaObjectNode.build(
            nodeContext,
            b ->
                b.setNodeId(objectNodeId)
                    .setBrowseName(new QualifiedName(1, "TestObject"))
                    .setDisplayName(LocalizedText.english("TestObject"))
                    .setTypeDefinition(NodeIds.BaseObjectType)
                    .build());

    nodeManager.addNode(objectNode);

    // Define QualifiedProperty instances for scalar, array, and matrix XVType
    QualifiedProperty<XVType> scalarProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/",
            "ScalarXVProperty",
            XVType.TYPE_ID,
            ValueRanks.Scalar,
            XVType.class);

    QualifiedProperty<XVType[]> arrayProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/",
            "ArrayXVProperty",
            XVType.TYPE_ID,
            ValueRanks.OneDimension,
            XVType[].class);

    QualifiedProperty<Matrix> matrixProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/", "MatrixXVProperty", XVType.TYPE_ID, 2, Matrix.class);

    // Set these to null to create the PropertyNodes
    objectNode.setProperty(scalarProperty, null);
    objectNode.setProperty(arrayProperty, null);
    objectNode.setProperty(matrixProperty, null);

    // Now set the values of each to contain ExtensionObjects containing the XVTypes
    XVType scalarValue = new XVType(1.0, 2.0f);
    XVType[] arrayValue = {new XVType(3.0, 4.0f), new XVType(5.0, 6.0f), new XVType(7.0, 8.0f)};
    XVType[][] matrixValue = {
      {new XVType(9.0, 10.0f), new XVType(11.0, 12.0f)},
      {new XVType(13.0, 14.0f), new XVType(15.0, 16.0f)}
    };

    objectNode
        .getPropertyNode(scalarProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofExtensionObject(
                    ExtensionObject.encode(server.getStaticEncodingContext(), scalarValue))));

    objectNode
        .getPropertyNode(arrayProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofExtensionObjectArray(
                    ExtensionObject.encodeArray(server.getStaticEncodingContext(), arrayValue))));

    objectNode
        .getPropertyNode(matrixProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofMatrix(
                    Matrix.ofStruct(matrixValue)
                        .transform(
                            e ->
                                ExtensionObject.encode(
                                    server.getStaticEncodingContext(), (XVType) e)))));

    // Get properties (this should decode ExtensionObjects back to XVType)
    Optional<XVType> retrievedScalar = objectNode.getProperty(scalarProperty);
    Optional<XVType[]> retrievedArray = objectNode.getProperty(arrayProperty);
    Optional<Matrix> retrievedMatrix = objectNode.getProperty(matrixProperty);

    // Assert scalar property
    XVType scalar = retrievedScalar.orElseThrow();
    assertEquals(1.0, scalar.getX(), "Scalar x value should match");
    assertEquals(2.0f, scalar.getValue(), "Scalar value should match");

    // Assert array property
    XVType[] array = retrievedArray.orElseThrow();
    assertEquals(3, array.length, "Array length should be 3");
    assertEquals(3.0, array[0].getX(), "Array[0] x value should match");
    assertEquals(4.0f, array[0].getValue(), "Array[0] value should match");
    assertEquals(5.0, array[1].getX(), "Array[1] x value should match");
    assertEquals(6.0f, array[1].getValue(), "Array[1] value should match");
    assertEquals(7.0, array[2].getX(), "Array[2] x value should match");
    assertEquals(8.0f, array[2].getValue(), "Array[2] value should match");

    // Assert matrix property
    Matrix matrix = retrievedMatrix.orElseThrow();
    int[] dimensions = matrix.getDimensions();
    assertEquals(2, dimensions.length, "Matrix should have 2 dimensions");
    assertEquals(2, dimensions[0], "Matrix should have 2 rows");
    assertEquals(2, dimensions[1], "Matrix should have 2 columns");

    // Convert back to a nested array to verify values
    XVType[][] nestedArray = (XVType[][]) matrix.nestedArrayValue();
    assertEquals(9.0, nestedArray[0][0].getX(), "Matrix[0][0] x value should match");
    assertEquals(10.0f, nestedArray[0][0].getValue(), "Matrix[0][0] value should match");
    assertEquals(11.0, nestedArray[0][1].getX(), "Matrix[0][1] x value should match");
    assertEquals(12.0f, nestedArray[0][1].getValue(), "Matrix[0][1] value should match");
    assertEquals(13.0, nestedArray[1][0].getX(), "Matrix[1][0] x value should match");
    assertEquals(14.0f, nestedArray[1][0].getValue(), "Matrix[1][0] value should match");
    assertEquals(15.0, nestedArray[1][1].getX(), "Matrix[1][1] x value should match");
    assertEquals(16.0f, nestedArray[1][1].getValue(), "Matrix[1][1] value should match");
  }
}
