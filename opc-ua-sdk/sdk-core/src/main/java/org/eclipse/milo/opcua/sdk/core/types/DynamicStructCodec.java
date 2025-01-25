/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
import org.jspecify.annotations.NonNull;

public class DynamicStructCodec extends GenericDataTypeCodec<DynamicStruct> {

  private final Map<StructureField, Object> fieldHints = new ConcurrentHashMap<>();

  private final Map<NodeId, Function<Integer, DynamicEnum>> enumFactories =
      new ConcurrentHashMap<>();

  private final DataType dataType;
  private final StructureDefinition definition;

  public DynamicStructCodec(DataType dataType, DataTypeTree dataTypeTree) {
    this.dataType = dataType;
    this.definition = (StructureDefinition) dataType.getDataTypeDefinition();

    assert definition != null;

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    for (StructureField field : fields) {
      NodeId dataTypeId = field.getDataType();

      Object hint;
      if (BuiltinDataType.isBuiltin(dataTypeId)) {
        hint = BuiltinDataType.fromNodeId(dataTypeId);
      } else if (dataTypeTree.isEnumType(dataTypeId)) {
        hint = TypeHint.ENUM;

        DataType enumDataType = dataTypeTree.getDataType(dataTypeId);
        enumFactories.put(dataTypeId, DynamicEnum.newInstanceFactory(enumDataType));
      } else if (dataTypeTree.isStructType(dataTypeId)) {
        hint = TypeHint.STRUCT;
      } else {
        hint = dataTypeTree.getBuiltinType(dataTypeId);
      }
      fieldHints.put(field, hint);
    }
  }

  @Override
  public Class<DynamicStruct> getType() {
    return DynamicStruct.class;
  }

