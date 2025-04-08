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

public class StructWithBuiltinScalarFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3004");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5020");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5021");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5022");

  private final Boolean _boolean;

  private final Byte sByte;

  private final UByte _byte;

  private final Short int16;

  private final UShort uInt16;

  private final Integer int32;

  private final UInteger uInt32;

  private final Long int64;

  private final ULong uInt64;

  private final Float _float;

  private final Double _double;

  private final @Nullable String string;

  private final DateTime dateTime;

  private final UUID guid;

  private final ByteString byteString;

  private final XmlElement xmlElement;

  private final NodeId nodeId;

  private final ExpandedNodeId expandedNodeId;

  private final StatusCode statusCode;

  private final QualifiedName qualifiedName;

  private final LocalizedText localizedText;

  private final DataValue dataValue;

  private final Variant variant;

  public StructWithBuiltinScalarFields(
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
      Variant variant) {
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

  public Boolean getBoolean() {
    return _boolean;
  }

  public Byte getSByte() {
    return sByte;
  }

  public UByte getByte() {
    return _byte;
  }

  public Short getInt16() {
    return int16;
  }

  public UShort getUInt16() {
    return uInt16;
  }

  public Integer getInt32() {
    return int32;
  }

  public UInteger getUInt32() {
    return uInt32;
  }

  public Long getInt64() {
    return int64;
  }

  public ULong getUInt64() {
    return uInt64;
  }

  public Float getFloat() {
    return _float;
  }

  public Double getDouble() {
    return _double;
  }

  public @Nullable String getString() {
    return string;
  }

  public DateTime getDateTime() {
    return dateTime;
  }

  public UUID getGuid() {
    return guid;
  }

  public ByteString getByteString() {
    return byteString;
  }

  public XmlElement getXmlElement() {
    return xmlElement;
  }

  public NodeId getNodeId() {
    return nodeId;
  }

  public ExpandedNodeId getExpandedNodeId() {
    return expandedNodeId;
  }

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public QualifiedName getQualifiedName() {
    return qualifiedName;
  }

  public LocalizedText getLocalizedText() {
    return localizedText;
  }

  public DataValue getDataValue() {
    return dataValue;
  }

  public Variant getVariant() {
    return variant;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithBuiltinScalarFields that = (StructWithBuiltinScalarFields) object;
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
        new StringJoiner(", ", StructWithBuiltinScalarFields.class.getSimpleName() + "[", "]");
    joiner.add("_boolean=" + getBoolean());
    joiner.add("sByte=" + getSByte());
    joiner.add("_byte=" + getByte());
    joiner.add("int16=" + getInt16());
    joiner.add("uInt16=" + getUInt16());
    joiner.add("int32=" + getInt32());
    joiner.add("uInt32=" + getUInt32());
    joiner.add("int64=" + getInt64());
    joiner.add("uInt64=" + getUInt64());
    joiner.add("_float=" + getFloat());
    joiner.add("_double=" + getDouble());
    joiner.add("string='" + getString() + "'");
    joiner.add("dateTime=" + getDateTime());
    joiner.add("guid=" + getGuid());
    joiner.add("byteString=" + getByteString());
    joiner.add("xmlElement=" + getXmlElement());
    joiner.add("nodeId=" + getNodeId());
    joiner.add("expandedNodeId=" + getExpandedNodeId());
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("qualifiedName=" + getQualifiedName());
    joiner.add("localizedText=" + getLocalizedText());
    joiner.add("dataValue=" + getDataValue());
    joiner.add("variant=" + getVariant());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5020")
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithBuiltinScalarFields> {
    @Override
    public Class<StructWithBuiltinScalarFields> getType() {
      return StructWithBuiltinScalarFields.class;
    }

    @Override
    public StructWithBuiltinScalarFields decodeType(EncodingContext context, UaDecoder decoder) {
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
      return new StructWithBuiltinScalarFields(
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
        EncodingContext context, UaEncoder encoder, StructWithBuiltinScalarFields value) {
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
    }
  }
}
