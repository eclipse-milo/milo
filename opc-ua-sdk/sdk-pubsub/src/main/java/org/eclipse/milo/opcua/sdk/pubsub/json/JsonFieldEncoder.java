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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes DataSet field values into a JSON {@code Payload} object per Part 14 §6.2.4.2 (Table 32,
 * Table 35) and §7.2.5.4.2/.3 (the PubSub VerboseEncoding collapse rules).
 *
 * <p>Representation selection: a {@link DataSetFieldContentMask} of {@code 0x0} or {@code RawData}
 * only selects the Variant representation; any of the StatusCode/timestamp/picoseconds bits selects
 * the DataValue representation with exactly the masked-in members.
 *
 * <p>Under VerboseEncoding, top-level values of fields with a concrete BuiltInType and ValueRank
 * are collapsed: the {@code UaType}/{@code UaTypeId} wrappers are emitted only for abstract types
 * (BuiltInType Variant), abstract ValueRanks (Any, OneOrMoreDimensions), structure values that are
 * subtypes of the field DataType, and multi-dimensional arrays (which keep the {@code Value}/{@code
 * Dimensions} object). {@code RawData=TRUE} strips {@code UaType}/{@code UaTypeId} from those
 * wrappers (§7.2.5.4.3). Under CompactEncoding no collapse applies and the RawData bit is ignored
 * (Table 112).
 *
 * <p>Value encoding is delegated per built-in type to {@link OpcUaJsonEncoder}; the encoded
 * fragment is spliced into the message writer.
 */
