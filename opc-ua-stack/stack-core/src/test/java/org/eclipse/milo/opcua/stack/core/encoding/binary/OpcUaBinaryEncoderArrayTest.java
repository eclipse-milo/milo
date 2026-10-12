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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Arrays of fixed-width builtin types are encoded in bulk rather than one element at a time. Part 6
 * §5.2.5 defines an array's encoding as its length followed by each element in its own encoding, so
 * the bulk path must produce exactly the bytes of the per-element scalar encoders.
 */
class OpcUaBinaryEncoderArrayTest {

  // Longer than one 8 KiB conversion block for every element width, so block boundaries are hit.
  private static final int LENGTH = 10_000;

  @ParameterizedTest(name = "{0}")
  @MethodSource("arrays")
  void variantArrayMatchesPerElementEncoding(String name, OpcUaDataType type, Object array) {
    ByteBuf expected = Unpooled.buffer();
    expected.writeByte(type.getTypeId() | 0x80);
    expected.writeIntLE(Array.getLength(array));
    encodeEachElement(expected, type, array);

    assertArrayEquals(bytes(expected), bytes(encode(new Variant(array))));
  }

  // Arrays are converted in 8 KiB blocks: 2,048 Int32 or 8,192 Boolean elements per block. These
  // lengths end exactly on, just before, and just after a block boundary.
  @ParameterizedTest(name = "{0} of length {2}")
  @MethodSource("blockBoundaryArrays")
  void arrayEndingAtABlockBoundaryMatchesPerElementEncoding(
      String name, OpcUaDataType type, int length, Object array) {

    ByteBuf expected = Unpooled.buffer();
    expected.writeByte(type.getTypeId() | 0x80);
    expected.writeIntLE(length);
    encodeEachElement(expected, type, array);

    assertArrayEquals(bytes(expected), bytes(encode(new Variant(array))));
  }

  static Stream<Arguments> blockBoundaryArrays() {
    return Stream.of(2048, 8192)
        .flatMap(
            perBlock ->
                Stream.of(0, 1, perBlock - 1, perBlock, perBlock + 1, 2 * perBlock)
                    .flatMap(
                        length ->
                            perBlock == 2048
                                ? Stream.of(
                                    Arguments.of(
                                        "int[]",
                                        OpcUaDataType.Int32,
                                        length,
                                        primitive(int.class, length, i -> i * 7)),
                                    Arguments.of(
                                        "Integer[]",
                                        OpcUaDataType.Int32,
                                        length,
                                        boxed(Integer.class, length, i -> i * 7)))
                                : Stream.of(
                                    Arguments.of(
                                        "boolean[]",
                                        OpcUaDataType.Boolean,
                                        length,
                                        primitive(boolean.class, length, i -> i % 2 == 0)),
                                    Arguments.of(
                                        "Boolean[]",
                                        OpcUaDataType.Boolean,
                                        length,
                                        boxed(Boolean.class, length, i -> i % 2 == 0)))));
  }

  // A Matrix writes its flattened elements through the same bulk path.
  @Test
  void matrixElementsMatchPerElementEncoding() {
    var matrix =
        new Matrix(new int[] {1, -2, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, 0}, new int[] {2, 3});

    ByteBuf expected = Unpooled.buffer();
    expected.writeByte(OpcUaDataType.Int32.getTypeId() | 0xC0);
    expected.writeIntLE(6);
    for (int value : new int[] {1, -2, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, 0}) {
      expected.writeIntLE(value);
    }
    expected.writeIntLE(2);
    expected.writeIntLE(2);
    expected.writeIntLE(3);

    assertArrayEquals(bytes(expected), bytes(encode(new Variant(matrix))));
  }

  static Stream<Arguments> arrays() {
    return Stream.of(
        Arguments.of("boolean[]", OpcUaDataType.Boolean, primitive(boolean.class, i -> i % 3 == 0)),
        Arguments.of("Boolean[]", OpcUaDataType.Boolean, boxed(Boolean.class, i -> i % 3 == 0)),
        Arguments.of("byte[]", OpcUaDataType.SByte, primitive(byte.class, i -> (byte) (i * 7))),
        Arguments.of("Byte[]", OpcUaDataType.SByte, boxed(Byte.class, i -> (byte) (i * 7))),
        Arguments.of("UByte[]", OpcUaDataType.Byte, boxed(Object.class, i -> ubyte(i % 256))),
        Arguments.of("short[]", OpcUaDataType.Int16, primitive(short.class, i -> (short) (i * 31))),
        Arguments.of("Short[]", OpcUaDataType.Int16, boxed(Short.class, i -> (short) (i * 31))),
        Arguments.of("UShort[]", OpcUaDataType.UInt16, boxed(Object.class, i -> ushort(i * 6))),
        Arguments.of("int[]", OpcUaDataType.Int32, primitive(int.class, i -> i * 2_654_435)),
        Arguments.of("Integer[]", OpcUaDataType.Int32, boxed(Integer.class, i -> i * 2_654_435)),
        Arguments.of(
            "UInteger[]", OpcUaDataType.UInt32, boxed(Object.class, i -> uint(i * 429_496L))),
        Arguments.of(
            "long[]", OpcUaDataType.Int64, primitive(long.class, i -> i * -0x1234_5678_9ABCL)),
        Arguments.of("Long[]", OpcUaDataType.Int64, boxed(Long.class, i -> i * -0x1234_5678_9ABCL)),
        Arguments.of(
            "ULong[]",
            OpcUaDataType.UInt64,
            boxed(Object.class, OpcUaBinaryEncoderArrayTest::ulongValue)),
        Arguments.of(
            "float[]",
            OpcUaDataType.Float,
            primitive(float.class, OpcUaBinaryEncoderArrayTest::floatValue)),
        Arguments.of(
            "Float[]",
            OpcUaDataType.Float,
            boxed(Float.class, OpcUaBinaryEncoderArrayTest::floatValue)),
        Arguments.of(
            "double[]",
            OpcUaDataType.Double,
            primitive(double.class, OpcUaBinaryEncoderArrayTest::doubleValue)),
        Arguments.of(
            "Double[]",
            OpcUaDataType.Double,
            boxed(Double.class, OpcUaBinaryEncoderArrayTest::doubleValue)));
  }

