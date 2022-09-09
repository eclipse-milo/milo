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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.26">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.26</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class NotificationMessage extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=803");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=805");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=804");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15343");

    private final UInteger sequenceNumber;

    private final DateTime publishTime;

    private final ExtensionObject[] notificationData;

    public NotificationMessage(UInteger sequenceNumber, DateTime publishTime,
                               ExtensionObject[] notificationData) {
        this.sequenceNumber = sequenceNumber;
        this.publishTime = publishTime;
        this.notificationData = notificationData;
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

    public UInteger getSequenceNumber() {
        return sequenceNumber;
    }

    public DateTime getPublishTime() {
        return publishTime;
    }

    public ExtensionObject[] getNotificationData() {
        return notificationData;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 805),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("SequenceNumber", LocalizedText.NULL_VALUE, new NodeId(0, 289), -1, null, UInteger.valueOf(0), false),
                new StructureField("PublishTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false),
                new StructureField("NotificationData", LocalizedText.NULL_VALUE, new NodeId(0, 22), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<NotificationMessage> {
        @Override
        public Class<NotificationMessage> getType() {
            return NotificationMessage.class;
        }

        @Override
        public NotificationMessage decodeType(SerializationContext context, UaDecoder decoder) {
            UInteger sequenceNumber = decoder.readUInt32("SequenceNumber");
            DateTime publishTime = decoder.readDateTime("PublishTime");
            ExtensionObject[] notificationData = decoder.readExtensionObjectArray("NotificationData");
            return new NotificationMessage(sequenceNumber, publishTime, notificationData);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               NotificationMessage value) {
            encoder.writeUInt32("SequenceNumber", value.getSequenceNumber());
            encoder.writeDateTime("PublishTime", value.getPublishTime());
            encoder.writeExtensionObjectArray("NotificationData", value.getNotificationData());
        }
    }
}
