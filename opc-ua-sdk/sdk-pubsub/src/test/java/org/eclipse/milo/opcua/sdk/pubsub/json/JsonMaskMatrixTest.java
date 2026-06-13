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

import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.decode;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encode;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
 * Content mask matrix: the pinned effective defaults for empty masks, per-bit member toggles of
 * Tables 107/111, the SingleDataSetMessage and DataSetClassId NetworkMessage splitting, all eight
 * §7.2.5.3 structural layouts on encode and decode, and the deprecated FieldEncoding upgrades.
 */
class JsonMaskMatrixTest {

  private static final DataSetMetaDataType META = metaData(field("Speed", 11, NodeIds.Double, -1));

  private static DataSetMessageDraft draft(JsonDataSetMessageContentMask dsmMask) {
    return keyFrame(
        writer("w", 3, dsmMask, DataSetFieldContentMask.of()),
        7,
        META,
        new DataValue(Variant.of(2.5), StatusCode.GOOD, null, null));
  }

  // region effective defaults

  /**
   * An empty JsonNetworkMessageContentMask (the config default) selects the pinned effective
   * default NetworkMessageHeader | DataSetMessageHeader | PublisherId; an empty
   * JsonDataSetMessageContentMask selects DataSetWriterId | SequenceNumber | MetaDataVersion |
   * Timestamp | Status | FieldEncoding2 (VerboseEncoding).
   */
  @Test
  void emptyMasksSelectPinnedEffectiveDefaults() throws Exception {
    JsonObject networkMessage =
        encodeSingle(
                group(JsonNetworkMessageContentMask.of()),
                List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();

    assertEquals(
        Set.of("MessageId", "MessageType", "PublisherId", "Messages"), networkMessage.keySet());
    assertEquals("ua-data", networkMessage.get("MessageType").getAsString());
    assertEquals("line-7", networkMessage.get("PublisherId").getAsString());
    assertTrue(networkMessage.get("Messages").isJsonArray());

    // Status is in the effective default mask but omitted because the draft status is Good
    JsonObject dataSetMessage = firstMessage(networkMessage);
    assertEquals(
        Set.of("DataSetWriterId", "SequenceNumber", "MetaDataVersion", "Timestamp", "Payload"),
        dataSetMessage.keySet());
    assertEquals(3, dataSetMessage.get("DataSetWriterId").getAsInt());
    assertEquals(7, dataSetMessage.get("SequenceNumber").getAsInt());

    // FieldEncoding2 default = VerboseEncoding: the concrete Double field collapses to a bare value
    assertEquals(2.5, dataSetMessage.get("Payload").getAsJsonObject().get("Speed").getAsDouble());
  }

  /** With a Bad draft status the default-mask Status member appears. */
  @Test
  void defaultMaskEmitsStatusWhenNotGood() throws Exception {
    var draft =
        DataSetMessageDraft.of(
            writer("w", 3, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            uint(7),
            null,
            new StatusCode(0x80000000L),
            new ConfigurationVersionDataType(uint(1), uint(1)),
            false,
            List.of(new DataValue(Variant.of(2.5), StatusCode.GOOD, null, null)),
            META);

    JsonObject dataSetMessage =
        firstMessage(encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft)));

    assertEquals(
        Set.of(
            "DataSetWriterId",
            "SequenceNumber",
            "MetaDataVersion",
            "Timestamp",
            "Status",
            "Payload"),
        dataSetMessage.keySet());
    assertEquals(
        0x80000000L, dataSetMessage.get("Status").getAsJsonObject().get("Code").getAsLong());
  }

  // endregion

  // region NetworkMessage mask bits

  @Test
  void writerGroupNameBitTogglesNetworkMessageMember() throws Exception {
    var withBit =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.WriterGroupName);

    JsonObject networkMessage =
        encodeSingle(group(withBit), List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();
    assertEquals("json-writers", networkMessage.get("WriterGroupName").getAsString());

    var withoutBit =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader);

