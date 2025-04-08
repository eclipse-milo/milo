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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2</a>
 */
public class AddNodesRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=486");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=488");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=487");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15167");

  private final RequestHeader requestHeader;

  private final AddNodesItem @Nullable [] nodesToAdd;

  public AddNodesRequest(RequestHeader requestHeader, AddNodesItem @Nullable [] nodesToAdd) {
    this.requestHeader = requestHeader;
    this.nodesToAdd = nodesToAdd;
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

  public AddNodesItem @Nullable [] getNodesToAdd() {
    return nodesToAdd;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AddNodesRequest that = (AddNodesRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getNodesToAdd(), that.getNodesToAdd());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getNodesToAdd());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddNodesRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("nodesToAdd=" + java.util.Arrays.toString(getNodesToAdd()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 488),
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
              "NodesToAdd",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 376),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AddNodesRequest> {
    @Override
    public Class<AddNodesRequest> getType() {
      return AddNodesRequest.class;
    }

    @Override
    public AddNodesRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final AddNodesItem[] nodesToAdd;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      nodesToAdd = (AddNodesItem[]) decoder.decodeStructArray("NodesToAdd", AddNodesItem.TYPE_ID);
      return new AddNodesRequest(requestHeader, nodesToAdd);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AddNodesRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray("NodesToAdd", value.getNodesToAdd(), AddNodesItem.TYPE_ID);
    }
  }
}
