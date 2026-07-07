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
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Array;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

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
}
