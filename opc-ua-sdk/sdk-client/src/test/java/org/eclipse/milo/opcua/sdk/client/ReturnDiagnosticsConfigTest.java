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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Field;
import org.junit.jupiter.api.Test;

class ReturnDiagnosticsConfigTest {

  // Existing clients must keep sending returnDiagnostics 0 unless they opt in.
  @Test
  void defaultRequestsNoDiagnostics() {
    OpcUaClientConfig config = OpcUaClientConfig.builder().build();

    assertSame(ReturnDiagnostics.NONE, config.getReturnDiagnostics());
  }

  @Test
  void copyKeepsTheConfiguredDiagnostics() {
    ReturnDiagnostics requested = ReturnDiagnostics.of(Field.SYMBOLIC_ID, Field.LOCALIZED_TEXT);

    OpcUaClientConfig config = OpcUaClientConfig.builder().setReturnDiagnostics(requested).build();
    OpcUaClientConfig copy = OpcUaClientConfig.copy(config, builder -> {});

    assertEquals(requested, copy.getReturnDiagnostics());
  }
}
