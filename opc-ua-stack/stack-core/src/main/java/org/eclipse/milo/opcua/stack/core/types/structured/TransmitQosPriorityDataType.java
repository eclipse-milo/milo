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

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.1.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.1.4.2</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class TransmitQosPriorityDataType extends TransmitQosDataType implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=23605");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23857");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23925");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23993");

    private final String priorityLabel;

    public TransmitQosPriorityDataType(String priorityLabel) {
        this.priorityLabel = priorityLabel;
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

    public String getPriorityLabel() {
        return priorityLabel;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 23857),
            new NodeId(0, 23604),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("PriorityLabel", LocalizedText.NULL_VALUE, new NodeId(0, 12), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<TransmitQosPriorityDataType> {
        @Override
        public Class<TransmitQosPriorityDataType> getType() {
            return TransmitQosPriorityDataType.class;
        }

        @Override
        public TransmitQosPriorityDataType decodeType(SerializationContext context, UaDecoder decoder) {
            String priorityLabel = decoder.readString("PriorityLabel");
            return new TransmitQosPriorityDataType(priorityLabel);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               TransmitQosPriorityDataType value) {
            encoder.writeString("PriorityLabel", value.getPriorityLabel());
        }
    }
}
