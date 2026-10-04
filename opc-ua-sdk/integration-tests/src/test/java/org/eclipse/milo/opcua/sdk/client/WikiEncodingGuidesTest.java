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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.StringWriter;
import java.lang.reflect.Array;
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
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoder;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecord;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
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
        "{\"UaType\":11,\"UaType\":6,\"Value\":42}",
        "{\"UaType\":11,\"Type\":6,\"Value\":42}"
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
  void streamedTimestampsAndLocalizedTextUseTheirLastOccurrence() {
    var context = new DefaultEncodingContext();
    String first = "2026-01-01T00:00:00Z";
    String last = "2026-01-02T00:00:00Z";
    String input =
        "{\"UaType\":6,\"Value\":42,\"SourceTimestamp\":\""
            + first
            + "\",\"SourceTimestamp\":\""
            + last
            + "\",\"ServerTimestamp\":\""
            + first
            + "\",\"ServerTimestamp\":\""
            + last
            + "\"}";
    DataValue decoded = new OpcUaJsonDecoder(context, input).decodeDataValue(null);
    assertEquals(new DateTime(Instant.parse(last)), decoded.sourceTime());
    assertEquals(decoded.sourceTime(), decoded.serverTime());
    assertEquals(42, decoded.value().value());
    LocalizedText text =
        new OpcUaJsonDecoder(context, "{\"Text\":\"before\",\"Text\":\"after\"}")
            .decodeLocalizedText(null);
    assertEquals("after", text.getText());
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
      assertNull(binaryVariant(context, new Variant(Matrix.ofNull())).value());
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

  // Variant normalization does not make a negative dimension valid in a Matrix field.
  @Test
  void negativeMatrixDimensionLosesShapeInVariantButFailsFieldDecoding() {
    var context = new DefaultEncodingContext();
    var matrix = new Matrix(new Integer[0], new int[] {-1, 2}, OpcUaDataType.Int32);
    assertEquals(
        0,
        assertInstanceOf(Integer[].class, binaryVariant(context, new Variant(matrix)).value())
            .length);
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeMatrix(null, matrix);
      UaSerializationException failure =
          assertThrows(
              UaSerializationException.class,
              () ->
                  new OpcUaBinaryDecoder(context)
                      .setBuffer(buffer)
                      .decodeMatrix(null, OpcUaDataType.Int32));
      assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
      assertTrue(failure.getMessage().contains("matrix dimension must not be negative"));
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
    assertEquals(255, ubyte((byte) -1).intValue());
    assertEquals(65535, ushort((short) -1).intValue());
    assertEquals(4294967295L, uint(-1).longValue());
    assertThrows(NumberFormatException.class, () -> uint(-1L));
    assertEquals(OpcUaDataType.SByte, new Variant(new byte[] {1}).getDataType().orElseThrow());
    assertEquals(
        OpcUaDataType.ByteString,
        Variant.of(ByteString.of(new byte[] {1})).getDataType().orElseThrow());
    assertThrows(IllegalArgumentException.class, () -> Variant.of(new Variant(1)));
  }

  // Persisting an OPC TCP value as JSON must not make callers assume the same Java array class.
  @ParameterizedTest
  @MethodSource("arrayRepresentations")
  void variantAndMatrixArraysUseDecoderSpecificJavaClasses(
      Object values, Class<?> jsonArrayClass, OpcUaDataType type) throws Exception {
    var context = new DefaultEncodingContext();
    Variant binaryArray = binaryVariant(context, new Variant(values));
    Variant jsonArray = jsonVariant(context, new Variant(values));
    assertEquals(values.getClass(), binaryArray.value().getClass());
    assertEquals(jsonArrayClass, jsonArray.value().getClass());
    assertEquals(type, jsonArray.getDataType().orElseThrow());
    assertArrayValues(values, binaryArray.value());
    assertArrayValues(values, jsonArray.value());

    Matrix original = new Matrix(values, new int[] {1, 2}, type);
    Matrix binaryMatrix =
        assertInstanceOf(Matrix.class, binaryVariant(context, new Variant(original)).value());
    Matrix jsonMatrix =
        assertInstanceOf(Matrix.class, jsonVariant(context, new Variant(original)).value());
    assertEquals(values.getClass().getComponentType(), binaryMatrix.getElementType().orElseThrow());
    assertEquals(jsonArrayClass.getComponentType(), jsonMatrix.getElementType().orElseThrow());
    assertEquals(type, jsonMatrix.getDataType().orElseThrow());
    assertArrayEquals(new int[] {1, 2}, binaryMatrix.getDimensions());
    assertArrayEquals(new int[] {1, 2}, jsonMatrix.getDimensions());
    assertArrayValues(values, binaryMatrix.getElements());
    assertArrayValues(values, jsonMatrix.getElements());
  }

  static Stream<Arguments> arrayRepresentations() {
    return Stream.of(
        Arguments.of(new Boolean[] {true, false}, boolean[].class, OpcUaDataType.Boolean),
        Arguments.of(new Byte[] {-1, 2}, byte[].class, OpcUaDataType.SByte),
        Arguments.of(new Short[] {-1, 2}, short[].class, OpcUaDataType.Int16),
        Arguments.of(new Integer[] {-1, 2}, int[].class, OpcUaDataType.Int32),
        Arguments.of(new Long[] {-1L, 2L}, long[].class, OpcUaDataType.Int64),
        Arguments.of(new Float[] {-1.25f, 2.5f}, float[].class, OpcUaDataType.Float),
        Arguments.of(new Double[] {-1.25, 2.5}, double[].class, OpcUaDataType.Double),
        Arguments.of(
            new UInteger[] {uint(0), uint(4_000_000_000L)},
            UInteger[].class,
            OpcUaDataType.UInt32));
  }

  // The matching-mode control proves that a mode error does not come from a bad optional value.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void optionalStructureRejectsMismatchedJsonMode(Encoding encoding) throws Exception {
    var context = new DefaultEncodingContext();
    var original =
        new LogRecord(
            new DateTime(Instant.parse("2026-01-01T00:00:00Z")),
            ushort(100),
            null,
            null,
            "source",
            LocalizedText.english("event"),
            null,
            null);
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, original, new LogRecord.Codec());
      json = encoder.getOutputString();
    }
    assertEquals(encoding == Encoding.COMPACT, json.contains("\"EncodingMask\":4"));
    var matching = new OpcUaJsonDecoder(context, json);
    matching.setEncoding(encoding);
    assertEquals(original, matching.decodeStruct(null, new LogRecord.Codec()));
    var mismatched = new OpcUaJsonDecoder(context, json);
    mismatched.setEncoding(encoding == Encoding.COMPACT ? Encoding.VERBOSE : Encoding.COMPACT);
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () -> mismatched.decodeStruct(null, new LogRecord.Codec()));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
    assertTrue(failure.getMessage().contains("Unexpected structure field"));
  }

  // Enum-bearing structures require matching modes even when they have no optional fields.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void enumStructureUsesNumericOrNamedValueAndRejectsMismatchedMode(Encoding encoding)
      throws Exception {
    var context = new DefaultEncodingContext();
    DateTime time = new DateTime(Instant.parse("2026-01-01T00:00:00Z"));
    var original =
        new ServerStatusDataType(
            time,
            time,
            ServerState.Failed,
            new BuildInfo("urn:wiki", "Milo", "Fixture", "1.2", "1", time),
            uint(10),
            LocalizedText.english("test"));
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, original, new ServerStatusDataType.Codec());
      json = encoder.getOutputString();
    }
    assertTrue(
        json.contains(encoding == Encoding.COMPACT ? "\"State\":1" : "\"State\":\"Failed_1\""));
    var matching = new OpcUaJsonDecoder(context, json);
    matching.setEncoding(encoding);
    assertEquals(original, matching.decodeStruct(null, new ServerStatusDataType.Codec()));
    var mismatched = new OpcUaJsonDecoder(context, json);
    mismatched.setEncoding(encoding == Encoding.COMPACT ? Encoding.VERBOSE : Encoding.COMPACT);
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () -> mismatched.decodeStruct(null, new ServerStatusDataType.Codec()));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
  }

  @Test
  void jsonNullElementsRoundTripOnlyForNullableElementTypes() throws Exception {
    var context = new DefaultEncodingContext();
    assertArrayEquals(
        new String[] {"a", null},
        assertInstanceOf(
            String[].class, jsonVariant(context, new Variant(new String[] {"a", null})).value()));
    ByteString[] bytes =
        assertInstanceOf(
            ByteString[].class,
            jsonVariant(
                    context, new Variant(new ByteString[] {ByteString.of(new byte[] {1}), null}))
                .value());
    assertArrayEquals(new byte[] {1}, bytes[0].bytesOrEmpty());
    assertEquals(ByteString.NULL_VALUE, bytes[1]);
    UaSerializationException failure =
        assertThrows(
            UaSerializationException.class,
            () -> jsonVariant(context, new Variant(new Integer[] {1, null})));
    assertEquals(StatusCodes.Bad_DecodingError, failure.getStatusCode().getValue());
  }

  // Reusing an application-owned output Writer requires knowing which encoder closes it.
  @Test
  void jsonEncoderClosesItsWriterButXmlEncoderLeavesItOwnedByTheCaller() throws Exception {
    class TrackedWriter extends StringWriter {
      boolean closed;

      @Override
      public void close() {
        closed = true;
      }
    }
    var context = new DefaultEncodingContext();
    var json = new TrackedWriter();
    try (var encoder = new OpcUaJsonEncoder(context, json)) {
      encoder.encodeInt32(null, 42);
    }
    assertTrue(json.closed);
    assertEquals(42, new OpcUaJsonDecoder(context, json.toString()).decodeInt32(null));

    var xml = new TrackedWriter();
    try (var encoder = new OpcUaXmlEncoder(context, xml)) {
      // A named field supplies the document element; a null field writes only character data.
      encoder.encodeInt32("Int32", 42);
    }
    assertFalse(xml.closed);
    try (var decoder = new OpcUaXmlDecoder(context, xml.toString())) {
      assertEquals(42, decoder.decodeInt32(null));
    } finally {
      xml.close();
    }
    assertTrue(xml.closed);
  }

  private static EncodingContext withLimits(EncodingLimits limits) {
    return new DefaultEncodingContext() {
      @Override
      public EncodingLimits getEncodingLimits() {
        return limits;
      }
    };
  }

  private static Variant binaryVariant(EncodingContext context, Variant value) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeVariant(null, value);
      Variant decoded = new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeVariant(null);
      assertFalse(buffer.isReadable());
      return decoded;
    } finally {
      buffer.release();
    }
  }

  private static Variant jsonVariant(EncodingContext context, Variant value) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeVariant(null, value);
      return new OpcUaJsonDecoder(context, encoder.getOutputString()).decodeVariant(null);
    }
  }

  private static void assertArrayValues(Object expected, Object actual) {
    assertEquals(Array.getLength(expected), Array.getLength(actual));
    for (int i = 0; i < Array.getLength(expected); i++) {
      assertEquals(Array.get(expected, i), Array.get(actual, i), "element " + i);
    }
  }

  private static String json(EncodingContext context, DataValue value) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, value);
      return encoder.getOutputString();
    }
  }
}
