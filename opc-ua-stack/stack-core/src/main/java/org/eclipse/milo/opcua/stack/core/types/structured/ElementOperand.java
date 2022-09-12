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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.2</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class ElementOperand extends FilterOperand implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=592");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=594");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=593");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15207");

    private final UInteger index;

    public ElementOperand(UInteger index) {
        this.index = index;
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

    public UInteger getIndex() {
        return index;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 594),
            new NodeId(0, 589),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("Index", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<ElementOperand> {
        @Override
        public Class<ElementOperand> getType() {
            return ElementOperand.class;
        }

        @Override
        public ElementOperand decodeType(SerializationContext context, UaDecoder decoder) {
            UInteger index = decoder.readUInt32("Index");
            return new ElementOperand(index);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, ElementOperand value) {
            encoder.writeUInt32("Index", value.getIndex());
        }
    }
}
