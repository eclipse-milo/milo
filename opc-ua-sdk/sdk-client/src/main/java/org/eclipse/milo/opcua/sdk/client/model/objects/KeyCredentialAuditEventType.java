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
 * Client API for the KeyCredentialAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.5.8">Model
 *     documentation</a>
 */
public interface KeyCredentialAuditEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18011L);

  QualifiedProperty<String> ResourceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ResourceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResourceUriNode() throws UaException;

  /** Asynchronous form of {@link #getResourceUriNode()}. */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /**
   * Reads the Value of the ResourceUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readResourceUri() throws UaException;

  /**
   * Writes the Value of the ResourceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readResourceUri()}. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Asynchronous form of {@link #writeResourceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);
}
