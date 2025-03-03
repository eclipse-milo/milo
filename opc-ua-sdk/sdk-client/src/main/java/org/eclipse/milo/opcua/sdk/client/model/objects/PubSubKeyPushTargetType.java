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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1</a>
 */
public interface PubSubKeyPushTargetType extends BaseObjectType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
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

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy> USER_TOKEN_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserTokenType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<UShort> REQUESTED_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> RETRY_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RetryInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<DateTime> LAST_PUSH_EXECUTION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastPushExecutionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> LAST_PUSH_ERROR_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastPushErrorTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /**
   * Get the local value of the ApplicationUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationUri Node.
   * @throws UaException if an error occurs creating or getting the ApplicationUri Node.
   */
  String getApplicationUri() throws UaException;

  /**
   * Set the local value of the ApplicationUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationUri Node.
   * @throws UaException if an error occurs creating or getting the ApplicationUri Node.
   */
  void setApplicationUri(String value) throws UaException;

  /**
   * Read the value of the ApplicationUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readApplicationUri() throws UaException;

  /**
   * Write a new value for the ApplicationUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readApplicationUriAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationUriAsync(String value);

  /**
   * Get the ApplicationUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

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
   * Get the local value of the UserTokenType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UserTokenType Node.
   * @throws UaException if an error occurs creating or getting the UserTokenType Node.
   */
  UserTokenPolicy getUserTokenType() throws UaException;

  /**
   * Set the local value of the UserTokenType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UserTokenType Node.
   * @throws UaException if an error occurs creating or getting the UserTokenType Node.
   */
  void setUserTokenType(UserTokenPolicy value) throws UaException;

  /**
   * Read the value of the UserTokenType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UserTokenPolicy} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UserTokenPolicy readUserTokenType() throws UaException;

  /**
   * Write a new value for the UserTokenType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UserTokenPolicy} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUserTokenType(UserTokenPolicy value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUserTokenType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UserTokenPolicy> readUserTokenTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeUserTokenType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUserTokenTypeAsync(UserTokenPolicy value);

  /**
   * Get the UserTokenType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UserTokenType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUserTokenTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUserTokenTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUserTokenTypeNodeAsync();

  /**
   * Get the local value of the RequestedKeyCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RequestedKeyCount Node.
   * @throws UaException if an error occurs creating or getting the RequestedKeyCount Node.
   */
  UShort getRequestedKeyCount() throws UaException;

  /**
   * Set the local value of the RequestedKeyCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RequestedKeyCount Node.
   * @throws UaException if an error occurs creating or getting the RequestedKeyCount Node.
   */
  void setRequestedKeyCount(UShort value) throws UaException;

  /**
   * Read the value of the RequestedKeyCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readRequestedKeyCount() throws UaException;

  /**
   * Write a new value for the RequestedKeyCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRequestedKeyCount(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRequestedKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readRequestedKeyCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRequestedKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRequestedKeyCountAsync(UShort value);

  /**
   * Get the RequestedKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RequestedKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRequestedKeyCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRequestedKeyCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRequestedKeyCountNodeAsync();

  /**
   * Get the local value of the RetryInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RetryInterval Node.
   * @throws UaException if an error occurs creating or getting the RetryInterval Node.
   */
  Double getRetryInterval() throws UaException;

  /**
   * Set the local value of the RetryInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RetryInterval Node.
   * @throws UaException if an error occurs creating or getting the RetryInterval Node.
   */
  void setRetryInterval(Double value) throws UaException;

  /**
   * Read the value of the RetryInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readRetryInterval() throws UaException;

  /**
   * Write a new value for the RetryInterval Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRetryInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRetryInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readRetryIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writeRetryInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRetryIntervalAsync(Double value);

  /**
   * Get the RetryInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RetryInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRetryIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRetryIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRetryIntervalNodeAsync();

  /**
   * Get the local value of the LastPushExecutionTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastPushExecutionTime Node.
   * @throws UaException if an error occurs creating or getting the LastPushExecutionTime Node.
   */
  DateTime getLastPushExecutionTime() throws UaException;

  /**
   * Set the local value of the LastPushExecutionTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastPushExecutionTime Node.
   * @throws UaException if an error occurs creating or getting the LastPushExecutionTime Node.
   */
  void setLastPushExecutionTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastPushExecutionTime Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastPushExecutionTime() throws UaException;

  /**
   * Write a new value for the LastPushExecutionTime Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastPushExecutionTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastPushExecutionTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastPushExecutionTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastPushExecutionTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastPushExecutionTimeAsync(DateTime value);

  /**
   * Get the LastPushExecutionTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastPushExecutionTime {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastPushExecutionTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastPushExecutionTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastPushExecutionTimeNodeAsync();

  /**
   * Get the local value of the LastPushErrorTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastPushErrorTime Node.
   * @throws UaException if an error occurs creating or getting the LastPushErrorTime Node.
   */
  DateTime getLastPushErrorTime() throws UaException;

  /**
   * Set the local value of the LastPushErrorTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastPushErrorTime Node.
   * @throws UaException if an error occurs creating or getting the LastPushErrorTime Node.
   */
  void setLastPushErrorTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastPushErrorTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastPushErrorTime() throws UaException;

  /**
   * Write a new value for the LastPushErrorTime Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastPushErrorTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastPushErrorTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastPushErrorTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastPushErrorTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastPushErrorTimeAsync(DateTime value);

  /**
   * Get the LastPushErrorTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastPushErrorTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastPushErrorTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastPushErrorTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastPushErrorTimeNodeAsync();
}
