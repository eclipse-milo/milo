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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.4</a>
 */
public interface KeyCredentialConfigurationType extends BaseObjectType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String[]> ENDPOINT_URLS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointUrls",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<StatusCode> SERVICE_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  /**
   * Get the local value of the ResourceUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ResourceUri Node.
   * @throws UaException if an error occurs creating or getting the ResourceUri Node.
   */
  String getResourceUri() throws UaException;

  /**
   * Set the local value of the ResourceUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ResourceUri Node.
   * @throws UaException if an error occurs creating or getting the ResourceUri Node.
   */
  void setResourceUri(String value) throws UaException;

  /**
   * Read the value of the ResourceUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readResourceUri() throws UaException;

  /**
   * Write a new value for the ResourceUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeResourceUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readResourceUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readResourceUriAsync();

  /**
   * An asynchronous implementation of {@link #writeResourceUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeResourceUriAsync(String value);

  /**
   * Get the ResourceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ResourceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getResourceUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getResourceUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /**
   * Get the local value of the ProfileUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ProfileUri Node.
   * @throws UaException if an error occurs creating or getting the ProfileUri Node.
   */
  String getProfileUri() throws UaException;

  /**
   * Set the local value of the ProfileUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ProfileUri Node.
   * @throws UaException if an error occurs creating or getting the ProfileUri Node.
   */
  void setProfileUri(String value) throws UaException;

  /**
   * Read the value of the ProfileUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readProfileUri() throws UaException;

  /**
   * Write a new value for the ProfileUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeProfileUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readProfileUriAsync();

  /**
   * An asynchronous implementation of {@link #writeProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeProfileUriAsync(String value);

  /**
   * Get the ProfileUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ProfileUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getProfileUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getProfileUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getProfileUriNodeAsync();

  /**
   * Get the local value of the EndpointUrls Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EndpointUrls Node.
   * @throws UaException if an error occurs creating or getting the EndpointUrls Node.
   */
  String[] getEndpointUrls() throws UaException;

  /**
   * Set the local value of the EndpointUrls Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EndpointUrls Node.
   * @throws UaException if an error occurs creating or getting the EndpointUrls Node.
   */
  void setEndpointUrls(String[] value) throws UaException;

  /**
   * Read the value of the EndpointUrls Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readEndpointUrls() throws UaException;

  /**
   * Write a new value for the EndpointUrls Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEndpointUrls(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEndpointUrls}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readEndpointUrlsAsync();

  /**
   * An asynchronous implementation of {@link #writeEndpointUrls}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEndpointUrlsAsync(String[] value);

  /**
   * Get the EndpointUrls {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EndpointUrls {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEndpointUrlsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndpointUrlsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEndpointUrlsNodeAsync();

  /**
   * Get the local value of the ServiceStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServiceStatus Node.
   * @throws UaException if an error occurs creating or getting the ServiceStatus Node.
   */
  StatusCode getServiceStatus() throws UaException;

  /**
   * Set the local value of the ServiceStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServiceStatus Node.
   * @throws UaException if an error occurs creating or getting the ServiceStatus Node.
   */
  void setServiceStatus(StatusCode value) throws UaException;

  /**
   * Read the value of the ServiceStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link StatusCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusCode readServiceStatus() throws UaException;

  /**
   * Write a new value for the ServiceStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link StatusCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServiceStatus(StatusCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServiceStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusCode> readServiceStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeServiceStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServiceStatusAsync(StatusCode value);

  /**
   * Get the ServiceStatus {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServiceStatus {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServiceStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServiceStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServiceStatusNodeAsync();
}
