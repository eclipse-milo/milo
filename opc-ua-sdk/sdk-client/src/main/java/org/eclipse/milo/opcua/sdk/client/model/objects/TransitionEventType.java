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
 * Client API for the TransitionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">Model
 *     documentation</a>
 */
public interface TransitionEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2311L);

  /**
   * Resolves the mandatory Transition child, a TransitionVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">TransitionVariableType
   *     documentation</a>
   */
  TransitionVariableType getTransitionNode() throws UaException;

  /** Asynchronous form of {@link #getTransitionNode()}. */
  CompletableFuture<? extends TransitionVariableType> getTransitionNodeAsync();

  /**
   * Reads the Value of the Transition child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readTransition() throws UaException;

  /**
   * Writes the Value of the Transition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransition(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readTransition()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readTransitionAsync();

  /** Asynchronous form of {@link #writeTransition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTransitionAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory ToState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableType getToStateNode() throws UaException;

  /** Asynchronous form of {@link #getToStateNode()}. */
  CompletableFuture<? extends StateVariableType> getToStateNodeAsync();

  /**
   * Reads the Value of the ToState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readToState() throws UaException;

  /**
   * Writes the Value of the ToState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeToState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readToState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readToStateAsync();

  /** Asynchronous form of {@link #writeToState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeToStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory FromState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableType getFromStateNode() throws UaException;

  /** Asynchronous form of {@link #getFromStateNode()}. */
  CompletableFuture<? extends StateVariableType> getFromStateNodeAsync();

  /**
   * Reads the Value of the FromState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readFromState() throws UaException;

  /**
   * Writes the Value of the FromState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFromState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readFromState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readFromStateAsync();

  /** Asynchronous form of {@link #writeFromState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFromStateAsync(@Nullable LocalizedText value);
}
