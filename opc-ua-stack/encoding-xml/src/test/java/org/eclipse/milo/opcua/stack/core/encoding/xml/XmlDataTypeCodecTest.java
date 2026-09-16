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

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

class XmlDataTypeCodecTest {
  private static final String MODEL = "urn:test:child";
  private static final String BASE = "urn:test:base";
  private static final String CHILD_XML = "urn:test:child:xml";
  private static final String BASE_XML = "urn:test:base:xml";
  private static final String INNER = "urn:test:inner";
  private static final ExpandedNodeId TYPE = ExpandedNodeId.parse("nsu=" + MODEL + ";s=Fixture");

  private final EncodingContext context = context();

  // The declaring model need not have a NodeId in the context; mappings are model URI metadata.
  @Test
  void mappingSnapshotQualifiesFieldsWithoutChangingNamespaceTable() throws Exception {
    var mappings = new HashMap<>(Map.of("Name", BASE));
    var codec = new XmlDataTypeCodec(CODEC, mappings);
    mappings.put("Name", "urn:changed");
    String[] namespaces = context.getNamespaceTable().toArray();
    Document doc = encode(new Fixture(e -> e.encodeString("Name", "base")), codec);
    assertEquals(BASE_XML, child(doc.getDocumentElement(), "Name").getNamespaceURI());
    assertNull(context.getNamespaceTable().getIndex(BASE));
    assertArrayEquals(namespaces, context.getNamespaceTable().toArray());
  }

  // Builtin components and array items have their own scopes even when names collide with fields.
  @Test
  void fieldMappingsDoNotLeakIntoBuiltinContentsOrArrayItems() throws Exception {
    Fixture value =
        new Fixture(
            e -> {
              e.encodeLocalizedText("Label", LocalizedText.english("hello"));
              e.encodeNodeId("Id", new NodeId(0, 42));
              e.encodeStringArray("Names", new String[] {"one", "two"});
              e.encodeString("After", "done");
            });
    var codec =
        new XmlDataTypeCodec(
            CODEC,
            Map.of(
                "Label",
                BASE,
                "Id",
                BASE,
                "Names",
                BASE,
                "Text",
                BASE,
                "Identifier",
                BASE,
                "String",
                BASE));
    Element root = encode(value, codec).getDocumentElement();
    assertEquals(BASE_XML, child(root, "Label").getNamespaceURI());
    assertEquals(Namespaces.OPC_UA_XSD, child(child(root, "Label"), "Text").getNamespaceURI());
    assertEquals(BASE_XML, child(root, "Id").getNamespaceURI());
    assertEquals(Namespaces.OPC_UA_XSD, child(child(root, "Id"), "Identifier").getNamespaceURI());
    assertEquals(BASE_XML, child(root, "Names").getNamespaceURI());
    assertEquals(Namespaces.OPC_UA_XSD, child(child(root, "Names"), "String").getNamespaceURI());
    assertEquals(CHILD_XML, child(root, "After").getNamespaceURI());
  }

  // Both structure entry points qualify a declared wrapper while isolating its contents. The
  // nested type deliberately shares the outer type's namespace to catch URI-based scope checks.
  @ParameterizedTest
  @ValueSource(strings = {"codec", "nodeId", "expandedNodeId", "inline"})
  void nestedStructuresRestoreTheEnclosingFieldMapping(String entry) throws Exception {
    Fixture nested = new Fixture(e -> e.encodeString("Name", "nested"));
    register(CODEC);
    Fixture outer =
        new Fixture(
            e -> {
              writeNested(e, nested, CODEC, entry);
              e.encodeString("After", "outer");
            });
    var codec = new XmlDataTypeCodec(CODEC, Map.of("Value", BASE, "Name", BASE, "After", BASE));
    Element root = encode(outer, codec).getDocumentElement();
    Element contents = entry.equals("inline") ? root : child(root, "Value");
    if (!entry.equals("inline")) {
      assertEquals(BASE_XML, contents.getNamespaceURI());
    }
    assertEquals(CHILD_XML, child(contents, "Name").getNamespaceURI());
    assertEquals(BASE_XML, child(root, "After").getNamespaceURI());
  }

