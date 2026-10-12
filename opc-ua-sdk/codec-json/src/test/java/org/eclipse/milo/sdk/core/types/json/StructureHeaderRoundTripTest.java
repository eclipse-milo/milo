/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType.UnionValue;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicStructCodec;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicUnionCodec;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.InputSource;

class StructureHeaderRoundTripTest {
  enum Representation {
    DYNAMIC,
    JSON
  }

  enum Format {
    BINARY,
    XML,
    COMPACT,
    VERBOSE
  }

  // Real codecs must preserve member presence across each format, including null and default
  // values.
  @ParameterizedTest
  @MethodSource("values")
  void roundTripsStructureHeaders(
      Representation representation, Format format, boolean union, String members, int header)
      throws Exception {
    var context = new DefaultEncodingContext();
    context.getNamespaceTable().add("urn:headers");
    var tree = mock(DataTypeTree.class);
    var dataType = mock(DataType.class);
    NodeId typeId = new NodeId(1, union ? "Union" : "Optional");
    when(dataType.getNodeId()).thenReturn(typeId);
    when(dataType.getBrowseName()).thenReturn(new QualifiedName(1, "Value"));
    when(dataType.getDataTypeDefinition())
        .thenReturn(
            new StructureDefinition(
                NodeId.NULL_VALUE,
                union ? NodeIds.Union : NodeIds.Structure,
                union ? StructureType.Union : StructureType.StructureWithOptionalFields,
                union
                    ? new StructureField[] {
                      field("Text", NodeIds.String, -1, false),
                      field("Count", NodeIds.Int32, -1, false),
                      field("Numbers", NodeIds.Int32, 1, false)
                    }
                    : new StructureField[] {
                      field("Text", NodeIds.String, -1, true),
                      field("Required", NodeIds.Int32, -1, false),
                      field("Numbers", NodeIds.Int32, 1, true)
                    }));
    DataTypeCodec codec =
        representation == Representation.JSON
            ? new JsonStructCodec(dataType, tree)
            : union
                ? new DynamicUnionCodec(dataType, tree)
                : new DynamicStructCodec(dataType, tree);
    JsonObject expected = JsonParser.parseString(members).getAsJsonObject();
    UaStructuredType original = value(representation, union, dataType, expected);
    UaStructuredType decoded;
    switch (format) {
      case BINARY -> {
        var buffer = Unpooled.buffer();
        try {
          new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeStruct(null, original, codec);
          assertEquals(header, buffer.getIntLE(0), "numeric header must retain its binary layout");
          decoded = new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeStruct(null, codec);
          assertEquals(0, buffer.readableBytes());
        } finally {
          buffer.release();
        }
      }
      case XML -> {
        String xml;
        try (var encoder = new OpcUaXmlEncoder(context)) {
          encoder.encodeStruct("Value", original, codec);
          xml = encoder.getOutputString();
        }
        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        var headers = document.getElementsByTagNameNS("*", union ? "SwitchField" : "EncodingMask");
        assertEquals(1, headers.getLength());
        assertEquals(Integer.toString(header), headers.item(0).getTextContent());
        try (var decoder = new OpcUaXmlDecoder(context, xml)) {
          decoded = decoder.decodeStruct(null, codec);
        }
      }
      default -> {
        Encoding encoding = format == Format.COMPACT ? Encoding.COMPACT : Encoding.VERBOSE;
        String json;
        try (var encoder = new OpcUaJsonEncoder(context)) {
          encoder.setEncoding(encoding);
          encoder.encodeStruct(null, original, codec);
          json = encoder.getOutputString();
        }
        JsonObject output = JsonParser.parseString(json).getAsJsonObject();
        if (format == Format.VERBOSE) {
          assertEquals(expected, output, "VERBOSE must preserve members without numeric headers");
        } else {
          String name = union ? "SwitchField" : "EncodingMask";
          assertEquals(header != 0, output.has(name));
          if (header != 0) assertEquals(header, output.get(name).getAsInt());
          assertFalse(output.has("SwitchValue"));
        }
        var decoder = new OpcUaJsonDecoder(context, json);
        decoder.setEncoding(encoding);
        decoded = decoder.decodeStruct(null, codec);
      }
    }
    assertEquals(expected, members(decoded));
  }

