/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.io.StringReader;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * A structure with a Matrix field "Values" (ValueRank 2) followed by an Int32 field "Tail". The
 * Matrix is null in every case, so the test shows that {@link JsonStructCodec} treats a null Matrix
 * as a valid field value for each element type, in each encoding, and keeps the field after it
 * intact.
 */
class JsonStructCodecNullMatrixTest {

  private static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

  private static final String NULL_MATRIX_THEN_TAIL = "{\"Values\":null,\"Tail\":42}";
  private static final String TAIL_ONLY = "{\"Tail\":42}";

  /** The Matrix element type decides which decode and encode branch the codec takes. */
  enum ElementType {
    BUILTIN(NodeIds.Int32, StructureType.Structure, false),
    ENUM(NodeIds.ApplicationType, StructureType.Structure, false),
    STRUCT(NodeIds.XVType, StructureType.Structure, false),
    SUBTYPED(NodeIds.XVType, StructureType.StructureWithSubtypedValues, true);

    final NodeId dataTypeId;
    final StructureType structureType;
    final boolean optional;

    ElementType(NodeId dataTypeId, StructureType structureType, boolean optional) {
      this.dataTypeId = dataTypeId;
      this.structureType = structureType;
      this.optional = optional;
    }
  }

  enum Format {
    BINARY,
    XML,
    COMPACT,
    VERBOSE
  }

  enum Member {
    JSON_NULL,
    OMITTED
  }

  record Wire(Format format, String input) {}

  // Part 6 §5.4.6 Table 45: a null field is omitted in compact JSON and written as JSON null in
  // verbose JSON. A null Matrix member, whether JSON null or absent from the JsonStruct, must
  // encode as a null Matrix for every element type and decode back as a null member.
  @ParameterizedTest
  @MethodSource("encodeCases")
  void encodesNullMatrixMemberAndDecodesItBack(
      ElementType elementType, Format format, Member member) throws Exception {
    EncodingContext context = context();
    Fixture fixture = fixture(elementType);
    JsonObject members = parse(member == Member.OMITTED ? TAIL_ONLY : NULL_MATRIX_THEN_TAIL);
    var original = new JsonStruct(fixture.dataType, members);

    JsonStruct decoded;
    switch (format) {
      case BINARY -> {
        byte[] bytes = encodeBinary(context, fixture.codec, original);
        // Dimensions length -1 marks a null Matrix (Part 6 §5.2.5), then Int32 42.
        assertEquals("ffffffff2a000000", ByteBufUtil.hexDump(bytes));
        decoded = decodeBinary(context, fixture.codec, bytes);
      }
      case XML -> {
        String xml = encodeXml(context, fixture.codec, original);
        assertNullValuesElement(xml);
        decoded = decodeXml(context, fixture.codec, xml);
      }
      case COMPACT -> {
        String json = encodeJson(Encoding.COMPACT, context, fixture.codec, original);
        assertEquals(parse(TAIL_ONLY), parse(json), "compact JSON omits a null field");
        decoded = decodeJson(Encoding.COMPACT, context, fixture.codec, json);
      }
      default -> {
        String json = encodeJson(Encoding.VERBOSE, context, fixture.codec, original);
        assertEquals(parse(NULL_MATRIX_THEN_TAIL), parse(json), "verbose JSON writes null");
        decoded = decodeJson(Encoding.VERBOSE, context, fixture.codec, json);
      }
    }

    assertEquals(parse(NULL_MATRIX_THEN_TAIL), members(decoded));
  }

  static Stream<Arguments> encodeCases() {
    return Stream.of(ElementType.values())
        .flatMap(
            e ->
                Stream.of(Format.values())
                    .flatMap(f -> Stream.of(Member.values()).map(m -> Arguments.of(e, f, m))));
  }

  // Each encoding has its own representation of a null Matrix field. The decoders map all of them
  // to a null Matrix, and the codec must accept that for enum and structure elements too, instead
  // of indexing into the Matrix's empty dimensions.
  @ParameterizedTest
  @MethodSource("decodeCases")
  void decodesNullMatrixFieldFromEachEncoding(ElementType elementType, Wire wire) throws Exception {
    EncodingContext context = context();
    Fixture fixture = fixture(elementType);

    JsonStruct decoded =
        switch (wire.format) {
          case BINARY ->
              decodeBinary(context, fixture.codec, ByteBufUtil.decodeHexDump(wire.input));
          case XML -> decodeXml(context, fixture.codec, wire.input);
          case COMPACT -> decodeJson(Encoding.COMPACT, context, fixture.codec, wire.input);
          case VERBOSE -> decodeJson(Encoding.VERBOSE, context, fixture.codec, wire.input);
        };

    assertEquals(parse(NULL_MATRIX_THEN_TAIL), members(decoded));
  }

  static Stream<Arguments> decodeCases() {
    Stream<Wire> wires =
        Stream.of(
            new Wire(Format.BINARY, "ffffffff2a000000"),
            new Wire(
                Format.XML,
                "<Value xmlns:xsi=\""
                    + XSI_NS
                    + "\"><Values xsi:nil=\"true\"/><Tail>42</Tail></Value>"),
            new Wire(Format.XML, "<Value><Tail>42</Tail></Value>"),
            new Wire(Format.COMPACT, TAIL_ONLY),
            new Wire(Format.VERBOSE, NULL_MATRIX_THEN_TAIL));
    return wires.flatMap(w -> Stream.of(ElementType.values()).map(e -> Arguments.of(e, w)));
  }

