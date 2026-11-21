/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class GuidConversionsTest {

  /**
   * The GUID used by every test in this class: Data1 = 0x00112233, Data2 = 0x4455, Data3 = 0x6677,
   * Data4 = 88 99 AA BB CC DD EE FF.
   */
  private static final UUID TEST_GUID = new UUID(0x0011223344556677L, 0x8899AABBCCDDEEFFL);

  /**
   * The OPC UA Part 6 §5.1.3 binary encoding of {@link #TEST_GUID}: Data1, Data2 and Data3 are
   * little-endian, Data4 is a raw byte array.
   */
  private static final byte[] TEST_GUID_WIRE_BYTES = {
    0x33, 0x22, 0x11, 0x00,
    0x55, 0x44,
    0x77, 0x66,
    (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE,
    (byte) 0xFF
  };

  @Test
  public void testGuidToByteString() {
    ByteString bs = GuidConversions.guidToByteString(TEST_GUID);

    assertEquals(ByteString.of(TEST_GUID_WIRE_BYTES), bs);
  }

  /**
   * The Guid → ByteString conversion must produce the same bytes the binary encoder writes for the
   * same GUID, so that a converted value is indistinguishable from a wire-encoded one.
   */
  @Test
  public void testGuidToByteStringMatchesTheBinaryEncoding() {
    ByteBuf buffer = Unpooled.buffer();
    new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
        .setBuffer(buffer)
        .encodeGuid(TEST_GUID);
    byte[] encoded = ByteBufUtil.getBytes(buffer);

    ByteString converted = GuidConversions.guidToByteString(TEST_GUID);

    assertEquals(ByteString.of(encoded), converted);
  }

  @Test
  public void testByteStringRoundTrip() {
    ByteString bs = GuidConversions.guidToByteString(TEST_GUID);

    UUID decoded = ByteStringConversions.byteStringToGuid(bs);

    assertEquals(TEST_GUID, decoded);
  }

  @Test
  public void testBinaryDecoderReadsTheConvertedBytesAsTheSameGuid() {
    ByteString bs = GuidConversions.guidToByteString(TEST_GUID);

    ByteBuf buffer = Unpooled.wrappedBuffer(bs.bytesOrEmpty());
    UUID decoded =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(buffer)
            .decodeGuid();

    assertEquals(TEST_GUID, decoded);
  }

  @ParameterizedTest
  @MethodSource("sampleGuids")
  public void testGuidByteStringRoundTripMatchesTheBinaryEncoding(UUID uuid) {
    // Guid -> ByteString must equal the binary encoding of the same GUID...
    ByteBuf encodeBuffer = Unpooled.buffer();
    new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
        .setBuffer(encodeBuffer)
        .encodeGuid(uuid);
    ByteString encoded = ByteString.of(ByteBufUtil.getBytes(encodeBuffer));
    assertEquals(encoded, GuidConversions.guidToByteString(uuid));

    // ...and ByteString -> Guid must decode to the original value...
    ByteBuf decodeBuffer = Unpooled.wrappedBuffer(encoded.bytesOrEmpty());
    UUID decoded =
        new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(decodeBuffer)
            .decodeGuid();
    assertEquals(uuid, decoded);

    // ...so the conversion round-trips through the wire representation.
    assertEquals(uuid, ByteStringConversions.byteStringToGuid(GuidConversions.guidToByteString(uuid)));
  }

  static Stream<UUID> sampleGuids() {
    return Stream.of(
        TEST_GUID,
        new UUID(0L, 0L),
        new UUID(-1L, -1L),
        UUID.nameUUIDFromBytes("eclipse-milo".getBytes()),
        UUID.randomUUID());
  }

  @Test
  public void testConvert() {
    ByteString bs = GuidConversions.guidToByteString(TEST_GUID);

    assertEquals(bs, GuidConversions.convert(TEST_GUID, OpcUaDataType.ByteString, false));
  }

  @Test
  public void testGuidToString() {
    UUID uuid = UUID.randomUUID();

    assertEquals(uuid.toString(), GuidConversions.guidToString(uuid));
  }
}
