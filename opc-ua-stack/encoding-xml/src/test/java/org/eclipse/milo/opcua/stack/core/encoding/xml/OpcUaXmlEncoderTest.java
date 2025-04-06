/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xmlunit.diff.Diff;

class OpcUaXmlEncoderTest {

  private static final boolean DEBUG = true;

  EncodingContext context = new DefaultEncodingContext();

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 127, 128, 129, 256})
  void nestedDiagnosticInfo(int depth) {
    var nested = createNestedDiagnosticInfo(depth);

    var encoder = new OpcUaXmlEncoder(context);

    if (depth <= encoder.getEncodingContext().getEncodingLimits().getMaxRecursionDepth()) {
      assertDoesNotThrow(() -> encoder.encodeDiagnosticInfo("Test", nested));
    } else {
      assertThrows(
          UaSerializationException.class, () -> encoder.encodeDiagnosticInfo("Test", nested));
    }
  }

  /**
   * Prints the expected and actual XML if there are differences or if DEBUG is enabled.
   *
   * @param diff the Diff object containing the differences.
   * @param expected the expected XML string.
   * @param actual the actual XML string.
   */
  static void maybePrintXml(Diff diff, String expected, String actual) {
    if (diff.hasDifferences() || DEBUG) {
      System.out.printf("Expected:%n%s%n", expected);
      System.out.printf("Actual:%n%s%n", actual);
    }
  }

  /**
   * Creates a nested DiagnosticInfo structure with the specified recursion depth.
   *
   * @param depth the recursion depth for nesting DiagnosticInfo objects.
   * @return a DiagnosticInfo object with nested innerDiagnosticInfo up to the specified depth.
   */
  private static DiagnosticInfo createNestedDiagnosticInfo(int depth) {
    if (depth <= 0) {
      return DiagnosticInfo.NULL_VALUE;
    }

    return new DiagnosticInfo(
        depth,
        depth * 10,
        depth * 100,
        depth * 1000,
        "Additional info at depth " + depth,
        StatusCode.GOOD,
        createNestedDiagnosticInfo(depth - 1));
  }
}