    networkMessage =
        encodeSingle(group(withoutBit), List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();
    assertFalse(networkMessage.has("WriterGroupName"));
  }

  @Test
  void publisherIdBitTogglesNetworkMessageMember() throws Exception {
    var withoutBit =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader);

    JsonObject networkMessage =
        encodeSingle(group(withoutBit), List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();
    assertFalse(networkMessage.has("PublisherId"));
  }

  /** The ReplyTo bit is "Not used." (Table 107); the member is never emitted. */
  @Test
  void replyToIsNeverEmitted() throws Exception {
    var mask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.ReplyTo);

    JsonObject networkMessage =
        encodeSingle(group(mask), List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();
    assertFalse(networkMessage.has("ReplyTo"));
  }

  /**
   * SingleDataSetMessage: one NetworkMessage per draft, each with the contributing writer's
   * attribution, a single-object Messages member, and a distinct MessageId.
   */
  @Test
  void singleDataSetMessageSplitsOneNetworkMessagePerDraft() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.SingleDataSetMessage);

    DataSetMessageDraft draft1 =
        keyFrame(
            writer("w1", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));
    DataSetMessageDraft draft2 =
        keyFrame(
            writer("w2", 2, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(2.0), StatusCode.GOOD, null, null));

    List<EncodedNetworkMessage> encoded = encode(group(nmMask), List.of(draft1, draft2));
    assertEquals(2, encoded.size());

    assertEquals(1, encoded.get(0).writers().size());
    assertEquals("w1", encoded.get(0).writers().get(0).name());
    assertEquals(ushort(1), encoded.get(0).writers().get(0).dataSetWriterId());
    assertEquals(1, encoded.get(1).writers().size());
    assertEquals("w2", encoded.get(1).writers().get(0).name());

    JsonObject first = toJson(encoded.get(0)).getAsJsonObject();
    JsonObject second = toJson(encoded.get(1)).getAsJsonObject();
    assertTrue(first.get("Messages").isJsonObject());
    assertEquals(1, first.get("Messages").getAsJsonObject().get("DataSetWriterId").getAsInt());
    assertEquals(2, second.get("Messages").getAsJsonObject().get("DataSetWriterId").getAsInt());
    assertNotEquals(first.get("MessageId").getAsString(), second.get("MessageId").getAsString());
  }

  /** A multi-draft group without SingleDataSetMessage shares one attributed NetworkMessage. */
  @Test
  void multiDraftGroupSharesOneNetworkMessageWithAllWritersAttributed() throws Exception {
    DataSetMessageDraft draft1 =
        keyFrame(
            writer("w1", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null));
    DataSetMessageDraft draft2 =
        keyFrame(
            writer("w2", 2, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            1,
            META,
            new DataValue(Variant.of(2.0), StatusCode.GOOD, null, null));

    List<EncodedNetworkMessage> encoded =
        encode(group(JsonNetworkMessageContentMask.of()), List.of(draft1, draft2));
    assertEquals(1, encoded.size());
    assertEquals(
        List.of("w1", "w2"),
        encoded.get(0).writers().stream().map(EncodedNetworkMessage.Writer::name).toList());

    JsonObject networkMessage = toJson(encoded.get(0)).getAsJsonObject();
    assertEquals(2, networkMessage.get("Messages").getAsJsonArray().size());
  }

  /**
   * The DataSetClassId bit batches drafts per distinct DataSetClassId into separate NetworkMessages
   * (Table 107 bit 4) and emits the member; the null Guid is omitted (no publisher-side classId
   * exists in the engine, so engine-built drafts never emit it).
   */
  @Test
  void dataSetClassIdBitSplitsBatchesAndEmitsMember() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetClassId);

    UUID classIdA = UUID.fromString("11111111-2222-3333-4444-555555555555");
    UUID classIdB = UUID.fromString("66666666-7777-8888-9999-aaaaaaaaaaaa");

    DataSetMetaDataType metaA =
        metaData(
            "A",
            classIdA,
            new ConfigurationVersionDataType(uint(1), uint(1)),
            field("Speed", 11, NodeIds.Double, -1));
    DataSetMetaDataType metaB =
        metaData(
            "B",
            classIdB,
            new ConfigurationVersionDataType(uint(1), uint(1)),
            field("Speed", 11, NodeIds.Double, -1));

    DataSetWriterConfig writer1 =
        writer("w1", 1, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of());
    DataSetWriterConfig writer2 =
        writer("w2", 2, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of());
    DataSetWriterConfig writer3 =
        writer("w3", 3, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of());

    DataValue value = new DataValue(Variant.of(1.0), StatusCode.GOOD, null, null);

    // w1 and w3 share classIdA, w2 has classIdB: two NetworkMessages, batched by classId
    List<EncodedNetworkMessage> encoded =
        encode(
            group(nmMask),
            List.of(
                keyFrame(writer1, 1, metaA, value),
                keyFrame(writer2, 1, metaB, value),
                keyFrame(writer3, 1, metaA, value)));

    assertEquals(2, encoded.size());

    JsonObject first = toJson(encoded.get(0)).getAsJsonObject();
    JsonObject second = toJson(encoded.get(1)).getAsJsonObject();

    assertEquals(classIdA.toString(), first.get("DataSetClassId").getAsString());
    assertEquals(2, first.get("Messages").getAsJsonArray().size());
    assertEquals(classIdB.toString(), second.get("DataSetClassId").getAsString());
    assertEquals(1, second.get("Messages").getAsJsonArray().size());

    // writer attribution follows the batches
    assertEquals(
        List.of("w1", "w3"),
        encoded.get(0).writers().stream().map(EncodedNetworkMessage.Writer::name).toList());
    assertEquals(
        List.of("w2"),
        encoded.get(1).writers().stream().map(EncodedNetworkMessage.Writer::name).toList());
  }

  @Test
  void nullGuidDataSetClassIdIsOmittedEvenWhenMaskBitSet() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetClassId);

    // META carries the null-Guid classId, the only value engine-built drafts ever carry
    JsonObject networkMessage =
        encodeSingle(group(nmMask), List.of(draft(JsonDataSetMessageContentMask.of())))
            .getAsJsonObject();
    assertFalse(networkMessage.has("DataSetClassId"));
  }

  // endregion

  // region DataSetMessage mask bits

  @Test
  void dataSetWriterNameBitTogglesMember() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterName,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(dsmMask))));
    assertEquals("w", dataSetMessage.get("DataSetWriterName").getAsString());
    assertFalse(dataSetMessage.has("DataSetWriterId"));
  }

  /**
   * Table 185 PublisherId row: "The value shall be omitted if the NetworkMessage header is
   * present." With the header absent the DataSetMessage-level PublisherId is emitted.
   */
  @Test
  void dataSetMessagePublisherIdSuppressedUnderNetworkMessageHeader() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.PublisherId,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    // NetworkMessage header present: DSM-level PublisherId suppressed
    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(dsmMask))));
    assertFalse(dataSetMessage.has("PublisherId"));

    // no NetworkMessage header (DataSetMessageHeader only): DSM-level PublisherId emitted
    var headerless =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.SingleDataSetMessage);
    JsonObject single = encodeSingle(group(headerless), List.of(draft(dsmMask))).getAsJsonObject();
    assertEquals("line-7", single.get("PublisherId").getAsString());
  }

  /**
   * Table 185 WriterGroupName row: omitted from the DataSetMessage when the name is already in the
   * NetworkMessage header.
   */
  @Test
  void dataSetMessageWriterGroupNameSuppressedWhenInNetworkMessageHeader() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.WriterGroupName,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    var nmWithGroupName =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.WriterGroupName);

    JsonObject networkMessage =
        encodeSingle(group(nmWithGroupName), List.of(draft(dsmMask))).getAsJsonObject();
    assertEquals("json-writers", networkMessage.get("WriterGroupName").getAsString());
    assertFalse(firstMessage(networkMessage).has("WriterGroupName"));

    // without the NetworkMessage-level WriterGroupName, the DSM-level member is emitted
    var nmWithoutGroupName =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader);
    networkMessage =
        encodeSingle(group(nmWithoutGroupName), List.of(draft(dsmMask))).getAsJsonObject();
    assertEquals("json-writers", firstMessage(networkMessage).get("WriterGroupName").getAsString());
  }

  /** Table 185 MinorVersion row: only emitted when the full MetaDataVersion is absent. */
  @Test
  void minorVersionOnlyEmittedWithoutMetaDataVersion() throws Exception {
    var minorOnly =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.MinorVersion,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(minorOnly))));
    assertEquals(1, dataSetMessage.get("MinorVersion").getAsLong());
    assertFalse(dataSetMessage.has("MetaDataVersion"));

    var both =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.MetaDataVersion,
            JsonDataSetMessageContentMask.Field.MinorVersion,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    dataSetMessage =
        firstMessage(encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(both))));
    assertTrue(dataSetMessage.has("MetaDataVersion"));
    assertFalse(dataSetMessage.has("MinorVersion"));
  }

  @Test
  void messageTypeBitEmitsUaKeyframe() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.MessageType,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(dsmMask))));
    assertEquals("ua-keyframe", dataSetMessage.get("MessageType").getAsString());
  }

  // endregion

  // region the eight structural layouts (§7.2.5.3), encode side

  private static JsonElement encodeLayout(JsonNetworkMessageContentMask nmMask) throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);
    return encodeSingle(group(nmMask), List.of(draft(dsmMask)));
  }

  @Test
  void encodeLayout110FullObjectWithMessagesArray() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
                JsonNetworkMessageContentMask.Field.DataSetMessageHeader));
    assertTrue(root.isJsonObject());
    assertTrue(root.getAsJsonObject().get("Messages").isJsonArray());
    JsonObject dsm =
        root.getAsJsonObject().get("Messages").getAsJsonArray().get(0).getAsJsonObject();
    assertEquals(3, dsm.get("DataSetWriterId").getAsInt());
  }

  @Test
  void encodeLayout111ObjectWithSingleMessageObject() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
                JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
                JsonNetworkMessageContentMask.Field.SingleDataSetMessage));
    assertTrue(root.getAsJsonObject().get("Messages").isJsonObject());
  }

  @Test
  void encodeLayout100ObjectWithPayloadArray() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.NetworkMessageHeader));
    JsonObject payload =
        root.getAsJsonObject().get("Messages").getAsJsonArray().get(0).getAsJsonObject();
    // no DataSetMessage header: the Messages elements are bare payload objects
    assertEquals(2.5, payload.get("Speed").getAsDouble());
    assertFalse(payload.has("DataSetWriterId"));
  }

  @Test
  void encodeLayout101ObjectWithSinglePayloadObject() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
                JsonNetworkMessageContentMask.Field.SingleDataSetMessage));
    JsonObject payload = root.getAsJsonObject().get("Messages").getAsJsonObject();
    assertEquals(2.5, payload.get("Speed").getAsDouble());
  }

  @Test
  void encodeLayout010ArrayOfDataSetMessages() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.DataSetMessageHeader));
    assertTrue(root.isJsonArray());
    assertEquals(
        3, root.getAsJsonArray().get(0).getAsJsonObject().get("DataSetWriterId").getAsInt());
  }

  @Test
  void encodeLayout011SingleDataSetMessageObject() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
                JsonNetworkMessageContentMask.Field.SingleDataSetMessage));
    assertTrue(root.isJsonObject());
    assertEquals(3, root.getAsJsonObject().get("DataSetWriterId").getAsInt());
    assertTrue(root.getAsJsonObject().has("Payload"));
  }

  /**
   * Layout 0/0/0 (array of bare payload objects) is reachable through a non-empty mask with none of
   * the structural bits set; the all-clear mask itself is indistinguishable from the unset config
   * default and selects the pinned defaults instead (Javadoc'd on {@link JsonMessageMapping}).
   */
  @Test
  void encodeLayout000ArrayOfPayloadObjects() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(JsonNetworkMessageContentMask.Field.PublisherId));
    assertTrue(root.isJsonArray());
    JsonObject payload = root.getAsJsonArray().get(0).getAsJsonObject();
    assertEquals(2.5, payload.get("Speed").getAsDouble());
    // PublisherId bit without NetworkMessageHeader: there is no header to carry it
    assertFalse(payload.has("PublisherId"));
  }

  @Test
  void encodeLayout001BarePayloadObject() throws Exception {
    JsonElement root =
        encodeLayout(
            JsonNetworkMessageContentMask.of(
                JsonNetworkMessageContentMask.Field.SingleDataSetMessage));
    assertTrue(root.isJsonObject());
    assertEquals(2.5, root.getAsJsonObject().get("Speed").getAsDouble());
    assertFalse(root.getAsJsonObject().has("Payload"));
  }

  // endregion

  // region the eight structural layouts, decode side (hand-built JSON text)

  @Test
  void decodeLayout110FullNetworkMessage() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"MessageId\":\"m\",\"MessageType\":\"ua-data\",\"PublisherId\":\"line-7\","
                + "\"Messages\":[{\"DataSetWriterId\":1,\"Payload\":{\"A\":1.5}},"
                + "{\"DataSetWriterId\":2,\"Payload\":{\"A\":2.5}}]}");
    assertEquals("line-7", decoded.publisherId().toCanonicalString());
    assertEquals(2, decoded.messages().size());
    assertEquals(ushort(1), decoded.messages().get(0).dataSetWriterId());
    assertEquals(ushort(2), decoded.messages().get(1).dataSetWriterId());
    assertEquals("A", decoded.messages().get(0).fields().get(0).fieldName());
  }

  @Test
  void decodeLayout111SingleMessageObject() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"MessageId\":\"m\",\"MessageType\":\"ua-data\","
                + "\"Messages\":{\"DataSetWriterId\":4,\"Payload\":{\"Speed\":1.0}}}");
    assertEquals(1, decoded.messages().size());
    assertEquals(ushort(4), decoded.messages().get(0).dataSetWriterId());
  }

  @Test
  void decodeLayout100PayloadArray() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"MessageId\":\"m\",\"MessageType\":\"ua-data\",\"PublisherId\":\"7\","
                + "\"Messages\":[{\"Speed\":1.5},{\"Speed\":2.5}]}");
    assertEquals("7", decoded.publisherId().toCanonicalString());
    assertEquals(2, decoded.messages().size());
    assertNull(decoded.messages().get(0).dataSetWriterId());
    assertEquals("Speed", decoded.messages().get(0).fields().get(0).fieldName());
    assertEquals(1.5, decoded.messages().get(0).fields().get(0).value().value().value());
  }

  @Test
  void decodeLayout101SinglePayloadObject() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "{\"MessageId\":\"m\",\"MessageType\":\"ua-data\",\"PublisherId\":\"7\","
                + "\"Messages\":{\"Speed\":1.0}}");
    assertEquals(1, decoded.messages().size());
    assertEquals("Speed", decoded.messages().get(0).fields().get(0).fieldName());
  }

  @Test
  void decodeLayout010ArrayOfDataSetMessages() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            "[{\"DataSetWriterId\":1,\"Payload\":{\"A\":1}},"
                + "{\"DataSetWriterId\":2,\"Payload\":{\"A\":2}}]");
    assertNull(decoded.publisherId());
    assertEquals(2, decoded.messages().size());
    assertEquals(ushort(2), decoded.messages().get(1).dataSetWriterId());
  }

  @Test
  void decodeLayout011SingleDataSetMessageObject() throws Exception {
    DecodedNetworkMessage decoded =
        decode("{\"DataSetWriterId\":9,\"SequenceNumber\":3,\"Payload\":{\"A\":true}}");
    assertEquals(1, decoded.messages().size());
    assertEquals(ushort(9), decoded.messages().get(0).dataSetWriterId());
    assertEquals(uint(3), decoded.messages().get(0).sequenceNumber());
    assertEquals(true, decoded.messages().get(0).fields().get(0).value().value().value());
  }

  @Test
  void decodeLayout000ArrayOfPayloadObjects() throws Exception {
    DecodedNetworkMessage decoded = decode("[{\"A\":1.5},{\"B\":2.5}]");
    assertEquals(2, decoded.messages().size());
    assertNull(decoded.messages().get(0).dataSetWriterId());
    assertEquals(DataSetMessageKind.KEY_FRAME, decoded.messages().get(0).kind());
    assertEquals("A", decoded.messages().get(0).fields().get(0).fieldName());
    assertEquals("B", decoded.messages().get(1).fields().get(0).fieldName());
  }

  @Test
  void decodeLayout001BarePayloadObject() throws Exception {
    DecodedNetworkMessage decoded = decode("{\"Speed\":2.5,\"Mode\":3}");
    assertEquals(1, decoded.messages().size());
    assertEquals(DataSetMessageKind.KEY_FRAME, decoded.messages().get(0).kind());
    // headerless DataSetMessages carry no identifiers and no status
    assertNull(decoded.messages().get(0).dataSetWriterId());
    assertNull(decoded.messages().get(0).status());
    assertEquals("Speed", decoded.messages().get(0).fields().get(0).fieldName());
    assertEquals(2.5, decoded.messages().get(0).fields().get(0).value().value().value());
    assertEquals("Mode", decoded.messages().get(0).fields().get(1).fieldName());
    assertEquals(3, decoded.messages().get(0).fields().get(1).value().value().value());
  }

  // endregion

  // region deprecated field encodings upgrade on encode (Table 112 / Annex H)

  /**
   * FieldEncoding1=0, FieldEncoding2=0 selects the deprecated NonReversibleEncoding; the mapping
   * upgrades it to VerboseEncoding on encode (collapsed bare value, not the deprecated form).
   */
  @Test
  void deprecatedNonReversibleEncodingUpgradesToVerbose() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(JsonDataSetMessageContentMask.Field.DataSetWriterId);

    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(dsmMask))));
    JsonElement speed = dataSetMessage.get("Payload").getAsJsonObject().get("Speed");
    assertTrue(speed.isJsonPrimitive());
    assertEquals(2.5, speed.getAsDouble());
  }

  /**
   * FieldEncoding1=1, FieldEncoding2=0 selects the deprecated ReversibleFieldEncoding; the mapping
   * upgrades it to CompactEncoding (the standard Variant object, "UaType"/"Value" — not the
   * deprecated "Type"/"Body" spelling).
   */
  @Test
  void deprecatedReversibleEncodingUpgradesToCompact() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.FieldEncoding1);

    JsonObject dataSetMessage =
        firstMessage(
            encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft(dsmMask))));
    JsonObject speed =
        dataSetMessage.get("Payload").getAsJsonObject().get("Speed").getAsJsonObject();
    assertEquals(11, speed.get("UaType").getAsInt());
    assertEquals(2.5, speed.get("Value").getAsDouble());
    assertFalse(speed.has("Type"));
    assertFalse(speed.has("Body"));
  }

  // endregion
}
