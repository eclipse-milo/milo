/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.Stack.ConnectionLimits;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ConnectionLimitsTest {

  private static final String NAME = "milo.test.limit";

  @ParameterizedTest
  @CsvSource({"1, 1", "500, 500", "' 500 ', 500", "2147483647, 2147483647"})
  void positiveIntPropertyUsesParsedValue(String value, int expected) {
    assertEquals(expected, ConnectionLimits.parsePositiveInt(NAME, value, 100));
  }

  // Zero or a negative limit would reject every non-loopback connection: no attempt count is ever
  // below maxAttempts=0, and no connection total is ever below maxConnections=0.
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"0", "-1", "abc", "1.5", "0x10", "2147483648"})
  void positiveIntPropertyFallsBackToDefault(String value) {
    assertEquals(100, ConnectionLimits.parsePositiveInt(NAME, value, 100));
  }

  @ParameterizedTest
  @CsvSource({"true, true", "TRUE, true", "false, false", "False, false", "' false ', false"})
  void booleanPropertyUsesParsedValue(String value, boolean expected) {
    assertEquals(expected, ConnectionLimits.parseBoolean(NAME, value, !expected));
  }

  // Boolean.parseBoolean treats anything but "true" as false, so a typo would silently turn off
  // rate limiting. An unrecognized value must keep the default instead.
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"flase", "yes", "no", "1", "0"})
  void booleanPropertyFallsBackToDefault(String value) {
    assertTrue(ConnectionLimits.parseBoolean(NAME, value, true));
    assertFalse(ConnectionLimits.parseBoolean(NAME, value, false));
  }
}
