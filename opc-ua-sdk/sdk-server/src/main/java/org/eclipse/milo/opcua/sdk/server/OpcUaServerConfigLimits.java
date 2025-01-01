/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public interface OpcUaServerConfigLimits {

  /**
   * Get the maximum number of sessions that can be open at any given time.
   *
   * @return the maximum number of sessions that can be open at any given time.
   */
  default UInteger getMaxSessions() {
    return uint(100);
  }

  /**
   * Get the maximum session timeout, in milliseconds, allowed by the server.
   *
   * <p>Requests by clients for larger timeouts will be revised down to this limit.
   *
   * @return the maximum session timeout, in milliseconds, allowed by the server.
   */
  default Double getMaxSessionTimeout() {
    return 2d * 60_000d;
  }

  /**
   * Get the minimum allowed publishing interval.
   *
   * @return the minimum allowed publishing interval.
   */
  default Double getMinPublishingInterval() {
    return 10.0;
  }

  /**
   * Get the maximum allowed publishing interval.
   *
   * @return the maximum allowed publishing interval.
   */
  default Double getMaxPublishingInterval() {
    return (double) TimeUnit.MILLISECONDS.convert(8, TimeUnit.HOURS);
  }

  /**
   * Get the default publishing interval, used when the requested interval is either invalid or
   * below the minimum.
   *
   * @return the default publishing interval.
   */
  default Double getDefaultPublishingInterval() {
    return 250.0;
  }

  /**
   * Get the minimum subscription lifetime, in milliseconds.
   *
   * <p>This value should be larger than the configured minimum publishing interval.
   *
   * @return the minimum subscription lifetime, in milliseconds.
   */
  default Double getMinSubscriptionLifetime() {
    return 10_000.0;
  }

  /**
   * Get the maximum subscription lifetime, in milliseconds.
   *
   * <p>This value should be larger than the configured maximum publishing interval.
   *
   * @return the maximum subscription lifetime, in milliseconds.
   */
  default Double getMaxSubscriptionLifetime() {
    return (double) TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS);
  }

  /**
   * Get the maximum number of Subscriptions, across all Sessions, that can be created.
   *
   * @return the maximum number of Subscriptions, across all Sessions, that can be created.
   */
  default UInteger getMaxSubscriptions() {
    return uint(Integer.MAX_VALUE);
  }

  /**
   * Get the maximum number of Subscriptions, per Session, that can be created.
   *
   * @return the maximum number of Subscriptions, per Session, that can be created.
   */
  default UInteger getMaxSubscriptionsPerSession() {
    return uint(Integer.MAX_VALUE);
  }

  default Double getMaxSupportedSampleRate() {
    return (double) TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS);
  }

  /**
   * Get the maximum allowed password length.
   *
   * @return the maximum allowed password length.
   */
  default UInteger getMaxPasswordLength() {
    return uint(1024);
  }

  /**
   * Get the maximum number of MonitoredItems, across all Sessions, that can be created.
   *
   * @return the maximum number of MonitoredItems, across all Sessions, that can be created.
   */
  default UInteger getMaxMonitoredItems() {
    return uint(Integer.MAX_VALUE);
  }

  /**
   * Get the maximum number of MonitoredItems, per Session, that can be created.
   *
   * @return the maximum number of MonitoredItems, per Session, that can be created.
   */
  default UInteger getMaxMonitoredItemsPerSession() {
    return uint(Integer.MAX_VALUE);
  }

  // region ServerCapabilities

  default Double getMinSupportedSampleRate() {
    return 0.0;
  }

  default UInteger getMaxArrayLength() {
    return uint(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE / 2);
  }

  default UInteger getMaxStringLength() {
    return uint(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE / 2);
  }

  default UInteger getMaxByteStringLength() {
    return uint(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE / 2);
  }

  default UShort getMaxBrowseContinuationPoints() {
    return ushort(250);
  }

  default UShort getMaxQueryContinuationPoints() {
    return ushort(250);
  }

  default UShort getMaxHistoryContinuationPoints() {
    return ushort(250);
  }

  // endregion

  // region OperationLimits

  default UInteger getMaxNodesPerRead() {
    return uint(10_000);
  }

  default UInteger getMaxNodesPerWrite() {
    return uint(10_000);
  }

  default UInteger getMaxMonitoredItemsPerCall() {
    return uint(10_000);
  }

  default UInteger getMaxNodesPerRegisterNodes() {
    return uint(250);
  }

  default UInteger getMaxNodesPerHistoryReadData() {
    return uint(250);
  }

  default UInteger getMaxNodesPerHistoryReadEvents() {
    return uint(250);
  }

  default UInteger getMaxNodesPerHistoryUpdateData() {
    return uint(250);
  }

  default UInteger getMaxNodesPerHistoryUpdateEvents() {
    return uint(250);
  }

  default UInteger getMaxNodesPerMethodCall() {
    return uint(250);
  }

  default UInteger getMaxNodesPerBrowse() {
    return uint(250);
  }

  default UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() {
    return uint(250);
  }

  default UInteger getMaxNodesPerNodeManagement() {
    return uint(250);
  }

  // endregion

}
