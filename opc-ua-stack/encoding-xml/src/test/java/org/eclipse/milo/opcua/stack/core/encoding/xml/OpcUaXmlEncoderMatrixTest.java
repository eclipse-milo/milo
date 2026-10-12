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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
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

  /**
   * {@link Matrix} accepts any flat array for its data type, and generic codecs such as
   * JsonStructCodec build Object[] ones. Every element must be written by the Matrix's built-in
   * type, so the XML doesn't depend on the flat array's Java class. The fixtures use primitive flat
   * arrays where a type has one; this adds Object[] for every type and the boxed array for those.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixOfBuiltinTypeWithOtherFlatArrays")
  void encodeMatrixWritesEveryElementWhateverTheFlatArrayClass(Matrix matrix, String expected)
      throws Exception {

    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeMatrix("Test", matrix);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  /**
   * JsonStructCodec builds an enum Matrix as an Object[] of enumeration values with data type
   * Int32. It must encode like the typed fixture, not as an element with no Dimensions.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixOfEnumeratedTypeWithObjectFlatArray")
  void encodeEnumMatrixWritesEveryElementOfAnObjectFlatArray(Matrix matrix, String expected)
      throws Exception {

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
   * An element whose class doesn't match the Matrix's data type can't be written as that type. The
   * encoder must fail instead of writing fewer elements than the dimensions describe, which Part 6
   * §5.3.1.17 requires a decoder to reject.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("matricesWithMismatchedElements")
  void encodeMatrixRejectsElementOfAnotherType(Matrix matrix) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> encoder.encodeMatrix("Test", matrix));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().getValue());
    }
  }

  /**
   * An enumeration element is written as its symbolic name and value, which only a {@code
   * UaEnumeratedType} carries. Any other element, including the Integer values {@code
   * decodeEnumMatrix} returns, must fail instead of turning the Matrix into a null one.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("enumMatricesWithNonEnumElements")
  void encodeEnumMatrixRejectsNonEnumElement(Matrix matrix) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      UaSerializationException e =
          assertThrows(
              UaSerializationException.class, () -> encoder.encodeEnumMatrix("Test", matrix));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().getValue());
    }
  }

  /**
   * Part 6 §5.3.1.17 requires the element count to match Dimensions, and §5.3.4 makes array
   * elements nillable because XML encoders would otherwise drop empty ones. A null element is
   * written in place: as xsi:nil, or for DateTime, Guid, Variant and StatusCode, whose list
   * elements the schema doesn't make nillable, as their null or default values from Part 6 Table 1.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("nullMatrixElementForms")
  void encodeMatrixWritesNullElementInPlace(OpcUaDataType dataType, String expectedElement)
      throws Exception {

    Object elements = Array.newInstance(dataType.getBackingClass(), 1);
    Matrix matrix = new Matrix(elements, new int[] {1, 1}, dataType);

    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeMatrix("Test", matrix);

      actual = encoder.getOutputString();
    }

    String expected =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <uax:Dimensions>
            <uax:Int32>1</uax:Int32>
            <uax:Int32>1</uax:Int32>
          </uax:Dimensions>
          <uax:Elements>%s</uax:Elements>
        </Test>
        """
            .formatted(expectedElement);

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  /**
   * Part 6 Table 1 gives Boolean and the numeric types no null value, so a null element of these
   * types can't be written. The encoder must fail instead of throwing NullPointerException.
   */
  @ParameterizedTest
  @EnumSource(
      value = OpcUaDataType.class,
      names = {
        "Boolean", "SByte", "Byte", "Int16", "UInt16", "Int32", "UInt32", "Int64", "UInt64",
        "Float", "Double"
      })
  void encodeMatrixRejectsNullElementOfNonNullableType(OpcUaDataType dataType) throws Exception {
    Object elements = Array.newInstance(dataType.getBackingClass(), 2);
    Matrix matrix = new Matrix(elements, new int[] {1, 2}, dataType);

    try (var encoder = new OpcUaXmlEncoder(context)) {
      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> encoder.encodeMatrix("Test", matrix));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().getValue());
    }
  }

  static Stream<Arguments> nullMatrixElementForms() {
    Stream<Arguments> nil =
        Stream.of(
                OpcUaDataType.String,
                OpcUaDataType.ByteString,
                OpcUaDataType.XmlElement,
                OpcUaDataType.NodeId,
                OpcUaDataType.ExpandedNodeId,
                OpcUaDataType.QualifiedName,
                OpcUaDataType.LocalizedText,
                OpcUaDataType.ExtensionObject,
                OpcUaDataType.DataValue,
                OpcUaDataType.DiagnosticInfo)
            .map(t -> Arguments.of(t, "<uax:%s xsi:nil=\"true\"/>".formatted(t.name())));

    Stream<Arguments> nullValue =
        Stream.of(
            Arguments.of(
                OpcUaDataType.DateTime, "<uax:DateTime>1601-01-01T00:00:00Z</uax:DateTime>"),
            Arguments.of(
                OpcUaDataType.Guid, "<uax:Guid>00000000-0000-0000-0000-000000000000</uax:Guid>"),
            Arguments.of(
                OpcUaDataType.Variant, "<uax:Variant><uax:Value xsi:nil=\"true\"/></uax:Variant>"),
            Arguments.of(
                OpcUaDataType.StatusCode,
                "<uax:StatusCode><uax:Code>0</uax:Code></uax:StatusCode>"));

    return Stream.concat(nil, nullValue);
  }

  static Stream<Arguments> matrixOfBuiltinTypeWithOtherFlatArrays() {
    return MatrixArguments.matrixOfBuiltinTypeArguments()
        .flatMap(
            arguments -> {
              Matrix matrix = (Matrix) arguments.get()[0];
              Object expected = arguments.get()[1];
              Object elements = matrix.getElements();

              Stream.Builder<Arguments> variants = Stream.builder();
              variants.add(withFlatArray(matrix, Object.class, expected));
              if (elements.getClass().getComponentType().isPrimitive()) {
                variants.add(withFlatArray(matrix, ArrayUtil.getBoxedType(elements), expected));
              }
              return variants.build();
            });
  }

  static Stream<Arguments> matrixOfEnumeratedTypeWithObjectFlatArray() {
    return MatrixArguments.matrixOfEnumeratedTypeArguments()
        .map(
            arguments ->
                withFlatArray((Matrix) arguments.get()[0], Object.class, arguments.get()[1]));
  }

  static Stream<Arguments> matricesWithMismatchedElements() {
    return Stream.of(
        Arguments.of(
            Named.of(
                "String in an Int32 Matrix",
                new Matrix(new Object[] {1, "2"}, new int[] {1, 2}, OpcUaDataType.Int32))),
        Arguments.of(
            Named.of(
                "byte[] in a Byte Matrix",
                new Matrix(new byte[] {1, 2}, new int[] {1, 2}, OpcUaDataType.Byte))));
  }

  static Stream<Arguments> enumMatricesWithNonEnumElements() {
    return Stream.of(
        Arguments.of(
            Named.of(
                "Integer[]",
                new Matrix(new Integer[] {0, 1}, new int[] {1, 2}, OpcUaDataType.Int32))),
        Arguments.of(
            Named.of(
                "Object[] with an Integer",
                new Matrix(
                    new Object[] {ApplicationType.Server, 1},
                    new int[] {1, 2},
                    OpcUaDataType.Int32))),
        Arguments.of(
            Named.of(
                "Object[] with a null",
                new Matrix(
                    new Object[] {ApplicationType.Server, null},
                    new int[] {1, 2},
                    OpcUaDataType.Int32))));
  }

  /** Copy {@code matrix} into a Matrix whose flat array has {@code componentType} elements. */
  private static Arguments withFlatArray(Matrix matrix, Class<?> componentType, Object expected) {
    Object elements = matrix.getElements();
    int length = Array.getLength(elements);
    Object flatArray = Array.newInstance(componentType, length);
    for (int i = 0; i < length; i++) {
      Array.set(flatArray, i, Array.get(elements, i));
    }

    OpcUaDataType dataType = matrix.getDataType().orElseThrow();
    String name =
        "%s%s %s[]"
            .formatted(
                dataType, Arrays.toString(matrix.getDimensions()), componentType.getSimpleName());

    return Arguments.of(
        Named.of(name, new Matrix(flatArray, matrix.getDimensions(), dataType)), expected);
  }

  interface MatrixEncoding {
    void encode(OpcUaXmlEncoder encoder) throws Exception;
  }

  interface MatrixDecoding {
    Matrix decode(OpcUaXmlDecoder decoder) throws Exception;
  }
}
