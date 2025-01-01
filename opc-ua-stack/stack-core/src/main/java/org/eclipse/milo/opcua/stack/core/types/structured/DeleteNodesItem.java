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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.6</a>
 */
public class DeleteNodesItem extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=382");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=384");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=383");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15172");

  private final NodeId nodeId;

  private final Boolean deleteTargetReferences;

  public DeleteNodesItem(NodeId nodeId, Boolean deleteTargetReferences) {
    this.nodeId = nodeId;
    this.deleteTargetReferences = deleteTargetReferences;
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

  public Boolean getDeleteTargetReferences() {
    return deleteTargetReferences;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteNodesItem that = (DeleteNodesItem) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getDeleteTargetReferences(), that.getDeleteTargetReferences());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getDeleteTargetReferences());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteNodesItem.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("deleteTargetReferences=" + getDeleteTargetReferences());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 384),
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
              "DeleteTargetReferences",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteNodesItem> {
    @Override
    public Class<DeleteNodesItem> getType() {
      return DeleteNodesItem.class;
    }

    @Override
    public DeleteNodesItem decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId nodeId = decoder.decodeNodeId("NodeId");
      Boolean deleteTargetReferences = decoder.decodeBoolean("DeleteTargetReferences");
      return new DeleteNodesItem(nodeId, deleteTargetReferences);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DeleteNodesItem value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeBoolean("DeleteTargetReferences", value.getDeleteTargetReferences());
    }
  }
}
