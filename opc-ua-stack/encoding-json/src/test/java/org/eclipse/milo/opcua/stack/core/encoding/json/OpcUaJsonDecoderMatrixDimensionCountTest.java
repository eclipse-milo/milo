/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class OpcUaJsonDecoderMatrixDimensionCountTest {

  private final EncodingContext context = new DefaultEncodingContext();

  /** A Matrix decoder method and the JSON of one element of its type. */
  private record MatrixDecoder(
      String name, String elementJson, Function<OpcUaJsonDecoder, Matrix> decode) {

    @Override
    public String toString() {
      return name;
    }
  }

  static Stream<MatrixDecoder> matrixDecoders() {
    NodeId xvTypeId =
        XVType.TYPE_ID.toNodeId(DefaultEncodingContext.INSTANCE.getNamespaceTable()).orElseThrow();
    String xvTypeJson = "{\"X\":1.0,\"Value\":2.0}";

    return Stream.of(
        new MatrixDecoder("decodeMatrix", "42", d -> d.decodeMatrix(null, OpcUaDataType.Int32)),
        new MatrixDecoder("decodeEnumMatrix", "0", d -> d.decodeEnumMatrix(null)),
        new MatrixDecoder(
            "decodeStructMatrix(NodeId)", xvTypeJson, d -> d.decodeStructMatrix(null, xvTypeId)),
        new MatrixDecoder(
            "decodeStructMatrix(ExpandedNodeId)",
            xvTypeJson,
            d -> d.decodeStructMatrix(null, XVType.TYPE_ID)));
  }

  static Stream<Arguments> decodersWithTooFewDimensions() {
    return matrixDecoders().flatMap(d -> Stream.of(Arguments.of(d, "[]"), Arguments.of(d, "[1]")));
  }

  private OpcUaJsonDecoder decoder(String json) {
    return new OpcUaJsonDecoder(context, new StringReader(json));
  }

  @ParameterizedTest(name = "{0} with Dimensions {1}")
  @MethodSource("decodersWithTooFewDimensions")
  void rejectsFewerThanTwoDimensions(MatrixDecoder decoder, String dimensions) {
    // One element, so a decoder that skips the dimension check passes the element count check
    // for [] (empty product 1) and for [1] and builds the Matrix.
    String json = "{\"Array\":[" + decoder.elementJson() + "],\"Dimensions\":" + dimensions + "}";

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decode().apply(decoder(json)));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Control: the same element with two dimensions is the smallest valid Matrix and still decodes.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesTwoDimensions(MatrixDecoder decoder) {
    String json = "{\"Array\":[" + decoder.elementJson() + "],\"Dimensions\":[1,1]}";

    Matrix matrix = decoder.decode().apply(decoder(json));

    assertArrayEquals(new int[] {1, 1}, matrix.getDimensions());
    assertEquals(1, Array.getLength(matrix.getElements()));
  }

  // Control: a JSON null is a null Matrix, not a Matrix with too few dimensions.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesNullMatrix(MatrixDecoder decoder) {
    assertEquals(Matrix.ofNull(), decoder.decode().apply(decoder("null")));
  }

  // Control: an empty Matrix still has two dimensions, so it is not rejected by the count check.
  @Test
  void decodesEmptyMatrixWithTwoDimensions() {
    Matrix matrix =
        decoder("{\"Array\":[],\"Dimensions\":[0,2]}").decodeMatrix(null, OpcUaDataType.Int32);

    assertArrayEquals(new int[] {0, 2}, matrix.getDimensions());
    assertEquals(0, Array.getLength(matrix.getElements()));
  }

  // A Variant with one dimension is a one-dimensional array, not a Matrix. It decodes to the same
  // value as the array written without Dimensions, which is what the Binary Variant decoder does.
  @Test
  void variantWithOneDimensionDecodesToArray() {
    Variant withDimensions =
        decoder("{\"UaType\":6,\"Value\":[1,2],\"Dimensions\":[2]}").decodeVariant(null);
    Variant withoutDimensions = decoder("{\"UaType\":6,\"Value\":[1,2]}").decodeVariant(null);

    assertArrayEquals(new int[] {1, 2}, assertInstanceOf(int[].class, withDimensions.value()));
    assertEquals(withoutDimensions, withDimensions);
  }

  // The one dimension must still describe the array: the element count check is not skipped.
  @Test
  void variantWithOneDimensionRejectsMismatchedLength() {
    OpcUaJsonDecoder decoder = decoder("{\"UaType\":6,\"Value\":[1,2],\"Dimensions\":[3]}");

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decodeVariant(null));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Zero dimensions describe neither an array nor a Matrix.
  @ParameterizedTest(name = "Value {0}")
  @ValueSource(strings = {"[42]", "[]"})
  void variantWithZeroDimensionsIsRejected(String value) {
    OpcUaJsonDecoder decoder = decoder("{\"UaType\":6,\"Value\":" + value + ",\"Dimensions\":[]}");

    UaSerializationException e =
        assertThrows(UaSerializationException.class, () -> decoder.decodeVariant(null));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Control: two dimensions still decode to a Matrix.
  @Test
  void variantWithTwoDimensionsDecodesToMatrix() {
    Variant variant =
        decoder("{\"UaType\":6,\"Value\":[1,2],\"Dimensions\":[2,1]}").decodeVariant(null);

    Matrix matrix = assertInstanceOf(Matrix.class, variant.value());
    assertArrayEquals(new int[] {2, 1}, matrix.getDimensions());
    assertTrue(matrix.getDataType().isPresent());
    assertEquals(OpcUaDataType.Int32, matrix.getDataType().orElseThrow());
  }
}
