/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.util.DataChangeMonitoringFilter;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.NonNull;

public class MonitoredDataItem extends BaseMonitoredItem<DataValue> implements DataItem {

  public static final DataChangeFilter DEFAULT_FILTER =
      new DataChangeFilter(DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

  private volatile DataValue lastValue = null;
  private volatile DataChangeFilter filter = null;
  private volatile ExtensionObject filterResult = null;

  public MonitoredDataItem(
      OpcUaServer server,
      Session session,
      UInteger id,
      UInteger subscriptionId,
      ReadValueId readValueId,
      MonitoringMode monitoringMode,
      TimestampsToReturn timestamps,
      UInteger clientHandle,
      double samplingInterval,
      UInteger queueSize,
      boolean discardOldest) {

    super(
        server,
        session,
        id,
        subscriptionId,
        readValueId,
        monitoringMode,
        timestamps,
        clientHandle,
        samplingInterval,
        queueSize,
        discardOldest);
  }

  @Override
  public synchronized void setValue(DataValue value) {
    boolean valuePassesFilter = DataChangeMonitoringFilter.filter(lastValue, value, filter);

    if (valuePassesFilter) {
      lastValue = value;

      enqueue(value);

      if (triggeredItems != null) {
        triggeredItems.values().forEach(item -> item.triggered = true);
      }
    }
  }

  @Override
  protected synchronized void enqueue(@NonNull DataValue value) {
    if (queue.size() < queue.maxSize()) {
      queue.add(value);
    } else {
      StatusCode statusCode = value.statusCode();

      if (getQueueSize() > 1) {
        /* Set overflow if queueSize > 1... */
        value = value.withStatus(statusCode.withOverflow());

        Subscription subscription =
            session.getSubscriptionManager().getSubscription(subscriptionId);

        if (subscription != null) {
          subscription.getSubscriptionDiagnostics().getMonitoringQueueOverflowCount().increment();
        }
      } else if (statusCode.isOverflowSet()) {
        /* But make sure it's clear otherwise. */
        value = value.withStatus(statusCode.withoutOverflow());
      }

      if (discardOldest) {
        queue.add(value);
      } else {
        queue.set(queue.maxSize() - 1, value);
      }
    }
  }

  @Override
  public synchronized void setQuality(StatusCode quality) {
    if (lastValue == null) {
      setValue(new DataValue(Variant.NULL_VALUE, quality, DateTime.now(), DateTime.now()));
    } else {
      DataValue value = new DataValue(lastValue.value(), quality, DateTime.now(), DateTime.now());

      setValue(value);
    }
  }

  @Override
  public boolean isSamplingEnabled() {
    return getMonitoringMode() != MonitoringMode.Disabled;
  }

  @Override
  public synchronized void setMonitoringMode(MonitoringMode monitoringMode) {
    if (monitoringMode == MonitoringMode.Disabled) {
      lastValue = null;
    }

    super.setMonitoringMode(monitoringMode);
  }

  public synchronized void maybeSendLastValue() {
    if (queue.isEmpty() && lastValue != null) {
      enqueue(lastValue);
    }
  }

  @Override
  public void installFilter(MonitoringFilter filter) throws UaException {
    if (filter instanceof DataChangeFilter) {
      this.filter = (DataChangeFilter) filter;
    } else {
      throw new UaException(StatusCodes.Bad_MonitoredItemFilterUnsupported);
    }
  }

  @Override
  public ExtensionObject getFilterResult() {
    return filterResult;
  }

  @Override
  protected MonitoredItemNotification wrapQueueValue(DataValue value) {
    boolean includeSource =
        timestamps == TimestampsToReturn.Source || timestamps == TimestampsToReturn.Both;
    boolean includeServer =
        timestamps == TimestampsToReturn.Server || timestamps == TimestampsToReturn.Both;

    // remove the source timestamp if not requested
    boolean sourceTimeUpdated = false;
    DateTime sourceTime = value.sourceTime();
    UShort sourcePicoseconds = value.sourcePicoseconds();
    if (!includeSource && (sourceTime != null || sourcePicoseconds != null)) {
      sourceTime = null;
      sourcePicoseconds = null;
      sourceTimeUpdated = true;
    }

    // remove server timestamp if not requested, add if requested but not present
    boolean serverTimeUpdated = false;
    DateTime serverTime = value.serverTime();
    UShort serverPicoseconds = value.serverPicoseconds();
    if (!includeServer && (serverTime != null || serverPicoseconds != null)) {
      serverTime = null;
      serverPicoseconds = null;
      serverTimeUpdated = true;
    } else if (includeServer && serverTime == null) {
      serverTime = DateTime.now();
      serverTimeUpdated = true;
    }

    // create a new DataValue instance if anything changed
    if (sourceTimeUpdated || serverTimeUpdated) {
      value =
          new DataValue(
              value.value(),
              value.statusCode(),
              sourceTime,
              sourcePicoseconds,
              serverTime,
              serverPicoseconds);
    }

    return new MonitoredItemNotification(uint(getClientHandle()), value);
  }
}
