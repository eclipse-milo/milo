/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate.groupMapCollate;

import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;

public class SubscriptionModel extends AbstractLifecycle {

  private final Set<DataItem> itemSet = ConcurrentHashMap.newKeySet();

  private final List<ScheduledUpdate> schedule = new CopyOnWriteArrayList<>();

  private final ExecutorService executor;
  private final ScheduledExecutorService scheduler;
  private final ExecutionQueue executionQueue;

  private final OpcUaServer server;
  private final AddressSpace addressSpace;

  public SubscriptionModel(OpcUaServer server, AddressSpace addressSpace) {
    this.server = server;

    this.addressSpace = addressSpace;

    executor = server.getExecutorService();
    scheduler = server.getScheduledExecutorService();

    executionQueue = new ExecutionQueue(executor);
  }

  @Override
  protected void onStartup() {}

  @Override
  protected void onShutdown() {
    executionQueue.submit(
        () -> {
          schedule.forEach(ScheduledUpdate::cancel);
          schedule.clear();
          itemSet.clear();
        });
  }

  public void onDataItemsCreated(List<DataItem> items) {
    if (isNotRunning()) {
      throw new IllegalArgumentException("not running");
    }

    executionQueue.submit(
        () -> {
          itemSet.addAll(items);
          reschedule();
        });
  }

  public void onDataItemsModified(List<DataItem> items) {
    if (isNotRunning()) {
      throw new IllegalArgumentException("not running");
    }

    executionQueue.submit(this::reschedule);
  }

  public void onDataItemsDeleted(List<DataItem> items) {
    if (isNotRunning()) {
      throw new IllegalArgumentException("not running");
    }

    executionQueue.submit(
        () -> {
          items.forEach(itemSet::remove);
          reschedule();
        });
  }

  public void onMonitoringModeChanged(List<MonitoredItem> items) {
    if (isNotRunning()) {
      throw new IllegalArgumentException("not running");
    }

    executionQueue.submit(this::reschedule);
  }

  private void reschedule() {
    Map<Double, List<DataItem>> bySamplingInterval =
        itemSet.stream()
            .filter(DataItem::isSamplingEnabled)
            .collect(Collectors.groupingBy(DataItem::getSamplingInterval));

    List<ScheduledUpdate> updates =
        bySamplingInterval.keySet().stream()
            .map(
                samplingInterval -> {
                  List<DataItem> items = bySamplingInterval.get(samplingInterval);

                  return new ScheduledUpdate(samplingInterval, items);
                })
            .collect(Collectors.toList());

    schedule.forEach(ScheduledUpdate::cancel);
    schedule.clear();
    schedule.addAll(updates);
    schedule.forEach(scheduler::execute);
  }

  private class ScheduledUpdate implements Runnable {

    private volatile boolean cancelled = false;

    private final long samplingInterval;
    private final List<DataItem> items;

    private ScheduledUpdate(double samplingInterval, List<DataItem> items) {
      this.samplingInterval = DoubleMath.roundToLong(samplingInterval, RoundingMode.UP);
      this.items = items;
    }

    private void cancel() {
      cancelled = true;
    }

    @Override
    public void run() {
      if (cancelled) return;

      List<DataValue> values =
          groupMapCollate(
              items,
              MonitoredItem::getSession,
              session ->
                  sessionItems -> {
                    List<PendingRead> pending =
                        sessionItems.stream()
                            .map(item -> new PendingRead(item.getReadValueId()))
                            .collect(Collectors.toList());

                    List<ReadValueId> ids =
                        pending.stream().map(PendingRead::getInput).collect(Collectors.toList());

                    var context = new ReadContext(server, session);

                    return addressSpace.read(context, 0d, TimestampsToReturn.Both, ids);
                  });

      Iterator<DataItem> ii = items.iterator();
      Iterator<DataValue> vi = values.iterator();

      while (ii.hasNext() && vi.hasNext()) {
        DataItem item = ii.next();
        DataValue value = vi.next();

        TimestampsToReturn timestamps = item.getTimestampsToReturn();

        if (timestamps != null) {
          UInteger attributeId = item.getReadValueId().getAttributeId();

          value =
              (AttributeId.Value.isEqual(attributeId))
                  ? DataValue.derivedValue(value, timestamps)
                  : DataValue.derivedNonValue(value, timestamps);
        }

        item.setValue(value);
      }

      if (!cancelled) {
        scheduler.schedule(() -> executor.execute(this), samplingInterval, TimeUnit.MILLISECONDS);
      }
    }
  }
}
