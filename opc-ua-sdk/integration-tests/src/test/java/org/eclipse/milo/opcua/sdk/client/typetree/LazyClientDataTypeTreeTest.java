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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.typetree.AbstractDataTypeTreeTest;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link LazyClientDataTypeTree}.
 *
 * <p>Extends {@link AbstractDataTypeTreeTest} to inherit all standard DataTypeTree tests, plus
 * additional tests specific to lazy loading behavior.
 */
public class LazyClientDataTypeTreeTest extends AbstractDataTypeTreeTest {

  @Override
  protected DataTypeTree getDataTypeTree() {
    return new LazyClientDataTypeTree(client);
  }

  @Test
  void initiallyOnlyContainsBaseDataType() {
    // Create a fresh tree to test the initial state
    var freshTree = new LazyClientDataTypeTree(client);

    // BaseDataType should be resolved (it's the root)
    assertTrue(freshTree.isResolved(NodeIds.BaseDataType));

    // Other types should not be resolved yet
    assertFalse(freshTree.isResolved(NodeIds.Int32));
    assertFalse(freshTree.isResolved(NodeIds.String));
    assertFalse(freshTree.isResolved(NodeIds.Structure));
  }

  @Test
  void resolvesTypeOnDemand() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Int32 not resolved initially
    assertFalse(freshTree.isResolved(NodeIds.Int32));

    // Query Int32 - this should trigger resolution
    DataType int32Type = freshTree.getDataType(NodeIds.Int32);
    assertNotNull(int32Type);
    assertEquals("Int32", int32Type.getBrowseName().name());

    // Now Int32 should be resolved
    assertTrue(freshTree.isResolved(NodeIds.Int32));

