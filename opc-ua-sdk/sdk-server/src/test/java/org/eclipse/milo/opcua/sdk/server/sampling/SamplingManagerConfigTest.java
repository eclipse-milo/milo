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
   * Bucketing collapses near intervals into one group without ever sampling slower than the revised
   * interval the client was told; an interval below one bucket is kept; the floor applies last, so
   * a zero interval becomes the floor rather than a 1 ms poll.
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
  }

  private static Stream<Arguments> groupIntervals() {
    return Stream.of(
        Arguments.of(100.0, 50, 1, 100),
        Arguments.of(100.4, 50, 1, 100),
        Arguments.of(149.0, 50, 1, 100),
        Arguments.of(150.0, 50, 1, 150),
        Arguments.of(20.0, 50, 1, 20),
        Arguments.of(0.0, 50, 1, 1),
        Arguments.of(-1.0, 50, 1, 1),
        Arguments.of(0.0, 50, 200, 200),
        Arguments.of(100.4, 0, 1, 101),
        Arguments.of(Double.NaN, 50, 1, 1));
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
    assertThrows(NullPointerException.class, () -> defaults.withReadAccessPolicy(null));
  }
}
