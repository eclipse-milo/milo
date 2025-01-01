/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.2</a>
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

  NodeId getSetpointNode();

  void setSetpointNode(NodeId value);

  PropertyType getSetpointNodeNode();

  NodeId getBaseSetpointNode();

  void setBaseSetpointNode(NodeId value);

  PropertyType getBaseSetpointNodeNode();
}
