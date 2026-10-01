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

import static org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoderTest.maybePrintXml;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

/**
 * XML encoding of a Matrix with a zero or negative dimension.
 *
 * <p>OPC 10000-6, 5.3.1.17 says all Matrix dimensions shall be greater than 0, and {@link
 * OpcUaXmlDecoder} rejects any other dimension. An empty Matrix therefore has no XML Matrix form.
 */
class OpcUaXmlEmptyMatrixTest {

  private final EncodingContext context = DefaultEncodingContext.INSTANCE;

  static Stream<Arguments> emptyMatrixVariants() {
    return Stream.of(
        Arguments.of(
            "Int32, zero last dimension",
            new Matrix(new Integer[0], new int[] {2, 0}),
            "ListOfInt32",
            new Integer[0]),
        Arguments.of(
            "primitive Int32, zero middle dimension",
            new Matrix(new int[0], new int[] {2, 0, 3}),
            "ListOfInt32",
            new Integer[0]),
        Arguments.of(
            "String, negative dimension",
            new Matrix(new String[0], new int[] {-1, 2}),
            "ListOfString",
            new String[0]),
        Arguments.of(
            "structure encodes as ExtensionObject",
            new Matrix(
                new XVType[0], new int[] {0, 2}, OpcUaDataType.ExtensionObject, XVType.TYPE_ID),
            "ListOfExtensionObject",
            new ExtensionObject[0]),
        Arguments.of(
            "enumeration encodes as Int32",
            new Matrix(new ApplicationType[0], new int[] {0, 1}),
            "ListOfInt32",
            new Integer[0]),
        Arguments.of(
            "option set encodes as Byte",
            new Matrix(new AccessLevelType[0], new int[] {2, -3}),
            "ListOfByte",
            new UByte[0]));
  }

  // A Variant can hold a one-dimensional array instead of a Matrix (5.3.1.17), so an empty Matrix
  // is written as an empty array of its element type, as OpcUaBinaryEncoder does (5.2.2.16).
  @ParameterizedTest(name = "{0}")
  @MethodSource("emptyMatrixVariants")
  void encodeVariantWritesEmptyMatrixAsEmptyArray(
      String description, Matrix matrix, String arrayElement, Object[] ignoredDecoded)
      throws Exception {

    String actual = encode(e -> e.encodeVariant("Test", new Variant(matrix)));

    String expected =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Value>
            <uax:%s></uax:%s>
          </uax:Value>
        </Test>
        """
            .formatted(arrayElement, arrayElement);

    assertXmlEquals(expected, actual);
  }

  // The empty array form carries no dimensions, so it decodes as an empty one-dimensional array of
  // the element's built-in type rather than as a Matrix.
  @ParameterizedTest(name = "{0}")
  @MethodSource("emptyMatrixVariants")
  void emptyMatrixVariantDecodesAsEmptyArray(
      String description, Matrix matrix, String ignoredArrayElement, Object[] expected)
      throws Exception {

    String encoded = encode(e -> e.encodeVariant("Test", new Variant(matrix)));

    Variant decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeVariant("Test");
    }

    assertEquals(expected.getClass(), decoded.value().getClass());
    assertArrayEquals(expected, (Object[]) decoded.value());
  }

  // Control: a Matrix whose dimensions are all positive keeps the Matrix form, including a
  // dimension of length 1.
  @Test
  void encodeVariantKeepsMatrixWithPositiveDimensions() throws Exception {
    Matrix matrix = Matrix.ofInt32(new Integer[][] {{1, 2}});

    String actual = encode(e -> e.encodeVariant("Test", new Variant(matrix)));

    String expected =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Value>
            <uax:Matrix>
              <uax:Dimensions>
                <uax:Int32>1</uax:Int32>
                <uax:Int32>2</uax:Int32>
              </uax:Dimensions>
              <uax:Elements>
                <uax:Int32>1</uax:Int32>
                <uax:Int32>2</uax:Int32>
              </uax:Elements>
            </uax:Matrix>
          </uax:Value>
        </Test>
        """;

    assertXmlEquals(expected, actual);
  }

  static Stream<Arguments> emptyMatrixFields() {
    return Stream.of(
        Arguments.of(
            "encodeMatrix, zero first dimension",
            (EncodeAction)
                e ->
                    e.encodeMatrix(
                        "Test", new Matrix(new Integer[0], new int[] {0, 2}, OpcUaDataType.Int32))),
        Arguments.of(
            "encodeMatrix, primitive array and zero middle dimension",
            (EncodeAction)
                e ->
                    e.encodeMatrix(
                        "Test", new Matrix(new int[0], new int[] {2, 0, 3}, OpcUaDataType.Int32))),
        Arguments.of(
            "encodeMatrix, negative dimension",
            (EncodeAction)
                e ->
                    e.encodeMatrix(
                        "Test",
                        new Matrix(new String[0], new int[] {-1, 2}, OpcUaDataType.String))),
        Arguments.of(
            "encodeEnumMatrix",
            (EncodeAction)
                e ->
                    e.encodeEnumMatrix(
                        "Test", new Matrix(new ApplicationType[0], new int[] {2, 0}))),
        Arguments.of(
            "encodeStructMatrix",
            (EncodeAction)
                e ->
                    e.encodeStructMatrix(
                        "Test",
                        new Matrix(
                            new XVType[0],
                            new int[] {0, 0},
                            OpcUaDataType.ExtensionObject,
                            XVType.TYPE_ID),
                        XVType.TYPE_ID)));
  }

