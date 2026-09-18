package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryRawModifyDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6">Model
 *     documentation</a>
 */
public interface AuditHistoryRawModifyDeleteEventType extends AuditHistoryDeleteEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3014L);

  QualifiedProperty<Boolean> IsDeleteModified_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IsDeleteModified",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> EndTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<DataValue[]> OldValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
          1,
          DataValue[].class);

  QualifiedProperty<DateTime> StartTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory IsDeleteModified child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIsDeleteModifiedNode() throws UaException;

  /** Asynchronous form of {@link #getIsDeleteModifiedNode()}. */
  CompletableFuture<? extends PropertyType> getIsDeleteModifiedNodeAsync();

  /**
   * Reads the Value of the IsDeleteModified child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIsDeleteModified() throws UaException;

  /**
   * Writes the Value of the IsDeleteModified child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIsDeleteModified(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIsDeleteModified()}. */
  CompletableFuture<? extends @Nullable Boolean> readIsDeleteModifiedAsync();

  /** Asynchronous form of {@link #writeIsDeleteModified}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIsDeleteModifiedAsync(@Nullable Boolean value);

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
