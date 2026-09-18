package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ReaderGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">Model
 *     documentation</a>
 */
public interface ReaderGroupType extends PubSubGroupType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17999L);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsReaderGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.10">PubSubDiagnosticsReaderGroupType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsReaderGroupType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsReaderGroupType> getDiagnosticsNodeAsync();

  /**
   * Resolves the optional MessageSettings child, a ReaderGroupMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.14">ReaderGroupMessageType
   *     documentation</a>
   */
  @Nullable ReaderGroupMessageType getMessageSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getMessageSettingsNode()}. */
  CompletableFuture<? extends @Nullable ReaderGroupMessageType> getMessageSettingsNodeAsync();

  /**
   * Resolves the optional TransportSettings child, a ReaderGroupTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.13">ReaderGroupTransportType
   *     documentation</a>
   */
  @Nullable ReaderGroupTransportType getTransportSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getTransportSettingsNode()}. */
  CompletableFuture<? extends @Nullable ReaderGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Resolves the optional AddDataSetReader Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetReaderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddDataSetReaderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddDataSetReaderMethodNodeAsync();

  /**
   * Calls the AddDataSetReader Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10">Model
   *     documentation</a>
   */
  @Nullable NodeId addDataSetReader(@Nullable DataSetReaderDataType configuration)
      throws UaException;

  /**
   * Calls the AddDataSetReader Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetReader(
      @Nullable DataSetReaderDataType configuration) throws UaException;

  /**
   * Calls the AddDataSetReader Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetReaderWith(
      MethodCallOptions options, @Nullable DataSetReaderDataType configuration) throws UaException;

  /** Asynchronous form of {@link #addDataSetReader}. */
  CompletableFuture<@Nullable NodeId> addDataSetReaderAsync(
      @Nullable DataSetReaderDataType configuration);

  /** Asynchronous form of {@link #callAddDataSetReader}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetReaderAsync(
      @Nullable DataSetReaderDataType configuration);

  /** Asynchronous form of {@link #callAddDataSetReaderWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetReaderWithAsync(
      MethodCallOptions options, @Nullable DataSetReaderDataType configuration);

  /**
   * Resolves the optional RemoveDataSetReader Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetReaderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveDataSetReaderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetReaderMethodNodeAsync();

  /**
   * Calls the RemoveDataSetReader Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11">Model
   *     documentation</a>
   */
  void removeDataSetReader(@Nullable NodeId dataSetReaderNodeId) throws UaException;

  /**
   * Calls the RemoveDataSetReader Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetReader(@Nullable NodeId dataSetReaderNodeId)
      throws UaException;

  /**
   * Calls the RemoveDataSetReader Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetReaderWith(
      MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId) throws UaException;

  /** Asynchronous form of {@link #removeDataSetReader}. */
  CompletableFuture<Void> removeDataSetReaderAsync(@Nullable NodeId dataSetReaderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetReader}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetReaderAsync(
      @Nullable NodeId dataSetReaderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetReaderWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetReaderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId);
}
