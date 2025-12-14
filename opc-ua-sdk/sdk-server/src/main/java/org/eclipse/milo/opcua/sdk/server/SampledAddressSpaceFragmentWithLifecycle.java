/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.sampling.SampleSink;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroup;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManager;

/**
 * A {@link ManagedAddressSpaceFragmentWithLifecycle} that provides built-in support for monitored
 * item sampling.
 *
 * <p>This class handles:
 *
 * <ul>
 *   <li>Scheduling sampling at the correct intervals
 *   <li>Efficiently grouping items by sampling interval
 *   <li>Handling monitoring mode changes
 *   <li>Providing hooks for external data source polling
 * </ul>
 *
 * <h2>Internal vs External Sampling</h2>
 *
 * <p>There are two independent sampling concerns:
 *
 * <ul>
 *   <li><b>Internal sampling</b> (framework responsibility): For ALL monitored items, the framework
 *       periodically reads from UaNodes and calls {@code DataItem.setValue()} for change detection
 *       and subscription notification.
 *   <li><b>External sampling</b> (user responsibility): For items backed by external systems (PLCs,
 *       databases, APIs), subclasses override {@link #sampleItems(SamplingGroup, SampleSink)} to
 *       poll external values and post them to the SampleSink. The values are applied to UaNodes,
 *       making them available for internal sampling to read.
 * </ul>
 *
 * <p>For nodes with static values or values updated by other means (direct writes, background
 * processes), the default no-op {@code sampleItems()} is sufficient.
 *
 * <h2>Usage Example</h2>
 *
 * <pre>{@code
 * public class PlcAddressSpace extends SampledAddressSpaceFragmentWithLifecycle {
 *
 *     private final PlcConnection plc;
 *
 *     public PlcAddressSpace(OpcUaServer server, PlcConnection plc) {
 *         super(server);
 *         this.plc = plc;
 *     }
 *
 *     @Override
 *     protected void sampleItems(SamplingGroup group, SampleSink sink) {
 *         DateTime timestamp = DateTime.now();
 *         List<SampledValue> values = new ArrayList<>();
 *         try {
 *             for (SampledItem item : group.getActiveItems()) {
 *                 String address = lookupPlcAddress(item.getNodeId());
 *                 Object value = plc.read(address);
 *                 values.add(SampledValue.of(item, new Variant(value)));
 *             }
 *         } catch (PlcException e) {
 *             StatusCode bad = new StatusCode(StatusCodes.Bad_CommunicationError);
 *             for (SampledItem item : group.getActiveItems()) {
 *                 values.add(new SampledValue(item, Variant.NULL_VALUE, bad, null));
 *             }
 *         }
 *         sink.post(new Sample(timestamp, values));
 *     }
 * }
 * }</pre>
 */
