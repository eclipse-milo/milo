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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.6/#5.8.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.6/#5.8.6.2</a>
 */
public class UnregisterNodesRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=564");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=566");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=565");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15197");

  private final RequestHeader requestHeader;

  private final NodeId @Nullable [] nodesToUnregister;

  public UnregisterNodesRequest(
      RequestHeader requestHeader, NodeId @Nullable [] nodesToUnregister) {
    this.requestHeader = requestHeader;
    this.nodesToUnregister = nodesToUnregister;
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

  public NodeId @Nullable [] getNodesToUnregister() {
    return nodesToUnregister;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UnregisterNodesRequest that = (UnregisterNodesRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getNodesToUnregister(), that.getNodesToUnregister());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getNodesToUnregister());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UnregisterNodesRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("nodesToUnregister=" + java.util.Arrays.toString(getNodesToUnregister()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 566),
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
              "NodesToUnregister",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UnregisterNodesRequest> {
    @Override
    public Class<UnregisterNodesRequest> getType() {
      return UnregisterNodesRequest.class;
    }

    @Override
    public UnregisterNodesRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      NodeId[] nodesToUnregister = decoder.decodeNodeIdArray("NodesToUnregister");
      return new UnregisterNodesRequest(requestHeader, nodesToUnregister);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UnregisterNodesRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeNodeIdArray("NodesToUnregister", value.getNodesToUnregister());
    }
  }
}
