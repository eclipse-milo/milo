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
 * Client API for the FiniteStateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.6">Model
 *     documentation</a>
 */
public interface FiniteStateVariableType extends StateVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2760L);

  QualifiedProperty<NodeId> FiniteStateVariableTypeId_PROPERTY =
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
  @Nullable NodeId readFiniteStateVariableTypeId() throws UaException;

  /**
   * Writes the Value of the Id child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFiniteStateVariableTypeId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readFiniteStateVariableTypeId()}. */
  CompletableFuture<? extends @Nullable NodeId> readFiniteStateVariableTypeIdAsync();

  /**
   * Asynchronous form of {@link #writeFiniteStateVariableTypeId}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeFiniteStateVariableTypeIdAsync(@Nullable NodeId value);
}
