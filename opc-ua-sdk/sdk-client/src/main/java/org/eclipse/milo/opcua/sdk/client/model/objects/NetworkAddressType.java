package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NetworkAddressType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">Model
 *     documentation</a>
 */
public interface NetworkAddressType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21145L);

  /**
   * Resolves the mandatory NetworkInterface child, a SelectionListType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">SelectionListType
   *     documentation</a>
   */
  SelectionListType getNetworkInterfaceNode() throws UaException;

  /** Asynchronous form of {@link #getNetworkInterfaceNode()}. */
  CompletableFuture<? extends SelectionListType> getNetworkInterfaceNodeAsync();

  /**
   * Reads the Value of the NetworkInterface child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readNetworkInterface() throws UaException;

  /**
   * Writes the Value of the NetworkInterface child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNetworkInterface(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readNetworkInterface()}. */
  CompletableFuture<? extends @Nullable String> readNetworkInterfaceAsync();

  /** Asynchronous form of {@link #writeNetworkInterface}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNetworkInterfaceAsync(@Nullable String value);
}
