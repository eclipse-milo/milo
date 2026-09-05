/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OpcUaXmlVariantWhitespaceTest {

  // Formatting and comments around Value must not turn a valid scalar into a null Variant.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n<!-- comment -->\n"})
  void scalarIgnoresWhitespaceBetweenElements(String spacing) throws Exception {
    String xml = "<Test>" + spacing + "<Value>" + spacing + "<Int32>7</Int32></Value></Test>";
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml)) {
      assertEquals(new Variant(7), decoder.decodeVariant("Test"));
    }
  }

  // Matrix dimensions and elements have the same meaning in compact and indented XML.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n<!-- comment -->\n"})
  void matrixIgnoresWhitespaceBetweenElements(String spacing) throws Exception {
    String xml = matrixXml(spacing, "<Int32>7</Int32><Int32>8</Int32>");
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml)) {
      assertEquals(
          new Variant(new Matrix(new Integer[] {7, 8}, new int[] {1, 2})),
          decoder.decodeVariant("Test"));
    }
  }

  // Variant matrices must use the same shape validation as direct matrix decoding.
  @ParameterizedTest
  @ValueSource(strings = {"<Int32>7</Int32>", "<Int32>7</Int32><String>8</String>"})
  void malformedMatrixFailsDecoding(String elements) throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, matrixXml("\n  ", elements))) {
      assertThrows(UaSerializationException.class, () -> decoder.decodeVariant("Test"));
    }
  }

  private static String matrixXml(String spacing, String elements) {
    return "<Test><Value><Matrix>"
        + spacing
        + "<Dimensions><Int32>1</Int32><Int32>2</Int32></Dimensions>"
        + spacing
        + "<Elements>"
        + elements
        + "</Elements></Matrix></Value></Test>";
  }
}
