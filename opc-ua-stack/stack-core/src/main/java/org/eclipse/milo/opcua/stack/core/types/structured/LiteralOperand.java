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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.3</a>
 */
public class LiteralOperand extends FilterOperand implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=595");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=597");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=596");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15208");

  private final Variant value;

  public LiteralOperand(Variant value) {
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
    LiteralOperand that = (LiteralOperand) object;
    var eqb = new EqualsBuilder();
    eqb.append(getValue(), that.getValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LiteralOperand.class.getSimpleName() + "[", "]");
    joiner.add("value=" + getValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 597),
        new NodeId(0, 589),
        StructureType.Structure,
        new StructureField[] {
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

  public static final class Codec extends GenericDataTypeCodec<LiteralOperand> {
    @Override
    public Class<LiteralOperand> getType() {
      return LiteralOperand.class;
    }

    @Override
    public LiteralOperand decodeType(EncodingContext context, UaDecoder decoder) {
      Variant value = decoder.decodeVariant("Value");
      return new LiteralOperand(value);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, LiteralOperand value) {
      encoder.encodeVariant("Value", value.getValue());
    }
  }
}
