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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Binary decoding of a null Matrix, which Part 6 encodes as a Dimensions array of length -1.
 *
 * <p>The {@code UaDecoder} Matrix methods return {@link Matrix#ofNull()} for a null Matrix, never
 * {@code null}. Generated codecs rely on this: they call {@link Matrix#transform} on the result of
 * {@code decodeEnumMatrix} and the option-set {@code decodeMatrix} calls without a null check.
 */
class NullMatrixSerializationTest {

  private static final byte[] NULL_MATRIX = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
  private static final byte SENTINEL = 0x7f;

  static Stream<Arguments> decodeMethods() {
    EncodingContext context = DefaultEncodingContext.INSTANCE;
    return Stream.of(
        Arguments.of(
            "decodeMatrix",
            (Function<OpcUaBinaryDecoder, Matrix>)
                decoder -> decoder.decodeMatrix(null, OpcUaDataType.Int32)),
        Arguments.of(
            "decodeEnumMatrix",
            (Function<OpcUaBinaryDecoder, Matrix>) decoder -> decoder.decodeEnumMatrix(null)),
        Arguments.of(
            "decodeStructMatrix(NodeId)",
            (Function<OpcUaBinaryDecoder, Matrix>)
                decoder ->
                    decoder.decodeStructMatrix(
                        null, XVType.TYPE_ID.toNodeId(context.getNamespaceTable()).orElseThrow())),
        Arguments.of(
            "decodeStructMatrix(ExpandedNodeId)",
            (Function<OpcUaBinaryDecoder, Matrix>)
                decoder -> decoder.decodeStructMatrix(null, XVType.TYPE_ID)));
  }

  // Part 6 §5.2.5: a null array has length -1, so a null Matrix is a -1 Dimensions length and
  // nothing else. Each method must return the Matrix null value and leave the next field in place.
  @ParameterizedTest(name = "{0}")
  @MethodSource("decodeMethods")
  void nullMatrixDecodesToMatrixOfNullAndConsumesOnlyTheDimensionsLength(
      String name, Function<OpcUaBinaryDecoder, Matrix> decode) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeBytes(NULL_MATRIX);
    buffer.writeByte(SENTINEL);

    Matrix matrix =
        decode.apply(new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer));

    assertNotNull(matrix, name + " must return Matrix.ofNull(), not null");
    assertTrue(matrix.isNull());
    assertEquals(Matrix.ofNull(), matrix);
    assertEquals(NULL_MATRIX.length, buffer.readerIndex(), "only the -1 length is consumed");
    assertEquals(SENTINEL, buffer.readByte(), "the following field is still readable");
  }

  // A decoded null Matrix must be a value the encoder accepts again, otherwise a structure that
  // was read from the wire cannot be written back to it.
  @ParameterizedTest(name = "{0}")
  @MethodSource("decodeMethods")
  void decodedNullMatrixEncodesBackToTheSameBytes(
      String name, Function<OpcUaBinaryDecoder, Matrix> decode) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeBytes(NULL_MATRIX);

    Matrix matrix =
        decode.apply(new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer));

    ByteBuf reencoded = Unpooled.buffer();
    new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
        .setBuffer(reencoded)
        .encodeMatrix(null, matrix);

    byte[] bytes = new byte[reencoded.readableBytes()];
    reencoded.readBytes(bytes);
    assertArrayEquals(NULL_MATRIX, bytes);
  }
}
