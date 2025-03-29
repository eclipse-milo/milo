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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OpcUaXmlDecoderTest {

  @Test
  void readVariantValue() throws Exception {
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

    assertNotNull(decoder.readVariantValue());
  }

  @ParameterizedTest
  @MethodSource("byteStringProvider")
  void decodeByteString(String xml, String fieldName, ByteString expectedByteString)
      throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    ByteString decodedByteString = decoder.decodeByteString(fieldName);
    assertEquals(expectedByteString, decodedByteString);
  }

  @ParameterizedTest
  @MethodSource("guidProvider")
  void decodeGuid(String xml, String fieldName, UUID expectedUuid) throws Exception {
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    UUID decodedUuid = decoder.decodeGuid(fieldName);
    assertEquals(expectedUuid, decodedUuid);
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

  static Stream<Arguments> guidProvider() {
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

  static Stream<Arguments> byteStringProvider() {
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
