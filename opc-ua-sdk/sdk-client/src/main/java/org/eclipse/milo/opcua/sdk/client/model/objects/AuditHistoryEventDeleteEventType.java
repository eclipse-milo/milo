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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditHistoryEventDeleteEventType extends AuditHistoryDeleteEventType {
  QualifiedProperty<ByteString[]> EVENT_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          1,
          ByteString[].class);

  QualifiedProperty<HistoryEventFieldList> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=920"),
          -1,
          HistoryEventFieldList.class);

  /** Gets the existing node's local value. */
  @Nullable ByteString @Nullable [] getEventIds() throws UaException;

  /** Sets the existing node's local value. */
  void setEventIds(@Nullable ByteString @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString @Nullable [] readEventIds() throws UaException;

  /** Writes the value remotely. */
  void writeEventIds(@Nullable ByteString @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString @Nullable []> readEventIdsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEventIdsAsync(@Nullable ByteString @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEventIdsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEventIdsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable HistoryEventFieldList getOldValues() throws UaException;

  /** Sets the existing node's local value. */
  void setOldValues(@Nullable HistoryEventFieldList value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable HistoryEventFieldList readOldValues() throws UaException;

  /** Writes the value remotely. */
  void writeOldValues(@Nullable HistoryEventFieldList value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable HistoryEventFieldList> readOldValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable HistoryEventFieldList value);

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
