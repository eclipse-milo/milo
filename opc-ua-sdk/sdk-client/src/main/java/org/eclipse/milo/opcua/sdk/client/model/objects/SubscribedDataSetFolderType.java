package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SubscribedDataSetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">Model
 *     documentation</a>
 */
public interface SubscribedDataSetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23795L);

  /**
   * Resolves the optional AddDataSetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddDataSetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddDataSetFolderMethodNodeAsync();

  /**
   * Calls the AddDataSetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addDataSetFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddDataSetFolder Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddDataSetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /** Asynchronous form of {@link #addDataSetFolder}. */
  CompletableFuture<@Nullable NodeId> addDataSetFolderAsync(@Nullable String name);

  /** Asynchronous form of {@link #callAddDataSetFolder}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderAsync(
      @Nullable String name);

  /** Asynchronous form of {@link #callAddDataSetFolderWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable String name);

  /**
   * Resolves the optional AddSubscribedDataSet Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddSubscribedDataSetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddSubscribedDataSetMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddSubscribedDataSetMethodNodeAsync();

  /**
   * Calls the AddSubscribedDataSet Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2">Model
   *     documentation</a>
   */
  @Nullable NodeId addSubscribedDataSet(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) throws UaException;

  /**
   * Calls the AddSubscribedDataSet Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddSubscribedDataSet(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) throws UaException;

  /**
   * Calls the AddSubscribedDataSet Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddSubscribedDataSetWith(
      MethodCallOptions options, @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet)
      throws UaException;

  /** Asynchronous form of {@link #addSubscribedDataSet}. */
  CompletableFuture<@Nullable NodeId> addSubscribedDataSetAsync(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet);

  /** Asynchronous form of {@link #callAddSubscribedDataSet}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSubscribedDataSetAsync(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet);

  /** Asynchronous form of {@link #callAddSubscribedDataSetWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSubscribedDataSetWithAsync(
      MethodCallOptions options, @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet);

  /**
   * Resolves the optional RemoveDataSetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveDataSetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetFolderMethodNodeAsync();

  /**
   * Calls the RemoveDataSetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5">Model
   *     documentation</a>
   */
  void removeDataSetFolder(@Nullable NodeId dataSetFolderNodeId) throws UaException;

  /**
   * Calls the RemoveDataSetFolder Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetFolder(@Nullable NodeId dataSetFolderNodeId)
      throws UaException;

  /**
   * Calls the RemoveDataSetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetFolderWith(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) throws UaException;

  /** Asynchronous form of {@link #removeDataSetFolder}. */
  CompletableFuture<Void> removeDataSetFolderAsync(@Nullable NodeId dataSetFolderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetFolder}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderAsync(
      @Nullable NodeId dataSetFolderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetFolderWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId);

  /**
   * Resolves the optional RemoveSubscribedDataSet Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveSubscribedDataSetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveSubscribedDataSetMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveSubscribedDataSetMethodNodeAsync();

  /**
   * Calls the RemoveSubscribedDataSet Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3">Model
   *     documentation</a>
   */
  void removeSubscribedDataSet(@Nullable NodeId subscribedDataSetNodeId) throws UaException;

  /**
   * Calls the RemoveSubscribedDataSet Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSubscribedDataSet(@Nullable NodeId subscribedDataSetNodeId)
      throws UaException;

  /**
   * Calls the RemoveSubscribedDataSet Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSubscribedDataSetWith(
      MethodCallOptions options, @Nullable NodeId subscribedDataSetNodeId) throws UaException;

  /** Asynchronous form of {@link #removeSubscribedDataSet}. */
  CompletableFuture<Void> removeSubscribedDataSetAsync(@Nullable NodeId subscribedDataSetNodeId);

  /** Asynchronous form of {@link #callRemoveSubscribedDataSet}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSubscribedDataSetAsync(
      @Nullable NodeId subscribedDataSetNodeId);

  /** Asynchronous form of {@link #callRemoveSubscribedDataSetWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSubscribedDataSetWithAsync(
      MethodCallOptions options, @Nullable NodeId subscribedDataSetNodeId);
}
