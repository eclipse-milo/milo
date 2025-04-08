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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2</a>
 */
public class WriteRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=671");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=673");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=672");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15277");

  private final RequestHeader requestHeader;

  private final WriteValue @Nullable [] nodesToWrite;

  public WriteRequest(RequestHeader requestHeader, WriteValue @Nullable [] nodesToWrite) {
    this.requestHeader = requestHeader;
    this.nodesToWrite = nodesToWrite;
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

  public WriteValue @Nullable [] getNodesToWrite() {
    return nodesToWrite;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    WriteRequest that = (WriteRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getNodesToWrite(), that.getNodesToWrite());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getNodesToWrite());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", WriteRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("nodesToWrite=" + java.util.Arrays.toString(getNodesToWrite()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 673),
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
              "NodesToWrite",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 668),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<WriteRequest> {
    @Override
    public Class<WriteRequest> getType() {
      return WriteRequest.class;
    }

    @Override
    public WriteRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final WriteValue[] nodesToWrite;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      nodesToWrite = (WriteValue[]) decoder.decodeStructArray("NodesToWrite", WriteValue.TYPE_ID);
      return new WriteRequest(requestHeader, nodesToWrite);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, WriteRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray("NodesToWrite", value.getNodesToWrite(), WriteValue.TYPE_ID);
    }
  }
}
