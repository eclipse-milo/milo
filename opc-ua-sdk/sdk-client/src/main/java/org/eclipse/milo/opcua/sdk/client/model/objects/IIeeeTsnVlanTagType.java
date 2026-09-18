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
 * Client API for the IIeeeTsnVlanTagType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14">Model
 *     documentation</a>
 */
public interface IIeeeTsnVlanTagType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24202L);

  /**
   * Resolves the mandatory PriorityCodePoint child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPriorityCodePointNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityCodePointNode()}. */
  CompletableFuture<? extends VariableNode> getPriorityCodePointNodeAsync();

  /**
   * Reads the Value of the PriorityCodePoint child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPriorityCodePoint() throws UaException;

  /**
   * Writes the Value of the PriorityCodePoint child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriorityCodePoint(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPriorityCodePoint()}. */
  CompletableFuture<? extends @Nullable UByte> readPriorityCodePointAsync();

  /** Asynchronous form of {@link #writePriorityCodePoint}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityCodePointAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory VlanId child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getVlanIdNode() throws UaException;

  /** Asynchronous form of {@link #getVlanIdNode()}. */
  CompletableFuture<? extends VariableNode> getVlanIdNodeAsync();

  /**
   * Reads the Value of the VlanId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readVlanId() throws UaException;

  /**
   * Writes the Value of the VlanId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeVlanId(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readVlanId()}. */
  CompletableFuture<? extends @Nullable UShort> readVlanIdAsync();

  /** Asynchronous form of {@link #writeVlanId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeVlanIdAsync(@Nullable UShort value);
}
