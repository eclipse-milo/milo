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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The error a caller gets from {@link OpcUaJsonDecoder} when the JSON is malformed. Each message
 * must name the decoder method that rejected the input and the member or token it rejected, and the
 * first error raised must be the one the caller sees.
 *
 * <p>Part 6 §5.4.5 Table 44 defines a Matrix object as the members {@code Array} and {@code
 * Dimensions}; the decoder rejects any other member. Member names are user data and may contain
 * {@code %}, so a message must never be built by passing the name to {@code String.format}.
 */
class JsonDecodingErrorMessageTest {

  private final EncodingContext context = new DefaultEncodingContext();

  private static final NodeId XV_TYPE_ID =
      XVType.TYPE_ID.toNodeId(new DefaultEncodingContext().getNamespaceTable()).orElseThrow();

  private static final String INT32_MATRIX_PREFIX = "{\"Array\":[1,2],\"Dimensions\":[1,2]";
  private static final String XV_MATRIX_PREFIX =
      "{\"Array\":[{\"X\":1.0},{\"X\":2.0}],\"Dimensions\":[1,2]";

  /**
   * Each Matrix decoder with the JSON prefix of a valid Matrix of its element type and the method
   * name its messages carry. The first argument is only a label for the test name.
   */
  static Stream<Arguments> matrixDecoders() {
    Function<OpcUaJsonDecoder, Object> decodeMatrix =
        d -> d.decodeMatrix(null, OpcUaDataType.Int32);
    Function<OpcUaJsonDecoder, Object> decodeEnumMatrix = d -> d.decodeEnumMatrix(null);
    Function<OpcUaJsonDecoder, Object> decodeStructMatrixNodeId =
        d -> d.decodeStructMatrix(null, XV_TYPE_ID);
    Function<OpcUaJsonDecoder, Object> decodeStructMatrixExpandedNodeId =
        d -> d.decodeStructMatrix(null, XVType.TYPE_ID);

    return Stream.of(
        Arguments.of("decodeMatrix", decodeMatrix, INT32_MATRIX_PREFIX, "readMatrix"),
        Arguments.of("decodeEnumMatrix", decodeEnumMatrix, INT32_MATRIX_PREFIX, "readMatrix"),
        Arguments.of(
            "decodeStructMatrix(NodeId)",
            decodeStructMatrixNodeId,
            XV_MATRIX_PREFIX,
            "decodeStructMatrix"),
        Arguments.of(
            "decodeStructMatrix(ExpandedNodeId)",
            decodeStructMatrixExpandedNodeId,
            XV_MATRIX_PREFIX,
            "decodeStructMatrix"));
  }

  static Stream<Arguments> matrixDecodersAndUnknownMembers() {
    return matrixDecoders()
        .flatMap(
            decoder ->
                Stream.of("Extra", "%d")
                    .map(
                        member -> {
                          Object[] args = decoder.get();
                          return Arguments.of(args[0], args[1], args[2], args[3], member);
                        }));
  }

