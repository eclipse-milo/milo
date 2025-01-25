/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.test.types;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

public class StructWithAbstractScalarFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3012");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5007");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5009");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5008");

  private final Number number;

  private final AbstractTestType att1;

  private final AbstractTestType att2;

  public StructWithAbstractScalarFields(
      Number number, AbstractTestType att1, AbstractTestType att2) {
    this.number = number;
    this.att1 = att1;
    this.att2 = att2;
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
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  public Number getNumber() {
    return number;
  }

  public AbstractTestType getAtt1() {
    return att1;
  }

  public AbstractTestType getAtt2() {
    return att2;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithAbstractScalarFields that = (StructWithAbstractScalarFields) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNumber(), that.getNumber());
    eqb.append(getAtt1(), that.getAtt1());
    eqb.append(getAtt2(), that.getAtt2());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNumber());
    hcb.append(getAtt1());
    hcb.append(getAtt2());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithAbstractScalarFields.class.getSimpleName() + "[", "]");
    joiner.add("number=" + getNumber());
    joiner.add("att1=" + getAtt1());
    joiner.add("att2=" + getAtt2());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5007")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.StructureWithSubtypedValues,
        new StructureField[] {
          new StructureField(
              "Number",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 26),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT1",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT2",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithAbstractScalarFields> {
    @Override
    public Class<StructWithAbstractScalarFields> getType() {
      return StructWithAbstractScalarFields.class;
    }

    @Override
    public StructWithAbstractScalarFields decodeType(EncodingContext context, UaDecoder decoder) {
      final Number number;
      final AbstractTestType att1;
      final AbstractTestType att2;
      {
        Variant variant = decoder.decodeVariant("Number");
        number = (Number) variant.getValue();
      }
      {
        ExtensionObject xo = decoder.decodeExtensionObject("ATT1");
        att1 = (AbstractTestType) xo.decode(context);
      }
      {
        ExtensionObject xo = decoder.decodeExtensionObject("ATT2");
        att2 = (AbstractTestType) xo.decode(context);
      }
      return new StructWithAbstractScalarFields(number, att1, att2);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithAbstractScalarFields value) {
      {
        Variant variant = Variant.of(value.getNumber());
        encoder.encodeVariant("Number", variant);
      }
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getAtt1());
        encoder.encodeExtensionObject("ATT1", xo);
      }
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getAtt2());
        encoder.encodeExtensionObject("ATT2", xo);
      }
    }
  }
}
