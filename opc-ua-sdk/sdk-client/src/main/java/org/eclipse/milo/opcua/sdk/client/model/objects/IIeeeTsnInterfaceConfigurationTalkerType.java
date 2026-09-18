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
 * Client API for the IIeeeTsnInterfaceConfigurationTalkerType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.11">Model
 *     documentation</a>
 */
public interface IIeeeTsnInterfaceConfigurationTalkerType
    extends IIeeeTsnInterfaceConfigurationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24191L);

  /**
   * Resolves the optional TimeAwareOffset child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getTimeAwareOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getTimeAwareOffsetNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getTimeAwareOffsetNodeAsync();

  /**
   * Reads the Value of the TimeAwareOffset child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTimeAwareOffset() throws UaException;

  /**
   * Writes the Value of the TimeAwareOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTimeAwareOffset(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTimeAwareOffset()}. */
  CompletableFuture<? extends @Nullable UInteger> readTimeAwareOffsetAsync();

  /** Asynchronous form of {@link #writeTimeAwareOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTimeAwareOffsetAsync(@Nullable UInteger value);
}
