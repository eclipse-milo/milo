/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12</a>
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
  BaseDataVariableType getSessionIdNode();

  NodeId getSessionId();

  void setSessionId(NodeId value);

  BaseDataVariableType getSubscriptionIdNode();

  UInteger getSubscriptionId();

  void setSubscriptionId(UInteger value);

  BaseDataVariableType getPriorityNode();

  UByte getPriority();

  void setPriority(UByte value);

  BaseDataVariableType getPublishingIntervalNode();

  Double getPublishingInterval();

  void setPublishingInterval(Double value);

  BaseDataVariableType getMaxKeepAliveCountNode();

  UInteger getMaxKeepAliveCount();

  void setMaxKeepAliveCount(UInteger value);

  BaseDataVariableType getMaxLifetimeCountNode();

  UInteger getMaxLifetimeCount();

  void setMaxLifetimeCount(UInteger value);

  BaseDataVariableType getMaxNotificationsPerPublishNode();

  UInteger getMaxNotificationsPerPublish();

  void setMaxNotificationsPerPublish(UInteger value);

  BaseDataVariableType getPublishingEnabledNode();

  Boolean getPublishingEnabled();

  void setPublishingEnabled(Boolean value);

  BaseDataVariableType getModifyCountNode();

  UInteger getModifyCount();

  void setModifyCount(UInteger value);

  BaseDataVariableType getEnableCountNode();

  UInteger getEnableCount();

  void setEnableCount(UInteger value);

  BaseDataVariableType getDisableCountNode();

  UInteger getDisableCount();

  void setDisableCount(UInteger value);

  BaseDataVariableType getRepublishRequestCountNode();

  UInteger getRepublishRequestCount();

  void setRepublishRequestCount(UInteger value);

  BaseDataVariableType getRepublishMessageRequestCountNode();

  UInteger getRepublishMessageRequestCount();

  void setRepublishMessageRequestCount(UInteger value);

  BaseDataVariableType getRepublishMessageCountNode();

  UInteger getRepublishMessageCount();

  void setRepublishMessageCount(UInteger value);

  BaseDataVariableType getTransferRequestCountNode();

  UInteger getTransferRequestCount();

  void setTransferRequestCount(UInteger value);

  BaseDataVariableType getTransferredToAltClientCountNode();

  UInteger getTransferredToAltClientCount();

  void setTransferredToAltClientCount(UInteger value);

  BaseDataVariableType getTransferredToSameClientCountNode();

  UInteger getTransferredToSameClientCount();

  void setTransferredToSameClientCount(UInteger value);

  BaseDataVariableType getPublishRequestCountNode();

  UInteger getPublishRequestCount();

  void setPublishRequestCount(UInteger value);

  BaseDataVariableType getDataChangeNotificationsCountNode();

  UInteger getDataChangeNotificationsCount();

  void setDataChangeNotificationsCount(UInteger value);

  BaseDataVariableType getEventNotificationsCountNode();

  UInteger getEventNotificationsCount();

  void setEventNotificationsCount(UInteger value);

  BaseDataVariableType getNotificationsCountNode();

  UInteger getNotificationsCount();

  void setNotificationsCount(UInteger value);

  BaseDataVariableType getLatePublishRequestCountNode();

  UInteger getLatePublishRequestCount();

  void setLatePublishRequestCount(UInteger value);

  BaseDataVariableType getCurrentKeepAliveCountNode();

  UInteger getCurrentKeepAliveCount();

  void setCurrentKeepAliveCount(UInteger value);

  BaseDataVariableType getCurrentLifetimeCountNode();

  UInteger getCurrentLifetimeCount();

  void setCurrentLifetimeCount(UInteger value);

  BaseDataVariableType getUnacknowledgedMessageCountNode();

  UInteger getUnacknowledgedMessageCount();

  void setUnacknowledgedMessageCount(UInteger value);

  BaseDataVariableType getDiscardedMessageCountNode();

  UInteger getDiscardedMessageCount();

  void setDiscardedMessageCount(UInteger value);

  BaseDataVariableType getMonitoredItemCountNode();

  UInteger getMonitoredItemCount();

  void setMonitoredItemCount(UInteger value);

  BaseDataVariableType getDisabledMonitoredItemCountNode();

  UInteger getDisabledMonitoredItemCount();

  void setDisabledMonitoredItemCount(UInteger value);

  BaseDataVariableType getMonitoringQueueOverflowCountNode();

  UInteger getMonitoringQueueOverflowCount();

  void setMonitoringQueueOverflowCount(UInteger value);

  BaseDataVariableType getNextSequenceNumberNode();

  UInteger getNextSequenceNumber();

  void setNextSequenceNumber(UInteger value);

  BaseDataVariableType getEventQueueOverflowCountNode();

  UInteger getEventQueueOverflowCount();

  void setEventQueueOverflowCount(UInteger value);
}
