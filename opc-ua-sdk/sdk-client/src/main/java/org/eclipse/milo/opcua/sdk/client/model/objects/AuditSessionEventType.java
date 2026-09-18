package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditSessionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.7">Model
 *     documentation</a>
 */
public interface AuditSessionEventType extends AuditSecurityEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2069L);

  QualifiedProperty<NodeId> SessionId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SessionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory SessionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getSessionIdNode()}. */
  CompletableFuture<? extends PropertyType> getSessionIdNodeAsync();

  /**
   * Reads the Value of the SessionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSessionId() throws UaException;

  /**
   * Writes the Value of the SessionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSessionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync();

  /** Asynchronous form of {@link #writeSessionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value);
}
