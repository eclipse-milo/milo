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

import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import org.slf4j.LoggerFactory;

class XmlSerializationUtil {

  private XmlSerializationUtil() {}

  /**
   * Encodes a DataType Name or Structure Field Name for use in XML DataEncoding.
   *
   * <p>The rules applied are:
   *
   * <ol>
   *   <li>Any character that is not a Unicode Letter, Digit, '_', '-', or '.' is replaced by '_'.
   *   <li>If the resulting name starts with a character that is not a Unicode Letter, or if it
   *       starts with "xml" (case-insensitive), an underscore '_' is prepended.
   * </ol>
   *
   * @param name the original name string.
   * @return the encoded name string, modified according to the rules if necessary.
   */
  static String encodeXmlName(String name) {
    if (name == null || name.isEmpty()) {
      return name;
    }

    var sb = new StringBuilder(name.length());

    // Rule 1: Replace characters not permitted by the XML DataEncoding

    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);

      // Allowed characters are: Unicode Letters, Digits, '_', '-', '.'
      if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-' || c == '.') {
        sb.append(c);
      } else {
        // Replace non-permitted characters with an underscore
        sb.append('_');
      }
    }

    // Rule 2: Add prefix if the first character is not valid or if it starts with "xml"
    // (case-insensitive)

    String encoded = sb.toString();

    if (!encoded.isEmpty() && !Character.isLetter(encoded.charAt(0))) {
      return "_" + encoded;
    } else if (encoded.length() >= 3 && encoded.substring(0, 3).equalsIgnoreCase("xml")) {
      return "_" + encoded;
    } else {
      return encoded;
    }
  }

  static void writeXmlFragment(XMLStreamWriter writer, String xmlFragment)
      throws XMLStreamException {

    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(xmlFragment));

    while (reader.hasNext()) {
      int eventType = reader.next();

      switch (eventType) {
        case XMLEvent.START_ELEMENT:
          // Write start element, copying namespace declarations and attributes
          writeStartElement(reader, writer);
          break;
        case XMLEvent.END_ELEMENT:
          writer.writeEndElement();
          break;
        case XMLEvent.CHARACTERS:
          writer.writeCharacters(reader.getText());
          break;
        case XMLEvent.CDATA:
          writer.writeCData(reader.getText());
          break;
        case XMLEvent.COMMENT:
          writer.writeComment(reader.getText());
          break;
        case XMLEvent.PROCESSING_INSTRUCTION:
          writer.writeProcessingInstruction(reader.getPITarget(), reader.getPIData());
          break;

        // Ignore whitespace, START_DOCUMENT, END_DOCUMENT, DTD, etc. from the fragment
        case XMLEvent.SPACE:
        case XMLEvent.START_DOCUMENT:
        case XMLEvent.END_DOCUMENT:
        case XMLEvent.DTD:
        case XMLEvent.ATTRIBUTE: // Handled by writeStartElement helper
        case XMLEvent.NAMESPACE: // Handled by writeStartElement helper
          break;

        default:
          LoggerFactory.getLogger(XmlSerializationUtil.class)
              .warn("Unhandled event type: {}", eventType);
          break;
      }
    }

    reader.close();
  }

  private static void writeStartElement(XMLStreamReader reader, XMLStreamWriter writer)
      throws XMLStreamException {

    String prefix = reader.getPrefix();
    String namespaceURI = reader.getNamespaceURI();
    String localName = reader.getLocalName();

    // Write the start element itself
    if (namespaceURI != null && !namespaceURI.isEmpty()) {
      if (prefix != null && !prefix.isEmpty()) {
        writer.writeStartElement(prefix, localName, namespaceURI);
      } else {
        // Let the writer handle the default namespace if prefix is null/empty
        writer.writeStartElement(localName, namespaceURI);

        // Or explicitly declare default:
        // writer.setDefaultNamespace(namespaceURI);
        // writer.writeStartElement(localName);
      }
    } else {
      writer.writeStartElement(localName);
    }

    // Copy namespace declarations defined ON this element
    for (int i = 0; i < reader.getNamespaceCount(); i++) {
      writer.writeNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
    }

    // Copy attributes
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      String attrPrefix = reader.getAttributePrefix(i);
      String attrNsURI = reader.getAttributeNamespace(i);
      String attrLocalName = reader.getAttributeLocalName(i);
      String attrValue = reader.getAttributeValue(i);

      if (attrNsURI != null && !attrNsURI.isEmpty()) {
        if (attrPrefix != null && !attrPrefix.isEmpty()) {
          writer.writeAttribute(attrPrefix, attrNsURI, attrLocalName, attrValue);
        } else {
          // Attribute in default namespace (rare, usually un-prefixed means no namespace)
          // Or attribute in a specific namespace needing declaration
          writer.writeAttribute(attrNsURI, attrLocalName, attrValue);
        }
      } else {
        // Attribute with no namespace
        writer.writeAttribute(attrLocalName, attrValue);
      }
    }
  }
}
