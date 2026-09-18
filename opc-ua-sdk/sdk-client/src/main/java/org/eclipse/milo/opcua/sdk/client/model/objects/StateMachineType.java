package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the StateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">Model
 *     documentation</a>
 */
public interface StateMachineType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2299L);

  /**
   * Resolves the mandatory CurrentState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableType getCurrentStateNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentStateNode()}. */
  CompletableFuture<? extends StateVariableType> getCurrentStateNodeAsync();

  /**
   * Reads the Value of the CurrentState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readCurrentState() throws UaException;

  /**
   * Writes the Value of the CurrentState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readCurrentState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readCurrentStateAsync();

  /** Asynchronous form of {@link #writeCurrentState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional LastTransition child, a TransitionVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">TransitionVariableType
   *     documentation</a>
   */
  @Nullable TransitionVariableType getLastTransitionNode() throws UaException;

  /** Asynchronous form of {@link #getLastTransitionNode()}. */
  CompletableFuture<? extends @Nullable TransitionVariableType> getLastTransitionNodeAsync();

  /**
   * Reads the Value of the LastTransition child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readLastTransition() throws UaException;

  /**
   * Writes the Value of the LastTransition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastTransition(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readLastTransition()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readLastTransitionAsync();

  /** Asynchronous form of {@link #writeLastTransition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastTransitionAsync(@Nullable LocalizedText value);
}
