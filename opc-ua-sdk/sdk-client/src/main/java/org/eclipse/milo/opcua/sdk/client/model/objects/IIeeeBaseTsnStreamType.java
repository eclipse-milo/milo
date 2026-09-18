package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeBaseTsnStreamType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24173L);

  /**
   * Resolves the mandatory StreamName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStreamNameNode() throws UaException;

  /** Asynchronous form of {@link #getStreamNameNode()}. */
  CompletableFuture<? extends VariableNode> getStreamNameNodeAsync();

  /**
   * Reads the Value of the StreamName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readStreamName() throws UaException;

  /**
   * Writes the Value of the StreamName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStreamName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readStreamName()}. */
  CompletableFuture<? extends @Nullable String> readStreamNameAsync();

  /** Asynchronous form of {@link #writeStreamName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStreamNameAsync(@Nullable String value);

  /**
   * Resolves the optional AccumulatedLatency child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getAccumulatedLatencyNode() throws UaException;

  /** Asynchronous form of {@link #getAccumulatedLatencyNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getAccumulatedLatencyNodeAsync();

  /**
   * Reads the Value of the AccumulatedLatency child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readAccumulatedLatency() throws UaException;

  /**
   * Writes the Value of the AccumulatedLatency child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAccumulatedLatency(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readAccumulatedLatency()}. */
  CompletableFuture<? extends @Nullable UInteger> readAccumulatedLatencyAsync();

  /** Asynchronous form of {@link #writeAccumulatedLatency}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAccumulatedLatencyAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory State child, a BaseDataVariableType with DataType TsnStreamState.
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
  @Nullable TsnStreamState readState() throws UaException;

  /**
   * Writes the Value of the State child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeState(@Nullable TsnStreamState value) throws UaException;

  /** Asynchronous form of {@link #readState()}. */
  CompletableFuture<? extends @Nullable TsnStreamState> readStateAsync();

  /** Asynchronous form of {@link #writeState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable TsnStreamState value);

  /**
   * Resolves the mandatory StreamId child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStreamIdNode() throws UaException;

  /** Asynchronous form of {@link #getStreamIdNode()}. */
  CompletableFuture<? extends VariableNode> getStreamIdNodeAsync();

  /**
   * Reads the Value of the StreamId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  UByte @Nullable [] readStreamId() throws UaException;

  /**
   * Writes the Value of the StreamId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStreamId(UByte @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readStreamId()}. */
  CompletableFuture<? extends UByte @Nullable []> readStreamIdAsync();

  /** Asynchronous form of {@link #writeStreamId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStreamIdAsync(UByte @Nullable [] value);

  /**
   * Resolves the optional SrClassId child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSrClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getSrClassIdNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSrClassIdNodeAsync();

  /**
   * Reads the Value of the SrClassId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readSrClassId() throws UaException;

  /**
   * Writes the Value of the SrClassId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSrClassId(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readSrClassId()}. */
  CompletableFuture<? extends @Nullable UByte> readSrClassIdAsync();

  /** Asynchronous form of {@link #writeSrClassId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSrClassIdAsync(@Nullable UByte value);
}
