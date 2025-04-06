/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import jakarta.xml.bind.DatatypeConverter;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import javax.xml.stream.*;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

public class OpcUaXmlEncoder implements UaEncoder {

  private StringWriter xmlString;
  private XMLStreamWriter xmlStreamWriter;

  private final Deque<String> namespaces = new ArrayDeque<>();

  private final EncodingContext context;

  public OpcUaXmlEncoder(EncodingContext context) {
    this.context = context;

    reset();
  }

  @Override
  public EncodingContext getEncodingContext() {
    return context;
  }

  public void reset() {
    try {
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
      xmlStreamWriter = factory.createXMLStreamWriter(xmlString = new StringWriter());
      xmlStreamWriter.setPrefix("uax", Namespaces.OPC_UA_XSD);
      xmlStreamWriter.setPrefix("xsi", Namespaces.XML_SCHEMA_INSTANCE);

      namespaces.clear();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  public String getDocumentXml() {
    try {
      xmlStreamWriter.flush();
      return xmlString.toString();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean beginField(String field) {
    return beginField(field, false, false);
  }

  private boolean beginField(String field, boolean isDefault, boolean isNillable) {
    return beginField(field, isDefault, isNillable, false);
  }

  private boolean beginField(
      String field, boolean isDefault, boolean isNillable, boolean isArrayElement) {

    try {
      if (field != null && !field.isEmpty()) {
        if (isNillable && isDefault && !isArrayElement) {
          return false;
        }

        if (namespaces.peek() != null) {
          xmlStreamWriter.writeStartElement(namespaces.peek(), field);
        } else {
          xmlStreamWriter.writeStartElement(field);
        }

        if (isDefault) {
          if (isNillable) {
            xmlStreamWriter.writeAttribute("xsi:nil", "true");
          }
          xmlStreamWriter.writeEndElement();
          return false;
        }
      }

      return !isDefault;
    } catch (XMLStreamException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  private void endField(String field) {
    try {
      if (field != null && !field.isEmpty()) {
        xmlStreamWriter.writeEndElement();
      }
    } catch (XMLStreamException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeBoolean(String field, Boolean value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printBoolean(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeSByte(String field, Byte value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printByte(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeInt16(String field, Short value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printShort(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeInt32(String field, Integer value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printInt(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeInt64(String field, Long value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printLong(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeByte(String field, UByte value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printShort(value.shortValue()));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt16(String field, UShort value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printInt(value.intValue()));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt32(String field, UInteger value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printLong(value.longValue()));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt64(String field, ULong value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printInteger(value.toBigInteger()));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeFloat(String field, Float value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printFloat(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeDouble(String field, Double value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(DatatypeConverter.printDouble(value));
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeString(String field, String value) throws UaSerializationException {
    encodeStringValue(field, value, false);
  }

  private void encodeStringValue(String field, String value, boolean isArrayElement) {
    if (beginField(field, value == null, true, isArrayElement)) {
      try {
        if (value != null && !value.isBlank()) {
          xmlStreamWriter.writeCharacters(value);
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeDateTime(String field, DateTime value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        if (value != null) {
          xmlStreamWriter.writeCharacters(value.toIso8601String());
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeGuid(String field, UUID value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        if (value != null) {
          xmlStreamWriter.writeCharacters(value.toString().toUpperCase());
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeByteString(String field, ByteString value) throws UaSerializationException {
    if (beginField(field, value == null, true)) {
      try {
        if (value != null && value.isNotNull()) {
          xmlStreamWriter.writeCharacters(
              DatatypeConverter.printBase64Binary(value.bytesOrEmpty()));
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeXmlElement(String field, XmlElement value) throws UaSerializationException {
    if (beginField(field, value == null, true)) {
      try {
        if (value != null && value.isNotNull()) {
          XmlSerializationUtil.writeXmlFragment(xmlStreamWriter, value.getFragmentOrEmpty());
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeNodeId(String field, NodeId value) throws UaSerializationException {
    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          encodeString("Identifier", value.toParseableString());
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeExpandedNodeId(String field, ExpandedNodeId value)
      throws UaSerializationException {

    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          encodeString("Identifier", value.toParseableString());
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {
    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          encodeUInt32("Code", UInteger.valueOf(value.getValue()));
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeQualifiedName(String field, QualifiedName value)
      throws UaSerializationException {

    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          encodeUInt16("NamespaceIndex", value.getNamespaceIndex());
          encodeString("Name", value.getName());
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeLocalizedText(String field, LocalizedText value)
      throws UaSerializationException {

    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          if (value.locale() != null && !value.locale().isBlank()) {
            encodeString("Locale", value.locale());
          }
          if (value.text() != null && !value.text().isBlank()) {
            encodeString("Text", value.text());
          }
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeExtensionObject(String field, ExtensionObject value)
      throws UaSerializationException {

    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value == null || value.isNull()) {
          return;
        }

        // TODO this is very incomplete

        if (value instanceof ExtensionObject.Xml xml) {
          NodeId typeId = xml.getEncodingOrTypeId();

          encodeNodeId("TypeId", typeId);

          xmlStreamWriter.writeStartElement(Namespaces.OPC_UA_XSD, "Body");
          XmlSerializationUtil.writeXmlFragment(
              xmlStreamWriter, xml.getBody().getFragmentOrEmpty());
          xmlStreamWriter.writeEndElement();
        }
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeDataValue(String field, DataValue value) throws UaSerializationException {
    if (beginField(field, value == null, true)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value != null) {
          if (!value.getValue().isNull()) {
            encodeVariant("Value", value.getValue());
          }

          if (!StatusCode.GOOD.equals(value.getStatusCode())) {
            encodeStatusCode("StatusCode", value.getStatusCode());
          }

          if (value.getSourceTime() != null) {
            encodeDateTime("SourceTimestamp", value.getSourceTime());
          }

          if (value.getSourcePicoseconds() != null) {
            encodeUInt16("SourcePicoseconds", value.getSourcePicoseconds());
          }

          if (value.getServerTime() != null) {
            encodeDateTime("ServerTimestamp", value.getServerTime());
          }

          if (value.getServerPicoseconds() != null) {
            encodeUInt16("ServerPicoseconds", value.getServerPicoseconds());
          }
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeVariant(String field, Variant value) throws UaSerializationException {
    if (beginField(field)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        xmlStreamWriter.writeStartElement(Namespaces.OPC_UA_XSD, "Value");
        encodeVariantValue(value.value());
        xmlStreamWriter.writeEndElement();
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  public void encodeVariantValue(@Nullable Object value) {
    try {
      if (value == null) {
        xmlStreamWriter.writeStartElement(Namespaces.OPC_UA_XSD, "Null");
        xmlStreamWriter.writeEndElement();
        return;
      }

      Class<?> valueClass;
      if (value instanceof Matrix m) {
        if (m.getElements() == null) return;
        valueClass = ArrayUtil.getType(m.getElements());
      } else {
        valueClass = ArrayUtil.getType(value);
      }

      TypeHint typeHint = TypeHint.BUILTIN;
      if (UaEnumeratedType.class.isAssignableFrom(valueClass)) {
        typeHint = TypeHint.ENUM;
      } else if (UaStructuredType.class.isAssignableFrom(valueClass)) {
        typeHint = TypeHint.STRUCT;
      } else if (OptionSetUInteger.class.isAssignableFrom(valueClass)) {
        typeHint = TypeHint.OPTION_SET;
      }

      int typeId =
          switch (typeHint) {
            case ENUM -> OpcUaDataType.Int32.getTypeId();
            case STRUCT -> OpcUaDataType.ExtensionObject.getTypeId();
            case OPTION_SET -> {
              if (OptionSetUI8.class.isAssignableFrom(valueClass)) {
                yield OpcUaDataType.Byte.getTypeId();
              } else if (OptionSetUI16.class.isAssignableFrom(valueClass)) {
                yield OpcUaDataType.UInt16.getTypeId();
              } else if (OptionSetUI32.class.isAssignableFrom(valueClass)) {
                yield OpcUaDataType.UInt32.getTypeId();
              } else if (OptionSetUI64.class.isAssignableFrom(valueClass)) {
                yield OpcUaDataType.UInt64.getTypeId();
              } else {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingError, "unknown OptionSet type: " + valueClass);
              }
            }
            default -> OpcUaDataType.getBuiltinTypeId(valueClass);
          };

      OpcUaDataType dataType = OpcUaDataType.fromTypeId(typeId);
      if (dataType == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "unknown typeId: " + typeId);
      }

      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (value.getClass().isArray()) {
          switch (typeHint) {
            case BUILTIN -> encodeBuiltinTypeArrayValue(value, dataType);
            case ENUM -> {
              if (value instanceof UaEnumeratedType[] array) {
                Integer[] values = new Integer[array.length];
                for (int i = 0; i < array.length; i++) {
                  values[i] = array[i].getValue();
                }
                encodeBuiltinTypeArrayValue(values, OpcUaDataType.Int32);
              }
            }
            case STRUCT -> {
              if (value instanceof UaStructuredType[] array) {
                ExtensionObject[] xos = new ExtensionObject[array.length];
                for (int i = 0; i < array.length; i++) {
                  UaStructuredType structValue = array[i];
                  xos[i] = ExtensionObject.encode(context, structValue);
                }
                encodeBuiltinTypeArrayValue(xos, OpcUaDataType.ExtensionObject);
              }
            }
            case OPTION_SET -> {
              if (value instanceof OptionSetUI8<?>[] array) {
                UByte[] values = new UByte[array.length];
                for (int i = 0; i < array.length; i++) {
                  values[i] = array[i].getValue();
                }
                encodeBuiltinTypeArrayValue(values, OpcUaDataType.Byte);
              } else if (value instanceof OptionSetUI16<?>[] array) {
                UShort[] values = new UShort[array.length];
                for (int i = 0; i < array.length; i++) {
                  values[i] = array[i].getValue();
                }
                encodeBuiltinTypeArrayValue(values, OpcUaDataType.UInt16);
              } else if (value instanceof OptionSetUI32<?>[] array) {
                UInteger[] values = new UInteger[array.length];
                for (int i = 0; i < array.length; i++) {
                  values[i] = array[i].getValue();
                }
                encodeBuiltinTypeArrayValue(values, OpcUaDataType.UInt32);
              } else if (value instanceof OptionSetUI64<?>[] array) {
                ULong[] values = new ULong[array.length];
                for (int i = 0; i < array.length; i++) {
                  values[i] = array[i].getValue();
                }
                encodeBuiltinTypeArrayValue(values, OpcUaDataType.UInt64);
              } else {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingError,
                    "unknown OptionSet type: " + valueClass.getName());
              }
            }
          }
        } else if (value instanceof Matrix m) {
          // TODO
        } else {
          switch (typeHint) {
            case BUILTIN -> encodeBuiltinTypeValue(value, dataType);
            case ENUM -> {
              UaEnumeratedType enumValue = (UaEnumeratedType) value;
              encodeBuiltinTypeValue(enumValue.getValue(), OpcUaDataType.Int32);
            }
            case STRUCT -> {
              UaStructuredType structValue = (UaStructuredType) value;
              var xo = ExtensionObject.encode(context, structValue);
              encodeBuiltinTypeValue(xo, OpcUaDataType.ExtensionObject);
            }
            case OPTION_SET -> {
              OptionSetUInteger<?> optionSet = (OptionSetUInteger<?>) value;
              encodeBuiltinTypeValue(optionSet.getValue(), dataType);
            }
          }
        }
      } finally {
        namespaces.pop();
      }
    } catch (XMLStreamException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  private void encodeBuiltinTypeValue(Object value, OpcUaDataType dataType) {
    switch (dataType) {
      case Boolean -> encodeBoolean("Boolean", (Boolean) value);
      case Byte -> encodeByte("Byte", (UByte) value);
      case SByte -> encodeSByte("SByte", (Byte) value);
      case Int16 -> encodeInt16("Int16", (Short) value);
      case UInt16 -> encodeUInt16("UInt16", (UShort) value);
      case Int32 -> encodeInt32("Int32", (Integer) value);
      case UInt32 -> encodeUInt32("UInt32", (UInteger) value);
      case Int64 -> encodeInt64("Int64", (Long) value);
      case UInt64 -> encodeUInt64("UInt64", (ULong) value);
      case Float -> encodeFloat("Float", (Float) value);
      case Double -> encodeDouble("Double", (Double) value);
      case String -> encodeString("String", (String) value);
      case DateTime -> encodeDateTime("DateTime", (DateTime) value);
      case Guid -> encodeGuid("Guid", (UUID) value);
      case ByteString -> encodeByteString("ByteString", (ByteString) value);
      case XmlElement -> encodeXmlElement("XmlElement", (XmlElement) value);
      case NodeId -> encodeNodeId("NodeId", (NodeId) value);
      case ExpandedNodeId -> encodeExpandedNodeId("ExpandedNodeId", (ExpandedNodeId) value);
      case StatusCode -> encodeStatusCode("StatusCode", (StatusCode) value);
      case QualifiedName -> encodeQualifiedName("QualifiedName", (QualifiedName) value);
      case LocalizedText -> encodeLocalizedText("LocalizedText", (LocalizedText) value);
      case ExtensionObject -> encodeExtensionObject("ExtensionObject", (ExtensionObject) value);
      case DataValue -> encodeDataValue("DataValue", (DataValue) value);
      case Variant -> encodeVariant("Variant", (Variant) value);
      case DiagnosticInfo -> encodeDiagnosticInfo("DiagnosticInfo", (DiagnosticInfo) value);
    }
  }

  private void encodeBuiltinTypeArrayValue(Object value, OpcUaDataType dataType) {
    switch (dataType) {
      case Boolean -> {
        if (value instanceof boolean[] primitiveArray) {
          Boolean[] boxedArray = new Boolean[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeBooleanArray("ListOfBoolean", boxedArray);
        } else {
          encodeBooleanArray("ListOfBoolean", (Boolean[]) value);
        }
      }
      case Byte -> encodeByteArray("ListOfByte", (UByte[]) value);
      case SByte -> {
        if (value instanceof byte[] primitiveArray) {
          Byte[] boxedArray = new Byte[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeSByteArray("ListOfSByte", boxedArray);
        } else {
          encodeSByteArray("ListOfSByte", (Byte[]) value);
        }
      }
      case Int16 -> {
        if (value instanceof short[] primitiveArray) {
          Short[] boxedArray = new Short[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeInt16Array("ListOfInt16", boxedArray);
        } else {
          encodeInt16Array("ListOfInt16", (Short[]) value);
        }
      }
      case UInt16 -> encodeUInt16Array("ListOfUInt16", (UShort[]) value);
      case Int32 -> {
        if (value instanceof int[] primitiveArray) {
          Integer[] boxedArray = new Integer[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeInt32Array("ListOfInt32", boxedArray);
        } else {
          encodeInt32Array("ListOfInt32", (Integer[]) value);
        }
      }
      case UInt32 -> encodeUInt32Array("ListOfUInt32", (UInteger[]) value);
      case Int64 -> {
        if (value instanceof long[] primitiveArray) {
          Long[] boxedArray = new Long[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeInt64Array("ListOfInt64", boxedArray);
        } else {
          encodeInt64Array("ListOfInt64", (Long[]) value);
        }
      }
      case UInt64 -> encodeUInt64Array("ListOfUInt64", (ULong[]) value);
      case Float -> {
        if (value instanceof float[] primitiveArray) {
          Float[] boxedArray = new Float[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeFloatArray("ListOfFloat", boxedArray);
        } else {
          encodeFloatArray("ListOfFloat", (Float[]) value);
        }
      }
      case Double -> {
        if (value instanceof double[] primitiveArray) {
          Double[] boxedArray = new Double[primitiveArray.length];
          for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
          }
          encodeDoubleArray("ListOfDouble", boxedArray);
        } else {
          encodeDoubleArray("ListOfDouble", (Double[]) value);
        }
      }
      case String -> encodeStringArray("ListOfString", (String[]) value);
      case DateTime -> encodeDateTimeArray("ListOfDateTime", (DateTime[]) value);
      case Guid -> encodeGuidArray("ListOfGuid", (UUID[]) value);
      case ByteString -> encodeByteStringArray("ListOfByteString", (ByteString[]) value);
      case XmlElement -> encodeXmlElementArray("ListOfXmlElement", (XmlElement[]) value);
      case NodeId -> encodeNodeIdArray("ListOfNodeId", (NodeId[]) value);
      case ExpandedNodeId ->
          encodeExpandedNodeIdArray("ListOfExpandedNodeId", (ExpandedNodeId[]) value);
      case StatusCode -> encodeStatusCodeArray("ListOfStatusCode", (StatusCode[]) value);
      case QualifiedName ->
          encodeQualifiedNameArray("ListOfQualifiedName", (QualifiedName[]) value);
      case LocalizedText ->
          encodeLocalizedTextArray("ListOfLocalizedText", (LocalizedText[]) value);
      case ExtensionObject ->
          encodeExtensionObjectArray("ListOfExtensionObject", (ExtensionObject[]) value);
      case DataValue -> encodeDataValueArray("ListOfDataValue", (DataValue[]) value);
      case Variant -> encodeVariantArray("ListOfVariant", (Variant[]) value);
      case DiagnosticInfo ->
          encodeDiagnosticInfoArray("ListOfDiagnosticInfo", (DiagnosticInfo[]) value);
    }
  }

  @Override
  public void encodeDiagnosticInfo(String field, DiagnosticInfo value)
      throws UaSerializationException {}

  @Override
  public void encodeMessage(String field, UaMessageType message) throws UaSerializationException {}

  @Override
  public void encodeEnum(String field, UaEnumeratedType value) {
    encodeString(field, String.format("%s_%s", value.getName(), value.getValue()));
  }

  @Override
  public void encodeStruct(String field, Object value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDateTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId));

    encodeStruct(field, value, localDateTypeId);
  }

  @Override
  public void encodeStruct(String field, Object value, NodeId dataTypeId)
      throws UaSerializationException {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId);
    }

    encodeStruct(field, value, codec);
  }

  @Override
  public void encodeStruct(String field, Object value, DataTypeCodec codec)
      throws UaSerializationException {

    if (beginField(field)) {
      try {
        codec.encode(context, this, value);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeBooleanArray(String field, Boolean[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Boolean v : value) {
          encodeBoolean("Boolean", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeSByteArray(String field, Byte[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Byte v : value) {
          encodeSByte("SByte", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeInt16Array(String field, Short[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Short v : value) {
          encodeInt16("Int16", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeInt32Array(String field, Integer[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Integer v : value) {
          encodeInt32("Int32", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeInt64Array(String field, Long[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Long v : value) {
          encodeInt64("Int64", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeByteArray(String field, UByte[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (UByte v : value) {
          encodeByte("Byte", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt16Array(String field, UShort[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (UShort v : value) {
          encodeUInt16("UInt16", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt32Array(String field, UInteger[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (UInteger v : value) {
          encodeUInt32("UInt32", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt64Array(String field, ULong[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (ULong v : value) {
          encodeUInt64("UInt64", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeFloatArray(String field, Float[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Float v : value) {
          encodeFloat("Float", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeDoubleArray(String field, Double[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Double v : value) {
          encodeDouble("Double", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeStringArray(String field, String[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (String v : value) {
          encodeStringValue("String", v, true);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeDateTimeArray(String field, DateTime[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (DateTime v : value) {
          encodeDateTime("DateTime", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeGuidArray(String field, UUID[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (UUID v : value) {
          encodeGuid("Guid", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeByteStringArray(String field, ByteString[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (ByteString v : value) {
          encodeByteString("ByteString", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeXmlElementArray(String field, XmlElement[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (XmlElement v : value) {
          encodeXmlElement("XmlElement", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeNodeIdArray(String field, NodeId[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (NodeId v : value) {
          encodeNodeId("NodeId", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeExpandedNodeIdArray(String field, ExpandedNodeId[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (ExpandedNodeId v : value) {
          encodeExpandedNodeId("ExpandedNodeId", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeStatusCodeArray(String field, StatusCode[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (StatusCode v : value) {
          encodeStatusCode("StatusCode", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeQualifiedNameArray(String field, QualifiedName[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (QualifiedName v : value) {
          encodeQualifiedName("QualifiedName", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeLocalizedTextArray(String field, LocalizedText[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (LocalizedText v : value) {
          encodeLocalizedText("LocalizedText", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeExtensionObjectArray(String field, ExtensionObject[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (ExtensionObject v : value) {
          encodeExtensionObject("ExtensionObject", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeDataValueArray(String field, DataValue[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (DataValue v : value) {
          encodeDataValue("DataValue", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeVariantArray(String field, Variant[] value) throws UaSerializationException {
    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Variant v : value) {
          encodeVariant("Variant", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeDiagnosticInfoArray(String field, DiagnosticInfo[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (DiagnosticInfo v : value) {
          encodeDiagnosticInfo("DiagnosticInfo", v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeEnumArray(String field, UaEnumeratedType[] value)
      throws UaSerializationException {

    if (beginField(field, value == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (UaEnumeratedType v : value) {
          // TODO should push the namespace from the DataTypeCodec
          // TODO should be the name from the DataTypeCodec
          encodeEnum(v.getClass().getSimpleName(), v);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeStructArray(String field, Object[] values, NodeId dataTypeId)
      throws UaSerializationException {

    if (beginField(field, values == null, true, true)) {
      try {
        namespaces.push(Namespaces.OPC_UA_XSD);

        for (Object v : values) {
          // TODO should push the namespace from the DataTypeCodec
          // TODO should be the name from the DataTypeCodec
          encodeStruct(v.getClass().getSimpleName(), v, dataTypeId);
        }
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  @Override
  public void encodeStructArray(String field, Object[] value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDateTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId));

    encodeStructArray(field, value, localDateTypeId);
  }

  @Override
  public void encodeMatrix(String field, Matrix value) throws UaSerializationException {}

  @Override
  public void encodeEnumMatrix(String field, Matrix value) throws UaSerializationException {}

  @Override
  public void encodeStructMatrix(String field, Matrix value, NodeId dataTypeId)
      throws UaSerializationException {}

  @Override
  public void encodeStructMatrix(String field, Matrix value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {}

  enum TypeHint {
    BUILTIN,
    ENUM,
    STRUCT,
    OPTION_SET
  }
}
