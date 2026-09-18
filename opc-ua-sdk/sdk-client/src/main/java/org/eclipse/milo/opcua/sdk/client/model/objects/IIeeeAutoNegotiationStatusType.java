package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIeeeAutoNegotiationStatusType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3">Model
 *     documentation</a>
 */
public interface IIeeeAutoNegotiationStatusType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24233L);

  /**
   * Resolves the mandatory NegotiationStatus child, a BaseDataVariableType with DataType
   * NegotiationStatus.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getNegotiationStatusNode() throws UaException;

  /** Asynchronous form of {@link #getNegotiationStatusNode()}. */
  CompletableFuture<? extends VariableNode> getNegotiationStatusNodeAsync();

  /**
   * Reads the Value of the NegotiationStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NegotiationStatus readNegotiationStatus() throws UaException;

  /**
   * Writes the Value of the NegotiationStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNegotiationStatus(@Nullable NegotiationStatus value) throws UaException;

  /** Asynchronous form of {@link #readNegotiationStatus()}. */
  CompletableFuture<? extends @Nullable NegotiationStatus> readNegotiationStatusAsync();

  /** Asynchronous form of {@link #writeNegotiationStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNegotiationStatusAsync(@Nullable NegotiationStatus value);
}
