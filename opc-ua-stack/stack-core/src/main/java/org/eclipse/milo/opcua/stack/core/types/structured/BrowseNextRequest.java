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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.3/#5.8.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.3/#5.8.3.2</a>
 */
public class BrowseNextRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=531");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=533");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=532");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15186");

  private final RequestHeader requestHeader;

  private final Boolean releaseContinuationPoints;

  private final ByteString @Nullable [] continuationPoints;

  public BrowseNextRequest(
      RequestHeader requestHeader,
      Boolean releaseContinuationPoints,
      ByteString @Nullable [] continuationPoints) {
    this.requestHeader = requestHeader;
    this.releaseContinuationPoints = releaseContinuationPoints;
    this.continuationPoints = continuationPoints;
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

  public Boolean getReleaseContinuationPoints() {
    return releaseContinuationPoints;
  }

  public ByteString @Nullable [] getContinuationPoints() {
    return continuationPoints;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowseNextRequest that = (BrowseNextRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getReleaseContinuationPoints(), that.getReleaseContinuationPoints());
    eqb.append(getContinuationPoints(), that.getContinuationPoints());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getReleaseContinuationPoints());
    hcb.append(getContinuationPoints());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowseNextRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("releaseContinuationPoints=" + getReleaseContinuationPoints());
    joiner.add("continuationPoints=" + java.util.Arrays.toString(getContinuationPoints()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 533),
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
              "ReleaseContinuationPoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ContinuationPoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowseNextRequest> {
    @Override
    public Class<BrowseNextRequest> getType() {
      return BrowseNextRequest.class;
    }

    @Override
    public BrowseNextRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      Boolean releaseContinuationPoints = decoder.decodeBoolean("ReleaseContinuationPoints");
      ByteString[] continuationPoints = decoder.decodeByteStringArray("ContinuationPoints");
      return new BrowseNextRequest(requestHeader, releaseContinuationPoints, continuationPoints);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowseNextRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeBoolean("ReleaseContinuationPoints", value.getReleaseContinuationPoints());
      encoder.encodeByteStringArray("ContinuationPoints", value.getContinuationPoints());
    }
  }
}
