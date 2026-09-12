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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
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
  @Nullable UInteger getSubscriptionId() throws UaException;

  /** Sets the existing node's local value. */
  void setSubscriptionId(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSubscriptionId() throws UaException;

  /** Writes the value remotely. */
  void writeSubscriptionId(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSubscriptionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSubscriptionIdAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSubscriptionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSubscriptionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority() throws UaException;

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPriority() throws UaException;

  /** Writes the value remotely. */
  void writePriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPriorityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getPublishingInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readPublishingInterval() throws UaException;

  /** Writes the value remotely. */
  void writePublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxKeepAliveCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxKeepAliveCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxKeepAliveCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxKeepAliveCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxKeepAliveCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxKeepAliveCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxLifetimeCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxLifetimeCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxLifetimeCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxLifetimeCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxLifetimeCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxLifetimeCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNotificationsPerPublish() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNotificationsPerPublish(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNotificationsPerPublish() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNotificationsPerPublish(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNotificationsPerPublishAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNotificationsPerPublishAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxNotificationsPerPublishNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxNotificationsPerPublishNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getPublishingEnabled() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readPublishingEnabled() throws UaException;

  /** Writes the value remotely. */
  void writePublishingEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readPublishingEnabledAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingEnabledAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingEnabledNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingEnabledNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getModifyCount() throws UaException;

  /** Sets the existing node's local value. */
  void setModifyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readModifyCount() throws UaException;

  /** Writes the value remotely. */
  void writeModifyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readModifyCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeModifyCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifyCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifyCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEnableCount() throws UaException;

  /** Sets the existing node's local value. */
  void setEnableCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readEnableCount() throws UaException;

  /** Writes the value remotely. */
  void writeEnableCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readEnableCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnableCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEnableCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEnableCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisableCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDisableCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDisableCount() throws UaException;

  /** Writes the value remotely. */
  void writeDisableCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDisableCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDisableCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisableCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDisableCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRepublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRepublishRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeRepublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRepublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishMessageRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRepublishMessageRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRepublishMessageRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeRepublishMessageRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishMessageRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRepublishMessageRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishMessageRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishMessageRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishMessageCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRepublishMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRepublishMessageCount() throws UaException;

  /** Writes the value remotely. */
  void writeRepublishMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishMessageCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRepublishMessageCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishMessageCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishMessageCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTransferRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTransferRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeTransferRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTransferRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransferRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferredToAltClientCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTransferredToAltClientCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTransferredToAltClientCount() throws UaException;

  /** Writes the value remotely. */
  void writeTransferredToAltClientCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTransferredToAltClientCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransferredToAltClientCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferredToAltClientCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferredToAltClientCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferredToSameClientCount() throws UaException;

  /** Sets the existing node's local value. */
  void setTransferredToSameClientCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTransferredToSameClientCount() throws UaException;

  /** Writes the value remotely. */
  void writeTransferredToSameClientCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTransferredToSameClientCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransferredToSameClientCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferredToSameClientCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferredToSameClientCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPublishRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readPublishRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writePublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readPublishRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDataChangeNotificationsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDataChangeNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDataChangeNotificationsCount() throws UaException;

  /** Writes the value remotely. */
  void writeDataChangeNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDataChangeNotificationsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataChangeNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDataChangeNotificationsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDataChangeNotificationsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEventNotificationsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setEventNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readEventNotificationsCount() throws UaException;

  /** Writes the value remotely. */
  void writeEventNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readEventNotificationsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEventNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEventNotificationsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEventNotificationsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getNotificationsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readNotificationsCount() throws UaException;

  /** Writes the value remotely. */
  void writeNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readNotificationsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getNotificationsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNotificationsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getLatePublishRequestCount() throws UaException;

  /** Sets the existing node's local value. */
  void setLatePublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readLatePublishRequestCount() throws UaException;

  /** Writes the value remotely. */
  void writeLatePublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readLatePublishRequestCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLatePublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLatePublishRequestCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLatePublishRequestCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentKeepAliveCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentKeepAliveCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentKeepAliveCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentKeepAliveCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentKeepAliveCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentKeepAliveCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentLifetimeCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentLifetimeCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentLifetimeCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentLifetimeCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentLifetimeCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentLifetimeCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnacknowledgedMessageCount() throws UaException;

  /** Sets the existing node's local value. */
  void setUnacknowledgedMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readUnacknowledgedMessageCount() throws UaException;

  /** Writes the value remotely. */
  void writeUnacknowledgedMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedMessageCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnacknowledgedMessageCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnacknowledgedMessageCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnacknowledgedMessageCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDiscardedMessageCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDiscardedMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDiscardedMessageCount() throws UaException;

  /** Writes the value remotely. */
  void writeDiscardedMessageCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDiscardedMessageCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDiscardedMessageCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDiscardedMessageCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDiscardedMessageCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMonitoredItemCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMonitoredItemCount() throws UaException;

  /** Writes the value remotely. */
  void writeMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMonitoredItemCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMonitoredItemCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMonitoredItemCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMonitoredItemCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisabledMonitoredItemCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDisabledMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDisabledMonitoredItemCount() throws UaException;

  /** Writes the value remotely. */
  void writeDisabledMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDisabledMonitoredItemCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDisabledMonitoredItemCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisabledMonitoredItemCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDisabledMonitoredItemCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMonitoringQueueOverflowCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMonitoringQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMonitoringQueueOverflowCount() throws UaException;

  /** Writes the value remotely. */
  void writeMonitoringQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMonitoringQueueOverflowCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMonitoringQueueOverflowCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMonitoringQueueOverflowCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMonitoringQueueOverflowCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getNextSequenceNumber() throws UaException;

  /** Sets the existing node's local value. */
  void setNextSequenceNumber(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readNextSequenceNumber() throws UaException;

  /** Writes the value remotely. */
  void writeNextSequenceNumber(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readNextSequenceNumberAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNextSequenceNumberAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getNextSequenceNumberNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNextSequenceNumberNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEventQueueOverflowCount() throws UaException;

  /** Sets the existing node's local value. */
  void setEventQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readEventQueueOverflowCount() throws UaException;

  /** Writes the value remotely. */
  void writeEventQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readEventQueueOverflowCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEventQueueOverflowCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEventQueueOverflowCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEventQueueOverflowCountNodeAsync();
}
