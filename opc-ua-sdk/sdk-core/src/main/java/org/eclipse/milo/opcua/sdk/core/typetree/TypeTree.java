/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.typetree;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

public abstract class TypeTree<T extends TypeTree.Type> {

  protected final Map<NodeId, Tree<T>> types = new HashMap<>();
  protected final Tree<T> tree;

  public TypeTree(Tree<T> tree) {
    this.tree = tree;

    tree.traverseNodes(treeNode -> types.put(treeNode.getValue().getNodeId(), treeNode));
  }

  /**
   * Get the root node of the underlying {@link Tree} structure.
   *
   * @return the root node of the underlying {@link Tree} structure.
   */
  public Tree<T> getRoot() {
    return tree;
  }

  /**
   * Get the underlying {@link Tree} node for the type identified by {@code nodeId}.
   *
   * @param nodeId the {@link NodeId} of a type.
   * @return the underlying {@link Tree} node for the type identified by {@code nodeId}.
   */
  public @Nullable Tree<T> getTreeNode(NodeId nodeId) {
    return types.get(nodeId);
  }

  /**
   * Get the {@link T} for the type identified by {@code nodeId}, if it exists.
   *
   * @param nodeId the {@link NodeId} of the type.
   * @return {@link T} for the type identified by {@code nodeId}, if it exists.
   */
  public @Nullable T getType(NodeId nodeId) {
    Tree<T> node = types.get(nodeId);
    return node != null ? node.getValue() : null;
  }

  /**
   * Return {@code true} if this TypeTree contains a type identified by {@code nodeId}.
   *
   * @param typeId the {@link NodeId} identifying the type.
   * @return {@code true} if this TypeTree contains a type identified by {@code nodeId}.
   */
  public boolean containsType(NodeId typeId) {
    return types.containsKey(typeId);
  }

  /**
   * Check if a type is a subtype of some other type.
   *
   * @param type the type to check.
   * @param supertype the potential supertype.
   * @return {@code true} if {@code type} is a subtype of {@code supertype}.
   */
  public boolean isSubtypeOf(T type, T supertype) {
    return isSubtypeOf(type.getNodeId(), supertype.getNodeId());
  }

  /**
   * Check if a type is a subtype of some other type.
   *
   * @param type the type to check.
   * @param superTypeId the NodeId of the potential supertype.
   * @return {@code true} if {@code type} is a subtype of the type identified by {@code
   *     superTypeId}.
   */
  public boolean isSubtypeOf(T type, NodeId superTypeId) {
    return isSubtypeOf(type.getNodeId(), superTypeId);
  }

  /**
   * Check if a type is a subtype of some other type.
   *
   * @param typeId the NodeId of the type to check.
   * @param supertype the potential supertype.
   * @return {@code true} if {@code typeId} identifies a type that is a subtype of {@code
   *     supertype}.
   */
  public boolean isSubtypeOf(NodeId typeId, T supertype) {
    return isSubtypeOf(typeId, supertype.getNodeId());
  }

  /**
   * Check if a type is a subtype of some other type.
   *
   * @param typeId the NodeId of the type to check.
   * @param superTypeId the NodeId of the potential supertype.
   * @return {@code true} if {@code typeId} identifies a type that is a subtype of the type
   *     identified by {@code superTypeId}.
   */
  public boolean isSubtypeOf(NodeId typeId, NodeId superTypeId) {
    Tree<T> tree = getTreeNode(typeId);
    if (tree == null) return false;

    Tree<T> parentTree = tree.getParent();
    if (parentTree == null) return false;

    NodeId parentNodeId = parentTree.getValue().getNodeId();

    return parentNodeId.equals(superTypeId) || isSubtypeOf(parentNodeId, superTypeId);
  }

  public interface Type {

    /**
     * Get the {@link NodeId} identifying this type.
     *
     * @return the {@link NodeId} identifying this type.
     */
    NodeId getNodeId();
  }
}
