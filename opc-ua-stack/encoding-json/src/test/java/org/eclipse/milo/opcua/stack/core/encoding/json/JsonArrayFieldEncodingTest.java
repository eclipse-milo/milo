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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class JsonArrayFieldEncodingTest {

  // Part 6 §5.4.6: null mandatory fields are explicit in VERBOSE and omitted in COMPACT.
  @ParameterizedTest
  @MethodSource("nullFields")
  void encodesNullArrayFieldsAccordingToMode(Encoding encoding, String expected) throws Exception {
    String json =
        encodeFields(
            encoding,
            e -> {
              e.encodeInt32Array("Numbers", null);
              e.encodeEnumArray("Enums", null);
              e.encodeStructArray("Structures", null, NodeIds.Range);
              e.encodeStructArray("ExpandedStructures", null, Range.TYPE_ID);
              e.encodeExtensionObjectArray("Objects", null);
            });
    assertEquals(expected, json);
    decodeFields(
        json,
        d -> {
          assertNull(d.decodeInt32Array("Numbers"));
          assertNull(d.decodeEnumArray("Enums"));
          assertNull(d.decodeStructArray("Structures", NodeIds.Range));
          assertNull(d.decodeStructArray("ExpandedStructures", Range.TYPE_ID));
          assertNull(d.decodeExtensionObjectArray("Objects"));
        });
  }

  static Stream<Arguments> nullFields() {
    return Stream.of(
        Arguments.of(Encoding.COMPACT, "{\"Tail\":88}"),
        Arguments.of(
            Encoding.VERBOSE,
            "{\"Numbers\":null,\"Enums\":null,\"Structures\":null,\"ExpandedStructures\":null,\"Objects\":null,\"Tail\":88}"));
  }

  // Part 6 §5.4.7: invoking a present-null field differs from skipping an absent field.
  @ParameterizedTest
  @MethodSource("optionalFields")
  void distinguishesAbsentNullEmptyAndPopulatedFields(
      Encoding encoding, boolean present, Integer[] values, String expected) throws Exception {
    String json =
        encodeFields(
            encoding,
            e -> {
              if (present) {
                e.encodeInt32Array("Numbers", values);
              }
            });
    assertEquals(expected, json);
    decodeFields(json, d -> assertArrayEquals(values, d.decodeInt32Array("Numbers")));
  }

  static Stream<Arguments> optionalFields() {
    return Stream.of(
        Arguments.of(Encoding.COMPACT, false, null, "{\"Tail\":88}"),
        Arguments.of(Encoding.VERBOSE, false, null, "{\"Tail\":88}"),
        Arguments.of(Encoding.COMPACT, true, null, "{\"Tail\":88}"),
        Arguments.of(Encoding.VERBOSE, true, null, "{\"Numbers\":null,\"Tail\":88}"),
        Arguments.of(Encoding.COMPACT, true, new Integer[0], "{\"Numbers\":[],\"Tail\":88}"),
        Arguments.of(Encoding.VERBOSE, true, new Integer[0], "{\"Numbers\":[],\"Tail\":88}"),
        Arguments.of(
            Encoding.COMPACT, true, new Integer[] {0, 1}, "{\"Numbers\":[0,1],\"Tail\":88}"),
        Arguments.of(
            Encoding.VERBOSE, true, new Integer[] {0, 1}, "{\"Numbers\":[0,1],\"Tail\":88}"));
  }

  // Empty arrays must remain distinguishable from null across all shared array entry points.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesEmptyArrayFields(Encoding encoding) throws Exception {
    String json =
        encodeFields(
            encoding,
            e -> {
              e.encodeInt32Array("Numbers", new Integer[0]);
              e.encodeEnumArray("Enums", new ApplicationType[0]);
              e.encodeStructArray("Structures", new Range[0], NodeIds.Range);
              e.encodeExtensionObjectArray("Objects", new ExtensionObject[0]);
            });
    assertEquals(
        "{\"Numbers\":[],\"Enums\":[],\"Structures\":[],\"Objects\":[],\"Tail\":88}", json);
    decodeFields(
        json,
        d -> {
          assertArrayEquals(new Integer[0], d.decodeInt32Array("Numbers"));
          assertArrayEquals(new Integer[0], d.decodeEnumArray("Enums"));
          assertArrayEquals(new Range[0], d.decodeStructArray("Structures", NodeIds.Range));
          assertArrayEquals(new ExtensionObject[0], d.decodeExtensionObjectArray("Objects"));
        });
  }

  // A null value must occupy its position rather than shift later JSON array elements.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesPositionalNullArrays(Encoding encoding) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(new DefaultEncodingContext())) {
      encoder.setEncoding(encoding);
      encoder.jsonWriter.beginArray();
      encoder.encodeInt32Array(null, null);
      encoder.encodeEnumArray(null, null);
      encoder.encodeStructArray(null, null, NodeIds.Range);
      encoder.encodeExtensionObjectArray(null, null);
      encoder.encodeInt32(null, 88);
      encoder.jsonWriter.endArray();
      assertEquals("[null,null,null,null,88]", encoder.getOutputString());

      var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), encoder.getOutputString());
      decoder.jsonReader.beginArray();
      assertNull(decoder.decodeInt32Array(null));
      assertNull(decoder.decodeEnumArray(null));
      assertNull(decoder.decodeStructArray(null, NodeIds.Range));
      assertNull(decoder.decodeExtensionObjectArray(null));
      assertEquals(88, decoder.decodeInt32(null));
      decoder.jsonReader.endArray();
    }
  }

  // COMPACT populated arrays must retain their values while null handling changes.
  @Test
  void roundTripsPopulatedArrayFields() throws Exception {
    var numbers = new Integer[] {0, 1};
    var enums = new ApplicationType[] {ApplicationType.Server, ApplicationType.Client};
    var ranges = new Range[] {new Range(1.0, 2.0)};
    var objects =
        new ExtensionObject[] {ExtensionObject.of("{\"Low\":1.0,\"High\":2.0}", NodeIds.Range)};
    String json =
        encodeFields(
            Encoding.COMPACT,
            e -> {
              e.encodeInt32Array("Numbers", numbers);
              e.encodeEnumArray("Enums", enums);
              e.encodeStructArray("Structures", ranges, NodeIds.Range);
              e.encodeStructArray("ExpandedStructures", ranges, Range.TYPE_ID);
              e.encodeExtensionObjectArray("Objects", objects);
            });
    decodeFields(
        json,
        d -> {
          assertArrayEquals(numbers, d.decodeInt32Array("Numbers"));
          assertArrayEquals(new Integer[] {0, 1}, d.decodeEnumArray("Enums"));
          assertArrayEquals(ranges, d.decodeStructArray("Structures", NodeIds.Range));
          assertArrayEquals(ranges, d.decodeStructArray("ExpandedStructures", Range.TYPE_ID));
          assertArrayEquals(objects, d.decodeExtensionObjectArray("Objects"));
        });
  }

  // Part 6 §5.4.5: null elements use JSON null even when the scalar VERBOSE default is {}.
  @ParameterizedTest
  @MethodSource("extensionObjectElements")
  void preservesNullExtensionObjectArrayElements(
      Encoding encoding, ExtensionObject[] values, String expected) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(new DefaultEncodingContext())) {
      encoder.setEncoding(encoding);
      encoder.encodeExtensionObjectArray(null, values);
      assertEquals(expected, encoder.getOutputString());

      var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), encoder.getOutputString());
      decoder.setEncoding(encoding);
      assertArrayEquals(values, decoder.decodeExtensionObjectArray(null));
    }
  }

  // Generic structure codecs must preserve null-first subtype arrays and the following field.
  @ParameterizedTest
  @MethodSource("extensionObjectElements")
  void preservesNullExtensionObjectElementsInStructureFields(
      Encoding encoding, ExtensionObject[] values, String expected) throws Exception {
    String json = encodeFields(encoding, e -> e.encodeExtensionObjectArray("Objects", values));
    assertEquals("{\"Objects\":" + expected + ",\"Tail\":88}", json);
    decodeFields(
        encoding, json, d -> assertArrayEquals(values, d.decodeExtensionObjectArray("Objects")));
  }

  static Stream<Arguments> extensionObjectElements() {
    ExtensionObject first = ExtensionObject.of("{\"Low\":1.0,\"High\":2.0}", NodeIds.Range);
    ExtensionObject second = ExtensionObject.of("{\"Low\":3.0,\"High\":4.0}", NodeIds.Range);
    String firstJson = "{\"UaTypeId\":\"i=884\",\"Low\":1.0,\"High\":2.0}";
    String secondJson = "{\"UaTypeId\":\"i=884\",\"Low\":3.0,\"High\":4.0}";
    return Stream.of(Encoding.values())
        .flatMap(
            encoding ->
                Stream.of(
                    Arguments.of(encoding, new ExtensionObject[] {null}, "[null]"),
                    Arguments.of(encoding, new ExtensionObject[] {null, null}, "[null,null]"),
                    Arguments.of(
                        encoding,
                        new ExtensionObject[] {null, first, null, second, null},
                        "[null," + firstJson + ",null," + secondJson + ",null]"),
                    Arguments.of(
                        encoding,
                        new ExtensionObject[] {first, second},
                        "[" + firstJson + "," + secondJson + "]")));
  }

  // Part 6 §5.4.2.16: the VERBOSE scalar default remains {}, independently of array rules.
  @ParameterizedTest
  @CsvSource(
      value = {"COMPACT|null", "VERBOSE|{}"},
      delimiter = '|')
  void preservesStandaloneNullExtensionObjectDefaults(Encoding encoding, String expected)
      throws Exception {
    try (var encoder = new OpcUaJsonEncoder(new DefaultEncodingContext())) {
      encoder.setEncoding(encoding);
      encoder.encodeExtensionObject(null, null);
      assertEquals(expected, encoder.getOutputString());
      var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), encoder.getOutputString());
      decoder.setEncoding(encoding);
      assertNull(decoder.decodeExtensionObject(null));
    }
  }

  // Scalar structure fields keep COMPACT omission and the VERBOSE empty-object default.
  @ParameterizedTest
  @CsvSource(
      value = {"COMPACT|{\"Tail\":88}", "VERBOSE|{\"Object\":{},\"Tail\":88}"},
      delimiter = '|')
  void preservesNullExtensionObjectStructureFieldDefaults(Encoding encoding, String expected)
      throws Exception {
    String json = encodeFields(encoding, e -> e.encodeExtensionObject("Object", null));
    assertEquals(expected, json);
    decodeFields(encoding, json, d -> assertNull(d.decodeExtensionObject("Object")));
  }

  private void decodeFields(String json, Consumer<UaDecoder> fields) {
    decodeFields(Encoding.COMPACT, json, fields);
  }

  private void decodeFields(Encoding encoding, String json, Consumer<UaDecoder> fields) {
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
    var decoder = new OpcUaJsonDecoder(new DefaultEncodingContext(), json);
    decoder.setEncoding(encoding);
    decoder.decodeStruct(null, codec);
  }

  private String encodeFields(Encoding encoding, Consumer<UaEncoder> fields) throws Exception {
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
    try (var encoder = new OpcUaJsonEncoder(new DefaultEncodingContext())) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, new Structure() {}, codec);
      return encoder.getOutputString();
    }
  }
}
