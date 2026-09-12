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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
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
  BaseDataVariableType getSubscriptionIdNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSubscriptionId();

  /** Sets the existing node's local value. */
  void setSubscriptionId(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority();

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getPublishingInterval();

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxKeepAliveCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxKeepAliveCount();

  /** Sets the existing node's local value. */
  void setMaxKeepAliveCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxLifetimeCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxLifetimeCount();

  /** Sets the existing node's local value. */
  void setMaxLifetimeCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxNotificationsPerPublishNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNotificationsPerPublish();

  /** Sets the existing node's local value. */
  void setMaxNotificationsPerPublish(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingEnabledNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getPublishingEnabled();

  /** Sets the existing node's local value. */
  void setPublishingEnabled(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getModifyCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getModifyCount();

  /** Sets the existing node's local value. */
  void setModifyCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEnableCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEnableCount();

  /** Sets the existing node's local value. */
  void setEnableCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisableCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisableCount();

  /** Sets the existing node's local value. */
  void setDisableCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishRequestCount();

  /** Sets the existing node's local value. */
  void setRepublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishMessageRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishMessageRequestCount();

  /** Sets the existing node's local value. */
  void setRepublishMessageRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRepublishMessageCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRepublishMessageCount();

  /** Sets the existing node's local value. */
  void setRepublishMessageCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferRequestCount();

  /** Sets the existing node's local value. */
  void setTransferRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferredToAltClientCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferredToAltClientCount();

  /** Sets the existing node's local value. */
  void setTransferredToAltClientCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransferredToSameClientCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTransferredToSameClientCount();

  /** Sets the existing node's local value. */
  void setTransferredToSameClientCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPublishRequestCount();

  /** Sets the existing node's local value. */
  void setPublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDataChangeNotificationsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDataChangeNotificationsCount();

  /** Sets the existing node's local value. */
  void setDataChangeNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEventNotificationsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEventNotificationsCount();

  /** Sets the existing node's local value. */
  void setEventNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getNotificationsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getNotificationsCount();

  /** Sets the existing node's local value. */
  void setNotificationsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLatePublishRequestCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getLatePublishRequestCount();

  /** Sets the existing node's local value. */
  void setLatePublishRequestCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentKeepAliveCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentKeepAliveCount();

  /** Sets the existing node's local value. */
  void setCurrentKeepAliveCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentLifetimeCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentLifetimeCount();

  /** Sets the existing node's local value. */
  void setCurrentLifetimeCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUnacknowledgedMessageCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnacknowledgedMessageCount();

  /** Sets the existing node's local value. */
  void setUnacknowledgedMessageCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDiscardedMessageCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDiscardedMessageCount();

  /** Sets the existing node's local value. */
  void setDiscardedMessageCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMonitoredItemCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMonitoredItemCount();

  /** Sets the existing node's local value. */
  void setMonitoredItemCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisabledMonitoredItemCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisabledMonitoredItemCount();

  /** Sets the existing node's local value. */
  void setDisabledMonitoredItemCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMonitoringQueueOverflowCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMonitoringQueueOverflowCount();

  /** Sets the existing node's local value. */
  void setMonitoringQueueOverflowCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getNextSequenceNumberNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getNextSequenceNumber();

  /** Sets the existing node's local value. */
  void setNextSequenceNumber(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEventQueueOverflowCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getEventQueueOverflowCount();

  /** Sets the existing node's local value. */
  void setEventQueueOverflowCount(@Nullable UInteger value);
}
