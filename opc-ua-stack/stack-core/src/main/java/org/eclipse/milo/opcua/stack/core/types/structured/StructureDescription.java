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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.33">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.33</a>
 */
public class StructureDescription extends DataTypeDescription implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15487");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=126");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15589");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15058");

  private final StructureDefinition structureDefinition;

  public StructureDescription(
      NodeId dataTypeId, QualifiedName name, StructureDefinition structureDefinition) {
    super(dataTypeId, name);
    this.structureDefinition = structureDefinition;
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

  public StructureDefinition getStructureDefinition() {
    return structureDefinition;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructureDescription that = (StructureDescription) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getStructureDefinition(), that.getStructureDefinition());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStructureDefinition());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", StructureDescription.class.getSimpleName() + "[", "]");
    joiner.add("structureDefinition=" + getStructureDefinition());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 126),
        new NodeId(0, 14525),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StructureDefinition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 99),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructureDescription> {
    @Override
    public Class<StructureDescription> getType() {
      return StructureDescription.class;
    }

    @Override
    public StructureDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId dataTypeId;
      final QualifiedName name;
      final StructureDefinition structureDefinition;
      dataTypeId = decoder.decodeNodeId("DataTypeId");
      name = decoder.decodeQualifiedName("Name");
      structureDefinition =
          (StructureDefinition)
              decoder.decodeStruct("StructureDefinition", StructureDefinition.TYPE_ID);
      return new StructureDescription(dataTypeId, name, structureDefinition);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, StructureDescription value) {
      encoder.encodeNodeId("DataTypeId", value.getDataTypeId());
      encoder.encodeQualifiedName("Name", value.getName());
      encoder.encodeStruct(
          "StructureDefinition", value.getStructureDefinition(), StructureDefinition.TYPE_ID);
    }
  }
}
