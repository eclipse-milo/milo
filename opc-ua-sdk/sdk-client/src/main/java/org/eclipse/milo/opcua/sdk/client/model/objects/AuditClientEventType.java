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
 * Client API for the AuditClientEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.36">Model
 *     documentation</a>
 */
public interface AuditClientEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23606L);

  QualifiedProperty<String> ServerUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
          -1,
          String.class);

  /**
   * Resolves the mandatory ServerUri child, a PropertyType with DataType UriString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerUriNode() throws UaException;

  /** Asynchronous form of {@link #getServerUriNode()}. */
  CompletableFuture<? extends PropertyType> getServerUriNodeAsync();

  /**
   * Reads the Value of the ServerUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readServerUri() throws UaException;

  /**
   * Writes the Value of the ServerUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readServerUri()}. */
  CompletableFuture<? extends @Nullable String> readServerUriAsync();

  /** Asynchronous form of {@link #writeServerUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String value);
}
