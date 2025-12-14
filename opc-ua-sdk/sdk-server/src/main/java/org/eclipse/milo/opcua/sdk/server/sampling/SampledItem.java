/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.jspecify.annotations.Nullable;

/**
 * Wraps a {@link DataItem} with a cached, bucketed sampling interval.
 *
 * <p>The sampling interval is immutable once created. If the requested sampling interval changes,
 * the SampledItem must be removed from its current {@link SamplingGroup} and a new one created.
 *
 * @param dataItem the wrapped DataItem.
 * @param samplingInterval the bucketed sampling interval in milliseconds. Use {@link
 *     #bucket(double, double)} to compute this from a requested interval.
 * @see SamplingGroup
 * @see SampledValue
 * @see SamplingManager
 */
public record SampledItem(DataItem dataItem, double samplingInterval) {

  /**
   * Get the NodeId of the node being monitored.
   *
   * @return the NodeId of the monitored node.
   */
  public NodeId getNodeId() {
    return dataItem.getReadValueId().getNodeId();
  }

  /**
   * Get the AttributeId being monitored.
   *
   * @return the AttributeId being monitored, or {@code null} if the attribute ID is not a valid
   *     AttributeId enum value.
   */
  public @Nullable AttributeId getAttributeId() {
    return AttributeId.from(dataItem.getReadValueId().getAttributeId()).orElse(null);
  }

  /**
   * Get the index range for array values, if specified.
   *
   * @return the index range string, or {@code null} if not specified.
   */
  public @Nullable String getIndexRange() {
    String indexRange = dataItem.getReadValueId().getIndexRange();
    return (indexRange == null || indexRange.isEmpty()) ? null : indexRange;
  }

  /**
   * Get the current monitoring mode.
   *
   * <p>This value is live and may change if the MonitoredItem's mode is modified.
   *
   * @return the current MonitoringMode.
   */
  public MonitoringMode getMonitoringMode() {
    if (dataItem instanceof BaseMonitoredItem<?> baseItem) {
      return baseItem.getMonitoringMode();
    }
    // Fallback: derive from isSamplingEnabled (cannot distinguish Sampling from Reporting)
    return dataItem.isSamplingEnabled() ? MonitoringMode.Reporting : MonitoringMode.Disabled;
  }

  /**
   * Get the originally requested sampling interval from the DataItem.
   *
   * <p>This value is live and may differ from {@link #samplingInterval()} which is the bucketed
   * value.
   *
   * @return the requested sampling interval in milliseconds.
   */
  public double getRequestedSamplingInterval() {
    return dataItem.getSamplingInterval();
  }

  /**
   * Check if this item is active (should be sampled).
   *
   * <p>An item is active if its monitoring mode is not Disabled.
   *
   * @return {@code true} if the item should be sampled.
   */
  public boolean isActive() {
    return dataItem.isSamplingEnabled();
  }

  /**
   * Bucket a requested sampling interval to the nearest bucket boundary.
   *
   * <p>Intervals are rounded UP to never sample faster than requested.
   *
   * @param requestedMs the requested sampling interval in milliseconds.
   * @param bucketSizeMs the bucket size in milliseconds.
   * @return the bucketed sampling interval.
   */
  public static double bucket(double requestedMs, double bucketSizeMs) {
    if (bucketSizeMs <= 0) {
      return requestedMs;
    }
    double remainder = requestedMs % bucketSizeMs;
    if (remainder == 0) {
      return requestedMs;
    }
    // Round UP - never sample faster than requested
    return requestedMs + (bucketSizeMs - remainder);
  }
}
