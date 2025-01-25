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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class StructWithStructureArrayFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3029");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5053");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5055");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5054");

  private final ExtensionObject @Nullable [] struct1;

  private final ExtensionObject @Nullable [] struct2;

  private final ExtensionObject @Nullable [] struct3;

  public StructWithStructureArrayFields(
      ExtensionObject @Nullable [] struct1,
      ExtensionObject @Nullable [] struct2,
      ExtensionObject @Nullable [] struct3) {
    this.struct1 = struct1;
    this.struct2 = struct2;
    this.struct3 = struct3;
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

  public ExtensionObject @Nullable [] getStruct1() {
    return struct1;
  }

  public ExtensionObject @Nullable [] getStruct2() {
    return struct2;
  }

  public ExtensionObject @Nullable [] getStruct3() {
    return struct3;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithStructureArrayFields that = (StructWithStructureArrayFields) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStruct1(), that.getStruct1());
    eqb.append(getStruct2(), that.getStruct2());
    eqb.append(getStruct3(), that.getStruct3());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStruct1());
    hcb.append(getStruct2());
    hcb.append(getStruct3());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithStructureArrayFields.class.getSimpleName() + "[", "]");
    joiner.add("struct1=" + java.util.Arrays.toString(getStruct1()));
    joiner.add("struct2=" + java.util.Arrays.toString(getStruct2()));
    joiner.add("struct3=" + java.util.Arrays.toString(getStruct3()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5053")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Struct1",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Struct2",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Struct3",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithStructureArrayFields> {
    @Override
    public Class<StructWithStructureArrayFields> getType() {
      return StructWithStructureArrayFields.class;
    }

    @Override
    public StructWithStructureArrayFields decodeType(EncodingContext context, UaDecoder decoder) {
      final ExtensionObject[] struct1;
      final ExtensionObject[] struct2;
      final ExtensionObject[] struct3;
      struct1 = decoder.decodeExtensionObjectArray("Struct1");
      struct2 = decoder.decodeExtensionObjectArray("Struct2");
      struct3 = decoder.decodeExtensionObjectArray("Struct3");
      return new StructWithStructureArrayFields(struct1, struct2, struct3);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithStructureArrayFields value) {
      encoder.encodeExtensionObjectArray("Struct1", value.getStruct1());
      encoder.encodeExtensionObjectArray("Struct2", value.getStruct2());
      encoder.encodeExtensionObjectArray("Struct3", value.getStruct3());
    }
  }
}
