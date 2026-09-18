package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryBulkInsertEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.10">Model
 *     documentation</a>
 */
public interface AuditHistoryBulkInsertEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32803L);

  QualifiedProperty<NodeId> UpdatedNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdatedNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> EndTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> StartTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

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
   * Resolves the mandatory EndTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEndTimeNode() throws UaException;

  /** Asynchronous form of {@link #getEndTimeNode()}. */
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

  /**
   * Reads the Value of the EndTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readEndTime() throws UaException;

  /**
   * Writes the Value of the EndTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readEndTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync();

  /** Asynchronous form of {@link #writeEndTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory StartTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStartTimeNode() throws UaException;

  /** Asynchronous form of {@link #getStartTimeNode()}. */
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

  /**
   * Reads the Value of the StartTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartTime() throws UaException;

  /**
   * Writes the Value of the StartTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Asynchronous form of {@link #writeStartTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);
}
