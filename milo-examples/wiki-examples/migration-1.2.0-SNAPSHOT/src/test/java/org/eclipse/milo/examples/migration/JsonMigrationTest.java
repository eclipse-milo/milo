/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.migration;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Base64;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Imports the preceding 1.1.7 process's files without sharing its class loader. */
public class JsonMigrationTest {
  private final DefaultEncodingContext context = new DefaultEncodingContext();
  private final Path exchange =
      Path.of(System.getProperty("wiki.json.exchange", "../migration-1.1.7/target/json-migration"));

  @Test
  void importLegacyBinaryAndWriteCurrentJson() throws Exception {
    assertEquals(
        "milo-json/1.1.7 -> opc-ua-binary/standard-types-v1\n",
        Files.readString(exchange.resolve("format-version.txt")));
    DataValue scalar = convert("scalar");
    assertEquals(42, scalar.value().value());
    assertEquals(
        "{\"UaType\":6,\"Value\":42}", Files.readString(exchange.resolve("scalar.current.json")));
    DataValue unsigned = convert("unsigned");
    assertArrayEquals(
        new UInteger[] {uint(0), uint(4_000_000_000L)},
        assertInstanceOf(UInteger[].class, unsigned.value().value()));
    assertEquals(StatusCode.UNCERTAIN, unsigned.statusCode());
    assertEquals(new DateTime(Instant.parse("2026-01-01T00:00:00Z")), unsigned.sourceTime());
    assertEquals(unsigned.sourceTime(), unsigned.serverTime());
    Matrix matrix = assertInstanceOf(Matrix.class, convert("matrix").value().value());
    assertArrayEquals(new int[] {2, 2}, matrix.getDimensions());
    assertArrayEquals(new int[] {1, 2, 3, 4}, assertInstanceOf(int[].class, matrix.getElements()));
    ByteString bytes = assertInstanceOf(ByteString.class, convert("bytes").value().value());
    assertFalse(bytes.isNull());
    assertEquals(0, bytes.bytesOrEmpty().length);
    for (String name : new String[] {"range", "range-binary", "range-xml"}) {
      assertEquals(
          new Range(0.0, 100.0),
          assertInstanceOf(ExtensionObject.class, convert(name).value().value()).decode(context));
      var envelope =
          JsonParser.parseString(Files.readString(exchange.resolve(name + ".current.json")))
              .getAsJsonObject()
              .getAsJsonObject("Value");
      assertEquals("i=884", envelope.get("UaTypeId").getAsString());
      assertEquals(100.0, envelope.get("High").getAsDouble());
      assertFalse(envelope.has("UaBody"));
    }

    // This scalar built-in Variant is compatible; that does not identify the input version.
    assertEquals(
        42,
        new OpcUaJsonDecoder(context, Files.readString(exchange.resolve("variant.legacy.json")))
            .decodeVariant(null)
            .value());
    String legacyValue = Files.readString(exchange.resolve("scalar.legacy.json"));
    UaSerializationException nesting =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, legacyValue).decodeDataValue(null));
    assertEquals(StatusCodes.Bad_DecodingError, nesting.getStatusCode().getValue());
    ExtensionObject legacyEnvelope =
        new OpcUaJsonDecoder(context, Files.readString(exchange.resolve("extension.legacy.json")))
            .decodeExtensionObject(null);
    UaSerializationException body =
        assertThrows(UaSerializationException.class, () -> legacyEnvelope.decode(context));
    assertEquals(StatusCodes.Bad_DecodingError, body.getStatusCode().getValue());
    ByteBuf buffer =
        Unpooled.wrappedBuffer(Files.readAllBytes(exchange.resolve("null-matrix-field.bin")));
    try {
      assertTrue(
          new OpcUaBinaryDecoder(context)
              .setBuffer(buffer)
              .decodeMatrix(null, OpcUaDataType.Int32)
              .isNull());
      assertFalse(buffer.isReadable());
    } finally {
      buffer.release();
    }
  }

  // Values read over OPC TCP normally contain Binary ExtensionObjects. The legacy JSON envelope
  // uses an encoding id, so 1.2 must reject it before decoding the structure body.
  @ParameterizedTest
  @ValueSource(strings = {"range-binary", "range-xml"})
  void legacyOpaqueExtensionObjectFailsWhileReadingItsEnvelope(String name) throws Exception {
    String input = Files.readString(exchange.resolve(name + ".variant.legacy.json"));
    var envelope = JsonParser.parseString(input).getAsJsonObject().getAsJsonObject("Value");
    assertEquals(
        name.equals("range-xml") ? "i=885" : "i=886", envelope.get("UaTypeId").getAsString());
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, input).decodeVariant(null));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
    assertTrue(failure.getMessage().contains("ExtensionObject encoding is unresolved"));
  }

  // The XML migration has two independent changes: type identity and a UTF-8 base64 body.
  @Test
  void legacyXmlBodyNeedsBase64AfterItsEnvelopeIdentityIsCorrected() throws Exception {
    JsonObject variant =
        JsonParser.parseString(Files.readString(exchange.resolve("range-xml.variant.legacy.json")))
            .getAsJsonObject();
    JsonObject envelope = variant.getAsJsonObject("Value");
    assertEquals("i=885", envelope.get("UaTypeId").getAsString());
    assertEquals(2, envelope.get("UaEncoding").getAsInt());
    String xml = envelope.get("UaBody").getAsString();
    assertTrue(xml.contains("<"));
    envelope.addProperty("UaTypeId", "i=884");
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, variant.toString()).decodeVariant(null));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
    assertFalse(failure.getMessage().contains("encoding is unresolved"));
    assertInstanceOf(IllegalArgumentException.class, failure.getCause());

    envelope.addProperty(
        "UaBody", Base64.getEncoder().encodeToString(xml.getBytes(StandardCharsets.UTF_8)));
    ExtensionObject repaired =
        assertInstanceOf(
            ExtensionObject.class,
            new OpcUaJsonDecoder(context, variant.toString()).decodeVariant(null).value());
    assertEquals(new Range(0.0, 100.0), repaired.decode(context));

    ExtensionObject.Xml current =
        assertInstanceOf(
            ExtensionObject.Xml.class,
            ExtensionObject.encode(
                context, new Range(0.0, 100.0), OpcUaDefaultXmlEncoding.getInstance()));
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeVariant(null, new Variant(current));
      String json = encoder.getOutputString();
      JsonObject encoded = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("Value");
      assertEquals("i=884", encoded.get("UaTypeId").getAsString());
      assertEquals(2, encoded.get("UaEncoding").getAsInt());
      assertEquals(
          current.getBody().getFragment(),
          new String(
              Base64.getDecoder().decode(encoded.get("UaBody").getAsString()),
              StandardCharsets.UTF_8));
      assertEquals(
          new Range(0.0, 100.0),
          assertInstanceOf(
                  ExtensionObject.class,
                  new OpcUaJsonDecoder(context, json).decodeVariant(null).value())
              .decode(context));
      Files.writeString(exchange.resolve("range-xml.variant.current.json"), json);
    }
  }

  private DataValue convert(String name) throws Exception {
    ByteBuf buffer = Unpooled.wrappedBuffer(Files.readAllBytes(exchange.resolve(name + ".bin")));
    DataValue decoded;
    try {
      decoded = new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeDataValue(null);
      assertFalse(buffer.isReadable());
    } finally {
      buffer.release();
    }
    if (decoded.value().value() instanceof ExtensionObject encoded) {
      ExtensionObject json =
          ExtensionObject.encode(
              context, encoded.decode(context), OpcUaDefaultJsonEncoding.getInstance());
      decoded =
          new DataValue(
              new Variant(json),
              decoded.statusCode(),
              decoded.sourceTime(),
              decoded.sourcePicoseconds(),
              decoded.serverTime(),
              decoded.serverPicoseconds());
    }
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, decoded);
      json = encoder.getOutputString();
    }
    Files.writeString(exchange.resolve(name + ".current.json"), json);
    return new OpcUaJsonDecoder(context, json).decodeDataValue(null);
  }
}
