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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.2</a>
 */
public interface OffNormalAlarmType extends DiscreteAlarmType {
  QualifiedProperty<NodeId> NORMAL_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NormalState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /**
   * Get the local value of the NormalState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NormalState Node.
   * @throws UaException if an error occurs creating or getting the NormalState Node.
   */
  NodeId getNormalState() throws UaException;

  /**
   * Set the local value of the NormalState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NormalState Node.
   * @throws UaException if an error occurs creating or getting the NormalState Node.
   */
  void setNormalState(NodeId value) throws UaException;

  /**
   * Read the value of the NormalState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readNormalState() throws UaException;

  /**
   * Write a new value for the NormalState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNormalState(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNormalState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readNormalStateAsync();

  /**
   * An asynchronous implementation of {@link #writeNormalState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNormalStateAsync(NodeId value);

  /**
   * Get the NormalState {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NormalState {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNormalStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNormalStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNormalStateNodeAsync();
}