  static Stream<Arguments> values() {
    return Stream.of(Representation.values())
        .flatMap(
            r ->
                Stream.of(Format.values())
                    .flatMap(
                        f ->
                            Stream.of(
                                Arguments.of(r, f, false, "{\"Required\":7}", 0),
                                Arguments.of(
                                    r, f, false, "{\"Text\":\"present\",\"Required\":7}", 1),
                                Arguments.of(r, f, false, "{\"Required\":7,\"Numbers\":null}", 2),
                                Arguments.of(
                                    r,
                                    f,
                                    false,
                                    "{\"Text\":null,\"Required\":7,\"Numbers\":[0]}",
                                    3),
                                Arguments.of(r, f, true, "{}", 0),
                                Arguments.of(r, f, true, "{\"Text\":\"selected\"}", 1),
                                Arguments.of(r, f, true, "{\"Count\":0}", 2),
                                Arguments.of(r, f, true, "{\"Numbers\":null}", 3))));
  }

  // UInt32 selectors above Integer.MAX_VALUE must produce a decoding error, not an array-index
  // error.
  @ParameterizedTest
  @MethodSource("invalidSelectors")
  void rejectsOutOfRangeCompactSelectors(Representation representation, String selector) {
    var type = mock(DataType.class);
    when(type.getDataTypeDefinition())
        .thenReturn(
            new StructureDefinition(
                NodeId.NULL_VALUE,
                NodeIds.Union,
                StructureType.Union,
                new StructureField[] {field("Text", NodeIds.String, -1, false)}));
    var tree = mock(DataTypeTree.class);
    DataTypeCodec codec =
        representation == Representation.JSON
            ? new JsonStructCodec(type, tree)
            : new DynamicUnionCodec(type, tree);
    var decoder =
        new OpcUaJsonDecoder(new DefaultEncodingContext(), "{\"SwitchField\":" + selector + "}");
    UaSerializationException error =
        assertThrows(UaSerializationException.class, () -> decoder.decodeStruct(null, codec));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().value());
  }

  static Stream<Arguments> invalidSelectors() {
    return Stream.of(Representation.values())
        .flatMap(r -> Stream.of("2", "2147483648", "4294967295").map(s -> Arguments.of(r, s)));
  }

  private static StructureField field(String name, NodeId type, int rank, boolean optional) {
    return new StructureField(name, LocalizedText.NULL_VALUE, type, rank, null, uint(0), optional);
  }

  private static UaStructuredType value(
      Representation representation, boolean union, DataType type, JsonObject members) {
    if (representation == Representation.JSON) return new JsonStruct(type, members);
    var values = new LinkedHashMap<String, Object>();
    members.entrySet().forEach(e -> values.put(e.getKey(), javaValue(e.getValue())));
    if (!union) return new DynamicStructType(type, values);
    if (values.isEmpty()) return DynamicUnionType.newInstance(type, null);
    var entry = values.entrySet().iterator().next();
    return DynamicUnionType.newInstance(type, new UnionValue(entry.getKey(), entry.getValue()));
  }

  private static Object javaValue(JsonElement value) {
    if (value.isJsonNull()) return null;
    if (value.isJsonArray()) return new Gson().fromJson(value, Integer[].class);
    return value.getAsJsonPrimitive().isString() ? value.getAsString() : value.getAsInt();
  }

  private static JsonObject members(UaStructuredType value) {
    if (value instanceof JsonStruct json) {
      JsonObject result = json.getJsonObject().deepCopy();
      result.remove("__metadata");
      return result;
    }
    var gson = new GsonBuilder().serializeNulls().create();
    if (value instanceof DynamicStructType struct)
      return gson.toJsonTree(struct.getMembers()).getAsJsonObject();
    var union = (DynamicUnionType) value;
    var result = new JsonObject();
    union.getValue().ifPresent(v -> result.add(v.fieldName(), gson.toJsonTree(v.fieldValue())));
    return result;
  }
}
