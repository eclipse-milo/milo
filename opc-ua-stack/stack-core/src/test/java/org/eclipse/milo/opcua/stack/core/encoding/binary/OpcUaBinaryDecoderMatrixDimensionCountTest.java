/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A non-null structure field Matrix must have at least two dimensions.
 *
 * <p>Part 6 §5.2.5, Table 28 says the number of dimensions of an inline matrix shall be at least 2,
 * and {@link Matrix} asserts the same. Each Matrix decoder must reject a dimension count of 0 or 1
 * with {@code Bad_DecodingError} rather than build an invalid Matrix, or throw {@code
 * AssertionError} with assertions enabled (https://github.com/eclipse-milo/milo/issues/2070).
 */
class OpcUaBinaryDecoderMatrixDimensionCountTest {

  private static final EncodingContext CONTEXT = DefaultEncodingContext.INSTANCE;

  /** A Matrix decoder method and a writer for one encoded element of its type. */
  private record MatrixDecoder(
      String name, Consumer<ByteBuf> writeElement, Function<OpcUaBinaryDecoder, Matrix> decode) {

    @Override
    public String toString() {
      return name;
    }
  }

  static Stream<MatrixDecoder> matrixDecoders() {
    NodeId xvTypeId = XVType.TYPE_ID.toNodeId(CONTEXT.getNamespaceTable()).orElseThrow();
    Consumer<ByteBuf> writeXvType =
        b -> {
          b.writeDoubleLE(1.0);
          b.writeFloatLE(2.0f);
        };

    return Stream.of(
        new MatrixDecoder(
            "decodeMatrix", b -> b.writeIntLE(42), d -> d.decodeMatrix(null, OpcUaDataType.Int32)),
        new MatrixDecoder("decodeEnumMatrix", b -> b.writeIntLE(0), d -> d.decodeEnumMatrix(null)),
        new MatrixDecoder(
            "decodeStructMatrix(NodeId)", writeXvType, d -> d.decodeStructMatrix(null, xvTypeId)),
        new MatrixDecoder(
            "decodeStructMatrix(ExpandedNodeId)",
            writeXvType,
            d -> d.decodeStructMatrix(null, XVType.TYPE_ID)));
  }

  static Stream<Arguments> decodersWithTooFewDimensions() {
    return matrixDecoders().flatMap(d -> Stream.of(Arguments.of(d, 0), Arguments.of(d, 1)));
  }

  /**
   * Encode a Matrix whose {@code dimensionCount} dimensions are all 1, followed by one element. The
   * element is present so a decoder that skips the dimension check reads it and builds the Matrix.
   */
  private static ByteBuf encodeMatrix(MatrixDecoder decoder, int dimensionCount) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(dimensionCount);
    for (int i = 0; i < dimensionCount; i++) {
      buffer.writeIntLE(1);
    }
    decoder.writeElement().accept(buffer);
    return buffer;
  }

  @ParameterizedTest(name = "{0} with {1} dimension(s)")
  @MethodSource("decodersWithTooFewDimensions")
  void rejectsFewerThanTwoDimensions(MatrixDecoder decoder, int dimensionCount) {
    ByteBuf buffer = encodeMatrix(decoder, dimensionCount);

    UaSerializationException e =
        assertThrows(
            UaSerializationException.class,
            () -> decoder.decode().apply(new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer)));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Control: the same layout with two dimensions is the smallest valid Matrix and still decodes.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesTwoDimensions(MatrixDecoder decoder) {
    ByteBuf buffer = encodeMatrix(decoder, 2);

    Matrix matrix = decoder.decode().apply(new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer));

    assertArrayEquals(new int[] {1, 1}, matrix.getDimensions());
    assertEquals(1, Array.getLength(matrix.getElements()));
    assertEquals(0, buffer.readableBytes(), "the element must be consumed");
  }

  // Control: a Dimensions length of -1 is a null Matrix, not a Matrix with too few dimensions.
  // Whether it decodes to null or Matrix.ofNull() is a separate concern (#2068).
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void decodesNullMatrix(MatrixDecoder decoder) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(-1);

    Matrix matrix = decoder.decode().apply(new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer));

    assertTrue(matrix == null || matrix.isNull(), "expected a null Matrix but got " + matrix);
  }
}
