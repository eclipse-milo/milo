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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14</a>
 */
public interface SessionDiagnosticsVariableType extends BaseDataVariableType {
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
   * Get the local value of the SessionName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionName Node.
   * @throws UaException if an error occurs creating or getting the SessionName Node.
   */
  String getSessionName() throws UaException;

  /**
   * Set the local value of the SessionName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionName Node.
   * @throws UaException if an error occurs creating or getting the SessionName Node.
   */
  void setSessionName(String value) throws UaException;

  /**
   * Read the value of the SessionName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSessionName() throws UaException;

  /**
   * Write a new value for the SessionName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSessionNameAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionNameAsync(String value);

  /**
   * Get the SessionName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionName {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSessionNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionNameNodeAsync();

  /**
   * Get the local value of the ClientDescription Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientDescription Node.
   * @throws UaException if an error occurs creating or getting the ClientDescription Node.
   */
  ApplicationDescription getClientDescription() throws UaException;

  /**
   * Set the local value of the ClientDescription Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientDescription Node.
   * @throws UaException if an error occurs creating or getting the ClientDescription Node.
   */
  void setClientDescription(ApplicationDescription value) throws UaException;

  /**
   * Read the value of the ClientDescription Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ApplicationDescription} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ApplicationDescription readClientDescription() throws UaException;

  /**
   * Write a new value for the ClientDescription Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ApplicationDescription} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientDescription(ApplicationDescription value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientDescription}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ApplicationDescription> readClientDescriptionAsync();

  /**
   * An asynchronous implementation of {@link #writeClientDescription}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientDescriptionAsync(ApplicationDescription value);

  /**
   * Get the ClientDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientDescriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientDescriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientDescriptionNodeAsync();

  /**
   * Get the local value of the ServerUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerUri Node.
   * @throws UaException if an error occurs creating or getting the ServerUri Node.
   */
  String getServerUri() throws UaException;

  /**
   * Set the local value of the ServerUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerUri Node.
   * @throws UaException if an error occurs creating or getting the ServerUri Node.
   */
  void setServerUri(String value) throws UaException;

  /**
   * Read the value of the ServerUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readServerUri() throws UaException;

  /**
   * Write a new value for the ServerUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readServerUriAsync();

  /**
   * An asynchronous implementation of {@link #writeServerUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerUriAsync(String value);

  /**
   * Get the ServerUri {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerUri {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getServerUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getServerUriNodeAsync();

  /**
   * Get the local value of the EndpointUrl Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EndpointUrl Node.
   * @throws UaException if an error occurs creating or getting the EndpointUrl Node.
   */
  String getEndpointUrl() throws UaException;

  /**
   * Set the local value of the EndpointUrl Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EndpointUrl Node.
   * @throws UaException if an error occurs creating or getting the EndpointUrl Node.
   */
  void setEndpointUrl(String value) throws UaException;

  /**
   * Read the value of the EndpointUrl Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readEndpointUrl() throws UaException;

  /**
   * Write a new value for the EndpointUrl Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEndpointUrl(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEndpointUrl}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readEndpointUrlAsync();

  /**
   * An asynchronous implementation of {@link #writeEndpointUrl}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(String value);

  /**
   * Get the EndpointUrl {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EndpointUrl {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getEndpointUrlNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndpointUrlNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEndpointUrlNodeAsync();

  /**
   * Get the local value of the LocaleIds Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LocaleIds Node.
   * @throws UaException if an error occurs creating or getting the LocaleIds Node.
   */
  String[] getLocaleIds() throws UaException;

  /**
   * Set the local value of the LocaleIds Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LocaleIds Node.
   * @throws UaException if an error occurs creating or getting the LocaleIds Node.
   */
  void setLocaleIds(String[] value) throws UaException;

  /**
   * Read the value of the LocaleIds Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readLocaleIds() throws UaException;

  /**
   * Write a new value for the LocaleIds Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLocaleIds(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLocaleIds}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readLocaleIdsAsync();

  /**
   * An asynchronous implementation of {@link #writeLocaleIds}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLocaleIdsAsync(String[] value);

  /**
   * Get the LocaleIds {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LocaleIds {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLocaleIdsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLocaleIdsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLocaleIdsNodeAsync();

  /**
   * Get the local value of the ActualSessionTimeout Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActualSessionTimeout Node.
   * @throws UaException if an error occurs creating or getting the ActualSessionTimeout Node.
   */
  Double getActualSessionTimeout() throws UaException;

