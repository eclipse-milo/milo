/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class StructWithBuiltinMatrixFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3010");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5041");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5043");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5042");

  private final @Nullable Matrix _boolean;

  private final @Nullable Matrix sByte;

  private final @Nullable Matrix _byte;

  private final @Nullable Matrix int16;

  private final @Nullable Matrix uInt16;

  private final @Nullable Matrix int32;

  private final @Nullable Matrix uInt32;

  private final @Nullable Matrix int64;

  private final @Nullable Matrix uInt64;

  private final @Nullable Matrix _float;

  private final @Nullable Matrix _double;

  private final @Nullable Matrix string;

  private final @Nullable Matrix dateTime;

  private final @Nullable Matrix guid;

  private final @Nullable Matrix byteString;

  private final @Nullable Matrix xmlElement;

  private final @Nullable Matrix nodeId;

  private final @Nullable Matrix expandedNodeId;

  private final @Nullable Matrix statusCode;

  private final @Nullable Matrix qualifiedName;

  private final @Nullable Matrix localizedText;

  private final @Nullable Matrix dataValue;

  private final @Nullable Matrix variant;

  public StructWithBuiltinMatrixFields(
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
      @Nullable Matrix variant) {
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

  public @Nullable Matrix getBoolean() {
    return _boolean;
  }

  public @Nullable Matrix getSByte() {
    return sByte;
  }

  public @Nullable Matrix getByte() {
    return _byte;
  }

  public @Nullable Matrix getInt16() {
    return int16;
  }

  public @Nullable Matrix getUInt16() {
    return uInt16;
  }

  public @Nullable Matrix getInt32() {
    return int32;
  }

  public @Nullable Matrix getUInt32() {
    return uInt32;
  }

  public @Nullable Matrix getInt64() {
    return int64;
  }

  public @Nullable Matrix getUInt64() {
    return uInt64;
  }

  public @Nullable Matrix getFloat() {
    return _float;
  }

  public @Nullable Matrix getDouble() {
    return _double;
  }

  public @Nullable Matrix getString() {
    return string;
  }

  public @Nullable Matrix getDateTime() {
    return dateTime;
  }

  public @Nullable Matrix getGuid() {
    return guid;
  }

  public @Nullable Matrix getByteString() {
    return byteString;
  }

  public @Nullable Matrix getXmlElement() {
    return xmlElement;
  }

  public @Nullable Matrix getNodeId() {
    return nodeId;
  }

  public @Nullable Matrix getExpandedNodeId() {
    return expandedNodeId;
  }

  public @Nullable Matrix getStatusCode() {
    return statusCode;
  }

  public @Nullable Matrix getQualifiedName() {
    return qualifiedName;
  }

  public @Nullable Matrix getLocalizedText() {
    return localizedText;
  }

  public @Nullable Matrix getDataValue() {
    return dataValue;
  }

  public @Nullable Matrix getVariant() {
    return variant;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithBuiltinMatrixFields that = (StructWithBuiltinMatrixFields) object;
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
        new StringJoiner(", ", StructWithBuiltinMatrixFields.class.getSimpleName() + "[", "]");
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
    joiner.add("string=" + getString());
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
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5041")
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithBuiltinMatrixFields> {
    @Override
    public Class<StructWithBuiltinMatrixFields> getType() {
      return StructWithBuiltinMatrixFields.class;
    }

    @Override
    public StructWithBuiltinMatrixFields decodeType(EncodingContext context, UaDecoder decoder) {
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
      return new StructWithBuiltinMatrixFields(
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
        EncodingContext context, UaEncoder encoder, StructWithBuiltinMatrixFields value) {
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
    }
  }
}
