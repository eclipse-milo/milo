package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the FiniteStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">Model
 *     documentation</a>
 */
public interface FiniteStateMachineType extends StateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2771L);

  /**
   * Resolves the mandatory CurrentState child, a FiniteStateVariableType with DataType
   * LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.6">FiniteStateVariableType
   *     documentation</a>
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentStateNode()}. */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /**
   * Resolves the optional LastTransition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  @Nullable FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /** Asynchronous form of {@link #getLastTransitionNode()}. */
  CompletableFuture<? extends @Nullable FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /**
   * Resolves the optional AvailableStates child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getAvailableStatesNode() throws UaException;

  /** Asynchronous form of {@link #getAvailableStatesNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getAvailableStatesNodeAsync();

  /**
   * Reads the Value of the AvailableStates child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readAvailableStates() throws UaException;

  /**
   * Writes the Value of the AvailableStates child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAvailableStates(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAvailableStates()}. */
  CompletableFuture<? extends NodeId @Nullable []> readAvailableStatesAsync();

  /** Asynchronous form of {@link #writeAvailableStates}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAvailableStatesAsync(NodeId @Nullable [] value);

  /**
   * Resolves the optional AvailableTransitions child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getAvailableTransitionsNode() throws UaException;

  /** Asynchronous form of {@link #getAvailableTransitionsNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getAvailableTransitionsNodeAsync();

  /**
   * Reads the Value of the AvailableTransitions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readAvailableTransitions() throws UaException;

  /**
   * Writes the Value of the AvailableTransitions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAvailableTransitions(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAvailableTransitions()}. */
  CompletableFuture<? extends NodeId @Nullable []> readAvailableTransitionsAsync();

  /**
   * Asynchronous form of {@link #writeAvailableTransitions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeAvailableTransitionsAsync(NodeId @Nullable [] value);
}
