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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.2/#5.11.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.2/#5.11.2.2</a>
 */
public class ReadRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=629");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=631");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=630");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15257");

  private final RequestHeader requestHeader;

  private final Double maxAge;

  private final TimestampsToReturn timestampsToReturn;

  private final ReadValueId @Nullable [] nodesToRead;

  public ReadRequest(
      RequestHeader requestHeader,
      Double maxAge,
      TimestampsToReturn timestampsToReturn,
      ReadValueId @Nullable [] nodesToRead) {
    this.requestHeader = requestHeader;
    this.maxAge = maxAge;
    this.timestampsToReturn = timestampsToReturn;
    this.nodesToRead = nodesToRead;
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

  public Double getMaxAge() {
    return maxAge;
  }

  public TimestampsToReturn getTimestampsToReturn() {
    return timestampsToReturn;
  }

  public ReadValueId @Nullable [] getNodesToRead() {
    return nodesToRead;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadRequest that = (ReadRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getMaxAge(), that.getMaxAge());
    eqb.append(getTimestampsToReturn(), that.getTimestampsToReturn());
    eqb.append(getNodesToRead(), that.getNodesToRead());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getMaxAge());
    hcb.append(getTimestampsToReturn());
    hcb.append(getNodesToRead());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("maxAge=" + getMaxAge());
    joiner.add("timestampsToReturn=" + getTimestampsToReturn());
    joiner.add("nodesToRead=" + java.util.Arrays.toString(getNodesToRead()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 631),
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
              "MaxAge",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TimestampsToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 625),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodesToRead",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 626),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadRequest> {
    @Override
    public Class<ReadRequest> getType() {
      return ReadRequest.class;
    }

    @Override
    public ReadRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final Double maxAge;
      final TimestampsToReturn timestampsToReturn;
      final ReadValueId[] nodesToRead;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      maxAge = decoder.decodeDouble("MaxAge");
      timestampsToReturn = TimestampsToReturn.from(decoder.decodeEnum("TimestampsToReturn"));
      nodesToRead = (ReadValueId[]) decoder.decodeStructArray("NodesToRead", ReadValueId.TYPE_ID);
      return new ReadRequest(requestHeader, maxAge, timestampsToReturn, nodesToRead);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReadRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeDouble("MaxAge", value.getMaxAge());
      encoder.encodeEnum("TimestampsToReturn", value.getTimestampsToReturn());
      encoder.encodeStructArray("NodesToRead", value.getNodesToRead(), ReadValueId.TYPE_ID);
    }
  }
}
