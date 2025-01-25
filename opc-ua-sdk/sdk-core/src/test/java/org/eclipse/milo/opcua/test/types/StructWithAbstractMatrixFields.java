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
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
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

public class StructWithAbstractMatrixFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3022");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5047");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5049");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5048");

  private final @Nullable Matrix number;

  private final @Nullable Matrix att1;

  private final @Nullable Matrix att2;

  public StructWithAbstractMatrixFields(
      @Nullable Matrix number, @Nullable Matrix att1, @Nullable Matrix att2) {
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

  public @Nullable Matrix getNumber() {
    return number;
  }

  public @Nullable Matrix getAtt1() {
    return att1;
  }

  public @Nullable Matrix getAtt2() {
    return att2;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithAbstractMatrixFields that = (StructWithAbstractMatrixFields) object;
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
        new StringJoiner(", ", StructWithAbstractMatrixFields.class.getSimpleName() + "[", "]");
    joiner.add("number=" + getNumber());
    joiner.add("att1=" + getAtt1());
    joiner.add("att2=" + getAtt2());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5047")
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
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT1",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ATT2",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithAbstractMatrixFields> {
    @Override
    public Class<StructWithAbstractMatrixFields> getType() {
      return StructWithAbstractMatrixFields.class;
    }

    @Override
    public StructWithAbstractMatrixFields decodeType(EncodingContext context, UaDecoder decoder) {
      final Matrix number;
      final Matrix att1;
      final Matrix att2;
      {
        Matrix matrix = decoder.decodeMatrix("Number", BuiltinDataType.Variant);
        number = matrix.transform(v -> ((Variant) v).getValue());
      }
      {
        Matrix matrix = decoder.decodeMatrix("ATT1", BuiltinDataType.ExtensionObject);
        att1 = matrix.transform(v -> ((ExtensionObject) v).decode(context));
      }
      {
        Matrix matrix = decoder.decodeMatrix("ATT2", BuiltinDataType.ExtensionObject);
        att2 = matrix.transform(v -> ((ExtensionObject) v).decode(context));
      }
      return new StructWithAbstractMatrixFields(number, att1, att2);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithAbstractMatrixFields value) {
      {
        Matrix matrix;
        if (value.getNumber() != null) {
          matrix = value.getNumber().transform(Variant::of);
        } else {
          matrix = Matrix.ofNull();
        }
        encoder.encodeMatrix("Number", matrix);
      }
      {
        Matrix matrix;
        if (value.getAtt1() != null) {
          matrix =
              value.getAtt1().transform(v -> ExtensionObject.encode(context, (UaStructuredType) v));
        } else {
          matrix = Matrix.ofNull();
        }
        encoder.encodeMatrix("ATT1", matrix);
      }
      {
        Matrix matrix;
        if (value.getAtt2() != null) {
          matrix =
              value.getAtt2().transform(v -> ExtensionObject.encode(context, (UaStructuredType) v));
        } else {
          matrix = Matrix.ofNull();
        }
        encoder.encodeMatrix("ATT2", matrix);
      }
    }
  }
}
