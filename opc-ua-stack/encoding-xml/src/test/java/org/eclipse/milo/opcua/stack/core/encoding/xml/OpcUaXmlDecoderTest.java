/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OpcUaXmlDecoderTest {

  @ParameterizedTest
  @MethodSource("decodeBooleanProvider")
  void decodeBoolean(String xml, String fieldName, Boolean expectedBoolean) throws Exception {
    var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml);

    Boolean decodedBoolean = decoder.decodeBoolean(fieldName);
    assertEquals(expectedBoolean, decodedBoolean);
  }

  @ParameterizedTest
  @MethodSource("decodeSByteProvider")
  void decodeSByte(String xml, String fieldName, Byte expectedSByte) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Byte decodedSByte = decoder.decodeSByte(fieldName);
    assertEquals(expectedSByte, decodedSByte);
  }

  @ParameterizedTest
  @MethodSource("decodeByteProvider")
  void decodeByte(String xml, String fieldName, UByte expectedByte) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    UByte decodedByte = decoder.decodeByte(fieldName);
    assertEquals(expectedByte, decodedByte);
  }

  @ParameterizedTest
  @MethodSource("decodeInt16Provider")
  void decodeInt16(String xml, String fieldName, Short expectedInt16) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Short decodedInt16 = decoder.decodeInt16(fieldName);
    assertEquals(expectedInt16, decodedInt16);
  }

  @ParameterizedTest
  @MethodSource("decodeUInt16Provider")
  void decodeUInt16(String xml, String fieldName, UShort expectedUInt16) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    UShort decodedUInt16 = decoder.decodeUInt16(fieldName);
    assertEquals(expectedUInt16, decodedUInt16);
  }

  @ParameterizedTest
  @MethodSource("decodeInt32Provider")
  void decodeInt32(String xml, String fieldName, Integer expectedInt32) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Integer decodedInt32 = decoder.decodeInt32(fieldName);
    assertEquals(expectedInt32, decodedInt32);
  }

  @ParameterizedTest
  @MethodSource("decodeUInt32Provider")
  void decodeUInt32(String xml, String fieldName, UInteger expectedUInt32) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    UInteger decodedUInt32 = decoder.decodeUInt32(fieldName);
    assertEquals(expectedUInt32, decodedUInt32);
  }

  @ParameterizedTest
  @MethodSource("decodeInt64Provider")
  void decodeInt64(String xml, String fieldName, Long expectedInt64) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Long decodedInt64 = decoder.decodeInt64(fieldName);
    assertEquals(expectedInt64, decodedInt64);
  }

  @ParameterizedTest
  @MethodSource("decodeUInt64Provider")
  void decodeUInt64(String xml, String fieldName, ULong expectedUInt64) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    ULong decodedUInt64 = decoder.decodeUInt64(fieldName);
    assertEquals(expectedUInt64, decodedUInt64);
  }

  @ParameterizedTest
  @MethodSource("decodeFloatProvider")
  void decodeFloat(String xml, String fieldName, Float expectedFloat) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Float decodedFloat = decoder.decodeFloat(fieldName);
    assertEquals(expectedFloat, decodedFloat);
  }

  @ParameterizedTest
  @MethodSource("decodeDoubleProvider")
  void decodeDouble(String xml, String fieldName, Double expectedDouble) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Double decodedDouble = decoder.decodeDouble(fieldName);
    assertEquals(expectedDouble, decodedDouble);
  }

  @ParameterizedTest
  @MethodSource("decodeGuidProvider")
  void decodeGuid(String xml, String fieldName, UUID expectedUuid) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    UUID decodedUuid = decoder.decodeGuid(fieldName);
    assertEquals(expectedUuid, decodedUuid);
  }

  @ParameterizedTest
  @MethodSource("decodeByteStringProvider")
  void decodeByteString(String xml, String fieldName, ByteString expectedByteString)
      throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    ByteString decodedByteString = decoder.decodeByteString(fieldName);
    assertEquals(expectedByteString, decodedByteString);
  }

  // Golden encoder fixtures independently protect all six decoded DataValue components.
  @ParameterizedTest
  @MethodSource("dataValueGoldenArguments")
  void decodeDataValueGoldenXml(DataValue expected, String xml) throws Exception {
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml)) {
      assertEquals(expected, decoder.decodeDataValue("Test"));
    }
  }

  static Stream<Arguments> dataValueGoldenArguments() {
    return org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments.dataValueArguments()
        .filter(arguments -> arguments.get()[0] != null);
  }

  @Test
  void decodeVariantValue() throws Exception {
    String xml =
"""
<ListOfExtensionObject xmlns="http://opcfoundation.org/UA/2008/02/Types.xsd"\
 xmlns:xsd="http://www.w3.org/2001/XMLSchema"\
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><ExtensionObject><TypeId><Identifier>i=297</Identifier>
         \
 </TypeId><Body><Argument><Name>BreakLockStatus</Name><DataType><Identifier>i=6</Identifier>
              </DataType><ValueRank>-1</ValueRank><ArrayDimensions/><Description\
 xmlns:p5="http://www.w3.org/2001/XMLSchema-instance" p5:nil="true"/>
            </Argument>
          </Body>
        </ExtensionObject>
      </ListOfExtensionObject>
""";

    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    var values = assertInstanceOf(ExtensionObject[].class, decoder.decodeVariantValue());
    assertEquals(1, values.length);
    var argument =
        assertInstanceOf(Argument.class, values[0].decode(DefaultEncodingContext.INSTANCE));
    assertEquals("BreakLockStatus", argument.getName());
    assertEquals(NodeIds.Int32, argument.getDataType());
    assertEquals(-1, argument.getValueRank());
    assertNotNull(argument.getArrayDimensions());
    assertEquals(0, argument.getArrayDimensions().length);
    assertEquals(LocalizedText.NULL_VALUE, argument.getDescription());
  }

  @Test
  void decodeEmptyEnumArray() throws Exception {
    String xml = "<TestEnumArray/>";

    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Integer[] values = decoder.decodeEnumArray("TestEnumArray");
    assertNotNull(values);
    assertEquals(0, values.length);
  }

  @Test
  void decodeEmptyStructArray() throws Exception {
    String xml = "<TestStructArray/>";

    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    Object[] values = decoder.decodeStructArray("TestStructArray", NodeIds.XVType);
    assertNotNull(values);
    assertEquals(0, values.length);
  }

  static Stream<Arguments> decodeBooleanProvider() {
    String trueXml =
        "<Boolean xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">true</Boolean>";

    String falseXml =
        "<Boolean xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">false</Boolean>";

    String whitespaceXml =
        """
        <Boolean xmlns="http://opcfoundation.org/UA/2008/02/Types.xsd">
            true   \s
        </Boolean>
        """;

    return Stream.of(
        Arguments.of(trueXml, "Boolean", true),
        Arguments.of(falseXml, "Boolean", false),
        Arguments.of(whitespaceXml, "Boolean", true),
        Arguments.of(trueXml, "NonMatchingField", false));
  }

  static Stream<Arguments> decodeSByteProvider() {
    // Test values
    byte minValue = Byte.MIN_VALUE;
    byte maxValue = Byte.MAX_VALUE;
    byte zeroValue = 0;
    byte defaultValue = 0;

    // XML strings
    String minXml =
        "<SByte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</SByte>";

    String maxXml =
        "<SByte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</SByte>";

    String zeroXml =
        "<SByte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</SByte>";

    String whitespaceXml =
        "<SByte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</SByte>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "SByte", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "SByte", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "SByte", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "SByte", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeByteProvider() {
    // Test values
    UByte minValue = UByte.MIN;
    UByte maxValue = UByte.MAX;
    UByte zeroValue = UByte.valueOf(0);
    UByte defaultValue = UByte.MIN;

    // XML strings
    String minXml =
        "<Byte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Byte>";

    String maxXml =
        "<Byte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Byte>";

    String zeroXml =
        "<Byte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</Byte>";

    String whitespaceXml =
        "<Byte xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Byte>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Byte", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Byte", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Byte", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Byte", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeInt16Provider() {
    // Test values
    short minValue = Short.MIN_VALUE;
    short maxValue = Short.MAX_VALUE;
    short zeroValue = 0;
    short defaultValue = 0;

    // XML strings
    String minXml =
        "<Int16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Int16>";

    String maxXml =
        "<Int16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Int16>";

    String zeroXml =
        "<Int16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</Int16>";

    String whitespaceXml =
        "<Int16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Int16>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Int16", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Int16", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Int16", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Int16", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeUInt16Provider() {
    // Test values
    UShort minValue = UShort.MIN;
    UShort maxValue = UShort.MAX;
    UShort zeroValue = UShort.valueOf(0);
    UShort defaultValue = UShort.MIN;

    // XML strings
    String minXml =
        "<UInt16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</UInt16>";

    String maxXml =
        "<UInt16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</UInt16>";

    String zeroXml =
        "<UInt16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + zeroValue
            + "</UInt16>";

    String whitespaceXml =
        "<UInt16 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</UInt16>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "UInt16", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "UInt16", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "UInt16", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "UInt16", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeInt32Provider() {
    // Test values
    int minValue = Integer.MIN_VALUE;
    int maxValue = Integer.MAX_VALUE;
    int zeroValue = 0;
    int defaultValue = 0;

    // XML strings
    String minXml =
        "<Int32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Int32>";

    String maxXml =
        "<Int32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Int32>";

    String zeroXml =
        "<Int32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</Int32>";

    String whitespaceXml =
        "<Int32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Int32>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Int32", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Int32", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Int32", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Int32", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeUInt32Provider() {
    // Test values
    UInteger minValue = UInteger.MIN;
    UInteger maxValue = UInteger.MAX;
    UInteger zeroValue = UInteger.valueOf(0);
    UInteger defaultValue = UInteger.MIN;

    // XML strings
    String minXml =
        "<UInt32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</UInt32>";

    String maxXml =
        "<UInt32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</UInt32>";

    String zeroXml =
        "<UInt32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + zeroValue
            + "</UInt32>";

    String whitespaceXml =
        "<UInt32 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</UInt32>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "UInt32", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "UInt32", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "UInt32", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "UInt32", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeInt64Provider() {
    // Test values
    long minValue = Long.MIN_VALUE;
    long maxValue = Long.MAX_VALUE;
    long zeroValue = 0L;
    long defaultValue = 0L;

    // XML strings
    String minXml =
        "<Int64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Int64>";

    String maxXml =
        "<Int64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Int64>";

    String zeroXml =
        "<Int64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</Int64>";

    String whitespaceXml =
        "<Int64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Int64>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Int64", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Int64", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Int64", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Int64", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeUInt64Provider() {
    // Test values
    ULong minValue = ULong.MIN;
    ULong maxValue = ULong.MAX;
    ULong zeroValue = ULong.valueOf(0);
    ULong defaultValue = ULong.MIN;

    // XML strings
    String minXml =
        "<UInt64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</UInt64>";

    String maxXml =
        "<UInt64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</UInt64>";

    String zeroXml =
        "<UInt64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + zeroValue
            + "</UInt64>";

    String whitespaceXml =
        "<UInt64 xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</UInt64>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "UInt64", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "UInt64", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "UInt64", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "UInt64", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeFloatProvider() {
    // Test values
    float minValue = Float.MIN_VALUE;
    float maxValue = Float.MAX_VALUE;
    float zeroValue = 0.0f;
    float defaultValue = 0.0f;

    // XML strings
    String minXml =
        "<Float xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Float>";

    String maxXml =
        "<Float xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Float>";

    String zeroXml =
        "<Float xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + zeroValue + "</Float>";

    String whitespaceXml =
        "<Float xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Float>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Float", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Float", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Float", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Float", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeDoubleProvider() {
    // Test values
    double minValue = Double.MIN_VALUE;
    double maxValue = Double.MAX_VALUE;
    double zeroValue = 0.0;
    double defaultValue = 0.0;

    // XML strings
    String minXml =
        "<Double xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + minValue + "</Double>";

    String maxXml =
        "<Double xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + maxValue + "</Double>";

    String zeroXml =
        "<Double xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + zeroValue
            + "</Double>";

    String whitespaceXml =
        "<Double xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + zeroValue
            + "    \n"
            + "</Double>";

    return Stream.of(
        // Test case 1: Minimum value
        Arguments.of(minXml, "Double", minValue),

        // Test case 2: Maximum value
        Arguments.of(maxXml, "Double", maxValue),

        // Test case 3: Zero value
        Arguments.of(zeroXml, "Double", zeroValue),

        // Test case 4: XML with whitespace
        Arguments.of(whitespaceXml, "Double", zeroValue),

        // Test case 5: Non-matching field name
        Arguments.of(zeroXml, "NonMatchingField", defaultValue));
  }

  static Stream<Arguments> decodeGuidProvider() {
    UUID expectedUuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    UUID zeroUuid = new UUID(0L, 0L);

    String standardXml =
        "<Guid xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + expectedUuid + "</Guid>";

    String whitespaceXml =
        "<Guid xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">\n"
            + "    "
            + expectedUuid
            + "    \n"
            + "</Guid>";

    return Stream.of(
        // Test case 1: Standard XML with a matching field name
        Arguments.of(standardXml, "Guid", expectedUuid),

        // Test case 2: Standard XML with a non-matching field name
        Arguments.of(standardXml, "NonMatchingField", zeroUuid),

        // Test case 3: XML with whitespace around the UUID
        Arguments.of(whitespaceXml, "Guid", expectedUuid));
  }

  static Stream<Arguments> decodeByteStringProvider() {
    // Test data
    byte[] testBytes = {1, 2, 3, 4, 5};
    ByteString testByteString = ByteString.of(testBytes);
    ByteString nullByteString = ByteString.NULL_VALUE;

    // Base64 encoded test bytes
    String base64TestBytes = Base64.getEncoder().encodeToString(testBytes);

    // XML strings
    String standardXml =
        "<ByteString xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + base64TestBytes
            + "</ByteString>";

    String emptyXml =
        "<ByteString xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">" + "</ByteString>";

    String whitespaceXml =
        "<ByteString xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
            + "  "
            + "</ByteString>";

    return Stream.of(
        // Test case 1: Standard XML with non-empty content
        Arguments.of(standardXml, "ByteString", testByteString),

        // Test case 2: XML with empty content
        Arguments.of(emptyXml, "ByteString", nullByteString),

        // Test case 3: XML with only whitespace content
        Arguments.of(whitespaceXml, "ByteString", nullByteString),

        // Test case 4: Standard XML with non-matching field name
        Arguments.of(standardXml, "NonMatchingField", nullByteString));
  }
}
