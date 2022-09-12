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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.11</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class TimeZoneDataType extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=8912");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=8917");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=8913");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15086");

    private final Short offset;

    private final Boolean daylightSavingInOffset;

    public TimeZoneDataType(Short offset, Boolean daylightSavingInOffset) {
        this.offset = offset;
        this.daylightSavingInOffset = daylightSavingInOffset;
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

    public Short getOffset() {
        return offset;
    }

    public Boolean getDaylightSavingInOffset() {
        return daylightSavingInOffset;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 8917),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("Offset", LocalizedText.NULL_VALUE, new NodeId(0, 4), -1, null, UInteger.valueOf(0), false),
                new StructureField("DaylightSavingInOffset", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<TimeZoneDataType> {
        @Override
        public Class<TimeZoneDataType> getType() {
            return TimeZoneDataType.class;
        }

        @Override
        public TimeZoneDataType decodeType(SerializationContext context, UaDecoder decoder) {
            Short offset = decoder.readInt16("Offset");
            Boolean daylightSavingInOffset = decoder.readBoolean("DaylightSavingInOffset");
            return new TimeZoneDataType(offset, daylightSavingInOffset);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               TimeZoneDataType value) {
            encoder.writeInt16("Offset", value.getOffset());
            encoder.writeBoolean("DaylightSavingInOffset", value.getDaylightSavingInOffset());
        }
    }
}
