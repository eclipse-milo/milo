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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25</a>
 */
public interface DiscrepancyAlarmType extends AlarmConditionType {
  QualifiedProperty<NodeId> TARGET_VALUE_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TargetValueNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Double> EXPECTED_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpectedTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> TOLERANCE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Tolerance",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  NodeId getTargetValueNode();

  void setTargetValueNode(NodeId value);

  PropertyType getTargetValueNodeNode();

  Double getExpectedTime();

  void setExpectedTime(Double value);

  PropertyType getExpectedTimeNode();

  Double getTolerance();

  void setTolerance(Double value);

  PropertyType getToleranceNode();
}
