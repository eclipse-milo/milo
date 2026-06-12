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
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.EXAMPLE_VERSION;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.MAPPING;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.PUBLISHER_ID;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.decode;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encodeSingle;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.field;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.firstMessage;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.group;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.metaData;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.normalizeUuidStrings;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.toJson;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.writer;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.junit.jupiter.api.Test;

/**
 * Golden vectors for the JSON mapping: the worked {@code ua-data} and {@code ua-metadata} examples
 * of the Phase 3 grounding (themselves built strictly from Part 14 §7.2.5.3/.4/.5 rules) and the
 * spec's own verbatim §7.2.5.4.2 DataSetMessage examples.
 *
 * <p>Encode comparisons are JsonElement-level (member order insensitive); the pinned Table 184/185
 * member order is asserted separately.
 */
class JsonGoldenVectorTest {

  private static final UUID CLASS_ID = UUID.fromString("2dc07ece-cab9-4470-8f8a-2c1ead207e0e");
  private static final UUID SPEED_FIELD_ID =
      UUID.fromString("3c1a4f6e-0f2a-4d3b-9d51-6e7f8a9b0c1d");
  private static final UUID MODE_FIELD_ID = UUID.fromString("0a1b2c3d-4e5f-6071-8293-a4b5c6d7e8f9");

  /** The masks of grounding worked example 7.1. */
  private static final JsonNetworkMessageContentMask EXAMPLE_NM_MASK =
      JsonNetworkMessageContentMask.of(
          JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
          JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
          JsonNetworkMessageContentMask.Field.PublisherId,
          JsonNetworkMessageContentMask.Field.DataSetClassId);

  private static final JsonDataSetMessageContentMask EXAMPLE_DSM_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.MetaDataVersion,
          JsonDataSetMessageContentMask.Field.SequenceNumber,
          JsonDataSetMessageContentMask.Field.Timestamp,
          JsonDataSetMessageContentMask.Field.Status,
          JsonDataSetMessageContentMask.Field.MessageType,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  private static final DataSetFieldContentMask EXAMPLE_FIELD_MASK =
      DataSetFieldContentMask.of(
          DataSetFieldContentMask.Field.StatusCode, DataSetFieldContentMask.Field.SourceTimestamp);

  private static DataSetMetaDataType exampleMetaData() {
    return metaData(
        "Commands",
        CLASS_ID,
        EXAMPLE_VERSION,
        field("CommandedSpeed", 11, NodeIds.Double, -1, SPEED_FIELD_ID),
        field("CommandedMode", 6, NodeIds.Int32, -1, MODE_FIELD_ID));
  }

  private static DataSetMessageDraft exampleDraft(StatusCode draftStatus) {
    DateTime sourceTimestamp = new DateTime(Instant.parse("2026-06-11T18:45:19.502Z"));

    return DataSetMessageDraft.of(
        writer("commands-writer", 17, EXAMPLE_DSM_MASK, EXAMPLE_FIELD_MASK),
        ushort(1042),
        new DateTime(Instant.parse("2026-06-11T18:45:19.555Z")),
        draftStatus,
        EXAMPLE_VERSION,
        false,
        List.of(
            new DataValue(Variant.of(1230.5), StatusCode.GOOD, sourceTimestamp, null, null, null),
            new DataValue(
                Variant.of(2), new StatusCode(0x40000000L), sourceTimestamp, null, null, null)),
        exampleMetaData());
  }

