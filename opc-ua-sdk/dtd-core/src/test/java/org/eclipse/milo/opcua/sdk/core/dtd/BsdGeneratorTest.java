/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.opcfoundation.opcua.binaryschema.StructuredType;
import org.w3c.dom.Element;

class BsdGeneratorTest {

  // Array lengths and optional switches must survive dictionary export for other clients.
  @Test
  void generatesNamedFieldsAndPreservesArrayAndOptionalMetadata() throws Exception {
    String xml =
        """
        <opc:TypeDictionary xmlns:opc="http://opcfoundation.org/BinarySchema/"
            TargetNamespace="urn:test:dictionary" DefaultByteOrder="LittleEndian">
          <opc:StructuredType Name="Sample">
            <opc:Field Name="HasLabel" TypeName="opc:Bit"/>
            <opc:Field Name="Count" TypeName="opc:Int32"/>
            <opc:Field Name="Values" TypeName="opc:Double" LengthField="Count"/>
            <opc:Field Name="Label" TypeName="opc:String" SwitchField="HasLabel"/>
          </opc:StructuredType>
        </opc:TypeDictionary>
        """;
    var dictionary =
        BsdParser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    var output = new ByteArrayOutputStream();
    BsdGenerator.generate(dictionary, output);

    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Element root =
        factory
            .newDocumentBuilder()
            .parse(new ByteArrayInputStream(output.toByteArray()))
            .getDocumentElement();
    String namespace = "http://opcfoundation.org/BinarySchema/";
    assertEquals(namespace, root.getNamespaceURI());
    assertEquals("TypeDictionary", root.getLocalName());
    assertEquals("urn:test:dictionary", root.getAttribute("TargetNamespace"));
    assertEquals("LittleEndian", root.getAttribute("DefaultByteOrder"));
    var types = root.getElementsByTagNameNS(namespace, "StructuredType");
    assertEquals(1, types.getLength());
    var type = (Element) types.item(0);
    assertEquals("Sample", type.getAttribute("Name"));
    var fields = type.getElementsByTagNameNS(namespace, "Field");
    assertEquals(4, fields.getLength());
    var names = List.of("HasLabel", "Count", "Values", "Label");
    var fieldTypes = List.of("Bit", "Int32", "Double", "String");
    for (int i = 0; i < fields.getLength(); i++) {
      var field = (Element) fields.item(i);
      assertEquals(names.get(i), field.getAttribute("Name"));
      String qualifiedType = field.getAttribute("TypeName");
      int separator = qualifiedType.indexOf(':');
      String prefix = separator < 0 ? null : qualifiedType.substring(0, separator);
      String localName = qualifiedType.substring(separator + 1);
      assertEquals(namespace, field.lookupNamespaceURI(prefix));
      assertEquals(fieldTypes.get(i), localName);
    }
    assertEquals("Count", ((Element) fields.item(2)).getAttribute("LengthField"));
    assertEquals("HasLabel", ((Element) fields.item(3)).getAttribute("SwitchField"));

    var parsed = BsdParser.parse(new ByteArrayInputStream(output.toByteArray()));
    assertEquals("urn:test:dictionary", parsed.getTargetNamespace());
    assertEquals(1, parsed.getOpaqueTypeOrEnumeratedTypeOrStructuredType().size());
    var parsedType =
        assertInstanceOf(
            StructuredType.class, parsed.getOpaqueTypeOrEnumeratedTypeOrStructuredType().get(0));
    assertEquals("Sample", parsedType.getName());
    assertEquals(names, parsedType.getField().stream().map(field -> field.getName()).toList());
    for (int i = 0; i < fieldTypes.size(); i++) {
      assertEquals(
          new QName(namespace, fieldTypes.get(i)), parsedType.getField().get(i).getTypeName());
    }
    assertEquals("Count", parsedType.getField().get(2).getLengthField());
    assertEquals("HasLabel", parsedType.getField().get(3).getSwitchField());
  }
}
