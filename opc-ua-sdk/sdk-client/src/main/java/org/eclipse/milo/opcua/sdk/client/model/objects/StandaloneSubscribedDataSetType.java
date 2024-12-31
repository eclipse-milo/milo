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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5</a>
 */
public interface StandaloneSubscribedDataSetType extends BaseObjectType {
  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<Boolean> IS_CONNECTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsConnected",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the DataSetMetaData Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetMetaData Node.
   * @throws UaException if an error occurs creating or getting the DataSetMetaData Node.
   */
  DataSetMetaDataType getDataSetMetaData() throws UaException;

  /**
   * Set the local value of the DataSetMetaData Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetMetaData Node.
   * @throws UaException if an error occurs creating or getting the DataSetMetaData Node.
   */
  void setDataSetMetaData(DataSetMetaDataType value) throws UaException;

  /**
   * Read the value of the DataSetMetaData Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DataSetMetaDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DataSetMetaDataType readDataSetMetaData() throws UaException;

  /**
   * Write a new value for the DataSetMetaData Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DataSetMetaDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetMetaData(DataSetMetaDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetMetaData}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DataSetMetaDataType> readDataSetMetaDataAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetMetaData}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(DataSetMetaDataType value);

  /**
   * Get the DataSetMetaData {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetMetaData {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetMetaDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /**
   * Get the local value of the IsConnected Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IsConnected Node.
   * @throws UaException if an error occurs creating or getting the IsConnected Node.
   */
  Boolean getIsConnected() throws UaException;

  /**
   * Set the local value of the IsConnected Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IsConnected Node.
   * @throws UaException if an error occurs creating or getting the IsConnected Node.
   */
  void setIsConnected(Boolean value) throws UaException;

  /**
   * Read the value of the IsConnected Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readIsConnected() throws UaException;

  /**
   * Write a new value for the IsConnected Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIsConnected(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIsConnected}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readIsConnectedAsync();

  /**
   * An asynchronous implementation of {@link #writeIsConnected}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIsConnectedAsync(Boolean value);

  /**
   * Get the IsConnected {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IsConnected {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIsConnectedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIsConnectedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIsConnectedNodeAsync();

  /**
   * Get the SubscribedDataSet {@link SubscribedDataSetType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscribedDataSet {@link SubscribedDataSetType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscribedDataSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SubscribedDataSetType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();
}
