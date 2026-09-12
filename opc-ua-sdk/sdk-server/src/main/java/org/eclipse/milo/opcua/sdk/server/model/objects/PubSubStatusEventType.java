/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
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
  @Nullable NodeId getConnectionId();

  /** Sets the existing node's local value. */
  void setConnectionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConnectionIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getGroupId();

  /** Sets the existing node's local value. */
  void setGroupId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupIdNode();

  /** Gets the existing node's local value. */
  @Nullable PubSubState getState();

  /** Sets the existing node's local value. */
  void setState(@Nullable PubSubState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStateNode();
}
