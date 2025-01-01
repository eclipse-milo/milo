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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1</a>
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

  /**
   * Get the local value of the ConfigurationVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  ConfigurationVersionDataType getConfigurationVersion() throws UaException;

  /**
   * Set the local value of the ConfigurationVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  void setConfigurationVersion(ConfigurationVersionDataType value) throws UaException;

  /**
   * Read the value of the ConfigurationVersion Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ConfigurationVersionDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ConfigurationVersionDataType readConfigurationVersion() throws UaException;

  /**
   * Write a new value for the ConfigurationVersion Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ConfigurationVersionDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfigurationVersion(ConfigurationVersionDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ConfigurationVersionDataType> readConfigurationVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(ConfigurationVersionDataType value);

  /**
   * Get the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfigurationVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConfigurationVersionNodeAsync();

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
   * Get the local value of the DataSetClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetClassId Node.
   * @throws UaException if an error occurs creating or getting the DataSetClassId Node.
   */
  UUID getDataSetClassId() throws UaException;

  /**
   * Set the local value of the DataSetClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetClassId Node.
   * @throws UaException if an error occurs creating or getting the DataSetClassId Node.
   */
  void setDataSetClassId(UUID value) throws UaException;

  /**
   * Read the value of the DataSetClassId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UUID} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UUID readDataSetClassId() throws UaException;

  /**
   * Write a new value for the DataSetClassId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UUID} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetClassId(UUID value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UUID> readDataSetClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(UUID value);

  /**
   * Get the DataSetClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetClassIdNodeAsync();

  /**
   * Get the local value of the CyclicDataSet Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CyclicDataSet Node.
   * @throws UaException if an error occurs creating or getting the CyclicDataSet Node.
   */
  Boolean getCyclicDataSet() throws UaException;

  /**
   * Set the local value of the CyclicDataSet Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CyclicDataSet Node.
   * @throws UaException if an error occurs creating or getting the CyclicDataSet Node.
   */
  void setCyclicDataSet(Boolean value) throws UaException;

  /**
   * Read the value of the CyclicDataSet Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readCyclicDataSet() throws UaException;

  /**
   * Write a new value for the CyclicDataSet Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCyclicDataSet(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCyclicDataSet}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readCyclicDataSetAsync();

  /**
   * An asynchronous implementation of {@link #writeCyclicDataSet}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCyclicDataSetAsync(Boolean value);

  /**
   * Get the CyclicDataSet {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CyclicDataSet {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCyclicDataSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCyclicDataSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCyclicDataSetNodeAsync();

  /**
   * Get the ExtensionFields {@link ExtensionFieldsType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExtensionFields {@link ExtensionFieldsType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ExtensionFieldsType getExtensionFieldsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExtensionFieldsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ExtensionFieldsType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ExtensionFieldsType> getExtensionFieldsNodeAsync();
}
