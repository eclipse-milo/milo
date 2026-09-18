package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the HistoricalEventConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">Model
 *     documentation</a>
 */
public interface HistoricalEventConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32621L);

  QualifiedProperty<DateTime> StartOfArchive_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartOfArchive",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<SimpleAttributeOperand[]> SortByEventFields_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SortByEventFields",
          ExpandedNodeId.of(Namespaces.OPC_UA, 601L),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<DateTime> StartOfOnlineArchive_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartOfOnlineArchive",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory EventTypes child, a FolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderType getEventTypesNode() throws UaException;

  /** Asynchronous form of {@link #getEventTypesNode()}. */
  CompletableFuture<? extends FolderType> getEventTypesNodeAsync();

  /**
   * Resolves the optional StartOfArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getStartOfArchiveNode() throws UaException;

  /** Asynchronous form of {@link #getStartOfArchiveNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfArchiveNodeAsync();

  /**
   * Reads the Value of the StartOfArchive child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartOfArchive() throws UaException;

  /**
   * Writes the Value of the StartOfArchive child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartOfArchive()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync();

  /** Asynchronous form of {@link #writeStartOfArchive}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value);

  /**
   * Resolves the optional SortByEventFields child, a PropertyType with DataType
   * SimpleAttributeOperand.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSortByEventFieldsNode() throws UaException;

  /** Asynchronous form of {@link #getSortByEventFieldsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSortByEventFieldsNodeAsync();

  /**
   * Reads the Value of the SortByEventFields child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SimpleAttributeOperand @Nullable [] readSortByEventFields() throws UaException;

  /**
   * Writes the Value of the SortByEventFields child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readSortByEventFields()}. */
  CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSortByEventFieldsAsync();

  /** Asynchronous form of {@link #writeSortByEventFields}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSortByEventFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Resolves the optional StartOfOnlineArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /** Asynchronous form of {@link #getStartOfOnlineArchiveNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfOnlineArchiveNodeAsync();

  /**
   * Reads the Value of the StartOfOnlineArchive child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartOfOnlineArchive() throws UaException;

  /**
   * Writes the Value of the StartOfOnlineArchive child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartOfOnlineArchive()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync();

  /**
   * Asynchronous form of {@link #writeStartOfOnlineArchive}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value);
}
