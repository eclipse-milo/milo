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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishedDataSetType extends BaseObjectType {
  QualifiedProperty<ConfigurationVersionDataType> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593"),
          -1,
          ConfigurationVersionDataType.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<UUID> DATA_SET_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14"),
          -1,
          UUID.class);

  QualifiedProperty<Boolean> CYCLIC_DATA_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CyclicDataSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable ConfigurationVersionDataType getConfigurationVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setConfigurationVersion(@Nullable ConfigurationVersionDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ConfigurationVersionDataType readConfigurationVersion() throws UaException;

  /** Writes the value remotely. */
  void writeConfigurationVersion(@Nullable ConfigurationVersionDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ConfigurationVersionDataType>
      readConfigurationVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      @Nullable ConfigurationVersionDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConfigurationVersionNodeAsync();

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
  @Nullable UUID getDataSetClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetClassId(@Nullable UUID value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UUID readDataSetClassId() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetClassId(@Nullable UUID value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDataSetClassIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDataSetClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getCyclicDataSet() throws UaException;

  /** Sets the existing node's local value. */
  void setCyclicDataSet(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readCyclicDataSet() throws UaException;

  /** Writes the value remotely. */
  void writeCyclicDataSet(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readCyclicDataSetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCyclicDataSetAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCyclicDataSetNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCyclicDataSetNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ExtensionFieldsType getExtensionFieldsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ExtensionFieldsType> getExtensionFieldsNodeAsync();
}
