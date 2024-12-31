/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.typetree;

import static java.util.Objects.requireNonNullElse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractDataTypeTreeTest extends AbstractClientServerTest {

  private DataTypeTree dataTypeTree;

  @BeforeAll
  public void buildDataTypeTree() throws UaException {
    dataTypeTree = getDataTypeTree();
  }

  protected abstract DataTypeTree getDataTypeTree() throws UaException;

  @Test
  public void testGetTree() {
    dataTypeTree
        .getRoot()
        .traverseWithDepth(
            (dataType, depth) -> {
              for (int i = 0; i < depth; i++) {
                System.out.print("\t");
              }
              System.out.println(dataType.getBrowseName().toParseableString());
            },
            (o1, o2) -> {
              String name1 = requireNonNullElse(o1.getValue().getBrowseName().getName(), "");
              String name2 = requireNonNullElse(o2.getValue().getBrowseName().getName(), "");
              return Comparator.<String>naturalOrder().compare(name1, name2);
            });
  }

  @Test
  public void testGetBackingClass() {
    // all subtypes of String are backed by String.class
    checkSubtypes(NodeIds.String, String.class);

    // all subtypes of DateTime are backed by DateTime.class
    checkSubtypes(NodeIds.DateTime, DateTime.class);

    // all subtypes of ByteString are backed by ByteString.class
    checkSubtypes(NodeIds.ByteString, ByteString.class);

    // all subtypes of NodeId are backed by NodeId.class
    checkSubtypes(NodeIds.NodeId, NodeId.class);

    // all subtypes of Structure are backed by ExtensionObject.class
    checkSubtypes(NodeIds.Structure, ExtensionObject.class);

    // all subtypes of Double are backed by Double.class
    checkSubtypes(NodeIds.Double, Double.class);

    // all subtypes of UInt32 are backed by UInteger.class
    checkSubtypes(NodeIds.UInt32, UInteger.class);

    // all subtypes of UInt64 are backed by ULong.class
    checkSubtypes(NodeIds.UInt64, ULong.class);

    // all subtypes of Enumeration are backed by Integer.class
    checkSubtypes(NodeIds.Enumeration, Integer.class);
  }

  private void checkSubtypes(NodeId dataTypeId, Class<?> expectedBackingClass) {
    Tree<DataType> nodeIdNode = dataTypeTree.getTreeNode(dataTypeId);
    assertNotNull(nodeIdNode);
    nodeIdNode.traverse(
        dataType -> {
          Class<?> backingClass = dataTypeTree.getBackingClass(dataType.getNodeId());
          System.out.println(dataType.getBrowseName().toParseableString() + " : " + backingClass);
          assertEquals(expectedBackingClass, backingClass);
        });
  }

  @Test
  public void testGetBuiltinType() {
    // Check all the builtin types
    for (BuiltinDataType expectedType : BuiltinDataType.values()) {
      BuiltinDataType builtinType = dataTypeTree.getBuiltinType(expectedType.getNodeId());

      assertEquals(expectedType, builtinType);
    }

    // Check that subtypes resolve to their builtin types
    assertEquals(BuiltinDataType.String, dataTypeTree.getBuiltinType(NodeIds.NumericRange));
    assertEquals(BuiltinDataType.DateTime, dataTypeTree.getBuiltinType(NodeIds.DateTime));
    assertEquals(BuiltinDataType.ByteString, dataTypeTree.getBuiltinType(NodeIds.Image));
    assertEquals(BuiltinDataType.ByteString, dataTypeTree.getBuiltinType(NodeIds.ImageBMP));
    assertEquals(
        BuiltinDataType.NodeId, dataTypeTree.getBuiltinType(NodeIds.SessionAuthenticationToken));
    assertEquals(
        BuiltinDataType.ExtensionObject, dataTypeTree.getBuiltinType(NodeIds.TrustListDataType));
    assertEquals(BuiltinDataType.Double, dataTypeTree.getBuiltinType(NodeIds.Duration));
    assertEquals(BuiltinDataType.UInt32, dataTypeTree.getBuiltinType(NodeIds.IntegerId));
    assertEquals(BuiltinDataType.UInt64, dataTypeTree.getBuiltinType(NodeIds.BitFieldMaskDataType));
    // note: enumerations resolve to BaseDataType aka Variant
    assertEquals(BuiltinDataType.Variant, dataTypeTree.getBuiltinType(NodeIds.NamingRuleType));
  }

  @Test
  public void testIsAssignable() {
    assertTrue(dataTypeTree.isAssignable(NodeIds.NumericRange, String.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.DateTime, DateTime.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Image, ByteString.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.ImageBMP, ByteString.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.SessionAuthenticationToken, NodeId.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.TrustListDataType, ExtensionObject.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Number.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Float.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Double.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Byte.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Short.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Integer.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, Long.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, UByte.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, UShort.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, UInteger.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Number, ULong.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Integer, Byte.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Integer, Short.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Integer, Integer.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Integer, Long.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.UInteger, UByte.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.UInteger, UShort.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.UInteger, UInteger.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.UInteger, ULong.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.Duration, Double.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.BitFieldMaskDataType, ULong.class));
    assertTrue(dataTypeTree.isAssignable(NodeIds.NamingRuleType, Integer.class));

    assertFalse(dataTypeTree.isAssignable(NodeIds.UInteger, Byte.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.UInteger, Short.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.UInteger, Integer.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.UInteger, Long.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.Integer, UByte.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.Integer, UShort.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.Integer, UInteger.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.Integer, ULong.class));
    assertFalse(dataTypeTree.isAssignable(NodeIds.Duration, Float.class));
  }

  @Test
  public void testGetEncodingIds() {
    Tree<DataType> treeNode = dataTypeTree.getTreeNode(NodeIds.Structure);
    assertNotNull(treeNode);

    treeNode.traverse(
        dataType -> {
          String name = dataType.getBrowseName().getName();

          if (!Objects.equals(name, "Structure") && !Objects.equals(name, "MatrixTestType")) {
            assertNotNull(dataType.getBinaryEncodingId());
            assertNotNull(dataType.getXmlEncodingId());
            assertNotNull(dataType.getJsonEncodingId());
            assertNotNull(dataTypeTree.getBinaryEncodingId(dataType.getNodeId()));
            assertNotNull(dataTypeTree.getXmlEncodingId(dataType.getNodeId()));
            assertNotNull(dataTypeTree.getJsonEncodingId(dataType.getNodeId()));
          }
        });
  }

  @Test
  public void enumerationsHaveDataTypeDefinitions() {
    Tree<DataType> treeNode = dataTypeTree.getTreeNode(NodeIds.Enumeration);
    assertNotNull(treeNode);

    treeNode.traverse(
        dataType -> {
          if (!Objects.equals(dataType.getBrowseName().getName(), "Enumeration")) {
            assertNotNull(dataType.getDataTypeDefinition());
            assertNotNull(dataTypeTree.getDataTypeDefinition(dataType.getNodeId()));
          }
        });
  }

  @Test
  public void structuresHaveDataTypeDefinitions() {
    Tree<DataType> treeNode = dataTypeTree.getTreeNode(NodeIds.Structure);
    assertNotNull(treeNode);

    treeNode.traverse(
        dataType -> {
          if (!Objects.equals(dataType.getBrowseName().getName(), "Structure")) {
            assertNotNull(dataType.getDataTypeDefinition());
            assertNotNull(dataTypeTree.getDataTypeDefinition(dataType.getNodeId()));
          }
        });
  }

  @Test
  void testIsAbstractAttribute() {
    DataType baseDataType = dataTypeTree.getType(NodeIds.BaseDataType);
    assertNotNull(baseDataType);
    assertTrue(baseDataType.isAbstract());

    DataType structureType = dataTypeTree.getType(NodeIds.Structure);
    assertNotNull(structureType);
    assertTrue(structureType.isAbstract());

    DataType xvType = dataTypeTree.getType(NodeIds.XVType);
    assertNotNull(xvType);
    assertFalse(xvType.isAbstract());
  }
}
