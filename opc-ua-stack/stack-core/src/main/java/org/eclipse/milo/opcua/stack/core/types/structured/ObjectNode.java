/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;

import lombok.EqualsAndHashCode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.jetbrains.annotations.Nullable;

@EqualsAndHashCode(
    callSuper = true
)
public class ObjectNode extends InstanceNode implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=261");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=263");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=262");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15071");

    private final UByte eventNotifier;

    public ObjectNode(NodeId nodeId, NodeClass nodeClass, QualifiedName browseName,
                      LocalizedText displayName, LocalizedText description, UInteger writeMask,
                      UInteger userWriteMask, RolePermissionType @Nullable [] rolePermissions,
                      RolePermissionType @Nullable [] userRolePermissions, UShort accessRestrictions,
                      ReferenceNode @Nullable [] references, UByte eventNotifier) {
        super(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions, references);
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
    public String toString() {
        var joiner = new StringJoiner(", ", ObjectNode.class.getSimpleName() + "[", "]");
        joiner.add("eventNotifier=" + getEventNotifier());
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 263),
            new NodeId(0, 11879),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("NodeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("NodeClass", LocalizedText.NULL_VALUE, new NodeId(0, 257), -1, null, UInteger.valueOf(0), false),
                new StructureField("BrowseName", LocalizedText.NULL_VALUE, new NodeId(0, 20), -1, null, UInteger.valueOf(0), false),
                new StructureField("DisplayName", LocalizedText.NULL_VALUE, new NodeId(0, 21), -1, null, UInteger.valueOf(0), false),
                new StructureField("Description", LocalizedText.NULL_VALUE, new NodeId(0, 21), -1, null, UInteger.valueOf(0), false),
                new StructureField("WriteMask", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("UserWriteMask", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("RolePermissions", LocalizedText.NULL_VALUE, new NodeId(0, 96), 1, null, UInteger.valueOf(0), false),
                new StructureField("UserRolePermissions", LocalizedText.NULL_VALUE, new NodeId(0, 96), 1, null, UInteger.valueOf(0), false),
                new StructureField("AccessRestrictions", LocalizedText.NULL_VALUE, new NodeId(0, 5), -1, null, UInteger.valueOf(0), false),
                new StructureField("References", LocalizedText.NULL_VALUE, new NodeId(0, 285), 1, null, UInteger.valueOf(0), false),
                new StructureField("EventNotifier", LocalizedText.NULL_VALUE, new NodeId(0, 3), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<ObjectNode> {
        @Override
        public Class<ObjectNode> getType() {
            return ObjectNode.class;
        }

        @Override
        public ObjectNode decodeType(EncodingContext context, UaDecoder decoder) {
            NodeId nodeId = decoder.decodeNodeId("NodeId");
            NodeClass nodeClass = NodeClass.from(decoder.decodeEnum("NodeClass"));
            QualifiedName browseName = decoder.decodeQualifiedName("BrowseName");
            LocalizedText displayName = decoder.decodeLocalizedText("DisplayName");
            LocalizedText description = decoder.decodeLocalizedText("Description");
            UInteger writeMask = decoder.decodeUInt32("WriteMask");
            UInteger userWriteMask = decoder.decodeUInt32("UserWriteMask");
            RolePermissionType[] rolePermissions = (RolePermissionType[]) decoder.decodeStructArray("RolePermissions", RolePermissionType.TYPE_ID);
            RolePermissionType[] userRolePermissions = (RolePermissionType[]) decoder.decodeStructArray("UserRolePermissions", RolePermissionType.TYPE_ID);
            UShort accessRestrictions = decoder.decodeUInt16("AccessRestrictions");
            ReferenceNode[] references = (ReferenceNode[]) decoder.decodeStructArray("References", ReferenceNode.TYPE_ID);
            UByte eventNotifier = decoder.decodeByte("EventNotifier");
            return new ObjectNode(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions, references, eventNotifier);
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
            encoder.encodeStructArray("RolePermissions", value.getRolePermissions(), RolePermissionType.TYPE_ID);
            encoder.encodeStructArray("UserRolePermissions", value.getUserRolePermissions(), RolePermissionType.TYPE_ID);
            encoder.encodeUInt16("AccessRestrictions", value.getAccessRestrictions());
            encoder.encodeStructArray("References", value.getReferences(), ReferenceNode.TYPE_ID);
            encoder.encodeByte("EventNotifier", value.getEventNotifier());
        }
    }
}
