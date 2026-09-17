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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonDiagnosticInfoMappingTest {
  private final EncodingContext context = new DefaultEncodingContext();
  private static final DiagnosticInfo INFO = new DiagnosticInfo(-1, -1, -1, -1, "info", null, null);
  private static final String ARRAY_JSON = "[null,{\"AdditionalInfo\":\"info\"},{},null]";

  // Part 6 §5.4.5: null positions must remain JSON null, distinct from an empty object.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void typedArrayPreservesNullElements(Encoding encoding) throws Exception {
    DiagnosticInfo[] values = {null, INFO, DiagnosticInfo.NULL_VALUE, null};
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeDiagnosticInfoArray(null, values);
      assertEquals(ARRAY_JSON, encoder.getOutputString());
    }

    var decoder = new OpcUaJsonDecoder(context, ARRAY_JSON);
    decoder.setEncoding(encoding);
    assertArrayEquals(values, decoder.decodeDiagnosticInfoArray(null));
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Variants use a separate element loop and must follow the same Part 6 §5.4.5 rule.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void variantArrayPreservesNullElements(Encoding encoding) throws Exception {
    var value = new Variant(new DiagnosticInfo[] {null, INFO, DiagnosticInfo.NULL_VALUE, null});
    String expected = "{\"UaType\":25,\"Value\":" + ARRAY_JSON + "}";
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeVariant(null, value);
      assertEquals(expected, encoder.getOutputString());
    }

    var decoder = new OpcUaJsonDecoder(context, expected);
    decoder.setEncoding(encoding);
    assertEquals(value, decoder.decodeVariant(null));
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Both Matrix entry points preserve flattened null positions and dimensions.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void matricesPreserveNullElements(Encoding encoding) throws Exception {
    DiagnosticInfo[] values = {null, INFO, DiagnosticInfo.NULL_VALUE, null};
    var matrix = new Matrix(values, new int[] {2, 2});
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeMatrix(null, matrix);
      String json = encoder.getOutputString();
      assertEquals("{\"Array\":" + ARRAY_JSON + ",\"Dimensions\":[2,2]}", json);
      var decoder = new OpcUaJsonDecoder(context, json);
      decoder.setEncoding(encoding);
      Matrix decoded = decoder.decodeMatrix(null, OpcUaDataType.DiagnosticInfo);
      assertArrayEquals(values, (DiagnosticInfo[]) decoded.getElements());
      assertArrayEquals(new int[] {2, 2}, decoded.getDimensions());
      assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
    }
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeVariant(null, new Variant(matrix));
      String json = encoder.getOutputString();
      assertEquals("{\"UaType\":25,\"Value\":" + ARRAY_JSON + ",\"Dimensions\":[2,2]}", json);
      var decoder = new OpcUaJsonDecoder(context, json);
      decoder.setEncoding(encoding);
      Matrix decoded = assertInstanceOf(Matrix.class, decoder.decodeVariant(null).value());
      assertArrayEquals(values, (DiagnosticInfo[]) decoded.getElements());
      assertArrayEquals(new int[] {2, 2}, decoded.getDimensions());
      assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
    }
  }

  // Independently supplied JSON must decode even before Milo's encoder can produce it.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void decodesNullElementsFromPeer(Encoding encoding) throws Exception {
    var decoder =
        new OpcUaJsonDecoder(
            context,
            "{\"Typed\":"
                + ARRAY_JSON
                + ",\"Variant\":{\"UaType\":25,\"Value\":"
                + ARRAY_JSON
                + "},\"Tail\":42}");
    decoder.setEncoding(encoding);
    decoder.jsonReader.beginObject();
    DiagnosticInfo[] expected = {null, INFO, DiagnosticInfo.NULL_VALUE, null};
    assertArrayEquals(expected, decoder.decodeDiagnosticInfoArray("Typed"));
    assertArrayEquals(
        expected,
        assertInstanceOf(DiagnosticInfo[].class, decoder.decodeVariant("Variant").value()));
    assertEquals(42, decoder.decodeInt32("Tail"));
    decoder.jsonReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Explicit null is consumed, while an omitted field keeps the existing default.
  @Test
  void scalarNullAndMissingFieldLeaveFollowingFieldReadable() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, "{\"Present\":null,\"Tail\":42}");
    decoder.jsonReader.beginObject();
    assertEquals(DiagnosticInfo.NULL_VALUE, decoder.decodeDiagnosticInfo("Missing"));
    assertNull(decoder.decodeDiagnosticInfo("Present"));
    assertEquals(42, decoder.decodeInt32("Tail"));
    decoder.jsonReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Part 6 §5.4.2.13, Table 38 applies to both modes, including nested diagnostics.
  @ParameterizedTest
  @MethodSource("diagnosticFields")
  void omitsOnlyDefaultFields(Encoding encoding, DiagnosticInfo value, String expected)
      throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeDiagnosticInfo(null, value);
      assertEquals(expected, encoder.getOutputString());
    }
  }

  private static Stream<Arguments> diagnosticFields() {
    return Stream.of(Encoding.values())
        .flatMap(
            encoding ->
                Stream.of(
                    Arguments.of(encoding, DiagnosticInfo.NULL_VALUE, "{}"),
                    Arguments.of(encoding, INFO, "{\"AdditionalInfo\":\"info\"}"),
                    Arguments.of(
                        encoding,
                        new DiagnosticInfo(3, -1, -1, -1, null, StatusCode.GOOD, null),
                        "{\"NamespaceUri\":3}"),
                    Arguments.of(
                        encoding,
                        new DiagnosticInfo(0, 0, 0, 0, "", null, null),
                        "{\"SymbolicId\":0,\"NamespaceUri\":0,\"Locale\":0,\"LocalizedText\":0,\"AdditionalInfo\":\"\"}"),
                    Arguments.of(
                        encoding,
                        new DiagnosticInfo(-1, -1, -1, -1, null, null, INFO),
                        "{\"InnerDiagnosticInfo\":{\"AdditionalInfo\":\"info\"}}")));
  }

  // Good severity with a nonzero code is not the default Good (0) and must not be lost.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesNonDefaultInnerStatusCodes(Encoding encoding) throws Exception {
    var good =
        new DiagnosticInfo(-1, -1, -1, -1, null, new StatusCode(StatusCodes.Good_Clamped), null);
    var bad =
        new DiagnosticInfo(
            -1, -1, -1, -1, null, new StatusCode(StatusCodes.Bad_InternalError), null);
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeDiagnosticInfoArray(null, new DiagnosticInfo[] {good, bad});
      json = encoder.getOutputString();
    }
    var array = JsonParser.parseString(json).getAsJsonArray();
    assertEquals(
        StatusCodes.Good_Clamped,
        array.get(0).getAsJsonObject().getAsJsonObject("InnerStatusCode").get("Code").getAsLong());
    assertEquals(
        StatusCodes.Bad_InternalError,
        array.get(1).getAsJsonObject().getAsJsonObject("InnerStatusCode").get("Code").getAsLong());
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(encoding);
    assertArrayEquals(new DiagnosticInfo[] {good, bad}, decoder.decodeDiagnosticInfoArray(null));
  }

  @ParameterizedTest
  @EnumSource(Encoding.class)
  void scalarNullRetainsEmptyObjectEncoding(Encoding encoding) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeDiagnosticInfo(null, null);
      assertEquals("{}", encoder.getOutputString());
    }
  }

  // Accepting JSON null must not accept other non-object DiagnosticInfo values.
  @ParameterizedTest
  @ValueSource(strings = {"[false]", "[1]", "[\"info\"]", "[[]]"})
  void rejectsNonObjectArrayElements(String json) {
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, json).decodeDiagnosticInfoArray(null));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().value());
  }
}
