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
 * Client API for the TransparentRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.8">Model
 *     documentation</a>
 */
public interface TransparentRedundancyType extends ServerRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2036L);

  QualifiedProperty<String> CurrentServerId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CurrentServerId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory CurrentServerId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCurrentServerIdNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentServerIdNode()}. */
  CompletableFuture<? extends PropertyType> getCurrentServerIdNodeAsync();

  /**
   * Reads the Value of the CurrentServerId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readCurrentServerId() throws UaException;

  /**
   * Writes the Value of the CurrentServerId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentServerId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readCurrentServerId()}. */
  CompletableFuture<? extends @Nullable String> readCurrentServerIdAsync();

  /** Asynchronous form of {@link #writeCurrentServerId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentServerIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory RedundantServerArray child, a PropertyType with DataType
   * RedundantServerDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRedundantServerArrayNode() throws UaException;

  /** Asynchronous form of {@link #getRedundantServerArrayNode()}. */
  CompletableFuture<? extends PropertyType> getRedundantServerArrayNodeAsync();
}
