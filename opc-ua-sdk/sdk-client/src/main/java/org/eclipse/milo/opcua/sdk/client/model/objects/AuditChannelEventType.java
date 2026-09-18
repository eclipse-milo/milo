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
 * Client API for the AuditChannelEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.5">Model
 *     documentation</a>
 */
public interface AuditChannelEventType extends AuditSecurityEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2059L);

  QualifiedProperty<String> SecureChannelId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecureChannelId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory SecureChannelId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecureChannelIdNode() throws UaException;

  /** Asynchronous form of {@link #getSecureChannelIdNode()}. */
  CompletableFuture<? extends PropertyType> getSecureChannelIdNodeAsync();

  /**
   * Reads the Value of the SecureChannelId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecureChannelId() throws UaException;

  /**
   * Writes the Value of the SecureChannelId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecureChannelId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecureChannelId()}. */
  CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync();

  /** Asynchronous form of {@link #writeSecureChannelId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String value);
}
