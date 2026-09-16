/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Array;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

public class OpcUaBinaryDecoderTest {

  @Test
  public void testReadDiagnosticInfoStackOverflow() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writeByte(0x40);
    }
    buffer.writeByte(0x00);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeDiagnosticInfo());
  }

  @Test
  public void testReadVariantStackOverflow() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writerIndex(5 * i);

      buffer.writeByte(24 | 0x80);
      buffer.writeByte(1);
      buffer.writeByte(0);
      buffer.writeByte(0);
      buffer.writeByte(0);
    }
    buffer.writeByte(0);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  public void testReadVariantStackOverflow2() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writerIndex(2 * i);

      buffer.writeByte(23);
      buffer.writeByte(1);
    }
    buffer.writeByte(0);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  void decodeVariantNullArrayConsumesDimensions() {
    ByteBuf buffer = Unpooled.buffer();

    // Int32 | array | dimensions, ArrayLength -1 (null array), then an ArrayDimensions field
    buffer.writeByte(6 | 0x80 | 0x40);
    buffer.writeIntLE(-1);
    buffer.writeIntLE(2);
    buffer.writeIntLE(2);
    buffer.writeIntLE(3);
    // a subsequent field, decoded correctly only if ArrayDimensions was consumed
    buffer.writeIntLE(42);

    OpcUaBinaryDecoder decoder =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);

    assertEquals(new Variant(null), decoder.decodeVariant());
    assertEquals(42, decoder.decodeInt32());
  }

  @Test
  void decodeMatrixRejectsNegativeDimensionCount() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(-2);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeMatrix(null, OpcUaDataType.Int32));
  }

  @Test
  void decodeMatrixRejectsNegativeDimensions() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(1);
    buffer.writeIntLE(-1);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeMatrix(null, OpcUaDataType.Int32));
  }

  @Test
  void decodeMatrixAllowsZeroDimensions() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(0);
    buffer.writeIntLE(2);

    Matrix matrix =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(buffer)
            .decodeMatrix(null, OpcUaDataType.Int32);

    assertArrayEquals(new int[] {0, 2}, matrix.getDimensions());
    assertEquals(0, Array.getLength(matrix.getElements()));
  }

  @Test
  void decodeStructMatrixAllowsZeroDimensions() throws Exception {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(0);
    buffer.writeIntLE(2);

    Matrix matrix =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(buffer)
            .decodeStructMatrix(
                null,
                XVType.TYPE_ID.toNodeIdOrThrow(
                    DefaultEncodingContext.INSTANCE.getNamespaceTable()));

    assertArrayEquals(new int[] {0, 2}, matrix.getDimensions());
    assertEquals(0, Array.getLength(matrix.getElements()));
    assertEquals(XVType.TYPE_ID, matrix.getDataTypeId().orElseThrow());
  }

  @Test
  void decodeMatrixRejectsDimensionOverflow() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(46341);
    buffer.writeIntLE(46341);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeMatrix(null, OpcUaDataType.Int32));
  }

  @Test
  void decodeVariantRejectsNegativeMatrixDimensionCount() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(0);
    buffer.writeIntLE(-2);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  void decodeVariantRejectsNegativeOneDimensionalArrayDimension() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(0);
    buffer.writeIntLE(1);
    buffer.writeIntLE(-1);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  void decodeVariantRejectsMismatchedOneDimensionalArrayDimension() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);
    buffer.writeIntLE(2);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  void decodeVariantAllowsMatchingOneDimensionalArrayDimension() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);

    Variant variant =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer).decodeVariant();

    assertArrayEquals(new Integer[] {1}, (Integer[]) variant.value());
  }

  @Test
  void decodeVariantRejectsMatrixDimensionOverflow() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(0);
    buffer.writeIntLE(2);
    buffer.writeIntLE(46341);
    buffer.writeIntLE(46341);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  void decodeVariantRejectsMismatchedMatrixDimensions() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);
    buffer.writeIntLE(2);
    buffer.writeIntLE(2);
    buffer.writeIntLE(2);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  // The component type of a decoded array is not an implementation detail: Variant.getDataType()
  // and Matrix both derive the DataType from it. An empty array is the case that matters most,
  // because there is no element to fall back on, so a wrong component type silently changes the
  // DataType a Variant reports rather than producing a visible ClassCastException.
  @ParameterizedTest
  @EnumSource(OpcUaDataType.class)
  void decodeVariantEmptyArrayUsesBackingClassAsComponentType(OpcUaDataType dataType) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(dataType.getTypeId() | 0x80);
    buffer.writeIntLE(0);

    Variant variant =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer).decodeVariant();

    Object value = variant.value();
    assertNotNull(value);
    assertEquals(dataType.getBackingClass(), value.getClass().getComponentType());
  }

  // Same contract as above, reached through decodeMatrix rather than decodeVariant.
  @ParameterizedTest
  @EnumSource(OpcUaDataType.class)
  void decodeMatrixEmptyUsesBackingClassAsComponentType(OpcUaDataType dataType) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(0);
    buffer.writeIntLE(2);

    Matrix matrix =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(buffer)
            .decodeMatrix(null, dataType);

    Object elements = matrix.getElements();
    assertNotNull(elements);
    assertEquals(dataType.getBackingClass(), elements.getClass().getComponentType());
  }

  // A builtin type id outside 1..25 in an array-encoded Variant is malformed input from a peer.
  // It must surface as Bad_DecodingError; it previously escaped the codec as a NullPointerException
  // from Array.newInstance(null, length).
  @ParameterizedTest
  @ValueSource(ints = {0, 26, 63})
  void decodeVariantRejectsUnknownArrayTypeId(int typeId) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(typeId | 0x80);
    buffer.writeIntLE(1);
    buffer.writeIntLE(0);

    UaSerializationException ex =
        assertThrows(
            UaSerializationException.class,
            () ->
                new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                    .setBuffer(buffer)
                    .decodeVariant());

    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), ex.getStatusCode());
  }

  // DiagnosticInfo is the one builtin type that cannot legally appear in a Variant, so it has no
  // round-trip coverage in VariantSerializationTest. Decode it from raw bytes instead, to prove the
  // array path dispatches to decodeDiagnosticInfo and not to some other element decoder.
  @Test
  void decodeVariantDiagnosticInfoArrayDispatchesToDiagnosticInfoDecoder() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.DiagnosticInfo.getTypeId() | 0x80);
    buffer.writeIntLE(1);
    buffer.writeByte(0x01); // SymbolicId present
    buffer.writeIntLE(7);

    Variant variant =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer).decodeVariant();

    DiagnosticInfo[] values = (DiagnosticInfo[]) variant.value();
    assertNotNull(values);
    assertEquals(1, values.length);
    assertEquals(7, values[0].symbolicId());
    assertEquals(0, buffer.readableBytes(), "element decoder consumed the wrong number of bytes");
  }
}
