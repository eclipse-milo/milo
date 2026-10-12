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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * A non-null Matrix must have at least two dimensions.
 *
 * <p>{@link Matrix} asserts that a non-null value has at least two dimensions. A structure field
 * Matrix with 0 or 1 dimensions must be rejected with {@code Bad_DecodingError} rather than built
 * as an invalid Matrix, or fail with {@code AssertionError} when assertions are enabled
 * (https://github.com/eclipse-milo/milo/issues/2070).
 *
 * <p>A Variant with exactly one dimension decodes to a one-dimensional array, as the Binary Variant
 * decoder already does. A Variant with zero dimensions is rejected.
 */
class OpcUaXmlDecoderMatrixDimensionCountTest {

  private static final String UAX_NS = "http://opcfoundation.org/UA/2008/02/Types.xsd";

  private static final String INT32_ELEMENT = "<uax:Int32>42</uax:Int32>";

  private static final String XV_TYPE_ELEMENT =
      """
      <uax:ExtensionObject>
        <uax:TypeId>
          <uax:Identifier>i=12082</uax:Identifier>
        </uax:TypeId>
        <uax:Body>
          <uax:XVType>
            <uax:X>1.0</uax:X>
            <uax:Value>2.0</uax:Value>
          </uax:XVType>
        </uax:Body>
      </uax:ExtensionObject>
      """;

  private final EncodingContext context = new DefaultEncodingContext();

  /** A Matrix decoder method and the XML of one element of its type. */
  private record MatrixDecoder(
      String name, String elementXml, Function<OpcUaXmlDecoder, Matrix> decode) {

    @Override
    public String toString() {
      return name;
    }
  }

  static Stream<MatrixDecoder> matrixDecoders() {
    NodeId xvTypeId =
        XVType.TYPE_ID.toNodeId(DefaultEncodingContext.INSTANCE.getNamespaceTable()).orElseThrow();

    return Stream.of(
        new MatrixDecoder(
            "decodeMatrix", INT32_ELEMENT, d -> d.decodeMatrix("Test", OpcUaDataType.Int32)),
        new MatrixDecoder(
            "decodeEnumMatrix",
            "<uax:ApplicationType>Server_0</uax:ApplicationType>",
            d -> d.decodeEnumMatrix("Test")),
        new MatrixDecoder(
            "decodeStructMatrix(NodeId)",
            XV_TYPE_ELEMENT,
            d -> d.decodeStructMatrix("Test", xvTypeId)),
        new MatrixDecoder(
            "decodeStructMatrix(ExpandedNodeId)",
            XV_TYPE_ELEMENT,
            d -> d.decodeStructMatrix("Test", XVType.TYPE_ID)));
  }

  static Stream<Arguments> decodersWithTooFewDimensions() {
    return matrixDecoders()
        .flatMap(d -> Stream.of(Arguments.of(d, ""), Arguments.of(d, "<uax:Int32>1</uax:Int32>")));
  }

  /** A structure field Matrix with the given Dimensions children and one element. */
  private static String matrixXml(String dimensions, String element) {
    return "<Test xmlns:uax=\""
        + UAX_NS
        + "\"><uax:Dimensions>"
        + dimensions
        + "</uax:Dimensions><uax:Elements>"
        + element
        + "</uax:Elements></Test>";
  }

  /** A Variant whose value is a Matrix with the given Dimensions children and Int32 elements. */
  private static String variantMatrixXml(String dimensions, String elements) {
    return "<Test xmlns:uax=\""
        + UAX_NS
        + "\"><uax:Value><uax:Matrix><uax:Dimensions>"
        + dimensions
        + "</uax:Dimensions><uax:Elements>"
        + elements
        + "</uax:Elements></uax:Matrix></uax:Value></Test>";
  }

  private OpcUaXmlDecoder decoder(String xml) throws Exception {
    return new OpcUaXmlDecoder(context).setInput(new StringReader(xml));
  }

  @ParameterizedTest(name = "{0} with Dimensions [{1}]")
  @MethodSource("decodersWithTooFewDimensions")
  void rejectsFewerThanTwoDimensions(MatrixDecoder decoder, String dimensions) throws Exception {
    // One element, so a decoder that skips the dimension check passes the element count check
    // for no dimensions (empty product 1) and for [1] and builds the Matrix.
    String xml = matrixXml(dimensions, decoder.elementXml());

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decode().apply(decoder(xml)));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    assertFalse(e.getCause() instanceof AssertionError, "must not be a wrapped AssertionError");
  }

  // Control: the same element with two dimensions is the smallest valid Matrix and still decodes.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesTwoDimensions(MatrixDecoder decoder) throws Exception {
    String xml =
        matrixXml("<uax:Int32>1</uax:Int32><uax:Int32>1</uax:Int32>", decoder.elementXml());

    Matrix matrix = decoder.decode().apply(decoder(xml));

    assertArrayEquals(new int[] {1, 1}, matrix.getDimensions());
    assertEquals(1, Array.getLength(matrix.getElements()));
  }

  // Control: an empty field is a null Matrix, not a Matrix with too few dimensions.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesNullMatrix(MatrixDecoder decoder) throws Exception {
    Matrix matrix = decoder.decode().apply(decoder("<Test/>"));

    assertTrue(matrix.isNull());
  }

  // A Variant with one dimension is a one-dimensional array, not a Matrix. It decodes to the same
  // value as the ListOf form of the array, which is what the Binary Variant decoder does.
  @Test
  void variantWithOneDimensionDecodesToArray() throws Exception {
    Variant withDimensions =
        decoder(
                variantMatrixXml(
                    "<uax:Int32>2</uax:Int32>", "<uax:Int32>1</uax:Int32><uax:Int32>2</uax:Int32>"))
            .decodeVariant("Test");
    Variant listOf =
        decoder(
                "<Test xmlns:uax=\""
                    + UAX_NS
                    + "\"><uax:Value><uax:ListOfInt32><uax:Int32>1</uax:Int32>"
                    + "<uax:Int32>2</uax:Int32></uax:ListOfInt32></uax:Value></Test>")
            .decodeVariant("Test");

    assertArrayEquals(
        new Integer[] {1, 2}, assertInstanceOf(Integer[].class, withDimensions.value()));
    assertEquals(listOf, withDimensions);
  }

  // The one dimension must still describe the array: the element count check is not skipped.
  @Test
  void variantWithOneDimensionRejectsMismatchedLength() throws Exception {
    OpcUaXmlDecoder decoder =
        decoder(
            variantMatrixXml(
                "<uax:Int32>3</uax:Int32>", "<uax:Int32>1</uax:Int32><uax:Int32>2</uax:Int32>"));

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decodeVariant("Test"));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Zero dimensions describe neither an array nor a Matrix. decodeVariant already wrapped the
  // AssertionError as Bad_DecodingError, so the cause is what shows the check is in place.
  @ParameterizedTest(name = "Elements [{0}]")
  @ValueSource(strings = {INT32_ELEMENT, ""})
  void variantWithZeroDimensionsIsRejected(String elements) throws Exception {
    OpcUaXmlDecoder decoder = decoder(variantMatrixXml("", elements));

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decodeVariant("Test"));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    assertFalse(e.getCause() instanceof AssertionError, "must not be a wrapped AssertionError");
  }

  // Control: two dimensions still decode to a Matrix.
  @Test
  void variantWithTwoDimensionsDecodesToMatrix() throws Exception {
    Variant variant =
        decoder(
                variantMatrixXml(
                    "<uax:Int32>2</uax:Int32><uax:Int32>1</uax:Int32>",
                    "<uax:Int32>1</uax:Int32><uax:Int32>2</uax:Int32>"))
            .decodeVariant("Test");

    Matrix matrix = assertInstanceOf(Matrix.class, variant.value());
    assertArrayEquals(new int[] {2, 1}, matrix.getDimensions());
    assertEquals(OpcUaDataType.Int32, matrix.getDataType().orElseThrow());
  }
}
