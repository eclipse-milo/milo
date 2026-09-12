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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable DataSetMetaDataType getDataSetMetaData() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(@Nullable DataSetMetaDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsConnected() throws UaException;

  /** Sets the existing node's local value. */
  void setIsConnected(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIsConnected() throws UaException;

  /** Writes the value remotely. */
  void writeIsConnected(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIsConnectedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIsConnectedAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsConnectedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIsConnectedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();
}
