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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1</a>
 */
public interface PubSubStatusEventType extends SystemEventType {
  QualifiedProperty<NodeId> CONNECTION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConnectionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<PubSubState> STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "State",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14647"),
          -1,
          PubSubState.class);

  /**
   * Get the local value of the ConnectionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConnectionId Node.
   * @throws UaException if an error occurs creating or getting the ConnectionId Node.
   */
  NodeId getConnectionId() throws UaException;

  /**
   * Set the local value of the ConnectionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConnectionId Node.
   * @throws UaException if an error occurs creating or getting the ConnectionId Node.
   */
  void setConnectionId(NodeId value) throws UaException;

  /**
   * Read the value of the ConnectionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readConnectionId() throws UaException;

  /**
   * Write a new value for the ConnectionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConnectionId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConnectionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readConnectionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeConnectionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConnectionIdAsync(NodeId value);

  /**
   * Get the ConnectionId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConnectionId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConnectionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConnectionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConnectionIdNodeAsync();

  /**
   * Get the local value of the GroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the GroupId Node.
   * @throws UaException if an error occurs creating or getting the GroupId Node.
   */
  NodeId getGroupId() throws UaException;

  /**
   * Set the local value of the GroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the GroupId Node.
   * @throws UaException if an error occurs creating or getting the GroupId Node.
   */
  void setGroupId(NodeId value) throws UaException;

  /**
   * Read the value of the GroupId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readGroupId() throws UaException;

  /**
   * Write a new value for the GroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeGroupId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeGroupIdAsync(NodeId value);

  /**
   * Get the GroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the GroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getGroupIdNodeAsync();

  /**
   * Get the local value of the State Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  PubSubState getState() throws UaException;

  /**
   * Set the local value of the State Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  void setState(PubSubState value) throws UaException;

  /**
   * Read the value of the State Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link PubSubState} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PubSubState readState() throws UaException;

  /**
   * Write a new value for the State Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link PubSubState} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeState(PubSubState value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PubSubState> readStateAsync();

  /**
   * An asynchronous implementation of {@link #writeState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStateAsync(PubSubState value);

  /**
   * Get the State {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the State {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStateNodeAsync();
}
