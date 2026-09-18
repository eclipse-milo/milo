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
 * Client API for the AuditHistoryDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.5">Model
 *     documentation</a>
 */
public interface AuditHistoryDeleteEventType extends AuditHistoryUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3012L);

  QualifiedProperty<NodeId> UpdatedNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdatedNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory UpdatedNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUpdatedNodeNode() throws UaException;

  /** Asynchronous form of {@link #getUpdatedNodeNode()}. */
  CompletableFuture<? extends PropertyType> getUpdatedNodeNodeAsync();

  /**
   * Reads the Value of the UpdatedNode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readUpdatedNode() throws UaException;

  /**
   * Writes the Value of the UpdatedNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUpdatedNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readUpdatedNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readUpdatedNodeAsync();

  /** Asynchronous form of {@link #writeUpdatedNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUpdatedNodeAsync(@Nullable NodeId value);
}