  /**
   * Encoding the worked-example draft reproduces grounding example 7.1, with two recorded
   * differences: {@code MessageId} is a random UUID (asserted separately, stripped before
   * comparison) and the Good DataSetMessage {@code Status} is omitted rather than emitted as {@code
   * {}} (the pinned N5 omit-when-Good behavior; the grounding notes a publisher "may equally omit"
   * it).
   */
  @Test
  void encodeKeyFrameMatchesWorkedExample() throws Exception {
    JsonObject actual =
        encodeSingle(group(EXAMPLE_NM_MASK), List.of(exampleDraft(StatusCode.GOOD)))
            .getAsJsonObject();

    // MessageId is a random UUID per NetworkMessage; validate and strip it
    UUID.fromString(actual.get("MessageId").getAsString());
    actual.remove("MessageId");

    JsonObject expected =
        JsonParser.parseString(
                """
                {
                  "MessageType": "ua-data",
                  "PublisherId": "line-7",
                  "DataSetClassId": "2dc07ece-cab9-4470-8f8a-2c1ead207e0e",
                  "Messages": [
                    {
                      "DataSetWriterId": 17,
                      "SequenceNumber": 1042,
                      "MetaDataVersion": { "MajorVersion": 1444863032, "MinorVersion": 1444863033 },
                      "Timestamp": "2026-06-11T18:45:19.555Z",
                      "MessageType": "ua-keyframe",
                      "Payload": {
                        "CommandedSpeed": {
                          "Value": 1230.5,
                          "SourceTimestamp": "2026-06-11T18:45:19.502Z"
                        },
                        "CommandedMode": {
                          "Value": 2,
                          "Status": { "Code": 1073741824, "Symbol": "Uncertain" },
                          "SourceTimestamp": "2026-06-11T18:45:19.502Z"
                        }
                      }
                    }
                  ]
                }
                """)
            .getAsJsonObject();

    assertEquals(expected, actual);
  }

  /**
   * Member order is pinned to Table 184 for the NetworkMessage and Table 185 for the
   * DataSetMessage. A Bad draft status is used so the {@code Status} member appears.
   */
  @Test
  void encodedMemberOrderFollowsTables184And185() throws Exception {
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.PublisherId,
            JsonNetworkMessageContentMask.Field.WriterGroupName,
            JsonNetworkMessageContentMask.Field.DataSetClassId);

    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.DataSetWriterName,
            JsonDataSetMessageContentMask.Field.SequenceNumber,
            JsonDataSetMessageContentMask.Field.MetaDataVersion,
            JsonDataSetMessageContentMask.Field.Timestamp,
            JsonDataSetMessageContentMask.Field.Status,
            JsonDataSetMessageContentMask.Field.MessageType,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    var draft =
        DataSetMessageDraft.of(
            writer("commands-writer", 17, dsmMask, DataSetFieldContentMask.of()),
            ushort(1),
            new DateTime(Instant.parse("2026-06-11T18:45:19.555Z")),
            new StatusCode(0x80000000L),
            EXAMPLE_VERSION,
            false,
            List.of(new DataValue(Variant.of(1.5), StatusCode.GOOD, null, null)),
            metaData(field("Speed", 11, NodeIds.Double, -1)));

    JsonObject networkMessage = encodeSingle(group(nmMask), List.of(draft)).getAsJsonObject();

    assertEquals(
        List.of("MessageId", "MessageType", "PublisherId", "WriterGroupName", "Messages"),
        List.copyOf(networkMessage.keySet()));

