/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OperationLimitsOverrideTest {

  static Stream<Arguments> resolveCases() {
    return Stream.of(
        Arguments.of("no override keeps an absent limit absent", null, null, null),
        Arguments.of("no override keeps the advertised limit", uint(100), null, uint(100)),
        Arguments.of("no override keeps an advertised 0", uint(0), null, uint(0)),
        Arguments.of("override of 0 keeps the advertised limit", uint(100), uint(0), uint(100)),
        Arguments.of("override supplies an absent limit", null, uint(50), uint(50)),
        // Part 5 §6.3.11: an advertised 0 means "no limit", so it cannot undercut an override.
        Arguments.of("override replaces an advertised 0", uint(0), uint(50), uint(50)),
        Arguments.of("lower override clamps the advertised limit", uint(100), uint(50), uint(50)),
        // An override can only tighten a limit; a server enforcing 100 would reject 500.
        Arguments.of("higher override does not raise the limit", uint(100), uint(500), uint(100)),
        Arguments.of(
            "comparison is unsigned above Integer.MAX_VALUE",
            UInteger.MAX,
            uint(Integer.MAX_VALUE + 1L),
            uint(Integer.MAX_VALUE + 1L)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("resolveCases")
  void resolveCombinesAdvertisedLimitAndOverride(
      String description,
      @Nullable UInteger advertised,
      @Nullable UInteger override,
      @Nullable UInteger expected) {

    assertEquals(expected, OperationLimits.resolve(advertised, override));
  }

  // Each override applies to its own limit only. A misrouted lookup would silently change the
  // partition size of an unrelated service.
  @Test
  void withOverridesAppliesEachOverrideToItsOwnLimit() {
    var advertised =
        new OperationLimits(
            Map.of(
                OperationLimit.MaxNodesPerRead, uint(1000),
                OperationLimit.MaxNodesPerBrowse, uint(1000),
                OperationLimit.MaxMonitoredItemsPerCall, uint(1000)));

    Function<OperationLimit, Optional<UInteger>> overrides =
        limit ->
            switch (limit) {
              case MaxNodesPerRead -> Optional.of(uint(10));
              case MaxNodesPerWrite -> Optional.of(uint(20));
              default -> Optional.empty();
            };

    OperationLimits effective = advertised.withOverrides(overrides);

    assertEquals(uint(10), effective.maxNodesPerRead().orElseThrow());
    assertEquals(uint(20), effective.maxNodesPerWrite().orElseThrow());
    assertEquals(uint(1000), effective.maxNodesPerBrowse().orElseThrow());
    assertEquals(uint(1000), effective.maxMonitoredItemsPerCall().orElseThrow());
    assertTrue(effective.maxNodesPerMethodCall().isEmpty());
  }

  // Applications derive modified configs with OpcUaClientConfig.copy; losing the overrides there
  // would silently restore the server's unreliable limits.
  @Test
  void copyPreservesOperationLimitOverrides() {
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setOperationLimitOverrides(Map.of(OperationLimit.MaxNodesPerRead, uint(10)))
            .build();

    OpcUaClientConfig copy = OpcUaClientConfig.copy(config, builder -> {});

    Function<OperationLimit, Optional<UInteger>> overrides =
        copy.getOperationLimitOverrides().orElseThrow();
    assertEquals(Optional.of(uint(10)), overrides.apply(OperationLimit.MaxNodesPerRead));
    assertEquals(Optional.empty(), overrides.apply(OperationLimit.MaxNodesPerBrowse));
  }
}
