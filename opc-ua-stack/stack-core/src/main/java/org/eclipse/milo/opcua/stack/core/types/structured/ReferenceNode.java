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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

public class ReferenceNode extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=285");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=287");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=286");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15080");

  private final NodeId referenceTypeId;

  private final Boolean isInverse;

  private final ExpandedNodeId targetId;

  public ReferenceNode(NodeId referenceTypeId, Boolean isInverse, ExpandedNodeId targetId) {
    this.referenceTypeId = referenceTypeId;
    this.isInverse = isInverse;
    this.targetId = targetId;
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

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsInverse() {
    return isInverse;
  }

  public ExpandedNodeId getTargetId() {
    return targetId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReferenceNode that = (ReferenceNode) object;
    var eqb = new EqualsBuilder();
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsInverse(), that.getIsInverse());
    eqb.append(getTargetId(), that.getTargetId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReferenceTypeId());
    hcb.append(getIsInverse());
    hcb.append(getTargetId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReferenceNode.class.getSimpleName() + "[", "]");
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isInverse=" + getIsInverse());
    joiner.add("targetId=" + getTargetId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 287),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ReferenceTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsInverse",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReferenceNode> {
    @Override
    public Class<ReferenceNode> getType() {
      return ReferenceNode.class;
    }

    @Override
    public ReferenceNode decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      Boolean isInverse = decoder.decodeBoolean("IsInverse");
      ExpandedNodeId targetId = decoder.decodeExpandedNodeId("TargetId");
      return new ReferenceNode(referenceTypeId, isInverse, targetId);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReferenceNode value) {
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsInverse", value.getIsInverse());
      encoder.encodeExpandedNodeId("TargetId", value.getTargetId());
    }
  }
}
