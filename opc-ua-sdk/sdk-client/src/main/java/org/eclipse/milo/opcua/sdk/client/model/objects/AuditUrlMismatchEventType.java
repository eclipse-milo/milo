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
 * Client API for the AuditUrlMismatchEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.9">Model
 *     documentation</a>
 */
public interface AuditUrlMismatchEventType extends AuditCreateSessionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2748L);

  QualifiedProperty<String> EndpointUrl_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndpointUrl",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory EndpointUrl child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEndpointUrlNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointUrlNode()}. */
  CompletableFuture<? extends PropertyType> getEndpointUrlNodeAsync();

  /**
   * Reads the Value of the EndpointUrl child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readEndpointUrl() throws UaException;

  /**
   * Writes the Value of the EndpointUrl child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readEndpointUrl()}. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Asynchronous form of {@link #writeEndpointUrl}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);
}
