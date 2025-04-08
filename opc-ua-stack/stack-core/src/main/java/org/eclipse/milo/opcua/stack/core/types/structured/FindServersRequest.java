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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2</a>
 */
public class FindServersRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=420");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=422");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=421");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15093");

  private final RequestHeader requestHeader;

  private final @Nullable String endpointUrl;

  private final String @Nullable [] localeIds;

  private final String @Nullable [] serverUris;

  public FindServersRequest(
      RequestHeader requestHeader,
      @Nullable String endpointUrl,
      String @Nullable [] localeIds,
      String @Nullable [] serverUris) {
    this.requestHeader = requestHeader;
    this.endpointUrl = endpointUrl;
    this.localeIds = localeIds;
    this.serverUris = serverUris;
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

  public @Nullable String getEndpointUrl() {
    return endpointUrl;
  }

  public String @Nullable [] getLocaleIds() {
    return localeIds;
  }

  public String @Nullable [] getServerUris() {
    return serverUris;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FindServersRequest that = (FindServersRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getEndpointUrl(), that.getEndpointUrl());
    eqb.append(getLocaleIds(), that.getLocaleIds());
    eqb.append(getServerUris(), that.getServerUris());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getEndpointUrl());
    hcb.append(getLocaleIds());
    hcb.append(getServerUris());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", FindServersRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("endpointUrl='" + getEndpointUrl() + "'");
    joiner.add("localeIds=" + java.util.Arrays.toString(getLocaleIds()));
    joiner.add("serverUris=" + java.util.Arrays.toString(getServerUris()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 422),
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
              "EndpointUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocaleIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 295),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<FindServersRequest> {
    @Override
    public Class<FindServersRequest> getType() {
      return FindServersRequest.class;
    }

    @Override
    public FindServersRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final String endpointUrl;
      final String[] localeIds;
      final String[] serverUris;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      endpointUrl = decoder.decodeString("EndpointUrl");
      localeIds = decoder.decodeStringArray("LocaleIds");
      serverUris = decoder.decodeStringArray("ServerUris");
      return new FindServersRequest(requestHeader, endpointUrl, localeIds, serverUris);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, FindServersRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeString("EndpointUrl", value.getEndpointUrl());
      encoder.encodeStringArray("LocaleIds", value.getLocaleIds());
      encoder.encodeStringArray("ServerUris", value.getServerUris());
    }
  }
}
