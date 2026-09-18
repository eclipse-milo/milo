package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeBaseEthernetPortType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">Model
 *     documentation</a>
 */
public interface IIeeeBaseEthernetPortType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24158L);

  /**
   * Resolves the mandatory MaxFrameLength child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxFrameLengthNode() throws UaException;

  /** Asynchronous form of {@link #getMaxFrameLengthNode()}. */
  CompletableFuture<? extends VariableNode> getMaxFrameLengthNodeAsync();

  /**
   * Reads the Value of the MaxFrameLength child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxFrameLength() throws UaException;

  /**
   * Writes the Value of the MaxFrameLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxFrameLength(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxFrameLength()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxFrameLengthAsync();

  /** Asynchronous form of {@link #writeMaxFrameLength}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxFrameLengthAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory Speed child, a AnalogUnitType with DataType UInt64.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.4">AnalogUnitType
   *     documentation</a>
   */
  AnalogUnitType getSpeedNode() throws UaException;

  /** Asynchronous form of {@link #getSpeedNode()}. */
  CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync();

  /**
   * Reads the Value of the Speed child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ULong readSpeed() throws UaException;

  /**
   * Writes the Value of the Speed child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSpeed(@Nullable ULong value) throws UaException;

  /** Asynchronous form of {@link #readSpeed()}. */
  CompletableFuture<? extends @Nullable ULong> readSpeedAsync();

  /** Asynchronous form of {@link #writeSpeed}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value);

  /**
   * Resolves the mandatory Duplex child, a BaseDataVariableType with DataType Duplex.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDuplexNode() throws UaException;

  /** Asynchronous form of {@link #getDuplexNode()}. */
  CompletableFuture<? extends VariableNode> getDuplexNodeAsync();

  /**
   * Reads the Value of the Duplex child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Duplex readDuplex() throws UaException;

  /**
   * Writes the Value of the Duplex child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDuplex(@Nullable Duplex value) throws UaException;

  /** Asynchronous form of {@link #readDuplex()}. */
  CompletableFuture<? extends @Nullable Duplex> readDuplexAsync();

  /** Asynchronous form of {@link #writeDuplex}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDuplexAsync(@Nullable Duplex value);
}
