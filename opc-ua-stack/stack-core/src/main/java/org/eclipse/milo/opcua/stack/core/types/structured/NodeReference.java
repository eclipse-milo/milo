package org.eclipse.milo.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.serialization.UaDecoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEncoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaStructure;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class NodeReference extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=580");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=582");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=581");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15203");

    private final NodeId nodeId;

    private final NodeId referenceTypeId;

    private final Boolean isForward;

    private final NodeId[] referencedNodeIds;

    public NodeReference(NodeId nodeId, NodeId referenceTypeId, Boolean isForward,
                         NodeId[] referencedNodeIds) {
        this.nodeId = nodeId;
        this.referenceTypeId = referenceTypeId;
        this.isForward = isForward;
        this.referencedNodeIds = referencedNodeIds;
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

    public NodeId getReferenceTypeId() {
        return referenceTypeId;
    }

    public Boolean getIsForward() {
        return isForward;
    }

    public NodeId[] getReferencedNodeIds() {
        return referencedNodeIds;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 582),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("NodeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("ReferenceTypeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("IsForward", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false),
                new StructureField("ReferencedNodeIds", LocalizedText.NULL_VALUE, new NodeId(0, 17), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<NodeReference> {
        @Override
        public Class<NodeReference> getType() {
            return NodeReference.class;
        }

        @Override
        public NodeReference decodeType(SerializationContext context, UaDecoder decoder) {
            NodeId nodeId = decoder.readNodeId("NodeId");
            NodeId referenceTypeId = decoder.readNodeId("ReferenceTypeId");
            Boolean isForward = decoder.readBoolean("IsForward");
            NodeId[] referencedNodeIds = decoder.readNodeIdArray("ReferencedNodeIds");
            return new NodeReference(nodeId, referenceTypeId, isForward, referencedNodeIds);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, NodeReference value) {
            encoder.writeNodeId("NodeId", value.getNodeId());
            encoder.writeNodeId("ReferenceTypeId", value.getReferenceTypeId());
            encoder.writeBoolean("IsForward", value.getIsForward());
            encoder.writeNodeIdArray("ReferencedNodeIds", value.getReferencedNodeIds());
        }
    }
}
