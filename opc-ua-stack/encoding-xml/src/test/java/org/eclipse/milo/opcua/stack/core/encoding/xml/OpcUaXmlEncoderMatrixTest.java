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

import static org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoderTest.maybePrintXml;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class OpcUaXmlEncoderMatrixTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfBuiltinTypeArguments")
  void encodeMatrixOfBuiltinType(@Nullable Matrix matrix, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeMatrix("Test", matrix);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfStructuredTypeArguments")
  void encodeMatrixOfStructuredType(Matrix matrix, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeStructMatrix("Test", matrix, matrix.getDataTypeId().orElseThrow());

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfEnumeratedTypeArguments")
  void encodeMatrixOfEnumeratedType(Matrix matrix, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeEnumMatrix("Test", matrix);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }
}
