/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReverseConnectConfigBuilderTest {

  @Test
  void setConnectIntervalRejectsNull() {
    NullPointerException exception =
        assertThrows(
            NullPointerException.class,
            () -> ReverseConnectConfig.newBuilder().setConnectInterval(null));

    assertEquals("connectInterval", exception.getMessage());
  }

  @Test
  void setConnectTimeoutRejectsNull() {
    NullPointerException exception =
        assertThrows(
            NullPointerException.class,
            () -> ReverseConnectConfig.newBuilder().setConnectTimeout(null));

    assertEquals("connectTimeout", exception.getMessage());
  }

  @Test
  void setRejectBackoffRejectsNull() {
    NullPointerException exception =
        assertThrows(
            NullPointerException.class,
            () -> ReverseConnectConfig.newBuilder().setRejectBackoff(null));

    assertEquals("rejectBackoff", exception.getMessage());
  }

  @Test
  void setMaxReconnectDelayRejectsNull() {
    NullPointerException exception =
        assertThrows(
            NullPointerException.class,
            () -> ReverseConnectConfig.newBuilder().setMaxReconnectDelay(null));

    assertEquals("maxReconnectDelay", exception.getMessage());
  }
}
