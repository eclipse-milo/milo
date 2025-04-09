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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2</a>
 */
public class HistoryUpdateRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=698");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=700");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=699");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15287");

  private final RequestHeader requestHeader;

  private final ExtensionObject @Nullable [] historyUpdateDetails;

  public HistoryUpdateRequest(
      RequestHeader requestHeader, ExtensionObject @Nullable [] historyUpdateDetails) {
    this.requestHeader = requestHeader;
    this.historyUpdateDetails = historyUpdateDetails;
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

  public ExtensionObject @Nullable [] getHistoryUpdateDetails() {
    return historyUpdateDetails;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryUpdateRequest that = (HistoryUpdateRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getHistoryUpdateDetails(), that.getHistoryUpdateDetails());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getHistoryUpdateDetails());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryUpdateRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("historyUpdateDetails=" + java.util.Arrays.toString(getHistoryUpdateDetails()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 700),
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
              "HistoryUpdateDetails",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryUpdateRequest> {
    @Override
    public Class<HistoryUpdateRequest> getType() {
      return HistoryUpdateRequest.class;
    }

    @Override
    public HistoryUpdateRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final ExtensionObject[] historyUpdateDetails;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      historyUpdateDetails = decoder.decodeExtensionObjectArray("HistoryUpdateDetails");
      return new HistoryUpdateRequest(requestHeader, historyUpdateDetails);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryUpdateRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeExtensionObjectArray("HistoryUpdateDetails", value.getHistoryUpdateDetails());
    }
  }
}
