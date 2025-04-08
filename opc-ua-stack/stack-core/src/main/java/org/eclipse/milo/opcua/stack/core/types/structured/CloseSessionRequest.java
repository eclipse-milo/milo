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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2</a>
 */
public class CloseSessionRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=471");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=473");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=472");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15147");

  private final RequestHeader requestHeader;

  private final Boolean deleteSubscriptions;

  public CloseSessionRequest(RequestHeader requestHeader, Boolean deleteSubscriptions) {
    this.requestHeader = requestHeader;
    this.deleteSubscriptions = deleteSubscriptions;
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

  public Boolean getDeleteSubscriptions() {
    return deleteSubscriptions;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CloseSessionRequest that = (CloseSessionRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getDeleteSubscriptions(), that.getDeleteSubscriptions());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getDeleteSubscriptions());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CloseSessionRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("deleteSubscriptions=" + getDeleteSubscriptions());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 473),
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
              "DeleteSubscriptions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CloseSessionRequest> {
    @Override
    public Class<CloseSessionRequest> getType() {
      return CloseSessionRequest.class;
    }

    @Override
    public CloseSessionRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final Boolean deleteSubscriptions;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      deleteSubscriptions = decoder.decodeBoolean("DeleteSubscriptions");
      return new CloseSessionRequest(requestHeader, deleteSubscriptions);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, CloseSessionRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeBoolean("DeleteSubscriptions", value.getDeleteSubscriptions());
    }
  }
}
