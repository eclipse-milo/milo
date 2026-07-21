/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

/**
 * Tests for the WP5 registry evolution of {@link ObjectTypeManager} and {@link
 * VariableTypeManager}: the additive snapshot-constructor registration overload and the
 * class-exposing {@code getRegisteredType} lookup, with the pre-existing tuple and {@code Legacy*}
 * surfaces byte-for-byte untouched (R4).
 */
public class TypeManagerRegistrationTest {

  private final NodeId typeId = new NodeId(1, "TestType");

  @Test
  void objectSnapshotRegistrationExposesClassAndSnapshotConstructor() {
    ObjectTypeManager manager = new ObjectTypeManager();

    ObjectTypeManager.SnapshotConstructor snapshotConstructor =
        (context, nodeId, attributes) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerObjectType(typeId, UaObjectNode.class, snapshotConstructor);

    ObjectTypeManager.RegisteredObjectType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertEquals(UaObjectNode.class, registered.nodeClass());
    assertSame(snapshotConstructor, registered.snapshotConstructor());
    assertNull(registered.nodeConstructor());

    // The legacy NodeFactory lookup path does not see snapshot registrations; it falls back to
    // its default construction exactly as it does for an unregistered type.
    assertTrue(manager.getNodeConstructor(typeId).isEmpty());
  }

  @Test
  void objectTupleRegistrationExposesClassAndTupleConstructor() {
    ObjectTypeManager manager = new ObjectTypeManager();

    ObjectTypeManager.ObjectNodeConstructor tupleConstructor =
        (context,
            nodeId,
            browseName,
            displayName,
            description,
            writeMask,
            userWriteMask,
            rolePermissions,
            userRolePermissions,
            accessRestrictions) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerObjectType(typeId, UaObjectNode.class, tupleConstructor);

    // The pre-existing lookup is unchanged (the legacy engine resolves this registration)...
    assertSame(tupleConstructor, manager.getNodeConstructor(typeId).orElseThrow());

    // ...and the new engine sees the same registration through the class-exposing accessor (R4).
    ObjectTypeManager.RegisteredObjectType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertEquals(UaObjectNode.class, registered.nodeClass());
    assertSame(tupleConstructor, registered.nodeConstructor());
    assertNull(registered.snapshotConstructor());
  }

  @Test
  void objectLegacyRegistrationStillAdaptsAndIsVisibleToBothLookups() {
    ObjectTypeManager manager = new ObjectTypeManager();

    ObjectTypeManager.LegacyObjectNodeConstructor legacyConstructor =
        (context, nodeId, browseName, displayName, description, writeMask, userWriteMask) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerObjectType(typeId, UaObjectNode.class, legacyConstructor);

    assertTrue(manager.getNodeConstructor(typeId).isPresent(), "legacy lookup unchanged");

    ObjectTypeManager.RegisteredObjectType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertNotNull(registered.nodeConstructor(), "adapted constructor visible to the new engine");
    assertNull(registered.snapshotConstructor());
  }

  @Test
  void variableSnapshotRegistrationExposesClassAndSnapshotConstructor() {
    VariableTypeManager manager = new VariableTypeManager();

    VariableTypeManager.SnapshotConstructor snapshotConstructor =
        (context, nodeId, attributes) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerVariableType(typeId, UaVariableNode.class, snapshotConstructor);

    VariableTypeManager.RegisteredVariableType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertEquals(UaVariableNode.class, registered.nodeClass());
    assertSame(snapshotConstructor, registered.snapshotConstructor());
    assertNull(registered.nodeConstructor());

    assertTrue(manager.getNodeConstructor(typeId).isEmpty());
  }

  @Test
  void variableTupleRegistrationExposesClassAndTupleConstructor() {
    VariableTypeManager manager = new VariableTypeManager();

    VariableTypeManager.VariableNodeConstructor tupleConstructor =
        (context,
            nodeId,
            browseName,
            displayName,
            description,
            writeMask,
            userWriteMask,
            rolePermissions,
            userRolePermissions,
            accessRestrictions,
            value,
            dataType,
            valueRank,
            arrayDimensions) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerVariableType(typeId, UaVariableNode.class, tupleConstructor);

    assertSame(tupleConstructor, manager.getNodeConstructor(typeId).orElseThrow());

    VariableTypeManager.RegisteredVariableType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertEquals(UaVariableNode.class, registered.nodeClass());
    assertSame(tupleConstructor, registered.nodeConstructor());
    assertNull(registered.snapshotConstructor());
  }

  @Test
  void variableLegacyRegistrationStillAdaptsAndIsVisibleToBothLookups() {
    VariableTypeManager manager = new VariableTypeManager();

    VariableTypeManager.LegacyVariableNodeConstructor legacyConstructor =
        (context, nodeId, browseName, displayName, description, writeMask, userWriteMask) -> {
          throw new UnsupportedOperationException("never invoked here");
        };

    manager.registerVariableType(typeId, UaVariableNode.class, legacyConstructor);

    assertTrue(manager.getNodeConstructor(typeId).isPresent(), "legacy lookup unchanged");

    VariableTypeManager.RegisteredVariableType registered =
        manager.getRegisteredType(typeId).orElseThrow();
    assertNotNull(registered.nodeConstructor(), "adapted constructor visible to the new engine");
    assertNull(registered.snapshotConstructor());
  }

  @Test
  void unregisteredTypeResolvesToNothingInBothLookups() {
    ObjectTypeManager objectTypeManager = new ObjectTypeManager();
    assertTrue(objectTypeManager.getNodeConstructor(typeId).isEmpty());
    assertTrue(objectTypeManager.getRegisteredType(typeId).isEmpty());

    VariableTypeManager variableTypeManager = new VariableTypeManager();
    assertTrue(variableTypeManager.getNodeConstructor(typeId).isEmpty());
    assertTrue(variableTypeManager.getRegisteredType(typeId).isEmpty());
  }
}
