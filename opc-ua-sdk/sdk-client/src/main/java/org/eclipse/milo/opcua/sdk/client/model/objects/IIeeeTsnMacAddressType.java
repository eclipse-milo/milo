package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeTsnMacAddressType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13">Model
 *     documentation</a>
 */
public interface IIeeeTsnMacAddressType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24199L);

  /**
   * Resolves the optional SourceAddress child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSourceAddressNode() throws UaException;

  /** Asynchronous form of {@link #getSourceAddressNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSourceAddressNodeAsync();

  /**
   * Reads the Value of the SourceAddress child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  UByte @Nullable [] readSourceAddress() throws UaException;

  /**
   * Writes the Value of the SourceAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSourceAddress(UByte @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSourceAddress()}. */
  CompletableFuture<? extends UByte @Nullable []> readSourceAddressAsync();

  /** Asynchronous form of {@link #writeSourceAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSourceAddressAsync(UByte @Nullable [] value);

  /**
   * Resolves the mandatory DestinationAddress child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDestinationAddressNode() throws UaException;

  /** Asynchronous form of {@link #getDestinationAddressNode()}. */
  CompletableFuture<? extends VariableNode> getDestinationAddressNodeAsync();

  /**
   * Reads the Value of the DestinationAddress child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  UByte @Nullable [] readDestinationAddress() throws UaException;

  /**
   * Writes the Value of the DestinationAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDestinationAddress(UByte @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDestinationAddress()}. */
  CompletableFuture<? extends UByte @Nullable []> readDestinationAddressAsync();

  /** Asynchronous form of {@link #writeDestinationAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDestinationAddressAsync(UByte @Nullable [] value);
}
