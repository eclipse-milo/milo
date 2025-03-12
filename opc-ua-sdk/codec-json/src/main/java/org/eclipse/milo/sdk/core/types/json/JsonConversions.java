/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class JsonConversions {

  // region OPC UA to JSON Conversions

  /**
   * Convert an OPC UA value of some {@link OpcUaDataType} to a {@link JsonElement}.
   *
   * <p>Converting from values of type {@link OpcUaDataType#DiagnosticInfo} is not supported.
   *
   * @param value the OPC UA value to convert.
   * @param dataType the OPC UA {@link OpcUaDataType} of the value.
   * @return the converted {@link JsonElement}.
   * @throws IllegalArgumentException if the {@link OpcUaDataType} is not supported.
   */
  public static JsonElement from(Object value, OpcUaDataType dataType) {
    return switch (dataType) {
      case Boolean -> fromBoolean((Boolean) value);
      case SByte -> fromSByte((Byte) value);
      case Byte -> fromByte((UByte) value);
      case Int16 -> fromInt16((Short) value);
      case UInt16 -> fromUInt16((UShort) value);
      case Int32 -> fromInt32((Integer) value);
      case UInt32 -> fromUInt32((UInteger) value);
      case Int64 -> fromInt64((Long) value);
      case UInt64 -> fromUInt64((ULong) value);
      case Float -> fromFloat((Float) value);
      case Double -> fromDouble((Double) value);
      case String -> fromString((String) value);
      case DateTime -> fromDateTime((DateTime) value);
      case Guid -> fromGuid((UUID) value);
      case ByteString -> fromByteString((ByteString) value);
      case XmlElement -> fromXmlElement((XmlElement) value);
      case NodeId -> fromNodeId((NodeId) value);
      case ExpandedNodeId -> fromExpandedNodeId((ExpandedNodeId) value);
      case StatusCode -> fromStatusCode((StatusCode) value);
      case QualifiedName -> fromQualifiedName((QualifiedName) value);
      case LocalizedText -> fromLocalizedText((LocalizedText) value);
      case ExtensionObject -> fromExtensionObject((ExtensionObject) value);
      case DataValue -> fromDataValue((DataValue) value);
      case Variant -> fromVariant((Variant) value);
      default -> throw new IllegalArgumentException("dataType: " + dataType);
    };
  }

  public static JsonElement fromBoolean(Boolean value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromSByte(Byte value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromByte(UByte value) {
    return new JsonPrimitive(value.shortValue());
  }

  public static JsonElement fromInt16(Short value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromUInt16(UShort value) {
    return new JsonPrimitive(value.intValue());
  }

  public static JsonElement fromInt32(Integer value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromUInt32(UInteger value) {
    return new JsonPrimitive(value.longValue());
  }

  public static JsonElement fromInt64(Long value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromUInt64(ULong value) {
    return new JsonPrimitive(value.toBigInteger());
  }

  public static JsonElement fromFloat(Float value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromDouble(Double value) {
    return new JsonPrimitive(value);
  }

  public static JsonElement fromString(@Nullable String value) {
    if (value == null) {
      return JsonNull.INSTANCE;
    } else {
      return new JsonPrimitive(value);
    }
  }

  public static JsonElement fromDateTime(DateTime value) {
    return new JsonPrimitive(value.toIso8601String());
  }

  public static JsonElement fromGuid(UUID value) {
    return new JsonPrimitive(value.toString());
  }

  public static JsonElement fromByteString(ByteString value) {
    byte[] bs = value.bytesOrEmpty();
    String hex = HexFormat.of().formatHex(bs);
    return new JsonPrimitive(hex);
  }

  public static JsonElement fromXmlElement(XmlElement value) {
    if (value.isNull()) {
      return JsonNull.INSTANCE;
    } else {
      return new JsonPrimitive(value.getFragment());
    }
  }

  public static JsonElement fromNodeId(NodeId value) {
    return new JsonPrimitive(value.toParseableString());
  }

  public static JsonElement fromExpandedNodeId(ExpandedNodeId value) {
    return new JsonPrimitive(value.toParseableString());
  }

  public static JsonElement fromStatusCode(StatusCode value) {
    return new JsonPrimitive(value.getValue());
  }

  public static JsonElement fromQualifiedName(QualifiedName value) {
    String name = value.name() == null ? "" : value.name();
    int ns = value.namespaceIndex().intValue();

    if (ns != 0) {
      return new JsonPrimitive(value.namespaceIndex() + ":" + name);
    } else {
      return new JsonPrimitive(name);
    }
  }

  public static JsonElement fromLocalizedText(LocalizedText value) {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("Locale", value.getLocale());
    jsonObject.addProperty("Text", value.getText());
    return jsonObject;
  }

  public static JsonElement fromExtensionObject(ExtensionObject value) {
    var jsonObject = new JsonObject();

    jsonObject.add("TypeId", fromNodeId(value.getEncodingId()));

    if (value.getBody() instanceof ByteString) {
      jsonObject.addProperty("Encoding", 1);
      jsonObject.add("Body", fromByteString((ByteString) value.getBody()));
    } else if (value.getBody() instanceof XmlElement) {
      jsonObject.addProperty("Encoding", 2);
      jsonObject.add("Body", fromXmlElement((XmlElement) value.getBody()));
    } else {
      // Omit "Encoding" field when Body is JSON
      jsonObject.addProperty("Body", value.getBody().toString());
    }

    return jsonObject;
  }

  public static JsonElement fromDataValue(DataValue value) {
    var jsonObject = new JsonObject();

    if (value.value().isNotNull()) {
      jsonObject.add("Value", fromVariant(value.value()));
    }
    if (value.statusCode() != null && value.statusCode().getValue() != 0L) {
      jsonObject.add("StatusCode", fromStatusCode(value.statusCode()));
    }
    if (value.sourceTime() != null) {
      jsonObject.add("SourceTime", fromDateTime(value.sourceTime()));
    }
    if (value.sourcePicoseconds() != null) {
      jsonObject.addProperty("SourcePicoseconds", value.sourcePicoseconds().intValue());
    }
    if (value.serverTime() != null) {
      jsonObject.add("ServerTime", fromDateTime(value.serverTime()));
    }
    if (value.serverPicoseconds() != null) {
      jsonObject.addProperty("ServerPicoseconds", value.serverPicoseconds().intValue());
    }

    return jsonObject;
  }

  public static JsonElement fromVariant(Variant value) {
    Object valueObject = value.value();
    if (valueObject == null) {
      return JsonNull.INSTANCE;
    }

    var jsonObject = new JsonObject();

    OpcUaDataType dataType = value.getDataType().orElseThrow();
    jsonObject.addProperty("Type", dataType.getTypeId());

    if (valueObject.getClass().isArray()) {
      int[] dimensions = ArrayUtil.getDimensions(valueObject);
      assert dimensions.length == 1;
      var jsonArray = new JsonArray();
      for (int i = 0; i < Array.getLength(valueObject); i++) {
        Object arrayValue = Array.get(valueObject, i);
        jsonArray.add(from(arrayValue, dataType));
      }
      jsonObject.add("Body", jsonArray);
    } else if (valueObject instanceof Matrix matrix) {
      Object flatArray = matrix.getElements();
      var jsonArray = new JsonArray();
      for (int i = 0; i < Array.getLength(flatArray); i++) {
        Object element = Array.get(flatArray, i);
        jsonArray.add(from(element, dataType));
      }
      jsonObject.add("Body", jsonArray);

      int[] dimensions = matrix.getDimensions();
      var dimensionsJsonArray = new JsonArray();
      Arrays.stream(dimensions).forEach(dimensionsJsonArray::add);
      jsonObject.add("Dimensions", dimensionsJsonArray);
    } else {
      jsonObject.add("Body", from(valueObject, dataType));
    }

    return jsonObject;
  }

  // endregion

  // region JSON to OPC UA Conversions

  /**
   * Convert a {@link JsonElement} to a value of some OPC UA {@link OpcUaDataType}.
   *
   * <p>Converting to values of type {@link OpcUaDataType#DiagnosticInfo} is not supported.
   *
   * @param element the {@link JsonElement} to convert.
   * @param dataType the OPC UA {@link OpcUaDataType} to convert to.
   * @return the converted value.
   * @throws IllegalArgumentException if the {@link OpcUaDataType} is not supported.
   */
  public static Object to(JsonElement element, OpcUaDataType dataType) {
    return switch (dataType) {
      case Boolean -> toBoolean(element);
      case SByte -> toSByte(element);
      case Byte -> toByte(element);
      case Int16 -> toInt16(element);
      case UInt16 -> toUInt16(element);
      case Int32 -> toInt32(element);
      case UInt32 -> toUInt32(element);
      case Int64 -> toInt64(element);
      case UInt64 -> toUInt64(element);
      case Float -> toFloat(element);
      case Double -> toDouble(element);
      case String -> toString(element);
      case DateTime -> toDateTime(element);
      case Guid -> toGuid(element);
      case ByteString -> toByteString(element);
      case XmlElement -> toXmlElement(element);
      case NodeId -> toNodeId(element);
      case ExpandedNodeId -> toExpandedNodeId(element);
      case StatusCode -> toStatusCode(element);
      case QualifiedName -> toQualifiedName(element);
      case LocalizedText -> toLocalizedText(element);
      case ExtensionObject -> toExtensionObject(element);
      case DataValue -> toDataValue(element);
      case Variant -> toVariant(element);
      default ->
          // note: shouldn't be possible to get here.
          // DiagnosticInfo is not allowed in Variant.
          // All other types should be handled above.
          throw new IllegalArgumentException("dataType: " + dataType);
    };
  }

  public static Boolean toBoolean(JsonElement element) {
    return element.getAsBoolean();
  }

  public static Byte toSByte(JsonElement element) {
    return element.getAsByte();
  }

  public static UByte toByte(JsonElement element) {
    return UByte.valueOf(element.getAsShort());
  }

  public static Short toInt16(JsonElement element) {
    return element.getAsShort();
  }

  public static UShort toUInt16(JsonElement element) {
    return UShort.valueOf(element.getAsInt());
  }

  public static Integer toInt32(JsonElement element) {
    return element.getAsInt();
  }

  public static UInteger toUInt32(JsonElement element) {
    return UInteger.valueOf(element.getAsLong());
  }

  public static Long toInt64(JsonElement element) {
    return element.getAsLong();
  }

  public static ULong toUInt64(JsonElement element) {
    return ULong.valueOf(element.getAsBigInteger());
  }

  public static Float toFloat(JsonElement element) {
    return element.getAsFloat();
  }

  public static Double toDouble(JsonElement element) {
    return element.getAsDouble();
  }

  public static String toString(JsonElement element) {
    return element.getAsString();
  }

  public static DateTime toDateTime(JsonElement element) {
    return new DateTime(Instant.parse(element.getAsString()));
  }

  public static UUID toGuid(JsonElement element) {
    return UUID.fromString(element.getAsString());
  }

  public static ByteString toByteString(JsonElement element) {
    String hex = element.getAsString();
    byte[] bs = HexFormat.of().parseHex(hex);
    return new ByteString(bs);
  }

  public static XmlElement toXmlElement(JsonElement element) {
    if (element.isJsonNull()) {
      return new XmlElement(null);
    } else {
      return new XmlElement(element.getAsString());
    }
  }

  public static NodeId toNodeId(JsonElement element) {
    return NodeId.parse(element.getAsString());
  }

  public static ExpandedNodeId toExpandedNodeId(JsonElement element) {
    return ExpandedNodeId.parse(element.getAsString());
  }

  public static StatusCode toStatusCode(JsonElement element) {
    return new StatusCode(element.getAsLong());
  }

  public static QualifiedName toQualifiedName(JsonElement element) {
    int ns = 0;
    String name = element.getAsString();

    if (name.contains(":")) {
      String[] ss = name.split(":", 2);
      ns = Integer.parseInt(ss[0]);
      name = ss[1];
    }

    return new QualifiedName(ns, name);
  }

  public static LocalizedText toLocalizedText(JsonElement element) {
    String locale = null;
    String text = null;

    JsonObject jsonObject = element.getAsJsonObject();

    JsonElement localeElement = jsonObject.get("Locale");
    if (!localeElement.isJsonNull()) {
      locale = localeElement.getAsString();
    }

    JsonElement textElement = jsonObject.get("Text");
    if (!textElement.isJsonNull()) {
      text = textElement.getAsString();
    }

    return new LocalizedText(locale, text);
  }

  public static ExtensionObject toExtensionObject(JsonElement element) {
    JsonObject jsonObject = element.getAsJsonObject();

    NodeId typeId = toNodeId(jsonObject.get("TypeId"));

    int encoding = 0;
    if (jsonObject.has("Encoding")) {
      encoding = jsonObject.get("Encoding").getAsInt();
    }

    return switch (encoding) {
      case 0 -> {
        String body = jsonObject.get("Body").toString();
        yield new ExtensionObject(body, typeId);
      }
      case 1 -> {
        ByteString body = toByteString(jsonObject.get("Body"));
        yield new ExtensionObject(body, typeId);
      }
      case 2 -> {
        XmlElement body = toXmlElement(jsonObject.get("Body"));
        yield new ExtensionObject(body, typeId);
      }
      default -> throw new IllegalArgumentException("unknown encoding: " + encoding);
    };
  }

  public static DataValue toDataValue(JsonElement element) {
    JsonObject jsonObject = element.getAsJsonObject();

    Variant value = Variant.NULL_VALUE;
    if (jsonObject.has("Value")) {
      value = toVariant(jsonObject.get("Value"));
    }

    StatusCode statusCode = StatusCode.GOOD;
    if (jsonObject.has("StatusCode")) {
      statusCode = toStatusCode(jsonObject.get("StatusCode"));
    }

    DateTime sourceTime = null;
    if (jsonObject.has("SourceTime")) {
      sourceTime = toDateTime(jsonObject.get("SourceTime"));
    }

    UShort sourcePicoseconds = null;
    if (jsonObject.has("SourcePicoseconds")) {
      sourcePicoseconds = UShort.valueOf(jsonObject.get("SourcePicoseconds").getAsInt());
    }

    DateTime serverTime = null;
    if (jsonObject.has("ServerTime")) {
      serverTime = toDateTime(jsonObject.get("ServerTime"));
    }

    UShort serverPicoseconds = null;
    if (jsonObject.has("ServerPicoseconds")) {
      serverPicoseconds = UShort.valueOf(jsonObject.get("ServerPicoseconds").getAsInt());
    }

    return new DataValue(
        value, statusCode, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
  }

  public static Variant toVariant(JsonElement element) {
    if (element.isJsonNull() || !element.isJsonObject()) {
      return Variant.NULL_VALUE;
    }

    JsonObject jsonObject = element.getAsJsonObject();

    int typeId = jsonObject.get("Type").getAsInt();
    JsonElement bodyElement = jsonObject.get("Body");

    OpcUaDataType dataType = OpcUaDataType.fromTypeId(typeId);
    if (dataType == null) {
      throw new IllegalArgumentException("unknown type: " + typeId);
    }

    if (bodyElement.isJsonArray()) {
      JsonArray jsonArray = jsonObject.getAsJsonArray();
      Class<?> clazz = dataType.getBackingClass();
      Object array = Array.newInstance(clazz, jsonArray.size());

      for (int i = 0; i < jsonArray.size(); i++) {
        JsonElement jsonElement = jsonArray.get(i);
        Object value = to(jsonElement, dataType);
        Array.set(array, i, value);
      }

      if (jsonObject.has("Dimensions")) {
        JsonArray dimensionsArray = jsonObject.get("Dimensions").getAsJsonArray();
        int[] dimensions = new int[dimensionsArray.size()];
        for (int i = 0; i < dimensionsArray.size(); i++) {
          dimensions[i] = dimensionsArray.get(i).getAsInt();
        }

        return Variant.ofMatrix(new Matrix(array, dimensions, dataType));
      } else {
        return Variant.of(array);
      }
    } else {
      return Variant.of(to(bodyElement, dataType));
    }
  }

  // endregion

}
