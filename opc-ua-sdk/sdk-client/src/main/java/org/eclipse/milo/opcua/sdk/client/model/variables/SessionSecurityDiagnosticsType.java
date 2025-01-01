/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16</a>
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  /**
   * Get the local value of the SessionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionId Node.
   * @throws UaException if an error occurs creating or getting the SessionId Node.
   */
  NodeId getSessionId() throws UaException;

  /**
   * Set the local value of the SessionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionId Node.
   * @throws UaException if an error occurs creating or getting the SessionId Node.
   */
  void setSessionId(NodeId value) throws UaException;

  /**
   * Read the value of the SessionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readSessionId() throws UaException;

  /**
   * Write a new value for the SessionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readSessionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionIdAsync(NodeId value);

  /**
   * Get the SessionId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionIdNodeAsync();

  /**
   * Get the local value of the ClientUserIdOfSession Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientUserIdOfSession Node.
   * @throws UaException if an error occurs creating or getting the ClientUserIdOfSession Node.
   */
  String getClientUserIdOfSession() throws UaException;

  /**
   * Set the local value of the ClientUserIdOfSession Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientUserIdOfSession Node.
   * @throws UaException if an error occurs creating or getting the ClientUserIdOfSession Node.
   */
  void setClientUserIdOfSession(String value) throws UaException;

  /**
   * Read the value of the ClientUserIdOfSession Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readClientUserIdOfSession() throws UaException;

  /**
   * Write a new value for the ClientUserIdOfSession Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientUserIdOfSession(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientUserIdOfSession}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readClientUserIdOfSessionAsync();

  /**
   * An asynchronous implementation of {@link #writeClientUserIdOfSession}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientUserIdOfSessionAsync(String value);

  /**
   * Get the ClientUserIdOfSession {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientUserIdOfSession {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientUserIdOfSessionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientUserIdOfSessionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientUserIdOfSessionNodeAsync();

  /**
   * Get the local value of the ClientUserIdHistory Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientUserIdHistory Node.
   * @throws UaException if an error occurs creating or getting the ClientUserIdHistory Node.
   */
  String[] getClientUserIdHistory() throws UaException;

  /**
   * Set the local value of the ClientUserIdHistory Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientUserIdHistory Node.
   * @throws UaException if an error occurs creating or getting the ClientUserIdHistory Node.
   */
  void setClientUserIdHistory(String[] value) throws UaException;

  /**
   * Read the value of the ClientUserIdHistory Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readClientUserIdHistory() throws UaException;

  /**
   * Write a new value for the ClientUserIdHistory Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientUserIdHistory(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientUserIdHistory}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readClientUserIdHistoryAsync();

  /**
   * An asynchronous implementation of {@link #writeClientUserIdHistory}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientUserIdHistoryAsync(String[] value);

  /**
   * Get the ClientUserIdHistory {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientUserIdHistory {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientUserIdHistoryNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientUserIdHistoryNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientUserIdHistoryNodeAsync();

  /**
   * Get the local value of the AuthenticationMechanism Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AuthenticationMechanism Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationMechanism Node.
   */
  String getAuthenticationMechanism() throws UaException;

  /**
   * Set the local value of the AuthenticationMechanism Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AuthenticationMechanism Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationMechanism Node.
   */
  void setAuthenticationMechanism(String value) throws UaException;

  /**
   * Read the value of the AuthenticationMechanism Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readAuthenticationMechanism() throws UaException;

  /**
   * Write a new value for the AuthenticationMechanism Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAuthenticationMechanism(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAuthenticationMechanism}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readAuthenticationMechanismAsync();

  /**
   * An asynchronous implementation of {@link #writeAuthenticationMechanism}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAuthenticationMechanismAsync(String value);

  /**
   * Get the AuthenticationMechanism {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AuthenticationMechanism {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAuthenticationMechanismNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAuthenticationMechanismNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAuthenticationMechanismNodeAsync();

  /**
   * Get the local value of the Encoding Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Encoding Node.
   * @throws UaException if an error occurs creating or getting the Encoding Node.
   */
  String getEncoding() throws UaException;

  /**
   * Set the local value of the Encoding Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Encoding Node.
   * @throws UaException if an error occurs creating or getting the Encoding Node.
   */
  void setEncoding(String value) throws UaException;

  /**
   * Read the value of the Encoding Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readEncoding() throws UaException;

  /**
   * Write a new value for the Encoding Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEncoding(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEncoding}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readEncodingAsync();

  /**
   * An asynchronous implementation of {@link #writeEncoding}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEncodingAsync(String value);

  /**
   * Get the Encoding {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Encoding {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getEncodingNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEncodingNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEncodingNodeAsync();

  /**
   * Get the local value of the TransportProtocol Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransportProtocol Node.
   * @throws UaException if an error occurs creating or getting the TransportProtocol Node.
   */
  String getTransportProtocol() throws UaException;

  /**
   * Set the local value of the TransportProtocol Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransportProtocol Node.
   * @throws UaException if an error occurs creating or getting the TransportProtocol Node.
   */
  void setTransportProtocol(String value) throws UaException;

  /**
   * Read the value of the TransportProtocol Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readTransportProtocol() throws UaException;

  /**
   * Write a new value for the TransportProtocol Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransportProtocol(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransportProtocol}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readTransportProtocolAsync();

  /**
   * An asynchronous implementation of {@link #writeTransportProtocol}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransportProtocolAsync(String value);

  /**
   * Get the TransportProtocol {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportProtocol {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTransportProtocolNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportProtocolNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransportProtocolNodeAsync();

  /**
   * Get the local value of the SecurityMode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  MessageSecurityMode getSecurityMode() throws UaException;

  /**
   * Set the local value of the SecurityMode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  void setSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * Read the value of the SecurityMode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link MessageSecurityMode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Write a new value for the SecurityMode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link MessageSecurityMode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends MessageSecurityMode> readSecurityModeAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityModeAsync(MessageSecurityMode value);

  /**
   * Get the SecurityMode {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityMode {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSecurityModeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityModeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityModeNodeAsync();

  /**
   * Get the local value of the SecurityPolicyUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityPolicyUri Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUri Node.
   */
  String getSecurityPolicyUri() throws UaException;

  /**
   * Set the local value of the SecurityPolicyUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityPolicyUri Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUri Node.
   */
  void setSecurityPolicyUri(String value) throws UaException;

  /**
   * Read the value of the SecurityPolicyUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecurityPolicyUri() throws UaException;

  /**
   * Write a new value for the SecurityPolicyUri Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityPolicyUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityPolicyUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecurityPolicyUriAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityPolicyUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(String value);

  /**
   * Get the SecurityPolicyUri {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityPolicyUri {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSecurityPolicyUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityPolicyUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityPolicyUriNodeAsync();

  /**
   * Get the local value of the ClientCertificate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  ByteString getClientCertificate() throws UaException;

  /**
   * Set the local value of the ClientCertificate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  void setClientCertificate(ByteString value) throws UaException;

  /**
   * Read the value of the ClientCertificate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readClientCertificate() throws UaException;

  /**
   * Write a new value for the ClientCertificate Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientCertificate(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readClientCertificateAsync();

  /**
   * An asynchronous implementation of {@link #writeClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientCertificateAsync(ByteString value);

  /**
   * Get the ClientCertificate {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientCertificate {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientCertificateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientCertificateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientCertificateNodeAsync();
}
