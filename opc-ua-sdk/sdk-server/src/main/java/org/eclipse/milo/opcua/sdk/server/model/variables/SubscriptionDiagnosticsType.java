package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SubscriptionDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">Model
 *     documentation</a>
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2172L);

  /**
   * Returns the mandatory CurrentKeepAliveCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentKeepAliveCountNode();

  /**
   * Returns the Value of the CurrentKeepAliveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentKeepAliveCount();

  /**
   * Sets the Value of the CurrentKeepAliveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentKeepAliveCount(@Nullable UInteger value);

  /**
   * Returns the mandatory CurrentLifetimeCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentLifetimeCountNode();

  /**
   * Returns the Value of the CurrentLifetimeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentLifetimeCount();

  /**
   * Sets the Value of the CurrentLifetimeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentLifetimeCount(@Nullable UInteger value);

  /**
   * Returns the mandatory DataChangeNotificationsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDataChangeNotificationsCountNode();

  /**
   * Returns the Value of the DataChangeNotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDataChangeNotificationsCount();

  /**
   * Sets the Value of the DataChangeNotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataChangeNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory DisableCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDisableCountNode();

  /**
   * Returns the Value of the DisableCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDisableCount();

  /**
   * Sets the Value of the DisableCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDisableCount(@Nullable UInteger value);

  /**
   * Returns the mandatory DisabledMonitoredItemCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDisabledMonitoredItemCountNode();

  /**
   * Returns the Value of the DisabledMonitoredItemCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDisabledMonitoredItemCount();

  /**
   * Sets the Value of the DisabledMonitoredItemCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDisabledMonitoredItemCount(@Nullable UInteger value);

  /**
   * Returns the mandatory DiscardedMessageCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDiscardedMessageCountNode();

  /**
   * Returns the Value of the DiscardedMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDiscardedMessageCount();

  /**
   * Sets the Value of the DiscardedMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiscardedMessageCount(@Nullable UInteger value);

  /**
   * Returns the mandatory EnableCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getEnableCountNode();

  /**
   * Returns the Value of the EnableCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getEnableCount();

  /**
   * Sets the Value of the EnableCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnableCount(@Nullable UInteger value);

  /**
   * Returns the mandatory EventNotificationsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getEventNotificationsCountNode();

  /**
   * Returns the Value of the EventNotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getEventNotificationsCount();

  /**
   * Sets the Value of the EventNotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory EventQueueOverflowCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getEventQueueOverflowCountNode();

  /**
   * Returns the Value of the EventQueueOverflowCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getEventQueueOverflowCount();

  /**
   * Sets the Value of the EventQueueOverflowCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventQueueOverflowCount(@Nullable UInteger value);

  /**
   * Returns the mandatory LatePublishRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLatePublishRequestCountNode();

  /**
   * Returns the Value of the LatePublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getLatePublishRequestCount();

  /**
   * Sets the Value of the LatePublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLatePublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxKeepAliveCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxKeepAliveCountNode();

  /**
   * Returns the Value of the MaxKeepAliveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxKeepAliveCount();

  /**
   * Sets the Value of the MaxKeepAliveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxKeepAliveCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxLifetimeCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxLifetimeCountNode();

  /**
   * Returns the Value of the MaxLifetimeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxLifetimeCount();

  /**
   * Sets the Value of the MaxLifetimeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxLifetimeCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxNotificationsPerPublish child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxNotificationsPerPublishNode();

  /**
   * Returns the Value of the MaxNotificationsPerPublish child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNotificationsPerPublish();

  /**
   * Sets the Value of the MaxNotificationsPerPublish child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNotificationsPerPublish(@Nullable UInteger value);

  /**
   * Returns the mandatory ModifyCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getModifyCountNode();

  /**
   * Returns the Value of the ModifyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getModifyCount();

  /**
   * Sets the Value of the ModifyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setModifyCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MonitoredItemCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMonitoredItemCountNode();

  /**
   * Returns the Value of the MonitoredItemCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMonitoredItemCount();

  /**
   * Sets the Value of the MonitoredItemCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMonitoredItemCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MonitoringQueueOverflowCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMonitoringQueueOverflowCountNode();

  /**
   * Returns the Value of the MonitoringQueueOverflowCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMonitoringQueueOverflowCount();

  /**
   * Sets the Value of the MonitoringQueueOverflowCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMonitoringQueueOverflowCount(@Nullable UInteger value);

  /**
   * Returns the mandatory NextSequenceNumber child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getNextSequenceNumberNode();

  /**
   * Returns the Value of the NextSequenceNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getNextSequenceNumber();

  /**
   * Sets the Value of the NextSequenceNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNextSequenceNumber(@Nullable UInteger value);

  /**
   * Returns the mandatory NotificationsCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getNotificationsCountNode();

  /**
   * Returns the Value of the NotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getNotificationsCount();

  /**
   * Sets the Value of the NotificationsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory Priority child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPriorityNode();

  /**
   * Returns the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPriority();

  /**
   * Sets the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the mandatory PublishRequestCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPublishRequestCountNode();

  /**
   * Returns the Value of the PublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getPublishRequestCount();

  /**
   * Sets the Value of the PublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory PublishingEnabled child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPublishingEnabledNode();

  /**
   * Returns the Value of the PublishingEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getPublishingEnabled();

  /**
   * Sets the Value of the PublishingEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingEnabled(@Nullable Boolean value);

  /**
   * Returns the mandatory PublishingInterval child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPublishingIntervalNode();

  /**
   * Returns the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getPublishingInterval();

  /**
   * Sets the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the mandatory RepublishMessageCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRepublishMessageCountNode();

  /**
   * Returns the Value of the RepublishMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRepublishMessageCount();

  /**
   * Sets the Value of the RepublishMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRepublishMessageCount(@Nullable UInteger value);

  /**
   * Returns the mandatory RepublishMessageRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRepublishMessageRequestCountNode();

  /**
   * Returns the Value of the RepublishMessageRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRepublishMessageRequestCount();

  /**
   * Sets the Value of the RepublishMessageRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRepublishMessageRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory RepublishRequestCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRepublishRequestCountNode();

  /**
   * Returns the Value of the RepublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRepublishRequestCount();

  /**
   * Sets the Value of the RepublishRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRepublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionIdNode();

  /**
   * Returns the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getSessionId();

  /**
   * Sets the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionId(@Nullable NodeId value);

  /**
   * Returns the mandatory SubscriptionId child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSubscriptionIdNode();

  /**
   * Returns the Value of the SubscriptionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSubscriptionId();

  /**
   * Sets the Value of the SubscriptionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSubscriptionId(@Nullable UInteger value);

  /**
   * Returns the mandatory TransferRequestCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTransferRequestCountNode();

  /**
   * Returns the Value of the TransferRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTransferRequestCount();

  /**
   * Sets the Value of the TransferRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransferRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory TransferredToAltClientCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTransferredToAltClientCountNode();

  /**
   * Returns the Value of the TransferredToAltClientCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTransferredToAltClientCount();

  /**
   * Sets the Value of the TransferredToAltClientCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransferredToAltClientCount(@Nullable UInteger value);

  /**
   * Returns the mandatory TransferredToSameClientCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTransferredToSameClientCountNode();

  /**
   * Returns the Value of the TransferredToSameClientCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTransferredToSameClientCount();

  /**
   * Sets the Value of the TransferredToSameClientCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransferredToSameClientCount(@Nullable UInteger value);

  /**
   * Returns the mandatory UnacknowledgedMessageCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getUnacknowledgedMessageCountNode();

  /**
   * Returns the Value of the UnacknowledgedMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getUnacknowledgedMessageCount();

  /**
   * Sets the Value of the UnacknowledgedMessageCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnacknowledgedMessageCount(@Nullable UInteger value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable SubscriptionDiagnosticsDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable SubscriptionDiagnosticsDataType value);
}
