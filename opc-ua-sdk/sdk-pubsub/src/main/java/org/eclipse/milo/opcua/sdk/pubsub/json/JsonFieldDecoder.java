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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes JSON {@code Payload} member values into {@link DataValue}s, tolerantly, across the
 * current Compact/Verbose encodings and the deprecated Reversible/NonReversible encodings (OPC
 * 10000-6 §5.4 and Annex H).
 *
 * <p>When the caller supplies a field's metadata-declared built-in type, shape-ambiguous values
 * decode against it (Part 14 §6.2.3: the DataSetMetaData carries "the information necessary to
 * decode DataSetMessages"): collapsed Verbose {@code StatusCode} objects decode as StatusCode
 * <em>values</em> on StatusCode-typed fields, Int64/UInt64/DateTime/Guid/ByteString/XmlElement/
 * NodeId/QualifiedName strings decode to their declared types, LocalizedText objects decode as
 * LocalizedText, numbers take their declared width and signedness, and arrays — including empty
 * ones — decode as arrays of the declared type. A value that does not parse as its declared type
 * falls back to the shape-based decode below (incoherent-publisher tolerance).
 *
 * <p>Without metadata, collapsed Verbose values are decoded by their JSON shape: booleans, strings,
 * and numbers map to Boolean, String, and Int32/Int64/Double (integral vs. decimal literal); {@code
 * UaType}/{@code Type} wrappers decode by the carried built-in type id; {@code Value}/{@code
 * Dimensions} objects become arrays and matrices; {@code UaTypeId} objects become JSON {@link
 * ExtensionObject}s; StatusCode-shaped objects in a value position carry the Bad field status of
 * Part 14 Table 35; other objects are surfaced as JSON {@link ExtensionObject}s without a type id,
 * for the application to decode against its metadata. Verbose Enumeration strings ({@code
 * "Name_Value"}) are indistinguishable from String values without metadata and decode as String.
 *
 * <p>Field decoding never throws: undecodable values yield a {@code Bad_DecodingError} {@link
 * DataValue} with a one-time WARN (lenient payload handling).
 */
final class JsonFieldDecoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonFieldDecoder.class);

  private static final AtomicBoolean LENIENT_PAYLOAD_WARNED = new AtomicBoolean(false);

  /** Members of the current and deprecated Variant/DataValue wrapper objects. */
  private static final Set<String> WRAPPER_MEMBERS =
      Set.of(
          "UaType",
          "Type",
          "Value",
          "Body",
          "Dimensions",
          "Status",
          "SourceTimestamp",
          "SourcePicoseconds",
          "ServerTimestamp",
          "ServerPicoseconds");

  private final EncodingContext encodingContext;

  JsonFieldDecoder(EncodingContext encodingContext) {
    this.encodingContext = encodingContext;
  }

  /**
   * Decode one payload member value, against {@code declaredType} when present, else by JSON shape.
   * Never throws.
   */
  DataValue decodeFieldValue(@Nullable JsonElement element, @Nullable OpcUaDataType declaredType) {
    try {
      return decodeFieldValueInternal(element, declaredType);
    } catch (Exception e) {
      if (LENIENT_PAYLOAD_WARNED.compareAndSet(false, true)) {
        LOGGER.warn(
            "undecodable JSON payload field value; substituting Bad_DecodingError (logged once,"
                + " subsequent occurrences at debug)",
            e);
      } else {
        LOGGER.debug("undecodable JSON payload field value", e);
      }
      return new DataValue(
          Variant.NULL_VALUE,
          new StatusCode(StatusCodes.Bad_DecodingError),
          null,
          null,
          null,
          null);
    }
  }

  private DataValue decodeFieldValueInternal(
      @Nullable JsonElement element, @Nullable OpcUaDataType declaredType) {

    if (element == null || element.isJsonNull()) {
      return plainValue(Variant.NULL_VALUE);
    }

    if (declaredType != null) {
      try {
        return decodeDeclaredValue(element, declaredType);
      } catch (Exception e) {
        LOGGER.debug(
            "field value does not decode as its metadata-declared type {}; falling back to the"
                + " shape-based decode",
            declaredType,
            e);
      }
    }

    if (element.isJsonPrimitive()) {
      return plainValue(Variant.of(decodeGenericScalar(element.getAsJsonPrimitive())));
    }

    if (element.isJsonArray()) {
      return plainValue(Variant.of(decodeGenericArray(element.getAsJsonArray())));
    }

    JsonObject object = element.getAsJsonObject();

    if (object.isEmpty()) {
      // a Good StatusCode object or a null/default structure value
      return plainValue(Variant.NULL_VALUE);
    }

    if (isStatusCodeObject(object)) {
      // Table 35 footnote c: a Bad status is transferred instead of the value
      return new DataValue(Variant.NULL_VALUE, decodeStatusCode(object), null, null, null, null);
    }

    if (isWrapperObject(object)) {
      return decodeWrapper(object);
    }

    return plainValue(Variant.of(decodeObjectValue(object)));
  }

  private static DataValue plainValue(Variant variant) {
    return new DataValue(variant, StatusCode.GOOD, null, null, null, null);
  }

  /**
   * Decode a value against its metadata-declared built-in type. Wrapper objects still take the
   * wrapper path — with the declared type directing their {@code Value} member when the wire
   * carries no {@code UaType} — and a StatusCode-shaped object on a field NOT declared StatusCode
   * remains the Table 35 footnote-c bad-status transfer. Everything else — including the
   * shape-ambiguous Verbose forms (StatusCode objects on StatusCode fields, Int64/DateTime/Guid/...
   * strings, empty or mixed-width arrays) — decodes as the declared type.
   */
  private DataValue decodeDeclaredValue(JsonElement element, OpcUaDataType declaredType) {
    if (element.isJsonArray()) {
      return plainValue(Variant.of(decodeTypedArray(declaredType, element.getAsJsonArray())));
    }

    if (element.isJsonObject()) {
      JsonObject object = element.getAsJsonObject();

      if (isWrapperObject(object)) {
        return decodeWrapper(object, declaredType);
      }
      if (declaredType != OpcUaDataType.StatusCode && isStatusCodeObject(object)) {
        // Table 35 footnote c: a Bad status is transferred instead of the value
        return new DataValue(Variant.NULL_VALUE, decodeStatusCode(object), null, null, null, null);
      }
    }

    return plainValue(Variant.of(decodeTypedScalar(declaredType, element)));
  }

  /** {@code true} for objects of the shape {@code {"Code":n}} / {@code {"Code":n,"Symbol":s}}. */
  private static boolean isStatusCodeObject(JsonObject object) {
    if (!object.has("Code")) {
      return false;
    }
    for (String name : object.keySet()) {
      if (!"Code".equals(name) && !"Symbol".equals(name)) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@code true} for Variant/DataValue wrapper objects: a {@code Value}/{@code Body} or {@code
   * UaType}/{@code Type} member is present and every member is a known wrapper member — objects
   * with additional members are treated as structure values instead.
   */
  private static boolean isWrapperObject(JsonObject object) {
    if (!(object.has("Value")
        || object.has("Body")
        || object.has("UaType")
        || object.has("Type"))) {
      return false;
    }
    for (String name : object.keySet()) {
      if (!WRAPPER_MEMBERS.contains(name)) {
        return false;
      }
    }
    return true;
  }

  /** Decode a Variant/DataValue wrapper object, current or deprecated-Reversible spelling. */
  private DataValue decodeWrapper(JsonObject object) {
    return decodeWrapper(object, null);
  }

  /**
   * Decode a Variant/DataValue wrapper object; a wire-carried {@code UaType}/{@code Type} directs
   * the {@code Value} member decode, else the metadata-declared type when present.
   */
  private DataValue decodeWrapper(JsonObject object, @Nullable OpcUaDataType declaredType) {
    OpcUaDataType dataType;
    JsonElement typeElement = object.has("UaType") ? object.get("UaType") : object.get("Type");
    if (typeElement != null && typeElement.isJsonPrimitive()) {
      // a wire-carried type id wins, even when it resolves to no built-in type (generic decode)
      dataType = OpcUaDataType.fromTypeId(typeElement.getAsInt());
    } else {
      dataType = declaredType;
    }

    JsonElement body = object.has("Value") ? object.get("Value") : object.get("Body");

    int[] dimensions = null;
    JsonElement dimensionsElement = object.get("Dimensions");
    if (dimensionsElement != null && dimensionsElement.isJsonArray()) {
      JsonArray array = dimensionsElement.getAsJsonArray();
      dimensions = new int[array.size()];
      for (int i = 0; i < array.size(); i++) {
        dimensions[i] = array.get(i).getAsInt();
      }
    }

    Object value = null;
    if (body != null && !body.isJsonNull()) {
      if (body.isJsonArray()) {
        Object array = decodeTypedArray(dataType, body.getAsJsonArray());
        if (dimensions != null && dimensions.length > 1) {
          value = new Matrix(array, dimensions);
        } else {
          value = array;
        }
      } else {
        value = decodeTypedScalar(dataType, body);
      }
    }

    StatusCode status = StatusCode.GOOD;
    JsonElement statusElement = object.get("Status");
    if (statusElement != null && !statusElement.isJsonNull()) {
      status = decodeStatusCode(statusElement);
    }

    DateTime sourceTime = decodeTimestamp(object.get("SourceTimestamp"));
    UShort sourcePicoseconds = decodePicoseconds(object.get("SourcePicoseconds"));
    DateTime serverTime = decodeTimestamp(object.get("ServerTimestamp"));
    UShort serverPicoseconds = decodePicoseconds(object.get("ServerPicoseconds"));

    return new DataValue(
        Variant.of(value), status, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
  }

  /** Decode a scalar value by built-in type; without one, by JSON shape. */
  private @Nullable Object decodeTypedScalar(
      @Nullable OpcUaDataType dataType, JsonElement element) {
    if (element.isJsonNull()) {
      return null;
    }

    if (dataType == null) {
      return decodeGenericValue(element);
    }

    return switch (dataType) {
      case Boolean -> element.getAsBoolean();
      case SByte -> element.getAsByte();
      case Byte -> Unsigned.ubyte(element.getAsInt());
      case Int16 -> element.getAsShort();
      case UInt16 -> Unsigned.ushort(element.getAsInt());
      case Int32 -> element.getAsInt();
      case UInt32 -> Unsigned.uint(element.getAsLong());
      case Int64 -> Long.parseLong(element.getAsString());
      case UInt64 -> ULong.valueOf(element.getAsString());
      case Float -> decodeFloat(element);
      case Double -> decodeDouble(element);
      case String -> element.getAsString();
      case DateTime -> decodeTimestamp(element);
      case Guid -> UUID.fromString(element.getAsString());
      case ByteString -> ByteString.of(Base64.getDecoder().decode(element.getAsString()));
      case XmlElement -> XmlElement.of(element.getAsString());
      case NodeId -> delegate(element).decodeNodeId(null);
      case ExpandedNodeId -> delegate(element).decodeExpandedNodeId(null);
      case StatusCode -> decodeStatusCode(element);
      case QualifiedName -> delegate(element).decodeQualifiedName(null);
      case LocalizedText -> delegate(element).decodeLocalizedText(null);
      case ExtensionObject ->
          element.isJsonObject()
              ? decodeObjectValue(element.getAsJsonObject())
              : decodeGenericValue(element);
      case DataValue -> delegate(element).decodeDataValue(null);
      case Variant -> delegate(element).decodeVariant(null);
      case DiagnosticInfo -> delegate(element).decodeDiagnosticInfo(null);
    };
  }

  /** Decode a typed array; with no usable type, falls back to the generic array decode. */
  private Object decodeTypedArray(@Nullable OpcUaDataType dataType, JsonArray array) {
    Class<?> backingClass = dataType != null ? dataType.getBackingClass() : null;

    if (backingClass == null) {
      return decodeGenericArray(array);
    }

    Object result = Array.newInstance(backingClass, array.size());
    for (int i = 0; i < array.size(); i++) {
      Array.set(result, i, decodeTypedScalar(dataType, array.get(i)));
    }
    return result;
  }

  private float decodeFloat(JsonElement element) {
    JsonPrimitive primitive = element.getAsJsonPrimitive();
    if (primitive.isString()) {
      return switch (primitive.getAsString()) {
        case "Infinity" -> Float.POSITIVE_INFINITY;
        case "-Infinity" -> Float.NEGATIVE_INFINITY;
        case "NaN" -> Float.NaN;
        default -> Float.parseFloat(primitive.getAsString());
      };
    }
    return primitive.getAsFloat();
  }

  private double decodeDouble(JsonElement element) {
    JsonPrimitive primitive = element.getAsJsonPrimitive();
    if (primitive.isString()) {
      return switch (primitive.getAsString()) {
        case "Infinity" -> Double.POSITIVE_INFINITY;
        case "-Infinity" -> Double.NEGATIVE_INFINITY;
        case "NaN" -> Double.NaN;
        default -> Double.parseDouble(primitive.getAsString());
      };
    }
    return primitive.getAsDouble();
  }

  /** Decode a value without type information, by JSON shape. */
  private @Nullable Object decodeGenericValue(JsonElement element) {
    if (element.isJsonNull()) {
      return null;
    }
    if (element.isJsonPrimitive()) {
      return decodeGenericScalar(element.getAsJsonPrimitive());
    }
    if (element.isJsonArray()) {
      return decodeGenericArray(element.getAsJsonArray());
    }

    JsonObject object = element.getAsJsonObject();
    if (object.isEmpty()) {
      return null;
    }
    if (isStatusCodeObject(object)) {
      return decodeStatusCode(object);
    }
    if (isWrapperObject(object)) {
      DataValue wrapped = decodeWrapper(object);
      return wrapped.value().value();
    }
    return decodeObjectValue(object);
  }

  private Object decodeGenericScalar(JsonPrimitive primitive) {
    if (primitive.isBoolean()) {
      return primitive.getAsBoolean();
    }
    if (primitive.isString()) {
      return primitive.getAsString();
    }

    // number: integral literals decode as Int32/Int64, decimal/exponent literals as Double
    String literal = primitive.getAsString();
    if (literal.indexOf('.') < 0 && literal.indexOf('e') < 0 && literal.indexOf('E') < 0) {
      try {
        long value = Long.parseLong(literal);
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
          return (int) value;
        } else {
          return value;
        }
      } catch (NumberFormatException e) {
        // fall through to Double
      }
    }
    return primitive.getAsDouble();
  }

  /**
   * Decode an array without type information. Arrays of one uniform element class become typed
   * arrays; numeric elements whose integral literals decode to mixed widths (e.g. {@code [0,
   * 4294967295]} as Integer + Long) are widened to the widest class present. Anything else — empty
   * arrays, arrays whose elements all decode to null, nested bare arrays, and genuinely
   * heterogeneous arrays — becomes a {@code Variant[]}, the OPC UA representation of an array with
   * no common concrete element type (a bare {@code Object[]} is not a legal Variant value).
   */
  private Object decodeGenericArray(JsonArray array) {
    var decoded = new Object[array.size()];
    Class<?> commonClass = null;
    boolean uniform = true;

    for (int i = 0; i < array.size(); i++) {
      decoded[i] = decodeGenericValue(array.get(i));
      if (decoded[i] != null) {
        if (commonClass == null) {
          commonClass = decoded[i].getClass();
        } else if (!commonClass.equals(decoded[i].getClass())) {
          uniform = false;
        }
      }
    }

    if (!uniform) {
      Class<?> widened = widenedNumericClass(decoded);
      if (widened != null) {
        for (int i = 0; i < decoded.length; i++) {
          decoded[i] = widenNumeric(decoded[i], widened);
        }
        commonClass = widened;
        uniform = true;
      }
    }

    if (uniform && commonClass != null && !commonClass.isArray()) {
      Object typed = Array.newInstance(commonClass, decoded.length);
      for (int i = 0; i < decoded.length; i++) {
        Array.set(typed, i, decoded[i]);
      }
      return typed;
    }

    var variants = new Variant[decoded.length];
    for (int i = 0; i < decoded.length; i++) {
      variants[i] = Variant.of(decoded[i]);
    }
    return variants;
  }

  /** The numeric classes the generic scalar decode produces, narrowest to widest. */
  private static final List<Class<?>> NUMERIC_WIDTH_ORDER =
      List.of(Integer.class, Long.class, Double.class);

  /**
   * The widest of {@code Integer}/{@code Long}/{@code Double} when every non-null element is one of
   * them, else {@code null}. All-null and all-Integer arrays need no widening and also yield {@code
   * null}.
   */
  private static @Nullable Class<?> widenedNumericClass(@Nullable Object[] decoded) {
    int widest = -1;
    for (Object value : decoded) {
      if (value != null) {
        int width = NUMERIC_WIDTH_ORDER.indexOf(value.getClass());
        if (width < 0) {
          return null;
        }
        widest = Math.max(widest, width);
      }
    }
    return widest > 0 ? NUMERIC_WIDTH_ORDER.get(widest) : null;
  }

  /**
   * {@code value} boxed as {@code target} ({@code Long} or {@code Double}); null passes through.
   */
  private static @Nullable Object widenNumeric(@Nullable Object value, Class<?> target) {
    if (value == null) {
      return null;
    }
    Number number = (Number) value;
    // no ternary: a Double/Long conditional expression numerically promotes both branches to Double
    if (target == Double.class) {
      return number.doubleValue();
    }
    return number.longValue();
  }

  /**
   * Decode a structure value: wrapped ExtensionObjects (current Milo and deprecated Reversible
   * forms) are delegated; objects with an inlined {@code UaTypeId} (the §5.4.2.16 form) and plain
   * structure objects are surfaced as JSON {@link ExtensionObject}s.
   */
  private Object decodeObjectValue(JsonObject object) {
    boolean hasTypeId = object.has("UaTypeId") || object.has("TypeId");
    boolean hasBody =
        object.has("UaBody")
            || object.has("UaEncoding")
            || (object.has("TypeId") && (object.has("Body") || object.has("Encoding")));

    if (hasTypeId && hasBody) {
      return delegate(object).decodeExtensionObject(null);
    }

    if (hasTypeId) {
      String idMember = object.has("UaTypeId") ? "UaTypeId" : "TypeId";
      JsonElement idElement = object.get(idMember);

      NodeId typeId;
      try {
        typeId = delegate(idElement).decodeNodeId(null);
      } catch (Exception e) {
        typeId = NodeId.NULL_VALUE;
      }

      var body = new JsonObject();
      object
          .entrySet()
          .forEach(
              entry -> {
                if (!entry.getKey().equals(idMember)) {
                  body.add(entry.getKey(), entry.getValue());
                }
              });

      return ExtensionObject.of(body.toString(), typeId);
    }

    return ExtensionObject.of(object.toString(), NodeId.NULL_VALUE);
  }

  /** Decode a StatusCode value: a JSON number (deprecated Reversible) or the object form. */
  static StatusCode decodeStatusCode(JsonElement element) {
    if (element.isJsonPrimitive()) {
      return new StatusCode(element.getAsLong());
    }
    if (element.isJsonObject()) {
      JsonElement code = element.getAsJsonObject().get("Code");
      if (code != null && code.isJsonPrimitive()) {
        return new StatusCode(code.getAsLong());
      }
      return StatusCode.GOOD;
    }
    return StatusCode.GOOD;
  }

  /** Decode an ISO 8601 timestamp, accepting offset forms; {@code null} when unparseable. */
  static @Nullable DateTime decodeTimestamp(@Nullable JsonElement element) {
    if (element == null || element.isJsonNull() || !element.isJsonPrimitive()) {
      return null;
    }
    String value = element.getAsString();
    try {
      return new DateTime(Instant.parse(value));
    } catch (Exception e) {
      try {
        return new DateTime(OffsetDateTime.parse(value).toInstant());
      } catch (Exception e2) {
        return null;
      }
    }
  }

  private static @Nullable UShort decodePicoseconds(@Nullable JsonElement element) {
    if (element == null || !element.isJsonPrimitive()) {
      return null;
    }
    try {
      return Unsigned.ushort(element.getAsInt());
    } catch (Exception e) {
      return null;
    }
  }

  private OpcUaJsonDecoder delegate(JsonElement element) {
    return new OpcUaJsonDecoder(encodingContext, element.toString());
  }
}
