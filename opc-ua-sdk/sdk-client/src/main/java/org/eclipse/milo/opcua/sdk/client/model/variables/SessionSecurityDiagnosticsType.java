/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable NodeId getSessionId() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readSessionId() throws UaException;

  /** Writes the value remotely. */
  void writeSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserIdOfSession() throws UaException;

  /** Sets the existing node's local value. */
  void setClientUserIdOfSession(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientUserIdOfSession() throws UaException;

  /** Writes the value remotely. */
  void writeClientUserIdOfSession(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientUserIdOfSessionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientUserIdOfSessionAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientUserIdOfSessionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientUserIdOfSessionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getClientUserIdHistory() throws UaException;

  /** Sets the existing node's local value. */
  void setClientUserIdHistory(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readClientUserIdHistory() throws UaException;

  /** Writes the value remotely. */
  void writeClientUserIdHistory(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readClientUserIdHistoryAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientUserIdHistoryAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientUserIdHistoryNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientUserIdHistoryNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getAuthenticationMechanism() throws UaException;

  /** Sets the existing node's local value. */
  void setAuthenticationMechanism(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readAuthenticationMechanism() throws UaException;

  /** Writes the value remotely. */
  void writeAuthenticationMechanism(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readAuthenticationMechanismAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAuthenticationMechanismAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAuthenticationMechanismNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAuthenticationMechanismNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getEncoding() throws UaException;

  /** Sets the existing node's local value. */
  void setEncoding(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readEncoding() throws UaException;

  /** Writes the value remotely. */
  void writeEncoding(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readEncodingAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEncodingAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getEncodingNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEncodingNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getTransportProtocol() throws UaException;

  /** Sets the existing node's local value. */
  void setTransportProtocol(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readTransportProtocol() throws UaException;

  /** Writes the value remotely. */
  void writeTransportProtocol(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readTransportProtocolAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransportProtocolAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTransportProtocolNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransportProtocolNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityModeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityModeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityPolicyUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityPolicyUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ByteString getClientCertificate() throws UaException;

  /** Sets the existing node's local value. */
  void setClientCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readClientCertificate() throws UaException;

  /** Writes the value remotely. */
  void writeClientCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientCertificateAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getClientCertificateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientCertificateNodeAsync();
}
