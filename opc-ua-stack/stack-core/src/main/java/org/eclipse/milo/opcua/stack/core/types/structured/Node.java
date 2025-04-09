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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part3/4.4.1</a>
 */
public class Node extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=258");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=260");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=259");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15068");

  private final NodeId nodeId;

  private final NodeClass nodeClass;

  private final QualifiedName browseName;

  private final LocalizedText displayName;

  private final LocalizedText description;

  private final UInteger writeMask;

  private final UInteger userWriteMask;

  private final RolePermissionType @Nullable [] rolePermissions;

  private final RolePermissionType @Nullable [] userRolePermissions;

  private final UShort accessRestrictions;

  private final ReferenceNode @Nullable [] references;

  public Node(
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
      ReferenceNode @Nullable [] references) {
    this.nodeId = nodeId;
    this.nodeClass = nodeClass;
    this.browseName = browseName;
    this.displayName = displayName;
    this.description = description;
    this.writeMask = writeMask;
    this.userWriteMask = userWriteMask;
    this.rolePermissions = rolePermissions;
    this.userRolePermissions = userRolePermissions;
    this.accessRestrictions = accessRestrictions;
    this.references = references;
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

  public NodeId getNodeId() {
    return nodeId;
  }

  public NodeClass getNodeClass() {
    return nodeClass;
  }

  public QualifiedName getBrowseName() {
    return browseName;
  }

  public LocalizedText getDisplayName() {
    return displayName;
  }

  public LocalizedText getDescription() {
    return description;
  }

  public UInteger getWriteMask() {
    return writeMask;
  }

  public UInteger getUserWriteMask() {
    return userWriteMask;
  }

  public RolePermissionType @Nullable [] getRolePermissions() {
    return rolePermissions;
  }

  public RolePermissionType @Nullable [] getUserRolePermissions() {
    return userRolePermissions;
  }

  public UShort getAccessRestrictions() {
    return accessRestrictions;
  }

  public ReferenceNode @Nullable [] getReferences() {
    return references;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Node that = (Node) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getNodeClass(), that.getNodeClass());
    eqb.append(getBrowseName(), that.getBrowseName());
    eqb.append(getDisplayName(), that.getDisplayName());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getWriteMask(), that.getWriteMask());
    eqb.append(getUserWriteMask(), that.getUserWriteMask());
    eqb.append(getRolePermissions(), that.getRolePermissions());
    eqb.append(getUserRolePermissions(), that.getUserRolePermissions());
    eqb.append(getAccessRestrictions(), that.getAccessRestrictions());
    eqb.append(getReferences(), that.getReferences());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getNodeClass());
    hcb.append(getBrowseName());
    hcb.append(getDisplayName());
    hcb.append(getDescription());
    hcb.append(getWriteMask());
    hcb.append(getUserWriteMask());
    hcb.append(getRolePermissions());
    hcb.append(getUserRolePermissions());
    hcb.append(getAccessRestrictions());
    hcb.append(getReferences());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", Node.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("nodeClass=" + getNodeClass());
    joiner.add("browseName=" + getBrowseName());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("description=" + getDescription());
    joiner.add("writeMask=" + getWriteMask());
    joiner.add("userWriteMask=" + getUserWriteMask());
    joiner.add("rolePermissions=" + java.util.Arrays.toString(getRolePermissions()));
    joiner.add("userRolePermissions=" + java.util.Arrays.toString(getUserRolePermissions()));
    joiner.add("accessRestrictions=" + getAccessRestrictions());
    joiner.add("references=" + java.util.Arrays.toString(getReferences()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 260),
        new NodeId(0, 22),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<Node> {
    @Override
    public Class<Node> getType() {
      return Node.class;
    }

    @Override
    public Node decodeType(EncodingContext context, UaDecoder decoder) {
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
      return new Node(
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
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, Node value) {
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
    }
  }
}
