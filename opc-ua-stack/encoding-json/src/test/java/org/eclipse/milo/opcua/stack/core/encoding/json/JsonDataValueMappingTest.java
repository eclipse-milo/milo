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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonDataValueMappingTest {

  private static final DataValue EMPTY = DataValue.newValue().build();
  private static final DataValue INT_42 = DataValue.valueOnly(new Variant(42));
  private static final String INT_42_JSON = "{\"UaType\":6,\"Value\":42}";

  private static final DateTime TIME = new DateTime(Instant.parse("2026-01-02T03:04:05Z"));

  // Part 6 §5.4.2.18: a DataValue shares one object with the Variant fields of its value.
  @ParameterizedTest
  @MethodSource("scalarValues")
  void encodesVariantFieldsInsideDataValueObject(
      Encoding encoding, DataValue value, String expected) {
    String json = encode(encoding, e -> e.encodeDataValue(null, value));
    assertEquals(expected, json);
    assertEquals(value, decode(encoding, json).decodeDataValue(null));
  }

  static Stream<Arguments> scalarValues() {
    var withMetadata =
        new DataValue(
            new Variant(42),
            new StatusCode(StatusCodes.Uncertain_InitialValue),
            TIME,
            ushort(1),
            TIME,
            ushort(2));
    String metadataJson =
        "\"SourceTimestamp\":\"2026-01-02T03:04:05Z\",\"SourcePicoseconds\":1,"
            + "\"ServerTimestamp\":\"2026-01-02T03:04:05Z\",\"ServerPicoseconds\":2}";
    var array = DataValue.valueOnly(new Variant(new Integer[] {1, 2}));
    var statusOnly =
        new DataValue(Variant.NULL_VALUE, new StatusCode(StatusCodes.Bad_NoData), null);
    return Stream.of(
        Arguments.of(Encoding.COMPACT, INT_42, INT_42_JSON),
        Arguments.of(Encoding.VERBOSE, INT_42, INT_42_JSON),
        Arguments.of(
            Encoding.COMPACT,
            withMetadata,
            "{\"UaType\":6,\"Value\":42,\"Status\":{\"Code\":1083310080}," + metadataJson),
        Arguments.of(
            Encoding.VERBOSE,
            withMetadata,
            "{\"UaType\":6,\"Value\":42,"
                + "\"Status\":{\"Code\":1083310080,\"Symbol\":\"Uncertain_InitialValue\"},"
                + metadataJson),
        Arguments.of(Encoding.COMPACT, array, "{\"UaType\":6,\"Value\":[1,2]}"),
        Arguments.of(Encoding.COMPACT, statusOnly, "{\"Status\":{\"Code\":2157641728}}"));
  }

  // Dimensions is a Variant field, so a Matrix value places it beside the DataValue fields.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesMatrixDimensionsInsideDataValueObject(Encoding encoding) {
    var value =
        DataValue.valueOnly(
            new Variant(
                new Matrix(new Integer[] {1, 2, 3, 4}, new int[] {2, 2}, OpcUaDataType.Int32)));
    String expected = "{\"UaType\":6,\"Value\":[1,2,3,4],\"Dimensions\":[2,2]}";

    String json = encode(encoding, e -> e.encodeDataValue(null, value));
    assertEquals(expected, json);

    DataValue decoded = decode(encoding, json).decodeDataValue(null);
    assertEquals(expected, encode(encoding, e -> e.encodeDataValue(null, decoded)));
  }

  // Part 6 §5.4.2.17: decoders accept UaType in any position, including after DataValue fields.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaType\":6,\"Value\":42}",
        "{\"Value\":42,\"UaType\":6}",
        "{\"Value\":42,\"Status\":{\"Code\":2157641728},\"UaType\":6}",
        "{\"Status\":{\"Code\":2157641728},\"Value\":42,\"UaType\":6}"
      })
  void decodesVariantFieldsInAnyOrder(String json) {
    DataValue decoded = decode(Encoding.COMPACT, json).decodeDataValue(null);
    assertEquals(new Variant(42), decoded.value());
  }

  // The pre-fix nested form carries no UaType beside Value and must not decode as some other type.
  @ParameterizedTest
  @ValueSource(strings = {"{\"Value\":{\"UaType\":6,\"Value\":42}}", "{\"Value\":42}"})
  void rejectsValueWithoutBuiltinType(String json) {
    var e =
        assertThrows(
            UaSerializationException.class,
            () -> decode(Encoding.COMPACT, json).decodeDataValue(null));
    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Part 6 §5.1.2 and §5.4.5: an all-default DataValue is null and keeps its array position.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesNullDataValueArrayElements(Encoding encoding) {
    var values = new DataValue[] {EMPTY, INT_42, null, EMPTY};
    String json = encode(encoding, e -> e.encodeDataValueArray(null, values));
    assertEquals("[null," + INT_42_JSON + ",null,null]", json);
    assertArrayEquals(
        new DataValue[] {EMPTY, INT_42, EMPTY, EMPTY},
        decode(encoding, json).decodeDataValueArray(null));
  }

  // Variant bodies of DataValue arrays follow the same array element rule.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesNullDataValueElementsInVariantArrays(Encoding encoding) {
    var variant = new Variant(new DataValue[] {EMPTY, INT_42});
    String json = encode(encoding, e -> e.encodeVariant(null, variant));
    assertEquals("{\"UaType\":23,\"Value\":[null," + INT_42_JSON + "]}", json);
    assertEquals(variant, decode(encoding, json).decodeVariant(null));
  }

  // A DataValue inside a Variant keeps its own object with its own UaType.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesDataValueVariantBody(Encoding encoding) {
    var variant = new Variant(INT_42);
    String json = encode(encoding, e -> e.encodeVariant(null, variant));
    assertEquals("{\"UaType\":23,\"Value\":" + INT_42_JSON + "}", json);
    assertEquals(variant, decode(encoding, json).decodeVariant(null));
  }

  // Outside a structure, COMPACT writes JSON null so the value remains a JSON document.
  @ParameterizedTest
  @CsvSource(
      value = {"COMPACT|null", "VERBOSE|{}"},
      delimiter = '|')
  void encodesStandaloneNullDataValue(Encoding encoding, String expected) {
    String json = encode(encoding, e -> e.encodeDataValue(null, EMPTY));
    assertEquals(expected, json);
    assertEquals(EMPTY, decode(encoding, json).decodeDataValue(null));
  }

  // Part 6 §5.4.2.1: COMPACT omits a null structure field; VERBOSE keeps its {} default.
  @ParameterizedTest
  @CsvSource(
      value = {"COMPACT|{\"Tail\":88}", "VERBOSE|{\"Value\":{},\"Tail\":88}"},
      delimiter = '|')
  void encodesNullDataValueStructureField(Encoding encoding, String expected) {
    String json = encodeFields(encoding, e -> e.encodeDataValue("Value", null));
    assertEquals(expected, json);
    decodeFields(encoding, json, d -> assertEquals(EMPTY, d.decodeDataValue("Value")));
  }

  // A flat structure field must be consumed completely so the following field still decodes.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void decodesDataValueStructureField(Encoding encoding) {
    String json = encodeFields(encoding, e -> e.encodeDataValue("Value", INT_42));
    assertEquals("{\"Value\":" + INT_42_JSON + ",\"Tail\":88}", json);
    decodeFields(encoding, json, d -> assertEquals(INT_42, d.decodeDataValue("Value")));
  }

  private static String encode(Encoding encoding, Consumer<OpcUaJsonEncoder> action) {
    try (var encoder = new OpcUaJsonEncoder(new DefaultEncodingContext())) {
      encoder.setEncoding(encoding);
      action.accept(encoder);
      return encoder.getOutputString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static OpcUaJsonDecoder decode(Encoding encoding, String json) {
    var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), json);
    decoder.setEncoding(encoding);
    return decoder;
  }

  private static String encodeFields(Encoding encoding, Consumer<UaEncoder> fields) {
    var codec =
        new GenericDataTypeCodec<Structure>() {
          @Override
          public Class<Structure> getType() {
            return Structure.class;
          }

          @Override
          public Structure decodeType(EncodingContext context, UaDecoder decoder) {
            throw new UnsupportedOperationException();
          }

          @Override
          public void encodeType(EncodingContext context, UaEncoder encoder, Structure value) {
            fields.accept(encoder);
            encoder.encodeInt32("Tail", 88);
          }
        };
    return encode(encoding, e -> e.encodeStruct(null, new Structure() {}, codec));
  }

  private static void decodeFields(Encoding encoding, String json, Consumer<UaDecoder> fields) {
    var codec =
        new GenericDataTypeCodec<Structure>() {
          @Override
          public Class<Structure> getType() {
            return Structure.class;
          }

          @Override
          public Structure decodeType(EncodingContext context, UaDecoder decoder) {
            fields.accept(decoder);
            assertEquals(88, decoder.decodeInt32("Tail"));
            return new Structure() {};
          }

          @Override
          public void encodeType(EncodingContext context, UaEncoder encoder, Structure value) {
            throw new UnsupportedOperationException();
          }
        };
    decode(encoding, json).decodeStruct(null, codec);
  }
}
