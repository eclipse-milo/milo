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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * Manages items at a specific bucketed sampling interval.
 *
 * <p>Provides scheduling infrastructure for periodic sampling execution and tracks which items
 * should be sampled.
 */
public class SamplingGroup {

  private final double samplingInterval;
  private final Set<SampledItem> items = new CopyOnWriteArraySet<>();
  private final AtomicBoolean dirty = new AtomicBoolean(true);
  private volatile List<SampledItem> activeItems = List.of();
  private volatile boolean shutdown = false;

  private final ExecutorService executor;
  private final ScheduledExecutorService scheduler;
  private final BiConsumer<SamplingGroup, SampleSink> sampleCallback;
  private final SampleSink sampleSink;

  /**
   * Create a new SamplingGroup.
   *
   * @param samplingInterval the sampling interval in milliseconds (0.0 indicates immediate/one-shot
   *     sampling).
   * @param executor the executor for running sampling tasks.
   * @param scheduler the scheduler for timing between executions.
   * @param sampleCallback the callback to invoke during sampling (receives this group and a
   *     SampleSink).
   * @param sampleSink the SampleSink to pass to the sample callback.
   */
  public SamplingGroup(
      double samplingInterval,
      ExecutorService executor,
      ScheduledExecutorService scheduler,
      BiConsumer<SamplingGroup, SampleSink> sampleCallback,
      SampleSink sampleSink) {
    this.samplingInterval = samplingInterval;
    this.executor = executor;
    this.scheduler = scheduler;
    this.sampleCallback = sampleCallback;
    this.sampleSink = sampleSink;
  }

  /**
   * Create a temporary SamplingGroup for immediate/one-shot sampling.
   *
   * <p>This constructor is used for immediate sampling of newly added items. The group has interval
   * 0.0 and does not schedule recurring executions.
   *
   * @param items the items to include in this temporary group.
   */
  public SamplingGroup(Collection<SampledItem> items) {
    this.samplingInterval = 0.0;
    this.executor = null;
    this.scheduler = null;
    this.sampleCallback = null;
    this.sampleSink = null;
    this.items.addAll(items);
    recomputeActiveItems();
  }

  // === User-facing API ===

  /**
   * Get the bucketed sampling interval in milliseconds.
   *
   * <p>A value of 0.0 indicates immediate/one-shot sampling.
   *
   * @return the sampling interval in milliseconds.
   */
  public double getSamplingInterval() {
    return samplingInterval;
  }

  /**
   * Get the currently active items (filtered to mode != Disabled).
   *
   * <p>The returned collection is a snapshot and is safe to iterate while the group may be
   * modified.
   *
   * @return the active items.
   */
  public Collection<SampledItem> getActiveItems() {
    return activeItems;
  }

  /**
   * Check if the group is dirty (items have changed since the last sampling execution).
   *
   * <p>This can be used by implementations to cache and reuse optimization work.
   *
   * @return true if items have changed since the last sampleItems() call.
   */
  public boolean isDirty() {
    return dirty.get();
  }

  // === Framework internal ===

  /**
   * Add items to this group.
   *
   * @param items the items to add.
   */
  void addItems(Collection<SampledItem> items) {
    this.items.addAll(items);
    markDirty();
  }

  /**
   * Remove items from this group.
   *
   * @param items the items to remove.
   */
  void removeItems(Collection<SampledItem> items) {
    this.items.removeAll(items);
    markDirty();
  }

  /** Mark the group as dirty, indicating items have changed. */
  void markDirty() {
    dirty.set(true);
  }

  /**
   * Check if this group has no items.
   *
   * @return true if the group is empty.
   */
  boolean isEmpty() {
    return items.isEmpty();
  }

  /** Start the sampling schedule for this group. */
  void start() {
    if (samplingInterval > 0 && executor != null) {
      executor.execute(this::executeSamplingCycle);
    }
  }

  /** Shutdown this group, stopping any scheduled executions. */
  void shutdown() {
    shutdown = true;
  }

  private void executeSamplingCycle() {
    if (shutdown) {
      return;
    }

    long startNanos = System.nanoTime();

    // Step 1: If dirty, recompute active items
    if (dirty.compareAndSet(true, false)) {
      recomputeActiveItems();
    }

    // Step 2: Call external sampling callback
    if (sampleCallback != null && sampleSink != null) {
      sampleCallback.accept(this, sampleSink);
    }

    // Step 3: Reschedule
    if (!shutdown && samplingInterval > 0 && scheduler != null && executor != null) {
      long elapsedNanos = System.nanoTime() - startNanos;
      long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
      long delay = Math.max(0, (long) samplingInterval - elapsedMillis);

      if (delay == 0) {
        executor.execute(this::executeSamplingCycle);
      } else {
        scheduler.schedule(
            () -> executor.execute(this::executeSamplingCycle), delay, TimeUnit.MILLISECONDS);
      }
    }
  }

  private void recomputeActiveItems() {
    activeItems = items.stream().filter(SampledItem::isActive).toList();
  }
}
