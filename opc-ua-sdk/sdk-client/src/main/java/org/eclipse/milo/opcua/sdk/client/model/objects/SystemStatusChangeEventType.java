package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SystemStatusChangeEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.30">Model
 *     documentation</a>
 */
public interface SystemStatusChangeEventType extends SystemEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11446L);

  QualifiedProperty<ServerState> SystemState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SystemState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 852L),
          -1,
          ServerState.class);

  /**
   * Resolves the mandatory SystemState child, a PropertyType with DataType ServerState.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSystemStateNode() throws UaException;

  /** Asynchronous form of {@link #getSystemStateNode()}. */
  CompletableFuture<? extends PropertyType> getSystemStateNodeAsync();

  /**
   * Reads the Value of the SystemState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerState readSystemState() throws UaException;

  /**
   * Writes the Value of the SystemState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemState(@Nullable ServerState value) throws UaException;

  /** Asynchronous form of {@link #readSystemState()}. */
  CompletableFuture<? extends @Nullable ServerState> readSystemStateAsync();

  /** Asynchronous form of {@link #writeSystemState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSystemStateAsync(@Nullable ServerState value);
}
