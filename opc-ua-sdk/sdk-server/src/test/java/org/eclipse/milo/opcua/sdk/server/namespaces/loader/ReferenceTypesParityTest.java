/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link ReferenceTypes} stays in exact 1:1 parity with the standard OPC UA {@code
 * ReferenceType} nodes loaded by {@link ReferenceTypeNodeLoader} and with {@link NodeIds}.
 *
 * <p>{@link ReferenceTypes} and {@link ReferenceTypeNodeLoader} are generated from the OPC UA
 * standard NodeSet by an external generator; this guard catches drift whenever a new NodeSet
 * version updates {@link NodeIds} and {@link ReferenceTypeNodeLoader} without regenerating {@link
 * ReferenceTypes}.
 */
class ReferenceTypesParityTest {

  // Every standard ReferenceType node loaded into namespace 0 must have an identically named
  // constant with the same NodeId in ReferenceTypes, and ReferenceTypes must not contain stale
  // or extra constants that are absent from the standard NodeSet.
  @Test
  void referenceTypesAreInOneToOneParityWithReferenceTypeNodeLoader() throws Exception {
    Map<String, NodeId> loadedByName = loadStandardReferenceTypes();
    Map<String, NodeId> constantsByName = readReferenceTypeConstants();

    assertFalse(loadedByName.isEmpty(), "ReferenceTypeNodeLoader must load standard types");
    assertEquals(
        loadedByName.size(),
        Set.copyOf(loadedByName.values()).size(),
        "loaded standard ReferenceType NodeIds must be unique");
    assertEquals(
        constantsByName.size(),
        Set.copyOf(constantsByName.values()).size(),
        "ReferenceTypes constant NodeIds must be unique");

    assertEquals(
        loadedByName,
        constantsByName,
        "ReferenceTypes constants must match ReferenceTypeNodeLoader 1:1 by name and NodeId");
  }

  // Every constant on ReferenceTypes must also match the identically named constant on NodeIds
  // so callers switching between ReferenceTypes and NodeIds always observe identical NodeIds.
  @Test
  void everyReferenceTypesConstantMatchesNodeIds() throws Exception {
    Map<String, NodeId> constantsByName = readReferenceTypeConstants();
    assertFalse(constantsByName.isEmpty(), "ReferenceTypes must declare standard constants");

    List<String> mismatches = new ArrayList<>();

    for (Map.Entry<String, NodeId> entry : constantsByName.entrySet()) {
      String name = entry.getKey();
      NodeId expected = entry.getValue();

      try {
        Field nodeIdsField = NodeIds.class.getField(name);
        int modifiers = nodeIdsField.getModifiers();
        if (!Modifier.isPublic(modifiers)
            || !Modifier.isStatic(modifiers)
            || !Modifier.isFinal(modifiers)
            || nodeIdsField.getType() != NodeId.class) {
          mismatches.add(name + ": NodeIds." + name + " is not public static final NodeId");
          continue;
        }

        NodeId actual = (NodeId) nodeIdsField.get(null);
        if (!expected.equals(actual)) {
          mismatches.add(name + ": ReferenceTypes=" + expected + " != NodeIds=" + actual);
        }
      } catch (NoSuchFieldException e) {
        mismatches.add(name + ": missing from NodeIds");
      }
    }

    assertEquals(List.of(), mismatches, "ReferenceTypes constants must match NodeIds");
  }

  private static Map<String, NodeId> loadStandardReferenceTypes() throws Exception {
    var nodeManager = new UaNodeManager();
    var namespaceTable = new NamespaceTable();

    UaNodeContext context =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            throw new UnsupportedOperationException("not needed for ReferenceTypeNodeLoader");
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }

          @Override
          public NamespaceTable getNamespaceTable() {
            return namespaceTable;
          }
        };

    new ReferenceTypeNodeLoader(context, nodeManager).load();

    Map<String, NodeId> loadedByName = new TreeMap<>();
    for (UaNode node : nodeManager.getNodes()) {
      assertTrue(
          node instanceof UaReferenceTypeNode,
          "expected only UaReferenceTypeNode instances from ReferenceTypeNodeLoader");

      String name = node.getBrowseName().name();
      assertNotNull(name, "standard ReferenceType browse name must not be null");
      assertEquals(
          0,
          node.getBrowseName().namespaceIndex().intValue(),
          "standard ReferenceType " + name + " must be in namespace 0");

      NodeId previous = loadedByName.put(name, node.getNodeId());
      assertNull(previous, "duplicate standard ReferenceType browse name: " + name);
    }

    return loadedByName;
  }

  private static Map<String, NodeId> readReferenceTypeConstants() throws IllegalAccessException {
    Map<String, NodeId> constantsByName = new LinkedHashMap<>();

    for (Field field : ReferenceTypes.class.getDeclaredFields()) {
      if (field.isSynthetic()) {
        continue;
      }

      int modifiers = field.getModifiers();
      assertTrue(
          Modifier.isPublic(modifiers)
              && Modifier.isStatic(modifiers)
              && Modifier.isFinal(modifiers),
          "ReferenceTypes." + field.getName() + " must be public static final");
      assertEquals(
          NodeId.class,
          field.getType(),
          "ReferenceTypes." + field.getName() + " must have type NodeId");

      NodeId nodeId = (NodeId) field.get(null);
      assertNotNull(nodeId, "ReferenceTypes." + field.getName() + " must not be null");

      NodeId previous = constantsByName.put(field.getName(), nodeId);
      assertNull(previous, "duplicate constant in ReferenceTypes: " + field.getName());
    }

    return new TreeMap<>(constantsByName);
  }
}
