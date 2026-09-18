package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryEventUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.2">Model
 *     documentation</a>
 */
public interface AuditHistoryEventUpdateEventType extends AuditHistoryUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2999L);

  QualifiedProperty<NodeId> UpdatedNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdatedNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<PerformUpdateType> PerformInsertReplace_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PerformInsertReplace",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11293L),
          -1,
          PerformUpdateType.class);

  QualifiedProperty<EventFilter> Filter_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Filter",
          ExpandedNodeId.of(Namespaces.OPC_UA, 725L),
          -1,
          EventFilter.class);

  QualifiedProperty<HistoryEventFieldList[]> NewValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NewValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 920L),
          1,
          HistoryEventFieldList[].class);

  QualifiedProperty<HistoryEventFieldList[]> OldValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 920L),
          1,
          HistoryEventFieldList[].class);

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

  /**
   * Resolves the mandatory PerformInsertReplace child, a PropertyType with DataType
   * PerformUpdateType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPerformInsertReplaceNode() throws UaException;

  /** Asynchronous form of {@link #getPerformInsertReplaceNode()}. */
  CompletableFuture<? extends PropertyType> getPerformInsertReplaceNodeAsync();

  /**
   * Reads the Value of the PerformInsertReplace child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PerformUpdateType readPerformInsertReplace() throws UaException;

  /**
   * Writes the Value of the PerformInsertReplace child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePerformInsertReplace(@Nullable PerformUpdateType value) throws UaException;

  /** Asynchronous form of {@link #readPerformInsertReplace()}. */
  CompletableFuture<? extends @Nullable PerformUpdateType> readPerformInsertReplaceAsync();

  /**
   * Asynchronous form of {@link #writePerformInsertReplace}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writePerformInsertReplaceAsync(@Nullable PerformUpdateType value);

  /**
   * Resolves the mandatory Filter child, a PropertyType with DataType EventFilter.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getFilterNode() throws UaException;

  /** Asynchronous form of {@link #getFilterNode()}. */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();

  /**
   * Reads the Value of the Filter child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EventFilter readFilter() throws UaException;

  /**
   * Writes the Value of the Filter child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFilter(@Nullable EventFilter value) throws UaException;

  /** Asynchronous form of {@link #readFilter()}. */
  CompletableFuture<? extends @Nullable EventFilter> readFilterAsync();

  /** Asynchronous form of {@link #writeFilter}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFilterAsync(@Nullable EventFilter value);

  /**
   * Resolves the mandatory NewValues child, a PropertyType with DataType HistoryEventFieldList.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNewValuesNode() throws UaException;

  /** Asynchronous form of {@link #getNewValuesNode()}. */
  CompletableFuture<? extends PropertyType> getNewValuesNodeAsync();

  /**
   * Reads the Value of the NewValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable HistoryEventFieldList @Nullable [] readNewValues() throws UaException;

  /**
   * Writes the Value of the NewValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNewValues(@Nullable HistoryEventFieldList @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readNewValues()}. */
  CompletableFuture<? extends @Nullable HistoryEventFieldList @Nullable []> readNewValuesAsync();

  /** Asynchronous form of {@link #writeNewValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNewValuesAsync(
      @Nullable HistoryEventFieldList @Nullable [] value);

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
  @Nullable HistoryEventFieldList @Nullable [] readOldValues() throws UaException;

  /**
   * Writes the Value of the OldValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldValues(@Nullable HistoryEventFieldList @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readOldValues()}. */
  CompletableFuture<? extends @Nullable HistoryEventFieldList @Nullable []> readOldValuesAsync();

  /** Asynchronous form of {@link #writeOldValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldValuesAsync(
      @Nullable HistoryEventFieldList @Nullable [] value);
}
