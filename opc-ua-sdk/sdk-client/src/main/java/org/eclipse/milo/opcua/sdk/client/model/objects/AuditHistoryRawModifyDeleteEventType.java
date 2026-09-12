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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditHistoryRawModifyDeleteEventType extends AuditHistoryDeleteEventType {
  QualifiedProperty<Boolean> IS_DELETE_MODIFIED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsDeleteModified",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> START_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> END_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DataValue[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsDeleteModified() throws UaException;

  /** Sets the existing node's local value. */
  void setIsDeleteModified(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIsDeleteModified() throws UaException;

  /** Writes the value remotely. */
  void writeIsDeleteModified(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIsDeleteModifiedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIsDeleteModifiedAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsDeleteModifiedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIsDeleteModifiedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime() throws UaException;

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartTime() throws UaException;

  /** Writes the value remotely. */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStartTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEndTime() throws UaException;

  /** Sets the existing node's local value. */
  void setEndTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readEndTime() throws UaException;

  /** Writes the value remotely. */
  void writeEndTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEndTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DataValue @Nullable [] getOldValues() throws UaException;

  /** Sets the existing node's local value. */
  void setOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataValue @Nullable [] readOldValues() throws UaException;

  /** Writes the value remotely. */
  void writeOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataValue @Nullable []> readOldValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable DataValue @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOldValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
