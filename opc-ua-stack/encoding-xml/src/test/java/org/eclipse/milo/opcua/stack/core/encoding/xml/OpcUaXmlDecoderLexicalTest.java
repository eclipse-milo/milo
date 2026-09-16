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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.time.Instant;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Part 6 §5.3.1 maps OPC UA builtin types to XML Schema types, so decoded text must follow the XML
 * Schema lexical and value spaces rather than Java parsing rules.
 */
class OpcUaXmlDecoderLexicalTest {

  private static final Function<OpcUaXmlDecoder, Object> SBYTE = d -> d.decodeSByte("V");
  private static final Function<OpcUaXmlDecoder, Object> BYTE = d -> d.decodeByte("V");
  private static final Function<OpcUaXmlDecoder, Object> INT16 = d -> d.decodeInt16("V");
  private static final Function<OpcUaXmlDecoder, Object> UINT16 = d -> d.decodeUInt16("V");
  private static final Function<OpcUaXmlDecoder, Object> INT32 = d -> d.decodeInt32("V");
  private static final Function<OpcUaXmlDecoder, Object> UINT32 = d -> d.decodeUInt32("V");
  private static final Function<OpcUaXmlDecoder, Object> INT64 = d -> d.decodeInt64("V");
  private static final Function<OpcUaXmlDecoder, Object> UINT64 = d -> d.decodeUInt64("V");
  private static final Function<OpcUaXmlDecoder, Object> FLOAT = d -> d.decodeFloat("V");
  private static final Function<OpcUaXmlDecoder, Object> DOUBLE = d -> d.decodeDouble("V");

  @Nested
  class Integers {

    @ParameterizedTest
    @MethodSource("validIntegers")
    void decodesSchemaIntegerForms(
        Function<OpcUaXmlDecoder, Object> read, String text, Object expected) throws Exception {
      assertEquals(expected, decode(text, read));
    }

    static Stream<Arguments> validIntegers() {
      return Stream.of(
          Arguments.of(INT32, " \t\r\n+42\n ", 42),
          Arguments.of(INT32, "007", 7),
          Arguments.of(SBYTE, "-128", (byte) -128),
          Arguments.of(SBYTE, "127", (byte) 127),
          Arguments.of(INT16, "-32768", Short.MIN_VALUE),
          Arguments.of(INT16, "32767", Short.MAX_VALUE),
          Arguments.of(INT32, "-2147483648", Integer.MIN_VALUE),
          Arguments.of(INT32, "2147483647", Integer.MAX_VALUE),
          Arguments.of(INT64, "-9223372036854775808", Long.MIN_VALUE),
          Arguments.of(INT64, "9223372036854775807", Long.MAX_VALUE),
          Arguments.of(BYTE, "255", ubyte(255)),
          Arguments.of(BYTE, "-0", ubyte(0)),
          Arguments.of(UINT16, "65535", ushort(65535)),
          Arguments.of(UINT32, "4294967295", uint(4294967295L)),
          Arguments.of(
              UINT64, "+18446744073709551615", ulong(new BigInteger("18446744073709551615"))),
          Arguments.of(UINT64, "-0", ulong(0L)));
    }

    // JAXB narrowed or wrapped these values instead of rejecting them.
    @ParameterizedTest
    @MethodSource("outOfRangeIntegers")
    void rejectsValuesOutsideTheTypeRange(Function<OpcUaXmlDecoder, Object> read, String text) {
      assertDecodingError(text, read);
    }

    static Stream<Arguments> outOfRangeIntegers() {
      return Stream.of(
          Arguments.of(SBYTE, "128"),
          Arguments.of(SBYTE, "-129"),
          Arguments.of(INT16, "32768"),
          Arguments.of(INT32, "2147483648"),
          Arguments.of(INT32, "-2147483649"),
          Arguments.of(INT64, "9223372036854775808"),
          Arguments.of(BYTE, "256"),
          Arguments.of(BYTE, "65536"),
          Arguments.of(BYTE, "-1"),
          Arguments.of(UINT16, "65536"),
          Arguments.of(UINT16, "4294967296"),
          Arguments.of(UINT32, "4294967296"),
          Arguments.of(UINT32, "-1"),
          Arguments.of(UINT64, "18446744073709551616"),
          Arguments.of(UINT64, "-1"));
    }

