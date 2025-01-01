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
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.22">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.22</a>
 */
public interface AuditAddReferencesEventType extends AuditNodeManagementEventType {
  QualifiedProperty<AddReferencesItem[]> REFERENCES_TO_ADD =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReferencesToAdd",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=379"),
          1,
          AddReferencesItem[].class);

  /**
   * Get the local value of the ReferencesToAdd Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReferencesToAdd Node.
   * @throws UaException if an error occurs creating or getting the ReferencesToAdd Node.
   */
  AddReferencesItem[] getReferencesToAdd() throws UaException;

  /**
   * Set the local value of the ReferencesToAdd Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReferencesToAdd Node.
   * @throws UaException if an error occurs creating or getting the ReferencesToAdd Node.
   */
  void setReferencesToAdd(AddReferencesItem[] value) throws UaException;

  /**
   * Read the value of the ReferencesToAdd Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link AddReferencesItem[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  AddReferencesItem[] readReferencesToAdd() throws UaException;

  /**
   * Write a new value for the ReferencesToAdd Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link AddReferencesItem[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReferencesToAdd(AddReferencesItem[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReferencesToAdd}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends AddReferencesItem[]> readReferencesToAddAsync();

  /**
   * An asynchronous implementation of {@link #writeReferencesToAdd}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReferencesToAddAsync(AddReferencesItem[] value);

  /**
   * Get the ReferencesToAdd {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReferencesToAdd {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReferencesToAddNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReferencesToAddNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReferencesToAddNodeAsync();
}
