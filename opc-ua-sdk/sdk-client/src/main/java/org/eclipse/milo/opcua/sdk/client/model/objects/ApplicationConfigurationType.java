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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12</a>
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<Boolean> ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Enabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

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
   * Get the local value of the ProductUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ProductUri Node.
   * @throws UaException if an error occurs creating or getting the ProductUri Node.
   */
  String getProductUri() throws UaException;

  /**
   * Set the local value of the ProductUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ProductUri Node.
   * @throws UaException if an error occurs creating or getting the ProductUri Node.
   */
  void setProductUri(String value) throws UaException;

  /**
   * Read the value of the ProductUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readProductUri() throws UaException;

  /**
   * Write a new value for the ProductUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeProductUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readProductUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readProductUriAsync();

  /**
   * An asynchronous implementation of {@link #writeProductUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeProductUriAsync(String value);

  /**
   * Get the ProductUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ProductUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getProductUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getProductUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getProductUriNodeAsync();

  /**
   * Get the local value of the ApplicationType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationType Node.
   * @throws UaException if an error occurs creating or getting the ApplicationType Node.
   */
  ApplicationType getApplicationType() throws UaException;

  /**
   * Set the local value of the ApplicationType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationType Node.
   * @throws UaException if an error occurs creating or getting the ApplicationType Node.
   */
  void setApplicationType(ApplicationType value) throws UaException;

  /**
   * Read the value of the ApplicationType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ApplicationType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ApplicationType readApplicationType() throws UaException;

  /**
   * Write a new value for the ApplicationType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ApplicationType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationType(ApplicationType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ApplicationType> readApplicationTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationTypeAsync(ApplicationType value);

  /**
   * Get the ApplicationType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationTypeNodeAsync();

  /**
   * Get the local value of the Enabled Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Enabled Node.
   * @throws UaException if an error occurs creating or getting the Enabled Node.
   */
  Boolean getEnabled() throws UaException;

  /**
   * Set the local value of the Enabled Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Enabled Node.
   * @throws UaException if an error occurs creating or getting the Enabled Node.
   */
  void setEnabled(Boolean value) throws UaException;

  /**
   * Read the value of the Enabled Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readEnabled() throws UaException;

  /**
   * Write a new value for the Enabled Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnabled(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readEnabledAsync();

  /**
   * An asynchronous implementation of {@link #writeEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnabledAsync(Boolean value);

  /**
   * Get the Enabled {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Enabled {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEnabledNodeAsync();
}
