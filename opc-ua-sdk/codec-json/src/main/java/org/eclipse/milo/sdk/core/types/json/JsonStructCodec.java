/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class JsonStructCodec extends GenericDataTypeCodec<JsonStruct> {

  private final Lazy<Map<StructureField, Object>> hints = new Lazy<>();

  private final StructureDefinition definition;

  private final DataType dataType;
  private final DataTypeTree dataTypeTree;

  public JsonStructCodec(DataType dataType, DataTypeTree dataTypeTree) {
    this.dataType = dataType;
    this.dataTypeTree = dataTypeTree;

    definition = (StructureDefinition) dataType.getDataTypeDefinition();
  }

  @Override
  public Class<JsonStruct> getType() {
    return JsonStruct.class;
  }

  // region Decoding

  @Override
  public JsonStruct decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {
    return switch (definition.getStructureType()) {
      case Structure, StructureWithOptionalFields, StructureWithSubtypedValues ->
          decodeStruct(decoder);
      case Union, UnionWithSubtypedValues -> decodeUnion(decoder);
    };
  }

  private JsonStruct decodeStruct(UaDecoder decoder) throws UaSerializationException {
    var jsonObject = new JsonObject();

    var switchField = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      switchField = decoder.decodeUInt32("SwitchField").longValue();
    }

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || (switchField >>> optionalFieldIndex++ & 1L) == 1L) {
          JsonElement value = decodeFieldValue(decoder, field);

          jsonObject.add(requireNonNull(field.getName()), value);
        }
      }
    } else {
      for (StructureField field : fields) {
        JsonElement value = decodeFieldValue(decoder, field);

        jsonObject.add(requireNonNull(field.getName()), value);
      }
    }

    var metadata = new JsonObject();
    metadata.addProperty("dataTypeId", dataType.getNodeId().toParseableString());
    jsonObject.add("__metadata", metadata);

    return new JsonStruct(dataType, jsonObject);
  }

  private JsonStruct decodeUnion(UaDecoder decoder) throws UaSerializationException {
    int switchField = decoder.decodeUInt32("SwitchField").intValue();
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (switchField == 0) {
      var jsonObject = new JsonObject();

      var metadata = new JsonObject();
      metadata.addProperty("dataTypeId", dataType.getNodeId().toParseableString());
      jsonObject.add("__metadata", metadata);

      return new JsonStruct(dataType, jsonObject);
    } else if (switchField <= fields.length) {
      StructureField field = fields[switchField - 1];
      JsonElement value = decodeFieldValue(decoder, field);

      var jsonObject = new JsonObject();
      jsonObject.add(requireNonNull(field.getName()), value);

      var metadata = new JsonObject();
      metadata.addProperty("dataTypeId", dataType.getNodeId().toParseableString());
      jsonObject.add("__metadata", metadata);

      return new JsonStruct(dataType, jsonObject);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "invalid Union SwitchField value: " + switchField);
    }
  }

  private JsonElement decodeFieldValue(UaDecoder decoder, StructureField field) {
    String fieldName = requireNonNull(field.getName());
    NodeId dataTypeId = field.getDataType();

    if (field.getValueRank() == -1) {
      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        return decodeBuiltinDataType(decoder, fieldName, (OpcUaDataType) hint);
      } else if (hint instanceof EnumHint) {
        int enumValue = decoder.decodeEnum(fieldName);

        return new JsonPrimitive(enumValue);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          ExtensionObject xo = decoder.decodeExtensionObject(fieldName);
          JsonStruct struct = (JsonStruct) xo.decode(decoder.getEncodingContext());

          return struct.getJsonObject();
        } else {
          JsonStruct struct = (JsonStruct) decoder.decodeStruct(fieldName, dataTypeId);

          return struct.getJsonObject();
        }
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else if (field.getValueRank() == 1) {
      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        return decodeBuiltinDataTypeArray(decoder, fieldName, (OpcUaDataType) hint);
      } else if (hint instanceof EnumHint) {
        var array = new JsonArray();
        for (int value : decoder.decodeEnumArray(fieldName)) {
          array.add(value);
        }
        return array;
      } else if (hint instanceof StructHint) {
        var array = new JsonArray();

        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          for (ExtensionObject value : decoder.decodeExtensionObjectArray(fieldName)) {
            JsonStruct struct = (JsonStruct) value.decode(decoder.getEncodingContext());
            array.add(struct.getJsonObject());
          }
        } else {
          for (Object o : decoder.decodeStructArray(fieldName, dataTypeId)) {
            JsonStruct struct = (JsonStruct) o;
            array.add(struct.getJsonObject());
          }
        }
        return array;
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else if (field.getValueRank() > 1) {
      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        Matrix matrix = decoder.decodeMatrix(fieldName, (OpcUaDataType) hint);

        return decodeBuiltinDataTypeMatrix(matrix);
      } else if (hint instanceof EnumHint) {
        Matrix matrix = decoder.decodeEnumMatrix(fieldName);

        return decodeEnumMatrix(matrix);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          Matrix matrix = decoder.decodeMatrix(fieldName, OpcUaDataType.ExtensionObject);

          return decodeStructMatrix(decoder.getEncodingContext(), matrix, true);
        } else {
          Matrix matrix = decoder.decodeStructMatrix(fieldName, dataTypeId);

          return decodeStructMatrix(decoder.getEncodingContext(), matrix, false);
        }
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else {
      throw new IllegalArgumentException("unsupported value rank: " + field.getValueRank());
    }
  }

  /**
   * Check if the field allows subtyping.
   *
   * <p>In Structures and Unions this means the field is encoded as an ExtensionObject.
   *
   * @param field the {@link StructureField} to check.
   * @return {@code true} if the field allows subtyping.
   */
  private boolean fieldAllowsSubtyping(StructureField field) {
    return field.getIsOptional()
        && (definition.getStructureType() == StructureType.StructureWithSubtypedValues
            || definition.getStructureType() == StructureType.UnionWithSubtypedValues);
  }

  private static JsonElement decodeBuiltinDataType(
      UaDecoder decoder, String fieldName, OpcUaDataType dataType) {
    return switch (dataType) {
      case Boolean -> JsonConversions.fromBoolean(decoder.decodeBoolean(fieldName));
      case SByte -> JsonConversions.fromSByte(decoder.decodeSByte(fieldName));
      case Byte -> JsonConversions.fromByte(decoder.decodeByte(fieldName));
      case Int16 -> JsonConversions.fromInt16(decoder.decodeInt16(fieldName));
      case UInt16 -> JsonConversions.fromUInt16(decoder.decodeUInt16(fieldName));
      case Int32 -> JsonConversions.fromInt32(decoder.decodeInt32(fieldName));
      case UInt32 -> JsonConversions.fromUInt32(decoder.decodeUInt32(fieldName));
      case Int64 -> JsonConversions.fromInt64(decoder.decodeInt64(fieldName));
      case UInt64 -> JsonConversions.fromUInt64(decoder.decodeUInt64(fieldName));
      case Float -> JsonConversions.fromFloat(decoder.decodeFloat(fieldName));
      case Double -> JsonConversions.fromDouble(decoder.decodeDouble(fieldName));
      case String -> JsonConversions.fromString(decoder.decodeString(fieldName));
      case DateTime -> JsonConversions.fromDateTime(decoder.decodeDateTime(fieldName));
      case Guid -> JsonConversions.fromGuid(decoder.decodeGuid(fieldName));
      case ByteString -> JsonConversions.fromByteString(decoder.decodeByteString(fieldName));
      case XmlElement -> JsonConversions.fromXmlElement(decoder.decodeXmlElement(fieldName));
      case NodeId -> JsonConversions.fromNodeId(decoder.decodeNodeId(fieldName));
      case ExpandedNodeId ->
          JsonConversions.fromExpandedNodeId(decoder.decodeExpandedNodeId(fieldName));
      case StatusCode -> JsonConversions.fromStatusCode(decoder.decodeStatusCode(fieldName));
      case QualifiedName ->
          JsonConversions.fromQualifiedName(decoder.decodeQualifiedName(fieldName));
      case LocalizedText ->
          JsonConversions.fromLocalizedText(decoder.decodeLocalizedText(fieldName));
      case ExtensionObject ->
          JsonConversions.fromExtensionObject(decoder.decodeExtensionObject(fieldName));
      case DataValue -> JsonConversions.fromDataValue(decoder.decodeDataValue(fieldName));
      case Variant -> JsonConversions.fromVariant(decoder.decodeVariant(fieldName));
      default -> JsonNull.INSTANCE;
    };
  }

  private static JsonElement decodeBuiltinDataTypeArray(
      UaDecoder decoder, String fieldName, OpcUaDataType dataType) {
    switch (dataType) {
      case Boolean:
        {
          var array = new JsonArray();
          for (boolean value : decoder.decodeBooleanArray(fieldName)) {
            array.add(JsonConversions.fromBoolean(value));
          }
          return array;
        }
      case SByte:
        {
          var array = new JsonArray();
          for (byte value : decoder.decodeSByteArray(fieldName)) {
            array.add(JsonConversions.fromSByte(value));
          }
          return array;
        }
      case Byte:
        {
          var array = new JsonArray();
          for (UByte value : decoder.decodeByteArray(fieldName)) {
            array.add(JsonConversions.fromByte(value));
          }
          return array;
        }
      case Int16:
        {
          var array = new JsonArray();
          for (short value : decoder.decodeInt16Array(fieldName)) {
            array.add(JsonConversions.fromInt16(value));
          }
          return array;
        }
      case UInt16:
        {
          var array = new JsonArray();
          for (UShort value : decoder.decodeUInt16Array(fieldName)) {
            array.add(JsonConversions.fromUInt16(value));
          }
          return array;
        }
      case Int32:
        {
          var array = new JsonArray();
          for (int value : decoder.decodeInt32Array(fieldName)) {
            array.add(JsonConversions.fromInt32(value));
          }
          return array;
        }
      case UInt32:
        {
          var array = new JsonArray();
          for (UInteger value : decoder.decodeUInt32Array(fieldName)) {
            array.add(JsonConversions.fromUInt32(value));
          }
          return array;
        }
      case Int64:
        {
          var array = new JsonArray();
          for (long value : decoder.decodeInt64Array(fieldName)) {
            array.add(JsonConversions.fromInt64(value));
          }
          return array;
        }
      case UInt64:
        {
          var array = new JsonArray();
          for (ULong value : decoder.decodeUInt64Array(fieldName)) {
            array.add(JsonConversions.fromUInt64(value));
          }
          return array;
        }
      case Float:
        {
          var array = new JsonArray();
          for (float value : decoder.decodeFloatArray(fieldName)) {
            array.add(JsonConversions.fromFloat(value));
          }
          return array;
        }
      case Double:
        {
          var array = new JsonArray();
          for (double value : decoder.decodeDoubleArray(fieldName)) {
            array.add(JsonConversions.fromDouble(value));
          }
          return array;
        }
      case String:
        {
          var array = new JsonArray();
          for (String value : decoder.decodeStringArray(fieldName)) {
            array.add(JsonConversions.fromString(value));
          }
          return array;
        }
      case DateTime:
        {
          var array = new JsonArray();
          for (DateTime value : decoder.decodeDateTimeArray(fieldName)) {
            array.add(JsonConversions.fromDateTime(value));
          }
          return array;
        }
      case Guid:
        {
          var array = new JsonArray();
          for (UUID value : decoder.decodeGuidArray(fieldName)) {
            array.add(JsonConversions.fromGuid(value));
          }
          return array;
        }
      case ByteString:
        {
          var array = new JsonArray();
          for (ByteString value : decoder.decodeByteStringArray(fieldName)) {
            array.add(JsonConversions.fromByteString(value));
          }
          return array;
        }
      case XmlElement:
        {
          var array = new JsonArray();
          for (XmlElement value : decoder.decodeXmlElementArray(fieldName)) {
            array.add(JsonConversions.fromXmlElement(value));
          }
          return array;
        }
      case NodeId:
        {
          var array = new JsonArray();
          for (NodeId value : decoder.decodeNodeIdArray(fieldName)) {
            array.add(JsonConversions.fromNodeId(value));
          }
          return array;
        }
      case ExpandedNodeId:
        {
          var array = new JsonArray();
          for (ExpandedNodeId value : decoder.decodeExpandedNodeIdArray(fieldName)) {
            array.add(JsonConversions.fromExpandedNodeId(value));
          }
          return array;
        }
      case StatusCode:
        {
          var array = new JsonArray();
          for (StatusCode value : decoder.decodeStatusCodeArray(fieldName)) {
            array.add(JsonConversions.fromStatusCode(value));
          }
          return array;
        }
      case QualifiedName:
        {
          var array = new JsonArray();
          for (QualifiedName value : decoder.decodeQualifiedNameArray(fieldName)) {
            array.add(JsonConversions.fromQualifiedName(value));
          }
          return array;
        }
      case LocalizedText:
        {
          var array = new JsonArray();
          for (LocalizedText value : decoder.decodeLocalizedTextArray(fieldName)) {
            array.add(JsonConversions.fromLocalizedText(value));
          }
          return array;
        }
      case ExtensionObject:
        {
          var array = new JsonArray();
          for (ExtensionObject value : decoder.decodeExtensionObjectArray(fieldName)) {
            array.add(JsonConversions.fromExtensionObject(value));
          }
          return array;
        }
      case DataValue:
        {
          var array = new JsonArray();
          for (DataValue value : decoder.decodeDataValueArray(fieldName)) {
            array.add(JsonConversions.fromDataValue(value));
          }
          return array;
        }
      case Variant:
        var array = new JsonArray();
        for (Variant value : decoder.decodeVariantArray(fieldName)) {
          array.add(JsonConversions.fromVariant(value));
        }
        return array;

      case DiagnosticInfo:
      default:
        return JsonNull.INSTANCE;
    }
  }

  static JsonElement decodeBuiltinDataTypeMatrix(Matrix matrix) {
    return matrix
        .getDataType()
        .map(
            dataType ->
                decodeBuiltinDataTypeMatrix(
                    matrix.getElements(), dataType, matrix.getDimensions(), 0))
        .orElse(JsonNull.INSTANCE);
  }

  private static JsonElement decodeBuiltinDataTypeMatrix(
      Object flatArray, OpcUaDataType dataType, int[] dimensions, int offset) {
    var jsonArray = new JsonArray();

    if (dimensions.length == 1) {
      for (int i = 0; i < dimensions[0]; i++) {
        jsonArray.add(JsonConversions.from(Array.get(flatArray, offset + i), dataType));
      }
    } else {
      int[] dimensionsTail = Arrays.copyOfRange(dimensions, 1, dimensions.length);

      for (int i = 0; i < dimensions[0]; i++) {
        JsonElement e =
            decodeBuiltinDataTypeMatrix(
                flatArray,
                dataType,
                dimensionsTail,
                offset + i * Arrays.stream(dimensionsTail).reduce(1, (a, b) -> a * b));
        jsonArray.add(e);
      }
    }

    return jsonArray;
  }

  static JsonElement decodeEnumMatrix(Matrix matrix) {
    return decodeBuiltinDataTypeMatrix(
        matrix.getElements(), OpcUaDataType.Int32, matrix.getDimensions(), 0);
  }

  static JsonElement decodeStructMatrix(EncodingContext context, Matrix matrix, boolean subtyped) {
    return decodeStructMatrix(context, matrix.getElements(), subtyped, matrix.getDimensions(), 0);
  }

  private static JsonElement decodeStructMatrix(
      EncodingContext context,
      Object flatArray,
      boolean decodeExtensionObject,
      int[] dimensions,
      int offset) {

    var jsonArray = new JsonArray();

    if (dimensions.length == 1) {
      for (int i = 0; i < dimensions[0]; i++) {
        Object value = Array.get(flatArray, offset + i);

        if (decodeExtensionObject) {
          ExtensionObject xo = (ExtensionObject) value;
          JsonStruct struct = (JsonStruct) xo.decode(context);
          jsonArray.add(struct.getJsonObject());
        } else {
          JsonStruct struct = (JsonStruct) value;
          jsonArray.add(struct.getJsonObject());
        }
      }
    } else {
      int[] dimensionsTail = Arrays.copyOfRange(dimensions, 1, dimensions.length);

      for (int i = 0; i < dimensions[0]; i++) {
        JsonElement e =
            decodeStructMatrix(
                context,
                flatArray,
                decodeExtensionObject,
                dimensionsTail,
                offset + i * Arrays.stream(dimensionsTail).reduce(1, (a, b) -> a * b));
        jsonArray.add(e);
      }
    }

    return jsonArray;
  }

  // endregion

  // region Encoding

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, JsonStruct value)
      throws UaSerializationException {
    switch (definition.getStructureType()) {
      case Structure:
      case StructureWithOptionalFields:
      case StructureWithSubtypedValues:
        encodeStruct(encoder, value);
        break;

      case Union:
      case UnionWithSubtypedValues:
        encodeUnion(encoder, value);
        break;

      default:
        throw new IllegalArgumentException(
            "unsupported structure type: " + definition.getStructureType());
    }
  }

  private void encodeStruct(UaEncoder encoder, JsonStruct value) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    var switchField = 0L;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (field.getIsOptional() && value.getJsonObject().has(requireNonNull(field.getName()))) {
          switchField = switchField | (1L << optionalFieldIndex++);
        }
      }
      encoder.encodeUInt32("SwitchField", UInteger.valueOf(switchField));
    }

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || ((switchField >>> optionalFieldIndex++) & 1L) == 1L) {
          JsonElement fieldValue = value.getJsonObject().get(requireNonNull(field.getName()));
          encodeFieldValue(encoder, field, fieldValue);
        }
      }
    } else {
      for (StructureField field : fields) {
        JsonElement fieldValue = value.getJsonObject().get(requireNonNull(field.getName()));
        encodeFieldValue(encoder, field, fieldValue);
      }
    }
  }

  private void encodeUnion(UaEncoder encoder, JsonStruct value) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    for (int i = 0; i < fields.length; i++) {
      StructureField field = fields[i];

      if (value.getJsonObject().has(requireNonNull(field.getName()))) {
        encoder.encodeUInt32("SwitchValue", UInteger.valueOf(i + 1));
        JsonElement fieldValue = value.getJsonObject().get(requireNonNull(field.getName()));
        encodeFieldValue(encoder, field, fieldValue);

        // Return as soon as a field has been encoded.
        // Unions are only one field, indicated by SwitchValue.
        return;
      }
    }

    // No field was found, so the union is null/empty.
    encoder.encodeUInt32("SwitchValue", UInteger.valueOf(0));
  }

  private void encodeFieldValue(UaEncoder encoder, StructureField field, JsonElement value) {
    String fieldName = requireNonNull(field.getName());
    NodeId dataTypeId = field.getDataType();

    if (field.getValueRank() == -1) {
      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        encodeBuiltinDataType(encoder, fieldName, (OpcUaDataType) hint, value);
      } else if (hint instanceof EnumHint) {
        encoder.encodeEnum(fieldName, new JsonEnumWrapper(value.getAsInt(), dataTypeId.expanded()));
      } else if (hint instanceof StructHint) {
        JsonObject jsonObject = value.getAsJsonObject();

        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          JsonObject metadata = jsonObject.getAsJsonObject("__metadata");
          NodeId concreteDataTypeId =
              NodeId.parse(metadata.getAsJsonPrimitive("dataTypeId").getAsString());
          var struct = new JsonStruct(dataTypeTree.getDataType(concreteDataTypeId), jsonObject);
          var xo = ExtensionObject.encode(encoder.getEncodingContext(), struct);
          encoder.encodeExtensionObject(fieldName, xo);
        } else {
          var struct = new JsonStruct(dataTypeTree.getDataType(dataTypeId), jsonObject);
          encoder.encodeStruct(fieldName, struct, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else if (field.getValueRank() == 1) {
      JsonArray jsonArray = value.getAsJsonArray();

      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        encodeBuiltinDataTypeArray(encoder, fieldName, (OpcUaDataType) hint, jsonArray);
      } else if (hint instanceof EnumHint) {
        JsonEnumWrapper[] enumValues = new JsonEnumWrapper[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
          enumValues[i] = new JsonEnumWrapper(jsonArray.get(i).getAsInt(), dataTypeId.expanded());
        }
        encoder.encodeEnumArray(fieldName, enumValues);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          var xoArray = new ExtensionObject[jsonArray.size()];

          NodeId concreteDataTypeId = dataTypeId;
          if (!jsonArray.isEmpty()) {
            JsonObject metadata = jsonArray.get(0).getAsJsonObject().getAsJsonObject("__metadata");
            String id = metadata.getAsJsonPrimitive("dataTypeId").getAsString();
            concreteDataTypeId = NodeId.parse(id);
          }

          for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            var struct = new JsonStruct(dataTypeTree.getDataType(concreteDataTypeId), jsonObject);
            xoArray[i] = ExtensionObject.encode(encoder.getEncodingContext(), struct);
          }

          encoder.encodeExtensionObjectArray(fieldName, xoArray);
        } else {
          var structArray = new JsonStruct[jsonArray.size()];

          for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            structArray[i] = new JsonStruct(dataTypeTree.getDataType(dataTypeId), jsonObject);
          }

          encoder.encodeStructArray(fieldName, structArray, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else if (field.getValueRank() > 1) {
      JsonArray jsonArray = value.getAsJsonArray();

      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        Object[] flatArray = encodeBuiltinDataTypeMatrix((OpcUaDataType) hint, jsonArray);
        var matrix = new Matrix(flatArray, getDimensions(jsonArray), (OpcUaDataType) hint);
        encoder.encodeMatrix(fieldName, matrix);
      } else if (hint instanceof EnumHint) {
        Object[] flatArray = encodeEnumMatrix(dataTypeId.expanded(), jsonArray);
        var matrix = new Matrix(flatArray, getDimensions(jsonArray), OpcUaDataType.Int32);
        encoder.encodeEnumMatrix(fieldName, matrix);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          Object[] flatArray =
              encodeStructMatrix(encoder.getEncodingContext(), dataTypeTree, jsonArray, true);
          var matrix =
              new Matrix(flatArray, getDimensions(jsonArray), OpcUaDataType.ExtensionObject);
          encoder.encodeMatrix(fieldName, matrix);
        } else {
          Object[] flatArray =
              encodeStructMatrix(encoder.getEncodingContext(), dataTypeTree, jsonArray, false);
          var matrix =
              new Matrix(flatArray, getDimensions(jsonArray), OpcUaDataType.ExtensionObject);
          encoder.encodeStructMatrix(fieldName, matrix, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + hint);
      }
    } else {
      throw new IllegalArgumentException("unsupported value rank: " + field.getValueRank());
    }
  }

  private void encodeBuiltinDataType(
      UaEncoder encoder, String fieldName, OpcUaDataType dataType, JsonElement value) {
    switch (dataType) {
      case Boolean:
        encoder.encodeBoolean(fieldName, JsonConversions.toBoolean(value));
        break;
      case SByte:
        encoder.encodeSByte(fieldName, JsonConversions.toSByte(value));
        break;
      case Byte:
        encoder.encodeByte(fieldName, JsonConversions.toByte(value));
        break;
      case Int16:
        encoder.encodeInt16(fieldName, JsonConversions.toInt16(value));
        break;
      case UInt16:
        encoder.encodeUInt16(fieldName, JsonConversions.toUInt16(value));
        break;
      case Int32:
        encoder.encodeInt32(fieldName, JsonConversions.toInt32(value));
        break;
      case UInt32:
        encoder.encodeUInt32(fieldName, JsonConversions.toUInt32(value));
        break;
      case Int64:
        encoder.encodeInt64(fieldName, JsonConversions.toInt64(value));
        break;
      case UInt64:
        encoder.encodeUInt64(fieldName, JsonConversions.toUInt64(value));
        break;
      case Float:
        encoder.encodeFloat(fieldName, JsonConversions.toFloat(value));
        break;
      case Double:
        encoder.encodeDouble(fieldName, JsonConversions.toDouble(value));
        break;
      case String:
        encoder.encodeString(fieldName, JsonConversions.toString(value));
        break;
      case DateTime:
        encoder.encodeDateTime(fieldName, JsonConversions.toDateTime(value));
        break;
      case Guid:
        encoder.encodeGuid(fieldName, JsonConversions.toGuid(value));
        break;
      case ByteString:
        encoder.encodeByteString(fieldName, JsonConversions.toByteString(value));
        break;
      case XmlElement:
        encoder.encodeXmlElement(fieldName, JsonConversions.toXmlElement(value));
        break;
      case NodeId:
        encoder.encodeNodeId(fieldName, JsonConversions.toNodeId(value));
        break;
      case ExpandedNodeId:
        encoder.encodeExpandedNodeId(fieldName, JsonConversions.toExpandedNodeId(value));
        break;
      case StatusCode:
        encoder.encodeStatusCode(fieldName, JsonConversions.toStatusCode(value));
        break;
      case QualifiedName:
        encoder.encodeQualifiedName(fieldName, JsonConversions.toQualifiedName(value));
        break;
      case LocalizedText:
        encoder.encodeLocalizedText(fieldName, JsonConversions.toLocalizedText(value));
        break;
      case ExtensionObject:
        encoder.encodeExtensionObject(fieldName, JsonConversions.toExtensionObject(value));
        break;
      case DataValue:
        encoder.encodeDataValue(fieldName, JsonConversions.toDataValue(value));
        break;
      case Variant:
        encoder.encodeVariant(fieldName, JsonConversions.toVariant(value));
        break;

      case DiagnosticInfo:
      default:
        throw new IllegalArgumentException("Unsupported BuiltinDataType: " + dataType);
    }
  }

  private void encodeBuiltinDataTypeArray(
      UaEncoder encoder, String fieldName, OpcUaDataType dataType, JsonArray value) {
    switch (dataType) {
      case Boolean:
        {
          Boolean[] array = new Boolean[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toBoolean(value.get(i));
          }
          encoder.encodeBooleanArray(fieldName, array);
          break;
        }
      case SByte:
        {
          Byte[] array = new Byte[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toSByte(value.get(i));
          }
          encoder.encodeSByteArray(fieldName, array);
          break;
        }
      case Byte:
        {
          UByte[] array = new UByte[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toByte(value.get(i));
          }
          encoder.encodeByteArray(fieldName, array);
          break;
        }
      case Int16:
        {
          Short[] array = new Short[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toInt16(value.get(i));
          }
          encoder.encodeInt16Array(fieldName, array);
          break;
        }
      case UInt16:
        {
          UShort[] array = new UShort[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toUInt16(value.get(i));
          }
          encoder.encodeUInt16Array(fieldName, array);
          break;
        }
      case Int32:
        {
          Integer[] array = new Integer[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toInt32(value.get(i));
          }
          encoder.encodeInt32Array(fieldName, array);
          break;
        }
      case UInt32:
        {
          UInteger[] array = new UInteger[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toUInt32(value.get(i));
          }
          encoder.encodeUInt32Array(fieldName, array);
          break;
        }
      case Int64:
        {
          Long[] array = new Long[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toInt64(value.get(i));
          }
          encoder.encodeInt64Array(fieldName, array);
          break;
        }
      case UInt64:
        {
          ULong[] array = new ULong[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toUInt64(value.get(i));
          }
          encoder.encodeUInt64Array(fieldName, array);
          break;
        }
      case Float:
        {
          Float[] array = new Float[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toFloat(value.get(i));
          }
          encoder.encodeFloatArray(fieldName, array);
          break;
        }
      case Double:
        {
          Double[] array = new Double[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toDouble(value.get(i));
          }
          encoder.encodeDoubleArray(fieldName, array);
          break;
        }
      case String:
        {
          String[] array = new String[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toString(value.get(i));
          }
          encoder.encodeStringArray(fieldName, array);
          break;
        }
      case DateTime:
        {
          DateTime[] array = new DateTime[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toDateTime(value.get(i));
          }
          encoder.encodeDateTimeArray(fieldName, array);
          break;
        }
      case Guid:
        {
          UUID[] array = new UUID[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toGuid(value.get(i));
          }
          encoder.encodeGuidArray(fieldName, array);
          break;
        }
      case ByteString:
        {
          ByteString[] array = new ByteString[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toByteString(value.get(i));
          }
          encoder.encodeByteStringArray(fieldName, array);
          break;
        }
      case XmlElement:
        {
          XmlElement[] array = new XmlElement[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toXmlElement(value.get(i));
          }
          encoder.encodeXmlElementArray(fieldName, array);
          break;
        }
      case NodeId:
        {
          NodeId[] array = new NodeId[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toNodeId(value.get(i));
          }
          encoder.encodeNodeIdArray(fieldName, array);
          break;
        }
      case ExpandedNodeId:
        {
          ExpandedNodeId[] array = new ExpandedNodeId[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toExpandedNodeId(value.get(i));
          }
          encoder.encodeExpandedNodeIdArray(fieldName, array);
          break;
        }
      case StatusCode:
        {
          StatusCode[] array = new StatusCode[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toStatusCode(value.get(i));
          }
          encoder.encodeStatusCodeArray(fieldName, array);
          break;
        }
      case QualifiedName:
        {
          QualifiedName[] array = new QualifiedName[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toQualifiedName(value.get(i));
          }
          encoder.encodeQualifiedNameArray(fieldName, array);
          break;
        }
      case LocalizedText:
        {
          LocalizedText[] array = new LocalizedText[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toLocalizedText(value.get(i));
          }
          encoder.encodeLocalizedTextArray(fieldName, array);
          break;
        }
      case ExtensionObject:
        {
          ExtensionObject[] array = new ExtensionObject[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toExtensionObject(value.get(i));
          }
          encoder.encodeExtensionObjectArray(fieldName, array);
          break;
        }
      case DataValue:
        {
          DataValue[] array = new DataValue[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toDataValue(value.get(i));
          }
          encoder.encodeDataValueArray(fieldName, array);
          break;
        }
      case Variant:
        {
          Variant[] array = new Variant[value.size()];
          for (int i = 0; i < value.size(); i++) {
            array[i] = JsonConversions.toVariant(value.get(i));
          }
          encoder.encodeVariantArray(fieldName, array);
          break;
        }

      case DiagnosticInfo:
      default:
        throw new IllegalArgumentException("Unsupported BuiltinDataType: " + dataType);
    }
  }

  static Object[] encodeBuiltinDataTypeMatrix(OpcUaDataType dataType, JsonArray jsonArray) {
    var elements = new ArrayList<>();

    for (int i = 0; i < jsonArray.size(); i++) {
      var element = jsonArray.get(i);
      if (element.isJsonArray()) {
        Collections.addAll(
            elements, encodeBuiltinDataTypeMatrix(dataType, element.getAsJsonArray()));
      } else {
        elements.add(JsonConversions.to(element, dataType));
      }
    }

    return elements.toArray();
  }

  static Object[] encodeEnumMatrix(ExpandedNodeId dataTypeId, JsonArray jsonArray) {
    var elements = new ArrayList<>();

    for (int i = 0; i < jsonArray.size(); i++) {
      var element = jsonArray.get(i);
      if (element.isJsonArray()) {
        Collections.addAll(elements, encodeEnumMatrix(dataTypeId, element.getAsJsonArray()));
      } else {
        var wrapper = new JsonEnumWrapper(element.getAsInt(), dataTypeId);
        elements.add(wrapper);
      }
    }

    return elements.toArray();
  }

  static Object[] encodeStructMatrix(
      EncodingContext context,
      DataTypeTree dataTypeTree,
      JsonArray jsonArray,
      boolean encodeExtensionObject) {

    var elements = new ArrayList<>();

    DataType dataType = null;

    for (int i = 0; i < jsonArray.size(); i++) {
      var element = jsonArray.get(i);
      if (element.isJsonArray()) {
        Collections.addAll(
            elements,
            encodeStructMatrix(
                context, dataTypeTree, element.getAsJsonArray(), encodeExtensionObject));
      } else {
        if (dataType == null) {
          JsonObject metadata = element.getAsJsonObject().getAsJsonObject("__metadata");
          String dataTypeId = metadata.getAsJsonPrimitive("dataTypeId").getAsString();
          dataType = dataTypeTree.getDataType(NodeId.parse(dataTypeId));
        }

        var struct = new JsonStruct(dataType, element.getAsJsonObject());

        if (encodeExtensionObject) {
          var xo = ExtensionObject.encode(context, struct);
          elements.add(xo);
        } else {
          elements.add(struct);
        }
      }
    }

    return elements.toArray();
  }

  static int[] getDimensions(JsonArray array) {
    var dimensions = new ArrayList<Integer>();
    dimensions.add(array.size());
    while (!array.isEmpty() && array.get(0).isJsonArray()) {
      array = array.get(0).getAsJsonArray();
      dimensions.add(array.size());
    }
    return dimensions.stream().mapToInt(i -> i).toArray();
  }

  // endregion

  private @Nullable Object getHint(StructureField field) {
    Map<StructureField, Object> hints =
        this.hints.get(
            () -> {
              Map<StructureField, Object> map = new HashMap<>();

              StructureField[] fields =
                  requireNonNullElse(definition.getFields(), new StructureField[0]);

              for (StructureField f : fields) {
                NodeId dataTypeId = f.getDataType();
                if (OpcUaDataType.isBuiltin(dataTypeId)) {
                  map.put(f, OpcUaDataType.fromNodeId(dataTypeId));
                } else if (dataTypeTree.isEnumType(dataTypeId)) {
                  map.put(f, new EnumHint());
                } else if (dataTypeTree.isStructType(dataTypeId)) {
                  map.put(f, new StructHint());
                } else {
                  // alias/simple type, find the builtin parent
                  map.put(f, dataTypeTree.getBuiltinType(dataTypeId));
                }
              }

              return map;
            });

    return hints.get(field);
  }

  private static class EnumHint {}

  private static class StructHint {}

  private static class JsonEnumWrapper implements UaEnumeratedType {
    private final int value;
    private final ExpandedNodeId typeId;

    JsonEnumWrapper(int value, ExpandedNodeId typeId) {
      this.value = value;
      this.typeId = typeId;
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return typeId;
    }

    @Override
    public int getValue() {
      return value;
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;
      JsonEnumWrapper that = (JsonEnumWrapper) object;
      return getValue() == that.getValue() && Objects.equals(getTypeId(), that.getTypeId());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getValue(), getTypeId());
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", JsonEnumWrapper.class.getSimpleName() + "[", "]")
          .add("value=" + value)
          .add("typeId=" + typeId)
          .toString();
    }
  }
}
