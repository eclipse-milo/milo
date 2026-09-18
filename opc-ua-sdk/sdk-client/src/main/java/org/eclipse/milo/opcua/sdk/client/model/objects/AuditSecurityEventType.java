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
 * Client API for the AuditSecurityEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.4">Model
 *     documentation</a>
 */
public interface AuditSecurityEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2058L);

  QualifiedProperty<StatusCode> StatusCodeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StatusCodeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  /**
   * Resolves the optional StatusCodeId child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getStatusCodeIdNode() throws UaException;

  /** Asynchronous form of {@link #getStatusCodeIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getStatusCodeIdNodeAsync();

  /**
   * Reads the Value of the StatusCodeId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readStatusCodeId() throws UaException;

  /**
   * Writes the Value of the StatusCodeId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readStatusCodeId()}. */
  CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync();

  /** Asynchronous form of {@link #writeStatusCodeId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode value);
}
