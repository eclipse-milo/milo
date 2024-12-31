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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.1</a>
 */
public class DeleteReferencesRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=504");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=506");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=505");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15176");

  private final RequestHeader requestHeader;

  private final DeleteReferencesItem @Nullable [] referencesToDelete;

  public DeleteReferencesRequest(
      RequestHeader requestHeader, DeleteReferencesItem @Nullable [] referencesToDelete) {
    this.requestHeader = requestHeader;
    this.referencesToDelete = referencesToDelete;
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

  public DeleteReferencesItem @Nullable [] getReferencesToDelete() {
    return referencesToDelete;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteReferencesRequest that = (DeleteReferencesRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getReferencesToDelete(), that.getReferencesToDelete());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getReferencesToDelete());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteReferencesRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("referencesToDelete=" + java.util.Arrays.toString(getReferencesToDelete()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 506),
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
              "ReferencesToDelete",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 385),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteReferencesRequest> {
    @Override
    public Class<DeleteReferencesRequest> getType() {
      return DeleteReferencesRequest.class;
    }

    @Override
    public DeleteReferencesRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      DeleteReferencesItem[] referencesToDelete =
          (DeleteReferencesItem[])
              decoder.decodeStructArray("ReferencesToDelete", DeleteReferencesItem.TYPE_ID);
      return new DeleteReferencesRequest(requestHeader, referencesToDelete);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DeleteReferencesRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray(
          "ReferencesToDelete", value.getReferencesToDelete(), DeleteReferencesItem.TYPE_ID);
    }
  }
}
