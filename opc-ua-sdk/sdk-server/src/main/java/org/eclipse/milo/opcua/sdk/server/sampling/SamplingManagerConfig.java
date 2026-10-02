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

/**
 * How a {@link SamplingManager} buckets intervals, floors them, debounces initial samples, and
 * refreshes read access.
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
 *     and 120 ms sample at 100 ms and 150 ms, and nothing samples faster than the interval it was
 *     given. Zero disables bucketing, and intervals are revised up to whole milliseconds only.
 * @param minimumIntervalMillis the fastest interval the framework samples at, applied after
 *     bucketing. A requested interval of zero, which asks for the fastest practical rate, is
 *     revised to this rather than becoming a 1 ms poll.
 * @param initialSampleDelayMillis how long a new item waits for more new items before they are all
 *     sampled once, ahead of their group's next cycle.
 * @param initialSampleMaxWindowMillis the longest a steady stream of new items can postpone that
 *     initial sample.
 * @param overrunWarningMultiple a cycle that has not completed after this many intervals is logged,
 *     since the group cannot sample again until it does.
 * @param readAccessPolicy how each group refreshes its items' read access results before sampling.
 */
public record SamplingManagerConfig(
    long bucketMillis,
    long minimumIntervalMillis,
    long initialSampleDelayMillis,
    long initialSampleMaxWindowMillis,
    long overrunWarningMultiple,
    ReadAccessPolicy readAccessPolicy) {

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
    if (readAccessPolicy == null) {
      throw new NullPointerException("readAccessPolicy");
    }
  }

  /**
   * The defaults: 50 ms buckets, a 1 ms minimum interval, an initial sample 100 ms after the first
   * new item and at most 500 ms after it, a warning after 3 overrun intervals, and {@link
   * ReadAccessPolicy#perCycle()}.
   *
   * @return the default configuration.
   */
  public static SamplingManagerConfig defaults() {
    return new SamplingManagerConfig(50, 1, 100, 500, 3, ReadAccessPolicy.perCycle());
  }

  public SamplingManagerConfig withBucketMillis(long bucketMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  public SamplingManagerConfig withMinimumIntervalMillis(long minimumIntervalMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  public SamplingManagerConfig withInitialSampleDelayMillis(long initialSampleDelayMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  public SamplingManagerConfig withInitialSampleMaxWindowMillis(long initialSampleMaxWindowMillis) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  public SamplingManagerConfig withOverrunWarningMultiple(long overrunWarningMultiple) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  public SamplingManagerConfig withReadAccessPolicy(ReadAccessPolicy readAccessPolicy) {
    return new SamplingManagerConfig(
        bucketMillis,
        minimumIntervalMillis,
        initialSampleDelayMillis,
        initialSampleMaxWindowMillis,
        overrunWarningMultiple,
        readAccessPolicy);
  }

  /**
   * The interval a group samples an item at: the item's sampling interval rounded up to a whole
   * millisecond, then up to the next multiple of {@link #bucketMillis()}, then floored at {@link
   * #minimumIntervalMillis()}. Never faster than the interval given.
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

    if (bucketMillis > 0) {
      millis = LongMath.divide(millis, bucketMillis, RoundingMode.CEILING) * bucketMillis;
    }

    return Math.max(minimumIntervalMillis, millis);
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
