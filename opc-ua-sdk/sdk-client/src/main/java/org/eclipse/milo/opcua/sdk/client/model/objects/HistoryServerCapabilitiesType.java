package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the HistoryServerCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">Model
 *     documentation</a>
 */
public interface HistoryServerCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2330L);

  QualifiedProperty<Boolean> DeleteRawCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DeleteRawCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MaxReturnDataValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxReturnDataValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> InsertDataCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InsertDataCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MaxReturnEventValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxReturnEventValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> UpdateDataCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdateDataCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DeleteEventCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DeleteEventCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> InsertEventCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InsertEventCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ReplaceDataCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReplaceDataCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UpdateEventCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdateEventCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DeleteAtTimeCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DeleteAtTimeCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ReplaceEventCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReplaceEventCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ServerTimestampSupported_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerTimestampSupported",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> InsertAnnotationCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InsertAnnotationCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> AccessHistoryDataCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AccessHistoryDataCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> AccessHistoryEventsCapability_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AccessHistoryEventsCapability",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory AggregateFunctions child, a FolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /** Asynchronous form of {@link #getAggregateFunctionsNode()}. */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();

  /**
   * Resolves the mandatory DeleteRawCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDeleteRawCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteRawCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getDeleteRawCapabilityNodeAsync();

  /**
   * Reads the Value of the DeleteRawCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readDeleteRawCapability() throws UaException;

  /**
   * Writes the Value of the DeleteRawCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteRawCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readDeleteRawCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteRawCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeDeleteRawCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory MaxReturnDataValues child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxReturnDataValuesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxReturnDataValuesNode()}. */
  CompletableFuture<? extends PropertyType> getMaxReturnDataValuesNodeAsync();

  /**
   * Reads the Value of the MaxReturnDataValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxReturnDataValues() throws UaException;

  /**
   * Writes the Value of the MaxReturnDataValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxReturnDataValues(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxReturnDataValues()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReturnDataValuesAsync();

  /**
   * Asynchronous form of {@link #writeMaxReturnDataValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory InsertDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInsertDataCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getInsertDataCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getInsertDataCapabilityNodeAsync();

  /**
   * Reads the Value of the InsertDataCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readInsertDataCapability() throws UaException;

  /**
   * Writes the Value of the InsertDataCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInsertDataCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readInsertDataCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readInsertDataCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeInsertDataCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory MaxReturnEventValues child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxReturnEventValuesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxReturnEventValuesNode()}. */
  CompletableFuture<? extends PropertyType> getMaxReturnEventValuesNodeAsync();

  /**
   * Reads the Value of the MaxReturnEventValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxReturnEventValues() throws UaException;

  /**
   * Writes the Value of the MaxReturnEventValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxReturnEventValues(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxReturnEventValues()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReturnEventValuesAsync();

  /**
   * Asynchronous form of {@link #writeMaxReturnEventValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory UpdateDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUpdateDataCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateDataCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getUpdateDataCapabilityNodeAsync();

  /**
   * Reads the Value of the UpdateDataCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readUpdateDataCapability() throws UaException;

  /**
   * Writes the Value of the UpdateDataCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUpdateDataCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readUpdateDataCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readUpdateDataCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeUpdateDataCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory DeleteEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDeleteEventCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteEventCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getDeleteEventCapabilityNodeAsync();

  /**
   * Reads the Value of the DeleteEventCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readDeleteEventCapability() throws UaException;

  /**
   * Writes the Value of the DeleteEventCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteEventCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readDeleteEventCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteEventCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeDeleteEventCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory InsertEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInsertEventCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getInsertEventCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getInsertEventCapabilityNodeAsync();

  /**
   * Reads the Value of the InsertEventCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readInsertEventCapability() throws UaException;

  /**
   * Writes the Value of the InsertEventCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInsertEventCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readInsertEventCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readInsertEventCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeInsertEventCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory ReplaceDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReplaceDataCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getReplaceDataCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getReplaceDataCapabilityNodeAsync();

  /**
   * Reads the Value of the ReplaceDataCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readReplaceDataCapability() throws UaException;

  /**
   * Writes the Value of the ReplaceDataCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReplaceDataCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readReplaceDataCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readReplaceDataCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeReplaceDataCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory UpdateEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUpdateEventCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateEventCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getUpdateEventCapabilityNodeAsync();

  /**
   * Reads the Value of the UpdateEventCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readUpdateEventCapability() throws UaException;

  /**
   * Writes the Value of the UpdateEventCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUpdateEventCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readUpdateEventCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readUpdateEventCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeUpdateEventCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory DeleteAtTimeCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDeleteAtTimeCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteAtTimeCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getDeleteAtTimeCapabilityNodeAsync();

  /**
   * Reads the Value of the DeleteAtTimeCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readDeleteAtTimeCapability() throws UaException;

  /**
   * Writes the Value of the DeleteAtTimeCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteAtTimeCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readDeleteAtTimeCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteAtTimeCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeDeleteAtTimeCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory ReplaceEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReplaceEventCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getReplaceEventCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getReplaceEventCapabilityNodeAsync();

  /**
   * Reads the Value of the ReplaceEventCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readReplaceEventCapability() throws UaException;

  /**
   * Writes the Value of the ReplaceEventCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReplaceEventCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readReplaceEventCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readReplaceEventCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeReplaceEventCapability}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the optional ServerTimestampSupported child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getServerTimestampSupportedNode() throws UaException;

  /** Asynchronous form of {@link #getServerTimestampSupportedNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getServerTimestampSupportedNodeAsync();

  /**
   * Reads the Value of the ServerTimestampSupported child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readServerTimestampSupported() throws UaException;

  /**
   * Writes the Value of the ServerTimestampSupported child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readServerTimestampSupported()}. */
  CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync();

  /**
   * Asynchronous form of {@link #writeServerTimestampSupported}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory InsertAnnotationCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInsertAnnotationCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getInsertAnnotationCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getInsertAnnotationCapabilityNodeAsync();

  /**
   * Reads the Value of the InsertAnnotationCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readInsertAnnotationCapability() throws UaException;

  /**
   * Writes the Value of the InsertAnnotationCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInsertAnnotationCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readInsertAnnotationCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readInsertAnnotationCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeInsertAnnotationCapability}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory AccessHistoryDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAccessHistoryDataCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getAccessHistoryDataCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getAccessHistoryDataCapabilityNodeAsync();

  /**
   * Reads the Value of the AccessHistoryDataCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readAccessHistoryDataCapability() throws UaException;

  /**
   * Writes the Value of the AccessHistoryDataCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAccessHistoryDataCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readAccessHistoryDataCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readAccessHistoryDataCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeAccessHistoryDataCapability}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory AccessHistoryEventsCapability child, a PropertyType with DataType
   * Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAccessHistoryEventsCapabilityNode() throws UaException;

  /** Asynchronous form of {@link #getAccessHistoryEventsCapabilityNode()}. */
  CompletableFuture<? extends PropertyType> getAccessHistoryEventsCapabilityNodeAsync();

  /**
   * Reads the Value of the AccessHistoryEventsCapability child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readAccessHistoryEventsCapability() throws UaException;

  /**
   * Writes the Value of the AccessHistoryEventsCapability child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readAccessHistoryEventsCapability()}. */
  CompletableFuture<? extends @Nullable Boolean> readAccessHistoryEventsCapabilityAsync();

  /**
   * Asynchronous form of {@link #writeAccessHistoryEventsCapability}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(@Nullable Boolean value);
}
