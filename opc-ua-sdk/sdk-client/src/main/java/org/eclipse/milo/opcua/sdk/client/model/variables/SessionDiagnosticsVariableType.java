/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  /** Gets the existing node's local value. */
  @Nullable NodeId getSessionId() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readSessionId() throws UaException;

  /** Writes the value remotely. */
  void writeSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSessionName() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSessionName() throws UaException;

  /** Writes the value remotely. */
  void writeSessionName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSessionNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ApplicationDescription getClientDescription() throws UaException;

  /** Sets the existing node's local value. */
  void setClientDescription(@Nullable ApplicationDescription value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ApplicationDescription readClientDescription() throws UaException;

  /** Writes the value remotely. */
  void writeClientDescription(@Nullable ApplicationDescription value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ApplicationDescription> readClientDescriptionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientDescriptionAsync(@Nullable ApplicationDescription value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientDescriptionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientDescriptionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getServerUri() throws UaException;

  /** Sets the existing node's local value. */
  void setServerUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readServerUri() throws UaException;

  /** Writes the value remotely. */
  void writeServerUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readServerUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getServerUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getServerUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readEndpointUrl() throws UaException;

  /** Writes the value remotely. */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEndpointUrlNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEndpointUrlNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIds() throws UaException;

  /** Sets the existing node's local value. */
  void setLocaleIds(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readLocaleIds() throws UaException;

  /** Writes the value remotely. */
  void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLocaleIdsAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLocaleIdsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLocaleIdsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getActualSessionTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setActualSessionTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readActualSessionTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeActualSessionTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readActualSessionTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActualSessionTimeoutAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getActualSessionTimeoutNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getActualSessionTimeoutNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxResponseMessageSize() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxResponseMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxResponseMessageSize() throws UaException;

  /** Writes the value remotely. */
  void writeMaxResponseMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxResponseMessageSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxResponseMessageSizeAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxResponseMessageSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxResponseMessageSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getClientConnectionTime() throws UaException;

  /** Sets the existing node's local value. */
  void setClientConnectionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readClientConnectionTime() throws UaException;

  /** Writes the value remotely. */
  void writeClientConnectionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readClientConnectionTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientConnectionTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientConnectionTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientConnectionTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getClientLastContactTime() throws UaException;

  /** Sets the existing node's local value. */
  void setClientLastContactTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readClientLastContactTime() throws UaException;

  /** Writes the value remotely. */
  void writeClientLastContactTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readClientLastContactTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientLastContactTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientLastContactTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientLastContactTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSubscriptionsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentSubscriptionsCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentSubscriptionsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSubscriptionsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSubscriptionsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentPublishRequestsInQueue() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentPublishRequestsInQueue() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentPublishRequestsInQueueAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentPublishRequestsInQueueAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentPublishRequestsInQueueNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentPublishRequestsInQueueNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTotalRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readTotalRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readTotalRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTotalRequestCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTotalRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTotalRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnauthorizedRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setUnauthorizedRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readUnauthorizedRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeUnauthorizedRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readUnauthorizedRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnauthorizedRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnauthorizedRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnauthorizedRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getReadCount() throws UaException;

  /** Sets the existing node's local value. */
  void setReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readReadCount() throws UaException;

  /** Writes the value remotely. */
  void writeReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readReadCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReadCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getReadCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getReadCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getHistoryReadCount() throws UaException;

  /** Sets the existing node's local value. */
  void setHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readHistoryReadCount() throws UaException;

  /** Writes the value remotely. */
  void writeHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryReadCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHistoryReadCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getHistoryReadCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getHistoryReadCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getWriteCount() throws UaException;

  /** Sets the existing node's local value. */
  void setWriteCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readWriteCount() throws UaException;

  /** Writes the value remotely. */
  void writeWriteCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readWriteCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeWriteCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getWriteCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getWriteCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getHistoryUpdateCount() throws UaException;

  /** Sets the existing node's local value. */
  void setHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readHistoryUpdateCount() throws UaException;

  /** Writes the value remotely. */
  void writeHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryUpdateCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHistoryUpdateCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getHistoryUpdateCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getHistoryUpdateCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCallCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCallCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readCallCount() throws UaException;

  /** Writes the value remotely. */
  void writeCallCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readCallCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCallCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCallCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCallCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCreateMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readCreateMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCreateMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getModifyMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readModifyMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifyMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeModifyMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifyMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifyMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetMonitoringModeCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readSetMonitoringModeCount() throws UaException;

  /** Writes the value remotely. */
  void writeSetMonitoringModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetMonitoringModeCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSetMonitoringModeCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetMonitoringModeCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetMonitoringModeCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetTriggeringCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readSetTriggeringCount() throws UaException;

  /** Writes the value remotely. */
  void writeSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetTriggeringCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSetTriggeringCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetTriggeringCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetTriggeringCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readDeleteMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getCreateSubscriptionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readCreateSubscriptionCount() throws UaException;

  /** Writes the value remotely. */
  void writeCreateSubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readCreateSubscriptionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCreateSubscriptionCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateSubscriptionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateSubscriptionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getModifySubscriptionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setModifySubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readModifySubscriptionCount() throws UaException;

  /** Writes the value remotely. */
  void writeModifySubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readModifySubscriptionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeModifySubscriptionCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifySubscriptionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifySubscriptionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getSetPublishingModeCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSetPublishingModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readSetPublishingModeCount() throws UaException;

  /** Writes the value remotely. */
  void writeSetPublishingModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetPublishingModeCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSetPublishingModeCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSetPublishingModeCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetPublishingModeCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getPublishCount() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readPublishCount() throws UaException;

  /** Writes the value remotely. */
  void writePublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readPublishCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getRepublishCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRepublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readRepublishCount() throws UaException;

  /** Writes the value remotely. */
  void writeRepublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readRepublishCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRepublishCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTransferSubscriptionsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readTransferSubscriptionsCount() throws UaException;

  /** Writes the value remotely. */
  void writeTransferSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTransferSubscriptionsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransferSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferSubscriptionsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferSubscriptionsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteSubscriptionsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readDeleteSubscriptionsCount() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteSubscriptionsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteSubscriptionsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteSubscriptionsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getAddNodesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readAddNodesCount() throws UaException;

  /** Writes the value remotely. */
  void writeAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddNodesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAddNodesCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAddNodesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAddNodesCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getAddReferencesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readAddReferencesCount() throws UaException;

  /** Writes the value remotely. */
  void writeAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddReferencesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAddReferencesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAddReferencesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAddReferencesCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteNodesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readDeleteNodesCount() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteNodesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteNodesCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteNodesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteNodesCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getDeleteReferencesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readDeleteReferencesCount() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteReferencesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteReferencesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDeleteReferencesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteReferencesCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getBrowseCount() throws UaException;

  /** Sets the existing node's local value. */
  void setBrowseCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readBrowseCount() throws UaException;

  /** Writes the value remotely. */
  void writeBrowseCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBrowseCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBrowseCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBrowseCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getBrowseNextCount() throws UaException;

  /** Sets the existing node's local value. */
  void setBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readBrowseNextCount() throws UaException;

  /** Writes the value remotely. */
  void writeBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseNextCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBrowseNextCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBrowseNextCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBrowseNextCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readTranslateBrowsePathsToNodeIdsCount() throws UaException;

  /** Writes the value remotely. */
  void writeTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTranslateBrowsePathsToNodeIdsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTranslateBrowsePathsToNodeIdsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTranslateBrowsePathsToNodeIdsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType>
      getTranslateBrowsePathsToNodeIdsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getQueryFirstCount() throws UaException;

  /** Sets the existing node's local value. */
  void setQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readQueryFirstCount() throws UaException;

  /** Writes the value remotely. */
  void writeQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryFirstCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeQueryFirstCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getQueryFirstCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getQueryFirstCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getQueryNextCount() throws UaException;

  /** Sets the existing node's local value. */
  void setQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readQueryNextCount() throws UaException;

  /** Writes the value remotely. */
  void writeQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryNextCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeQueryNextCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getQueryNextCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getQueryNextCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getRegisterNodesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readRegisterNodesCount() throws UaException;

  /** Writes the value remotely. */
  void writeRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readRegisterNodesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRegisterNodesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRegisterNodesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRegisterNodesCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServiceCounterDataType getUnregisterNodesCount() throws UaException;

  /** Sets the existing node's local value. */
  void setUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServiceCounterDataType readUnregisterNodesCount() throws UaException;

  /** Writes the value remotely. */
  void writeUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readUnregisterNodesCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnregisterNodesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnregisterNodesCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnregisterNodesCountNodeAsync();
}
