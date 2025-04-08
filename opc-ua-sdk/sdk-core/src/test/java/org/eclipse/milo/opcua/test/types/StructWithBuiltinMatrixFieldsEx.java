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
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
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

public class StructWithBuiltinMatrixFieldsEx extends StructWithBuiltinMatrixFields
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3017");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5044");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5045");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5046");

  private final @Nullable Matrix duration;

  private final @Nullable Matrix applicationType;

  private final @Nullable Matrix testEnumType;

  private final @Nullable Matrix xvType;

  private final @Nullable Matrix concreteTestType;

  private final @Nullable Matrix unionOfScalar;

  private final @Nullable Matrix unionOfArray;

  private final @Nullable Matrix optionSetUi8;

  private final @Nullable Matrix optionSetUi16;

  private final @Nullable Matrix optionSetUi32;

  private final @Nullable Matrix optionSetUi64;

  public StructWithBuiltinMatrixFieldsEx(
      @Nullable Matrix _boolean,
      @Nullable Matrix sByte,
      @Nullable Matrix _byte,
      @Nullable Matrix int16,
      @Nullable Matrix uInt16,
      @Nullable Matrix int32,
      @Nullable Matrix uInt32,
      @Nullable Matrix int64,
      @Nullable Matrix uInt64,
      @Nullable Matrix _float,
      @Nullable Matrix _double,
      @Nullable Matrix string,
      @Nullable Matrix dateTime,
      @Nullable Matrix guid,
      @Nullable Matrix byteString,
      @Nullable Matrix xmlElement,
      @Nullable Matrix nodeId,
      @Nullable Matrix expandedNodeId,
      @Nullable Matrix statusCode,
      @Nullable Matrix qualifiedName,
      @Nullable Matrix localizedText,
      @Nullable Matrix dataValue,
      @Nullable Matrix variant,
      @Nullable Matrix duration,
      @Nullable Matrix applicationType,
      @Nullable Matrix testEnumType,
      @Nullable Matrix xvType,
      @Nullable Matrix concreteTestType,
      @Nullable Matrix unionOfScalar,
      @Nullable Matrix unionOfArray,
      @Nullable Matrix optionSetUi8,
      @Nullable Matrix optionSetUi16,
      @Nullable Matrix optionSetUi32,
      @Nullable Matrix optionSetUi64) {
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

  public @Nullable Matrix getDuration() {
    return duration;
  }

  public @Nullable Matrix getApplicationType() {
    return applicationType;
  }

  public @Nullable Matrix getTestEnumType() {
    return testEnumType;
  }

  public @Nullable Matrix getXvType() {
    return xvType;
  }

  public @Nullable Matrix getConcreteTestType() {
    return concreteTestType;
  }

  public @Nullable Matrix getUnionOfScalar() {
    return unionOfScalar;
  }

  public @Nullable Matrix getUnionOfArray() {
    return unionOfArray;
  }

  public @Nullable Matrix getOptionSetUi8() {
    return optionSetUi8;
  }

  public @Nullable Matrix getOptionSetUi16() {
    return optionSetUi16;
  }

  public @Nullable Matrix getOptionSetUi32() {
    return optionSetUi32;
  }

  public @Nullable Matrix getOptionSetUi64() {
    return optionSetUi64;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithBuiltinMatrixFieldsEx that = (StructWithBuiltinMatrixFieldsEx) object;
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
        new StringJoiner(", ", StructWithBuiltinMatrixFieldsEx.class.getSimpleName() + "[", "]");
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
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5044")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3010")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Boolean",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 8),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 9),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Float",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Double",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "String",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DateTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 13),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Guid",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ByteString",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "XmlElement",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 16),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ExpandedNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QualifiedName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocalizedText",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Variant",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Duration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 307),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TestEnumType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3011")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "XVType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12080),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnionOfScalar",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3020")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnionOfArray",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3023")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI8",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15031),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 95),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15406),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionSetUI64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11737),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithBuiltinMatrixFieldsEx> {
    @Override
    public Class<StructWithBuiltinMatrixFieldsEx> getType() {
      return StructWithBuiltinMatrixFieldsEx.class;
    }

    @Override
    public StructWithBuiltinMatrixFieldsEx decodeType(EncodingContext context, UaDecoder decoder) {
      final Matrix _boolean;
      final Matrix sByte;
      final Matrix _byte;
      final Matrix int16;
      final Matrix uInt16;
      final Matrix int32;
      final Matrix uInt32;
      final Matrix int64;
      final Matrix uInt64;
      final Matrix _float;
      final Matrix _double;
      final Matrix string;
      final Matrix dateTime;
      final Matrix guid;
      final Matrix byteString;
      final Matrix xmlElement;
      final Matrix nodeId;
      final Matrix expandedNodeId;
      final Matrix statusCode;
      final Matrix qualifiedName;
      final Matrix localizedText;
      final Matrix dataValue;
      final Matrix variant;
      final Matrix duration;
      final Matrix applicationType;
      final Matrix testEnumType;
      final Matrix xvType;
      final Matrix concreteTestType;
      final Matrix unionOfScalar;
      final Matrix unionOfArray;
      final Matrix optionSetUi8;
      final Matrix optionSetUi16;
      final Matrix optionSetUi32;
      final Matrix optionSetUi64;
      _boolean = decoder.decodeMatrix("Boolean", OpcUaDataType.Boolean);
      sByte = decoder.decodeMatrix("SByte", OpcUaDataType.SByte);
      _byte = decoder.decodeMatrix("Byte", OpcUaDataType.Byte);
      int16 = decoder.decodeMatrix("Int16", OpcUaDataType.Int16);
      uInt16 = decoder.decodeMatrix("UInt16", OpcUaDataType.UInt16);
      int32 = decoder.decodeMatrix("Int32", OpcUaDataType.Int32);
      uInt32 = decoder.decodeMatrix("UInt32", OpcUaDataType.UInt32);
      int64 = decoder.decodeMatrix("Int64", OpcUaDataType.Int64);
      uInt64 = decoder.decodeMatrix("UInt64", OpcUaDataType.UInt64);
      _float = decoder.decodeMatrix("Float", OpcUaDataType.Float);
      _double = decoder.decodeMatrix("Double", OpcUaDataType.Double);
      string = decoder.decodeMatrix("String", OpcUaDataType.String);
      dateTime = decoder.decodeMatrix("DateTime", OpcUaDataType.DateTime);
      guid = decoder.decodeMatrix("Guid", OpcUaDataType.Guid);
      byteString = decoder.decodeMatrix("ByteString", OpcUaDataType.ByteString);
      xmlElement = decoder.decodeMatrix("XmlElement", OpcUaDataType.XmlElement);
      nodeId = decoder.decodeMatrix("NodeId", OpcUaDataType.NodeId);
      expandedNodeId = decoder.decodeMatrix("ExpandedNodeId", OpcUaDataType.ExpandedNodeId);
      statusCode = decoder.decodeMatrix("StatusCode", OpcUaDataType.StatusCode);
      qualifiedName = decoder.decodeMatrix("QualifiedName", OpcUaDataType.QualifiedName);
      localizedText = decoder.decodeMatrix("LocalizedText", OpcUaDataType.LocalizedText);
      dataValue = decoder.decodeMatrix("DataValue", OpcUaDataType.DataValue);
      variant = decoder.decodeMatrix("Variant", OpcUaDataType.Variant);
      duration = decoder.decodeMatrix("Duration", OpcUaDataType.Double);
      {
        Matrix matrix = decoder.decodeEnumMatrix("ApplicationType");
        applicationType = matrix.transform(i -> ApplicationType.from((Integer) i));
      }
      {
        Matrix matrix = decoder.decodeEnumMatrix("TestEnumType");
        testEnumType = matrix.transform(i -> TestEnumType.from((Integer) i));
      }
      xvType = (Matrix) decoder.decodeStructMatrix("XVType", XVType.TYPE_ID);
      concreteTestType =
          (Matrix) decoder.decodeStructMatrix("ConcreteTestType", ConcreteTestType.TYPE_ID);
      unionOfScalar = (Matrix) decoder.decodeStructMatrix("UnionOfScalar", UnionOfScalar.TYPE_ID);
      unionOfArray = (Matrix) decoder.decodeStructMatrix("UnionOfArray", UnionOfArray.TYPE_ID);
      {
        Matrix matrix = decoder.decodeMatrix("OptionSetUI8", OpcUaDataType.Byte);
        optionSetUi8 = matrix.transform(i -> new AccessLevelType((UByte) i));
      }
      {
        Matrix matrix = decoder.decodeMatrix("OptionSetUI16", OpcUaDataType.UInt16);
        optionSetUi16 = matrix.transform(i -> new AccessRestrictionType((UShort) i));
      }
      {
        Matrix matrix = decoder.decodeMatrix("OptionSetUI32", OpcUaDataType.UInt32);
        optionSetUi32 = matrix.transform(i -> new AccessLevelExType((UInteger) i));
      }
      optionSetUi64 = decoder.decodeMatrix("OptionSetUI64", OpcUaDataType.UInt64);
      return new StructWithBuiltinMatrixFieldsEx(
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
        EncodingContext context, UaEncoder encoder, StructWithBuiltinMatrixFieldsEx value) {
      encoder.encodeMatrix("Boolean", value.getBoolean());
      encoder.encodeMatrix("SByte", value.getSByte());
      encoder.encodeMatrix("Byte", value.getByte());
      encoder.encodeMatrix("Int16", value.getInt16());
      encoder.encodeMatrix("UInt16", value.getUInt16());
      encoder.encodeMatrix("Int32", value.getInt32());
      encoder.encodeMatrix("UInt32", value.getUInt32());
      encoder.encodeMatrix("Int64", value.getInt64());
      encoder.encodeMatrix("UInt64", value.getUInt64());
      encoder.encodeMatrix("Float", value.getFloat());
      encoder.encodeMatrix("Double", value.getDouble());
      encoder.encodeMatrix("String", value.getString());
      encoder.encodeMatrix("DateTime", value.getDateTime());
      encoder.encodeMatrix("Guid", value.getGuid());
      encoder.encodeMatrix("ByteString", value.getByteString());
      encoder.encodeMatrix("XmlElement", value.getXmlElement());
      encoder.encodeMatrix("NodeId", value.getNodeId());
      encoder.encodeMatrix("ExpandedNodeId", value.getExpandedNodeId());
      encoder.encodeMatrix("StatusCode", value.getStatusCode());
      encoder.encodeMatrix("QualifiedName", value.getQualifiedName());
      encoder.encodeMatrix("LocalizedText", value.getLocalizedText());
      encoder.encodeMatrix("DataValue", value.getDataValue());
      encoder.encodeMatrix("Variant", value.getVariant());
      encoder.encodeMatrix("Duration", value.getDuration());
      encoder.encodeEnumMatrix("ApplicationType", value.getApplicationType());
      encoder.encodeEnumMatrix("TestEnumType", value.getTestEnumType());
      encoder.encodeStructMatrix("XVType", value.getXvType(), XVType.TYPE_ID);
      encoder.encodeStructMatrix(
          "ConcreteTestType", value.getConcreteTestType(), ConcreteTestType.TYPE_ID);
      encoder.encodeStructMatrix("UnionOfScalar", value.getUnionOfScalar(), UnionOfScalar.TYPE_ID);
      encoder.encodeStructMatrix("UnionOfArray", value.getUnionOfArray(), UnionOfArray.TYPE_ID);
      encoder.encodeMatrix("OptionSetUI8", value.getOptionSetUi8());
      encoder.encodeMatrix("OptionSetUI16", value.getOptionSetUi16());
      encoder.encodeMatrix("OptionSetUI32", value.getOptionSetUi32());
      encoder.encodeMatrix("OptionSetUI64", value.getOptionSetUi64());
    }
  }
}
