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

import com.google.gson.stream.JsonWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.jspecify.annotations.Nullable;

/**
 * Encodes {@code ua-data} (Part 14 §7.2.5.3/.4) and {@code ua-metadata} (§7.2.5.5.2) JSON
 * NetworkMessages.
 *
 * <p>The structural collapse rules of §7.2.5.3 are applied exactly: depending on the effective
 * {@link JsonNetworkMessageContentMask} the top-level JSON value is the full NetworkMessage object,
 * a single DataSetMessage object, an array of DataSetMessages, an array of bare payload objects, or
 * a single bare payload object.
 *
 * <p>Key frame, delta frame, event, and keep-alive drafts are supported; a delta frame emits
 * MessageType {@code "ua-deltaframe"} with a Payload containing only the changed fields,
 * name-resolved from the draft metadata by index (§7.2.5.4.1 Table 185). An event emits MessageType
 * {@code "ua-event"} with Variant-represented fields (§7.2.5.4.1: the DataValue field
 * representation coerces to Variant). Delta and event frames each require the DataSetMessageHeader
 * and the MessageType member (Annex A.3.3.4: without them the wire shape is indistinguishable from
 * a key frame).
 */
final class JsonNetworkMessageEncoder {

  private static final UUID NULL_GUID = new UUID(0L, 0L);

  private JsonNetworkMessageEncoder() {}

  /**
   * Encode the drafts in {@code context} into one or more {@code ua-data} NetworkMessages.
   *
   * <p>One NetworkMessage is produced per draft when {@code SingleDataSetMessage} is set, and
   * drafts with differing DataSetClassIds are split into separate NetworkMessages when {@code
   * DataSetClassId} is set (§6.3.2.1.1 Table 107); otherwise all drafts share one NetworkMessage.
   */
  static List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
    List<DataSetMessageDraft> drafts = context.messages();

