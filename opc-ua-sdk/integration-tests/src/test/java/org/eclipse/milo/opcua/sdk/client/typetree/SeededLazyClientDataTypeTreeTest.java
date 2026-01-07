/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.typetree.AbstractDataTypeTreeTest;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link LazyClientDataTypeTree} using a pre-seeded tree.
 *
 * <p>Extends {@link AbstractDataTypeTreeTest} to inherit all standard DataTypeTree tests. Unlike
 * {@link LazyClientDataTypeTreeTest}, this version uses the seed from {@link
 * LazyClientDataTypeTreeSeed} which pre-populates non-structure types, so they are immediately
 * resolvable without lazy browsing.
 */
public class SeededLazyClientDataTypeTreeTest extends AbstractDataTypeTreeTest {

  @Override
  protected DataTypeTree getDataTypeTree() {
    return new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());
  }

  @Test
  void seededTypesAreImmediatelyResolved() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // All types from the seed should be resolved immediately
    assertTrue(seededTree.isResolved(NodeIds.BaseDataType));
    assertTrue(seededTree.isResolved(NodeIds.Boolean));
    assertTrue(seededTree.isResolved(NodeIds.String));
    assertTrue(seededTree.isResolved(NodeIds.Int32));
    assertTrue(seededTree.isResolved(NodeIds.Double));
    assertTrue(seededTree.isResolved(NodeIds.DateTime));
    assertTrue(seededTree.isResolved(NodeIds.ByteString));
    assertTrue(seededTree.isResolved(NodeIds.Number));
    assertTrue(seededTree.isResolved(NodeIds.Integer));
    assertTrue(seededTree.isResolved(NodeIds.UInteger));
    assertTrue(seededTree.isResolved(NodeIds.Enumeration));
  }

  @Test
  void primitiveTypesAvailableWithoutResolution() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Query primitive types - should return immediately without server browsing
    DataType int32Type = seededTree.getDataType(NodeIds.Int32);
    assertNotNull(int32Type);
    assertEquals("Int32", int32Type.getBrowseName().name());

    DataType stringType = seededTree.getDataType(NodeIds.String);
    assertNotNull(stringType);
    assertEquals("String", stringType.getBrowseName().name());

    DataType doubleType = seededTree.getDataType(NodeIds.Double);
    assertNotNull(doubleType);
    assertEquals("Double", doubleType.getBrowseName().name());
  }

  @Test
  void subTypesAreAvailable() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // String subtypes from the seed
    assertTrue(seededTree.isResolved(NodeIds.NumericRange));
    assertTrue(seededTree.isResolved(NodeIds.LocaleId));

    // ByteString subtypes from the seed
    assertTrue(seededTree.isResolved(NodeIds.Image));
    assertTrue(seededTree.isResolved(NodeIds.ImageBMP));

    // Number subtypes from the seed
    assertTrue(seededTree.isResolved(NodeIds.Integer));
    assertTrue(seededTree.isResolved(NodeIds.UInteger));
    assertTrue(seededTree.isResolved(NodeIds.Float));
  }

  @Test
  void isSubtypeOfWorksForSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Int32 -> Integer -> Number -> BaseDataType
    assertTrue(seededTree.isSubtypeOf(NodeIds.Int32, NodeIds.Integer));
    assertTrue(seededTree.isSubtypeOf(NodeIds.Int32, NodeIds.Number));
    assertTrue(seededTree.isSubtypeOf(NodeIds.Int32, NodeIds.BaseDataType));

    // String -> BaseDataType
    assertTrue(seededTree.isSubtypeOf(NodeIds.String, NodeIds.BaseDataType));

    // LocaleId -> String -> BaseDataType
    assertTrue(seededTree.isSubtypeOf(NodeIds.LocaleId, NodeIds.String));
    assertTrue(seededTree.isSubtypeOf(NodeIds.LocaleId, NodeIds.BaseDataType));

    // Int32 is not a subtype of String
    assertFalse(seededTree.isSubtypeOf(NodeIds.Int32, NodeIds.String));
  }

  @Test
  void enumerationSubtypesAreSeeded() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Enumeration and its subtypes are in the seed
    assertTrue(seededTree.isResolved(NodeIds.Enumeration));
    assertTrue(seededTree.isResolved(NodeIds.NodeClass));
    assertTrue(seededTree.isResolved(NodeIds.ServerState));
    assertTrue(seededTree.isResolved(NodeIds.RedundancySupport));
  }

  @Test
  void seededEnumerationSubtypesHaveDefinitions() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // NodeClass is a seeded enum subtype with an EnumDefinition
    DataType nodeClassType = seededTree.getDataType(NodeIds.NodeClass);
    assertNotNull(nodeClassType);
    assertEquals("NodeClass", nodeClassType.getBrowseName().name());
    assertTrue(seededTree.isResolved(NodeIds.NodeClass));
    assertNotNull(nodeClassType.getDataTypeDefinition());

    // ServerState is a seeded enum subtype with an EnumDefinition
    DataType serverStateType = seededTree.getDataType(NodeIds.ServerState);
    assertNotNull(serverStateType);
    assertEquals("ServerState", serverStateType.getBrowseName().name());
    assertNotNull(serverStateType.getDataTypeDefinition());
  }

  @Test
  void structureSubtypesAreSeeded() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Structure and its subtypes are in the seed
    assertTrue(seededTree.isResolved(NodeIds.Structure));
    assertTrue(seededTree.isResolved(NodeIds.XVType));
    assertTrue(seededTree.isResolved(NodeIds.RolePermissionType));
    assertTrue(seededTree.isResolved(NodeIds.StructureDefinition));
  }

  @Test
  void seededStructureSubtypesHaveEncodingIdsAndDefinitions() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // XVType is a seeded structure subtype with encoding IDs and definition
    DataType xvType = seededTree.getDataType(NodeIds.XVType);
    assertNotNull(xvType);
    assertEquals("XVType", xvType.getBrowseName().name());
    assertTrue(seededTree.isResolved(NodeIds.XVType));
    assertNotNull(xvType.getBinaryEncodingId());
    assertNotNull(xvType.getDataTypeDefinition());

    // RolePermissionType is a seeded structure subtype with encoding IDs and definition
    DataType rolePermType = seededTree.getDataType(NodeIds.RolePermissionType);
    assertNotNull(rolePermType);
    assertEquals("RolePermissionType", rolePermType.getBrowseName().name());
    assertNotNull(rolePermType.getBinaryEncodingId());
    assertNotNull(rolePermType.getDataTypeDefinition());
  }

  @Test
  void seededTreeMatchesEagerTreeForCommonTypes() throws UaException {
    DataTypeTree eagerTree = DataTypeTreeBuilder.build(client);
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Test types that are in the seed
    NodeId[] seededTypes = {
      NodeIds.Int32,
      NodeIds.String,
      NodeIds.Double,
      NodeIds.DateTime,
      NodeIds.ByteString,
      NodeIds.Boolean,
      NodeIds.Guid,
      NodeIds.Number,
      NodeIds.Integer,
      NodeIds.UInteger,
      NodeIds.Enumeration
    };

    for (NodeId typeId : seededTypes) {
      DataType eagerType = eagerTree.getDataType(typeId);
      DataType seededType = seededTree.getDataType(typeId);

      if (eagerType != null) {
        assertNotNull(seededType, "Seeded tree should have type: " + typeId);
        assertEquals(
            eagerType.getBrowseName(),
            seededType.getBrowseName(),
            "BrowseName mismatch for " + typeId);
        assertEquals(
            eagerType.isAbstract(), seededType.isAbstract(), "IsAbstract mismatch for " + typeId);
      }
    }
  }

  @Test
  void getRootContainsAllSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    Tree<DataType> root = seededTree.getRoot();
    var count = new AtomicInteger(0);
    root.traverseNodes(node -> count.incrementAndGet());

    // The seed tree should contain many types (all the primitives, enums, etc.)
    assertTrue(count.get() > 50, "Seeded tree should contain many pre-loaded types");
  }

  @Test
  void clearFailedResolutionsAllowsRetry() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Try to resolve a non-existent type
    NodeId fakeTypeId = new NodeId(999, "FakeDataType");
    DataType result = seededTree.getDataType(fakeTypeId);

    // Should return null
    assertTrue(result == null || !seededTree.isResolved(fakeTypeId));

    // Clear failed resolutions
    seededTree.clearFailedResolutions();

    // Now it can be attempted again
    result = seededTree.getDataType(fakeTypeId);
    assertTrue(result == null || !seededTree.isResolved(fakeTypeId));
  }

  @Test
  void containsTypeReturnsTrueForSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // These should return true immediately without triggering resolution
    assertTrue(seededTree.containsType(NodeIds.Int32));
    assertTrue(seededTree.containsType(NodeIds.String));
    assertTrue(seededTree.containsType(NodeIds.Double));
    assertTrue(seededTree.containsType(NodeIds.Enumeration));
    assertTrue(seededTree.containsType(NodeIds.Boolean));
  }

  @Test
  void getBuiltinTypeWorksForSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Builtin type resolution should work for seeded types
    assertEquals(OpcUaDataType.Int32, seededTree.getBuiltinType(NodeIds.Int32));
    assertEquals(OpcUaDataType.String, seededTree.getBuiltinType(NodeIds.String));
    assertEquals(OpcUaDataType.Double, seededTree.getBuiltinType(NodeIds.Double));
    assertEquals(OpcUaDataType.Boolean, seededTree.getBuiltinType(NodeIds.Boolean));
  }

  @Test
  void isEnumTypeWorksForSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Enumeration subtypes are seeded and isEnumType works immediately
    assertTrue(seededTree.isEnumType(NodeIds.NodeClass));
    assertTrue(seededTree.isEnumType(NodeIds.ServerState));
    assertTrue(seededTree.isEnumType(NodeIds.RedundancySupport));

    // Non-enum types (which are seeded) should not be recognized as enums
    assertFalse(seededTree.isEnumType(NodeIds.Int32));
    assertFalse(seededTree.isEnumType(NodeIds.String));
  }

  @Test
  void isStructTypeWorksForSeededTypes() {
    var seededTree =
        new LazyClientDataTypeTree(client, LazyClientDataTypeTreeSeed.createSeedTree());

    // Structure is in the seed
    assertTrue(seededTree.isResolved(NodeIds.Structure));

    // Note: isStructType uses isSubtypeOf, so Structure itself is NOT a struct type
    // (a type is not a subtype of itself)
    assertFalse(seededTree.isStructType(NodeIds.Structure));

    // Primitive types (which are seeded) are not structs
    assertFalse(seededTree.isStructType(NodeIds.Int32));
    assertFalse(seededTree.isStructType(NodeIds.String));
    assertFalse(seededTree.isStructType(NodeIds.Boolean));

    // Seeded structure subtypes can be verified with isStructType immediately
    assertTrue(seededTree.isStructType(NodeIds.XVType));
    assertTrue(seededTree.isStructType(NodeIds.RolePermissionType));
  }
}
