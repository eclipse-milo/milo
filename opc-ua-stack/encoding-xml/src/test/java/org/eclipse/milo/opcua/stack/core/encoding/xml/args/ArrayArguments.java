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

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
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
}
