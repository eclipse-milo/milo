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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

/**
 * Coordinates all sampling operations for monitored items.
 *
 * <p>Manages the mapping from DataItems to SampledItems, groups items by bucketed sampling
 * interval, and handles both scheduled and immediate sampling.
 *
 * <h2>Thread Safety</h2>
 *
 * <p>All public methods are thread-safe. This class uses:
 *
 * <ul>
 *   <li>{@link java.util.concurrent.ConcurrentHashMap ConcurrentHashMap} for item and group
 *       tracking
 *   <li>{@link java.util.concurrent.ConcurrentLinkedQueue ConcurrentLinkedQueue} for the immediate
 *       sampling queue
 *   <li>{@link java.util.concurrent.atomic.AtomicBoolean AtomicBoolean} for scheduling state
 * </ul>
 *
 * <h2>Bucket Size</h2>
 *
 * <p>Sampling intervals are bucketed to reduce the number of distinct groups. The default bucket
 * size is {@value #DEFAULT_BUCKET_SIZE}ms. Use the constructor with a {@code bucketSize} parameter
 * to customize this value.
 *
 * <p>Note: The bucket size cannot be changed through {@link
 * org.eclipse.milo.opcua.sdk.server.SampledAddressSpaceFragmentWithLifecycle
 * SampledAddressSpaceFragmentWithLifecycle} directly; subclass this class if you need a custom
 * bucket size.
 *
 * @see SampledAddressSpaceFragmentWithLifecycle
 * @see SamplingGroup
 * @see SampledItem
 */
public class SamplingManager extends AbstractLifecycle {

  /** Default bucket size in milliseconds. */
  public static final double DEFAULT_BUCKET_SIZE = 25.0;

  private final double bucketSize;
  private final Map<DataItem, SampledItem> itemsByDataItem = new ConcurrentHashMap<>();
  private final Map<Double, SamplingGroup> groupsByInterval = new ConcurrentHashMap<>();
  private final Queue<SampledItem> immediateQueue = new ConcurrentLinkedQueue<>();
  private final AtomicBoolean immediateScheduled = new AtomicBoolean(false);

  private final ExecutorService executor;
  private final ScheduledExecutorService scheduler;
  private final UaNodeManager nodeManager;
  private final AsyncValueChannel asyncValueChannel;
  private final BlockingValueChannel blockingValueChannel;
  private final BiConsumer<SamplingGroup, SampleSink> sampleCallback;
  private final Consumer<SamplingGroup> groupRemovedCallback;

  /**
   * Create a new SamplingManager with the default bucket size.
   *
   * @param executor the executor for running sampling tasks.
   * @param scheduler the scheduler for timing between executions.
   * @param nodeManager the UaNodeManager for looking up nodes.
   * @param sampleCallback the callback to invoke during sampling.
   * @param groupRemovedCallback the callback to invoke when a group is removed.
   */
  public SamplingManager(
      ExecutorService executor,
      ScheduledExecutorService scheduler,
      UaNodeManager nodeManager,
      BiConsumer<SamplingGroup, SampleSink> sampleCallback,
      Consumer<SamplingGroup> groupRemovedCallback) {
    this(
        DEFAULT_BUCKET_SIZE,
        executor,
        scheduler,
        nodeManager,
        sampleCallback,
        groupRemovedCallback);
  }

  /**
   * Create a new SamplingManager.
   *
   * @param bucketSize the bucket size in milliseconds for interval bucketing.
   * @param executor the executor for running sampling tasks.
   * @param scheduler the scheduler for timing between executions.
   * @param nodeManager the UaNodeManager for looking up nodes.
   * @param sampleCallback the callback to invoke during sampling.
   * @param groupRemovedCallback the callback to invoke when a group is removed.
   */
  public SamplingManager(
      double bucketSize,
      ExecutorService executor,
      ScheduledExecutorService scheduler,
      UaNodeManager nodeManager,
      BiConsumer<SamplingGroup, SampleSink> sampleCallback,
      Consumer<SamplingGroup> groupRemovedCallback) {
    this.bucketSize = bucketSize;
    this.executor = executor;
    this.scheduler = scheduler;
    this.nodeManager = nodeManager;
    this.sampleCallback = sampleCallback;
    this.groupRemovedCallback = groupRemovedCallback;
    this.asyncValueChannel = new AsyncValueChannel(executor, nodeManager);
    this.blockingValueChannel = new BlockingValueChannel(nodeManager);
  }

  @Override
  protected void onStartup() {
    // Start any existing groups (normally none at startup)
    groupsByInterval.values().forEach(SamplingGroup::start);
  }

  @Override
  protected void onShutdown() {
    groupsByInterval.values().forEach(SamplingGroup::shutdown);
    groupsByInterval.clear();
    itemsByDataItem.clear();
  }

  /**
   * Add data items to be sampled.
   *
   * @param dataItems the data items to add.
   */
  public void addItems(List<DataItem> dataItems) {
    List<SampledItem> newItems = new ArrayList<>();

    for (DataItem dataItem : dataItems) {
      double requestedInterval = dataItem.getSamplingInterval();
      double bucketedInterval = SampledItem.bucket(requestedInterval, bucketSize);

      SampledItem sampledItem = new SampledItem(dataItem, bucketedInterval);
      itemsByDataItem.put(dataItem, sampledItem);
      newItems.add(sampledItem);

      // Add to the appropriate group
      SamplingGroup group =
          groupsByInterval.computeIfAbsent(bucketedInterval, this::createSamplingGroup);
      group.addItems(List.of(sampledItem));
    }

    // Queue for immediate sampling
    if (!newItems.isEmpty()) {
      queueImmediateSampling(newItems);
    }
  }

