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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.management.ManagementFactory;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.DiscoveryConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Lengths read from an OPC UA Binary message describe data that should follow in the same message.
 * The decoder must not size an allocation from a length the remaining bytes cannot hold, and must
 * not let nested arrays each reserve their full length before any element is decoded.
 */
class OpcUaBinaryDecoderLengthTest {

  private static final EncodingContext CONTEXT = DefaultEncodingContext.INSTANCE;

  private static final int VARIANT_ARRAY = OpcUaDataType.Variant.getTypeId() | 0x80;

  // Each case declares a length larger than the bytes that follow it. Every element of a builtin
  // array, and every byte of a ByteString or String, takes at least one byte, so each must fail as
  // a decoding error before the decoder reads past the end of the buffer.
  static Stream<Arguments> lengthsBeyondRemainingBytes() {
    return Stream.of(
        lengthCase(
            "Variant array",
            b -> {
              b.writeByte(VARIANT_ARRAY);
              b.writeIntLE(2_000_000);
            },
            OpcUaBinaryDecoder::decodeVariant),
        lengthCase(
            "Variant ArrayDimensions",
            b -> {
              b.writeByte(OpcUaDataType.Int32.getTypeId() | 0x80 | 0x40);
              b.writeIntLE(0);
              b.writeIntLE(1_000);
            },
            OpcUaBinaryDecoder::decodeVariant),
        lengthCase(
            "Int32 array",
            b -> {
              b.writeIntLE(1_000);
              b.writeIntLE(1);
            },
            d -> d.decodeInt32Array(null)),
        lengthCase(
            "Variant field array", b -> b.writeIntLE(2_000_000), d -> d.decodeVariantArray(null)),
        lengthCase(
            "Matrix elements",
            b -> {
              b.writeIntLE(2);
              b.writeIntLE(1_000);
              b.writeIntLE(1_000);
            },
            d -> d.decodeMatrix(null, OpcUaDataType.Variant)),
        lengthCase(
            "Matrix dimensions",
            b -> b.writeIntLE(1_000),
            d -> d.decodeMatrix(null, OpcUaDataType.Int32)),
        lengthCase(
            "ByteString",
            b -> {
              b.writeIntLE(1_000);
              b.writeByte(0);
            },
            OpcUaBinaryDecoder::decodeByteString),
        lengthCase(
            "String",
            b -> {
              b.writeIntLE(1_000);
              b.writeByte('a');
            },
            OpcUaBinaryDecoder::decodeString));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("lengthsBeyondRemainingBytes")
  void lengthBeyondRemainingBytesIsDecodingError(
      String name, ByteBuf buffer, Function<OpcUaBinaryDecoder, ?> decode) {

    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer);

    UaSerializationException ex =
        assertThrows(UaSerializationException.class, () -> decode.apply(decoder));

    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), ex.getStatusCode());
  }

  // A Variant array of Variant arrays, each claiming as many elements as there are bytes left,
  // passes every remaining-bytes check. If each level allocated its full length up front, the 64
  // levels below would hold 64 arrays of about 256K references when the innermost element fails.
  // The arrays must grow with the elements actually decoded instead.
  @Test
  void nestedArraysDoNotAllocateTheirDeclaredLengthsUpFront() {
    int depth = 64;
    int messageSize = 256 * 1024;

    ByteBuf buffer = Unpooled.buffer(messageSize);
    for (int i = 0; i < depth; i++) {
      buffer.writeByte(VARIANT_ARRAY);
      buffer.writeIntLE(messageSize - buffer.writerIndex() - Integer.BYTES);
    }
    // 0x3F is a scalar Variant with an unknown builtin type id, so the innermost element fails.
    while (buffer.writerIndex() < messageSize) {
      buffer.writeByte(0x3F);
    }

    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer);

    long allocated =
        allocatedBytesDuring(
            () -> assertThrows(UaSerializationException.class, decoder::decodeVariant));

    // Up-front allocation would take about 64 MiB here.
    assertTrue(
        allocated < 8 * 1024 * 1024,
        "allocated " + allocated + " bytes decoding a " + messageSize + " byte message");
  }

  // Structure array elements do not have a minimum size: a structure with no fields encodes as no
  // bytes at all (Part 6 §5.2.6). An array of them may declare more elements than bytes remain.
  @Test
  void zeroFieldStructArrayMayDeclareMoreElementsThanRemainingBytes() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(3);

    UaStructuredType[] values =
        new OpcUaBinaryDecoder(CONTEXT)
            .setBuffer(buffer)
            .decodeStructArray(null, localId(DiscoveryConfiguration.TYPE_ID));

    assertNotNull(values);
    assertEquals(3, values.length);
    assertInstanceOf(DiscoveryConfiguration[].class, values);
  }

  // Same as above, for a Matrix of structures, where the dimensions precede the elements.
  @Test
  void zeroFieldStructMatrixMayDeclareMoreElementsThanRemainingBytes() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(2);
    buffer.writeIntLE(2);
    buffer.writeIntLE(3);

    Matrix matrix =
        new OpcUaBinaryDecoder(CONTEXT)
            .setBuffer(buffer)
            .decodeStructMatrix(null, localId(DiscoveryConfiguration.TYPE_ID));

    assertArrayEquals(new int[] {2, 3}, matrix.getDimensions());
    assertInstanceOf(DiscoveryConfiguration[].class, matrix.getElements());
    assertEquals(6, ((Object[]) matrix.getElements()).length);
  }

  // Arrays longer than the decoder's initial capacity grow while their elements are decoded. The
  // result must still hold every element in order, with the builtin backing class as its
  // component type, and the decoder must stop exactly at the end of the array.
  @Test
  void largeVariantArrayDecodesEveryElement() {
    int length = 10_000;

    ByteBuf buffer = Unpooled.buffer();
    buffer.writeByte(OpcUaDataType.Int32.getTypeId() | 0x80);
    buffer.writeIntLE(length);
    for (int i = 0; i < length; i++) {
      buffer.writeIntLE(i);
    }
    buffer.writeIntLE(-42);

    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer);
    Variant variant = decoder.decodeVariant();

    Integer[] values = assertInstanceOf(Integer[].class, variant.value());
    assertEquals(length, values.length);
    for (int i = 0; i < length; i++) {
      assertEquals(i, values[i], "element " + i);
    }
    assertEquals(-42, decoder.decodeInt32(), "decoder did not stop at the end of the array");
  }

  // Same as above, through a structure array, whose component type comes from the codec.
  @Test
  void largeStructArrayDecodesEveryElement() {
    int length = 10_000;

    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(length);
    for (int i = 0; i < length; i++) {
      buffer.writeDoubleLE(i);
      buffer.writeFloatLE(-i);
    }
    buffer.writeIntLE(-42);

    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(CONTEXT).setBuffer(buffer);
    UaStructuredType[] values = decoder.decodeStructArray(null, localId(XVType.TYPE_ID));

    XVType[] xvs = assertInstanceOf(XVType[].class, values);
    assertEquals(length, xvs.length);
    for (int i = 0; i < length; i++) {
      assertEquals(new XVType((double) i, (float) -i), xvs[i], "element " + i);
    }
    assertEquals(-42, decoder.decodeInt32(), "decoder did not stop at the end of the array");
  }

  private static Arguments lengthCase(
      String name, Consumer<ByteBuf> write, Function<OpcUaBinaryDecoder, ?> decode) {

    ByteBuf buffer = Unpooled.buffer();
    write.accept(buffer);

    return Arguments.of(name, buffer, decode);
  }

  private static NodeId localId(ExpandedNodeId id) {
    return id.toNodeId(CONTEXT.getNamespaceTable()).orElseThrow();
  }

  /** Returns the bytes this thread allocated while running {@code action}. */
  private static long allocatedBytesDuring(Runnable action) {
    var threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
    assumeTrue(
        threads.isThreadAllocatedMemorySupported() && threads.isThreadAllocatedMemoryEnabled(),
        "per-thread allocation counters are not available");

    long before = threads.getCurrentThreadAllocatedBytes();
    action.run();
    return threads.getCurrentThreadAllocatedBytes() - before;
  }
}
