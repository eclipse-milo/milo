/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Converts XML Schema builtin type text to and from Java values.
 *
 * <p>Parsers accept leading and trailing XML whitespace and otherwise follow the XML Schema lexical
 * and value spaces. They throw {@link IllegalArgumentException} for text that is malformed or out
 * of range for the target type.
 */
final class XmlSchemaValues {

  private static final Pattern INTEGER = Pattern.compile("[+-]?[0-9]+");

  private static final Pattern DECIMAL_OR_SCIENTIFIC =
      Pattern.compile("[+-]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][+-]?[0-9]+)?");

  private static final BigInteger UINT64_MAX =
      BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);

  // The JDK implementation is stateless and avoids a service lookup on the application classpath.
  private static final DatatypeFactory DATATYPE_FACTORY = DatatypeFactory.newDefaultInstance();

  private XmlSchemaValues() {}

  /**
   * Parse an {@code xs:boolean}.
   *
   * @param text the lexical value.
   * @return the parsed value.
   */
  static boolean parseBoolean(String text) {
    return switch (trim(text)) {
      case "true", "1" -> true;
      case "false", "0" -> false;
      default -> throw new IllegalArgumentException("invalid xs:boolean: " + text);
    };
  }

  /**
   * Parse an XML Schema integer type whose value space fits in a {@code long}.
   *
   * @param text the lexical value.
   * @param min the smallest value of the target type.
   * @param max the largest value of the target type.
   * @return the parsed value, between {@code min} and {@code max}.
   */
  static long parseLong(String text, long min, long max) {
    String trimmed = requireInteger(trim(text));
    long value = Long.parseLong(trimmed);
    if (value < min || value > max) {
      throw new NumberFormatException("value out of range: " + trimmed);
    }
    return value;
  }

  /**
   * Parse an {@code xs:unsignedLong}.
   *
   * @param text the lexical value.
   * @return the parsed value, between 0 and 2<sup>64</sup>-1.
   */
  static BigInteger parseUnsignedLong(String text) {
    String trimmed = requireInteger(trim(text));
    BigInteger value = new BigInteger(trimmed);
    if (value.signum() < 0 || value.compareTo(UINT64_MAX) > 0) {
      throw new NumberFormatException("value out of range: " + trimmed);
    }
    return value;
  }

  /**
   * Parse an {@code xs:float}.
   *
   * @param text the lexical value.
   * @return the parsed value; magnitudes too large for a float become infinite.
   */
  static float parseFloat(String text) {
    String trimmed = trim(text);
    return switch (trimmed) {
      case "INF" -> Float.POSITIVE_INFINITY;
      case "-INF" -> Float.NEGATIVE_INFINITY;
      case "NaN" -> Float.NaN;
      default -> Float.parseFloat(requireDecimal(trimmed));
    };
  }

  /**
   * Parse an {@code xs:double}.
   *
   * @param text the lexical value.
   * @return the parsed value; magnitudes too large for a double become infinite.
   */
  static double parseDouble(String text) {
    String trimmed = trim(text);
    return switch (trimmed) {
      case "INF" -> Double.POSITIVE_INFINITY;
      case "-INF" -> Double.NEGATIVE_INFINITY;
      case "NaN" -> Double.NaN;
      default -> Double.parseDouble(requireDecimal(trimmed));
    };
  }

  static String printFloat(float value) {
    if (Float.isNaN(value)) {
      return "NaN";
    } else if (value == Float.POSITIVE_INFINITY) {
      return "INF";
    } else if (value == Float.NEGATIVE_INFINITY) {
      return "-INF";
    } else {
      return Float.toString(value);
    }
  }

  static String printDouble(double value) {
    if (Double.isNaN(value)) {
      return "NaN";
    } else if (value == Double.POSITIVE_INFINITY) {
      return "INF";
    } else if (value == Double.NEGATIVE_INFINITY) {
      return "-INF";
    } else {
      return Double.toString(value);
    }
  }

  /**
   * Parse an {@code xs:base64Binary}.
   *
   * <p>XML whitespace may appear anywhere in the text. The remaining characters must be padded
   * base64 using the standard alphabet.
   *
   * @param text the lexical value.
   * @return the decoded bytes.
   */
  static byte[] parseBase64Binary(String text) {
    String compact = removeWhitespace(text);
    if (compact.length() % 4 != 0) {
      throw new IllegalArgumentException("invalid xs:base64Binary length: " + compact.length());
    }
    return Base64.getDecoder().decode(compact);
  }

  static String printBase64Binary(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * Parse an {@code xs:dateTime}.
   *
   * <p>A value without a time zone is interpreted in the JVM default time zone. Fractional seconds
   * are retained to nanosecond precision.
   *
   * @param text the lexical value.
   * @return the instant identified by the value.
   */
  static Instant parseDateTime(String text) {
    XMLGregorianCalendar calendar = DATATYPE_FACTORY.newXMLGregorianCalendar(trim(text));
    if (!DatatypeConstants.DATETIME.equals(calendar.getXMLSchemaType())) {
      throw new IllegalArgumentException("invalid xs:dateTime: " + text);
    }

    Instant instant = calendar.toGregorianCalendar().toInstant();

    // GregorianCalendar keeps milliseconds only; restore the full fraction.
    BigDecimal fraction = calendar.getFractionalSecond();
    if (fraction != null) {
      instant = instant.with(ChronoField.NANO_OF_SECOND, fraction.movePointRight(9).intValue());
    }
    return instant;
  }

  // Java parsers also accept non-ASCII digits, hexadecimal floats, and type suffixes.
  private static String requireInteger(String trimmed) {
    if (!INTEGER.matcher(trimmed).matches()) {
      throw new NumberFormatException("invalid XML Schema integer: " + trimmed);
    }
    return trimmed;
  }

  private static String requireDecimal(String trimmed) {
    if (!DECIMAL_OR_SCIENTIFIC.matcher(trimmed).matches()) {
      throw new NumberFormatException("invalid XML Schema floating-point value: " + trimmed);
    }
    return trimmed;
  }

  private static String trim(String text) {
    int start = 0;
    int end = text.length();
    while (start < end && isWhitespace(text.charAt(start))) {
      start++;
    }
    while (end > start && isWhitespace(text.charAt(end - 1))) {
      end--;
    }
    return text.substring(start, end);
  }

  private static String removeWhitespace(String text) {
    var sb = new StringBuilder(text.length());
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (!isWhitespace(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private static boolean isWhitespace(char c) {
    return c == ' ' || c == '\t' || c == '\n' || c == '\r';
  }
}
