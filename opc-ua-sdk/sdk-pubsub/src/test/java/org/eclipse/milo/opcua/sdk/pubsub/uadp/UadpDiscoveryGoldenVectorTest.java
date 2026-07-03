/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Bit-exact golden vectors for the UADP discovery codec, hand-derived from the OPC UA Part 14 v1.05
 * wire format tables: the discovery NetworkMessage header (§7.2.4.6.3 / §7.2.4.6.12.1), the
 * DataSetMetaData probe (§7.2.4.6.12.3-4, Tables 178-180), and the DataSetMetaData announcement
 * (§7.2.4.6.3-4, Tables 168/170).
 *
 * <p>Every expected byte array is derived BY HAND from the spec tables, byte by byte. Each vector
 * is encoded and compared bit-exactly, then decoded again via {@link
 * UadpMessageMapping#decodeMessage} and compared against the expected message model.
 */
class UadpDiscoveryGoldenVectorTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region probe vectors

  private static Stream<Arguments> probeVectors() {
    return Stream.of(
        // §3 worked example: target publisher Byte 7, DataSetWriterId 100, security None.
        Arguments.of(
            "Byte",
            PublisherId.ubyte(ubyte(7)),
            bytes(
                0x91, // byte 0: UADPVersion 1 | PublisherId 0x10 | ExtendedFlags1 0x80
                0x90, // ExtendedFlags1: SecurityHeader 0x10 | ExtendedFlags2 0x80 |
                //         PublisherId type 000 (Byte)
                0x04, // ExtendedFlags2: NetworkMessage type 001 = discovery probe; no chunk,
                //         no PromotedFields, no ActionHeader
                0x07, // PublisherId: Byte = 7 -- the TARGET publisher
                0x00, // SecurityFlags = 0 (None)
                0x00,
                0x00,
                0x00,
                0x00, // SecurityTokenId = 0 (UInt32 LE)
                0x00, // NonceLength = 0
                0x01, // ProbeType = 1 (Publisher information, Table 178)
                0x02, // InformationType = 2 (DataSetMetaData, Table 179)
                0x01,
                0x00,
                0x00,
                0x00, // DataSetWriterIds count = 1 (Int32 LE, Table 180)
                0x64,
                0x00)), // DataSetWriterIds[0] = 100 (UInt16 LE)
        // The same probe with a UInt16 PublisherId 0x1234: ExtendedFlags1 gains type 001 and
        // the id widens to two bytes (19-byte datagram).
        Arguments.of(
            "UInt16",
            PublisherId.uint16(ushort(0x1234)),
            bytes(
                0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
                0x91, // ExtendedFlags1: SecurityHeader | ExtendedFlags2 | type 001 (UInt16)
                0x04, // ExtendedFlags2: discovery probe
                0x34, 0x12, // PublisherId: UInt16 LE = 0x1234
                0x00, // SecurityFlags = 0
                0x00, 0x00, 0x00, 0x00, // SecurityTokenId = 0
                0x00, // NonceLength = 0
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0x01, 0x00, 0x00, 0x00, // DataSetWriterIds count = 1
                0x64, 0x00)), // DataSetWriterIds[0] = 100
        // String PublisherId "pub": type bits 100, standard length-prefixed UTF-8 string.
        Arguments.of(
            "String",
            PublisherId.string("pub"),
            bytes(
                0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
                0x94, // ExtendedFlags1: SecurityHeader | ExtendedFlags2 | type 100 (String)
                0x04, // ExtendedFlags2: discovery probe
                0x03, 0x00, 0x00, 0x00, // PublisherId: String length = 3 (Int32 LE)
                0x70, 0x75, 0x62, // "pub" UTF-8
                0x00, // SecurityFlags = 0
                0x00, 0x00, 0x00, 0x00, // SecurityTokenId = 0
                0x00, // NonceLength = 0
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0x01, 0x00, 0x00, 0x00, // DataSetWriterIds count = 1
                0x64, 0x00))); // DataSetWriterIds[0] = 100
  }

  /**
   * DataSetMetaData probe golden vectors; note the probe carries NO sequence number anywhere (Table
   * 178's only field is ProbeType) and the header PublisherId is the probed Publisher's.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("probeVectors")
  void probeEncodesByteExact(String name, PublisherId targetPublisherId, byte[] expected)
      throws UaException {

    UadpDiscoveryProbe probe = UadpDiscoveryProbe.of(targetPublisherId, List.of(ushort(100)));

    assertArrayEquals(expected, encodeProbeToBytes(probe));

    // The same bytes decode back into an equal probe model.
    UadpDecodedMessage decoded = decodeMessage(expected);
    assertEquals(probe, assertInstanceOf(UadpDiscoveryProbe.class, decoded));
  }

  // endregion

  // region announcement vector

  /**
   * §4 worked example: publisher Byte id 7, announcement sequence 12, DataSetWriterId 100, minimal
   * metadata (name "Demo", null arrays/fields, null DataSetClassId, ConfigurationVersion 1/1),
   * status Good, security None -- 72 bytes.
   */
  @Test
  void announcementEncodesByteExact() throws UaException {
    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)), ushort(12), ushort(100), demoMetaData(), StatusCode.GOOD);

    byte[] expected =
        bytes(
            0x91, // byte 0: version 1 | PublisherId 0x10 | ExtendedFlags1 0x80
            0x90, // ExtendedFlags1: SecurityHeader | ExtendedFlags2 | type 000 (Byte)
            0x08, // ExtendedFlags2: NetworkMessage type 010 = discovery announcement
            0x07, // PublisherId: Byte = 7 -- the SENDER's id
            0x00, // SecurityFlags = 0 (None)
            0x00,
            0x00,
            0x00,
            0x00, // SecurityTokenId = 0
            0x00, // NonceLength = 0
            0x02, // AnnouncementType = 2 (DataSetMetaData, Table 168)
            0x0C,
            0x00, // announcement SequenceNumber = 12 (UInt16 LE, per-PublisherId counter)
            0x64,
            0x00, // DataSetWriterId = 100 (scalar, Table 170)
            // DataSetMetaDataType, inline (no ExtensionObject wrapper), parent
            // DataTypeSchemaHeader fields first:
            0xFF,
            0xFF,
            0xFF,
            0xFF, // Namespaces = null (length -1)
            0xFF,
            0xFF,
            0xFF,
            0xFF, // StructureDataTypes = null
            0xFF,
            0xFF,
            0xFF,
            0xFF, // EnumDataTypes = null
            0xFF,
            0xFF,
            0xFF,
            0xFF, // SimpleDataTypes = null
            0x04,
            0x00,
            0x00,
            0x00, // Name length = 4
            0x44,
            0x65,
            0x6D,
            0x6F, // "Demo"
            0x00, // Description: LocalizedText with no locale/text (encoding mask 0)
            0xFF,
            0xFF,
            0xFF,
            0xFF, // Fields = null
            0x00,
            0x00,
            0x00,
            0x00, // DataSetClassId = null Guid (16 zero bytes)
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x01,
            0x00,
            0x00,
            0x00, // ConfigurationVersion.MajorVersion = 1
            0x01,
            0x00,
            0x00,
            0x00, // ConfigurationVersion.MinorVersion = 1
            0x00,
            0x00,
            0x00,
            0x00); // statusCode = Good

    byte[] encoded = encodeAnnouncementToBytes(announcement);

    assertEquals(72, encoded.length);
    assertArrayEquals(expected, encoded);

    // The same bytes decode back into an equal announcement model.
    UadpDecodedMessage decoded = decodeMessage(expected);
    assertEquals(announcement, assertInstanceOf(UadpMetaDataAnnouncement.class, decoded));
  }

  // endregion

  // region SecurityHeader

  /**
   * The SecurityHeader is mandatory for discovery messages even with security mode None
   * (§7.2.4.6.12.1) and is exactly 6 zero bytes: SecurityFlags 0, SecurityTokenId 0, NonceLength 0.
   */
  @Test
  void probeSecurityHeaderIsExactlySixZeroBytes() throws UaException {
    byte[] encoded =
        encodeProbeToBytes(
            UadpDiscoveryProbe.of(PublisherId.ubyte(ubyte(7)), List.of(ushort(100))));

    // Byte PublisherId: flag bytes 0-2, the id at byte 3; SecurityHeader occupies bytes 4-9.
    assertArrayEquals(new byte[6], Arrays.copyOfRange(encoded, 4, 10));
  }

  /** The announcement SecurityHeader is the same mandatory 6-zero-byte mode None shape. */
  @Test
  void announcementSecurityHeaderIsExactlySixZeroBytes() throws UaException {
    byte[] encoded =
        encodeAnnouncementToBytes(
            UadpMetaDataAnnouncement.of(
                PublisherId.ubyte(ubyte(7)),
                ushort(1),
                ushort(100),
                demoMetaData(),
                StatusCode.GOOD));

    // Byte PublisherId: flag bytes 0-2, the id at byte 3; SecurityHeader occupies bytes 4-9.
    assertArrayEquals(new byte[6], Arrays.copyOfRange(encoded, 4, 10));
  }

  // endregion

  // region helpers

  /** The minimal metadata of the §4 worked example: name "Demo", ConfigurationVersion 1/1. */
  private static DataSetMetaDataType demoMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        "Demo",
        new LocalizedText(null, null),
        null,
        new UUID(0L, 0L),
        new ConfigurationVersionDataType(uint(1), uint(1)));
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private byte[] encodeProbeToBytes(UadpDiscoveryProbe probe) throws UaException {
    EncodedNetworkMessage encoded = new UadpMessageMapping().encodeProbe(encodingContext, probe);
    try {
      return ByteBufUtil.getBytes(encoded.data());
    } finally {
      encoded.data().release();
    }
  }

  private byte[] encodeAnnouncementToBytes(UadpMetaDataAnnouncement announcement)
      throws UaException {

    EncodedNetworkMessage encoded =
        new UadpMessageMapping().encodeAnnouncement(encodingContext, announcement);
    try {
      return ByteBufUtil.getBytes(encoded.data());
    } finally {
      encoded.data().release();
    }
  }

  private UadpDecodedMessage decodeMessage(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decodeMessage(DecodeContext.of(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  // endregion
}
