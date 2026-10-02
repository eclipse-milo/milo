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
 * @param bucketMillis the bucket size items are grouped by. An item whose revised sampling interval
 *     is at least one bucket samples at the largest multiple of the bucket that does not exceed its
 *     interval, so items at 100 ms and 100.4 ms share one group and nothing samples slower than the
 *     client was told. An interval below one bucket is kept as it is. Zero disables bucketing.
 * @param minimumIntervalMillis the slowest interval a group may be given, applied after bucketing.
 *     A revised interval of zero, which asks for the fastest sampling the server supports, becomes
 *     this rather than a 1 ms poll.
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
   * The interval a group samples an item at: the item's revised sampling interval rounded up to a
   * whole millisecond, bucketed, and floored at {@link #minimumIntervalMillis()}.
   *
   * @param samplingIntervalMillis an item's revised sampling interval, as {@link
   *     org.eclipse.milo.opcua.sdk.server.items.DataItem#getSamplingInterval()} reports it.
   * @return the interval of the group the item belongs in.
   */
  public long groupIntervalMillis(double samplingIntervalMillis) {
    long millis =
        samplingIntervalMillis > 0 && Double.isFinite(samplingIntervalMillis)
            ? DoubleMath.roundToLong(samplingIntervalMillis, RoundingMode.UP)
            : 0;

    if (bucketMillis > 0 && millis >= bucketMillis) {
      millis = (millis / bucketMillis) * bucketMillis;
    }

    return Math.max(minimumIntervalMillis, millis);
  }
}
