/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class DataTypeNode extends TypeNode implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=282");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=284");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=283");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15079");

  private final Boolean isAbstract;

  private final ExtensionObject dataTypeDefinition;

  public DataTypeNode(
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      UShort accessRestrictions,
      ReferenceNode @Nullable [] references,
      Boolean isAbstract,
      ExtensionObject dataTypeDefinition) {
    super(
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        references);
    this.isAbstract = isAbstract;
    this.dataTypeDefinition = dataTypeDefinition;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public Boolean getIsAbstract() {
    return isAbstract;
  }

  public ExtensionObject getDataTypeDefinition() {
    return dataTypeDefinition;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DataTypeNode that = (DataTypeNode) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getIsAbstract(), that.getIsAbstract());
    eqb.append(getDataTypeDefinition(), that.getDataTypeDefinition());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getIsAbstract());
    hcb.append(getDataTypeDefinition());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataTypeNode.class.getSimpleName() + "[", "]");
    joiner.add("isAbstract=" + getIsAbstract());
    joiner.add("dataTypeDefinition=" + getDataTypeDefinition());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 284),
        new NodeId(0, 11880),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeClass",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 257),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowseName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserWriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RolePermissions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 96),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserRolePermissions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 96),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AccessRestrictions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "References",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 285),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsAbstract",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataTypeDefinition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DataTypeNode> {
    @Override
    public Class<DataTypeNode> getType() {
      return DataTypeNode.class;
    }

    @Override
    public DataTypeNode decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final NodeClass nodeClass;
      final QualifiedName browseName;
      final LocalizedText displayName;
      final LocalizedText description;
      final UInteger writeMask;
      final UInteger userWriteMask;
      final RolePermissionType[] rolePermissions;
      final RolePermissionType[] userRolePermissions;
      final UShort accessRestrictions;
      final ReferenceNode[] references;
      final Boolean isAbstract;
      final ExtensionObject dataTypeDefinition;
      nodeId = decoder.decodeNodeId("NodeId");
      nodeClass = NodeClass.from(decoder.decodeEnum("NodeClass"));
      browseName = decoder.decodeQualifiedName("BrowseName");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      writeMask = decoder.decodeUInt32("WriteMask");
      userWriteMask = decoder.decodeUInt32("UserWriteMask");
      rolePermissions =
          (RolePermissionType[])
              decoder.decodeStructArray("RolePermissions", RolePermissionType.TYPE_ID);
      userRolePermissions =
          (RolePermissionType[])
              decoder.decodeStructArray("UserRolePermissions", RolePermissionType.TYPE_ID);
      accessRestrictions = decoder.decodeUInt16("AccessRestrictions");
      references = (ReferenceNode[]) decoder.decodeStructArray("References", ReferenceNode.TYPE_ID);
      isAbstract = decoder.decodeBoolean("IsAbstract");
      dataTypeDefinition = decoder.decodeExtensionObject("DataTypeDefinition");
      return new DataTypeNode(
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          references,
          isAbstract,
          dataTypeDefinition);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DataTypeNode value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeEnum("NodeClass", value.getNodeClass());
      encoder.encodeQualifiedName("BrowseName", value.getBrowseName());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt32("WriteMask", value.getWriteMask());
      encoder.encodeUInt32("UserWriteMask", value.getUserWriteMask());
      encoder.encodeStructArray(
          "RolePermissions", value.getRolePermissions(), RolePermissionType.TYPE_ID);
      encoder.encodeStructArray(
          "UserRolePermissions", value.getUserRolePermissions(), RolePermissionType.TYPE_ID);
      encoder.encodeUInt16("AccessRestrictions", value.getAccessRestrictions());
      encoder.encodeStructArray("References", value.getReferences(), ReferenceNode.TYPE_ID);
      encoder.encodeBoolean("IsAbstract", value.getIsAbstract());
      encoder.encodeExtensionObject("DataTypeDefinition", value.getDataTypeDefinition());
    }
  }
}
