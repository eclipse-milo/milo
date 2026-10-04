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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
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

/** Exports real 1.1.7 JSON through a Binary intermediate for a separate 1.2 process. */
public class JsonMigrationTest {
  private final DefaultEncodingContext context = new DefaultEncodingContext();
  private final Path exchange =
      Path.of(System.getProperty("wiki.json.exchange", "target/json-migration"));

  @Test
  void exportLegacyJsonToVersionedBinaryIntermediate() throws Exception {
    Files.createDirectories(exchange);
    DateTime time = new DateTime(Instant.parse("2026-01-01T00:00:00Z"));
    Map<String, DataValue> cases =
        Map.of(
            "scalar", DataValue.valueOnly(new Variant(42)),
            "unsigned",
                new DataValue(
                    new Variant(new UInteger[] {uint(0), uint(4_000_000_000L)}),
                    StatusCode.UNCERTAIN,
                    time),
            "matrix",
                DataValue.valueOnly(
                    new Variant(
                        new Matrix(
                            new Integer[] {1, 2, 3, 4}, new int[] {2, 2}, OpcUaDataType.Int32))),
            "bytes", DataValue.valueOnly(new Variant(ByteString.NULL_VALUE)),
            "range",
                DataValue.valueOnly(
                    new Variant(
                        ExtensionObject.encode(
                            context,
                            new Range(0.0, 100.0),
                            OpcUaDefaultJsonEncoding.getInstance()))));
    for (var entry : cases.entrySet()) {
      String json;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeDataValue(null, entry.getValue());
        json = encoder.getOutputString();
      }
      Files.writeString(exchange.resolve(entry.getKey() + ".legacy.json"), json);
      DataValue decoded = new OpcUaJsonDecoder(context, json).decodeDataValue(null);
      if (entry.getKey().equals("bytes")) {
        // The old serializer already collapses null ByteString to empty; conversion cannot infer
        // null.
        ByteString bytes = assertInstanceOf(ByteString.class, decoded.value().value());
        assertFalse(bytes.isNull());
        assertEquals(0, bytes.bytesOrEmpty().length);
      }
      if (decoded.value().value() instanceof ExtensionObject encoded) {
        assertEquals(new Range(0.0, 100.0), encoded.decode(context));
        // JSON ExtensionObject bytes cannot be relabeled as Binary. Decode and re-encode the body.
        ExtensionObject binary = ExtensionObject.encode(context, encoded.decode(context));
        decoded =
            new DataValue(
                new Variant(binary),
                decoded.statusCode(),
                decoded.sourceTime(),
                decoded.sourcePicoseconds(),
                decoded.serverTime(),
                decoded.serverPicoseconds());
      }
      ByteBuf buffer = Unpooled.buffer();
      try {
        new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeDataValue(null, decoded);
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.getBytes(0, bytes);
        Files.write(exchange.resolve(entry.getKey() + ".bin"), bytes);
        DataValue checked = new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeDataValue(null);
        assertEquals(decoded.statusCode(), checked.statusCode());
        assertEquals(
            decoded.sourceTime() == null ? DateTime.NULL_VALUE : decoded.sourceTime(),
            checked.sourceTime());
        assertEquals(
            decoded.serverTime() == null ? DateTime.NULL_VALUE : decoded.serverTime(),
            checked.serverTime());
        assertFalse(buffer.isReadable());
      } finally {
        buffer.release();
      }
    }
    assertEquals(
        "{\"Value\":{\"UaType\":6,\"Value\":42}}",
        Files.readString(exchange.resolve("scalar.legacy.json")));
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeVariant(null, new Variant(42));
      assertEquals("{\"UaType\":6,\"Value\":42}", encoder.getOutputString());
      Files.writeString(exchange.resolve("variant.legacy.json"), encoder.getOutputString());
    }
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeExtensionObject(
          null,
          ExtensionObject.encode(
              context, new Range(0.0, 100.0), OpcUaDefaultJsonEncoding.getInstance()));
      String json = encoder.getOutputString();
      assertTrue(json.contains("\"UaTypeId\":\"i=884\""));
      assertTrue(json.contains("\"UaBody\":{"));
      assertEquals(
          new Range(0.0, 100.0),
          new OpcUaJsonDecoder(context, json).decodeExtensionObject(null).decode(context));
      Files.writeString(exchange.resolve("extension.legacy.json"), json);
    }
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeMatrix(null, Matrix.ofNull());
      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.getBytes(0, bytes);
      Files.write(exchange.resolve("null-matrix-field.bin"), bytes);
      assertNull(
          new OpcUaBinaryDecoder(context)
              .setBuffer(buffer)
              .decodeMatrix(null, OpcUaDataType.Int32));
    } finally {
      buffer.release();
    }
    Files.writeString(
        exchange.resolve("format-version.txt"),
        "milo-json/1.1.7 -> opc-ua-binary/standard-types-v1\n");
  }
}
