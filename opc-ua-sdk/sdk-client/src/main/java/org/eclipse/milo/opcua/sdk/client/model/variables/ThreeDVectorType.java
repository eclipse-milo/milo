package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the 3DVectorType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.22">Model
 *     documentation</a>
 */
public interface ThreeDVectorType extends VectorType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17716L);

  /**
   * Resolves the mandatory X child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getXNode() throws UaException;

  /** Asynchronous form of {@link #getXNode()}. */
  CompletableFuture<? extends VariableNode> getXNodeAsync();

  /**
   * Reads the Value of the X child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readX() throws UaException;

  /**
   * Writes the Value of the X child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeX(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readX()}. */
  CompletableFuture<? extends @Nullable Double> readXAsync();

  /** Asynchronous form of {@link #writeX}; completes with the operation status. */
  CompletableFuture<StatusCode> writeXAsync(@Nullable Double value);

  /**
   * Resolves the mandatory Y child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getYNode() throws UaException;

  /** Asynchronous form of {@link #getYNode()}. */
  CompletableFuture<? extends VariableNode> getYNodeAsync();

  /**
   * Reads the Value of the Y child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readY() throws UaException;

  /**
   * Writes the Value of the Y child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeY(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readY()}. */
  CompletableFuture<? extends @Nullable Double> readYAsync();

  /** Asynchronous form of {@link #writeY}; completes with the operation status. */
  CompletableFuture<StatusCode> writeYAsync(@Nullable Double value);

  /**
   * Resolves the mandatory Z child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getZNode() throws UaException;

  /** Asynchronous form of {@link #getZNode()}. */
  CompletableFuture<? extends VariableNode> getZNodeAsync();

  /**
   * Reads the Value of the Z child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readZ() throws UaException;

  /**
   * Writes the Value of the Z child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeZ(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readZ()}. */
  CompletableFuture<? extends @Nullable Double> readZAsync();

  /** Asynchronous form of {@link #writeZ}; completes with the operation status. */
  CompletableFuture<StatusCode> writeZAsync(@Nullable Double value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ThreeDVector readThreeDVectorValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeThreeDVectorValue(@Nullable ThreeDVector value) throws UaException;

  /** Asynchronous form of {@link #readThreeDVectorValue()}. */
  CompletableFuture<? extends @Nullable ThreeDVector> readThreeDVectorValueAsync();

  /** Asynchronous form of {@link #writeThreeDVectorValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeThreeDVectorValueAsync(@Nullable ThreeDVector value);
}
