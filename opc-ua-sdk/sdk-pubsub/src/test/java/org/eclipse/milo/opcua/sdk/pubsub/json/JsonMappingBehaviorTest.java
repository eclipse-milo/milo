/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.ENCODING_CONTEXT;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.MAPPING;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.PUBLISHER_ID;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.decode;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encode;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encodeContext;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encodeSingle;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.field;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.firstMessage;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.group;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.keyFrame;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.metaData;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.toJson;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.writer;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.junit.jupiter.api.Test;

/**
 * Behavioral pins of the JSON mapping: keep-alive shapes, name-keyed delta decode, UInt32 sequence
 * numbers, MessageId uniqueness, tolerant header/payload decoding, error status codes, and buffer
 * ownership.
 */
class JsonMappingBehaviorTest {

  private static final DataSetMetaDataType META = metaData(field("Speed", 11, NodeIds.Double, -1));

  private static DataSetMessageDraft keepAliveDraft(JsonDataSetMessageContentMask dsmMask) {
    return DataSetMessageDraft.of(
        writer("w", 17, dsmMask, DataSetFieldContentMask.of()),
        uint(1044),
        null,
        null,
        new ConfigurationVersionDataType(uint(1), uint(1)),
        true,
        List.of(),
        META);
  }

  // region keep-alive

  /** A keep-alive DataSetMessage has no Payload member; SequenceNumber is the next expected. */
  @Test
  void keepAliveEncodesWithoutPayload() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.SequenceNumber,
            JsonDataSetMessageContentMask.Field.MessageType,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject message =
        firstMessage(
            encodeSingle(
                group(JsonNetworkMessageContentMask.of()), List.of(keepAliveDraft(dsmMask))));

