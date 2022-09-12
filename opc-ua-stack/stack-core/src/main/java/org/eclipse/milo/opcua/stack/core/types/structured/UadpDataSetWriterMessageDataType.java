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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.6</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class UadpDataSetWriterMessageDataType extends DataSetWriterMessageDataType implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15652");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15717");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16015");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16391");

    private final UadpDataSetMessageContentMask dataSetMessageContentMask;

    private final UShort configuredSize;

    private final UShort networkMessageNumber;

    private final UShort dataSetOffset;

    public UadpDataSetWriterMessageDataType(UadpDataSetMessageContentMask dataSetMessageContentMask,
                                            UShort configuredSize, UShort networkMessageNumber, UShort dataSetOffset) {
        this.dataSetMessageContentMask = dataSetMessageContentMask;
        this.configuredSize = configuredSize;
        this.networkMessageNumber = networkMessageNumber;
        this.dataSetOffset = dataSetOffset;
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

    public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
        return dataSetMessageContentMask;
    }

    public UShort getConfiguredSize() {
        return configuredSize;
    }

    public UShort getNetworkMessageNumber() {
        return networkMessageNumber;
    }

    public UShort getDataSetOffset() {
        return dataSetOffset;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 15717),
            new NodeId(0, 15605),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("DataSetMessageContentMask", LocalizedText.NULL_VALUE, new NodeId(0, 15646), -1, null, UInteger.valueOf(0), false),
                new StructureField("ConfiguredSize", LocalizedText.NULL_VALUE, new NodeId(0, 5), -1, null, UInteger.valueOf(0), false),
                new StructureField("NetworkMessageNumber", LocalizedText.NULL_VALUE, new NodeId(0, 5), -1, null, UInteger.valueOf(0), false),
                new StructureField("DataSetOffset", LocalizedText.NULL_VALUE, new NodeId(0, 5), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<UadpDataSetWriterMessageDataType> {
        @Override
        public Class<UadpDataSetWriterMessageDataType> getType() {
            return UadpDataSetWriterMessageDataType.class;
        }

        @Override
        public UadpDataSetWriterMessageDataType decodeType(SerializationContext context,
                                                           UaDecoder decoder) {
            UadpDataSetMessageContentMask dataSetMessageContentMask = new UadpDataSetMessageContentMask(decoder.readUInt32("DataSetMessageContentMask"));
            UShort configuredSize = decoder.readUInt16("ConfiguredSize");
            UShort networkMessageNumber = decoder.readUInt16("NetworkMessageNumber");
            UShort dataSetOffset = decoder.readUInt16("DataSetOffset");
            return new UadpDataSetWriterMessageDataType(dataSetMessageContentMask, configuredSize, networkMessageNumber, dataSetOffset);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               UadpDataSetWriterMessageDataType value) {
            encoder.writeUInt32("DataSetMessageContentMask", value.getDataSetMessageContentMask().getValue());
            encoder.writeUInt16("ConfiguredSize", value.getConfiguredSize());
            encoder.writeUInt16("NetworkMessageNumber", value.getNetworkMessageNumber());
            encoder.writeUInt16("DataSetOffset", value.getDataSetOffset());
        }
    }
}
