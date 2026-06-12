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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Encode/decode round trips for the UADP codec across a matrix of content-mask combinations, status
 * propagation rules, delta-frame round trips against hand-derived Table 164 bytes, hand-built
 * event/heartbeat decoding, and the encode-side error contracts.
 *
 * <p>Wire-format expectations follow OPC UA Part 14 v1.05 §6.2.4.2, §6.3.1.1.4, §6.3.1.3.2, and
 * §7.2.4.4-7.2.4.5.
 */
class UadpRoundTripTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4660));

  private static final UShort WRITER_GROUP_ID = ushort(258);
  private static final UInteger GROUP_VERSION = uint(123456);
  private static final UShort NETWORK_MESSAGE_NUMBER = ushort(3);
  private static final UShort NETWORK_MESSAGE_SEQUENCE = ushort(99);
  private static final DateTime NETWORK_MESSAGE_TIMESTAMP = new DateTime(987654321L);

  private static final DateTime DSM_TIMESTAMP = new DateTime(1_000_000L);

  /** Uncertain_SubNormal (0x40950000); the wire carries only the high 16 bits, 0x4095. */
  private static final StatusCode DSM_STATUS = new StatusCode(0x40950000L);

  private static final UInteger MAJOR_VERSION = uint(5);
  private static final UInteger MINOR_VERSION = uint(6);

  /** Field values of varied builtin types, including an array and a null value. */
  private static final List<DataValue> FIELDS =
      List.of(
          new DataValue(
              Variant.ofInt32(42),
              StatusCode.GOOD,
              new DateTime(101L),
              ushort(1),
              new DateTime(201L),
              ushort(2)),
          new DataValue(
              Variant.ofString("hello"),
              StatusCode.GOOD,
              new DateTime(102L),
              ushort(3),
              new DateTime(202L),
              ushort(4)),
          new DataValue(
              Variant.ofInt32Array(new Integer[] {1, 2, 3}),
              StatusCode.GOOD,
              new DateTime(103L),
              null,
              new DateTime(203L),
              null),
          new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null, null, null, null),
          new DataValue(
              Variant.ofDouble(3.5),
              StatusCode.GOOD,
              new DateTime(104L),
              ushort(5),
              new DateTime(204L),
              ushort(6)),
          new DataValue(
              Variant.ofByteString(ByteString.of(new byte[] {1, 2, 3})),
              StatusCode.GOOD,
              new DateTime(105L),
              null,
              new DateTime(205L),
              null),
          new DataValue(
              Variant.of(new DateTime(123456789L)),
              StatusCode.GOOD,
              new DateTime(106L),
              null,
              new DateTime(206L),
              null));

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region mask matrix round trips

  private static Stream<Arguments> maskCombinations() {
    // UadpNetworkMessageContentMask bits (Part 14 Table 97): 0 PublisherId, 1 GroupHeader,
    // 2 WriterGroupId, 3 GroupVersion, 4 NetworkMessageNumber, 5 SequenceNumber,
    // 6 PayloadHeader, 7 Timestamp, 8 PicoSeconds, 9 DataSetClassId.
    List<Long> networkMasks =
        List.of(
            0x000L, // nothing: bare version/flags byte
            0x001L, // PublisherId
            0x041L, // PublisherId | PayloadHeader (Annex A.2.2 "UADP-Dynamic")
            0x006L, // GroupHeader | WriterGroupId
            0x02AL, // GroupHeader | GroupVersion | SequenceNumber
            0x07FL, // PublisherId | full GroupHeader | PayloadHeader
            0x0C1L, // PublisherId | PayloadHeader | Timestamp
            0x1C1L, // PublisherId | PayloadHeader | Timestamp | PicoSeconds
            0x241L, // PublisherId | PayloadHeader | DataSetClassId
            0x3FFL, // everything supported (all bits except PromotedFields)
            0x024L, // WriterGroupId | SequenceNumber WITHOUT GroupHeader: sub-bits ignored
            0x141L); // PicoSeconds WITHOUT Timestamp: ignored (Table 97)

    // UadpDataSetMessageContentMask bits (Table 101): 0 Timestamp, 1 PicoSeconds, 2 Status,
    // 3 MajorVersion, 4 MinorVersion, 5 SequenceNumber.
    List<Long> dataSetMasks =
        List.of(
            0x00L, // nothing: DataSetFlags1 only
            0x20L, // SequenceNumber
            0x05L, // Timestamp | Status
            0x35L, // Timestamp | Status | MinorVersion | SequenceNumber ("UADP-Dynamic")
            0x3FL, // everything
            0x02L); // PicoSeconds WITHOUT Timestamp: ignored (Table 101)

    // DataSetFieldContentMask bits (Table 32): 0 StatusCode, 1 SourceTimestamp,
    // 2 ServerTimestamp, 3 SourcePicoSeconds, 4 ServerPicoSeconds. 0 = Variant encoding.
    List<Long> fieldMasks =
        List.of(
            0x00L, // Variant field encoding
            0x01L, // DataValue: StatusCode
            0x06L, // DataValue: SourceTimestamp | ServerTimestamp
            0x1FL); // DataValue: all five members

    List<Arguments> combinations = new ArrayList<>();
    for (long networkMask : networkMasks) {
      for (long dataSetMask : dataSetMasks) {
        for (long fieldMask : fieldMasks) {
          combinations.add(
              Arguments.of(
                  String.format(
                      "nm=0x%03X dsm=0x%02X field=0x%02X", networkMask, dataSetMask, fieldMask),
                  new UadpNetworkMessageContentMask(uint(networkMask)),
                  new UadpDataSetMessageContentMask(uint(dataSetMask)),
                  new DataSetFieldContentMask(uint(fieldMask))));
        }
      }
    }
    return combinations.stream();
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("maskCombinations")
  void roundTripAcrossMaskMatrix(
      String label,
      UadpNetworkMessageContentMask networkMask,
      UadpDataSetMessageContentMask dataSetMask,
      DataSetFieldContentMask fieldMask)
      throws UaException {

    boolean payloadHeader = networkMask.getPayloadHeader();
    // Without a PayloadHeader the decoder can only handle a single DataSetMessage.
    int writerCount = payloadHeader ? 2 : 1;

    List<DataSetWriterConfig> writers = new ArrayList<>();
    List<DataSetMessageDraft> drafts = new ArrayList<>();
    for (int i = 0; i < writerCount; i++) {
      DataSetWriterConfig writer = writer(i + 1, dataSetMask, fieldMask);
      writers.add(writer);
      drafts.add(
          new DataSetMessageDraft(
              writer,
              ushort(11 + i),
              DSM_TIMESTAMP,
              DSM_STATUS,
              new ConfigurationVersionDataType(MAJOR_VERSION, MINOR_VERSION),
              false,
              FIELDS));
    }

    WriterGroupConfig group = group(networkMask, writers);

    EncodeContext context =
        new EncodeContext(
            encodingContext,
            PUBLISHER_ID,
            group,
            GROUP_VERSION,
            NETWORK_MESSAGE_NUMBER,
            NETWORK_MESSAGE_SEQUENCE,
            NETWORK_MESSAGE_TIMESTAMP,
            drafts);

    DecodedNetworkMessage decoded = decode(encodeToBytes(context));

    boolean groupHeader = networkMask.getGroupHeader();

    assertEquals(networkMask.getPublisherId() ? PUBLISHER_ID : null, decoded.publisherId(), label);
    assertEquals(
        groupHeader && networkMask.getWriterGroupId() ? WRITER_GROUP_ID : null,
        decoded.writerGroupId(),
        label);
    assertEquals(
        groupHeader && networkMask.getGroupVersion() ? GROUP_VERSION : null,
        decoded.groupVersion(),
        label);
    assertEquals(
        groupHeader && networkMask.getNetworkMessageNumber() ? NETWORK_MESSAGE_NUMBER : null,
        decoded.networkMessageNumber(),
        label);
    assertEquals(
        groupHeader && networkMask.getSequenceNumber() ? NETWORK_MESSAGE_SEQUENCE : null,
        decoded.sequenceNumber(),
        label);
    // PicoSeconds is only valid (and only emitted) together with Timestamp.
    assertEquals(
        networkMask.getTimestamp() ? NETWORK_MESSAGE_TIMESTAMP : null, decoded.timestamp(), label);

    assertTrue(decoded.metaData().isEmpty(), label);
    assertEquals(writerCount, decoded.messages().size(), label);

    for (int i = 0; i < writerCount; i++) {
      DecodedDataSetMessage message = decoded.messages().get(i);

      assertEquals(payloadHeader ? ushort(i + 1) : null, message.dataSetWriterId(), label);
      assertEquals(DataSetMessageKind.KEY_FRAME, message.kind(), label);
      assertTrue(message.valid(), label);
      assertEquals(
          dataSetMask.getSequenceNumber() ? uint(11 + i) : null, message.sequenceNumber(), label);
      assertEquals(dataSetMask.getTimestamp() ? DSM_TIMESTAMP : null, message.timestamp(), label);
      assertEquals(dataSetMask.getStatus() ? DSM_STATUS : null, message.status(), label);

      ConfigurationVersionDataType expectedVersion = null;
      if (dataSetMask.getMajorVersion() || dataSetMask.getMinorVersion()) {
        expectedVersion =
            new ConfigurationVersionDataType(
                dataSetMask.getMajorVersion() ? MAJOR_VERSION : uint(0),
                dataSetMask.getMinorVersion() ? MINOR_VERSION : uint(0));
      }
      assertEquals(expectedVersion, message.configurationVersion(), label);

      assertEquals(FIELDS.size(), message.fields().size(), label);
      for (int j = 0; j < FIELDS.size(); j++) {
        DecodedField field = message.fields().get(j);
        assertEquals(j, field.index(), label);
        assertEquals(expectedDecodedField(FIELDS.get(j), fieldMask), field.value(), label);
      }
    }
  }

  /**
   * Compute the expected post-round-trip field value.
   *
   * <p>Variant encoding sends the bare value for Good fields (Part 14 Table 34); the DataValue
   * encoding retains only the members selected by the DataSetFieldContentMask (§6.2.4.2), and the
   * Part 6 DataValue wire format then normalizes absent members on decode (Good status, MIN_VALUE
   * timestamps, null picoseconds).
   */
  private static DataValue expectedDecodedField(DataValue original, DataSetFieldContentMask mask) {
    boolean dataValueEncoding =
        mask.getStatusCode()
            || mask.getSourceTimestamp()
            || mask.getServerTimestamp()
            || mask.getSourcePicoSeconds()
            || mask.getServerPicoSeconds();

    if (!dataValueEncoding) {
      // Variant field encoding; all FIELDS values are Good, so the bare value is sent.
      return new DataValue(original.value(), StatusCode.GOOD, null, null, null, null);
    }

    StatusCode status = mask.getStatusCode() ? original.statusCode() : StatusCode.GOOD;
    DateTime sourceTime = mask.getSourceTimestamp() ? original.sourceTime() : null;
    UShort sourcePicoseconds =
        mask.getSourceTimestamp() && mask.getSourcePicoSeconds()
            ? original.sourcePicoseconds()
            : null;
    DateTime serverTime = mask.getServerTimestamp() ? original.serverTime() : null;
    UShort serverPicoseconds =
        mask.getServerTimestamp() && mask.getServerPicoSeconds()
            ? original.serverPicoseconds()
            : null;

    // Part 6 wire normalization of absent members:
    Variant value = original.value().isNotNull() ? original.value() : Variant.NULL_VALUE;
    if (status.value() == 0L) {
      status = StatusCode.GOOD;
    }
    if (sourceTime == null || sourceTime.equals(DateTime.MIN_VALUE)) {
      sourceTime = DateTime.MIN_VALUE;
    }
    if (serverTime == null || serverTime.equals(DateTime.MIN_VALUE)) {
      serverTime = DateTime.MIN_VALUE;
    }
    if (sourcePicoseconds == null || sourcePicoseconds.intValue() == 0) {
      sourcePicoseconds = null;
    }
    if (serverPicoseconds == null || serverPicoseconds.intValue() == 0) {
      serverPicoseconds = null;
    }

    return new DataValue(
        value, status, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
  }

  // endregion

  // region status propagation, keep-alive, default timestamps

  /**
   * Variant field encoding status propagation per Part 14 Table 34: Good sends the value, Bad sends
   * the StatusCode itself, Uncertain sends a DataValue carrying value + status.
   */
  @Test
  void variantEncodingStatusPropagation() throws UaException {
    DataSetWriterConfig writer =
        writer(1, new UadpDataSetMessageContentMask(uint(0)), new DataSetFieldContentMask(uint(0)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    StatusCode bad = new StatusCode(StatusCodes.Bad_InternalError);
    StatusCode uncertain = new StatusCode(0x40950000L);

    List<DataValue> fields =
        List.of(
            new DataValue(
                Variant.ofString("good"), StatusCode.GOOD, new DateTime(1L), null, null, null),
            new DataValue(Variant.ofInt32(1), bad, new DateTime(2L), null, null, null),
            new DataValue(Variant.ofDouble(2.5), uncertain, new DateTime(3L), null, null, null));

    EncodeContext context = encodeContext(group, List.of(keyFrame(writer, 1, fields)));

    DecodedNetworkMessage decoded = decode(encodeToBytes(context));

    assertEquals(1, decoded.messages().size());
    List<DecodedField> decodedFields = decoded.messages().get(0).fields();
    assertEquals(3, decodedFields.size());

    // Good: bare value, no timestamps survive the Variant encoding.
    assertEquals(
        new DataValue(Variant.ofString("good"), StatusCode.GOOD, null, null, null, null),
        decodedFields.get(0).value());

    // Bad: the wire carries Variant(StatusCode); the value is lost.
    assertEquals(
        new DataValue(Variant.NULL_VALUE, bad, null, null, null, null),
        decodedFields.get(1).value());

    // Uncertain: the wire carries Variant(DataValue(value, status)); absent DataValue members
    // decode to their Part 6 defaults.
    assertEquals(
        new DataValue(
            Variant.ofDouble(2.5), uncertain, DateTime.MIN_VALUE, null, DateTime.MIN_VALUE, null),
        decodedFields.get(2).value());
  }

  /** A group cycle mixing a key frame and a keep-alive draft, exercising the Sizes path. */
  @Test
  void mixedKeyFrameAndKeepAlive() throws UaException {
    UadpDataSetMessageContentMask dataSetMask = new UadpDataSetMessageContentMask(uint(0x20));
    DataSetFieldContentMask fieldMask = new DataSetFieldContentMask(uint(0));

    DataSetWriterConfig writer1 = writer(1, dataSetMask, fieldMask);
    DataSetWriterConfig writer2 = writer(2, dataSetMask, fieldMask);
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer1, writer2));

    DataSetMessageDraft keyFrame = keyFrame(writer1, 21, List.of(goodValue(Variant.ofInt32(7))));
    DataSetMessageDraft keepAlive =
        new DataSetMessageDraft(
            writer2,
            ushort(33),
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            true,
            List.of());

    EncodeContext context = encodeContext(group, List.of(keyFrame, keepAlive));

    DecodedNetworkMessage decoded = decode(encodeToBytes(context));

    assertEquals(2, decoded.messages().size());

    DecodedDataSetMessage first = decoded.messages().get(0);
    assertEquals(DataSetMessageKind.KEY_FRAME, first.kind());
    assertTrue(first.valid());
    assertEquals(uint(21), first.sequenceNumber());
    assertEquals(1, first.fields().size());

    DecodedDataSetMessage second = decoded.messages().get(1);
    assertEquals(ushort(2), second.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEEP_ALIVE, second.kind());
    assertTrue(second.valid());
    assertEquals(uint(33), second.sequenceNumber());
    assertTrue(second.fields().isEmpty());
  }

  /** Timestamp mask bits set with null timestamps: the encoder substitutes the current time. */
  @Test
  void nullTimestampsEncodeAsNow() throws UaException {
    // NM mask 0xC1: PublisherId | PayloadHeader | Timestamp. DSM mask 0x01: Timestamp.
    DataSetWriterConfig writer =
        writer(
            1, new UadpDataSetMessageContentMask(uint(0x01)), new DataSetFieldContentMask(uint(0)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0xC1)), List.of(writer));

    EncodeContext context =
        new EncodeContext(
            encodingContext,
            PUBLISHER_ID,
            group,
            GROUP_VERSION,
            NETWORK_MESSAGE_NUMBER,
            NETWORK_MESSAGE_SEQUENCE,
            null, // no NetworkMessage timestamp supplied
            List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    DecodedNetworkMessage decoded = decode(encodeToBytes(context));

    assertNotNull(decoded.timestamp());
    assertEquals(1, decoded.messages().size());
    assertNotNull(decoded.messages().get(0).timestamp());
  }

  @Test
  void mappingName() {
    assertEquals("uadp", new UadpMessageMapping().mappingName());
    assertEquals(UadpMessageMapping.MAPPING_NAME, new UadpMessageMapping().mappingName());
  }

  // endregion

  // region delta frame round trips, hand-built decode: event, heartbeat

  /**
   * Data Delta Frame with Variant field encoding, hand-built per Part 14 §7.2.4.5.6 Table 164 and
   * round-tripped: the encoder produces the hand-derived bytes bit-exactly and the decoder recovers
   * the (index, value) pairs.
   */
  @Test
  void deltaFrameRoundTripWithVariantEncoding() throws UaException {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1, all optional NetworkMessage headers off
            0x81, // DataSetFlags1: valid 0x01 | Variant encoding | DataSetFlags2 0x80
            0x01, // DataSetFlags2: type 0001 = Data Delta Frame
            0x02, 0x00, // FieldCount = 2
            0x01, 0x00, // FieldIndex = 1 (position in the DataSetMetaData)
            0x06, 0x2A, 0x00, 0x00, 0x00, // FieldValue: Variant Int32 = 42
            0x03, 0x00, // FieldIndex = 3
            0x0C, 0x02, 0x00, 0x00, 0x00, 0x6F, 0x6B); // FieldValue: Variant String "ok"

    DataSetWriterConfig writer =
        writer(1, new UadpDataSetMessageContentMask(uint(0)), new DataSetFieldContentMask(uint(0)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0)), List.of(writer));

    DataSetMessageDraft draft =
        deltaFrame(
            writer,
            1,
            new DataSetMessageDraft.DeltaField(1, goodValue(Variant.ofInt32(42))),
            new DataSetMessageDraft.DeltaField(3, goodValue(Variant.ofString("ok"))));

    assertArrayEquals(message, encodeToBytes(encodeContext(group, List.of(draft))));

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);

    assertEquals(DataSetMessageKind.DELTA_FRAME, dsm.kind());
    assertTrue(dsm.valid());
    assertEquals(
        List.of(
            new DecodedField(1, goodValue(Variant.ofInt32(42))),
            new DecodedField(3, goodValue(Variant.ofString("ok")))),
        dsm.fields());
  }

  /**
   * Data Delta Frame with DataValue field encoding, hand-built per Table 164 and round-tripped. The
   * encoded DataValue carries exactly the mask-selected members (§6.2.4.2).
   */
  @Test
  void deltaFrameRoundTripWithDataValueEncoding() throws UaException {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x85, // DataSetFlags1: valid 0x01 | encoding 10 = DataValue | DataSetFlags2 0x80
            0x01, // DataSetFlags2: type 0001 = Data Delta Frame
            0x01, 0x00, // FieldCount = 1
            0x04, 0x00, // FieldIndex = 4
            0x03, // DataValue mask: Value 0x01 | StatusCode 0x02
            0x06, 0x07, 0x00, 0x00, 0x00, // Value: Variant Int32 = 7
            0x00, 0x00, 0x95, 0x40); // StatusCode = 0x40950000 (Uncertain_SubNormal)

    // field mask 0x01: StatusCode -> DataValue field encoding with value + status on the wire
    DataSetWriterConfig writer =
        writer(
            1, new UadpDataSetMessageContentMask(uint(0)), new DataSetFieldContentMask(uint(0x01)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0)), List.of(writer));

    DataSetMessageDraft draft =
        deltaFrame(
            writer,
            1,
            new DataSetMessageDraft.DeltaField(
                4,
                new DataValue(
                    Variant.ofInt32(7), new StatusCode(0x40950000L), null, null, null, null)));

    assertArrayEquals(message, encodeToBytes(encodeContext(group, List.of(draft))));

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);

    assertEquals(DataSetMessageKind.DELTA_FRAME, dsm.kind());
    assertTrue(dsm.valid());
    assertEquals(1, dsm.fields().size());
    assertEquals(4, dsm.fields().get(0).index());
    assertEquals(
        new DataValue(
            Variant.ofInt32(7),
            new StatusCode(0x40950000L),
            DateTime.MIN_VALUE,
            null,
            DateTime.MIN_VALUE,
            null),
        dsm.fields().get(0).value());
  }

  /**
   * A group cycle mixing a key frame and a delta frame through the Sizes path: the delta carries
   * ONLY the changed fields with their explicit indices, the key frame all fields positionally, and
   * both survive the round trip — the wire shape of a key frame following deltas (the per-cadence
   * baseline refresh) is identical to any other key frame.
   */
  @Test
  void mixedKeyFrameAndDeltaFrameRoundTrip() throws UaException {
    UadpDataSetMessageContentMask dataSetMask = new UadpDataSetMessageContentMask(uint(0x20));
    DataSetFieldContentMask fieldMask = new DataSetFieldContentMask(uint(0));

    DataSetWriterConfig writer1 = writer(1, dataSetMask, fieldMask);
    DataSetWriterConfig writer2 = writer(2, dataSetMask, fieldMask);
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer1, writer2));

    DataSetMessageDraft keyFrame =
        keyFrame(
            writer1, 21, List.of(goodValue(Variant.ofInt32(7)), goodValue(Variant.ofInt32(8))));
    DataSetMessageDraft deltaFrame =
        deltaFrame(
            writer2, 33, new DataSetMessageDraft.DeltaField(1, goodValue(Variant.ofInt32(9))));

    DecodedNetworkMessage decoded =
        decode(encodeToBytes(encodeContext(group, List.of(keyFrame, deltaFrame))));

    assertEquals(2, decoded.messages().size());

    DecodedDataSetMessage first = decoded.messages().get(0);
    assertEquals(ushort(1), first.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEY_FRAME, first.kind());
    assertEquals(uint(21), first.sequenceNumber());
    assertEquals(2, first.fields().size());

    DecodedDataSetMessage second = decoded.messages().get(1);
    assertEquals(ushort(2), second.dataSetWriterId());
    assertEquals(DataSetMessageKind.DELTA_FRAME, second.kind());
    assertEquals(uint(33), second.sequenceNumber());
    assertEquals(List.of(new DecodedField(1, goodValue(Variant.ofInt32(9)))), second.fields());
  }

  /**
   * Frame behavior across the UInt16 DataSetMessage sequence number wraparound: a delta frame draft
   * at 0xFFFF followed by a key frame draft at 0 — consecutive cycles of one writer, since the
   * engine's counter wraps after 0xFFFF (§7.2.3) — encodes and decodes correctly, with kind,
   * sequence number, and fields intact on both sides of the wrap. Drafts carry the sequence number,
   * so the wrap is pinned without 65536 publish cycles. The key-frame cadence counter
   * (cyclesSinceKeyFrame in the engine's DataSetWriterRuntime) is a separate per-writer long that
   * never derives from the sequence number, but it is engine-internal with no seam reachable from
   * this package; its independence is covered behaviorally by DeltaFrameCadenceTest, whose frame
   * classification never consults sequence numbers.
   */
  @Test
  void sequenceNumberWrapAroundRoundTrip() throws UaException {
    // DSM mask 0x20: SequenceNumber on the wire
    DataSetWriterConfig writer =
        writer(
            1, new UadpDataSetMessageContentMask(uint(0x20)), new DataSetFieldContentMask(uint(0)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    DataSetMessageDraft delta =
        deltaFrame(
            writer, 0xFFFF, new DataSetMessageDraft.DeltaField(0, goodValue(Variant.ofInt32(1))));

    DecodedDataSetMessage decodedDelta =
        decode(encodeToBytes(encodeContext(group, List.of(delta)))).messages().get(0);
    assertEquals(DataSetMessageKind.DELTA_FRAME, decodedDelta.kind());
    assertEquals(uint(0xFFFF), decodedDelta.sequenceNumber());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(1)))), decodedDelta.fields());

    DataSetMessageDraft keyFrame = keyFrame(writer, 0, List.of(goodValue(Variant.ofInt32(2))));

    DecodedDataSetMessage decodedKey =
        decode(encodeToBytes(encodeContext(group, List.of(keyFrame)))).messages().get(0);
    assertEquals(DataSetMessageKind.KEY_FRAME, decodedKey.kind());
    assertEquals(uint(0), decodedKey.sequenceNumber());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(2)))), decodedKey.fields());
  }

  /** Event DataSetMessage drafts are rejected: emission is out of scope. */
  @Test
  void encodeRejectsEventDrafts() {
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    DataSetMessageDraft draft =
        new DataSetMessageDraft(
            writer,
            ushort(1),
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            DataSetMessageKind.EVENT,
            List.of(goodValue(Variant.ofInt32(1))),
            List.of(),
            null);

    EncodeContext context = encodeContext(group, List.of(draft));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
  }

  /** Event DataSetMessage (type 0010), fields encoded as Variants (§7.2.4.5.7 Table 165). */
  @Test
  void eventDecode() {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x81, // DataSetFlags1: valid | Variant encoding | DataSetFlags2 present
            0x02, // DataSetFlags2: type 0010 = Event
            0x01, 0x00, // FieldCount = 1
            0x0C, 0x03, 0x00, 0x00, 0x00, 0x65, 0x76, 0x74); // Variant String "evt"

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);

    assertEquals(DataSetMessageKind.EVENT, dsm.kind());
    assertTrue(dsm.valid());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofString("evt")))), dsm.fields());
  }

  /**
   * Heartbeat: a key frame consisting of the header only, detected via the payload Sizes array
   * (§7.2.4.5.5).
   */
  @Test
  void heartbeatDecode() {
    byte[] message =
        bytes(
            0x41, // byte 0: version 1 | PayloadHeader 0x40
            0x02, // PayloadHeader: Count = 2
            0x01, 0x00, // DataSetWriterIds[0] = 1
            0x02, 0x00, // DataSetWriterIds[1] = 2
            0x01, 0x00, // Sizes[0] = 1: header-only DataSetMessage (heartbeat)
            0x05, 0x00, // Sizes[1] = 5
            0x01, // DSM 1: DataSetFlags1 valid; no body at all
            0x01, // DSM 2: DataSetFlags1 valid
            0x01, 0x00, // DSM 2: FieldCount = 1
            0x01, 0x01); // DSM 2: Variant Boolean = true

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(2, decoded.messages().size());

    DecodedDataSetMessage heartbeat = decoded.messages().get(0);
    assertEquals(ushort(1), heartbeat.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEY_FRAME, heartbeat.kind());
    assertTrue(heartbeat.valid());
    assertTrue(heartbeat.fields().isEmpty());

    DecodedDataSetMessage keyFrame = decoded.messages().get(1);
    assertEquals(ushort(2), keyFrame.dataSetWriterId());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofBoolean(true)))), keyFrame.fields());
  }

  // endregion

  // region encode error contracts

  @Test
  void encodeRejectsRawDataFieldMask() {
    // DataSetFieldContentMask bit 5 = RawData.
    DataSetWriterConfig writer =
        writer(
            1, new UadpDataSetMessageContentMask(uint(0)), new DataSetFieldContentMask(uint(0x20)));
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    EncodeContext context =
        encodeContext(group, List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsPromotedFields() {
    // UadpNetworkMessageContentMask bit 10 = PromotedFields.
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x441)), List.of(writer));

    EncodeContext context =
        encodeContext(group, List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsNonNoneSecurity() {
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(UadpWriterGroupSettings.builder().build())
            .messageSecurity(MessageSecurityConfig.builder().mode(MessageSecurityMode.Sign).build())
            .dataSetWriter(writer)
            .build();

    EncodeContext context =
        encodeContext(group, List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsNonUadpGroupSettings() {
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(JsonWriterGroupSettings.builder().build())
            .dataSetWriter(writer)
            .build();

    EncodeContext context =
        encodeContext(group, List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsNonUadpWriterSettings() {
    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer-1")
            .dataSet(new PublishedDataSetRef("ds"))
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetWriterSettings.builder().build())
            .build();
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    EncodeContext context =
        encodeContext(group, List.of(keyFrame(writer, 1, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsEmptyDraftList() {
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    EncodeContext context = encodeContext(group, List.of());

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsMoreThan255MessagesWithPayloadHeader() {
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    // The PayloadHeader Count is a single byte: 256 DataSetMessages cannot be represented.
    List<DataSetMessageDraft> drafts = Collections.nCopies(256, keyFrame(writer, 1, List.of()));

    EncodeContext context = encodeContext(group, drafts);

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, e.getStatusCode().value());
  }

  @Test
  void encodeRejectsDataSetMessageLargerThan65535WithSizes() {
    DataSetWriterConfig writer1 = variantWriter(1);
    DataSetWriterConfig writer2 = variantWriter(2);
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer1, writer2));

    // Sizes entries are UInt16: a DataSetMessage larger than 65535 bytes cannot be sized.
    DataValue oversized = goodValue(Variant.ofString("x".repeat(70_000)));

    EncodeContext context =
        encodeContext(
            group,
            List.of(
                keyFrame(writer1, 1, List.of(oversized)),
                keyFrame(writer2, 2, List.of(goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, e.getStatusCode().value());
  }

  /**
   * Delta frame drafts combined with a non-zero ConfiguredSize are rejected: fixed-size layouts are
   * key-frame-only (Part 14 Annex A.2.1.7), and a delta exceeding the ConfiguredSize would be
   * valid-bit cleared (§6.3.1.3.3) yet still sent, silently losing the changed values. The engine
   * never produces the combination (startup/reconfigure validation plus every-cycle key-frame
   * degradation); this pins the backstop for external mapping users.
   */
  @Test
  void encodeRejectsDeltaFrameWithConfiguredSize() {
    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer-1")
            .dataSet(new PublishedDataSetRef("ds"))
            .dataSetWriterId(ushort(1))
            .settings(UadpDataSetWriterSettings.builder().configuredSize(ushort(64)).build())
            .build();
    WriterGroupConfig group = group(new UadpNetworkMessageContentMask(uint(0x41)), List.of(writer));

    EncodeContext context =
        encodeContext(
            group,
            List.of(
                deltaFrame(
                    writer,
                    1,
                    new DataSetMessageDraft.DeltaField(0, goodValue(Variant.ofInt32(1))))));

    UaException e = assertThrows(UaException.class, () -> new UadpMessageMapping().encode(context));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  /**
   * Part 14 §7.2.4.5.6 Table 164: "A Publisher shall use an index only once." The engine's diff
   * cannot produce duplicate indices; the draft factory guards external mapping users.
   */
  @Test
  void ofDeltaFrameRejectsDuplicateFieldIndex() {
    DataSetWriterConfig writer = variantWriter(1);

    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                deltaFrame(
                    writer,
                    1,
                    new DataSetMessageDraft.DeltaField(2, goodValue(Variant.ofInt32(1))),
                    new DataSetMessageDraft.DeltaField(2, goodValue(Variant.ofInt32(2)))));
    assertTrue(e.getMessage().contains("index 2"), "unexpected message: " + e.getMessage());
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

  private static DataValue goodValue(Variant value) {
    return new DataValue(value, StatusCode.GOOD, null, null, null, null);
  }

  private static DataSetWriterConfig variantWriter(int dataSetWriterId) {
    return writer(
        dataSetWriterId,
        new UadpDataSetMessageContentMask(uint(0)),
        new DataSetFieldContentMask(uint(0)));
  }

  private static DataSetWriterConfig writer(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      DataSetFieldContentMask fieldContentMask) {

    return DataSetWriterConfig.builder("writer-" + dataSetWriterId)
        .dataSet(new PublishedDataSetRef("ds"))
        .dataSetWriterId(ushort(dataSetWriterId))
        .fieldContentMask(fieldContentMask)
        .settings(
            UadpDataSetWriterSettings.builder()
                .dataSetMessageContentMask(dataSetMessageContentMask)
                .build())
        .build();
  }

  private static WriterGroupConfig group(
      UadpNetworkMessageContentMask networkMessageContentMask, List<DataSetWriterConfig> writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(networkMessageContentMask)
                    .build());
    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static DataSetMessageDraft keyFrame(
      DataSetWriterConfig writer, int sequenceNumber, List<DataValue> fields) {

    return new DataSetMessageDraft(
        writer,
        ushort(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        false,
        fields);
  }

  private static DataSetMessageDraft deltaFrame(
      DataSetWriterConfig writer, int sequenceNumber, DataSetMessageDraft.DeltaField... fields) {

    return DataSetMessageDraft.ofDeltaFrame(
        writer,
        ushort(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        List.of(fields),
        null);
  }

  private EncodeContext encodeContext(WriterGroupConfig group, List<DataSetMessageDraft> drafts) {
    return new EncodeContext(
        encodingContext,
        PUBLISHER_ID,
        group,
        GROUP_VERSION,
        NETWORK_MESSAGE_NUMBER,
        NETWORK_MESSAGE_SEQUENCE,
        NETWORK_MESSAGE_TIMESTAMP,
        drafts);
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
