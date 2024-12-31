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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.7</a>
 */
public class DeleteReferencesItem extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=385");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=387");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=386");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15175");

  private final NodeId sourceNodeId;

  private final NodeId referenceTypeId;

  private final Boolean isForward;

  private final ExpandedNodeId targetNodeId;

  private final Boolean deleteBidirectional;

  public DeleteReferencesItem(
      NodeId sourceNodeId,
      NodeId referenceTypeId,
      Boolean isForward,
      ExpandedNodeId targetNodeId,
      Boolean deleteBidirectional) {
    this.sourceNodeId = sourceNodeId;
    this.referenceTypeId = referenceTypeId;
    this.isForward = isForward;
    this.targetNodeId = targetNodeId;
    this.deleteBidirectional = deleteBidirectional;
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

  public NodeId getSourceNodeId() {
    return sourceNodeId;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsForward() {
    return isForward;
  }

  public ExpandedNodeId getTargetNodeId() {
    return targetNodeId;
  }

  public Boolean getDeleteBidirectional() {
    return deleteBidirectional;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteReferencesItem that = (DeleteReferencesItem) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSourceNodeId(), that.getSourceNodeId());
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsForward(), that.getIsForward());
    eqb.append(getTargetNodeId(), that.getTargetNodeId());
    eqb.append(getDeleteBidirectional(), that.getDeleteBidirectional());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSourceNodeId());
    hcb.append(getReferenceTypeId());
    hcb.append(getIsForward());
    hcb.append(getTargetNodeId());
    hcb.append(getDeleteBidirectional());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteReferencesItem.class.getSimpleName() + "[", "]");
    joiner.add("sourceNodeId=" + getSourceNodeId());
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isForward=" + getIsForward());
    joiner.add("targetNodeId=" + getTargetNodeId());
    joiner.add("deleteBidirectional=" + getDeleteBidirectional());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 387),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SourceNodeId",
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
              "TargetNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeleteBidirectional",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteReferencesItem> {
    @Override
    public Class<DeleteReferencesItem> getType() {
      return DeleteReferencesItem.class;
    }

    @Override
    public DeleteReferencesItem decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId sourceNodeId = decoder.decodeNodeId("SourceNodeId");
      NodeId referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      Boolean isForward = decoder.decodeBoolean("IsForward");
      ExpandedNodeId targetNodeId = decoder.decodeExpandedNodeId("TargetNodeId");
      Boolean deleteBidirectional = decoder.decodeBoolean("DeleteBidirectional");
      return new DeleteReferencesItem(
          sourceNodeId, referenceTypeId, isForward, targetNodeId, deleteBidirectional);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DeleteReferencesItem value) {
      encoder.encodeNodeId("SourceNodeId", value.getSourceNodeId());
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsForward", value.getIsForward());
      encoder.encodeExpandedNodeId("TargetNodeId", value.getTargetNodeId());
      encoder.encodeBoolean("DeleteBidirectional", value.getDeleteBidirectional());
    }
  }
}