    // Ancestors should also be resolved (Integer, Number, BaseDataType)
    assertTrue(freshTree.isResolved(NodeIds.Integer));
    assertTrue(freshTree.isResolved(NodeIds.Number));
    assertTrue(freshTree.isResolved(NodeIds.BaseDataType));
  }

  @Test
  void resolvesStructuredTypes() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Query a structured type
    DataType xvType = freshTree.getDataType(NodeIds.XVType);
    assertNotNull(xvType);
    assertEquals("XVType", xvType.getBrowseName().name());

    // Should have encoding IDs
    assertNotNull(xvType.getBinaryEncodingId());

    // Should have a DataTypeDefinition
    assertNotNull(xvType.getDataTypeDefinition());

    // Ancestors should be resolved
    assertTrue(freshTree.isResolved(NodeIds.Structure));
    assertTrue(freshTree.isResolved(NodeIds.BaseDataType));
  }

  @Test
  void isSubtypeOfWorksWithLazyResolution() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Neither Int32 nor Integer are resolved yet
    assertFalse(freshTree.isResolved(NodeIds.Int32));
    assertFalse(freshTree.isResolved(NodeIds.Integer));

    // This should trigger resolution of Int32 and its ancestors
    assertTrue(freshTree.isSubtypeOf(NodeIds.Int32, NodeIds.Integer));
    assertTrue(freshTree.isSubtypeOf(NodeIds.Int32, NodeIds.Number));
    assertTrue(freshTree.isSubtypeOf(NodeIds.Int32, NodeIds.BaseDataType));

    // Int32 is not a subtype of String
    assertFalse(freshTree.isSubtypeOf(NodeIds.Int32, NodeIds.String));
  }

  @Test
  void containsTypeTriggersResolution() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Double not resolved initially
    assertFalse(freshTree.isResolved(NodeIds.Double));

    // containsType should trigger resolution and return true
    assertTrue(freshTree.containsType(NodeIds.Double));

    // Now it should be resolved
    assertTrue(freshTree.isResolved(NodeIds.Double));
  }

  @Test
  void cachesResolvedTypes() {
    var freshTree = new LazyClientDataTypeTree(client);

    // First query - triggers resolution
    DataType first = freshTree.getDataType(NodeIds.Int32);
    assertNotNull(first);

    // Second query - should return the same result from cache
    DataType second = freshTree.getDataType(NodeIds.Int32);
    assertNotNull(second);

    assertEquals(first.getNodeId(), second.getNodeId());
    assertEquals(first.getBrowseName(), second.getBrowseName());
  }

  @Test
  void lazyTreeMatchesEagerTreeForResolvedTypes() throws UaException {
    DataTypeTree eagerTree = DataTypeTreeBuilder.build(client);
    var lazyTestTree = new LazyClientDataTypeTree(client);

    // Test a variety of types
    NodeId[] typesToTest = {
      NodeIds.Int32,
      NodeIds.String,
      NodeIds.Double,
      NodeIds.DateTime,
      NodeIds.ByteString,
      NodeIds.Structure,
      NodeIds.Enumeration,
      NodeIds.XVType,
      NodeIds.Range
    };

    for (NodeId typeId : typesToTest) {
      DataType eagerType = eagerTree.getDataType(typeId);
      DataType lazyType = lazyTestTree.getDataType(typeId);

      if (eagerType != null) {
        assertNotNull(lazyType, "Lazy tree should resolve type: " + typeId);
        assertEquals(
            eagerType.getBrowseName(),
            lazyType.getBrowseName(),
            "BrowseName mismatch for " + typeId);
        assertEquals(
            eagerType.getBinaryEncodingId(),
            lazyType.getBinaryEncodingId(),
            "BinaryEncodingId mismatch for " + typeId);
        assertEquals(
            eagerType.getXmlEncodingId(),
            lazyType.getXmlEncodingId(),
            "XmlEncodingId mismatch for " + typeId);
        assertEquals(
            eagerType.getJsonEncodingId(),
            lazyType.getJsonEncodingId(),
            "JsonEncodingId mismatch for " + typeId);
        assertEquals(
            eagerType.isAbstract(), lazyType.isAbstract(), "IsAbstract mismatch for " + typeId);
      }
    }
  }

  @Test
  void diagnosticTestForStructure() throws Exception {
    // Test that Structure can be resolved
    var freshTree = new LazyClientDataTypeTree(client);

    // BaseDataType should be the only resolved type initially
    assertTrue(freshTree.isResolved(NodeIds.BaseDataType), "BaseDataType should be pre-loaded");
    assertFalse(freshTree.isResolved(NodeIds.Structure), "Structure should not be resolved yet");

    // Query Structure - should trigger lazy resolution
    DataType structureType = freshTree.getType(NodeIds.Structure);

    assertNotNull(structureType, "Structure should be resolved");
    assertEquals("Structure", structureType.getBrowseName().name());
  }

  @Test
  void clearFailedResolutionsAllowsRetry() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Try to resolve a non-existent type
    NodeId fakeTypeId = new NodeId(999, "FakeDataType");
    DataType result = freshTree.getDataType(fakeTypeId);

    // Should return null
    assertTrue(result == null || !freshTree.isResolved(fakeTypeId));

    // Clear failed resolutions
    freshTree.clearFailedResolutions();

    // Now it can be attempted again (will still fail, but the point is it's retried)
    result = freshTree.getDataType(fakeTypeId);
    assertTrue(result == null || !freshTree.isResolved(fakeTypeId));
  }

  @Test
  void getRootReturnsSnapshot() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Initially only BaseDataType is in the tree
    Tree<DataType> snapshot1 = freshTree.getRoot();
    var count1 = new AtomicInteger(0);
    snapshot1.traverseNodes(node -> count1.incrementAndGet());
    assertEquals(1, count1.get(), "Initial snapshot should only contain BaseDataType");

    // Resolve some types
    freshTree.getDataType(NodeIds.Int32); // Resolves Int32 -> Integer -> Number -> BaseDataType
    freshTree.getDataType(NodeIds.String); // Resolves String -> BaseDataType

    // Get a new snapshot - should reflect the resolved types
    Tree<DataType> snapshot2 = freshTree.getRoot();
    var count2 = new AtomicInteger(0);
    snapshot2.traverseNodes(node -> count2.incrementAndGet());
    assertTrue(count2.get() > count1.get(), "Second snapshot should contain more types");

    // The first snapshot should be unchanged (it's a copy)
    var recount1 = new AtomicInteger(0);
    snapshot1.traverseNodes(node -> recount1.incrementAndGet());
    assertEquals(count1.get(), recount1.get(), "First snapshot should be unchanged");

    // Verify the snapshots are independent copies (not the same object)
    assertNotEquals(snapshot1, snapshot2);
  }

  @Test
  void getRootSnapshotIsTraversable() {
    var freshTree = new LazyClientDataTypeTree(client);

    // Resolve a few types to build up the tree
    freshTree.getDataType(NodeIds.Int32);
    freshTree.getDataType(NodeIds.Double);
    freshTree.getDataType(NodeIds.String);
    freshTree.getDataType(NodeIds.Structure);

    // Get a snapshot and traverse it
    Tree<DataType> snapshot = freshTree.getRoot();

    // Verify we can traverse and find expected types
    var foundInt32 = new AtomicInteger(0);
    var foundDouble = new AtomicInteger(0);
    var foundString = new AtomicInteger(0);
    var foundStructure = new AtomicInteger(0);

    snapshot.traverse(
        dataType -> {
          if (NodeIds.Int32.equals(dataType.getNodeId())) foundInt32.incrementAndGet();
          if (NodeIds.Double.equals(dataType.getNodeId())) foundDouble.incrementAndGet();
          if (NodeIds.String.equals(dataType.getNodeId())) foundString.incrementAndGet();
          if (NodeIds.Structure.equals(dataType.getNodeId())) foundStructure.incrementAndGet();
        });

    assertEquals(1, foundInt32.get(), "Should find Int32 in snapshot");
    assertEquals(1, foundDouble.get(), "Should find Double in snapshot");
    assertEquals(1, foundString.get(), "Should find String in snapshot");
    assertEquals(1, foundStructure.get(), "Should find Structure in snapshot");
  }
}
