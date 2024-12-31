/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14</a>
 */
public interface SessionDiagnosticsVariableType extends BaseDataVariableType {
  BaseDataVariableType getSessionIdNode();

  NodeId getSessionId();

  void setSessionId(NodeId value);

  BaseDataVariableType getSessionNameNode();

  String getSessionName();

  void setSessionName(String value);

  BaseDataVariableType getClientDescriptionNode();

  ApplicationDescription getClientDescription();

  void setClientDescription(ApplicationDescription value);

  BaseDataVariableType getServerUriNode();

  String getServerUri();

  void setServerUri(String value);

  BaseDataVariableType getEndpointUrlNode();

  String getEndpointUrl();

  void setEndpointUrl(String value);

  BaseDataVariableType getLocaleIdsNode();

  String[] getLocaleIds();

  void setLocaleIds(String[] value);

  BaseDataVariableType getActualSessionTimeoutNode();

  Double getActualSessionTimeout();

  void setActualSessionTimeout(Double value);

  BaseDataVariableType getMaxResponseMessageSizeNode();

  UInteger getMaxResponseMessageSize();

  void setMaxResponseMessageSize(UInteger value);

  BaseDataVariableType getClientConnectionTimeNode();

  DateTime getClientConnectionTime();

  void setClientConnectionTime(DateTime value);

  BaseDataVariableType getClientLastContactTimeNode();

  DateTime getClientLastContactTime();

  void setClientLastContactTime(DateTime value);

  BaseDataVariableType getCurrentSubscriptionsCountNode();

  UInteger getCurrentSubscriptionsCount();

  void setCurrentSubscriptionsCount(UInteger value);

  BaseDataVariableType getCurrentMonitoredItemsCountNode();

  UInteger getCurrentMonitoredItemsCount();

  void setCurrentMonitoredItemsCount(UInteger value);

  BaseDataVariableType getCurrentPublishRequestsInQueueNode();

  UInteger getCurrentPublishRequestsInQueue();

  void setCurrentPublishRequestsInQueue(UInteger value);

  BaseDataVariableType getTotalRequestCountNode();

  ServiceCounterDataType getTotalRequestCount();

  void setTotalRequestCount(ServiceCounterDataType value);

  BaseDataVariableType getUnauthorizedRequestCountNode();

  UInteger getUnauthorizedRequestCount();

  void setUnauthorizedRequestCount(UInteger value);

  BaseDataVariableType getReadCountNode();

  ServiceCounterDataType getReadCount();

  void setReadCount(ServiceCounterDataType value);

  BaseDataVariableType getHistoryReadCountNode();

  ServiceCounterDataType getHistoryReadCount();

  void setHistoryReadCount(ServiceCounterDataType value);

  BaseDataVariableType getWriteCountNode();

  ServiceCounterDataType getWriteCount();

  void setWriteCount(ServiceCounterDataType value);

  BaseDataVariableType getHistoryUpdateCountNode();

  ServiceCounterDataType getHistoryUpdateCount();

  void setHistoryUpdateCount(ServiceCounterDataType value);

  BaseDataVariableType getCallCountNode();

  ServiceCounterDataType getCallCount();

  void setCallCount(ServiceCounterDataType value);

  BaseDataVariableType getCreateMonitoredItemsCountNode();

  ServiceCounterDataType getCreateMonitoredItemsCount();

  void setCreateMonitoredItemsCount(ServiceCounterDataType value);

  BaseDataVariableType getModifyMonitoredItemsCountNode();

  ServiceCounterDataType getModifyMonitoredItemsCount();

  void setModifyMonitoredItemsCount(ServiceCounterDataType value);

  BaseDataVariableType getSetMonitoringModeCountNode();

  ServiceCounterDataType getSetMonitoringModeCount();

  void setSetMonitoringModeCount(ServiceCounterDataType value);

  BaseDataVariableType getSetTriggeringCountNode();

  ServiceCounterDataType getSetTriggeringCount();

  void setSetTriggeringCount(ServiceCounterDataType value);

  BaseDataVariableType getDeleteMonitoredItemsCountNode();

  ServiceCounterDataType getDeleteMonitoredItemsCount();

  void setDeleteMonitoredItemsCount(ServiceCounterDataType value);

  BaseDataVariableType getCreateSubscriptionCountNode();

  ServiceCounterDataType getCreateSubscriptionCount();

  void setCreateSubscriptionCount(ServiceCounterDataType value);

  BaseDataVariableType getModifySubscriptionCountNode();

  ServiceCounterDataType getModifySubscriptionCount();

  void setModifySubscriptionCount(ServiceCounterDataType value);

  BaseDataVariableType getSetPublishingModeCountNode();

  ServiceCounterDataType getSetPublishingModeCount();

  void setSetPublishingModeCount(ServiceCounterDataType value);

  BaseDataVariableType getPublishCountNode();

  ServiceCounterDataType getPublishCount();

  void setPublishCount(ServiceCounterDataType value);

  BaseDataVariableType getRepublishCountNode();

  ServiceCounterDataType getRepublishCount();

  void setRepublishCount(ServiceCounterDataType value);

  BaseDataVariableType getTransferSubscriptionsCountNode();

  ServiceCounterDataType getTransferSubscriptionsCount();

  void setTransferSubscriptionsCount(ServiceCounterDataType value);

  BaseDataVariableType getDeleteSubscriptionsCountNode();

  ServiceCounterDataType getDeleteSubscriptionsCount();

  void setDeleteSubscriptionsCount(ServiceCounterDataType value);

  BaseDataVariableType getAddNodesCountNode();

  ServiceCounterDataType getAddNodesCount();

  void setAddNodesCount(ServiceCounterDataType value);

  BaseDataVariableType getAddReferencesCountNode();

  ServiceCounterDataType getAddReferencesCount();

  void setAddReferencesCount(ServiceCounterDataType value);

  BaseDataVariableType getDeleteNodesCountNode();

  ServiceCounterDataType getDeleteNodesCount();

  void setDeleteNodesCount(ServiceCounterDataType value);

  BaseDataVariableType getDeleteReferencesCountNode();

  ServiceCounterDataType getDeleteReferencesCount();

  void setDeleteReferencesCount(ServiceCounterDataType value);

  BaseDataVariableType getBrowseCountNode();

  ServiceCounterDataType getBrowseCount();

  void setBrowseCount(ServiceCounterDataType value);

  BaseDataVariableType getBrowseNextCountNode();

  ServiceCounterDataType getBrowseNextCount();

  void setBrowseNextCount(ServiceCounterDataType value);

  BaseDataVariableType getTranslateBrowsePathsToNodeIdsCountNode();

  ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount();

  void setTranslateBrowsePathsToNodeIdsCount(ServiceCounterDataType value);

  BaseDataVariableType getQueryFirstCountNode();

  ServiceCounterDataType getQueryFirstCount();

  void setQueryFirstCount(ServiceCounterDataType value);

  BaseDataVariableType getQueryNextCountNode();

  ServiceCounterDataType getQueryNextCount();

  void setQueryNextCount(ServiceCounterDataType value);

  BaseDataVariableType getRegisterNodesCountNode();

  ServiceCounterDataType getRegisterNodesCount();

  void setRegisterNodesCount(ServiceCounterDataType value);

  BaseDataVariableType getUnregisterNodesCountNode();

  ServiceCounterDataType getUnregisterNodesCount();

  void setUnregisterNodesCount(ServiceCounterDataType value);
}