    // For SByte, Byte, Int16, UInt16, and Int32, JAXB skipped inner whitespace, accepted signs
    // anywhere, and read an empty value as 0.
    @ParameterizedTest
    @MethodSource("malformedIntegers")
    void rejectsMalformedIntegers(Function<OpcUaXmlDecoder, Object> read, String text) {
      assertDecodingError(text, read);
    }

    static Stream<Arguments> malformedIntegers() {
      return Stream.of(SBYTE, BYTE, INT16, UINT16, INT32, UINT32, INT64, UINT64)
          .flatMap(
              read ->
                  Stream.of(
                          "", " ", "+", "-", "1 2", "1-2", "--1", "+-1", "1.0", "1e3", "0x10", "١٢")
                      .map(text -> Arguments.of(read, text)));
    }

    @Test
    void decodesStatusCodeAsUInt32() throws Exception {
      try (var decoder = decoder("<V><Code> 2147483648 </Code></V>")) {
        assertEquals(new StatusCode(0x80000000L), decoder.decodeStatusCode("V"));
      }
      assertDecodingXmlError("<V><Code>4294967296</Code></V>", d -> d.decodeStatusCode("V"));
    }

    @Test
    void decodesQualifiedNameNamespaceIndexAsUInt16() throws Exception {
      try (var decoder = decoder("<V><NamespaceIndex> 0 </NamespaceIndex><Name>n</Name></V>")) {
        assertEquals(new QualifiedName(0, "n"), decoder.decodeQualifiedName("V"));
      }
      assertDecodingXmlError(
          "<V><NamespaceIndex>65536</NamespaceIndex><Name>n</Name></V>",
          d -> d.decodeQualifiedName("V"));
    }
  }

  @Nested
  class FloatingPoint {

    @ParameterizedTest
    @MethodSource("validDoubles")
    void decodesSchemaDoubleForms(String text, double expected) throws Exception {
      assertEquals(expected, decode(text, DOUBLE));
      assertEquals((float) expected, decode(text, FLOAT));
    }

    static Stream<Arguments> validDoubles() {
      return Stream.of(
          Arguments.of("INF", Double.POSITIVE_INFINITY),
          Arguments.of(" -INF\n", Double.NEGATIVE_INFINITY),
          Arguments.of("NaN", Double.NaN),
          Arguments.of("-0", -0.0),
          Arguments.of("+1.5E3", 1500.0),
          Arguments.of("1e-3", 0.001),
          Arguments.of(".5", 0.5),
          Arguments.of("5.", 5.0),
          Arguments.of("\t0012.250\r\n", 12.25));
    }

    // Java accepts these forms, but the XML Schema float and double types do not.
    @ParameterizedTest
    @ValueSource(
        strings = {
          "",
          "+INF",
          "inf",
          "nan",
          "Infinity",
          "0x1p3",
          "1.5f",
          "1d",
          "1e",
          "e3",
          ".",
          "1 2",
          "١"
        })
    void rejectsNonSchemaFloatingPointForms(String text) {
      assertDecodingError(text, DOUBLE);
      assertDecodingError(text, FLOAT);
    }
  }

  @Nested
  class Booleans {

