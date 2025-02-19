/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.nodes;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaViewNode;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;

class AttributeTestHelper {

  private AttributeTestHelper() {}

  /** Static test value used for RolePermissions attributes. */
  static final RolePermissionType[] ROLE_PERMISSIONS =
      new RolePermissionType[] {
        new RolePermissionType(NodeId.NULL_VALUE, new PermissionType(uint(0)))
      };

  /** Static test value used for UserRolePermissions attributes. */
  static final RolePermissionType[] USER_ROLE_PERMISSIONS =
      new RolePermissionType[] {
        new RolePermissionType(NodeId.NULL_VALUE, new PermissionType(uint(1)))
      };

  /** Static test value used for AccessRestrictions attributes. */
  static final AccessRestrictionType ACCESS_RESTRICTIONS = new AccessRestrictionType(ushort(0));

  /** Static test value used for DataTypeDefinition attributes. */
  static final DataTypeDefinition DATA_TYPE_DEFINITION =
      new StructureDefinition(
          NodeId.NULL_VALUE,
          NodeIds.Structure,
          StructureType.Structure,
          new StructureField[] {
            new StructureField(
                "foo",
                LocalizedText.NULL_VALUE,
                NodeIds.String,
                ValueRanks.Scalar,
                null,
                uint(0),
                false)
          });

  public static void configureDataTypeNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var dataTypeNode =
              new UaDataTypeNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestDataType"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestDataType"),
                  LocalizedText.english("TestDataType"),
                  LocalizedText.english("TestDataType Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  false,
                  DATA_TYPE_DEFINITION);

          dataTypeNode.addReference(
              new Reference(
                  dataTypeNode.getNodeId(),
                  NodeIds.HasSubtype,
                  NodeIds.Structure.expanded(),
                  Reference.Direction.INVERSE));

          nodeManager.addNode(dataTypeNode);
        });
  }

  public static void configureMethodNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var methodNode =
              new UaMethodNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestMethod"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestMethod"),
                  LocalizedText.english("TestMethod"),
                  LocalizedText.english("TestMethod Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  true,
                  true);

          nodeManager.addNode(methodNode);
        });
  }

  public static void configureObjectNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var objectNode =
              new UaObjectNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestObject"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestObject"),
                  LocalizedText.english("TestObject"),
                  LocalizedText.english("TestObject Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  ubyte(0));

          nodeManager.addNode(objectNode);
        });
  }

  public static void configureObjectTypeNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var objectTypeNode =
              new UaObjectTypeNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestObjectType"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestObjectType"),
                  LocalizedText.english("TestObjectType"),
                  LocalizedText.english("TestObjectType Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  true);

          nodeManager.addNode(objectTypeNode);
        });
  }

  public static void configureReferenceTypeNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var referenceTypeNode =
              new UaReferenceTypeNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestReferenceType"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestReferenceType"),
                  LocalizedText.english("TestReferenceType"),
                  LocalizedText.english("TestReferenceType Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  true,
                  true,
                  LocalizedText.english("Inverse"));

          nodeManager.addNode(referenceTypeNode);
        });
  }

  public static void configureVariableNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var variableNode =
              new UaVariableNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestVariable"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestVariable"),
                  LocalizedText.english("TestVariable"),
                  LocalizedText.english("TestVariable Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  new DataValue(new Variant(42)),
                  OpcUaDataType.Int32.getNodeId(),
                  -1,
                  null,
                  AccessLevel.toValue(AccessLevel.READ_WRITE),
                  AccessLevel.toValue(AccessLevel.READ_WRITE),
                  -1.0,
                  false,
                  new AccessLevelExType(uint(3)));

          nodeManager.addNode(variableNode);
        });
  }

  public static void configureVariableTypeNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var variableTypeNode =
              new UaVariableTypeNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestVariableType"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestVariableType"),
                  LocalizedText.english("TestVariableType"),
                  LocalizedText.english("TestVariableType Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  new DataValue(new Variant(42)),
                  OpcUaDataType.Int32.getNodeId(),
                  -1,
                  null,
                  false);

          nodeManager.addNode(variableTypeNode);
        });
  }

  public static void configureViewNode(TestNamespace namespace) {
    namespace.configure(
        (nodeContext, nodeManager) -> {
          var viewNode =
              new UaViewNode(
                  nodeContext,
                  new NodeId(namespace.getNamespaceIndex(), "TestView"),
                  new QualifiedName(namespace.getNamespaceIndex(), "TestView"),
                  LocalizedText.english("TestView"),
                  LocalizedText.english("TestView Description"),
                  uint(0),
                  uint(0),
                  ROLE_PERMISSIONS,
                  USER_ROLE_PERMISSIONS,
                  ACCESS_RESTRICTIONS,
                  true,
                  ubyte(0));

          nodeManager.addNode(viewNode);
        });
  }
}
