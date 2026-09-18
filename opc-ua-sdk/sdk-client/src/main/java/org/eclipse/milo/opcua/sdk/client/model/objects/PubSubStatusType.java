package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubStatusType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">Model
 *     documentation</a>
 */
public interface PubSubStatusType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14643L);

  /**
   * Resolves the mandatory State child, a BaseDataVariableType with DataType PubSubState.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStateNode() throws UaException;

  /** Asynchronous form of {@link #getStateNode()}. */
  CompletableFuture<? extends VariableNode> getStateNodeAsync();

  /**
   * Reads the Value of the State child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PubSubState readState() throws UaException;

  /**
   * Writes the Value of the State child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeState(@Nullable PubSubState value) throws UaException;

  /** Asynchronous form of {@link #readState()}. */
  CompletableFuture<? extends @Nullable PubSubState> readStateAsync();

  /** Asynchronous form of {@link #writeState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable PubSubState value);

  /**
   * Resolves the optional Disable Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDisableMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDisableMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getDisableMethodNodeAsync();

  /**
   * Calls the Disable Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3">Model
   *     documentation</a>
   */
  void disable() throws UaException;

  /**
   * Calls the Disable Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDisable() throws UaException;

  /**
   * Calls the Disable Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDisableWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #disable}. */
  CompletableFuture<Void> disableAsync();

  /** Asynchronous form of {@link #callDisable}. */
  CompletableFuture<MethodCallResult<Void>> callDisableAsync();

  /** Asynchronous form of {@link #callDisableWith}. */
  CompletableFuture<MethodCallResult<Void>> callDisableWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Enable Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getEnableMethodNode() throws UaException;

  /** Asynchronous form of {@link #getEnableMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getEnableMethodNodeAsync();

  /**
   * Calls the Enable Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2">Model
   *     documentation</a>
   */
  void enable() throws UaException;

  /**
   * Calls the Enable Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callEnable() throws UaException;

  /**
   * Calls the Enable Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callEnableWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #enable}. */
  CompletableFuture<Void> enableAsync();

  /** Asynchronous form of {@link #callEnable}. */
  CompletableFuture<MethodCallResult<Void>> callEnableAsync();

  /** Asynchronous form of {@link #callEnableWith}. */
  CompletableFuture<MethodCallResult<Void>> callEnableWithAsync(MethodCallOptions options);
}
