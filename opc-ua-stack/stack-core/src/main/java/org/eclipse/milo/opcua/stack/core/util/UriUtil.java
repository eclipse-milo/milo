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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

/**
 * Percent-encodes and decodes URI components for OPC UA string encodings ({@code nsu=} and {@code
 * svu=}) as defined by OPC 10000-6 §5.1.12 ({@code ENCODEDURI}).
 */
@NullMarked
public final class UriUtil {

  private static final boolean[] ALLOWED_URI_CHARS = createAllowedUriChars();
  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

  private UriUtil() {}

  /**
   * Percent-encode a URI for embedding in an OPC UA {@code nsu=} or {@code svu=} field.
   *
   * <p>Preserves ASCII characters in the RFC 3986 URI repertoire ({@code unreserved}, {@code
   * gen-delims}, and {@code sub-delims} other than {@code ;}). Replaces {@code %} with {@code %25},
   * {@code ;} with {@code %3B}, and all characters outside the RFC 3986 URI repertoire (such as
   * whitespace, disallowed ASCII characters, and non-ASCII Unicode code points) with uppercase
   * UTF-8 percent-escapes per OPC 10000-6 §5.1.12 and RFC 3986 §2.1–§2.4.
   *
   * @param uri the URI to percent-encode.
   * @return the percent-encoded URI.
   * @throws IllegalArgumentException if {@code uri} contains an unpaired UTF-16 surrogate.
   */
  public static String escape(String uri) {
    Objects.requireNonNull(uri, "uri");

    int length = uri.length();
    int firstEscape = -1;
    for (int i = 0; i < length; i++) {
      if (!isAllowedUriChar(uri.charAt(i))) {
        firstEscape = i;
        break;
      }
    }

    if (firstEscape < 0) {
      return uri;
    }

    var sb = new StringBuilder(length + 8);
    sb.append(uri, 0, firstEscape);

    int i = firstEscape;
    while (i < length) {
      char c = uri.charAt(i);
      if (isAllowedUriChar(c)) {
        sb.append(c);
        i++;
      } else if (c < 0x80) {
        appendPercentByte(sb, c);
        i++;
      } else {
        int cp = uri.codePointAt(i);
        if (cp <= 0x7FF) {
          appendPercentByte(sb, 0xC0 | (cp >> 6));
          appendPercentByte(sb, 0x80 | (cp & 0x3F));
          i++;
        } else if (cp <= 0xFFFF) {
          if (Character.isSurrogate((char) cp)) {
            throw new IllegalArgumentException(
                "Unpaired UTF-16 surrogate at index " + i + " in URI");
          }
          appendPercentByte(sb, 0xE0 | (cp >> 12));
          appendPercentByte(sb, 0x80 | ((cp >> 6) & 0x3F));
          appendPercentByte(sb, 0x80 | (cp & 0x3F));
          i++;
        } else {
          appendPercentByte(sb, 0xF0 | (cp >> 18));
          appendPercentByte(sb, 0x80 | ((cp >> 12) & 0x3F));
          appendPercentByte(sb, 0x80 | ((cp >> 6) & 0x3F));
          appendPercentByte(sb, 0x80 | (cp & 0x3F));
          i += 2;
        }
      }
    }

    return sb.toString();
  }

