/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.15</a>
 */
public class SubscriptionDiagnosticsDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=874");

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

  private final UInteger eventQueueOverflowCount;

  public SubscriptionDiagnosticsDataType(
      NodeId sessionId,
      UInteger subscriptionId,
      UByte priority,
      Double publishingInterval,
      UInteger maxKeepAliveCount,
      UInteger maxLifetimeCount,
      UInteger maxNotificationsPerPublish,
      Boolean publishingEnabled,
      UInteger modifyCount,
      UInteger enableCount,
      UInteger disableCount,
      UInteger republishRequestCount,
      UInteger republishMessageRequestCount,
      UInteger republishMessageCount,
      UInteger transferRequestCount,
      UInteger transferredToAltClientCount,
      UInteger transferredToSameClientCount,
      UInteger publishRequestCount,
      UInteger dataChangeNotificationsCount,
      UInteger eventNotificationsCount,
      UInteger notificationsCount,
      UInteger latePublishRequestCount,
      UInteger currentKeepAliveCount,
      UInteger currentLifetimeCount,
      UInteger unacknowledgedMessageCount,
      UInteger discardedMessageCount,
      UInteger monitoredItemCount,
      UInteger disabledMonitoredItemCount,
      UInteger monitoringQueueOverflowCount,
      UInteger nextSequenceNumber,
      UInteger eventQueueOverflowCount) {
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
    this.eventQueueOverflowCount = eventQueueOverflowCount;
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

  public UInteger getEventQueueOverflowCount() {
    return eventQueueOverflowCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SubscriptionDiagnosticsDataType that = (SubscriptionDiagnosticsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSessionId(), that.getSessionId());
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getPriority(), that.getPriority());
    eqb.append(getPublishingInterval(), that.getPublishingInterval());
    eqb.append(getMaxKeepAliveCount(), that.getMaxKeepAliveCount());
    eqb.append(getMaxLifetimeCount(), that.getMaxLifetimeCount());
    eqb.append(getMaxNotificationsPerPublish(), that.getMaxNotificationsPerPublish());
    eqb.append(getPublishingEnabled(), that.getPublishingEnabled());
    eqb.append(getModifyCount(), that.getModifyCount());
    eqb.append(getEnableCount(), that.getEnableCount());
    eqb.append(getDisableCount(), that.getDisableCount());
    eqb.append(getRepublishRequestCount(), that.getRepublishRequestCount());
    eqb.append(getRepublishMessageRequestCount(), that.getRepublishMessageRequestCount());
    eqb.append(getRepublishMessageCount(), that.getRepublishMessageCount());
    eqb.append(getTransferRequestCount(), that.getTransferRequestCount());
    eqb.append(getTransferredToAltClientCount(), that.getTransferredToAltClientCount());
    eqb.append(getTransferredToSameClientCount(), that.getTransferredToSameClientCount());
    eqb.append(getPublishRequestCount(), that.getPublishRequestCount());
    eqb.append(getDataChangeNotificationsCount(), that.getDataChangeNotificationsCount());
    eqb.append(getEventNotificationsCount(), that.getEventNotificationsCount());
    eqb.append(getNotificationsCount(), that.getNotificationsCount());
    eqb.append(getLatePublishRequestCount(), that.getLatePublishRequestCount());
    eqb.append(getCurrentKeepAliveCount(), that.getCurrentKeepAliveCount());
    eqb.append(getCurrentLifetimeCount(), that.getCurrentLifetimeCount());
    eqb.append(getUnacknowledgedMessageCount(), that.getUnacknowledgedMessageCount());
    eqb.append(getDiscardedMessageCount(), that.getDiscardedMessageCount());
    eqb.append(getMonitoredItemCount(), that.getMonitoredItemCount());
    eqb.append(getDisabledMonitoredItemCount(), that.getDisabledMonitoredItemCount());
    eqb.append(getMonitoringQueueOverflowCount(), that.getMonitoringQueueOverflowCount());
    eqb.append(getNextSequenceNumber(), that.getNextSequenceNumber());
    eqb.append(getEventQueueOverflowCount(), that.getEventQueueOverflowCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSessionId());
    hcb.append(getSubscriptionId());
    hcb.append(getPriority());
    hcb.append(getPublishingInterval());
    hcb.append(getMaxKeepAliveCount());
    hcb.append(getMaxLifetimeCount());
    hcb.append(getMaxNotificationsPerPublish());
    hcb.append(getPublishingEnabled());
    hcb.append(getModifyCount());
    hcb.append(getEnableCount());
    hcb.append(getDisableCount());
    hcb.append(getRepublishRequestCount());
    hcb.append(getRepublishMessageRequestCount());
    hcb.append(getRepublishMessageCount());
    hcb.append(getTransferRequestCount());
    hcb.append(getTransferredToAltClientCount());
    hcb.append(getTransferredToSameClientCount());
    hcb.append(getPublishRequestCount());
    hcb.append(getDataChangeNotificationsCount());
    hcb.append(getEventNotificationsCount());
    hcb.append(getNotificationsCount());
    hcb.append(getLatePublishRequestCount());
    hcb.append(getCurrentKeepAliveCount());
    hcb.append(getCurrentLifetimeCount());
    hcb.append(getUnacknowledgedMessageCount());
    hcb.append(getDiscardedMessageCount());
    hcb.append(getMonitoredItemCount());
    hcb.append(getDisabledMonitoredItemCount());
    hcb.append(getMonitoringQueueOverflowCount());
    hcb.append(getNextSequenceNumber());
    hcb.append(getEventQueueOverflowCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SubscriptionDiagnosticsDataType.class.getSimpleName() + "[", "]");
    joiner.add("sessionId=" + getSessionId());
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add("priority=" + getPriority());
    joiner.add("publishingInterval=" + getPublishingInterval());
    joiner.add("maxKeepAliveCount=" + getMaxKeepAliveCount());
    joiner.add("maxLifetimeCount=" + getMaxLifetimeCount());
    joiner.add("maxNotificationsPerPublish=" + getMaxNotificationsPerPublish());
    joiner.add("publishingEnabled=" + getPublishingEnabled());
    joiner.add("modifyCount=" + getModifyCount());
    joiner.add("enableCount=" + getEnableCount());
    joiner.add("disableCount=" + getDisableCount());
    joiner.add("republishRequestCount=" + getRepublishRequestCount());
    joiner.add("republishMessageRequestCount=" + getRepublishMessageRequestCount());
    joiner.add("republishMessageCount=" + getRepublishMessageCount());
    joiner.add("transferRequestCount=" + getTransferRequestCount());
    joiner.add("transferredToAltClientCount=" + getTransferredToAltClientCount());
    joiner.add("transferredToSameClientCount=" + getTransferredToSameClientCount());
    joiner.add("publishRequestCount=" + getPublishRequestCount());
    joiner.add("dataChangeNotificationsCount=" + getDataChangeNotificationsCount());
    joiner.add("eventNotificationsCount=" + getEventNotificationsCount());
    joiner.add("notificationsCount=" + getNotificationsCount());
    joiner.add("latePublishRequestCount=" + getLatePublishRequestCount());
    joiner.add("currentKeepAliveCount=" + getCurrentKeepAliveCount());
    joiner.add("currentLifetimeCount=" + getCurrentLifetimeCount());
    joiner.add("unacknowledgedMessageCount=" + getUnacknowledgedMessageCount());
    joiner.add("discardedMessageCount=" + getDiscardedMessageCount());
    joiner.add("monitoredItemCount=" + getMonitoredItemCount());
    joiner.add("disabledMonitoredItemCount=" + getDisabledMonitoredItemCount());
    joiner.add("monitoringQueueOverflowCount=" + getMonitoringQueueOverflowCount());
    joiner.add("nextSequenceNumber=" + getNextSequenceNumber());
    joiner.add("eventQueueOverflowCount=" + getEventQueueOverflowCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 876),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SubscriptionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Priority",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxKeepAliveCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxLifetimeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxNotificationsPerPublish",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingEnabled",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ModifyCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EnableCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisableCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RepublishRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RepublishMessageRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RepublishMessageCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransferRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransferredToAltClientCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransferredToSameClientCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataChangeNotificationsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventNotificationsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NotificationsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LatePublishRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentKeepAliveCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentLifetimeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnacknowledgedMessageCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscardedMessageCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MonitoredItemCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisabledMonitoredItemCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MonitoringQueueOverflowCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NextSequenceNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventQueueOverflowCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SubscriptionDiagnosticsDataType> {
    @Override
    public Class<SubscriptionDiagnosticsDataType> getType() {
      return SubscriptionDiagnosticsDataType.class;
    }

    @Override
    public SubscriptionDiagnosticsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId sessionId;
      final UInteger subscriptionId;
      final UByte priority;
      final Double publishingInterval;
      final UInteger maxKeepAliveCount;
      final UInteger maxLifetimeCount;
      final UInteger maxNotificationsPerPublish;
      final Boolean publishingEnabled;
      final UInteger modifyCount;
      final UInteger enableCount;
      final UInteger disableCount;
      final UInteger republishRequestCount;
      final UInteger republishMessageRequestCount;
      final UInteger republishMessageCount;
      final UInteger transferRequestCount;
      final UInteger transferredToAltClientCount;
      final UInteger transferredToSameClientCount;
      final UInteger publishRequestCount;
      final UInteger dataChangeNotificationsCount;
      final UInteger eventNotificationsCount;
      final UInteger notificationsCount;
      final UInteger latePublishRequestCount;
      final UInteger currentKeepAliveCount;
      final UInteger currentLifetimeCount;
      final UInteger unacknowledgedMessageCount;
      final UInteger discardedMessageCount;
      final UInteger monitoredItemCount;
      final UInteger disabledMonitoredItemCount;
      final UInteger monitoringQueueOverflowCount;
      final UInteger nextSequenceNumber;
      final UInteger eventQueueOverflowCount;
      sessionId = decoder.decodeNodeId("SessionId");
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      priority = decoder.decodeByte("Priority");
      publishingInterval = decoder.decodeDouble("PublishingInterval");
      maxKeepAliveCount = decoder.decodeUInt32("MaxKeepAliveCount");
      maxLifetimeCount = decoder.decodeUInt32("MaxLifetimeCount");
      maxNotificationsPerPublish = decoder.decodeUInt32("MaxNotificationsPerPublish");
      publishingEnabled = decoder.decodeBoolean("PublishingEnabled");
      modifyCount = decoder.decodeUInt32("ModifyCount");
      enableCount = decoder.decodeUInt32("EnableCount");
      disableCount = decoder.decodeUInt32("DisableCount");
      republishRequestCount = decoder.decodeUInt32("RepublishRequestCount");
      republishMessageRequestCount = decoder.decodeUInt32("RepublishMessageRequestCount");
      republishMessageCount = decoder.decodeUInt32("RepublishMessageCount");
      transferRequestCount = decoder.decodeUInt32("TransferRequestCount");
      transferredToAltClientCount = decoder.decodeUInt32("TransferredToAltClientCount");
      transferredToSameClientCount = decoder.decodeUInt32("TransferredToSameClientCount");
      publishRequestCount = decoder.decodeUInt32("PublishRequestCount");
      dataChangeNotificationsCount = decoder.decodeUInt32("DataChangeNotificationsCount");
      eventNotificationsCount = decoder.decodeUInt32("EventNotificationsCount");
      notificationsCount = decoder.decodeUInt32("NotificationsCount");
      latePublishRequestCount = decoder.decodeUInt32("LatePublishRequestCount");
      currentKeepAliveCount = decoder.decodeUInt32("CurrentKeepAliveCount");
      currentLifetimeCount = decoder.decodeUInt32("CurrentLifetimeCount");
      unacknowledgedMessageCount = decoder.decodeUInt32("UnacknowledgedMessageCount");
      discardedMessageCount = decoder.decodeUInt32("DiscardedMessageCount");
      monitoredItemCount = decoder.decodeUInt32("MonitoredItemCount");
      disabledMonitoredItemCount = decoder.decodeUInt32("DisabledMonitoredItemCount");
      monitoringQueueOverflowCount = decoder.decodeUInt32("MonitoringQueueOverflowCount");
      nextSequenceNumber = decoder.decodeUInt32("NextSequenceNumber");
      eventQueueOverflowCount = decoder.decodeUInt32("EventQueueOverflowCount");
      return new SubscriptionDiagnosticsDataType(
          sessionId,
          subscriptionId,
          priority,
          publishingInterval,
          maxKeepAliveCount,
          maxLifetimeCount,
          maxNotificationsPerPublish,
          publishingEnabled,
          modifyCount,
          enableCount,
          disableCount,
          republishRequestCount,
          republishMessageRequestCount,
          republishMessageCount,
          transferRequestCount,
          transferredToAltClientCount,
          transferredToSameClientCount,
          publishRequestCount,
          dataChangeNotificationsCount,
          eventNotificationsCount,
          notificationsCount,
          latePublishRequestCount,
          currentKeepAliveCount,
          currentLifetimeCount,
          unacknowledgedMessageCount,
          discardedMessageCount,
          monitoredItemCount,
          disabledMonitoredItemCount,
          monitoringQueueOverflowCount,
          nextSequenceNumber,
          eventQueueOverflowCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SubscriptionDiagnosticsDataType value) {
      encoder.encodeNodeId("SessionId", value.getSessionId());
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeByte("Priority", value.getPriority());
      encoder.encodeDouble("PublishingInterval", value.getPublishingInterval());
      encoder.encodeUInt32("MaxKeepAliveCount", value.getMaxKeepAliveCount());
      encoder.encodeUInt32("MaxLifetimeCount", value.getMaxLifetimeCount());
      encoder.encodeUInt32("MaxNotificationsPerPublish", value.getMaxNotificationsPerPublish());
      encoder.encodeBoolean("PublishingEnabled", value.getPublishingEnabled());
      encoder.encodeUInt32("ModifyCount", value.getModifyCount());
      encoder.encodeUInt32("EnableCount", value.getEnableCount());
      encoder.encodeUInt32("DisableCount", value.getDisableCount());
      encoder.encodeUInt32("RepublishRequestCount", value.getRepublishRequestCount());
      encoder.encodeUInt32("RepublishMessageRequestCount", value.getRepublishMessageRequestCount());
      encoder.encodeUInt32("RepublishMessageCount", value.getRepublishMessageCount());
      encoder.encodeUInt32("TransferRequestCount", value.getTransferRequestCount());
      encoder.encodeUInt32("TransferredToAltClientCount", value.getTransferredToAltClientCount());
      encoder.encodeUInt32("TransferredToSameClientCount", value.getTransferredToSameClientCount());
      encoder.encodeUInt32("PublishRequestCount", value.getPublishRequestCount());
      encoder.encodeUInt32("DataChangeNotificationsCount", value.getDataChangeNotificationsCount());
      encoder.encodeUInt32("EventNotificationsCount", value.getEventNotificationsCount());
      encoder.encodeUInt32("NotificationsCount", value.getNotificationsCount());
      encoder.encodeUInt32("LatePublishRequestCount", value.getLatePublishRequestCount());
      encoder.encodeUInt32("CurrentKeepAliveCount", value.getCurrentKeepAliveCount());
      encoder.encodeUInt32("CurrentLifetimeCount", value.getCurrentLifetimeCount());
      encoder.encodeUInt32("UnacknowledgedMessageCount", value.getUnacknowledgedMessageCount());
      encoder.encodeUInt32("DiscardedMessageCount", value.getDiscardedMessageCount());
      encoder.encodeUInt32("MonitoredItemCount", value.getMonitoredItemCount());
      encoder.encodeUInt32("DisabledMonitoredItemCount", value.getDisabledMonitoredItemCount());
      encoder.encodeUInt32("MonitoringQueueOverflowCount", value.getMonitoringQueueOverflowCount());
      encoder.encodeUInt32("NextSequenceNumber", value.getNextSequenceNumber());
      encoder.encodeUInt32("EventQueueOverflowCount", value.getEventQueueOverflowCount());
    }
  }
}
