/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OpcUaJsonDecoderTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @Test
  void decodeBoolean() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("true"));
    assertTrue(decoder.decodeBoolean(null));

    decoder.reset(new StringReader("false"));
    assertFalse(decoder.decodeBoolean(null));

    decoder.reset(new StringReader("{\"foo\":true}"));
    decoder.jsonReader.beginObject();
    assertTrue(decoder.decodeBoolean("foo"));
    decoder.jsonReader.endObject();

    decoder.reset(new StringReader("{\"foo\":false}"));
    decoder.jsonReader.beginObject();
    assertFalse(decoder.decodeBoolean("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeSByte() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0"));
    assertEquals((byte) 0, decoder.decodeSByte(null));

    decoder.reset(new StringReader(String.valueOf(Byte.MIN_VALUE)));
    assertEquals(Byte.MIN_VALUE, decoder.decodeSByte(null));

    decoder.reset(new StringReader(String.valueOf(Byte.MAX_VALUE)));
    assertEquals(Byte.MAX_VALUE, decoder.decodeSByte(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals((byte) 0, decoder.decodeSByte("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeInt16() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0"));
    assertEquals((short) 0, decoder.decodeInt16(null));

    decoder.reset(new StringReader(String.valueOf(Short.MIN_VALUE)));
    assertEquals(Short.MIN_VALUE, decoder.decodeInt16(null));

    decoder.reset(new StringReader(String.valueOf(Short.MAX_VALUE)));
    assertEquals(Short.MAX_VALUE, decoder.decodeInt16(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals((short) 0, decoder.decodeInt16("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeInt32() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0"));
    assertEquals(0, decoder.decodeInt32(null));

    decoder.reset(new StringReader(String.valueOf(Integer.MIN_VALUE)));
    assertEquals(Integer.MIN_VALUE, decoder.decodeInt32(null));

    decoder.reset(new StringReader(String.valueOf(Integer.MAX_VALUE)));
    assertEquals(Integer.MAX_VALUE, decoder.decodeInt32(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(0, decoder.decodeInt32("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeInt64() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("\"0\""));
    assertEquals(0L, decoder.decodeInt64(null));

    decoder.reset(new StringReader(String.format("\"%s\"", Long.MIN_VALUE)));
    assertEquals(Long.MIN_VALUE, decoder.decodeInt64(null));

    decoder.reset(new StringReader(String.format("\"%s\"", Long.MAX_VALUE)));
    assertEquals(Long.MAX_VALUE, decoder.decodeInt64(null));

    decoder.reset(new StringReader("{\"foo\":\"0\"}"));
    decoder.jsonReader.beginObject();
    assertEquals(0L, decoder.decodeInt64("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeByte() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader(String.valueOf(UByte.MIN)));
    assertEquals(UByte.MIN, decoder.decodeByte(null));

    decoder.reset(new StringReader(String.valueOf(UByte.MAX)));
    assertEquals(UByte.MAX, decoder.decodeByte(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(UByte.MIN, decoder.decodeByte("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeUInt16() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader(String.valueOf(UShort.MIN)));
    assertEquals(UShort.MIN, decoder.decodeUInt16(null));

    decoder.reset(new StringReader(String.valueOf(UShort.MAX)));
    assertEquals(UShort.MAX, decoder.decodeUInt16(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(UShort.MIN, decoder.decodeUInt16("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeUInt32() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader(String.valueOf(UInteger.MIN)));
    assertEquals(UInteger.MIN, decoder.decodeUInt32(null));

    decoder.reset(new StringReader(String.valueOf(UInteger.MAX)));
    assertEquals(UInteger.MAX, decoder.decodeUInt32(null));

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(UInteger.MIN, decoder.decodeUInt32("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeUInt64() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader(String.format("\"%s\"", ULong.MIN)));
    assertEquals(ULong.MIN, decoder.decodeUInt64(null));

    decoder.reset(new StringReader(String.format("\"%s\"", ULong.MAX)));
    assertEquals(ULong.MAX, decoder.decodeUInt64(null));

    decoder.reset(new StringReader("{\"foo\":\"0\"}"));
    decoder.jsonReader.beginObject();
    assertEquals(ULong.MIN, decoder.decodeUInt64("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeFloat() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0.0"));
    assertEquals(0.0f, decoder.decodeFloat(null));

    decoder.reset(new StringReader(String.valueOf(Float.MIN_VALUE)));
    assertEquals(Float.MIN_VALUE, decoder.decodeFloat(null));

    decoder.reset(new StringReader(String.valueOf(Float.MAX_VALUE)));
    assertEquals(Float.MAX_VALUE, decoder.decodeFloat(null));

    decoder.reset(new StringReader("\"Infinity\""));
    assertEquals(Float.POSITIVE_INFINITY, decoder.decodeFloat(null));

    decoder.reset(new StringReader("\"-Infinity\""));
    assertEquals(Float.NEGATIVE_INFINITY, decoder.decodeFloat(null));

    decoder.reset(new StringReader("\"NaN\""));
    assertEquals(Float.NaN, decoder.decodeFloat(null));

    decoder.reset(new StringReader("{\"foo\":0.0}"));
    decoder.jsonReader.beginObject();
    assertEquals(0.0f, decoder.decodeFloat("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeDouble() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0.0"));
    assertEquals(0.0, decoder.decodeDouble(null));

    decoder.reset(new StringReader(String.valueOf(Double.MIN_VALUE)));
    assertEquals(Double.MIN_VALUE, decoder.decodeDouble(null));

    decoder.reset(new StringReader(String.valueOf(Double.MAX_VALUE)));
    assertEquals(Double.MAX_VALUE, decoder.decodeDouble(null));

    decoder.reset(new StringReader("\"Infinity\""));
    assertEquals(Double.POSITIVE_INFINITY, decoder.decodeDouble(null));

    decoder.reset(new StringReader("\"-Infinity\""));
    assertEquals(Double.NEGATIVE_INFINITY, decoder.decodeDouble(null));

    decoder.reset(new StringReader("\"NaN\""));
    assertEquals(Double.NaN, decoder.decodeDouble(null));

    decoder.reset(new StringReader("{\"foo\":0.0}"));
    decoder.jsonReader.beginObject();
    assertEquals(0.0, decoder.decodeDouble("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeString() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("\"\""));
    assertEquals("", decoder.decodeString(null));

    decoder.reset(new StringReader("\"foo\""));
    assertEquals("foo", decoder.decodeString(null));

    decoder.reset(new StringReader("{\"foo\":\"bar\"}"));
    decoder.jsonReader.beginObject();
    assertEquals("bar", decoder.decodeString("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeDateTime() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader(String.format("\"%s\"", DateTime.MIN_ISO_8601_STRING)));
    assertEquals(DateTime.MIN_DATE_TIME, decoder.decodeDateTime(null));

    decoder.reset(new StringReader(String.format("\"%s\"", DateTime.MAX_ISO_8601_STRING)));
    assertEquals(DateTime.MAX_DATE_TIME, decoder.decodeDateTime(null));

    Instant minus1 = DateTime.MIN_ISO_8601_INSTANT.minus(1, ChronoUnit.SECONDS);
    decoder.reset(new StringReader(String.format("\"%s\"", minus1)));
    assertEquals(DateTime.MIN_DATE_TIME, decoder.decodeDateTime(null));

    Instant plus1 = DateTime.MAX_ISO_8601_INSTANT.plus(1, ChronoUnit.SECONDS);
    decoder.reset(new StringReader(String.format("\"%s\"", plus1)));
    assertEquals(DateTime.MAX_DATE_TIME, decoder.decodeDateTime(null));

    DateTime now = DateTime.nowNanos();
    String isoNow = now.toIso8601String();
    decoder.reset(new StringReader(String.format("\"%s\"", isoNow)));
    assertEquals(now, decoder.decodeDateTime(null));

    decoder.reset(new StringReader(String.format("{\"foo\":\"%s\"}", isoNow)));
    decoder.jsonReader.beginObject();
    assertEquals(now, decoder.decodeDateTime("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeGuid() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    UUID uuid = UUID.randomUUID();

    decoder.reset(new StringReader(String.format("\"%s\"", uuid.toString().toLowerCase())));
    assertEquals(uuid, decoder.decodeGuid(null));

    decoder.reset(new StringReader(String.format("\"%s\"", uuid.toString().toUpperCase())));
    assertEquals(uuid, decoder.decodeGuid(null));

    decoder.reset(new StringReader(String.format("{\"foo\":\"%s\"}", uuid)));
    decoder.jsonReader.beginObject();
    assertEquals(uuid, decoder.decodeGuid("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeByteString() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    byte[] emptyBytes = new byte[0];
    decoder.reset(
        new StringReader(String.format("\"%s\"", Base64.getEncoder().encodeToString(emptyBytes))));
    assertEquals(ByteString.of(emptyBytes), decoder.decodeByteString(null));

    byte[] randomBytes = randomBytes16();
    decoder.reset(
        new StringReader(String.format("\"%s\"", Base64.getEncoder().encodeToString(randomBytes))));
    assertEquals(ByteString.of(randomBytes), decoder.decodeByteString(null));

    decoder.reset(
        new StringReader(
            String.format("{\"foo\":\"%s\"}", Base64.getEncoder().encodeToString(randomBytes))));
    decoder.jsonReader.beginObject();
    assertEquals(ByteString.of(randomBytes), decoder.decodeByteString("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeXmlElement() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var emptyElement = new XmlElement("");
    decoder.reset(new StringReader("\"\""));
    assertEquals(emptyElement, decoder.decodeXmlElement(null));

    var element = new XmlElement("<foo>bar</foo>");
    decoder.reset(new StringReader(String.format("\"%s\"", element.fragment())));
    assertEquals(element, decoder.decodeXmlElement(null));

    decoder.reset(new StringReader(String.format("{\"foo\":\"%s\"}", element.fragment())));
    decoder.jsonReader.beginObject();
    assertEquals(element, decoder.decodeXmlElement("foo"));
    decoder.jsonReader.endObject();
  }

  @MethodSource("decodeNodeIdArguments")
  @ParameterizedTest(name = "field={0} json={1} expected={2}")
  void decodeNodeId(String field, String json, NodeId expected) throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(json));

    if (field != null) {
      decoder.jsonReader.beginObject();
      assertEquals(expected, decoder.decodeNodeId(field));
      decoder.jsonReader.endObject();
    } else {
      assertEquals(expected, decoder.decodeNodeId(null));
    }
  }

  static Stream<Arguments> decodeNodeIdArguments() {
    var uuid = UUID.randomUUID();
    var bs = ByteString.of(randomBytes16());

    return Stream.of(
        // field, json, expected
        // IdType == UInt32, Namespace = 0
        Arguments.of(null, "\"i=0\"", new NodeId(0, 0)),
        // IdType == UInt32, Namespace != 0
        Arguments.of(null, "\"ns=1;i=0\"", new NodeId(1, 0)),
        // IdType == String, Namespace = 0
        Arguments.of(null, "\"s=foo\"", new NodeId(0, "foo")),
        // IdType == String, Namespace != 0
        Arguments.of(null, "\"ns=1;s=foo\"", new NodeId(1, "foo")),
        // IdType == UInt32, Namespace > 1
        Arguments.of(null, "\"ns=2;i=0\"", new NodeId(2, 0)),

        // IdType == Guid, Namespace = 0
        Arguments.of(
            null, "\"g=%s\"".formatted(uuid.toString().toUpperCase()), new NodeId(0, uuid)),

        // IdType == Guid, Namespace != 0
        Arguments.of(
            null, "\"ns=1;g=%s\"".formatted(uuid.toString().toUpperCase()), new NodeId(1, uuid)),

        // IdType == ByteString, Namespace = 0
        Arguments.of(
            null,
            "\"b=%s\"".formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty())),
            new NodeId(0, bs)),

        // IdType == ByteString, Namespace != 0
        Arguments.of(
            null,
            "\"ns=1;b=%s\"".formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty())),
            new NodeId(1, bs)),

        // Namespace > 1 but not in table
        Arguments.of(null, "\"ns=99;i=0\"", new NodeId(99, 0)),

        // key != null
        Arguments.of("foo", "{\"foo\":\"ns=1;s=foo\"}", new NodeId(1, "foo")));
  }

  @MethodSource("decodeExpandedNodeIdArguments")
  @ParameterizedTest(name = "field={0} json={1} expected={2}")
  void decodeExpandedNodeId(String field, String json, ExpandedNodeId expected) throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(json));

    if (field != null) {
      decoder.jsonReader.beginObject();
      assertEquals(expected, decoder.decodeExpandedNodeId(field));
      decoder.jsonReader.endObject();
    } else {
      assertEquals(expected, decoder.decodeExpandedNodeId(null));
    }
  }

  static Stream<Arguments> decodeExpandedNodeIdArguments() {
    var uuid = UUID.randomUUID();
    var bs = ByteString.of(randomBytes16());

    return Stream.of(
        // field, json, expected
        // Local server (serverIndex = 0), namespace = 0, different identifier types
        Arguments.of(null, "\"i=0\"", ExpandedNodeId.of(uint(0))),
        Arguments.of(
            null,
            "\"s=foo\"",
            new ExpandedNodeId(
                ServerReference.of(uint(0)), NamespaceReference.of(ushort(0)), "foo")),
        Arguments.of(
            null,
            "\"g=%s\"".formatted(uuid.toString().toUpperCase()),
            new ExpandedNodeId(
                ServerReference.of(uint(0)), NamespaceReference.of(ushort(0)), uuid)),
        Arguments.of(
            null,
            "\"b=%s\"".formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty())),
            new ExpandedNodeId(ServerReference.of(uint(0)), NamespaceReference.of(ushort(0)), bs)),

        // Local server (serverIndex = 0), namespace != 0, different identifier types
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0))),
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test1;s=foo\"",
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                "foo")),
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test1;g=%s\"".formatted(uuid.toString().toUpperCase()),
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                uuid)),
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test1;b=%s\""
                .formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty())),
            new ExpandedNodeId(
                ServerReference.of(uint(0)), NamespaceReference.of("urn:eclipse:milo:test1"), bs)),

        // Local server (serverIndex = 0), namespace > 1
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test2;i=0\"",
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test2"),
                uint(0))),

        // Non-local server (serverIndex != 0), namespace = 0
        Arguments.of(
            null,
            "\"svu=urn:eclipse:milo:server1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of("urn:eclipse:milo:server1"), NamespaceReference.of(0), uint(0))),

        // Non-local server (serverIndex != 0), namespace != 0
        Arguments.of(
            null,
            "\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of("urn:eclipse:milo:server1"),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0))),

        // Server URI, namespace = 0
        Arguments.of(
            null,
            "\"svu=urn:eclipse:milo:server1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of("urn:eclipse:milo:server1"), NamespaceReference.of(0), uint(0))),

        // Server URI, namespace URI
        Arguments.of(
            null,
            "\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of("urn:eclipse:milo:server1"),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0))),

        // Namespace URI, local server
        Arguments.of(
            null,
            "\"nsu=urn:eclipse:milo:test1;i=0\"",
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0))),

        // With field name
        Arguments.of(
            "foo",
            "{\"foo\":\"nsu=urn:eclipse:milo:test1;s=bar\"}",
            new ExpandedNodeId(
                ServerReference.of(uint(0)),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                "bar")),

        // With field name, server URI, namespace URI
        Arguments.of(
            "foo",
            "{\"foo\":\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;s=bar\"}",
            new ExpandedNodeId(
                ServerReference.of("urn:eclipse:milo:server1"),
                NamespaceReference.of("urn:eclipse:milo:test1"),
                "bar")));
  }

  @Test
  void decodeStatusCode() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0"));
    assertEquals(StatusCode.GOOD, decoder.decodeStatusCode(null));

    decoder.reset(new StringReader(String.valueOf(StatusCode.UNCERTAIN.value())));
    assertEquals(StatusCode.UNCERTAIN, decoder.decodeStatusCode(null));

    decoder.reset(new StringReader(String.valueOf(StatusCode.BAD.value())));
    assertEquals(StatusCode.BAD, decoder.decodeStatusCode(null));

    StatusCode[] statusCodes =
        new StatusCode[] {
          new StatusCode(StatusCodes.Good_Overload),
          new StatusCode(StatusCodes.Uncertain_InitialValue),
          new StatusCode(StatusCodes.Bad_DecodingError)
        };

    for (StatusCode statusCode : statusCodes) {
      decoder.reset(new StringReader(String.valueOf(statusCode.value())));
      assertEquals(statusCode, decoder.decodeStatusCode(null));
    }

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(StatusCode.GOOD, decoder.decodeStatusCode("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeQualifiedName() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    // Test name in namespace 0
    decoder.reset(new StringReader("\"TestName\""));
    assertEquals(new QualifiedName(0, "TestName"), decoder.decodeQualifiedName(null));

    // Test name in non-zero namespace
    context.getNamespaceTable().add("urn:test:namespace");
    decoder.reset(new StringReader("\"nsu=urn:test:namespace;TestName\""));
    assertEquals(new QualifiedName(1, "TestName"), decoder.decodeQualifiedName(null));

    // Test with field name
    decoder.reset(new StringReader("{\"qname\":\"TestName\"}"));
    decoder.jsonReader.beginObject();
    assertEquals(new QualifiedName(0, "TestName"), decoder.decodeQualifiedName("qname"));
    decoder.jsonReader.endObject();

    // Test with field name and non-zero namespace
    decoder.reset(new StringReader("{\"qname\":\"nsu=urn:test:namespace;TestName\"}"));
    decoder.jsonReader.beginObject();
    assertEquals(new QualifiedName(1, "TestName"), decoder.decodeQualifiedName("qname"));
    decoder.jsonReader.endObject();

    // Test null name
    decoder.reset(new StringReader("null"));
    assertEquals(QualifiedName.NULL_VALUE, decoder.decodeQualifiedName(null));
  }

  @Test
  void decodeLocalizedText() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{\"Locale\":\"en\",\"Text\":\"foo\"}"));
    assertEquals(LocalizedText.english("foo"), decoder.decodeLocalizedText(null));

    decoder.reset(new StringReader("{\"Locale\":\"en\"}"));
    assertEquals(new LocalizedText("en", null), decoder.decodeLocalizedText(null));

    decoder.reset(new StringReader("{\"Text\":\"foo\"}"));
    assertEquals(new LocalizedText(null, "foo"), decoder.decodeLocalizedText(null));

    decoder.reset(new StringReader("{\"foo\":{\"Locale\":\"en\",\"Text\":\"foo\"}}"));
    decoder.jsonReader.beginObject();
    assertEquals(LocalizedText.english("foo"), decoder.decodeLocalizedText("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeExtensionObject() throws IOException {
    context.getNamespaceTable().add("urn:eclipse:milo:test1");
    context.getNamespaceTable().add("urn:eclipse:milo:test2");

    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var jsonStringXo = ExtensionObject.of("{\"foo\":\"bar\",\"baz\":42}", new NodeId(2, 42));

    var byteStringXo =
        ExtensionObject.of(ByteString.of(new byte[] {0x00, 0x01, 0x02, 0x03}), new NodeId(2, 42));

    var xmlElementXo = ExtensionObject.of(new XmlElement("<foo>bar</foo>"), new NodeId(2, 42));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Body\":{\"foo\":\"bar\",\"baz\":42}}"));
    assertEquals(jsonStringXo, decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Encoding\":1,\"Body\":\"AAECAw==\"}"));
    assertEquals(byteStringXo, decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Encoding\":2,\"Body\":\"<foo>bar</foo>\"}"));
    assertEquals(xmlElementXo, decoder.decodeExtensionObject(null));

    decoder.reset(new StringReader("null"));
    assertNull(decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"foo\":{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Body\":{\"foo\":\"bar\",\"baz\":42}}}"));
    decoder.jsonReader.beginObject();
    assertEquals(jsonStringXo, decoder.decodeExtensionObject("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeDataValue() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    DateTime now = DateTime.now();
    String isoNow = now.toIso8601String();

    var allFieldsValue =
        new DataValue(
            new Variant("foo"),
            new StatusCode(StatusCodes.Good_Overload),
            now,
            ushort(100),
            now,
            ushort(200));

    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                isoNow, isoNow)));
    assertEquals(allFieldsValue, decoder.decodeDataValue(null));

    // omit "Value"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                isoNow, isoNow)));
    assertEquals(
        allFieldsValue.copy(b -> b.setValue(Variant.NULL_VALUE)), decoder.decodeDataValue(null));

    // omit "Status"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                isoNow, isoNow)));
    assertEquals(
        allFieldsValue.copy(b -> b.setStatus(StatusCode.GOOD)), decoder.decodeDataValue(null));

    // omit "SourceTimestamp"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                isoNow)));
    assertEquals(allFieldsValue.copy(b -> b.setSourceTime(null)), decoder.decodeDataValue(null));

    // omit "SourcePicoseconds"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                isoNow, isoNow)));
    assertEquals(
        allFieldsValue.copy(b -> b.setSourcePicoseconds(null)), decoder.decodeDataValue(null));

    // omit "ServerTimestamp"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerPicoseconds\":200}",
                isoNow)));
    assertEquals(allFieldsValue.copy(b -> b.setServerTime(null)), decoder.decodeDataValue(null));

    // omit "ServerPicoseconds"
    decoder.reset(
        new StringReader(
            String.format(
                "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\"}",
                isoNow, isoNow)));
    assertEquals(
        allFieldsValue.copy(b -> b.setServerPicoseconds(null)), decoder.decodeDataValue(null));

    // omit all fields
    decoder.reset(new StringReader("{}"));
    assertEquals(
        new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null), decoder.decodeDataValue(null));

    decoder.reset(
        new StringReader(
            String.format(
                "{\"foo\":{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}}",
                isoNow, isoNow)));
    decoder.jsonReader.beginObject();
    assertEquals(allFieldsValue, decoder.decodeDataValue("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeVariant() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));
    context.getNamespaceTable().add("urn:eclipse:milo:test1");

    decoder.reset(new StringReader("{\"Type\":1,\"Body\":true}"));
    assertEquals(new Variant(true), decoder.decodeVariant(null));

    decoder.reset(new StringReader("{\"Type\":20,\"Body\":\"nsu=urn:eclipse:milo:test1;foo\"}"));
    assertEquals(new Variant(new QualifiedName(1, "foo")), decoder.decodeVariant(null));

    decoder.reset(
        new StringReader(
            "{\"Type\":24,\"Body\":[{\"Type\":12,\"Body\":\"foo\"},{\"Type\":12,\"Body\":\"bar\"}]}"));
    assertEquals(
        new Variant(new Variant[] {new Variant("foo"), new Variant("bar")}),
        decoder.decodeVariant(null));

    int[] value1d = {0, 1, 2, 3};
    int[][] value2d = {
      {0, 2, 3},
      {1, 3, 4}
    };
    int[][][] value3d = {
      {
        {0, 1},
        {2, 3}
      },
      {
        {4, 5},
        {6, 7},
      }
    };

    decoder.reset(new StringReader("{\"Type\":6,\"Body\":[0,1,2,3]}"));
    assertEquals(new Variant(value1d), decoder.decodeVariant(null));

    decoder.reset(new StringReader("{\"Type\":6,\"Body\":[0,2,3,1,3,4],\"Dimensions\":[2,3]}"));
    assertEquals(new Variant(Matrix.ofInt32(value2d)), decoder.decodeVariant(null));

    decoder.reset(
        new StringReader("{\"Type\":6,\"Body\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}"));
    assertEquals(new Variant(Matrix.ofInt32(value3d)), decoder.decodeVariant(null));

    decoder.reset(new StringReader("{\"Type\":6,\"Body\":[1],\"Dimensions\":[2147483647]}"));
    assertThrows(UaSerializationException.class, () -> decoder.decodeVariant(null));

    decoder.reset(new StringReader("{\"foo\":{\"Type\":1,\"Body\":true}}"));
    decoder.jsonReader.beginObject();
    assertEquals(new Variant(true), decoder.decodeVariant("foo"));
    decoder.jsonReader.endObject();
  }

  /**
   * Round-trip companion to {@code OpcUaJsonEncoderTest.encodeVariantArray_nullElementWritesNull}:
   * the encoder writes JSON null for null Variant array elements, so the decoder must accept it.
   */
  @Test
  void decodeVariant_acceptsJsonNull() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("null"));
    assertEquals(Variant.NULL_VALUE, decoder.decodeVariant(null));
  }

  @Test
  void decodeDiagnosticInfo() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var diagnosticInfo = new DiagnosticInfo(0, 1, 2, 3, "foo", null, null);

    var nestedDiagnosticInfo =
        new DiagnosticInfo(4, 5, 6, 7, "bar", StatusCode.GOOD, diagnosticInfo);

    decoder.reset(
        new StringReader(
            "{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}"));
    assertEquals(diagnosticInfo, decoder.decodeDiagnosticInfo(null));

    decoder.reset(
        new StringReader(
            "{\"SymbolicId\":5,\"NamespaceUri\":4,\"Locale\":6,\"LocalizedText\":7,\"AdditionalInfo\":\"bar\",\"InnerStatusCode\":0,\"InnerDiagnosticInfo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}"));
    assertEquals(nestedDiagnosticInfo, decoder.decodeDiagnosticInfo(null));

    decoder.reset(
        new StringReader(
            "{\"foo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}"));
    decoder.jsonReader.beginObject();
    assertEquals(diagnosticInfo, decoder.decodeDiagnosticInfo("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeMessage() {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var message =
        new ReadRequest(
            new RequestHeader(
                NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0), uint(0), "foo", uint(0), null),
            0.0,
            TimestampsToReturn.Both,
            new ReadValueId[] {
              new ReadValueId(new NodeId(0, 1), uint(13), null, QualifiedName.NULL_VALUE)
            });

    decoder.reset(
        new StringReader(
            "{\"TypeId\":\"i=15257\",\"Body\":{\"RequestHeader\":{\"AuthenticationToken\":\"i=0\",\"Timestamp\":\"1601-01-01T00:00:00Z\",\"RequestHandle\":0,\"ReturnDiagnostics\":0,\"AuditEntryId\":\"foo\",\"TimeoutHint\":0,\"AdditionalHeader\":null},\"MaxAge\":0.0,\"TimestampsToReturn\":2,\"NodesToRead\":[{\"NodeId\":\"i=1\",\"AttributeId\":13,\"IndexRange\":null,\"DataEncoding\":null}]}}"));
    assertEquals(message, decoder.decodeMessage(null));
  }

  @Test
  void decodeEnum() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    for (ApplicationType applicationType : ApplicationType.values()) {
      decoder.reset(new StringReader(String.valueOf(applicationType.getValue())));
      assertEquals(applicationType, ApplicationType.from(decoder.decodeEnum(null)));
    }

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(ApplicationType.Server, ApplicationType.from(decoder.decodeEnum("foo")));
    decoder.jsonReader.endObject();
  }

  @MethodSource("decodeStructArguments")
  @ParameterizedTest
  void decodeStruct(UaStructuredType struct, String json) {
    var decoder = new OpcUaJsonDecoder(context, json);

    assertEquals(struct, decoder.decodeStruct(null, struct.getTypeId()));
  }

  static Stream<Arguments> decodeStructArguments() {
    return Stream.of(
        Arguments.of(new XVType(0.0, 0.0f), "{}"),
        Arguments.of(new XVType(1.0, 0.0f), "{\"X\":1.0}"),
        Arguments.of(new XVType(0.0, 1.0f), "{\"Value\":1.0}"),
        Arguments.of(
            new Argument("foo", NodeIds.Int32, -1, null, LocalizedText.english("foo desc")),
            "{\"Name\":\"foo\",\"DataType\":\"i=6\",\"ValueRank\":-1,\"Description\":{\"Locale\":\"en\",\"Text\":\"foo"
                + " desc\"}}"),
        Arguments.of(
            new EUInformation(
                null, 0, LocalizedText.NULL_VALUE, LocalizedText.english("description")),
            "{\"Description\":{\"Locale\":\"en\",\"Text\":\"description\"}}"));
  }

  @Test
  void decodeMatrix() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var matrix2d =
        new Matrix(
            new Integer[][] {
              new Integer[] {0, 1},
              new Integer[] {2, 3}
            });

    var matrix3d =
        new Matrix(
            new Integer[][][] {
              new Integer[][] {{0, 1}, {2, 3}},
              new Integer[][] {{4, 5}, {6, 7}}
            });

    decoder.reset(new StringReader("{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}"));
    assertEquals(matrix2d, decoder.decodeMatrix(null, OpcUaDataType.Int32));

    decoder.reset(new StringReader("{\"Array\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}"));
    assertEquals(matrix3d, decoder.decodeMatrix(null, OpcUaDataType.Int32));

    decoder.reset(new StringReader("{\"foo\":{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix2d, decoder.decodeMatrix("foo", OpcUaDataType.Int32));
    decoder.jsonReader.endObject();

    decoder.reset(
        new StringReader("{\"foo\":{\"Array\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix3d, decoder.decodeMatrix("foo", OpcUaDataType.Int32));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeMatrixRejectsInvalidDimensions() {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{\"Array\":[1],\"Dimensions\":[2147483647]}"));
    assertThrows(
        UaSerializationException.class, () -> decoder.decodeMatrix(null, OpcUaDataType.Int32));

    decoder.reset(new StringReader("{\"Array\":[1],\"Dimensions\":[-1]}"));
    assertThrows(
        UaSerializationException.class, () -> decoder.decodeMatrix(null, OpcUaDataType.Int32));

    decoder.reset(new StringReader("{\"Array\":[1],\"Dimensions\":[2,1]}"));
    assertThrows(
        UaSerializationException.class, () -> decoder.decodeMatrix(null, OpcUaDataType.Int32));
  }

  @Test
  void decodeEnumMatrix() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}"));
    assertEquals(Matrix.ofInt32(new Integer[][] {{0, 1}, {2, 3}}), decoder.decodeEnumMatrix(null));

    decoder.reset(new StringReader("{\"foo\":{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}}"));
    decoder.jsonReader.beginObject();
    assertEquals(Matrix.ofInt32(new Integer[][] {{0, 1}, {2, 3}}), decoder.decodeEnumMatrix("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeStructMatrix() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    XVType[][] xvTypes =
        new XVType[][] {
          new XVType[] {new XVType(0.0d, 1.0f), new XVType(2.0d, 3.0f)},
          new XVType[] {new XVType(4.0d, 5.0f), new XVType(6.0d, 7.0f)}
        };

    var matrix = new Matrix(xvTypes);

    decoder.reset(
        new StringReader(
            "{\"Array\":[{\"Value\":1.0},{\"X\":2.0,\"Value\":3.0},{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}],\"Dimensions\":[2,2]}"));
    assertEquals(matrix, decoder.decodeStructMatrix(null, XVType.TYPE_ID));

    decoder.reset(
        new StringReader(
            "{\"foo\":{\"Array\":[{\"Value\":1.0},{\"X\":2.0,\"Value\":3.0},{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}],\"Dimensions\":[2,2]}}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix, decoder.decodeStructMatrix("foo", XVType.TYPE_ID));
    decoder.jsonReader.endObject();

    decoder.reset(new StringReader("{\"Array\":[{\"Value\":1.0}],\"Dimensions\":[2147483647]}"));
    assertThrows(
        UaSerializationException.class, () -> decoder.decodeStructMatrix(null, XVType.TYPE_ID));
  }

  // ------------------------------------------------------------------------
  // Spec-compliance tests for behaviors defined in section 5.4 of OPC 10000-6.
  // Each test documents the spec rule it covers.
  // ------------------------------------------------------------------------

  /**
   * Issue: {@code decodeStatusCode} only handles {@code JsonToken.NUMBER}, but the encoder emits
   * VERBOSE StatusCode as a JSON object ({@code {"Code":..., "Symbol":"..."}}). The decoder must be
   * symmetric.
   */
  @Test
  void decodeStatusCode_verboseObject() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{\"Code\":1083310080,\"Symbol\":\"Uncertain_InitialValue\"}"));
    assertEquals(
        new StatusCode(StatusCodes.Uncertain_InitialValue), decoder.decodeStatusCode(null));

    decoder.reset(new StringReader("{}"));
    assertEquals(StatusCode.GOOD, decoder.decodeStatusCode(null));
  }

  /**
   * Issue: {@code decodeLocalizedText} unconditionally calls {@code jsonReader.beginObject()}
   * without first checking for {@code JsonToken.NULL}. The encoder emits bare {@code null} for a
   * null LocalizedText (see {@link
   * OpcUaJsonEncoderTest#encodeLocalizedTextVerbose_nullProducesJsonNull()}), so this is a
   * round-trip hazard.
   */
  @Test
  void decodeLocalizedText_acceptsJsonNull() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("null"));
    // Should be tolerated and return a null-valued LocalizedText, not throw.
    LocalizedText result = assertDoesNotThrow(() -> decoder.decodeLocalizedText(null));
    assertEquals(LocalizedText.NULL_VALUE, result);
  }

  /**
   * Issue: {@code decodeExtensionObject} switches on {@code encoding} when reading {@code Body}.
   * JSON object field order is not guaranteed; if {@code Body} appears before {@code Encoding}, the
   * wrong branch is taken.
   */
  @Test
  void decodeExtensionObject_bodyBeforeEncoding() throws IOException {
    context.getNamespaceTable().add("urn:eclipse:milo:test1");
    context.getNamespaceTable().add("urn:eclipse:milo:test2");

    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var byteStringXo =
        ExtensionObject.of(ByteString.of(new byte[] {0x00, 0x01, 0x02, 0x03}), new NodeId(2, 42));

    // Body field appears BEFORE Encoding field.
    decoder.reset(
        new StringReader(
            "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Body\":\"AAECAw==\",\"Encoding\":1}"));
    assertEquals(byteStringXo, decoder.decodeExtensionObject(null));
  }

  /**
   * Issue: {@code decodeDiagnosticInfo} initializes its Int32 locals to 0, so omitted fields decode
   * to 0 instead of the spec-defined default -1.
   *
   * <p>Round-trip: encode {@link DiagnosticInfo#NULL_VALUE} (all -1) → omit all Int32 fields per
   * spec → decode back should yield NULL_VALUE again.
   */
  @Test
  void decodeDiagnosticInfo_omittedInt32FieldsDefaultToMinusOne() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader("{}"));

    DiagnosticInfo decoded = decoder.decodeDiagnosticInfo(null);
    assertEquals(DiagnosticInfo.NULL_VALUE, decoded);
  }

  /**
   * Issue 20: a QualifiedName encoded with a malformed {@code "nsu="} prefix that is missing the
   * {@code ';'} separator must surface as a {@link UaSerializationException} instead of a raw
   * {@link StringIndexOutOfBoundsException}.
   */
  @Test
  void decodeQualifiedName_malformedNsuRejected() {
    var decoder = new OpcUaJsonDecoder(context, new StringReader("\"nsu=urn:no-semicolon-here\""));

    assertThrows(UaSerializationException.class, () -> decoder.decodeQualifiedName(null));
  }

  /**
   * Issue 1773: {@code decodeStruct(String field, ...)} throws {@code Bad_DecodingError} on a
   * member-name mismatch, but the CompactEncoding omits default-valued (and NULL) struct members
   * (OPC 10000-6 §5.4.6, Table 45). Like every sibling field decoder, it must instead stash the
   * peeked name and return {@code null} so the surrounding object keeps decoding.
   */
  @Test
  void decodeStruct_omittedMember_restoresNameAndReturnsNull() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader("{\"Other\":42}"));
    decoder.jsonReader.beginObject();

    // "ConfigurationVersion" is absent; the next member is "Other".
    assertNull(decoder.decodeStruct("ConfigurationVersion", XVType.TYPE_ID));
    // The peeked name was restored, so the following member still decodes.
    assertEquals(42, decoder.decodeInt32("Other"));
    decoder.jsonReader.endObject();
  }

  /**
   * Issue 1773: a struct member encoded as JSON {@code null} (the VerboseEncoding of NULL per OPC
   * 10000-6 §5.4.6, Table 45) must decode to {@code null} rather than failing with "Expected
   * BEGIN_OBJECT but was NULL".
   */
  @Test
  void decodeStruct_jsonNullMember_returnsNull() throws IOException {
    var decoder =
        new OpcUaJsonDecoder(context, new StringReader("{\"ConfigurationVersion\":null}"));
    decoder.jsonReader.beginObject();

    assertNull(decoder.decodeStruct("ConfigurationVersion", XVType.TYPE_ID));
    decoder.jsonReader.endObject();
  }

  /**
   * Issue 1773: the real-world interop case. A spec-conformant Compact document legally omits the
   * all-default {@code ConfigurationVersion} member of {@link DataSetMetaDataType}; the whole
   * structure must still decode, with that member taking its default ({@code null}) value.
   */
  @Test
  void decodeStruct_compactOmitsDefaultValuedStructMember() {
    var decoder = new OpcUaJsonDecoder(context, "{\"Name\":\"Demo\",\"Fields\":[]}");

    var decoded = (DataSetMetaDataType) decoder.decodeStruct(null, DataSetMetaDataType.TYPE_ID);

    assertEquals("Demo", decoded.getName());
    assertNull(decoded.getConfigurationVersion());
  }

  private static byte[] randomBytes16() {
    var random = new Random();
    var bs = new byte[16];
    random.nextBytes(bs);
    return bs;
  }
}
