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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

/**
 * A tree-based representation of a VariableType hierarchy.
 *
 * <p>Allows for convenient operations such as:
 *
 * <ul>
 *   <li>checking if a VariableType is a subtype of another VariableType
 *   <li>retrieving VariableType information by NodeId
 * </ul>
 */
public class VariableTypeTree extends TypeTree<VariableType> {

  public VariableTypeTree(Tree<VariableType> tree) {
    super(tree);
  }

  /**
   * Get the {@link VariableType} info for the VariableType identified by {@code variableTypeId}, if
   * it exists.
   *
   * @param variableTypeId the {@link NodeId} of a VariableType Node.
   * @return the {@link VariableType} info for the VariableType identified by {@code
   *     variableTypeId}, if it exists.
   */
  public @Nullable VariableType getVariableType(NodeId variableTypeId) {
    return getType(variableTypeId);
  }

  /**
   * Get the underlying {@link Tree} node for the VariableType identified by {@code variableTypeId}.
   *
   * @param variableTypeId the {@link NodeId} of a VariableType Node.
   * @return the underlying {@link Tree} node for the VariableType identified by {@code
   *     variableTypeId}.
   */
  public @Nullable Tree<VariableType> getTreeNode(NodeId variableTypeId) {
    return types.get(variableTypeId);
  }
}
