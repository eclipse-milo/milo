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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.6/#6.9.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.6/#6.9.6.1</a>
 */
public class DeleteAtTimeDetails extends HistoryUpdateDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=689");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=691");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=690");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15284");

  private final NodeId nodeId;

  private final DateTime @Nullable [] reqTimes;

  public DeleteAtTimeDetails(NodeId nodeId, DateTime @Nullable [] reqTimes) {
    this.nodeId = nodeId;
    this.reqTimes = reqTimes;
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

  public DateTime @Nullable [] getReqTimes() {
    return reqTimes;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteAtTimeDetails that = (DeleteAtTimeDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getReqTimes(), that.getReqTimes());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getReqTimes());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteAtTimeDetails.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("reqTimes=" + java.util.Arrays.toString(getReqTimes()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 691),
        new NodeId(0, 677),
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
              "ReqTimes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteAtTimeDetails> {
    @Override
    public Class<DeleteAtTimeDetails> getType() {
      return DeleteAtTimeDetails.class;
    }

    @Override
    public DeleteAtTimeDetails decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final DateTime[] reqTimes;
      nodeId = decoder.decodeNodeId("NodeId");
      reqTimes = decoder.decodeDateTimeArray("ReqTimes");
      return new DeleteAtTimeDetails(nodeId, reqTimes);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DeleteAtTimeDetails value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeDateTimeArray("ReqTimes", value.getReqTimes());
    }
  }
}
