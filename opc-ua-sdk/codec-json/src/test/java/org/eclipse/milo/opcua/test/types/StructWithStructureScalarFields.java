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

public class StructWithStructureScalarFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3026");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5050");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5051");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5052");

  private final ExtensionObject struct1;

  private final ExtensionObject struct2;

  private final ExtensionObject struct3;

  public StructWithStructureScalarFields(
      ExtensionObject struct1, ExtensionObject struct2, ExtensionObject struct3) {
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

  public ExtensionObject getStruct1() {
    return struct1;
  }

  public ExtensionObject getStruct2() {
    return struct2;
  }

  public ExtensionObject getStruct3() {
    return struct3;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithStructureScalarFields that = (StructWithStructureScalarFields) object;
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
        new StringJoiner(", ", StructWithStructureScalarFields.class.getSimpleName() + "[", "]");
    joiner.add("struct1=" + getStruct1());
    joiner.add("struct2=" + getStruct2());
    joiner.add("struct3=" + getStruct3());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5050")
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
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Struct2",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Struct3",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithStructureScalarFields> {
    @Override
    public Class<StructWithStructureScalarFields> getType() {
      return StructWithStructureScalarFields.class;
    }

    @Override
    public StructWithStructureScalarFields decodeType(EncodingContext context, UaDecoder decoder) {
      final ExtensionObject struct1;
      final ExtensionObject struct2;
      final ExtensionObject struct3;
      struct1 = decoder.decodeExtensionObject("Struct1");
      struct2 = decoder.decodeExtensionObject("Struct2");
      struct3 = decoder.decodeExtensionObject("Struct3");
      return new StructWithStructureScalarFields(struct1, struct2, struct3);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithStructureScalarFields value) {
      encoder.encodeExtensionObject("Struct1", value.getStruct1());
      encoder.encodeExtensionObject("Struct2", value.getStruct2());
      encoder.encodeExtensionObject("Struct3", value.getStruct3());
    }
  }
}
