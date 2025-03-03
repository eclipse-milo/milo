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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3</a>
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

  /**
   * Get the local value of the StartOfArchive Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartOfArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfArchive Node.
   */
  DateTime getStartOfArchive() throws UaException;

  /**
   * Set the local value of the StartOfArchive Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartOfArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfArchive Node.
   */
  void setStartOfArchive(DateTime value) throws UaException;

  /**
   * Read the value of the StartOfArchive Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartOfArchive() throws UaException;

  /**
   * Write a new value for the StartOfArchive Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartOfArchive(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartOfArchive}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartOfArchiveAsync();

  /**
   * An asynchronous implementation of {@link #writeStartOfArchive}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(DateTime value);

  /**
   * Get the StartOfArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartOfArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStartOfArchiveNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartOfArchiveNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStartOfArchiveNodeAsync();

  /**
   * Get the local value of the StartOfOnlineArchive Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartOfOnlineArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfOnlineArchive Node.
   */
  DateTime getStartOfOnlineArchive() throws UaException;

  /**
   * Set the local value of the StartOfOnlineArchive Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartOfOnlineArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfOnlineArchive Node.
   */
  void setStartOfOnlineArchive(DateTime value) throws UaException;

  /**
   * Read the value of the StartOfOnlineArchive Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartOfOnlineArchive() throws UaException;

  /**
   * Write a new value for the StartOfOnlineArchive Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartOfOnlineArchive(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartOfOnlineArchive}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartOfOnlineArchiveAsync();

  /**
   * An asynchronous implementation of {@link #writeStartOfOnlineArchive}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(DateTime value);

  /**
   * Get the StartOfOnlineArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartOfOnlineArchive {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartOfOnlineArchiveNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStartOfOnlineArchiveNodeAsync();

  /**
   * Get the local value of the SortByEventFields Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SortByEventFields Node.
   * @throws UaException if an error occurs creating or getting the SortByEventFields Node.
   */
  SimpleAttributeOperand[] getSortByEventFields() throws UaException;

  /**
   * Set the local value of the SortByEventFields Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SortByEventFields Node.
   * @throws UaException if an error occurs creating or getting the SortByEventFields Node.
   */
  void setSortByEventFields(SimpleAttributeOperand[] value) throws UaException;

  /**
   * Read the value of the SortByEventFields Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link SimpleAttributeOperand[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SimpleAttributeOperand[] readSortByEventFields() throws UaException;

  /**
   * Write a new value for the SortByEventFields Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link SimpleAttributeOperand[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSortByEventFields(SimpleAttributeOperand[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSortByEventFields}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SimpleAttributeOperand[]> readSortByEventFieldsAsync();

  /**
   * An asynchronous implementation of {@link #writeSortByEventFields}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSortByEventFieldsAsync(SimpleAttributeOperand[] value);

  /**
   * Get the SortByEventFields {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SortByEventFields {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSortByEventFieldsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSortByEventFieldsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSortByEventFieldsNodeAsync();

  /**
   * Get the EventTypes {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventTypes {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getEventTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getEventTypesNodeAsync();
}
