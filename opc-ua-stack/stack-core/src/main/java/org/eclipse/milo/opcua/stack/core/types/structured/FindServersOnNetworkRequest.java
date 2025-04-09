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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.3/#5.5.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.3/#5.5.3.2</a>
 */
public class FindServersOnNetworkRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12190");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12208");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12196");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15096");

  private final RequestHeader requestHeader;

  private final UInteger startingRecordId;

  private final UInteger maxRecordsToReturn;

  private final String @Nullable [] serverCapabilityFilter;

  public FindServersOnNetworkRequest(
      RequestHeader requestHeader,
      UInteger startingRecordId,
      UInteger maxRecordsToReturn,
      String @Nullable [] serverCapabilityFilter) {
    this.requestHeader = requestHeader;
    this.startingRecordId = startingRecordId;
    this.maxRecordsToReturn = maxRecordsToReturn;
    this.serverCapabilityFilter = serverCapabilityFilter;
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

  public UInteger getStartingRecordId() {
    return startingRecordId;
  }

  public UInteger getMaxRecordsToReturn() {
    return maxRecordsToReturn;
  }

  public String @Nullable [] getServerCapabilityFilter() {
    return serverCapabilityFilter;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FindServersOnNetworkRequest that = (FindServersOnNetworkRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getStartingRecordId(), that.getStartingRecordId());
    eqb.append(getMaxRecordsToReturn(), that.getMaxRecordsToReturn());
    eqb.append(getServerCapabilityFilter(), that.getServerCapabilityFilter());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getStartingRecordId());
    hcb.append(getMaxRecordsToReturn());
    hcb.append(getServerCapabilityFilter());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", FindServersOnNetworkRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("startingRecordId=" + getStartingRecordId());
    joiner.add("maxRecordsToReturn=" + getMaxRecordsToReturn());
    joiner.add("serverCapabilityFilter=" + java.util.Arrays.toString(getServerCapabilityFilter()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12208),
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
              "StartingRecordId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxRecordsToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerCapabilityFilter",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<FindServersOnNetworkRequest> {
    @Override
    public Class<FindServersOnNetworkRequest> getType() {
      return FindServersOnNetworkRequest.class;
    }

    @Override
    public FindServersOnNetworkRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final UInteger startingRecordId;
      final UInteger maxRecordsToReturn;
      final String[] serverCapabilityFilter;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      startingRecordId = decoder.decodeUInt32("StartingRecordId");
      maxRecordsToReturn = decoder.decodeUInt32("MaxRecordsToReturn");
      serverCapabilityFilter = decoder.decodeStringArray("ServerCapabilityFilter");
      return new FindServersOnNetworkRequest(
          requestHeader, startingRecordId, maxRecordsToReturn, serverCapabilityFilter);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, FindServersOnNetworkRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32("StartingRecordId", value.getStartingRecordId());
      encoder.encodeUInt32("MaxRecordsToReturn", value.getMaxRecordsToReturn());
      encoder.encodeStringArray("ServerCapabilityFilter", value.getServerCapabilityFilter());
    }
  }
}
