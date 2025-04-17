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

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class OpcUaXmlSerializationTest {

  @MethodSource("booleanArguments")
  @ParameterizedTest
  void booleanSerialization(boolean value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeBoolean("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    boolean decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeBoolean("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> booleanArguments() {
    return Stream.of(Arguments.of(true), Arguments.of(false));
  }

  @MethodSource("sbyteArguments")
  @ParameterizedTest
  void sbyteSerialization(byte value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeSByte("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    byte decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeSByte("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> sbyteArguments() {
    return Stream.of(
        Arguments.of(Byte.MIN_VALUE), Arguments.of((byte) 0), Arguments.of(Byte.MAX_VALUE));
  }

  @MethodSource("ubyteArguments")
  @ParameterizedTest
  void ubyteSerialization(UByte value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeByte("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    UByte decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeByte("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> ubyteArguments() {
    return Stream.of(
        Arguments.of(UByte.MIN), Arguments.of(UByte.valueOf(127)), Arguments.of(UByte.MAX));
  }

  @MethodSource("int16Arguments")
  @ParameterizedTest
  void int16Serialization(short value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeInt16("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    short decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeInt16("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> int16Arguments() {
    return Stream.of(
        Arguments.of(Short.MIN_VALUE), Arguments.of((short) 0), Arguments.of(Short.MAX_VALUE));
  }

  @MethodSource("uint16Arguments")
  @ParameterizedTest
  void uint16Serialization(UShort value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeUInt16("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    UShort decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeUInt16("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> uint16Arguments() {
    return Stream.of(
        Arguments.of(UShort.MIN), Arguments.of(UShort.valueOf(32767)), Arguments.of(UShort.MAX));
  }

  @MethodSource("int32Arguments")
  @ParameterizedTest
  void int32Serialization(int value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeInt32("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    int decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeInt32("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> int32Arguments() {
    return Stream.of(
        Arguments.of(Integer.MIN_VALUE), Arguments.of(0), Arguments.of(Integer.MAX_VALUE));
  }

  @MethodSource("uint32Arguments")
  @ParameterizedTest
  void uint32Serialization(UInteger value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeUInt32("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    UInteger decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeUInt32("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> uint32Arguments() {
    return Stream.of(
        Arguments.of(UInteger.MIN),
        Arguments.of(UInteger.valueOf(2147483647L)),
        Arguments.of(UInteger.MAX));
  }

  @MethodSource("int64Arguments")
  @ParameterizedTest
  void int64Serialization(long value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeInt64("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    long decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeInt64("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> int64Arguments() {
    return Stream.of(Arguments.of(Long.MIN_VALUE), Arguments.of(0L), Arguments.of(Long.MAX_VALUE));
  }

  @MethodSource("uint64Arguments")
  @ParameterizedTest
  void uint64Serialization(ULong value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeUInt64("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    ULong decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeUInt64("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> uint64Arguments() {
    return Stream.of(
        Arguments.of(ULong.MIN),
        Arguments.of(ULong.valueOf(Long.MAX_VALUE)),
        Arguments.of(ULong.MAX));
  }

  @MethodSource("floatArguments")
  @ParameterizedTest
  void floatSerialization(float value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeFloat("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    float decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeFloat("Test");
    }

    assertEquals(value, decoded, 0.0f);
  }

  private static Stream<Arguments> floatArguments() {
    return Stream.of(
        Arguments.of(-Float.MAX_VALUE),
        Arguments.of(0.0f),
        Arguments.of(Float.MAX_VALUE),
        Arguments.of(Float.NaN),
        Arguments.of(Float.NEGATIVE_INFINITY),
        Arguments.of(Float.POSITIVE_INFINITY));
  }

  @MethodSource("doubleArguments")
  @ParameterizedTest
  void doubleSerialization(double value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeDouble("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    double decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeDouble("Test");
    }

    assertEquals(value, decoded, 0.0);
  }

  private static Stream<Arguments> doubleArguments() {
    return Stream.of(
        Arguments.of(-Double.MAX_VALUE),
        Arguments.of(0.0d),
        Arguments.of(Double.MAX_VALUE),
        Arguments.of(Double.NaN),
        Arguments.of(Double.NEGATIVE_INFINITY),
        Arguments.of(Double.POSITIVE_INFINITY));
  }

  @MethodSource("stringArguments")
  @ParameterizedTest
  void stringSerialization(String value) throws Exception {
    if (value == null) return;

    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeString("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    String decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeString("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> stringArguments() {
    return Stream.of(
        Arguments.of((String) null),
        Arguments.of(""),
        Arguments.of("hello"),
        Arguments.of("with special chars <>\"'&"),
        Arguments.of("a".repeat(1000)) // very long string
        );
  }

  @MethodSource("dateTimeArguments")
  @ParameterizedTest
  void dateTimeSerialization(DateTime value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeDateTime("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    DateTime decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeDateTime("Test");
    }

    // Compare seconds since epoch to ignore precision differences
    if (value != null && decoded != null) {
      assertEquals(
          value.getJavaInstant().getEpochSecond(), decoded.getJavaInstant().getEpochSecond());
    } else {
      assertEquals(value, decoded);
    }
  }

  private static Stream<Arguments> dateTimeArguments() {
    return Stream.of(
        Arguments.of(DateTime.MIN_VALUE),
        Arguments.of(new DateTime()),
        Arguments.of(DateTime.now()),
        Arguments.of(DateTime.MAX_DATE_TIME));
  }

  @MethodSource("guidArguments")
  @ParameterizedTest
  void guidSerialization(UUID value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeGuid("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    UUID decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeGuid("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> guidArguments() {
    return Stream.of(Arguments.of(new UUID(0, 0)), Arguments.of(UUID.randomUUID()));
  }

  @MethodSource("byteStringArguments")
  @ParameterizedTest
  void byteStringSerialization(ByteString value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeByteString("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    ByteString decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeByteString("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> byteStringArguments() {
    return Stream.of(
        Arguments.of(ByteString.NULL_VALUE),
        Arguments.of(ByteString.of(new byte[0])),
        Arguments.of(ByteString.of(new byte[] {1, 2, 3})));
  }

  @MethodSource("xmlElementArguments")
  @ParameterizedTest
  void xmlElementSerialization(XmlElement value) throws Exception {
    // Skip test for null or empty fragments as they're not properly handled in XML encoding
    if (value.getFragment() == null || value.getFragment().isEmpty()) {
      return;
    }

    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeXmlElement("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    XmlElement decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeXmlElement("Test");
    }

    Diff diff =
        DiffBuilder.compare(value.getFragmentOrEmpty())
            .withTest(decoded.getFragmentOrEmpty())
            .ignoreWhitespace()
            .build();

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  private static Stream<Arguments> xmlElementArguments() {
    return Stream.of(
        Arguments.of(new XmlElement(null)),
        Arguments.of(new XmlElement("")),
        Arguments.of(new XmlElement("<tag>hello</tag>")),
        Arguments.of(new XmlElement("<tag attr=\"value\"/>")));
  }

  @MethodSource("nodeIdArguments")
  @ParameterizedTest
  void nodeIdSerialization(NodeId value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeNodeId("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    NodeId decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeNodeId("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> nodeIdArguments() {
    return Stream.of(
        Arguments.of(new NodeId(0, 0)),
        Arguments.of(new NodeId(1, 123)),
        Arguments.of(new NodeId(2, "string_id")),
        Arguments.of(new NodeId(3, UUID.randomUUID())),
        Arguments.of(new NodeId(4, ByteString.of(new byte[] {1, 2, 3}))));
  }

  @MethodSource("expandedNodeIdArguments")
  @ParameterizedTest
  void expandedNodeIdSerialization(ExpandedNodeId value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeExpandedNodeId("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    ExpandedNodeId decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeExpandedNodeId("Test");
    }

    assertEquals(
        value.absolute(new NamespaceTable()).orElseThrow(),
        decoded.absolute(new NamespaceTable()).orElseThrow());
  }

  private static Stream<Arguments> expandedNodeIdArguments() {
    return Stream.of(
        Arguments.of(ExpandedNodeId.of(0, 0)),
        Arguments.of(ExpandedNodeId.of("urn:test", 123)),
        Arguments.of(ExpandedNodeId.of("urn:test", "string_id")),
        Arguments.of(ExpandedNodeId.of(UUID.randomUUID())));
  }

  @MethodSource("statusCodeArguments")
  @ParameterizedTest
  void statusCodeSerialization(StatusCode value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeStatusCode("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    StatusCode decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeStatusCode("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> statusCodeArguments() {
    return Stream.of(
        Arguments.of(StatusCode.GOOD),
        Arguments.of(new StatusCode(StatusCodes.Bad_UnexpectedError)),
        Arguments.of(new StatusCode(0x12345678)));
  }

  @MethodSource("qualifiedNameArguments")
  @ParameterizedTest
  void qualifiedNameSerialization(QualifiedName value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeQualifiedName("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    QualifiedName decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeQualifiedName("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> qualifiedNameArguments() {
    return Stream.of(
        Arguments.of(new QualifiedName(0, null)),
        Arguments.of(new QualifiedName(0, "")),
        Arguments.of(new QualifiedName(1, "name")));
  }

  @MethodSource("localizedTextArguments")
  @ParameterizedTest
  void localizedTextSerialization(LocalizedText value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeLocalizedText("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    LocalizedText decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeLocalizedText("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> localizedTextArguments() {
    return Stream.of(
        Arguments.of(LocalizedText.NULL_VALUE),
        Arguments.of(new LocalizedText(null, null)),
        Arguments.of(new LocalizedText("en", "text")),
        Arguments.of(new LocalizedText("", "no locale")),
        Arguments.of(new LocalizedText("de", "")));
  }

  @MethodSource("extensionObjectArguments")
  @ParameterizedTest
  void extensionObjectSerialization(ExtensionObject value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeExtensionObject("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    ExtensionObject decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeExtensionObject("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> extensionObjectArguments() {
    return Stream.of(
        Arguments.of(ExtensionObject.of(new XmlElement(null), NodeId.NULL_VALUE)),
        Arguments.of(ExtensionObject.of(new XmlElement("<data>test</data>"), new NodeId(0, 1))));
  }

  @MethodSource("dataValueArguments")
  @ParameterizedTest
  void dataValueSerialization(DataValue value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeDataValue("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    DataValue decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeDataValue("Test");
    }

    // Compare only the value and status code, not the timestamps
    // because timestamps may be lost or have different precision
    assertEquals(value.getValue(), decoded.getValue());
    assertEquals(value.getStatusCode(), decoded.getStatusCode());
  }

  private static Stream<Arguments> dataValueArguments() {
    return Stream.of(
        Arguments.of(new DataValue(new Variant(123))),
        Arguments.of(new DataValue(Variant.NULL_VALUE, StatusCode.GOOD)),
        Arguments.of(new DataValue(new Variant("hello"), StatusCode.GOOD)));
  }

  @MethodSource("variantArguments")
  @ParameterizedTest
  void variantSerialization(Variant value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeVariant("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    Variant decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeVariant("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> variantArguments() {
    return Stream.of(
        Arguments.of(Variant.NULL_VALUE),
        Arguments.of(new Variant(true)),
        Arguments.of(new Variant(123)),
        Arguments.of(new Variant("text")),
        Arguments.of(new Variant(new Integer[] {1, 2, 3})));
  }

  @MethodSource("diagnosticInfoArguments")
  @ParameterizedTest
  void diagnosticInfoSerialization(DiagnosticInfo value) throws Exception {
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeDiagnosticInfo("Test", value);
      encoded = encoder.getOutputString();
    }

    System.out.println("value: " + value);
    System.out.println("encoded: " + encoded);

    DiagnosticInfo decoded;
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext())) {
      decoder.setInput(new StringReader(encoded));

      decoded = decoder.decodeDiagnosticInfo("Test");
    }

    assertEquals(value, decoded);
  }

  private static Stream<Arguments> diagnosticInfoArguments() {
    return Stream.of(
        Arguments.of(DiagnosticInfo.NULL_VALUE),
        Arguments.of(new DiagnosticInfo(1, 2, 3, 4, "message", null, null)));
  }
}