  /**
   * Percent-decode a URI from an OPC UA {@code nsu=} or {@code svu=} field using strict UTF-8
   * decoding.
   *
   * <p>Every {@code %} must introduce a valid two-digit ASCII hexadecimal escape ({@code %XX}), and
   * consecutive percent-encoded octets must form valid UTF-8 byte sequences.
   *
   * @param uri the percent-encoded URI to decode.
   * @return the decoded URI.
   * @throws IllegalArgumentException if {@code uri} contains a truncated percent-escape, a non-hex
   *     digit after {@code %}, an invalid UTF-8 byte sequence, or an unpaired UTF-16 surrogate.
   */
  public static String unescape(String uri) {
    Objects.requireNonNull(uri, "uri");

    int firstPercent = uri.indexOf('%');
    if (firstPercent < 0) {
      validateSurrogates(uri, 0, uri.length());
      return uri;
    }

    validateSurrogates(uri, 0, firstPercent);

    int length = uri.length();
    var sb = new StringBuilder(length);
    sb.append(uri, 0, firstPercent);

    CharsetDecoder utf8Decoder = null;
    byte[] utf8Bytes = null;

    int i = firstPercent;
    while (i < length) {
      char c = uri.charAt(i);
      if (c != '%') {
        if (Character.isHighSurrogate(c)) {
          if (i + 1 >= length || !Character.isLowSurrogate(uri.charAt(i + 1))) {
            throw new IllegalArgumentException(
                "Unpaired UTF-16 surrogate at index " + i + " in URI");
          }
          sb.append(c).append(uri.charAt(i + 1));
          i += 2;
        } else if (Character.isLowSurrogate(c)) {
          throw new IllegalArgumentException("Unpaired UTF-16 surrogate at index " + i + " in URI");
        } else {
          sb.append(c);
          i++;
        }
        continue;
      }

      int firstByte = decodePercentByte(uri, i);
      if (firstByte < 0x80) {
        sb.append((char) firstByte);
        i += 3;
        continue;
      }

      if (utf8Decoder == null) {
        utf8Decoder =
            StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        utf8Bytes = new byte[Math.max(4, Math.min(32, (length - i) / 3))];
      }

      int byteCount = 0;
      utf8Bytes[byteCount++] = (byte) firstByte;
      i += 3;

      while (i < length && uri.charAt(i) == '%') {
        int nextByte = decodePercentByte(uri, i);
        if (byteCount == utf8Bytes.length) {
          utf8Bytes = Arrays.copyOf(utf8Bytes, utf8Bytes.length * 2);
        }
        utf8Bytes[byteCount++] = (byte) nextByte;
        i += 3;
      }

      try {
        CharBuffer decoded = utf8Decoder.decode(ByteBuffer.wrap(utf8Bytes, 0, byteCount));
        sb.append(decoded);
      } catch (CharacterCodingException e) {
        throw new IllegalArgumentException(
            "Invalid UTF-8 percent-encoded sequence in URI: " + uri, e);
      }
    }

    return sb.toString();
  }

  private static void validateSurrogates(String uri, int start, int end) {
    for (int i = start; i < end; i++) {
      char c = uri.charAt(i);
      if (Character.isHighSurrogate(c)) {
        if (i + 1 >= end || !Character.isLowSurrogate(uri.charAt(i + 1))) {
          throw new IllegalArgumentException("Unpaired UTF-16 surrogate at index " + i + " in URI");
        }
        i++;
      } else if (Character.isLowSurrogate(c)) {
        throw new IllegalArgumentException("Unpaired UTF-16 surrogate at index " + i + " in URI");
      }
    }
  }

  private static boolean[] createAllowedUriChars() {
    var allowed = new boolean[128];

    // RFC 3986 §2.3 unreserved: ALPHA / DIGIT / "-" / "." / "_" / "~"
    for (char c = 'A'; c <= 'Z'; c++) allowed[c] = true;
    for (char c = 'a'; c <= 'z'; c++) allowed[c] = true;
    for (char c = '0'; c <= '9'; c++) allowed[c] = true;
    for (char c : "-._~".toCharArray()) allowed[c] = true;

    // RFC 3986 §2.2 gen-delims: ":" / "/" / "?" / "#" / "[" / "]" / "@"
    for (char c : ":/?#[]@".toCharArray()) allowed[c] = true;

    // RFC 3986 §2.2 sub-delims excluding ';' (OPC 10000-6 §5.1.12 requires ';' to be encoded):
    // "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / "="
    for (char c : "!$&'()*+,=".toCharArray()) allowed[c] = true;

    return allowed;
  }

  private static boolean isAllowedUriChar(char c) {
    return c < 128 && ALLOWED_URI_CHARS[c];
  }

  private static void appendPercentByte(StringBuilder sb, int b) {
    sb.append('%');
    sb.append(HEX_DIGITS[(b >>> 4) & 0x0F]);
    sb.append(HEX_DIGITS[b & 0x0F]);
  }

  private static int decodePercentByte(String uri, int index) {
    if (index + 2 >= uri.length()) {
      throw new IllegalArgumentException(
          "Truncated percent-escape at index " + index + " in URI: " + uri);
    }

    int high = hexValue(uri.charAt(index + 1));
    int low = hexValue(uri.charAt(index + 2));
    if (high < 0 || low < 0) {
      throw new IllegalArgumentException(
          "Invalid percent-escape '"
              + uri.substring(index, index + 3)
              + "' at index "
              + index
              + " in URI: "
              + uri);
    }

    return (high << 4) | low;
  }

  private static int hexValue(char c) {
    if (c >= '0' && c <= '9') {
      return c - '0';
    } else if (c >= 'A' && c <= 'F') {
      return c - 'A' + 10;
    } else if (c >= 'a' && c <= 'f') {
      return c - 'a' + 10;
    } else {
      return -1;
    }
  }
}
