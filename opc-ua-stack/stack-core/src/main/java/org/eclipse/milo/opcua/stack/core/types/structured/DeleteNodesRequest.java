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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2</a>
 */
public class DeleteNodesRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=498");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=500");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=499");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15173");

  private final RequestHeader requestHeader;

  private final DeleteNodesItem @Nullable [] nodesToDelete;

  public DeleteNodesRequest(
      RequestHeader requestHeader, DeleteNodesItem @Nullable [] nodesToDelete) {
    this.requestHeader = requestHeader;
    this.nodesToDelete = nodesToDelete;
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

  public DeleteNodesItem @Nullable [] getNodesToDelete() {
    return nodesToDelete;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteNodesRequest that = (DeleteNodesRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getNodesToDelete(), that.getNodesToDelete());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getNodesToDelete());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteNodesRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("nodesToDelete=" + java.util.Arrays.toString(getNodesToDelete()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 500),
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
              "NodesToDelete",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 382),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteNodesRequest> {
    @Override
    public Class<DeleteNodesRequest> getType() {
      return DeleteNodesRequest.class;
    }

    @Override
    public DeleteNodesRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      DeleteNodesItem[] nodesToDelete =
          (DeleteNodesItem[]) decoder.decodeStructArray("NodesToDelete", DeleteNodesItem.TYPE_ID);
      return new DeleteNodesRequest(requestHeader, nodesToDelete);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DeleteNodesRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray("NodesToDelete", value.getNodesToDelete(), DeleteNodesItem.TYPE_ID);
    }
  }
}