  // Boxed arrays may hold nulls; each is encoded as the type's zero, as the scalar encoders do.
  @ParameterizedTest(name = "{0}")
  @MethodSource("arraysWithNulls")
  void nullElementsOfABoxedArrayEncodeAsZero(String name, OpcUaDataType type, Object array) {
    ByteBuf expected = Unpooled.buffer();
    expected.writeByte(type.getTypeId() | 0x80);
    expected.writeIntLE(Array.getLength(array));
    encodeEachElement(expected, type, array);

    assertArrayEquals(bytes(expected), bytes(encode(new Variant(array))));
  }

  static Stream<Arguments> arraysWithNulls() {
    return arrays()
        .filter(arguments -> !arguments.get()[2].getClass().getComponentType().isPrimitive())
        .map(
            arguments -> {
              Object[] array = ((Object[]) arguments.get()[2]).clone();
              for (int i = 0; i < array.length; i += 5) {
                array[i] = null;
              }
              return Arguments.of(arguments.get()[0], arguments.get()[1], array);
            });
  }

  // Values at and above 2^63 have the sign bit set in their two's complement encoding.
  private static ULong ulongValue(int i) {
    return switch (i % 3) {
      case 0 -> ULong.MAX;
      case 1 -> ulong(new BigInteger("9223372036854775808"));
      default -> ulong(i * 0x1_0000_0001L);
    };
  }

  private static float floatValue(int i) {
    return switch (i % 5) {
      case 0 -> Float.NaN;
      case 1 -> Float.intBitsToFloat(0x7FC0_1234); // a NaN with a payload, kept by raw bits
      case 2 -> -0.0f;
      case 3 -> Float.MIN_VALUE;
      default -> i / 3.0f;
    };
  }

  private static double doubleValue(int i) {
    return switch (i % 5) {
      case 0 -> Double.NaN;
      case 1 -> Double.longBitsToDouble(0x7FF8_0000_1234_5678L);
      case 2 -> -0.0d;
      case 3 -> Double.MAX_VALUE;
      default -> i / 7.0d;
    };
  }

  private static Object primitive(Class<?> componentType, IntFunction<Object> element) {
    return primitive(componentType, LENGTH, element);
  }

  private static Object primitive(Class<?> componentType, int length, IntFunction<Object> element) {
    Object array = Array.newInstance(componentType, length);
    for (int i = 0; i < length; i++) {
      Array.set(array, i, element.apply(i));
    }
    return array;
  }

  private static Object boxed(Class<?> componentType, IntFunction<Object> element) {
    return boxed(componentType, LENGTH, element);
  }

  private static Object boxed(Class<?> componentType, int length, IntFunction<Object> element) {
    Class<?> type = componentType == Object.class ? element.apply(0).getClass() : componentType;
    Object array = Array.newInstance(type, length);
    for (int i = 0; i < length; i++) {
      Array.set(array, i, element.apply(i));
    }
    return array;
  }

  /** The reference encoding: each element through the encoder's scalar method for the type. */
  private static void encodeEachElement(ByteBuf target, OpcUaDataType type, Object array) {
    var encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(target);

    for (int i = 0; i < Array.getLength(array); i++) {
      @Nullable Object element = Array.get(array, i);

      switch (type) {
        case Boolean -> encoder.encodeBoolean((Boolean) element);
        case SByte -> encoder.encodeSByte((Byte) element);
        case Byte -> encoder.encodeByte((UByte) element);
        case Int16 -> encoder.encodeInt16((Short) element);
        case UInt16 -> encoder.encodeUInt16((UShort) element);
        case Int32 -> encoder.encodeInt32((Integer) element);
        case UInt32 -> encoder.encodeUInt32((UInteger) element);
        case Int64 -> encoder.encodeInt64((Long) element);
        case UInt64 -> encoder.encodeUInt64((ULong) element);
        case Float -> encoder.encodeFloat((Float) element);
        case Double -> encoder.encodeDouble((Double) element);
        default -> throw new IllegalArgumentException("not fixed-width: " + type);
      }
    }
  }

  private static ByteBuf encode(Variant variant) {
    ByteBuf buffer = Unpooled.buffer();
    new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
        .setBuffer(buffer)
        .encodeVariant(variant);
    return buffer;
  }

  private static byte[] bytes(ByteBuf buffer) {
    return ByteBufUtil.getBytes(buffer);
  }
}