    assertEquals("ua-keepalive", message.get("MessageType").getAsString());
    assertEquals(1044, message.get("SequenceNumber").getAsInt());
    assertFalse(message.has("Payload"));
  }

  /** With the DataSetMessage header disabled a keep-alive degenerates to {@code {}} (Table 185). */
  @Test
  void headerlessKeepAliveDegeneratesToEmptyObject() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(JsonNetworkMessageContentMask.Field.SingleDataSetMessage);

    var root =
        encodeSingle(group(nmMask), List.of(keepAliveDraft(JsonDataSetMessageContentMask.of())));
    assertTrue(root.isJsonObject());
    assertTrue(root.getAsJsonObject().isEmpty());

    // ... and {} decodes back as a KEEP_ALIVE
    DecodedNetworkMessage decoded = decode("{}");
    assertEquals(DataSetMessageKind.KEEP_ALIVE, decoded.messages().get(0).kind());
  }

  @Test
  void decodeKeepAliveVariants() throws Exception {
    // explicit MessageType
    DecodedNetworkMessage explicit =
        decode("{\"DataSetWriterId\":17,\"SequenceNumber\":1044,\"MessageType\":\"ua-keepalive\"}");
    assertEquals(DataSetMessageKind.KEEP_ALIVE, explicit.messages().get(0).kind());
    assertEquals(uint(1044), explicit.messages().get(0).sequenceNumber());

    // header members but no MessageType and no Payload
    DecodedNetworkMessage headerOnly = decode("{\"DataSetWriterId\":17,\"SequenceNumber\":9}");
    assertEquals(DataSetMessageKind.KEEP_ALIVE, headerOnly.messages().get(0).kind());

    // inside a ua-data NetworkMessage
    DecodedNetworkMessage nested =
        decode(
            "{\"MessageId\":\"x\",\"MessageType\":\"ua-data\","
                + "\"Messages\":[{\"DataSetWriterId\":1,\"MessageType\":\"ua-keepalive\"}]}");
    assertEquals(DataSetMessageKind.KEEP_ALIVE, nested.messages().get(0).kind());
  }

  // endregion

  // region delta frames and events

  private static final DataSetMetaDataType TWO_FIELD_META =
      metaData(field("Speed", 11, NodeIds.Double, -1), field("Mode", 6, NodeIds.Int32, -1));

  /** A DataSetMessage mask able to express delta frames: MessageType present (Annex A.3.3.4). */
  private static final JsonDataSetMessageContentMask DELTA_DSM_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.SequenceNumber,
          JsonDataSetMessageContentMask.Field.MessageType,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  /**
   * A delta frame draft encodes MessageType {@code "ua-deltaframe"} and a Payload containing only
   * the changed fields, name-resolved from the draft metadata by index (Table 185).
   */
  @Test
  void deltaFrameEncodesOnlyChangedFields() throws Exception {
    var draft =
        JsonTestFixtures.deltaFrame(
            writer("w", 17, DELTA_DSM_MASK, DataSetFieldContentMask.of()),
            9,
            TWO_FIELD_META,
            new DataSetMessageDraft.DeltaField(
                1, new DataValue(Variant.of(3), StatusCode.GOOD, null, null)));

    JsonObject message =
        firstMessage(encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft)));

    assertEquals("ua-deltaframe", message.get("MessageType").getAsString());
    assertEquals(9, message.get("SequenceNumber").getAsInt());

    JsonObject payload = message.get("Payload").getAsJsonObject();
    assertEquals(List.of("Mode"), List.copyOf(payload.keySet()));
    assertEquals(3, payload.get("Mode").getAsInt());
  }

  /**
   * A delta frame whose effective DataSetMessage mask lacks the MessageType member is rejected: the
   * wire shape would be indistinguishable from a key frame (Annex A.3.3.4: "If the KeyFrameCount is
   * not 1, the MessageType bit shall be true").
   */
  @Test
  void deltaFrameWithoutMessageTypeMaskRejected() {
    var draft =
        JsonTestFixtures.deltaFrame(
            writer("w", 17, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            9,
            TWO_FIELD_META,
            new DataSetMessageDraft.DeltaField(
                0, new DataValue(Variant.of(1.5), StatusCode.GOOD, null, null)));

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  /** A delta frame with the DataSetMessageHeader disabled is rejected for the same reason. */
  @Test
  void deltaFrameWithoutDataSetMessageHeaderRejected() {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.PublisherId);

    var draft =
        JsonTestFixtures.deltaFrame(
            writer("w", 17, DELTA_DSM_MASK, DataSetFieldContentMask.of()),
            9,
            TWO_FIELD_META,
            new DataSetMessageDraft.DeltaField(
                0, new DataValue(Variant.of(1.5), StatusCode.GOOD, null, null)));

    UaException e =
        assertThrows(
            UaException.class, () -> MAPPING.encode(encodeContext(group(nmMask), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  /** Delta field indices must resolve to a metadata field name. */
  @Test
  void deltaFrameFieldIndexOutsideMetaDataRejected() {
    var draft =
        JsonTestFixtures.deltaFrame(
            writer("w", 17, DELTA_DSM_MASK, DataSetFieldContentMask.of()),
            9,
            TWO_FIELD_META,
            new DataSetMessageDraft.DeltaField(
                2, new DataValue(Variant.of(1.5), StatusCode.GOOD, null, null)));

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  /** Name-keyed delta payloads decode as DELTA_FRAME with {@code fieldName} set per field. */
  @Test
  void nameKeyedDeltaDecodesAsDeltaFrame() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"DataSetWriterId\":17,\"SequenceNumber\":2,\"MessageType\":\"ua-deltaframe\","
                + "\"Payload\":{\"Mode\":3,\"Speed\":1.5}}");

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(DataSetMessageKind.DELTA_FRAME, message.kind());
    assertEquals(2, message.fields().size());

    // fields are name-keyed in document order
    DecodedField mode = message.fields().get(0);
    assertEquals(0, mode.index());
    assertEquals("Mode", mode.fieldName());
    assertEquals(3, mode.value().value().value());

    DecodedField speed = message.fields().get(1);
    assertEquals(1, speed.index());
    assertEquals("Speed", speed.fieldName());
    assertEquals(1.5, speed.value().value().value());
  }

  @Test
  void uaEventDecodesAsEventKind() throws Exception {
    DecodedNetworkMessage decoded =
        decode("{\"DataSetWriterId\":9,\"MessageType\":\"ua-event\",\"Payload\":{\"E\":true}}");
    assertEquals(DataSetMessageKind.EVENT, decoded.messages().get(0).kind());
    assertEquals("E", decoded.messages().get(0).fields().get(0).fieldName());
  }

  // endregion

  // region sequence numbers (UInt32)

  /** Wire SequenceNumbers are UInt32: values beyond the UInt16 range decode losslessly. */
  @Test
  void sequenceNumbersDecodeAsUInt32() throws Exception {
    DecodedNetworkMessage decoded =
        decode("{\"DataSetWriterId\":1,\"SequenceNumber\":70000,\"Payload\":{\"A\":1}}");
    assertEquals(uint(70000), decoded.messages().get(0).sequenceNumber());

    DecodedNetworkMessage max =
        decode("{\"DataSetWriterId\":1,\"SequenceNumber\":4294967295,\"Payload\":{\"A\":1}}");
    assertEquals(uint(4294967295L), max.messages().get(0).sequenceNumber());
  }

  /**
   * Encoded sequence numbers span the full UInt32 range (Table 185): the engine's JSON sequence
   * counter rolls over at the DataType maximum 2^32 (§7.2.3), NOT at the UADP UInt16 width, so
   * 65535 is followed by 65536 on the wire and both round-trip losslessly, as does the largest
   * UInt32 value.
   */
  @Test
  void encodedSequenceNumberRoundTripsAsUInt32() throws Exception {
    for (long sequenceNumber : new long[] {65535L, 65536L, 4294967295L}) {
      var draft =
          keyFrame(
              writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
              sequenceNumber,
              META,
              new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));

      var encoded = encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft));
      assertEquals(sequenceNumber, firstMessage(encoded).get("SequenceNumber").getAsLong());

      DecodedNetworkMessage decoded = decode(encoded.toString());
      assertEquals(uint(sequenceNumber), decoded.messages().get(0).sequenceNumber());
    }
  }

  // endregion

  // region MessageId

  /** MessageId is a random UUID, unique across NetworkMessages and across encode calls. */
  @Test
  void messageIdIsUniqueUuidPerNetworkMessage() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.SingleDataSetMessage);

    DataValue value = new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null);
    List<DataSetMessageDraft> drafts =
        List.of(
            keyFrame(
                writer("w1", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
                1,
                META,
                value),
            keyFrame(
                writer("w2", 2, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
                1,
                META,
                value),
            keyFrame(
                writer("w3", 3, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
                1,
                META,
                value));

    var messageIds = new HashSet<String>();
    for (int call = 0; call < 2; call++) {
      for (EncodedNetworkMessage message : encode(group(nmMask), drafts)) {
        String messageId = toJson(message).getAsJsonObject().get("MessageId").getAsString();
        UUID.fromString(messageId);
        messageIds.add(messageId);
      }
    }
    assertEquals(6, messageIds.size());
  }

  // endregion

  // region tolerant decode

  /** Unknown members in NetworkMessage and DataSetMessage headers are skipped. */
  @Test
  void unknownHeaderMembersAreSkipped() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"MessageId\":\"m\",\"MessageType\":\"ua-data\",\"VendorThing\":[1,2],"
                + "\"PublisherId\":\"line-7\","
                + "\"Messages\":[{\"DataSetWriterId\":4,\"VendorBit\":true,"
                + "\"Payload\":{\"A\":1}}]}");

    assertEquals("line-7", decoded.publisherId().toCanonicalString());
    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(4), message.dataSetWriterId());
    assertEquals(1, message.fields().size());
    assertEquals("A", message.fields().get(0).fieldName());
  }

  /** Other {@code ua-*} message types (optional discovery messages) are tolerated and skipped. */
  @Test
  void otherUaMessageTypesAreToleratedAndSkipped() throws Exception {
    DecodedNetworkMessage decoded =
        decode("{\"MessageId\":\"x\",\"MessageType\":\"ua-status\",\"IsCyclic\":true}");
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** An unknown DataSetMessage MessageType yields an invalid placeholder, like the UADP codec. */
  @Test
  void unknownDataSetMessageTypeYieldsInvalidPlaceholder() throws Exception {
    DecodedNetworkMessage decoded =
        decode("{\"DataSetWriterId\":9,\"MessageType\":\"ua-mystery\",\"Payload\":{\"E\":1}}");
    assertFalse(decoded.messages().get(0).valid());
    assertTrue(decoded.messages().get(0).fields().isEmpty());
    assertEquals(DataSetMessageKind.KEY_FRAME, decoded.messages().get(0).kind());
  }

  /**
   * Payload decoding is lenient: an undecodable field value becomes a Bad_DecodingError DataValue
   * and the remaining fields are still delivered.
   */
  @Test
  void lenientPayloadDeliversRemainingFields() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"DataSetWriterId\":1,\"Payload\":{"
                + "\"Broken\":{\"UaType\":11,\"Value\":\"not-a-number\"},"
                + "\"Fine\":2.5}}");

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(2, message.fields().size());

    DecodedField broken = message.fields().get(0);
    assertEquals("Broken", broken.fieldName());
    assertEquals(new StatusCode(StatusCodes.Bad_DecodingError), broken.value().statusCode());
    assertTrue(broken.value().value().isNull());

    DecodedField fine = message.fields().get(1);
    assertEquals("Fine", fine.fieldName());
    assertTrue(fine.value().statusCode().isGood());
    assertEquals(2.5, fine.value().value().value());
  }

  // endregion

  // region errors and buffer ownership

  @Test
  void emptyDraftListThrowsBadInvalidArgument() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of())));
    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
  }

  @Test
  void nonJsonGroupSettingsThrowBadConfigurationError() {
    WriterGroupConfig uadpGroup =
        WriterGroupConfig.builder("uadp-group")
            .writerGroupId(ushort(1))
            .messageSettings(UadpWriterGroupSettings.builder().build())
            .build();

    var draft =
        keyFrame(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));

    UaException e =
        assertThrows(
            UaException.class, () -> MAPPING.encode(encodeContext(uadpGroup, List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void nonJsonWriterSettingsThrowBadConfigurationError() {
    DataSetWriterConfig uadpWriter =
        DataSetWriterConfig.builder("uadp-writer")
            .dataSet(new PublishedDataSetRef("Commands"))
            .dataSetWriterId(ushort(1))
            .settings(UadpDataSetWriterSettings.builder().build())
            .build();

    var draft =
        keyFrame(uadpWriter, 1, META, new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  /** A data draft without resolved metadata cannot produce a name-keyed payload. */
  @Test
  void missingDraftMetaDataThrowsBadConfigurationError() {
    var draft =
        DataSetMessageDraft.of(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            uint(1),
            null,
            null,
            new ConfigurationVersionDataType(uint(1), uint(1)),
            false,
            List.of(new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null)));

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void fieldCountMetaDataMismatchThrowsBadConfigurationError() {
    var draft =
        keyFrame(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META, // one field of metadata...
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null),
            new DataValue(Variant.of(2.0), StatusCode.GOOD, null, null)); // ...two values

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MAPPING.encode(
                    encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft))));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void nonJsonPayloadThrowsBadDecodingError() {
    UaException garbage = assertThrows(UaException.class, () -> decode("binarygarbage"));
    assertEquals(StatusCodes.Bad_DecodingError, garbage.getStatusCode().value());

    UaException primitive = assertThrows(UaException.class, () -> decode("42"));
    assertEquals(StatusCodes.Bad_DecodingError, primitive.getStatusCode().value());
  }

  /** The caller retains ownership of the payload buffer, including when decode fails. */
  @Test
  void decodeFailureLeavesPayloadBufferOwnedByCaller() {
    ByteBuf buffer = Unpooled.wrappedBuffer("not json".getBytes(StandardCharsets.UTF_8));
    try {
      assertThrows(
          UaException.class, () -> MAPPING.decode(DecodeContext.of(ENCODING_CONTEXT), buffer));
      assertEquals(1, buffer.refCnt());
    } finally {
      buffer.release();
    }
  }

  /** The caller owns every returned buffer on encode success. */
  @Test
  void encodedBuffersAreOwnedByCaller() throws Exception {
    var draft =
        keyFrame(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));

    List<EncodedNetworkMessage> encoded =
        encode(group(JsonNetworkMessageContentMask.of()), List.of(draft));
    for (EncodedNetworkMessage message : encoded) {
      assertEquals(1, message.data().refCnt());
      message.data().release();
      assertEquals(0, message.data().refCnt());
    }
  }

  // endregion

  // region provider surface

  /** The provider is named exactly "json" and delegates to the mapping. */
  @Test
  void providerIsNamedJsonAndDelegates() throws Exception {
    var provider = new JsonMessageMappingProvider();
    assertEquals("json", provider.mappingName());

    var draft =
        keyFrame(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.5), StatusCode.GOOD, null, null));

    List<EncodedNetworkMessage> encoded =
        provider.encode(encodeContext(group(JsonNetworkMessageContentMask.of()), List.of(draft)));
    assertEquals(1, encoded.size());

    ByteBuf data = encoded.get(0).data();
    String wire;
    try {
      wire = data.toString(StandardCharsets.UTF_8);
    } finally {
      data.release();
    }

    ByteBuf buffer = Unpooled.wrappedBuffer(wire.getBytes(StandardCharsets.UTF_8));
    try {
      DecodedNetworkMessage decoded = provider.decode(DecodeContext.of(ENCODING_CONTEXT), buffer);
      assertEquals(1.5, decoded.messages().get(0).fields().get(0).value().value().value());
    } finally {
      buffer.release();
    }
  }

  /**
   * The SPI sequence number parameter is ignored by encodeMetaData: Table 188 defines no sequence
   * number member for {@code ua-metadata} (per-stream sequencing is engine-side only).
   */
  @Test
  void encodeMetaDataIgnoresSequenceNumberParameter() throws Exception {
    var provider = new JsonMessageMappingProvider();
    var context =
        MetaDataEncodeContext.of(
            ENCODING_CONTEXT,
            PUBLISHER_ID,
            group(JsonNetworkMessageContentMask.of()),
            writer("w", 17, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()));

    JsonObject encoded =
        toJson(provider.encodeMetaData(context, META, ushort(41))).getAsJsonObject();
    assertFalse(encoded.has("SequenceNumber"));
    assertEquals("ua-metadata", encoded.get("MessageType").getAsString());
  }

  // endregion

  // region timestamps and status defaults

  /**
   * The engine never fills draft timestamps for JSON writers because it pattern-matches UADP
   * settings only: with the Timestamp bit set and a null draft timestamp the mapping self-supplies
   * {@code DateTime.now()}.
   */
  @Test
  void timestampSelfSuppliedWhenDraftTimestampNull() throws Exception {
    Instant before = Instant.now().minusSeconds(60);

    var draft =
        keyFrame(
            writer("w", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));

    JsonObject message =
        firstMessage(encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft)));

    Instant timestamp = Instant.parse(message.get("Timestamp").getAsString());
    assertTrue(timestamp.isAfter(before));
    assertTrue(timestamp.isBefore(Instant.now().plusSeconds(60)));
  }

  /**
   * A missing Status member decodes as Good on a header-ful DataSetMessage; a bare payload-only
   * DataSetMessage has no header whose omission could mean Good, so its status is null.
   */
  @Test
  void missingStatusDecodesGoodOnHeaderfulMessagesOnly() throws Exception {
    DecodedNetworkMessage headerful = decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":1}}");
    StatusCode status = headerful.messages().get(0).status();
    assertNotNull(status);
    assertTrue(status.isGood());

    DecodedNetworkMessage bare = decode("{\"A\":1}");
    assertNull(bare.messages().get(0).status());
  }

  // endregion
}
