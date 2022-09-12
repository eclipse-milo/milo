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
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.6.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.6.6</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@SuperBuilder
@ToString
public class BrokerDataSetReaderTransportDataType extends DataSetReaderTransportDataType implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15670");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15733");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16023");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16526");

    private final String queueName;

    private final String resourceUri;

    private final String authenticationProfileUri;

    private final BrokerTransportQualityOfService requestedDeliveryGuarantee;

    private final String metaDataQueueName;

    public BrokerDataSetReaderTransportDataType(String queueName, String resourceUri,
                                                String authenticationProfileUri, BrokerTransportQualityOfService requestedDeliveryGuarantee,
                                                String metaDataQueueName) {
        this.queueName = queueName;
        this.resourceUri = resourceUri;
        this.authenticationProfileUri = authenticationProfileUri;
        this.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
        this.metaDataQueueName = metaDataQueueName;
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

    public String getQueueName() {
        return queueName;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public String getAuthenticationProfileUri() {
        return authenticationProfileUri;
    }

    public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
        return requestedDeliveryGuarantee;
    }

    public String getMetaDataQueueName() {
        return metaDataQueueName;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 15733),
            new NodeId(0, 15628),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("QueueName", LocalizedText.NULL_VALUE, new NodeId(0, 12), -1, null, UInteger.valueOf(0), false),
                new StructureField("ResourceUri", LocalizedText.NULL_VALUE, new NodeId(0, 12), -1, null, UInteger.valueOf(0), false),
                new StructureField("AuthenticationProfileUri", LocalizedText.NULL_VALUE, new NodeId(0, 12), -1, null, UInteger.valueOf(0), false),
                new StructureField("RequestedDeliveryGuarantee", LocalizedText.NULL_VALUE, new NodeId(0, 15008), -1, null, UInteger.valueOf(0), false),
                new StructureField("MetaDataQueueName", LocalizedText.NULL_VALUE, new NodeId(0, 12), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<BrokerDataSetReaderTransportDataType> {
        @Override
        public Class<BrokerDataSetReaderTransportDataType> getType() {
            return BrokerDataSetReaderTransportDataType.class;
        }

        @Override
        public BrokerDataSetReaderTransportDataType decodeType(SerializationContext context,
                                                               UaDecoder decoder) {
            String queueName = decoder.readString("QueueName");
            String resourceUri = decoder.readString("ResourceUri");
            String authenticationProfileUri = decoder.readString("AuthenticationProfileUri");
            BrokerTransportQualityOfService requestedDeliveryGuarantee = BrokerTransportQualityOfService.from(decoder.readEnum("RequestedDeliveryGuarantee"));
            String metaDataQueueName = decoder.readString("MetaDataQueueName");
            return new BrokerDataSetReaderTransportDataType(queueName, resourceUri, authenticationProfileUri, requestedDeliveryGuarantee, metaDataQueueName);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               BrokerDataSetReaderTransportDataType value) {
            encoder.writeString("QueueName", value.getQueueName());
            encoder.writeString("ResourceUri", value.getResourceUri());
            encoder.writeString("AuthenticationProfileUri", value.getAuthenticationProfileUri());
            encoder.writeEnum("RequestedDeliveryGuarantee", value.getRequestedDeliveryGuarantee());
            encoder.writeString("MetaDataQueueName", value.getMetaDataQueueName());
        }
    }
}
