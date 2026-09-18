package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the FileTransferStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6">Model
 *     documentation</a>
 */
public interface FileTransferStateMachineType extends FiniteStateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15803L);

  /**
   * Resolves the mandatory Reset Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/1">Model
   *     documentation</a>
   */
  UaMethodNode getResetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResetMethodNode()}. */
  CompletableFuture<UaMethodNode> getResetMethodNodeAsync();

  /**
   * Calls the Reset Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/1">Model
   *     documentation</a>
   */
  void reset() throws UaException;

  /**
   * Calls the Reset Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset() throws UaException;

  /**
   * Calls the Reset Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #reset}. */
  CompletableFuture<Void> resetAsync();

  /** Asynchronous form of {@link #callReset}. */
  CompletableFuture<MethodCallResult<Void>> callResetAsync();

  /** Asynchronous form of {@link #callResetWith}. */
  CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options);
}
