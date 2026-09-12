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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getTargetValueNode();

  /** Sets the existing node's local value. */
  void setTargetValueNode(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTargetValueNodeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getExpectedTime();

  /** Sets the existing node's local value. */
  void setExpectedTime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getExpectedTimeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getTolerance();

  /** Sets the existing node's local value. */
  void setTolerance(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getToleranceNode();
}
