package org.eclipse.milo.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.serialization.UaDecoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEncoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.4/#5.6.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.4/#5.6.4.2</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class CloseSessionRequest extends Structure implements UaRequestMessageType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=471");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=473");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=472");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15147");

    private final RequestHeader requestHeader;

    private final Boolean deleteSubscriptions;

    public CloseSessionRequest(RequestHeader requestHeader, Boolean deleteSubscriptions) {
        this.requestHeader = requestHeader;
        this.deleteSubscriptions = deleteSubscriptions;
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

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public Boolean getDeleteSubscriptions() {
        return deleteSubscriptions;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 473),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("RequestHeader", LocalizedText.NULL_VALUE, new NodeId(0, 389), -1, null, UInteger.valueOf(0), false),
                new StructureField("DeleteSubscriptions", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<CloseSessionRequest> {
        @Override
        public Class<CloseSessionRequest> getType() {
            return CloseSessionRequest.class;
        }

        @Override
        public CloseSessionRequest decodeType(SerializationContext context, UaDecoder decoder) {
            RequestHeader requestHeader = (RequestHeader) decoder.readStruct("RequestHeader", RequestHeader.TYPE_ID);
            Boolean deleteSubscriptions = decoder.readBoolean("DeleteSubscriptions");
            return new CloseSessionRequest(requestHeader, deleteSubscriptions);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               CloseSessionRequest value) {
            encoder.writeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
            encoder.writeBoolean("DeleteSubscriptions", value.getDeleteSubscriptions());
        }
    }
}
