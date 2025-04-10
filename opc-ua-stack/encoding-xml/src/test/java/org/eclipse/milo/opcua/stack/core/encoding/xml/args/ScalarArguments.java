/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml.args;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class ScalarArguments {

  public static Stream<Arguments> booleanArguments() {
    return Stream.of(
        Arguments.of(
            false,
            """
            <Test>false</Test>
            """),
        Arguments.of(
            true,
            """
            <Test>true</Test>
            """));
  }

  public static Stream<Arguments> sByteArguments() {
    return Stream.of(
        Arguments.of(
            Byte.MIN_VALUE,
            """
            <Test>-128</Test>
            """),
        Arguments.of(
            (byte) 0,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            Byte.MAX_VALUE,
            """
            <Test>127</Test>
            """));
  }

  public static Stream<Arguments> int16Arguments() {
    return Stream.of(
        Arguments.of(
            Short.MIN_VALUE,
            """
            <Test>-32768</Test>
            """),
        Arguments.of(
            (short) 0,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            Short.MAX_VALUE,
            """
            <Test>32767</Test>
            """));
  }

  public static Stream<Arguments> int32Arguments() {
    return Stream.of(
        Arguments.of(
            Integer.MIN_VALUE,
            """
            <Test>-2147483648</Test>
            """),
        Arguments.of(
            0,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            Integer.MAX_VALUE,
            """
            <Test>2147483647</Test>
            """));
  }

  public static Stream<Arguments> int64Arguments() {
    return Stream.of(
        Arguments.of(
            Long.MIN_VALUE,
            """
            <Test>-9223372036854775808</Test>
            """),
        Arguments.of(
            0L,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            Long.MAX_VALUE,
            """
            <Test>9223372036854775807</Test>
            """));
  }

  public static Stream<Arguments> byteArguments() {
    return Stream.of(
        Arguments.of(
            UByte.MIN,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            UByte.MAX,
            """
            <Test>255</Test>
            """));
  }

  public static Stream<Arguments> uInt16Arguments() {
    return Stream.of(
        Arguments.of(
            UShort.MIN,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            UShort.MAX,
            """
            <Test>65535</Test>
            """));
  }

  public static Stream<Arguments> uInt32Arguments() {
    return Stream.of(
        Arguments.of(
            UInteger.MIN,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            UInteger.MAX,
            """
            <Test>4294967295</Test>
            """));
  }

  public static Stream<Arguments> uInt64Arguments() {
    return Stream.of(
        Arguments.of(
            ULong.MIN,
            """
            <Test>0</Test>
            """),
        Arguments.of(
            ULong.MAX,
            """
            <Test>18446744073709551615</Test>
            """));
  }

  public static Stream<Arguments> nodeIdArguments() {
    return Stream.of(
        Arguments.of(
            new NodeId(0, 123),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>i=123</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(1, "Hello"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=1;s=Hello</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(2, UUID.fromString("12345678-1234-1234-1234-123456789012")),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=2;g=12345678-1234-1234-1234-123456789012</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(3, ByteString.of(new byte[] {1, 2, 3, 4})),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=3;b=AQIDBA==</uax:Identifier>
            </Test>
            """),
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> localizedTextArguments() {
    return Stream.of(
        // LocalizedText with both locale and text
        Arguments.of(
            new LocalizedText("en-US", "Hello, World!"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Locale>en-US</uax:Locale>
              <uax:Text>Hello, World!</uax:Text>
            </Test>
            """),
        // LocalizedText with only locale
        Arguments.of(
            new LocalizedText("en-US", ""),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Locale>en-US</uax:Locale>
            </Test>
            """),
        // LocalizedText with only text
        Arguments.of(
            new LocalizedText("", "Hello, World!"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Text>Hello, World!</uax:Text>
            </Test>
            """),
        // LocalizedText with null locale and text
        Arguments.of(
            LocalizedText.NULL_VALUE,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        // null LocalizedText
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> dateTimeArguments() {
    return Stream.of(
        // Normal DateTime
        Arguments.of(
            new DateTime(Instant.parse("2023-01-01T12:34:56Z")),
            """
            <Test>2023-01-01T12:34:56Z</Test>
            """),
        // MIN_DATE_TIME
        Arguments.of(
            DateTime.MIN_DATE_TIME,
            """
            <Test>0001-01-01T00:00:00Z</Test>
            """),
        // MAX_DATE_TIME
        Arguments.of(
            DateTime.MAX_DATE_TIME,
            """
            <Test>9999-12-31T23:59:59Z</Test>
            """),
        // NULL_VALUE
        Arguments.of(
            DateTime.NULL_VALUE,
            """
            <Test>1601-01-01T00:00:00Z</Test>
            """),
        // null value
        Arguments.of(
            null,
            """
            <Test></Test>
            """));
  }

  public static Stream<Arguments> guidArguments() {
    return Stream.of(
        // Normal UUID
        Arguments.of(
            UUID.fromString("12345678-1234-1234-1234-123456789012"),
            """
            <Test>12345678-1234-1234-1234-123456789012</Test>
            """),
        // Zero UUID
        Arguments.of(
            new UUID(0L, 0L),
            """
            <Test>00000000-0000-0000-0000-000000000000</Test>
            """),
        // null value
        Arguments.of(
            null,
            """
            <Test></Test>
            """));
  }

  public static Stream<Arguments> byteStringArguments() {
    return Stream.of(
        // Normal ByteString with data
        Arguments.of(
            ByteString.of(new byte[] {1, 2, 3, 4}),
            """
            <Test>AQIDBA==</Test>
            """),
        // Empty ByteString
        Arguments.of(
            ByteString.of(new byte[0]),
            """
            <Test></Test>
            """),
        // Null ByteString
        Arguments.of(
            ByteString.NULL_VALUE,
            """
            <Test></Test>
            """),
        // null value
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> expandedNodeIdArguments() {
    return Stream.of(
        // ExpandedNodeId with numeric identifier
        Arguments.of(
            ExpandedNodeId.of(0, 123L),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>i=123</uax:Identifier>
            </Test>
            """),
        // ExpandedNodeId with string identifier
        Arguments.of(
            ExpandedNodeId.of(UShort.valueOf(1), "Hello"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=1;s=Hello</uax:Identifier>
            </Test>
            """),
        // ExpandedNodeId with UUID identifier
        Arguments.of(
            ExpandedNodeId.of(
                UShort.valueOf(2), UUID.fromString("12345678-1234-1234-1234-123456789012")),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=2;g=12345678-1234-1234-1234-123456789012</uax:Identifier>
            </Test>
            """),
        // ExpandedNodeId with ByteString identifier
        Arguments.of(
            ExpandedNodeId.of(UShort.valueOf(3), ByteString.of(new byte[] {1, 2, 3, 4})),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=3;b=AQIDBA==</uax:Identifier>
            </Test>
            """),
        // ExpandedNodeId with namespace URI
        Arguments.of(
            ExpandedNodeId.of("http://example.org/UA/", 123L),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>nsu=http://example.org/UA/;i=123</uax:Identifier>
            </Test>
            """),
        // ExpandedNodeId with server index
        Arguments.of(
            new ExpandedNodeId(
                ExpandedNodeId.ServerReference.of(2),
                ExpandedNodeId.NamespaceReference.of(1),
                UInteger.valueOf(123)),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>svr=2;ns=1;i=123</uax:Identifier>
            </Test>
            """),
        // null ExpandedNodeId
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> statusCodeArguments() {
    return Stream.of(
        // Good status code
        Arguments.of(
            StatusCode.GOOD,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Code>0</uax:Code>
            </Test>
            """),
        // Bad status code
        Arguments.of(
            StatusCode.BAD,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Code>2147483648</uax:Code>
            </Test>
            """),
        // Specific status code
        Arguments.of(
            new StatusCode(StatusCodes.Bad_InternalError),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Code>2147614720</uax:Code>
            </Test>
            """),
        // null StatusCode
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> qualifiedNameArguments() {
    return Stream.of(
        // Normal QualifiedName with namespace index and name
        Arguments.of(
            new QualifiedName(1, "TestName"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:NamespaceIndex>1</uax:NamespaceIndex>
              <uax:Name>TestName</uax:Name>
            </Test>
            """),
        // QualifiedName with namespace index 0 (default namespace)
        Arguments.of(
            new QualifiedName(0, "DefaultNamespace"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:NamespaceIndex>0</uax:NamespaceIndex>
              <uax:Name>DefaultNamespace</uax:Name>
            </Test>
            """),
        // QualifiedName with empty name
        Arguments.of(
            new QualifiedName(2, ""),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:NamespaceIndex>2</uax:NamespaceIndex>
              <uax:Name></uax:Name>
            </Test>
            """),
        // NULL_VALUE QualifiedName
        Arguments.of(
            QualifiedName.NULL_VALUE,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:NamespaceIndex>0</uax:NamespaceIndex>
            </Test>
            """),
        // null QualifiedName
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> floatArguments() {
    return Stream.of(
        // Regular float value
        Arguments.of(
            3.14f,
            """
            <Test>3.14</Test>
            """),
        // Minimum value
        Arguments.of(
            Float.MIN_VALUE,
            """
            <Test>1.4E-45</Test>
            """),
        // Maximum value
        Arguments.of(
            Float.MAX_VALUE,
            """
            <Test>3.4028235E38</Test>
            """),
        // Negative infinity
        Arguments.of(
            Float.NEGATIVE_INFINITY,
            """
            <Test>-INF</Test>
            """),
        // Positive infinity
        Arguments.of(
            Float.POSITIVE_INFINITY,
            """
            <Test>INF</Test>
            """),
        // NaN
        Arguments.of(
            Float.NaN,
            """
            <Test>NaN</Test>
            """));
  }

  public static Stream<Arguments> doubleArguments() {
    return Stream.of(
        // Regular double value
        Arguments.of(
            3.14159265359,
            """
            <Test>3.14159265359</Test>
            """),
        // Minimum value
        Arguments.of(
            Double.MIN_VALUE,
            """
            <Test>4.9E-324</Test>
            """),
        // Maximum value
        Arguments.of(
            Double.MAX_VALUE,
            """
            <Test>1.7976931348623157E308</Test>
            """),
        // Negative infinity
        Arguments.of(
            Double.NEGATIVE_INFINITY,
            """
            <Test>-INF</Test>
            """),
        // Positive infinity
        Arguments.of(
            Double.POSITIVE_INFINITY,
            """
            <Test>INF</Test>
            """),
        // NaN
        Arguments.of(
            Double.NaN,
            """
            <Test>NaN</Test>
            """));
  }

  public static Stream<Arguments> extensionObjectArguments() {
    return Stream.of(
        // ExtensionObject containing XML-encoded XVType
        Arguments.of(
            ExtensionObject.encode(
                new DefaultEncodingContext(),
                new XVType(1.0, 2.0f),
                OpcUaDefaultXmlEncoding.getInstance()),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ua="http://opcfoundation.org/UA/">
              <uax:TypeId>
                <uax:Identifier>i=12082</uax:Identifier>
              </uax:TypeId>
              <uax:Body>
                <ua:XVType>
                  <ua:X>1.0</ua:X>
                  <ua:Value>2.0</ua:Value>
                </ua:XVType>
              </uax:Body>
            </Test>
            """),
        // ExtensionObject containing Binary-encoded XVType
        Arguments.of(
            ExtensionObject.encode(
                new DefaultEncodingContext(),
                new XVType(1.0, 2.0f),
                OpcUaDefaultBinaryEncoding.getInstance()),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:TypeId>
                <uax:Identifier>i=12090</uax:Identifier>
              </uax:TypeId>
              <uax:Body>
                <uax:ByteString>AAAAAAAA8D8AAABA</uax:ByteString>
              </uax:Body>
            </Test>
            """),
        // null ExtensionObject
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> dataValueArguments() {
    DateTime sourceTime = new DateTime(Instant.parse("2020-01-01T00:00:00Z"));
    DateTime serverTime = new DateTime(Instant.parse("2020-01-02T00:00:00Z"));
    UShort sourcePicoseconds = UShort.valueOf(1000);
    UShort serverPicoseconds = UShort.valueOf(2000);

    return Stream.of(
        // All fields non-null
        Arguments.of(
            new DataValue(
                Variant.of(true),
                new StatusCode(0x80000000L),
                sourceTime,
                sourcePicoseconds,
                serverTime,
                serverPicoseconds),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
              <uax:StatusCode>
                <uax:Code>2147483648</uax:Code>
              </uax:StatusCode>
              <uax:SourceTimestamp>2020-01-01T00:00:00Z</uax:SourceTimestamp>
              <uax:SourcePicoseconds>1000</uax:SourcePicoseconds>
              <uax:ServerTimestamp>2020-01-02T00:00:00Z</uax:ServerTimestamp>
              <uax:ServerPicoseconds>2000</uax:ServerPicoseconds>
            </Test>
            """),
        // Null source timestamp and picoseconds
        Arguments.of(
            new DataValue(
                Variant.of(true),
                new StatusCode(0x80000000L),
                null,
                null,
                serverTime,
                serverPicoseconds),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
              <uax:StatusCode>
                <uax:Code>2147483648</uax:Code>
              </uax:StatusCode>
              <uax:ServerTimestamp>2020-01-02T00:00:00Z</uax:ServerTimestamp>
              <uax:ServerPicoseconds>2000</uax:ServerPicoseconds>
            </Test>
            """),
        // Null server timestamp and picoseconds
        Arguments.of(
            new DataValue(
                Variant.of(true),
                new StatusCode(0x80000000L),
                sourceTime,
                sourcePicoseconds,
                null,
                null),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
              <uax:StatusCode>
                <uax:Code>2147483648</uax:Code>
              </uax:StatusCode>
              <uax:SourceTimestamp>2020-01-01T00:00:00Z</uax:SourceTimestamp>
              <uax:SourcePicoseconds>1000</uax:SourcePicoseconds>
            </Test>
            """),
        // Null source and server picoseconds
        Arguments.of(
            new DataValue(
                Variant.of(true), new StatusCode(0x80000000L), sourceTime, null, serverTime, null),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
              <uax:StatusCode>
                <uax:Code>2147483648</uax:Code>
              </uax:StatusCode>
              <uax:SourceTimestamp>2020-01-01T00:00:00Z</uax:SourceTimestamp>
              <uax:ServerTimestamp>2020-01-02T00:00:00Z</uax:ServerTimestamp>
            </Test>
            """),
        // Good status code (should not appear in XML)
        Arguments.of(
            new DataValue(
                Variant.of(true),
                StatusCode.GOOD,
                sourceTime,
                sourcePicoseconds,
                serverTime,
                serverPicoseconds),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
              <uax:SourceTimestamp>2020-01-01T00:00:00Z</uax:SourceTimestamp>
              <uax:SourcePicoseconds>1000</uax:SourcePicoseconds>
              <uax:ServerTimestamp>2020-01-02T00:00:00Z</uax:ServerTimestamp>
              <uax:ServerPicoseconds>2000</uax:ServerPicoseconds>
            </Test>
            """),
        // Only value
        Arguments.of(
            new DataValue(Variant.of(true), StatusCode.GOOD, null, null, null, null),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Value>
            </Test>
            """),
        // Null DataValue
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> stringArguments() {
    return Stream.of(
        // Regular string
        Arguments.of(
            "Hello, World!",
            """
            <Test>Hello, World!</Test>
            """),
        // Empty string
        Arguments.of(
            "",
            """
            <Test></Test>
            """),
        // null string
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> xmlElementArguments() {
    return Stream.of(
        // Normal XmlElement with a simple XML fragment
        Arguments.of(
            XmlElement.of("<SimpleElement>Hello, World!</SimpleElement>"),
            """
            <Test><SimpleElement>Hello, World!</SimpleElement></Test>
            """),
        // XmlElement with a complex XML fragment
        Arguments.of(
            XmlElement.of(
                "<ComplexElement><Child1>Value1</Child1><Child2>Value2</Child2></ComplexElement>"),
            """
            <Test><ComplexElement><Child1>Value1</Child1><Child2>Value2</Child2></ComplexElement></Test>
            """),
        // XmlElement with an empty element
        Arguments.of(
            XmlElement.of("<Empty></Empty>"),
            """
            <Test><Empty></Empty></Test>
            """),
        // NULL_VALUE XmlElement
        Arguments.of(
            XmlElement.NULL_VALUE,
            """
            <Test></Test>
            """),
        // null XmlElement
        Arguments.of(null, ""));
  }

  public static Stream<Arguments> diagnosticInfoArguments() {
    DiagnosticInfo diagnosticInfo1 =
        new DiagnosticInfo(1, 2, 3, 4, "Additional Info", new StatusCode(5), null);

    DiagnosticInfo diagnosticInfo2 =
        new DiagnosticInfo(-1, -1, -1, -1, "Only Additional Info", null, null);

    DiagnosticInfo diagnosticInfo3 =
        new DiagnosticInfo(-1, -1, -1, -1, null, null, diagnosticInfo1);

    return Stream.of(
        // DiagnosticInfo with all fields
        Arguments.of(
            diagnosticInfo1,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:SymbolicId>2</uax:SymbolicId>
              <uax:NamespaceUri>1</uax:NamespaceUri>
              <uax:Locale>3</uax:Locale>
              <uax:LocalizedText>4</uax:LocalizedText>
              <uax:AdditionalInfo>Additional Info</uax:AdditionalInfo>
              <uax:InnerStatusCode>
                <uax:Code>5</uax:Code>
              </uax:InnerStatusCode>
            </Test>
            """),
        // DiagnosticInfo with only AdditionalInfo
        Arguments.of(
            diagnosticInfo2,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:SymbolicId>-1</uax:SymbolicId>
              <uax:NamespaceUri>-1</uax:NamespaceUri>
              <uax:Locale>-1</uax:Locale>
              <uax:LocalizedText>-1</uax:LocalizedText>
              <uax:AdditionalInfo>Only Additional Info</uax:AdditionalInfo>
            </Test>
            """),
        // DiagnosticInfo with nested DiagnosticInfo
        Arguments.of(
            diagnosticInfo3,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:SymbolicId>-1</uax:SymbolicId>
              <uax:NamespaceUri>-1</uax:NamespaceUri>
              <uax:Locale>-1</uax:Locale>
              <uax:LocalizedText>-1</uax:LocalizedText>
              <uax:InnerDiagnosticInfo>
                <uax:SymbolicId>2</uax:SymbolicId>
                <uax:NamespaceUri>1</uax:NamespaceUri>
                <uax:Locale>3</uax:Locale>
                <uax:LocalizedText>4</uax:LocalizedText>
                <uax:AdditionalInfo>Additional Info</uax:AdditionalInfo>
                <uax:InnerStatusCode>
                  <uax:Code>5</uax:Code>
                </uax:InnerStatusCode>
              </uax:InnerDiagnosticInfo>
            </Test>
            """),
        // NULL_VALUE DiagnosticInfo
        Arguments.of(
            DiagnosticInfo.NULL_VALUE,
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:SymbolicId>-1</uax:SymbolicId>
              <uax:NamespaceUri>-1</uax:NamespaceUri>
              <uax:Locale>-1</uax:Locale>
              <uax:LocalizedText>-1</uax:LocalizedText>
            </Test>
            """),
        // null DiagnosticInfo
        Arguments.of(null, ""));
  }
}
