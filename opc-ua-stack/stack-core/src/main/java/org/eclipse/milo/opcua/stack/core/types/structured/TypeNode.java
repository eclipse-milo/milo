package org.eclipse.milo.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.serialization.UaDecoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEncoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class TypeNode extends Node implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=11880");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=11890");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=11888");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15070");

    public TypeNode(NodeId nodeId, NodeClass nodeClass, QualifiedName browseName,
                    LocalizedText displayName, LocalizedText description, UInteger writeMask,
                    UInteger userWriteMask, RolePermissionType[] rolePermissions,
                    RolePermissionType[] userRolePermissions, UShort accessRestrictions,
                    ReferenceNode[] references) {
        super(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions, references);
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

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 11890),
            new NodeId(0, 258),
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
                new StructureField("References", LocalizedText.NULL_VALUE, new NodeId(0, 285), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<TypeNode> {
        @Override
        public Class<TypeNode> getType() {
            return TypeNode.class;
        }

        @Override
        public TypeNode decodeType(SerializationContext context, UaDecoder decoder) {
            NodeId nodeId = decoder.readNodeId("NodeId");
            NodeClass nodeClass = NodeClass.from(decoder.readEnum("NodeClass"));
            QualifiedName browseName = decoder.readQualifiedName("BrowseName");
            LocalizedText displayName = decoder.readLocalizedText("DisplayName");
            LocalizedText description = decoder.readLocalizedText("Description");
            UInteger writeMask = decoder.readUInt32("WriteMask");
            UInteger userWriteMask = decoder.readUInt32("UserWriteMask");
            RolePermissionType[] rolePermissions = (RolePermissionType[]) decoder.readStructArray("RolePermissions", RolePermissionType.TYPE_ID);
            RolePermissionType[] userRolePermissions = (RolePermissionType[]) decoder.readStructArray("UserRolePermissions", RolePermissionType.TYPE_ID);
            UShort accessRestrictions = decoder.readUInt16("AccessRestrictions");
            ReferenceNode[] references = (ReferenceNode[]) decoder.readStructArray("References", ReferenceNode.TYPE_ID);
            return new TypeNode(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions, references);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, TypeNode value) {
            encoder.writeNodeId("NodeId", value.getNodeId());
            encoder.writeEnum("NodeClass", value.getNodeClass());
            encoder.writeQualifiedName("BrowseName", value.getBrowseName());
            encoder.writeLocalizedText("DisplayName", value.getDisplayName());
            encoder.writeLocalizedText("Description", value.getDescription());
            encoder.writeUInt32("WriteMask", value.getWriteMask());
            encoder.writeUInt32("UserWriteMask", value.getUserWriteMask());
            encoder.writeStructArray("RolePermissions", value.getRolePermissions(), RolePermissionType.TYPE_ID);
            encoder.writeStructArray("UserRolePermissions", value.getUserRolePermissions(), RolePermissionType.TYPE_ID);
            encoder.writeUInt16("AccessRestrictions", value.getAccessRestrictions());
            encoder.writeStructArray("References", value.getReferences(), ReferenceNode.TYPE_ID);
        }
    }
}
