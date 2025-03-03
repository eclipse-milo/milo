/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18</a>
 */
public interface SelectionListType extends BaseDataVariableType {
  QualifiedProperty<Object[]> SELECTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Selections",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<LocalizedText[]> SELECTION_DESCRIPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectionDescriptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Boolean> RESTRICT_TO_LIST =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RestrictToList",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the Selections Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Selections Node.
   * @throws UaException if an error occurs creating or getting the Selections Node.
   */
  Object[] getSelections() throws UaException;

  /**
   * Set the local value of the Selections Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Selections Node.
   * @throws UaException if an error occurs creating or getting the Selections Node.
   */
  void setSelections(Object[] value) throws UaException;

  /**
   * Read the value of the Selections Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readSelections() throws UaException;

  /**
   * Write a new value for the Selections Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSelections(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSelections}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readSelectionsAsync();

  /**
   * An asynchronous implementation of {@link #writeSelections}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSelectionsAsync(Object[] value);

  /**
   * Get the Selections {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Selections {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSelectionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSelectionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSelectionsNodeAsync();

  /**
   * Get the local value of the SelectionDescriptions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SelectionDescriptions Node.
   * @throws UaException if an error occurs creating or getting the SelectionDescriptions Node.
   */
  LocalizedText[] getSelectionDescriptions() throws UaException;

  /**
   * Set the local value of the SelectionDescriptions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SelectionDescriptions Node.
   * @throws UaException if an error occurs creating or getting the SelectionDescriptions Node.
   */
  void setSelectionDescriptions(LocalizedText[] value) throws UaException;

  /**
   * Read the value of the SelectionDescriptions Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText[] readSelectionDescriptions() throws UaException;

  /**
   * Write a new value for the SelectionDescriptions Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link LocalizedText[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSelectionDescriptions(LocalizedText[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSelectionDescriptions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText[]> readSelectionDescriptionsAsync();

  /**
   * An asynchronous implementation of {@link #writeSelectionDescriptions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSelectionDescriptionsAsync(LocalizedText[] value);

  /**
   * Get the SelectionDescriptions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SelectionDescriptions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSelectionDescriptionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSelectionDescriptionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSelectionDescriptionsNodeAsync();

  /**
   * Get the local value of the RestrictToList Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RestrictToList Node.
   * @throws UaException if an error occurs creating or getting the RestrictToList Node.
   */
  Boolean getRestrictToList() throws UaException;

  /**
   * Set the local value of the RestrictToList Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RestrictToList Node.
   * @throws UaException if an error occurs creating or getting the RestrictToList Node.
   */
  void setRestrictToList(Boolean value) throws UaException;

  /**
   * Read the value of the RestrictToList Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readRestrictToList() throws UaException;

  /**
   * Write a new value for the RestrictToList Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRestrictToList(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRestrictToList}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readRestrictToListAsync();

  /**
   * An asynchronous implementation of {@link #writeRestrictToList}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRestrictToListAsync(Boolean value);

  /**
   * Get the RestrictToList {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RestrictToList {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRestrictToListNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRestrictToListNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRestrictToListNodeAsync();
}
