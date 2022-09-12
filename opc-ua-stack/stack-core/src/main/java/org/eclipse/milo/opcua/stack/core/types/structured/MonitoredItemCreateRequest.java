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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class MonitoredItemCreateRequest extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=743");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=745");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=744");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15321");

    private final ReadValueId itemToMonitor;

    private final MonitoringMode monitoringMode;

    private final MonitoringParameters requestedParameters;

    public MonitoredItemCreateRequest(ReadValueId itemToMonitor, MonitoringMode monitoringMode,
                                      MonitoringParameters requestedParameters) {
        this.itemToMonitor = itemToMonitor;
        this.monitoringMode = monitoringMode;
        this.requestedParameters = requestedParameters;
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

    public ReadValueId getItemToMonitor() {
        return itemToMonitor;
    }

    public MonitoringMode getMonitoringMode() {
        return monitoringMode;
    }

    public MonitoringParameters getRequestedParameters() {
        return requestedParameters;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 745),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("ItemToMonitor", LocalizedText.NULL_VALUE, new NodeId(0, 626), -1, null, UInteger.valueOf(0), false),
                new StructureField("MonitoringMode", LocalizedText.NULL_VALUE, new NodeId(0, 716), -1, null, UInteger.valueOf(0), false),
                new StructureField("RequestedParameters", LocalizedText.NULL_VALUE, new NodeId(0, 740), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<MonitoredItemCreateRequest> {
        @Override
        public Class<MonitoredItemCreateRequest> getType() {
            return MonitoredItemCreateRequest.class;
        }

        @Override
        public MonitoredItemCreateRequest decodeType(SerializationContext context, UaDecoder decoder) {
            ReadValueId itemToMonitor = (ReadValueId) decoder.readStruct("ItemToMonitor", ReadValueId.TYPE_ID);
            MonitoringMode monitoringMode = MonitoringMode.from(decoder.readEnum("MonitoringMode"));
            MonitoringParameters requestedParameters = (MonitoringParameters) decoder.readStruct("RequestedParameters", MonitoringParameters.TYPE_ID);
            return new MonitoredItemCreateRequest(itemToMonitor, monitoringMode, requestedParameters);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               MonitoredItemCreateRequest value) {
            encoder.writeStruct("ItemToMonitor", value.getItemToMonitor(), ReadValueId.TYPE_ID);
            encoder.writeEnum("MonitoringMode", value.getMonitoringMode());
            encoder.writeStruct("RequestedParameters", value.getRequestedParameters(), MonitoringParameters.TYPE_ID);
        }
    }
}