  /**
   * Set the local value of the ActualSessionTimeout Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActualSessionTimeout Node.
   * @throws UaException if an error occurs creating or getting the ActualSessionTimeout Node.
   */
  void setActualSessionTimeout(Double value) throws UaException;

  /**
   * Read the value of the ActualSessionTimeout Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readActualSessionTimeout() throws UaException;

  /**
   * Write a new value for the ActualSessionTimeout Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActualSessionTimeout(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActualSessionTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readActualSessionTimeoutAsync();

  /**
   * An asynchronous implementation of {@link #writeActualSessionTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActualSessionTimeoutAsync(Double value);

  /**
   * Get the ActualSessionTimeout {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActualSessionTimeout {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getActualSessionTimeoutNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActualSessionTimeoutNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getActualSessionTimeoutNodeAsync();

  /**
   * Get the local value of the MaxResponseMessageSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxResponseMessageSize Node.
   * @throws UaException if an error occurs creating or getting the MaxResponseMessageSize Node.
   */
  UInteger getMaxResponseMessageSize() throws UaException;

  /**
   * Set the local value of the MaxResponseMessageSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxResponseMessageSize Node.
   * @throws UaException if an error occurs creating or getting the MaxResponseMessageSize Node.
   */
  void setMaxResponseMessageSize(UInteger value) throws UaException;

  /**
   * Read the value of the MaxResponseMessageSize Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxResponseMessageSize() throws UaException;

  /**
   * Write a new value for the MaxResponseMessageSize Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxResponseMessageSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxResponseMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxResponseMessageSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxResponseMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxResponseMessageSizeAsync(UInteger value);

  /**
   * Get the MaxResponseMessageSize {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxResponseMessageSize {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxResponseMessageSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxResponseMessageSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxResponseMessageSizeNodeAsync();

  /**
   * Get the local value of the ClientConnectionTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientConnectionTime Node.
   * @throws UaException if an error occurs creating or getting the ClientConnectionTime Node.
   */
  DateTime getClientConnectionTime() throws UaException;

  /**
   * Set the local value of the ClientConnectionTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientConnectionTime Node.
   * @throws UaException if an error occurs creating or getting the ClientConnectionTime Node.
   */
  void setClientConnectionTime(DateTime value) throws UaException;

  /**
   * Read the value of the ClientConnectionTime Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readClientConnectionTime() throws UaException;

  /**
   * Write a new value for the ClientConnectionTime Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientConnectionTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientConnectionTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readClientConnectionTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeClientConnectionTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientConnectionTimeAsync(DateTime value);

  /**
   * Get the ClientConnectionTime {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientConnectionTime {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientConnectionTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientConnectionTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientConnectionTimeNodeAsync();

  /**
   * Get the local value of the ClientLastContactTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientLastContactTime Node.
   * @throws UaException if an error occurs creating or getting the ClientLastContactTime Node.
   */
  DateTime getClientLastContactTime() throws UaException;

  /**
   * Set the local value of the ClientLastContactTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientLastContactTime Node.
   * @throws UaException if an error occurs creating or getting the ClientLastContactTime Node.
   */
  void setClientLastContactTime(DateTime value) throws UaException;

  /**
   * Read the value of the ClientLastContactTime Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readClientLastContactTime() throws UaException;

  /**
   * Write a new value for the ClientLastContactTime Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientLastContactTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientLastContactTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readClientLastContactTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeClientLastContactTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientLastContactTimeAsync(DateTime value);

  /**
   * Get the ClientLastContactTime {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientLastContactTime {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getClientLastContactTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientLastContactTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getClientLastContactTimeNodeAsync();

  /**
   * Get the local value of the CurrentSubscriptionsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSubscriptionsCount Node.
   */
  UInteger getCurrentSubscriptionsCount() throws UaException;

  /**
   * Set the local value of the CurrentSubscriptionsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSubscriptionsCount Node.
   */
  void setCurrentSubscriptionsCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentSubscriptionsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentSubscriptionsCount() throws UaException;

  /**
   * Write a new value for the CurrentSubscriptionsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentSubscriptionsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentSubscriptionsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentSubscriptionsCountAsync(UInteger value);

  /**
   * Get the CurrentSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentSubscriptionsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentSubscriptionsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSubscriptionsCountNodeAsync();

  /**
   * Get the local value of the CurrentMonitoredItemsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentMonitoredItemsCount Node.
   */
  UInteger getCurrentMonitoredItemsCount() throws UaException;

