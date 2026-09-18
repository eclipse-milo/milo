package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditDeleteNodesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.21">Model
 *     documentation</a>
 */
public interface AuditDeleteNodesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2093L);

  QualifiedProperty<DeleteNodesItem[]> NodesToDelete_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodesToDelete",
          ExpandedNodeId.of(Namespaces.OPC_UA, 382L),
          1,
          DeleteNodesItem[].class);

  /**
   * Resolves the mandatory NodesToDelete child, a PropertyType with DataType DeleteNodesItem.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNodesToDeleteNode() throws UaException;

  /** Asynchronous form of {@link #getNodesToDeleteNode()}. */
  CompletableFuture<? extends PropertyType> getNodesToDeleteNodeAsync();

  /**
   * Reads the Value of the NodesToDelete child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DeleteNodesItem @Nullable [] readNodesToDelete() throws UaException;

  /**
   * Writes the Value of the NodesToDelete child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNodesToDelete(@Nullable DeleteNodesItem @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readNodesToDelete()}. */
  CompletableFuture<? extends @Nullable DeleteNodesItem @Nullable []> readNodesToDeleteAsync();

  /** Asynchronous form of {@link #writeNodesToDelete}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNodesToDeleteAsync(
      @Nullable DeleteNodesItem @Nullable [] value);
}
