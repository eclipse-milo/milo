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
import java.util.ArrayList;
import java.util.IdentityHashMap;
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
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Samples data MonitoredItems on behalf of an {@link AddressSpace} by reading them from it at each
 * item's sampling interval.
 *
 * <p>An {@link AddressSpace} that does not have its own sampling mechanism forwards its {@code
 * onDataItemsCreated}, {@code onDataItemsModified}, {@code onDataItemsDeleted}, and {@code
 * onMonitoringModeChanged} callbacks to an instance of this class and adds it to its lifecycle.
 *
 * <p>Every sampling cycle first refreshes each item's read access result for its current Session
 * with the server's {@link org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController},
 * then reads the items the Session may read. An item the Session may not read is not read and
 * reports the denial status instead; see {@link DataItem#setReadAccessResult}. A failed check keeps
 * each item's last result.
 */
public class SubscriptionModel extends AbstractLifecycle {

  private static final Logger logger = LoggerFactory.getLogger(SubscriptionModel.class);

  private final Set<DataItem> itemSet = ConcurrentHashMap.newKeySet();

  private final List<ScheduledUpdate> schedule = new CopyOnWriteArrayList<>();

  // Held while reschedule() cancels the current updates and while a cycle applies its read access
  // results, so a cycle that was cancelled mid-check can never apply a result that a replacement
  // cycle has already superseded.
  private final Object cycleLock = new Object();

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

  /**
   * Get a copy of the {@link DataItem}s in this {@link SubscriptionModel}.
   *
   * @return a copy of the {@link DataItem}s in this {@link SubscriptionModel}.
   */
  public List<DataItem> getDataItems() {
    return List.copyOf(itemSet);
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
            .toList();

    synchronized (cycleLock) {
      schedule.forEach(ScheduledUpdate::cancel);
      schedule.clear();
      schedule.addAll(updates);
    }
    schedule.forEach(executor::execute);
  }

  /**
   * Check {@code session}'s read access to {@code sessionItems}, which were grouped under that
   * Session.
   *
   * <p>Part 4 §5.13.2.1 requires a change in access rights after CreateMonitoredItems to reach the
   * client in a Publish response, and data to resume once access is allowed again. Checking on
   * every cycle is what makes both transitions visible, and it uses the item's current Session, so
   * a transferred subscription is checked for its new Session on its next cycle.
   *
   * @return the result for each item the check answered for. Empty if the check failed: a fault in
   *     the check is not an access decision, so each item keeps its last result.
   */
  private Map<DataItem, AccessResult> checkReadAccess(
      Session session, List<DataItem> sessionItems) {
    List<ReadValueId> readValueIds =
        sessionItems.stream().map(MonitoredItem::getReadValueId).collect(Collectors.toList());

    Map<ReadValueId, AccessResult> byReadValueId;
    try {
      byReadValueId = server.getAccessController().checkReadAccess(session, readValueIds);
    } catch (Exception e) {
      logger.warn("Read access check failed: {}", e.getMessage(), e);
      return Map.of();
    }

    var byItem = new IdentityHashMap<DataItem, AccessResult>();

    for (DataItem item : sessionItems) {
      AccessResult accessResult = byReadValueId.get(item.getReadValueId());

      if (accessResult != null) {
        byItem.put(item, accessResult);
      }
    }

    return byItem;
  }

  static long nextDelayMillis(long samplingInterval, long elapsedMillis) {
    return Math.max(1, samplingInterval - elapsedMillis);
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

      long startNanos = System.nanoTime();

      // Check read access, one call per Session, outside the lock. The result is applied under
      // the lock, and only if this cycle has not been cancelled meanwhile, so a check that was
      // overtaken by a replacement cycle cannot clear a denial that cycle applied.
      var accessResults = new IdentityHashMap<DataItem, AccessResult>();

      items.stream()
          .collect(Collectors.groupingBy(MonitoredItem::getSession))
          .forEach(
              (session, sessionItems) ->
                  accessResults.putAll(checkReadAccess(session, sessionItems)));

      var readableItems = new ArrayList<DataItem>(items.size());

      synchronized (cycleLock) {
        if (cancelled) return;

        for (DataItem item : items) {
          AccessResult accessResult = accessResults.get(item);

          if (accessResult != null) {
            item.setReadAccessResult(accessResult);
          }
          if (accessResult == null || !accessResult.isDenied()) {
            readableItems.add(item);
          }
        }
      }

      // Values set from here on pass through the item's gate, which holds the latest applied
      // result, so a read that outlives a cancellation cannot bypass a newer denial.
      List<DataValue> values =
          groupMapCollate(
              readableItems,
              MonitoredItem::getSession,
              session ->
                  (List<DataItem> sessionItems) -> {
                    List<ReadValueId> readValueIds =
                        sessionItems.stream()
                            .map(MonitoredItem::getReadValueId)
                            .collect(Collectors.toList());

                    var context = new ReadContext(server, session);

                    return addressSpace.read(context, 0d, TimestampsToReturn.Both, readValueIds);
                  });

      Iterator<DataItem> ii = readableItems.iterator();
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
        long elapsedNanos = System.nanoTime() - startNanos;
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        long delay = nextDelayMillis(samplingInterval, elapsedMillis);

        scheduler.schedule(() -> executor.execute(this), delay, TimeUnit.MILLISECONDS);
      }
    }
  }
}