  // A structure field has no array form to fall back on (5.3.4), so an empty Matrix is written as
  // a null Matrix. OPC 10000-6, 5.1.11 says null and empty arrays are semantically the same.
  @ParameterizedTest(name = "{0}")
  @MethodSource("emptyMatrixFields")
  void encodeMatrixFieldWritesEmptyMatrixAsNil(String description, EncodeAction action)
      throws Exception {

    String actual = encode(action);

    String expected =
        """
        <Test xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"></Test>
        """;

    assertXmlEquals(expected, actual);
  }

  // An empty Matrix field reads back as a null Matrix, and the fields after it still decode.
  @Test
  void structureWithEmptyMatrixFieldsDecodesNullMatrices() throws Exception {
    var value =
        new MatrixFields(
            new Matrix(new Integer[0], new int[] {0, 2}, OpcUaDataType.Int32),
            new Matrix(new ApplicationType[0], new int[] {2, 0}),
            new Matrix(
                new XVType[0], new int[] {0, 0}, OpcUaDataType.ExtensionObject, XVType.TYPE_ID),
            "after");

    String encoded = encode(e -> e.encodeStruct("Test", value, MatrixFields.CODEC));

    UaStructuredType decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeStruct("Test", MatrixFields.CODEC);
    }

    assertEquals(
        new MatrixFields(Matrix.ofNull(), Matrix.ofNull(), Matrix.ofNull(), "after"), decoded);
  }

  static Stream<Arguments> inconsistentMatrices() {
    return Stream.of(
        Arguments.of(
            "encodeVariant",
            (EncodeAction)
                e ->
                    e.encodeVariant(
                        "Test", new Variant(new Matrix(new Integer[] {1, 2}, new int[] {0, 2})))),
        Arguments.of(
            "encodeMatrix",
            (EncodeAction)
                e -> e.encodeMatrix("Test", new Matrix(new Integer[] {1, 2}, new int[] {0, 2}))),
        Arguments.of(
            "encodeEnumMatrix",
            (EncodeAction)
                e ->
                    e.encodeEnumMatrix(
                        "Test",
                        new Matrix(
                            new ApplicationType[] {ApplicationType.Server, ApplicationType.Client},
                            new int[] {2, 0}))),
        Arguments.of(
            "encodeStructMatrix",
            (EncodeAction)
                e ->
                    e.encodeStructMatrix(
                        "Test",
                        new Matrix(
                            new XVType[] {new XVType(1.0, 2.0f)},
                            new int[] {-1, 1},
                            OpcUaDataType.ExtensionObject,
                            XVType.TYPE_ID),
                        XVType.TYPE_ID)));
  }

  // A Matrix with elements and a zero or negative dimension is inconsistent. Writing it as an
  // empty array or a null Matrix would drop its elements.
  @ParameterizedTest(name = "{0}")
  @MethodSource("inconsistentMatrices")
  void encodeRejectsMatrixWithElementsAndNonPositiveDimension(
      String description, EncodeAction action) throws Exception {

    UaSerializationException e = assertThrows(UaSerializationException.class, () -> encode(action));

    assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().value());
  }

  private static void assertXmlEquals(String expected, String actual) {
    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();
    maybePrintXml(diff, expected, actual);
    assertFalse(diff.hasDifferences(), diff.toString());
  }

  private interface EncodeAction {
    void accept(OpcUaXmlEncoder encoder) throws Exception;
  }

  private String encode(EncodeAction action) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      action.accept(encoder);
      return encoder.getOutputString();
    }
  }

  /** A structure with one Matrix field per encode method, followed by a String field. */
  private record MatrixFields(Matrix numbers, Matrix modes, Matrix vectors, String after)
      implements UaStructuredType {

    static final DataTypeCodec CODEC =
        new DataTypeCodec() {
          @Override
          public Class<?> getType() {
            return MatrixFields.class;
          }

          @Override
          public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
            return new MatrixFields(
                decoder.decodeMatrix("Numbers", OpcUaDataType.Int32),
                decoder.decodeEnumMatrix("Modes"),
                decoder.decodeStructMatrix("Vectors", XVType.TYPE_ID),
                decoder.decodeString("After"));
          }

          @Override
          public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
            var fields = (MatrixFields) value;
            encoder.encodeMatrix("Numbers", fields.numbers());
            encoder.encodeEnumMatrix("Modes", fields.modes());
            encoder.encodeStructMatrix("Vectors", fields.vectors(), XVType.TYPE_ID);
            encoder.encodeString("After", fields.after());
          }
        };

    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }
}
