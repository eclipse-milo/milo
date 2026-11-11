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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBufUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DiagnosticInfoSerializationTest extends BinarySerializationFixture {

  private static final byte[] SPEC_BYTES =
      ByteBufUtil.decodeHexDump(
          "0f" // mask: SymbolicId, NamespaceUri, LocalizedText, Locale
              + "01000000" // SymbolicId
              + "02000000" // NamespaceUri
              + "03000000" // Locale
              + "04000000"); // LocalizedText

  private static final DiagnosticInfo SPEC_VALUE = new DiagnosticInfo(2, 1, 3, 4, null, null, null);

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

  // Part 6 §5.2.2.12, Table 22: Locale is encoded before LocalizedText. A round trip can't catch
  // the two being swapped, so check the bytes.
  @Test
  void encoderWritesLocaleBeforeLocalizedText() {
    writer.encodeDiagnosticInfo(SPEC_VALUE);

    assertArrayEquals(SPEC_BYTES, ByteBufUtil.getBytes(buffer));
  }

  @Test
  void decoderReadsLocaleBeforeLocalizedText() {
    buffer.writeBytes(SPEC_BYTES);

    assertEquals(SPEC_VALUE, reader.decodeDiagnosticInfo());
    assertEquals(0, buffer.readableBytes());
  }
}
