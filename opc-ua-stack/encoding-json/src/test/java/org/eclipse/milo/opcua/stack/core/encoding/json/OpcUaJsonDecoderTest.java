/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
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
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

class OpcUaJsonDecoderTest {

  private final EncodingContext context = DefaultEncodingContext.INSTANCE;

  @Test
  void readBoolean() throws IOException {
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
  void readSByte() throws IOException {
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
  void readInt16() throws IOException {
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
  void readInt32() throws IOException {
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
  void readInt64() throws IOException {
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
  void readByte() throws IOException {
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
  void readUInt16() throws IOException {
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
  void readUInt32() throws IOException {
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
  void readUInt64() throws IOException {
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
  void readFloat() throws IOException {
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
  void readDouble() throws IOException {
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
  void readString() throws IOException {
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
  void readDateTime() throws IOException {
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
  void readGuid() throws IOException {
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
  void readByteString() throws IOException {
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
  void readXmlElement() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var emptyElement = new XmlElement("");
    decoder.reset(new StringReader("\"\""));
    assertEquals(emptyElement, decoder.decodeXmlElement(null));

    var element = new XmlElement("<foo>bar</foo>");
    decoder.reset(new StringReader(String.format("\"%s\"", element.getFragment())));
    assertEquals(element, decoder.decodeXmlElement(null));

    decoder.reset(new StringReader(String.format("{\"foo\":\"%s\"}", element.getFragment())));
    decoder.jsonReader.beginObject();
    assertEquals(element, decoder.decodeXmlElement("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readNodeId() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    // IdType == UInt32, Namespace = 0
    var nodeId = new NodeId(0, 0);
    decoder.reset(new StringReader("{\"Id\":0}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == UInt32, Namespace != 0
    nodeId = new NodeId(1, 0);
    decoder.reset(new StringReader("{\"Id\":0,\"Namespace\":1}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == String, Namespace = 0
    nodeId = new NodeId(0, "foo");
    decoder.reset(new StringReader("{\"IdType\":1,\"Id\":\"foo\"}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == String, Namespace != 0
    nodeId = new NodeId(1, "foo");
    decoder.reset(new StringReader("{\"IdType\":1,\"Id\":\"foo\",\"Namespace\":1}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == Guid, Namespace = 0
    UUID uuid = UUID.randomUUID();
    nodeId = new NodeId(0, uuid);
    decoder.reset(
        new StringReader("{\"IdType\":2,\"Id\":\"" + uuid.toString().toUpperCase() + "\"}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == Guid, Namespace != 0
    nodeId = new NodeId(1, uuid);
    decoder.reset(
        new StringReader(
            "{\"IdType\":2,\"Id\":\"" + uuid.toString().toUpperCase() + "\",\"Namespace\":1}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == ByteString, Namespace = 0
    ByteString bs = ByteString.of(randomBytes16());
    nodeId = new NodeId(0, bs);
    decoder.reset(
        new StringReader(
            "{\"IdType\":3,\"Id\":\""
                + Base64.getEncoder().encodeToString(bs.bytesOrEmpty())
                + "\"}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    // IdType == ByteString, Namespace != 0
    nodeId = new NodeId(1, bs);
    decoder.reset(
        new StringReader(
            "{\"IdType\":3,\"Id\":\""
                + Base64.getEncoder().encodeToString(bs.bytesOrEmpty())
                + "\",\"Namespace\":1}"));
    assertEquals(nodeId, decoder.decodeNodeId(null));

    nodeId = new NodeId(0, 0);
    decoder.reset(new StringReader("{\"foo\":{\"Id\":0}}"));
    decoder.jsonReader.beginObject();
    assertEquals(nodeId, decoder.decodeNodeId("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readExpandedNodeId() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    // namespace URI specified
    var xni = new ExpandedNodeId(ushort(0), "http://opcfoundation.org/UA/", "foo");
    decoder.reset(
        new StringReader(
            "{\"IdType\":1,\"Id\":\"foo\",\"Namespace\":\"http://opcfoundation.org/UA/\"}"));
    assertEquals(xni, decoder.decodeExpandedNodeId(null));

    // remote server index
    xni = new ExpandedNodeId(ushort(0), null, "foo", uint(1));
    decoder.reset(new StringReader("{\"IdType\":1,\"Id\":\"foo\",\"ServerUri\":1}"));
    assertEquals(xni, decoder.decodeExpandedNodeId(null));

    decoder.reset(new StringReader("{\"foo\":{\"IdType\":1,\"Id\":\"foo\",\"ServerUri\":1}}"));
    decoder.jsonReader.beginObject();
    assertEquals(xni, decoder.decodeExpandedNodeId("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readStatusCode() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("0"));
    assertEquals(StatusCode.GOOD, decoder.decodeStatusCode(null));

    decoder.reset(new StringReader(String.valueOf(StatusCode.UNCERTAIN.getValue())));
    assertEquals(StatusCode.UNCERTAIN, decoder.decodeStatusCode(null));

    decoder.reset(new StringReader(String.valueOf(StatusCode.BAD.getValue())));
    assertEquals(StatusCode.BAD, decoder.decodeStatusCode(null));

    StatusCode[] statusCodes =
        new StatusCode[] {
          new StatusCode(StatusCodes.Good_Overload),
          new StatusCode(StatusCodes.Uncertain_InitialValue),
          new StatusCode(StatusCodes.Bad_DecodingError)
        };

    for (StatusCode statusCode : statusCodes) {
      decoder.reset(new StringReader(String.valueOf(statusCode.getValue())));
      assertEquals(statusCode, decoder.decodeStatusCode(null));
    }

    decoder.reset(new StringReader("{\"foo\":0}"));
    decoder.jsonReader.beginObject();
    assertEquals(StatusCode.GOOD, decoder.decodeStatusCode("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readQualifiedName() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{}"));
    assertEquals(new QualifiedName(0, null), decoder.decodeQualifiedName(null));

    decoder.reset(new StringReader("{\"Uri\":1}"));
    assertEquals(new QualifiedName(1, null), decoder.decodeQualifiedName(null));

    decoder.reset(new StringReader("{\"Name\":\"foo\"}"));
    assertEquals(new QualifiedName(0, "foo"), decoder.decodeQualifiedName(null));

    decoder.reset(new StringReader("{\"Name\":\"foo\",\"Uri\":1}"));
    assertEquals(new QualifiedName(1, "foo"), decoder.decodeQualifiedName(null));

    decoder.reset(new StringReader("{\"foo\":{\"Name\":\"foo\"}}"));
    decoder.jsonReader.beginObject();
    assertEquals(new QualifiedName(0, "foo"), decoder.decodeQualifiedName("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readLocalizedText() throws IOException {
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
  void readExtensionObject() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var jsonStringXo = new ExtensionObject("{\"foo\":\"bar\",\"baz\":42}", new NodeId(2, 42));

    var byteStringXo =
        new ExtensionObject(ByteString.of(new byte[] {0x00, 0x01, 0x02, 0x03}), new NodeId(2, 42));

    var xmlElementXo = new ExtensionObject(new XmlElement("<foo>bar</foo>"), new NodeId(2, 42));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Body\":{\"foo\":\"bar\",\"baz\":42}}"));
    assertEquals(jsonStringXo, decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Encoding\":1,\"Body\":\"AAECAw==\"}"));
    assertEquals(byteStringXo, decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Encoding\":2,\"Body\":\"<foo>bar</foo>\"}"));
    assertEquals(xmlElementXo, decoder.decodeExtensionObject(null));

    decoder.reset(new StringReader("null"));
    assertNull(decoder.decodeExtensionObject(null));

    decoder.reset(
        new StringReader(
            "{\"foo\":{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Body\":{\"foo\":\"bar\",\"baz\":42}}}"));
    decoder.jsonReader.beginObject();
    assertEquals(jsonStringXo, decoder.decodeExtensionObject("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readDataValue() throws IOException {
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
  void readVariant() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("{\"Type\":1,\"Body\":true}"));
    assertEquals(new Variant(true), decoder.decodeVariant(null));

    decoder.reset(new StringReader("{\"Type\":20,\"Body\":{\"Name\":\"foo\",\"Uri\":1}}"));
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

    decoder.reset(new StringReader("{\"foo\":{\"Type\":1,\"Body\":true}}"));
    decoder.jsonReader.beginObject();
    assertEquals(new Variant(true), decoder.decodeVariant("foo"));
    decoder.jsonReader.endObject();
  }

  @Test
  void readDiagnosticInfo() throws IOException {
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
  void readMessage() {
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
            "{\"TypeId\":{\"Id\":15257},\"Body\":{\"RequestHeader\":{\"AuthenticationToken\":{\"Id\":0},\"Timestamp\":\"1601-01-01T00:00:00Z\",\"RequestHandle\":0,\"ReturnDiagnostics\":0,\"AuditEntryId\":\"foo\",\"TimeoutHint\":0,\"AdditionalHeader\":null},\"MaxAge\":0.0,\"TimestampsToReturn\":2,\"NodesToRead\":[{\"NodeId\":{\"Id\":1},\"AttributeId\":13,\"IndexRange\":null,\"DataEncoding\":{\"Name\":null}}]}}"));
    assertEquals(message, decoder.decodeMessage(null));
  }

  @Test
  void readEnum() throws IOException {
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

  @Test
  void readStruct() throws IOException {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    var struct = new Argument("foo", NodeIds.Int32, -1, null, LocalizedText.english("foo desc"));

    decoder.reset(
        new StringReader(
            "{\"Name\":\"foo\",\"DataType\":{\"Id\":6},\"ValueRank\":-1,\"Description\":{\"Locale\":\"en\",\"Text\":\"foo"
                + " desc\"}}"));
    assertEquals(struct, decoder.decodeStruct(null, Argument.TYPE_ID));
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

    decoder.reset(new StringReader("[[0,1],[2,3]]"));
    assertEquals(matrix2d, decoder.decodeMatrix(null, BuiltinDataType.Int32));

    decoder.reset(new StringReader("[[[0,1],[2,3]],[[4,5],[6,7]]]"));
    assertEquals(matrix3d, decoder.decodeMatrix(null, BuiltinDataType.Int32));

    decoder.reset(new StringReader("{\"foo\":[[0,1],[2,3]]}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix2d, decoder.decodeMatrix("foo", BuiltinDataType.Int32));
    decoder.jsonReader.endObject();

    decoder.reset(new StringReader("{\"foo\":[[[0,1],[2,3]],[[4,5],[6,7]]]}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix3d, decoder.decodeMatrix("foo", BuiltinDataType.Int32));
    decoder.jsonReader.endObject();
  }

  @Test
  void decodeEnumMatrix() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, new StringReader(""));

    decoder.reset(new StringReader("[[0,1],[2,3]]"));
    assertEquals(Matrix.ofInt32(new Integer[][] {{0, 1}, {2, 3}}), decoder.decodeEnumMatrix(null));

    decoder.reset(new StringReader("{\"foo\":[[0,1],[2,3]]}"));
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
            "[[{\"X\":0.0,\"Value\":1.0},{\"X\":2.0,\"Value\":3.0}],[{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}]]"));
    assertEquals(matrix, decoder.decodeStructMatrix(null, XVType.TYPE_ID));

    decoder.reset(
        new StringReader(
            "{\"foo\":[[{\"X\":0.0,\"Value\":1.0},{\"X\":2.0,\"Value\":3.0}],[{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}]]}"));
    decoder.jsonReader.beginObject();
    assertEquals(matrix, decoder.decodeStructMatrix("foo", XVType.TYPE_ID));
    decoder.jsonReader.endObject();
  }

  private static byte[] randomBytes16() {
    var random = new Random();
    var bs = new byte[16];
    random.nextBytes(bs);
    return bs;
  }
}
