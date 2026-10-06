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

import com.google.common.math.DoubleMath;
import com.google.common.math.LongMath;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.concurrent.Executor;
import org.jspecify.annotations.Nullable;

/**
 * How a {@link SamplingManager} buckets intervals, floors them, debounces initial samples,
 * refreshes read access, and where its groups run.
 *
 * <p>Start from {@link #defaults()} and change what you need:
 *
 * <pre>{@code
 * SamplingManagerConfig config =
 *     SamplingManagerConfig.defaults()
 *         .withMinimumIntervalMillis(100)
 *         .withReadAccessPolicy(ReadAccessPolicy.cached());
 * }</pre>
 *
 * @param bucketMillis the bucket size: the sampling intervals the framework supports are the
 *     multiples of it, and a requested interval is revised up to the next one (Part 4 §7.21: the
 *     revised interval is equal to or higher than the requested one), so items asking for 100 ms
 *     and 120 ms sample at 100 ms and 125 ms with the default bucket size, and nothing samples
 *     faster than the interval it was given. Zero disables bucketing, and intervals are revised up
 *     to whole milliseconds only.
 * @param minimumIntervalMillis the fastest interval the framework samples at, applied before
 *     bucketing, so the fastest interval it supports is the first multiple of the bucket at or
 *     above this. A requested interval of zero, which asks for the fastest practical rate, is
 *     revised to that interval rather than becoming a 1 ms poll.
 * @param initialSampleDelayMillis how long a new item waits for more new items before they are all
 *     sampled once, ahead of their group's next cycle.
 * @param initialSampleMaxWindowMillis the longest a steady stream of new items can postpone that
 *     initial sample.
 * @param overrunWarningMultiple a cycle that has not completed after this many intervals is logged,
 *     since the group cannot sample again until it does.
 * @param readAccessPolicy how each group refreshes its items' read access results before sampling.
 * @param executor the executor each group runs its refreshes and samples on, or {@code null} for
 *     the server's executor. Timers stay on the server's scheduled executor. The manager never
 *     shuts this executor down.
 */
public record SamplingManagerConfig(
    long bucketMillis,
    long minimumIntervalMillis,
    long initialSampleDelayMillis,
    long initialSampleMaxWindowMillis,
    long overrunWarningMultiple,
    ReadAccessPolicy readAccessPolicy,
    @Nullable Executor executor) {

  public SamplingManagerConfig {
    if (bucketMillis < 0) {
      throw new IllegalArgumentException("bucketMillis < 0: " + bucketMillis);
    }
    if (minimumIntervalMillis < 1) {
      throw new IllegalArgumentException("minimumIntervalMillis < 1: " + minimumIntervalMillis);
    }
    if (initialSampleDelayMillis < 0) {
      throw new IllegalArgumentException(
          "initialSampleDelayMillis < 0: " + initialSampleDelayMillis);
    }
    if (initialSampleMaxWindowMillis < initialSampleDelayMillis) {
      throw new IllegalArgumentException(
          "initialSampleMaxWindowMillis < initialSampleDelayMillis: "
              + initialSampleMaxWindowMillis);
    }
    if (overrunWarningMultiple < 1) {
      throw new IllegalArgumentException("overrunWarningMultiple < 1: " + overrunWarningMultiple);
    }
    Objects.requireNonNull(readAccessPolicy, "readAccessPolicy");
  }

  /**
   * The defaults: 25 ms buckets and a 1 ms minimum interval, so the fastest interval supported is
   * 25 ms; an initial sample 100 ms after the first new item and at most 500 ms after it; a warning
   * after 3 overrun intervals; {@link ReadAccessPolicy#perCycle()}; and the server's executor.
   *
   * @return the default configuration.
   */
  public static SamplingManagerConfig defaults() {
    return new SamplingManagerConfig(25, 1, 100, 500, 3, ReadAccessPolicy.perCycle(), null);
  }

  public SamplingManagerConfig withBucketMillis(long bucketMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  public SamplingManagerConfig withMinimumIntervalMillis(long minimumIntervalMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  public SamplingManagerConfig withInitialSampleDelayMillis(long initialSampleDelayMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  public SamplingManagerConfig withInitialSampleMaxWindowMillis(long initialSampleMaxWindowMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  public SamplingManagerConfig withOverrunWarningMultiple(long overrunWarningMultiple) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  public SamplingManagerConfig withReadAccessPolicy(ReadAccessPolicy readAccessPolicy) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  /**
   * Run each group's refreshes and samples on {@code executor} instead of the server's executor.
   *
   * <p>A sampler that blocks in its reads can keep that blocking off the server's shared executor.
   * On Java 21 or later, for example, each turn can run on a virtual thread:
   *
   * <pre>{@code
   * SamplingManagerConfig config =
   *     SamplingManagerConfig.defaults()
   *         .withExecutor(Executors.newVirtualThreadPerTaskExecutor());
   * }</pre>
   *
   * <p>A group runs one turn at a time whatever the executor, so it need not be serial or bounded.
   * The caller owns the executor and shuts it down, if it needs to, after the manager has shut
   * down.
   *
   * @param executor the executor to sample on, or {@code null} for the server's executor.
   * @return a copy of this configuration with {@code executor}.
   */
  public SamplingManagerConfig withExecutor(@Nullable Executor executor) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy,
        executor);
  }

  /**
   * The interval a group samples an item at: the item's sampling interval rounded up to a whole
   * millisecond, raised to {@link #minimumIntervalMillis()} if below it, then up to the next
   * multiple of {@link #bucketMillis()}. Never faster than the interval given, and the result of
   * applying it again is the same, so the interval reported to the client is the one sampled at.
   *
   * @param samplingIntervalMillis an item's sampling interval, as {@link
   *     org.eclipse.milo.opcua.sdk.server.items.DataItem#getSamplingInterval()} reports it.
   * @return the interval of the group the item belongs in.
   */
  public long groupIntervalMillis(double samplingIntervalMillis) {
    long millis =
        samplingIntervalMillis > 0 && Double.isFinite(samplingIntervalMillis)
            ? DoubleMath.roundToLong(samplingIntervalMillis, RoundingMode.UP)
            : 0;

    millis = Math.max(minimumIntervalMillis, millis);

    if (bucketMillis > 0) {
      millis = LongMath.divide(millis, bucketMillis, RoundingMode.CEILING) * bucketMillis;
    }

    return millis;
  }

  /**
   * The revised sampling interval to report for a requested one: the interval the framework will
   * actually sample at, from {@link #groupIntervalMillis(double)}.
   *
   * <p>{@link org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace} returns this from {@code
   * onCreateDataItem} and {@code onModifyDataItem}, so that the client is told the interval its
   * item is sampled at (Part 4 §7.21). An AddressSpace that wires a {@link SamplingManager} itself
   * should do the same.
   *
   * @param requestedSamplingInterval the requested sampling interval, after the server's limits and
   *     the Node's MinimumSamplingInterval have been applied.
   * @return the revised sampling interval, in milliseconds.
   */
  public double reviseSamplingInterval(double requestedSamplingInterval) {
    return (double) groupIntervalMillis(requestedSamplingInterval);
  }
}
