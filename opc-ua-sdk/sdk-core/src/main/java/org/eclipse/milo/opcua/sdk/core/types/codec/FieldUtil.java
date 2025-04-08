/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types.codec;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.types.DynamicEnumType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
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
import org.slf4j.LoggerFactory;

/** Field serialization logic shared by {@link DynamicStructCodec} and {@link DynamicUnionCodec}. */
class FieldUtil {

  private FieldUtil() {}

  static Object decodeFieldValue(
      UaDecoder decoder,
      StructureDefinition definition,
      StructureField field,
      Map<StructureField, FieldHint> fieldHints) {

    Object value = _decodeFieldValue(decoder, definition, field, fieldHints);

    if (value instanceof Variant v && v.getValue() != null) {
      value = Variant.of(maybeEagerlyDecodeValue(decoder, field, v.getValue()));
    } else {
      value = maybeEagerlyDecodeValue(decoder, field, value);
    }

    return value;
  }

  /**
   * Eagerly decode a value if it is an ExtensionObject, array of ExtensionObject, or Matrix of
   * ExtensionObject.
   *
   * @param decoder the {@link UaDecoder} to use for decoding.
   * @param field the {@link StructureField} to decode.
   * @param value the value to decode.
   * @return the decoded value, or the original value if it cannot be decoded or is not an
   *     ExtensionObject.
   */
  private static Object maybeEagerlyDecodeValue(
      UaDecoder decoder, StructureField field, Object value) {

    try {
      if (value instanceof ExtensionObject xo) {
        return xo.decode(decoder.getEncodingContext());
      } else if (value instanceof ExtensionObject[] xos) {
        Object decodedArray = null;
        for (int i = 0; i < xos.length; i++) {
          Object decoded = xos[i].decode(decoder.getEncodingContext());
          if (decodedArray == null) {
            decodedArray = Array.newInstance(decoded.getClass(), xos.length);
          }
          Array.set(decodedArray, i, decoded);
        }
        if (decodedArray != null) {
          return decodedArray;
        }
      } else if (value instanceof Matrix matrix) {
        Class<?> elementType = matrix.getElementType().orElse(Object.class);

        if (elementType == ExtensionObject.class) {
          return matrix.transform(o -> ((ExtensionObject) o).decode(decoder.getEncodingContext()));
        }
      }
    } catch (Exception e) {
      // This is okay - ExtensionObjects can be opaque and contain an encoded type we
      // don't know about for some reason.
      LoggerFactory.getLogger(FieldUtil.class)
          .debug(
              "Failed to decode ExtensionObject for field {} of type {}",
              field.getName(),
              field.getDataType());
    }

    return value;
  }

