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

import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class ArrayArguments {

  public static Stream<Arguments> booleanArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Boolean[] {false, true, false, true},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Boolean>false</uax:Boolean>
              <uax:Boolean>true</uax:Boolean>
              <uax:Boolean>false</uax:Boolean>
              <uax:Boolean>true</uax:Boolean>
            </Test>
            """),
        Arguments.of(
            new Boolean[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> sByteArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Byte[] {Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:SByte>-128</uax:SByte>
              <uax:SByte>0</uax:SByte>
              <uax:SByte>127</uax:SByte>
            </Test>
            """),
        Arguments.of(
            new Byte[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> int16ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Short[] {Short.MIN_VALUE, (short) 0, Short.MAX_VALUE},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Int16>-32768</uax:Int16>
              <uax:Int16>0</uax:Int16>
              <uax:Int16>32767</uax:Int16>
            </Test>
            """),
        Arguments.of(
            new Short[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> int32ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Integer[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Int32>-2147483648</uax:Int32>
              <uax:Int32>0</uax:Int32>
              <uax:Int32>2147483647</uax:Int32>
            </Test>
            """),
        Arguments.of(
            new Integer[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> int64ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Long[] {Long.MIN_VALUE, 0L, Long.MAX_VALUE},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Int64>-9223372036854775808</uax:Int64>
              <uax:Int64>0</uax:Int64>
              <uax:Int64>9223372036854775807</uax:Int64>
            </Test>
            """),
        Arguments.of(
            new Long[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> byteArrayArguments() {
    return Stream.of(
        Arguments.of(
            new UByte[] {UByte.MIN, UByte.valueOf(0), UByte.MAX},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Byte>0</uax:Byte>
              <uax:Byte>0</uax:Byte>
              <uax:Byte>255</uax:Byte>
            </Test>
            """),
        Arguments.of(
            new UByte[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> uInt16ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new UShort[] {UShort.MIN, UShort.valueOf(0), UShort.MAX},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:UInt16>0</uax:UInt16>
              <uax:UInt16>0</uax:UInt16>
              <uax:UInt16>65535</uax:UInt16>
            </Test>
            """),
        Arguments.of(
            new UShort[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> uInt32ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new UInteger[] {UInteger.MIN, UInteger.valueOf(0), UInteger.MAX},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:UInt32>0</uax:UInt32>
              <uax:UInt32>0</uax:UInt32>
              <uax:UInt32>4294967295</uax:UInt32>
            </Test>
            """),
        Arguments.of(
            new UInteger[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> uInt64ArrayArguments() {
    return Stream.of(
        Arguments.of(
            new ULong[] {ULong.MIN, ULong.valueOf(0), ULong.MAX},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:UInt64>0</uax:UInt64>
              <uax:UInt64>0</uax:UInt64>
              <uax:UInt64>18446744073709551615</uax:UInt64>
            </Test>
            """),
        Arguments.of(
            new ULong[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> floatArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Float[] {
              Float.MIN_VALUE,
              0.0f,
              Float.MAX_VALUE,
              Float.NEGATIVE_INFINITY,
              Float.POSITIVE_INFINITY,
              Float.NaN
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Float>1.4E-45</uax:Float>
              <uax:Float>0.0</uax:Float>
              <uax:Float>3.4028235E38</uax:Float>
              <uax:Float>-INF</uax:Float>
              <uax:Float>INF</uax:Float>
              <uax:Float>NaN</uax:Float>
            </Test>
            """),
        Arguments.of(
            new Float[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> doubleArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Double[] {
              Double.MIN_VALUE,
              0.0,
              Double.MAX_VALUE,
              Double.NEGATIVE_INFINITY,
              Double.POSITIVE_INFINITY,
              Double.NaN
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Double>4.9E-324</uax:Double>
              <uax:Double>0.0</uax:Double>
              <uax:Double>1.7976931348623157E308</uax:Double>
              <uax:Double>-INF</uax:Double>
              <uax:Double>INF</uax:Double>
              <uax:Double>NaN</uax:Double>
            </Test>
            """),
        Arguments.of(
            new Double[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> dataValueArrayArguments() {
    // Create DataValue instances for testing
    java.time.Instant sourceTimeInstant = java.time.Instant.parse("2020-01-01T00:00:00Z");
    java.time.Instant serverTimeInstant = java.time.Instant.parse("2020-01-02T00:00:00Z");

    DateTime sourceTime = new DateTime(sourceTimeInstant);
    DateTime serverTime = new DateTime(serverTimeInstant);
    UShort sourcePicoseconds = UShort.valueOf(1000);
    UShort serverPicoseconds = UShort.valueOf(2000);

    // DataValue with all fields non-null
    DataValue dataValue1 =
        new DataValue(
            Variant.of(true),
            new StatusCode(0x80000000L),
            sourceTime,
            sourcePicoseconds,
            serverTime,
            serverPicoseconds);

    // DataValue with good status code (should not appear in XML)
    DataValue dataValue2 =
        new DataValue(
            Variant.of(42),
            StatusCode.GOOD,
            sourceTime,
            sourcePicoseconds,
            serverTime,
            serverPicoseconds);

    return Stream.of(
        Arguments.of(
            new DataValue[] {dataValue1, dataValue2},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:DataValue>
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
              </uax:DataValue>
              <uax:DataValue>
                <uax:Value>
                  <uax:Value>
                    <uax:Int32>42</uax:Int32>
                  </uax:Value>
                </uax:Value>
                <uax:SourceTimestamp>2020-01-01T00:00:00Z</uax:SourceTimestamp>
                <uax:SourcePicoseconds>1000</uax:SourcePicoseconds>
                <uax:ServerTimestamp>2020-01-02T00:00:00Z</uax:ServerTimestamp>
                <uax:ServerPicoseconds>2000</uax:ServerPicoseconds>
              </uax:DataValue>
            </Test>
            """),
        Arguments.of(
            new DataValue[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> stringArrayArguments() {
    return Stream.of(
        Arguments.of(
            new String[] {"Hello, World!", "", "Test String"},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:String>Hello, World!</uax:String>
              <uax:String></uax:String>
              <uax:String>Test String</uax:String>
            </Test>
            """),
        Arguments.of(
            new String[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> dateTimeArrayArguments() {
    return Stream.of(
        Arguments.of(
            new DateTime[] {
              new DateTime(java.time.Instant.parse("2023-01-01T12:34:56Z")),
              DateTime.MIN_DATE_TIME,
              DateTime.MAX_DATE_TIME
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:DateTime>2023-01-01T12:34:56Z</uax:DateTime>
              <uax:DateTime>0001-01-01T00:00:00Z</uax:DateTime>
              <uax:DateTime>9999-12-31T23:59:59Z</uax:DateTime>
            </Test>
            """),
        Arguments.of(
            new DateTime[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> guidArrayArguments() {
    return Stream.of(
        Arguments.of(
            new UUID[] {UUID.fromString("12345678-1234-1234-1234-123456789012"), new UUID(0L, 0L)},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Guid>12345678-1234-1234-1234-123456789012</uax:Guid>
              <uax:Guid>00000000-0000-0000-0000-000000000000</uax:Guid>
            </Test>
            """),
        Arguments.of(
            new UUID[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> byteStringArrayArguments() {
    return Stream.of(
        Arguments.of(
            new ByteString[] {
              ByteString.of(new byte[] {1, 2, 3, 4}),
              ByteString.of(new byte[0]),
              ByteString.NULL_VALUE
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:ByteString>AQIDBA==</uax:ByteString>
              <uax:ByteString></uax:ByteString>
              <uax:ByteString></uax:ByteString>
            </Test>
            """),
        Arguments.of(
            new ByteString[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> xmlElementArrayArguments() {
    return Stream.of(
        Arguments.of(
            new XmlElement[] {
              XmlElement.of("<SimpleElement>Hello, World!</SimpleElement>"),
              XmlElement.of("<Empty></Empty>"),
              XmlElement.NULL_VALUE
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:XmlElement><SimpleElement>Hello, World!</SimpleElement></uax:XmlElement>
              <uax:XmlElement><Empty></Empty></uax:XmlElement>
              <uax:XmlElement></uax:XmlElement>
            </Test>
            """),
        Arguments.of(
            new XmlElement[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> nodeIdArrayArguments() {
    return Stream.of(
        Arguments.of(
            new NodeId[] {
              new NodeId(0, 123),
              new NodeId(1, "Hello"),
              new NodeId(2, UUID.fromString("12345678-1234-1234-1234-123456789012")),
              new NodeId(3, ByteString.of(new byte[] {1, 2, 3, 4}))
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:NodeId>
                <uax:Identifier>i=123</uax:Identifier>
              </uax:NodeId>
              <uax:NodeId>
                <uax:Identifier>ns=1;s=Hello</uax:Identifier>
              </uax:NodeId>
              <uax:NodeId>
                <uax:Identifier>ns=2;g=12345678-1234-1234-1234-123456789012</uax:Identifier>
              </uax:NodeId>
              <uax:NodeId>
                <uax:Identifier>ns=3;b=AQIDBA==</uax:Identifier>
              </uax:NodeId>
            </Test>
            """),
        Arguments.of(
            new NodeId[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> expandedNodeIdArrayArguments() {
    return Stream.of(
        Arguments.of(
            new ExpandedNodeId[] {
              ExpandedNodeId.of(0, 123L),
              ExpandedNodeId.of(UShort.valueOf(1), "Hello"),
              ExpandedNodeId.of(
                  UShort.valueOf(2), UUID.fromString("12345678-1234-1234-1234-123456789012")),
              ExpandedNodeId.of(UShort.valueOf(3), ByteString.of(new byte[] {1, 2, 3, 4})),
              ExpandedNodeId.of("http://example.org/UA/", 123L)
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:ExpandedNodeId>
                <uax:Identifier>i=123</uax:Identifier>
              </uax:ExpandedNodeId>
              <uax:ExpandedNodeId>
                <uax:Identifier>ns=1;s=Hello</uax:Identifier>
              </uax:ExpandedNodeId>
              <uax:ExpandedNodeId>
                <uax:Identifier>ns=2;g=12345678-1234-1234-1234-123456789012</uax:Identifier>
              </uax:ExpandedNodeId>
              <uax:ExpandedNodeId>
                <uax:Identifier>ns=3;b=AQIDBA==</uax:Identifier>
              </uax:ExpandedNodeId>
              <uax:ExpandedNodeId>
                <uax:Identifier>nsu=http://example.org/UA/;i=123</uax:Identifier>
              </uax:ExpandedNodeId>
            </Test>
            """),
        Arguments.of(
            new ExpandedNodeId[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> statusCodeArrayArguments() {
    return Stream.of(
        Arguments.of(
            new StatusCode[] {
              StatusCode.GOOD, StatusCode.BAD, new StatusCode(0x80340000L) // Bad_InvalidState
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:StatusCode>
                <uax:Code>0</uax:Code>
              </uax:StatusCode>
              <uax:StatusCode>
                <uax:Code>2147483648</uax:Code>
              </uax:StatusCode>
              <uax:StatusCode>
                <uax:Code>2150891520</uax:Code>
              </uax:StatusCode>
            </Test>
            """),
        Arguments.of(
            new StatusCode[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> qualifiedNameArrayArguments() {
    return Stream.of(
        Arguments.of(
            new QualifiedName[] {
              new QualifiedName(1, "TestName"),
              new QualifiedName(0, "DefaultNamespace"),
              new QualifiedName(2, ""),
              QualifiedName.NULL_VALUE
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:QualifiedName>
                <uax:NamespaceIndex>1</uax:NamespaceIndex>
                <uax:Name>TestName</uax:Name>
              </uax:QualifiedName>
              <uax:QualifiedName>
                <uax:NamespaceIndex>0</uax:NamespaceIndex>
                <uax:Name>DefaultNamespace</uax:Name>
              </uax:QualifiedName>
              <uax:QualifiedName>
                <uax:NamespaceIndex>2</uax:NamespaceIndex>
                <uax:Name></uax:Name>
              </uax:QualifiedName>
              <uax:QualifiedName>
                <uax:NamespaceIndex>0</uax:NamespaceIndex>
              </uax:QualifiedName>
            </Test>
            """),
        Arguments.of(
            new QualifiedName[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> localizedTextArrayArguments() {
    return Stream.of(
        Arguments.of(
            new LocalizedText[] {
              new LocalizedText("en-US", "Hello, World!"),
              new LocalizedText("en-US", ""),
              new LocalizedText("", "Hello, World!"),
              LocalizedText.NULL_VALUE
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:LocalizedText>
                <uax:Locale>en-US</uax:Locale>
                <uax:Text>Hello, World!</uax:Text>
              </uax:LocalizedText>
              <uax:LocalizedText>
                <uax:Locale>en-US</uax:Locale>
                <uax:Text></uax:Text>
              </uax:LocalizedText>
              <uax:LocalizedText>
                <uax:Locale></uax:Locale>
                <uax:Text>Hello, World!</uax:Text>
              </uax:LocalizedText>
              <uax:LocalizedText>
              </uax:LocalizedText>
            </Test>
            """),
        Arguments.of(
            new LocalizedText[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> extensionObjectArrayArguments() {
    return Stream.of(
        Arguments.of(
            new ExtensionObject[] {
              ExtensionObject.encode(
                  new DefaultEncodingContext(),
                  new Argument(
                      "name", NodeId.parse("i=1"), -1, null, LocalizedText.english("description")),
                  OpcUaDefaultXmlEncoding.getInstance()),
              ExtensionObject.encode(
                  new DefaultEncodingContext(),
                  new Argument(
                      "name2",
                      NodeId.parse("i=2"),
                      1,
                      new UInteger[] {UInteger.valueOf(1)},
                      LocalizedText.english("description2")),
                  OpcUaDefaultXmlEncoding.getInstance())
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:ExtensionObject>
                <uax:TypeId>
                  <uax:Identifier>i=297</uax:Identifier>
                </uax:TypeId>
                <uax:Body>
                  <uax:Argument>
                    <uax:Name>name</uax:Name>
                    <uax:DataType>
                      <uax:Identifier>i=1</uax:Identifier>
                    </uax:DataType>
                    <uax:ValueRank>-1</uax:ValueRank>
                    <uax:ArrayDimensions xsi:nil="true"></uax:ArrayDimensions>
                    <uax:Description>
                      <uax:Locale>en</uax:Locale>
                      <uax:Text>description</uax:Text>
                    </uax:Description>
                  </uax:Argument>
                </uax:Body>
              </uax:ExtensionObject>
              <uax:ExtensionObject>
                <uax:TypeId>
                  <uax:Identifier>i=297</uax:Identifier>
                </uax:TypeId>
                <uax:Body>
                  <uax:Argument>
                    <uax:Name>name2</uax:Name>
                    <uax:DataType>
                      <uax:Identifier>i=2</uax:Identifier>
                    </uax:DataType>
                    <uax:ValueRank>1</uax:ValueRank>
                    <uax:ArrayDimensions>
                      <uax:UInt32>1</uax:UInt32>
                    </uax:ArrayDimensions>
                    <uax:Description>
                      <uax:Locale>en</uax:Locale>
                      <uax:Text>description2</uax:Text>
                    </uax:Description>
                  </uax:Argument>
                </uax:Body>
              </uax:ExtensionObject>
            </Test>
            """),
        Arguments.of(
            new ExtensionObject[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> variantArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Variant[] {
              Variant.of(true),
              Variant.of(42),
              Variant.of("Hello, World!"),
              Variant.of(new Double[] {1.0, 2.0, 3.0})
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Variant>
                <uax:Value>
                  <uax:Boolean>true</uax:Boolean>
                </uax:Value>
              </uax:Variant>
              <uax:Variant>
                <uax:Value>
                  <uax:Int32>42</uax:Int32>
                </uax:Value>
              </uax:Variant>
              <uax:Variant>
                <uax:Value>
                  <uax:String>Hello, World!</uax:String>
                </uax:Value>
              </uax:Variant>
              <uax:Variant>
                <uax:Value>
                  <uax:ListOfDouble>
                    <uax:Double>1.0</uax:Double>
                    <uax:Double>2.0</uax:Double>
                    <uax:Double>3.0</uax:Double>
                  </uax:ListOfDouble>
                </uax:Value>
              </uax:Variant>
            </Test>
            """),
        Arguments.of(
            new Variant[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> diagnosticInfoArrayArguments() {
    DiagnosticInfo diagnosticInfo1 =
        new DiagnosticInfo(1, 2, 3, 4, "Additional Info", new StatusCode(5), null);

    DiagnosticInfo diagnosticInfo2 =
        new DiagnosticInfo(-1, -1, -1, -1, "Only Additional Info", null, null);

    DiagnosticInfo diagnosticInfo3 =
        new DiagnosticInfo(-1, -1, -1, -1, null, null, diagnosticInfo1);

    return Stream.of(
        Arguments.of(
            new DiagnosticInfo[] {diagnosticInfo1, diagnosticInfo2, diagnosticInfo3},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:DiagnosticInfo>
                <uax:SymbolicId>2</uax:SymbolicId>
                <uax:NamespaceUri>1</uax:NamespaceUri>
                <uax:Locale>3</uax:Locale>
                <uax:LocalizedText>4</uax:LocalizedText>
                <uax:AdditionalInfo>Additional Info</uax:AdditionalInfo>
                <uax:InnerStatusCode>
                  <uax:Code>5</uax:Code>
                </uax:InnerStatusCode>
              </uax:DiagnosticInfo>
              <uax:DiagnosticInfo>
                <uax:SymbolicId>-1</uax:SymbolicId>
                <uax:NamespaceUri>-1</uax:NamespaceUri>
                <uax:Locale>-1</uax:Locale>
                <uax:LocalizedText>-1</uax:LocalizedText>
                <uax:AdditionalInfo>Only Additional Info</uax:AdditionalInfo>
              </uax:DiagnosticInfo>
              <uax:DiagnosticInfo>
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
              </uax:DiagnosticInfo>
            </Test>
            """),
        Arguments.of(
            new DiagnosticInfo[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> enumArrayArguments() {
    return Stream.of(
        Arguments.of(
            new UaEnumeratedType[] {BrowseDirection.Forward, BrowseDirection.Inverse},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:BrowseDirection>Forward_0</uax:BrowseDirection>
              <uax:BrowseDirection>Inverse_1</uax:BrowseDirection>
            </Test>
            """),
        Arguments.of(
            new UaEnumeratedType[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }

  public static Stream<Arguments> structArrayArguments() {
    return Stream.of(
        Arguments.of(
            new XVType[] {
              new XVType(1.0, 2.0f), new XVType(3.0, 4.0f), new XVType(Double.NaN, Float.NaN)
            },
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:XVType>
                <uax:X>1.0</uax:X>
                <uax:Value>2.0</uax:Value>
              </uax:XVType>
              <uax:XVType>
                <uax:X>3.0</uax:X>
                <uax:Value>4.0</uax:Value>
              </uax:XVType>
              <uax:XVType>
                <uax:X>NaN</uax:X>
                <uax:Value>NaN</uax:Value>
              </uax:XVType>
            </Test>
            """),
        Arguments.of(
            new XVType[] {},
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
            </Test>
            """),
        Arguments.of(
            null,
            """
            <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
            """));
  }
}
