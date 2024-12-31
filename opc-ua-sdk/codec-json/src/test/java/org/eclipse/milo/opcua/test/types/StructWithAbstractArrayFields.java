/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

public class StructWithAbstractArrayFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3005");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5023");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5025");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5024");

  private final Number @Nullable [] number;

  private final AbstractTestType @Nullable [] att1;

  private final AbstractTestType @Nullable [] att2;

  public StructWithAbstractArrayFields(
      Number @Nullable [] number,
      AbstractTestType @Nullable [] att1,
      AbstractTestType @Nullable [] att2) {
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

  public Number @Nullable [] getNumber() {
    return number;
  }

  public AbstractTestType @Nullable [] getAtt1() {
    return att1;
  }

  public AbstractTestType @Nullable [] getAtt2() {
    return att2;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithAbstractArrayFields that = (StructWithAbstractArrayFields) object;
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
        new StringJoiner(", ", StructWithAbstractArrayFields.class.getSimpleName() + "[", "]");
    joiner.add("number=" + java.util.Arrays.toString(getNumber()));
    joiner.add("att1=" + java.util.Arrays.toString(getAtt1()));
    joiner.add("att2=" + java.util.Arrays.toString(getAtt2()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5023")
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
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT1",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT2",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithAbstractArrayFields> {
    @Override
    public Class<StructWithAbstractArrayFields> getType() {
      return StructWithAbstractArrayFields.class;
    }

    @Override
    public StructWithAbstractArrayFields decodeType(EncodingContext context, UaDecoder decoder) {
      final Number[] number;
      final AbstractTestType[] att1;
      final AbstractTestType[] att2;
      {
        Variant[] variants = decoder.decodeVariantArray("Number");
        if (variants != null) {
          number = new Number[variants.length];
          for (int i = 0; i < number.length; i++) {
            number[i] = (Number) variants[i].getValue();
          }
        } else {
          number = null;
        }
      }
      {
        ExtensionObject[] xos = decoder.decodeExtensionObjectArray("ATT1");
        if (xos != null) {
          att1 = new AbstractTestType[xos.length];
          for (int i = 0; i < xos.length; i++) {
            att1[i] = (AbstractTestType) xos[i].decode(context);
          }
        } else {
          att1 = null;
        }
      }
      {
        ExtensionObject[] xos = decoder.decodeExtensionObjectArray("ATT2");
        if (xos != null) {
          att2 = new AbstractTestType[xos.length];
          for (int i = 0; i < xos.length; i++) {
            att2[i] = (AbstractTestType) xos[i].decode(context);
          }
        } else {
          att2 = null;
        }
      }
      return new StructWithAbstractArrayFields(number, att1, att2);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithAbstractArrayFields value) {
      {
        Variant[] variants = null;
        Number[] number = value.getNumber();
        if (number != null) {
          variants = new Variant[number.length];
          for (int i = 0; i < variants.length; i++) {
            variants[i] = Variant.of(number[i]);
          }
        }
        encoder.encodeVariantArray("Number", variants);
      }
      {
        ExtensionObject[] xos = null;
        AbstractTestType[] att1 = value.getAtt1();
        if (att1 != null) {
          xos = new ExtensionObject[att1.length];
          for (int i = 0; i < xos.length; i++) {
            xos[i] = ExtensionObject.encode(context, att1[i]);
          }
        }
        encoder.encodeExtensionObjectArray("ATT1", xos);
      }
      {
        ExtensionObject[] xos = null;
        AbstractTestType[] att2 = value.getAtt2();
        if (att2 != null) {
          xos = new ExtensionObject[att2.length];
          for (int i = 0; i < xos.length; i++) {
            xos[i] = ExtensionObject.encode(context, att2[i]);
          }
        }
        encoder.encodeExtensionObjectArray("ATT2", xos);
      }
    }
  }
}
