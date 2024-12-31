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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.2</a>
 */
public class HistoryReadRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=662");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=664");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=663");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15274");

  private final RequestHeader requestHeader;

  private final ExtensionObject historyReadDetails;

  private final TimestampsToReturn timestampsToReturn;

  private final Boolean releaseContinuationPoints;

  private final HistoryReadValueId @Nullable [] nodesToRead;

  public HistoryReadRequest(
      RequestHeader requestHeader,
      ExtensionObject historyReadDetails,
      TimestampsToReturn timestampsToReturn,
      Boolean releaseContinuationPoints,
      HistoryReadValueId @Nullable [] nodesToRead) {
    this.requestHeader = requestHeader;
    this.historyReadDetails = historyReadDetails;
    this.timestampsToReturn = timestampsToReturn;
    this.releaseContinuationPoints = releaseContinuationPoints;
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

  public ExtensionObject getHistoryReadDetails() {
    return historyReadDetails;
  }

  public TimestampsToReturn getTimestampsToReturn() {
    return timestampsToReturn;
  }

  public Boolean getReleaseContinuationPoints() {
    return releaseContinuationPoints;
  }

  public HistoryReadValueId @Nullable [] getNodesToRead() {
    return nodesToRead;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryReadRequest that = (HistoryReadRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getHistoryReadDetails(), that.getHistoryReadDetails());
    eqb.append(getTimestampsToReturn(), that.getTimestampsToReturn());
    eqb.append(getReleaseContinuationPoints(), that.getReleaseContinuationPoints());
    eqb.append(getNodesToRead(), that.getNodesToRead());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getHistoryReadDetails());
    hcb.append(getTimestampsToReturn());
    hcb.append(getReleaseContinuationPoints());
    hcb.append(getNodesToRead());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryReadRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("historyReadDetails=" + getHistoryReadDetails());
    joiner.add("timestampsToReturn=" + getTimestampsToReturn());
    joiner.add("releaseContinuationPoints=" + getReleaseContinuationPoints());
    joiner.add("nodesToRead=" + java.util.Arrays.toString(getNodesToRead()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 664),
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
              "HistoryReadDetails",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
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
              "ReleaseContinuationPoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodesToRead",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 635),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryReadRequest> {
    @Override
    public Class<HistoryReadRequest> getType() {
      return HistoryReadRequest.class;
    }

    @Override
    public HistoryReadRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      ExtensionObject historyReadDetails = decoder.decodeExtensionObject("HistoryReadDetails");
      TimestampsToReturn timestampsToReturn =
          TimestampsToReturn.from(decoder.decodeEnum("TimestampsToReturn"));
      Boolean releaseContinuationPoints = decoder.decodeBoolean("ReleaseContinuationPoints");
      HistoryReadValueId[] nodesToRead =
          (HistoryReadValueId[])
              decoder.decodeStructArray("NodesToRead", HistoryReadValueId.TYPE_ID);
      return new HistoryReadRequest(
          requestHeader,
          historyReadDetails,
          timestampsToReturn,
          releaseContinuationPoints,
          nodesToRead);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryReadRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeExtensionObject("HistoryReadDetails", value.getHistoryReadDetails());
      encoder.encodeEnum("TimestampsToReturn", value.getTimestampsToReturn());
      encoder.encodeBoolean("ReleaseContinuationPoints", value.getReleaseContinuationPoints());
      encoder.encodeStructArray("NodesToRead", value.getNodesToRead(), HistoryReadValueId.TYPE_ID);
    }
  }
}
