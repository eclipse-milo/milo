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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PublishedDataSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">Model
 *     documentation</a>
 */
public interface PublishedDataSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14509L);

  QualifiedProperty<Boolean> CyclicDataSet_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CyclicDataSet",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UUID> DataSetClassId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetClassId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14L),
          -1,
          UUID.class);

  QualifiedProperty<DataSetMetaDataType> DataSetMetaData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMetaData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14523L),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<ConfigurationVersionDataType> ConfigurationVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14593L),
          -1,
          ConfigurationVersionDataType.class);

  /**
   * Resolves the optional CyclicDataSet child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCyclicDataSetNode() throws UaException;

  /** Asynchronous form of {@link #getCyclicDataSetNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCyclicDataSetNodeAsync();

  /**
   * Reads the Value of the CyclicDataSet child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readCyclicDataSet() throws UaException;

  /**
   * Writes the Value of the CyclicDataSet child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCyclicDataSet(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readCyclicDataSet()}. */
  CompletableFuture<? extends @Nullable Boolean> readCyclicDataSetAsync();

  /** Asynchronous form of {@link #writeCyclicDataSet}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCyclicDataSetAsync(@Nullable Boolean value);

  /**
   * Resolves the optional DataSetClassId child, a PropertyType with DataType Guid.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDataSetClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetClassIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDataSetClassIdNodeAsync();

  /**
   * Reads the Value of the DataSetClassId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UUID readDataSetClassId() throws UaException;

  /**
   * Writes the Value of the DataSetClassId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetClassId(@Nullable UUID value) throws UaException;

  /** Asynchronous form of {@link #readDataSetClassId()}. */
  CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync();

  /** Asynchronous form of {@link #writeDataSetClassId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value);

  /**
   * Resolves the mandatory DataSetMetaData child, a PropertyType with DataType DataSetMetaDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetMetaDataNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /**
   * Reads the Value of the DataSetMetaData child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException;

  /**
   * Writes the Value of the DataSetMetaData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Asynchronous form of {@link #readDataSetMetaData()}. */
  CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync();

  /** Asynchronous form of {@link #writeDataSetMetaData}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(@Nullable DataSetMetaDataType value);

  /**
   * Resolves the optional ExtensionFields child, a ExtensionFieldsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">ExtensionFieldsType
   *     documentation</a>
   */
  @Nullable ExtensionFieldsType getExtensionFieldsNode() throws UaException;

  /** Asynchronous form of {@link #getExtensionFieldsNode()}. */
  CompletableFuture<? extends @Nullable ExtensionFieldsType> getExtensionFieldsNodeAsync();

  /**
   * Resolves the mandatory ConfigurationVersion child, a PropertyType with DataType
   * ConfigurationVersionDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConfigurationVersionNode() throws UaException;

  /** Asynchronous form of {@link #getConfigurationVersionNode()}. */
  CompletableFuture<? extends PropertyType> getConfigurationVersionNodeAsync();

  /**
   * Reads the Value of the ConfigurationVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ConfigurationVersionDataType readConfigurationVersion() throws UaException;

  /**
   * Writes the Value of the ConfigurationVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfigurationVersion(@Nullable ConfigurationVersionDataType value) throws UaException;

  /** Asynchronous form of {@link #readConfigurationVersion()}. */
  CompletableFuture<? extends @Nullable ConfigurationVersionDataType>
      readConfigurationVersionAsync();

  /**
   * Asynchronous form of {@link #writeConfigurationVersion}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      @Nullable ConfigurationVersionDataType value);
}
