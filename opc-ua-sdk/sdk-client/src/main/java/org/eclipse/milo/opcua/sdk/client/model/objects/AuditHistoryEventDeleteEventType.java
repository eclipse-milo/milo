package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryEventDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8">Model
 *     documentation</a>
 */
public interface AuditHistoryEventDeleteEventType extends AuditHistoryDeleteEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3022L);

  QualifiedProperty<ByteString[]> EventIds_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EventIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          1,
          ByteString[].class);

  QualifiedProperty<HistoryEventFieldList> OldValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 920L),
          -1,
          HistoryEventFieldList.class);

  /**
   * Resolves the mandatory EventIds child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEventIdsNode() throws UaException;

  /** Asynchronous form of {@link #getEventIdsNode()}. */
  CompletableFuture<? extends PropertyType> getEventIdsNodeAsync();

  /**
   * Reads the Value of the EventIds child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  ByteString @Nullable [] readEventIds() throws UaException;

  /**
   * Writes the Value of the EventIds child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventIds(ByteString @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readEventIds()}. */
  CompletableFuture<? extends ByteString @Nullable []> readEventIdsAsync();

  /** Asynchronous form of {@link #writeEventIds}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEventIdsAsync(ByteString @Nullable [] value);

  /**
   * Resolves the mandatory OldValues child, a PropertyType with DataType HistoryEventFieldList.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOldValuesNode() throws UaException;

  /** Asynchronous form of {@link #getOldValuesNode()}. */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();

  /**
   * Reads the Value of the OldValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable HistoryEventFieldList readOldValues() throws UaException;

  /**
   * Writes the Value of the OldValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldValues(@Nullable HistoryEventFieldList value) throws UaException;

  /** Asynchronous form of {@link #readOldValues()}. */
  CompletableFuture<? extends @Nullable HistoryEventFieldList> readOldValuesAsync();

  /** Asynchronous form of {@link #writeOldValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable HistoryEventFieldList value);
}
