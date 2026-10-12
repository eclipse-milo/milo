/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * ByteString is a nullable built-in type (Part 6 §5.1.2, Table 1). A NULL ByteString is the JSON
 * literal {@code null} in a VERBOSE field and in any array element (Part 6 §5.4.2.1), while an
 * empty ByteString is the Base64 string {@code ""}.
 *
 * <p>{@link ByteString#equals} treats null and empty as equal, so these tests check {@link
 * ByteString#isNull()} directly.
 */
class JsonByteStringNullTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @Test
  void verboseEncodesNullByteStringFieldAsJsonNull() throws Exception {
    assertEquals(
        "{\"Algorithm\":\"urn:test\",\"Signature\":null}",
        encodeSignatureData(Encoding.VERBOSE, ByteString.NULL_VALUE));
  }

  @Test
  void compactOmitsNullByteStringField() throws Exception {
    assertEquals(
        "{\"Algorithm\":\"urn:test\"}",
        encodeSignatureData(Encoding.COMPACT, ByteString.NULL_VALUE));
  }

  // An empty ByteString is a present value in both modes and must not become null.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesEmptyByteStringFieldAsEmptyString(Encoding encoding) throws Exception {
    assertEquals(
        "{\"Algorithm\":\"urn:test\",\"Signature\":\"\"}",
        encodeSignatureData(encoding, ByteString.of(new byte[0])));
  }

  // The decoder must accept the VERBOSE null form and yield a null ByteString, as the binary and
  // XML decoders do.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void decodesJsonNullByteStringFieldAsNullValue(Encoding encoding) {
    var decoder = new OpcUaJsonDecoder(context, "{\"Algorithm\":\"urn:test\",\"Signature\":null}");
    decoder.setEncoding(encoding);

    var value = (SignatureData) decoder.decodeStruct(null, new SignatureData.Codec());

    assertEquals("urn:test", value.getAlgorithm());
    assertTrue(value.getSignature().isNull());
  }

  // Consuming the null token must leave the reader on the next field.
  @Test
  void decodingNullByteStringFieldConsumesOnlyTheNullToken() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, "{\"Bytes\":null,\"Tail\":88}");
    decoder.jsonReader.beginObject();

    assertTrue(decoder.decodeByteString("Bytes").isNull());
    assertEquals(88, decoder.decodeInt32("Tail"));

    decoder.jsonReader.endObject();
  }

  // Part 6 §5.4.2.1: a NULL array element is JSON null in both modes.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesNullByteStringArrayElementAsJsonNull(Encoding encoding) throws Exception {
    var values =
        new ByteString[] {
          ByteString.NULL_VALUE, ByteString.of(new byte[0]), ByteString.of(new byte[] {1})
        };

    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeByteStringArray(null, values);

      assertEquals("[null,\"\",\"AQ==\"]", encoder.getOutputString());
    }
  }

  @ParameterizedTest
  @EnumSource(Encoding.class)
  void decodesNullByteStringArrayElementAsNullValue(Encoding encoding) {
    var decoder = new OpcUaJsonDecoder(context, "[null,\"\",\"AQ==\"]");
    decoder.setEncoding(encoding);

    ByteString[] values = decoder.decodeByteStringArray(null);

    assertEquals(3, values.length);
    assertTrue(values[0].isNull());
    assertFalse(values[1].isNull());
    assertEquals(0, values[1].length());
    assertArrayEquals(new byte[] {1}, values[2].bytes());
  }

  // A Variant holding a null ByteString keeps its type and null value through a round trip.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsVariantHoldingNullByteString(Encoding encoding) throws Exception {
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeVariant(null, Variant.ofByteString(ByteString.NULL_VALUE));
      json = encoder.getOutputString();
    }

    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(encoding);
    Variant decoded = decoder.decodeVariant(null);

    assertEquals(OpcUaDataType.ByteString, decoded.getDataType().orElseThrow());
    assertTrue(((ByteString) decoded.value()).isNull());
  }

  private String encodeSignatureData(Encoding encoding, ByteString signature) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(
          null, new SignatureData("urn:test", signature), new SignatureData.Codec());
      return encoder.getOutputString();
    }
  }
}
