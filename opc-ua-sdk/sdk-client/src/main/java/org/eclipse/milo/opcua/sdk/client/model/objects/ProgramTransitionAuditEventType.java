package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Client API for the ProgramTransitionAuditEventType ObjectType. */
public interface ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3806L);

  /**
   * Resolves the mandatory Transition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  FiniteTransitionVariableType getTransitionNode() throws UaException;

  /** Asynchronous form of {@link #getTransitionNode()}. */
  CompletableFuture<? extends FiniteTransitionVariableType> getTransitionNodeAsync();

  /**
   * Reads the Value of the Transition child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readTransition() throws UaException;

  /**
   * Writes the Value of the Transition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransition(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readTransition()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readTransitionAsync();

  /** Asynchronous form of {@link #writeTransition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTransitionAsync(@Nullable LocalizedText value);
}
