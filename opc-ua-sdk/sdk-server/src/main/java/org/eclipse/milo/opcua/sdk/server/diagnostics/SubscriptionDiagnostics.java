/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class SubscriptionDiagnostics {

  private final LongAdder modifyCount = new LongAdder();
  private final LongAdder enableCount = new LongAdder();
  private final LongAdder disableCount = new LongAdder();
  private final LongAdder republishRequestCount = new LongAdder();
  private final LongAdder republishMessageRequestCount = new LongAdder();
  private final LongAdder republishMessageCount = new LongAdder();
  private final LongAdder transferRequestCount = new LongAdder();
  private final LongAdder transferredToAltClientCount = new LongAdder();
  private final LongAdder transferredToSameClientCount = new LongAdder();
  private final LongAdder publishRequestCount = new LongAdder();
  private final LongAdder dataChangeNotificationsCount = new LongAdder();
  private final LongAdder eventNotificationsCount = new LongAdder();
  private final LongAdder notificationsCount = new LongAdder();
  private final LongAdder latePublishRequestCount = new LongAdder();
  private final LongAdder discardedMessageCount = new LongAdder();
  private final LongAdder monitoringQueueOverflowCount = new LongAdder();
  private final LongAdder eventQueueOverflowCount = new LongAdder();

  private final Subscription subscription;

  public SubscriptionDiagnostics(Subscription subscription) {
    this.subscription = subscription;
  }

  public NodeId getSessionId() {
    return subscription.getSession().getSessionId();
  }

  public UInteger getSubscriptionId() {
    return subscription.getId();
  }

  public UByte getPriority() {
    return ubyte(subscription.getPriority());
  }

  public double getPublishingInterval() {
    return subscription.getPublishingInterval();
  }

  public UInteger getMaxKeepAliveCount() {
    return uint(subscription.getMaxKeepAliveCount());
  }

  public UInteger getMaxLifetimeCount() {
    return uint(subscription.getLifetimeCount());
  }

  public UInteger getMaxNotificationsPerPublish() {
    return uint(subscription.getMaxNotificationsPerPublish());
  }

  public boolean isPublishingEnabled() {
    return subscription.isPublishingEnabled();
  }

  public LongAdder getModifyCount() {
    return modifyCount;
  }

  public LongAdder getEnableCount() {
    return enableCount;
  }

  public LongAdder getDisableCount() {
    return disableCount;
  }

  public LongAdder getRepublishRequestCount() {
    return republishRequestCount;
  }

  public LongAdder getRepublishMessageRequestCount() {
    return republishMessageRequestCount;
  }

  public LongAdder getRepublishMessageCount() {
    return republishMessageCount;
  }

  public LongAdder getTransferRequestCount() {
    return transferRequestCount;
  }

  public LongAdder getTransferredToAltClientCount() {
    return transferredToAltClientCount;
  }

  public LongAdder getTransferredToSameClientCount() {
    return transferredToSameClientCount;
  }

  public LongAdder getPublishRequestCount() {
    return publishRequestCount;
  }

  public LongAdder getDataChangeNotificationsCount() {
    return dataChangeNotificationsCount;
  }

  public LongAdder getEventNotificationsCount() {
    return eventNotificationsCount;
  }

  public LongAdder getNotificationsCount() {
    return notificationsCount;
  }

  public LongAdder getLatePublishRequestCount() {
    return latePublishRequestCount;
  }

  public UInteger getCurrentKeepAliveCount() {
    return uint(subscription.getKeepAliveCounter());
  }

  public UInteger getCurrentLifetimeCount() {
    return uint(subscription.getLifetimeCounter());
  }

  public UInteger getUnacknowledgedMessageCount() {
    return subscription.getUnacknowledgeMessageCount();
  }

  public LongAdder getDiscardedMessageCount() {
    return discardedMessageCount;
  }

  public UInteger getMonitoredItemCount() {
    return subscription.getMonitoredItemCount();
  }

  public UInteger getDisabledMonitoredItemCount() {
    return subscription.getDisabledMonitoredItemCount();
  }

  public LongAdder getMonitoringQueueOverflowCount() {
    return monitoringQueueOverflowCount;
  }

  public UInteger getNextSequenceNumber() {
    return subscription.getNextSequenceNumber();
  }

  public LongAdder getEventQueueOverflowCount() {
    return eventQueueOverflowCount;
  }

  /**
   * @return a {@link SubscriptionDiagnosticsDataType} containing the most current values.
   */
  public SubscriptionDiagnosticsDataType getSubscriptionDiagnosticsDataType() {
    return new SubscriptionDiagnosticsDataType(
        getSessionId(),
        getSubscriptionId(),
        getPriority(),
        getPublishingInterval(),
        getMaxKeepAliveCount(),
        getMaxLifetimeCount(),
        getMaxNotificationsPerPublish(),
        isPublishingEnabled(),
        uint(getModifyCount().sum()),
        uint(getEnableCount().sum()),
        uint(getDisableCount().sum()),
        uint(getRepublishRequestCount().sum()),
        uint(getRepublishMessageRequestCount().sum()),
        uint(getRepublishRequestCount().sum()),
        uint(getTransferRequestCount().sum()),
        uint(getTransferredToAltClientCount().sum()),
        uint(getTransferredToSameClientCount().sum()),
        uint(getPublishRequestCount().sum()),
        uint(getDataChangeNotificationsCount().sum()),
        uint(getEventNotificationsCount().sum()),
        uint(getNotificationsCount().sum()),
        uint(getLatePublishRequestCount().sum()),
        getCurrentKeepAliveCount(),
        getCurrentLifetimeCount(),
        getUnacknowledgedMessageCount(),
        uint(getDiscardedMessageCount().sum()),
        getMonitoredItemCount(),
        getDisabledMonitoredItemCount(),
        uint(getMonitoringQueueOverflowCount().sum()),
        getNextSequenceNumber(),
        uint(getEventQueueOverflowCount().sum()));
  }
}
