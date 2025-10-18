/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.typetree;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public interface VariableType extends TypeTree.Type {

  /**
   * Get the Browse Name of this VariableType.
   *
   * @return the Browse Name of this VariableType.
   */
  QualifiedName getBrowseName();

  /**
   * Get the {@link NodeId} of this VariableType.
   *
   * @return the {@link NodeId} of this VariableType.
   */
  NodeId getNodeId();

  /**
   * Get the Value of this VariableType.
   *
   * @return the Value of this VariableType.
   */
  DataValue getValue();

  /**
   * Get the DataType of this VariableType.
   *
   * @return the DataType of this VariableType.
   */
  NodeId getDataType();

  /**
   * Get the ValueRank of this VariableType.
   *
   * @return the ValueRank of this VariableType.
   */
  Integer getValueRank();

  /**
   * Get the ArrayDimensions of this VariableType.
   *
   * @return the ArrayDimensions of this VariableType.
   */
  UInteger[] getArrayDimensions();

  /**
   * Get whether this VariableType is abstract.
   *
   * @return {@code true} if this VariableType is abstract.
   */
  Boolean isAbstract();
}
