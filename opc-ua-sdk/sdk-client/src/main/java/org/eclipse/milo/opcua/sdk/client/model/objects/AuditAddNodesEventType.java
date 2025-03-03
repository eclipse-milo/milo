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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.20">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.20</a>
 */
public interface AuditAddNodesEventType extends AuditNodeManagementEventType {
  QualifiedProperty<AddNodesItem[]> NODES_TO_ADD =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NodesToAdd",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=376"),
          1,
          AddNodesItem[].class);

  /**
   * Get the local value of the NodesToAdd Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NodesToAdd Node.
   * @throws UaException if an error occurs creating or getting the NodesToAdd Node.
   */
  AddNodesItem[] getNodesToAdd() throws UaException;

  /**
   * Set the local value of the NodesToAdd Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NodesToAdd Node.
   * @throws UaException if an error occurs creating or getting the NodesToAdd Node.
   */
  void setNodesToAdd(AddNodesItem[] value) throws UaException;

  /**
   * Read the value of the NodesToAdd Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link AddNodesItem[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  AddNodesItem[] readNodesToAdd() throws UaException;

  /**
   * Write a new value for the NodesToAdd Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link AddNodesItem[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNodesToAdd(AddNodesItem[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNodesToAdd}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends AddNodesItem[]> readNodesToAddAsync();

  /**
   * An asynchronous implementation of {@link #writeNodesToAdd}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNodesToAddAsync(AddNodesItem[] value);

  /**
   * Get the NodesToAdd {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NodesToAdd {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNodesToAddNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNodesToAddNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNodesToAddNodeAsync();
}
