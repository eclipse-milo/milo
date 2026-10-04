/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
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
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Executes the Wiki's encoding fragments and rejects malformed serialized input. */
public class WikiEncodingGuidesTest {

  // A round-trip must preserve the unsigned element above Integer.MAX_VALUE and the matrix shape.
  @Test
  void unsignedMatrixKeepsDimensionsAndTimestampThroughJson() throws Exception {
    // wiki:matrix:start
    Matrix grid =
        new Matrix(
            new UInteger[] {uint(1), uint(2), uint(3), uint(4_000_000_000L)},
            new int[] {2, 2},
            OpcUaDataType.UInt32);
    DataValue sample =
        new DataValue(
            new Variant(grid),
            StatusCode.GOOD,
            new DateTime(Instant.parse("2026-01-01T00:00:00Z")));
    // wiki:matrix:end
    var context = new DefaultEncodingContext();
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, sample);
      json = encoder.getOutputString();
    }
    DataValue decoded = new OpcUaJsonDecoder(context, json).decodeDataValue(null);
    Matrix decodedGrid = assertInstanceOf(Matrix.class, decoded.value().value());
    assertArrayEquals(new int[] {2, 2}, decodedGrid.getDimensions());
    assertEquals(4_000_000_000L, ((UInteger[]) decodedGrid.getElements())[3].longValue());
    assertEquals(sample.sourceTime(), decoded.sourceTime());
  }

  // All three encoding modules must preserve a standard structure, rather than only emit bytes.
  @ParameterizedTest
  @MethodSource("encodings")
  void rangeRoundTripsThroughEachEncoding(DataTypeEncoding encoding) {
    EncodingContext context = new DefaultEncodingContext();
    // wiki:extension-roundtrip:start
    Range original = new Range(0.0, 100.0);
    ExtensionObject encoded = ExtensionObject.encode(context, original, encoding);
    Range decoded = (Range) encoded.decode(context, encoding);
    // wiki:extension-roundtrip:end
    assertEquals(original, decoded);
  }

  static Stream<DataTypeEncoding> encodings() {
    return Stream.of(
        OpcUaDefaultBinaryEncoding.getInstance(),
        OpcUaDefaultXmlEncoding.getInstance(),
        OpcUaDefaultJsonEncoding.getInstance());
  }

  // The precise 1.05 JSON shape is a compatibility contract, not merely any valid JSON object.
  @Test
  void dataValueUsesFlatJsonTypeAndValueFields() throws Exception {
    EncodingContext context = new DefaultEncodingContext();
    // wiki:json-value:start
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, DataValue.valueOnly(new Variant(42)));
      json = encoder.getOutputString();
    }
    DataValue decoded = new OpcUaJsonDecoder(context, json).decodeDataValue(null);
    // wiki:json-value:end
    assertEquals("{\"UaType\":6,\"Value\":42}", json);
    assertEquals(42, decoded.value().value());
    assertEquals(StatusCode.GOOD, decoded.statusCode());
  }

  // Buffered objects reject duplicate members; the streamed DataValue wrapper is tested separately.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaType\":24,\"Value\":{\"UaType\":6,\"UaType\":6,\"Value\":42}}",
        "{\"UaType\":6,\"Value\":42,\"Unexpected\":1}",
        "{\"Value\":{\"UaType\":6,\"Value\":42}}"
      })
  void malformedJsonReturnsBadDecodingError(String json) {
    var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), json);
    UaSerializationException failure =
        assertThrows(UaSerializationException.class, () -> decoder.decodeDataValue(null));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
  }

  // The streamed wrapper does not detect repeated type tags, so document its actual limitation.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaType\":6,\"UaType\":6,\"Value\":42}",
        "{\"UaType\":11,\"UaType\":6,\"Value\":42}"
      })
  void duplicateTopLevelDataValueTypeUsesLastOccurrence(String json) {
    var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), json);
    DataValue decoded = decoder.decodeDataValue(null);
    assertEquals(42, assertInstanceOf(Integer.class, decoded.value().value()));
  }

  @Test
  void truncatedBinaryStructureReturnsBadDecodingError() {
    var context = new DefaultEncodingContext();
    ExtensionObject truncated =
        ExtensionObject.of(
            ByteString.of(new byte[] {1}),
            Range.BINARY_ENCODING_ID.toNodeId(context.getNamespaceTable()).orElseThrow());
    UaSerializationException failure =
        assertThrows(UaSerializationException.class, () -> truncated.decode(context));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
  }

  @Test
  void xmlIntegerOverflowReturnsBadDecodingError() throws Exception {
    String xml =
        "<Int32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">2147483648</Int32>";
    try (var decoder = new OpcUaXmlDecoder(new DefaultEncodingContext(), xml)) {
      UaSerializationException failure =
          assertThrows(UaSerializationException.class, () -> decoder.decodeInt32(null));
      assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
    }
  }
}
