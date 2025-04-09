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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2</a>
 */
public class HistoryReadValueId extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=635");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=637");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=636");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15259");

  private final NodeId nodeId;

  private final String indexRange;

  private final QualifiedName dataEncoding;

  private final ByteString continuationPoint;

  public HistoryReadValueId(
      NodeId nodeId, String indexRange, QualifiedName dataEncoding, ByteString continuationPoint) {
    this.nodeId = nodeId;
    this.indexRange = indexRange;
    this.dataEncoding = dataEncoding;
    this.continuationPoint = continuationPoint;
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

  public String getIndexRange() {
    return indexRange;
  }

  public QualifiedName getDataEncoding() {
    return dataEncoding;
  }

  public ByteString getContinuationPoint() {
    return continuationPoint;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryReadValueId that = (HistoryReadValueId) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getIndexRange(), that.getIndexRange());
    eqb.append(getDataEncoding(), that.getDataEncoding());
    eqb.append(getContinuationPoint(), that.getContinuationPoint());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getIndexRange());
    hcb.append(getDataEncoding());
    hcb.append(getContinuationPoint());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryReadValueId.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("indexRange='" + getIndexRange() + "'");
    joiner.add("dataEncoding=" + getDataEncoding());
    joiner.add("continuationPoint=" + getContinuationPoint());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 637),
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
              "IndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataEncoding",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryReadValueId> {
    @Override
    public Class<HistoryReadValueId> getType() {
      return HistoryReadValueId.class;
    }

    @Override
    public HistoryReadValueId decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final String indexRange;
      final QualifiedName dataEncoding;
      final ByteString continuationPoint;
      nodeId = decoder.decodeNodeId("NodeId");
      indexRange = decoder.decodeString("IndexRange");
      dataEncoding = decoder.decodeQualifiedName("DataEncoding");
      continuationPoint = decoder.decodeByteString("ContinuationPoint");
      return new HistoryReadValueId(nodeId, indexRange, dataEncoding, continuationPoint);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryReadValueId value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeString("IndexRange", value.getIndexRange());
      encoder.encodeQualifiedName("DataEncoding", value.getDataEncoding());
      encoder.encodeByteString("ContinuationPoint", value.getContinuationPoint());
    }
  }
}
