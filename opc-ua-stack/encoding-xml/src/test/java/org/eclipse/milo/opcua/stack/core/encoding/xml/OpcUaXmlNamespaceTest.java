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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class OpcUaXmlNamespaceTest {
  private static final String DI = "http://opcfoundation.org/UA/DI/";
  private static final String DI_XML = "http://opcfoundation.org/UA/DI/Types.xsd";
  private static final String AMB = "http://opcfoundation.org/UA/AMB/";
  private static final String AMB_XML = "http://opcfoundation.org/UA/AMB/Types.xsd";
  private static final String CUSTOM = "urn:milo:test:model";
  private static final String CUSTOM_XML = "urn:milo:test:xml";
  private static final Map<String, String> MAPPINGS =
      Map.of(DI, DI_XML, AMB, AMB_XML, CUSTOM, CUSTOM_XML);
  private static Schema schema;

  @BeforeAll
  static void loadOfflineSchemas() throws Exception {
    var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    // Imports have no schemaLocation. Supplying the complete pinned corpus resolves them offline.
    schema =
        factory.newSchema(
            new Source[] {
              source("Opc.Ua.Types.xsd"), source("Opc.Ua.Di.Types.xsd"),
              source("Opc.Ua.AMB.Types.xsd"), source("NamespaceFixtures.xsd"),
              source("Opc.Ua.Machinery_Result.Types.xsd"), source("Opc.Ua.Ijt.Base.Types.xsd")
            });
  }

  private static final String RESULT = "http://opcfoundation.org/UA/Machinery/Result/";
  private static final String IJT = "http://opcfoundation.org/UA/IJT/Base/";

  // The published child schema extends ResultMetaDataType and inherits its single mask.
  @Test
  void publishedJoiningSchemaAcceptsInheritedMaskAndFields() throws Exception {
    validate(
        """
        <j:JoiningResultMetaDataType
            xmlns:r="http://opcfoundation.org/UA/Machinery/Result/Types.xsd"
            xmlns:j="http://opcfoundation.org/UA/IJT/Base/Types.xsd">
          <r:EncodingMask>2097152</r:EncodingMask>
          <r:ResultId>result</r:ResultId>
          <j:Name>child</j:Name>
        </j:JoiningResultMetaDataType>
        """);
  }

  // A flattened optional codec must retain the declaring namespaces without a second mask.
  @Test
  void flattenedJoiningFieldsValidateAgainstPublishedSchema() throws Exception {
    EncodingContext context = context(false);
    context.getNamespaceTable().add(RESULT);
    context.getNamespaceTable().add(IJT);
    EncodingContext mapped =
        context.withXmlNamespaceUris(Map.of(RESULT, RESULT + "Types.xsd", IJT, IJT + "Types.xsd"));
    Fixture value =
        fixture(
            IJT,
            "JoiningResultMetaDataType",
            (c, e) -> {
              e.encodeUInt32("EncodingMask", UInteger.valueOf(1L << 21));
              e.encodeString("ResultId", "result");
              e.encodeString("Name", "child");
            });

    String original = encode(mapped, value);
    assertThrows(SAXException.class, () -> validate(original));

    DataTypeCodec adapted =
        new XmlDataTypeCodec(CODEC, Map.of("EncodingMask", RESULT, "ResultId", RESULT));
    String output = encode(mapped, value, adapted);
    validate(output);
    Document doc = document(output);
    assertEquals(1, doc.getElementsByTagNameNS("*", "EncodingMask").getLength());
    assertEquals(
        "2097152",
        doc.getElementsByTagNameNS(RESULT + "Types.xsd", "EncodingMask").item(0).getTextContent());
    assertEquals(
        "result",
        doc.getElementsByTagNameNS(RESULT + "Types.xsd", "ResultId").item(0).getTextContent());
    assertEquals(
        "child", doc.getElementsByTagNameNS(IJT + "Types.xsd", "Name").item(0).getTextContent());

    // Register the same adapter for type and encoding lookups, as an existing model library would.
    mapped
        .getDataTypeManager()
        .registerType(
            value.getTypeId().toNodeIdOrThrow(mapped.getNamespaceTable()),
            adapted,
            null,
            value.getXmlEncodingId().toNodeIdOrThrow(mapped.getNamespaceTable()),
            null);
    ExtensionObject encoded =
        ExtensionObject.encode(mapped, value, OpcUaDefaultXmlEncoding.getInstance());
    var xml = assertInstanceOf(ExtensionObject.Xml.class, encoded);
    validate(xml.getBody().getFragmentOrEmpty());

    // Variant conversion creates an encoder internally and must still find the registered adapter.
    Fixture container =
        fixture(CUSTOM, "Container", (c, e) -> e.encodeVariant("Value", new Variant(value)));
    Document variant = document(encode(mapped, container));
    Node body =
        variant.getElementsByTagNameNS(IJT + "Types.xsd", "JoiningResultMetaDataType").item(0);
    assertNotNull(body);
    schema.newValidator().validate(new DOMSource(body));
  }

  // Part 6 F.2: XmlSchemaUri identifies serialized elements; ModelUri identifies NodeIds.
  // The unmapped control must fail the same authoritative schema that accepts mapped output.
  @ParameterizedTest
  @MethodSource("modelValues")
  void modelValuesValidateOnlyWithSchemaNamespaces(Fixture value) throws Exception {
    EncodingContext original = context(false);
    String unmapped = encode(original, value);
    assertEquals(value.model, document(unmapped).getDocumentElement().getNamespaceURI());
    assertThrows(SAXException.class, () -> validate(unmapped));

    String mapped = encode(original.withXmlNamespaceUris(MAPPINGS), value);
    validate(mapped);
    assertEquals(
        MAPPINGS.get(value.model), document(mapped).getDocumentElement().getNamespaceURI());
  }

  // A default XML wrapper constructs its own encoder. URI mappings must follow the context and
  // must not replace model URIs or namespace indexes in its TypeId or in embedded NodeIds.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void internallyCreatedEncodersPreserveNodeIdIdentities(boolean shiftIndexes) throws Exception {
    EncodingContext original = context(shiftIndexes);
    String[] namespaces = original.getNamespaceTable().toArray();
    EncodingContext mapped = original.withXmlNamespaceUris(MAPPINGS);
    Fixture value = rootCause();
    register(mapped, value);

    ExtensionObject encoded =
        ExtensionObject.encode(mapped, value, OpcUaDefaultXmlEncoding.getInstance());
    var xml = assertInstanceOf(ExtensionObject.Xml.class, encoded);
    validate(xml.getBody().getFragmentOrEmpty());
    assertEquals(
        value.getXmlEncodingId().toNodeIdOrThrow(original.getNamespaceTable()),
        xml.getEncodingOrTypeId());

    Document body = document(xml.getBody().getFragmentOrEmpty());
    assertEquals(
        nodeId(mapped, DI, 42).toParseableString(),
        body.getElementsByTagNameNS(Namespaces.OPC_UA_XSD, "Identifier").item(0).getTextContent());
    assertArrayEquals(namespaces, original.getNamespaceTable().toArray());
    assertNull(original.getNamespaceTable().getIndex(AMB_XML));
    assertNull(original.getNamespaceTable().getIndex(DI_XML));

    Fixture wrapper =
        fixture(CUSTOM, "Wrapped", (c, e) -> e.encodeExtensionObject("Value", encoded));
    String output = encode(mapped, wrapper);
    validate(output);
    assertEquals(
        1, document(output).getElementsByTagNameNS(AMB_XML, "RootCauseDataType").getLength());
  }

  // Part 6 5.3.5: local field names belong to their declaring schema, including inherited fields.
  // This also exercises both NodeId overloads and restores the enclosing namespace after nesting.
  @Test
  void nestedCrossModelAndInheritedFieldsUseTheirDeclaringNamespaces() throws Exception {
    EncodingContext mapped = context(false).withXmlNamespaceUris(MAPPINGS);
    Fixture base = nameNodeId();
    Fixture nested = rootCause();
    register(mapped, base);
    register(mapped, nested);
    Fixture derived =
        fixture(
            CUSTOM,
            "Derived",
            (c, e) -> {
              e.encodeStruct(null, base, base.getTypeId());
              e.encodeStruct("Cause", nested, nodeId(c, AMB, nested.name));
              e.encodeInt32("After", 7);
            });

    String output = encode(mapped, derived);
    validate(output);
    Document doc = document(output);
    assertEquals(1, doc.getElementsByTagNameNS(AMB_XML, "Name").getLength());
    assertEquals(1, doc.getElementsByTagNameNS(CUSTOM_XML, "Cause").getLength());
    assertEquals(1, doc.getElementsByTagNameNS(AMB_XML, "RootCauseId").getLength());
    assertEquals("7", doc.getElementsByTagNameNS(CUSTOM_XML, "After").item(0).getTextContent());
  }

  // Structure and enumeration array items use their type's schema, while the array field belongs
  // to the enclosing model. Empty arrays must retain that same enclosing field namespace.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void crossModelArraysUseItemNamespaces(boolean empty) throws Exception {
    EncodingContext mapped = context(false).withXmlNamespaceUris(MAPPINGS);
    Fixture item = parameter();
    register(mapped, item);
    Fixture arrays =
        fixture(
            CUSTOM,
            "Arrays",
            (c, e) -> {
              e.encodeStructArray(
                  "Parameters",
                  empty ? new Fixture[0] : new Fixture[] {item, item},
                  item.getTypeId());
              e.encodeEnumArray("Health", empty ? new Health[0] : new Health[] {Health.NORMAL});
              e.encodeString("After", "done");
            });
    String output = encode(mapped, arrays);
    validate(output);
    Document doc = document(output);
    assertEquals(
        empty ? 0 : 2, doc.getElementsByTagNameNS(DI_XML, "ParameterResultDataType").getLength());
    assertEquals(
        empty ? 0 : 1, doc.getElementsByTagNameNS(DI_XML, "DeviceHealthEnumeration").getLength());
    assertEquals("done", doc.getElementsByTagNameNS(CUSTOM_XML, "After").item(0).getTextContent());
  }

  // Variants create Default XML ExtensionObjects inside the encoder. This protects propagation
  // through scalar, array, and matrix conversions rather than relying on direct wrapper calls.
  @ParameterizedTest(name = "{0}")
  @MethodSource("variantValues")
  void variantConversionsUseMappingsForInternallyEncodedBodies(
      String name, Object value, int expectedBodies) throws Exception {
    EncodingContext mapped = context(false).withXmlNamespaceUris(MAPPINGS);
    register(mapped, rootCause());
    Fixture container =
        fixture(CUSTOM, "VariantValue", (c, e) -> e.encodeVariant("Value", new Variant(value)));

    String output = encode(mapped, container);
    validate(output);
    Document doc = document(output);
    assertEquals(
        expectedBodies, doc.getElementsByTagNameNS(AMB_XML, "RootCauseDataType").getLength());
    assertEquals(0, doc.getElementsByTagNameNS(AMB, "RootCauseDataType").getLength());
  }

  private static Stream<Arguments> variantValues() {
    Fixture value = rootCause();
    return Stream.of(
        Arguments.of("scalar", value, 1),
        Arguments.of("array", new Fixture[] {value, value}, 2),
        Arguments.of("matrix", Matrix.ofStruct(new Fixture[][] {{value, value}}), 2));
  }

  // Existing contexts need no new implementation, and models already using their schema URI keep
  // the same output. A schema namespace shared with UA must not select UA Java class naming rules.
  @Test
  void missingAndIdentityMappingsPreserveOutputAndTypeNames() throws Exception {
    EncodingContext original = context(false);
    Fixture value = parameter();
    assertEquals(
        encode(original, value), encode(original.withXmlNamespaceUris(Map.of(DI, DI)), value));
    EncodingContext shared = original.withXmlNamespaceUris(Map.of(DI, Namespaces.OPC_UA_XSD));
    register(shared, value);
    var encoded = (ExtensionObject.Xml) OpcUaDefaultXmlEncoding.getInstance().encode(shared, value);
    assertEquals(
        "ParameterResultDataType",
        document(encoded.getBody().getFragmentOrEmpty()).getDocumentElement().getLocalName());
  }

  // Namespace zero is fixed by Part 6 5.3.1 even when custom model mappings are present.
  @Test
  void namespaceZeroRetainsStandardSchemaAndSymbolicName() throws Exception {
    EncodingContext mapped = new DefaultEncodingContext().withXmlNamespaceUris(MAPPINGS);
    var encoded =
        (ExtensionObject.Xml)
            OpcUaDefaultXmlEncoding.getInstance().encode(mapped, new XVType(1.0, 2.0f));
    String output = encoded.getBody().getFragmentOrEmpty();
    validate(output);
    assertEquals(Namespaces.OPC_UA_XSD, document(output).getDocumentElement().getNamespaceURI());
    assertEquals("XVType", document(output).getDocumentElement().getLocalName());
  }

  private static Stream<Fixture> modelValues() {
    Fixture parameter = parameter();
    return Stream.of(
        parameter,
        fixture(DI, "TransferResultErrorDataType", (c, e) -> e.encodeInt32("Status", -5)),
        fixture(
            DI,
            "TransferResultDataDataType",
            (c, e) -> {
              register(c, parameter);
              e.encodeInt32("SequenceNumber", 1);
              e.encodeBoolean("EndOfResults", true);
              e.encodeStructArray(
                  "ParameterDefs", new Fixture[] {parameter}, parameter.getTypeId());
            }),
        nameNodeId(),
        rootCause());
  }

  private static Fixture parameter() {
    return fixture(
        DI,
        "ParameterResultDataType",
        (c, e) -> {
          e.encodeQualifiedNameArray(
              "NodePath",
              new QualifiedName[] {
                new QualifiedName(c.getNamespaceTable().getIndex(DI), "Temperature")
              });
          e.encodeStatusCode("StatusCode", StatusCode.GOOD);
        });
  }

  private static Fixture nameNodeId() {
    return fixture(
        AMB,
        "NameNodeIdDataType",
        (c, e) -> {
          e.encodeLocalizedText("Name", LocalizedText.english("Pump"));
          e.encodeNodeId("NodeId", nodeId(c, AMB, 42));
        });
  }

  private static Fixture rootCause() {
    return fixture(
        AMB,
        "RootCauseDataType",
        (c, e) -> {
          e.encodeNodeId("RootCauseId", nodeId(c, DI, 42));
          e.encodeLocalizedText("RootCause", LocalizedText.english("Overtemperature"));
        });
  }

  private static Fixture fixture(
      String model, String name, BiConsumer<EncodingContext, UaEncoder> fields) {
    return new Fixture(model, name, fields);
  }

  private static EncodingContext context(boolean shiftIndexes) {
    DataTypeManager manager = new DefaultDataTypeManager();
    var context =
        new DefaultEncodingContext() {
          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    if (shiftIndexes) context.getNamespaceTable().add("urn:unrelated");
    context.getNamespaceTable().add(DI);
    context.getNamespaceTable().add(AMB);
    context.getNamespaceTable().add(CUSTOM);
    return context;
  }

  private static NodeId nodeId(EncodingContext context, String model, int id) {
    return new NodeId(context.getNamespaceTable().getIndex(model), id);
  }

  private static NodeId nodeId(EncodingContext context, String model, String id) {
    return new NodeId(context.getNamespaceTable().getIndex(model), id);
  }

  private static void register(EncodingContext context, Fixture value) {
    context
        .getDataTypeManager()
        .registerType(
            nodeId(context, value.model, value.name),
            CODEC,
            null,
            nodeId(context, value.model, value.name + ".Xml"),
            null);
  }

  private static String encode(EncodingContext context, Fixture value) throws Exception {
    return encode(context, value, CODEC);
  }

  private static String encode(EncodingContext context, Fixture value, DataTypeCodec codec)
      throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStruct(value.name, value, codec);
      return encoder.getOutputString();
    }
  }

  private static Source source(String name) {
    return new StreamSource(
        OpcUaXmlNamespaceTest.class.getResource("/schema/" + name).toExternalForm());
  }

  private static void validate(String xml) throws Exception {
    var validator = schema.newValidator();
    validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    validator.validate(new DOMSource(document(xml)));
  }

  private static Document document(String xml) throws Exception {
    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
  }

  // Hand-written field layouts are deliberately independent of Milo's XML decoder. Test-only
  // string NodeIds identify these schema-shaped values; they are not the models' published IDs.
  private record Fixture(String model, String name, BiConsumer<EncodingContext, UaEncoder> fields)
      implements UaStructuredType {
    @Override
    public String getTypeName() {
      return name;
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.parse("nsu=" + model + ";s=" + name);
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.parse("nsu=" + model + ";s=" + name + ".Xml");
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public String toString() {
      return name;
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
          throw new UnsupportedOperationException("Schema validation is the test oracle");
        }

        @Override
        public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
          ((Fixture) value).fields.accept(context, encoder);
        }
      };

  private enum Health implements UaEnumeratedType {
    NORMAL;

    @Override
    public String getName() {
      return "NORMAL";
    }

    @Override
    public int getValue() {
      return 0;
    }

    @Override
    public String getTypeName() {
      return "DeviceHealthEnumeration";
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.parse("nsu=" + DI + ";i=6244");
    }
  }
}
