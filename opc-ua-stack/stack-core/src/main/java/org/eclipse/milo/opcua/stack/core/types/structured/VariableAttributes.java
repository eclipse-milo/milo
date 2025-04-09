/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.3</a>
 */
public class VariableAttributes extends NodeAttributes implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=355");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=357");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=356");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15153");

  private final Variant value;

  private final NodeId dataType;

  private final Integer valueRank;

  private final UInteger @Nullable [] arrayDimensions;

  private final UByte accessLevel;

  private final UByte userAccessLevel;

  private final Double minimumSamplingInterval;

  private final Boolean historizing;

  public VariableAttributes(
      UInteger specifiedAttributes,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Variant value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing) {
    super(specifiedAttributes, displayName, description, writeMask, userWriteMask);
    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.accessLevel = accessLevel;
    this.userAccessLevel = userAccessLevel;
    this.minimumSamplingInterval = minimumSamplingInterval;
    this.historizing = historizing;
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

  public Variant getValue() {
    return value;
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

  public UByte getAccessLevel() {
    return accessLevel;
  }

  public UByte getUserAccessLevel() {
    return userAccessLevel;
  }

  public Double getMinimumSamplingInterval() {
    return minimumSamplingInterval;
  }

  public Boolean getHistorizing() {
    return historizing;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    VariableAttributes that = (VariableAttributes) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getValue(), that.getValue());
    eqb.append(getDataType(), that.getDataType());
    eqb.append(getValueRank(), that.getValueRank());
    eqb.append(getArrayDimensions(), that.getArrayDimensions());
    eqb.append(getAccessLevel(), that.getAccessLevel());
    eqb.append(getUserAccessLevel(), that.getUserAccessLevel());
    eqb.append(getMinimumSamplingInterval(), that.getMinimumSamplingInterval());
    eqb.append(getHistorizing(), that.getHistorizing());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getValue());
    hcb.append(getDataType());
    hcb.append(getValueRank());
    hcb.append(getArrayDimensions());
    hcb.append(getAccessLevel());
    hcb.append(getUserAccessLevel());
    hcb.append(getMinimumSamplingInterval());
    hcb.append(getHistorizing());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", VariableAttributes.class.getSimpleName() + "[", "]");
    joiner.add("value=" + getValue());
    joiner.add("dataType=" + getDataType());
    joiner.add("valueRank=" + getValueRank());
    joiner.add("arrayDimensions=" + java.util.Arrays.toString(getArrayDimensions()));
    joiner.add("accessLevel=" + getAccessLevel());
    joiner.add("userAccessLevel=" + getUserAccessLevel());
    joiner.add("minimumSamplingInterval=" + getMinimumSamplingInterval());
    joiner.add("historizing=" + getHistorizing());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 357),
        new NodeId(0, 349),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SpecifiedAttributes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
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
              "WriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserWriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Value",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
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
              "AccessLevel",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserAccessLevel",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MinimumSamplingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Historizing",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<VariableAttributes> {
    @Override
    public Class<VariableAttributes> getType() {
      return VariableAttributes.class;
    }

    @Override
    public VariableAttributes decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger specifiedAttributes;
      final LocalizedText displayName;
      final LocalizedText description;
      final UInteger writeMask;
      final UInteger userWriteMask;
      final Variant value;
      final NodeId dataType;
      final Integer valueRank;
      final UInteger[] arrayDimensions;
      final UByte accessLevel;
      final UByte userAccessLevel;
      final Double minimumSamplingInterval;
      final Boolean historizing;
      specifiedAttributes = decoder.decodeUInt32("SpecifiedAttributes");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      writeMask = decoder.decodeUInt32("WriteMask");
      userWriteMask = decoder.decodeUInt32("UserWriteMask");
      value = decoder.decodeVariant("Value");
      dataType = decoder.decodeNodeId("DataType");
      valueRank = decoder.decodeInt32("ValueRank");
      arrayDimensions = decoder.decodeUInt32Array("ArrayDimensions");
      accessLevel = decoder.decodeByte("AccessLevel");
      userAccessLevel = decoder.decodeByte("UserAccessLevel");
      minimumSamplingInterval = decoder.decodeDouble("MinimumSamplingInterval");
      historizing = decoder.decodeBoolean("Historizing");
      return new VariableAttributes(
          specifiedAttributes,
          displayName,
          description,
          writeMask,
          userWriteMask,
          value,
          dataType,
          valueRank,
          arrayDimensions,
          accessLevel,
          userAccessLevel,
          minimumSamplingInterval,
          historizing);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, VariableAttributes value) {
      encoder.encodeUInt32("SpecifiedAttributes", value.getSpecifiedAttributes());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt32("WriteMask", value.getWriteMask());
      encoder.encodeUInt32("UserWriteMask", value.getUserWriteMask());
      encoder.encodeVariant("Value", value.getValue());
      encoder.encodeNodeId("DataType", value.getDataType());
      encoder.encodeInt32("ValueRank", value.getValueRank());
      encoder.encodeUInt32Array("ArrayDimensions", value.getArrayDimensions());
      encoder.encodeByte("AccessLevel", value.getAccessLevel());
      encoder.encodeByte("UserAccessLevel", value.getUserAccessLevel());
      encoder.encodeDouble("MinimumSamplingInterval", value.getMinimumSamplingInterval());
      encoder.encodeBoolean("Historizing", value.getHistorizing());
    }
  }
}
