package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeBaseTsnStatusStreamType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24183L);

  /**
   * Resolves the mandatory FailureCode child, a BaseDataVariableType with DataType TsnFailureCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getFailureCodeNode() throws UaException;

  /** Asynchronous form of {@link #getFailureCodeNode()}. */
  CompletableFuture<? extends VariableNode> getFailureCodeNodeAsync();

  /**
   * Reads the Value of the FailureCode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TsnFailureCode readFailureCode() throws UaException;

  /**
   * Writes the Value of the FailureCode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFailureCode(@Nullable TsnFailureCode value) throws UaException;

  /** Asynchronous form of {@link #readFailureCode()}. */
  CompletableFuture<? extends @Nullable TsnFailureCode> readFailureCodeAsync();

  /** Asynchronous form of {@link #writeFailureCode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFailureCodeAsync(@Nullable TsnFailureCode value);

  /**
   * Resolves the optional TalkerStatus child, a BaseDataVariableType with DataType TsnTalkerStatus.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getTalkerStatusNode() throws UaException;

  /** Asynchronous form of {@link #getTalkerStatusNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getTalkerStatusNodeAsync();

  /**
   * Reads the Value of the TalkerStatus child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TsnTalkerStatus readTalkerStatus() throws UaException;

  /**
   * Writes the Value of the TalkerStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException;

  /** Asynchronous form of {@link #readTalkerStatus()}. */
  CompletableFuture<? extends @Nullable TsnTalkerStatus> readTalkerStatusAsync();

  /** Asynchronous form of {@link #writeTalkerStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTalkerStatusAsync(@Nullable TsnTalkerStatus value);

  /**
   * Resolves the optional ListenerStatus child, a BaseDataVariableType with DataType
   * TsnListenerStatus.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getListenerStatusNode() throws UaException;

  /** Asynchronous form of {@link #getListenerStatusNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getListenerStatusNodeAsync();

  /**
   * Reads the Value of the ListenerStatus child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TsnListenerStatus readListenerStatus() throws UaException;

  /**
   * Writes the Value of the ListenerStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeListenerStatus(@Nullable TsnListenerStatus value) throws UaException;

  /** Asynchronous form of {@link #readListenerStatus()}. */
  CompletableFuture<? extends @Nullable TsnListenerStatus> readListenerStatusAsync();

  /** Asynchronous form of {@link #writeListenerStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeListenerStatusAsync(@Nullable TsnListenerStatus value);

  /**
   * Resolves the mandatory FailureSystemIdentifier child, a BaseDataVariableType with DataType
   * Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getFailureSystemIdentifierNode() throws UaException;

  /** Asynchronous form of {@link #getFailureSystemIdentifierNode()}. */
  CompletableFuture<? extends VariableNode> getFailureSystemIdentifierNodeAsync();

  /**
   * Reads the Value of the FailureSystemIdentifier child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Object readFailureSystemIdentifier() throws UaException;

  /**
   * Writes the Value of the FailureSystemIdentifier child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFailureSystemIdentifier(@Nullable Object value) throws UaException;

  /** Asynchronous form of {@link #readFailureSystemIdentifier()}. */
  CompletableFuture<? extends @Nullable Object> readFailureSystemIdentifierAsync();

  /**
   * Asynchronous form of {@link #writeFailureSystemIdentifier}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(@Nullable Object value);
}
