package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryValueUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.3">Model
 *     documentation</a>
 */
public interface AuditHistoryValueUpdateEventType extends AuditHistoryUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3006L);

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

  QualifiedProperty<DataValue[]> NewValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NewValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
          1,
          DataValue[].class);

  QualifiedProperty<DataValue[]> OldValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
          1,
          DataValue[].class);

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
   * Resolves the mandatory NewValues child, a PropertyType with DataType DataValue.
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
  DataValue @Nullable [] readNewValues() throws UaException;

  /**
   * Writes the Value of the NewValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNewValues(DataValue @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readNewValues()}. */
  CompletableFuture<? extends DataValue @Nullable []> readNewValuesAsync();

  /** Asynchronous form of {@link #writeNewValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNewValuesAsync(DataValue @Nullable [] value);

  /**
   * Resolves the mandatory OldValues child, a PropertyType with DataType DataValue.
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
  DataValue @Nullable [] readOldValues() throws UaException;

  /**
   * Writes the Value of the OldValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldValues(DataValue @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readOldValues()}. */
  CompletableFuture<? extends DataValue @Nullable []> readOldValuesAsync();

  /** Asynchronous form of {@link #writeOldValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldValuesAsync(DataValue @Nullable [] value);
}
