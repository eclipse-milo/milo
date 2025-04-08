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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.5</a>
 */
public class DoubleComplexNumberType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12172");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12182");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12174");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15378");

  private final Double real;

  private final Double imaginary;

  public DoubleComplexNumberType(Double real, Double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
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

  public Double getReal() {
    return real;
  }

  public Double getImaginary() {
    return imaginary;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DoubleComplexNumberType that = (DoubleComplexNumberType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getReal(), that.getReal());
    eqb.append(getImaginary(), that.getImaginary());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReal());
    hcb.append(getImaginary());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DoubleComplexNumberType.class.getSimpleName() + "[", "]");
    joiner.add("real=" + getReal());
    joiner.add("imaginary=" + getImaginary());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12182),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Real",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Imaginary",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DoubleComplexNumberType> {
    @Override
    public Class<DoubleComplexNumberType> getType() {
      return DoubleComplexNumberType.class;
    }

    @Override
    public DoubleComplexNumberType decodeType(EncodingContext context, UaDecoder decoder) {
      final Double real;
      final Double imaginary;
      real = decoder.decodeDouble("Real");
      imaginary = decoder.decodeDouble("Imaginary");
      return new DoubleComplexNumberType(real, imaginary);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DoubleComplexNumberType value) {
      encoder.encodeDouble("Real", value.getReal());
      encoder.encodeDouble("Imaginary", value.getImaginary());
    }
  }
}
