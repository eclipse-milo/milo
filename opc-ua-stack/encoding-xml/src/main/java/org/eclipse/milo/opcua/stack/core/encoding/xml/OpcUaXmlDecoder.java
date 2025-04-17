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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.*;

import com.google.common.io.CharStreams;
import jakarta.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.eclipse.milo.opcua.stack.core.util.SecureXmlUtil;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OpcUaXmlDecoder implements UaDecoder, AutoCloseable {

  private final DocumentBuilder builder;

  private Document document;
  private Node currentNode;

  private final EncodingContext context;

  public OpcUaXmlDecoder(EncodingContext context) {
    this.context = context;

    try {
      builder = SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, e);
    }
  }

  public OpcUaXmlDecoder(EncodingContext context, String xml) throws IOException, SAXException {
    this.context = context;

    try {
      builder = SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, e);
    }

    setInput(new StringReader(xml));
  }

  @Override
  public EncodingContext getEncodingContext() {
    return context;
  }

  @Override
  public void close() {
    // noop
  }

  public OpcUaXmlDecoder setInput(Document document) {
    this.document = document;
    this.currentNode = document.getFirstChild();

    return this;
  }

  public OpcUaXmlDecoder setInput(Reader reader) throws IOException, SAXException {
    return setInput(
        new ByteArrayInputStream(CharStreams.toString(reader).getBytes(StandardCharsets.UTF_8)));
  }

  public OpcUaXmlDecoder setInput(InputStream inputStream) throws IOException, SAXException {
    return setInput(builder.parse(inputStream));
  }

  private boolean currentNode(String field) throws UaSerializationException {
    if (currentNode == null) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, "currentNode==null");
    }

    return field == null || field.equals(currentNode.getLocalName());
  }

  @Override
  public Boolean decodeBoolean(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseBoolean(currentNode.getTextContent());
      } catch (IllegalArgumentException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return false;
    }
  }

  @Override
  public Byte decodeSByte(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseByte(currentNode.getTextContent());
      } catch (IllegalArgumentException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return (byte) 0;
    }
  }

  @Override
  public Short decodeInt16(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseShort(currentNode.getTextContent());
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0;
    }
  }

  @Override
  public Integer decodeInt32(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseInt(currentNode.getTextContent());
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0;
    }
  }

  @Override
  public Long decodeInt64(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseLong(currentNode.getTextContent());
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0L;
    }
  }

  @Override
  public UByte decodeByte(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return ubyte(DatatypeConverter.parseShort(currentNode.getTextContent()));
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return UByte.MIN;
    }
  }

  @Override
  public UShort decodeUInt16(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return ushort(DatatypeConverter.parseInt(currentNode.getTextContent()));
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return UShort.MIN;
    }
  }

  @Override
  public UInteger decodeUInt32(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return uint(DatatypeConverter.parseLong(currentNode.getTextContent()));
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return UInteger.MIN;
    }
  }

  @Override
  public ULong decodeUInt64(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return ulong(DatatypeConverter.parseInteger(currentNode.getTextContent()));
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return ULong.MIN;
    }
  }

  @Override
  public Float decodeFloat(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseFloat(currentNode.getTextContent());
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0f;
    }
  }

  @Override
  public Double decodeDouble(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return DatatypeConverter.parseDouble(currentNode.getTextContent());
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0.0;
    }
  }

  @Override
  public String decodeString(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return currentNode.getTextContent();
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return null;
    }
  }

  @Override
  public DateTime decodeDateTime(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        Calendar calendar = DatatypeConverter.parseDateTime(currentNode.getTextContent());

        return new DateTime(calendar.getTime());
      } catch (IllegalArgumentException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return DateTime.NULL_VALUE;
    }
  }

  @Override
  public UUID decodeGuid(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return UUID.fromString(currentNode.getTextContent().trim());
      } catch (IllegalArgumentException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return new UUID(0L, 0L);
    }
  }

  @Override
  public ByteString decodeByteString(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        String textContent = currentNode.getTextContent().trim();
        if (textContent.isEmpty()) {
          return ByteString.NULL_VALUE;
        } else {
          byte[] bs = DatatypeConverter.parseBase64Binary(textContent);

          return ByteString.of(bs);
        }
      } catch (IllegalArgumentException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return ByteString.NULL_VALUE;
    }
  }

  @Override
  public XmlElement decodeXmlElement(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        return nodeToXmlElement(currentNode.getFirstChild());
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return XmlElement.of(null);
    }
  }

  @Override
  public NodeId decodeNodeId(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node idNode = currentNode.getFirstChild();

      try {
        if (idNode != null) {
          String textContent = idNode.getTextContent();

          NodeId nodeId =
              NodeId.parseSafe(textContent)
                  .orElseThrow(
                      () ->
                          new UaSerializationException(
                              StatusCodes.Bad_DecodingError, "invalid NodeId: " + textContent));

          return reindexNodeId(nodeId);
        } else {
          return NodeId.NULL_VALUE;
        }
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return NodeId.NULL_VALUE;
    }
  }

  @Override
  public ExpandedNodeId decodeExpandedNodeId(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node expandedIdNode = currentNode.getFirstChild();

      try {
        if (expandedIdNode != null) {
          ExpandedNodeId xni = ExpandedNodeId.parse(expandedIdNode.getTextContent());

          return reindexExpandedNodeId(xni);
        } else {
          return ExpandedNodeId.NULL_VALUE;
        }
      } catch (UaRuntimeException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return ExpandedNodeId.NULL_VALUE;
    }
  }

  @Override
  public StatusCode decodeStatusCode(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        long code = 0L;

        Node codeNode = currentNode.getFirstChild();

        if (codeNode != null) {
          code = DatatypeConverter.parseUnsignedInt(codeNode.getTextContent());
        }

        return new StatusCode(code);
      } catch (NumberFormatException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return new StatusCode(0L);
    }
  }

  @Override
  public QualifiedName decodeQualifiedName(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        Map<String, Node> children = nodeMap(currentNode.getChildNodes());

        int namespaceIndex = 0;
        String name = null;

        Node namespaceIndexNode = children.get("NamespaceIndex");
        if (namespaceIndexNode != null) {
          namespaceIndex = DatatypeConverter.parseInt(namespaceIndexNode.getTextContent());
        }

        Node nameNode = children.get("Name");
        if (nameNode != null) {
          name = nameNode.getTextContent();
        }

        return reindexQualifiedName(new QualifiedName(namespaceIndex, name));
      } catch (Throwable t) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, t);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return QualifiedName.NULL_VALUE;
    }
  }

  @Override
  public LocalizedText decodeLocalizedText(String field) throws UaSerializationException {
    if (currentNode(field)) {
      try {
        Map<String, Node> children = nodeMap(currentNode.getChildNodes());

        String locale = null;
        String text = null;

        Node localeNode = children.get("Locale");
        if (localeNode != null) {
          locale = localeNode.getTextContent();
        }

        Node textNode = children.get("Text");
        if (textNode != null) {
          text = textNode.getTextContent();
        }

        return new LocalizedText(locale, text);
      } catch (Throwable t) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, t);
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return LocalizedText.NULL_VALUE;
    }
  }

  @Override
  public ExtensionObject decodeExtensionObject(String field) throws UaSerializationException {
    NodeId typeId = NodeId.NULL_VALUE;

    ExtensionObject extensionObject = ExtensionObject.of(XmlElement.NULL_VALUE, NodeId.NULL_VALUE);

    if (currentNode(field)) {
      Node node = currentNode;

      try {
        Map<String, Node> children = nodeMap(currentNode.getChildNodes());

        Node typeIdNode = children.get("TypeId");
        if (typeIdNode != null) {
          currentNode = typeIdNode;
          typeId = decodeNodeId("TypeId");
        }

        Node bodyNode = children.get("Body");
        if (bodyNode != null) {
          if ("ByteString".equals(bodyNode.getLocalName())
              && Namespaces.OPC_UA_XSD.equals(bodyNode.getNamespaceURI())) {

            currentNode = bodyNode;

            extensionObject = ExtensionObject.of(decodeByteString("ByteString"), typeId);
          } else {
            extensionObject =
                ExtensionObject.of(nodeToXmlElement(bodyNode.getFirstChild()), typeId);
          }
        }

        return extensionObject;
      } catch (Throwable t) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, t);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return extensionObject;
    }
  }

  @Override
  public DataValue decodeDataValue(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      Map<String, Node> children = nodeMap(currentNode.getChildNodes());

      Variant value = Variant.NULL_VALUE;
      StatusCode statusCode = StatusCode.GOOD;
      DateTime sourceTimestamp = null;
      UShort sourcePicoseconds = null;
      DateTime serverTimestamp = null;
      UShort serverPicoseconds = null;

      try {
        Node valueNode = children.get("Value");
        if (valueNode != null) {
          currentNode = valueNode;
          value = decodeVariant("Value");
        }

        Node statusCodeNode = children.get("StatusCode");
        if (statusCodeNode != null) {
          currentNode = statusCodeNode;
          statusCode = decodeStatusCode("StatusCode");
        }

        Node sourceTimestampNode = children.get("SourceTimestamp");
        if (sourceTimestampNode != null) {
          currentNode = sourceTimestampNode;
          sourceTimestamp = decodeDateTime("SourceTimestamp");
        }

        Node sourcePicosecondsNode = children.get("SourcePicoseconds");
        if (sourcePicosecondsNode != null) {
          currentNode = sourcePicosecondsNode;
          sourcePicoseconds = decodeUInt16("SourcePicoseconds");
        }

        Node serverTimestampNode = children.get("ServerTimestamp");
        if (serverTimestampNode != null) {
          currentNode = serverTimestampNode;
          serverTimestamp = decodeDateTime("ServerTimestamp");
        }

        Node serverPicosecondsNode = children.get("ServerPicoseconds");
        if (serverPicosecondsNode != null) {
          currentNode = serverPicosecondsNode;
          serverPicoseconds = decodeUInt16("ServerPicoseconds");
        }

        return new DataValue(
            value,
            statusCode,
            sourceTimestamp,
            sourcePicoseconds,
            serverTimestamp,
            serverPicoseconds);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return new DataValue(Variant.NULL_VALUE);
    }
  }

  @Override
  public Variant decodeVariant(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      try {
        currentNode = node.getFirstChild().getFirstChild();

        if (currentNode == null) {
          return Variant.NULL_VALUE;
        } else {
          Object value = decodeVariantValue();
          return new Variant(value);
        }
      } catch (Throwable t) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, t);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return Variant.NULL_VALUE;
    }
  }

  public Object decodeVariantValue() {
    if (currentNode(null)) {
      Node node = currentNode;

      String nodeName = node.getLocalName();

      if (nodeName.startsWith("ListOf")) {
        String type = nodeName.substring(6);

        List<Object> values = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
          currentNode = childNodes.item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            values.add(readBuiltinType(type, type));
          }
        }

        Object array = Array.newInstance(builtinTypeClass(type), values.size());
        for (int i = 0; i < values.size(); i++) {
          Array.set(array, i, values.get(i));
        }

        return array;
      } else if (nodeName.equals("Matrix")) {
        List<Integer> dimensions = new ArrayList<>();
        Node child = node.getFirstChild();
        for (int i = 0; i < child.getChildNodes().getLength(); i++) {
          currentNode = child.getChildNodes().item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            dimensions.add(decodeInt32("Int32"));
          }
        }

        List<Object> elements = new ArrayList<>();
        child = child.getNextSibling();
        for (int i = 0; i < child.getChildNodes().getLength(); i++) {
          currentNode = child.getChildNodes().item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            String type = currentNode.getLocalName();
            elements.add(readBuiltinType(type, type));
          }
        }

        Class<?> clazz = elements.get(0).getClass();
        Object array = Array.newInstance(clazz, elements.size());
        for (int i = 0; i < elements.size(); i++) {
          Array.set(array, i, elements.get(i));
        }

        int[] dims = new int[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
          dims[i] = dimensions.get(i);
        }

        return new Matrix(array, dims);
      } else {
        return readBuiltinType(nodeName, nodeName);
      }
    } else {
      return null;
    }
  }

  private Object readBuiltinType(String field, String type) {
    return switch (type) {
      case "Boolean" -> decodeBoolean(field);
      case "SByte" -> decodeSByte(field);
      case "Int16" -> decodeInt16(field);
      case "Int32" -> decodeInt32(field);
      case "Int64" -> decodeInt64(field);
      case "Byte" -> decodeByte(field);
      case "UInt16" -> decodeUInt16(field);
      case "UInt32" -> decodeUInt32(field);
      case "UInt64" -> decodeUInt64(field);
      case "Float" -> decodeFloat(field);
      case "Double" -> decodeDouble(field);
      case "String" -> decodeString(field);
      case "DateTime" -> decodeDateTime(field);
      case "Guid" -> decodeGuid(field);
      case "ByteString" -> decodeByteString(field);
      case "XmlElement" -> decodeXmlElement(field);
      case "NodeId" -> decodeNodeId(field);
      case "ExpandedNodeId" -> decodeExpandedNodeId(field);
      case "StatusCode" -> decodeStatusCode(field);
      case "QualifiedName" -> decodeQualifiedName(field);
      case "LocalizedText" -> decodeLocalizedText(field);
      case "ExtensionObject" -> decodeExtensionObject(field);
      case "DataValue" -> decodeDataValue(field);
      case "Variant" -> decodeVariant(field);
      case "DiagnosticInfo" -> decodeDiagnosticInfo(field);
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "not builtin type: " + type);
    };
  }

  private static Class<?> builtinTypeClass(String type) {
    return switch (type) {
      case "Boolean" -> Boolean.class;
      case "SByte" -> Byte.class;
      case "Int16" -> Short.class;
      case "Int32" -> Integer.class;
      case "Int64" -> Long.class;
      case "Byte" -> UByte.class;
      case "UInt16" -> UShort.class;
      case "UInt32" -> UInteger.class;
      case "UInt64" -> ULong.class;
      case "Float" -> Float.class;
      case "Double" -> Double.class;
      case "String" -> String.class;
      case "DateTime" -> DateTime.class;
      case "Guid" -> UUID.class;
      case "ByteString" -> ByteString.class;
      case "XmlElement" -> XmlElement.class;
      case "NodeId" -> NodeId.class;
      case "ExpandedNodeId" -> ExpandedNodeId.class;
      case "StatusCode" -> StatusCode.class;
      case "QualifiedName" -> QualifiedName.class;
      case "LocalizedText" -> LocalizedText.class;
      case "ExtensionObject" -> ExtensionObject.class;
      case "DataValue" -> DataValue.class;
      case "Variant" -> Variant.class;
      case "DiagnosticInfo" -> DiagnosticInfo.class;
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "not builtin type: " + type);
    };
  }

  @Override
  public DiagnosticInfo decodeDiagnosticInfo(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      Map<String, Node> children = nodeMap(node.getChildNodes());

      int symbolicId = -1;
      int namespaceUri = -1;
      int locale = -1;
      int localizedText = -1;
      String additionalInfo = null;
      StatusCode innerStatusCode = null;
      DiagnosticInfo innerDiagnosticInfo = null;

      Node child = children.get("SymbolicId");
      if (child != null) {
        currentNode = child;
        symbolicId = decodeInt32("SymbolicId");
      }

      child = children.get("NamespaceUri");
      if (child != null) {
        currentNode = child;
        namespaceUri = decodeInt32("NamespaceUri");
      }

      child = children.get("Locale");
      if (child != null) {
        currentNode = child;
        locale = decodeInt32("Locale");
      }

      child = children.get("LocalizedText");
      if (child != null) {
        currentNode = child;
        localizedText = decodeInt32("LocalizedText");
      }

      child = children.get("AdditionalInfo");
      if (child != null) {
        currentNode = child;
        additionalInfo = decodeString("AdditionalInfo");
      }

      child = children.get("InnerStatusCode");
      if (child != null) {
        currentNode = child;
        innerStatusCode = decodeStatusCode("InnerStatusCode");
      }

      child = children.get("InnerDiagnosticInfo");
      if (child != null) {
        currentNode = child;
        innerDiagnosticInfo = decodeDiagnosticInfo("InnerDiagnosticInfo");
      }

      try {
        return new DiagnosticInfo(
            namespaceUri,
            symbolicId,
            locale,
            localizedText,
            additionalInfo,
            innerStatusCode,
            innerDiagnosticInfo);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return DiagnosticInfo.NULL_VALUE;
    }
  }

  @Override
  public UaMessageType decodeMessage(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      String typeName = node.getLocalName();

      DataTypeCodec codec = null;

      DataTypeDictionary dictionary =
          context.getDataTypeManager().getTypeDictionary(Namespaces.OPC_UA_XSD);

      if (dictionary != null) {
        codec = dictionary.getCodec(String.format("//xs:element[@name='%s']", typeName));
      }

      if (codec != null) {
        currentNode = node.getFirstChild();

        try {
          return (UaMessageType) codec.decode(context, this);
        } finally {
          currentNode = node.getNextSibling();
        }
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "no codec registered: " + typeName);
      }
    } else {
      // TODO could be better if we passed Class<?> into method
      return null;
    }
  }

  @Override
  public Integer decodeEnum(String field) {
    if (currentNode(field)) {
      try {
        String s = currentNode.getTextContent();
        int lastIndex = s.lastIndexOf("_");

        if (lastIndex != -1) {
          try {
            return Integer.parseInt(s.substring(lastIndex + 1));
          } catch (Exception e) {
            throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
          }
        } else {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "invalid enum value: " + s);
        }
      } finally {
        currentNode = currentNode.getNextSibling();
      }
    } else {
      return 0;
    }
  }

  @Override
  public UaStructuredType decodeStruct(String field, NodeId dataTypeId)
      throws UaSerializationException {

    if (currentNode(field)) {
      Node node = currentNode;

      DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

      if (codec != null) {
        try {
          currentNode = node.getFirstChild();
          return codec.decode(context, this);
        } finally {
          currentNode = node.getNextSibling();
        }
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "no codec registered: " + dataTypeId);
      }
    } else {
      // TODO could be better if we passed Class<?> into method
      return null;
    }
  }

  @Override
  public UaStructuredType decodeStruct(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError, "namespace not registered: " + dataTypeId));

    return decodeStruct(field, localDataTypeId);
  }

  @Override
  public UaStructuredType decodeStruct(String field, DataTypeCodec codec)
      throws UaSerializationException {

    if (currentNode(field)) {
      Node node = currentNode;

      try {
        currentNode = node.getFirstChild();

        return codec.decode(context, this);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      // TODO could be better if we passed Class<?> into method
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T[] decodeArray(String field, Function<String, T> decoder, Class<T> clazz)
      throws UaSerializationException {

    if (currentNode(field)) {
      Node node = currentNode;

      List<Object> values = new ArrayList<>();
      Node listNode = node.getFirstChild();

      if (listNode != null) {
        NodeList children = listNode.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
          currentNode = children.item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            values.add(decoder.apply(currentNode.getLocalName()));
          }
        }
      }

      try {
        checkArrayLength(values.size());

        Object array = Array.newInstance(clazz, values.size());
        for (int i = 0; i < values.size(); i++) {
          Array.set(array, i, values.get(i));
        }

        return (T[]) array;
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return null;
    }
  }

  @Override
  public Boolean[] decodeBooleanArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeBoolean, Boolean.class);
  }

  @Override
  public Byte[] decodeSByteArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeSByte, Byte.class);
  }

  @Override
  public Short[] decodeInt16Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt16, Short.class);
  }

  @Override
  public Integer[] decodeInt32Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt32, Integer.class);
  }

  @Override
  public Long[] decodeInt64Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt64, Long.class);
  }

  @Override
  public UByte[] decodeByteArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeByte, UByte.class);
  }

  @Override
  public UShort[] decodeUInt16Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt16, UShort.class);
  }

  @Override
  public UInteger[] decodeUInt32Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt32, UInteger.class);
  }

  @Override
  public ULong[] decodeUInt64Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt64, ULong.class);
  }

  @Override
  public Float[] decodeFloatArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeFloat, Float.class);
  }

  @Override
  public Double[] decodeDoubleArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDouble, Double.class);
  }

  @Override
  public String[] decodeStringArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeString, String.class);
  }

  @Override
  public DateTime[] decodeDateTimeArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDateTime, DateTime.class);
  }

  @Override
  public UUID[] decodeGuidArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeGuid, UUID.class);
  }

  @Override
  public ByteString[] decodeByteStringArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeByteString, ByteString.class);
  }

  @Override
  public XmlElement[] decodeXmlElementArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeXmlElement, XmlElement.class);
  }

  @Override
  public NodeId[] decodeNodeIdArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeNodeId, NodeId.class);
  }

  @Override
  public ExpandedNodeId[] decodeExpandedNodeIdArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeExpandedNodeId, ExpandedNodeId.class);
  }

  @Override
  public StatusCode[] decodeStatusCodeArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeStatusCode, StatusCode.class);
  }

  @Override
  public QualifiedName[] decodeQualifiedNameArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeQualifiedName, QualifiedName.class);
  }

  @Override
  public LocalizedText[] decodeLocalizedTextArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeLocalizedText, LocalizedText.class);
  }

  @Override
  public ExtensionObject[] decodeExtensionObjectArray(String field)
      throws UaSerializationException {
    return decodeArray(field, this::decodeExtensionObject, ExtensionObject.class);
  }

  @Override
  public DataValue[] decodeDataValueArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDataValue, DataValue.class);
  }

  @Override
  public Variant[] decodeVariantArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeVariant, Variant.class);
  }

  @Override
  public DiagnosticInfo[] decodeDiagnosticInfoArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDiagnosticInfo, DiagnosticInfo.class);
  }

  @Override
  public Integer[] decodeEnumArray(String field) throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      List<Integer> values = new ArrayList<>();
      Node listNode = node.getFirstChild();

      if (listNode != null) {
        NodeList children = listNode.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
          currentNode = children.item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            values.add(decodeEnum(currentNode.getLocalName()));
          }
        }
      }

      try {
        return values.toArray(Integer[]::new);
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return null;
    }
  }

  @Override
  public UaStructuredType @Nullable [] decodeStructArray(String field, NodeId dataTypeId)
      throws UaSerializationException {
    if (currentNode(field)) {
      Node node = currentNode;

      DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "no codec registered: " + dataTypeId);
      }

      List<Object> values = new ArrayList<>();
      Node listNode = node.getFirstChild();

      if (listNode != null) {
        NodeList children = listNode.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
          currentNode = children.item(i);

          if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            values.add(decodeStruct(currentNode.getLocalName(), dataTypeId));
          }
        }
      }

      try {
        Object array = Array.newInstance(codec.getType(), values.size());
        for (int i = 0; i < values.size(); i++) {
          Array.set(array, i, values.get(i));
        }

        return (UaStructuredType[]) array;
      } finally {
        currentNode = node.getNextSibling();
      }
    } else {
      return null;
    }
  }

  @Override
  public UaStructuredType @Nullable [] decodeStructArray(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId dataTypeNodeId = dataTypeId.toNodeId(context.getNamespaceTable()).orElse(null);

    if (dataTypeNodeId != null) {
      return decodeStructArray(field, dataTypeNodeId);
    } else {
      if (dataTypeId.isLocal()) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "namespace not registered: " + dataTypeId.namespace());
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "ExpandedNodeId not local: " + dataTypeId);
      }
    }
  }

  @Override
  public Matrix decodeMatrix(String field, OpcUaDataType dataType) throws UaSerializationException {
    // TODO implement decodeMatrix
    if (currentNode(field)) {
      currentNode = currentNode.getNextSibling();
    }
    return Matrix.ofNull();
  }

  @Override
  public Matrix decodeEnumMatrix(String field) {
    if (currentNode(field)) {
      // TODO implement decodeEnumMatrix
      return Matrix.ofNull();
    } else {
      return Matrix.ofNull();
    }
  }

  @Override
  public Matrix decodeStructMatrix(String field, NodeId dataTypeId) {
    // TODO implement decodeStructMatrix
    if (currentNode(field)) {
      currentNode = currentNode.getNextSibling();
    }
    return Matrix.ofNull();
  }

  @Override
  public Matrix decodeStructMatrix(String field, ExpandedNodeId dataTypeId) {
    // TODO implement decodeStructMatrix
    if (currentNode(field)) {
      currentNode = currentNode.getNextSibling();
    }
    return Matrix.ofNull();
  }

  /**
   * Special overload for use when processing values from a UANodeSet.
   *
   * @param nodeId the NodeId to reindex.
   * @return the re-indexed NodeId.
   */
  protected NodeId reindexNodeId(NodeId nodeId) {
    return nodeId;
  }

  /**
   * Special overload for use when processing values from a UANodeSet.
   *
   * @param expandedNodeId the ExpandedNodeId to reindex.
   * @return the re-indexed ExpandedNodeId.
   */
  protected ExpandedNodeId reindexExpandedNodeId(ExpandedNodeId expandedNodeId) {
    return expandedNodeId;
  }

  /**
   * Special overload for use when processing values from a UANodeSet.
   *
   * @param qualifiedName the QualifiedName to reindex.
   * @return the re-indexed QualifiedName.
   */
  protected QualifiedName reindexQualifiedName(QualifiedName qualifiedName) {
    return qualifiedName;
  }

  private void checkArrayLength(int length) throws UaSerializationException {
    if (length > context.getEncodingLimits().getMaxMessageSize()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          String.format(
              "array length exceeds max message size (length=%s, max=%s)",
              length, context.getEncodingLimits().getMaxMessageSize()));
    }
  }

  private static XmlElement nodeToXmlElement(Node node) throws UaSerializationException {
    try {
      StringWriter sw = new StringWriter();

      Transformer transformer = SecureXmlUtil.SHARED_TRANSFORMER_FACTORY.newTransformer();
      transformer.setOutputProperty("omit-xml-declaration", "yes");
      transformer.transform(new DOMSource(node), new StreamResult(sw));

      return new XmlElement(sw.toString());
    } catch (TransformerException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  private static Map<String, Node> nodeMap(NodeList nodeList) {
    LinkedHashMap<String, Node> nodeMap = new LinkedHashMap<>();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      nodeMap.put(node.getLocalName(), node);
    }

    return nodeMap;
  }
}
