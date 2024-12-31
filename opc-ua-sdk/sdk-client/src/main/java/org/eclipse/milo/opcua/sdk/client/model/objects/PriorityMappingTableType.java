/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2</a>
 */
public interface PriorityMappingTableType extends BaseObjectType {
  QualifiedProperty<PriorityMappingEntryType[]> PRIORITY_MAPPPING_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PriorityMapppingEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25220"),
          1,
          PriorityMappingEntryType[].class);

  /**
   * Get the local value of the PriorityMapppingEntries Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PriorityMapppingEntries Node.
   * @throws UaException if an error occurs creating or getting the PriorityMapppingEntries Node.
   */
  PriorityMappingEntryType[] getPriorityMapppingEntries() throws UaException;

  /**
   * Set the local value of the PriorityMapppingEntries Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PriorityMapppingEntries Node.
   * @throws UaException if an error occurs creating or getting the PriorityMapppingEntries Node.
   */
  void setPriorityMapppingEntries(PriorityMappingEntryType[] value) throws UaException;

  /**
   * Read the value of the PriorityMapppingEntries Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link PriorityMappingEntryType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PriorityMappingEntryType[] readPriorityMapppingEntries() throws UaException;

  /**
   * Write a new value for the PriorityMapppingEntries Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link PriorityMappingEntryType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriorityMapppingEntries(PriorityMappingEntryType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriorityMapppingEntries}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PriorityMappingEntryType[]> readPriorityMapppingEntriesAsync();

  /**
   * An asynchronous implementation of {@link #writePriorityMapppingEntries}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityMapppingEntriesAsync(PriorityMappingEntryType[] value);

  /**
   * Get the PriorityMapppingEntries {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PriorityMapppingEntries {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPriorityMapppingEntriesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityMapppingEntriesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPriorityMapppingEntriesNodeAsync();
}
