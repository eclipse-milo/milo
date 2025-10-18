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
 * A tree-based representation of an ObjectType hierarchy.
 *
 * <p>Allows for convenient operations such as:
 *
 * <ul>
 *   <li>checking if an ObjectType is a subtype of another ObjectType
 *   <li>retrieving ObjectType information by NodeId
 * </ul>
 */
public class ObjectTypeTree extends TypeTree<ObjectType> {

  public ObjectTypeTree(Tree<ObjectType> tree) {
    super(tree);
  }

  /**
   * Get the {@link ObjectType} info for the ObjectType identified by {@code objectTypeId}, if it
   * exists.
   *
   * @param objectTypeId the {@link NodeId} of an ObjectType Node.
   * @return the {@link ObjectType} info for the ObjectType identified by {@code objectTypeId}, if
   *     it exists.
   */
  public @Nullable ObjectType getObjectType(NodeId objectTypeId) {
    return getType(objectTypeId);
  }

  /**
   * Get the underlying {@link Tree} node for the ObjectType identified by {@code objectTypeId}.
   *
   * @param objectTypeId the {@link NodeId} of an ObjectType Node.
   * @return the underlying {@link Tree} node for the ObjectType identified by {@code objectTypeId}.
   */
  public @Nullable Tree<ObjectType> getTreeNode(NodeId objectTypeId) {
    return types.get(objectTypeId);
  }
}
