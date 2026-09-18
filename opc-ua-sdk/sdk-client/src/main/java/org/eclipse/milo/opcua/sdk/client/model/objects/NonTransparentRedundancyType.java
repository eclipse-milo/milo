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
 * Client API for the NonTransparentRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.9">Model
 *     documentation</a>
 */
public interface NonTransparentRedundancyType extends ServerRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2039L);

  QualifiedProperty<String[]> ServerUriArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerUriArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  /**
   * Resolves the mandatory ServerUriArray child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerUriArrayNode() throws UaException;

  /** Asynchronous form of {@link #getServerUriArrayNode()}. */
  CompletableFuture<? extends PropertyType> getServerUriArrayNodeAsync();

  /**
   * Reads the Value of the ServerUriArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readServerUriArray() throws UaException;

  /**
   * Writes the Value of the ServerUriArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerUriArray(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readServerUriArray()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerUriArrayAsync();

  /** Asynchronous form of {@link #writeServerUriArray}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerUriArrayAsync(@Nullable String @Nullable [] value);
}
