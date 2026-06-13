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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Bit-exact golden vectors for the UADP codec, hand-derived from the OPC UA Part 14 v1.05 wire
 * format tables (§7.2.4.4.2 Table 154, §7.2.4.5.2-5.8 Tables 160-164, §7.2.4.6 Tables 168/170).
 *
 * <p>Every expected byte array is derived BY HAND from the spec tables, byte by byte, and doubles
 * as the regression fixture set for future interop work. Each vector is encoded and compared
 * bit-exactly, then decoded again and compared against the expected message model.
 */
class UadpGoldenVectorTest {

  /** Wire GroupVersion used by all vectors that carry a GroupHeader. */
  private static final UInteger GROUP_VERSION = uint(0x01020304L);

  /** Wire NetworkMessageNumber used by all vectors that carry a GroupHeader. */
  private static final UShort NETWORK_MESSAGE_NUMBER = ushort(1);

  /** Wire NetworkMessage SequenceNumber used by all vectors that carry a GroupHeader. */
  private static final UShort NETWORK_MESSAGE_SEQUENCE = ushort(16);

  /** DataSetMessage content mask with no optional header fields (DataSetFlags1 only). */
  private static final UadpDataSetMessageContentMask DSM_NONE =
      new UadpDataSetMessageContentMask(uint(0x00));

  /** Empty field content mask: fields are encoded as Variants (Part 14 §6.2.4.2). */
  private static final DataSetFieldContentMask VARIANT_FIELDS =
      new DataSetFieldContentMask(uint(0x00));

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region golden vectors

