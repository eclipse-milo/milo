/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1</a>
 */
public interface SecurityGroupType extends BaseObjectType {
  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> KEY_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_FUTURE_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFutureKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PAST_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPastKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /**
   * Get the local value of the SecurityGroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  String getSecurityGroupId() throws UaException;

  /**
   * Set the local value of the SecurityGroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  void setSecurityGroupId(String value) throws UaException;

  /**
   * Read the value of the SecurityGroupId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecurityGroupId() throws UaException;

  /**
   * Write a new value for the SecurityGroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityGroupId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecurityGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(String value);

  /**
   * Get the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Get the local value of the KeyLifetime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the KeyLifetime Node.
   * @throws UaException if an error occurs creating or getting the KeyLifetime Node.
   */
  Double getKeyLifetime() throws UaException;

  /**
   * Set the local value of the KeyLifetime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the KeyLifetime Node.
   * @throws UaException if an error occurs creating or getting the KeyLifetime Node.
   */
  void setKeyLifetime(Double value) throws UaException;

  /**
   * Read the value of the KeyLifetime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readKeyLifetime() throws UaException;

  /**
   * Write a new value for the KeyLifetime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeKeyLifetime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readKeyLifetime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readKeyLifetimeAsync();

  /**
   * An asynchronous implementation of {@link #writeKeyLifetime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeKeyLifetimeAsync(Double value);

  /**
   * Get the KeyLifetime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the KeyLifetime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getKeyLifetimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getKeyLifetimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getKeyLifetimeNodeAsync();

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
   * Get the local value of the MaxFutureKeyCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxFutureKeyCount Node.
   * @throws UaException if an error occurs creating or getting the MaxFutureKeyCount Node.
   */
  UInteger getMaxFutureKeyCount() throws UaException;

  /**
   * Set the local value of the MaxFutureKeyCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxFutureKeyCount Node.
   * @throws UaException if an error occurs creating or getting the MaxFutureKeyCount Node.
   */
  void setMaxFutureKeyCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxFutureKeyCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxFutureKeyCount() throws UaException;

  /**
   * Write a new value for the MaxFutureKeyCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxFutureKeyCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxFutureKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxFutureKeyCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxFutureKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxFutureKeyCountAsync(UInteger value);

  /**
   * Get the MaxFutureKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxFutureKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxFutureKeyCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxFutureKeyCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxFutureKeyCountNodeAsync();

  /**
   * Get the local value of the MaxPastKeyCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxPastKeyCount Node.
   * @throws UaException if an error occurs creating or getting the MaxPastKeyCount Node.
   */
  UInteger getMaxPastKeyCount() throws UaException;

  /**
   * Set the local value of the MaxPastKeyCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxPastKeyCount Node.
   * @throws UaException if an error occurs creating or getting the MaxPastKeyCount Node.
   */
  void setMaxPastKeyCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxPastKeyCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxPastKeyCount() throws UaException;

  /**
   * Write a new value for the MaxPastKeyCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxPastKeyCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxPastKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxPastKeyCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxPastKeyCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxPastKeyCountAsync(UInteger value);

  /**
   * Get the MaxPastKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxPastKeyCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxPastKeyCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxPastKeyCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxPastKeyCountNodeAsync();
}
