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
 * Client API for the AuditHistoryAtTimeDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7">Model
 *     documentation</a>
 */
public interface AuditHistoryAtTimeDeleteEventType extends AuditHistoryDeleteEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3019L);

  QualifiedProperty<DateTime[]> ReqTimes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReqTimes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          1,
          DateTime[].class);

  QualifiedProperty<DataValue[]> OldValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
          1,
          DataValue[].class);

  /**
   * Resolves the mandatory ReqTimes child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReqTimesNode() throws UaException;

  /** Asynchronous form of {@link #getReqTimesNode()}. */
  CompletableFuture<? extends PropertyType> getReqTimesNodeAsync();

  /**
   * Reads the Value of the ReqTimes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  DateTime @Nullable [] readReqTimes() throws UaException;

  /**
   * Writes the Value of the ReqTimes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReqTimes(DateTime @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readReqTimes()}. */
  CompletableFuture<? extends DateTime @Nullable []> readReqTimesAsync();

  /** Asynchronous form of {@link #writeReqTimes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReqTimesAsync(DateTime @Nullable [] value);

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
