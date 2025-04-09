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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.45">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.45</a>
 */
public class BitFieldDefinition extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=32421");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=32422");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=32426");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=32430");

  private final @Nullable String name;

  private final LocalizedText description;

  private final Boolean reserved;

  private final UInteger startingBitPosition;

  private final UInteger endingBitPosition;

  public BitFieldDefinition(
      @Nullable String name,
      LocalizedText description,
      Boolean reserved,
      UInteger startingBitPosition,
      UInteger endingBitPosition) {
    this.name = name;
    this.description = description;
    this.reserved = reserved;
    this.startingBitPosition = startingBitPosition;
    this.endingBitPosition = endingBitPosition;
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

  public Boolean getReserved() {
    return reserved;
  }

  public UInteger getStartingBitPosition() {
    return startingBitPosition;
  }

  public UInteger getEndingBitPosition() {
    return endingBitPosition;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BitFieldDefinition that = (BitFieldDefinition) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getReserved(), that.getReserved());
    eqb.append(getStartingBitPosition(), that.getStartingBitPosition());
    eqb.append(getEndingBitPosition(), that.getEndingBitPosition());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getDescription());
    hcb.append(getReserved());
    hcb.append(getStartingBitPosition());
    hcb.append(getEndingBitPosition());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BitFieldDefinition.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("description=" + getDescription());
    joiner.add("reserved=" + getReserved());
    joiner.add("startingBitPosition=" + getStartingBitPosition());
    joiner.add("endingBitPosition=" + getEndingBitPosition());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32422),
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
              "Reserved",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StartingBitPosition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndingBitPosition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BitFieldDefinition> {
    @Override
    public Class<BitFieldDefinition> getType() {
      return BitFieldDefinition.class;
    }

    @Override
    public BitFieldDefinition decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final LocalizedText description;
      final Boolean reserved;
      final UInteger startingBitPosition;
      final UInteger endingBitPosition;
      name = decoder.decodeString("Name");
      description = decoder.decodeLocalizedText("Description");
      reserved = decoder.decodeBoolean("Reserved");
      startingBitPosition = decoder.decodeUInt32("StartingBitPosition");
      endingBitPosition = decoder.decodeUInt32("EndingBitPosition");
      return new BitFieldDefinition(
          name, description, reserved, startingBitPosition, endingBitPosition);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BitFieldDefinition value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeBoolean("Reserved", value.getReserved());
      encoder.encodeUInt32("StartingBitPosition", value.getStartingBitPosition());
      encoder.encodeUInt32("EndingBitPosition", value.getEndingBitPosition());
    }
  }
}
