package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the StandaloneSubscribedDataSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">Model
 *     documentation</a>
 */
public interface StandaloneSubscribedDataSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23828L);

  QualifiedProperty<Boolean> IsConnected_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IsConnected",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<DataSetMetaDataType> DataSetMetaData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMetaData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14523L),
          -1,
          DataSetMetaDataType.class);

  /**
   * Resolves the mandatory IsConnected child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIsConnectedNode() throws UaException;

  /** Asynchronous form of {@link #getIsConnectedNode()}. */
  CompletableFuture<? extends PropertyType> getIsConnectedNodeAsync();

  /**
   * Reads the Value of the IsConnected child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIsConnected() throws UaException;

  /**
   * Writes the Value of the IsConnected child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIsConnected(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIsConnected()}. */
  CompletableFuture<? extends @Nullable Boolean> readIsConnectedAsync();

  /** Asynchronous form of {@link #writeIsConnected}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIsConnectedAsync(@Nullable Boolean value);

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
   * Resolves the mandatory SubscribedDataSet child, a SubscribedDataSetType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.1">SubscribedDataSetType
   *     documentation</a>
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /** Asynchronous form of {@link #getSubscribedDataSetNode()}. */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();
}
