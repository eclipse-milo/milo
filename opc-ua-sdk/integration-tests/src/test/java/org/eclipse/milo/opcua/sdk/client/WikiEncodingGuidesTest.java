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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.time.Instant;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
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
    assertEquals(sample.sourceTime(), decoded.serverTime());
    assertTrue(json.contains("\"Dimensions\":[2,2]"));
    assertTrue(json.contains("\"SourceTimestamp\":\"2026-01-01T00:00:00Z\""));
    assertTrue(json.contains("\"ServerTimestamp\":\"2026-01-01T00:00:00Z\""));
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
        "{\"UaType\":21,\"Value\":{\"Text\":\"before\",\"Text\":\"after\"}}",
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
        "{\"UaType\":6,\"Value\":1,\"Value\":42}",
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

  @Test
  void localizedTextWithoutDuplicateHasUsableValue() {
    DataValue value =
        new OpcUaJsonDecoder(
                new DefaultEncodingContext(), "{\"UaType\":21,\"Value\":{\"Text\":\"after\"}}")
            .decodeDataValue(null);
    assertEquals("after", assertInstanceOf(LocalizedText.class, value.value().value()).getText());
  }

  // A second decode cannot switch a previously decoded ExtensionObject to a different model.
  @Test
  void extensionObjectCachesFirstDecodeRegardlessOfContext() {
    var context = new DefaultEncodingContext();
    var empty =
        new DefaultEncodingContext() {
          private final DataTypeManager manager = new DefaultDataTypeManager();

          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    var value = ExtensionObject.encode(context, new Range(0.0, 100.0));
    var decoded = value.decode(context);
    assertSame(decoded, value.decode(empty));
    var fresh = ExtensionObject.encode(context, new Range(0.0, 100.0));
    UaSerializationException failure =
        assertThrows(UaSerializationException.class, () -> fresh.decode(empty));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
  }

  @Test
  void binaryStructuresStayOpaqueUntilExplicitlyEncodedAsJson() throws Exception {
    EncodingContext context = new DefaultEncodingContext();
    Range range = new Range(0.0, 100.0);
    String direct = json(context, DataValue.valueOnly(new Variant(range)));
    String binary =
        json(context, DataValue.valueOnly(new Variant(ExtensionObject.encode(context, range))));
    assertEquals(direct, binary);
    assertTrue(binary.contains("\"UaEncoding\":1"));
    assertTrue(binary.contains("\"UaBody\":\""));
    assertFalse(binary.contains("\"High\""));
    // wiki:json-inline:start
    ExtensionObject jsonStructure =
        ExtensionObject.encode(context, range, OpcUaDefaultJsonEncoding.getInstance());
    String inline;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, DataValue.valueOnly(new Variant(jsonStructure)));
      inline = encoder.getOutputString();
    }
    // wiki:json-inline:end
    assertTrue(inline.contains("\"UaTypeId\":\"i=884\""));
    assertTrue(inline.contains("\"High\":100"));
    assertFalse(inline.contains("UaBody"));
    ExtensionObject restored =
        assertInstanceOf(
            ExtensionObject.class,
            new OpcUaJsonDecoder(context, inline).decodeDataValue(null).value().value());
    assertEquals(range, restored.decode(context));
  }

  @Test
  void legacyVariantAliasesAreAcceptedByBothOuterDecoders() {
    String input = "{\"Type\":6,\"Body\":42}";
    assertEquals(
        42, new OpcUaJsonDecoder(new DefaultEncodingContext(), input).decodeVariant(null).value());
    assertEquals(
        42,
        new OpcUaJsonDecoder(new DefaultEncodingContext(), input)
            .decodeDataValue(null)
            .value()
            .value());
  }

  @Test
  void duplicateStatusAndDimensionsUseTheirLastValues() {
    String input =
        "{\"UaType\":6,\"Value\":[1,2,3,4],\"Dimensions\":[1,4],\"Dimensions\":[2,2],\"Status\":{\"Code\":2147483648},\"Status\":{\"Code\":0}}";
    DataValue value =
        new OpcUaJsonDecoder(new DefaultEncodingContext(), input).decodeDataValue(null);
    assertEquals(StatusCode.GOOD, value.statusCode());
    assertArrayEquals(
        new int[] {2, 2}, assertInstanceOf(Matrix.class, value.value().value()).getDimensions());
  }

  @Test
  void unknownNamespaceUrisRemainRawNamespaceZeroStrings() {
    var context = new DefaultEncodingContext();
    String id = "nsu=urn:unknown;i=5";
    String name = "nsu=urn:unknown;Name";
    assertEquals(
        new NodeId(0, id), new OpcUaJsonDecoder(context, "\"" + id + "\"").decodeNodeId(null));
    assertEquals(
        new QualifiedName(0, name),
        new OpcUaJsonDecoder(context, "\"" + name + "\"").decodeQualifiedName(null));
  }

  @Test
  void missingNamespaceIndexChangesNodeIdButRejectsOtherIdentities() throws Exception {
    var context = new DefaultEncodingContext();
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeNodeId(null, new NodeId(5, 1));
      String json = encoder.getOutputString();
      assertEquals("\"s=ns=5;i=1\"", json);
      assertEquals(
          new NodeId(0, "ns=5;i=1"), new OpcUaJsonDecoder(context, json).decodeNodeId(null));
    }
    UaSerializationException expandedFailure =
        assertThrows(
            UaSerializationException.class,
            () -> {
              try (var encoder = new OpcUaJsonEncoder(context)) {
                encoder.encodeExpandedNodeId(null, new NodeId(5, 1).expanded());
              }
            });
    assertEquals(StatusCodes.Bad_EncodingError, expandedFailure.getStatusCode().getValue());
    UaSerializationException nameFailure =
        assertThrows(
            UaSerializationException.class,
            () -> {
              try (var encoder = new OpcUaJsonEncoder(context)) {
                encoder.encodeQualifiedName(null, new QualifiedName(5, "Name"));
              }
            });
    assertEquals(StatusCodes.Bad_EncodingError, nameFailure.getStatusCode().getValue());
  }

  @Test
  void numericPrefixInNamespaceZeroQualifiedNameChangesItsIdentity() throws Exception {
    var context = new DefaultEncodingContext();
    String encoded;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeQualifiedName(null, new QualifiedName(0, "1:Name"));
      encoded = encoder.getOutputString();
    }
    assertEquals(
        new QualifiedName(1, "Name"),
        new OpcUaJsonDecoder(context, encoded).decodeQualifiedName(null));
  }

  @Test
  void jsonCharacterAndBufferedDepthLimitsReportEncodingLimitsExceeded() {
    var tiny = withLimits(new EncodingLimits(65535, 1, 10, 2));
    UaSerializationException size =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(tiny, "{\"UaType\":6,\"Value\":42}").decodeDataValue(null));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, size.getStatusCode().getValue());
    var shallow = withLimits(new EncodingLimits(65535, 100, 1 << 20, 1));
    UaSerializationException depth =
        assertThrows(
            UaSerializationException.class,
            () ->
                new OpcUaJsonDecoder(shallow, "{\"UaType\":6,\"Value\":[[1]]}")
                    .decodeDataValue(null));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, depth.getStatusCode().getValue());
    assertEquals(
        42,
        new OpcUaJsonDecoder(shallow, "{\"UaType\":6,\"Value\":42}")
            .decodeDataValue(null)
            .value()
            .value());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void nonpositiveMaxMessageSizeStillHasFiniteDefault(int limit) {
    String input = "\"" + "x".repeat(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE) + "\"";
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () ->
                new OpcUaJsonDecoder(withLimits(new EncodingLimits(65535, 100, limit, 100)), input)
                    .decodeString(null));
    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, failure.getStatusCode().getValue());
  }

  @Test
  void emptyVariantMatrixLosesShapeButStructureFieldKeepsIt() {
    var context = new DefaultEncodingContext();
    var matrix = new Matrix(new Integer[0], new int[] {0, 2}, OpcUaDataType.Int32);
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeVariant(null, new Variant(matrix));
      assertEquals(
          0,
          assertInstanceOf(
                  Integer[].class,
                  new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeVariant(null).value())
              .length);
      buffer.clear();
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeMatrix(null, matrix);
      Matrix restored =
          new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeMatrix(null, OpcUaDataType.Int32);
      assertArrayEquals(new int[] {0, 2}, restored.getDimensions());
      assertEquals(0, assertInstanceOf(Integer[].class, restored.getElements()).length);
      buffer.clear();
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeMatrix(null, Matrix.ofNull());
      assertTrue(
          new OpcUaBinaryDecoder(context)
              .setBuffer(buffer)
              .decodeMatrix(null, OpcUaDataType.Int32)
              .isNull());
    } finally {
      buffer.release();
    }
  }

  @Test
  void invalidVariantMatrixIsRejectedAtTheDocumentedBoundary() {
    var context = new DefaultEncodingContext();
    ByteBuf buffer = Unpooled.buffer();
    try {
      var invalidDimensions =
          new Matrix(new Integer[] {1, 2, 3, 4}, new int[] {4, 0}, OpcUaDataType.Int32);
      UaSerializationException encode =
          assertThrows(
              UaSerializationException.class,
              () ->
                  new OpcUaBinaryEncoder(context)
                      .setBuffer(buffer)
                      .encodeVariant(null, new Variant(invalidDimensions)));
      assertEquals(StatusCodes.Bad_EncodingError, encode.getStatusCode().getValue());
      buffer.clear();
      var wrongCount = new Matrix(new Integer[] {1, 2, 3}, new int[] {2, 2}, OpcUaDataType.Int32);
      new OpcUaBinaryEncoder(context)
          .setBuffer(buffer)
          .encodeVariant(null, new Variant(wrongCount));
      UaSerializationException decode =
          assertThrows(
              UaSerializationException.class,
              () -> new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeVariant(null));
      assertEquals(StatusCodes.Bad_DecodingError, decode.getStatusCode().getValue());
    } finally {
      buffer.release();
    }
  }

  @Test
  void dateOnlyXmlIsNotADateTime() throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            new DefaultEncodingContext(),
            "<DateTime"
                + " xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">2026-01-01</DateTime>")) {
      UaSerializationException failure =
          assertThrows(UaSerializationException.class, () -> decoder.decodeDateTime(null));
      assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
    }
  }

  @Test
  void unsignedOverloadsAndVariantJavaTypesHaveDifferentSemantics() {
    assertEquals(4294967295L, uint(-1).longValue());
    assertThrows(NumberFormatException.class, () -> uint(-1L));
    assertEquals(OpcUaDataType.SByte, new Variant(new byte[] {1}).getDataType().orElseThrow());
    assertEquals(
        OpcUaDataType.ByteString,
        Variant.of(ByteString.of(new byte[] {1})).getDataType().orElseThrow());
    assertThrows(IllegalArgumentException.class, () -> Variant.of(new Variant(1)));
  }

  private static EncodingContext withLimits(EncodingLimits limits) {
    return new DefaultEncodingContext() {
      @Override
      public EncodingLimits getEncodingLimits() {
        return limits;
      }
    };
  }

  private static String json(EncodingContext context, DataValue value) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, value);
      return encoder.getOutputString();
    }
  }
}
