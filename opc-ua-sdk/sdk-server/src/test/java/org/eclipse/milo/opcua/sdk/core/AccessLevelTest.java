/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AccessLevelTest {

  @Test
  public void testToValue() {
    for (AccessLevel accessLevel : AccessLevel.values()) {
      assertEquals(AccessLevel.toValue(accessLevel), ubyte(accessLevel.getValue()));
    }

    assertEquals(AccessLevel.toValue(AccessLevel.NONE), ubyte(0));

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY).intValue(), AccessLevel.CurrentRead.getValue());

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE).intValue(),
        AccessLevel.CurrentRead.getValue() | AccessLevel.CurrentWrite.getValue());

    assertEquals(
        AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY).intValue(),
        AccessLevel.HistoryRead.getValue());

    assertEquals(
        AccessLevel.toValue(AccessLevel.HISTORY_READ_WRITE).intValue(),
        AccessLevel.HistoryRead.getValue() | AccessLevel.HistoryWrite.getValue());
  }

  @Test
  public void testFromValue() {
    assertEquals(AccessLevel.fromValue(0), AccessLevel.NONE);

    assertEquals(AccessLevel.fromValue(AccessLevel.CurrentRead.getValue()), AccessLevel.READ_ONLY);

    assertEquals(
        AccessLevel.fromValue(
            AccessLevel.CurrentRead.getValue() | AccessLevel.CurrentWrite.getValue()),
        AccessLevel.READ_WRITE);

    assertEquals(
        AccessLevel.fromValue(AccessLevel.HistoryRead.getValue()), AccessLevel.HISTORY_READ_ONLY);

    assertEquals(
        AccessLevel.fromValue(
            AccessLevel.HistoryRead.getValue() | AccessLevel.HistoryWrite.getValue()),
        AccessLevel.HISTORY_READ_WRITE);
  }
}
