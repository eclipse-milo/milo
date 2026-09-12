/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class ObjectNode extends InstanceNode implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=261");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=263");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=262");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15071");

  private final UByte eventNotifier;

  public ObjectNode(
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
      UByte eventNotifier) {
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
    this.eventNotifier = eventNotifier;
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

  public UByte getEventNotifier() {
    return eventNotifier;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ObjectNode that = (ObjectNode) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getEventNotifier(), that.getEventNotifier());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEventNotifier());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ObjectNode.class.getSimpleName() + "[", "]");
    joiner.add("eventNotifier=" + getEventNotifier());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        NodeId.parse("i=263"),
        NodeId.parse("i=11879"),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=17"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeClass",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=257"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowseName",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=20"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=21"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=21"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriteMask",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=7"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserWriteMask",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=7"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RolePermissions",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=96"),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserRolePermissions",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=96"),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AccessRestrictions",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=5"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "References",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=285"),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventNotifier",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=3"),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ObjectNode> {
    @Override
    public Class<ObjectNode> getType() {
      return ObjectNode.class;
    }

    @Override
    public ObjectNode decodeType(EncodingContext context, UaDecoder decoder) {
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
      final UByte eventNotifier;
      nodeId = decoder.decodeNodeId("NodeId");
      {
        Integer enumValue = decoder.decodeEnum("NodeClass");
        if (enumValue != null && !((Object) enumValue instanceof NodeClass)) {
          if (!(enumValue instanceof Integer)) {
            throw new UaSerializationException(
                StatusCodes.Bad_TypeMismatch,
                "NodeClass: expected org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass"
                    + " or Int32, got "
                    + enumValue);
          }
          if (NodeClass.from((Integer) enumValue) == null) {
            throw new UaSerializationException(
                StatusCodes.Bad_OutOfRange,
                "NodeClass: unknown org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass"
                    + " value "
                    + enumValue);
          }
        }
        nodeClass = enumValue == null ? null : NodeClass.from(enumValue);
      }
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
      eventNotifier = decoder.decodeByte("EventNotifier");
      return new ObjectNode(
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
          eventNotifier);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ObjectNode value) {
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
      encoder.encodeByte("EventNotifier", value.getEventNotifier());
    }
  }
}
