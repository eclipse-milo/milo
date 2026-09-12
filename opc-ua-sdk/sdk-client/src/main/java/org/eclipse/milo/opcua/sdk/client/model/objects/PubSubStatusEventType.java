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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getConnectionId() throws UaException;

  /** Sets the existing node's local value. */
  void setConnectionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readConnectionId() throws UaException;

  /** Writes the value remotely. */
  void writeConnectionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readConnectionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConnectionIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConnectionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConnectionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setGroupId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeGroupId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeGroupIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable PubSubState getState() throws UaException;

  /** Sets the existing node's local value. */
  void setState(@Nullable PubSubState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PubSubState readState() throws UaException;

  /** Writes the value remotely. */
  void writeState(@Nullable PubSubState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PubSubState> readStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable PubSubState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStateNodeAsync();
}
