package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LldpRemoteStatisticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18996L);

  /**
   * Resolves the mandatory RemoteDrops child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRemoteDropsNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteDropsNode()}. */
  CompletableFuture<? extends VariableNode> getRemoteDropsNodeAsync();

  /**
   * Reads the Value of the RemoteDrops child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRemoteDrops() throws UaException;

  /**
   * Writes the Value of the RemoteDrops child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteDrops(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRemoteDrops()}. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteDropsAsync();

  /** Asynchronous form of {@link #writeRemoteDrops}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteDropsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RemoteAgeouts child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRemoteAgeoutsNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteAgeoutsNode()}. */
  CompletableFuture<? extends VariableNode> getRemoteAgeoutsNodeAsync();

  /**
   * Reads the Value of the RemoteAgeouts child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRemoteAgeouts() throws UaException;

  /**
   * Writes the Value of the RemoteAgeouts child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteAgeouts(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRemoteAgeouts()}. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteAgeoutsAsync();

  /** Asynchronous form of {@link #writeRemoteAgeouts}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RemoteDeletes child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRemoteDeletesNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteDeletesNode()}. */
  CompletableFuture<? extends VariableNode> getRemoteDeletesNodeAsync();

  /**
   * Reads the Value of the RemoteDeletes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRemoteDeletes() throws UaException;

  /**
   * Writes the Value of the RemoteDeletes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteDeletes(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRemoteDeletes()}. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteDeletesAsync();

  /** Asynchronous form of {@link #writeRemoteDeletes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteDeletesAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RemoteInserts child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRemoteInsertsNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteInsertsNode()}. */
  CompletableFuture<? extends VariableNode> getRemoteInsertsNodeAsync();

  /**
   * Reads the Value of the RemoteInserts child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRemoteInserts() throws UaException;

  /**
   * Writes the Value of the RemoteInserts child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteInserts(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRemoteInserts()}. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteInsertsAsync();

  /** Asynchronous form of {@link #writeRemoteInserts}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteInsertsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory LastChangeTime child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastChangeTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastChangeTimeNode()}. */
  CompletableFuture<? extends VariableNode> getLastChangeTimeNodeAsync();

  /**
   * Reads the Value of the LastChangeTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readLastChangeTime() throws UaException;

  /**
   * Writes the Value of the LastChangeTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastChangeTime(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readLastChangeTime()}. */
  CompletableFuture<? extends @Nullable UInteger> readLastChangeTimeAsync();

  /** Asynchronous form of {@link #writeLastChangeTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastChangeTimeAsync(@Nullable UInteger value);
}
