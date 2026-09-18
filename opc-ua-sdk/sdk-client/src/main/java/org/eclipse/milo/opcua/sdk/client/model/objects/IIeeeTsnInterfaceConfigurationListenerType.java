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
 * Client API for the IIeeeTsnInterfaceConfigurationListenerType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.12">Model
 *     documentation</a>
 */
public interface IIeeeTsnInterfaceConfigurationListenerType
    extends IIeeeTsnInterfaceConfigurationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24195L);

  /**
   * Resolves the optional ReceiveOffset child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getReceiveOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getReceiveOffsetNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getReceiveOffsetNodeAsync();

  /**
   * Reads the Value of the ReceiveOffset child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readReceiveOffset() throws UaException;

  /**
   * Writes the Value of the ReceiveOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReceiveOffset(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readReceiveOffset()}. */
  CompletableFuture<? extends @Nullable UInteger> readReceiveOffsetAsync();

  /** Asynchronous form of {@link #writeReceiveOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable UInteger value);
}
