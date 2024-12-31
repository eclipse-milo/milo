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
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.4</a>
 */
public interface BrokerDataSetReaderTransportType extends DataSetReaderTransportType {
  QualifiedProperty<String> QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AUTHENTICATION_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AuthenticationProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<BrokerTransportQualityOfService> REQUESTED_DELIVERY_GUARANTEE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedDeliveryGuarantee",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15008"),
          -1,
          BrokerTransportQualityOfService.class);

  QualifiedProperty<String> META_DATA_QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataQueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the QueueName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the QueueName Node.
   * @throws UaException if an error occurs creating or getting the QueueName Node.
   */
  String getQueueName() throws UaException;

  /**
   * Set the local value of the QueueName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the QueueName Node.
   * @throws UaException if an error occurs creating or getting the QueueName Node.
   */
  void setQueueName(String value) throws UaException;

  /**
   * Read the value of the QueueName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readQueueName() throws UaException;

  /**
   * Write a new value for the QueueName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeQueueName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readQueueName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readQueueNameAsync();

  /**
   * An asynchronous implementation of {@link #writeQueueName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeQueueNameAsync(String value);

  /**
   * Get the QueueName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the QueueName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getQueueNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getQueueNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getQueueNameNodeAsync();

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
   * Get the local value of the AuthenticationProfileUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AuthenticationProfileUri Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationProfileUri Node.
   */
  String getAuthenticationProfileUri() throws UaException;

  /**
   * Set the local value of the AuthenticationProfileUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AuthenticationProfileUri Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationProfileUri Node.
   */
  void setAuthenticationProfileUri(String value) throws UaException;

  /**
   * Read the value of the AuthenticationProfileUri Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readAuthenticationProfileUri() throws UaException;

  /**
   * Write a new value for the AuthenticationProfileUri Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAuthenticationProfileUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAuthenticationProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readAuthenticationProfileUriAsync();

  /**
   * An asynchronous implementation of {@link #writeAuthenticationProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(String value);

  /**
   * Get the AuthenticationProfileUri {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AuthenticationProfileUri {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAuthenticationProfileUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAuthenticationProfileUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAuthenticationProfileUriNodeAsync();

  /**
   * Get the local value of the RequestedDeliveryGuarantee Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RequestedDeliveryGuarantee Node.
   * @throws UaException if an error occurs creating or getting the RequestedDeliveryGuarantee Node.
   */
  BrokerTransportQualityOfService getRequestedDeliveryGuarantee() throws UaException;

  /**
   * Set the local value of the RequestedDeliveryGuarantee Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RequestedDeliveryGuarantee Node.
   * @throws UaException if an error occurs creating or getting the RequestedDeliveryGuarantee Node.
   */
  void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService value) throws UaException;

  /**
   * Read the value of the RequestedDeliveryGuarantee Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link BrokerTransportQualityOfService} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  BrokerTransportQualityOfService readRequestedDeliveryGuarantee() throws UaException;

  /**
   * Write a new value for the RequestedDeliveryGuarantee Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link BrokerTransportQualityOfService} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRequestedDeliveryGuarantee(BrokerTransportQualityOfService value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRequestedDeliveryGuarantee}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends BrokerTransportQualityOfService>
      readRequestedDeliveryGuaranteeAsync();

  /**
   * An asynchronous implementation of {@link #writeRequestedDeliveryGuarantee}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRequestedDeliveryGuaranteeAsync(
      BrokerTransportQualityOfService value);

  /**
   * Get the RequestedDeliveryGuarantee {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RequestedDeliveryGuarantee {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRequestedDeliveryGuaranteeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRequestedDeliveryGuaranteeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRequestedDeliveryGuaranteeNodeAsync();

  /**
   * Get the local value of the MetaDataQueueName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MetaDataQueueName Node.
   * @throws UaException if an error occurs creating or getting the MetaDataQueueName Node.
   */
  String getMetaDataQueueName() throws UaException;

  /**
   * Set the local value of the MetaDataQueueName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MetaDataQueueName Node.
   * @throws UaException if an error occurs creating or getting the MetaDataQueueName Node.
   */
  void setMetaDataQueueName(String value) throws UaException;

  /**
   * Read the value of the MetaDataQueueName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMetaDataQueueName() throws UaException;

  /**
   * Write a new value for the MetaDataQueueName Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMetaDataQueueName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMetaDataQueueName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMetaDataQueueNameAsync();

  /**
   * An asynchronous implementation of {@link #writeMetaDataQueueName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMetaDataQueueNameAsync(String value);

  /**
   * Get the MetaDataQueueName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MetaDataQueueName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMetaDataQueueNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMetaDataQueueNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMetaDataQueueNameNodeAsync();
}
