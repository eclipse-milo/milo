package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditDeleteReferencesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.23">Model
 *     documentation</a>
 */
public interface AuditDeleteReferencesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2097L);

  QualifiedProperty<DeleteReferencesItem[]> ReferencesToDelete_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReferencesToDelete",
          ExpandedNodeId.of(Namespaces.OPC_UA, 385L),
          1,
          DeleteReferencesItem[].class);

  /**
   * Resolves the mandatory ReferencesToDelete child, a PropertyType with DataType
   * DeleteReferencesItem.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReferencesToDeleteNode() throws UaException;

  /** Asynchronous form of {@link #getReferencesToDeleteNode()}. */
  CompletableFuture<? extends PropertyType> getReferencesToDeleteNodeAsync();

  /**
   * Reads the Value of the ReferencesToDelete child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DeleteReferencesItem @Nullable [] readReferencesToDelete() throws UaException;

  /**
   * Writes the Value of the ReferencesToDelete child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReferencesToDelete(@Nullable DeleteReferencesItem @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readReferencesToDelete()}. */
  CompletableFuture<? extends @Nullable DeleteReferencesItem @Nullable []>
      readReferencesToDeleteAsync();

  /** Asynchronous form of {@link #writeReferencesToDelete}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReferencesToDeleteAsync(
      @Nullable DeleteReferencesItem @Nullable [] value);
}
