package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ISrClassType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6">Model
 *     documentation</a>
 */
public interface ISrClassType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24169L);

  /**
   * Resolves the mandatory Id child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getIdNode() throws UaException;

  /** Asynchronous form of {@link #getIdNode()}. */
  CompletableFuture<? extends VariableNode> getIdNodeAsync();

  /**
   * Reads the Value of the Id child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readId() throws UaException;

  /**
   * Writes the Value of the Id child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeId(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readId()}. */
  CompletableFuture<? extends @Nullable UByte> readIdAsync();

  /** Asynchronous form of {@link #writeId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIdAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory Vid child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getVidNode() throws UaException;

  /** Asynchronous form of {@link #getVidNode()}. */
  CompletableFuture<? extends VariableNode> getVidNodeAsync();

  /**
   * Reads the Value of the Vid child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readVid() throws UaException;

  /**
   * Writes the Value of the Vid child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeVid(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readVid()}. */
  CompletableFuture<? extends @Nullable UShort> readVidAsync();

  /** Asynchronous form of {@link #writeVid}; completes with the operation status. */
  CompletableFuture<StatusCode> writeVidAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory Priority child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPriorityNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityNode()}. */
  CompletableFuture<? extends VariableNode> getPriorityNodeAsync();

  /**
   * Reads the Value of the Priority child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPriority() throws UaException;

  /**
   * Writes the Value of the Priority child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriority(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPriority()}. */
  CompletableFuture<? extends @Nullable UByte> readPriorityAsync();

  /** Asynchronous form of {@link #writePriority}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value);
}
