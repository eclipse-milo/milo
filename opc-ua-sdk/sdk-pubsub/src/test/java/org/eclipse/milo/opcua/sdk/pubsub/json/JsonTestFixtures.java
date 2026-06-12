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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * Shared fixtures for the JSON mapping tests: hand-built configs, metadata, drafts, and
 * encode/decode helpers driving {@link JsonMessageMapping} directly through the SPI {@code of()}
 * factories.
 */
final class JsonTestFixtures {

  static final EncodingContext ENCODING_CONTEXT = new DefaultEncodingContext();

  static final JsonMessageMapping MAPPING = new JsonMessageMapping();

  static final PublisherId PUBLISHER_ID = PublisherId.string("line-7");

  /** The ConfigurationVersion used by the grounding worked examples. */
  static final ConfigurationVersionDataType EXAMPLE_VERSION =
      new ConfigurationVersionDataType(uint(1444863032L), uint(1444863033L));

  private JsonTestFixtures() {}

  /** A {@code DataSetMetaDataType} named "Commands" with the worked-example version. */
  static DataSetMetaDataType metaData(FieldMetaData... fields) {
    return metaData("Commands", null, EXAMPLE_VERSION, fields);
  }

  static DataSetMetaDataType metaData(
      String name,
      @Nullable UUID dataSetClassId,
      ConfigurationVersionDataType version,
      FieldMetaData... fields) {

    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        name,
        LocalizedText.NULL_VALUE,
        fields,
        dataSetClassId != null ? dataSetClassId : new UUID(0L, 0L),
        version);
  }

  static FieldMetaData field(String name, int builtInType, NodeId dataType, int valueRank) {
    return field(name, builtInType, dataType, valueRank, UUID.randomUUID());
  }

  static FieldMetaData field(
      String name, int builtInType, NodeId dataType, int valueRank, UUID dataSetFieldId) {

    return new FieldMetaData(
        name,
        LocalizedText.NULL_VALUE,
        new DataSetFieldFlags(ushort(0)),
        ubyte(builtInType),
        dataType,
        valueRank,
        null,
        uint(0),
        dataSetFieldId,
        null);
  }

  /** A WriterGroupConfig "json-writers" with the given JSON network message content mask. */
  static WriterGroupConfig group(JsonNetworkMessageContentMask mask) {
    return WriterGroupConfig.builder("json-writers")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(1000))
        .messageSettings(JsonWriterGroupSettings.builder().networkMessageContentMask(mask).build())
        .build();
  }

  static DataSetWriterConfig writer(
      String name, int id, JsonDataSetMessageContentMask mask, DataSetFieldContentMask fieldMask) {

    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef("Commands"))
        .dataSetWriterId(ushort(id))
        .settings(JsonDataSetWriterSettings.builder().dataSetMessageContentMask(mask).build())
        .fieldContentMask(fieldMask)
        .build();
  }

  static EncodeContext encodeContext(WriterGroupConfig group, List<DataSetMessageDraft> drafts) {
    return EncodeContext.of(
        ENCODING_CONTEXT, PUBLISHER_ID, group, uint(0), ushort(1), ushort(0), null, drafts);
  }

  /** A key frame draft at the given sequence number with no timestamp and no status. */
  static DataSetMessageDraft keyFrame(
      DataSetWriterConfig writer,
      int sequenceNumber,
      DataSetMetaDataType metaData,
      DataValue... values) {

    return DataSetMessageDraft.of(
        writer,
        ushort(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(1), uint(1)),
        false,
        List.of(values),
        metaData);
  }

  static List<EncodedNetworkMessage> encode(
      WriterGroupConfig group, List<DataSetMessageDraft> drafts) throws UaException {

    return MAPPING.encode(encodeContext(group, drafts));
  }

  /** Encode, assert exactly one NetworkMessage, and return it parsed (buffer released). */
  static JsonElement encodeSingle(WriterGroupConfig group, List<DataSetMessageDraft> drafts)
      throws UaException {

    List<EncodedNetworkMessage> encoded = encode(group, drafts);
    assertEquals(1, encoded.size(), "expected a single encoded NetworkMessage");
    return toJson(encoded.get(0));
  }

  /** Parse one encoded NetworkMessage as a JSON document and release its buffer. */
  static JsonElement toJson(EncodedNetworkMessage message) {
    ByteBuf data = message.data();
    try {
      return JsonParser.parseString(data.toString(StandardCharsets.UTF_8));
    } finally {
      data.release();
    }
  }

  static DecodedNetworkMessage decode(String json) throws UaException {
    ByteBuf buffer = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));
    try {
      return MAPPING.decode(DecodeContext.of(ENCODING_CONTEXT), buffer);
    } finally {
      buffer.release();
    }
  }

  /** The first DataSetMessage object of a NetworkMessage with header. */
  static JsonObject firstMessage(JsonElement networkMessage) {
    JsonElement messages = networkMessage.getAsJsonObject().get("Messages");
    if (messages.isJsonArray()) {
      return messages.getAsJsonArray().get(0).getAsJsonObject();
    }
    return messages.getAsJsonObject();
  }

  /** The named Payload member of the first DataSetMessage. */
  static JsonElement payloadField(JsonElement networkMessage, String fieldName) {
    return firstMessage(networkMessage).get("Payload").getAsJsonObject().get(fieldName);
  }

  /**
   * A deep copy of {@code element} with every string primitive that parses as a UUID lowercased,
   * for case-insensitive Guid comparison (the spec is case-agnostic for Guid strings; Milo's stock
   * struct codec uppercases them while the mapping's own header Guids are lowercase).
   */
  static JsonElement normalizeUuidStrings(JsonElement element) {
    if (element.isJsonObject()) {
      var copy = new JsonObject();
      for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
        copy.add(entry.getKey(), normalizeUuidStrings(entry.getValue()));
      }
      return copy;
    }
    if (element.isJsonArray()) {
      var copy = new JsonArray();
      for (JsonElement child : element.getAsJsonArray()) {
        copy.add(normalizeUuidStrings(child));
      }
      return copy;
    }
    if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
      String value = element.getAsString();
      try {
        UUID.fromString(value);
        return new JsonPrimitive(value.toLowerCase());
      } catch (IllegalArgumentException e) {
        return element;
      }
    }
    return element;
  }
}
