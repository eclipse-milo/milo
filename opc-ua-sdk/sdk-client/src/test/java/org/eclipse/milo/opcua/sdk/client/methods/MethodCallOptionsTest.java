/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

class MethodCallOptionsTest {

  // The timeout becomes a UInt32 TimeoutHint in milliseconds, so the builder must reject what the
  // header cannot carry instead of letting the conversion fail later on the calling thread.
  @Test
  void timeoutIsBoundedByWhatTheRequestHeaderCanCarry() {
    Duration max = Duration.ofMillis(UInteger.MAX_VALUE);

    assertEquals(Optional.of(max), MethodCallOptions.builder().timeout(max).build().timeout());
    assertEquals(
        Optional.of(Duration.ZERO),
        MethodCallOptions.builder().timeout(Duration.ZERO).build().timeout());

    assertThrows(
        IllegalArgumentException.class,
        () -> MethodCallOptions.builder().timeout(max.plusMillis(1)));
    assertThrows(
        IllegalArgumentException.class,
        () -> MethodCallOptions.builder().timeout(Duration.ofSeconds(Long.MAX_VALUE)));
    assertThrows(
        IllegalArgumentException.class,
        () -> MethodCallOptions.builder().timeout(Duration.ofMillis(-1)));
  }

  @Test
  void unsetValuesAreEmpty() {
    assertTrue(MethodCallOptions.DEFAULT.timeout().isEmpty());
    assertTrue(MethodCallOptions.DEFAULT.returnDiagnostics().isEmpty());
    assertEquals(MethodCallOptions.DEFAULT, MethodCallOptions.builder().build());
  }
}
