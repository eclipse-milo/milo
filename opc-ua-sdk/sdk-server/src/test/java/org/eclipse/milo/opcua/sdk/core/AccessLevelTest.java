/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AccessLevelTest {

  @Test
  public void testToValue() {
    for (AccessLevel accessLevel : AccessLevel.values()) {
      assertEquals(ubyte(accessLevel.getValue()), AccessLevel.toValue(accessLevel));
    }

    assertEquals(ubyte(0), AccessLevel.toValue(AccessLevel.NONE));

    assertEquals(
        AccessLevel.CurrentRead.getValue(), AccessLevel.toValue(AccessLevel.READ_ONLY).intValue());

    assertEquals(
        AccessLevel.CurrentRead.getValue() | AccessLevel.CurrentWrite.getValue(),
        AccessLevel.toValue(AccessLevel.READ_WRITE).intValue());

    assertEquals(
        AccessLevel.HistoryRead.getValue(),
        AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY).intValue());

    assertEquals(
        AccessLevel.HistoryRead.getValue() | AccessLevel.HistoryWrite.getValue(),
        AccessLevel.toValue(AccessLevel.HISTORY_READ_WRITE).intValue());
  }

  @Test
  public void testFromValue() {
    assertEquals(AccessLevel.NONE, AccessLevel.fromValue(0));

    assertEquals(AccessLevel.READ_ONLY, AccessLevel.fromValue(AccessLevel.CurrentRead.getValue()));

    assertEquals(
        AccessLevel.READ_WRITE,
        AccessLevel.fromValue(
            AccessLevel.CurrentRead.getValue() | AccessLevel.CurrentWrite.getValue()));

    assertEquals(
        AccessLevel.HISTORY_READ_ONLY, AccessLevel.fromValue(AccessLevel.HistoryRead.getValue()));

    assertEquals(
        AccessLevel.HISTORY_READ_WRITE,
        AccessLevel.fromValue(
            AccessLevel.HistoryRead.getValue() | AccessLevel.HistoryWrite.getValue()));
  }
}
