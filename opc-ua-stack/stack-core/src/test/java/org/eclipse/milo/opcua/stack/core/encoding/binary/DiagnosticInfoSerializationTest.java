/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DiagnosticInfoSerializationTest extends BinarySerializationFixture {

  public static Object[][] getDiagnosticInfos() {
    return new Object[][] {
      {null},
      {new DiagnosticInfo(1, -1, -1, -1, null, null, null)},
      {new DiagnosticInfo(-1, 1, -1, -1, null, null, null)},
      {new DiagnosticInfo(-1, -1, 1, -1, null, null, null)},
      {new DiagnosticInfo(-1, -1, -1, 1, null, null, null)},
      {new DiagnosticInfo(-1, -1, -1, -1, "hello, world", null, null)},
      {new DiagnosticInfo(-1, -1, -1, -1, null, StatusCode.GOOD, null)},
      {
        new DiagnosticInfo(
            -1,
            -1,
            -1,
            -1,
            null,
            null,
            new DiagnosticInfo(1, 2, 3, 4, "abc", StatusCode.GOOD, null))
      },
      {new DiagnosticInfo(1, 2, 3, 4, "abc", StatusCode.GOOD, null)},
    };
  }

  @ParameterizedTest
  @MethodSource("getDiagnosticInfos")
  public void testDiagnosticInfoRoundTrip(DiagnosticInfo diagnosticInfo) {
    writer.encodeDiagnosticInfo(diagnosticInfo);
    DiagnosticInfo decoded = reader.decodeDiagnosticInfo();

    assertEquals(diagnosticInfo, decoded);
  }
}
