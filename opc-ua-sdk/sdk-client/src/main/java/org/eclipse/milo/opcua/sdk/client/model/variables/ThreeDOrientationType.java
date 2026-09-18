package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the 3DOrientationType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">Model
 *     documentation</a>
 */
public interface ThreeDOrientationType extends OrientationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18781L);

  /**
   * Resolves the mandatory A child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getANode() throws UaException;

  /** Asynchronous form of {@link #getANode()}. */
  CompletableFuture<? extends VariableNode> getANodeAsync();

  /**
   * Reads the Value of the A child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readA() throws UaException;

  /**
   * Writes the Value of the A child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeA(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readA()}. */
  CompletableFuture<? extends @Nullable Double> readAAsync();

  /** Asynchronous form of {@link #writeA}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAAsync(@Nullable Double value);

  /**
   * Resolves the mandatory B child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getBNode() throws UaException;

  /** Asynchronous form of {@link #getBNode()}. */
  CompletableFuture<? extends VariableNode> getBNodeAsync();

  /**
   * Reads the Value of the B child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readB() throws UaException;

  /**
   * Writes the Value of the B child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeB(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readB()}. */
  CompletableFuture<? extends @Nullable Double> readBAsync();

  /** Asynchronous form of {@link #writeB}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBAsync(@Nullable Double value);

  /**
   * Resolves the mandatory C child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCNode() throws UaException;

  /** Asynchronous form of {@link #getCNode()}. */
  CompletableFuture<? extends VariableNode> getCNodeAsync();

  /**
   * Reads the Value of the C child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readC() throws UaException;

  /**
   * Writes the Value of the C child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeC(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readC()}. */
  CompletableFuture<? extends @Nullable Double> readCAsync();

  /** Asynchronous form of {@link #writeC}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCAsync(@Nullable Double value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ThreeDOrientation readThreeDOrientationValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeThreeDOrientationValue(@Nullable ThreeDOrientation value) throws UaException;

  /** Asynchronous form of {@link #readThreeDOrientationValue()}. */
  CompletableFuture<? extends @Nullable ThreeDOrientation> readThreeDOrientationValueAsync();

  /**
   * Asynchronous form of {@link #writeThreeDOrientationValue}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeThreeDOrientationValueAsync(@Nullable ThreeDOrientation value);
}
