package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditAddReferencesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.22">Model
 *     documentation</a>
 */
public interface AuditAddReferencesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2095L);

  QualifiedProperty<AddReferencesItem[]> ReferencesToAdd_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReferencesToAdd",
          ExpandedNodeId.of(Namespaces.OPC_UA, 379L),
          1,
          AddReferencesItem[].class);

  /**
   * Resolves the mandatory ReferencesToAdd child, a PropertyType with DataType AddReferencesItem.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReferencesToAddNode() throws UaException;

  /** Asynchronous form of {@link #getReferencesToAddNode()}. */
  CompletableFuture<? extends PropertyType> getReferencesToAddNodeAsync();

  /**
   * Reads the Value of the ReferencesToAdd child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AddReferencesItem @Nullable [] readReferencesToAdd() throws UaException;

  /**
   * Writes the Value of the ReferencesToAdd child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReferencesToAdd(@Nullable AddReferencesItem @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readReferencesToAdd()}. */
  CompletableFuture<? extends @Nullable AddReferencesItem @Nullable []> readReferencesToAddAsync();

  /** Asynchronous form of {@link #writeReferencesToAdd}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReferencesToAddAsync(
      @Nullable AddReferencesItem @Nullable [] value);
}
