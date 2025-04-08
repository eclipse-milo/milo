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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.2</a>
 */
public class LinearConversionDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=32435");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=32561");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=32573");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=32585");

  private final Float initialAddend;

  private final Float multiplicand;

  private final Float divisor;

  private final Float finalAddend;

  public LinearConversionDataType(
      Float initialAddend, Float multiplicand, Float divisor, Float finalAddend) {
    this.initialAddend = initialAddend;
    this.multiplicand = multiplicand;
    this.divisor = divisor;
    this.finalAddend = finalAddend;
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

  public Float getInitialAddend() {
    return initialAddend;
  }

  public Float getMultiplicand() {
    return multiplicand;
  }

  public Float getDivisor() {
    return divisor;
  }

  public Float getFinalAddend() {
    return finalAddend;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LinearConversionDataType that = (LinearConversionDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getInitialAddend(), that.getInitialAddend());
    eqb.append(getMultiplicand(), that.getMultiplicand());
    eqb.append(getDivisor(), that.getDivisor());
    eqb.append(getFinalAddend(), that.getFinalAddend());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getInitialAddend());
    hcb.append(getMultiplicand());
    hcb.append(getDivisor());
    hcb.append(getFinalAddend());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LinearConversionDataType.class.getSimpleName() + "[", "]");
    joiner.add("initialAddend=" + getInitialAddend());
    joiner.add("multiplicand=" + getMultiplicand());
    joiner.add("divisor=" + getDivisor());
    joiner.add("finalAddend=" + getFinalAddend());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32561),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "InitialAddend",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Multiplicand",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Divisor",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FinalAddend",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LinearConversionDataType> {
    @Override
    public Class<LinearConversionDataType> getType() {
      return LinearConversionDataType.class;
    }

    @Override
    public LinearConversionDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final Float initialAddend;
      final Float multiplicand;
      final Float divisor;
      final Float finalAddend;
      initialAddend = decoder.decodeFloat("InitialAddend");
      multiplicand = decoder.decodeFloat("Multiplicand");
      divisor = decoder.decodeFloat("Divisor");
      finalAddend = decoder.decodeFloat("FinalAddend");
      return new LinearConversionDataType(initialAddend, multiplicand, divisor, finalAddend);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, LinearConversionDataType value) {
      encoder.encodeFloat("InitialAddend", value.getInitialAddend());
      encoder.encodeFloat("Multiplicand", value.getMultiplicand());
      encoder.encodeFloat("Divisor", value.getDivisor());
      encoder.encodeFloat("FinalAddend", value.getFinalAddend());
    }
  }
}