  /**
   * Handle modifications to data items.
   *
   * <p>If the bucketed interval changes, moves the item to a different group.
   *
   * @param dataItems the modified data items.
   */
  public void modifyItems(List<DataItem> dataItems) {
    for (DataItem dataItem : dataItems) {
      SampledItem existingItem = itemsByDataItem.get(dataItem);
      if (existingItem == null) {
        continue;
      }

      double newRequestedInterval = dataItem.getSamplingInterval();
      double newBucketedInterval = SampledItem.bucket(newRequestedInterval, bucketSize);

      // Check if the bucketed interval changed
      if (existingItem.samplingInterval() != newBucketedInterval) {
        // Remove from the old group
        SamplingGroup oldGroup = groupsByInterval.get(existingItem.samplingInterval());
        if (oldGroup != null) {
          oldGroup.removeItems(List.of(existingItem));
          maybeRemoveGroup(oldGroup);
        }

        // Create a new SampledItem with the new bucketed interval
        SampledItem newItem = new SampledItem(dataItem, newBucketedInterval);
        itemsByDataItem.put(dataItem, newItem);

        // Add to the new group
        SamplingGroup newGroup =
            groupsByInterval.computeIfAbsent(newBucketedInterval, this::createSamplingGroup);
        newGroup.addItems(List.of(newItem));
      } else {
        // Interval unchanged, but mark group dirty for monitoring mode changes
        SamplingGroup group = groupsByInterval.get(existingItem.samplingInterval());
        if (group != null) {
          group.markDirty();
        }
      }
    }
  }

  /**
   * Remove data items from sampling.
   *
   * @param dataItems the data items to remove.
   */
  public void removeItems(List<DataItem> dataItems) {
    for (DataItem dataItem : dataItems) {
      SampledItem sampledItem = itemsByDataItem.remove(dataItem);
      if (sampledItem != null) {
        SamplingGroup group = groupsByInterval.get(sampledItem.samplingInterval());
        if (group != null) {
          group.removeItems(List.of(sampledItem));
          maybeRemoveGroup(group);
        }
      }
    }
  }

  private SamplingGroup createSamplingGroup(double interval) {
    SamplingGroup group =
        new SamplingGroup(
            interval, executor, scheduler, this::executeSamplingCallback, asyncValueChannel);

    if (isRunning()) {
      group.start();
    }

    return group;
  }

  private void maybeRemoveGroup(SamplingGroup group) {
    if (group.isEmpty()) {
      groupsByInterval.remove(group.getSamplingInterval());
      group.shutdown();
      if (groupRemovedCallback != null) {
        groupRemovedCallback.accept(group);
      }
    }
  }

  private void executeSamplingCallback(SamplingGroup group, SampleSink sink) {
    // Step 1: External sampling - let user update UaNodes
    if (sampleCallback != null) {
      sampleCallback.accept(group, sink);
    }

    // Step 2: Internal sampling - read from UaNodes, notify DataItems
    for (SampledItem item : group.getActiveItems()) {
      DataValue value = readFromNode(item);
      setDataItemValue(item, value);
    }
  }

  private void queueImmediateSampling(List<SampledItem> items) {
    immediateQueue.addAll(items);
    if (immediateScheduled.compareAndSet(false, true)) {
      scheduler.schedule(
          () -> executor.execute(this::processImmediateQueue), 5, TimeUnit.MILLISECONDS);
    }
  }

  private void processImmediateQueue() {
    immediateScheduled.set(false);

    // Drain queue
    List<SampledItem> items = new ArrayList<>();
    SampledItem item;
    while ((item = immediateQueue.poll()) != null) {
      items.add(item);
    }

    if (items.isEmpty()) {
      return;
    }

    // Filter to active items
    List<SampledItem> activeItems = items.stream().filter(SampledItem::isActive).toList();

    if (activeItems.isEmpty()) {
      return;
    }

    // Create a temporary group for sampleCallback (interval=0 indicates immediate/one-shot)
    SamplingGroup tempGroup = new SamplingGroup(activeItems);

    // Step 1: External sampling - use the blocking channel to ensure UaNodes are updated before
    // step 2
    if (sampleCallback != null) {
      sampleCallback.accept(tempGroup, blockingValueChannel);
    }

    // Step 2: Internal sampling - read from UaNodes, notify DataItems
    for (SampledItem si : activeItems) {
      DataValue value = readFromNode(si);
      setDataItemValue(si, value);
    }
  }

  private DataValue readFromNode(SampledItem item) {
    UaServerNode node = nodeManager.get(item.getNodeId());

    if (node == null) {
      return new DataValue(StatusCodes.Bad_NodeIdUnknown);
    }

    AttributeId attributeId = item.getAttributeId();
    if (attributeId == null) {
      return new DataValue(StatusCodes.Bad_AttributeIdInvalid);
    }

    return AttributeReader.readAttribute(
        AccessContext.INTERNAL, node, attributeId, null, item.getIndexRange(), null);
  }

  private void setDataItemValue(SampledItem item, DataValue value) {
    DataItem dataItem = item.dataItem();
    TimestampsToReturn timestamps = dataItem.getTimestampsToReturn();

    if (timestamps != null) {
      UInteger attributeId = dataItem.getReadValueId().getAttributeId();
      value =
          (AttributeId.Value.isEqual(attributeId))
              ? DataValue.derivedValue(value, timestamps)
              : DataValue.derivedNonValue(value, timestamps);
    }

    dataItem.setValue(value);
  }
}
