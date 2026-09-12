/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/8.39">https://reference.opcfoundation.org/v105/Core/docs/Part3/8.39</a>
 */
public class EnumValueType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=7594");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=8251");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=7616");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15082");

  private final Long value2;

  private final LocalizedText displayName;

  private final LocalizedText description;

  public EnumValueType(Long value2, LocalizedText displayName, LocalizedText description) {
    this.value2 = value2;
    this.displayName = displayName;
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

  public Long getValue() {
    return value2;
  }

  public LocalizedText getDisplayName() {
    return displayName;
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
    EnumValueType that = (EnumValueType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getValue(), that.getValue());
    eqb.append(getDisplayName(), that.getDisplayName());
    eqb.append(getDescription(), that.getDescription());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getValue());
    hcb.append(getDisplayName());
    hcb.append(getDescription());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EnumValueType.class.getSimpleName() + "[", "]");
    joiner.add("value=" + getValue());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("description=" + getDescription());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        NodeId.parse("i=8251"),
        NodeId.parse("i=22"),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Value",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=8"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=21"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=21"),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EnumValueType> {
    @Override
    public Class<EnumValueType> getType() {
      return EnumValueType.class;
    }

    @Override
    public EnumValueType decodeType(EncodingContext context, UaDecoder decoder) {
      final Long value2;
      final LocalizedText displayName;
      final LocalizedText description;
      value2 = decoder.decodeInt64("Value");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      return new EnumValueType(value2, displayName, description);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EnumValueType value) {
      encoder.encodeInt64("Value", value.getValue());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
    }
  }
}
