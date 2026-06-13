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
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.encodeSingle;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.field;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.firstMessage;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.group;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.keyFrame;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.metaData;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.writer;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * DataSet field value encoding and decoding: the Variant-vs-DataValue representation decision (Part
 * 14 Table 32/112), the PubSub Verbose collapse rules (§7.2.5.4.2/.3, Tables 186/187), Bad and
 * Uncertain field status handling (Table 35), and tolerant decode across the current
 * Compact/Verbose and deprecated Reversible/NonReversible spellings (OPC 10000-6 §5.4 / Annex H).
 */
class JsonFieldEncodingTest {

  private static final JsonDataSetMessageContentMask VERBOSE_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  private static final JsonDataSetMessageContentMask COMPACT_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.FieldEncoding1,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  /** Encode one field and return the whole DataSetMessage object. */
  private static JsonObject encodeMessage(
      JsonDataSetMessageContentMask dsmMask,
      DataSetFieldContentMask fieldMask,
      FieldMetaData fieldMetaData,
      DataValue value)
      throws Exception {

    DataSetMetaDataType meta = metaData(fieldMetaData);
    return firstMessage(
        encodeSingle(
            group(JsonNetworkMessageContentMask.of()),
            List.of(keyFrame(writer("w", 3, dsmMask, fieldMask), 7, meta, value))));
  }

  /** Encode one field and return its Payload member. */
  private static JsonElement encodeField(
      JsonDataSetMessageContentMask dsmMask,
      DataSetFieldContentMask fieldMask,
      FieldMetaData fieldMetaData,
      DataValue value)
      throws Exception {

    return encodeMessage(dsmMask, fieldMask, fieldMetaData, value)
        .get("Payload")
        .getAsJsonObject()
        .get(fieldMetaData.getName());
  }

  private static DataValue good(Object value) {
    return new DataValue(Variant.of(value), StatusCode.GOOD, null, null);
  }

  // region representation selection (Table 32 / 7.2.5.4.1)

