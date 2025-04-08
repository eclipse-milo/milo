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
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.12</a>
 */
public class SessionSecurityDiagnosticsDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=868");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=870");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=869");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15369");

  private final NodeId sessionId;

  private final @Nullable String clientUserIdOfSession;

  private final String @Nullable [] clientUserIdHistory;

  private final @Nullable String authenticationMechanism;

  private final @Nullable String encoding;

  private final @Nullable String transportProtocol;

  private final MessageSecurityMode securityMode;

  private final @Nullable String securityPolicyUri;

  private final ByteString clientCertificate;

  public SessionSecurityDiagnosticsDataType(
      NodeId sessionId,
      @Nullable String clientUserIdOfSession,
      String @Nullable [] clientUserIdHistory,
      @Nullable String authenticationMechanism,
      @Nullable String encoding,
      @Nullable String transportProtocol,
      MessageSecurityMode securityMode,
      @Nullable String securityPolicyUri,
      ByteString clientCertificate) {
    this.sessionId = sessionId;
    this.clientUserIdOfSession = clientUserIdOfSession;
    this.clientUserIdHistory = clientUserIdHistory;
    this.authenticationMechanism = authenticationMechanism;
    this.encoding = encoding;
    this.transportProtocol = transportProtocol;
    this.securityMode = securityMode;
    this.securityPolicyUri = securityPolicyUri;
    this.clientCertificate = clientCertificate;
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

  public NodeId getSessionId() {
    return sessionId;
  }

  public @Nullable String getClientUserIdOfSession() {
    return clientUserIdOfSession;
  }

  public String @Nullable [] getClientUserIdHistory() {
    return clientUserIdHistory;
  }

  public @Nullable String getAuthenticationMechanism() {
    return authenticationMechanism;
  }

  public @Nullable String getEncoding() {
    return encoding;
  }

  public @Nullable String getTransportProtocol() {
    return transportProtocol;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public ByteString getClientCertificate() {
    return clientCertificate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SessionSecurityDiagnosticsDataType that = (SessionSecurityDiagnosticsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSessionId(), that.getSessionId());
    eqb.append(getClientUserIdOfSession(), that.getClientUserIdOfSession());
    eqb.append(getClientUserIdHistory(), that.getClientUserIdHistory());
    eqb.append(getAuthenticationMechanism(), that.getAuthenticationMechanism());
    eqb.append(getEncoding(), that.getEncoding());
    eqb.append(getTransportProtocol(), that.getTransportProtocol());
    eqb.append(getSecurityMode(), that.getSecurityMode());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    eqb.append(getClientCertificate(), that.getClientCertificate());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSessionId());
    hcb.append(getClientUserIdOfSession());
    hcb.append(getClientUserIdHistory());
    hcb.append(getAuthenticationMechanism());
    hcb.append(getEncoding());
    hcb.append(getTransportProtocol());
    hcb.append(getSecurityMode());
    hcb.append(getSecurityPolicyUri());
    hcb.append(getClientCertificate());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SessionSecurityDiagnosticsDataType.class.getSimpleName() + "[", "]");
    joiner.add("sessionId=" + getSessionId());
    joiner.add("clientUserIdOfSession='" + getClientUserIdOfSession() + "'");
    joiner.add("clientUserIdHistory=" + java.util.Arrays.toString(getClientUserIdHistory()));
    joiner.add("authenticationMechanism='" + getAuthenticationMechanism() + "'");
    joiner.add("encoding='" + getEncoding() + "'");
    joiner.add("transportProtocol='" + getTransportProtocol() + "'");
    joiner.add("securityMode=" + getSecurityMode());
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    joiner.add("clientCertificate=" + getClientCertificate());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 870),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientUserIdOfSession",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientUserIdHistory",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthenticationMechanism",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Encoding",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportProtocol",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientCertificate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SessionSecurityDiagnosticsDataType> {
    @Override
    public Class<SessionSecurityDiagnosticsDataType> getType() {
      return SessionSecurityDiagnosticsDataType.class;
    }

    @Override
    public SessionSecurityDiagnosticsDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final NodeId sessionId;
      final String clientUserIdOfSession;
      final String[] clientUserIdHistory;
      final String authenticationMechanism;
      final String encoding;
      final String transportProtocol;
      final MessageSecurityMode securityMode;
      final String securityPolicyUri;
      final ByteString clientCertificate;
      sessionId = decoder.decodeNodeId("SessionId");
      clientUserIdOfSession = decoder.decodeString("ClientUserIdOfSession");
      clientUserIdHistory = decoder.decodeStringArray("ClientUserIdHistory");
      authenticationMechanism = decoder.decodeString("AuthenticationMechanism");
      encoding = decoder.decodeString("Encoding");
      transportProtocol = decoder.decodeString("TransportProtocol");
      securityMode = MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      clientCertificate = decoder.decodeByteString("ClientCertificate");
      return new SessionSecurityDiagnosticsDataType(
          sessionId,
          clientUserIdOfSession,
          clientUserIdHistory,
          authenticationMechanism,
          encoding,
          transportProtocol,
          securityMode,
          securityPolicyUri,
          clientCertificate);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SessionSecurityDiagnosticsDataType value) {
      encoder.encodeNodeId("SessionId", value.getSessionId());
      encoder.encodeString("ClientUserIdOfSession", value.getClientUserIdOfSession());
      encoder.encodeStringArray("ClientUserIdHistory", value.getClientUserIdHistory());
      encoder.encodeString("AuthenticationMechanism", value.getAuthenticationMechanism());
      encoder.encodeString("Encoding", value.getEncoding());
      encoder.encodeString("TransportProtocol", value.getTransportProtocol());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
      encoder.encodeByteString("ClientCertificate", value.getClientCertificate());
    }
  }
}
