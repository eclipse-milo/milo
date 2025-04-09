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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.1</a>
 */
public class Argument extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=296");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=298");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=297");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15081");

  private final @Nullable String name;

  private final NodeId dataType;

  private final Integer valueRank;

  private final UInteger @Nullable [] arrayDimensions;

  private final LocalizedText description;

  public Argument(
      @Nullable String name,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      LocalizedText description) {
    this.name = name;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.description = description;
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

  public NodeId getDataType() {
    return dataType;
  }

  public Integer getValueRank() {
    return valueRank;
  }

  public UInteger @Nullable [] getArrayDimensions() {
    return arrayDimensions;
  }

  public LocalizedText getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Argument that = (Argument) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getDataType(), that.getDataType());
    eqb.append(getValueRank(), that.getValueRank());
    eqb.append(getArrayDimensions(), that.getArrayDimensions());
    eqb.append(getDescription(), that.getDescription());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getDataType());
    hcb.append(getValueRank());
    hcb.append(getArrayDimensions());
    hcb.append(getDescription());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", Argument.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("dataType=" + getDataType());
    joiner.add("valueRank=" + getValueRank());
    joiner.add("arrayDimensions=" + java.util.Arrays.toString(getArrayDimensions()));
    joiner.add("description=" + getDescription());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 298),
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
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<Argument> {
    @Override
    public Class<Argument> getType() {
      return Argument.class;
    }

    @Override
    public Argument decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final NodeId dataType;
      final Integer valueRank;
      final UInteger[] arrayDimensions;
      final LocalizedText description;
      name = decoder.decodeString("Name");
      dataType = decoder.decodeNodeId("DataType");
      valueRank = decoder.decodeInt32("ValueRank");
      arrayDimensions = decoder.decodeUInt32Array("ArrayDimensions");
      description = decoder.decodeLocalizedText("Description");
      return new Argument(name, dataType, valueRank, arrayDimensions, description);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, Argument value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeNodeId("DataType", value.getDataType());
      encoder.encodeInt32("ValueRank", value.getValueRank());
      encoder.encodeUInt32Array("ArrayDimensions", value.getArrayDimensions());
      encoder.encodeLocalizedText("Description", value.getDescription());
    }
  }
}
