/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.test.types;

import java.util.StringJoiner;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class StructWithBuiltinScalarFieldsEx extends StructWithBuiltinScalarFields
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3015");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5026");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5028");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5027");

  private final Double duration;

  private final ApplicationType applicationType;

  private final TestEnumType testEnumType;

  private final XVType xvType;

  private final ConcreteTestType concreteTestType;

  private final UnionOfScalar unionOfScalar;

  private final UnionOfArray unionOfArray;

  private final AccessLevelType optionSetUi8;

  private final AccessRestrictionType optionSetUi16;

  private final AccessLevelExType optionSetUi32;

  private final ULong optionSetUi64;

  public StructWithBuiltinScalarFieldsEx(
      Boolean _boolean,
      Byte sByte,
      UByte _byte,
      Short int16,
      UShort uInt16,
      Integer int32,
      UInteger uInt32,
      Long int64,
      ULong uInt64,
      Float _float,
      Double _double,
      @Nullable String string,
      DateTime dateTime,
      UUID guid,
      ByteString byteString,
      XmlElement xmlElement,
      NodeId nodeId,
      ExpandedNodeId expandedNodeId,
      StatusCode statusCode,
      QualifiedName qualifiedName,
      LocalizedText localizedText,
      DataValue dataValue,
      Variant variant,
      Double duration,
      ApplicationType applicationType,
      TestEnumType testEnumType,
      XVType xvType,
      ConcreteTestType concreteTestType,
      UnionOfScalar unionOfScalar,
      UnionOfArray unionOfArray,
      AccessLevelType optionSetUi8,
      AccessRestrictionType optionSetUi16,
      AccessLevelExType optionSetUi32,
      ULong optionSetUi64) {
    super(
        _boolean,
        sByte,
        _byte,
        int16,
        uInt16,
        int32,
        uInt32,
        int64,
        uInt64,
        _float,
        _double,
        string,
        dateTime,
        guid,
        byteString,
        xmlElement,
        nodeId,
        expandedNodeId,
        statusCode,
        qualifiedName,
        localizedText,
        dataValue,
        variant);
    this.duration = duration;
    this.applicationType = applicationType;
    this.testEnumType = testEnumType;
    this.xvType = xvType;
    this.concreteTestType = concreteTestType;
    this.unionOfScalar = unionOfScalar;
    this.unionOfArray = unionOfArray;
    this.optionSetUi8 = optionSetUi8;
    this.optionSetUi16 = optionSetUi16;
    this.optionSetUi32 = optionSetUi32;
    this.optionSetUi64 = optionSetUi64;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  public Double getDuration() {
    return duration;
  }

  public ApplicationType getApplicationType() {
    return applicationType;
  }

  public TestEnumType getTestEnumType() {
    return testEnumType;
  }

  public XVType getXvType() {
    return xvType;
  }

  public ConcreteTestType getConcreteTestType() {
    return concreteTestType;
  }

  public UnionOfScalar getUnionOfScalar() {
    return unionOfScalar;
  }

  public UnionOfArray getUnionOfArray() {
    return unionOfArray;
  }

  public AccessLevelType getOptionSetUi8() {
    return optionSetUi8;
  }

  public AccessRestrictionType getOptionSetUi16() {
    return optionSetUi16;
  }

  public AccessLevelExType getOptionSetUi32() {
    return optionSetUi32;
  }

  public ULong getOptionSetUi64() {
    return optionSetUi64;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithBuiltinScalarFieldsEx that = (StructWithBuiltinScalarFieldsEx) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getDuration(), that.getDuration());
    eqb.append(getApplicationType(), that.getApplicationType());
    eqb.append(getTestEnumType(), that.getTestEnumType());
    eqb.append(getXvType(), that.getXvType());
    eqb.append(getConcreteTestType(), that.getConcreteTestType());
    eqb.append(getUnionOfScalar(), that.getUnionOfScalar());
    eqb.append(getUnionOfArray(), that.getUnionOfArray());
    eqb.append(getOptionSetUi8(), that.getOptionSetUi8());
    eqb.append(getOptionSetUi16(), that.getOptionSetUi16());
    eqb.append(getOptionSetUi32(), that.getOptionSetUi32());
    eqb.append(getOptionSetUi64(), that.getOptionSetUi64());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDuration());
    hcb.append(getApplicationType());
    hcb.append(getTestEnumType());
    hcb.append(getXvType());
    hcb.append(getConcreteTestType());
    hcb.append(getUnionOfScalar());
    hcb.append(getUnionOfArray());
    hcb.append(getOptionSetUi8());
    hcb.append(getOptionSetUi16());
    hcb.append(getOptionSetUi32());
    hcb.append(getOptionSetUi64());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithBuiltinScalarFieldsEx.class.getSimpleName() + "[", "]");
    joiner.add("duration=" + getDuration());
    joiner.add("applicationType=" + getApplicationType());
    joiner.add("testEnumType=" + getTestEnumType());
    joiner.add("xvType=" + getXvType());
    joiner.add("concreteTestType=" + getConcreteTestType());
    joiner.add("unionOfScalar=" + getUnionOfScalar());
    joiner.add("unionOfArray=" + getUnionOfArray());
    joiner.add("optionSetUi8=" + getOptionSetUi8());
    joiner.add("optionSetUi16=" + getOptionSetUi16());
    joiner.add("optionSetUi32=" + getOptionSetUi32());
    joiner.add("optionSetUi64=" + getOptionSetUi64());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5026")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3004")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Boolean",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 8),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 9),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Float",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Double",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "String",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DateTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 13),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Guid",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ByteString",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "XmlElement",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 16),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ExpandedNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QualifiedName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocalizedText",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Variant",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Duration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 307),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TestEnumType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3011")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "XVType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12080),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnionOfScalar",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3020")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnionOfArray",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3023")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI8",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15031),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 95),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15406),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11737),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithBuiltinScalarFieldsEx> {
    @Override
    public Class<StructWithBuiltinScalarFieldsEx> getType() {
      return StructWithBuiltinScalarFieldsEx.class;
    }

    @Override
    public StructWithBuiltinScalarFieldsEx decodeType(EncodingContext context, UaDecoder decoder) {
      final Boolean _boolean;
      final Byte sByte;
      final UByte _byte;
      final Short int16;
      final UShort uInt16;
      final Integer int32;
      final UInteger uInt32;
      final Long int64;
      final ULong uInt64;
      final Float _float;
      final Double _double;
      final String string;
      final DateTime dateTime;
      final UUID guid;
      final ByteString byteString;
      final XmlElement xmlElement;
      final NodeId nodeId;
      final ExpandedNodeId expandedNodeId;
      final StatusCode statusCode;
      final QualifiedName qualifiedName;
      final LocalizedText localizedText;
      final DataValue dataValue;
      final Variant variant;
      final Double duration;
      final ApplicationType applicationType;
      final TestEnumType testEnumType;
      final XVType xvType;
      final ConcreteTestType concreteTestType;
      final UnionOfScalar unionOfScalar;
      final UnionOfArray unionOfArray;
      final AccessLevelType optionSetUi8;
      final AccessRestrictionType optionSetUi16;
      final AccessLevelExType optionSetUi32;
      final ULong optionSetUi64;
      _boolean = decoder.decodeBoolean("Boolean");
      sByte = decoder.decodeSByte("SByte");
      _byte = decoder.decodeByte("Byte");
      int16 = decoder.decodeInt16("Int16");
      uInt16 = decoder.decodeUInt16("UInt16");
      int32 = decoder.decodeInt32("Int32");
      uInt32 = decoder.decodeUInt32("UInt32");
      int64 = decoder.decodeInt64("Int64");
      uInt64 = decoder.decodeUInt64("UInt64");
      _float = decoder.decodeFloat("Float");
      _double = decoder.decodeDouble("Double");
      string = decoder.decodeString("String");
      dateTime = decoder.decodeDateTime("DateTime");
      guid = decoder.decodeGuid("Guid");
      byteString = decoder.decodeByteString("ByteString");
      xmlElement = decoder.decodeXmlElement("XmlElement");
      nodeId = decoder.decodeNodeId("NodeId");
      expandedNodeId = decoder.decodeExpandedNodeId("ExpandedNodeId");
      statusCode = decoder.decodeStatusCode("StatusCode");
      qualifiedName = decoder.decodeQualifiedName("QualifiedName");
      localizedText = decoder.decodeLocalizedText("LocalizedText");
      dataValue = decoder.decodeDataValue("DataValue");
      variant = decoder.decodeVariant("Variant");
      duration = decoder.decodeDouble("Duration");
      applicationType = ApplicationType.from(decoder.decodeEnum("ApplicationType"));
      testEnumType = TestEnumType.from(decoder.decodeEnum("TestEnumType"));
      xvType = (XVType) decoder.decodeStruct("XVType", XVType.TYPE_ID);
      concreteTestType =
          (ConcreteTestType) decoder.decodeStruct("ConcreteTestType", ConcreteTestType.TYPE_ID);
      unionOfScalar = (UnionOfScalar) decoder.decodeStruct("UnionOfScalar", UnionOfScalar.TYPE_ID);
      unionOfArray = (UnionOfArray) decoder.decodeStruct("UnionOfArray", UnionOfArray.TYPE_ID);
      optionSetUi8 = new AccessLevelType(decoder.decodeByte("OptionSetUI8"));
      optionSetUi16 = new AccessRestrictionType(decoder.decodeUInt16("OptionSetUI16"));
      optionSetUi32 = new AccessLevelExType(decoder.decodeUInt32("OptionSetUI32"));
      optionSetUi64 = decoder.decodeUInt64("OptionSetUI64");
      return new StructWithBuiltinScalarFieldsEx(
          _boolean,
          sByte,
          _byte,
          int16,
          uInt16,
          int32,
          uInt32,
          int64,
          uInt64,
          _float,
          _double,
          string,
          dateTime,
          guid,
          byteString,
          xmlElement,
          nodeId,
          expandedNodeId,
          statusCode,
          qualifiedName,
          localizedText,
          dataValue,
          variant,
          duration,
          applicationType,
          testEnumType,
          xvType,
          concreteTestType,
          unionOfScalar,
          unionOfArray,
          optionSetUi8,
          optionSetUi16,
          optionSetUi32,
          optionSetUi64);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithBuiltinScalarFieldsEx value) {
      encoder.encodeBoolean("Boolean", value.getBoolean());
      encoder.encodeSByte("SByte", value.getSByte());
      encoder.encodeByte("Byte", value.getByte());
      encoder.encodeInt16("Int16", value.getInt16());
      encoder.encodeUInt16("UInt16", value.getUInt16());
      encoder.encodeInt32("Int32", value.getInt32());
      encoder.encodeUInt32("UInt32", value.getUInt32());
      encoder.encodeInt64("Int64", value.getInt64());
      encoder.encodeUInt64("UInt64", value.getUInt64());
      encoder.encodeFloat("Float", value.getFloat());
      encoder.encodeDouble("Double", value.getDouble());
      encoder.encodeString("String", value.getString());
      encoder.encodeDateTime("DateTime", value.getDateTime());
      encoder.encodeGuid("Guid", value.getGuid());
      encoder.encodeByteString("ByteString", value.getByteString());
      encoder.encodeXmlElement("XmlElement", value.getXmlElement());
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeExpandedNodeId("ExpandedNodeId", value.getExpandedNodeId());
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeQualifiedName("QualifiedName", value.getQualifiedName());
      encoder.encodeLocalizedText("LocalizedText", value.getLocalizedText());
      encoder.encodeDataValue("DataValue", value.getDataValue());
      encoder.encodeVariant("Variant", value.getVariant());
      encoder.encodeDouble("Duration", value.getDuration());
      encoder.encodeEnum("ApplicationType", value.getApplicationType());
      encoder.encodeEnum("TestEnumType", value.getTestEnumType());
      encoder.encodeStruct("XVType", value.getXvType(), XVType.TYPE_ID);
      encoder.encodeStruct(
          "ConcreteTestType", value.getConcreteTestType(), ConcreteTestType.TYPE_ID);
      encoder.encodeStruct("UnionOfScalar", value.getUnionOfScalar(), UnionOfScalar.TYPE_ID);
      encoder.encodeStruct("UnionOfArray", value.getUnionOfArray(), UnionOfArray.TYPE_ID);
      encoder.encodeByte("OptionSetUI8", value.getOptionSetUi8().getValue());
      encoder.encodeUInt16("OptionSetUI16", value.getOptionSetUi16().getValue());
      encoder.encodeUInt32("OptionSetUI32", value.getOptionSetUi32().getValue());
      encoder.encodeUInt64("OptionSetUI64", value.getOptionSetUi64());
    }
  }
}
