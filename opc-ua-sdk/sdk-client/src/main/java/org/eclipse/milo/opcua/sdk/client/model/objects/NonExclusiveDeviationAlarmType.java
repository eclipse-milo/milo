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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NonExclusiveDeviationAlarmType extends NonExclusiveLimitAlarmType {
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getSetpointNode() throws UaException;

  /** Sets the existing node's local value. */
  void setSetpointNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readSetpointNode() throws UaException;

  /** Writes the value remotely. */
  void writeSetpointNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readSetpointNodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSetpointNodeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSetpointNodeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSetpointNodeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getBaseSetpointNode() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseSetpointNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readBaseSetpointNode() throws UaException;

  /** Writes the value remotely. */
  void writeBaseSetpointNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readBaseSetpointNodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseSetpointNodeAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseSetpointNodeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBaseSetpointNodeNodeAsync();
}