  /**
   * Before this was fixed the member loop threw inside {@code try ... finally {
   * jsonReader.endObject(); }}. The finally block then failed on the unread member value and
   * replaced the decoder's error with Gson's "Expected END_OBJECT but was NUMBER". The decoder's
   * message also named {@code readLocalizedText}, and passed the member name to {@code
   * String.format}, so a member named {@code %d} failed with MissingFormatArgumentException.
   */
  @ParameterizedTest(name = "{0}: {4}")
  @MethodSource("matrixDecodersAndUnknownMembers")
  void unknownMatrixMemberIsReportedByNameWithTheDecoderMethod(
      String label,
      Function<OpcUaJsonDecoder, Object> decode,
      String matrixPrefix,
      String methodName,
      String member) {

    var decoder = new OpcUaJsonDecoder(context, matrixPrefix + ",\"" + member + "\":0}");

    var e = assertThrows(UaSerializationException.class, () -> decode.apply(decoder));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().getValue());
    assertEquals(methodName + ": unexpected field: " + member, e.getMessage());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixDecoders")
  void nonObjectMatrixIsReportedWithTheDecoderMethod(
      String label,
      Function<OpcUaJsonDecoder, Object> decode,
      String matrixPrefix,
      String methodName) {

    var decoder = new OpcUaJsonDecoder(context, "[1,2]");

    var e = assertThrows(UaSerializationException.class, () -> decode.apply(decoder));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().getValue());
    assertEquals(methodName + ": unexpected token: BEGIN_ARRAY", e.getMessage());
  }

  /**
   * The same finally block hid an element's own error behind "Expected END_OBJECT but was STRING
   * ... path $.Array[1]". The element decoder's message must reach the caller unchanged, as it does
   * from a plain array.
   */
  @Test
  void badMatrixElementReportsTheElementDecodersError() {
    var decoder = new OpcUaJsonDecoder(context, "{\"Array\":[1,\"x\"],\"Dimensions\":[1,2]}");

    var e =
        assertThrows(
            UaSerializationException.class, () -> decoder.decodeMatrix(null, OpcUaDataType.Int32));

    assertEquals("readInt32: unexpected token: STRING", e.getMessage());
  }

  @Test
  void badEnumMatrixElementReportsTheElementDecodersError() {
    var decoder = new OpcUaJsonDecoder(context, "{\"Array\":[1,\"x\"],\"Dimensions\":[1,2]}");

    var e = assertThrows(UaSerializationException.class, () -> decoder.decodeEnumMatrix(null));

    assertEquals("readInt32: unexpected token: STRING", e.getMessage());
  }

  @Test
  void badStructMatrixElementReportsTheStructDecodersError() {
    var decoder =
        new OpcUaJsonDecoder(
            context, "{\"Array\":[{\"X\":1.0},{\"Bogus\":0}],\"Dimensions\":[1,2]}");

    var e =
        assertThrows(
            UaSerializationException.class, () -> decoder.decodeStructMatrix(null, XV_TYPE_ID));

    assertEquals("Unexpected structure field: Bogus", e.getMessage());
  }

  // The member name used to be the String.format format string, so "%d" failed with
  // MissingFormatArgumentException instead of Bad_DecodingError.
  @Test
  void unknownLocalizedTextMemberIsReportedByName() {
    var decoder = new OpcUaJsonDecoder(context, "{\"Text\":\"a\",\"%d\":0}");

    var e = assertThrows(UaSerializationException.class, () -> decoder.decodeLocalizedText(null));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().getValue());
    assertEquals("readLocalizedText: unexpected field: %d", e.getMessage());
  }

  /**
   * An ExpandedNodeId whose namespace URI is not in the namespace table cannot be resolved to a
   * codec. decodeStructArray used to describe this as "no codec registered", which is the message
   * for a registered namespace with no codec; it now says "namespace not registered" like
   * decodeStruct and decodeStructMatrix.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("structDecodersByExpandedNodeId")
  void unregisteredNamespaceIsReportedAsSuch(
      String label, Function<OpcUaJsonDecoder, Object> decode, String methodName) {

    var decoder = new OpcUaJsonDecoder(context, "null");

    var e = assertThrows(UaSerializationException.class, () -> decode.apply(decoder));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().getValue());
    assertEquals(
        methodName + ": namespace not registered: " + UNREGISTERED_TYPE_ID, e.getMessage());
  }

  private static final ExpandedNodeId UNREGISTERED_TYPE_ID =
      ExpandedNodeId.of("urn:eclipse:milo:test:unregistered", 1);

  static Stream<Arguments> structDecodersByExpandedNodeId() {
    Function<OpcUaJsonDecoder, Object> decodeStruct =
        d -> d.decodeStruct(null, UNREGISTERED_TYPE_ID);
    Function<OpcUaJsonDecoder, Object> decodeStructArray =
        d -> d.decodeStructArray(null, UNREGISTERED_TYPE_ID);
    Function<OpcUaJsonDecoder, Object> decodeStructMatrix =
        d -> d.decodeStructMatrix(null, UNREGISTERED_TYPE_ID);

    return Stream.of(
        Arguments.of("decodeStruct", decodeStruct, "readStruct"),
        Arguments.of("decodeStructArray", decodeStructArray, "readStructArray"),
        Arguments.of("decodeStructMatrix", decodeStructMatrix, "decodeStructMatrix"));
  }
}