    if (drafts.isEmpty()) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "no DataSetMessage drafts");
    }

    if (!(context.writerGroup().getMessageSettings() instanceof JsonWriterGroupSettings settings)) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "writer group '%s' message settings are not JsonWriterGroupSettings"
              .formatted(context.writerGroup().getName()));
    }

    JsonNetworkMessageContentMask networkMask =
        JsonContentMasks.effectiveNetworkMessageMask(settings.getNetworkMessageContentMask());

    for (DataSetMessageDraft draft : drafts) {
      validateDraft(draft);
      if (draft.kind() == DataSetMessageKind.DELTA_FRAME) {
        requireDeltaExpressible(networkMask, draft);
      } else if (draft.kind() == DataSetMessageKind.EVENT) {
        requireEventExpressible(networkMask, draft);
      }
    }

    // group drafts by DataSetClassId when the mask requires homogeneous NetworkMessages
    Map<UUID, List<DataSetMessageDraft>> batches = new LinkedHashMap<>();
    if (networkMask.getDataSetClassId()) {
      for (DataSetMessageDraft draft : drafts) {
        batches.computeIfAbsent(classIdOf(draft), k -> new ArrayList<>()).add(draft);
      }
    } else {
      batches.put(NULL_GUID, new ArrayList<>(drafts));
    }

    var fieldEncoder = new JsonFieldEncoder(context.encodingContext());

    var encoded = new ArrayList<EncodedNetworkMessage>();
    try {
      for (Map.Entry<UUID, List<DataSetMessageDraft>> batch : batches.entrySet()) {
        UUID classId = networkMask.getDataSetClassId() ? batch.getKey() : NULL_GUID;

        if (networkMask.getSingleDataSetMessage()) {
          for (DataSetMessageDraft draft : batch.getValue()) {
            encoded.add(
                encodeNetworkMessage(context, networkMask, classId, List.of(draft), fieldEncoder));
          }
        } else {
          encoded.add(
              encodeNetworkMessage(context, networkMask, classId, batch.getValue(), fieldEncoder));
        }
      }
    } catch (Exception e) {
      encoded.forEach(message -> message.data().release());
      throw toUaException(e);
    }

    return encoded;
  }

  /** Encode one {@code ua-metadata} NetworkMessage (§7.2.5.5.2 Table 188, CompactEncoding). */
  static EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData) throws UaException {

    ByteBuf buffer = BufferUtil.pooledBuffer();
    try {
      try (var writer =
          new JsonWriter(
              new OutputStreamWriter(new ByteBufOutputStream(buffer), StandardCharsets.UTF_8))) {
        writer.setHtmlSafe(false);

        String metaDataJson;
        try (var structEncoder = new OpcUaJsonEncoder(context.encodingContext())) {
          structEncoder.setEncoding(OpcUaJsonEncoder.Encoding.COMPACT);
          structEncoder.encodeStruct(null, metaData, DataSetMetaDataType.TYPE_ID);
          metaDataJson = structEncoder.getOutputString();
        }

        writer.beginObject();
        writer.name("MessageId").value(UUID.randomUUID().toString());
        writer.name("MessageType").value("ua-metadata");
        writer.name("PublisherId").value(context.publisherId().toCanonicalString());
        writer.name("DataSetWriterId").value(context.writer().getDataSetWriterId().intValue());
        writer.name("WriterGroupName").value(context.writerGroup().getName());
        writer.name("DataSetWriterName").value(context.writer().getName());
        writer.name("Timestamp").jsonValue(isoTimestamp(context, DateTime.now()));
        writer.name("MetaData").jsonValue(metaDataJson);
        writer.endObject();
      }

      return EncodedNetworkMessage.of(
          buffer,
          List.of(
              new EncodedNetworkMessage.Writer(
                  context.writer().getName(), context.writer().getDataSetWriterId())));
    } catch (Exception e) {
      buffer.release();
      throw toUaException(e);
    }
  }

  private static String isoTimestamp(MetaDataEncodeContext context, DateTime timestamp) {
    @SuppressWarnings("resource")
    var encoder = new OpcUaJsonEncoder(context.encodingContext());
    encoder.encodeDateTime(null, timestamp);
    return encoder.getOutputString();
  }

  private static void validateDraft(DataSetMessageDraft draft) throws UaException {
    if (!(draft.writer().getSettings() instanceof JsonDataSetWriterSettings)) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "writer '%s' message settings are not JsonDataSetWriterSettings"
              .formatted(draft.writer().getName()));
    }

    if (draft.keepAlive()) {
      return;
    }

    DataSetMetaDataType metaData = draft.metaData();
    if (metaData == null) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          ("writer '%s' draft carries no resolved DataSetMetaData; the JSON mapping requires field"
                  + " names")
              .formatted(draft.writer().getName()));
    }

    FieldMetaData[] fields = metaData.getFields();
    int metaFieldCount = fields != null ? fields.length : 0;

    if (draft.kind() == DataSetMessageKind.DELTA_FRAME) {
      // a delta frame carries only the changed fields (Table 185), so there is no full-count
      // requirement; every index must resolve to a metadata field name
      for (DataSetMessageDraft.DeltaField field : draft.deltaFields()) {
        if (field.index() < 0 || field.index() >= metaFieldCount) {
          throw new UaException(
              StatusCodes.Bad_ConfigurationError,
              "writer '%s' delta draft field index %d is outside its DataSetMetaData (%d fields)"
                  .formatted(draft.writer().getName(), field.index(), metaFieldCount));
        }
      }
    } else if (draft.fields().size() != metaFieldCount) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "writer '%s' draft has %d fields but its DataSetMetaData describes %d"
              .formatted(draft.writer().getName(), draft.fields().size(), metaFieldCount));
    }
  }

  /**
   * Reject a delta frame draft whose effective masks cannot represent it: without the
   * DataSetMessageHeader, or without the MessageType member, a delta payload is indistinguishable
   * from a key frame on the wire (the decoder defaults to key frame). Part 14 Annex A.3.3.4 and
   * A.3.4.4: "If the KeyFrameCount is not 1, the MessageType bit shall be true."
   */
  private static void requireDeltaExpressible(
      JsonNetworkMessageContentMask networkMask, DataSetMessageDraft draft) throws UaException {

    if (!networkMask.getDataSetMessageHeader()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          ("writer '%s' delta frame requires the DataSetMessageHeader in the"
                  + " JsonNetworkMessageContentMask (Part 14 A.3.3.4)")
              .formatted(draft.writer().getName()));
    }

    var settings = (JsonDataSetWriterSettings) draft.writer().getSettings();
    JsonDataSetMessageContentMask messageMask =
        JsonContentMasks.effectiveDataSetMessageMask(settings.getDataSetMessageContentMask());
    if (!messageMask.getMessageType()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          ("writer '%s' delta frame requires the MessageType member in the effective"
                  + " JsonDataSetMessageContentMask (Part 14 A.3.3.4)")
              .formatted(draft.writer().getName()));
    }
  }

  /**
   * Reject an event draft whose effective masks cannot represent it: without the
   * DataSetMessageHeader, or without the MessageType member, a {@code ua-event} payload is
   * indistinguishable from a key frame on the wire (the decoder defaults to key frame). Part 14
   * Annex A.3.3.4: "If the KeyFrameCount is not 1, the MessageType bit shall be true."
   */
  private static void requireEventExpressible(
      JsonNetworkMessageContentMask networkMask, DataSetMessageDraft draft) throws UaException {

    if (!networkMask.getDataSetMessageHeader()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          ("writer '%s' event requires the DataSetMessageHeader in the"
                  + " JsonNetworkMessageContentMask (Part 14 A.3.3.4)")
              .formatted(draft.writer().getName()));
    }

    var settings = (JsonDataSetWriterSettings) draft.writer().getSettings();
    JsonDataSetMessageContentMask messageMask =
        JsonContentMasks.effectiveDataSetMessageMask(settings.getDataSetMessageContentMask());
    if (!messageMask.getMessageType()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          ("writer '%s' event requires the MessageType member in the effective"
                  + " JsonDataSetMessageContentMask (Part 14 A.3.3.4)")
              .formatted(draft.writer().getName()));
    }
  }

  private static UUID classIdOf(DataSetMessageDraft draft) {
    DataSetMetaDataType metaData = draft.metaData();
    if (metaData == null || metaData.getDataSetClassId() == null) {
      return NULL_GUID;
    }
    return metaData.getDataSetClassId();
  }

  private static EncodedNetworkMessage encodeNetworkMessage(
      EncodeContext context,
      JsonNetworkMessageContentMask networkMask,
      UUID classId,
      List<DataSetMessageDraft> drafts,
      JsonFieldEncoder fieldEncoder)
      throws Exception {

    ByteBuf buffer = BufferUtil.pooledBuffer();
    try {
      try (var writer =
          new JsonWriter(
              new OutputStreamWriter(new ByteBufOutputStream(buffer), StandardCharsets.UTF_8))) {
        writer.setHtmlSafe(false);

        writeNetworkMessage(writer, context, networkMask, classId, drafts, fieldEncoder);
      }

      var writers = new ArrayList<EncodedNetworkMessage.Writer>(drafts.size());
      for (DataSetMessageDraft draft : drafts) {
        writers.add(
            new EncodedNetworkMessage.Writer(
                draft.writer().getName(), draft.writer().getDataSetWriterId()));
      }

      return EncodedNetworkMessage.of(buffer, writers);
    } catch (Exception e) {
      buffer.release();
      throw e;
    }
  }

  private static void writeNetworkMessage(
      JsonWriter writer,
      EncodeContext context,
      JsonNetworkMessageContentMask networkMask,
      UUID classId,
      List<DataSetMessageDraft> drafts,
      JsonFieldEncoder fieldEncoder)
      throws Exception {

    if (networkMask.getNetworkMessageHeader()) {
      writer.beginObject();
      writer.name("MessageId").value(UUID.randomUUID().toString());
      writer.name("MessageType").value("ua-data");
      if (networkMask.getPublisherId()) {
        writer.name("PublisherId").value(context.publisherId().toCanonicalString());
      }
      if (networkMask.getWriterGroupName()) {
        writer.name("WriterGroupName").value(context.writerGroup().getName());
      }
      if (networkMask.getDataSetClassId() && !NULL_GUID.equals(classId)) {
        writer.name("DataSetClassId").value(classId.toString());
      }
      writer.name("Messages");
      writeMessages(writer, context, networkMask, drafts, fieldEncoder);
      writer.endObject();
    } else {
      // §7.2.5.3: without the NetworkMessageHeader the NetworkMessage is the contents of the
      // Messages member
      writeMessages(writer, context, networkMask, drafts, fieldEncoder);
    }
  }

  private static void writeMessages(
      JsonWriter writer,
      EncodeContext context,
      JsonNetworkMessageContentMask networkMask,
      List<DataSetMessageDraft> drafts,
      JsonFieldEncoder fieldEncoder)
      throws Exception {

    if (networkMask.getSingleDataSetMessage()) {
      writeDataSetMessage(writer, context, networkMask, drafts.get(0), fieldEncoder);
    } else {
      writer.beginArray();
      for (DataSetMessageDraft draft : drafts) {
        writeDataSetMessage(writer, context, networkMask, draft, fieldEncoder);
      }
      writer.endArray();
    }
  }

  private static void writeDataSetMessage(
      JsonWriter writer,
      EncodeContext context,
      JsonNetworkMessageContentMask networkMask,
      DataSetMessageDraft draft,
      JsonFieldEncoder fieldEncoder)
      throws Exception {

    DataSetWriterConfig writerConfig = draft.writer();
    var settings = (JsonDataSetWriterSettings) writerConfig.getSettings();

    JsonDataSetMessageContentMask messageMask =
        JsonContentMasks.effectiveDataSetMessageMask(settings.getDataSetMessageContentMask());
    OpcUaJsonEncoder.Encoding encoding = JsonContentMasks.resolveFieldEncoding(messageMask);
    DataSetFieldContentMask fieldMask = writerConfig.getFieldContentMask();

    if (draft.kind() == DataSetMessageKind.EVENT
        && JsonFieldEncoder.isDataValueRepresentation(fieldMask)) {
      // §7.2.5.4.1: event fields use the Variant (or RawData) representation only; the DataValue
      // representation coerces to Variant. Preserve only the RawData bit, before both the header
      // status folding and the payload are written from this mask.
      fieldMask =
          fieldMask.getRawData()
              ? DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData)
              : DataSetFieldContentMask.of();
    }

    boolean networkMessageHeader = networkMask.getNetworkMessageHeader();

    if (!networkMask.getDataSetMessageHeader()) {
      // §7.2.5.3: without the DataSetMessageHeader the DataSetMessage is its Payload contents;
      // a keep-alive degenerates to an empty JSON object (Table 185)
      if (draft.keepAlive()) {
        writer.beginObject();
        writer.endObject();
      } else {
        writePayload(writer, draft, fieldMask, encoding, fieldEncoder);
      }
      return;
    }

    writer.beginObject();

    if (messageMask.getDataSetWriterId()) {
      writer.name("DataSetWriterId").value(writerConfig.getDataSetWriterId().intValue());
    }
    if (messageMask.getDataSetWriterName()) {
      writer.name("DataSetWriterName").value(writerConfig.getName());
    }
    if (messageMask.getPublisherId() && !networkMessageHeader) {
      // Table 185: the value shall be omitted if the NetworkMessage header is present
      writer.name("PublisherId").value(context.publisherId().toCanonicalString());
    }
    if (messageMask.getWriterGroupName()
        && !(networkMessageHeader && networkMask.getWriterGroupName())) {
      // Table 185: omitted if the WriterGroupName is contained in the NetworkMessage header
      writer.name("WriterGroupName").value(context.writerGroup().getName());
    }
    if (messageMask.getSequenceNumber()) {
      // emitted as UInt32 (Table 185); the engine's JSON sequence counter spans the full UInt32
      // range and rolls over at 2^32 (§7.2.3 rollover at the DataType maximum)
      writer.name("SequenceNumber").value(draft.sequenceNumber().longValue());
    }
    if (messageMask.getMetaDataVersion()) {
      ConfigurationVersionDataType version = draft.configurationVersion();
      writer.name("MetaDataVersion");
      writer.beginObject();
      writer.name("MajorVersion").value(version.getMajorVersion().longValue());
      writer.name("MinorVersion").value(version.getMinorVersion().longValue());
      writer.endObject();
    } else if (messageMask.getMinorVersion()) {
      // Table 185: only when the full MetaDataVersion is not in the header
      writer.name("MinorVersion").value(draft.configurationVersion().getMinorVersion().longValue());
    }
    if (messageMask.getTimestamp()) {
      DateTime timestamp = draft.timestamp() != null ? draft.timestamp() : DateTime.now();
      writer.name("Timestamp");
      fieldEncoder.writeDateTime(writer, timestamp);
    }
    if (messageMask.getStatus()) {
      StatusCode status = resolveHeaderStatus(draft, fieldMask);
      if (status != null && !status.isGood()) {
        // omitted when Good, per the §7.2.5.4.2 recommendation
        writer.name("Status");
        fieldEncoder.writeStatusCode(writer, status, OpcUaJsonEncoder.Encoding.COMPACT);
      }
    }
    if (messageMask.getMessageType()) {
      // Table 185 MessageType values
      String messageType =
          switch (draft.kind()) {
            case KEY_FRAME -> "ua-keyframe";
            case DELTA_FRAME -> "ua-deltaframe";
            case EVENT -> "ua-event";
            case KEEP_ALIVE -> "ua-keepalive";
          };
      writer.name("MessageType").value(messageType);
    }

    if (!draft.keepAlive()) {
      writer.name("Payload");
      writePayload(writer, draft, fieldMask, encoding, fieldEncoder);
    }

    writer.endObject();
  }

  /**
   * The DataSetMessage header status: the draft status when present and not Good, otherwise — for
   * the Variant field representation — the first Uncertain field status, which Table 35 folds into
   * the header because the field level cannot carry it.
   */
  private static @Nullable StatusCode resolveHeaderStatus(
      DataSetMessageDraft draft, DataSetFieldContentMask fieldMask) {

    StatusCode status = draft.status();
    if (status != null && !status.isGood()) {
      return status;
    }

    if (!JsonFieldEncoder.isDataValueRepresentation(fieldMask)) {
      for (DataValue value : transmittedValues(draft)) {
        if (value.statusCode().isUncertain()) {
          return value.statusCode();
        }
      }
    }

    return status;
  }

  /** The field values this draft puts on the wire: key frame fields or delta changed fields. */
  private static List<DataValue> transmittedValues(DataSetMessageDraft draft) {
    if (draft.kind() == DataSetMessageKind.DELTA_FRAME) {
      return draft.deltaFields().stream().map(DataSetMessageDraft.DeltaField::value).toList();
    } else {
      return draft.fields();
    }
  }

  private static void writePayload(
      JsonWriter writer,
      DataSetMessageDraft draft,
      DataSetFieldContentMask fieldMask,
      OpcUaJsonEncoder.Encoding encoding,
      JsonFieldEncoder fieldEncoder)
      throws Exception {

    DataSetMetaDataType metaData = draft.metaData();
    FieldMetaData[] metaFields = metaData != null ? metaData.getFields() : null;

    writer.beginObject();
    if (draft.kind() == DataSetMessageKind.DELTA_FRAME) {
      // Table 185: a delta frame Payload contains "only name and value for the changed fields";
      // names are resolved from the draft metadata by each field's explicit index
      for (DataSetMessageDraft.DeltaField field : draft.deltaFields()) {
        int index = field.index();
        FieldMetaData fieldMetaData =
            metaFields != null && index >= 0 && index < metaFields.length
                ? metaFields[index]
                : null;

        String name =
            fieldMetaData != null && fieldMetaData.getName() != null
                ? fieldMetaData.getName()
                : "Field_" + index;

        fieldEncoder.writeField(writer, name, field.value(), fieldMetaData, fieldMask, encoding);
      }
    } else {
      List<DataValue> values = draft.fields();
      for (int i = 0; i < values.size(); i++) {
        FieldMetaData fieldMetaData =
            metaFields != null && i < metaFields.length ? metaFields[i] : null;

        String name =
            fieldMetaData != null && fieldMetaData.getName() != null
                ? fieldMetaData.getName()
                : "Field_" + i;

        fieldEncoder.writeField(writer, name, values.get(i), fieldMetaData, fieldMask, encoding);
      }
    }
    writer.endObject();
  }

  private static UaException toUaException(Exception e) {
    if (e instanceof UaException ua) {
      return ua;
    } else {
      return new UaException(StatusCodes.Bad_EncodingError, e);
    }
  }
}
