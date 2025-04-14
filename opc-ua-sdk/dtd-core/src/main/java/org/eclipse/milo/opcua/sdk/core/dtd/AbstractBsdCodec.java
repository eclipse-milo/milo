/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.opcua.binaryschema.EnumeratedType;
import org.opcfoundation.opcua.binaryschema.FieldType;
import org.opcfoundation.opcua.binaryschema.StructuredType;
import org.opcfoundation.opcua.binaryschema.SwitchOperand;
import org.opcfoundation.opcua.binaryschema.TypeDescription;

public abstract class AbstractBsdCodec<StructureT, MemberT> implements BinaryDataTypeCodec {

  private final Map<String, FieldType> fields = new HashMap<>();
  private final Map<String, FieldType> lengthFields = new HashMap<>();

  protected final String namespaceUri;
  protected final NodeId dataTypeId;
  protected final NodeId encodingId;
  protected final StructuredType structuredType;

  protected AbstractBsdCodec(
      String namespaceUri, NodeId dataTypeId, NodeId encodingId, StructuredType structuredType) {

    this.namespaceUri = namespaceUri;
    this.dataTypeId = dataTypeId;
    this.encodingId = encodingId;
    this.structuredType = structuredType;

    structuredType
        .getField()
        .forEach(
            field -> {
              fields.put(field.getName(), field);

              String lengthField = field.getLengthField();

              if (lengthField != null) {
                lengthFields.put(lengthField, fields.get(lengthField));
              }
            });
  }

  @Override
  public final Class<?> getType() {
    return BsdStructWrapper.class;
  }

  @Override
  public BsdStructWrapper<StructureT> decodeBinary(
      EncodingContext context, OpcUaBinaryDecoder decoder) throws UaSerializationException {

    LinkedHashMap<String, MemberT> members = new LinkedHashMap<>();

    PeekingIterator<FieldType> fieldIterator =
        Iterators.peekingIterator(structuredType.getField().iterator());

    while (fieldIterator.hasNext()) {
      FieldType field = fieldIterator.next();
      String fieldName = field.getName();
      String typeName = field.getTypeName().getLocalPart();
      String typeNamespace = field.getTypeName().getNamespaceURI();

      if (fieldIsAbsent(field, members)) {
        continue;
      }

      boolean typeNamespaceIsUa =
          Namespaces.OPC_UA.equals(typeNamespace) || Namespaces.OPC_UA_BSD.equals(typeNamespace);

      if (fieldIsScalar(field)) {
        Function<OpcUaBinaryDecoder, Object> reader;
        if (typeNamespaceIsUa && (reader = getReader(typeName)) != null) {
          Object value = reader.apply(decoder);

          members.put(fieldName, opcUaToMemberTypeScalar(fieldName, value, typeName));
        } else {
          Object value = decodeField(context, fieldName, typeNamespace, typeName, decoder);

          members.put(fieldName, opcUaToMemberTypeScalar(fieldName, value, typeName));
        }
      } else {
        if (field.isIsLengthInBytes()) {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "IsLengthInBytes=true not supported");
        }

        int length = fieldLength(field, members);

        if ("Bit".equals(typeName) && typeNamespaceIsUa) {
          BigInteger bitAccumulation = BigInteger.valueOf(0L);

          for (int i = 0; i < length; i++) {
            BigInteger bitValue = BigInteger.valueOf(decoder.decodeBit());

            bitAccumulation = bitAccumulation.or(bitValue.shiftLeft(i));
          }

          members.put(
              fieldName, opcUaToMemberTypeArray(fieldName, bitAccumulation.intValue(), typeName));
        } else {
          Object[] values = null;

          if (length >= 0) {
            values = new Object[length];

            Function<OpcUaBinaryDecoder, Object> reader;
            if (typeNamespaceIsUa && (reader = getReader(typeName)) != null) {
              for (int i = 0; i < length; i++) {
                Object value = reader.apply(decoder);

                values[i] = value;
              }
            } else {
              for (int i = 0; i < length; i++) {
                Object value = decodeField(context, fieldName, typeNamespace, typeName, decoder);

                values[i] = value;
              }
            }
          }

          members.put(fieldName, opcUaToMemberTypeArray(fieldName, values, typeName));
        }
      }
    }

