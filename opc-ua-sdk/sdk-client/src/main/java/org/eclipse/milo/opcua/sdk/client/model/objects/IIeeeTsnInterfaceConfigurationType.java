package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeTsnInterfaceConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10">Model
 *     documentation</a>
 */
public interface IIeeeTsnInterfaceConfigurationType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24188L);

  /**
   * Resolves the mandatory MacAddress child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMacAddressNode() throws UaException;

  /** Asynchronous form of {@link #getMacAddressNode()}. */
  CompletableFuture<? extends VariableNode> getMacAddressNodeAsync();

  /**
   * Reads the Value of the MacAddress child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMacAddress() throws UaException;

  /**
   * Writes the Value of the MacAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMacAddress(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMacAddress()}. */
  CompletableFuture<? extends @Nullable String> readMacAddressAsync();

  /** Asynchronous form of {@link #writeMacAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMacAddressAsync(@Nullable String value);

  /**
   * Resolves the optional InterfaceName child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getInterfaceNameNode() throws UaException;

  /** Asynchronous form of {@link #getInterfaceNameNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getInterfaceNameNodeAsync();

  /**
   * Reads the Value of the InterfaceName child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readInterfaceName() throws UaException;

  /**
   * Writes the Value of the InterfaceName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInterfaceName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readInterfaceName()}. */
  CompletableFuture<? extends @Nullable String> readInterfaceNameAsync();

  /** Asynchronous form of {@link #writeInterfaceName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInterfaceNameAsync(@Nullable String value);
}
