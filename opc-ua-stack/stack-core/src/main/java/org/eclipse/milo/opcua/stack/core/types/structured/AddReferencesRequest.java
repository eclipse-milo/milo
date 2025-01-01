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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2</a>
 */
public class AddReferencesRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=492");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=494");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=493");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15170");

  private final RequestHeader requestHeader;

  private final AddReferencesItem @Nullable [] referencesToAdd;

  public AddReferencesRequest(
      RequestHeader requestHeader, AddReferencesItem @Nullable [] referencesToAdd) {
    this.requestHeader = requestHeader;
    this.referencesToAdd = referencesToAdd;
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

  public AddReferencesItem @Nullable [] getReferencesToAdd() {
    return referencesToAdd;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AddReferencesRequest that = (AddReferencesRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getReferencesToAdd(), that.getReferencesToAdd());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getReferencesToAdd());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddReferencesRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("referencesToAdd=" + java.util.Arrays.toString(getReferencesToAdd()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 494),
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
              "ReferencesToAdd",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 379),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AddReferencesRequest> {
    @Override
    public Class<AddReferencesRequest> getType() {
      return AddReferencesRequest.class;
    }

    @Override
    public AddReferencesRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      AddReferencesItem[] referencesToAdd =
          (AddReferencesItem[])
              decoder.decodeStructArray("ReferencesToAdd", AddReferencesItem.TYPE_ID);
      return new AddReferencesRequest(requestHeader, referencesToAdd);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AddReferencesRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray(
          "ReferencesToAdd", value.getReferencesToAdd(), AddReferencesItem.TYPE_ID);
    }
  }
}
