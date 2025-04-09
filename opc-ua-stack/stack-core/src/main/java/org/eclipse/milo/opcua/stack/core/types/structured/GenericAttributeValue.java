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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.10">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.10</a>
 */
public class GenericAttributeValue extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=17606");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=17610");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=17608");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15163");

  private final UInteger attributeId;

  private final Variant value;

  public GenericAttributeValue(UInteger attributeId, Variant value) {
    this.attributeId = attributeId;
    this.value = value;
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

  public UInteger getAttributeId() {
    return attributeId;
  }

  public Variant getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    GenericAttributeValue that = (GenericAttributeValue) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getValue(), that.getValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAttributeId());
    hcb.append(getValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", GenericAttributeValue.class.getSimpleName() + "[", "]");
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("value=" + getValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 17610),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "AttributeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<GenericAttributeValue> {
    @Override
    public Class<GenericAttributeValue> getType() {
      return GenericAttributeValue.class;
    }

    @Override
    public GenericAttributeValue decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger attributeId;
      final Variant value;
      attributeId = decoder.decodeUInt32("AttributeId");
      value = decoder.decodeVariant("Value");
      return new GenericAttributeValue(attributeId, value);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, GenericAttributeValue value) {
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeVariant("Value", value.getValue());
    }
  }
}