  @Test
  void emptyFieldMaskSelectsVariantRepresentation() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(),
            field("Speed", 11, NodeIds.Double, -1),
            good(2.5));
    assertTrue(encoded.isJsonPrimitive());
    assertEquals(2.5, encoded.getAsDouble());
  }

  @Test
  void rawDataOnlyMaskSelectsVariantRepresentation() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData),
            field("Speed", 11, NodeIds.Double, -1),
            good(2.5));
    assertTrue(encoded.isJsonPrimitive());
    assertEquals(2.5, encoded.getAsDouble());
  }

  @Test
  void statusCodeBitSelectsDataValueRepresentation() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
            field("Speed", 11, NodeIds.Double, -1),
            good(2.5));
    assertTrue(encoded.isJsonObject());
    assertEquals(2.5, encoded.getAsJsonObject().get("Value").getAsDouble());
  }

  /** The DataValue object carries exactly the masked-in members, defaults omitted (§5.4.2.18). */
  @Test
  void dataValueRepresentationEmitsExactlyMaskedMembers() throws Exception {
    DateTime sourceTime = new DateTime(Instant.parse("2026-06-11T10:00:00Z"));
    DateTime serverTime = new DateTime(Instant.parse("2026-06-11T10:00:01Z"));
    var value =
        new DataValue(
            Variant.of(2.5),
            new StatusCode(0x40000000L),
            sourceTime,
            ushort(100),
            serverTime,
            ushort(200));

    var allBits =
        DataSetFieldContentMask.of(
            DataSetFieldContentMask.Field.StatusCode,
            DataSetFieldContentMask.Field.SourceTimestamp,
            DataSetFieldContentMask.Field.ServerTimestamp,
            DataSetFieldContentMask.Field.SourcePicoSeconds,
            DataSetFieldContentMask.Field.ServerPicoSeconds);

    JsonObject encoded =
        encodeField(VERBOSE_MASK, allBits, field("Speed", 11, NodeIds.Double, -1), value)
            .getAsJsonObject();
    assertEquals(
        Set.of(
            "Value",
            "Status",
            "SourceTimestamp",
            "SourcePicoseconds",
            "ServerTimestamp",
            "ServerPicoseconds"),
        encoded.keySet());
    assertEquals(100, encoded.get("SourcePicoseconds").getAsInt());
    assertEquals(200, encoded.get("ServerPicoseconds").getAsInt());
    assertEquals("2026-06-11T10:00:00Z", encoded.get("SourceTimestamp").getAsString());

    // SourceTimestamp-only mask: the other members are absent even though the value carries them
    JsonObject sourceOnly =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.SourceTimestamp),
                field("Speed", 11, NodeIds.Double, -1),
                value)
            .getAsJsonObject();
    assertEquals(Set.of("Value", "SourceTimestamp"), sourceOnly.keySet());
  }

  /**
   * §6.2.4.2 Table 32: SourcePicoSeconds and ServerPicoSeconds are ignored unless their parent
   * timestamp bit is also set — even when the value carries them and a picosecond bit alone selects
   * the DataValue representation.
   */
  @Test
  void picosecondsRequireTheirParentTimestamp() throws Exception {
    DateTime sourceTime = new DateTime(Instant.parse("2026-06-11T10:00:00Z"));
    DateTime serverTime = new DateTime(Instant.parse("2026-06-11T10:00:01Z"));
    var value =
        new DataValue(
            Variant.of(2.5), StatusCode.GOOD, sourceTime, ushort(100), serverTime, ushort(200));

    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(
                    DataSetFieldContentMask.Field.SourcePicoSeconds,
                    DataSetFieldContentMask.Field.ServerPicoSeconds),
                field("Speed", 11, NodeIds.Double, -1),
                value)
            .getAsJsonObject();

    // the picosecond bits select the DataValue representation, but with neither parent timestamp
    // masked in the picoseconds members must be absent
    assertEquals(Set.of("Value"), encoded.keySet());
  }

  /** Good status, null timestamps, and zero picoseconds are omitted as defaults. */
  @Test
  void dataValueRepresentationOmitsDefaults() throws Exception {
    var allBits =
        DataSetFieldContentMask.of(
            DataSetFieldContentMask.Field.StatusCode,
            DataSetFieldContentMask.Field.SourceTimestamp,
            DataSetFieldContentMask.Field.ServerTimestamp,
            DataSetFieldContentMask.Field.SourcePicoSeconds,
            DataSetFieldContentMask.Field.ServerPicoSeconds);

    JsonObject encoded =
        encodeField(VERBOSE_MASK, allBits, field("Speed", 11, NodeIds.Double, -1), good(2.5))
            .getAsJsonObject();
    assertEquals(Set.of("Value"), encoded.keySet());
  }

  // endregion

  // region Verbose collapse (Tables 186/187)

  @Test
  void verboseCollapsesConcreteScalarToBareValue() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(),
            field("Count", 6, NodeIds.Int32, -1),
            good(1234));
    assertEquals(1234, encoded.getAsInt());
  }

  @Test
  void verboseCollapsesConcreteArrayToBareArray() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(),
            field("Counts", 6, NodeIds.Int32, 1),
            good(new Integer[] {1, 2, 3, 4}));
    assertTrue(encoded.isJsonArray());
    assertEquals(4, encoded.getAsJsonArray().size());
    assertEquals(3, encoded.getAsJsonArray().get(2).getAsInt());
  }

  /**
   * Table 186 BaseDataType row: an abstract field type keeps the {@code {"UaType":12,"Value":...}}
   * wrapper with RawData=FALSE and drops {@code UaType} with RawData=TRUE.
   */
  @Test
  void verboseAbstractTypeKeepsUaTypeWrapper() throws Exception {
    FieldMetaData abstractField = field("Anything", 24, NodeIds.BaseDataType, -1);

    JsonObject withUaType =
        encodeField(VERBOSE_MASK, DataSetFieldContentMask.of(), abstractField, good("Apple"))
            .getAsJsonObject();
    assertEquals(12, withUaType.get("UaType").getAsInt());
    assertEquals("Apple", withUaType.get("Value").getAsString());

    JsonObject rawData =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData),
                abstractField,
                good("Apple"))
            .getAsJsonObject();
    assertEquals(Set.of("Value"), rawData.keySet());
    assertEquals("Apple", rawData.get("Value").getAsString());
  }

  /**
   * Table 186 "ValueRank Any" row: an abstract ValueRank (-2 or 0) keeps the {@code {"Value":...}}
   * wrapper even for a concrete type, without {@code UaType}.
   */
  @Test
  void verboseAbstractValueRankKeepsValueWrapper() throws Exception {
    JsonObject rankAny =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Count", 6, NodeIds.Int32, -2),
                good(11))
            .getAsJsonObject();
    assertEquals(Set.of("Value"), rankAny.keySet());
    assertEquals(11, rankAny.get("Value").getAsInt());

    JsonObject oneOrMore =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Counts", 6, NodeIds.Int32, 0),
                good(new Integer[] {1, 2}))
            .getAsJsonObject();
    assertEquals(Set.of("Value"), oneOrMore.keySet());
    assertTrue(oneOrMore.get("Value").isJsonArray());
  }

  /** Table 186 two-dimensional array row: flat {@code Value} plus {@code Dimensions}. */
  @Test
  void verboseMatrixKeepsValueAndDimensions() throws Exception {
    var matrix = new Matrix(new Integer[] {1, 2, 3, 4}, new int[] {2, 2});

    JsonObject concrete =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Grid", 6, NodeIds.Int32, 2),
                good(matrix))
            .getAsJsonObject();
    assertEquals(Set.of("Value", "Dimensions"), concrete.keySet());
    assertEquals(4, concrete.get("Value").getAsJsonArray().size());
    assertEquals(2, concrete.get("Dimensions").getAsJsonArray().get(0).getAsInt());
    assertEquals(2, concrete.get("Dimensions").getAsJsonArray().get(1).getAsInt());

    // an abstract field type adds UaType to the matrix wrapper (RawData clear)
    JsonObject abstractType =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Grid", 24, NodeIds.BaseDataType, -2),
                good(matrix))
            .getAsJsonObject();
    assertEquals(Set.of("UaType", "Value", "Dimensions"), abstractType.keySet());
    assertEquals(6, abstractType.get("UaType").getAsInt());
  }

  /** A structure value of exactly the field DataType encodes as the bare structure object. */
  @Test
  void verboseStructWithMatchingTypeOmitsUaTypeId() throws Exception {
    var struct = new ConfigurationVersionDataType(uint(3), uint(9));

    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Version", 22, NodeIds.ConfigurationVersionDataType, -1),
                good(struct))
            .getAsJsonObject();
    assertEquals(Set.of("MajorVersion", "MinorVersion"), encoded.keySet());
    assertEquals(3, encoded.get("MajorVersion").getAsLong());
    assertEquals(9, encoded.get("MinorVersion").getAsLong());
  }

  /**
   * A structure value that is a subtype of the field DataType carries {@code UaTypeId} as its first
   * member (§7.2.5.4.3); RawData=TRUE strips it.
   */
  @Test
  void verboseStructSubtypeInsertsUaTypeId() throws Exception {
    var struct = new ConfigurationVersionDataType(uint(3), uint(9));
    FieldMetaData abstractStructField = field("Version", 22, NodeIds.Structure, -1);

    JsonObject withTypeId =
        encodeField(VERBOSE_MASK, DataSetFieldContentMask.of(), abstractStructField, good(struct))
            .getAsJsonObject();
    assertEquals(
        List.of("UaTypeId", "MajorVersion", "MinorVersion"), List.copyOf(withTypeId.keySet()));
    assertEquals(
        NodeIds.ConfigurationVersionDataType.toParseableString(),
        withTypeId.get("UaTypeId").getAsString());

    JsonObject rawData =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData),
                abstractStructField,
                good(struct))
            .getAsJsonObject();
    assertEquals(Set.of("MajorVersion", "MinorVersion"), rawData.keySet());
  }

  /** Verbose Enumeration: the {@code "Name_Value"} string form (OPC 10000-6 §5.4.4.2). */
  @Test
  void verboseEnumEmitsNameValueString() throws Exception {
    JsonElement encoded =
        encodeField(
            VERBOSE_MASK,
            DataSetFieldContentMask.of(),
            field("Mode", 6, NodeIds.MessageSecurityMode, -1),
            good(MessageSecurityMode.Sign));
    assertEquals("Sign_2", encoded.getAsString());
  }

  /**
   * An enumeration inside a {@code UaType} wrapper (abstract field type) is a JSON number even
   * under VerboseEncoding (OPC 10000-6 §5.4.4: inside a Variant, UaType is Int32(6) and the value
   * is a number); the {@code "Name_Value"} string applies only to the collapsed bare emission. A
   * conformant decoder typing by {@code UaType} round-trips the numeric value.
   */
  @Test
  void verboseEnumInsideUaTypeWrapperEmitsNumber() throws Exception {
    FieldMetaData abstractField = field("Anything", 24, NodeIds.BaseDataType, -1);

    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                abstractField,
                good(MessageSecurityMode.Sign))
            .getAsJsonObject();
    assertEquals(6, encoded.get("UaType").getAsInt());
    assertEquals(2, encoded.get("Value").getAsInt());

    // round trip: the decoder types the value by UaType=6 (Int32)
    DataValue decoded =
        decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":" + encoded + "}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value();
    assertTrue(decoded.statusCode().isGood());
    assertEquals(2, decoded.value().value());

    // RawData strips the UaType member but the value stays the Variant's numeric form
    JsonObject rawData =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData),
                abstractField,
                good(MessageSecurityMode.Sign))
            .getAsJsonObject();
    assertEquals(Set.of("Value"), rawData.keySet());
    assertEquals(2, rawData.get("Value").getAsInt());
  }

  /** The DataValue-representation {@code UaType} wrapper also carries the numeric enum form. */
  @Test
  void verboseEnumInDataValueRepresentationWrapperEmitsNumber() throws Exception {
    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
                field("Anything", 24, NodeIds.BaseDataType, -1),
                good(MessageSecurityMode.Sign))
            .getAsJsonObject();
    assertEquals(6, encoded.get("UaType").getAsInt());
    assertEquals(2, encoded.get("Value").getAsInt());
  }

  // endregion

  // region Compact forms

  /** CompactEncoding Variant representation: the standard §5.4.2.17 Variant object, no collapse. */
  @Test
  void compactEmitsFullVariantObject() throws Exception {
    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(),
                field("Speed", 11, NodeIds.Double, -1),
                good(2.5))
            .getAsJsonObject();
    assertEquals(11, encoded.get("UaType").getAsInt());
    assertEquals(2.5, encoded.get("Value").getAsDouble());
  }

  /** Compact ignores the RawData bit (Table 112). */
  @Test
  void compactIgnoresRawDataBit() throws Exception {
    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData),
                field("Speed", 11, NodeIds.Double, -1),
                good(2.5))
            .getAsJsonObject();
    assertEquals(11, encoded.get("UaType").getAsInt());
  }

  /**
   * Compact DataValue representation: the hoisted {@code {UaType, Value, ...}} form of OPC 10000-6
   * Table 42, with {@code UaType} present even for concrete field types (no collapse).
   */
  @Test
  void compactDataValueRepresentationHoistsUaType() throws Exception {
    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.SourceTimestamp),
                field("Speed", 11, NodeIds.Double, -1),
                new DataValue(
                    Variant.of(2.5),
                    StatusCode.GOOD,
                    new DateTime(Instant.parse("2026-06-11T10:00:00Z")),
                    null,
                    null,
                    null))
            .getAsJsonObject();
    assertEquals(Set.of("UaType", "Value", "SourceTimestamp"), encoded.keySet());
    assertEquals(11, encoded.get("UaType").getAsInt());
    assertEquals(2.5, encoded.get("Value").getAsDouble());
  }

  /** Compact Enumeration: a JSON number (§5.4.4.1). */
  @Test
  void compactEnumEmitsNumber() throws Exception {
    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
                field("Mode", 6, NodeIds.MessageSecurityMode, -1),
                good(MessageSecurityMode.Sign))
            .getAsJsonObject();
    assertEquals(6, encoded.get("UaType").getAsInt());
    assertEquals(2, encoded.get("Value").getAsInt());
  }

  /**
   * Compact structure values are the standard ExtensionObject form of OPC 10000-6 §5.4.2.16: the
   * structure's JSON object with {@code UaTypeId} always inserted — even when the value's DataType
   * equals the field DataType (the §7.2.5.4.2 collapse is Verbose-only).
   */
  @Test
  void compactStructInsertsUaTypeIdEvenForExactType() throws Exception {
    var struct = new ConfigurationVersionDataType(uint(3), uint(9));

    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
                field("Version", 22, NodeIds.ConfigurationVersionDataType, -1),
                good(struct))
            .getAsJsonObject();
    assertEquals(22, encoded.get("UaType").getAsInt());

    JsonObject value = encoded.get("Value").getAsJsonObject();
    assertEquals(
        NodeIds.ConfigurationVersionDataType.toParseableString(),
        value.get("UaTypeId").getAsString());
    assertEquals(3, value.get("MajorVersion").getAsLong());
    assertEquals(9, value.get("MinorVersion").getAsLong());
  }

  /**
   * Per Table 112 the RawData bit is ignored under CompactEncoding: it must not strip {@code
   * UaTypeId} from structure values.
   */
  @Test
  void compactStructIgnoresRawDataBit() throws Exception {
    var struct = new ConfigurationVersionDataType(uint(3), uint(9));

    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(
                    DataSetFieldContentMask.Field.StatusCode,
                    DataSetFieldContentMask.Field.RawData),
                field("Version", 22, NodeIds.Structure, -1),
                good(struct))
            .getAsJsonObject();

    JsonObject value = encoded.get("Value").getAsJsonObject();
    assertEquals(
        NodeIds.ConfigurationVersionDataType.toParseableString(),
        value.get("UaTypeId").getAsString());
  }

  // endregion

  // region Bad/Uncertain status handling (Table 35)

  /** Table 35 footnote c: a Bad status is transferred instead of the value (Variant repr). */
  @Test
  void badFieldStatusReplacesVariantValue() throws Exception {
    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(),
                field("Speed", 11, NodeIds.Double, -1),
                new DataValue(Variant.of(2.5), new StatusCode(0x80000000L), null, null))
            .getAsJsonObject();
    assertEquals(0x80000000L, encoded.get("Code").getAsLong());
    assertFalse(encoded.has("Value"));

    // ... and decodes back as a Bad field status with a null value
    JsonObject message = new JsonObject();
    message.addProperty("DataSetWriterId", 3);
    var payload = new JsonObject();
    payload.add("Speed", encoded);
    message.add("Payload", payload);

    DataValue decoded = decode(message.toString()).messages().get(0).fields().get(0).value();
    assertTrue(decoded.statusCode().isBad());
    assertTrue(decoded.value().isNull());
  }

  /** Under Compact the StatusCode object is the bare {@code {"Code":n}} (no Symbol). */
  @Test
  void badFieldStatusUnderCompactIsCodeOnly() throws Exception {
    JsonObject encoded =
        encodeField(
                COMPACT_MASK,
                DataSetFieldContentMask.of(),
                field("Speed", 11, NodeIds.Double, -1),
                new DataValue(Variant.of(2.5), new StatusCode(0x80000000L), null, null))
            .getAsJsonObject();
    assertEquals(Set.of("Code"), encoded.keySet());
    assertEquals(0x80000000L, encoded.get("Code").getAsLong());
  }

  /** DataValue representation: Bad status stays at the field level, value position null. */
  @Test
  void badFieldStatusInDataValueRepresentation() throws Exception {
    JsonObject encoded =
        encodeField(
                VERBOSE_MASK,
                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
                field("Speed", 11, NodeIds.Double, -1),
                new DataValue(Variant.NULL_VALUE, new StatusCode(0x80000000L), null, null))
            .getAsJsonObject();
    // Verbose emits the null Value member explicitly
    assertTrue(encoded.get("Value").isJsonNull());
    assertEquals(0x80000000L, encoded.get("Status").getAsJsonObject().get("Code").getAsLong());
  }

  /**
   * Table 35 footnote b: an Uncertain field status is dropped at the field level under the Variant
   * representation and folded into the DataSetMessage header Status.
   */
  @Test
  void uncertainFieldStatusFoldsIntoHeaderUnderVariantRepresentation() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.Status,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject message =
        encodeMessage(
            dsmMask,
            DataSetFieldContentMask.of(),
            field("Speed", 11, NodeIds.Double, -1),
            new DataValue(Variant.of(1.0), new StatusCode(0x40000000L), null, null));

    assertEquals(0x40000000L, message.get("Status").getAsJsonObject().get("Code").getAsLong());
    // the value itself is emitted as-is
    assertEquals(1.0, message.get("Payload").getAsJsonObject().get("Speed").getAsDouble());
  }

  /** A non-Good draft status wins over folded Uncertain field statuses. */
  @Test
  void draftStatusWinsOverFoldedUncertainFieldStatus() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.Status,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    var draft =
        org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft.of(
            writer("w", 3, dsmMask, DataSetFieldContentMask.of()),
            uint(7),
            null,
            new StatusCode(0x80000000L),
            new ConfigurationVersionDataType(uint(1), uint(1)),
            false,
            List.of(new DataValue(Variant.of(1.0), new StatusCode(0x40000000L), null, null)),
            metaData(field("Speed", 11, NodeIds.Double, -1)));

    JsonObject message =
        firstMessage(encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft)));
    assertEquals(0x80000000L, message.get("Status").getAsJsonObject().get("Code").getAsLong());
  }

  /**
   * Under the DataValue representation an Uncertain status stays at the field level and the header
   * Status remains Good (omitted).
   */
  @Test
  void uncertainFieldStatusStaysAtFieldLevelUnderDataValueRepresentation() throws Exception {
    var dsmMask =
        JsonDataSetMessageContentMask.of(
            JsonDataSetMessageContentMask.Field.Status,
            JsonDataSetMessageContentMask.Field.FieldEncoding2);

    JsonObject message =
        encodeMessage(
            dsmMask,
            DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode),
            field("Speed", 11, NodeIds.Double, -1),
            new DataValue(Variant.of(1.0), new StatusCode(0x40000000L), null, null));

    assertFalse(message.has("Status"));
    JsonObject encodedField =
        message.get("Payload").getAsJsonObject().get("Speed").getAsJsonObject();
    assertEquals(0x40000000L, encodedField.get("Status").getAsJsonObject().get("Code").getAsLong());
  }

  // endregion

  // region decode: all four field encodings, tolerant

  /** The same Double value in all four field-encoding spellings decodes identically. */
  @ParameterizedTest
  @MethodSource("fourEncodingSpellings")
  void decodeDoubleAcrossAllFourFieldEncodings(String name, String payloadJson) throws Exception {
    DataValue value =
        decode("{\"DataSetWriterId\":1,\"Payload\":" + payloadJson + "}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value();
    assertEquals(2.5, value.value().value(), name);
    assertTrue(value.statusCode().isGood());
  }

  static Stream<Arguments> fourEncodingSpellings() {
    return Stream.of(
        Arguments.of("Verbose collapsed", "{\"A\":2.5}"),
        Arguments.of("Compact wrapper", "{\"A\":{\"UaType\":11,\"Value\":2.5}}"),
        Arguments.of("deprecated Reversible", "{\"A\":{\"Type\":11,\"Body\":2.5}}"),
        // NonReversible inlines the bare body value (Annex H.8/H.9)
        Arguments.of("deprecated NonReversible", "{\"A\":2.5}"));
  }

  /** Typed wrapper decode by built-in type id, including string-form Int64/UInt64 and specials. */
  @ParameterizedTest
  @MethodSource("typedWrapperValues")
  void decodeTypedWrapperValues(String json, Object expected) throws Exception {
    Object value =
        decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":" + json + "}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value()
            .value()
            .value();
    assertEquals(expected, value);
  }

  static Stream<Arguments> typedWrapperValues() {
    return Stream.of(
        Arguments.of("{\"UaType\":1,\"Value\":true}", true),
        Arguments.of("{\"UaType\":8,\"Value\":\"-9007199254740993\"}", -9007199254740993L),
        Arguments.of(
            "{\"UaType\":9,\"Value\":\"18446744073709551615\"}",
            ULong.valueOf("18446744073709551615")),
        Arguments.of("{\"UaType\":10,\"Value\":\"Infinity\"}", Float.POSITIVE_INFINITY),
        Arguments.of("{\"UaType\":11,\"Value\":\"NaN\"}", Double.NaN),
        Arguments.of("{\"UaType\":12,\"Value\":\"hello\"}", "hello"),
        Arguments.of(
            "{\"UaType\":13,\"Value\":\"2026-06-11T12:00:00+02:00\"}",
            new DateTime(Instant.parse("2026-06-11T10:00:00Z"))),
        Arguments.of(
            "{\"UaType\":14,\"Value\":\"3c1a4f6e-0f2a-4d3b-9d51-6e7f8a9b0c1d\"}",
            UUID.fromString("3c1a4f6e-0f2a-4d3b-9d51-6e7f8a9b0c1d")),
        Arguments.of("{\"UaType\":15,\"Value\":\"AQID\"}", ByteString.of(new byte[] {1, 2, 3})),
        Arguments.of("{\"UaType\":17,\"Value\":\"i=2253\"}", new NodeId(0, 2253)));
  }

  @Test
  void decodeWrapperArrayAndMatrixForms() throws Exception {
    DecodedFields fields =
        new DecodedFields(
            decode(
                "{\"DataSetWriterId\":1,\"Payload\":{"
                    + "\"Arr\":{\"UaType\":6,\"Value\":[1,2,3]},"
                    + "\"Mat\":{\"UaType\":6,\"Value\":[1,2,3,4],\"Dimensions\":[2,2]}}}"));

    Object array = fields.value(0);
    assertInstanceOf(Integer[].class, array);
    assertEquals(3, ((Integer[]) array)[2]);

    Matrix matrix = assertInstanceOf(Matrix.class, fields.value(1));
    assertEquals(2, matrix.getDimensions()[0]);
    assertEquals(2, matrix.getDimensions()[1]);
  }

  /** Current-form DataValue members decode into the DataValue slots. */
  @Test
  void decodeDataValueRepresentationMembers() throws Exception {
    DataValue value =
        decode(
                "{\"DataSetWriterId\":1,\"Payload\":{\"A\":{"
                    + "\"Value\":2.5,"
                    + "\"Status\":{\"Code\":1073741824},"
                    + "\"SourceTimestamp\":\"2026-06-11T10:00:00Z\","
                    + "\"SourcePicoseconds\":100,"
                    + "\"ServerTimestamp\":\"2026-06-11T10:00:01Z\","
                    + "\"ServerPicoseconds\":200}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value();

    assertEquals(2.5, value.value().value());
    assertEquals(new StatusCode(0x40000000L), value.statusCode());
    assertNotNull(value.sourceTime());
    assertEquals(Instant.parse("2026-06-11T10:00:00Z"), value.sourceTime().getJavaInstant());
    assertEquals(ushort(100), value.sourcePicoseconds());
    assertNotNull(value.serverTime());
    assertEquals(ushort(200), value.serverPicoseconds());
  }

  /** The hoisted {@code UaType} of a Table 42/187 DataValue object is accepted. */
  @Test
  void decodeHoistedUaTypeDataValue() throws Exception {
    DataValue value =
        decode(
                "{\"DataSetWriterId\":1,\"Payload\":{\"A\":{"
                    + "\"UaType\":12,\"Value\":\"Apple\","
                    + "\"SourceTimestamp\":\"2026-06-11T10:00:00Z\"}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value();
    assertEquals("Apple", value.value().value());
    assertNotNull(value.sourceTime());
  }

  /** A deprecated-Reversible ExtensionObject wrapper delegates to the stock decoder. */
  @Test
  void decodeDeprecatedReversibleExtensionObject() throws Exception {
    Object value =
        decode(
                "{\"DataSetWriterId\":1,\"Payload\":{\"A\":{"
                    + "\"TypeId\":\"i=14593\","
                    + "\"Body\":{\"MajorVersion\":3,\"MinorVersion\":9}}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value()
            .value()
            .value();
    assertInstanceOf(ExtensionObject.class, value);
  }

  /** An inlined {@code UaTypeId} struct (§5.4.2.16) surfaces as a JSON ExtensionObject. */
  @Test
  void decodeInlinedUaTypeIdStruct() throws Exception {
    Object value =
        decode(
                "{\"DataSetWriterId\":1,\"Payload\":{\"A\":{"
                    + "\"UaTypeId\":\"i=14593\",\"MajorVersion\":3,\"MinorVersion\":9}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value()
            .value()
            .value();

    ExtensionObject.Json xo = assertInstanceOf(ExtensionObject.Json.class, value);
    assertEquals(new NodeId(0, 14593), xo.getEncodingOrTypeId());
    JsonObject body = JsonParser.parseString(xo.getBody()).getAsJsonObject();
    assertFalse(body.has("UaTypeId"));
    assertEquals(3, body.get("MajorVersion").getAsInt());
  }

  /** A collapsed struct without any type id surfaces as a JSON ExtensionObject without one. */
  @Test
  void decodePlainStructObjectAsJsonExtensionObject() throws Exception {
    Object value =
        decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":{\"X\":1,\"Y\":0.2}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value()
            .value()
            .value();

    ExtensionObject.Json xo = assertInstanceOf(ExtensionObject.Json.class, value);
    assertEquals(NodeId.NULL_VALUE, xo.getEncodingOrTypeId());
  }

  /** Verbose enum strings are indistinguishable from String values without metadata. */
  @Test
  void verboseEnumStringDecodesAsString() throws Exception {
    Object value =
        decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":\"Suspended_3\"}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value()
            .value()
            .value();
    assertEquals("Suspended_3", value);
  }

  /** An empty object in a value position is a null value with Good status. */
  @Test
  void emptyObjectFieldValueDecodesAsNullGood() throws Exception {
    DataValue value =
        decode("{\"DataSetWriterId\":1,\"Payload\":{\"A\":{}}}")
            .messages()
            .get(0)
            .fields()
            .get(0)
            .value();
    assertTrue(value.value().isNull());
    assertTrue(value.statusCode().isGood());
  }

  /** Deprecated-Reversible numeric StatusCode in the DataSetMessage header is accepted. */
  @Test
  void decodeDeprecatedNumericHeaderStatus() throws Exception {
    var decoded = decode("{\"DataSetWriterId\":1,\"Status\":2147483648,\"Payload\":{\"A\":1}}");
    StatusCode status = decoded.messages().get(0).status();
    assertNotNull(status);
    assertTrue(status.isBad());
  }

  /** A Verbose-encoded payload round-trips through decode with name-keyed fields. */
  @Test
  void verboseEncodeDecodeRoundTrip() throws Exception {
    DataSetMetaDataType meta =
        metaData(
            field("Speed", 11, NodeIds.Double, -1),
            field("Name", 12, NodeIds.String, -1),
            field("Counts", 6, NodeIds.Int32, 1));

    var draft =
        keyFrame(
            writer("w", 12, JsonDataSetMessageContentMask.of(), DataSetFieldContentMask.of()),
            99,
            meta,
            good(10.5),
            good("hello"),
            good(new Integer[] {1, 2, 3}));

    JsonElement encoded = encodeSingle(group(JsonNetworkMessageContentMask.of()), List.of(draft));
    var decoded = decode(encoded.toString());

    assertEquals("line-7", decoded.publisherId().toCanonicalString());
    var message = decoded.messages().get(0);
    assertEquals(ushort(12), message.dataSetWriterId());
    assertEquals(uint(99), message.sequenceNumber());
    assertEquals(3, message.fields().size());
    assertEquals("Speed", message.fields().get(0).fieldName());
    assertEquals(10.5, message.fields().get(0).value().value().value());
    assertEquals("hello", message.fields().get(1).value().value().value());
    Object counts = message.fields().get(2).value().value().value();
    assertInstanceOf(Integer[].class, counts);
    assertEquals(3, ((Integer[]) counts)[2]);
  }

  // endregion

  /** Small accessor for multi-field decode assertions. */
  private record DecodedFields(
      org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage decoded) {

    Object value(int index) {
      return decoded.messages().get(0).fields().get(index).value().value().value();
    }
  }
}
