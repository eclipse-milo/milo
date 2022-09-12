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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.8">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.8</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class XVType extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12080");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12090");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12082");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15380");

    private final Double x;

    private final Float value;

    public XVType(Double x, Float value) {
        this.x = x;
        this.value = value;
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

    public Double getX() {
        return x;
    }

    public Float getValue() {
        return value;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 12090),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("X", LocalizedText.NULL_VALUE, new NodeId(0, 11), -1, null, UInteger.valueOf(0), false),
                new StructureField("Value", LocalizedText.NULL_VALUE, new NodeId(0, 10), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<XVType> {
        @Override
        public Class<XVType> getType() {
            return XVType.class;
        }

        @Override
        public XVType decodeType(SerializationContext context, UaDecoder decoder) {
            Double x = decoder.readDouble("X");
            Float value = decoder.readFloat("Value");
            return new XVType(x, value);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, XVType value) {
            encoder.writeDouble("X", value.getX());
            encoder.writeFloat("Value", value.getValue());
        }
    }
}
