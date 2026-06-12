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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedMetaData;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * Decodes JSON NetworkMessages (Part 14 §7.2.5), tolerantly, across all eight structural layouts of
 * §7.2.5.3: the layout is detected from the JSON shape, since collapsed layouts carry no in-band
 * indication.
 *
 * <ul>
 *   <li>A top-level object with a {@code Messages} member (or {@code MessageType} {@code
 *       "ua-data"}) is a NetworkMessage with header.
 *   <li>A top-level array is the contents of the {@code Messages} member (no NetworkMessage
 *       header).
 *   <li>An object carrying DataSetMessage header members ({@code Payload}, {@code DataSetWriterId},
 *       {@code SequenceNumber}, ...) is a single DataSetMessage.
 *   <li>Any other object is the bare name/value payload of a single DataSet; an empty object is a
 *       keep-alive (Table 185).
 *   <li>{@code MessageType} {@code "ua-metadata"} messages surface as {@link DecodedMetaData};
 *       other discovery message types ({@code ua-status}, {@code ua-connection}, ...) are tolerated
 *       and skipped.
 * </ul>
 *
 * <p>Unknown members are skipped in headers; payload values are decoded leniently by {@link
 * JsonFieldDecoder}. DataSetMessage-level {@code PublisherId} values (legal when no NetworkMessage
 * header is present) are hoisted into {@link DecodedNetworkMessage#publisherId()}. Headers the
 * decoded records have no slot for ({@code MessageId}, {@code WriterGroupName}, {@code
 * DataSetWriterName}, {@code DataSetClassId}) are dropped.
 */
final class JsonNetworkMessageDecoder {

  private static final Map<String, DataSetMessageKind> MESSAGE_KINDS =
      Map.of(
          "ua-keyframe", DataSetMessageKind.KEY_FRAME,
          "ua-deltaframe", DataSetMessageKind.DELTA_FRAME,
          "ua-event", DataSetMessageKind.EVENT,
          "ua-keepalive", DataSetMessageKind.KEEP_ALIVE);

  private JsonNetworkMessageDecoder() {}

  /**
   * Decode one JSON NetworkMessage from {@code payload} (a complete UTF-8 JSON document).
   *
   * @throws UaException with {@code Bad_DecodingError} if the payload is not a JSON document of any
   *     recognizable NetworkMessage shape.
   */
  static DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException {
    String text = payload.toString(StandardCharsets.UTF_8);

    JsonElement root;
    try {
      root = JsonParser.parseString(text);
    } catch (JsonParseException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, "payload is not a JSON document", e);
    }

    var fieldDecoder = new JsonFieldDecoder(context.encodingContext());
    var hoist = new Hoist();

    if (root.isJsonArray()) {
      // §7.2.5.3: the NetworkMessage is the contents of the Messages member
      List<DecodedDataSetMessage> messages = new ArrayList<>();
      for (JsonElement element : root.getAsJsonArray()) {
        if (element.isJsonObject()) {
          messages.add(decodeDataSetMessage(element.getAsJsonObject(), hoist, fieldDecoder));
        }
      }
      return networkMessage(hoist.publisherId, messages, List.of());
    }

    if (!root.isJsonObject()) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "top-level JSON value is not an object or array");
    }

    JsonObject object = root.getAsJsonObject();
    String messageType = stringMember(object, "MessageType");

    if ("ua-metadata".equals(messageType)) {
      return decodeMetaDataMessage(context, object);
    }

    if ("ua-data".equals(messageType) || object.has("Messages")) {
      return decodeDataMessage(object, hoist, fieldDecoder);
    }

    if (messageType != null
        && messageType.startsWith("ua-")
        && !MESSAGE_KINDS.containsKey(messageType)
        && !hasDataSetMessageMembers(object)) {
      // optional discovery messages (ua-status, ua-connection, ua-application, ...) and Action
      // messages are tolerated and skipped
      return networkMessage(null, List.of(), List.of());
    }

    // a single DataSetMessage or a bare payload object (§7.2.5.3 collapse rules 3/4)
    DecodedDataSetMessage message = decodeDataSetMessage(object, hoist, fieldDecoder);
    return networkMessage(hoist.publisherId, List.of(message), List.of());
  }

  /** Decode a NetworkMessage with header (Table 184). Unknown members are skipped. */
  private static DecodedNetworkMessage decodeDataMessage(
      JsonObject object, Hoist hoist, JsonFieldDecoder fieldDecoder) {

    PublisherId publisherId = null;
    String publisherIdValue = stringMember(object, "PublisherId");
    if (publisherIdValue != null) {
      publisherId = PublisherId.string(publisherIdValue);
    }

    List<DecodedDataSetMessage> messages = new ArrayList<>();
    JsonElement messagesElement = object.get("Messages");
    if (messagesElement != null) {
      if (messagesElement.isJsonObject()) {
        // SingleDataSetMessage: a JSON object containing a single DataSetMessage
        messages.add(decodeDataSetMessage(messagesElement.getAsJsonObject(), hoist, fieldDecoder));
      } else if (messagesElement.isJsonArray()) {
        for (JsonElement element : messagesElement.getAsJsonArray()) {
          if (element.isJsonObject()) {
            messages.add(decodeDataSetMessage(element.getAsJsonObject(), hoist, fieldDecoder));
          }
        }
      }
    }

    if (publisherId == null) {
      publisherId = hoist.publisherId;
    }

    return networkMessage(publisherId, messages, List.of());
  }

  /** Decode a {@code ua-metadata} message (Table 188) into the {@link DecodedMetaData} path. */
  private static DecodedNetworkMessage decodeMetaDataMessage(
      DecodeContext context, JsonObject object) throws UaException {

    UShort dataSetWriterId = ushortMember(object, "DataSetWriterId");
    if (dataSetWriterId == null) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "ua-metadata message carries no DataSetWriterId");
    }

    JsonElement metaDataElement = object.get("MetaData");
    if (metaDataElement == null || !metaDataElement.isJsonObject()) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "ua-metadata message carries no MetaData");
    }

    JsonObject metaDataObject = metaDataElement.getAsJsonObject();
    normalizeMetaData(metaDataObject);

    DataSetMetaDataType metaData;
    try {
      var decoder = new OpcUaJsonDecoder(context.encodingContext(), metaDataObject.toString());
      metaData = (DataSetMetaDataType) decoder.decodeStruct(null, DataSetMetaDataType.TYPE_ID);
    } catch (Exception e) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "undecodable ua-metadata MetaData member", e);
    }

    PublisherId publisherId = null;
    String publisherIdValue = stringMember(object, "PublisherId");
    if (publisherIdValue != null) {
      publisherId = PublisherId.string(publisherIdValue);
    }

    return networkMessage(
        publisherId, List.of(), List.of(new DecodedMetaData(dataSetWriterId, metaData)));
  }

  /**
   * Materialize the v1.05 CompactEncoding omission semantics for the {@code ConfigurationVersion}
   * member before the stock struct decoder sees the document: an omitted member means the default
   * value, and a ConfigurationVersion whose VersionTimes are 0 means "not used" (Part 14), but
   * {@link OpcUaJsonDecoder} applies omitted-member defaulting only to non-struct members and fails
   * the decode on an omitted (or JSON-null) struct-typed member.
   *
   * <p>Scope is the {@code ua-metadata} {@code MetaData} member only; data-plane decoding is
   * untouched. Unknown vendor members inside {@code MetaData} still fail decode (status quo,
   * intentional). A publisher omitting {@code ConfigurationVersion} here while transmitting a
   * non-zero major version in its data messages will mismatch against the synthesized (0, 0) —
   * incoherent-publisher territory, and spec-defensible. In-place mutation is safe: the Gson tree
   * is built per decode call.
   */
  private static void normalizeMetaData(JsonObject metaData) {
    JsonElement version = metaData.get("ConfigurationVersion");
    if (version == null || version.isJsonNull()) {
      var defaultVersion = new JsonObject();
      defaultVersion.addProperty("MajorVersion", 0);
      defaultVersion.addProperty("MinorVersion", 0);
      metaData.add("ConfigurationVersion", defaultVersion);
    }
  }

  /** Decode one Messages element: a DataSetMessage with header, or a bare payload object. */
  private static DecodedDataSetMessage decodeDataSetMessage(
      JsonObject object, Hoist hoist, JsonFieldDecoder fieldDecoder) {

    if (!hasDataSetMessageHeader(object)) {
      return decodePayloadOnly(object, fieldDecoder);
    }

    UShort dataSetWriterId = ushortMember(object, "DataSetWriterId");
    UInteger sequenceNumber = uintMember(object, "SequenceNumber");

    ConfigurationVersionDataType configurationVersion = null;
    JsonElement versionElement = object.get("MetaDataVersion");
    if (versionElement != null && versionElement.isJsonObject()) {
      JsonObject version = versionElement.getAsJsonObject();
      UInteger major = uintMember(version, "MajorVersion");
      UInteger minor = uintMember(version, "MinorVersion");
      configurationVersion =
          new ConfigurationVersionDataType(
              major != null ? major : Unsigned.uint(0), minor != null ? minor : Unsigned.uint(0));
    } else {
      UInteger minor = uintMember(object, "MinorVersion");
      if (minor != null) {
        // MinorVersion-only header: major 0 = "not transmitted" (VersionTime 0 is "not used")
        configurationVersion = new ConfigurationVersionDataType(Unsigned.uint(0), minor);
      }
    }

    DateTime timestamp = JsonFieldDecoder.decodeTimestamp(object.get("Timestamp"));

    // Status is omitted when Good on the wire: missing decodes as Good
    StatusCode status = StatusCode.GOOD;
    JsonElement statusElement = object.get("Status");
    if (statusElement != null && !statusElement.isJsonNull()) {
      status = JsonFieldDecoder.decodeStatusCode(statusElement);
    }

    // a DataSetMessage-level PublisherId is legal when no NetworkMessage header is present
    // (Table 185); hoist it to the NetworkMessage level, where the matching chain reads it
    String publisherIdValue = stringMember(object, "PublisherId");
    if (publisherIdValue != null) {
      hoist.offer(PublisherId.string(publisherIdValue));
    }

    boolean valid = true;
    DataSetMessageKind kind;
    String messageType = stringMember(object, "MessageType");
    if (messageType == null) {
      kind = object.has("Payload") ? DataSetMessageKind.KEY_FRAME : DataSetMessageKind.KEEP_ALIVE;
    } else {
      DataSetMessageKind known = MESSAGE_KINDS.get(messageType);
      if (known != null) {
        kind = known;
      } else {
        // unknown MessageType: surfaced like the UADP decoder's undecodable placeholder
        kind = DataSetMessageKind.KEY_FRAME;
        valid = false;
      }
    }

    List<DecodedField> fields = List.of();
    if (valid && kind != DataSetMessageKind.KEEP_ALIVE) {
      JsonElement payloadElement = object.get("Payload");
      if (payloadElement != null && payloadElement.isJsonObject()) {
        fields = decodeFields(payloadElement.getAsJsonObject(), fieldDecoder);
      }
    }

    return new DecodedDataSetMessage(
        dataSetWriterId,
        kind,
        valid,
        sequenceNumber,
        timestamp,
        status,
        configurationVersion,
        fields);
  }

  /**
   * Decode a bare payload object (no DataSetMessage header): an empty object is a keep-alive (Table
   * 185), anything else a key frame keyed by field name.
   */
  private static DecodedDataSetMessage decodePayloadOnly(
      JsonObject object, JsonFieldDecoder fieldDecoder) {

    if (object.isEmpty()) {
      return new DecodedDataSetMessage(
          null, DataSetMessageKind.KEEP_ALIVE, true, null, null, null, null, List.of());
    }

    return new DecodedDataSetMessage(
        null,
        DataSetMessageKind.KEY_FRAME,
        true,
        null,
        null,
        null,
        null,
        decodeFields(object, fieldDecoder));
  }

  /** Decode a Payload object into name-keyed fields, in document order. */
  private static List<DecodedField> decodeFields(
      JsonObject payload, JsonFieldDecoder fieldDecoder) {

    var fields = new ArrayList<DecodedField>(payload.size());
    int index = 0;
    for (Map.Entry<String, JsonElement> entry : payload.entrySet()) {
      DataValue value = fieldDecoder.decodeFieldValue(entry.getValue());
      fields.add(new DecodedField(index, entry.getKey(), value));
      index++;
    }
    return fields;
  }

  /**
   * {@code true} when {@code object} carries DataSetMessage header members rather than being a bare
   * payload object. Only members that are unambiguous header indicators are considered: {@code
   * Payload}, the identifying/sequencing members of Table 185, and a {@code MessageType} member
   * with one of the four DataSetMessage type values. ({@code Timestamp}, {@code Status}, {@code
   * PublisherId}, and {@code WriterGroupName} alone do not qualify — they collide with plausible
   * dataset field names.)
   */
  private static boolean hasDataSetMessageHeader(JsonObject object) {
    if (hasDataSetMessageMembers(object)) {
      return true;
    }

    String messageType = stringMember(object, "MessageType");
    return messageType != null && MESSAGE_KINDS.containsKey(messageType);
  }

  /**
   * {@code true} when {@code object} carries DataSetMessage header members other than {@code
   * MessageType}.
   */
  private static boolean hasDataSetMessageMembers(JsonObject object) {
    return object.has("Payload")
        || object.has("DataSetWriterId")
        || object.has("DataSetWriterName")
        || object.has("SequenceNumber")
        || object.has("MetaDataVersion")
        || object.has("MinorVersion");
  }

  private static DecodedNetworkMessage networkMessage(
      @Nullable PublisherId publisherId,
      List<DecodedDataSetMessage> messages,
      List<DecodedMetaData> metaData) {

    return DecodedNetworkMessage.of(publisherId, null, null, null, null, null, messages, metaData);
  }

  private static @Nullable String stringMember(JsonObject object, String name) {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      return element.getAsString();
    }
    return null;
  }

  private static @Nullable UShort ushortMember(JsonObject object, String name) {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      try {
        int value = element.getAsInt();
        if (value >= 0 && value <= 65535) {
          return Unsigned.ushort(value);
        }
      } catch (RuntimeException e) {
        // tolerated: not a number
      }
    }
    return null;
  }

  private static @Nullable UInteger uintMember(JsonObject object, String name) {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      try {
        long value = element.getAsLong();
        if (value >= 0L && value <= 0xFFFF_FFFFL) {
          return Unsigned.uint(value);
        }
      } catch (RuntimeException e) {
        // tolerated: not a number
      }
    }
    return null;
  }

  /** Accumulates a DataSetMessage-level PublisherId for hoisting to the NetworkMessage level. */
  private static final class Hoist {

    private @Nullable PublisherId publisherId;

    void offer(PublisherId publisherId) {
      if (this.publisherId == null) {
        this.publisherId = publisherId;
      }
    }
  }
}
