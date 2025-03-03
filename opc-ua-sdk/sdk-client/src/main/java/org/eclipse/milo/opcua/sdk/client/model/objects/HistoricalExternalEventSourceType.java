/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2</a>
 */
public interface HistoricalExternalEventSourceType extends BaseObjectType {
  QualifiedProperty<String> SERVER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Server",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> ENDPOINT_URL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointUrl",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy> IDENTITY_TOKEN_POLICY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IdentityTokenPolicy",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<String> TRANSPORT_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TransportProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EventFilter> HISTORICAL_EVENT_FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HistoricalEventFilter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=725"),
          -1,
          EventFilter.class);

  /**
   * Get the local value of the Server Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Server Node.
   * @throws UaException if an error occurs creating or getting the Server Node.
   */
  String getServer() throws UaException;

  /**
   * Set the local value of the Server Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Server Node.
   * @throws UaException if an error occurs creating or getting the Server Node.
   */
  void setServer(String value) throws UaException;

  /**
   * Read the value of the Server Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readServer() throws UaException;

  /**
   * Write a new value for the Server Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServer(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServer}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readServerAsync();

  /**
   * An asynchronous implementation of {@link #writeServer}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerAsync(String value);

  /**
   * Get the Server {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Server {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerNodeAsync();

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
   * Get the EndpointUrl {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EndpointUrl {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEndpointUrlNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndpointUrlNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEndpointUrlNodeAsync();

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
   * Get the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityModeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityModeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

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
   * Get the SecurityPolicyUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityPolicyUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityPolicyUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

  /**
   * Get the local value of the IdentityTokenPolicy Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IdentityTokenPolicy Node.
   * @throws UaException if an error occurs creating or getting the IdentityTokenPolicy Node.
   */
  UserTokenPolicy getIdentityTokenPolicy() throws UaException;

  /**
   * Set the local value of the IdentityTokenPolicy Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IdentityTokenPolicy Node.
   * @throws UaException if an error occurs creating or getting the IdentityTokenPolicy Node.
   */
  void setIdentityTokenPolicy(UserTokenPolicy value) throws UaException;

  /**
   * Read the value of the IdentityTokenPolicy Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UserTokenPolicy} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UserTokenPolicy readIdentityTokenPolicy() throws UaException;

  /**
   * Write a new value for the IdentityTokenPolicy Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UserTokenPolicy} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIdentityTokenPolicy(UserTokenPolicy value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIdentityTokenPolicy}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UserTokenPolicy> readIdentityTokenPolicyAsync();

  /**
   * An asynchronous implementation of {@link #writeIdentityTokenPolicy}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIdentityTokenPolicyAsync(UserTokenPolicy value);

  /**
   * Get the IdentityTokenPolicy {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IdentityTokenPolicy {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIdentityTokenPolicyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdentityTokenPolicyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIdentityTokenPolicyNodeAsync();

  /**
   * Get the local value of the TransportProfileUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransportProfileUri Node.
   * @throws UaException if an error occurs creating or getting the TransportProfileUri Node.
   */
  String getTransportProfileUri() throws UaException;

  /**
   * Set the local value of the TransportProfileUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransportProfileUri Node.
   * @throws UaException if an error occurs creating or getting the TransportProfileUri Node.
   */
  void setTransportProfileUri(String value) throws UaException;

  /**
   * Read the value of the TransportProfileUri Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readTransportProfileUri() throws UaException;

  /**
   * Write a new value for the TransportProfileUri Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransportProfileUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransportProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readTransportProfileUriAsync();

  /**
   * An asynchronous implementation of {@link #writeTransportProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(String value);

  /**
   * Get the TransportProfileUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportProfileUri {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTransportProfileUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportProfileUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTransportProfileUriNodeAsync();

  /**
   * Get the local value of the HistoricalEventFilter Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HistoricalEventFilter Node.
   * @throws UaException if an error occurs creating or getting the HistoricalEventFilter Node.
   */
  EventFilter getHistoricalEventFilter() throws UaException;

  /**
   * Set the local value of the HistoricalEventFilter Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HistoricalEventFilter Node.
   * @throws UaException if an error occurs creating or getting the HistoricalEventFilter Node.
   */
  void setHistoricalEventFilter(EventFilter value) throws UaException;

  /**
   * Read the value of the HistoricalEventFilter Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link EventFilter} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EventFilter readHistoricalEventFilter() throws UaException;

  /**
   * Write a new value for the HistoricalEventFilter Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link EventFilter} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHistoricalEventFilter(EventFilter value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHistoricalEventFilter}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EventFilter> readHistoricalEventFilterAsync();

  /**
   * An asynchronous implementation of {@link #writeHistoricalEventFilter}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHistoricalEventFilterAsync(EventFilter value);

  /**
   * Get the HistoricalEventFilter {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HistoricalEventFilter {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHistoricalEventFilterNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHistoricalEventFilterNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHistoricalEventFilterNodeAsync();
}
