package org.eclipse.milo.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.serialization.UaDecoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEncoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaRequestMessage;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class TranslateBrowsePathsToNodeIdsRequest extends Structure implements UaRequestMessage {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=552");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=554");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=553");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15193");

    private final RequestHeader requestHeader;

    private final BrowsePath[] browsePaths;

    public TranslateBrowsePathsToNodeIdsRequest(RequestHeader requestHeader,
                                                BrowsePath[] browsePaths) {
        this.requestHeader = requestHeader;
        this.browsePaths = browsePaths;
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

    public BrowsePath[] getBrowsePaths() {
        return browsePaths;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 554),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("RequestHeader", LocalizedText.NULL_VALUE, new NodeId(0, 389), -1, null, UInteger.valueOf(0), false),
                new StructureField("BrowsePaths", LocalizedText.NULL_VALUE, new NodeId(0, 543), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<TranslateBrowsePathsToNodeIdsRequest> {
        @Override
        public Class<TranslateBrowsePathsToNodeIdsRequest> getType() {
            return TranslateBrowsePathsToNodeIdsRequest.class;
        }

        @Override
        public TranslateBrowsePathsToNodeIdsRequest decodeType(SerializationContext context,
                                                               UaDecoder decoder) {
            RequestHeader requestHeader = (RequestHeader) decoder.readStruct("RequestHeader", RequestHeader.TYPE_ID);
            BrowsePath[] browsePaths = (BrowsePath[]) decoder.readStructArray("BrowsePaths", BrowsePath.TYPE_ID);
            return new TranslateBrowsePathsToNodeIdsRequest(requestHeader, browsePaths);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               TranslateBrowsePathsToNodeIdsRequest value) {
            encoder.writeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
            encoder.writeStructArray("BrowsePaths", value.getBrowsePaths(), BrowsePath.TYPE_ID);
        }
    }
}