  /**
   * Minimal "UADP-Dynamic" NetworkMessage (Annex A.2.2): PublisherId + PayloadHeader, one key frame
   * DataSetMessage with Variant fields and no optional DataSetMessage header fields.
   */
  @Test
  void minimalNetworkMessage() throws UaException {
    // NM mask 0x41: bit 0 PublisherId | bit 6 PayloadHeader (Table 97).
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x41), writer);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(42)),
            group,
            null,
            keyFrame(
                writer, 0, goodValue(Variant.ofInt32(42)), goodValue(Variant.ofBoolean(true))));

    byte[] expected =
        bytes(
            0x51, // byte 0: UADPVersion 1 (bits 0-3) | PublisherId 0x10 | PayloadHeader 0x40
            //         no ExtendedFlags1: PublisherId type Byte = 000, all other bits false
            0x2A, // PublisherId: Byte = 42
            0x01, // PayloadHeader: Count = 1
            0x01,
            0x00, // PayloadHeader: DataSetWriterIds[0] = 1 (UInt16 LE)
            //         no Sizes array: PayloadHeader enabled but Count == 1 (Table 161)
            0x01, // DataSetFlags1: valid 0x01 | field encoding 00 (Variant) | no options
            0x02,
            0x00, // FieldCount = 2 (UInt16 LE)
            0x06,
            0x2A,
            0x00,
            0x00,
            0x00, // field 0: Variant, type Int32 (6), value 42
            0x01,
            0x01); // field 1: Variant, type Boolean (1), value true

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(PublisherId.ubyte(ubyte(42)), decoded.publisherId());
    assertNull(decoded.writerGroupId());
    assertNull(decoded.groupVersion());
    assertNull(decoded.networkMessageNumber());
    assertNull(decoded.sequenceNumber());
    assertNull(decoded.timestamp());
    assertTrue(decoded.metaData().isEmpty());
    assertEquals(1, decoded.messages().size());

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(1), message.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
    assertTrue(message.valid());
    assertNull(message.sequenceNumber());
    assertNull(message.timestamp());
    assertNull(message.status());
    assertNull(message.configurationVersion());
    assertEquals(
        List.of(
            new DecodedField(0, goodValue(Variant.ofInt32(42))),
            new DecodedField(1, goodValue(Variant.ofBoolean(true)))),
        message.fields());
  }

  private static Stream<Arguments> publisherIdVectors() {
    return Stream.of(
        // Type bits 000 = Byte: ExtendedFlags1 would be 0 so it is omitted (byte 0 bit 7 false).
        Arguments.of(
            "Byte",
            PublisherId.ubyte(ubyte(42)),
            bytes(
                0x11, // byte 0: version 1 | PublisherId 0x10
                0x2A)), // PublisherId: Byte = 42
        Arguments.of(
            "UInt16",
            PublisherId.uint16(ushort(0x1234)),
            bytes(
                0x91, // byte 0: version 1 | PublisherId 0x10 | ExtendedFlags1 0x80
                0x01, // ExtendedFlags1: PublisherId type 001 = UInt16
                0x34, 0x12)), // PublisherId: UInt16 LE = 0x1234
        Arguments.of(
            "UInt32",
            PublisherId.uint32(uint(0xDEADBEEFL)),
            bytes(
                0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
                0x02, // ExtendedFlags1: PublisherId type 010 = UInt32
                0xEF, 0xBE, 0xAD, 0xDE)), // PublisherId: UInt32 LE = 0xDEADBEEF
        Arguments.of(
            "UInt64",
            PublisherId.uint64(ulong(0x0102030405060708L)),
            bytes(
                0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
                0x03, // ExtendedFlags1: PublisherId type 011 = UInt64
                0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01)), // UInt64 LE
        Arguments.of(
            "String",
            PublisherId.string("pub"),
            bytes(
                0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
                0x04, // ExtendedFlags1: PublisherId type 100 = String
                0x03, 0x00, 0x00, 0x00, // String length = 3 (Int32 LE)
                0x70, 0x75, 0x62))); // "pub" UTF-8
  }

  /** Each PublisherId width (Table 154 / §7.2.4.4.2; type bits per ExtendedFlags1 bits 0-2). */
  @ParameterizedTest(name = "{0}")
  @MethodSource("publisherIdVectors")
  void publisherIdWidths(String name, PublisherId publisherId, byte[] headerBytes)
      throws UaException {

    // NM mask 0x01: PublisherId only; no PayloadHeader, so no Count/DataSetWriterIds on the wire.
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x01), writer);

    EncodeContext context =
        encodeContext(publisherId, group, null, keyFrame(writer, 0, goodValue(Variant.ofInt32(7))));

    byte[] dataSetMessage =
        bytes(
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x07, 0x00, 0x00, 0x00); // Variant Int32 = 7

    byte[] expected = concat(headerBytes, dataSetMessage);

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(publisherId, decoded.publisherId());
    assertEquals(1, decoded.messages().size());

    DecodedDataSetMessage message = decoded.messages().get(0);
    // No PayloadHeader: the DataSetWriterId is unknown to the decoder.
    assertNull(message.dataSetWriterId());
    assertTrue(message.valid());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(7)))), message.fields());
  }

  /** Full GroupHeader: WriterGroupId + GroupVersion + NetworkMessageNumber + SequenceNumber. */
  @Test
  void groupHeaderFull() throws UaException {
    // NM mask 0x3E: bit 1 GroupHeader | bit 2 WriterGroupId | bit 3 GroupVersion |
    // bit 4 NetworkMessageNumber | bit 5 SequenceNumber (Table 97).
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x3E), writer);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(1)),
            group,
            null,
            keyFrame(writer, 0, goodValue(Variant.ofInt32(7))));

    byte[] expected =
        bytes(
            0x21, // byte 0: version 1 | GroupHeader 0x20 (PublisherId mask bit not set)
            0x0F, // GroupFlags: WriterGroupId 0x01 | GroupVersion 0x02 |
            //               NetworkMessageNumber 0x04 | SequenceNumber 0x08 (Table 154)
            0x02,
            0x01, // WriterGroupId = 258 (UInt16 LE)
            0x04,
            0x03,
            0x02,
            0x01, // GroupVersion = 0x01020304 (VersionTime, UInt32 LE)
            0x01,
            0x00, // NetworkMessageNumber = 1 (UInt16 LE)
            0x10,
            0x00, // SequenceNumber = 16 (UInt16 LE)
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01,
            0x00, // FieldCount = 1
            0x06,
            0x07,
            0x00,
            0x00,
            0x00); // Variant Int32 = 7

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertNull(decoded.publisherId());
    assertEquals(ushort(258), decoded.writerGroupId());
    assertEquals(GROUP_VERSION, decoded.groupVersion());
    assertEquals(NETWORK_MESSAGE_NUMBER, decoded.networkMessageNumber());
    assertEquals(NETWORK_MESSAGE_SEQUENCE, decoded.sequenceNumber());
    assertEquals(1, decoded.messages().size());
    assertTrue(decoded.messages().get(0).valid());
  }

  /** Multi-writer NetworkMessage: PayloadHeader with Count > 1 requires the Sizes array. */
  @Test
  void multiWriterWithSizes() throws UaException {
    // NM mask 0x41: PublisherId | PayloadHeader.
    DataSetWriterConfig writer1 = writer(1, DSM_NONE, VARIANT_FIELDS, 0);
    DataSetWriterConfig writer2 = writer(2, DSM_NONE, VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x41), writer1, writer2);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(7)),
            group,
            null,
            keyFrame(writer1, 0, goodValue(Variant.ofInt32(42))),
            keyFrame(writer2, 0, goodValue(Variant.ofBoolean(true))));

    byte[] expected =
        bytes(
            0x51, // byte 0: version 1 | PublisherId 0x10 | PayloadHeader 0x40
            0x07, // PublisherId: Byte = 7
            0x02, // PayloadHeader: Count = 2
            0x01,
            0x00, // PayloadHeader: DataSetWriterIds[0] = 1
            0x02,
            0x00, // PayloadHeader: DataSetWriterIds[1] = 2
            // Sizes: present because PayloadHeader enabled AND Count > 1 (Table 161)
            0x08,
            0x00, // Sizes[0] = 8 bytes
            0x05,
            0x00, // Sizes[1] = 5 bytes
            // DataSetMessage 1 (8 bytes):
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01,
            0x00, // FieldCount = 1
            0x06,
            0x2A,
            0x00,
            0x00,
            0x00, // Variant Int32 = 42
            // DataSetMessage 2 (5 bytes):
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01,
            0x00, // FieldCount = 1
            0x01,
            0x01); // Variant Boolean = true

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertEquals(2, decoded.messages().size());

    DecodedDataSetMessage message1 = decoded.messages().get(0);
    assertEquals(ushort(1), message1.dataSetWriterId());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))), message1.fields());

    DecodedDataSetMessage message2 = decoded.messages().get(1);
    assertEquals(ushort(2), message2.dataSetWriterId());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofBoolean(true)))), message2.fields());
  }

  /** NetworkMessage header Timestamp + PicoSeconds (ExtendedFlags1 bits 5 and 6). */
  @Test
  void timestampAndPicoSecondsHeader() throws UaException {
    // NM mask 0x180: bit 7 Timestamp | bit 8 PicoSeconds (Table 97).
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x180), writer);

    DateTime timestamp = new DateTime(0x0807060504030201L);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(1)),
            group,
            timestamp,
            keyFrame(writer, 0, goodValue(Variant.ofInt32(7))));

    byte[] expected =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1 0x80
            0x60, // ExtendedFlags1: Timestamp 0x20 | PicoSeconds 0x40
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // Timestamp (Int64 LE)
            0x00, 0x00, // PicoSeconds = 0 (no sub-100ns source)
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x07, 0x00, 0x00, 0x00); // Variant Int32 = 7

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(timestamp, decoded.timestamp());
    assertEquals(1, decoded.messages().size());
    assertTrue(decoded.messages().get(0).valid());
  }

  /**
   * DataSetMessage header with SequenceNumber + Timestamp + Status + MajorVersion + MinorVersion.
   *
   * <p>Verifies the exact header field ORDER of Table 162: the sequence number comes BEFORE the
   * timestamp, and Status comes AFTER it, even though Status is gated by DataSetFlags1 and the
   * timestamp by DataSetFlags2.
   */
  @Test
  void dataSetMessageHeaderFieldOrder() throws UaException {
    // NM mask 0x00: bare NetworkMessage, every optional header off; payload = one DSM.
    // DSM mask 0x3D: bit 0 Timestamp | bit 2 Status | bit 3 MajorVersion |
    // bit 4 MinorVersion | bit 5 SequenceNumber (Table 101). No PicoSeconds.
    DataSetWriterConfig writer =
        writer(1, new UadpDataSetMessageContentMask(uint(0x3D)), VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x00), writer);

    DataSetMessageDraft draft =
        DataSetMessageDraft.of(
            writer,
            uint(7),
            new DateTime(0x0102030405060708L),
            new StatusCode(0x80000000L), // Bad
            new ConfigurationVersionDataType(uint(5), uint(6)),
            false,
            List.of(goodValue(Variant.ofInt32(42))));

    EncodeContext context = encodeContext(PublisherId.ubyte(ubyte(1)), group, null, draft);

    byte[] expected =
        bytes(
            0x01, // byte 0: version 1, all optional NetworkMessage headers off
            0xF9, // DataSetFlags1: valid 0x01 | seq 0x08 | status 0x10 |
            //                 major 0x20 | minor 0x40 | DataSetFlags2 0x80
            0x10, // DataSetFlags2: type 0000 Data Key Frame | Timestamp 0x10
            0x07,
            0x00, // DataSetMessageSequenceNumber = 7 (BEFORE the timestamp)
            0x08,
            0x07,
            0x06,
            0x05,
            0x04,
            0x03,
            0x02,
            0x01, // Timestamp (Int64 LE)
            0x00,
            0x80, // Status = high 16 bits of 0x80000000 (AFTER the timestamp)
            0x05,
            0x00,
            0x00,
            0x00, // ConfigurationVersion MajorVersion = 5
            0x06,
            0x00,
            0x00,
            0x00, // ConfigurationVersion MinorVersion = 6
            0x01,
            0x00, // FieldCount = 1
            0x06,
            0x2A,
            0x00,
            0x00,
            0x00); // Variant Int32 = 42

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);

    assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
    assertTrue(message.valid());
    assertEquals(uint(7), message.sequenceNumber());
    assertEquals(new DateTime(0x0102030405060708L), message.timestamp());
    // The header Status is the high-order 16 bits of a StatusCode.
    assertEquals(new StatusCode(0x80000000L), message.status());
    assertEquals(
        new ConfigurationVersionDataType(uint(5), uint(6)), message.configurationVersion());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))), message.fields());
  }

  /**
   * DataValue field encoding with a member subset: only StatusCode and SourceTimestamp selected;
   * picoseconds and server timestamp members are stripped on the wire (Part 14 §6.2.4.2).
   */
  @Test
  void dataValueFieldEncodingWithMemberSubset() throws UaException {
    // Field mask 0x03: bit 0 StatusCode | bit 1 SourceTimestamp (Table 32).
    DataSetWriterConfig writer = writer(1, DSM_NONE, new DataSetFieldContentMask(uint(0x03)), 0);
    WriterGroupConfig group = group(uint(0x00), writer);

    DataValue field =
        new DataValue(
            Variant.ofInt32(42),
            new StatusCode(0x40000000L), // Uncertain
            new DateTime(10L),
            ushort(3), // SourcePicoSeconds: NOT selected, must not appear on the wire
            new DateTime(20L), // ServerTimestamp: NOT selected, must not appear on the wire
            ushort(4)); // ServerPicoSeconds: NOT selected

    EncodeContext context =
        encodeContext(PublisherId.ubyte(ubyte(1)), group, null, keyFrame(writer, 0, field));

    byte[] expected =
        bytes(
            0x01, // byte 0: version 1, all optional NetworkMessage headers off
            0x05, // DataSetFlags1: valid 0x01 | field encoding 10 = DataValue (bits 1-2)
            0x01,
            0x00, // FieldCount = 1
            // Part 6 DataValue: mask 0x07 = Value 0x01 | StatusCode 0x02 | SourceTimestamp 0x04
            0x07,
            0x06,
            0x2A,
            0x00,
            0x00,
            0x00, // Value: Variant Int32 = 42
            0x00,
            0x00,
            0x00,
            0x40, // StatusCode = 0x40000000 (Uncertain)
            0x0A,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00); // SourceTimestamp = DateTime(10)

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(1, message.fields().size());

    // Absent DataValue members decode to their Part 6 defaults (MIN_VALUE timestamps).
    assertEquals(
        new DataValue(
            Variant.ofInt32(42),
            new StatusCode(0x40000000L),
            new DateTime(10L),
            null,
            DateTime.MIN_VALUE,
            null),
        message.fields().get(0).value());
  }

  /** Keep Alive DataSetMessage: header only, no body at all (§7.2.4.5.8). */
  @Test
  void keepAliveDataSetMessage() throws UaException {
    // NM mask 0x41: PublisherId | PayloadHeader. DSM mask 0x20: SequenceNumber.
    DataSetWriterConfig writer =
        writer(4, new UadpDataSetMessageContentMask(uint(0x20)), VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x41), writer);

    DataSetMessageDraft keepAlive =
        DataSetMessageDraft.of(
            writer,
            uint(5), // the NEXT EXPECTED sequence number; keep-alives do not increment
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            true,
            List.of());

    EncodeContext context = encodeContext(PublisherId.ubyte(ubyte(9)), group, null, keepAlive);

    byte[] expected =
        bytes(
            0x51, // byte 0: version 1 | PublisherId 0x10 | PayloadHeader 0x40
            0x09, // PublisherId: Byte = 9
            0x01, // PayloadHeader: Count = 1
            0x04, 0x00, // PayloadHeader: DataSetWriterIds[0] = 4
            0x89, // DataSetFlags1: valid 0x01 | seq 0x08 | DataSetFlags2 0x80
            0x03, // DataSetFlags2: type 0011 = Keep Alive
            0x05, 0x00); // DataSetMessageSequenceNumber = 5; NO body follows

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);

    assertEquals(ushort(4), message.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEEP_ALIVE, message.kind());
    assertTrue(message.valid());
    assertEquals(uint(5), message.sequenceNumber());
    assertTrue(message.fields().isEmpty());
  }

  /**
   * Data Delta Frame DataSetMessage (§7.2.4.5.6 Table 164): the message type is signaled in the
   * DataSetFlags2 type bits ({@code 0001}, §7.2.4.5.4 Table 162), and the body is {@code
   * FieldCount} followed by {@code FieldCount} pairs of explicit {@code FieldIndex} (UInt16, the
   * field's position in the DataSetMetaData, each used at most once) and the field value in the
   * same Variant field encoding as key frames.
   */
  @Test
  void deltaFrameDataSetMessage() throws UaException {
    // NM mask 0x00: bare NetworkMessage, every optional header off. DSM mask 0x20: SequenceNumber.
    DataSetWriterConfig writer =
        writer(1, new UadpDataSetMessageContentMask(uint(0x20)), VARIANT_FIELDS, 0);
    WriterGroupConfig group = group(uint(0x00), writer);

    DataSetMessageDraft draft =
        deltaFrame(
            writer,
            7,
            new DataSetMessageDraft.DeltaField(1, goodValue(Variant.ofInt32(42))),
            new DataSetMessageDraft.DeltaField(3, goodValue(Variant.ofBoolean(true))));

    EncodeContext context = encodeContext(PublisherId.ubyte(ubyte(1)), group, null, draft);

    byte[] expected =
        bytes(
            0x01, // byte 0: version 1, all optional NetworkMessage headers off
            0x89, // DataSetFlags1: valid 0x01 | Variant encoding 00 | seq 0x08 | Flags2 0x80
            0x01, // DataSetFlags2: type 0001 = Data Delta Frame (Table 162)
            0x07, 0x00, // DataSetMessageSequenceNumber = 7 (UInt16 LE)
            0x02, 0x00, // FieldCount = 2 (Table 164)
            0x01, 0x00, // FieldIndex = 1 (position in the DataSetMetaData)
            0x06, 0x2A, 0x00, 0x00, 0x00, // FieldValue: Variant, type Int32 (6), value 42
            0x03, 0x00, // FieldIndex = 3
            0x01, 0x01); // FieldValue: Variant, type Boolean (1), value true

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);

    assertEquals(DataSetMessageKind.DELTA_FRAME, message.kind());
    assertTrue(message.valid());
    assertEquals(uint(7), message.sequenceNumber());
    assertEquals(
        List.of(
            new DecodedField(1, goodValue(Variant.ofInt32(42))),
            new DecodedField(3, goodValue(Variant.ofBoolean(true)))),
        message.fields());
  }

  /** ConfiguredSize larger than the encoded message: zero-padding (Part 14 §6.3.1.3.3). */
  @Test
  void configuredSizePadsWithZeroBytes() throws UaException {
    // ConfiguredSize = 12; the encoded DataSetMessage is 8 bytes, so 4 zero padding bytes.
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 12);
    WriterGroupConfig group = group(uint(0x41), writer);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(5)),
            group,
            null,
            keyFrame(writer, 0, goodValue(Variant.ofInt32(42))));

    byte[] expected =
        bytes(
            0x51, // byte 0: version 1 | PublisherId | PayloadHeader
            0x05, // PublisherId: Byte = 5
            0x01, // PayloadHeader: Count = 1
            0x01, 0x00, // PayloadHeader: DataSetWriterIds[0] = 1
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00, // Variant Int32 = 42
            0x00, 0x00, 0x00, 0x00); // zero padding up to ConfiguredSize = 12

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);
    assertTrue(message.valid());
    // Padding bytes after the fields are ignored by the decoder.
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))), message.fields());
  }

  /**
   * ConfiguredSize smaller than the encoded message: the message is re-encoded header-only with the
   * DataSetFlags1 "valid" bit clear, padded to ConfiguredSize (Part 14 §6.3.1.3.3).
   */
  @Test
  void configuredSizeOverflowSendsInvalidHeaderOnly() throws UaException {
    // ConfiguredSize = 3; the encoded DataSetMessage would be far larger.
    DataSetWriterConfig writer = writer(1, DSM_NONE, VARIANT_FIELDS, 3);
    WriterGroupConfig group = group(uint(0x41), writer);

    EncodeContext context =
        encodeContext(
            PublisherId.ubyte(ubyte(5)),
            group,
            null,
            keyFrame(writer, 0, goodValue(Variant.ofString("does not fit in 3 bytes"))));

    byte[] expected =
        bytes(
            0x51, // byte 0: version 1 | PublisherId | PayloadHeader
            0x05, // PublisherId: Byte = 5
            0x01, // PayloadHeader: Count = 1
            0x01, 0x00, // PayloadHeader: DataSetWriterIds[0] = 1
            0x00, // DataSetFlags1: valid bit CLEAR; no body follows
            0x00, 0x00); // zero padding up to ConfiguredSize = 3

    assertArrayEquals(expected, encodeToBytes(context));

    DecodedNetworkMessage decoded = decode(expected);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(1), message.dataSetWriterId());
    assertFalse(message.valid());
    assertTrue(message.fields().isEmpty());
  }

  // endregion

  // region discovery DataSetMetaData announcement (decode-only fixture)

  /**
   * Discovery announcement framing per §7.2.4.6.3 (byte 0 = 0x91, ExtendedFlags1 = security +
   * ExtendedFlags2 present, ExtendedFlags2 NM type 010) followed by the announcement header (Table
   * 168) and the DataSetMetaData body (Table 170), with the trailing StatusCode supplied by the
   * caller.
   */
  private static byte[] metaDataAnnouncement(byte[] statusCodeBytes) {
    byte[] header =
        bytes(
            0x91, // byte 0: version 1 | PublisherId 0x10 | ExtendedFlags1 0x80;
            //         GroupHeader/PayloadHeader forced off for announcements
            0x90, // ExtendedFlags1: PublisherId type 000 (Byte) | SecurityHeader 0x10 |
            //                  ExtendedFlags2 0x80 (announcements always carry a SecurityHeader)
            0x08, // ExtendedFlags2: NetworkMessage type 010 (discovery announcement) << 2
            0x07, // PublisherId: Byte = 7
            // SecurityHeader; SecurityFlags = 0 indicates security mode None:
            0x00, // SecurityFlags = 0
            0x00,
            0x00,
            0x00,
            0x00, // SecurityTokenId = 0 (IntegerId, UInt32 LE)
            0x00, // NonceLength = 0 (no MessageNonce bytes)
            // Discovery announcement header (Table 168):
            0x02, // AnnouncementType = 2 (DataSetMetaData)
            0x01,
            0x00, // SequenceNumber = 1 (per-PublisherId; not surfaced by the decoder)
            // DataSetMetaData announcement body (Table 170):
            0x0A,
            0x00); // DataSetWriterId = 10

    byte[] metaDataStruct =
        bytes(
            // DataSetMetaDataType, standard OPC UA Binary encoding, inline (no ExtensionObject):
            0xFF,
            0xFF,
            0xFF,
            0xFF, // Namespaces: null array (length -1)
            0xFF,
            0xFF,
            0xFF,
            0xFF, // StructureDataTypes: null array
            0xFF,
            0xFF,
            0xFF,
            0xFF, // EnumDataTypes: null array
            0xFF,
            0xFF,
            0xFF,
            0xFF, // SimpleDataTypes: null array
            0x02,
            0x00,
            0x00,
            0x00,
            0x44,
            0x53, // Name = "DS"
            0x00, // Description: null LocalizedText (encoding mask 0)
            0xFF,
            0xFF,
            0xFF,
            0xFF, // Fields: null array
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00, // DataSetClassId: null Guid
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00, // (16 zero bytes total)
            0x07,
            0x00,
            0x00,
            0x00, // ConfigurationVersion.MajorVersion = 7
            0x03,
            0x00,
            0x00,
            0x00); // ConfigurationVersion.MinorVersion = 3

    return concat(header, metaDataStruct, statusCodeBytes);
  }

  /** AnnouncementType 2 with a Good status decodes into a {@link DecodedMetaData}. */
  @Test
  void metaDataAnnouncementDecode() {
    byte[] message = metaDataAnnouncement(bytes(0x00, 0x00, 0x00, 0x00)); // StatusCode = Good

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    // The announcement SequenceNumber is not a NetworkMessage SequenceNumber.
    assertNull(decoded.sequenceNumber());
    assertTrue(decoded.messages().isEmpty());
    assertEquals(1, decoded.metaData().size());

    DecodedMetaData metaData = decoded.metaData().get(0);
    assertEquals(ushort(10), metaData.dataSetWriterId());

    DataSetMetaDataType expected =
        new DataSetMetaDataType(
            null,
            null,
            null,
            null,
            "DS",
            new LocalizedText(null, null),
            null,
            new UUID(0L, 0L),
            new ConfigurationVersionDataType(uint(7), uint(3)));
    assertEquals(expected, metaData.metaData());
  }

  /** An announcement with a Bad status means the Publisher has no metadata: not recorded. */
  @Test
  void metaDataAnnouncementWithBadStatusIsIgnored() {
    byte[] message = metaDataAnnouncement(bytes(0x00, 0x00, 0x00, 0x80)); // StatusCode = Bad

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  // endregion

  // region helpers

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private static byte[] concat(byte[]... arrays) {
    int length = 0;
    for (byte[] array : arrays) {
      length += array.length;
    }
    byte[] result = new byte[length];
    int offset = 0;
    for (byte[] array : arrays) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
  }

  private static DataValue goodValue(Variant value) {
    return new DataValue(value, StatusCode.GOOD, null, null, null, null);
  }

  private static DataSetWriterConfig writer(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      DataSetFieldContentMask fieldContentMask,
      int configuredSize) {

    return DataSetWriterConfig.builder("writer-" + dataSetWriterId)
        .dataSet(new PublishedDataSetRef("ds"))
        .dataSetWriterId(ushort(dataSetWriterId))
        .fieldContentMask(fieldContentMask)
        .settings(
            UadpDataSetWriterSettings.builder()
                .dataSetMessageContentMask(dataSetMessageContentMask)
                .configuredSize(ushort(configuredSize))
                .build())
        .build();
  }

  private static WriterGroupConfig group(
      UInteger networkMessageContentMask, DataSetWriterConfig... writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder("group")
            .writerGroupId(ushort(258))
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(
                        new UadpNetworkMessageContentMask(networkMessageContentMask))
                    .build());
    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static DataSetMessageDraft keyFrame(
      DataSetWriterConfig writer, int sequenceNumber, DataValue... fields) {

    return DataSetMessageDraft.of(
        writer,
        uint(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        false,
        List.of(fields));
  }

  private static DataSetMessageDraft deltaFrame(
      DataSetWriterConfig writer, int sequenceNumber, DataSetMessageDraft.DeltaField... fields) {

    return DataSetMessageDraft.ofDeltaFrame(
        writer,
        uint(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        List.of(fields),
        null);
  }

  private EncodeContext encodeContext(
      PublisherId publisherId,
      WriterGroupConfig group,
      @Nullable DateTime timestamp,
      DataSetMessageDraft... drafts) {

    return new EncodeContext(
        encodingContext,
        publisherId,
        group,
        GROUP_VERSION,
        NETWORK_MESSAGE_NUMBER,
        NETWORK_MESSAGE_SEQUENCE,
        timestamp,
        List.of(drafts));
  }

  private byte[] encodeToBytes(EncodeContext context) throws UaException {
    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size(), "UADP encode returns a singleton list");
    try {
      return ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }
  }

  private DecodedNetworkMessage decode(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decode(new DecodeContext(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  // endregion
}
