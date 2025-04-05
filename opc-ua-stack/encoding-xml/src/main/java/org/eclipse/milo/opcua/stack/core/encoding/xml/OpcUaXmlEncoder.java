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

import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public class OpcUaXmlEncoder implements UaEncoder {

  private StringWriter xml;
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
      xmlStreamWriter = factory.createXMLStreamWriter(xml = new StringWriter());
      xmlStreamWriter.setPrefix("uax", Namespaces.OPC_UA_XSD);

      namespaces.clear();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  public String getDocumentXml() {
    try {
      xmlStreamWriter.flush();
      return xml.toString();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean beginField(String field) {
    try {
      if (field != null && !field.isEmpty()) {
        if (namespaces.peek() != null) {
          xmlStreamWriter.writeStartElement(namespaces.peek(), field);
        } else {
          xmlStreamWriter.writeStartElement(field);
        }
        return true;
      }
      return false;
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
        xmlStreamWriter.writeCharacters(value.toString());
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
        xmlStreamWriter.writeCharacters(value.toString());
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeInt16(String field, Short value) throws UaSerializationException {}

  @Override
  public void encodeInt32(String field, Integer value) throws UaSerializationException {}

  @Override
  public void encodeInt64(String field, Long value) throws UaSerializationException {}

  @Override
  public void encodeByte(String field, UByte value) throws UaSerializationException {
    if (beginField(field)) {
      try {
        xmlStreamWriter.writeCharacters(value.toString());
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        endField(field);
      }
    }
  }

  @Override
  public void encodeUInt16(String field, UShort value) throws UaSerializationException {}

  @Override
  public void encodeUInt32(String field, UInteger value) throws UaSerializationException {}

  @Override
  public void encodeUInt64(String field, ULong value) throws UaSerializationException {}

  @Override
  public void encodeFloat(String field, Float value) throws UaSerializationException {}

  @Override
  public void encodeDouble(String field, Double value) throws UaSerializationException {}

  @Override
  public void encodeString(String field, String value) throws UaSerializationException {}

  @Override
  public void encodeDateTime(String field, DateTime value) throws UaSerializationException {}

  @Override
  public void encodeGuid(String field, UUID value) throws UaSerializationException {}

  @Override
  public void encodeByteString(String field, ByteString value) throws UaSerializationException {}

  @Override
  public void encodeXmlElement(String field, XmlElement value) throws UaSerializationException {}

  @Override
  public void encodeNodeId(String field, NodeId value) throws UaSerializationException {}

  @Override
  public void encodeExpandedNodeId(String field, ExpandedNodeId value)
      throws UaSerializationException {}

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {}

  @Override
  public void encodeQualifiedName(String field, QualifiedName value)
      throws UaSerializationException {}

  @Override
  public void encodeLocalizedText(String field, LocalizedText value)
      throws UaSerializationException {}

  @Override
  public void encodeExtensionObject(String field, ExtensionObject value)
      throws UaSerializationException {}

  @Override
  public void encodeDataValue(String field, DataValue value) throws UaSerializationException {}

  @Override
  public void encodeVariant(String field, Variant value) throws UaSerializationException {
    if (beginField(field)) {
      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        xmlStreamWriter.writeStartElement(Namespaces.OPC_UA_XSD, "Value");
        encodeVariantValue(value);
        xmlStreamWriter.writeEndElement();
      } catch (XMLStreamException e) {
        throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
      } finally {
        namespaces.pop();

        endField(field);
      }
    }
  }

  private void encodeVariantValue(Variant value) {
    try {
      if (value.isNull()) {
        xmlStreamWriter.writeStartElement(Namespaces.OPC_UA_XSD, "Null");
        xmlStreamWriter.writeEndElement();
        return;
      }

      Object v = value.value();
      boolean isArray = v != null && v.getClass().isArray();

      OpcUaDataType dataType = value.getDataType().orElseThrow();

      namespaces.push(Namespaces.OPC_UA_XSD);
      try {
        if (isArray) {
          switch (dataType) {
            case Boolean -> encodeBooleanArray("ListOfBoolean", (Boolean[]) value.value());
          }
        } else {
          switch (dataType) {
            case Boolean -> encodeBoolean("Boolean", (Boolean) value.value());
          }
        }
      } finally {
        namespaces.pop();
      }

    } catch (XMLStreamException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
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
  public void encodeStruct(String field, Object value, NodeId dataTypeId)
      throws UaSerializationException {}

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
  public void encodeStruct(String field, Object value, DataTypeCodec codec)
      throws UaSerializationException {}

  @Override
  public void encodeBooleanArray(String field, Boolean[] value) throws UaSerializationException {
    if (beginField(field)) {
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
  public void encodeSByteArray(String field, Byte[] value) throws UaSerializationException {}

  @Override
  public void encodeInt16Array(String field, Short[] value) throws UaSerializationException {}

  @Override
  public void encodeInt32Array(String field, Integer[] value) throws UaSerializationException {}

  @Override
  public void encodeInt64Array(String field, Long[] value) throws UaSerializationException {}

  @Override
  public void encodeByteArray(String field, UByte[] value) throws UaSerializationException {}

  @Override
  public void encodeUInt16Array(String field, UShort[] value) throws UaSerializationException {}

  @Override
  public void encodeUInt32Array(String field, UInteger[] value) throws UaSerializationException {}

  @Override
  public void encodeUInt64Array(String field, ULong[] value) throws UaSerializationException {}

  @Override
  public void encodeFloatArray(String field, Float[] value) throws UaSerializationException {}

  @Override
  public void encodeDoubleArray(String field, Double[] value) throws UaSerializationException {}

  @Override
  public void encodeStringArray(String field, String[] value) throws UaSerializationException {}

  @Override
  public void encodeDateTimeArray(String field, DateTime[] value) throws UaSerializationException {}

  @Override
  public void encodeGuidArray(String field, UUID[] value) throws UaSerializationException {}

  @Override
  public void encodeByteStringArray(String field, ByteString[] value)
      throws UaSerializationException {}

  @Override
  public void encodeXmlElementArray(String field, XmlElement[] value)
      throws UaSerializationException {}

  @Override
  public void encodeNodeIdArray(String field, NodeId[] value) throws UaSerializationException {}

  @Override
  public void encodeExpandedNodeIdArray(String field, ExpandedNodeId[] value)
      throws UaSerializationException {}

  @Override
  public void encodeStatusCodeArray(String field, StatusCode[] value)
      throws UaSerializationException {}

  @Override
  public void encodeQualifiedNameArray(String field, QualifiedName[] value)
      throws UaSerializationException {}

  @Override
  public void encodeLocalizedTextArray(String field, LocalizedText[] value)
      throws UaSerializationException {}

  @Override
  public void encodeExtensionObjectArray(String field, ExtensionObject[] value)
      throws UaSerializationException {}

  @Override
  public void encodeDataValueArray(String field, DataValue[] value)
      throws UaSerializationException {}

  @Override
  public void encodeVariantArray(String field, Variant[] value) throws UaSerializationException {}

  @Override
  public void encodeDiagnosticInfoArray(String field, DiagnosticInfo[] value)
      throws UaSerializationException {}

  @Override
  public void encodeEnumArray(String field, UaEnumeratedType[] value)
      throws UaSerializationException {}

  @Override
  public void encodeStructArray(String field, Object[] values, NodeId dataTypeId)
      throws UaSerializationException {}

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
}
