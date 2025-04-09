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
import org.jspecify.annotations.Nullable;

public class NodeReference extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=580");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=582");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=581");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15203");

  private final NodeId nodeId;

  private final NodeId referenceTypeId;

  private final Boolean isForward;

  private final NodeId @Nullable [] referencedNodeIds;

  public NodeReference(
      NodeId nodeId,
      NodeId referenceTypeId,
      Boolean isForward,
      NodeId @Nullable [] referencedNodeIds) {
    this.nodeId = nodeId;
    this.referenceTypeId = referenceTypeId;
    this.isForward = isForward;
    this.referencedNodeIds = referencedNodeIds;
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

  public NodeId getNodeId() {
    return nodeId;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsForward() {
    return isForward;
  }

  public NodeId @Nullable [] getReferencedNodeIds() {
    return referencedNodeIds;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NodeReference that = (NodeReference) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsForward(), that.getIsForward());
    eqb.append(getReferencedNodeIds(), that.getReferencedNodeIds());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getReferenceTypeId());
    hcb.append(getIsForward());
    hcb.append(getReferencedNodeIds());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NodeReference.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isForward=" + getIsForward());
    joiner.add("referencedNodeIds=" + java.util.Arrays.toString(getReferencedNodeIds()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 582),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReferenceTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsForward",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReferencedNodeIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NodeReference> {
    @Override
    public Class<NodeReference> getType() {
      return NodeReference.class;
    }

    @Override
    public NodeReference decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final NodeId referenceTypeId;
      final Boolean isForward;
      final NodeId[] referencedNodeIds;
      nodeId = decoder.decodeNodeId("NodeId");
      referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      isForward = decoder.decodeBoolean("IsForward");
      referencedNodeIds = decoder.decodeNodeIdArray("ReferencedNodeIds");
      return new NodeReference(nodeId, referenceTypeId, isForward, referencedNodeIds);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, NodeReference value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsForward", value.getIsForward());
      encoder.encodeNodeIdArray("ReferencedNodeIds", value.getReferencedNodeIds());
    }
  }
}
