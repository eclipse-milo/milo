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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class EventFieldList extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=917");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=919");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=918");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15348");

    private final UInteger clientHandle;

    private final Variant[] eventFields;

    public EventFieldList(UInteger clientHandle, Variant[] eventFields) {
        this.clientHandle = clientHandle;
        this.eventFields = eventFields;
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

    public UInteger getClientHandle() {
        return clientHandle;
    }

    public Variant[] getEventFields() {
        return eventFields;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 919),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("ClientHandle", LocalizedText.NULL_VALUE, new NodeId(0, 288), -1, null, UInteger.valueOf(0), false),
                new StructureField("EventFields", LocalizedText.NULL_VALUE, new NodeId(0, 24), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<EventFieldList> {
        @Override
        public Class<EventFieldList> getType() {
            return EventFieldList.class;
        }

        @Override
        public EventFieldList decodeType(SerializationContext context, UaDecoder decoder) {
            UInteger clientHandle = decoder.readUInt32("ClientHandle");
            Variant[] eventFields = decoder.readVariantArray("EventFields");
            return new EventFieldList(clientHandle, eventFields);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, EventFieldList value) {
            encoder.writeUInt32("ClientHandle", value.getClientHandle());
            encoder.writeVariantArray("EventFields", value.getEventFields());
        }
    }
}
