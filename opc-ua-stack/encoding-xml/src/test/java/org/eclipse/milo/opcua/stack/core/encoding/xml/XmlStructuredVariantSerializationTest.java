/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.eclipse.milo.opcua.stack.core.util.SecureXmlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class XmlStructuredVariantSerializationTest {
  private final EncodingContext context = new DefaultEncodingContext();

  // Each null structure must remain an xsi:nil ExtensionObject element in the array.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureArrays")
  void structureArrayVariantRoundTrips(String name, UaStructuredType[] values) throws Exception {
    String xml = encode(new Variant(values));
    assertXmlElements(values, xml);

    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      ExtensionObject[] decoded =
          assertInstanceOf(ExtensionObject[].class, decoder.decodeVariant("Test").value());
      assertStructureElements(values, decoded);
    }
  }

  // Typed Matrix conversion must preserve nulls without invoking their XML structure codec.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureMatrices")
  void structureMatrixVariantRoundTrips(String name, Matrix matrix) throws Exception {
    UaStructuredType[] values = (UaStructuredType[]) matrix.getElements();
    String xml = encode(new Variant(matrix));
    assertXmlElements(values, xml);

    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      Matrix decoded = assertInstanceOf(Matrix.class, decoder.decodeVariant("Test").value());
      assertEquals(OpcUaDataType.ExtensionObject, decoded.getDataType().orElseThrow());
      assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
      assertStructureElements(
          values, assertInstanceOf(ExtensionObject[].class, decoded.getElements()));
    }
  }

  // Required array positions must not change omission of optional null scalar fields.
  @Test
  void nullExtensionObjectScalarFieldIsOmitted() throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeExtensionObject("OptionalField", null);
      assertEquals("", encoder.getOutputString());
    }
  }

  private String encode(Variant value) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeVariant("Test", value);
      return encoder.getOutputString();
    }
  }

  private static void assertXmlElements(UaStructuredType[] expected, String xml) throws Exception {
    var document =
        SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader(xml)));
    NodeList elements = document.getElementsByTagNameNS(Namespaces.OPC_UA_XSD, "ExtensionObject");
    assertEquals(expected.length, elements.getLength());
    for (int i = 0; i < expected.length; i++) {
      Element element = (Element) elements.item(i);
      assertEquals(
          expected[i] == null ? "true" : "",
          element.getAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil"),
          "XML nil at index " + i);
    }
  }

  private void assertStructureElements(UaStructuredType[] expected, ExtensionObject[] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      if (expected[i] == null) {
        assertTrue(actual[i].isNull(), "null structure at index " + i);
        assertEquals(NodeId.NULL_VALUE, actual[i].getEncodingOrTypeId());
      } else {
        assertEquals(expected[i], actual[i].decode(context), "structure at index " + i);
      }
    }
  }

  static Stream<Arguments> structureArrays() {
    XVType first = new XVType(1.0, 2.0f);
    XVType second = new XVType(3.0, 4.0f);
    return Stream.of(
        Arguments.of("concrete empty", new XVType[0]),
        Arguments.of("concrete all null", new XVType[4]),
        Arguments.of("concrete trailing null", new XVType[] {first, null}),
        Arguments.of("concrete mixed null", new XVType[] {null, first, second, null}),
        Arguments.of("concrete nonnull", new XVType[] {first, second, first, second}),
        Arguments.of("interface empty", new UaStructuredType[0]),
        Arguments.of("interface all null", new UaStructuredType[4]),
        Arguments.of("interface mixed null", new UaStructuredType[] {null, first, second, null}),
        Arguments.of("interface nonnull", new UaStructuredType[] {first, second, first, second}));
  }

  static Stream<Arguments> structureMatrices() {
    return structureArrays()
        .filter(arguments -> ((UaStructuredType[]) arguments.get()[1]).length > 0)
        .flatMap(
            arguments -> {
              Object[] values = arguments.get();
              String name = (String) values[0];
              UaStructuredType[] elements = (UaStructuredType[]) values[1];
              int rows = elements.length / 2;
              return Stream.of(
                  Arguments.of(name + " 2D", structureMatrix(elements, new int[] {rows, 2})),
                  Arguments.of(name + " 3D", structureMatrix(elements, new int[] {1, rows, 2})));
            });
  }

  private static Matrix structureMatrix(UaStructuredType[] elements, int[] dimensions) {
    // Explicit metadata supports XML encoding even when no non-null first element identifies a
    // type.
    return new Matrix(elements, dimensions, OpcUaDataType.ExtensionObject, XVType.TYPE_ID);
  }
}
