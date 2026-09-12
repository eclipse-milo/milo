/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BrokerDataSetWriterTransportType extends DataSetWriterTransportType {
  QualifiedProperty<String> QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> META_DATA_QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataQueueName",
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

  QualifiedProperty<Double> META_DATA_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable String getQueueName() throws UaException;

  /** Sets the existing node's local value. */
  void setQueueName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readQueueName() throws UaException;

  /** Writes the value remotely. */
  void writeQueueName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readQueueNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeQueueNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getQueueNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getQueueNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getMetaDataQueueName() throws UaException;

  /** Sets the existing node's local value. */
  void setMetaDataQueueName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMetaDataQueueName() throws UaException;

  /** Writes the value remotely. */
  void writeMetaDataQueueName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMetaDataQueueNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMetaDataQueueNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMetaDataQueueNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMetaDataQueueNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getResourceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readResourceUri() throws UaException;

  /** Writes the value remotely. */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResourceUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getAuthenticationProfileUri() throws UaException;

  /** Sets the existing node's local value. */
  void setAuthenticationProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readAuthenticationProfileUri() throws UaException;

  /** Writes the value remotely. */
  void writeAuthenticationProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readAuthenticationProfileUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAuthenticationProfileUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAuthenticationProfileUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable BrokerTransportQualityOfService getRequestedDeliveryGuarantee() throws UaException;

  /** Sets the existing node's local value. */
  void setRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable BrokerTransportQualityOfService readRequestedDeliveryGuarantee() throws UaException;

  /** Writes the value remotely. */
  void writeRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable BrokerTransportQualityOfService>
      readRequestedDeliveryGuaranteeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRequestedDeliveryGuaranteeAsync(
      @Nullable BrokerTransportQualityOfService value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedDeliveryGuaranteeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRequestedDeliveryGuaranteeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMetaDataUpdateTime() throws UaException;

  /** Sets the existing node's local value. */
  void setMetaDataUpdateTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMetaDataUpdateTime() throws UaException;

  /** Writes the value remotely. */
  void writeMetaDataUpdateTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMetaDataUpdateTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMetaDataUpdateTimeAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMetaDataUpdateTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMetaDataUpdateTimeNodeAsync();
}
