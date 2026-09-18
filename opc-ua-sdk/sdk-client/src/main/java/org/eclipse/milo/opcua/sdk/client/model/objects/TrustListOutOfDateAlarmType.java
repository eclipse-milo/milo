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
 * Client API for the TrustListOutOfDateAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">Model
 *     documentation</a>
 */
public interface TrustListOutOfDateAlarmType extends SystemOffNormalAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19297L);

  QualifiedProperty<NodeId> TrustListId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TrustListId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> LastUpdateTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastUpdateTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UpdateFrequency_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdateFrequency",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  /**
   * Resolves the mandatory TrustListId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTrustListIdNode() throws UaException;

  /** Asynchronous form of {@link #getTrustListIdNode()}. */
  CompletableFuture<? extends PropertyType> getTrustListIdNodeAsync();

  /**
   * Reads the Value of the TrustListId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readTrustListId() throws UaException;

  /**
   * Writes the Value of the TrustListId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTrustListId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readTrustListId()}. */
  CompletableFuture<? extends @Nullable NodeId> readTrustListIdAsync();

  /** Asynchronous form of {@link #writeTrustListId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTrustListIdAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastUpdateTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /**
   * Reads the Value of the LastUpdateTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /**
   * Writes the Value of the LastUpdateTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastUpdateTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Asynchronous form of {@link #writeLastUpdateTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory UpdateFrequency child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUpdateFrequencyNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateFrequencyNode()}. */
  CompletableFuture<? extends PropertyType> getUpdateFrequencyNodeAsync();

  /**
   * Reads the Value of the UpdateFrequency child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readUpdateFrequency() throws UaException;

  /**
   * Writes the Value of the UpdateFrequency child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUpdateFrequency(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readUpdateFrequency()}. */
  CompletableFuture<? extends @Nullable Double> readUpdateFrequencyAsync();

  /** Asynchronous form of {@link #writeUpdateFrequency}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUpdateFrequencyAsync(@Nullable Double value);
}
