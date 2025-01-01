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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.3</a>
 */
public interface ExclusiveDeviationAlarmType extends ExclusiveLimitAlarmType {
  QualifiedProperty<NodeId> SETPOINT_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SetpointNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> BASE_SETPOINT_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseSetpointNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /**
   * Get the local value of the SetpointNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SetpointNode Node.
   * @throws UaException if an error occurs creating or getting the SetpointNode Node.
   */
  NodeId getSetpointNode() throws UaException;

  /**
   * Set the local value of the SetpointNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SetpointNode Node.
   * @throws UaException if an error occurs creating or getting the SetpointNode Node.
   */
  void setSetpointNode(NodeId value) throws UaException;

  /**
   * Read the value of the SetpointNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readSetpointNode() throws UaException;

  /**
   * Write a new value for the SetpointNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSetpointNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSetpointNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readSetpointNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeSetpointNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSetpointNodeAsync(NodeId value);

  /**
   * Get the SetpointNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SetpointNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSetpointNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSetpointNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSetpointNodeNodeAsync();

  /**
   * Get the local value of the BaseSetpointNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseSetpointNode Node.
   * @throws UaException if an error occurs creating or getting the BaseSetpointNode Node.
   */
  NodeId getBaseSetpointNode() throws UaException;

  /**
   * Set the local value of the BaseSetpointNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseSetpointNode Node.
   * @throws UaException if an error occurs creating or getting the BaseSetpointNode Node.
   */
  void setBaseSetpointNode(NodeId value) throws UaException;

  /**
   * Read the value of the BaseSetpointNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readBaseSetpointNode() throws UaException;

  /**
   * Write a new value for the BaseSetpointNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseSetpointNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseSetpointNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readBaseSetpointNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseSetpointNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseSetpointNodeAsync(NodeId value);

  /**
   * Get the BaseSetpointNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseSetpointNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getBaseSetpointNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseSetpointNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getBaseSetpointNodeNodeAsync();
}
