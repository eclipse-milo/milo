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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StructuredVariantSerializationTest {

  private final ByteBuf buffer = Unpooled.buffer();
  private final OpcUaBinaryEncoder encoder =
      new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
  private final OpcUaBinaryDecoder decoder =
      new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);

  @AfterEach
  void releaseBuffer() {
    buffer.release();
  }

  // Typed structure arrays must preserve null positions as null ExtensionObjects on the wire.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureArrays")
  void structureArrayVariantRoundTrips(String name, UaStructuredType[] values) {
    encoder.encodeVariant(new Variant(values));

    assertEquals(0x96, buffer.getUnsignedByte(0));
    ExtensionObject[] decoded =
        assertInstanceOf(ExtensionObject[].class, decoder.decodeVariant().value());
    assertStructureElements(values, decoded);
    assertEquals(0, buffer.readableBytes());
  }

  // Matrix dimensions and element order must survive the same nullable structure conversion.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureMatrices")
  void structureMatrixVariantRoundTrips(String name, Matrix matrix) {
    encoder.encodeVariant(new Variant(matrix));

    assertEquals(0xD6, buffer.getUnsignedByte(0));
    Matrix decoded = assertInstanceOf(Matrix.class, decoder.decodeVariant().value());
    assertEquals(OpcUaDataType.ExtensionObject, decoded.getDataType().orElseThrow());
    assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
    assertStructureElements(
        (UaStructuredType[]) matrix.getElements(),
        assertInstanceOf(ExtensionObject[].class, decoded.getElements()));
    assertEquals(0, buffer.readableBytes());
  }

  static Stream<Arguments> structureArrays() {
    XVType first = new XVType(1.0, 2.0f);
    XVType second = new XVType(3.0, 4.0f);
    return Stream.of(
        Arguments.of("concrete empty", new XVType[0]),
        Arguments.of("concrete all null", new XVType[4]),
        Arguments.of("concrete trailing null", new XVType[] {first, null}),
        Arguments.of("concrete mixed null", new XVType[] {null, first, second, null}),
        Arguments.of("concrete nonnull", new XVType[] {first, second, first, second}),
        Arguments.of("interface empty", new UaStructuredType[0]),
        Arguments.of("interface all null", new UaStructuredType[4]),
        Arguments.of("interface mixed null", new UaStructuredType[] {null, first, second, null}),
        Arguments.of("interface nonnull", new UaStructuredType[] {first, second, first, second}));
  }

  static Stream<Arguments> structureMatrices() {
    return structureArrays()
        // Part 6, 5.2.2.16: wire Matrix dimensions must be positive; empty values use arrays.
        .filter(arguments -> ((UaStructuredType[]) arguments.get()[1]).length > 0)
        .flatMap(
            arguments -> {
              Object[] values = arguments.get();
              String name = (String) values[0];
              UaStructuredType[] elements = (UaStructuredType[]) values[1];
              int rows = elements.length / 2;
              return Stream.of(
                  Arguments.of(name + " 2D", new Matrix(elements, new int[] {rows, 2})),
                  Arguments.of(name + " 3D", new Matrix(elements, new int[] {1, rows, 2})));
            });
  }

  private static void assertStructureElements(
      UaStructuredType[] expected, ExtensionObject[] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      if (expected[i] == null) {
        assertTrue(actual[i].isNull(), "null structure at index " + i);
        assertEquals(NodeId.NULL_VALUE, actual[i].getEncodingOrTypeId());
      } else {
        assertEquals(
            expected[i],
            actual[i].decode(DefaultEncodingContext.INSTANCE),
            "structure at index " + i);
      }
    }
  }
}
