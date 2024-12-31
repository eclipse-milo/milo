/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

public class SessionSecurityDiagnostics {

  private final Session session;

  public SessionSecurityDiagnostics(Session session) {
    this.session = session;
  }

  /**
   * @return server-assigned identifier of the session.
   */
  public NodeId getSessionId() {
    return session.getSessionId();
  }

  /**
   * @return name of authenticated user when creating the session.
   */
  public String getClientUserIdOfSession() {
    return session.getClientUserId();
  }

  /**
   * @return array containing the name of the authenticated user currently active (either from
   *     creating the session or from calling the ActivateSession Service) and the history of those
   *     names. Each time the active user changes, an entry shall be made at the end of the array.
   *     The active user is always at the end of the array.
   */
  public String[] getClientUserIdHistory() {
    return session.getClientUserIdHistory().toArray(new String[0]);
  }

  /**
   * @return type of authentication currently used by the session. The String shall be one of the
   *     lexical names of the {@link UserTokenType} enum.
   */
  public String getAuthenticationMechanism() {
    return Objects.toString(session.getTokenType());
  }

  /**
   * @return the encoding used on the wire. The String shall be "XML", "JSON" or "UA Binary".
   */
  public String getEncoding() {
    return "UA Binary";
  }

  /**
   * @return which transport protocol is used. The String shall be the scheme from the URL used to
   *     establish the session, e.g. "opc.tcp", "opc.wss" or "https".
   */
  public String getTransportProtocol() {
    String endpointUrl = session.getEndpoint().getEndpointUrl();
    return EndpointUtil.getScheme(endpointUrl);
  }

  /**
   * @return the {@link MessageSecurityMode} used for the session.
   */
  public MessageSecurityMode getSecurityMode() {
    return session.getSecurityConfiguration().getSecurityMode();
  }

  /**
   * @return the URI of the {@link SecurityPolicy} used for the session.
   */
  public String getSecurityPolicyUri() {
    return session.getSecurityConfiguration().getSecurityPolicy().getUri();
  }

  /**
   * @return the application instance certificate provided by the client in the {@link
   *     CreateSessionRequest}.
   */
  public ByteString getClientCertificate() {
    try {
      return session.getSecurityConfiguration().getClientCertificateBytes();
    } catch (UaException e) {
      return ByteString.NULL_VALUE;
    }
  }

  /**
   * @return a {@link SessionSecurityDiagnosticsDataType} containing the most current values.
   */
  public SessionSecurityDiagnosticsDataType getSessionSecurityDiagnosticsDataType() {
    return new SessionSecurityDiagnosticsDataType(
        getSessionId(),
        getClientUserIdOfSession(),
        getClientUserIdHistory(),
        getAuthenticationMechanism(),
        getEncoding(),
        getTransportProtocol(),
        getSecurityMode(),
        getSecurityPolicyUri(),
        getClientCertificate());
  }
}
