package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditConditionShelvingEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.8">Model
 *     documentation</a>
 */
public interface AuditConditionShelvingEventType extends AuditConditionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11093L);

  QualifiedProperty<Double> ShelvingTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ShelvingTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  /**
   * Resolves the optional ShelvingTime child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getShelvingTimeNode() throws UaException;

  /** Asynchronous form of {@link #getShelvingTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getShelvingTimeNodeAsync();

  /**
   * Reads the Value of the ShelvingTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readShelvingTime() throws UaException;

  /**
   * Writes the Value of the ShelvingTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeShelvingTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readShelvingTime()}. */
  CompletableFuture<? extends @Nullable Double> readShelvingTimeAsync();

  /** Asynchronous form of {@link #writeShelvingTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeShelvingTimeAsync(@Nullable Double value);
}