  // Array wrappers belong to the declaring model; empty arrays preserve the same qualification.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void registeredArrayItemsUseTheirOwnAdapter(boolean empty) throws Exception {
    register(new XmlDataTypeCodec(CODEC, Map.of("Name", INNER)));
    Fixture item = new Fixture(e -> e.encodeString("Name", "nested"));
    Fixture outer =
        new Fixture(
            e -> {
              e.encodeStructArray("Items", empty ? new Fixture[0] : new Fixture[] {item}, TYPE);
              e.encodeString("After", "outer");
            });
    var codec =
        new XmlDataTypeCodec(
            CODEC, Map.of("Items", BASE, "Fixture", BASE, "Name", BASE, "After", BASE));
    Element root = encode(outer, codec).getDocumentElement();
    Element items = child(root, "Items");
    assertEquals(BASE_XML, items.getNamespaceURI());
    assertEquals(empty ? 0 : 1, items.getElementsByTagNameNS(CHILD_XML, "Fixture").getLength());
    assertEquals(empty ? 0 : 1, items.getElementsByTagNameNS(INNER, "Name").getLength());
    assertEquals(BASE_XML, child(root, "After").getNamespaceURI());
  }

  // A caught nested failure must not leave its namespace or mapping on subsequent outer fields.
  @ParameterizedTest
  @ValueSource(strings = {"codec", "nodeId", "expandedNodeId", "array"})
  void nestedCodecFailureRestoresNamespacesAndMetadata(String entry) throws Exception {
    var failure = new UaSerializationException(StatusCodes.Bad_EncodingError, "fixture failure");
    Fixture broken =
        new Fixture(
            e -> {
              throw failure;
            });
    DataTypeCodec inner = new XmlDataTypeCodec(CODEC, Map.of("After", INNER));
    register(inner);
    Fixture outer =
        new Fixture(
            e -> {
              assertSame(
                  failure,
                  assertThrows(
                      UaSerializationException.class, () -> writeNested(e, broken, inner, entry)));
              e.encodeString("After", "recovered");
            });
    Element root =
        encode(outer, new XmlDataTypeCodec(CODEC, Map.of("After", BASE))).getDocumentElement();
    assertEquals(BASE_XML, child(root, "After").getNamespaceURI());
    assertEquals("recovered", child(root, "After").getTextContent());
  }

  // A failed top-level adapter must not affect a subsequent undecorated structure or reset.
  @Test
  void failedAdapterDoesNotAffectSubsequentEncoding() throws Exception {
    var failure = new UaSerializationException(StatusCodes.Bad_EncodingError, "fixture failure");
    Fixture broken =
        new Fixture(
            e -> {
              throw failure;
            });
    Fixture good = new Fixture(e -> e.encodeString("Name", "normal"));
    try (var encoder = new OpcUaXmlEncoder(context)) {
      assertThrows(
          UaSerializationException.class,
          () ->
              encoder.encodeStruct(
                  "Broken", broken, new XmlDataTypeCodec(CODEC, Map.of("Name", BASE))));
      encoder.encodeStruct("Good", good, CODEC);
      Document doc = document("<Root>" + encoder.getOutputString() + "</Root>");
      assertEquals(
          CHILD_XML, child(child(doc.getDocumentElement(), "Good"), "Name").getNamespaceURI());
      encoder.reset();
      encoder.encodeStruct("Good", good, CODEC);
      assertEquals(
          CHILD_XML,
          child(document(encoder.getOutputString()).getDocumentElement(), "Name")
              .getNamespaceURI());
    }
  }

  @Test
  void standardModelUriUsesFixedUaSchemaAndUnknownUriFallsBack() throws Exception {
    Fixture value =
        new Fixture(
            e -> {
              e.encodeString("Standard", "ua");
              e.encodeString("Unmapped", "custom");
            });
    Element root =
        encode(
                value,
                new XmlDataTypeCodec(
                    CODEC, Map.of("Standard", Namespaces.OPC_UA, "Unmapped", INNER)))
            .getDocumentElement();
    assertEquals(Namespaces.OPC_UA_XSD, child(root, "Standard").getNamespaceURI());
    assertEquals(INNER, child(root, "Unmapped").getNamespaceURI());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\n"})
  void blankMetadataIsRejected(String blank) {
    assertThrows(
        IllegalArgumentException.class, () -> new XmlDataTypeCodec(CODEC, Map.of(blank, BASE)));
    assertThrows(
        IllegalArgumentException.class, () -> new XmlDataTypeCodec(CODEC, Map.of("Name", blank)));
  }

  @Test
  void nullMetadataIsRejected() {
    assertThrows(NullPointerException.class, () -> new XmlDataTypeCodec(null, Map.of()));
    assertThrows(NullPointerException.class, () -> new XmlDataTypeCodec(CODEC, null));
    var mappings = new HashMap<String, String>();
    mappings.put("Name", null);
    assertThrows(NullPointerException.class, () -> new XmlDataTypeCodec(CODEC, mappings));
  }

  private void writeNested(UaEncoder encoder, Fixture value, DataTypeCodec codec, String entry) {
    switch (entry) {
      case "codec" -> encoder.encodeStruct("Value", value, codec);
      case "nodeId" ->
          encoder.encodeStruct(
              "Value", value, TYPE.toNodeId(context.getNamespaceTable()).orElseThrow());
      case "expandedNodeId" -> encoder.encodeStruct("Value", value, TYPE);
      case "inline" -> encoder.encodeStruct(null, value, TYPE);
      case "array" -> encoder.encodeStructArray("Value", new Fixture[] {value}, TYPE);
      default -> throw new IllegalArgumentException(entry);
    }
  }

  private void register(DataTypeCodec codec) {
    context
        .getDataTypeManager()
        .registerType(
            TYPE.toNodeId(context.getNamespaceTable()).orElseThrow(), codec, null, null, null);
  }

  private Document encode(Fixture value, DataTypeCodec codec) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStruct("Fixture", value, codec);
      return document(encoder.getOutputString());
    }
  }

  private static EncodingContext context() {
    DataTypeManager manager = new DefaultDataTypeManager();
    var context =
        new DefaultEncodingContext() {
          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    context.getNamespaceTable().add(MODEL);
    return context.withXmlNamespaceUris(Map.of(MODEL, CHILD_XML, BASE, BASE_XML));
  }

  private static Document document(String xml) throws Exception {
    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
  }

  private static Element child(Element parent, String name) {
    for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
      if (node instanceof Element element && name.equals(element.getLocalName())) return element;
    }
    throw new AssertionError("no immediate child " + name + " in " + parent.getLocalName());
  }

  private record Fixture(Consumer<UaEncoder> fields) implements UaStructuredType {
    @Override
    public String getTypeName() {
      return "Fixture";
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return TYPE;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }

  private static final DataTypeCodec CODEC =
      new DataTypeCodec() {
        @Override
        public Class<?> getType() {
          return Fixture.class;
        }

        @Override
        public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
          throw new UnsupportedOperationException("XML namespace assertions inspect the DOM");
        }

        @Override
        public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
          ((Fixture) value).fields.accept(encoder);
        }
      };
}
