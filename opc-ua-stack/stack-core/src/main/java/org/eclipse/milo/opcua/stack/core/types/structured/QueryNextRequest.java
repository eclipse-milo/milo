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
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2</a>
 */
public class QueryNextRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=619");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=621");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=620");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15254");

  private final RequestHeader requestHeader;

  private final Boolean releaseContinuationPoint;

  private final ByteString continuationPoint;

  public QueryNextRequest(
      RequestHeader requestHeader, Boolean releaseContinuationPoint, ByteString continuationPoint) {
    this.requestHeader = requestHeader;
    this.releaseContinuationPoint = releaseContinuationPoint;
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

  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public Boolean getReleaseContinuationPoint() {
    return releaseContinuationPoint;
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
    QueryNextRequest that = (QueryNextRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getReleaseContinuationPoint(), that.getReleaseContinuationPoint());
    eqb.append(getContinuationPoint(), that.getContinuationPoint());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getReleaseContinuationPoint());
    hcb.append(getContinuationPoint());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QueryNextRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("releaseContinuationPoint=" + getReleaseContinuationPoint());
    joiner.add("continuationPoint=" + getContinuationPoint());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 621),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 389),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReleaseContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
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

  public static final class Codec extends GenericDataTypeCodec<QueryNextRequest> {
    @Override
    public Class<QueryNextRequest> getType() {
      return QueryNextRequest.class;
    }

    @Override
    public QueryNextRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      Boolean releaseContinuationPoint = decoder.decodeBoolean("ReleaseContinuationPoint");
      ByteString continuationPoint = decoder.decodeByteString("ContinuationPoint");
      return new QueryNextRequest(requestHeader, releaseContinuationPoint, continuationPoint);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QueryNextRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeBoolean("ReleaseContinuationPoint", value.getReleaseContinuationPoint());
      encoder.encodeByteString("ContinuationPoint", value.getContinuationPoint());
    }
  }
}
