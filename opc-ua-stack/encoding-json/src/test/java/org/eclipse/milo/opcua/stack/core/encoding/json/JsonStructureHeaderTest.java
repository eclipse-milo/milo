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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecord;
import org.eclipse.milo.opcua.stack.core.types.structured.NameValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonStructureHeaderTest {
  private final EncodingContext context = new DefaultEncodingContext();

  // Part 6 §5.4.7: presence, including present-null and present-default, survives both mappings.
  @ParameterizedTest
  @MethodSource("optionalValues")
  void roundTripsOptionalFields(
      Encoding encoding, long mask, String text, Integer[] numbers, String expected)
      throws Exception {
    var codec = optionalCodec(mask, text, numbers);
    assertEquals(expected, roundTrip(encoding, codec));
  }

  static Stream<Arguments> optionalValues() {
    return Stream.of(
        Arguments.of(Encoding.COMPACT, 0L, null, null, "{\"Tail\":88}"),
        Arguments.of(Encoding.VERBOSE, 0L, null, null, "{\"Required\":0,\"Tail\":88}"),
        Arguments.of(Encoding.COMPACT, 1L, null, null, "{\"EncodingMask\":1,\"Tail\":88}"),
        Arguments.of(
            Encoding.VERBOSE, 1L, null, null, "{\"Required\":0,\"Text\":null,\"Tail\":88}"),
        Arguments.of(Encoding.COMPACT, 2L, null, null, "{\"EncodingMask\":2,\"Tail\":88}"),
        Arguments.of(
            Encoding.VERBOSE, 2L, null, null, "{\"Required\":0,\"Numbers\":null,\"Tail\":88}"),
        Arguments.of(
            Encoding.COMPACT,
            3L,
            "",
            new Integer[] {0},
            "{\"EncodingMask\":3,\"Text\":\"\",\"Numbers\":[0],\"Tail\":88}"),
        Arguments.of(
            Encoding.VERBOSE,
            3L,
            "",
            new Integer[] {0},
            "{\"Required\":0,\"Text\":\"\",\"Numbers\":[0],\"Tail\":88}"));
  }

  private GenericDataTypeCodec<Structure> optionalCodec(long mask, String text, Integer[] numbers) {
    return codec(
        e -> {
          e.encodeEncodingMask(uint(mask));
          e.encodeInt32("Required", 0);
          if ((mask & 1) != 0) e.encodeString("Text", text);
          if ((mask & 2) != 0) e.encodeInt32Array("Numbers", numbers);
          e.encodeInt32("Tail", 88);
        },
        d -> {
          assertEquals(mask, d.decodeEncodingMask("Text", "Numbers").longValue());
          assertEquals(0, d.decodeInt32("Required"));
          if ((mask & 1) != 0) assertEquals(text, d.decodeString("Text"));
          if ((mask & 2) != 0) assertArrayEquals(numbers, d.decodeInt32Array("Numbers"));
          assertEquals(88, d.decodeInt32("Tail"));
        });
  }

  // Part 6 §5.4.8: empty unions and selected null/default members are distinct.
  @ParameterizedTest
  @MethodSource("unionValues")
  void roundTripsUnions(Encoding encoding, int selector, String expected) throws Exception {
    assertEquals(expected, roundTrip(encoding, unionCodec(selector)));
  }

  static Stream<Arguments> unionValues() {
    return Stream.of(
        Arguments.of(Encoding.COMPACT, 0, "{}"),
        Arguments.of(Encoding.VERBOSE, 0, "{}"),
        Arguments.of(Encoding.COMPACT, 1, "{\"SwitchField\":1}"),
        Arguments.of(Encoding.VERBOSE, 1, "{\"Text\":null}"),
        Arguments.of(Encoding.COMPACT, 2, "{\"SwitchField\":2}"),
        Arguments.of(Encoding.VERBOSE, 2, "{\"Count\":0}"));
  }

  private GenericDataTypeCodec<Structure> unionCodec(int selector) {
    return codec(
        e -> {
          e.encodeSwitchField(uint(selector));
          if (selector == 1) e.encodeString("Text", null);
          if (selector == 2) e.encodeInt32("Count", 0);
        },
        d -> {
          assertEquals(selector, d.decodeSwitchField("Text", "Count").intValue());
          if (selector == 1) assertNull(d.decodeString("Text"));
          if (selector == 2) assertEquals(0, d.decodeInt32("Count"));
        });
  }

  // Headers must use the current object's fields and restore the enclosing lookup scope.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsNestedOptionalStructureAndUnion(Encoding encoding) throws Exception {
    var optional = optionalCodec(2, null, null);
    var union = unionCodec(1);
    var outer =
        codec(
            e -> {
              e.encodeEncodingMask(uint(1));
              e.encodeStruct("Optional", new Structure() {}, optional);
              e.encodeStruct("Union", new Structure() {}, union);
              e.encodeInt32("Tail", 17);
            },
            d -> {
              assertEquals(uint(1), d.decodeEncodingMask("Optional"));
              d.decodeStruct("Optional", optional);
              d.decodeStruct("Union", union);
              assertEquals(17, d.decodeInt32("Tail"));
            });
    roundTrip(encoding, outer);
  }

  // Part 6 §5.4.7 allows all 32 optional bits; bit 31 must not sign-extend or wrap.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsHighestOptionalBit(Encoding encoding) throws Exception {
    String[] names = IntStream.range(0, 32).mapToObj(i -> "F" + i).toArray(String[]::new);
    var codec =
        codec(
            e -> {
              e.encodeEncodingMask(uint(0x80000000L));
              e.encodeInt32("F31", 0);
            },
            d -> {
              assertEquals(uint(0x80000000L), d.decodeEncodingMask(names));
              assertEquals(0, d.decodeInt32("F31"));
            });
    roundTrip(encoding, codec);
  }

  // Primitive fields are application data, even if their names resemble protocol headers.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesOrdinaryFieldsNamedLikeHeaders(Encoding encoding) throws Exception {
    var codec =
        codec(
            e -> {
              e.encodeUInt32("EncodingMask", uint(3));
              e.encodeUInt32("SwitchField", uint(4));
            },
            d -> {
              assertEquals(uint(3), d.decodeUInt32("EncodingMask"));
              assertEquals(uint(4), d.decodeUInt32("SwitchField"));
            });
    assertEquals("{\"EncodingMask\":3,\"SwitchField\":4}", roundTrip(encoding, codec));
  }

  // Multiple selected members, unknown members, and duplicate names cannot describe a union.
  @ParameterizedTest
  @ValueSource(
      strings = {"{\"Text\":null,\"Count\":0}", "{\"Unknown\":0}", "{\"Text\":null,\"Text\":null}"})
  void rejectsMalformedVerboseUnion(String json) {
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(Encoding.VERBOSE);
    var codec =
        codec(
            e -> {},
            d -> {
              int selector = d.decodeSwitchField("Text", "Count").intValue();
              if (selector == 1) d.decodeString("Text");
              if (selector == 2) d.decodeInt32("Count");
            });
    UaSerializationException error =
        assertThrows(UaSerializationException.class, () -> decoder.decodeStruct(null, codec));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().value());
  }

  // A decoder's explicit mode must survive reset without retaining previous members.
  @Test
  void resetRetainsVerboseModeAndClearsPresence() {
    var decoder = new OpcUaJsonDecoder(context, "{\"Text\":null}");
    decoder.setEncoding(Encoding.VERBOSE);
    decoder.decodeStruct(null, unionCodec(1));
    decoder.reset(new StringReader("{}"));
    decoder.decodeStruct(null, unionCodec(0));
  }

  // Buffering headers cannot introduce an ordering requirement for the fields they describe.
  @Test
  void decodesReorderedVerboseOptionalFields() {
    var decoder =
        new OpcUaJsonDecoder(context, "{\"Tail\":88,\"Numbers\":[0],\"Text\":\"\",\"Required\":0}");
    decoder.setEncoding(Encoding.VERBOSE);
    decoder.decodeStruct(null, optionalCodec(3, "", new Integer[] {0}));
  }

  // Part 6 §5.4.4.2: enum strings retain the numeric value, while enum Variants remain Int32.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsEnumMemberArraysMatricesAndVariants(Encoding encoding) throws Exception {
    var codec =
        codec(
            e -> {
              e.encodeSwitchField(uint(1));
              e.encodeEnum("State", ApplicationType.Server);
            },
            d -> {
              assertEquals(uint(1), d.decodeSwitchField("State"));
              assertEquals(0, d.decodeEnum("State"));
            });
    assertEquals(
        encoding == Encoding.COMPACT ? "{\"SwitchField\":1}" : "{\"State\":\"Server_0\"}",
        roundTrip(encoding, codec));
    var arrays =
        codec(
            e -> {
              e.encodeEnumArray(
                  "States", new ApplicationType[] {ApplicationType.Server, ApplicationType.Client});
              e.encodeEnumMatrix(
                  "Matrix",
                  new Matrix(
                      new ApplicationType[][] {{ApplicationType.Server, ApplicationType.Client}}));
              e.encodeVariant("Variant", Variant.ofInt32(1));
            },
            d -> {
              assertArrayEquals(new Integer[] {0, 1}, d.decodeEnumArray("States"));
              Matrix matrix = d.decodeEnumMatrix("Matrix");
              assertArrayEquals(new int[] {0, 1}, (int[]) matrix.getElements());
              assertArrayEquals(new int[] {1, 2}, matrix.getDimensions());
              assertEquals(Variant.ofInt32(1), d.decodeVariant("Variant"));
            });
    roundTrip(encoding, arrays);
  }

  // Names can contain underscores, and unknown enum literals retain just their numeric value.
  @ParameterizedTest
  @ValueSource(strings = {"\"Some_Name_-12\"", "\"-12\""})
  void decodesVerboseEnumNumericSuffix(String json) {
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(Encoding.VERBOSE);
    assertEquals(-12, decoder.decodeEnum(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"\"Name_\"", "\"Name_2147483648\"", "null", "{}"})
  void rejectsInvalidVerboseEnum(String json) {
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(Encoding.VERBOSE);
    UaSerializationException error =
        assertThrows(UaSerializationException.class, () -> decoder.decodeEnum(null));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().value());
  }

  // The built-in optional codec must migrate with the API, preserving its nonconsecutive mask bits.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsLogRecordOptionalFields(Encoding encoding) throws Exception {
    var original =
        new LogRecord(
            DateTime.NULL_VALUE,
            UShort.MIN,
            null,
            null,
            "",
            LocalizedText.english("event"),
            null,
            new NameValuePair[0]);
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, original, new LogRecord.Codec());
      String json = encoder.getOutputString();
      assertEquals(encoding == Encoding.COMPACT, json.contains("EncodingMask"));
      var decoder = new OpcUaJsonDecoder(context, json);
      decoder.setEncoding(encoding);
      var decoded = (LogRecord) decoder.decodeStruct(null, new LogRecord.Codec());
      assertEquals(original, decoded);
    }
  }

  // Every array element needs its own header lookup scope, including nested arrays in a structure.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsUnionArray(Encoding encoding) throws Exception {
    var union = unionCodec(1);
    var typeId = new NodeId(1, "HeaderUnion");
    context.getDataTypeManager().registerType(typeId, union, null, null, null);
    var codec =
        codec(
            e -> {
              e.encodeStructArray(
                  "Items", new Structure[] {new Structure() {}, new Structure() {}}, typeId);
              e.encodeInt32("Tail", 88);
            },
            d -> {
              assertEquals(2, d.decodeStructArray("Items", typeId).length);
              assertEquals(88, d.decodeInt32("Tail"));
            });
    roundTrip(encoding, codec);
  }

  // Part 6 §5.4.2.16: a selected default ExtensionObject is {} in VERBOSE, not an absent member.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsSelectedNullExtensionObject(Encoding encoding) throws Exception {
    var codec =
        codec(
            e -> {
              e.encodeSwitchField(uint(1));
              e.encodeExtensionObject("Value", null);
            },
            d -> {
              assertEquals(uint(1), d.decodeSwitchField("Value"));
              assertNull(d.decodeExtensionObject("Value"));
            });
    assertEquals(
        encoding == Encoding.COMPACT ? "{\"SwitchField\":1}" : "{\"Value\":{}}",
        roundTrip(encoding, codec));
  }

  private String roundTrip(Encoding encoding, GenericDataTypeCodec<Structure> codec)
      throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, new Structure() {}, codec);
      String json = encoder.getOutputString();
      var decoder = new OpcUaJsonDecoder(context, json);
      decoder.setEncoding(encoding);
      decoder.decodeStruct(null, codec);
      return json;
    }
  }

  private GenericDataTypeCodec<Structure> codec(
      Consumer<UaEncoder> encode, Consumer<UaDecoder> decode) {
    return new GenericDataTypeCodec<>() {
      @Override
      public Class<Structure> getType() {
        return Structure.class;
      }

      @Override
      public Structure decodeType(EncodingContext c, UaDecoder d) {
        decode.accept(d);
        return new Structure() {};
      }

      @Override
      public void encodeType(EncodingContext c, UaEncoder e, Structure value) {
        encode.accept(e);
      }
    };
  }
}
