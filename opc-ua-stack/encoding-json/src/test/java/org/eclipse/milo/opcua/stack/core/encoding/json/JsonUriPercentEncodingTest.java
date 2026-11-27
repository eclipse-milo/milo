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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies OPC 10000-6 §5.1.12 ({@code ENCODEDURI}) percent-encoding and strict UTF-8 decoding of
 * {@code nsu=} and {@code svu=} URIs in JSON encodings for {@link NodeId}, {@link ExpandedNodeId},
 * and {@link QualifiedName}.
 */
class JsonUriPercentEncodingTest {

  private static final ByteString SPEC_OPAQUE_ID =
      ByteString.of(Base64.getDecoder().decode("M/RbKBsRVkePCePcx24oRA=="));

  private final EncodingContext context = new DefaultEncodingContext();

  @Nested
  class NodeIdEncoding {

    // OPC 10000-6 §5.4.2.10 & §5.1.12: NodeId JSON encoding writes nsu= URIs with ';', '%',
    // spaces, and non-ASCII characters percent-encoded while leaving the identifier unescaped.
    @ParameterizedTest
    @MethodSource("nodeIdUriCases")
    void encodeAndDecodeNodeIdWithEscapedNamespaceUri(
        String namespaceUri, Object identifier, String expectedJson) throws Exception {

      UShort nsIndex = context.getNamespaceTable().add(namespaceUri);
      NodeId nodeId = nodeIdOf(nsIndex, identifier);

      String json;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeNodeId(null, nodeId);
        json = encoder.getOutputString();
      }

      assertEquals(expectedJson, json);
      assertEquals(nodeId, new OpcUaJsonDecoder(context, json).decodeNodeId(null));
    }

    static Stream<Arguments> nodeIdUriCases() {
      return Stream.of(
          Arguments.of(
              "tag:acme.com,2023:schemas:data#off;",
              SPEC_OPAQUE_ID,
              "\"nsu=tag:acme.com,2023:schemas:data#off%3B;b=M/RbKBsRVkePCePcx24oRA==\""),
          Arguments.of("urn:example;version=2", "Tag1", "\"nsu=urn:example%3Bversion=2;s=Tag1\""),
          Arguments.of("urn:x;s=a", "b", "\"nsu=urn:x%3Bs=a;s=b\""),
          Arguments.of("urn:example:100%", uint(42), "\"nsu=urn:example:100%25;i=42\""),
          Arguments.of("urn:a%3Bb%25c", "id;100%%ZZ", "\"nsu=urn:a%253Bb%2525c;s=id;100%%ZZ\""),
          Arguments.of("http://example.com/a b", uint(1), "\"nsu=http://example.com/a%20b;i=1\""),
          Arguments.of(
              "http://example.com/café", uint(1), "\"nsu=http://example.com/caf%C3%A9;i=1\""));
    }

    // Multi-byte UTF-8 percent-escapes in nsu= must decode before NamespaceTable lookup.
    @Test
    void decodeNodeIdResolvesMultiByteUtf8EscapedNamespaceUri() {
      UShort nsIndex = context.getNamespaceTable().add("urn:café;");

      NodeId decoded =
          new OpcUaJsonDecoder(context, "\"nsu=urn:caf%C3%A9%3B;i=7\"").decodeNodeId(null);

      assertEquals(new NodeId(nsIndex, 7), decoded);
    }

