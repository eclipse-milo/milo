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
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class BrowseDescription extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=514");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=516");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=515");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15180");

    private final NodeId nodeId;

    private final BrowseDirection browseDirection;

    private final NodeId referenceTypeId;

    private final Boolean includeSubtypes;

    private final UInteger nodeClassMask;

    private final UInteger resultMask;

    public BrowseDescription(NodeId nodeId, BrowseDirection browseDirection, NodeId referenceTypeId,
                             Boolean includeSubtypes, UInteger nodeClassMask, UInteger resultMask) {
        this.nodeId = nodeId;
        this.browseDirection = browseDirection;
        this.referenceTypeId = referenceTypeId;
        this.includeSubtypes = includeSubtypes;
        this.nodeClassMask = nodeClassMask;
        this.resultMask = resultMask;
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

    public BrowseDirection getBrowseDirection() {
        return browseDirection;
    }

    public NodeId getReferenceTypeId() {
        return referenceTypeId;
    }

    public Boolean getIncludeSubtypes() {
        return includeSubtypes;
    }

    public UInteger getNodeClassMask() {
        return nodeClassMask;
    }

    public UInteger getResultMask() {
        return resultMask;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 516),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("NodeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("BrowseDirection", LocalizedText.NULL_VALUE, new NodeId(0, 510), -1, null, UInteger.valueOf(0), false),
                new StructureField("ReferenceTypeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("IncludeSubtypes", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false),
                new StructureField("NodeClassMask", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("ResultMask", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<BrowseDescription> {
        @Override
        public Class<BrowseDescription> getType() {
            return BrowseDescription.class;
        }

        @Override
        public BrowseDescription decodeType(SerializationContext context, UaDecoder decoder) {
            NodeId nodeId = decoder.readNodeId("NodeId");
            BrowseDirection browseDirection = (BrowseDirection) decoder.readEnum("BrowseDirection", BrowseDirection.class);
            NodeId referenceTypeId = decoder.readNodeId("ReferenceTypeId");
            Boolean includeSubtypes = decoder.readBoolean("IncludeSubtypes");
            UInteger nodeClassMask = decoder.readUInt32("NodeClassMask");
            UInteger resultMask = decoder.readUInt32("ResultMask");
            return new BrowseDescription(nodeId, browseDirection, referenceTypeId, includeSubtypes, nodeClassMask, resultMask);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               BrowseDescription value) {
            encoder.writeNodeId("NodeId", value.getNodeId());
            encoder.writeEnum("BrowseDirection", value.getBrowseDirection());
            encoder.writeNodeId("ReferenceTypeId", value.getReferenceTypeId());
            encoder.writeBoolean("IncludeSubtypes", value.getIncludeSubtypes());
            encoder.writeUInt32("NodeClassMask", value.getNodeClassMask());
            encoder.writeUInt32("ResultMask", value.getResultMask());
        }
    }
}
