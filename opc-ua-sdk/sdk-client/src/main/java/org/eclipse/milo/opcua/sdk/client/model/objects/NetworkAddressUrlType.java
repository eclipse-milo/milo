package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NetworkAddressUrlType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.7">Model
 *     documentation</a>
 */
public interface NetworkAddressUrlType extends NetworkAddressType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21147L);

  /**
   * Resolves the mandatory Url child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getUrlNode() throws UaException;

  /** Asynchronous form of {@link #getUrlNode()}. */
  CompletableFuture<? extends VariableNode> getUrlNodeAsync();

  /**
   * Reads the Value of the Url child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readUrl() throws UaException;

  /**
   * Writes the Value of the Url child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUrl(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readUrl()}. */
  CompletableFuture<? extends @Nullable String> readUrlAsync();

  /** Asynchronous form of {@link #writeUrl}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUrlAsync(@Nullable String value);
}
