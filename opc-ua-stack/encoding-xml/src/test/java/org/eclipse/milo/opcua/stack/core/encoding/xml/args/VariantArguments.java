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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.AttributeWriteMask;
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
            """),

        // Enum (NodeClass)
        Arguments.of(
            Variant.of(NodeClass.Variable),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:Int32>2</uax:Int32>
              </uax:Value>
            </Test>
            """),

        // Struct (Argument)
        Arguments.of(
            Variant.of(
                ExtensionObject.encode(
                    new DefaultEncodingContext(),
                    new Argument(
                        "name",
                        NodeId.parse("i=1"),
                        -1,
                        null,
                        LocalizedText.english("description")),
                    Argument.XML_ENCODING_ID,
                    OpcUaDefaultXmlEncoding.getInstance())),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:Value>
                <uax:ExtensionObject>
                  <uax:TypeId>
                    <uax:Identifier>i=297</uax:Identifier>
                  </uax:TypeId>
                  <uax:Body>
                    <Argument>
                      <Name>name</Name>
                      <DataType>
                        <uax:Identifier>i=1</uax:Identifier>
                      </DataType>
                      <ValueRank>-1</ValueRank>
                      <ArrayDimensions xsi:nil="true"></ArrayDimensions>
                      <Description>
                        <uax:Locale>en</uax:Locale>
                        <uax:Text>description</uax:Text>
                      </Description>
                    </Argument>
                  </uax:Body>
                </uax:ExtensionObject>
              </uax:Value>
            </Test>
            """),

        // OptionSet (AttributeWriteMask)
        Arguments.of(
            Variant.of(AttributeWriteMask.of(AttributeWriteMask.Field.DisplayName)),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:UInt32>64</uax:UInt32>
              </uax:Value>
            </Test>
            """));
  }

  public static Stream<Arguments> variantOfArrayArguments() {
    return Stream.of(
        // Boolean array
        Arguments.of(
            Variant.ofBooleanArray(new Boolean[] {false, true, false, true}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfBoolean>
                  <uax:Boolean>false</uax:Boolean>
                  <uax:Boolean>true</uax:Boolean>
                  <uax:Boolean>false</uax:Boolean>
                  <uax:Boolean>true</uax:Boolean>
                </uax:ListOfBoolean>
              </uax:Value>
            </Test>
            """),

        // SByte array
        Arguments.of(
            Variant.of(new Byte[] {0, 0}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfSByte>
                  <uax:SByte>0</uax:SByte>
                  <uax:SByte>0</uax:SByte>
                </uax:ListOfSByte>
              </uax:Value>
            </Test>
            """),

        // Byte array
        Arguments.of(
            Variant.of(new UByte[] {UByte.MIN, UByte.MIN}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfByte>
                  <uax:Byte>0</uax:Byte>
                  <uax:Byte>0</uax:Byte>
                </uax:ListOfByte>
              </uax:Value>
            </Test>
            """),

        // Int16 array
        Arguments.of(
            Variant.of(new Short[] {0, 0}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfInt16>
                  <uax:Int16>0</uax:Int16>
                  <uax:Int16>0</uax:Int16>
                </uax:ListOfInt16>
              </uax:Value>
            </Test>
            """),

        // UInt16 array
        Arguments.of(
            Variant.of(new UShort[] {UShort.MIN, UShort.MIN}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfUInt16>
                  <uax:UInt16>0</uax:UInt16>
                  <uax:UInt16>0</uax:UInt16>
                </uax:ListOfUInt16>
              </uax:Value>
            </Test>
            """),

        // Int32 array
        Arguments.of(
            Variant.of(new Integer[] {0, 0}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfInt32>
                  <uax:Int32>0</uax:Int32>
                  <uax:Int32>0</uax:Int32>
                </uax:ListOfInt32>
              </uax:Value>
            </Test>
            """),

        // UInt32 array
        Arguments.of(
            Variant.of(new UInteger[] {UInteger.MIN, UInteger.MIN}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfUInt32>
                  <uax:UInt32>0</uax:UInt32>
                  <uax:UInt32>0</uax:UInt32>
                </uax:ListOfUInt32>
              </uax:Value>
            </Test>
            """),

        // Int64 array
        Arguments.of(
            Variant.of(new Long[] {0L, 0L}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfInt64>
                  <uax:Int64>0</uax:Int64>
                  <uax:Int64>0</uax:Int64>
                </uax:ListOfInt64>
              </uax:Value>
            </Test>
            """),

        // UInt64 array
        Arguments.of(
            Variant.of(new ULong[] {ULong.MIN, ULong.MIN}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfUInt64>
                  <uax:UInt64>0</uax:UInt64>
                  <uax:UInt64>0</uax:UInt64>
                </uax:ListOfUInt64>
              </uax:Value>
            </Test>
            """),

        // Float array
        Arguments.of(
            Variant.of(new Float[] {0.0f, 0.0f}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfFloat>
                  <uax:Float>0.0</uax:Float>
                  <uax:Float>0.0</uax:Float>
                </uax:ListOfFloat>
              </uax:Value>
            </Test>
            """),

        // Double array
        Arguments.of(
            Variant.of(new Double[] {0.0, 0.0}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfDouble>
                  <uax:Double>0.0</uax:Double>
                  <uax:Double>0.0</uax:Double>
                </uax:ListOfDouble>
              </uax:Value>
            </Test>
            """),

        // String array
        Arguments.of(
            Variant.ofStringArray(new String[] {"", ""}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfString>
                  <uax:String></uax:String>
                  <uax:String></uax:String>
                </uax:ListOfString>
              </uax:Value>
            </Test>
            """),

        // DateTime array
        Arguments.of(
            Variant.ofDateTimeArray(new DateTime[] {DateTime.NULL_VALUE, DateTime.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfDateTime>
                  <uax:DateTime>1601-01-01T00:00:00Z</uax:DateTime>
                  <uax:DateTime>1601-01-01T00:00:00Z</uax:DateTime>
                </uax:ListOfDateTime>
              </uax:Value>
            </Test>
            """),

        // Guid array
        Arguments.of(
            Variant.ofGuidArray(new UUID[] {new UUID(0L, 0L), new UUID(0L, 0L)}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfGuid>
                  <uax:Guid>00000000-0000-0000-0000-000000000000</uax:Guid>
                  <uax:Guid>00000000-0000-0000-0000-000000000000</uax:Guid>
                </uax:ListOfGuid>
              </uax:Value>
            </Test>
            """),

        // ByteString array
        Arguments.of(
            Variant.ofByteStringArray(
                new ByteString[] {ByteString.NULL_VALUE, ByteString.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfByteString>
                  <uax:ByteString></uax:ByteString>
                  <uax:ByteString></uax:ByteString>
                </uax:ListOfByteString>
              </uax:Value>
            </Test>
            """),

        // XmlElement array
        Arguments.of(
            Variant.ofXmlElementArray(
                new XmlElement[] {XmlElement.NULL_VALUE, XmlElement.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfXmlElement>
                  <uax:XmlElement></uax:XmlElement>
                  <uax:XmlElement></uax:XmlElement>
                </uax:ListOfXmlElement>
              </uax:Value>
            </Test>
            """),

        // NodeId array
        Arguments.of(
            Variant.of(new NodeId[] {NodeId.NULL_VALUE, NodeId.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfNodeId>
                  <uax:NodeId>
                    <uax:Identifier>i=0</uax:Identifier>
                  </uax:NodeId>
                  <uax:NodeId>
                    <uax:Identifier>i=0</uax:Identifier>
                  </uax:NodeId>
                </uax:ListOfNodeId>
              </uax:Value>
            </Test>
            """),

        // ExpandedNodeId array
        Arguments.of(
            Variant.of(new ExpandedNodeId[] {ExpandedNodeId.NULL_VALUE, ExpandedNodeId.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfExpandedNodeId>
                  <uax:ExpandedNodeId>
                    <uax:Identifier>i=0</uax:Identifier>
                  </uax:ExpandedNodeId>
                  <uax:ExpandedNodeId>
                    <uax:Identifier>i=0</uax:Identifier>
                  </uax:ExpandedNodeId>
                </uax:ListOfExpandedNodeId>
              </uax:Value>
            </Test>
            """),

        // StatusCode array
        Arguments.of(
            Variant.of(new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfStatusCode>
                  <uax:StatusCode>
                    <uax:Code>0</uax:Code>
                  </uax:StatusCode>
                  <uax:StatusCode>
                    <uax:Code>0</uax:Code>
                  </uax:StatusCode>
                </uax:ListOfStatusCode>
              </uax:Value>
            </Test>
            """),

        // QualifiedName array
        Arguments.of(
            Variant.of(new QualifiedName[] {QualifiedName.NULL_VALUE, QualifiedName.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfQualifiedName>
                  <uax:QualifiedName>
                    <uax:NamespaceIndex>0</uax:NamespaceIndex>
                  </uax:QualifiedName>
                  <uax:QualifiedName>
                    <uax:NamespaceIndex>0</uax:NamespaceIndex>
                  </uax:QualifiedName>
                </uax:ListOfQualifiedName>
              </uax:Value>
            </Test>
            """),

        // LocalizedText array
        Arguments.of(
            Variant.of(new LocalizedText[] {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfLocalizedText>
                  <uax:LocalizedText>
                  </uax:LocalizedText>
                  <uax:LocalizedText>
                  </uax:LocalizedText>
                </uax:ListOfLocalizedText>
              </uax:Value>
            </Test>
            """),

        // DataValue array
        Arguments.of(
            Variant.of(
                new DataValue[] {
                  DataValue.valueOnly(Variant.NULL_VALUE), DataValue.valueOnly(Variant.NULL_VALUE)
                }),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfDataValue>
                  <uax:DataValue>
                  </uax:DataValue>
                  <uax:DataValue>
                  </uax:DataValue>
                </uax:ListOfDataValue>
              </uax:Value>
            </Test>
            """),

        // ExtensionObject array
        Arguments.of(
            Variant.of(
                new ExtensionObject[] {
                  ExtensionObject.encode(
                      new DefaultEncodingContext(),
                      new Argument(
                          "name",
                          NodeId.parse("i=1"),
                          -1,
                          null,
                          LocalizedText.english("description")),
                      Argument.XML_ENCODING_ID,
                      OpcUaDefaultXmlEncoding.getInstance()),
                  ExtensionObject.encode(
                      new DefaultEncodingContext(),
                      new Argument(
                          "name",
                          NodeId.parse("i=1"),
                          -1,
                          null,
                          LocalizedText.english("description")),
                      Argument.XML_ENCODING_ID,
                      OpcUaDefaultXmlEncoding.getInstance())
                }),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:Value>
                <uax:ListOfExtensionObject>
                  <uax:ExtensionObject>
                    <uax:TypeId>
                      <uax:Identifier>i=297</uax:Identifier>
                    </uax:TypeId>
                    <uax:Body>
                      <Argument>
                        <Name>name</Name>
                        <DataType>
                          <uax:Identifier>i=1</uax:Identifier>
                        </DataType>
                        <ValueRank>-1</ValueRank>
                        <ArrayDimensions xsi:nil="true"></ArrayDimensions>
                        <Description>
                          <uax:Locale>en</uax:Locale>
                          <uax:Text>description</uax:Text>
                        </Description>
                      </Argument>
                    </uax:Body>
                  </uax:ExtensionObject>
                  <uax:ExtensionObject>
                    <uax:TypeId>
                      <uax:Identifier>i=297</uax:Identifier>
                    </uax:TypeId>
                    <uax:Body>
                      <Argument>
                        <Name>name</Name>
                        <DataType>
                          <uax:Identifier>i=1</uax:Identifier>
                        </DataType>
                        <ValueRank>-1</ValueRank>
                        <ArrayDimensions xsi:nil="true"></ArrayDimensions>
                        <Description>
                          <uax:Locale>en</uax:Locale>
                          <uax:Text>description</uax:Text>
                        </Description>
                      </Argument>
                    </uax:Body>
                  </uax:ExtensionObject>
                </uax:ListOfExtensionObject>
              </uax:Value>
            </Test>
            """),

        // Variant array
        Arguments.of(
            Variant.of(new Variant[] {Variant.of(0), Variant.of(0)}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfVariant>
                  <uax:Variant>
                    <uax:Value>
                      <uax:Int32>0</uax:Int32>
                    </uax:Value>
                  </uax:Variant>
                  <uax:Variant>
                    <uax:Value>
                      <uax:Int32>0</uax:Int32>
                    </uax:Value>
                  </uax:Variant>
                </uax:ListOfVariant>
              </uax:Value>
            </Test>
            """),

        // Enum array (NodeClass)
        Arguments.of(
            Variant.of(new NodeClass[] {NodeClass.Variable, NodeClass.Object}),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfInt32>
                  <uax:Int32>2</uax:Int32>
                  <uax:Int32>1</uax:Int32>
                </uax:ListOfInt32>
              </uax:Value>
            </Test>
            """),

        // Struct array (Argument)
        Arguments.of(
            Variant.of(
                new ExtensionObject[] {
                  ExtensionObject.encode(
                      new DefaultEncodingContext(),
                      new Argument(
                          "name1",
                          NodeId.parse("i=1"),
                          -1,
                          null,
                          LocalizedText.english("description1")),
                      Argument.XML_ENCODING_ID,
                      OpcUaDefaultXmlEncoding.getInstance()),
                  ExtensionObject.encode(
                      new DefaultEncodingContext(),
                      new Argument(
                          "name2",
                          NodeId.parse("i=1"),
                          -1,
                          null,
                          LocalizedText.english("description2")),
                      Argument.XML_ENCODING_ID,
                      OpcUaDefaultXmlEncoding.getInstance())
                }),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <uax:Value>
                <uax:ListOfExtensionObject>
                  <uax:ExtensionObject>
                    <uax:TypeId>
                      <uax:Identifier>i=297</uax:Identifier>
                    </uax:TypeId>
                    <uax:Body>
                      <Argument>
                        <Name>name1</Name>
                        <DataType>
                          <uax:Identifier>i=1</uax:Identifier>
                        </DataType>
                        <ValueRank>-1</ValueRank>
                        <ArrayDimensions xsi:nil="true"></ArrayDimensions>
                        <Description>
                          <uax:Locale>en</uax:Locale>
                          <uax:Text>description1</uax:Text>
                        </Description>
                      </Argument>
                    </uax:Body>
                  </uax:ExtensionObject>
                  <uax:ExtensionObject>
                    <uax:TypeId>
                      <uax:Identifier>i=297</uax:Identifier>
                    </uax:TypeId>
                    <uax:Body>
                      <Argument>
                        <Name>name2</Name>
                        <DataType>
                          <uax:Identifier>i=1</uax:Identifier>
                        </DataType>
                        <ValueRank>-1</ValueRank>
                        <ArrayDimensions xsi:nil="true"></ArrayDimensions>
                        <Description>
                          <uax:Locale>en</uax:Locale>
                          <uax:Text>description2</uax:Text>
                        </Description>
                      </Argument>
                    </uax:Body>
                  </uax:ExtensionObject>
                </uax:ListOfExtensionObject>
              </uax:Value>
            </Test>
            """),

        // OptionSet array (AttributeWriteMask)
        Arguments.of(
            Variant.of(
                new AttributeWriteMask[] {
                  AttributeWriteMask.of(AttributeWriteMask.Field.DisplayName),
                  AttributeWriteMask.of(AttributeWriteMask.Field.Description)
                }),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Value>
                <uax:ListOfUInt32>
                  <uax:UInt32>64</uax:UInt32>
                  <uax:UInt32>32</uax:UInt32>
                </uax:ListOfUInt32>
              </uax:Value>
            </Test>
            """));
  }
}
