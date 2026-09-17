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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReturnDiagnosticsTest {

  // OPC 10000-4, Table 178 assigns each ReturnDiagnostics flag a fixed bit; a wrong constant would
  // silently request the wrong diagnostics from every server.
  @ParameterizedTest
  @MethodSource("flags")
  void flagsMatchPart4Bits(int flag, int expectedBit) {
    assertEquals(expectedBit, flag);
  }

  static Stream<Arguments> flags() {
    return Stream.of(
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_SYMBOLIC_ID, 0x0001),
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_LOCALIZED_TEXT, 0x0002),
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_ADDITIONAL_INFO, 0x0004),
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_INNER_STATUS_CODE, 0x0008),
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_INNER_DIAGNOSTICS, 0x0010),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_SYMBOLIC_ID, 0x0020),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_LOCALIZED_TEXT, 0x0040),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_ADDITIONAL_INFO, 0x0080),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_INNER_STATUS_CODE, 0x0100),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_INNER_DIAGNOSTICS, 0x0200),
        Arguments.of(ReturnDiagnostics.SERVICE_LEVEL_ALL, 0x001F),
        Arguments.of(ReturnDiagnostics.OPERATION_LEVEL_ALL, 0x03E0));
  }

  @Test
  void combinedFlagsRoundTripThroughTheRequestHeaderMask() {
    ReturnDiagnostics diagnostics =
        ReturnDiagnostics.of(
            ReturnDiagnostics.OPERATION_LEVEL_ADDITIONAL_INFO,
            ReturnDiagnostics.OPERATION_LEVEL_LOCALIZED_TEXT);

    assertEquals(uint(0x00C0), diagnostics.toUInteger());
    assertEquals(diagnostics, ReturnDiagnostics.from(diagnostics.toUInteger()));
    assertTrue(diagnostics.includes(ReturnDiagnostics.OPERATION_LEVEL_ADDITIONAL_INFO));
    assertTrue(diagnostics.includesOperationLevel());
    assertFalse(diagnostics.includesServiceLevel());
    assertFalse(
        diagnostics.includes(ReturnDiagnostics.OPERATION_LEVEL_ALL),
        "includes requires every bit of the queried combination");
  }

  // A request header carries a null mask when the encoder saw no value; that means nothing
  // requested, not a decoding problem.
  @Test
  void nullAndZeroMasksAreNone() {
    assertSame(ReturnDiagnostics.NONE, ReturnDiagnostics.from(null));
    assertSame(ReturnDiagnostics.NONE, ReturnDiagnostics.from(uint(0)));
    assertSame(ReturnDiagnostics.NONE, ReturnDiagnostics.of());
    assertEquals(uint(0), ReturnDiagnostics.NONE.toUInteger());
  }
}
