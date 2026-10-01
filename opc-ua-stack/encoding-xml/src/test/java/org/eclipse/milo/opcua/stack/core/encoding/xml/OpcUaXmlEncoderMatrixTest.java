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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class OpcUaXmlEncoderMatrixTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfBuiltinTypeArguments")
  void encodeMatrixOfBuiltinType(@Nullable Matrix matrix, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeMatrix("Test", matrix);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfStructuredTypeArguments")
  void encodeMatrixOfStructuredType(Matrix matrix, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStructMatrix("Test", matrix, matrix.getDataTypeId().orElseThrow());

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "matrix = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfEnumeratedTypeArguments")
  void encodeMatrixOfEnumeratedType(Matrix matrix, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeEnumMatrix("Test", matrix);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  /**
   * Part 6 §5.3.1.17 says all dimensions of a Matrix shall be specified, so an empty element with
   * no Dimensions is not a valid Matrix. Opc.Ua.Types.xsd declares the Matrix element nillable, and
   * every Matrix encoder writes a null Matrix, Java null or {@link Matrix#ofNull()}, that way.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("nullMatrixEncodings")
  void nullMatrixEncodesAsNilElement(MatrixEncoding encoding) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoding.encode(encoder);

      actual = encoder.getOutputString();
    }

    String expected =
        """
        <Test xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
        """;

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());

    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(actual));

      assertTrue(decoder.decodeMatrix("Test", OpcUaDataType.Int32).isNull());
    }
  }

  static Stream<Arguments> nullMatrixEncodings() {
    return Stream.of(
        nullMatrixEncoding("encodeMatrix(null)", e -> e.encodeMatrix("Test", null)),
        nullMatrixEncoding("encodeMatrix(ofNull)", e -> e.encodeMatrix("Test", Matrix.ofNull())),
        nullMatrixEncoding("encodeEnumMatrix(null)", e -> e.encodeEnumMatrix("Test", null)),
        nullMatrixEncoding(
            "encodeEnumMatrix(ofNull)", e -> e.encodeEnumMatrix("Test", Matrix.ofNull())),
        nullMatrixEncoding(
            "encodeStructMatrix(null, NodeId)",
            e -> e.encodeStructMatrix("Test", null, XV_TYPE_NODE_ID)),
        nullMatrixEncoding(
            "encodeStructMatrix(ofNull, NodeId)",
            e -> e.encodeStructMatrix("Test", Matrix.ofNull(), XV_TYPE_NODE_ID)),
        // A null Matrix needs no type metadata, so an unregistered namespace must not matter.
        nullMatrixEncoding(
            "encodeStructMatrix(null, unregistered ExpandedNodeId)",
            e -> e.encodeStructMatrix("Test", null, UNREGISTERED_TYPE_ID)),
        nullMatrixEncoding(
            "encodeStructMatrix(ofNull, unregistered ExpandedNodeId)",
            e -> e.encodeStructMatrix("Test", Matrix.ofNull(), UNREGISTERED_TYPE_ID)));
  }

  private static final NodeId XV_TYPE_NODE_ID = new NodeId(0, 12080);

  private static final ExpandedNodeId UNREGISTERED_TYPE_ID =
      ExpandedNodeId.parse("nsu=urn:eclipse:milo:test:unregistered;i=1");

  private static Arguments nullMatrixEncoding(String name, MatrixEncoding encoding) {
    return Arguments.of(Named.of(name, encoding));
  }

  interface MatrixEncoding {
    void encode(OpcUaXmlEncoder encoder) throws Exception;
  }
}
