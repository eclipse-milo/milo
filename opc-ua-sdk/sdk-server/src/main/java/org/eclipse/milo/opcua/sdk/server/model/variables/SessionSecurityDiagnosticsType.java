/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16</a>
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  BaseDataVariableType getSessionIdNode();

  NodeId getSessionId();

  void setSessionId(NodeId value);

  BaseDataVariableType getClientUserIdOfSessionNode();

  String getClientUserIdOfSession();

  void setClientUserIdOfSession(String value);

  BaseDataVariableType getClientUserIdHistoryNode();

  String[] getClientUserIdHistory();

  void setClientUserIdHistory(String[] value);

  BaseDataVariableType getAuthenticationMechanismNode();

  String getAuthenticationMechanism();

  void setAuthenticationMechanism(String value);

  BaseDataVariableType getEncodingNode();

  String getEncoding();

  void setEncoding(String value);

  BaseDataVariableType getTransportProtocolNode();

  String getTransportProtocol();

  void setTransportProtocol(String value);

  BaseDataVariableType getSecurityModeNode();

  MessageSecurityMode getSecurityMode();

  void setSecurityMode(MessageSecurityMode value);

  BaseDataVariableType getSecurityPolicyUriNode();

  String getSecurityPolicyUri();

  void setSecurityPolicyUri(String value);

  BaseDataVariableType getClientCertificateNode();

  ByteString getClientCertificate();

  void setClientCertificate(ByteString value);
}
