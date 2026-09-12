/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSessionId();

  /** Sets the existing node's local value. */
  void setSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientUserIdOfSessionNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserIdOfSession();

  /** Sets the existing node's local value. */
  void setClientUserIdOfSession(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientUserIdHistoryNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getClientUserIdHistory();

  /** Sets the existing node's local value. */
  void setClientUserIdHistory(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAuthenticationMechanismNode();

  /** Gets the existing node's local value. */
  @Nullable String getAuthenticationMechanism();

  /** Sets the existing node's local value. */
  void setAuthenticationMechanism(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEncodingNode();

  /** Gets the existing node's local value. */
  @Nullable String getEncoding();

  /** Sets the existing node's local value. */
  void setEncoding(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransportProtocolNode();

  /** Gets the existing node's local value. */
  @Nullable String getTransportProtocol();

  /** Sets the existing node's local value. */
  void setTransportProtocol(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityModeNode();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode();

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityPolicyUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri();

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientCertificateNode();

  /** Gets the existing node's local value. */
  @Nullable ByteString getClientCertificate();

  /** Sets the existing node's local value. */
  void setClientCertificate(@Nullable ByteString value);
}