public abstract class SampledAddressSpaceFragmentWithLifecycle
    extends ManagedAddressSpaceFragmentWithLifecycle {

  private final SamplingManager samplingManager;

  /**
   * Create a new SampledAddressSpaceFragmentWithLifecycle using the server's {@link
   * AddressSpaceManager} as the composite.
   *
   * @param server the {@link OpcUaServer} instance.
   */
  public SampledAddressSpaceFragmentWithLifecycle(OpcUaServer server) {
    this(server, server.getAddressSpaceManager());
  }

  /**
   * Create a new SampledAddressSpaceFragmentWithLifecycle using the specified composite.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param composite the {@link AddressSpaceComposite} this fragment is part of.
   */
  public SampledAddressSpaceFragmentWithLifecycle(
      OpcUaServer server, AddressSpaceComposite composite) {

    super(server, composite);

    this.samplingManager =
        new SamplingManager(
            server.getExecutorService(),
            server.getScheduledExecutorService(),
            getNodeManager(),
            this::sampleItems,
            this::onGroupRemoved);

    getLifecycleManager().addLifecycle(samplingManager);
  }

  /**
   * Create a new SampledAddressSpaceFragmentWithLifecycle using the server's {@link
   * AddressSpaceManager} as the composite and the specified node manager.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param nodeManager the {@link UaNodeManager} to manage nodes with.
   */
  public SampledAddressSpaceFragmentWithLifecycle(OpcUaServer server, UaNodeManager nodeManager) {
    this(server, nodeManager, server.getAddressSpaceManager());
  }

  /**
   * Create a new SampledAddressSpaceFragmentWithLifecycle using the specified composite and node
   * manager.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param nodeManager the {@link UaNodeManager} to manage nodes with.
   * @param composite the {@link AddressSpaceComposite} this fragment is part of.
   */
  public SampledAddressSpaceFragmentWithLifecycle(
      OpcUaServer server, UaNodeManager nodeManager, AddressSpaceComposite composite) {

    super(server, nodeManager, composite);

    this.samplingManager =
        new SamplingManager(
            server.getExecutorService(),
            server.getScheduledExecutorService(),
            getNodeManager(),
            this::sampleItems,
            this::onGroupRemoved);

    getLifecycleManager().addLifecycle(samplingManager);
  }

  // === Hooks for subclasses ===

  /**
   * Called during {@link SamplingGroup} execution to allow external value updates.
   *
   * <p>Override to fetch values from external systems (PLCs, databases, APIs) and post them to the
   * sink. Posted values are applied to UaNodes, where they will be read by subsequent internal
   * sampling cycles.
   *
   * <p>The default implementation is a no-op, suitable for nodes with static values or values
   * updated by other means (direct writes, background processes).
   *
   * <p>Use {@code group.isDirty()} to check if items have changed since the last call, allowing
   * cached optimization work to be reused.
   *
   * <h3>Timing</h3>
   *
   * <p>For <b>periodic sampling</b>, values are posted to an async channel. They are typically
   * applied to UaNodes before the internal sampling in the same cycle reads them, but this is not
   * strictly guaranteed.
   *
   * <p>For <b>immediate sampling</b> (when items are first created), a synchronous sink is used to
   * ensure values are applied to UaNodes before internal sampling reads them.
   *
   * <h3>Error Handling</h3>
   *
   * <p>Implementations are responsible for posting appropriate values for ALL items, including bad
   * StatusCodes on failure:
   *
   * <pre>{@code
   * protected void sampleItems(SamplingGroup group, SampleSink sink) {
   *     DateTime timestamp = DateTime.now();
   *     List<SampledValue> values = new ArrayList<>();
   *     try {
   *         // Read from external system...
   *     } catch (Exception e) {
   *         StatusCode bad = new StatusCode(StatusCodes.Bad_CommunicationError);
   *         for (SampledItem item : group.getActiveItems()) {
   *             values.add(new SampledValue(item, Variant.NULL_VALUE, bad, null));
   *         }
   *     }
   *     sink.post(new Sample(timestamp, values));
   * }
   * }</pre>
   *
   * @param group the {@link SamplingGroup} being executed.
   * @param sink the {@link SampleSink} to post values to.
   */
  protected void sampleItems(SamplingGroup group, SampleSink sink) {}

  /**
   * Called when a {@link SamplingGroup} is removed (has no items remaining).
   *
   * <p>Override to clean up any cached state keyed by the group.
   *
   * @param group the {@link SamplingGroup} being removed.
   */
  protected void onGroupRemoved(SamplingGroup group) {}

  // === DataItem lifecycle callbacks ===

  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {
    samplingManager.addItems(dataItems);
  }

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {
    samplingManager.modifyItems(dataItems);
  }

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {
    samplingManager.removeItems(dataItems);
  }

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
    // Filter to DataItems and trigger modify to update dirty flags
    List<DataItem> dataItems =
        monitoredItems.stream()
            .filter(DataItem.class::isInstance)
            .map(DataItem.class::cast)
            .toList();

    if (!dataItems.isEmpty()) {
      samplingManager.modifyItems(dataItems);
    }
  }

  // === Accessors ===

  /**
   * Get the {@link SamplingManager} for this fragment.
   *
   * @return the {@link SamplingManager}.
   */
  protected SamplingManager getSamplingManager() {
    return samplingManager;
  }
}
