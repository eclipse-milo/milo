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
  private final String[] optionalFieldNames;
  private final String[] fieldNames;

  private final DataType dataType;
  private final DataTypeTree dataTypeTree;

  public JsonStructCodec(DataType dataType, DataTypeTree dataTypeTree) {
    this.dataType = dataType;
    this.dataTypeTree = dataTypeTree;

    definition = (StructureDefinition) dataType.getDataTypeDefinition();
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);
    optionalFieldNames =
        Arrays.stream(fields)
            .filter(StructureField::getIsOptional)
            .map(StructureField::getName)
            .toArray(String[]::new);
    fieldNames = Arrays.stream(fields).map(StructureField::getName).toArray(String[]::new);
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

    var encodingMask = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      encodingMask = decoder.decodeEncodingMask(optionalFieldNames).longValue();
    }

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || (encodingMask >>> optionalFieldIndex++ & 1L) == 1L) {
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
    int switchField = decoder.decodeSwitchField(fieldNames).intValue();
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (switchField == 0) {
      var jsonObject = new JsonObject();

      var metadata = new JsonObject();
      metadata.addProperty("dataTypeId", dataType.getNodeId().toParseableString());
      jsonObject.add("__metadata", metadata);

      return new JsonStruct(dataType, jsonObject);
    } else if (switchField > 0 && switchField <= fields.length) {
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
        Integer[] values = decoder.decodeEnumArray(fieldName);
        if (values == null) return JsonNull.INSTANCE;
        var array = new JsonArray();
        for (int value : values) {
          array.add(value);
        }
        return array;
      } else if (hint instanceof StructHint) {
        var array = new JsonArray();

        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          ExtensionObject[] values = decoder.decodeExtensionObjectArray(fieldName);
          if (values == null) return JsonNull.INSTANCE;
          for (ExtensionObject value : values) {
            JsonStruct struct =
                value == null ? null : (JsonStruct) value.decode(decoder.getEncodingContext());
            array.add(struct == null ? JsonNull.INSTANCE : struct.getJsonObject());
          }
        } else {
          Object[] values = decoder.decodeStructArray(fieldName, dataTypeId);
          if (values == null) return JsonNull.INSTANCE;
          for (Object o : values) {
            JsonStruct struct = (JsonStruct) o;
            array.add(struct == null ? JsonNull.INSTANCE : struct.getJsonObject());
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
    Object[] values =
        switch (dataType) {
          case Boolean -> decoder.decodeBooleanArray(fieldName);
          case SByte -> decoder.decodeSByteArray(fieldName);
          case Byte -> decoder.decodeByteArray(fieldName);
          case Int16 -> decoder.decodeInt16Array(fieldName);
          case UInt16 -> decoder.decodeUInt16Array(fieldName);
          case Int32 -> decoder.decodeInt32Array(fieldName);
          case UInt32 -> decoder.decodeUInt32Array(fieldName);
          case Int64 -> decoder.decodeInt64Array(fieldName);
          case UInt64 -> decoder.decodeUInt64Array(fieldName);
          case Float -> decoder.decodeFloatArray(fieldName);
          case Double -> decoder.decodeDoubleArray(fieldName);
          case String -> decoder.decodeStringArray(fieldName);
          case DateTime -> decoder.decodeDateTimeArray(fieldName);
          case Guid -> decoder.decodeGuidArray(fieldName);
          case ByteString -> decoder.decodeByteStringArray(fieldName);
          case XmlElement -> decoder.decodeXmlElementArray(fieldName);
          case NodeId -> decoder.decodeNodeIdArray(fieldName);
          case ExpandedNodeId -> decoder.decodeExpandedNodeIdArray(fieldName);
          case StatusCode -> decoder.decodeStatusCodeArray(fieldName);
          case QualifiedName -> decoder.decodeQualifiedNameArray(fieldName);
          case LocalizedText -> decoder.decodeLocalizedTextArray(fieldName);
          case ExtensionObject -> decoder.decodeExtensionObjectArray(fieldName);
          case DataValue -> decoder.decodeDataValueArray(fieldName);
          case Variant -> decoder.decodeVariantArray(fieldName);
          default -> throw new IllegalArgumentException("Unsupported BuiltinDataType: " + dataType);
        };
    if (values == null) return JsonNull.INSTANCE;
    var array = new JsonArray();
    for (Object value : values) {
      array.add(JsonConversions.from(value, dataType));
    }
    return array;
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

    var encodingMask = 0L;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (field.getIsOptional()) {
          if (value.getJsonObject().has(requireNonNull(field.getName()))) {
            encodingMask |= 1L << optionalFieldIndex;
          }
          optionalFieldIndex++;
        }
      }
      encoder.encodeEncodingMask(UInteger.valueOf(encodingMask));
    }

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || ((encodingMask >>> optionalFieldIndex++) & 1L) == 1L) {
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
        encoder.encodeSwitchField(UInteger.valueOf(i + 1));
        JsonElement fieldValue = value.getJsonObject().get(requireNonNull(field.getName()));
        encodeFieldValue(encoder, field, fieldValue);

        // Return as soon as a field has been encoded.
        // Unions are only one field, indicated by SwitchField.
        return;
      }
    }

    // No field was found, so the union is null/empty.
    encoder.encodeSwitchField(UInteger.valueOf(0));
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
      JsonArray jsonArray = value == null || value.isJsonNull() ? null : value.getAsJsonArray();

      Object hint = getHint(field);
      if (hint instanceof OpcUaDataType) {
        encodeBuiltinDataTypeArray(encoder, fieldName, (OpcUaDataType) hint, jsonArray);
      } else if (hint instanceof EnumHint) {
        if (jsonArray == null) {
          encoder.encodeEnumArray(fieldName, null);
          return;
        }
        JsonEnumWrapper[] enumValues = new JsonEnumWrapper[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
          enumValues[i] = new JsonEnumWrapper(jsonArray.get(i).getAsInt(), dataTypeId.expanded());
        }
        encoder.encodeEnumArray(fieldName, enumValues);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          if (jsonArray == null) {
            encoder.encodeExtensionObjectArray(fieldName, null);
            return;
          }
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
          if (jsonArray == null) {
            encoder.encodeStructArray(fieldName, null, dataTypeId);
            return;
          }
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
        var matrix =
            new Matrix(
                flatArray, getDimensions(jsonArray), OpcUaDataType.Int32, dataTypeId.expanded());
        encoder.encodeEnumMatrix(fieldName, matrix);
      } else if (hint instanceof StructHint) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
          Object[] flatArray =
              encodeStructMatrix(encoder.getEncodingContext(), dataTypeTree, jsonArray, true);
          var matrix =
              new Matrix(
                  flatArray,
                  getDimensions(jsonArray),
                  OpcUaDataType.ExtensionObject,
                  dataTypeId.expanded());
          encoder.encodeMatrix(fieldName, matrix);
        } else {
          Object[] flatArray =
              encodeStructMatrix(encoder.getEncodingContext(), dataTypeTree, jsonArray, false);
          var matrix =
              new Matrix(
                  flatArray,
                  getDimensions(jsonArray),
                  OpcUaDataType.ExtensionObject,
                  dataTypeId.expanded());
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
        encoder.encodeString(
            fieldName,
            value == null || value.isJsonNull() ? null : JsonConversions.toString(value));
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
      UaEncoder encoder, String fieldName, OpcUaDataType dataType, @Nullable JsonArray value) {
    Object[] values = null;
    if (value != null) {
      values = (Object[]) Array.newInstance(dataType.getBackingClass(), value.size());
      for (int i = 0; i < value.size(); i++) {
        JsonElement element = value.get(i);
        values[i] = element.isJsonNull() ? null : JsonConversions.to(element, dataType);
      }
    }
    switch (dataType) {
      case Boolean -> encoder.encodeBooleanArray(fieldName, (Boolean[]) values);
      case SByte -> encoder.encodeSByteArray(fieldName, (Byte[]) values);
      case Byte -> encoder.encodeByteArray(fieldName, (UByte[]) values);
      case Int16 -> encoder.encodeInt16Array(fieldName, (Short[]) values);
      case UInt16 -> encoder.encodeUInt16Array(fieldName, (UShort[]) values);
      case Int32 -> encoder.encodeInt32Array(fieldName, (Integer[]) values);
      case UInt32 -> encoder.encodeUInt32Array(fieldName, (UInteger[]) values);
      case Int64 -> encoder.encodeInt64Array(fieldName, (Long[]) values);
      case UInt64 -> encoder.encodeUInt64Array(fieldName, (ULong[]) values);
      case Float -> encoder.encodeFloatArray(fieldName, (Float[]) values);
      case Double -> encoder.encodeDoubleArray(fieldName, (Double[]) values);
      case String -> encoder.encodeStringArray(fieldName, (String[]) values);
      case DateTime -> encoder.encodeDateTimeArray(fieldName, (DateTime[]) values);
      case Guid -> encoder.encodeGuidArray(fieldName, (UUID[]) values);
      case ByteString -> encoder.encodeByteStringArray(fieldName, (ByteString[]) values);
      case XmlElement -> encoder.encodeXmlElementArray(fieldName, (XmlElement[]) values);
      case NodeId -> encoder.encodeNodeIdArray(fieldName, (NodeId[]) values);
      case ExpandedNodeId ->
          encoder.encodeExpandedNodeIdArray(fieldName, (ExpandedNodeId[]) values);
      case StatusCode -> encoder.encodeStatusCodeArray(fieldName, (StatusCode[]) values);
      case QualifiedName -> encoder.encodeQualifiedNameArray(fieldName, (QualifiedName[]) values);
      case LocalizedText -> encoder.encodeLocalizedTextArray(fieldName, (LocalizedText[]) values);
      case ExtensionObject ->
          encoder.encodeExtensionObjectArray(fieldName, (ExtensionObject[]) values);
      case DataValue -> encoder.encodeDataValueArray(fieldName, (DataValue[]) values);
      case Variant -> encoder.encodeVariantArray(fieldName, (Variant[]) values);
      default -> throw new IllegalArgumentException("Unsupported BuiltinDataType: " + dataType);
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
    public @Nullable String getName() {
      return null;
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