  private static Object _decodeFieldValue(
      UaDecoder decoder,
      StructureDefinition definition,
      StructureField field,
      Map<StructureField, FieldHint> fieldHints) {

    String fieldName = requireNonNull(field.getName());
    NodeId dataTypeId = field.getDataType();

    FieldHint fieldHint = fieldHints.get(field);

    // Note: shall be scalar or fixed dimension
    Integer valueRank = field.getValueRank();

    if (valueRank == -1) {
      if (fieldHint instanceof FieldHint.Builtin hint) {
        return decodeBuiltinDataType(decoder, fieldName, hint.dataType);
      } else if (fieldHint instanceof FieldHint.Enum hint) {
        Integer enumValue = decoder.decodeEnum(fieldName);

        return new DynamicEnumType(hint.dataType, enumValue);
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          ExtensionObject xo = decoder.decodeExtensionObject(fieldName);

          return xo.decode(decoder.getEncodingContext());
        } else {
          return decoder.decodeStruct(fieldName, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else if (valueRank == 1) {
      if (fieldHint instanceof FieldHint.Builtin hint) {
        return decodeBuiltinDataTypeArray(decoder, fieldName, hint.dataType);
      } else if (fieldHint instanceof FieldHint.Enum hint) {
        Integer[] enumValues = decoder.decodeEnumArray(fieldName);

        return Arrays.stream(enumValues)
            .map(value -> new DynamicEnumType(hint.dataType, value))
            .toArray(DynamicEnumType[]::new);
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          ExtensionObject[] xos = decoder.decodeExtensionObjectArray(fieldName);

          return Arrays.stream(xos)
              .map(xo -> (DynamicStructType) xo.decode(decoder.getEncodingContext()))
              .toArray(DynamicStructType[]::new);
        } else {
          return decoder.decodeStructArray(fieldName, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else if (valueRank > 1) {
      if (fieldHint instanceof FieldHint.Builtin hint) {
        return decoder.decodeMatrix(fieldName, hint.dataType);
      } else if (fieldHint instanceof FieldHint.Enum hint) {
        Matrix matrix = decoder.decodeEnumMatrix(fieldName);

        return matrix.transform(v -> new DynamicEnumType(hint.dataType, (Integer) v));
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          Matrix matrix = decoder.decodeMatrix(fieldName, OpcUaDataType.ExtensionObject);

          return matrix.transform(
              o -> {
                ExtensionObject xo = (ExtensionObject) o;
                return xo.decode(decoder.getEncodingContext());
              });
        } else {
          return decoder.decodeStructMatrix(fieldName, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "illegal ValueRank: " + valueRank);
    }
  }

  static void encodeFieldValue(
      UaEncoder encoder,
      StructureDefinition definition,
      StructureField field,
      Map<StructureField, FieldHint> fieldHints,
      Object value) {

    String fieldName = requireNonNull(field.getName());
    NodeId dataTypeId = field.getDataType();

    FieldHint fieldHint = fieldHints.get(field);

    // Note: shall be scalar or fixed dimension
    Integer valueRank = field.getValueRank();

    if (valueRank == -1) {
      if (fieldHint instanceof FieldHint.Builtin hint) {
        if (hint.dataType == OpcUaDataType.ExtensionObject
            && value instanceof UaStructuredType structuredType) {

          ExtensionObject xo = ExtensionObject.encode(encoder.getEncodingContext(), structuredType);

          encodeBuiltinDataType(encoder, fieldName, hint.dataType, xo);
        } else {
          encodeBuiltinDataType(encoder, fieldName, hint.dataType, value);
        }
      } else if (fieldHint instanceof FieldHint.Enum) {
        encoder.encodeEnum(fieldName, (UaEnumeratedType) value);
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          DynamicStructType structValue = (DynamicStructType) value;
          ExtensionObject xo = ExtensionObject.encode(encoder.getEncodingContext(), structValue);
          encoder.encodeExtensionObject(fieldName, xo);
        } else {
          encoder.encodeStruct(fieldName, (UaStructuredType) value, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else if (valueRank == 1) {
      if (fieldHint instanceof FieldHint.Builtin hint) {
        if (hint.dataType == OpcUaDataType.ExtensionObject
            && value instanceof UaStructuredType[] structuredTypes) {

          ExtensionObject[] xos =
              Arrays.stream(structuredTypes)
                  .map(s -> ExtensionObject.encode(encoder.getEncodingContext(), s))
                  .toArray(ExtensionObject[]::new);

          encodeBuiltinDataTypeArray(encoder, fieldName, hint.dataType, xos);
        } else {
          encodeBuiltinDataTypeArray(encoder, fieldName, hint.dataType, value);
        }
      } else if (fieldHint instanceof FieldHint.Enum) {
        encoder.encodeEnumArray(fieldName, (UaEnumeratedType[]) value);
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          DynamicStructType[] structArray = (DynamicStructType[]) value;

          ExtensionObject[] xoArray =
              Arrays.stream(structArray)
                  .map(s -> ExtensionObject.encode(encoder.getEncodingContext(), s))
                  .toArray(ExtensionObject[]::new);

          encoder.encodeExtensionObjectArray(fieldName, xoArray);
        } else {
          encoder.encodeStructArray(fieldName, (UaStructuredType[]) value, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else if (valueRank > 1) {
      Matrix matrix = (Matrix) value;

      if (fieldHint instanceof FieldHint.Builtin hint) {
        if (hint.dataType == OpcUaDataType.ExtensionObject
            && UaStructuredType.class.isAssignableFrom(
                matrix.getElementType().orElse(Object.class))) {

          Matrix xoMatrix =
              matrix.transform(
                  o -> ExtensionObject.encode(encoder.getEncodingContext(), (UaStructuredType) o));

          encoder.encodeMatrix(fieldName, xoMatrix);
        } else {
          encoder.encodeMatrix(fieldName, matrix);
        }
      } else if (fieldHint instanceof FieldHint.Enum) {
        encoder.encodeEnumMatrix(fieldName, matrix);
      } else if (fieldHint instanceof FieldHint.Struct) {
        if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(definition, field)) {
          Matrix xoMatrix =
              matrix.transform(
                  o -> {
                    DynamicStructType structValue = (DynamicStructType) o;
                    return ExtensionObject.encode(encoder.getEncodingContext(), structValue);
                  });

          encoder.encodeMatrix(fieldName, xoMatrix);
        } else {
          encoder.encodeStructMatrix(fieldName, matrix, dataTypeId);
        }
      } else {
        throw new IllegalArgumentException("hint: " + fieldHint);
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "illegal ValueRank: " + valueRank);
    }
  }

  /**
   * Check if a field allows subtyping.
   *
   * <p>In Structures and Unions this means the field is encoded as an ExtensionObject.
   *
   * @param field the {@link StructureField} to check.
   * @return {@code true} if the field allows subtyping.
   */
  static boolean fieldAllowsSubtyping(StructureDefinition definition, StructureField field) {
    return field.getIsOptional()
        && (definition.getStructureType() == StructureType.StructureWithSubtypedValues
            || definition.getStructureType() == StructureType.UnionWithSubtypedValues);
  }

  static Map<StructureField, FieldHint> createFieldHints(
      StructureDefinition definition, DataTypeTree dataTypeTree) {

    StructureField[] fields =
        Objects.requireNonNullElse(definition.getFields(), new StructureField[0]);

    Map<StructureField, FieldHint> fieldHints = new HashMap<>();

    for (StructureField field : fields) {
      NodeId dataTypeId = field.getDataType();

      FieldHint hint;
      if (OpcUaDataType.isBuiltin(dataTypeId)) {
        OpcUaDataType dataType = requireNonNull(OpcUaDataType.fromNodeId(dataTypeId));
        hint = new FieldHint.Builtin(dataType);
      } else if (dataTypeTree.isEnumType(dataTypeId)) {
        DataType dataType = requireNonNull(dataTypeTree.getDataType(dataTypeId));
        hint = new FieldHint.Enum(dataType);
      } else if (dataTypeTree.isStructType(dataTypeId)) {
        hint = new FieldHint.Struct();
      } else {
        // simple/alias type resolved to its builtin type
        hint = new FieldHint.Builtin(dataTypeTree.getBuiltinType(dataTypeId));
      }
      fieldHints.put(field, hint);
    }

    return fieldHints;
  }

  private static Object decodeBuiltinDataType(
      UaDecoder decoder, String fieldName, OpcUaDataType dataType) {

    return switch (dataType) {
      case Boolean -> decoder.decodeBoolean(fieldName);
      case SByte -> decoder.decodeSByte(fieldName);
      case Byte -> decoder.decodeByte(fieldName);
      case Int16 -> decoder.decodeInt16(fieldName);
      case UInt16 -> decoder.decodeUInt16(fieldName);
      case Int32 -> decoder.decodeInt32(fieldName);
      case UInt32 -> decoder.decodeUInt32(fieldName);
      case Int64 -> decoder.decodeInt64(fieldName);
      case UInt64 -> decoder.decodeUInt64(fieldName);
      case Float -> decoder.decodeFloat(fieldName);
      case Double -> decoder.decodeDouble(fieldName);
      case String -> decoder.decodeString(fieldName);
      case DateTime -> decoder.decodeDateTime(fieldName);
      case Guid -> decoder.decodeGuid(fieldName);
      case ByteString -> decoder.decodeByteString(fieldName);
      case XmlElement -> decoder.decodeXmlElement(fieldName);
      case NodeId -> decoder.decodeNodeId(fieldName);
      case ExpandedNodeId -> decoder.decodeExpandedNodeId(fieldName);
      case StatusCode -> decoder.decodeStatusCode(fieldName);
      case QualifiedName -> decoder.decodeQualifiedName(fieldName);
      case LocalizedText -> decoder.decodeLocalizedText(fieldName);
      case ExtensionObject -> decoder.decodeExtensionObject(fieldName);
      case DataValue -> decoder.decodeDataValue(fieldName);
      case Variant -> decoder.decodeVariant(fieldName);
      case DiagnosticInfo -> decoder.decodeDiagnosticInfo(fieldName);
    };
  }

  private static Object decodeBuiltinDataTypeArray(
      UaDecoder decoder, String fieldName, OpcUaDataType dataType) {

    return switch (dataType) {
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
      case DiagnosticInfo -> decoder.decodeDiagnosticInfoArray(fieldName);
    };
  }

  private static void encodeBuiltinDataType(
      UaEncoder encoder, String fieldName, OpcUaDataType dataType, Object value) {

    switch (dataType) {
      case Boolean:
        encoder.encodeBoolean(fieldName, (Boolean) value);
        break;
      case SByte:
        encoder.encodeSByte(fieldName, (Byte) value);
        break;
      case Byte:
        encoder.encodeByte(fieldName, (UByte) value);
        break;
      case Int16:
        encoder.encodeInt16(fieldName, (Short) value);
        break;
      case UInt16:
        encoder.encodeUInt16(fieldName, (UShort) value);
        break;
      case Int32:
        encoder.encodeInt32(fieldName, (Integer) value);
        break;
      case UInt32:
        encoder.encodeUInt32(fieldName, (UInteger) value);
        break;
      case Int64:
        encoder.encodeInt64(fieldName, (Long) value);
        break;
      case UInt64:
        encoder.encodeUInt64(fieldName, (ULong) value);
        break;
      case Float:
        encoder.encodeFloat(fieldName, (Float) value);
        break;
      case Double:
        encoder.encodeDouble(fieldName, (Double) value);
        break;
      case String:
        encoder.encodeString(fieldName, (String) value);
        break;
      case DateTime:
        encoder.encodeDateTime(fieldName, (DateTime) value);
        break;
      case Guid:
        encoder.encodeGuid(fieldName, (UUID) value);
        break;
      case ByteString:
        encoder.encodeByteString(fieldName, (ByteString) value);
        break;
      case XmlElement:
        encoder.encodeXmlElement(fieldName, (XmlElement) value);
        break;
      case NodeId:
        encoder.encodeNodeId(fieldName, (NodeId) value);
        break;
      case ExpandedNodeId:
        encoder.encodeExpandedNodeId(fieldName, (ExpandedNodeId) value);
        break;
      case StatusCode:
        encoder.encodeStatusCode(fieldName, (StatusCode) value);
        break;
      case QualifiedName:
        encoder.encodeQualifiedName(fieldName, (QualifiedName) value);
        break;
      case LocalizedText:
        encoder.encodeLocalizedText(fieldName, (LocalizedText) value);
        break;
      case ExtensionObject:
        encoder.encodeExtensionObject(fieldName, (ExtensionObject) value);
        break;
      case DataValue:
        encoder.encodeDataValue(fieldName, (DataValue) value);
        break;
      case Variant:
        encoder.encodeVariant(fieldName, (Variant) value);
        break;
      case DiagnosticInfo:
        encoder.encodeDiagnosticInfo(fieldName, (DiagnosticInfo) value);
        break;
      default:
        // Shouldn't happen
        throw new RuntimeException("unhandled BuiltinDataType: " + dataType);
    }
  }

  private static void encodeBuiltinDataTypeArray(
      UaEncoder encoder, String fieldName, OpcUaDataType dataType, Object value) {

    switch (dataType) {
      case Boolean:
        encoder.encodeBooleanArray(fieldName, (Boolean[]) value);
        break;
      case SByte:
        encoder.encodeSByteArray(fieldName, (Byte[]) value);
        break;
      case Byte:
        encoder.encodeByteArray(fieldName, (UByte[]) value);
        break;
      case Int16:
        encoder.encodeInt16Array(fieldName, (Short[]) value);
        break;
      case UInt16:
        encoder.encodeUInt16Array(fieldName, (UShort[]) value);
        break;
      case Int32:
        encoder.encodeInt32Array(fieldName, (Integer[]) value);
        break;
      case UInt32:
        encoder.encodeUInt32Array(fieldName, (UInteger[]) value);
        break;
      case Int64:
        encoder.encodeInt64Array(fieldName, (Long[]) value);
        break;
      case UInt64:
        encoder.encodeUInt64Array(fieldName, (ULong[]) value);
        break;
      case Float:
        encoder.encodeFloatArray(fieldName, (Float[]) value);
        break;
      case Double:
        encoder.encodeDoubleArray(fieldName, (Double[]) value);
        break;
      case String:
        encoder.encodeStringArray(fieldName, (String[]) value);
        break;
      case DateTime:
        encoder.encodeDateTimeArray(fieldName, (DateTime[]) value);
        break;
      case Guid:
        encoder.encodeGuidArray(fieldName, (UUID[]) value);
        break;
      case ByteString:
        encoder.encodeByteStringArray(fieldName, (ByteString[]) value);
        break;
      case XmlElement:
        encoder.encodeXmlElementArray(fieldName, (XmlElement[]) value);
        break;
      case NodeId:
        encoder.encodeNodeIdArray(fieldName, (NodeId[]) value);
        break;
      case ExpandedNodeId:
        encoder.encodeExpandedNodeIdArray(fieldName, (ExpandedNodeId[]) value);
        break;
      case StatusCode:
        encoder.encodeStatusCodeArray(fieldName, (StatusCode[]) value);
        break;
      case QualifiedName:
        encoder.encodeQualifiedNameArray(fieldName, (QualifiedName[]) value);
        break;
      case LocalizedText:
        encoder.encodeLocalizedTextArray(fieldName, (LocalizedText[]) value);
        break;
      case ExtensionObject:
        encoder.encodeExtensionObjectArray(fieldName, (ExtensionObject[]) value);
        break;
      case DataValue:
        encoder.encodeDataValueArray(fieldName, (DataValue[]) value);
        break;
      case Variant:
        encoder.encodeVariantArray(fieldName, (Variant[]) value);
        break;
      case DiagnosticInfo:
        encoder.encodeDiagnosticInfoArray(fieldName, (DiagnosticInfo[]) value);
        break;
      default:
        // Shouldn't happen
        throw new RuntimeException("unhandled BuiltinDataType: " + dataType);
    }
  }

  sealed interface FieldHint permits FieldHint.Builtin, FieldHint.Enum, FieldHint.Struct {

    record Builtin(OpcUaDataType dataType) implements FieldHint {}

    record Enum(DataType dataType) implements FieldHint {}

    record Struct() implements FieldHint {}
  }
}