    for (String lengthField : lengthFields.keySet()) {
      members.remove(lengthField);
    }

    StructureT structure = createStructure(structuredType.getName(), members);
    BsdDataType dataType =
        new BsdDataType(structuredType.getName(), dataTypeId, encodingId, structuredType);

    return new BsdStructWrapper<>(dataType, structure);
  }

  @Override
  public void encodeBinary(
      EncodingContext context, OpcUaBinaryEncoder encoder, UaStructuredType structure)
      throws UaSerializationException {

    //noinspection unchecked
    BsdStructWrapper<StructureT> wrapper = (BsdStructWrapper<StructureT>) structure;
    StructureT unwrappedStructure = wrapper.object();

    LinkedHashMap<String, MemberT> members = new LinkedHashMap<>(getMembers(unwrappedStructure));

    PeekingIterator<FieldType> fieldIterator =
        Iterators.peekingIterator(structuredType.getField().iterator());

    while (fieldIterator.hasNext()) {
      FieldType field = fieldIterator.next();

      if (fieldIsAbsent(field, members)) {
        continue;
      }

      if (lengthFields.containsKey(field.getName())) {
        // let arrays encode their own length field so the
        // structure representation isn't required to include it
        continue;
      }

      encodeField(context, encoder, members, field);
    }
  }

  private void encodeField(
      EncodingContext context,
      OpcUaBinaryEncoder encoder,
      LinkedHashMap<String, MemberT> members,
      FieldType field) {

    String typeName = field.getTypeName().getLocalPart();
    String typeNamespace = field.getTypeName().getNamespaceURI();

    MemberT member = members.get(field.getName());

    boolean typeNamespaceIsUa =
        Namespaces.OPC_UA.equals(typeNamespace) || Namespaces.OPC_UA_BSD.equals(typeNamespace);

    if (fieldIsScalar(field)) {
      Object scalarValue = memberTypeToOpcUaScalar(member, typeName);

      BiConsumer<OpcUaBinaryEncoder, Object> writer;
      if (typeNamespaceIsUa && (writer = getWriter(typeName)) != null) {
        writer.accept(encoder, scalarValue);
      } else {
        encodeField(context, field.getName(), typeNamespace, typeName, scalarValue, encoder);
      }
    } else {
      if (field.isIsLengthInBytes()) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "IsLengthInBytes=true not supported");
      }

      if ("Bit".equals(typeName) && typeNamespaceIsUa) {
        int length = fieldLength(field, members);

        Number number = (Number) memberTypeToOpcUaArray(member, typeName);
        BigInteger bi = BigInteger.valueOf(number.longValue());

        for (int i = 0; i < length; i++) {
          encoder.encodeBit(bi.shiftRight(i).and(BigInteger.ONE).intValue());
        }
      } else {
        Object[] valueArray = (Object[]) memberTypeToOpcUaArray(member, typeName);

        FieldType lengthField = fields.get(field.getLengthField());

        if (lengthField != null) {
          int length = valueArray.length;

          members.put(
              lengthField.getName(),
              opcUaToMemberTypeScalar(
                  lengthField.getName(), length, lengthField.getTypeName().getLocalPart()));

          encodeField(context, encoder, members, lengthField);
        }

        if (valueArray != null) {
          BiConsumer<OpcUaBinaryEncoder, Object> writer;
          if (typeNamespaceIsUa && (writer = getWriter(typeName)) != null) {
            for (Object value : valueArray) {
              writer.accept(encoder, value);
            }
          } else {
            for (Object value : valueArray) {
              encodeField(context, field.getName(), typeNamespace, typeName, value, encoder);
            }
          }
        }
      }
    }
  }

  private Object decodeField(
      EncodingContext context,
      String fieldName,
      String namespaceUri,
      String description,
      OpcUaBinaryDecoder decoder)
      throws UaSerializationException {

    DataTypeDictionary dictionary = context.getDataTypeManager().getTypeDictionary(namespaceUri);

    if (dictionary instanceof BinaryDataTypeDictionary) {
      BinaryDataTypeDictionary binaryDictionary = (BinaryDataTypeDictionary) dictionary;
      TypeDescription typeDescription = binaryDictionary.getTypeDescription(description);

      if (typeDescription instanceof StructuredType) {
        DataTypeCodec codec = dictionary.getCodec(description);

        if (codec != null) {
          return codec.decode(context, decoder);
        } else {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              String.format(
                  "no OpcBinaryDataTypeCodec registered for description=%s under namespaceUri=%s",
                  description, namespaceUri));
        }
      } else if (typeDescription instanceof EnumeratedType) {
        return decoder.decodeEnum(fieldName);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            String.format("type not supported: %s / %s", namespaceUri, description));
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError,
          String.format(
              "no BinaryDataTypeDictionary registered for under namespaceUri=%s", namespaceUri));
    }
  }

  private void encodeField(
      EncodingContext context,
      String fieldName,
      String namespaceUri,
      String description,
      Object value,
      OpcUaBinaryEncoder encoder)
      throws UaSerializationException {

    DataTypeDictionary dictionary = context.getDataTypeManager().getTypeDictionary(namespaceUri);

    if (dictionary instanceof BinaryDataTypeDictionary) {
      BinaryDataTypeDictionary binaryDictionary = (BinaryDataTypeDictionary) dictionary;
      TypeDescription typeDescription = binaryDictionary.getTypeDescription(description);

      if (typeDescription instanceof StructuredType) {
        DataTypeCodec codec = dictionary.getCodec(description);

        if (codec != null) {
          codec.encode(context, encoder, (UaStructuredType) value);
        } else {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              String.format(
                  "no OpcBinaryDataTypeCodec registered for description=%s under namespaceUri=%s",
                  description, namespaceUri));
        }
      } else if (typeDescription instanceof EnumeratedType) {
        encoder.encodeEnum(
            fieldName,
            new UaEnumeratedType() {
              @Override
              public ExpandedNodeId getTypeId() {
                return ExpandedNodeId.NULL_VALUE;
              }

              @Override
              public int getValue() {
                return ((Number) value).intValue();
              }
            });
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            String.format("type not supported: %s / %s", namespaceUri, description));
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError,
          String.format(
              "no BinaryDataTypeDictionary registered for under namespaceUri=%s", namespaceUri));
    }
  }

  protected abstract StructureT createStructure(
      String name, LinkedHashMap<String, MemberT> members);

  protected abstract Map<String, MemberT> getMembers(StructureT value);

  /**
   * Convert an OPC UA scalar value into a member of type {@link MemberT}.
   *
   * @param name then name of the member.
   * @param value the value of the member.
   * @param typeName the name of the OPC UA DataType.
   * @return a member of type {@link MemberT}.
   */
  protected abstract MemberT opcUaToMemberTypeScalar(
      String name, @Nullable Object value, String typeName);

  /**
   * Convert an OPC UA array value into a member of type {@link MemberT}.
   *
   * @param name the name of the member.
   * @param values the values of the member array.
   * @param typeName the name of the OPC UA DataType.
   * @return member of type {@link MemberT}.
   */
  protected abstract MemberT opcUaToMemberTypeArray(
      String name, @Nullable Object values, String typeName);

  protected abstract Object memberTypeToOpcUaScalar(MemberT member, String typeName);

  protected abstract Object memberTypeToOpcUaArray(MemberT member, String typeName);

  private int fieldLength(FieldType field, LinkedHashMap<String, MemberT> members) {
    int length = 1;

    if (field.getLength() != null) {
      length = field.getLength().intValue();
    } else if (field.getLengthField() != null) {
      MemberT lengthMember = members.get(field.getLengthField());

      if (lengthMember != null) {
        String lengthTypeName =
            structuredType.getField().stream()
                .filter(f -> f.getName().equals(field.getLengthField()))
                .findFirst()
                .map(f -> f.getTypeName().getLocalPart())
                .orElse("Int32");

        length = ((Number) memberTypeToOpcUaScalar(lengthMember, lengthTypeName)).intValue();
      }
    }

    return length;
  }

  private boolean fieldIsAbsent(FieldType field, Map<String, MemberT> members) {
    if (field.getSwitchField() == null) {
      return false;
    } else {
      MemberT controlField = members.get(field.getSwitchField());

      String controlTypeName =
          structuredType.getField().stream()
              .filter(f -> f.getName().equals(field.getSwitchField()))
              .findFirst()
              .map(f -> f.getTypeName().getLocalPart())
              .orElse("Int32");

      long controlValue =
          ((Number) memberTypeToOpcUaScalar(controlField, controlTypeName)).longValue();

      long switchValue = field.getSwitchValue() != null ? field.getSwitchValue() : 1L;

      SwitchOperand switchOperand =
          field.getSwitchOperand() != null ? field.getSwitchOperand() : SwitchOperand.EQUALS;

      return !compareToSwitchValue(controlValue, switchOperand, switchValue);
    }
  }

  private static boolean compareToSwitchValue(
      long controlValue, SwitchOperand switchOperand, long switchValue) {
    return switch (switchOperand) {
      case EQUALS -> controlValue == switchValue;
      case NOT_EQUAL -> controlValue != switchValue;
      case GREATER_THAN -> controlValue > switchValue;
      case GREATER_THAN_OR_EQUAL -> controlValue >= switchValue;
      case LESS_THAN -> controlValue < switchValue;
      case LESS_THAN_OR_EQUAL -> controlValue <= switchValue;
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_InternalError, "unknown SwitchOperand: " + switchOperand);
    };
  }

  private static boolean fieldIsScalar(FieldType field) {
    return field.getLengthField() == null && field.getLength() == null;
  }

  private static Function<OpcUaBinaryDecoder, Object> getReader(String typeName) {
    return switch (typeName) {
      case "Boolean" -> OpcUaBinaryDecoder::decodeBoolean;
      case "SByte" -> OpcUaBinaryDecoder::decodeSByte;
      case "Int16" -> OpcUaBinaryDecoder::decodeInt16;
      case "Int32" -> OpcUaBinaryDecoder::decodeInt32;
      case "Int64" -> OpcUaBinaryDecoder::decodeInt64;
      case "Byte" -> OpcUaBinaryDecoder::decodeByte;
      case "UInt16" -> OpcUaBinaryDecoder::decodeUInt16;
      case "UInt32" -> OpcUaBinaryDecoder::decodeUInt32;
      case "UInt64" -> OpcUaBinaryDecoder::decodeUInt64;
      case "Float" -> OpcUaBinaryDecoder::decodeFloat;
      case "Double" -> OpcUaBinaryDecoder::decodeDouble;
      case "String" -> OpcUaBinaryDecoder::decodeString;
      case "DateTime" -> OpcUaBinaryDecoder::decodeDateTime;
      case "Guid" -> OpcUaBinaryDecoder::decodeGuid;
      case "ByteString" -> OpcUaBinaryDecoder::decodeByteString;
      case "XmlElement" -> OpcUaBinaryDecoder::decodeXmlElement;
      case "NodeId" -> OpcUaBinaryDecoder::decodeNodeId;
      case "ExpandedNodeId" -> OpcUaBinaryDecoder::decodeExpandedNodeId;
      case "StatusCode" -> OpcUaBinaryDecoder::decodeStatusCode;
      case "QualifiedName" -> OpcUaBinaryDecoder::decodeQualifiedName;
      case "LocalizedText" -> OpcUaBinaryDecoder::decodeLocalizedText;
      case "ExtensionObject" -> OpcUaBinaryDecoder::decodeExtensionObject;
      case "DataValue" -> OpcUaBinaryDecoder::decodeDataValue;
      case "Variant" -> OpcUaBinaryDecoder::decodeVariant;
      case "DiagnosticInfo" -> OpcUaBinaryDecoder::decodeDiagnosticInfo;
      case "Bit" -> OpcUaBinaryDecoder::decodeBit;
      case "Char" -> OpcUaBinaryDecoder::decodeCharacter;
      case "CharArray" -> OpcUaBinaryDecoder::decodeUtf8CharArray;
      case "WideChar" -> OpcUaBinaryDecoder::decodeWideChar;
      case "WideCharArray", "WideString" -> OpcUaBinaryDecoder::decodeUtf16CharArray;
      default -> null;
    };
  }

  private static BiConsumer<OpcUaBinaryEncoder, Object> getWriter(String typeName) {
    return switch (typeName) {
      case "Boolean" -> (w, v) -> w.encodeBoolean((Boolean) v);
      case "SByte" -> (w, v) -> w.encodeSByte((Byte) v);
      case "Int16" -> (w, v) -> w.encodeInt16((Short) v);
      case "Int32" -> (w, v) -> w.encodeInt32((Integer) v);
      case "Int64" -> (w, v) -> w.encodeInt64((Long) v);
      case "Byte" -> (w, v) -> w.encodeByte((UByte) v);
      case "UInt16" -> (w, v) -> w.encodeUInt16((UShort) v);
      case "UInt32" -> (w, v) -> w.encodeUInt32((UInteger) v);
      case "UInt64" -> (w, v) -> w.encodeUInt64((ULong) v);
      case "Float" -> (w, v) -> w.encodeFloat((Float) v);
      case "Double" -> (w, v) -> w.encodeDouble((Double) v);
      case "String" -> (w, v) -> w.encodeString((String) v);
      case "DateTime" -> (w, v) -> w.encodeDateTime((DateTime) v);
      case "Guid" -> (w, v) -> w.encodeGuid((UUID) v);
      case "ByteString" -> (w, v) -> w.encodeByteString((ByteString) v);
      case "XmlElement" -> (w, v) -> w.encodeXmlElement((XmlElement) v);
      case "NodeId" -> (w, v) -> w.encodeNodeId((NodeId) v);
      case "ExpandedNodeId" -> (w, v) -> w.encodeExpandedNodeId((ExpandedNodeId) v);
      case "StatusCode" -> (w, v) -> w.encodeStatusCode((StatusCode) v);
      case "QualifiedName" -> (w, v) -> w.encodeQualifiedName((QualifiedName) v);
      case "LocalizedText" -> (w, v) -> w.encodeLocalizedText((LocalizedText) v);
      case "ExtensionObject" -> (w, v) -> w.encodeExtensionObject((ExtensionObject) v);
      case "DataValue" -> (w, v) -> w.encodeDataValue((DataValue) v);
      case "Variant" -> (w, v) -> w.encodeVariant((Variant) v);
      case "DiagnosticInfo" -> (w, v) -> w.encodeDiagnosticInfo((DiagnosticInfo) v);
      case "Bit" -> (w, v) -> w.encodeBit((Integer) v);
      case "Char" -> (w, v) -> w.encodeCharacter((Character) v);
      case "CharArray" -> (w, v) -> w.encodeUtf8CharArray((String) v);
      case "WideChar" -> (w, v) -> w.encodeWideChar((Character) v);
      case "WideCharArray", "WideString" -> (w, v) -> w.encodeUtf16CharArray((String) v);
      default -> null;
    };
  }
}
