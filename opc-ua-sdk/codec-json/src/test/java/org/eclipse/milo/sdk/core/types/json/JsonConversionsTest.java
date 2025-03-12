/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonConversionsTest {

  @ParameterizedTest
  @ValueSource(ints = {Byte.MIN_VALUE, 0, Byte.MAX_VALUE})
  void convertSByte(int input) {
    var original = (byte) input;

    JsonElement jsonValue = JsonConversions.fromSByte(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    byte opcValue = JsonConversions.toSByte(jsonValue);
    assertEquals(original, opcValue);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, Byte.MAX_VALUE, UByte.MAX_VALUE})
  void convertByte(int input) {
    var original = UByte.valueOf(input);

    JsonElement jsonValue = JsonConversions.fromByte(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    UByte opcValue = JsonConversions.toByte(jsonValue);
    assertEquals(original, opcValue);
  }

  @ParameterizedTest
  @ValueSource(ints = {Short.MIN_VALUE, 0, Short.MAX_VALUE})
  void convertInt16(int input) {
    var original = (short) input;

    JsonElement jsonValue = JsonConversions.fromInt16(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    short opcValue = JsonConversions.toInt16(jsonValue);
    assertEquals(original, opcValue);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, Short.MAX_VALUE, UShort.MAX_VALUE})
  void convertUInt16(int input) {
    var original = UShort.valueOf(input);

    JsonElement jsonValue = JsonConversions.fromUInt16(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    UShort opcValue = JsonConversions.toUInt16(jsonValue);
    assertEquals(original, opcValue);
  }

  @ParameterizedTest
  @ValueSource(ints = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE})
  void convertInt32(int input) {
    JsonElement jsonValue = JsonConversions.fromInt32(input);
    assertEquals(new JsonPrimitive(input), jsonValue);

    int opcValue = JsonConversions.toInt32(jsonValue);
    assertEquals(input, opcValue);
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, 1L, Integer.MAX_VALUE, UInteger.MAX_VALUE})
  void convertUInt32(long input) {
    var original = UInteger.valueOf(input);

    JsonElement jsonValue = JsonConversions.fromUInt32(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    UInteger opcValue = JsonConversions.toUInt32(jsonValue);
    assertEquals(original, opcValue);
  }

  @ParameterizedTest
  @ValueSource(longs = {Long.MIN_VALUE, 0L, Long.MAX_VALUE})
  void convertInt64(long input) {
    JsonElement jsonValue = JsonConversions.fromInt64(input);
    assertEquals(new JsonPrimitive(input), jsonValue);

    long opcValue = JsonConversions.toInt64(jsonValue);
    assertEquals(input, opcValue);
  }

  @ParameterizedTest
  @MethodSource("convertUInt64Provider")
  void convertUInt64(BigInteger input) {
    var original = ULong.valueOf(input);

    JsonElement jsonValue = JsonConversions.fromUInt64(original);
    assertEquals(new JsonPrimitive(input), jsonValue);

    ULong opcValue = JsonConversions.toUInt64(jsonValue);
    assertEquals(original, opcValue);
  }

  private static Stream<Arguments> convertUInt64Provider() {
    return Stream.of(
        () -> new Object[] {BigInteger.ZERO},
        () -> new Object[] {BigInteger.ONE},
        () -> new Object[] {ULong.MAX_VALUE_LONG},
        () -> new Object[] {ULong.MAX_VALUE});
  }

  @ParameterizedTest
  @ValueSource(
      floats = {
        Float.MIN_VALUE,
        0,
        Float.MAX_VALUE,
        Float.NEGATIVE_INFINITY,
        Float.POSITIVE_INFINITY,
        Float.NaN
      })
  void convertFloat(float input) {
    JsonElement jsonValue = JsonConversions.fromFloat(input);
    assertEquals(new JsonPrimitive(input), jsonValue);

    float opcValue = JsonConversions.toFloat(jsonValue);
    assertEquals((float) input, opcValue);
  }

  @ParameterizedTest
  @ValueSource(
      doubles = {
        Double.MIN_VALUE,
        0,
        Double.MAX_VALUE,
        Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.NaN
      })
  void convertDouble(double input) {
    JsonElement jsonValue = JsonConversions.fromDouble(input);
    assertEquals(new JsonPrimitive(input), jsonValue);

    double opcValue = JsonConversions.toDouble(jsonValue);
    assertEquals(input, opcValue);
  }

  @Test
  void convertDateTime() {
    var now = Instant.now();
    String iso8601 = now.toString();

    DateTime result = JsonConversions.toDateTime(new JsonPrimitive(iso8601));
    assertEquals(new DateTime(now), result);
  }

  @Test
  void convertUUID() {
    String uuid = "123e4567-e89b-12d3-a456-426614174000";
    UUID result = JsonConversions.toGuid(new JsonPrimitive(uuid));
    assertEquals(UUID.fromString(uuid), result);
  }

  @ParameterizedTest
  @MethodSource("convertByteStringProvider")
  void convertByteString(ByteString input, String expected) {
    JsonElement jsonValue = JsonConversions.fromByteString(input);
    assertEquals(new JsonPrimitive(expected), jsonValue);

    ByteString opcValue = JsonConversions.toByteString(jsonValue);
    assertEquals(input, opcValue);
  }

  private static Stream<Arguments> convertByteStringProvider() {
    return Stream.of(
        Arguments.of(ByteString.of(new byte[] {1, 2, 3, 4, 5}), "0102030405"),
        Arguments.of(ByteString.of(new byte[0]), ""),
        Arguments.of(ByteString.NULL_VALUE, ""));
  }

  @ParameterizedTest
  @MethodSource("convertXmlElementProvider")
  void convertXmlElement(XmlElement input) {
    JsonElement jsonValue = JsonConversions.fromXmlElement(input);

    if (input.isNull()) {
      assertEquals(JsonNull.INSTANCE, jsonValue);
    } else {
      assertEquals(new JsonPrimitive(input.fragment()), jsonValue);
    }

    XmlElement opcValue = JsonConversions.toXmlElement(jsonValue);
    assertEquals(input, opcValue);
  }

  private static Stream<Arguments> convertXmlElementProvider() {
    return Stream.of(
        Arguments.of(new XmlElement("<root><element>value</element></root>")),
        Arguments.of(new XmlElement(null)));
  }

  @ParameterizedTest
  @MethodSource("convertNodeIdProvider")
  void convertNodeId(NodeId nodeId) {
    JsonElement jsonValue = JsonConversions.fromNodeId(nodeId);
    assertEquals(new JsonPrimitive(nodeId.toParseableString()), jsonValue);

    NodeId opcValue = JsonConversions.toNodeId(jsonValue);
    assertEquals(nodeId, opcValue);
  }

  private static Stream<Arguments> convertNodeIdProvider() {
    return Stream.of(
        Arguments.of(NodeId.parse("ns=0;i=1")),
        Arguments.of(NodeId.parse("ns=0;s=foo")),
        Arguments.of(NodeId.parse("ns=0;g=123e4567-e89b-12d3-a456-426614174000")),
        Arguments.of(NodeId.parse("ns=0;b=AAECAwQFBgcICQoLDA0ODw==")),
        Arguments.of(NodeId.parse("ns=1;i=1")),
        Arguments.of(NodeId.parse("ns=1;s=foo")),
        Arguments.of(NodeId.parse("ns=1;g=123e4567-e89b-12d3-a456-426614174000")),
        Arguments.of(NodeId.parse("ns=1;b=AAECAwQFBgcICQoLDA0ODw==")));
  }

  @ParameterizedTest
  @MethodSource("convertExpandedNodeIdProvider")
  void convertExpandedNodeId(ExpandedNodeId expandedNodeId) {
    JsonElement jsonValue = JsonConversions.fromExpandedNodeId(expandedNodeId);
    assertEquals(new JsonPrimitive(expandedNodeId.toParseableString()), jsonValue);

    ExpandedNodeId opcValue = JsonConversions.toExpandedNodeId(jsonValue);
    assertEquals(expandedNodeId, opcValue);
  }

  private static Stream<Arguments> convertExpandedNodeIdProvider() {
    // TODO include test values that use explicit namespace URI and server index
    return Stream.of(
        Arguments.of(ExpandedNodeId.parse("ns=0;i=1")),
        Arguments.of(ExpandedNodeId.parse("ns=0;s=foo")),
        Arguments.of(ExpandedNodeId.parse("ns=0;g=123e4567-e89b-12d3-a456-426614174000")),
        Arguments.of(ExpandedNodeId.parse("ns=0;b=AAECAwQFBgcICQoLDA0ODw==")),
        Arguments.of(ExpandedNodeId.parse("ns=1;i=1")),
        Arguments.of(ExpandedNodeId.parse("ns=1;s=foo")),
        Arguments.of(ExpandedNodeId.parse("ns=1;g=123e4567-e89b-12d3-a456-426614174000")),
        Arguments.of(ExpandedNodeId.parse("ns=1;b=AAECAwQFBgcICQoLDA0ODw==")),
        Arguments.of(ExpandedNodeId.parse("nsu=https://example.com;i=1")),
        Arguments.of(ExpandedNodeId.parse("nsu=https://example.com;s=foo")),
        Arguments.of(
            ExpandedNodeId.parse("nsu=https://example.com;g=123e4567-e89b-12d3-a456-426614174000")),
        Arguments.of(ExpandedNodeId.parse("nsu=https://example.com;b=AAECAwQFBgcICQoLDA0ODw==")));
  }

  @ParameterizedTest
  @MethodSource("convertStatusCodeProvider")
  void convertStatusCode(StatusCode input) {
    var jsonValue = JsonConversions.fromStatusCode(input);
    assertEquals(new JsonPrimitive(input.getValue()), jsonValue);

    StatusCode opcValue = JsonConversions.toStatusCode(jsonValue);
    assertEquals(input, opcValue);
  }

  private static Stream<Arguments> convertStatusCodeProvider() {
    return Stream.of(
        Arguments.of(StatusCode.GOOD),
        Arguments.of(StatusCode.BAD),
        Arguments.of(StatusCode.UNCERTAIN));
  }

  @ParameterizedTest
  @MethodSource("convertQualifiedNameProvider")
  void convertQualifiedName(QualifiedName qualifiedName, String encoded) {
    JsonElement jsonValue = JsonConversions.fromQualifiedName(qualifiedName);
    assertEquals(new JsonPrimitive(encoded), jsonValue);

    QualifiedName opcValue = JsonConversions.toQualifiedName(jsonValue);
    assertEquals(qualifiedName, opcValue);
  }

  private static Stream<Arguments> convertQualifiedNameProvider() {
    return Stream.of(
        Arguments.of(new QualifiedName(0, "foo"), "foo"),
        Arguments.of(new QualifiedName(1, "bar"), "1:bar"),
        Arguments.of(new QualifiedName(2, "baz:qux"), "2:baz:qux"));
  }

  @ParameterizedTest
  @MethodSource("convertLocalizedTextProvider")
  void convertLocalizedText(LocalizedText localizedText) {
    JsonElement jsonValue = JsonConversions.fromLocalizedText(localizedText);
    var expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("Locale", localizedText.locale());
    expectedJsonObject.addProperty("Text", localizedText.text());
    assertEquals(expectedJsonObject, jsonValue);

    LocalizedText opcValue = JsonConversions.toLocalizedText(jsonValue);
    assertEquals(localizedText, opcValue);
  }

  private static Stream<Arguments> convertLocalizedTextProvider() {
    return Stream.of(
        Arguments.of(new LocalizedText("en", "Hello, World!")),
        Arguments.of(new LocalizedText("de", "Hallo, Welt!")),
        Arguments.of(new LocalizedText(null, "Hello, World!")),
        Arguments.of(new LocalizedText("en", null)),
        Arguments.of(new LocalizedText(null, null)));
  }

  @ParameterizedTest
  @MethodSource("convertDataValueProvider")
  void convertDataValue(DataValue dataValue, String expectedJson) {
    JsonElement asJson = JsonConversions.fromDataValue(dataValue);
    System.out.println(asJson);

    assertEquals(expectedJson, asJson.toString());

    DataValue asOpcUa = JsonConversions.toDataValue(asJson);
    assertEquals(dataValue, asOpcUa);
  }

  private static Stream<Arguments> convertDataValueProvider() {
    return Stream.of(
        Arguments.of(
            new DataValue(
                Variant.ofInt32(42),
                StatusCode.GOOD,
                DateTime.MIN_DATE_TIME,
                UShort.valueOf(0),
                DateTime.MIN_DATE_TIME,
                UShort.valueOf(0)),
            "{\"Value\":{\"Type\":6,\"Body\":42},\"SourceTime\":\"0001-01-01T00:00:00Z\",\"SourcePicoseconds\":0,\"ServerTime\":\"0001-01-01T00:00:00Z\",\"ServerPicoseconds\":0}"),
        Arguments.of(
            new DataValue(
                Variant.ofInt32(42),
                StatusCode.GOOD,
                DateTime.MIN_DATE_TIME,
                null,
                DateTime.MIN_DATE_TIME,
                null),
            "{\"Value\":{\"Type\":6,\"Body\":42},\"SourceTime\":\"0001-01-01T00:00:00Z\",\"ServerTime\":\"0001-01-01T00:00:00Z\"}"),
        Arguments.of(
            new DataValue(
                Variant.ofInt32(42),
                StatusCode.GOOD,
                null,
                UShort.valueOf(0),
                null,
                UShort.valueOf(0)),
            "{\"Value\":{\"Type\":6,\"Body\":42},\"SourcePicoseconds\":0,\"ServerPicoseconds\":0}"),
        Arguments.of(
            new DataValue(Variant.ofInt32(42), StatusCode.GOOD, null, null, null, null),
            "{\"Value\":{\"Type\":6,\"Body\":42}}"),
        Arguments.of(
            new DataValue(Variant.ofInt32(42), StatusCode.GOOD, null, null, null, null),
            "{\"Value\":{\"Type\":6,\"Body\":42}}"),
        Arguments.of(
            new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null, null, null, null), "{}"));
  }
}
