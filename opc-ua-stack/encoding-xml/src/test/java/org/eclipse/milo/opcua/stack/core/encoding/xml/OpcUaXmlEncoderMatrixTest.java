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
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
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
  void nullMatrixEncodesAsNilElement(MatrixEncoding encoding, MatrixDecoding decoding)
      throws Exception {

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

      assertTrue(decoding.decode(decoder).isNull(), "the matching decoder reads a null Matrix");
    }
  }

  static Stream<Arguments> nullMatrixEncodings() {
    return Stream.of(
        nullMatrixEncoding(
            "encodeMatrix(null)",
            e -> e.encodeMatrix("Test", null),
            d -> d.decodeMatrix("Test", OpcUaDataType.Int32)),
        nullMatrixEncoding(
            "encodeMatrix(ofNull)",
            e -> e.encodeMatrix("Test", Matrix.ofNull()),
            d -> d.decodeMatrix("Test", OpcUaDataType.Int32)),
        nullMatrixEncoding(
            "encodeEnumMatrix(null)",
            e -> e.encodeEnumMatrix("Test", null),
            d -> d.decodeEnumMatrix("Test")),
        nullMatrixEncoding(
            "encodeEnumMatrix(ofNull)",
            e -> e.encodeEnumMatrix("Test", Matrix.ofNull()),
            d -> d.decodeEnumMatrix("Test")),
        nullMatrixEncoding(
            "encodeStructMatrix(null, NodeId)",
            e -> e.encodeStructMatrix("Test", null, XV_TYPE_NODE_ID),
            d -> d.decodeStructMatrix("Test", XV_TYPE_NODE_ID)),
        nullMatrixEncoding(
            "encodeStructMatrix(ofNull, NodeId)",
            e -> e.encodeStructMatrix("Test", Matrix.ofNull(), XV_TYPE_NODE_ID),
            d -> d.decodeStructMatrix("Test", XV_TYPE_NODE_ID)),
        nullMatrixEncoding(
            "encodeStructMatrix(null, ExpandedNodeId)",
            e -> e.encodeStructMatrix("Test", null, XVType.TYPE_ID),
            d -> d.decodeStructMatrix("Test", XVType.TYPE_ID)),
        nullMatrixEncoding(
            "encodeStructMatrix(ofNull, ExpandedNodeId)",
            e -> e.encodeStructMatrix("Test", Matrix.ofNull(), XVType.TYPE_ID),
            d -> d.decodeStructMatrix("Test", XVType.TYPE_ID)));
  }

  private static final NodeId XV_TYPE_NODE_ID = new NodeId(0, 12080);

  private static Arguments nullMatrixEncoding(
      String name, MatrixEncoding encoding, MatrixDecoding decoding) {

    return Arguments.of(Named.of(name, encoding), decoding);
  }

  interface MatrixEncoding {
    void encode(OpcUaXmlEncoder encoder) throws Exception;
  }

  interface MatrixDecoding {
    Matrix decode(OpcUaXmlDecoder decoder) throws Exception;
  }
}
