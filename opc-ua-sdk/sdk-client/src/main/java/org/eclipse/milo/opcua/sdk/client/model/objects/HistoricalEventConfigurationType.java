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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface HistoricalEventConfigurationType extends BaseObjectType {
  QualifiedProperty<DateTime> START_OF_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> START_OF_ONLINE_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfOnlineArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<SimpleAttributeOperand[]> SORT_BY_EVENT_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SortByEventFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfArchive() throws UaException;

  /** Sets the existing node's local value. */
  void setStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartOfArchive() throws UaException;

  /** Writes the value remotely. */
  void writeStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfArchiveNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfArchiveNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfOnlineArchive() throws UaException;

  /** Sets the existing node's local value. */
  void setStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartOfOnlineArchive() throws UaException;

  /** Writes the value remotely. */
  void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfOnlineArchiveNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SimpleAttributeOperand @Nullable [] getSortByEventFields() throws UaException;

  /** Sets the existing node's local value. */
  void setSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SimpleAttributeOperand @Nullable [] readSortByEventFields() throws UaException;

  /** Writes the value remotely. */
  void writeSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSortByEventFieldsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSortByEventFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSortByEventFieldsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSortByEventFieldsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getEventTypesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FolderType> getEventTypesNodeAsync();
}
