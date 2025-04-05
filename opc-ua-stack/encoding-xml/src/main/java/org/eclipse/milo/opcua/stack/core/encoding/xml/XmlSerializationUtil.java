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
