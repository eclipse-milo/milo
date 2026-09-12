/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface HistoryServerCapabilitiesType extends BaseObjectType {
  QualifiedProperty<Boolean> ACCESS_HISTORY_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ACCESS_HISTORY_EVENTS_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryEventsCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MAX_RETURN_DATA_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnDataValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_RETURN_EVENT_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnEventValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> INSERT_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_RAW_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteRawCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_AT_TIME_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteAtTimeCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_ANNOTATION_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertAnnotationCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SERVER_TIMESTAMP_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerTimestampSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getAccessHistoryDataCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setAccessHistoryDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readAccessHistoryDataCapability() throws UaException;

  /** Writes the value remotely. */
  void writeAccessHistoryDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readAccessHistoryDataCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAccessHistoryDataCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAccessHistoryDataCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAccessHistoryEventsCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readAccessHistoryEventsCapability() throws UaException;

  /** Writes the value remotely. */
  void writeAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readAccessHistoryEventsCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAccessHistoryEventsCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAccessHistoryEventsCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReturnDataValues() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxReturnDataValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxReturnDataValues() throws UaException;

  /** Writes the value remotely. */
  void writeMaxReturnDataValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReturnDataValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReturnDataValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxReturnDataValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReturnEventValues() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxReturnEventValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxReturnEventValues() throws UaException;

  /** Writes the value remotely. */
  void writeMaxReturnEventValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReturnEventValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReturnEventValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxReturnEventValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertDataCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setInsertDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readInsertDataCapability() throws UaException;

  /** Writes the value remotely. */
  void writeInsertDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readInsertDataCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertDataCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInsertDataCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getReplaceDataCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setReplaceDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readReplaceDataCapability() throws UaException;

  /** Writes the value remotely. */
  void writeReplaceDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readReplaceDataCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReplaceDataCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReplaceDataCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUpdateDataCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setUpdateDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readUpdateDataCapability() throws UaException;

  /** Writes the value remotely. */
  void writeUpdateDataCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readUpdateDataCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateDataCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUpdateDataCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteRawCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteRawCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readDeleteRawCapability() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteRawCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteRawCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteRawCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDeleteRawCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteAtTimeCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteAtTimeCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readDeleteAtTimeCapability() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteAtTimeCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteAtTimeCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteAtTimeCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDeleteAtTimeCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertEventCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setInsertEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readInsertEventCapability() throws UaException;

  /** Writes the value remotely. */
  void writeInsertEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readInsertEventCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertEventCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInsertEventCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getReplaceEventCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setReplaceEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readReplaceEventCapability() throws UaException;

  /** Writes the value remotely. */
  void writeReplaceEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readReplaceEventCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReplaceEventCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReplaceEventCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUpdateEventCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setUpdateEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readUpdateEventCapability() throws UaException;

  /** Writes the value remotely. */
  void writeUpdateEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readUpdateEventCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateEventCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUpdateEventCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteEventCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setDeleteEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readDeleteEventCapability() throws UaException;

  /** Writes the value remotely. */
  void writeDeleteEventCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readDeleteEventCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteEventCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDeleteEventCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertAnnotationCapability() throws UaException;

  /** Sets the existing node's local value. */
  void setInsertAnnotationCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readInsertAnnotationCapability() throws UaException;

  /** Writes the value remotely. */
  void writeInsertAnnotationCapability(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readInsertAnnotationCapabilityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertAnnotationCapabilityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInsertAnnotationCapabilityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getServerTimestampSupported() throws UaException;

  /** Sets the existing node's local value. */
  void setServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readServerTimestampSupported() throws UaException;

  /** Writes the value remotely. */
  void writeServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerTimestampSupportedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getServerTimestampSupportedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();
}
