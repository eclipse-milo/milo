/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReverseConnectTargetTest {

  @Test
  void builderRejectsNullId() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> ReverseConnectTarget.builder().setId(null));

    assertEquals("id", exception.getMessage());
  }
}
