package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeBaseTsnTrafficSpecificationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnTrafficSpecificationType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24179L);

  /**
   * Resolves the mandatory MaxFrameSize child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxFrameSizeNode() throws UaException;

  /** Asynchronous form of {@link #getMaxFrameSizeNode()}. */
  CompletableFuture<? extends VariableNode> getMaxFrameSizeNodeAsync();

  /**
   * Reads the Value of the MaxFrameSize child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxFrameSize() throws UaException;

  /**
   * Writes the Value of the MaxFrameSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxFrameSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxFrameSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFrameSizeAsync();

  /** Asynchronous form of {@link #writeMaxFrameSize}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxFrameSizeAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxIntervalFrames child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxIntervalFramesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxIntervalFramesNode()}. */
  CompletableFuture<? extends VariableNode> getMaxIntervalFramesNodeAsync();

  /**
   * Reads the Value of the MaxIntervalFrames child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxIntervalFrames() throws UaException;

  /**
   * Writes the Value of the MaxIntervalFrames child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxIntervalFrames(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxIntervalFrames()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxIntervalFramesAsync();

  /** Asynchronous form of {@link #writeMaxIntervalFrames}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory Interval child, a BaseDataVariableType with DataType
   * UnsignedRationalNumber.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getIntervalNode()}. */
  CompletableFuture<? extends VariableNode> getIntervalNodeAsync();

  /**
   * Reads the Value of the Interval child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UnsignedRationalNumber readInterval() throws UaException;

  /**
   * Writes the Value of the Interval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInterval(@Nullable UnsignedRationalNumber value) throws UaException;

  /** Asynchronous form of {@link #readInterval()}. */
  CompletableFuture<? extends @Nullable UnsignedRationalNumber> readIntervalAsync();

  /** Asynchronous form of {@link #writeInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIntervalAsync(@Nullable UnsignedRationalNumber value);
}
