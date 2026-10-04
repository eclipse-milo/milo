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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
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
    assertEquals(
        new Range(0.0, 100.0),
        assertInstanceOf(ExtensionObject.class, convert("range").value().value()).decode(context));

    // Successful trial decoding cannot identify the input version: bare Variants still decode.
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
