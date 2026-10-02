/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Organizes an AddressSpace's data MonitoredItems into interval groups and drives their lifecycle.
 *
 * <p>An AddressSpace forwards its four data item callbacks here and adds the manager to its
 * lifecycle. The manager buckets each item by its revised sampling interval, keeps one {@link
 * SamplingGroup} per interval, created by the {@link SamplingGroupFactory} it was given, moves an
 * item between groups when a ModifyMonitoredItems changes its interval, parks an item whose
 * MonitoringMode is Disabled outside every group until it is enabled again, and shuts a group down
 * when its last item leaves. {@link org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace} does
 * this wiring for its subclasses.
 *
 * <pre>{@code
 * SamplingManager samplingManager =
 *     new SamplingManager(
 *         server,
 *         (s, intervalMillis) -> new AddressSpaceSamplingGroup(s, addressSpace, intervalMillis));
 *
 * getLifecycleManager().addLifecycle(samplingManager);
 * }</pre>
 *
 * <p>Callbacks that arrive before {@link #startup()} are kept and their groups start with the
 * manager; callbacks after {@link #shutdown()} are ignored. All methods are safe to call from any
 * thread.
 */
public final class SamplingManager extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Object lock = new Object();

  /** Every item the manager knows, including Disabled ones, with the interval of its group. */
  private final Map<DataItem, Long> intervalByItem = new LinkedHashMap<>();

  private final Map<Long, SamplingGroup> groups = new TreeMap<>();
  private final Set<DataItem> disabledItems = new LinkedHashSet<>();
  private boolean stopped = false;

  private final OpcUaServer server;
  private final SamplingGroupFactory groupFactory;
  private final SamplingManagerConfig config;

  /**
   * Create a manager with {@link SamplingManagerConfig#defaults()}.
   *
   * @param server the server the groups run on.
   * @param groupFactory creates the group for each interval.
   */
  public SamplingManager(OpcUaServer server, SamplingGroupFactory groupFactory) {
    this(server, groupFactory, SamplingManagerConfig.defaults());
  }

  /**
   * Create a manager.
   *
   * @param server the server the groups run on.
   * @param groupFactory creates the group for each interval.
   * @param config how intervals are bucketed and floored, how initial samples are debounced, and
   *     how read access is refreshed.
   */
  public SamplingManager(
      OpcUaServer server, SamplingGroupFactory groupFactory, SamplingManagerConfig config) {

    this.server = server;
    this.groupFactory = groupFactory;
    this.config = config;
  }

  /**
   * Get this manager's configuration.
   *
   * @return this manager's configuration.
   */
  public SamplingManagerConfig getConfig() {
    return config;
  }

  @Override
  protected void onStartup() {
    synchronized (lock) {
      groups.values().forEach(SamplingGroup::startup);
    }
  }

  @Override
  protected void onShutdown() {
    List<SamplingGroup> toStop;

    synchronized (lock) {
      stopped = true;
      toStop = new ArrayList<>(groups.values());
      groups.clear();
      intervalByItem.clear();
      disabledItems.clear();
    }

    toStop.forEach(SamplingGroup::shutdown);
  }

  /**
   * Items were created. Each joins the group for its interval, or the Disabled set, and the group
   * samples it once after a short delay.
   *
   * @param dataItems the items that were created.
   */
  public void onDataItemsCreated(List<DataItem> dataItems) {
    synchronized (lock) {
      if (stopped) {
        return;
      }

      var byGroup = new LinkedHashMap<SamplingGroup, List<DataItem>>();

      for (DataItem item : dataItems) {
        long intervalMillis = config.groupIntervalMillis(item.getSamplingInterval());

        intervalByItem.put(item, intervalMillis);

        if (item.isSamplingEnabled()) {
          byGroup.computeIfAbsent(groupFor(intervalMillis), g -> new ArrayList<>()).add(item);
        } else {
          disabledItems.add(item);
        }
      }

      byGroup.forEach(SamplingGroup::addItems);
    }
  }

  /**
   * Items were modified. An item whose interval changed moves to the group for the new interval;
   * other modifications need no change here, since the group reads each item's own parameters when
   * it delivers a value.
   *
   * @param dataItems the items that were modified.
   */
  public void onDataItemsModified(List<DataItem> dataItems) {
    List<SamplingGroup> emptied;

    synchronized (lock) {
      if (stopped) {
        return;
      }

      var removals = new LinkedHashMap<SamplingGroup, List<DataItem>>();
      var additions = new LinkedHashMap<SamplingGroup, List<DataItem>>();

      for (DataItem item : dataItems) {
        Long previousIntervalMillis = intervalByItem.get(item);

        if (previousIntervalMillis == null) {
          logger.debug("Modified item is not managed here: {}", item.getReadValueId());
          continue;
        }

        long intervalMillis = config.groupIntervalMillis(item.getSamplingInterval());

        if (intervalMillis == previousIntervalMillis) {
          continue;
        }

        intervalByItem.put(item, intervalMillis);

        if (!disabledItems.contains(item)) {
          SamplingGroup previousGroup = groups.get(previousIntervalMillis);

          if (previousGroup != null) {
            removals.computeIfAbsent(previousGroup, g -> new ArrayList<>()).add(item);
          }

          additions.computeIfAbsent(groupFor(intervalMillis), g -> new ArrayList<>()).add(item);
        }
      }

      removals.forEach(SamplingGroup::removeItems);
      additions.forEach(SamplingGroup::addItems);

      emptied = removeEmptyGroups();
    }

    emptied.forEach(SamplingGroup::shutdown);
  }

  /**
   * Items were deleted. Each leaves its group; a group left empty is shut down.
   *
   * @param dataItems the items that were deleted.
   */
  public void onDataItemsDeleted(List<DataItem> dataItems) {
    List<SamplingGroup> emptied;

    synchronized (lock) {
      if (stopped) {
        return;
      }

      var removals = new LinkedHashMap<SamplingGroup, List<DataItem>>();

      for (DataItem item : dataItems) {
        Long intervalMillis = intervalByItem.remove(item);

        if (intervalMillis == null || disabledItems.remove(item)) {
          continue;
        }

        SamplingGroup group = groups.get(intervalMillis);

        if (group != null) {
          removals.computeIfAbsent(group, g -> new ArrayList<>()).add(item);
        }
      }

      removals.forEach(SamplingGroup::removeItems);

      emptied = removeEmptyGroups();
    }

    emptied.forEach(SamplingGroup::shutdown);
  }

  /**
   * Items had their MonitoringMode changed. A data item that is now Disabled leaves its group; one
   * that is enabled again rejoins it and is sampled once after a short delay, with its read access
   * refreshed first. Event items are ignored.
   *
   * @param monitoredItems the items whose MonitoringMode changed.
   */
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
    List<SamplingGroup> emptied;

    synchronized (lock) {
      if (stopped) {
        return;
      }

      var removals = new LinkedHashMap<SamplingGroup, List<DataItem>>();
      var additions = new LinkedHashMap<SamplingGroup, List<DataItem>>();

      for (MonitoredItem monitoredItem : monitoredItems) {
        if (!(monitoredItem instanceof DataItem item)) {
          continue;
        }

        Long intervalMillis = intervalByItem.get(item);

        if (intervalMillis == null) {
          continue;
        }

        if (item.isSamplingEnabled()) {
          if (disabledItems.remove(item)) {
            additions.computeIfAbsent(groupFor(intervalMillis), g -> new ArrayList<>()).add(item);
          }
        } else if (disabledItems.add(item)) {
          SamplingGroup group = groups.get(intervalMillis);

          if (group != null) {
            removals.computeIfAbsent(group, g -> new ArrayList<>()).add(item);
          }
        }
      }

      removals.forEach(SamplingGroup::removeItems);
      additions.forEach(SamplingGroup::addItems);

      emptied = removeEmptyGroups();
    }

    emptied.forEach(SamplingGroup::shutdown);
  }

  /**
   * Get a copy of every item this manager knows, including Disabled ones.
   *
   * @return a copy of every item this manager knows.
   */
  public List<DataItem> getDataItems() {
    synchronized (lock) {
      return List.copyOf(intervalByItem.keySet());
    }
  }

  /**
   * Get a snapshot of the groups, ordered by interval.
   *
   * @return the interval, item count, and request count of each group.
   */
  public List<SamplingGroupInfo> getGroups() {
    synchronized (lock) {
      return groups.values().stream().map(SamplingGroup::getInfo).toList();
    }
  }

  /** Caller holds the lock. */
  private SamplingGroup groupFor(long intervalMillis) {
    SamplingGroup group = groups.get(intervalMillis);

    if (group == null) {
      group = groupFactory.create(server, intervalMillis);
      group.configure(config);
      groups.put(intervalMillis, group);

      if (isRunning()) {
        group.startup();
      }
    }

    return group;
  }

  /** Caller holds the lock. Returns the removed groups so they can be shut down outside it. */
  private List<SamplingGroup> removeEmptyGroups() {
    var emptied = new ArrayList<SamplingGroup>();

    groups
        .values()
        .removeIf(
            group -> {
              if (group.isEmpty()) {
                emptied.add(group);
                return true;
              }
              return false;
            });

    return emptied;
  }
}
