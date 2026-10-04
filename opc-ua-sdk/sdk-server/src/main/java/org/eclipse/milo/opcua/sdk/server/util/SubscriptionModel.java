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

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.sampling.AddressSpaceSamplingGroup;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManager;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManagerConfig;

/**
 * Samples data MonitoredItems on behalf of an {@link AddressSpace} by reading them from it at each
 * item's sampling interval.
 *
 * <p>An {@link AddressSpace} that does not have its own sampling mechanism forwards its {@code
 * onDataItemsCreated}, {@code onDataItemsModified}, {@code onDataItemsDeleted}, and {@code
 * onMonitoringModeChanged} callbacks to an instance of this class and adds it to its lifecycle.
 *
 * <p>This is an adapter over a {@link SamplingManager} with {@link AddressSpaceSamplingGroup}s and
 * the default {@link SamplingManagerConfig} without bucketing: every sampling cycle refreshes each
 * item's read access result for its current Session and then reads the items the Session may read,
 * as the framework does for any group. Each item is sampled at its own sampling interval rounded up
 * to a whole millisecond, as before this class became an adapter, because that is the interval the
 * AddressSpace reported to the client.
 *
 * @deprecated {@link ManagedAddressSpace} forwards the four callbacks to a {@link SamplingManager}
 *     of its own, so a subclass that used this class need only delete its forwarding. An
 *     AddressSpace that is not a {@code ManagedAddressSpace} creates a {@link SamplingManager} with
 *     an {@link AddressSpaceSamplingGroup} factory directly, which also gives it the configuration
 *     this class does not expose.
 */
@Deprecated
public class SubscriptionModel extends AbstractLifecycle {

  private final SamplingManager samplingManager;

  public SubscriptionModel(OpcUaServer server, AddressSpace addressSpace) {
    // An AddressSpace that forwards here revises intervals with its own onCreateDataItem, which
    // knows nothing of buckets, so bucketing would sample at an interval the client was not told.
    samplingManager =
        new SamplingManager(
            server,
            (s, intervalMillis) -> new AddressSpaceSamplingGroup(s, addressSpace, intervalMillis),
            SamplingManagerConfig.defaults().withBucketMillis(0));
  }

  @Override
  protected void onStartup() {
    samplingManager.startup();
  }

  @Override
  protected void onShutdown() {
    samplingManager.shutdown();
  }

  public void onDataItemsCreated(List<DataItem> items) {
    samplingManager.onDataItemsCreated(items);
  }

  public void onDataItemsModified(List<DataItem> items) {
    samplingManager.onDataItemsModified(items);
  }

  public void onDataItemsDeleted(List<DataItem> items) {
    samplingManager.onDataItemsDeleted(items);
  }

  public void onMonitoringModeChanged(List<MonitoredItem> items) {
    samplingManager.onMonitoringModeChanged(items);
  }

  /**
   * Get a copy of the {@link DataItem}s in this {@link SubscriptionModel}.
   *
   * @return a copy of the {@link DataItem}s in this {@link SubscriptionModel}.
   */
  public List<DataItem> getDataItems() {
    return samplingManager.getDataItems();
  }

  /**
   * Get the {@link SamplingManager} this model forwards to.
   *
   * @return the {@link SamplingManager} this model forwards to.
   */
  public SamplingManager getSamplingManager() {
    return samplingManager;
  }
}
