package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the OperationLimitsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">Model
 *     documentation</a>
 */
public interface OperationLimitsType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11564L);

  QualifiedProperty<UInteger> MaxNodesPerRead_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerRead",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerWrite_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerWrite",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerBrowse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerBrowse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerMethodCall_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerMethodCall",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxMonitoredItemsPerCall_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxMonitoredItemsPerCall",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerRegisterNodes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerRegisterNodes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerNodeManagement_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerNodeManagement",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerHistoryReadData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerHistoryReadData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerHistoryReadEvents_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerHistoryReadEvents",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerHistoryUpdateData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerHistoryUpdateData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerHistoryUpdateEvents_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerHistoryUpdateEvents",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNodesPerTranslateBrowsePathsToNodeIds_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNodesPerTranslateBrowsePathsToNodeIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional MaxNodesPerRead child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerReadNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerReadNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerReadNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerRead child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerRead() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerRead child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerRead(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerRead()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerReadAsync();

  /** Asynchronous form of {@link #writeMaxNodesPerRead}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxNodesPerReadAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerWrite child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerWriteNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerWriteNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerWriteNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerWrite child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerWrite() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerWrite child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerWrite(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerWrite()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerWriteAsync();

  /** Asynchronous form of {@link #writeMaxNodesPerWrite}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxNodesPerWriteAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerBrowse child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerBrowseNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerBrowseNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerBrowseNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerBrowse child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerBrowse() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerBrowse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerBrowse(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerBrowse()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerBrowseAsync();

  /** Asynchronous form of {@link #writeMaxNodesPerBrowse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxNodesPerBrowseAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerMethodCall child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerMethodCallNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerMethodCallNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerMethodCallNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerMethodCall child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerMethodCall() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerMethodCall child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerMethodCall()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerMethodCallAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerMethodCall}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerMethodCallAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxMonitoredItemsPerCall child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxMonitoredItemsPerCallNode() throws UaException;

  /** Asynchronous form of {@link #getMaxMonitoredItemsPerCallNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsPerCallNodeAsync();

  /**
   * Reads the Value of the MaxMonitoredItemsPerCall child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxMonitoredItemsPerCall() throws UaException;

  /**
   * Writes the Value of the MaxMonitoredItemsPerCall child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxMonitoredItemsPerCall()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerCallAsync();

  /**
   * Asynchronous form of {@link #writeMaxMonitoredItemsPerCall}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsPerCallAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerRegisterNodes child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerRegisterNodesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerRegisterNodesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerRegisterNodesNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerRegisterNodes child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerRegisterNodes() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerRegisterNodes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerRegisterNodes()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerRegisterNodesAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerRegisterNodes}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerRegisterNodesAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerNodeManagement child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerNodeManagementNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerNodeManagementNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerNodeManagementNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerNodeManagement child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerNodeManagement() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerNodeManagement child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerNodeManagement()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerNodeManagementAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerNodeManagement}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerNodeManagementAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerHistoryReadData child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadDataNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerHistoryReadDataNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryReadDataNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerHistoryReadData child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerHistoryReadData() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerHistoryReadData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerHistoryReadData()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadDataAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerHistoryReadData}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadDataAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerHistoryReadEvents child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadEventsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerHistoryReadEventsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryReadEventsNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerHistoryReadEvents child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerHistoryReadEvents() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerHistoryReadEvents child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerHistoryReadEvents()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadEventsAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerHistoryReadEvents}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadEventsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerHistoryUpdateData child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateDataNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerHistoryUpdateDataNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryUpdateDataNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerHistoryUpdateData child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerHistoryUpdateData() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerHistoryUpdateData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerHistoryUpdateData()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateDataAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerHistoryUpdateData}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateDataAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerHistoryUpdateEvents child, a PropertyType with DataType
   * UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateEventsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerHistoryUpdateEventsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryUpdateEventsNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerHistoryUpdateEvents child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerHistoryUpdateEvents() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerHistoryUpdateEvents child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerHistoryUpdateEvents()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateEventsAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerHistoryUpdateEvents}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateEventsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNodesPerTranslateBrowsePathsToNodeIds child, a PropertyType with
   * DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNodesPerTranslateBrowsePathsToNodeIdsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType>
      getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync();

  /**
   * Reads the Value of the MaxNodesPerTranslateBrowsePathsToNodeIds child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException;

  /**
   * Writes the Value of the MaxNodesPerTranslateBrowsePathsToNodeIds child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNodesPerTranslateBrowsePathsToNodeIds()}. */
  CompletableFuture<? extends @Nullable UInteger>
      readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync();

  /**
   * Asynchronous form of {@link #writeMaxNodesPerTranslateBrowsePathsToNodeIds}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(
      @Nullable UInteger value);
}
