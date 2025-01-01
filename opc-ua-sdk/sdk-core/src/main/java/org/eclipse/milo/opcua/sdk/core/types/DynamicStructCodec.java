/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

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
    switch (definition.getStructureType()) {
      case Structure:
      case StructureWithOptionalFields:
      case StructureWithSubtypedValues:
        return decodeStruct(decoder);
      case Union:
      case UnionWithSubtypedValues:
        return decodeUnion(decoder);
      default:
        throw new IllegalArgumentException(
            "unhandled StructureType: " + definition.getStructureType());
    }
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
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    LinkedHashMap<String, Object> members = new LinkedHashMap<>();

    long switchField = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      switchField = decoder.decodeUInt32("SwitchField").longValue();
    }

    for (int i = 0; i < fields.length; i++) {
      StructureField field = fields[i];

      if (!field.getIsOptional() || ((switchField >>> i) & 1) == 1) {
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

    long switchField = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      switchField = 0L;
      for (int i = 0; i < fields.length; i++) {
        StructureField field = fields[i];
        if (!field.getIsOptional()
            || (field.getIsOptional() && struct.getMembers().containsKey(field.getName()))) {

          switchField |= (1L << i);
        }
      }
      encoder.encodeUInt32("SwitchField", UInteger.valueOf(switchField));
    }

    for (int i = 0; i < fields.length; i++) {
      StructureField field = fields[i];

      if (!field.getIsOptional() || ((switchField >>> i) & 1) == 1) {
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
            value = decoder.decodeStruct(fieldName, dataTypeId);
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
            value = decoder.decodeStructArray(fieldName, dataTypeId);
            break;
          default:
            throw new RuntimeException("codecType: " + typeHint);
        }
      }

      return value;
    } else if (valueRank > 1) {
      Object value;

      Object hint = fieldHints.get(field);
      if (hint instanceof BuiltinDataType) {
        BuiltinDataType builtinDataType = (BuiltinDataType) hint;

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
            value = decoder.decodeStructMatrix(fieldName, dataTypeId);
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
            encoder.encodeStruct(fieldName, value, dataTypeId);
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
            encoder.encodeStructArray(fieldName, (Object[]) value, dataTypeId);
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
            encoder.encodeStructMatrix(fieldName, matrix, dataTypeId);
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

  private static Object decodeBuiltinDataType(
      UaDecoder decoder, String fieldName, BuiltinDataType builtinDataType) {

    switch (builtinDataType) {
      case Boolean:
        return decoder.decodeBoolean(fieldName);
      case SByte:
        return decoder.decodeSByte(fieldName);
      case Byte:
        return decoder.decodeByte(fieldName);
      case Int16:
        return decoder.decodeInt16(fieldName);
      case UInt16:
        return decoder.decodeUInt16(fieldName);
      case Int32:
        return decoder.decodeInt32(fieldName);
      case UInt32:
        return decoder.decodeUInt32(fieldName);
      case Int64:
        return decoder.decodeInt64(fieldName);
      case UInt64:
        return decoder.decodeUInt64(fieldName);
      case Float:
        return decoder.decodeFloat(fieldName);
      case Double:
        return decoder.decodeDouble(fieldName);
      case String:
        return decoder.decodeString(fieldName);
      case DateTime:
        return decoder.decodeDateTime(fieldName);
      case Guid:
        return decoder.decodeGuid(fieldName);
      case ByteString:
        return decoder.decodeByteString(fieldName);
      case XmlElement:
        return decoder.decodeXmlElement(fieldName);
      case NodeId:
        return decoder.decodeNodeId(fieldName);
      case ExpandedNodeId:
        return decoder.decodeExpandedNodeId(fieldName);
      case StatusCode:
        return decoder.decodeStatusCode(fieldName);
      case QualifiedName:
        return decoder.decodeQualifiedName(fieldName);
      case LocalizedText:
        return decoder.decodeLocalizedText(fieldName);
      case ExtensionObject:
        return decoder.decodeExtensionObject(fieldName);
      case DataValue:
        return decoder.decodeDataValue(fieldName);
      case Variant:
        return decoder.decodeVariant(fieldName);
      case DiagnosticInfo:
        return decoder.decodeDiagnosticInfo(fieldName);
      default:
        // Shouldn't happen
        throw new RuntimeException("unhandled BuiltinDataType: " + builtinDataType);
    }
  }

  private static Object decodeBuiltinDataTypeArray(
      UaDecoder decoder, String fieldName, BuiltinDataType builtinDataType) {

    switch (builtinDataType) {
      case Boolean:
        return decoder.decodeBooleanArray(fieldName);
      case SByte:
        return decoder.decodeSByteArray(fieldName);
      case Byte:
        return decoder.decodeByteArray(fieldName);
      case Int16:
        return decoder.decodeInt16Array(fieldName);
      case UInt16:
        return decoder.decodeUInt16Array(fieldName);
      case Int32:
        return decoder.decodeInt32Array(fieldName);
      case UInt32:
        return decoder.decodeUInt32Array(fieldName);
      case Int64:
        return decoder.decodeInt64Array(fieldName);
      case UInt64:
        return decoder.decodeUInt64Array(fieldName);
      case Float:
        return decoder.decodeFloatArray(fieldName);
      case Double:
        return decoder.decodeDoubleArray(fieldName);
      case String:
        return decoder.decodeStringArray(fieldName);
      case DateTime:
        return decoder.decodeDateTimeArray(fieldName);
      case Guid:
        return decoder.decodeGuidArray(fieldName);
      case ByteString:
        return decoder.decodeByteStringArray(fieldName);
      case XmlElement:
        return decoder.decodeXmlElementArray(fieldName);
      case NodeId:
        return decoder.decodeNodeIdArray(fieldName);
      case ExpandedNodeId:
        return decoder.decodeExpandedNodeIdArray(fieldName);
      case StatusCode:
        return decoder.decodeStatusCodeArray(fieldName);
      case QualifiedName:
        return decoder.decodeQualifiedNameArray(fieldName);
      case LocalizedText:
        return decoder.decodeLocalizedTextArray(fieldName);
      case ExtensionObject:
        return decoder.decodeExtensionObjectArray(fieldName);
      case DataValue:
        return decoder.decodeDataValueArray(fieldName);
      case Variant:
        return decoder.decodeVariantArray(fieldName);
      case DiagnosticInfo:
        return decoder.decodeDiagnosticInfoArray(fieldName);
      default:
        // Shouldn't happen
        throw new RuntimeException("unhandled BuiltinDataType: " + builtinDataType);
    }
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
