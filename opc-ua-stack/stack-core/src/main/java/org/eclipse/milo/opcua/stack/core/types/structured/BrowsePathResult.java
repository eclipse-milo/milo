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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
public class BrowsePathResult extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=549");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=551");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=550");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15192");

    private final StatusCode statusCode;

    private final BrowsePathTarget[] targets;

    public BrowsePathResult(StatusCode statusCode, BrowsePathTarget[] targets) {
        this.statusCode = statusCode;
        this.targets = targets;
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

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public BrowsePathTarget[] getTargets() {
        return targets;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 551),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("StatusCode", LocalizedText.NULL_VALUE, new NodeId(0, 19), -1, null, UInteger.valueOf(0), false),
                new StructureField("Targets", LocalizedText.NULL_VALUE, new NodeId(0, 546), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<BrowsePathResult> {
        @Override
        public Class<BrowsePathResult> getType() {
            return BrowsePathResult.class;
        }

        @Override
        public BrowsePathResult decodeType(SerializationContext context, UaDecoder decoder) {
            StatusCode statusCode = decoder.readStatusCode("StatusCode");
            BrowsePathTarget[] targets = (BrowsePathTarget[]) decoder.readStructArray("Targets", BrowsePathTarget.TYPE_ID);
            return new BrowsePathResult(statusCode, targets);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               BrowsePathResult value) {
            encoder.writeStatusCode("StatusCode", value.getStatusCode());
            encoder.writeStructArray("Targets", value.getTargets(), BrowsePathTarget.TYPE_ID);
        }
    }
}
