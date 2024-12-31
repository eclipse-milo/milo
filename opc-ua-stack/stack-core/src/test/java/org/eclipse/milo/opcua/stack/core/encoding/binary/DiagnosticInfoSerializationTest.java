/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.testng.Assert.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DiagnosticInfoSerializationTest extends BinarySerializationFixture {

  @DataProvider(name = "DiagnosticInfoProvider")
  public Object[][] getDiagnosticInfos() {
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

  @Test(dataProvider = "DiagnosticInfoProvider")
  public void testDiagnosticInfoRoundTrip(DiagnosticInfo diagnosticInfo) {
    writer.encodeDiagnosticInfo(diagnosticInfo);
    DiagnosticInfo decoded = reader.decodeDiagnosticInfo();

    assertEquals(decoded, diagnosticInfo);
  }
}
