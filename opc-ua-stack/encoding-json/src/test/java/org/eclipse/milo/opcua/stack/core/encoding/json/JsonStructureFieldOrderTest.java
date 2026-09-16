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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonStructureFieldOrderTest {
  private final DefaultEncodingContext context = new DefaultEncodingContext();

  // Part 6 §5.4.6 requires the same value regardless of JSON member order.
  @ParameterizedTest
  @ValueSource(strings = {"{\"X\":1,\"Value\":2}", "{\"Value\":2,\"X\":1}"})
  void decodesReorderedStructureThroughEveryOverload(String json) throws Exception {
    var decoder = new OpcUaJsonDecoder(context, json);
    assertEquals(new XVType(1.0, 2.0f), decoder.decodeStruct(null, new XVType.Codec()));
    decoder.reset(new StringReader(json));
    assertEquals(new XVType(1.0, 2.0f), decoder.decodeStruct(null, XVType.TYPE_ID));
    decoder.reset(new StringReader(json));
    assertEquals(
        new XVType(1.0, 2.0f),
        decoder.decodeStruct(null, XVType.TYPE_ID.toNodeIdOrThrow(context.getNamespaceTable())));
  }

  // Mixed scalar, array and built-in object fields must work across every permutation.
  @ParameterizedTest
  @MethodSource("argumentPermutations")
  void decodesEveryPermutationOfBuiltinStructure(String json) {
    var expected =
        new Argument(
            "items",
            NodeIds.Int32,
            1,
            new UInteger[] {uint(2)},
            LocalizedText.english("description"));
    assertEquals(
        expected, new OpcUaJsonDecoder(context, json).decodeStruct(null, new Argument.Codec()));
  }

  static Stream<String> argumentPermutations() {
    return permutations(
            List.of(
                "\"Name\":\"items\"",
                "\"DataType\":\"i=6\"",
                "\"ValueRank\":1",
                "\"ArrayDimensions\":[2]",
                "\"Description\":{\"Text\":\"description\",\"Locale\":\"en\"}"))
        .map(fields -> "{" + String.join(",", fields) + "}");
  }

  private static Stream<List<String>> permutations(List<String> values) {
    if (values.isEmpty()) return Stream.of(List.of());
    return values.stream()
        .flatMap(
            first -> {
              var rest = new ArrayList<>(values);
              rest.remove(first);
              return permutations(rest)
                  .map(
                      tail -> {
                        var result = new ArrayList<String>();
                        result.add(first);
                        result.addAll(tail);
                        return result;
                      });
            });
  }

  // Variant decoding temporarily switches readers; subsequent named fields must use the parent.
  @Test
  void restoresParentScopeAfterVariantArray() {
    var codec = codec(d -> probe(d.decodeVariant("Variant"), d.decodeInt32("X")));
    assertEquals(
        probe(new Variant(new Integer[] {1, 2}), 9),
        new OpcUaJsonDecoder(context, "{\"X\":9,\"Variant\":{\"Value\":[1,2],\"UaType\":6}}")
            .decodeStruct(null, codec));
  }

  // Reordering must not change COMPACT's default and null omission rules (Part 6 §5.4.6).
  @ParameterizedTest
  @MethodSource("omissions")
  void preservesOmittedDefaults(String json, XVType expected) {
    assertEquals(
        expected, new OpcUaJsonDecoder(context, json).decodeStruct(null, new XVType.Codec()));
  }

  static Stream<Arguments> omissions() {
    return Stream.of(
        Arguments.of("{}", new XVType(0.0, 0.0f)),
        Arguments.of("{\"Value\":2}", new XVType(0.0, 2.0f)),
        Arguments.of("{\"X\":1}", new XVType(1.0, 0.0f)),
        Arguments.of("null", null));
  }

  // A child's missing Value must never read its parent's Value, including in array elements.
  @Test
  void isolatesNestedStructuresAndArrays() {
    String json =
        """
        {"Value":9,"Children":[{"Value":2,"X":1},{"X":3},null,{}],
         "Child":{"X":4}}
        """;
    var decoder = new OpcUaJsonDecoder(context, json);
    var codec =
        codec(
            d ->
                probe(
                    d.decodeStruct("Missing", new XVType.Codec()),
                    d.decodeStruct("Child", new XVType.Codec()),
                    Arrays.asList(d.decodeStructArray("Children", XVType.TYPE_ID)),
                    d.decodeInt32("Value")));
    assertEquals(
        probe(
            null,
            new XVType(4.0, 0.0f),
            Arrays.asList(
                new XVType(1.0, 2.0f), new XVType(3.0, 0.0f), null, new XVType(0.0, 0.0f)),
            9),
        decoder.decodeStruct(null, codec));
  }

  // Part 6 §5.4.7 uses the mask to distinguish present defaults from absent optional fields.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"EncodingMask\":2,\"X\":1,\"Y\":2}",
        "{\"Y\":2,\"EncodingMask\":2,\"X\":1}",
        "{\"Y\":2,\"X\":1,\"EncodingMask\":2}"
      })
  void readsMaskInAnyPositionAndPreservesPresentDefault(String json) {
    var codec =
        codec(
            d -> {
              long mask = d.decodeUInt32("EncodingMask").longValue();
              return probe(
                  d.decodeInt32("X"),
                  (mask & 1) != 0 ? d.decodeInt32("O1") : null,
                  d.decodeSByte("Y"),
                  (mask & 2) != 0 ? d.decodeInt32("O2") : null);
            });
    assertEquals(
        probe(1, null, (byte) 2, 0), new OpcUaJsonDecoder(context, json).decodeStruct(null, codec));
    assertEquals(
        probe(0, null, (byte) 0, null),
        new OpcUaJsonDecoder(context, "{}").decodeStruct(null, codec));
  }

  // Part 6 §5.4.8 permits the selector after the selected union payload.
  @ParameterizedTest
  @ValueSource(strings = {"{\"SwitchField\":1,\"Value\":7}", "{\"Value\":7,\"SwitchField\":1}"})
  void readsUnionSelectorAfterPayload(String json) {
    var codec =
        codec(
            d -> {
              int selector = d.decodeUInt32("SwitchField").intValue();
              return probe(selector, selector == 1 ? d.decodeInt32("Value") : null);
            });
    assertEquals(probe(1, 7), new OpcUaJsonDecoder(context, json).decodeStruct(null, codec));
    assertEquals(
        probe(1, 0),
        new OpcUaJsonDecoder(context, "{\"SwitchField\":1}").decodeStruct(null, codec));
  }

  // Buffering must preserve signed zero, subnormal values, and integers beyond double precision.
  @Test
  void preservesNumericTokens() {
    String json =
        """
        {"UInt64":"18446744073709551615","Int64":"-9223372036854775808",
         "UInt32":4294967295,"Int32":-2147483648,"Small":4.9e-324,
         "Large":1.7976931348623157e308,"Zero":-0}
        """;
    var codec =
        codec(
            d ->
                probe(
                    d.decodeDouble("Zero"),
                    d.decodeDouble("Large"),
                    d.decodeDouble("Small"),
                    d.decodeInt32("Int32"),
                    d.decodeUInt32("UInt32"),
                    d.decodeInt64("Int64"),
                    d.decodeUInt64("UInt64")));
    assertEquals(
        probe(
            -0.0,
            Double.MAX_VALUE,
            Double.MIN_VALUE,
            Integer.MIN_VALUE,
            uint(4294967295L),
            Long.MIN_VALUE,
            ULong.MAX),
        new OpcUaJsonDecoder(context, json).decodeStruct(null, codec));
  }

  // ExtensionObject buffering must not erase ordering or numbers in the structure body.
  @Test
  void decodesReorderedExtensionObjectBodyAndSibling() {
    String json =
        """
        {"X":8,"Object":{"Value":2,"X":1,"UaTypeId":"i=12080"}}
        """;
    var codec =
        codec(d -> probe(d.decodeExtensionObject("Object").decode(context), d.decodeInt32("X")));
    assertEquals(
        probe(new XVType(1.0, 2.0f), 8),
        new OpcUaJsonDecoder(context, json).decodeStruct(null, codec));
  }

  // Unknown structure fields remain errors; duplicates are rejected before a tree can hide them.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"X\":1,\"Unknown\":2}",
        "{\"Unknown\":2,\"X\":1}",
        "{\"X\":1,\"X\":2}",
        "{\"X\":1,\"\\u0058\":2}",
        "{\"X\":true}",
        "{\"X\":{}}",
        "{\"X\":[1]}",
        "{\"X\":1,}",
        "{\"X\":",
        "[]",
        "1",
        "{\"X\":1"
      })
  void rejectsInvalidStructureWithDecodingErrorAndCanReset(String json) {
    var decoder = new OpcUaJsonDecoder(context, json);
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class, () -> decoder.decodeStruct(null, new XVType.Codec()));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
    decoder.reset(new StringReader("{\"Value\":4,\"X\":3}"));
    assertEquals(new XVType(3.0, 4.0f), decoder.decodeStruct(null, new XVType.Codec()));
  }

  @Test
  void rejectsDuplicatesInExtensionObjectBodyBeforeTheyAreLost() {
    String json = "{\"UaBody\":{\"X\":1,\"X\":2},\"UaTypeId\":\"i=15380\"}";
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
  }

  // Built-in StatusCode objects have historically skipped unknown fields; retain that behavior.
  @Test
  void preservesUnknownFieldPolicyOfBuiltinObjects() {
    var codec = codec(d -> probe(d.decodeStatusCode("Status"), d.decodeInt32("X")));
    assertEquals(
        probe(StatusCode.GOOD, 2),
        new OpcUaJsonDecoder(context, "{\"X\":2,\"Status\":{\"Extra\":1,\"Code\":0}}")
            .decodeStruct(null, codec));
  }

  // A codec failure must restore the containing array's cursor even without reset.
  @Test
  void restoresOuterReaderAfterCodecFailure() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, "[{\"X\":true},{\"Value\":2,\"X\":1}]");
    decoder.jsonReader.beginArray();
    assertThrows(
        UaSerializationException.class, () -> decoder.decodeStruct(null, new XVType.Codec()));
    assertEquals(new XVType(1.0, 2.0f), decoder.decodeStruct(null, new XVType.Codec()));
    decoder.jsonReader.endArray();
  }

  @Test
  void resetClearsSavedUnscopedFieldName() throws Exception {
    var decoder = new OpcUaJsonDecoder(context, "{\"Later\":1}");
    decoder.jsonReader.beginObject();
    assertEquals(0, decoder.decodeInt32("Missing"));
    decoder.reset(new StringReader("{\"Value\":2,\"X\":1}"));
    assertEquals(new XVType(1.0, 2.0f), decoder.decodeStruct(null, new XVType.Codec()));
  }

  // Resource limits must apply before allocating a whole tree, including ignored fields.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"Unused\":\"123456789012345678901234567890\"}",
        "{\"Unused\":[[[0]]]}",
        "{\"Unused\":{\"Child\":{}}}"
      })
  void boundsBufferedSizeAndNesting(String json) {
    var limitedContext =
        new DefaultEncodingContext() {
          @Override
          public EncodingLimits getEncodingLimits() {
            return new EncodingLimits(8196, 1, 40, 2);
          }
        };
    var decoder = new OpcUaJsonDecoder(limitedContext, json);
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class, () -> decoder.decodeStruct(null, new XVType.Codec()));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, error.getStatusCode().getValue());
    decoder.reset(new StringReader("{\"Value\":2,\"X\":1}"));
    assertEquals(new XVType(1.0, 2.0f), decoder.decodeStruct(null, new XVType.Codec()));
  }

  // Limit errors must remain distinguishable from malformed data at every public entry point.
  @Test
  void nodeIdPreservesInputLimitStatus() {
    var limitedContext = limitedContext(4, 128);
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(limitedContext, "\"ns=1;i=123\"").decodeNodeId(null));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, error.getStatusCode().getValue());
  }

  @Test
  void acceptsInputAtCharacterAndDepthLimits() {
    String json = "{\"Value\":2,\"X\":1}";
    assertEquals(
        new XVType(1.0, 2.0f),
        new OpcUaJsonDecoder(limitedContext(json.length(), 1), json)
            .decodeStruct(null, new XVType.Codec()));
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () ->
                new OpcUaJsonDecoder(limitedContext(json.length() - 1, 1), json)
                    .decodeStruct(null, new XVType.Codec()));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, error.getStatusCode().getValue());
  }

  private static DefaultEncodingContext limitedContext(int maxCharacters, int maxDepth) {
    return new DefaultEncodingContext() {
      @Override
      public EncodingLimits getEncodingLimits() {
        return new EncodingLimits(8196, 1, maxCharacters, maxDepth);
      }
    };
  }

  private static Probe probe(Object... values) {
    return new Probe(Arrays.asList(values));
  }

  private static DataTypeCodec codec(Function<UaDecoder, Probe> decode) {
    return new DataTypeCodec() {
      @Override
      public Class<?> getType() {
        return Probe.class;
      }

      @Override
      public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
        return decode.apply(decoder);
      }

      @Override
      public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
        throw new UnsupportedOperationException();
      }
    };
  }

  private record Probe(List<Object> values) implements UaStructuredType {
    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }
}
