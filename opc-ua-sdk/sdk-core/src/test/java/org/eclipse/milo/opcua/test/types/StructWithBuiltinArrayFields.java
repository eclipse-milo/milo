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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class StructWithBuiltinArrayFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3019");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5014");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5015");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5016");

  private final Boolean @Nullable [] _boolean;

  private final Byte @Nullable [] sByte;

  private final UByte @Nullable [] _byte;

  private final Short @Nullable [] int16;

  private final UShort @Nullable [] uInt16;

  private final Integer @Nullable [] int32;

  private final UInteger @Nullable [] uInt32;

  private final Long @Nullable [] int64;

  private final ULong @Nullable [] uInt64;

  private final Float @Nullable [] _float;

  private final Double @Nullable [] _double;

  private final String @Nullable [] string;

  private final DateTime @Nullable [] dateTime;

  private final UUID @Nullable [] guid;

  private final ByteString @Nullable [] byteString;

  private final XmlElement @Nullable [] xmlElement;

  private final NodeId @Nullable [] nodeId;

  private final ExpandedNodeId @Nullable [] expandedNodeId;

  private final StatusCode @Nullable [] statusCode;

  private final QualifiedName @Nullable [] qualifiedName;

  private final LocalizedText @Nullable [] localizedText;

  private final DataValue @Nullable [] dataValue;

  private final Variant @Nullable [] variant;

  public StructWithBuiltinArrayFields(
      Boolean @Nullable [] _boolean,
      Byte @Nullable [] sByte,
      UByte @Nullable [] _byte,
      Short @Nullable [] int16,
      UShort @Nullable [] uInt16,
      Integer @Nullable [] int32,
      UInteger @Nullable [] uInt32,
      Long @Nullable [] int64,
      ULong @Nullable [] uInt64,
      Float @Nullable [] _float,
      Double @Nullable [] _double,
      String @Nullable [] string,
      DateTime @Nullable [] dateTime,
      UUID @Nullable [] guid,
      ByteString @Nullable [] byteString,
      XmlElement @Nullable [] xmlElement,
      NodeId @Nullable [] nodeId,
      ExpandedNodeId @Nullable [] expandedNodeId,
      StatusCode @Nullable [] statusCode,
      QualifiedName @Nullable [] qualifiedName,
      LocalizedText @Nullable [] localizedText,
      DataValue @Nullable [] dataValue,
      Variant @Nullable [] variant) {
    this._boolean = _boolean;
    this.sByte = sByte;
    this._byte = _byte;
    this.int16 = int16;
    this.uInt16 = uInt16;
    this.int32 = int32;
    this.uInt32 = uInt32;
    this.int64 = int64;
    this.uInt64 = uInt64;
    this._float = _float;
    this._double = _double;
    this.string = string;
    this.dateTime = dateTime;
    this.guid = guid;
    this.byteString = byteString;
    this.xmlElement = xmlElement;
    this.nodeId = nodeId;
    this.expandedNodeId = expandedNodeId;
    this.statusCode = statusCode;
    this.qualifiedName = qualifiedName;
    this.localizedText = localizedText;
    this.dataValue = dataValue;
    this.variant = variant;
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

  public Boolean @Nullable [] getBoolean() {
    return _boolean;
  }

  public Byte @Nullable [] getSByte() {
    return sByte;
  }

  public UByte @Nullable [] getByte() {
    return _byte;
  }

  public Short @Nullable [] getInt16() {
    return int16;
  }

  public UShort @Nullable [] getUInt16() {
    return uInt16;
  }

  public Integer @Nullable [] getInt32() {
    return int32;
  }

  public UInteger @Nullable [] getUInt32() {
    return uInt32;
  }

  public Long @Nullable [] getInt64() {
    return int64;
  }

  public ULong @Nullable [] getUInt64() {
    return uInt64;
  }

  public Float @Nullable [] getFloat() {
    return _float;
  }

  public Double @Nullable [] getDouble() {
    return _double;
  }

  public String @Nullable [] getString() {
    return string;
  }

  public DateTime @Nullable [] getDateTime() {
    return dateTime;
  }

  public UUID @Nullable [] getGuid() {
    return guid;
  }

  public ByteString @Nullable [] getByteString() {
    return byteString;
  }

  public XmlElement @Nullable [] getXmlElement() {
    return xmlElement;
  }

  public NodeId @Nullable [] getNodeId() {
    return nodeId;
  }

  public ExpandedNodeId @Nullable [] getExpandedNodeId() {
    return expandedNodeId;
  }

  public StatusCode @Nullable [] getStatusCode() {
    return statusCode;
  }

  public QualifiedName @Nullable [] getQualifiedName() {
    return qualifiedName;
  }

  public LocalizedText @Nullable [] getLocalizedText() {
    return localizedText;
  }

  public DataValue @Nullable [] getDataValue() {
    return dataValue;
  }

  public Variant @Nullable [] getVariant() {
    return variant;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithBuiltinArrayFields that = (StructWithBuiltinArrayFields) object;
    var eqb = new EqualsBuilder();
    eqb.append(getBoolean(), that.getBoolean());
    eqb.append(getSByte(), that.getSByte());
    eqb.append(getByte(), that.getByte());
    eqb.append(getInt16(), that.getInt16());
    eqb.append(getUInt16(), that.getUInt16());
    eqb.append(getInt32(), that.getInt32());
    eqb.append(getUInt32(), that.getUInt32());
    eqb.append(getInt64(), that.getInt64());
    eqb.append(getUInt64(), that.getUInt64());
    eqb.append(getFloat(), that.getFloat());
    eqb.append(getDouble(), that.getDouble());
    eqb.append(getString(), that.getString());
    eqb.append(getDateTime(), that.getDateTime());
    eqb.append(getGuid(), that.getGuid());
    eqb.append(getByteString(), that.getByteString());
    eqb.append(getXmlElement(), that.getXmlElement());
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getExpandedNodeId(), that.getExpandedNodeId());
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getQualifiedName(), that.getQualifiedName());
    eqb.append(getLocalizedText(), that.getLocalizedText());
    eqb.append(getDataValue(), that.getDataValue());
    eqb.append(getVariant(), that.getVariant());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getBoolean());
    hcb.append(getSByte());
    hcb.append(getByte());
    hcb.append(getInt16());
    hcb.append(getUInt16());
    hcb.append(getInt32());
    hcb.append(getUInt32());
    hcb.append(getInt64());
    hcb.append(getUInt64());
    hcb.append(getFloat());
    hcb.append(getDouble());
    hcb.append(getString());
    hcb.append(getDateTime());
    hcb.append(getGuid());
    hcb.append(getByteString());
    hcb.append(getXmlElement());
    hcb.append(getNodeId());
    hcb.append(getExpandedNodeId());
    hcb.append(getStatusCode());
    hcb.append(getQualifiedName());
    hcb.append(getLocalizedText());
    hcb.append(getDataValue());
    hcb.append(getVariant());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithBuiltinArrayFields.class.getSimpleName() + "[", "]");
    joiner.add("_boolean=" + java.util.Arrays.toString(getBoolean()));
    joiner.add("sByte=" + java.util.Arrays.toString(getSByte()));
    joiner.add("_byte=" + java.util.Arrays.toString(getByte()));
    joiner.add("int16=" + java.util.Arrays.toString(getInt16()));
    joiner.add("uInt16=" + java.util.Arrays.toString(getUInt16()));
    joiner.add("int32=" + java.util.Arrays.toString(getInt32()));
    joiner.add("uInt32=" + java.util.Arrays.toString(getUInt32()));
    joiner.add("int64=" + java.util.Arrays.toString(getInt64()));
    joiner.add("uInt64=" + java.util.Arrays.toString(getUInt64()));
    joiner.add("_float=" + java.util.Arrays.toString(getFloat()));
    joiner.add("_double=" + java.util.Arrays.toString(getDouble()));
    joiner.add("string=" + java.util.Arrays.toString(getString()));
    joiner.add("dateTime=" + java.util.Arrays.toString(getDateTime()));
    joiner.add("guid=" + java.util.Arrays.toString(getGuid()));
    joiner.add("byteString=" + java.util.Arrays.toString(getByteString()));
    joiner.add("xmlElement=" + java.util.Arrays.toString(getXmlElement()));
    joiner.add("nodeId=" + java.util.Arrays.toString(getNodeId()));
    joiner.add("expandedNodeId=" + java.util.Arrays.toString(getExpandedNodeId()));
    joiner.add("statusCode=" + java.util.Arrays.toString(getStatusCode()));
    joiner.add("qualifiedName=" + java.util.Arrays.toString(getQualifiedName()));
    joiner.add("localizedText=" + java.util.Arrays.toString(getLocalizedText()));
    joiner.add("dataValue=" + java.util.Arrays.toString(getDataValue()));
    joiner.add("variant=" + java.util.Arrays.toString(getVariant()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5014")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Boolean",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt16",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Int64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 8),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UInt64",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 9),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Float",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Double",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "String",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DateTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 13),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Guid",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ByteString",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "XmlElement",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 16),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ExpandedNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QualifiedName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocalizedText",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Variant",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              1,
              new UInteger[] {UInteger.valueOf(4)},
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithBuiltinArrayFields> {
    @Override
    public Class<StructWithBuiltinArrayFields> getType() {
      return StructWithBuiltinArrayFields.class;
    }

    @Override
    public StructWithBuiltinArrayFields decodeType(EncodingContext context, UaDecoder decoder) {
      final Boolean[] _boolean;
      final Byte[] sByte;
      final UByte[] _byte;
      final Short[] int16;
      final UShort[] uInt16;
      final Integer[] int32;
      final UInteger[] uInt32;
      final Long[] int64;
      final ULong[] uInt64;
      final Float[] _float;
      final Double[] _double;
      final String[] string;
      final DateTime[] dateTime;
      final UUID[] guid;
      final ByteString[] byteString;
      final XmlElement[] xmlElement;
      final NodeId[] nodeId;
      final ExpandedNodeId[] expandedNodeId;
      final StatusCode[] statusCode;
      final QualifiedName[] qualifiedName;
      final LocalizedText[] localizedText;
      final DataValue[] dataValue;
      final Variant[] variant;
      _boolean = decoder.decodeBooleanArray("Boolean");
      sByte = decoder.decodeSByteArray("SByte");
      _byte = decoder.decodeByteArray("Byte");
      int16 = decoder.decodeInt16Array("Int16");
      uInt16 = decoder.decodeUInt16Array("UInt16");
      int32 = decoder.decodeInt32Array("Int32");
      uInt32 = decoder.decodeUInt32Array("UInt32");
      int64 = decoder.decodeInt64Array("Int64");
      uInt64 = decoder.decodeUInt64Array("UInt64");
      _float = decoder.decodeFloatArray("Float");
      _double = decoder.decodeDoubleArray("Double");
      string = decoder.decodeStringArray("String");
      dateTime = decoder.decodeDateTimeArray("DateTime");
      guid = decoder.decodeGuidArray("Guid");
      byteString = decoder.decodeByteStringArray("ByteString");
      xmlElement = decoder.decodeXmlElementArray("XmlElement");
      nodeId = decoder.decodeNodeIdArray("NodeId");
      expandedNodeId = decoder.decodeExpandedNodeIdArray("ExpandedNodeId");
      statusCode = decoder.decodeStatusCodeArray("StatusCode");
      qualifiedName = decoder.decodeQualifiedNameArray("QualifiedName");
      localizedText = decoder.decodeLocalizedTextArray("LocalizedText");
      dataValue = decoder.decodeDataValueArray("DataValue");
      variant = decoder.decodeVariantArray("Variant");
      return new StructWithBuiltinArrayFields(
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
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithBuiltinArrayFields value) {
      encoder.encodeBooleanArray("Boolean", value.getBoolean());
      encoder.encodeSByteArray("SByte", value.getSByte());
      encoder.encodeByteArray("Byte", value.getByte());
      encoder.encodeInt16Array("Int16", value.getInt16());
      encoder.encodeUInt16Array("UInt16", value.getUInt16());
      encoder.encodeInt32Array("Int32", value.getInt32());
      encoder.encodeUInt32Array("UInt32", value.getUInt32());
      encoder.encodeInt64Array("Int64", value.getInt64());
      encoder.encodeUInt64Array("UInt64", value.getUInt64());
      encoder.encodeFloatArray("Float", value.getFloat());
      encoder.encodeDoubleArray("Double", value.getDouble());
      encoder.encodeStringArray("String", value.getString());
      encoder.encodeDateTimeArray("DateTime", value.getDateTime());
      encoder.encodeGuidArray("Guid", value.getGuid());
      encoder.encodeByteStringArray("ByteString", value.getByteString());
      encoder.encodeXmlElementArray("XmlElement", value.getXmlElement());
      encoder.encodeNodeIdArray("NodeId", value.getNodeId());
      encoder.encodeExpandedNodeIdArray("ExpandedNodeId", value.getExpandedNodeId());
      encoder.encodeStatusCodeArray("StatusCode", value.getStatusCode());
      encoder.encodeQualifiedNameArray("QualifiedName", value.getQualifiedName());
      encoder.encodeLocalizedTextArray("LocalizedText", value.getLocalizedText());
      encoder.encodeDataValueArray("DataValue", value.getDataValue());
      encoder.encodeVariantArray("Variant", value.getVariant());
    }
  }
}
