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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** How {@link SamplingManagerConfig} turns a revised sampling interval into a group interval. */
class SamplingManagerConfigTest {

  /**
   * Part 4 §7.21: the revised interval is equal to or higher than the requested one. Bucketing
   * rounds up to the next supported interval and never down; the floor applies first, so a zero
   * interval, which asks for the fastest practical rate, becomes the fastest supported interval
   * rather than a 1 ms poll. Applying the function to its own result changes nothing, so the
   * interval the client is told is the one the manager groups the item under.
   */
  @ParameterizedTest(name = "{0} ms with bucket {1} and floor {2} samples at {3} ms")
  @MethodSource("groupIntervals")
  void groupIntervalBucketsAndFloors(
      double samplingInterval, long bucketMillis, long minimumIntervalMillis, long expected) {

    SamplingManagerConfig config =
        SamplingManagerConfig.defaults()
            .withBucketMillis(bucketMillis)
            .withMinimumIntervalMillis(minimumIntervalMillis);

    assertEquals(expected, config.groupIntervalMillis(samplingInterval));
    assertEquals(expected, config.groupIntervalMillis(expected), "idempotent");
  }

  private static Stream<Arguments> groupIntervals() {
    return Stream.of(
        Arguments.of(100.0, 50, 1, 100),
        Arguments.of(100.4, 50, 1, 150),
        Arguments.of(149.0, 50, 1, 150),
        Arguments.of(150.0, 50, 1, 150),
        Arguments.of(20.0, 50, 1, 50),
        Arguments.of(0.0, 50, 1, 50),
        Arguments.of(-1.0, 50, 1, 50),
        Arguments.of(0.0, 50, 200, 200),
        Arguments.of(0.0, 50, 120, 150),
        Arguments.of(100.4, 0, 1, 101),
        Arguments.of(0.0, 0, 1, 1),
        Arguments.of(Double.NaN, 50, 1, 50));
  }

  // What the client is told is what the framework samples at.
  @Test
  void theRevisedIntervalIsTheGroupInterval() {
    SamplingManagerConfig config = SamplingManagerConfig.defaults().withMinimumIntervalMillis(100);

    assertEquals(125.0, config.reviseSamplingInterval(120.0));
    assertEquals(100.0, config.reviseSamplingInterval(0.0));
    assertEquals(1000.0, config.reviseSamplingInterval(1000.0));
  }

  @Test
  void invalidValuesAreRejected() {
    SamplingManagerConfig defaults = SamplingManagerConfig.defaults();

    assertThrows(IllegalArgumentException.class, () -> defaults.withBucketMillis(-1));
    assertThrows(IllegalArgumentException.class, () -> defaults.withMinimumIntervalMillis(0));
    assertThrows(IllegalArgumentException.class, () -> defaults.withInitialSampleDelayMillis(-1));
    assertThrows(
        IllegalArgumentException.class, () -> defaults.withInitialSampleMaxWindowMillis(50));
    assertThrows(IllegalArgumentException.class, () -> defaults.withOverrunWarningMultiple(0));
    //noinspection DataFlowIssue
    assertThrows(NullPointerException.class, () -> defaults.withReadAccessPolicy(null));
  }
}
