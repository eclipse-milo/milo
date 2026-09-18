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
 * Client API for the AuditConditionRespondEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.5">Model
 *     documentation</a>
 */
public interface AuditConditionRespondEventType extends AuditConditionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 8927L);

  QualifiedProperty<UInteger> SelectedResponse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SelectedResponse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory SelectedResponse child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSelectedResponseNode() throws UaException;

  /** Asynchronous form of {@link #getSelectedResponseNode()}. */
  CompletableFuture<? extends PropertyType> getSelectedResponseNodeAsync();

  /**
   * Reads the Value of the SelectedResponse child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSelectedResponse() throws UaException;

  /**
   * Writes the Value of the SelectedResponse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSelectedResponse(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSelectedResponse()}. */
  CompletableFuture<? extends @Nullable UInteger> readSelectedResponseAsync();

  /** Asynchronous form of {@link #writeSelectedResponse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSelectedResponseAsync(@Nullable UInteger value);
}