  /**
   * Set the local value of the CurrentMonitoredItemsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentMonitoredItemsCount Node.
   */
  void setCurrentMonitoredItemsCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentMonitoredItemsCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentMonitoredItemsCount() throws UaException;

  /**
   * Write a new value for the CurrentMonitoredItemsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentMonitoredItemsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentMonitoredItemsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentMonitoredItemsCountAsync(UInteger value);

  /**
   * Get the CurrentMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentMonitoredItemsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentMonitoredItemsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentMonitoredItemsCountNodeAsync();

  /**
   * Get the local value of the CurrentPublishRequestsInQueue Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentPublishRequestsInQueue Node.
   * @throws UaException if an error occurs creating or getting the CurrentPublishRequestsInQueue
   *     Node.
   */
  UInteger getCurrentPublishRequestsInQueue() throws UaException;

  /**
   * Set the local value of the CurrentPublishRequestsInQueue Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentPublishRequestsInQueue Node.
   * @throws UaException if an error occurs creating or getting the CurrentPublishRequestsInQueue
   *     Node.
   */
  void setCurrentPublishRequestsInQueue(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentPublishRequestsInQueue Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentPublishRequestsInQueue() throws UaException;

  /**
   * Write a new value for the CurrentPublishRequestsInQueue Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentPublishRequestsInQueue(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentPublishRequestsInQueue}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentPublishRequestsInQueueAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentPublishRequestsInQueue}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentPublishRequestsInQueueAsync(UInteger value);

  /**
   * Get the CurrentPublishRequestsInQueue {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentPublishRequestsInQueue {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentPublishRequestsInQueueNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentPublishRequestsInQueueNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentPublishRequestsInQueueNodeAsync();

  /**
   * Get the local value of the TotalRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TotalRequestCount Node.
   * @throws UaException if an error occurs creating or getting the TotalRequestCount Node.
   */
  ServiceCounterDataType getTotalRequestCount() throws UaException;

  /**
   * Set the local value of the TotalRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TotalRequestCount Node.
   * @throws UaException if an error occurs creating or getting the TotalRequestCount Node.
   */
  void setTotalRequestCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the TotalRequestCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readTotalRequestCount() throws UaException;

  /**
   * Write a new value for the TotalRequestCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTotalRequestCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTotalRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readTotalRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTotalRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTotalRequestCountAsync(ServiceCounterDataType value);

  /**
   * Get the TotalRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TotalRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTotalRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTotalRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTotalRequestCountNodeAsync();

  /**
   * Get the local value of the UnauthorizedRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnauthorizedRequestCount Node.
   * @throws UaException if an error occurs creating or getting the UnauthorizedRequestCount Node.
   */
  UInteger getUnauthorizedRequestCount() throws UaException;

  /**
   * Set the local value of the UnauthorizedRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnauthorizedRequestCount Node.
   * @throws UaException if an error occurs creating or getting the UnauthorizedRequestCount Node.
   */
  void setUnauthorizedRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the UnauthorizedRequestCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readUnauthorizedRequestCount() throws UaException;

  /**
   * Write a new value for the UnauthorizedRequestCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnauthorizedRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnauthorizedRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readUnauthorizedRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeUnauthorizedRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnauthorizedRequestCountAsync(UInteger value);

  /**
   * Get the UnauthorizedRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnauthorizedRequestCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getUnauthorizedRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnauthorizedRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnauthorizedRequestCountNodeAsync();

  /**
   * Get the local value of the ReadCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReadCount Node.
   * @throws UaException if an error occurs creating or getting the ReadCount Node.
   */
  ServiceCounterDataType getReadCount() throws UaException;

  /**
   * Set the local value of the ReadCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReadCount Node.
   * @throws UaException if an error occurs creating or getting the ReadCount Node.
   */
  void setReadCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the ReadCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readReadCount() throws UaException;

  /**
   * Write a new value for the ReadCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReadCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReadCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readReadCountAsync();

  /**
   * An asynchronous implementation of {@link #writeReadCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReadCountAsync(ServiceCounterDataType value);

  /**
   * Get the ReadCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getReadCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getReadCountNodeAsync();

  /**
   * Get the local value of the HistoryReadCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HistoryReadCount Node.
   * @throws UaException if an error occurs creating or getting the HistoryReadCount Node.
   */
  ServiceCounterDataType getHistoryReadCount() throws UaException;

  /**
   * Set the local value of the HistoryReadCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HistoryReadCount Node.
   * @throws UaException if an error occurs creating or getting the HistoryReadCount Node.
   */
  void setHistoryReadCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the HistoryReadCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readHistoryReadCount() throws UaException;

  /**
   * Write a new value for the HistoryReadCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHistoryReadCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHistoryReadCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readHistoryReadCountAsync();

  /**
   * An asynchronous implementation of {@link #writeHistoryReadCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHistoryReadCountAsync(ServiceCounterDataType value);

  /**
   * Get the HistoryReadCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HistoryReadCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getHistoryReadCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHistoryReadCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getHistoryReadCountNodeAsync();

  /**
   * Get the local value of the WriteCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the WriteCount Node.
   * @throws UaException if an error occurs creating or getting the WriteCount Node.
   */
  ServiceCounterDataType getWriteCount() throws UaException;

  /**
   * Set the local value of the WriteCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the WriteCount Node.
   * @throws UaException if an error occurs creating or getting the WriteCount Node.
   */
  void setWriteCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the WriteCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readWriteCount() throws UaException;

  /**
   * Write a new value for the WriteCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeWriteCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readWriteCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readWriteCountAsync();

  /**
   * An asynchronous implementation of {@link #writeWriteCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeWriteCountAsync(ServiceCounterDataType value);

  /**
   * Get the WriteCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the WriteCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getWriteCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getWriteCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getWriteCountNodeAsync();

  /**
   * Get the local value of the HistoryUpdateCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HistoryUpdateCount Node.
   * @throws UaException if an error occurs creating or getting the HistoryUpdateCount Node.
   */
  ServiceCounterDataType getHistoryUpdateCount() throws UaException;

  /**
   * Set the local value of the HistoryUpdateCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HistoryUpdateCount Node.
   * @throws UaException if an error occurs creating or getting the HistoryUpdateCount Node.
   */
  void setHistoryUpdateCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the HistoryUpdateCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readHistoryUpdateCount() throws UaException;

  /**
   * Write a new value for the HistoryUpdateCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHistoryUpdateCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHistoryUpdateCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readHistoryUpdateCountAsync();

  /**
   * An asynchronous implementation of {@link #writeHistoryUpdateCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHistoryUpdateCountAsync(ServiceCounterDataType value);

  /**
   * Get the HistoryUpdateCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HistoryUpdateCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getHistoryUpdateCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHistoryUpdateCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getHistoryUpdateCountNodeAsync();

  /**
   * Get the local value of the CallCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CallCount Node.
   * @throws UaException if an error occurs creating or getting the CallCount Node.
   */
  ServiceCounterDataType getCallCount() throws UaException;

  /**
   * Set the local value of the CallCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CallCount Node.
   * @throws UaException if an error occurs creating or getting the CallCount Node.
   */
  void setCallCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the CallCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readCallCount() throws UaException;

  /**
   * Write a new value for the CallCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCallCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCallCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readCallCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCallCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCallCountAsync(ServiceCounterDataType value);

  /**
   * Get the CallCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CallCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCallCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCallCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCallCountNodeAsync();

  /**
   * Get the local value of the CreateMonitoredItemsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CreateMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the CreateMonitoredItemsCount Node.
   */
  ServiceCounterDataType getCreateMonitoredItemsCount() throws UaException;

  /**
   * Set the local value of the CreateMonitoredItemsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CreateMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the CreateMonitoredItemsCount Node.
   */
  void setCreateMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the CreateMonitoredItemsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readCreateMonitoredItemsCount() throws UaException;

  /**
   * Write a new value for the CreateMonitoredItemsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCreateMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCreateMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readCreateMonitoredItemsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCreateMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCreateMonitoredItemsCountAsync(ServiceCounterDataType value);

  /**
   * Get the CreateMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCreateMonitoredItemsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateMonitoredItemsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateMonitoredItemsCountNodeAsync();

  /**
   * Get the local value of the ModifyMonitoredItemsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ModifyMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the ModifyMonitoredItemsCount Node.
   */
  ServiceCounterDataType getModifyMonitoredItemsCount() throws UaException;

  /**
   * Set the local value of the ModifyMonitoredItemsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ModifyMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the ModifyMonitoredItemsCount Node.
   */
  void setModifyMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the ModifyMonitoredItemsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readModifyMonitoredItemsCount() throws UaException;

  /**
   * Write a new value for the ModifyMonitoredItemsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeModifyMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readModifyMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readModifyMonitoredItemsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeModifyMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeModifyMonitoredItemsCountAsync(ServiceCounterDataType value);

  /**
   * Get the ModifyMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ModifyMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getModifyMonitoredItemsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModifyMonitoredItemsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifyMonitoredItemsCountNodeAsync();

  /**
   * Get the local value of the SetMonitoringModeCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SetMonitoringModeCount Node.
   * @throws UaException if an error occurs creating or getting the SetMonitoringModeCount Node.
   */
  ServiceCounterDataType getSetMonitoringModeCount() throws UaException;

  /**
   * Set the local value of the SetMonitoringModeCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SetMonitoringModeCount Node.
   * @throws UaException if an error occurs creating or getting the SetMonitoringModeCount Node.
   */
  void setSetMonitoringModeCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the SetMonitoringModeCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readSetMonitoringModeCount() throws UaException;

  /**
   * Write a new value for the SetMonitoringModeCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSetMonitoringModeCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSetMonitoringModeCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readSetMonitoringModeCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSetMonitoringModeCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSetMonitoringModeCountAsync(ServiceCounterDataType value);

  /**
   * Get the SetMonitoringModeCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SetMonitoringModeCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSetMonitoringModeCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSetMonitoringModeCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetMonitoringModeCountNodeAsync();

  /**
   * Get the local value of the SetTriggeringCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SetTriggeringCount Node.
   * @throws UaException if an error occurs creating or getting the SetTriggeringCount Node.
   */
  ServiceCounterDataType getSetTriggeringCount() throws UaException;

  /**
   * Set the local value of the SetTriggeringCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SetTriggeringCount Node.
   * @throws UaException if an error occurs creating or getting the SetTriggeringCount Node.
   */
  void setSetTriggeringCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the SetTriggeringCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readSetTriggeringCount() throws UaException;

  /**
   * Write a new value for the SetTriggeringCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSetTriggeringCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSetTriggeringCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readSetTriggeringCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSetTriggeringCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSetTriggeringCountAsync(ServiceCounterDataType value);

  /**
   * Get the SetTriggeringCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SetTriggeringCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSetTriggeringCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSetTriggeringCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetTriggeringCountNodeAsync();

  /**
   * Get the local value of the DeleteMonitoredItemsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteMonitoredItemsCount Node.
   */
  ServiceCounterDataType getDeleteMonitoredItemsCount() throws UaException;

  /**
   * Set the local value of the DeleteMonitoredItemsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteMonitoredItemsCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteMonitoredItemsCount Node.
   */
  void setDeleteMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the DeleteMonitoredItemsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readDeleteMonitoredItemsCount() throws UaException;

  /**
   * Write a new value for the DeleteMonitoredItemsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteMonitoredItemsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readDeleteMonitoredItemsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteMonitoredItemsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteMonitoredItemsCountAsync(ServiceCounterDataType value);

  /**
   * Get the DeleteMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteMonitoredItemsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDeleteMonitoredItemsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteMonitoredItemsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteMonitoredItemsCountNodeAsync();

  /**
   * Get the local value of the CreateSubscriptionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CreateSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CreateSubscriptionCount Node.
   */
  ServiceCounterDataType getCreateSubscriptionCount() throws UaException;

  /**
   * Set the local value of the CreateSubscriptionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CreateSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CreateSubscriptionCount Node.
   */
  void setCreateSubscriptionCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the CreateSubscriptionCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readCreateSubscriptionCount() throws UaException;

  /**
   * Write a new value for the CreateSubscriptionCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCreateSubscriptionCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCreateSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readCreateSubscriptionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCreateSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCreateSubscriptionCountAsync(ServiceCounterDataType value);

  /**
   * Get the CreateSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCreateSubscriptionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateSubscriptionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateSubscriptionCountNodeAsync();

  /**
   * Get the local value of the ModifySubscriptionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ModifySubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the ModifySubscriptionCount Node.
   */
  ServiceCounterDataType getModifySubscriptionCount() throws UaException;

  /**
   * Set the local value of the ModifySubscriptionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ModifySubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the ModifySubscriptionCount Node.
   */
  void setModifySubscriptionCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the ModifySubscriptionCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readModifySubscriptionCount() throws UaException;

  /**
   * Write a new value for the ModifySubscriptionCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeModifySubscriptionCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readModifySubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readModifySubscriptionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeModifySubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeModifySubscriptionCountAsync(ServiceCounterDataType value);

  /**
   * Get the ModifySubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ModifySubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getModifySubscriptionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModifySubscriptionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifySubscriptionCountNodeAsync();

  /**
   * Get the local value of the SetPublishingModeCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SetPublishingModeCount Node.
   * @throws UaException if an error occurs creating or getting the SetPublishingModeCount Node.
   */
  ServiceCounterDataType getSetPublishingModeCount() throws UaException;

  /**
   * Set the local value of the SetPublishingModeCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SetPublishingModeCount Node.
   * @throws UaException if an error occurs creating or getting the SetPublishingModeCount Node.
   */
  void setSetPublishingModeCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the SetPublishingModeCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readSetPublishingModeCount() throws UaException;

  /**
   * Write a new value for the SetPublishingModeCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSetPublishingModeCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSetPublishingModeCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readSetPublishingModeCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSetPublishingModeCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSetPublishingModeCountAsync(ServiceCounterDataType value);

  /**
   * Get the SetPublishingModeCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SetPublishingModeCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSetPublishingModeCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSetPublishingModeCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSetPublishingModeCountNodeAsync();

  /**
   * Get the local value of the PublishCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishCount Node.
   * @throws UaException if an error occurs creating or getting the PublishCount Node.
   */
  ServiceCounterDataType getPublishCount() throws UaException;

  /**
   * Set the local value of the PublishCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishCount Node.
   * @throws UaException if an error occurs creating or getting the PublishCount Node.
   */
  void setPublishCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the PublishCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readPublishCount() throws UaException;

  /**
   * Write a new value for the PublishCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readPublishCountAsync();

  /**
   * An asynchronous implementation of {@link #writePublishCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishCountAsync(ServiceCounterDataType value);

  /**
   * Get the PublishCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPublishCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishCountNodeAsync();

  /**
   * Get the local value of the RepublishCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RepublishCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishCount Node.
   */
  ServiceCounterDataType getRepublishCount() throws UaException;

  /**
   * Set the local value of the RepublishCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RepublishCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishCount Node.
   */
  void setRepublishCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the RepublishCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readRepublishCount() throws UaException;

  /**
   * Write a new value for the RepublishCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRepublishCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRepublishCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readRepublishCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRepublishCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRepublishCountAsync(ServiceCounterDataType value);

  /**
   * Get the RepublishCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RepublishCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRepublishCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRepublishCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishCountNodeAsync();

  /**
   * Get the local value of the TransferSubscriptionsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransferSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the TransferSubscriptionsCount Node.
   */
  ServiceCounterDataType getTransferSubscriptionsCount() throws UaException;

  /**
   * Set the local value of the TransferSubscriptionsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransferSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the TransferSubscriptionsCount Node.
   */
  void setTransferSubscriptionsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the TransferSubscriptionsCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readTransferSubscriptionsCount() throws UaException;

  /**
   * Write a new value for the TransferSubscriptionsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransferSubscriptionsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransferSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readTransferSubscriptionsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTransferSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransferSubscriptionsCountAsync(ServiceCounterDataType value);

  /**
   * Get the TransferSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransferSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTransferSubscriptionsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransferSubscriptionsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferSubscriptionsCountNodeAsync();

  /**
   * Get the local value of the DeleteSubscriptionsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteSubscriptionsCount Node.
   */
  ServiceCounterDataType getDeleteSubscriptionsCount() throws UaException;

  /**
   * Set the local value of the DeleteSubscriptionsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteSubscriptionsCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteSubscriptionsCount Node.
   */
  void setDeleteSubscriptionsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the DeleteSubscriptionsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readDeleteSubscriptionsCount() throws UaException;

  /**
   * Write a new value for the DeleteSubscriptionsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteSubscriptionsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readDeleteSubscriptionsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteSubscriptionsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteSubscriptionsCountAsync(ServiceCounterDataType value);

  /**
   * Get the DeleteSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteSubscriptionsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDeleteSubscriptionsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteSubscriptionsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteSubscriptionsCountNodeAsync();

  /**
   * Get the local value of the AddNodesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AddNodesCount Node.
   * @throws UaException if an error occurs creating or getting the AddNodesCount Node.
   */
  ServiceCounterDataType getAddNodesCount() throws UaException;

  /**
   * Set the local value of the AddNodesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AddNodesCount Node.
   * @throws UaException if an error occurs creating or getting the AddNodesCount Node.
   */
  void setAddNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the AddNodesCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readAddNodesCount() throws UaException;

  /**
   * Write a new value for the AddNodesCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAddNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAddNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readAddNodesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeAddNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAddNodesCountAsync(ServiceCounterDataType value);

  /**
   * Get the AddNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AddNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAddNodesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAddNodesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAddNodesCountNodeAsync();

  /**
   * Get the local value of the AddReferencesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AddReferencesCount Node.
   * @throws UaException if an error occurs creating or getting the AddReferencesCount Node.
   */
  ServiceCounterDataType getAddReferencesCount() throws UaException;

  /**
   * Set the local value of the AddReferencesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AddReferencesCount Node.
   * @throws UaException if an error occurs creating or getting the AddReferencesCount Node.
   */
  void setAddReferencesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the AddReferencesCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readAddReferencesCount() throws UaException;

  /**
   * Write a new value for the AddReferencesCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAddReferencesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAddReferencesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readAddReferencesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeAddReferencesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAddReferencesCountAsync(ServiceCounterDataType value);

  /**
   * Get the AddReferencesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AddReferencesCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAddReferencesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAddReferencesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAddReferencesCountNodeAsync();

  /**
   * Get the local value of the DeleteNodesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteNodesCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteNodesCount Node.
   */
  ServiceCounterDataType getDeleteNodesCount() throws UaException;

  /**
   * Set the local value of the DeleteNodesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteNodesCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteNodesCount Node.
   */
  void setDeleteNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the DeleteNodesCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readDeleteNodesCount() throws UaException;

  /**
   * Write a new value for the DeleteNodesCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readDeleteNodesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteNodesCountAsync(ServiceCounterDataType value);

  /**
   * Get the DeleteNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDeleteNodesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteNodesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteNodesCountNodeAsync();

  /**
   * Get the local value of the DeleteReferencesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteReferencesCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteReferencesCount Node.
   */
  ServiceCounterDataType getDeleteReferencesCount() throws UaException;

  /**
   * Set the local value of the DeleteReferencesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteReferencesCount Node.
   * @throws UaException if an error occurs creating or getting the DeleteReferencesCount Node.
   */
  void setDeleteReferencesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the DeleteReferencesCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readDeleteReferencesCount() throws UaException;

  /**
   * Write a new value for the DeleteReferencesCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteReferencesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteReferencesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readDeleteReferencesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteReferencesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteReferencesCountAsync(ServiceCounterDataType value);

  /**
   * Get the DeleteReferencesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteReferencesCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDeleteReferencesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteReferencesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDeleteReferencesCountNodeAsync();

  /**
   * Get the local value of the BrowseCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BrowseCount Node.
   * @throws UaException if an error occurs creating or getting the BrowseCount Node.
   */
  ServiceCounterDataType getBrowseCount() throws UaException;

  /**
   * Set the local value of the BrowseCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BrowseCount Node.
   * @throws UaException if an error occurs creating or getting the BrowseCount Node.
   */
  void setBrowseCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the BrowseCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readBrowseCount() throws UaException;

  /**
   * Write a new value for the BrowseCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBrowseCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBrowseCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readBrowseCountAsync();

  /**
   * An asynchronous implementation of {@link #writeBrowseCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBrowseCountAsync(ServiceCounterDataType value);

  /**
   * Get the BrowseCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BrowseCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getBrowseCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBrowseCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBrowseCountNodeAsync();

  /**
   * Get the local value of the BrowseNextCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BrowseNextCount Node.
   * @throws UaException if an error occurs creating or getting the BrowseNextCount Node.
   */
  ServiceCounterDataType getBrowseNextCount() throws UaException;

  /**
   * Set the local value of the BrowseNextCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BrowseNextCount Node.
   * @throws UaException if an error occurs creating or getting the BrowseNextCount Node.
   */
  void setBrowseNextCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the BrowseNextCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readBrowseNextCount() throws UaException;

  /**
   * Write a new value for the BrowseNextCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBrowseNextCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBrowseNextCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readBrowseNextCountAsync();

  /**
   * An asynchronous implementation of {@link #writeBrowseNextCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBrowseNextCountAsync(ServiceCounterDataType value);

  /**
   * Get the BrowseNextCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BrowseNextCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getBrowseNextCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBrowseNextCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBrowseNextCountNodeAsync();

  /**
   * Get the local value of the TranslateBrowsePathsToNodeIdsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TranslateBrowsePathsToNodeIdsCount Node.
   * @throws UaException if an error occurs creating or getting the
   *     TranslateBrowsePathsToNodeIdsCount Node.
   */
  ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() throws UaException;

  /**
   * Set the local value of the TranslateBrowsePathsToNodeIdsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TranslateBrowsePathsToNodeIdsCount Node.
   * @throws UaException if an error occurs creating or getting the
   *     TranslateBrowsePathsToNodeIdsCount Node.
   */
  void setTranslateBrowsePathsToNodeIdsCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the TranslateBrowsePathsToNodeIdsCount Node from the server and update the
   * local value if the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readTranslateBrowsePathsToNodeIdsCount() throws UaException;

  /**
   * Write a new value for the TranslateBrowsePathsToNodeIdsCount Node to the server and update the
   * local value if the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTranslateBrowsePathsToNodeIdsCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTranslateBrowsePathsToNodeIdsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readTranslateBrowsePathsToNodeIdsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTranslateBrowsePathsToNodeIdsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTranslateBrowsePathsToNodeIdsCountAsync(
      ServiceCounterDataType value);

  /**
   * Get the TranslateBrowsePathsToNodeIdsCount {@link BaseDataVariableType} Node, or {@code null}
   * if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TranslateBrowsePathsToNodeIdsCount {@link BaseDataVariableType} Node, or {@code
   *     null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTranslateBrowsePathsToNodeIdsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTranslateBrowsePathsToNodeIdsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType>
      getTranslateBrowsePathsToNodeIdsCountNodeAsync();

  /**
   * Get the local value of the QueryFirstCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the QueryFirstCount Node.
   * @throws UaException if an error occurs creating or getting the QueryFirstCount Node.
   */
  ServiceCounterDataType getQueryFirstCount() throws UaException;

  /**
   * Set the local value of the QueryFirstCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the QueryFirstCount Node.
   * @throws UaException if an error occurs creating or getting the QueryFirstCount Node.
   */
  void setQueryFirstCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the QueryFirstCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readQueryFirstCount() throws UaException;

  /**
   * Write a new value for the QueryFirstCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeQueryFirstCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readQueryFirstCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readQueryFirstCountAsync();

  /**
   * An asynchronous implementation of {@link #writeQueryFirstCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeQueryFirstCountAsync(ServiceCounterDataType value);

  /**
   * Get the QueryFirstCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the QueryFirstCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getQueryFirstCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getQueryFirstCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getQueryFirstCountNodeAsync();

  /**
   * Get the local value of the QueryNextCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the QueryNextCount Node.
   * @throws UaException if an error occurs creating or getting the QueryNextCount Node.
   */
  ServiceCounterDataType getQueryNextCount() throws UaException;

  /**
   * Set the local value of the QueryNextCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the QueryNextCount Node.
   * @throws UaException if an error occurs creating or getting the QueryNextCount Node.
   */
  void setQueryNextCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the QueryNextCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readQueryNextCount() throws UaException;

  /**
   * Write a new value for the QueryNextCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeQueryNextCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readQueryNextCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readQueryNextCountAsync();

  /**
   * An asynchronous implementation of {@link #writeQueryNextCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeQueryNextCountAsync(ServiceCounterDataType value);

  /**
   * Get the QueryNextCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the QueryNextCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getQueryNextCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getQueryNextCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getQueryNextCountNodeAsync();

  /**
   * Get the local value of the RegisterNodesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RegisterNodesCount Node.
   * @throws UaException if an error occurs creating or getting the RegisterNodesCount Node.
   */
  ServiceCounterDataType getRegisterNodesCount() throws UaException;

  /**
   * Set the local value of the RegisterNodesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RegisterNodesCount Node.
   * @throws UaException if an error occurs creating or getting the RegisterNodesCount Node.
   */
  void setRegisterNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the RegisterNodesCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readRegisterNodesCount() throws UaException;

  /**
   * Write a new value for the RegisterNodesCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRegisterNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRegisterNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readRegisterNodesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRegisterNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRegisterNodesCountAsync(ServiceCounterDataType value);

  /**
   * Get the RegisterNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RegisterNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRegisterNodesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRegisterNodesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRegisterNodesCountNodeAsync();

  /**
   * Get the local value of the UnregisterNodesCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnregisterNodesCount Node.
   * @throws UaException if an error occurs creating or getting the UnregisterNodesCount Node.
   */
  ServiceCounterDataType getUnregisterNodesCount() throws UaException;

  /**
   * Set the local value of the UnregisterNodesCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnregisterNodesCount Node.
   * @throws UaException if an error occurs creating or getting the UnregisterNodesCount Node.
   */
  void setUnregisterNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * Read the value of the UnregisterNodesCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ServiceCounterDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServiceCounterDataType readUnregisterNodesCount() throws UaException;

  /**
   * Write a new value for the UnregisterNodesCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ServiceCounterDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnregisterNodesCount(ServiceCounterDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnregisterNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServiceCounterDataType> readUnregisterNodesCountAsync();

  /**
   * An asynchronous implementation of {@link #writeUnregisterNodesCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnregisterNodesCountAsync(ServiceCounterDataType value);

  /**
   * Get the UnregisterNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnregisterNodesCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getUnregisterNodesCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnregisterNodesCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnregisterNodesCountNodeAsync();
}