    @ParameterizedTest
    @ValueSource(strings = {"true", "1", " true\n"})
    void decodesTrue(String text) throws Exception {
      assertEquals(true, decode(text, d -> d.decodeBoolean("V")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "0", "\tfalse "})
    void decodesFalse(String text) throws Exception {
      assertEquals(false, decode(text, d -> d.decodeBoolean("V")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "TRUE", "yes", "tru", "t", "2", "1 0"})
    void rejectsOtherBooleanForms(String text) {
      assertDecodingError(text, d -> d.decodeBoolean("V"));
    }
  }

  @Nested
  class ByteStrings {

    @ParameterizedTest
    @MethodSource("validBase64")
    void decodesPaddedBase64WithXmlWhitespace(String text, byte[] expected) throws Exception {
      ByteString value = (ByteString) decode(text, d -> d.decodeByteString("V"));
      assertArrayEquals(expected, value.bytesOrEmpty());
    }

    static Stream<Arguments> validBase64() {
      return Stream.of(
          Arguments.of("", new byte[0]),
          Arguments.of("AQID", new byte[] {1, 2, 3}),
          Arguments.of("AQ==", new byte[] {1}),
          Arguments.of("\n  AQI=\n", new byte[] {1, 2}),
          Arguments.of("AQID\r\n\tBAUG BwgJ", new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
    }

    // JAXB skipped unknown characters and dropped an unpadded final group.
    @ParameterizedTest
    @ValueSource(strings = {"AQ", "AQI", "AQ*D", "AQ-_", "AQ=D", "A===", "AQ==AQ=="})
    void rejectsMalformedBase64(String text) {
      assertDecodingError(text, d -> d.decodeByteString("V"));
    }
  }

  @Nested
  class DateTimes {

    @ParameterizedTest
    @MethodSource("validDateTimes")
    void decodesOffsetsAndFractionalSeconds(String text, String expected) throws Exception {
      assertEquals(new DateTime(Instant.parse(expected)), decode(text, d -> d.decodeDateTime("V")));
    }

    static Stream<Arguments> validDateTimes() {
      return Stream.of(
          // Part 6 §5.3.1.6 examples of equivalent values.
          Arguments.of("2002-10-10T00:00:00+05:00", "2002-10-09T19:00:00Z"),
          Arguments.of(" 2002-10-09T19:00:00Z\n", "2002-10-09T19:00:00Z"),
          Arguments.of("2002-10-09T16:30:00.5-02:30", "2002-10-09T19:00:00.5Z"),
          // DateTime has 100 ns resolution; finer digits are truncated.
          Arguments.of("2002-10-09T19:00:00.1234567Z", "2002-10-09T19:00:00.1234567Z"),
          Arguments.of("2002-10-09T19:00:00.123456789Z", "2002-10-09T19:00:00.1234567Z"),
          Arguments.of("0001-01-01T00:00:00Z", "0001-01-01T00:00:00Z"),
          Arguments.of("9999-12-31T23:59:59Z", "9999-12-31T23:59:59Z"));
    }

    // The encoder writes 100 ns precision, so decoding must not truncate to milliseconds.
    @Test
    void roundTripsSubMillisecondPrecision() throws Exception {
      var value = new DateTime(Instant.parse("2023-01-01T12:34:56.1234567Z"));
      String xml;
      try (var encoder = new OpcUaXmlEncoder(DefaultEncodingContext.INSTANCE)) {
        encoder.encodeDateTime("V", value);
        xml = encoder.getOutputString();
      }
      try (var decoder = decoder(xml)) {
        assertEquals(value, decoder.decodeDateTime("V"));
      }
    }

    @ParameterizedTest
    @ValueSource(
        strings = {"", "2002-10-09", "19:00:00Z", "2002-10-09 19:00:00Z", "2002-13-01T00:00:00Z"})
    void rejectsValuesThatAreNotSchemaDateTimes(String text) {
      assertDecodingError(text, d -> d.decodeDateTime("V"));
    }
  }

  private static Object decode(String text, Function<OpcUaXmlDecoder, Object> read)
      throws Exception {
    try (var decoder = decoder("<V>" + text + "</V>")) {
      return read.apply(decoder);
    }
  }

  private static void assertDecodingError(String text, Function<OpcUaXmlDecoder, Object> read) {
    assertDecodingXmlError("<V>" + text + "</V>", read);
  }

  private static void assertDecodingXmlError(String xml, Function<OpcUaXmlDecoder, Object> read) {
    var e =
        assertThrows(
            UaSerializationException.class,
            () -> {
              try (var decoder = decoder(xml)) {
                read.apply(decoder);
              }
            },
            () -> "accepted " + xml);
    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value(), xml);
  }

  private static OpcUaXmlDecoder decoder(String xml) throws Exception {
    return new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml);
  }
}