    JsonObject dataSetMessage = firstMessage(networkMessage);
    assertEquals(
        List.of(
            "DataSetWriterId",
            "DataSetWriterName",
            "SequenceNumber",
            "MetaDataVersion",
            "Timestamp",
            "Status",
            "MessageType",
            "Payload"),
        List.copyOf(dataSetMessage.keySet()));
  }

  /** Grounding worked example 7.1, decoded verbatim (including the {@code Status: {}} member). */
  @Test
  void decodeWorkedExampleNetworkMessage() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            """
            {
              "MessageId": "32235546-05d9-4fd7-97df-ea3ff3408574",
              "MessageType": "ua-data",
              "PublisherId": "line-7",
              "DataSetClassId": "2dc07ece-cab9-4470-8f8a-2c1ead207e0e",
              "Messages": [
                {
                  "DataSetWriterId": 17,
                  "SequenceNumber": 1042,
                  "MetaDataVersion": { "MajorVersion": 1444863032, "MinorVersion": 1444863033 },
                  "Timestamp": "2026-06-11T18:45:19.555Z",
                  "Status": {},
                  "MessageType": "ua-keyframe",
                  "Payload": {
                    "CommandedSpeed": {
                      "Value": 1230.5,
                      "SourceTimestamp": "2026-06-11T18:45:19.502Z"
                    },
                    "CommandedMode": {
                      "Value": 2,
                      "Status": { "Code": 1073741824, "Symbol": "Uncertain" },
                      "SourceTimestamp": "2026-06-11T18:45:19.502Z"
                    }
                  }
                }
              ]
            }
            """);

    assertNotNull(decoded.publisherId());
    assertEquals("line-7", decoded.publisherId().toCanonicalString());
    assertEquals(1, decoded.messages().size());

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(17), message.dataSetWriterId());
    assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
    assertTrue(message.valid());
    assertEquals(uint(1042), message.sequenceNumber());
    assertEquals(
        new ConfigurationVersionDataType(uint(1444863032L), uint(1444863033L)),
        message.configurationVersion());
    assertNotNull(message.status());
    assertTrue(message.status().isGood());
    assertNotNull(message.timestamp());
    assertEquals(Instant.parse("2026-06-11T18:45:19.555Z"), message.timestamp().getJavaInstant());

    assertEquals(2, message.fields().size());

    DecodedField speed = message.fields().get(0);
    assertEquals(0, speed.index());
    assertEquals("CommandedSpeed", speed.fieldName());
    assertEquals(1230.5, speed.value().value().value());
    assertTrue(speed.value().statusCode().isGood());
    assertNotNull(speed.value().sourceTime());
    assertEquals(
        Instant.parse("2026-06-11T18:45:19.502Z"), speed.value().sourceTime().getJavaInstant());
    assertNull(speed.value().serverTime());

    DecodedField mode = message.fields().get(1);
    assertEquals(1, mode.index());
    assertEquals("CommandedMode", mode.fieldName());
    assertEquals(2, ((Number) mode.value().value().value()).intValue());
    assertEquals(new StatusCode(0x40000000L), mode.value().statusCode());
  }

  /** Grounding worked example 7.2: headerless single-DSM delta frame, Variant fields. */
  @Test
  void decodeWorkedExampleDeltaFrame() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            """
            {
              "DataSetWriterId": 17,
              "SequenceNumber": 1043,
              "MinorVersion": 1444863033,
              "MessageType": "ua-deltaframe",
              "Payload": {
                "CommandedSpeed": 1240.0
              }
            }
            """);

    assertNull(decoded.publisherId());

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(DataSetMessageKind.DELTA_FRAME, message.kind());
    assertEquals(ushort(17), message.dataSetWriterId());
    assertEquals(uint(1043), message.sequenceNumber());
    // MinorVersion-only header: major 0 = "not transmitted"
    assertEquals(
        new ConfigurationVersionDataType(uint(0), uint(1444863033L)),
        message.configurationVersion());
    // a missing Status member on a header-ful DataSetMessage decodes as Good
    assertNotNull(message.status());
    assertTrue(message.status().isGood());

    assertEquals(1, message.fields().size());
    DecodedField field = message.fields().get(0);
    assertEquals("CommandedSpeed", field.fieldName());
    assertEquals(1240.0, field.value().value().value());
  }

  /**
   * Encoding a delta frame draft reproduces grounding worked example 7.2 (the decode vector above):
   * a headerless single-DSM {@code ua-deltaframe} whose Payload carries only the changed field,
   * name-resolved from the draft metadata by its explicit index (Part 14 §7.2.5.4.1 Table 185).
   * Member order is pinned to the Table 185 order.
   */
  @Test
  void encodeWorkedExampleDeltaFrame() throws Exception {
    // headerless single DataSetMessage: DataSetMessageHeader + SingleDataSetMessage, no
    // NetworkMessageHeader (§7.2.5.3 collapse)
    var nmMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
            JsonNetworkMessageContentMask.Field.SingleDataSetMessage);

    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.DataSetWriterId,
            JsonDataSetMessageContentMask.Field.SequenceNumber,
            JsonDataSetMessageContentMask.Field.MinorVersion,
            JsonDataSetMessageContentMask.Field.MessageType,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    var draft =
        DataSetMessageDraft.ofDeltaFrame(
            writer("commands-writer", 17, dsmMask, DataSetFieldContentMask.of()),
            ushort(1043),
            null,
            null,
            EXAMPLE_VERSION,
            List.of(
                new DataSetMessageDraft.DeltaField(
                    0, new DataValue(Variant.of(1240.0), StatusCode.GOOD, null, null))),
            exampleMetaData());

    JsonObject actual = encodeSingle(group(nmMask), List.of(draft)).getAsJsonObject();

    JsonObject expected =
        JsonParser.parseString(
                """
                {
                  "DataSetWriterId": 17,
                  "SequenceNumber": 1043,
                  "MinorVersion": 1444863033,
                  "MessageType": "ua-deltaframe",
                  "Payload": {
                    "CommandedSpeed": 1240.0
                  }
                }
                """)
            .getAsJsonObject();

    assertEquals(expected, actual);

    // member order pinned to Table 185
    assertEquals(
        List.of("DataSetWriterId", "SequenceNumber", "MinorVersion", "MessageType", "Payload"),
        List.copyOf(actual.keySet()));
    assertEquals(
        List.of("CommandedSpeed"), List.copyOf(actual.get("Payload").getAsJsonObject().keySet()));
  }

  /** The spec's own §7.2.5.4.2 example 1, verbatim: collapsed Verbose fields, no field status. */
  @Test
  void decodeSpecExampleWithoutFieldStatus() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            """
            {
              "PublisherId":"MyPublisher",
              "DataSetWriterId":102,
              "SequenceNumber":25460,
              "MinorVersion":672341762,
              "Timestamp":"2021-09-27T18:45:19.555Z",
              "Payload":
              {
                "LocationName":"Building A",
                "Coordinate":
                {
                  "X":1,
                  "Y":0.2
                }
              }
            }
            """);

    // the DataSetMessage-level PublisherId (legal without a NetworkMessage header) is hoisted
    assertNotNull(decoded.publisherId());
    assertEquals("MyPublisher", decoded.publisherId().toCanonicalString());

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(102), message.dataSetWriterId());
    assertEquals(uint(25460), message.sequenceNumber());
    assertEquals(
        new ConfigurationVersionDataType(uint(0), uint(672341762L)),
        message.configurationVersion());
    // no MessageType member: a DataSetMessage with a Payload is a key frame
    assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
    assertNotNull(message.timestamp());

    assertEquals(2, message.fields().size());
    DecodedField locationName = message.fields().get(0);
    assertEquals("LocationName", locationName.fieldName());
    assertEquals("Building A", locationName.value().value().value());

    // the collapsed structure value surfaces as a JSON ExtensionObject (metadata-less decode)
    DecodedField coordinate = message.fields().get(1);
    assertEquals("Coordinate", coordinate.fieldName());
    ExtensionObject.Json xo =
        assertInstanceOf(ExtensionObject.Json.class, coordinate.value().value().value());
    JsonObject body = JsonParser.parseString(xo.getBody()).getAsJsonObject();
    assertEquals(1, body.get("X").getAsInt());
    assertEquals(0.2, body.get("Y").getAsDouble());
  }

  /** The spec's own §7.2.5.4.2 example 2, verbatim: DataValue fields with status/timestamps. */
  @Test
  void decodeSpecExampleWithFieldStatus() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            """
            {
              "PublisherId":"MyPublisher",
              "DataSetWriterId":102,
              "SequenceNumber":68468,
              "MinorVersion":672341762,
              "Timestamp":"2021-09-27T18:45:19.555Z",
              "Payload":
              {
                "LocationName":
                {
                  "Value":"Building A",
                  "Status":{"Code":1073741824,"Symbol":"Uncertain"},
                  "SourceTimestamp":"2021-09-27T11:32:38.349925Z"
                },
                "Coordinate":
                {
                  "Value":
                  {
                    "X":1,
                    "Y":0.2
                  },
                  "SourceTimestamp":"2021-09-27T11:32:38.349925Z"
                }
              }
            }
            """);

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(uint(68468), message.sequenceNumber());

    DataValue locationName = message.fields().get(0).value();
    assertEquals("Building A", locationName.value().value());
    assertEquals(new StatusCode(0x40000000L), locationName.statusCode());
    assertNotNull(locationName.sourceTime());
    assertEquals(
        Instant.parse("2021-09-27T11:32:38.349925Z"), locationName.sourceTime().getJavaInstant());

    DataValue coordinate = message.fields().get(1).value();
    assertInstanceOf(ExtensionObject.Json.class, coordinate.value().value());
    assertTrue(coordinate.statusCode().isGood());
    assertNotNull(coordinate.sourceTime());
  }

  /**
   * Encoding the worked-example metadata reproduces grounding example 7.3: all eight Table 188
   * members in order, CompactEncoding throughout. {@code MessageId} and {@code Timestamp} are
   * generated and asserted by shape; Guid strings are compared case-insensitively (the stock struct
   * codec uppercases Guids inside {@code MetaData}, the spec is case-agnostic).
   */
  @Test
  void encodeMetaDataMatchesWorkedExample() throws Exception {
    var context =
        MetaDataEncodeContext.of(
            ENCODING_CONTEXT,
            PUBLISHER_ID,
            group(JsonNetworkMessageContentMask.of()),
            writer(
                "commands-writer",
                17,
                JsonDataSetMessageContentMask.of(),
                DataSetFieldContentMask.of()));

    EncodedNetworkMessage encoded = MAPPING.encodeMetaData(context, exampleMetaData());

    assertEquals(1, encoded.writers().size());
    assertEquals("commands-writer", encoded.writers().get(0).name());
    assertEquals(ushort(17), encoded.writers().get(0).dataSetWriterId());

    JsonObject actual = toJson(encoded).getAsJsonObject();

    // all 8 Table 188 members, in table order, and nothing else
    assertEquals(
        List.of(
            "MessageId",
            "MessageType",
            "PublisherId",
            "DataSetWriterId",
            "WriterGroupName",
            "DataSetWriterName",
            "Timestamp",
            "MetaData"),
        List.copyOf(actual.keySet()));

    UUID.fromString(actual.get("MessageId").getAsString());
    assertEquals("ua-metadata", actual.get("MessageType").getAsString());
    assertEquals("line-7", actual.get("PublisherId").getAsString());
    assertEquals(17, actual.get("DataSetWriterId").getAsInt());
    assertEquals("json-writers", actual.get("WriterGroupName").getAsString());
    assertEquals("commands-writer", actual.get("DataSetWriterName").getAsString());
    Instant.parse(actual.get("Timestamp").getAsString());

    // the MetaData member is the CompactEncoding of DataSetMetaDataType: null/default members
    // (Namespaces, Description, FieldFlags=0, MaxStringLength=0, ArrayDimensions, Properties)
    // are omitted
    JsonObject expectedMetaData =
        JsonParser.parseString(
                """
                {
                  "Name": "Commands",
                  "Fields": [
                    {
                      "Name": "CommandedSpeed",
                      "BuiltInType": 11,
                      "DataType": "i=11",
                      "ValueRank": -1,
                      "DataSetFieldId": "3c1a4f6e-0f2a-4d3b-9d51-6e7f8a9b0c1d"
                    },
                    {
                      "Name": "CommandedMode",
                      "BuiltInType": 6,
                      "DataType": "i=6",
                      "ValueRank": -1,
                      "DataSetFieldId": "0a1b2c3d-4e5f-6071-8293-a4b5c6d7e8f9"
                    }
                  ],
                  "DataSetClassId": "2dc07ece-cab9-4470-8f8a-2c1ead207e0e",
                  "ConfigurationVersion": { "MajorVersion": 1444863032, "MinorVersion": 1444863033 }
                }
                """)
            .getAsJsonObject();

    assertEquals(
        normalizeUuidStrings(expectedMetaData), normalizeUuidStrings(actual.get("MetaData")));
  }

  /** Grounding worked example 7.3, decoded verbatim. */
  @Test
  void decodeMetaDataWorkedExample() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            """
            {
              "MessageId": "9f1b2c44-7a3e-4ce2-9b1f-1a2b3c4d5e6f",
              "MessageType": "ua-metadata",
              "PublisherId": "line-7",
              "DataSetWriterId": 17,
              "WriterGroupName": "json-writers",
              "DataSetWriterName": "commands-writer",
              "Timestamp": "2026-06-11T18:45:00Z",
              "MetaData": {
                "Name": "Commands",
                "Fields": [
                  {
                    "Name": "CommandedSpeed",
                    "FieldFlags": 0,
                    "BuiltInType": 11,
                    "DataType": "i=11",
                    "ValueRank": -1,
                    "DataSetFieldId": "3c1a4f6e-0f2a-4d3b-9d51-6e7f8a9b0c1d"
                  },
                  {
                    "Name": "CommandedMode",
                    "FieldFlags": 0,
                    "BuiltInType": 6,
                    "DataType": "i=6",
                    "ValueRank": -1,
                    "DataSetFieldId": "0a1b2c3d-4e5f-6071-8293-a4b5c6d7e8f9"
                  }
                ],
                "DataSetClassId": "2dc07ece-cab9-4470-8f8a-2c1ead207e0e",
                "ConfigurationVersion": { "MajorVersion": 1444863032, "MinorVersion": 1444863033 }
              }
            }
            """);

    assertTrue(decoded.messages().isEmpty());
    assertNotNull(decoded.publisherId());
    assertEquals("line-7", decoded.publisherId().toCanonicalString());

    assertEquals(1, decoded.metaData().size());
    assertEquals(ushort(17), decoded.metaData().get(0).dataSetWriterId());

    DataSetMetaDataType metaData = decoded.metaData().get(0).metaData();
    assertEquals("Commands", metaData.getName());
    assertEquals(CLASS_ID, metaData.getDataSetClassId());
    assertEquals(EXAMPLE_VERSION, metaData.getConfigurationVersion());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(2, fields.length);
    assertEquals("CommandedSpeed", fields[0].getName());
    assertEquals(11, fields[0].getBuiltInType().intValue());
    assertEquals(NodeIds.Double, fields[0].getDataType());
    assertEquals(-1, fields[0].getValueRank());
    assertEquals(SPEED_FIELD_ID, fields[0].getDataSetFieldId());
    assertEquals("CommandedMode", fields[1].getName());
    assertEquals(NodeIds.Int32, fields[1].getDataType());
    assertEquals(MODE_FIELD_ID, fields[1].getDataSetFieldId());
  }

  /** {@code ua-metadata} round-trips: encode, decode, and recover the salient struct members. */
  @Test
  void metaDataRoundTripsThroughDecode() throws Exception {
    var context =
        MetaDataEncodeContext.of(
            ENCODING_CONTEXT,
            PUBLISHER_ID,
            group(JsonNetworkMessageContentMask.of()),
            writer(
                "commands-writer",
                17,
                JsonDataSetMessageContentMask.of(),
                DataSetFieldContentMask.of()));

    DataSetMetaDataType original = exampleMetaData();
    EncodedNetworkMessage encoded = MAPPING.encodeMetaData(context, original);

    String wire;
    try {
      wire = encoded.data().toString(java.nio.charset.StandardCharsets.UTF_8);
    } finally {
      encoded.data().release();
    }

    DecodedNetworkMessage decoded = decode(wire);
    assertEquals(1, decoded.metaData().size());
    assertEquals(ushort(17), decoded.metaData().get(0).dataSetWriterId());

    DataSetMetaDataType roundTripped = decoded.metaData().get(0).metaData();
    assertEquals(original.getName(), roundTripped.getName());
    assertEquals(original.getDataSetClassId(), roundTripped.getDataSetClassId());
    assertEquals(original.getConfigurationVersion(), roundTripped.getConfigurationVersion());

    FieldMetaData[] originalFields = original.getFields();
    FieldMetaData[] roundTrippedFields = roundTripped.getFields();
    assertNotNull(roundTrippedFields);
    assertEquals(originalFields.length, roundTrippedFields.length);
    for (int i = 0; i < originalFields.length; i++) {
      assertEquals(originalFields[i].getName(), roundTrippedFields[i].getName());
      assertEquals(originalFields[i].getBuiltInType(), roundTrippedFields[i].getBuiltInType());
      assertEquals(originalFields[i].getDataType(), roundTrippedFields[i].getDataType());
      assertEquals(originalFields[i].getValueRank(), roundTrippedFields[i].getValueRank());
      assertEquals(
          originalFields[i].getDataSetFieldId(), roundTrippedFields[i].getDataSetFieldId());
    }
  }
}
