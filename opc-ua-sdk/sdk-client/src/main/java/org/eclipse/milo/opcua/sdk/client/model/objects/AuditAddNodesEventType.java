package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditAddNodesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.20">Model
 *     documentation</a>
 */
public interface AuditAddNodesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2091L);

  QualifiedProperty<AddNodesItem[]> NodesToAdd_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodesToAdd",
          ExpandedNodeId.of(Namespaces.OPC_UA, 376L),
          1,
          AddNodesItem[].class);

  /**
   * Resolves the mandatory NodesToAdd child, a PropertyType with DataType AddNodesItem.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNodesToAddNode() throws UaException;

  /** Asynchronous form of {@link #getNodesToAddNode()}. */
  CompletableFuture<? extends PropertyType> getNodesToAddNodeAsync();

  /**
   * Reads the Value of the NodesToAdd child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AddNodesItem @Nullable [] readNodesToAdd() throws UaException;

  /**
   * Writes the Value of the NodesToAdd child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNodesToAdd(@Nullable AddNodesItem @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readNodesToAdd()}. */
  CompletableFuture<? extends @Nullable AddNodesItem @Nullable []> readNodesToAddAsync();

  /** Asynchronous form of {@link #writeNodesToAdd}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNodesToAddAsync(@Nullable AddNodesItem @Nullable [] value);
}
