/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.3</a>
 */
public class FieldMetaData extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14524");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=14839");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=14795");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15051");

  private final @Nullable String name;

  private final LocalizedText description;

  private final DataSetFieldFlags fieldFlags;

  private final UByte builtInType;

  private final NodeId dataType;

  private final Integer valueRank;

  private final UInteger @Nullable [] arrayDimensions;

  private final UInteger maxStringLength;

  private final UUID dataSetFieldId;

  private final KeyValuePair @Nullable [] properties;

  public FieldMetaData(
      @Nullable String name,
      LocalizedText description,
      DataSetFieldFlags fieldFlags,
      UByte builtInType,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UInteger maxStringLength,
      UUID dataSetFieldId,
      KeyValuePair @Nullable [] properties) {
    this.name = name;
    this.description = description;
    this.fieldFlags = fieldFlags;
    this.builtInType = builtInType;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.maxStringLength = maxStringLength;
    this.dataSetFieldId = dataSetFieldId;
    this.properties = properties;
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
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public @Nullable String getName() {
    return name;
  }

  public LocalizedText getDescription() {
    return description;
  }

  public DataSetFieldFlags getFieldFlags() {
    return fieldFlags;
  }

  public UByte getBuiltInType() {
    return builtInType;
  }

  public NodeId getDataType() {
    return dataType;
  }

  public Integer getValueRank() {
    return valueRank;
  }

  public UInteger @Nullable [] getArrayDimensions() {
    return arrayDimensions;
  }

  public UInteger getMaxStringLength() {
    return maxStringLength;
  }

  public UUID getDataSetFieldId() {
    return dataSetFieldId;
  }

  public KeyValuePair @Nullable [] getProperties() {
    return properties;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FieldMetaData that = (FieldMetaData) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getFieldFlags(), that.getFieldFlags());
    eqb.append(getBuiltInType(), that.getBuiltInType());
    eqb.append(getDataType(), that.getDataType());
    eqb.append(getValueRank(), that.getValueRank());
    eqb.append(getArrayDimensions(), that.getArrayDimensions());
    eqb.append(getMaxStringLength(), that.getMaxStringLength());
    eqb.append(getDataSetFieldId(), that.getDataSetFieldId());
    eqb.append(getProperties(), that.getProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getDescription());
    hcb.append(getFieldFlags());
    hcb.append(getBuiltInType());
    hcb.append(getDataType());
    hcb.append(getValueRank());
    hcb.append(getArrayDimensions());
    hcb.append(getMaxStringLength());
    hcb.append(getDataSetFieldId());
    hcb.append(getProperties());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", FieldMetaData.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("description=" + getDescription());
    joiner.add("fieldFlags=" + getFieldFlags());
    joiner.add("builtInType=" + getBuiltInType());
    joiner.add("dataType=" + getDataType());
    joiner.add("valueRank=" + getValueRank());
    joiner.add("arrayDimensions=" + java.util.Arrays.toString(getArrayDimensions()));
    joiner.add("maxStringLength=" + getMaxStringLength());
    joiner.add("dataSetFieldId=" + getDataSetFieldId());
    joiner.add("properties=" + java.util.Arrays.toString(getProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 14839),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FieldFlags",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15904),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BuiltInType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ValueRank",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ArrayDimensions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxStringLength",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetFieldId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Properties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<FieldMetaData> {
    @Override
    public Class<FieldMetaData> getType() {
      return FieldMetaData.class;
    }

    @Override
    public FieldMetaData decodeType(EncodingContext context, UaDecoder decoder) {
      String name = decoder.decodeString("Name");
      LocalizedText description = decoder.decodeLocalizedText("Description");
      DataSetFieldFlags fieldFlags = new DataSetFieldFlags(decoder.decodeUInt16("FieldFlags"));
      UByte builtInType = decoder.decodeByte("BuiltInType");
      NodeId dataType = decoder.decodeNodeId("DataType");
      Integer valueRank = decoder.decodeInt32("ValueRank");
      UInteger[] arrayDimensions = decoder.decodeUInt32Array("ArrayDimensions");
      UInteger maxStringLength = decoder.decodeUInt32("MaxStringLength");
      UUID dataSetFieldId = decoder.decodeGuid("DataSetFieldId");
      KeyValuePair[] properties =
          (KeyValuePair[]) decoder.decodeStructArray("Properties", KeyValuePair.TYPE_ID);
      return new FieldMetaData(
          name,
          description,
          fieldFlags,
          builtInType,
          dataType,
          valueRank,
          arrayDimensions,
          maxStringLength,
          dataSetFieldId,
          properties);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, FieldMetaData value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt16("FieldFlags", value.getFieldFlags().getValue());
      encoder.encodeByte("BuiltInType", value.getBuiltInType());
      encoder.encodeNodeId("DataType", value.getDataType());
      encoder.encodeInt32("ValueRank", value.getValueRank());
      encoder.encodeUInt32Array("ArrayDimensions", value.getArrayDimensions());
      encoder.encodeUInt32("MaxStringLength", value.getMaxStringLength());
      encoder.encodeGuid("DataSetFieldId", value.getDataSetFieldId());
      encoder.encodeStructArray("Properties", value.getProperties(), KeyValuePair.TYPE_ID);
    }
  }
}
