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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class VariantArguments {

  public static Stream<Arguments> variantOfScalarArguments() {
    return Stream.of(
        // Boolean
        Arguments.of(
            Variant.of(false),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Boolean>false</uax:Boolean>
                  </uax:Value>
                </Test>
                """),

        // SByte
        Arguments.of(
            Variant.of((byte) 0),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:SByte>0</uax:SByte>
                  </uax:Value>
                </Test>
                """),

        // Byte
        Arguments.of(
            Variant.of(UByte.MIN),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Byte>0</uax:Byte>
                  </uax:Value>
                </Test>
                """),

        // Int16
        Arguments.of(
            Variant.of((short) 0),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Int16>0</uax:Int16>
                  </uax:Value>
                </Test>
                """),

        // UInt16
        Arguments.of(
            Variant.of(UShort.MIN),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:UInt16>0</uax:UInt16>
                  </uax:Value>
                </Test>
                """),

        // Int32
        Arguments.of(
            Variant.of(0),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Int32>0</uax:Int32>
                  </uax:Value>
                </Test>
                """),

        // UInt32
        Arguments.of(
            Variant.of(UInteger.MIN),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:UInt32>0</uax:UInt32>
                  </uax:Value>
                </Test>
                """),

        // Int64
        Arguments.of(
            Variant.of(0L),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Int64>0</uax:Int64>
                  </uax:Value>
                </Test>
                """),

        // UInt64
        Arguments.of(
            Variant.of(ULong.MIN),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:UInt64>0</uax:UInt64>
                  </uax:Value>
                </Test>
                """),

        // Float
        Arguments.of(
            Variant.of(0.0f),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Float>0.0</uax:Float>
                  </uax:Value>
                </Test>
                """),

        // Double
        Arguments.of(
            Variant.of(0.0),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Double>0.0</uax:Double>
                  </uax:Value>
                </Test>
                """),

        // String
        Arguments.of(
            Variant.of(""),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:String></uax:String>
                  </uax:Value>
                </Test>
                """),

        // DateTime
        Arguments.of(
            Variant.of(DateTime.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:DateTime>1601-01-01T00:00:00Z</uax:DateTime>
                  </uax:Value>
                </Test>
                """),

        // Guid
        Arguments.of(
            Variant.of(new UUID(0L, 0L)),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:Guid>00000000-0000-0000-0000-000000000000</uax:Guid>
                  </uax:Value>
                </Test>
                """),

        // ByteString
        Arguments.of(
            Variant.of(ByteString.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:ByteString></uax:ByteString>
                  </uax:Value>
                </Test>
                """),

        // XmlElement
        Arguments.of(
            Variant.of(XmlElement.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:XmlElement></uax:XmlElement>
                  </uax:Value>
                </Test>
                """),

        // NodeId
        Arguments.of(
            Variant.of(NodeId.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:NodeId>
                      <uax:Identifier>i=0</uax:Identifier>
                    </uax:NodeId>
                  </uax:Value>
                </Test>
                """),

        // ExpandedNodeId
        Arguments.of(
            Variant.of(ExpandedNodeId.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:ExpandedNodeId>
                      <uax:Identifier>i=0</uax:Identifier>
                    </uax:ExpandedNodeId>
                  </uax:Value>
                </Test>
                """),

        // StatusCode
        Arguments.of(
            Variant.of(StatusCode.GOOD),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:StatusCode>
                      <uax:Code>0</uax:Code>
                    </uax:StatusCode>
                  </uax:Value>
                </Test>
                """),

        // QualifiedName
        Arguments.of(
            Variant.of(QualifiedName.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:QualifiedName>
                      <uax:NamespaceIndex>0</uax:NamespaceIndex>
                    </uax:QualifiedName>
                  </uax:Value>
                </Test>
                """),

        // LocalizedText
        Arguments.of(
            Variant.of(LocalizedText.NULL_VALUE),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:LocalizedText>
                    </uax:LocalizedText>
                  </uax:Value>
                </Test>
                """),

        // DataValue
        Arguments.of(
            Variant.of(DataValue.valueOnly(Variant.NULL_VALUE)),
            """
                <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                  <uax:Value>
                    <uax:DataValue>
                    </uax:DataValue>
                  </uax:Value>
                </Test>
                """));
  }
}
