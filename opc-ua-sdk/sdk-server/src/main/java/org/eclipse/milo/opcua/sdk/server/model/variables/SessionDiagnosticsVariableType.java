/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionDiagnosticsVariableType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSessionId();

  /** Sets the existing node's local value. */
  void setSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getSessionName();

  /** Sets the existing node's local value. */
  void setSessionName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientDescriptionNode();

  /** Gets the existing node's local value. */
  @Nullable ApplicationDescription getClientDescription();

  /** Sets the existing node's local value. */
  void setClientDescription(@Nullable ApplicationDescription value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getServerUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getServerUri();

  /** Sets the existing node's local value. */
  void setServerUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEndpointUrlNode();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl();

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLocaleIdsNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIds();

  /** Sets the existing node's local value. */
  void setLocaleIds(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getActualSessionTimeoutNode();

  /** Gets the existing node's local value. */
  @Nullable Double getActualSessionTimeout();

  /** Sets the existing node's local value. */
  void setActualSessionTimeout(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxResponseMessageSizeNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxResponseMessageSize();

  /** Sets the existing node's local value. */
  void setMaxResponseMessageSize(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientConnectionTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getClientConnectionTime();

  /** Sets the existing node's local value. */
  void setClientConnectionTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientLastContactTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getClientLastContactTime();

  /** Sets the existing node's local value. */
  void setClientLastContactTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSubscriptionsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSubscriptionsCount();

  /** Sets the existing node's local value. */
  void setCurrentSubscriptionsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setCurrentMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentPublishRequestsInQueueNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentPublishRequestsInQueue();

  /** Sets the existing node's local value. */
  void setCurrentPublishRequestsInQueue(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTotalRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTotalRequestCount();

  /** Sets the existing node's local value. */
  void setTotalRequestCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnauthorizedRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnauthorizedRequestCount();

  /** Sets the existing node's local value. */
  void setUnauthorizedRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getReadCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getReadCount();

  /** Sets the existing node's local value. */
  void setReadCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getHistoryReadCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getHistoryReadCount();

  /** Sets the existing node's local value. */
  void setHistoryReadCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getWriteCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getWriteCount();

  /** Sets the existing node's local value. */
  void setWriteCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getHistoryUpdateCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getHistoryUpdateCount();

  /** Sets the existing node's local value. */
  void setHistoryUpdateCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCallCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCallCount();

  /** Sets the existing node's local value. */
  void setCallCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCreateMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifyMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getModifyMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetMonitoringModeCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetMonitoringModeCount();

  /** Sets the existing node's local value. */
  void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetTriggeringCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetTriggeringCount();

  /** Sets the existing node's local value. */
  void setSetTriggeringCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateSubscriptionCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCreateSubscriptionCount();

  /** Sets the existing node's local value. */
  void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifySubscriptionCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getModifySubscriptionCount();

  /** Sets the existing node's local value. */
  void setModifySubscriptionCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetPublishingModeCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetPublishingModeCount();

  /** Sets the existing node's local value. */
  void setSetPublishingModeCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getPublishCount();

  /** Sets the existing node's local value. */
  void setPublishCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getRepublishCount();

  /** Sets the existing node's local value. */
  void setRepublishCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferSubscriptionsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTransferSubscriptionsCount();

  /** Sets the existing node's local value. */
  void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteSubscriptionsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteSubscriptionsCount();

  /** Sets the existing node's local value. */
  void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAddNodesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getAddNodesCount();

  /** Sets the existing node's local value. */
  void setAddNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAddReferencesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getAddReferencesCount();

  /** Sets the existing node's local value. */
  void setAddReferencesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteNodesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteNodesCount();

  /** Sets the existing node's local value. */
  void setDeleteNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteReferencesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteReferencesCount();

  /** Sets the existing node's local value. */
  void setDeleteReferencesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBrowseCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getBrowseCount();

  /** Sets the existing node's local value. */
  void setBrowseCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBrowseNextCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getBrowseNextCount();

  /** Sets the existing node's local value. */
  void setBrowseNextCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTranslateBrowsePathsToNodeIdsCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount();

  /** Sets the existing node's local value. */
  void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getQueryFirstCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getQueryFirstCount();

  /** Sets the existing node's local value. */
  void setQueryFirstCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getQueryNextCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getQueryNextCount();

  /** Sets the existing node's local value. */
  void setQueryNextCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRegisterNodesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getRegisterNodesCount();

  /** Sets the existing node's local value. */
  void setRegisterNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnregisterNodesCountNode();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getUnregisterNodesCount();

  /** Sets the existing node's local value. */
  void setUnregisterNodesCount(@Nullable ServiceCounterDataType value);
}