final class JsonFieldEncoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonFieldEncoder.class);

  private static final AtomicBoolean OPAQUE_STRUCT_FALLBACK_WARNED = new AtomicBoolean(false);

  private final EncodingContext encodingContext;
  private final OpcUaJsonEncoder valueEncoder;

  JsonFieldEncoder(EncodingContext encodingContext) {
    this.encodingContext = encodingContext;
    this.valueEncoder = new OpcUaJsonEncoder(encodingContext);
  }

  /** {@code true} when {@code mask} selects the DataValue representation (Table 32). */
  static boolean isDataValueRepresentation(DataSetFieldContentMask mask) {
    return mask.getStatusCode()
        || mask.getSourceTimestamp()
        || mask.getServerTimestamp()
        || mask.getSourcePicoSeconds()
        || mask.getServerPicoSeconds();
  }

  /**
   * Write one {@code "<name>": <value>} payload member.
   *
   * @param writer the message writer, positioned inside the {@code Payload} object.
   * @param name the field name.
   * @param value the field value.
   * @param fieldMetaData the field's metadata, driving the Verbose collapse rules.
   * @param fieldMask the writer's {@link DataSetFieldContentMask}.
   * @param encoding the resolved field encoding.
   */
  void writeField(
      JsonWriter writer,
      String name,
      DataValue value,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      OpcUaJsonEncoder.Encoding encoding)
      throws IOException {

    writer.name(name);

    if (isDataValueRepresentation(fieldMask)) {
      writeDataValueRepresentation(writer, value, fieldMetaData, fieldMask, encoding);
    } else {
      writeVariantRepresentation(writer, value, fieldMetaData, fieldMask, encoding);
    }
  }

  /**
   * Variant representation (Table 35): a Bad field status is transferred instead of the value; an
   * Uncertain field status is dropped here and folded into the DataSetMessage header status by the
   * caller.
   */
  private void writeVariantRepresentation(
      JsonWriter writer,
      DataValue value,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      OpcUaJsonEncoder.Encoding encoding)
      throws IOException {

    if (value.statusCode().isBad()) {
      writeStatusCode(writer, value.statusCode(), encoding);
      return;
    }

    writeValue(writer, value.value(), fieldMetaData, fieldMask, encoding);
  }

  /**
   * DataValue representation: an object carrying the (possibly collapsed) value plus exactly the
   * masked-in Status/timestamp/picoseconds members, defaults omitted (OPC 10000-6 §5.4.2.18).
   */
  private void writeDataValueRepresentation(
      JsonWriter writer,
      DataValue value,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      OpcUaJsonEncoder.Encoding encoding)
      throws IOException {

    writer.beginObject();

    Variant variant = value.value();
    if (variant.isNotNull()) {
      Object raw = variant.value();
      assert raw != null;

      boolean insideUaTypeWrapper;
      boolean includeUaType;
      if (encoding == OpcUaJsonEncoder.Encoding.COMPACT) {
        // no collapse under CompactEncoding: the hoisted Variant carries its UaType
        insideUaTypeWrapper = true;
        includeUaType = true;
      } else {
        // RawData strips the UaType member but the value stays Variant-typed (§7.2.5.4.3)
        insideUaTypeWrapper = isAbstractType(fieldMetaData);
        includeUaType = insideUaTypeWrapper && !fieldMask.getRawData();
      }

      if (includeUaType) {
        writer.name("UaType").value(builtinTypeIdOf(raw));
      }

      writer.name("Value");
      if (raw instanceof Matrix matrix) {
        writeFlatElements(writer, matrix, encoding, fieldMetaData, fieldMask, insideUaTypeWrapper);
        writeDimensions(writer, matrix);
      } else {
        writeBody(writer, raw, encoding, fieldMetaData, fieldMask, insideUaTypeWrapper);
      }
    } else if (encoding == OpcUaJsonEncoder.Encoding.VERBOSE) {
      writer.name("Value").nullValue();
    }

    StatusCode status = value.statusCode();
    if (fieldMask.getStatusCode() && !status.isGood()) {
      writer.name("Status");
      writeStatusCode(writer, status, encoding);
    }

    DateTime sourceTime = value.sourceTime();
    if (fieldMask.getSourceTimestamp() && sourceTime != null && sourceTime.isNotNull()) {
      writer.name("SourceTimestamp");
      writeDateTime(writer, sourceTime);
    }
    UShort sourcePicoseconds = value.sourcePicoseconds();
    if (fieldMask.getSourcePicoSeconds()
        && sourcePicoseconds != null
        && sourcePicoseconds.intValue() > 0) {
      writer.name("SourcePicoseconds").value(sourcePicoseconds.intValue());
    }

    DateTime serverTime = value.serverTime();
    if (fieldMask.getServerTimestamp() && serverTime != null && serverTime.isNotNull()) {
      writer.name("ServerTimestamp");
      writeDateTime(writer, serverTime);
    }
    UShort serverPicoseconds = value.serverPicoseconds();
    if (fieldMask.getServerPicoSeconds()
        && serverPicoseconds != null
        && serverPicoseconds.intValue() > 0) {
      writer.name("ServerPicoseconds").value(serverPicoseconds.intValue());
    }

    writer.endObject();
  }

  /** Write a bare (possibly collapsed or wrapped) Variant value. */
  private void writeValue(
      JsonWriter writer,
      Variant variant,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      OpcUaJsonEncoder.Encoding encoding)
      throws IOException {

    if (variant.isNull()) {
      writer.nullValue();
      return;
    }

    Object raw = variant.value();
    assert raw != null;

    if (encoding == OpcUaJsonEncoder.Encoding.COMPACT) {
      // CompactEncoding: the standard Variant object of OPC 10000-6 §5.4.2.17, RawData ignored
      writer.jsonValue(toJson(encoding, encoder -> encoder.encodeVariant(null, variant)));
      return;
    }

    boolean rawData = fieldMask.getRawData();
    boolean abstractType = isAbstractType(fieldMetaData);
    boolean abstractRank = isAbstractValueRank(fieldMetaData);

    if (raw instanceof Matrix matrix) {
      writer.beginObject();
      if (abstractType && !rawData) {
        writer.name("UaType").value(builtinTypeIdOf(raw));
      }
      writer.name("Value");
      writeFlatElements(writer, matrix, encoding, fieldMetaData, fieldMask, abstractType);
      writeDimensions(writer, matrix);
      writer.endObject();
    } else if (abstractType || abstractRank) {
      writer.beginObject();
      if (abstractType && !rawData) {
        writer.name("UaType").value(builtinTypeIdOf(raw));
      }
      writer.name("Value");
      writeBody(writer, raw, encoding, fieldMetaData, fieldMask, abstractType);
      writer.endObject();
    } else {
      writeBody(writer, raw, encoding, fieldMetaData, fieldMask, false);
    }
  }

  /**
   * Write a scalar or one-dimensional array body. {@code insideUaTypeWrapper} is {@code true} when
   * the body sits inside a {@code UaType}-typed Variant wrapper (emitted or stripped by RawData).
   */
  private void writeBody(
      JsonWriter writer,
      Object raw,
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      boolean insideUaTypeWrapper)
      throws IOException {

    if (raw.getClass().isArray()) {
      writer.beginArray();
      int length = Array.getLength(raw);
      for (int i = 0; i < length; i++) {
        writeScalarBody(
            writer, Array.get(raw, i), encoding, fieldMetaData, fieldMask, insideUaTypeWrapper);
      }
      writer.endArray();
    } else {
      writeScalarBody(writer, raw, encoding, fieldMetaData, fieldMask, insideUaTypeWrapper);
    }
  }

  private void writeFlatElements(
      JsonWriter writer,
      Matrix matrix,
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      boolean insideUaTypeWrapper)
      throws IOException {

    writer.beginArray();
    Object elements = matrix.getElements();
    if (elements != null) {
      int length = Array.getLength(elements);
      for (int i = 0; i < length; i++) {
        writeScalarBody(
            writer,
            Array.get(elements, i),
            encoding,
            fieldMetaData,
            fieldMask,
            insideUaTypeWrapper);
      }
    }
    writer.endArray();
  }

  private static void writeDimensions(JsonWriter writer, Matrix matrix) throws IOException {
    writer.name("Dimensions");
    writer.beginArray();
    for (int dimension : matrix.getDimensions()) {
      writer.value(dimension);
    }
    writer.endArray();
  }

  /**
   * Write one scalar value, dispatching on its Java type. {@code insideUaTypeWrapper} is {@code
   * true} when the value sits inside a {@code UaType}-typed Variant wrapper, where an enumeration
   * is always a JSON number — even under VerboseEncoding (OPC 10000-6 §5.4.4, mirroring the stock
   * Variant body encoding); the {@code "Name_Value"} string form applies only to the collapsed bare
   * emission of concrete enum fields (Table 186).
   */
  private void writeScalarBody(
      JsonWriter writer,
      @Nullable Object value,
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask,
      boolean insideUaTypeWrapper)
      throws IOException {

    if (value == null) {
      writer.nullValue();
    } else if (value instanceof UaEnumeratedType enumerated) {
      if (insideUaTypeWrapper) {
        // enumeration inside a Variant: a JSON number regardless of the resolved encoding
        writer.value(enumerated.getValue());
      } else {
        // collapsed verbose Enumeration: "Name_Value"; compact: number (OPC 10000-6 §5.4.4)
        writer.jsonValue(toJson(encoding, encoder -> encoder.encodeEnum(null, enumerated)));
      }
    } else if (value instanceof OptionSetUInteger<?> optionSet) {
      writeScalarBody(
          writer, optionSet.getValue(), encoding, fieldMetaData, fieldMask, insideUaTypeWrapper);
    } else if (value instanceof UaStructuredType struct) {
      writeStructBody(writer, struct, encoding, fieldMetaData, fieldMask);
    } else if (value instanceof ExtensionObject xo) {
      writeExtensionObjectBody(writer, xo, encoding, fieldMetaData, fieldMask);
    } else {
      int typeId = builtinTypeIdOf(value);
      writer.jsonValue(toJson(encoding, encoder -> encodeBuiltinValue(encoder, typeId, value)));
    }
  }

  /**
   * Write a structure value: the structure's JSON object, with {@code UaTypeId} inserted always
   * under CompactEncoding (the standard ExtensionObject form, OPC 10000-6 §5.4.2.16; the RawData
   * bit is ignored, Table 112) and — collapsed per §7.2.5.4.2 — under VerboseEncoding only when the
   * value's DataType is not the field DataType (a subtype) and {@code RawData} is clear.
   */
  private void writeStructBody(
      JsonWriter writer,
      UaStructuredType struct,
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask)
      throws IOException {

    String structJson =
        toJson(encoding, encoder -> encoder.encodeStruct(null, struct, struct.getTypeId()));

    NodeId typeId = struct.getTypeId().toNodeId(encodingContext.getNamespaceTable()).orElse(null);

    if (insertsUaTypeId(encoding, typeId, fieldMetaData, fieldMask)) {
      writer.jsonValue(insertUaTypeId(structJson, typeId));
    } else {
      writer.jsonValue(structJson);
    }
  }

  /**
   * Whether a structure body carries the {@code UaTypeId} member: always under CompactEncoding (OPC
   * 10000-6 §5.4.2.16; the RawData bit is ignored, Table 112), under VerboseEncoding only for
   * subtype values with {@code RawData} clear (§7.2.5.4.2/.3).
   */
  private static boolean insertsUaTypeId(
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable NodeId valueTypeId,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask) {

    if (encoding == OpcUaJsonEncoder.Encoding.COMPACT) {
      return true;
    }
    return !fieldMask.getRawData() && isSubtypeValue(valueTypeId, fieldMetaData);
  }

  /**
   * Write an already-encoded structure value: JSON bodies are inlined; binary and XML bodies are
   * decoded to their structure first, falling back to the standard ExtensionObject form when no
   * codec is registered.
   */
  private void writeExtensionObjectBody(
      JsonWriter writer,
      ExtensionObject xo,
      OpcUaJsonEncoder.Encoding encoding,
      @Nullable FieldMetaData fieldMetaData,
      DataSetFieldContentMask fieldMask)
      throws IOException {

    if (xo instanceof ExtensionObject.Json json) {
      NodeId typeId = json.getEncodingOrTypeId();
      if (insertsUaTypeId(encoding, typeId, fieldMetaData, fieldMask)) {
        writer.jsonValue(insertUaTypeId(json.getBody(), typeId));
      } else {
        writer.jsonValue(json.getBody());
      }
      return;
    }

    try {
      UaStructuredType struct = xo.decode(encodingContext);
      writeStructBody(writer, struct, encoding, fieldMetaData, fieldMask);
    } catch (Exception e) {
      if (OPAQUE_STRUCT_FALLBACK_WARNED.compareAndSet(false, true)) {
        LOGGER.warn(
            "structure value {} could not be decoded for Verbose collapse; encoding the"
                + " ExtensionObject form instead (logged once)",
            xo.getEncodingOrTypeId(),
            e);
      }
      writer.jsonValue(toJson(encoding, encoder -> encoder.encodeExtensionObject(null, xo)));
    }
  }

  /** Insert a leading {@code UaTypeId} member into an encoded structure object. */
  private String insertUaTypeId(String structJson, @Nullable NodeId typeId) {
    JsonObject body = JsonParser.parseString(structJson).getAsJsonObject();
    var withTypeId = new JsonObject();
    withTypeId.addProperty("UaTypeId", nodeIdToString(typeId));
    body.entrySet().forEach(entry -> withTypeId.add(entry.getKey(), entry.getValue()));
    return withTypeId.toString();
  }

  /** Write a StatusCode object: {@code {"Code":n}} plus {@code Symbol} under VerboseEncoding. */
  void writeStatusCode(JsonWriter writer, StatusCode status, OpcUaJsonEncoder.Encoding encoding)
      throws IOException {
    writer.jsonValue(toJson(encoding, encoder -> encoder.encodeStatusCode(null, status)));
  }

  /** Write a DateTime as its clamped ISO 8601 string. */
  void writeDateTime(JsonWriter writer, DateTime value) throws IOException {
    writer.jsonValue(
        toJson(OpcUaJsonEncoder.Encoding.COMPACT, encoder -> encoder.encodeDateTime(null, value)));
  }

  /** Encode one value with the delegate encoder and return the JSON fragment. */
  private String toJson(OpcUaJsonEncoder.Encoding encoding, Consumer<OpcUaJsonEncoder> action) {
    valueEncoder.reset();
    valueEncoder.setEncoding(encoding);
    action.accept(valueEncoder);
    return valueEncoder.getOutputString();
  }

  /** The OPC 10000-6 §5.4.2.10 string form of {@code nodeId}. */
  private String nodeIdToString(@Nullable NodeId nodeId) {
    if (nodeId == null) {
      return NodeId.NULL_VALUE.toParseableString();
    }
    String quoted =
        toJson(OpcUaJsonEncoder.Encoding.COMPACT, encoder -> encoder.encodeNodeId(null, nodeId));
    return JsonParser.parseString(quoted).getAsString();
  }

  /**
   * {@code true} when the field's BuiltInType is abstract (Variant/BaseDataType) and the {@code
   * UaType} wrapper is required for decoding (§7.2.5.4.3).
   */
  private static boolean isAbstractType(@Nullable FieldMetaData fieldMetaData) {
    if (fieldMetaData == null) {
      return false;
    }
    int builtInType = fieldMetaData.getBuiltInType().intValue();
    return builtInType == 0 || builtInType == OpcUaDataType.Variant.getTypeId();
  }

  /** {@code true} for the abstract ValueRanks Any (-2) and OneOrMoreDimensions (0). */
  private static boolean isAbstractValueRank(@Nullable FieldMetaData fieldMetaData) {
    if (fieldMetaData == null) {
      return false;
    }
    int valueRank = fieldMetaData.getValueRank();
    return valueRank == -2 || valueRank == 0;
  }

  /**
   * {@code true} when a structure value's DataType differs from the field DataType, requiring the
   * {@code UaTypeId} member (§7.2.5.4.3). Unresolvable type ids are treated as subtypes.
   */
  private static boolean isSubtypeValue(
      @Nullable NodeId valueTypeId, @Nullable FieldMetaData fieldMetaData) {

    if (valueTypeId == null) {
      return true;
    }
    if (fieldMetaData == null) {
      return false;
    }
    return !valueTypeId.equals(fieldMetaData.getDataType());
  }

  /** The built-in type id used for {@code UaType} members, derived from the value's Java type. */
  private static int builtinTypeIdOf(Object value) {
    if (value instanceof Matrix matrix) {
      Object elements = matrix.getElements();
      if (elements == null) {
        return OpcUaDataType.Variant.getTypeId();
      }
      return builtinTypeIdOfClass(ArrayUtil.getType(elements));
    }
    if (value.getClass().isArray()) {
      return builtinTypeIdOfClass(ArrayUtil.getType(value));
    }
    return builtinTypeIdOfClass(value.getClass());
  }

  private static int builtinTypeIdOfClass(Class<?> clazz) {
    if (UaEnumeratedType.class.isAssignableFrom(clazz)) {
      return OpcUaDataType.Int32.getTypeId();
    }
    if (UaStructuredType.class.isAssignableFrom(clazz)
        || ExtensionObject.class.isAssignableFrom(clazz)) {
      return OpcUaDataType.ExtensionObject.getTypeId();
    }
    OpcUaDataType dataType = OpcUaDataType.fromBackingClass(clazz);
    if (dataType == null) {
      throw new IllegalArgumentException("not a built-in type: " + clazz.getName());
    }
    return dataType.getTypeId();
  }

  /** Encode one built-in scalar value with the delegate encoder. */
  private static void encodeBuiltinValue(OpcUaJsonEncoder encoder, int typeId, Object value) {
    OpcUaDataType dataType = OpcUaDataType.fromTypeId(typeId);
    if (dataType == null) {
      throw new IllegalArgumentException("not a built-in type id: " + typeId);
    }

    switch (dataType) {
      case Boolean -> encoder.encodeBoolean(null, (Boolean) value);
      case SByte -> encoder.encodeSByte(null, (Byte) value);
      case Byte -> encoder.encodeByte(null, (UByte) value);
      case Int16 -> encoder.encodeInt16(null, (Short) value);
      case UInt16 -> encoder.encodeUInt16(null, (UShort) value);
      case Int32 -> encoder.encodeInt32(null, (Integer) value);
      case UInt32 -> encoder.encodeUInt32(null, (UInteger) value);
      case Int64 -> encoder.encodeInt64(null, (Long) value);
      case UInt64 -> encoder.encodeUInt64(null, (ULong) value);
      case Float -> encoder.encodeFloat(null, (Float) value);
      case Double -> encoder.encodeDouble(null, (Double) value);
      case String -> encoder.encodeString(null, (String) value);
      case DateTime -> encoder.encodeDateTime(null, (DateTime) value);
      case Guid -> encoder.encodeGuid(null, (UUID) value);
      case ByteString -> encoder.encodeByteString(null, (ByteString) value);
      case XmlElement -> encoder.encodeXmlElement(null, (XmlElement) value);
      case NodeId -> encoder.encodeNodeId(null, (NodeId) value);
      case ExpandedNodeId -> encoder.encodeExpandedNodeId(null, (ExpandedNodeId) value);
      case StatusCode -> encoder.encodeStatusCode(null, (StatusCode) value);
      case QualifiedName -> encoder.encodeQualifiedName(null, (QualifiedName) value);
      case LocalizedText -> encoder.encodeLocalizedText(null, (LocalizedText) value);
      case ExtensionObject -> encoder.encodeExtensionObject(null, (ExtensionObject) value);
      case DataValue -> encoder.encodeDataValue(null, (DataValue) value);
      case Variant -> encoder.encodeVariant(null, (Variant) value);
      case DiagnosticInfo -> encoder.encodeDiagnosticInfo(null, (DiagnosticInfo) value);
    }
  }
}
