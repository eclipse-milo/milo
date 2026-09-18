package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IBaseEthernetCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.4">Model
 *     documentation</a>
 */
public interface IBaseEthernetCapabilitiesType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24167L);

  /**
   * Resolves the mandatory VlanTagCapable child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getVlanTagCapableNode() throws UaException;

  /** Asynchronous form of {@link #getVlanTagCapableNode()}. */
  CompletableFuture<? extends VariableNode> getVlanTagCapableNodeAsync();

  /**
   * Reads the Value of the VlanTagCapable child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readVlanTagCapable() throws UaException;

  /**
   * Writes the Value of the VlanTagCapable child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeVlanTagCapable(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readVlanTagCapable()}. */
  CompletableFuture<? extends @Nullable Boolean> readVlanTagCapableAsync();

  /** Asynchronous form of {@link #writeVlanTagCapable}; completes with the operation status. */
  CompletableFuture<StatusCode> writeVlanTagCapableAsync(@Nullable Boolean value);
}
