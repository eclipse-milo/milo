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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.2/#5.6.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.2/#5.6.2.2</a>
 */
public class CreateSessionRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=459");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=461");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=460");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15138");

  private final RequestHeader requestHeader;

  private final ApplicationDescription clientDescription;

  private final @Nullable String serverUri;

  private final @Nullable String endpointUrl;

  private final @Nullable String sessionName;

  private final ByteString clientNonce;

  private final ByteString clientCertificate;

  private final Double requestedSessionTimeout;

  private final UInteger maxResponseMessageSize;

  public CreateSessionRequest(
      RequestHeader requestHeader,
      ApplicationDescription clientDescription,
      @Nullable String serverUri,
      @Nullable String endpointUrl,
      @Nullable String sessionName,
      ByteString clientNonce,
      ByteString clientCertificate,
      Double requestedSessionTimeout,
      UInteger maxResponseMessageSize) {
    this.requestHeader = requestHeader;
    this.clientDescription = clientDescription;
    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
    this.sessionName = sessionName;
    this.clientNonce = clientNonce;
    this.clientCertificate = clientCertificate;
    this.requestedSessionTimeout = requestedSessionTimeout;
    this.maxResponseMessageSize = maxResponseMessageSize;
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

  public ApplicationDescription getClientDescription() {
    return clientDescription;
  }

  public @Nullable String getServerUri() {
    return serverUri;
  }

  public @Nullable String getEndpointUrl() {
    return endpointUrl;
  }

  public @Nullable String getSessionName() {
    return sessionName;
  }

  public ByteString getClientNonce() {
    return clientNonce;
  }

  public ByteString getClientCertificate() {
    return clientCertificate;
  }

  public Double getRequestedSessionTimeout() {
    return requestedSessionTimeout;
  }

  public UInteger getMaxResponseMessageSize() {
    return maxResponseMessageSize;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CreateSessionRequest that = (CreateSessionRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getClientDescription(), that.getClientDescription());
    eqb.append(getServerUri(), that.getServerUri());
    eqb.append(getEndpointUrl(), that.getEndpointUrl());
    eqb.append(getSessionName(), that.getSessionName());
    eqb.append(getClientNonce(), that.getClientNonce());
    eqb.append(getClientCertificate(), that.getClientCertificate());
    eqb.append(getRequestedSessionTimeout(), that.getRequestedSessionTimeout());
    eqb.append(getMaxResponseMessageSize(), that.getMaxResponseMessageSize());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getClientDescription());
    hcb.append(getServerUri());
    hcb.append(getEndpointUrl());
    hcb.append(getSessionName());
    hcb.append(getClientNonce());
    hcb.append(getClientCertificate());
    hcb.append(getRequestedSessionTimeout());
    hcb.append(getMaxResponseMessageSize());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CreateSessionRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("clientDescription=" + getClientDescription());
    joiner.add("serverUri='" + getServerUri() + "'");
    joiner.add("endpointUrl='" + getEndpointUrl() + "'");
    joiner.add("sessionName='" + getSessionName() + "'");
    joiner.add("clientNonce=" + getClientNonce());
    joiner.add("clientCertificate=" + getClientCertificate());
    joiner.add("requestedSessionTimeout=" + getRequestedSessionTimeout());
    joiner.add("maxResponseMessageSize=" + getMaxResponseMessageSize());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 461),
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
              "ClientDescription",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 308),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
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
              "SessionName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientNonce",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientCertificate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 311),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedSessionTimeout",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxResponseMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CreateSessionRequest> {
    @Override
    public Class<CreateSessionRequest> getType() {
      return CreateSessionRequest.class;
    }

    @Override
    public CreateSessionRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      ApplicationDescription clientDescription =
          (ApplicationDescription)
              decoder.decodeStruct("ClientDescription", ApplicationDescription.TYPE_ID);
      String serverUri = decoder.decodeString("ServerUri");
      String endpointUrl = decoder.decodeString("EndpointUrl");
      String sessionName = decoder.decodeString("SessionName");
      ByteString clientNonce = decoder.decodeByteString("ClientNonce");
      ByteString clientCertificate = decoder.decodeByteString("ClientCertificate");
      Double requestedSessionTimeout = decoder.decodeDouble("RequestedSessionTimeout");
      UInteger maxResponseMessageSize = decoder.decodeUInt32("MaxResponseMessageSize");
      return new CreateSessionRequest(
          requestHeader,
          clientDescription,
          serverUri,
          endpointUrl,
          sessionName,
          clientNonce,
          clientCertificate,
          requestedSessionTimeout,
          maxResponseMessageSize);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, CreateSessionRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStruct(
          "ClientDescription", value.getClientDescription(), ApplicationDescription.TYPE_ID);
      encoder.encodeString("ServerUri", value.getServerUri());
      encoder.encodeString("EndpointUrl", value.getEndpointUrl());
      encoder.encodeString("SessionName", value.getSessionName());
      encoder.encodeByteString("ClientNonce", value.getClientNonce());
      encoder.encodeByteString("ClientCertificate", value.getClientCertificate());
      encoder.encodeDouble("RequestedSessionTimeout", value.getRequestedSessionTimeout());
      encoder.encodeUInt32("MaxResponseMessageSize", value.getMaxResponseMessageSize());
    }
  }
}
