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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.3</a>
 */
public class NumberRange extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23903");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=24250");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=24352");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24368");

  private final Number low;

  private final Number high;

  public NumberRange(Number low, Number high) {
    this.low = low;
    this.high = high;
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

  public Number getLow() {
    return low;
  }

  public Number getHigh() {
    return high;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NumberRange that = (NumberRange) object;
    var eqb = new EqualsBuilder();
    eqb.append(getLow(), that.getLow());
    eqb.append(getHigh(), that.getHigh());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getLow());
    hcb.append(getHigh());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NumberRange.class.getSimpleName() + "[", "]");
    joiner.add("low=" + getLow());
    joiner.add("high=" + getHigh());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24250),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Low",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 26),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "High",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 26),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NumberRange> {
    @Override
    public Class<NumberRange> getType() {
      return NumberRange.class;
    }

    @Override
    public NumberRange decodeType(EncodingContext context, UaDecoder decoder) {
      final Number low;
      final Number high;
      {
        Variant variant = decoder.decodeVariant("Low");
        low = (Number) variant.getValue();
      }
      {
        Variant variant = decoder.decodeVariant("High");
        high = (Number) variant.getValue();
      }
      return new NumberRange(low, high);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, NumberRange value) {
      {
        Variant variant = Variant.of(value.getLow());
        encoder.encodeVariant("Low", variant);
      }
      {
        Variant variant = Variant.of(value.getHigh());
        encoder.encodeVariant("High", variant);
      }
    }
  }
}
