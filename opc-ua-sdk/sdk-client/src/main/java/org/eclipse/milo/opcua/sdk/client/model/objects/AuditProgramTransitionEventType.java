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
 * Client API for the AuditProgramTransitionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.6">Model
 *     documentation</a>
 */
public interface AuditProgramTransitionEventType extends AuditUpdateStateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11856L);

  QualifiedProperty<UInteger> TransitionNumber_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TransitionNumber",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory TransitionNumber child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTransitionNumberNode() throws UaException;

  /** Asynchronous form of {@link #getTransitionNumberNode()}. */
  CompletableFuture<? extends PropertyType> getTransitionNumberNodeAsync();

  /**
   * Reads the Value of the TransitionNumber child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTransitionNumber() throws UaException;

  /**
   * Writes the Value of the TransitionNumber child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransitionNumber(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTransitionNumber()}. */
  CompletableFuture<? extends @Nullable UInteger> readTransitionNumberAsync();

  /** Asynchronous form of {@link #writeTransitionNumber}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTransitionNumberAsync(@Nullable UInteger value);
}
