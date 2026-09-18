package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the FiniteTransitionVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">Model
 *     documentation</a>
 */
public interface FiniteTransitionVariableType extends TransitionVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2767L);

  QualifiedProperty<NodeId> FiniteTransitionVariableTypeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Id", ExpandedNodeId.of(Namespaces.OPC_UA, 17L), -1, NodeId.class);

  /**
   * Resolves the mandatory Id child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIdNode() throws UaException;

  /** Asynchronous form of {@link #getIdNode()}. */
  CompletableFuture<? extends PropertyType> getIdNodeAsync();

  /**
   * Reads the Value of the Id child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readFiniteTransitionVariableTypeId() throws UaException;

  /**
   * Writes the Value of the Id child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFiniteTransitionVariableTypeId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readFiniteTransitionVariableTypeId()}. */
  CompletableFuture<? extends @Nullable NodeId> readFiniteTransitionVariableTypeIdAsync();

  /**
   * Asynchronous form of {@link #writeFiniteTransitionVariableTypeId}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeFiniteTransitionVariableTypeIdAsync(@Nullable NodeId value);
}
