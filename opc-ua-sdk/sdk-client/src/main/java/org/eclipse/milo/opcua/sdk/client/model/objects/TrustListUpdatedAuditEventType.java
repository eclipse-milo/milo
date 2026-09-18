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
 * Client API for the TrustListUpdatedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.13">Model
 *     documentation</a>
 */
public interface TrustListUpdatedAuditEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12561L);

  QualifiedProperty<NodeId> TrustListId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TrustListId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory TrustListId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTrustListIdNode() throws UaException;

  /** Asynchronous form of {@link #getTrustListIdNode()}. */
  CompletableFuture<? extends PropertyType> getTrustListIdNodeAsync();

  /**
   * Reads the Value of the TrustListId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readTrustListId() throws UaException;

  /**
   * Writes the Value of the TrustListId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTrustListId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readTrustListId()}. */
  CompletableFuture<? extends @Nullable NodeId> readTrustListIdAsync();

  /** Asynchronous form of {@link #writeTrustListId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTrustListIdAsync(@Nullable NodeId value);
}