  @Override
  public DynamicStruct decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {

    return switch (definition.getStructureType()) {
      case Structure, StructureWithOptionalFields, StructureWithSubtypedValues ->
          decodeStruct(decoder);
      case Union, UnionWithSubtypedValues -> decodeUnion(decoder);
    };
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, DynamicStruct value)
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
            "unhandled StructureType: " + definition.getStructureType());
    }
  }

  private @NonNull DynamicStruct decodeStruct(UaDecoder decoder) {
    LinkedHashMap<String, Object> members = new LinkedHashMap<>();

    long switchField = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      switchField = decoder.decodeUInt32("SwitchField").longValue();
    }

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || (switchField >>> optionalFieldIndex++ & 1L) == 1L) {
          Object value = decodeFieldValue(decoder, field);

          members.put(field.getName(), value);
        }
      }
    } else {
      for (StructureField field : fields) {
        Object value = decodeFieldValue(decoder, field);

        members.put(field.getName(), value);
      }
    }

    return new DynamicStruct(dataType, members);
  }

  private @NonNull DynamicUnion decodeUnion(UaDecoder decoder) {
    int switchField = decoder.decodeUInt32("SwitchField").intValue();

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (switchField == 0) {
      return DynamicUnion.ofNull(dataType);
    } else if (switchField <= fields.length) {
      StructureField field = fields[switchField - 1];

      Object value = decodeFieldValue(decoder, field);

      return DynamicUnion.of(dataType, field.getName(), value);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "invalid Union SwitchField value: " + switchField);
    }
  }

  private void encodeStruct(UaEncoder encoder, DynamicStruct struct) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    var switchField = 0L;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (field.getIsOptional()
            && struct.getMembers().containsKey(requireNonNull(field.getName()))) {
          switchField = switchField | (1L << optionalFieldIndex++);
        }
      }
      encoder.encodeUInt32("SwitchField", UInteger.valueOf(switchField));
    }

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || ((switchField >>> optionalFieldIndex++) & 1L) == 1L) {
          Object value = struct.getMembers().get(field.getName());
          encodeFieldValue(encoder, field, value);
        }
      }
    } else {
      for (StructureField field : fields) {
        Object value = struct.getMembers().get(field.getName());
        encodeFieldValue(encoder, field, value);
      }
    }
  }

  private void encodeUnion(UaEncoder encoder, DynamicStruct struct) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (struct.getMembers().isEmpty()) {
      encoder.encodeUInt32("SwitchValue", UInteger.valueOf(0));
    } else {
      for (int i = 0; i < fields.length; i++) {
        StructureField field = fields[i];
        String fieldName = field.getName();

        if (struct.getMembers().containsKey(fieldName)) {
          encoder.encodeUInt32("SwitchValue", UInteger.valueOf(i + 1));

          Object value = struct.getMembers().get(fieldName);

          encodeFieldValue(encoder, field, value);

          // Return as soon as a field has been encoded.
          // Unions are only one field, indicated by SwitchValue.
          return;
        }
      }

      // struct contained no members or the name didn't match a field name... encoding failure.
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, "no Union value found");
    }
  }

  private Object decodeFieldValue(UaDecoder decoder, StructureField field) {
    String fieldName = field.getName();
    NodeId dataTypeId = field.getDataType();

    // Note: shall be scalar or fixed dimension
    Integer valueRank = field.getValueRank();
    if (valueRank == -1) {
      Object value;

      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        value = decodeBuiltinDataType(decoder, fieldName, (BuiltinDataType) hint);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            {
              Function<Integer, DynamicEnum> factory = enumFactories.get(dataTypeId);
              Integer enumValue = decoder.decodeEnum(fieldName);
              value = factory.apply(enumValue);
              break;
            }
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              ExtensionObject xo = decoder.decodeExtensionObject(fieldName);
              value = xo.decode(decoder.getEncodingContext());
            } else {
              value = decoder.decodeStruct(fieldName, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }

      return value;
    } else if (valueRank == 1) {
      Object value;

      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        value = decodeBuiltinDataTypeArray(decoder, fieldName, (BuiltinDataType) hint);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            {
              Function<Integer, DynamicEnum> factory = enumFactories.get(dataTypeId);
              Integer[] enumValues = decoder.decodeEnumArray(fieldName);
              value = Arrays.stream(enumValues).map(factory).toArray(DynamicEnum[]::new);
              break;
            }
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              ExtensionObject[] xoArray = decoder.decodeExtensionObjectArray(fieldName);

              value =
                  Arrays.stream(xoArray)
                      .map(xo -> (DynamicStruct) xo.decode(decoder.getEncodingContext()))
                      .toArray(DynamicStruct[]::new);
            } else {
              value = decoder.decodeStructArray(fieldName, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }

      return value;
    } else if (valueRank > 1) {
      Object value;

      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType builtinDataType) {
        value = decoder.decodeMatrix(fieldName, builtinDataType);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            {
              Matrix matrix = decoder.decodeEnumMatrix(fieldName);

              if (matrix.isNotNull()) {
                Function<Integer, DynamicEnum> factory = enumFactories.get(dataTypeId);
                assert factory != null;

                value = matrix.transform(o -> factory.apply((Integer) o));
              } else {
                value = matrix;
              }
              break;
            }
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              Matrix matrix = decoder.decodeMatrix(fieldName, BuiltinDataType.ExtensionObject);

              value =
                  matrix.transform(
                      o -> {
                        ExtensionObject xo = (ExtensionObject) o;
                        return xo.decode(decoder.getEncodingContext());
                      });
            } else {
              value = decoder.decodeStructMatrix(fieldName, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }

      return value;
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "illegal ValueRank: " + valueRank);
    }
  }

  private void encodeFieldValue(UaEncoder encoder, StructureField field, Object value) {
    String fieldName = field.getName();
    NodeId dataTypeId = field.getDataType();

    // Note: shall be scalar or fixed dimension
    Integer valueRank = field.getValueRank();
    if (valueRank == -1) {
      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        encodeBuiltinDataType(encoder, fieldName, (BuiltinDataType) hint, value);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            encoder.encodeEnum(fieldName, (UaEnumeratedType) value);
            break;
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              DynamicStruct structValue = (DynamicStruct) value;
              ExtensionObject xo =
                  ExtensionObject.encode(encoder.getEncodingContext(), structValue);
              encoder.encodeExtensionObject(fieldName, xo);
            } else {
              encoder.encodeStruct(fieldName, value, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }
    } else if (valueRank == 1) {
      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        encodeBuiltinDataTypeArray(encoder, fieldName, (BuiltinDataType) hint, value);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            encoder.encodeEnumArray(fieldName, (UaEnumeratedType[]) value);
            break;
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              DynamicStruct[] structArray = (DynamicStruct[]) value;

              ExtensionObject[] xoArray =
                  Arrays.stream(structArray)
                      .map(s -> ExtensionObject.encode(encoder.getEncodingContext(), s))
                      .toArray(ExtensionObject[]::new);

              encoder.encodeExtensionObjectArray(fieldName, xoArray);
            } else {
              encoder.encodeStructArray(fieldName, (Object[]) value, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }
    } else if (valueRank > 1) {
      Matrix matrix = (Matrix) value;

      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        encoder.encodeMatrix(fieldName, matrix);
      } else {
        TypeHint typeHint = (TypeHint) hint;

        switch (typeHint) {
          case ENUM:
            encoder.encodeEnumMatrix(fieldName, matrix);
            break;
          case STRUCT:
            if (dataTypeId.equals(NodeIds.Structure) || fieldAllowsSubtyping(field)) {
              Matrix xoMatrix =
                  matrix.transform(
                      o -> {
                        DynamicStruct structValue = (DynamicStruct) o;
                        return ExtensionObject.encode(encoder.getEncodingContext(), structValue);
                      });

              encoder.encodeMatrix(fieldName, xoMatrix);
            } else {
              encoder.encodeStructMatrix(fieldName, matrix, dataTypeId);
            }
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "illegal ValueRank: " + valueRank);
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

  private static Object decodeBuiltinDataType(
      UaDecoder decoder, String fieldName, BuiltinDataType builtinDataType) {

    return switch (builtinDataType) {
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
      UaDecoder decoder, String fieldName, BuiltinDataType builtinDataType) {

    return switch (builtinDataType) {
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
      UaEncoder encoder, String fieldName, BuiltinDataType builtinDataType, Object value) {

    switch (builtinDataType) {
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
        throw new RuntimeException("unhandled BuiltinDataType: " + builtinDataType);
    }
  }

  private static void encodeBuiltinDataTypeArray(
      UaEncoder encoder, String fieldName, BuiltinDataType builtinDataType, Object value) {

    switch (builtinDataType) {
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
        throw new RuntimeException("unhandled BuiltinDataType: " + builtinDataType);
    }
  }

  private enum TypeHint {
    ENUM,
    STRUCT
  }
}
