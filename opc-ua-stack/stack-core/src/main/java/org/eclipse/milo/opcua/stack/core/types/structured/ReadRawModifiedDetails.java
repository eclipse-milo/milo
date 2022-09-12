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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.3/#6.4.3.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.3/#6.4.3.1</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class ReadRawModifiedDetails extends HistoryReadDetails implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=647");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=649");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=648");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15263");

    private final Boolean isReadModified;

    private final DateTime startTime;

    private final DateTime endTime;

    private final UInteger numValuesPerNode;

    private final Boolean returnBounds;

    public ReadRawModifiedDetails(Boolean isReadModified, DateTime startTime, DateTime endTime,
                                  UInteger numValuesPerNode, Boolean returnBounds) {
        this.isReadModified = isReadModified;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numValuesPerNode = numValuesPerNode;
        this.returnBounds = returnBounds;
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

    public Boolean getIsReadModified() {
        return isReadModified;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public UInteger getNumValuesPerNode() {
        return numValuesPerNode;
    }

    public Boolean getReturnBounds() {
        return returnBounds;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 649),
            new NodeId(0, 641),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("IsReadModified", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false),
                new StructureField("StartTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false),
                new StructureField("EndTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false),
                new StructureField("NumValuesPerNode", LocalizedText.NULL_VALUE, new NodeId(0, 289), -1, null, UInteger.valueOf(0), false),
                new StructureField("ReturnBounds", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<ReadRawModifiedDetails> {
        @Override
        public Class<ReadRawModifiedDetails> getType() {
            return ReadRawModifiedDetails.class;
        }

        @Override
        public ReadRawModifiedDetails decodeType(SerializationContext context, UaDecoder decoder) {
            Boolean isReadModified = decoder.readBoolean("IsReadModified");
            DateTime startTime = decoder.readDateTime("StartTime");
            DateTime endTime = decoder.readDateTime("EndTime");
            UInteger numValuesPerNode = decoder.readUInt32("NumValuesPerNode");
            Boolean returnBounds = decoder.readBoolean("ReturnBounds");
            return new ReadRawModifiedDetails(isReadModified, startTime, endTime, numValuesPerNode, returnBounds);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               ReadRawModifiedDetails value) {
            encoder.writeBoolean("IsReadModified", value.getIsReadModified());
            encoder.writeDateTime("StartTime", value.getStartTime());
            encoder.writeDateTime("EndTime", value.getEndTime());
            encoder.writeUInt32("NumValuesPerNode", value.getNumValuesPerNode());
            encoder.writeBoolean("ReturnBounds", value.getReturnBounds());
        }
    }
}