    // OPC 10000-6 §5.4.2.10: a validly percent-encoded nsu= not in the NamespaceTable still falls
    // back to namespace 0 with the raw JSON string as the String identifier.
    @Test
    void decodeNodeIdWithUnknownValidEscapedUriFallsBackToRawString() {
      NodeId decoded =
          new OpcUaJsonDecoder(context, "\"nsu=urn:unknown%3Bv=2;i=7\"").decodeNodeId(null);

      assertEquals(new NodeId(0, "nsu=urn:unknown%3Bv=2;i=7"), decoded);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "\"nsu=urn:example:100%;i=1\"",
          "\"nsu=urn:example%3;i=1\"",
          "\"nsu=urn:example%GG;i=1\"",
          "\"nsu=urn:example%FF;i=1\"",
          "\"nsu=urn:example%C3;i=1\"",
          "\"nsu=urn:\\uD800;i=1\"",
          "\"nsu=urn:\\uD800%3B;i=1\"",
          "\"nsu=urn:%3B\\uDC00;i=1\""
        })
    void decodeNodeIdRejectsMalformedPercentEscapesWithBadDecodingError(String json) {
      var decoder = new OpcUaJsonDecoder(context, json);

      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> decoder.decodeNodeId(null));

      assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    }

    @Test
    void encodeNodeIdRejectsUnpairedSurrogateInNamespaceUriWithBadEncodingError() {
      UShort nsIndex = context.getNamespaceTable().add("urn:\uD800");
      var nodeId = new NodeId(nsIndex, 1);
      var encoder = new OpcUaJsonEncoder(context);

      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> encoder.encodeNodeId(null, nodeId));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().value());
    }
  }

  @Nested
  class ExpandedNodeIdEncoding {

    // OPC 10000-6 §5.4.2.11 & §5.1.12: index-resolved URIs from ServerTable and NamespaceTable
    // must be percent-encoded when written as svu= and nsu=.
    @Test
    void encodeExpandedNodeIdEscapesIndexResolvedServerAndNamespaceUris() throws Exception {
      context.getServerTable().add("http://localhost");
      context.getServerTable().add("urn:server;east%1 café");
      UShort nsIndex = context.getNamespaceTable().add("urn:ns;v=2%3B");

      ExpandedNodeId original =
          new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(nsIndex), "Tag1");

      String json;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeExpandedNodeId(null, original);
        json = encoder.getOutputString();
      }

      assertEquals("\"svu=urn:server%3Beast%251%20caf%C3%A9;nsu=urn:ns%3Bv=2%253B;s=Tag1\"", json);
    }

    // OPC 10000-6 §5.4.2.11 & §5.1.12: direct ServerUri and NamespaceUri values must be
    // percent-encoded on write, including ';', '%', spaces, and non-ASCII UTF-8 characters.
    @ParameterizedTest
    @MethodSource("encodeExpandedNodeIdDirectUriCases")
    void encodeExpandedNodeIdEscapesDirectServerAndNamespaceUris(
        ExpandedNodeId original, String expectedJson) throws Exception {

      String json;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeExpandedNodeId(null, original);
        json = encoder.getOutputString();
      }

      assertEquals(expectedJson, json);
    }

    static Stream<Arguments> encodeExpandedNodeIdDirectUriCases() {
      return Stream.of(
          Arguments.of(
              new ExpandedNodeId(
                  ServerReference.of("http://smith.com/west/factory"),
                  NamespaceReference.of("tag:acme.com,2023:schemas:data#off;"),
                  SPEC_OPAQUE_ID),
              "\"svu=http://smith.com/west/factory;nsu=tag:acme.com,2023:schemas:data#off%3B;b=M/RbKBsRVkePCePcx24oRA==\""),
          Arguments.of(
              new ExpandedNodeId(
                  ServerReference.of("urn:server;east"),
                  NamespaceReference.of("urn:ns;west"),
                  "Tag1"),
              "\"svu=urn:server%3Beast;nsu=urn:ns%3Bwest;s=Tag1\""),
          Arguments.of(
              new ExpandedNodeId(
                  ServerReference.of("urn:server:100%"),
                  NamespaceReference.of("urn:ns%3B%25"),
                  uint(9)),
              "\"svu=urn:server:100%25;nsu=urn:ns%253B%2525;i=9\""),
          Arguments.of(
              new ExpandedNodeId(
                  ServerReference.of("http://example.com/west plant"),
                  NamespaceReference.of("http://example.com/café"),
                  uint(1)),
              "\"svu=http://example.com/west%20plant;nsu=http://example.com/caf%C3%A9;i=1\""),
          Arguments.of(
              new ExpandedNodeId(ServerReference.of(0), NamespaceReference.of("urn:x;s=a"), "b"),
              "\"nsu=urn:x%3Bs=a;s=b\""));
    }

    // OPC 10000-6 §5.4.2.11: when ServerIndex is 0 and no NamespaceTable mapping exists for nsu=,
    // the decoder stores the percent-decoded NamespaceUri, and re-encoding produces a valid
    // ENCODEDURI again.
    @ParameterizedTest
    @MethodSource("decodeLocalUnmappedExpandedNodeIdCases")
    void decodeAndReencodeLocalExpandedNodeIdWithUnmappedNamespaceUri(
        String json, ExpandedNodeId expected) throws Exception {

      ExpandedNodeId decoded = new OpcUaJsonDecoder(context, json).decodeExpandedNodeId(null);

      assertEquals(expected, decoded);

      String reencoded;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeExpandedNodeId(null, decoded);
        reencoded = encoder.getOutputString();
      }

      assertEquals(json, reencoded);
    }

    static Stream<Arguments> decodeLocalUnmappedExpandedNodeIdCases() {
      return Stream.of(
          Arguments.of(
              "\"nsu=tag:acme.com,2023:schemas:data#off%3B;b=M/RbKBsRVkePCePcx24oRA==\"",
              new ExpandedNodeId(
                  ServerReference.of(0),
                  NamespaceReference.of("tag:acme.com,2023:schemas:data#off;"),
                  SPEC_OPAQUE_ID)),
          Arguments.of(
              "\"nsu=urn:x%3Bs=a;s=b\"",
              new ExpandedNodeId(ServerReference.of(0), NamespaceReference.of("urn:x;s=a"), "b")),
          Arguments.of(
              "\"nsu=urn:ns%253B%2525;i=9\"",
              new ExpandedNodeId(
                  ServerReference.of(0), NamespaceReference.of("urn:ns%3B%25"), uint(9))),
          Arguments.of(
              "\"nsu=http://example.com/a%20b;i=1\"",
              new ExpandedNodeId(
                  ServerReference.of(0), NamespaceReference.of("http://example.com/a b"), uint(1))),
          Arguments.of(
              "\"nsu=http://example.com/caf%C3%A9;i=1\"",
              new ExpandedNodeId(
                  ServerReference.of(0),
                  NamespaceReference.of("http://example.com/café"),
                  uint(1))),
          Arguments.of(
              "\"nsu=urn:ns%E6%B0%B4%3B;i=1\"",
              new ExpandedNodeId(
                  ServerReference.of(0), NamespaceReference.of("urn:ns水;"), uint(1))));
    }

    // Malformed percent-escapes in ExpandedNodeId JSON strings must surface as
    // UaSerializationException(Bad_DecodingError), not leak UaRuntimeException(Bad_NodeIdInvalid).
    @ParameterizedTest
    @ValueSource(
        strings = {
          "\"nsu=urn:example:100%;i=1\"",
          "\"nsu=urn:example%3;i=1\"",
          "\"nsu=urn:example%GG;i=1\"",
          "\"nsu=urn:example%FF;i=1\"",
          "\"nsu=urn:example%C3;i=1\"",
          "\"svu=urn:server:100%;i=1\"",
          "\"svu=urn:server%3;i=1\"",
          "\"svu=urn:server%GG;i=1\"",
          "\"svu=urn:server%C3;nsu=urn:ok;i=1\"",
          "\"nsu=urn:\\uD800;i=1\"",
          "\"svu=urn:\\uD800;i=1\"",
          "\"nsu=urn:%3B\\uDC00;i=1\""
        })
    void decodeExpandedNodeIdRejectsMalformedPercentEscapesWithBadDecodingError(String json) {
      var decoder = new OpcUaJsonDecoder(context, json);

      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> decoder.decodeExpandedNodeId(null));

      assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    }

    @Test
    void encodeExpandedNodeIdRejectsUnpairedSurrogateWithBadEncodingError() {
      var nodeId =
          new ExpandedNodeId(ServerReference.of(0), NamespaceReference.of("urn:\uD800"), uint(1));
      var encoder = new OpcUaJsonEncoder(context);

      UaSerializationException e =
          assertThrows(
              UaSerializationException.class, () -> encoder.encodeExpandedNodeId(null, nodeId));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().value());
    }
  }

  @Nested
  class QualifiedNameEncoding {

    // OPC 10000-6 §5.4.2.14 & §5.1.12: QualifiedName JSON encoding percent-encodes ';', '%',
    // spaces, and non-ASCII characters in the nsu= URI while preserving the name literally.
    @ParameterizedTest
    @MethodSource("qualifiedNameUriCases")
    void encodeAndDecodeQualifiedNameWithEscapedNamespaceUri(
        String namespaceUri, String name, String expectedJson) throws Exception {

      UShort nsIndex = context.getNamespaceTable().add(namespaceUri);
      var qn = new QualifiedName(nsIndex, name);

      String json;
      try (var encoder = new OpcUaJsonEncoder(context)) {
        encoder.encodeQualifiedName(null, qn);
        json = encoder.getOutputString();
      }

      assertEquals(expectedJson, json);
      assertEquals(qn, new OpcUaJsonDecoder(context, json).decodeQualifiedName(null));
    }

    static Stream<Arguments> qualifiedNameUriCases() {
      return Stream.of(
          Arguments.of(
              "tag:acme.com,2023:schemas:data#off;",
              "Boiler2",
              "\"nsu=tag:acme.com,2023:schemas:data#off%3B;Boiler2\""),
          Arguments.of(
              "urn:example;version=2",
              "Hello;World",
              "\"nsu=urn:example%3Bversion=2;Hello;World\""),
          Arguments.of("urn:example:100%", "Load%25%ZZ", "\"nsu=urn:example:100%25;Load%25%ZZ\""),
          Arguments.of("urn:a%3Bb%25c", "Name", "\"nsu=urn:a%253Bb%2525c;Name\""),
          Arguments.of(
              "http://example.com/a b", "Room 1", "\"nsu=http://example.com/a%20b;Room 1\""),
          Arguments.of(
              "http://example.com/café", "Café", "\"nsu=http://example.com/caf%C3%A9;Café\""));
    }

    @Test
    void decodeQualifiedNameResolvesMultiByteUtf8EscapedNamespaceUri() {
      UShort nsIndex = context.getNamespaceTable().add("urn:café;");

      QualifiedName decoded =
          new OpcUaJsonDecoder(context, "\"nsu=urn:caf%C3%A9%3B;Boiler2\"")
              .decodeQualifiedName(null);

      assertEquals(new QualifiedName(nsIndex, "Boiler2"), decoded);
    }

    // OPC 10000-6 §5.4.2.14: a validly percent-encoded nsu= not in the NamespaceTable still falls
    // back to namespace 0 with the raw JSON string as the Name.
    @Test
    void decodeQualifiedNameWithUnknownValidEscapedUriFallsBackToRawString() {
      QualifiedName decoded =
          new OpcUaJsonDecoder(context, "\"nsu=urn:unknown%3Bns;Boiler2\"")
              .decodeQualifiedName(null);

      assertEquals(new QualifiedName(ushort(0), "nsu=urn:unknown%3Bns;Boiler2"), decoded);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "\"nsu=urn:example:100%;Name\"",
          "\"nsu=urn:example%3;Name\"",
          "\"nsu=urn:example%GG;Name\"",
          "\"nsu=urn:example%FF;Name\"",
          "\"nsu=urn:example%C3;Name\"",
          "\"nsu=urn:\\uD800;Name\"",
          "\"nsu=urn:%3B\\uDC00;Name\""
        })
    void decodeQualifiedNameRejectsMalformedPercentEscapesWithBadDecodingError(String json) {
      var decoder = new OpcUaJsonDecoder(context, json);

      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> decoder.decodeQualifiedName(null));

      assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    }

    @Test
    void encodeQualifiedNameRejectsUnpairedSurrogateInNamespaceUriWithBadEncodingError() {
      UShort nsIndex = context.getNamespaceTable().add("urn:\uD800");
      var qn = new QualifiedName(nsIndex, "Name");
      var encoder = new OpcUaJsonEncoder(context);

      UaSerializationException e =
          assertThrows(UaSerializationException.class, () -> encoder.encodeQualifiedName(null, qn));

      assertEquals(StatusCodes.Bad_EncodingError, e.getStatusCode().value());
    }
  }

  private static NodeId nodeIdOf(UShort namespaceIndex, Object identifier) {
    if (identifier instanceof UInteger i) {
      return new NodeId(namespaceIndex, i);
    } else if (identifier instanceof String s) {
      return new NodeId(namespaceIndex, s);
    } else if (identifier instanceof ByteString bs) {
      return new NodeId(namespaceIndex, bs);
    } else {
      throw new IllegalArgumentException("unsupported identifier: " + identifier);
    }
  }
}
