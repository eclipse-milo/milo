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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JsonStructuredVariantSerializationTest {
  private final EncodingContext context = new DefaultEncodingContext();

  // Structure arrays preserve nullable positions rather than passing null into a structure codec.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureArrays")
  void structureArrayVariantRoundTrips(String name, Encoding encoding, UaStructuredType[] values)
      throws Exception {
    String json = encode(new Variant(values), encoding);
    assertJsonElements(values, json);

    var decoder = new OpcUaJsonDecoder(context, json);
    ExtensionObject[] decoded =
        assertInstanceOf(ExtensionObject[].class, decoder.decodeVariant(null).value());
    assertStructureElements(values, decoded);
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Positive Matrix dimensions and structure element order must survive Variant conversion.
  @ParameterizedTest(name = "{0}")
  @MethodSource("structureMatrices")
  void structureMatrixVariantRoundTrips(String name, Encoding encoding, Matrix matrix)
      throws Exception {
    UaStructuredType[] values = (UaStructuredType[]) matrix.getElements();
    String json = encode(new Variant(matrix), encoding);
    assertJsonElements(values, json);

    var decoder = new OpcUaJsonDecoder(context, json);
    Matrix decoded = assertInstanceOf(Matrix.class, decoder.decodeVariant(null).value());
    assertEquals(OpcUaDataType.ExtensionObject, decoded.getDataType().orElseThrow());
    assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
    assertStructureElements(
        values, assertInstanceOf(ExtensionObject[].class, decoded.getElements()));
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  private String encode(Variant value, Encoding encoding) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeVariant(null, value);
      return encoder.getOutputString();
    }
  }

  private static void assertJsonElements(UaStructuredType[] expected, String json) {
    JsonObject object = JsonParser.parseString(json).getAsJsonObject();
    assertEquals(22, object.get("UaType").getAsInt());
    JsonArray elements = object.getAsJsonArray("Value");
    assertEquals(expected.length, elements.size());
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i] == null, elements.get(i).isJsonNull(), "JSON null at index " + i);
    }
  }

  private void assertStructureElements(UaStructuredType[] expected, ExtensionObject[] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      if (expected[i] == null) {
        assertNull(actual[i], "null structure at index " + i);
      } else {
        assertEquals(expected[i], actual[i].decode(context), "structure at index " + i);
      }
    }
  }

  static Stream<Arguments> structureValues() {
    XVType first = new XVType(1.0, 2.0f);
    XVType second = new XVType(3.0, 4.0f);
    return Stream.of(
        Arguments.of("concrete empty", new XVType[0]),
        Arguments.of("concrete all null", new XVType[4]),
        Arguments.of("concrete trailing null", new XVType[] {first, null}),
        Arguments.of("concrete mixed null", new XVType[] {null, first, second, null}),
        Arguments.of("concrete nonnull", new XVType[] {first, second, first, second}),
        Arguments.of("interface empty", new UaStructuredType[0]),
        Arguments.of("interface all null", new UaStructuredType[4]),
        Arguments.of("interface mixed null", new UaStructuredType[] {null, first, second, null}),
        Arguments.of("interface nonnull", new UaStructuredType[] {first, second, first, second}));
  }

  static Stream<Arguments> structureMatrices() {
    return structureValues()
        .filter(arguments -> ((UaStructuredType[]) arguments.get()[1]).length > 0)
        .flatMap(
            arguments -> {
              Object[] values = arguments.get();
              String name = (String) values[0];
              UaStructuredType[] elements = (UaStructuredType[]) values[1];
              int rows = elements.length / 2;
              return Stream.of(
                      Arguments.of(name + " 2D", structureMatrix(elements, new int[] {rows, 2})),
                      Arguments.of(name + " 3D", structureMatrix(elements, new int[] {1, rows, 2})))
                  .flatMap(JsonStructuredVariantSerializationTest::withEncodings);
            });
  }

  static Stream<Arguments> structureArrays() {
    return structureValues().flatMap(JsonStructuredVariantSerializationTest::withEncodings);
  }

  private static Stream<Arguments> withEncodings(Arguments arguments) {
    Object[] values = arguments.get();
    return Stream.of(Encoding.values())
        .map(encoding -> Arguments.of(values[0] + " " + encoding, encoding, values[1]));
  }

  private static Matrix structureMatrix(UaStructuredType[] elements, int[] dimensions) {
    return new Matrix(elements, dimensions, OpcUaDataType.ExtensionObject, XVType.TYPE_ID);
  }
}
