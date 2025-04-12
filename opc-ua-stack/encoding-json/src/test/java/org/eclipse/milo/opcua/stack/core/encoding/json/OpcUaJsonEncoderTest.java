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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
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

class OpcUaJsonEncoderTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @Test
  void encodeBoolean() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeBoolean(null, true);
      assertEquals("true", encoder.getOutputString());

      encoder.reset();
      encoder.encodeBoolean(null, false);
      assertEquals("false", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeBoolean("foo", true);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":true}", encoder.getOutputString());
    }
  }

  @Test
  void encodeSByte() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeSByte(null, (byte) 0);
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeSByte(null, Byte.MIN_VALUE);
      assertEquals(String.valueOf(Byte.MIN_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.encodeSByte(null, Byte.MAX_VALUE);
      assertEquals(String.valueOf(Byte.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeSByte("foo", (byte) 0);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeInt16() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeInt16(null, (short) 0);
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt16(null, Short.MIN_VALUE);
      assertEquals(String.valueOf(Short.MIN_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt16(null, Short.MAX_VALUE);
      assertEquals(String.valueOf(Short.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeInt16("foo", (short) 0);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeInt32() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeInt32(null, 0);
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt32(null, Integer.MIN_VALUE);
      assertEquals(String.valueOf(Integer.MIN_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt32(null, Integer.MAX_VALUE);
      assertEquals(String.valueOf(Integer.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeInt32("foo", 0);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeInt64() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Int64 and UInt64 values shall be formatted as a decimal number
      // encoded as a JSON string.

      encoder.encodeInt64(null, 0L);
      assertEquals("\"0\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt64(null, Long.MIN_VALUE);
      assertEquals("\"" + Long.MIN_VALUE + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeInt64(null, Long.MAX_VALUE);
      assertEquals("\"" + Long.MAX_VALUE + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeInt64("foo", Long.MAX_VALUE);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":\"" + Long.MAX_VALUE + "\"}", encoder.getOutputString());
    }
  }

  @Test
  void encodeByte() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeByte(null, ubyte(0));
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeByte(null, UByte.MAX);
      assertEquals(String.valueOf(UByte.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeByte("foo", ubyte(0));
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeUInt16() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeUInt16(null, ushort(0));
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeUInt16(null, UShort.MAX);
      assertEquals(String.valueOf(UShort.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeUInt16("foo", ushort(0));
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeUInt32() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeUInt32(null, uint(0));
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeUInt32(null, UInteger.MAX);
      assertEquals(String.valueOf(UInteger.MAX_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeUInt32("foo", uint(0));
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeUInt64() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Int64 and UInt64 values shall be formatted as a decimal number
      // encoded as a JSON string (See the XML encoding of 64-bit values
      // described in 5.3.1.3).

      encoder.encodeUInt64(null, ULong.MIN);
      assertEquals("\"" + ULong.MIN_VALUE + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeUInt64(null, ULong.MAX);
      assertEquals("\"" + ULong.MAX_VALUE + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeUInt64("foo", ULong.MAX);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":\"" + ULong.MAX_VALUE + "\"}", encoder.getOutputString());
    }
  }

  @Test
  void encodeFloat() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Normal Float and Double values shall be encoded as a JSON number.

      encoder.encodeFloat(null, 0.0f);
      assertEquals("0.0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeFloat(null, Float.MIN_VALUE);
      assertEquals(String.valueOf(Float.MIN_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.encodeFloat(null, Float.MAX_VALUE);
      assertEquals(String.valueOf(Float.MAX_VALUE), encoder.getOutputString());

      // Special floating-point numbers such as positive infinity (INF),
      // negative infinity (-INF) and not-a- number (NaN) shall be
      // represented by the values "Infinity", "-Infinity" and "NaN" encoded
      // as a JSON string.

      encoder.reset();
      encoder.encodeFloat(null, Float.POSITIVE_INFINITY);
      assertEquals("\"Infinity\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeFloat(null, Float.NEGATIVE_INFINITY);
      assertEquals("\"-Infinity\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeFloat(null, Float.NaN);
      assertEquals("\"NaN\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeFloat("foo", 0.0f);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0.0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeDouble() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Normal Float and Double values shall be encoded as a JSON number.

      encoder.encodeDouble(null, 0.0);
      assertEquals("0.0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeDouble(null, Double.MIN_VALUE);
      assertEquals(String.valueOf(Double.MIN_VALUE), encoder.getOutputString());

      encoder.reset();
      encoder.encodeDouble(null, Double.MAX_VALUE);
      assertEquals(String.valueOf(Double.MAX_VALUE), encoder.getOutputString());

      // Special floating-point numbers such as positive infinity (INF),
      // negative infinity (-INF) and not-a- number (NaN) shall be
      // represented by the values "Infinity", "-Infinity" and "NaN" encoded
      // as a JSON string.

      encoder.reset();
      encoder.encodeDouble(null, Double.POSITIVE_INFINITY);
      assertEquals("\"Infinity\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeDouble(null, Double.NEGATIVE_INFINITY);
      assertEquals("\"-Infinity\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeDouble(null, Double.NaN);
      assertEquals("\"NaN\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeDouble("foo", 0.0);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0.0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeString() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // String values shall be encoded as JSON strings.
      // Any characters which are not allowed in JSON strings are escaped
      // using the rules defined in RFC 7159.

      encoder.encodeString(null, "");
      assertEquals("\"\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeString(null, "foo");
      assertEquals("\"foo\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeString(null, "\"quoted\"");
      assertEquals("\"\\\"quoted\\\"\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeString("foo", "bar");
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":\"bar\"}", encoder.getOutputString());
    }
  }

  @Test
  void encodeDateTime() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // DateTime values shall be formatted as specified by ISO 8601:2004
      // and encoded as a JSON string.

      encoder.encodeDateTime(null, new DateTime(DateTime.MIN_ISO_8601_INSTANT));
      assertEquals("\"" + DateTime.MIN_ISO_8601_STRING + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeDateTime(null, new DateTime(DateTime.MAX_ISO_8601_INSTANT));
      assertEquals("\"9999-12-31T23:59:59Z\"", encoder.getOutputString());

      DateTime now = DateTime.nowNanos();
      String isoNow = now.toIso8601String();
      encoder.reset();
      encoder.encodeDateTime(null, now);
      assertEquals("\"" + isoNow + "\"", encoder.getOutputString());

      // DateTime values which exceed the minimum or maximum values supported
      // on a platform shall be encoded as "0001-01-01T00:00:00Z" or
      // "9999-12-31T23:59:59Z" respectively. During decoding, these values
      // shall be converted to the minimum or maximum values supported on the
      // platform.

      encoder.reset();
      encoder.encodeDateTime(
          null, new DateTime(DateTime.MIN_ISO_8601_INSTANT.minus(1, ChronoUnit.SECONDS)));
      assertEquals("\"" + DateTime.MIN_ISO_8601_STRING + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeDateTime(
          null, new DateTime(DateTime.MAX_ISO_8601_INSTANT.plus(1, ChronoUnit.SECONDS)));
      assertEquals("\"" + DateTime.MAX_ISO_8601_STRING + "\"", encoder.getOutputString());

      now = DateTime.nowNanos();
      isoNow = now.toIso8601String();

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeDateTime("foo", now);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":\"" + isoNow + "\"}", encoder.getOutputString());
    }
  }

  @Test
  void encodeGuid() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeGuid(null, UUID.fromString("00000000-0000-0000-0000-000000000000"));
      assertEquals("\"00000000-0000-0000-0000-000000000000\"", encoder.getOutputString());

      UUID uuid = UUID.randomUUID();
      encoder.reset();
      encoder.encodeGuid(null, uuid);
      assertEquals("\"" + uuid.toString().toUpperCase() + "\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeGuid("foo", uuid);
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":\"" + uuid.toString().toUpperCase() + "\"}", encoder.getOutputString());
    }
  }

  @Test
  void encodeByteString() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // ByteString values shall be formatted as a Base64 text and encoded as
      // a JSON string.
      // Any characters which are not allowed in JSON strings are escaped
      // using the rules defined in RFC 7159.

      for (int i = 0; i < 100; i++) {
        ByteString bs = ByteString.of(randomBytes(16 * i));
        encoder.reset();
        encoder.encodeByteString(null, bs);
        assertEquals(
            "\"" + Base64.getEncoder().encodeToString(bs.bytes()) + "\"",
            encoder.getOutputString());
      }

      ByteString bs = ByteString.of(randomBytes(16));
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeByteString("foo", bs);
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":\"" + Base64.getEncoder().encodeToString(bs.bytes()) + "\"}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeXmlElement() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeXmlElement(null, new XmlElement(""));
      assertEquals("\"\"", encoder.getOutputString());

      encoder.reset();
      encoder.encodeXmlElement(null, new XmlElement("<foo>bar</foo>"));
      assertEquals("\"<foo>bar</foo>\"", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeXmlElement("foo", new XmlElement("<foo>bar</foo>"));
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":\"<foo>bar</foo>\"}", encoder.getOutputString());
    }
  }

  @ParameterizedTest(name = "field={0} {1} -> {2}")
  @MethodSource("encodeNodeIdArguments")
  void encodeNodeId(String field, NodeId nodeId, String expected) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test1");
      encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test2");

      if (field != null) {
        encoder.jsonWriter.beginObject();
      }

      encoder.encodeNodeId(field, nodeId);

      if (field != null) {
        encoder.jsonWriter.endObject();
      }

      assertEquals(expected, encoder.getOutputString());
    }
  }

  static Stream<Arguments> encodeNodeIdArguments() {
    var uuid = UUID.randomUUID();
    var bs = ByteString.of(randomBytes(16));

    return Stream.of(
        // field, nodeId, expected
        // IdType == UInt32, Namespace = 0
        Arguments.of(null, new NodeId(0, uint(0)), "\"i=0\""),
        // IdType == UInt32, Namespace != 0
        Arguments.of(null, new NodeId(1, uint(0)), "\"nsu=urn:eclipse:milo:test1;i=0\""),
        // IdType == String, Namespace = 0
        Arguments.of(null, new NodeId(0, "foo"), "\"s=foo\""),
        // IdType == String, Namespace != 0
        Arguments.of(null, new NodeId(1, "foo"), "\"nsu=urn:eclipse:milo:test1;s=foo\""),
        // IdType == UInt32, Namespace > 1
        Arguments.of(null, new NodeId(2, uint(0)), "\"nsu=urn:eclipse:milo:test2;i=0\""),

        // IdType == Guid, Namespace = 0
        Arguments.of(
            null, new NodeId(0, uuid), "\"g=%s\"".formatted(uuid.toString().toUpperCase())),

        // IdType == Guid, Namespace != 0
        Arguments.of(
            null,
            new NodeId(1, uuid),
            "\"nsu=urn:eclipse:milo:test1;g=%s\"".formatted(uuid.toString().toUpperCase())),

        // IdType == ByteString, Namespace = 0
        Arguments.of(
            null,
            new NodeId(0, bs),
            "\"b=%s\"".formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty()))),

        // IdType == ByteString, Namespace != 0
        Arguments.of(
            null,
            new NodeId(1, bs),
            "\"nsu=urn:eclipse:milo:test1;b=%s\""
                .formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty()))),

        // Namespace > 1 but not in table
        Arguments.of(null, new NodeId(99, uint(0)), "\"s=ns=99;i=0\""),
        // key != null
        Arguments.of(
            "foo", new NodeId(1, "foo"), "{\"foo\":\"nsu=urn:eclipse:milo:test1;s=foo\"}"));
  }

  @ParameterizedTest(name = "field={0} {1} -> {2}")
  @MethodSource("encodeExpandedNodeIdArguments")
  void encodeExpandedNodeId(String field, ExpandedNodeId expandedNodeId, String expected)
      throws Exception {

    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Set up namespace table
      encoder
          .encodingContext
          .getNamespaceTable()
          .update(
              map -> {
                map.clear();
                map.put(ushort(0), "http://opcfoundation.org/UA/"); // Index 0
                map.put(ushort(1), "urn:eclipse:milo:test1"); // Index 1
                map.put(ushort(2), "urn:eclipse:milo:test2"); // Index 2
              });

      // Set up server table
      encoder
          .encodingContext
          .getServerTable()
          .update(
              map -> {
                map.clear();
                map.put(uint(0), "localhost"); // Index 0
                map.put(uint(1), "urn:eclipse:milo:server1"); // Index 1
                map.put(uint(2), "urn:eclipse:milo:server2"); // Index 2
              });

      if (field != null) {
        encoder.jsonWriter.beginObject();
      }

      encoder.encodeExpandedNodeId(field, expandedNodeId);

      if (field != null) {
        encoder.jsonWriter.endObject();
      }

      assertEquals(expected, encoder.getOutputString());
    }
  }

  static Stream<Arguments> encodeExpandedNodeIdArguments() {
    var uuid = UUID.randomUUID();
    var bs = ByteString.of(randomBytes(16));

    return Stream.of(
        // field, expandedNodeId, expected
        // Local server (serverIndex = 0), namespace = 0, different identifier types
        Arguments.of(null, ExpandedNodeId.of(uint(0)), "\"i=0\""),
        Arguments.of(null, ExpandedNodeId.of("foo"), "\"s=foo\""),
        Arguments.of(
            null, ExpandedNodeId.of(uuid), "\"g=%s\"".formatted(uuid.toString().toUpperCase())),
        Arguments.of(
            null,
            ExpandedNodeId.of(bs),
            "\"b=%s\"".formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty()))),

        // Local server (serverIndex = 0), namespace != 0, different identifier types
        Arguments.of(
            null, ExpandedNodeId.of(ushort(1), uint(0)), "\"nsu=urn:eclipse:milo:test1;i=0\""),
        Arguments.of(
            null, ExpandedNodeId.of(ushort(1), "foo"), "\"nsu=urn:eclipse:milo:test1;s=foo\""),
        Arguments.of(
            null,
            ExpandedNodeId.of(ushort(1), uuid),
            "\"nsu=urn:eclipse:milo:test1;g=%s\"".formatted(uuid.toString().toUpperCase())),
        Arguments.of(
            null,
            ExpandedNodeId.of(ushort(1), bs),
            "\"nsu=urn:eclipse:milo:test1;b=%s\""
                .formatted(Base64.getEncoder().encodeToString(bs.bytesOrEmpty()))),

        // Local server (serverIndex = 0), namespace > 1
        Arguments.of(
            null, ExpandedNodeId.of(ushort(2), uint(0)), "\"nsu=urn:eclipse:milo:test2;i=0\""),

        // Non-local server (serverIndex != 0), namespace = 0
        Arguments.of(
            null,
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of(uint(1)),
                ExpandedNodeId.NamespaceReference.of(0),
                uint(0)),
            "\"svu=urn:eclipse:milo:server1;i=0\""),

        // Non-local server (serverIndex != 0), namespace != 0
        Arguments.of(
            null,
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of(uint(1)),
                ExpandedNodeId.NamespaceReference.of(1),
                uint(0)),
            "\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;i=0\""),

        // Server URI, namespace = 0
        Arguments.of(
            null,
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of("urn:eclipse:milo:server1"),
                ExpandedNodeId.NamespaceReference.of(0),
                uint(0)),
            "\"svu=urn:eclipse:milo:server1;i=0\""),

        // Server URI, namespace URI
        Arguments.of(
            null,
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of("urn:eclipse:milo:server1"),
                ExpandedNodeId.NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0)),
            "\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;i=0\""),

        // Namespace URI, local server
        Arguments.of(
            null,
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of(uint(0)),
                ExpandedNodeId.NamespaceReference.of("urn:eclipse:milo:test1"),
                uint(0)),
            "\"nsu=urn:eclipse:milo:test1;i=0\""),

        // With field name
        Arguments.of(
            "foo",
            ExpandedNodeId.of(ushort(1), "bar"),
            "{\"foo\":\"nsu=urn:eclipse:milo:test1;s=bar\"}"),

        // With field name, server URI, namespace URI
        Arguments.of(
            "foo",
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of("urn:eclipse:milo:server1"),
                ExpandedNodeId.NamespaceReference.of("urn:eclipse:milo:test1"),
                "bar"),
            "{\"foo\":\"svu=urn:eclipse:milo:server1;nsu=urn:eclipse:milo:test1;s=bar\"}"));
  }

  @Test
  void encodeStatusCodeCompact() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.COMPACT;

      // compact form without field
      encoder.encodeStatusCode(null, StatusCode.GOOD);
      assertEquals("0", encoder.getOutputString());

      encoder.reset();
      encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Uncertain_InitialValue));
      assertEquals(Long.toString(StatusCodes.Uncertain_InitialValue), encoder.getOutputString());

      encoder.reset();
      encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Bad_UnexpectedError));
      assertEquals(Long.toString(StatusCodes.Bad_UnexpectedError), encoder.getOutputString());

      // compact form with field
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeStatusCode("foo", StatusCode.GOOD);
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":0}", encoder.getOutputString());
    }
  }

  @Test
  void encodeStatusCodeVerbose() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.VERBOSE;

      // verbose form without field
      encoder.encodeStatusCode(null, StatusCode.GOOD);
      assertEquals("", encoder.getOutputString());

      encoder.reset();
      encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Uncertain_InitialValue));
      assertEquals(
          "{\"Code\":1083310080,\"Symbol\":\"Uncertain_InitialValue\"}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Bad_UnexpectedError));
      assertEquals(
          "{\"Code\":2147549184,\"Symbol\":\"Bad_UnexpectedError\"}", encoder.getOutputString());

      // verbose form with field
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeStatusCode("foo", StatusCode.GOOD);
      encoder.jsonWriter.endObject();
      assertEquals("{}", encoder.getOutputString()); // key/value omitted because code==0

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeStatusCode("foo", new StatusCode(StatusCodes.Uncertain_InitialValue));
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":{\"Code\":1083310080,\"Symbol\":\"Uncertain_InitialValue\"}}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeQualifiedName() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      // Test name in namespace 0
      encoder.encodeQualifiedName(null, new QualifiedName(0, "TestName"));
      assertEquals("\"TestName\"", encoder.getOutputString());

      // Test name in non-zero namespace
      context.getNamespaceTable().add("urn:test:namespace");
      encoder.reset();
      encoder.encodeQualifiedName(null, new QualifiedName(1, "TestName"));
      assertEquals("\"nsu=urn:test:namespace;TestName\"", encoder.getOutputString());

      // Test with field name
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeQualifiedName("qname", new QualifiedName(0, "TestName"));
      encoder.jsonWriter.endObject();
      assertEquals("{\"qname\":\"TestName\"}", encoder.getOutputString());

      // Test with field name and non-zero namespace
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeQualifiedName("qname", new QualifiedName(1, "TestName"));
      encoder.jsonWriter.endObject();
      assertEquals("{\"qname\":\"nsu=urn:test:namespace;TestName\"}", encoder.getOutputString());

      // Test null name
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeQualifiedName("qname", QualifiedName.NULL_VALUE);
      encoder.jsonWriter.endObject();
      assertEquals("{\"qname\":null}", encoder.getOutputString());
    }
  }

  @Test
  void encodeLocalizedText() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeLocalizedText(null, LocalizedText.english("foo"));
      assertEquals("{\"Locale\":\"en\",\"Text\":\"foo\"}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeLocalizedText(null, new LocalizedText("en", null));
      assertEquals("{\"Locale\":\"en\"}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeLocalizedText(null, new LocalizedText(null, "foo"));
      assertEquals("{\"Text\":\"foo\"}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeLocalizedText(null, new LocalizedText(null, null));
      assertEquals("null", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeLocalizedText("foo", LocalizedText.english("foo"));
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":{\"Locale\":\"en\",\"Text\":\"foo\"}}", encoder.getOutputString());
    }
  }

  @Test
  void encodeExtensionObject() throws Exception {
    context.getNamespaceTable().add("urn:eclipse:milo:test1");
    context.getNamespaceTable().add("urn:eclipse:milo:test2");

    try (var encoder = new OpcUaJsonEncoder(context)) {
      var byteStringXo =
          ExtensionObject.of(ByteString.of(new byte[] {0x00, 0x01, 0x02, 0x03}), new NodeId(2, 42));

      var xmlElementXo = ExtensionObject.of(new XmlElement("<foo>bar</foo>"), new NodeId(2, 42));

      var jsonStringXo = ExtensionObject.of("{\"foo\":\"bar\",\"baz\":42}", new NodeId(2, 42));

      encoder.encodeExtensionObject(null, jsonStringXo);
      assertEquals(
          "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Body\":{\"foo\":\"bar\",\"baz\":42}}",
          encoder.getOutputString());

      encoder.reset();
      encoder.encodeExtensionObject(null, xmlElementXo);
      assertEquals(
          "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Encoding\":2,\"Body\":\"<foo>bar</foo>\"}",
          encoder.getOutputString());

      encoder.reset();
      encoder.encodeExtensionObject(null, byteStringXo);
      assertEquals(
          "{\"TypeId\":\"nsu=urn:eclipse:milo:test2;i=42\",\"Encoding\":1,\"Body\":\"AAECAw==\"}",
          encoder.getOutputString());

      encoder.reset();
      encoder.encodeExtensionObject(null, null);
      assertEquals("null", encoder.getOutputString());
    }
  }

  @Test
  void encodeDataValue() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      DateTime now = DateTime.now();
      String isoNow = now.toIso8601String();

      DataValue allFieldsValue =
          new DataValue(
              new Variant("foo"),
              new StatusCode(StatusCodes.Good_Overload),
              now,
              ushort(100),
              now,
              ushort(200));

      encoder.encodeDataValue(null, allFieldsValue);
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
              isoNow, isoNow),
          encoder.getOutputString());

      // omit "Value"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setValue(Variant.NULL_VALUE)));
      assertEquals(
          String.format(
              "{\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
              isoNow, isoNow),
          encoder.getOutputString());

      // omit "Status"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setStatus(StatusCode.GOOD)));
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
              isoNow, isoNow),
          encoder.getOutputString());

      // omit "SourceTimestamp"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setSourceTime(null)));
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
              isoNow),
          encoder.getOutputString());

      // omit "SourcePicoseconds"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setSourcePicoseconds(null)));
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
              isoNow, isoNow),
          encoder.getOutputString());

      // omit "ServerTimestamp"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setServerTime(null)));
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerPicoseconds\":200}",
              isoNow),
          encoder.getOutputString());

      // omit "ServerPicoseconds"
      encoder.reset();
      encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setServerPicoseconds(null)));
      assertEquals(
          String.format(
              "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\"}",
              isoNow, isoNow),
          encoder.getOutputString());

      // omit all fields
      encoder.reset();
      encoder.encodeDataValue(null, new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null));
      assertEquals("", encoder.getOutputString());

      // omit all fields while embedded in object
      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeDataValue("foo", new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null));
      encoder.jsonWriter.endObject();
      assertEquals("{}", encoder.getOutputString());
    }
  }

  @Test
  void encodeVariant() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      context.getNamespaceTable().add("urn:eclipse:milo:test1");

      encoder.encodeVariant(null, new Variant(true));
      assertEquals("{\"Type\":1,\"Body\":true}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeVariant(null, new Variant(new QualifiedName(1, "foo")));
      assertEquals(
          "{\"Type\":20,\"Body\":\"nsu=urn:eclipse:milo:test1;foo\"}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeVariant(
          null, new Variant(new Variant[] {new Variant("foo"), new Variant("bar")}));
      assertEquals(
          "{\"Type\":24,\"Body\":[{\"Type\":12,\"Body\":\"foo\"},{\"Type\":12,\"Body\":\"bar\"}]}",
          encoder.getOutputString());

      encoder.reset();
      encoder.encodeVariant(null, new Variant(Matrix.ofInt32(new int[][] {{0, 1}, {2, 3}})));
      assertEquals(
          "{\"Type\":6,\"Body\":[0,1,2,3],\"Dimensions\":[2,2]}", encoder.getOutputString());

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

      encoder.reset();
      encoder.encodeVariant(null, new Variant(value1d));
      assertEquals("{\"Type\":6,\"Body\":[0,1,2,3]}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value2d)));
      assertEquals(
          "{\"Type\":6,\"Body\":[0,2,3,1,3,4],\"Dimensions\":[2,3]}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value3d)));
      assertEquals(
          "{\"Type\":6,\"Body\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeDiagnosticInfo() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      var diagnosticInfo = new DiagnosticInfo(0, 1, 2, 3, "foo", null, null);

      var nestedDiagnosticInfo =
          new DiagnosticInfo(4, 5, 6, 7, "bar", StatusCode.GOOD, diagnosticInfo);

      encoder.encodeDiagnosticInfo(null, diagnosticInfo);
      assertEquals(
          "{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}",
          encoder.getOutputString());

      encoder.reset();
      encoder.encodeDiagnosticInfo(null, nestedDiagnosticInfo);
      assertEquals(
          "{\"SymbolicId\":5,\"NamespaceUri\":4,\"Locale\":6,\"LocalizedText\":7,\"AdditionalInfo\":\"bar\",\"InnerStatusCode\":0,\"InnerDiagnosticInfo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}",
          encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeDiagnosticInfo("foo", diagnosticInfo);
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeMessage() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      var message =
          new ReadRequest(
              new RequestHeader(
                  NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0), uint(0), "foo", uint(0), null),
              0.0,
              TimestampsToReturn.Both,
              new ReadValueId[] {
                new ReadValueId(new NodeId(0, 1), uint(13), null, QualifiedName.NULL_VALUE)
              });

      encoder.encodeMessage(null, message);
      assertEquals(
          "{\"TypeId\":\"i=15257\",\"Body\":{\"RequestHeader\":{\"Timestamp\":\"1601-01-01T00:00:00Z\",\"AuditEntryId\":\"foo\"},\"TimestampsToReturn\":2,\"NodesToRead\":[{\"NodeId\":\"i=1\",\"AttributeId\":13}]}}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeEnumCompact() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.COMPACT;

      for (ApplicationType applicationType : ApplicationType.values()) {
        encoder.reset();
        encoder.encodeEnum(null, applicationType);
        assertEquals(String.valueOf(applicationType.getValue()), encoder.getOutputString());

        encoder.reset();
        encoder.jsonWriter.beginObject();
        encoder.encodeEnum("foo", applicationType);
        encoder.jsonWriter.endObject();
        assertEquals(
            String.format("{\"foo\":%d}", applicationType.getValue()), encoder.getOutputString());
      }
    }
  }

  @Test
  void encodeEnumVerbose() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.VERBOSE;

      for (ApplicationType applicationType : ApplicationType.values()) {
        String expected =
            String.format("\"%s_%s\"", applicationType.getName(), applicationType.getValue());

        encoder.reset();
        encoder.encodeEnum(null, applicationType);
        assertEquals(expected, encoder.getOutputString());

        encoder.reset();
        encoder.jsonWriter.beginObject();
        encoder.encodeEnum("foo", applicationType);
        encoder.jsonWriter.endObject();
        assertEquals(String.format("{\"foo\":%s}", expected), encoder.getOutputString());
      }
    }
  }

  @Test
  void encodeStruct() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      var struct = new Argument("foo", NodeIds.Int32, -1, null, LocalizedText.english("foo desc"));

      encoder.encodeStruct(null, struct, Argument.TYPE_ID);
      assertEquals(
          "{\"Name\":\"foo\",\"DataType\":\"i=6\",\"ValueRank\":-1,\"Description\":{\"Locale\":\"en\",\"Text\":\"foo"
              + " desc\"}}",
          encoder.getOutputString());
    }
  }

  @MethodSource("encodeStructCompactArguments")
  @ParameterizedTest
  void encodeStructCompact(UaStructuredType struct, String expectedJson) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.COMPACT;

      encoder.encodeStruct(null, struct, struct.getTypeId());
      String encodedJson = encoder.getOutputString();

      assertEquals(expectedJson, encodedJson);
    }
  }

  static Stream<Arguments> encodeStructCompactArguments() {
    return Stream.of(
        Arguments.of(new XVType(0.0, 0.0f), "{}"),
        Arguments.of(new XVType(1.0, 0.0f), "{\"X\":1.0}"),
        Arguments.of(new XVType(0.0, 1.0f), "{\"Value\":1.0}"),
        Arguments.of(
            new EUInformation(
                null, 0, LocalizedText.NULL_VALUE, LocalizedText.english("description")),
            "{\"Description\":{\"Locale\":\"en\",\"Text\":\"description\"}}"));
  }

  @MethodSource("encodeStructVerboseArguments")
  @ParameterizedTest
  void encodeStructVerbose(UaStructuredType struct, String expectedJson) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encoding = Encoding.VERBOSE;

      encoder.encodeStruct(null, struct, struct.getTypeId());
      String encodedJson = encoder.getOutputString();

      assertEquals(expectedJson, encodedJson);
    }
  }

  static Stream<Arguments> encodeStructVerboseArguments() {
    return Stream.of(
        Arguments.of(new XVType(0.0, 0.0f), "{\"X\":0.0,\"Value\":0.0}"),
        Arguments.of(new XVType(1.0, 0.0f), "{\"X\":1.0,\"Value\":0.0}"),
        Arguments.of(new XVType(0.0, 1.0f), "{\"X\":0.0,\"Value\":1.0}"),
        Arguments.of(
            new EUInformation(
                null, 0, LocalizedText.NULL_VALUE, LocalizedText.english("description")),
            "{\"NamespaceUri\":null,\"UnitId\":0,\"DisplayName\":null,\"Description\":{\"Locale\":\"en\",\"Text\":\"description\"}}"));
  }

  @Test
  void encodeBooleanArray() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeBooleanArray(null, null);
      assertEquals("", encoder.getOutputString());

      encoder.reset();
      encoder.encodeBooleanArray(null, new Boolean[] {});
      assertEquals("[]", encoder.getOutputString());

      encoder.reset();
      encoder.encodeBooleanArray(null, new Boolean[] {true, false, true});
      assertEquals("[true,false,true]", encoder.getOutputString());

      encoder.reset();
      encoder.encodeBooleanArray(null, new Boolean[] {true, false, null});
      assertEquals("[true,false,null]", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeBooleanArray("foo", new Boolean[] {true, false, true});
      encoder.jsonWriter.endObject();
      assertEquals("{\"foo\":[true,false,true]}", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeBooleanArray("foo", null);
      encoder.jsonWriter.endObject();
      assertEquals("{}", encoder.getOutputString());
    }
  }

  @Test
  void encodeMatrix() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
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

      encoder.encodeMatrix(null, matrix2d);
      assertEquals("{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}", encoder.getOutputString());

      encoder.reset();
      encoder.encodeMatrix(null, matrix3d);
      assertEquals(
          "{\"Array\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeMatrix("foo", matrix2d);
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}}", encoder.getOutputString());

      encoder.reset();
      encoder.jsonWriter.beginObject();
      encoder.encodeMatrix("foo", matrix3d);
      encoder.jsonWriter.endObject();
      assertEquals(
          "{\"foo\":{\"Array\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}}",
          encoder.getOutputString());
    }
  }

  @Test
  void encodeEnumMatrix() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      var applicationTypes =
          new ApplicationType[][] {
            new ApplicationType[] {ApplicationType.Server, ApplicationType.Client},
            new ApplicationType[] {ApplicationType.ClientAndServer, ApplicationType.DiscoveryServer}
          };

      var matrix = new Matrix(applicationTypes);

      encoder.encodeEnumMatrix(null, matrix);
      assertEquals("{\"Array\":[0,1,2,3],\"Dimensions\":[2,2]}", encoder.getOutputString());
    }
  }

  @Test
  void encodeStructMatrix() throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      XVType[][] xvTypes =
          new XVType[][] {
            new XVType[] {new XVType(0.0d, 1.0f), new XVType(2.0d, 3.0f)},
            new XVType[] {new XVType(4.0d, 5.0f), new XVType(6.0d, 7.0f)}
          };

      var matrix = new Matrix(xvTypes);

      encoder.encodeStructMatrix(null, matrix, XVType.TYPE_ID);
      assertEquals(
          "{\"Array\":[{\"Value\":1.0},{\"X\":2.0,\"Value\":3.0},{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}],\"Dimensions\":[2,2]}",
          encoder.getOutputString());
    }
  }

  private static byte[] randomBytes(int length) {
    var random = new Random();
    var bs = new byte[length];
    random.nextBytes(bs);
    return bs;
  }
}
