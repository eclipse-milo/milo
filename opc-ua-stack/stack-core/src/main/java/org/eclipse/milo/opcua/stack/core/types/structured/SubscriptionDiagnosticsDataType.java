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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.15</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class SubscriptionDiagnosticsDataType extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=874");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=876");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=875");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15372");

    private final NodeId sessionId;

    private final UInteger subscriptionId;

    private final UByte priority;

    private final Double publishingInterval;

    private final UInteger maxKeepAliveCount;

    private final UInteger maxLifetimeCount;

    private final UInteger maxNotificationsPerPublish;

    private final Boolean publishingEnabled;

    private final UInteger modifyCount;

    private final UInteger enableCount;

    private final UInteger disableCount;

    private final UInteger republishRequestCount;

    private final UInteger republishMessageRequestCount;

    private final UInteger republishMessageCount;

    private final UInteger transferRequestCount;

    private final UInteger transferredToAltClientCount;

    private final UInteger transferredToSameClientCount;

    private final UInteger publishRequestCount;

    private final UInteger dataChangeNotificationsCount;

    private final UInteger eventNotificationsCount;

    private final UInteger notificationsCount;

    private final UInteger latePublishRequestCount;

    private final UInteger currentKeepAliveCount;

    private final UInteger currentLifetimeCount;

    private final UInteger unacknowledgedMessageCount;

    private final UInteger discardedMessageCount;

    private final UInteger monitoredItemCount;

    private final UInteger disabledMonitoredItemCount;

    private final UInteger monitoringQueueOverflowCount;

    private final UInteger nextSequenceNumber;

    private final UInteger eventQueueOverFlowCount;

    public SubscriptionDiagnosticsDataType(NodeId sessionId, UInteger subscriptionId, UByte priority,
                                           Double publishingInterval, UInteger maxKeepAliveCount, UInteger maxLifetimeCount,
                                           UInteger maxNotificationsPerPublish, Boolean publishingEnabled, UInteger modifyCount,
                                           UInteger enableCount, UInteger disableCount, UInteger republishRequestCount,
                                           UInteger republishMessageRequestCount, UInteger republishMessageCount,
                                           UInteger transferRequestCount, UInteger transferredToAltClientCount,
                                           UInteger transferredToSameClientCount, UInteger publishRequestCount,
                                           UInteger dataChangeNotificationsCount, UInteger eventNotificationsCount,
                                           UInteger notificationsCount, UInteger latePublishRequestCount, UInteger currentKeepAliveCount,
                                           UInteger currentLifetimeCount, UInteger unacknowledgedMessageCount,
                                           UInteger discardedMessageCount, UInteger monitoredItemCount,
                                           UInteger disabledMonitoredItemCount, UInteger monitoringQueueOverflowCount,
                                           UInteger nextSequenceNumber, UInteger eventQueueOverFlowCount) {
        this.sessionId = sessionId;
        this.subscriptionId = subscriptionId;
        this.priority = priority;
        this.publishingInterval = publishingInterval;
        this.maxKeepAliveCount = maxKeepAliveCount;
        this.maxLifetimeCount = maxLifetimeCount;
        this.maxNotificationsPerPublish = maxNotificationsPerPublish;
        this.publishingEnabled = publishingEnabled;
        this.modifyCount = modifyCount;
        this.enableCount = enableCount;
        this.disableCount = disableCount;
        this.republishRequestCount = republishRequestCount;
        this.republishMessageRequestCount = republishMessageRequestCount;
        this.republishMessageCount = republishMessageCount;
        this.transferRequestCount = transferRequestCount;
        this.transferredToAltClientCount = transferredToAltClientCount;
        this.transferredToSameClientCount = transferredToSameClientCount;
        this.publishRequestCount = publishRequestCount;
        this.dataChangeNotificationsCount = dataChangeNotificationsCount;
        this.eventNotificationsCount = eventNotificationsCount;
        this.notificationsCount = notificationsCount;
        this.latePublishRequestCount = latePublishRequestCount;
        this.currentKeepAliveCount = currentKeepAliveCount;
        this.currentLifetimeCount = currentLifetimeCount;
        this.unacknowledgedMessageCount = unacknowledgedMessageCount;
        this.discardedMessageCount = discardedMessageCount;
        this.monitoredItemCount = monitoredItemCount;
        this.disabledMonitoredItemCount = disabledMonitoredItemCount;
        this.monitoringQueueOverflowCount = monitoringQueueOverflowCount;
        this.nextSequenceNumber = nextSequenceNumber;
        this.eventQueueOverFlowCount = eventQueueOverFlowCount;
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

    public NodeId getSessionId() {
        return sessionId;
    }

    public UInteger getSubscriptionId() {
        return subscriptionId;
    }

    public UByte getPriority() {
        return priority;
    }

    public Double getPublishingInterval() {
        return publishingInterval;
    }

    public UInteger getMaxKeepAliveCount() {
        return maxKeepAliveCount;
    }

    public UInteger getMaxLifetimeCount() {
        return maxLifetimeCount;
    }

    public UInteger getMaxNotificationsPerPublish() {
        return maxNotificationsPerPublish;
    }

    public Boolean getPublishingEnabled() {
        return publishingEnabled;
    }

    public UInteger getModifyCount() {
        return modifyCount;
    }

    public UInteger getEnableCount() {
        return enableCount;
    }

    public UInteger getDisableCount() {
        return disableCount;
    }

    public UInteger getRepublishRequestCount() {
        return republishRequestCount;
    }

    public UInteger getRepublishMessageRequestCount() {
        return republishMessageRequestCount;
    }

    public UInteger getRepublishMessageCount() {
        return republishMessageCount;
    }

    public UInteger getTransferRequestCount() {
        return transferRequestCount;
    }

    public UInteger getTransferredToAltClientCount() {
        return transferredToAltClientCount;
    }

    public UInteger getTransferredToSameClientCount() {
        return transferredToSameClientCount;
    }

    public UInteger getPublishRequestCount() {
        return publishRequestCount;
    }

    public UInteger getDataChangeNotificationsCount() {
        return dataChangeNotificationsCount;
    }

    public UInteger getEventNotificationsCount() {
        return eventNotificationsCount;
    }

    public UInteger getNotificationsCount() {
        return notificationsCount;
    }

    public UInteger getLatePublishRequestCount() {
        return latePublishRequestCount;
    }

    public UInteger getCurrentKeepAliveCount() {
        return currentKeepAliveCount;
    }

    public UInteger getCurrentLifetimeCount() {
        return currentLifetimeCount;
    }

    public UInteger getUnacknowledgedMessageCount() {
        return unacknowledgedMessageCount;
    }

    public UInteger getDiscardedMessageCount() {
        return discardedMessageCount;
    }

    public UInteger getMonitoredItemCount() {
        return monitoredItemCount;
    }

    public UInteger getDisabledMonitoredItemCount() {
        return disabledMonitoredItemCount;
    }

    public UInteger getMonitoringQueueOverflowCount() {
        return monitoringQueueOverflowCount;
    }

    public UInteger getNextSequenceNumber() {
        return nextSequenceNumber;
    }

    public UInteger getEventQueueOverFlowCount() {
        return eventQueueOverFlowCount;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 876),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("SessionId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("SubscriptionId", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("Priority", LocalizedText.NULL_VALUE, new NodeId(0, 3), -1, null, UInteger.valueOf(0), false),
                new StructureField("PublishingInterval", LocalizedText.NULL_VALUE, new NodeId(0, 290), -1, null, UInteger.valueOf(0), false),
                new StructureField("MaxKeepAliveCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("MaxLifetimeCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("MaxNotificationsPerPublish", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("PublishingEnabled", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false),
                new StructureField("ModifyCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("EnableCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("DisableCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("RepublishRequestCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("RepublishMessageRequestCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("RepublishMessageCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("TransferRequestCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("TransferredToAltClientCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("TransferredToSameClientCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("PublishRequestCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("DataChangeNotificationsCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("EventNotificationsCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("NotificationsCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("LatePublishRequestCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("CurrentKeepAliveCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("CurrentLifetimeCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("UnacknowledgedMessageCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("DiscardedMessageCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("MonitoredItemCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("DisabledMonitoredItemCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("MonitoringQueueOverflowCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("NextSequenceNumber", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false),
                new StructureField("EventQueueOverFlowCount", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<SubscriptionDiagnosticsDataType> {
        @Override
        public Class<SubscriptionDiagnosticsDataType> getType() {
            return SubscriptionDiagnosticsDataType.class;
        }

        @Override
        public SubscriptionDiagnosticsDataType decodeType(SerializationContext context,
                                                          UaDecoder decoder) {
            NodeId sessionId = decoder.readNodeId("SessionId");
            UInteger subscriptionId = decoder.readUInt32("SubscriptionId");
            UByte priority = decoder.readByte("Priority");
            Double publishingInterval = decoder.readDouble("PublishingInterval");
            UInteger maxKeepAliveCount = decoder.readUInt32("MaxKeepAliveCount");
            UInteger maxLifetimeCount = decoder.readUInt32("MaxLifetimeCount");
            UInteger maxNotificationsPerPublish = decoder.readUInt32("MaxNotificationsPerPublish");
            Boolean publishingEnabled = decoder.readBoolean("PublishingEnabled");
            UInteger modifyCount = decoder.readUInt32("ModifyCount");
            UInteger enableCount = decoder.readUInt32("EnableCount");
            UInteger disableCount = decoder.readUInt32("DisableCount");
            UInteger republishRequestCount = decoder.readUInt32("RepublishRequestCount");
            UInteger republishMessageRequestCount = decoder.readUInt32("RepublishMessageRequestCount");
            UInteger republishMessageCount = decoder.readUInt32("RepublishMessageCount");
            UInteger transferRequestCount = decoder.readUInt32("TransferRequestCount");
            UInteger transferredToAltClientCount = decoder.readUInt32("TransferredToAltClientCount");
            UInteger transferredToSameClientCount = decoder.readUInt32("TransferredToSameClientCount");
            UInteger publishRequestCount = decoder.readUInt32("PublishRequestCount");
            UInteger dataChangeNotificationsCount = decoder.readUInt32("DataChangeNotificationsCount");
            UInteger eventNotificationsCount = decoder.readUInt32("EventNotificationsCount");
            UInteger notificationsCount = decoder.readUInt32("NotificationsCount");
            UInteger latePublishRequestCount = decoder.readUInt32("LatePublishRequestCount");
            UInteger currentKeepAliveCount = decoder.readUInt32("CurrentKeepAliveCount");
            UInteger currentLifetimeCount = decoder.readUInt32("CurrentLifetimeCount");
            UInteger unacknowledgedMessageCount = decoder.readUInt32("UnacknowledgedMessageCount");
            UInteger discardedMessageCount = decoder.readUInt32("DiscardedMessageCount");
            UInteger monitoredItemCount = decoder.readUInt32("MonitoredItemCount");
            UInteger disabledMonitoredItemCount = decoder.readUInt32("DisabledMonitoredItemCount");
            UInteger monitoringQueueOverflowCount = decoder.readUInt32("MonitoringQueueOverflowCount");
            UInteger nextSequenceNumber = decoder.readUInt32("NextSequenceNumber");
            UInteger eventQueueOverFlowCount = decoder.readUInt32("EventQueueOverFlowCount");
            return new SubscriptionDiagnosticsDataType(sessionId, subscriptionId, priority, publishingInterval, maxKeepAliveCount, maxLifetimeCount, maxNotificationsPerPublish, publishingEnabled, modifyCount, enableCount, disableCount, republishRequestCount, republishMessageRequestCount, republishMessageCount, transferRequestCount, transferredToAltClientCount, transferredToSameClientCount, publishRequestCount, dataChangeNotificationsCount, eventNotificationsCount, notificationsCount, latePublishRequestCount, currentKeepAliveCount, currentLifetimeCount, unacknowledgedMessageCount, discardedMessageCount, monitoredItemCount, disabledMonitoredItemCount, monitoringQueueOverflowCount, nextSequenceNumber, eventQueueOverFlowCount);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder,
                               SubscriptionDiagnosticsDataType value) {
            encoder.writeNodeId("SessionId", value.getSessionId());
            encoder.writeUInt32("SubscriptionId", value.getSubscriptionId());
            encoder.writeByte("Priority", value.getPriority());
            encoder.writeDouble("PublishingInterval", value.getPublishingInterval());
            encoder.writeUInt32("MaxKeepAliveCount", value.getMaxKeepAliveCount());
            encoder.writeUInt32("MaxLifetimeCount", value.getMaxLifetimeCount());
            encoder.writeUInt32("MaxNotificationsPerPublish", value.getMaxNotificationsPerPublish());
            encoder.writeBoolean("PublishingEnabled", value.getPublishingEnabled());
            encoder.writeUInt32("ModifyCount", value.getModifyCount());
            encoder.writeUInt32("EnableCount", value.getEnableCount());
            encoder.writeUInt32("DisableCount", value.getDisableCount());
            encoder.writeUInt32("RepublishRequestCount", value.getRepublishRequestCount());
            encoder.writeUInt32("RepublishMessageRequestCount", value.getRepublishMessageRequestCount());
            encoder.writeUInt32("RepublishMessageCount", value.getRepublishMessageCount());
            encoder.writeUInt32("TransferRequestCount", value.getTransferRequestCount());
            encoder.writeUInt32("TransferredToAltClientCount", value.getTransferredToAltClientCount());
            encoder.writeUInt32("TransferredToSameClientCount", value.getTransferredToSameClientCount());
            encoder.writeUInt32("PublishRequestCount", value.getPublishRequestCount());
            encoder.writeUInt32("DataChangeNotificationsCount", value.getDataChangeNotificationsCount());
            encoder.writeUInt32("EventNotificationsCount", value.getEventNotificationsCount());
            encoder.writeUInt32("NotificationsCount", value.getNotificationsCount());
            encoder.writeUInt32("LatePublishRequestCount", value.getLatePublishRequestCount());
            encoder.writeUInt32("CurrentKeepAliveCount", value.getCurrentKeepAliveCount());
            encoder.writeUInt32("CurrentLifetimeCount", value.getCurrentLifetimeCount());
            encoder.writeUInt32("UnacknowledgedMessageCount", value.getUnacknowledgedMessageCount());
            encoder.writeUInt32("DiscardedMessageCount", value.getDiscardedMessageCount());
            encoder.writeUInt32("MonitoredItemCount", value.getMonitoredItemCount());
            encoder.writeUInt32("DisabledMonitoredItemCount", value.getDisabledMonitoredItemCount());
            encoder.writeUInt32("MonitoringQueueOverflowCount", value.getMonitoringQueueOverflowCount());
            encoder.writeUInt32("NextSequenceNumber", value.getNextSequenceNumber());
            encoder.writeUInt32("EventQueueOverFlowCount", value.getEventQueueOverFlowCount());
        }
    }
}
