/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class UriUtilTest {

  @Nested
  class Escape {

    // OPC 10000-6 §5.1.12 / RFC 3986 §2.2-§2.3: valid RFC 3986 URI characters (unreserved,
    // gen-delims, and sub-delims except ';') must remain byte-for-byte identical.
    @ParameterizedTest
    @ValueSource(
        strings = {
          "",
          "http://opcfoundation.org/UA/",
          "urn:eclipse:milo:test",
          "http://softing.com/dataFEEDSIS/nsuri?conn=Demo&uri=http://opcfoundation.org/UA/",
          "tag:acme.com,2023:schemas:data#off",
          "http://user:pass@[::1]:8080/path-._~!$'*+,=/?a=1&b=2#frag"
        })
    void leavesRfc3986UrisWithoutPercentOrSemicolonUnchanged(String uri) {
      assertSame(uri, UriUtil.escape(uri));
    }

    // OPC 10000-6 §5.1.12 Table 4 / RFC 3986: ';', '%', spaces, disallowed ASCII characters,
    // control characters, and non-ASCII Unicode code points must be UTF-8 percent-encoded.
    @ParameterizedTest
    @MethodSource("escapeCases")
    void escapesPercentSemicolonAndNonRfc3986Characters(String input, String expected) {
      assertEquals(expected, UriUtil.escape(input));
    }

    static Stream<Arguments> escapeCases() {
      return Stream.of(
          Arguments.of(
              "tag:acme.com,2023:schemas:data#off;", "tag:acme.com,2023:schemas:data#off%3B"),
          Arguments.of("urn:example;version=2", "urn:example%3Bversion=2"),
          Arguments.of("urn:example:100%", "urn:example:100%25"),
          Arguments.of("urn:a%3Bb", "urn:a%253Bb"),
          Arguments.of("urn:a%25b", "urn:a%2525b"),
          Arguments.of(";", "%3B"),
          Arguments.of("%", "%25"),
          Arguments.of("%;;%", "%25%3B%3B%25"),
          Arguments.of("http://example.com/a b", "http://example.com/a%20b"),
          Arguments.of("http://example.com/café", "http://example.com/caf%C3%A9"),
          Arguments.of("urn:café;100%:水", "urn:caf%C3%A9%3B100%25:%E6%B0%B4"),
          Arguments.of("urn:emoji:\uD83D\uDE00", "urn:emoji:%F0%9F%98%80"),
          Arguments.of(
              "urn:disallowed:\"<>\\^`{|}\t\u007F",
              "urn:disallowed:%22%3C%3E%5C%5E%60%7B%7C%7D%09%7F"));
    }

    // Unpaired UTF-16 surrogates cannot form valid UTF-8 and must be rejected rather than emitting
    // invalid %ED%A0%80 escapes or silently substituting U+FFFD.
    @ParameterizedTest
    @ValueSource(strings = {"urn:\uD800", "urn:\uD83Da", "urn:\uDE00"})
    void rejectsUnpairedUtf16Surrogates(String malformed) {
      assertThrows(IllegalArgumentException.class, () -> UriUtil.escape(malformed));
    }
  }

  @Nested
  class Unescape {

    @ParameterizedTest
    @ValueSource(
        strings = {
          "",
          "http://opcfoundation.org/UA/",
          "http://softing.com/dataFEEDSIS/nsuri?conn=Demo&uri=http://opcfoundation.org/UA/",
          "urn:café:水",
          "urn:emoji:\uD83D\uDE00"
        })
    void leavesUrisWithoutPercentUnchanged(String uri) {
      assertSame(uri, UriUtil.unescape(uri));
    }

    // RFC 3986 §2.1 allows both uppercase and lowercase hex digits, and decoding must happen in a
    // single pass so '%253B' becomes literal '%3B' rather than ';'.
    @ParameterizedTest
    @MethodSource("unescapeAsciiCases")
    void decodesAsciiPercentEscapesInSinglePass(String input, String expected) {
      assertEquals(expected, UriUtil.unescape(input));
    }

    static Stream<Arguments> unescapeAsciiCases() {
      return Stream.of(
          Arguments.of(
              "tag:acme.com,2023:schemas:data#off%3B", "tag:acme.com,2023:schemas:data#off;"),
          Arguments.of("urn:example%3bversion=2", "urn:example;version=2"),
          Arguments.of("urn:example:100%25", "urn:example:100%"),
          Arguments.of("urn:a%253Bb", "urn:a%3Bb"),
          Arguments.of("urn:a%2525b", "urn:a%25b"),
          Arguments.of("http://example.com/a%20b%2Fc", "http://example.com/a b/c"),
          Arguments.of("%25%3B%3b%25", "%;;%"));
    }

    // Consecutive percent-encoded octets must be decoded together as UTF-8, including 2-, 3-, and
    // 4-byte code points and mixtures with ASCII escapes.
    @ParameterizedTest
    @MethodSource("unescapeUtf8Cases")
    void decodesMultiByteUtf8PercentEscapes(String input, String expected) {
      assertEquals(expected, UriUtil.unescape(input));
    }

    static Stream<Arguments> unescapeUtf8Cases() {
      return Stream.of(
          Arguments.of("urn:caf%C3%A9", "urn:café"),
          Arguments.of("urn:caf%c3%A9", "urn:café"),
          Arguments.of("urn:%E6%B0%B4", "urn:水"),
          Arguments.of("urn:%E2%82%AC", "urn:€"),
          Arguments.of("urn:%F0%9F%98%80", "urn:\uD83D\uDE00"),
          Arguments.of("urn:caf%C3%A9%3B", "urn:café;"),
          Arguments.of("urn:%3Bcaf%C3%A9", "urn:;café"),
          Arguments.of("urn:caf%C3%A9/%E2%82%AC%253B", "urn:café/€%3B"),
          Arguments.of("urn:" + "%C3%A9".repeat(20), "urn:" + "é".repeat(20)));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "",
          "urn:plain",
          "urn:example;version=2",
          "urn:example:100%",
          "urn:literal%3B;and%25;both%;",
          "http://example.com/a b",
          "http://example.com/café",
          "urn:café;100%;\uD83D\uDE00"
        })
    void escapeThenUnescapeRoundTrips(String original) {
      assertEquals(original, UriUtil.unescape(UriUtil.escape(original)));
    }

    // OPC 10000-6 §5.1.12 Table 4: parsing an external ENCODEDURI and writing it back must emit a
    // valid ENCODEDURI rather than leaking unescaped spaces or raw non-ASCII characters.
    @ParameterizedTest
    @MethodSource("wireRoundTripCases")
    void unescapeThenEscapePreservesValidEncodedUri(
        String wireUri, String expectedDecoded, String expectedReencoded) {

      String decoded = UriUtil.unescape(wireUri);

      assertEquals(expectedDecoded, decoded);
      assertEquals(expectedReencoded, UriUtil.escape(decoded));
    }

    static Stream<Arguments> wireRoundTripCases() {
      return Stream.of(
          Arguments.of(
              "http://example.com/a%20b", "http://example.com/a b", "http://example.com/a%20b"),
          Arguments.of(
              "http://example.com/caf%C3%A9",
              "http://example.com/café", "http://example.com/caf%C3%A9"),
          Arguments.of(
              "http://example.com/caf%c3%a9",
              "http://example.com/café", "http://example.com/caf%C3%A9"),
          Arguments.of(
              "tag:acme.com,2023:schemas:data#off%3B",
              "tag:acme.com,2023:schemas:data#off;", "tag:acme.com,2023:schemas:data#off%3B"));
    }

    // Strict RFC 3986 / UTF-8 decoding must reject bare '%', truncated escapes, non-ASCII or
    // non-hex digits, and invalid UTF-8 byte sequences rather than silently substituting U+FFFD.
    @ParameterizedTest
    @ValueSource(
        strings = {
          "%",
          "urn:example:100%",
          "urn:example%3",
          "urn:example%a",
          "%%25",
          "urn:example%ZZ",
          "urn:example%3G",
          "urn:example%G3",
          "urn:example% 0",
          "urn:example%\uFF13\uFF22",
          "urn:bad%80",
          "urn:bad%BF",
          "urn:bad%C0%AF",
          "urn:bad%C1%BF",
          "urn:bad%F5%80%80%80",
          "urn:bad%FF",
          "urn:truncated%C3",
          "urn:truncated%C3a",
          "urn:truncated%C3%3B",
          "urn:truncated%C3é",
          "urn:truncated%E2%82",
          "urn:truncated%F0%9F%98",
          "urn:overlong%E0%80%AF",
          "urn:overlong%F0%80%80%AF",
          "urn:surrogate%ED%A0%80",
          "urn:surrogate%ED%BF%BF",
          "urn:above-max%F4%90%80%80",
          "urn:\uD800",
          "urn:\uD83Da",
          "urn:\uDE00",
          "urn:\uDC00\uD800",
          "urn:\uD800%3B",
          "urn:%3B\uD800",
          "urn:%3B\uDC00"
        })
    void rejectsMalformedPercentEscapesAndInvalidUtf8(String malformed) {
      assertThrows(IllegalArgumentException.class, () -> UriUtil.unescape(malformed));
    }
  }
}