  // Control: a populated Matrix member still encodes its dimensions and elements, so the null
  // handling does not swallow real values.
  @ParameterizedTest
  @EnumSource(Format.class)
  void populatedMatrixMemberStillRoundTrips(Format format) throws Exception {
    EncodingContext context = context();
    Fixture fixture = fixture(ElementType.BUILTIN);
    JsonObject members = parse("{\"Values\":[[1,2],[3,4]],\"Tail\":42}");
    var original = new JsonStruct(fixture.dataType, members);

    JsonStruct decoded;
    switch (format) {
      case BINARY -> {
        byte[] bytes = encodeBinary(context, fixture.codec, original);
        // 2 dimensions of 2, four Int32 elements, then Int32 42.
        assertEquals(
            "020000000200000002000000" + "01000000020000000300000004000000" + "2a000000",
            ByteBufUtil.hexDump(bytes));
        decoded = decodeBinary(context, fixture.codec, bytes);
      }
      case XML ->
          decoded = decodeXml(context, fixture.codec, encodeXml(context, fixture.codec, original));
      case COMPACT ->
          decoded =
              decodeJson(
                  Encoding.COMPACT,
                  context,
                  fixture.codec,
                  encodeJson(Encoding.COMPACT, context, fixture.codec, original));
      default -> {
        String json = encodeJson(Encoding.VERBOSE, context, fixture.codec, original);
        // Part 6 §5.4.5: a multi-dimensional array is a flat Array plus its Dimensions.
        assertEquals(
            parse("{\"Values\":{\"Array\":[1,2,3,4],\"Dimensions\":[2,2]},\"Tail\":42}"),
            parse(json));
        decoded = decodeJson(Encoding.VERBOSE, context, fixture.codec, json);
      }
    }

    assertEquals(members, members(decoded));
  }

  private record Fixture(DataType dataType, JsonStructCodec codec) {}

  private static Fixture fixture(ElementType elementType) {
    var tree = mock(DataTypeTree.class);
    when(tree.isEnumType(NodeIds.ApplicationType)).thenReturn(true);
    when(tree.isStructType(NodeIds.XVType)).thenReturn(true);

    var dataType = mock(DataType.class);
    when(dataType.getNodeId()).thenReturn(new NodeId(1, "NullMatrixExample"));
    when(dataType.getBrowseName()).thenReturn(new QualifiedName(1, "Value"));
    when(dataType.getDataTypeDefinition())
        .thenReturn(
            new StructureDefinition(
                NodeId.NULL_VALUE,
                NodeIds.Structure,
                elementType.structureType,
                new StructureField[] {
                  field("Values", elementType.dataTypeId, 2, elementType.optional),
                  field("Tail", NodeIds.Int32, -1, false)
                }));

    return new Fixture(dataType, new JsonStructCodec(dataType, tree));
  }

  private static StructureField field(String name, NodeId type, int rank, boolean optional) {
    return new StructureField(name, LocalizedText.NULL_VALUE, type, rank, null, uint(0), optional);
  }

  private static EncodingContext context() {
    var context = new DefaultEncodingContext();
    context.getNamespaceTable().add("urn:eclipse:milo:test:null-matrix");
    return context;
  }

  private static byte[] encodeBinary(
      EncodingContext context, JsonStructCodec codec, JsonStruct value) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeStruct(null, value, codec);
      return ByteBufUtil.getBytes(buffer);
    } finally {
      buffer.release();
    }
  }

  private static JsonStruct decodeBinary(
      EncodingContext context, JsonStructCodec codec, byte[] bytes) {
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    try {
      var decoded =
          (JsonStruct) new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeStruct(null, codec);
      assertEquals(0, buffer.readableBytes(), "the Matrix and Tail consume the whole buffer");
      return decoded;
    } finally {
      buffer.release();
    }
  }

  private static String encodeXml(EncodingContext context, JsonStructCodec codec, JsonStruct value)
      throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStruct("Value", value, codec);
      return encoder.getOutputString();
    }
  }

  private static JsonStruct decodeXml(EncodingContext context, JsonStructCodec codec, String xml)
      throws Exception {
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      return (JsonStruct) decoder.decodeStruct(null, codec);
    }
  }

  private static String encodeJson(
      Encoding encoding, EncodingContext context, JsonStructCodec codec, JsonStruct value)
      throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, value, codec);
      return encoder.getOutputString();
    }
  }

  private static JsonStruct decodeJson(
      Encoding encoding, EncodingContext context, JsonStructCodec codec, String json) {
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(encoding);
    return (JsonStruct) decoder.decodeStruct(null, codec);
  }

  /** Part 6 §5.3.1.2 allows a null XML field to be omitted or written with xsi:nil="true". */
  private static void assertNullValuesElement(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

    NodeList values = document.getElementsByTagNameNS("*", "Values");
    if (values.getLength() == 1) {
      Element element = (Element) values.item(0);
      assertEquals("true", element.getAttributeNS(XSI_NS, "nil"), "Values must be nil: " + xml);
    } else {
      assertEquals(0, values.getLength(), "at most one Values element: " + xml);
    }

    NodeList tail = document.getElementsByTagNameNS("*", "Tail");
    assertEquals(1, tail.getLength(), xml);
    assertEquals("42", tail.item(0).getTextContent());
  }

  private static JsonObject parse(String json) {
    return JsonParser.parseString(json).getAsJsonObject();
  }

  private static JsonObject members(JsonStruct value) {
    JsonObject result = value.getJsonObject().deepCopy();
    result.remove("__metadata");
    return result;
  }
}
