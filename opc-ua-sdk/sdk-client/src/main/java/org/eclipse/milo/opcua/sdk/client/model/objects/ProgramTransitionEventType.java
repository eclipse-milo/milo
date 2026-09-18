package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ProgramTransitionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2">Model
 *     documentation</a>
 */
public interface ProgramTransitionEventType extends TransitionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2378L);

  /**
   * Resolves the mandatory IntermediateResult child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getIntermediateResultNode() throws UaException;

  /** Asynchronous form of {@link #getIntermediateResultNode()}. */
  CompletableFuture<? extends VariableNode> getIntermediateResultNodeAsync();

  /**
   * Reads the Value of the IntermediateResult child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readIntermediateResult() throws UaException;

  /**
   * Writes the Value of the IntermediateResult child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIntermediateResult(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readIntermediateResult()}. */
  CompletableFuture<? extends @Nullable Variant> readIntermediateResultAsync();

  /** Asynchronous form of {@link #writeIntermediateResult}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIntermediateResultAsync(@Nullable Variant value);
}
