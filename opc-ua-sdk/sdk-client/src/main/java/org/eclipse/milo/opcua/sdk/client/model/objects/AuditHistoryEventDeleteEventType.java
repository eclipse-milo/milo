/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8</a>
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

  /**
   * Get the local value of the EventIds Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EventIds Node.
   * @throws UaException if an error occurs creating or getting the EventIds Node.
   */
  ByteString[] getEventIds() throws UaException;

  /**
   * Set the local value of the EventIds Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EventIds Node.
   * @throws UaException if an error occurs creating or getting the EventIds Node.
   */
  void setEventIds(ByteString[] value) throws UaException;

  /**
   * Read the value of the EventIds Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ByteString[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString[] readEventIds() throws UaException;

  /**
   * Write a new value for the EventIds Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ByteString[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEventIds(ByteString[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEventIds}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString[]> readEventIdsAsync();

  /**
   * An asynchronous implementation of {@link #writeEventIds}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEventIdsAsync(ByteString[] value);

  /**
   * Get the EventIds {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventIds {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEventIdsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventIdsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEventIdsNodeAsync();

  /**
   * Get the local value of the OldValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OldValues Node.
   * @throws UaException if an error occurs creating or getting the OldValues Node.
   */
  HistoryEventFieldList getOldValues() throws UaException;

  /**
   * Set the local value of the OldValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OldValues Node.
   * @throws UaException if an error occurs creating or getting the OldValues Node.
   */
  void setOldValues(HistoryEventFieldList value) throws UaException;

  /**
   * Read the value of the OldValues Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link HistoryEventFieldList} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  HistoryEventFieldList readOldValues() throws UaException;

  /**
   * Write a new value for the OldValues Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link HistoryEventFieldList} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOldValues(HistoryEventFieldList value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOldValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends HistoryEventFieldList> readOldValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeOldValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOldValuesAsync(HistoryEventFieldList value);

  /**
   * Get the OldValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OldValues {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOldValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOldValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
