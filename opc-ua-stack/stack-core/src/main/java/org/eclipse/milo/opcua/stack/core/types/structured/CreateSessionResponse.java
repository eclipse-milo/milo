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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2</a>
 */
public class CreateSessionResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=462");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=464");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=463");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15139");

  private final ResponseHeader responseHeader;

  private final NodeId sessionId;

  private final NodeId authenticationToken;

  private final Double revisedSessionTimeout;

  private final ByteString serverNonce;

  private final ByteString serverCertificate;

  private final EndpointDescription @Nullable [] serverEndpoints;

  private final SignedSoftwareCertificate @Nullable [] serverSoftwareCertificates;

  private final SignatureData serverSignature;

  private final UInteger maxRequestMessageSize;

  public CreateSessionResponse(
      ResponseHeader responseHeader,
      NodeId sessionId,
      NodeId authenticationToken,
      Double revisedSessionTimeout,
      ByteString serverNonce,
      ByteString serverCertificate,
      EndpointDescription @Nullable [] serverEndpoints,
      SignedSoftwareCertificate @Nullable [] serverSoftwareCertificates,
      SignatureData serverSignature,
      UInteger maxRequestMessageSize) {
    this.responseHeader = responseHeader;
    this.sessionId = sessionId;
    this.authenticationToken = authenticationToken;
    this.revisedSessionTimeout = revisedSessionTimeout;
    this.serverNonce = serverNonce;
    this.serverCertificate = serverCertificate;
    this.serverEndpoints = serverEndpoints;
    this.serverSoftwareCertificates = serverSoftwareCertificates;
    this.serverSignature = serverSignature;
    this.maxRequestMessageSize = maxRequestMessageSize;
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

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public NodeId getSessionId() {
    return sessionId;
  }

  public NodeId getAuthenticationToken() {
    return authenticationToken;
  }

  public Double getRevisedSessionTimeout() {
    return revisedSessionTimeout;
  }

  public ByteString getServerNonce() {
    return serverNonce;
  }

  public ByteString getServerCertificate() {
    return serverCertificate;
  }

  public EndpointDescription @Nullable [] getServerEndpoints() {
    return serverEndpoints;
  }

  public SignedSoftwareCertificate @Nullable [] getServerSoftwareCertificates() {
    return serverSoftwareCertificates;
  }

  public SignatureData getServerSignature() {
    return serverSignature;
  }

  public UInteger getMaxRequestMessageSize() {
    return maxRequestMessageSize;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CreateSessionResponse that = (CreateSessionResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getSessionId(), that.getSessionId());
    eqb.append(getAuthenticationToken(), that.getAuthenticationToken());
    eqb.append(getRevisedSessionTimeout(), that.getRevisedSessionTimeout());
    eqb.append(getServerNonce(), that.getServerNonce());
    eqb.append(getServerCertificate(), that.getServerCertificate());
    eqb.append(getServerEndpoints(), that.getServerEndpoints());
    eqb.append(getServerSoftwareCertificates(), that.getServerSoftwareCertificates());
    eqb.append(getServerSignature(), that.getServerSignature());
    eqb.append(getMaxRequestMessageSize(), that.getMaxRequestMessageSize());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getSessionId());
    hcb.append(getAuthenticationToken());
    hcb.append(getRevisedSessionTimeout());
    hcb.append(getServerNonce());
    hcb.append(getServerCertificate());
    hcb.append(getServerEndpoints());
    hcb.append(getServerSoftwareCertificates());
    hcb.append(getServerSignature());
    hcb.append(getMaxRequestMessageSize());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CreateSessionResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("sessionId=" + getSessionId());
    joiner.add("authenticationToken=" + getAuthenticationToken());
    joiner.add("revisedSessionTimeout=" + getRevisedSessionTimeout());
    joiner.add("serverNonce=" + getServerNonce());
    joiner.add("serverCertificate=" + getServerCertificate());
    joiner.add("serverEndpoints=" + java.util.Arrays.toString(getServerEndpoints()));
    joiner.add(
        "serverSoftwareCertificates=" + java.util.Arrays.toString(getServerSoftwareCertificates()));
    joiner.add("serverSignature=" + getServerSignature());
    joiner.add("maxRequestMessageSize=" + getMaxRequestMessageSize());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 464),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResponseHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 392),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthenticationToken",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 388),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedSessionTimeout",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerNonce",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerCertificate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 311),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerEndpoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 312),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerSoftwareCertificates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 344),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerSignature",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 456),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxRequestMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CreateSessionResponse> {
    @Override
    public Class<CreateSessionResponse> getType() {
      return CreateSessionResponse.class;
    }

    @Override
    public CreateSessionResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final NodeId sessionId;
      final NodeId authenticationToken;
      final Double revisedSessionTimeout;
      final ByteString serverNonce;
      final ByteString serverCertificate;
      final EndpointDescription[] serverEndpoints;
      final SignedSoftwareCertificate[] serverSoftwareCertificates;
      final SignatureData serverSignature;
      final UInteger maxRequestMessageSize;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      sessionId = decoder.decodeNodeId("SessionId");
      authenticationToken = decoder.decodeNodeId("AuthenticationToken");
      revisedSessionTimeout = decoder.decodeDouble("RevisedSessionTimeout");
      serverNonce = decoder.decodeByteString("ServerNonce");
      serverCertificate = decoder.decodeByteString("ServerCertificate");
      serverEndpoints =
          (EndpointDescription[])
              decoder.decodeStructArray("ServerEndpoints", EndpointDescription.TYPE_ID);
      serverSoftwareCertificates =
          (SignedSoftwareCertificate[])
              decoder.decodeStructArray(
                  "ServerSoftwareCertificates", SignedSoftwareCertificate.TYPE_ID);
      serverSignature =
          (SignatureData) decoder.decodeStruct("ServerSignature", SignatureData.TYPE_ID);
      maxRequestMessageSize = decoder.decodeUInt32("MaxRequestMessageSize");
      return new CreateSessionResponse(
          responseHeader,
          sessionId,
          authenticationToken,
          revisedSessionTimeout,
          serverNonce,
          serverCertificate,
          serverEndpoints,
          serverSoftwareCertificates,
          serverSignature,
          maxRequestMessageSize);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, CreateSessionResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeNodeId("SessionId", value.getSessionId());
      encoder.encodeNodeId("AuthenticationToken", value.getAuthenticationToken());
      encoder.encodeDouble("RevisedSessionTimeout", value.getRevisedSessionTimeout());
      encoder.encodeByteString("ServerNonce", value.getServerNonce());
      encoder.encodeByteString("ServerCertificate", value.getServerCertificate());
      encoder.encodeStructArray(
          "ServerEndpoints", value.getServerEndpoints(), EndpointDescription.TYPE_ID);
      encoder.encodeStructArray(
          "ServerSoftwareCertificates",
          value.getServerSoftwareCertificates(),
          SignedSoftwareCertificate.TYPE_ID);
      encoder.encodeStruct("ServerSignature", value.getServerSignature(), SignatureData.TYPE_ID);
      encoder.encodeUInt32("MaxRequestMessageSize", value.getMaxRequestMessageSize());
    }
  }
}
