package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the StateType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.8">Model
 *     documentation</a>
 */
public interface StateType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2307L);

  QualifiedProperty<UInteger> StateNumber_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StateNumber",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory StateNumber child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStateNumberNode() throws UaException;

  /** Asynchronous form of {@link #getStateNumberNode()}. */
  CompletableFuture<? extends PropertyType> getStateNumberNodeAsync();

  /**
   * Reads the Value of the StateNumber child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readStateNumber() throws UaException;

  /**
   * Writes the Value of the StateNumber child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStateNumber(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readStateNumber()}. */
  CompletableFuture<? extends @Nullable UInteger> readStateNumberAsync();

  /** Asynchronous form of {@link #writeStateNumber}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStateNumberAsync(@Nullable UInteger value);
}
