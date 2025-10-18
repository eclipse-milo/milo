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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public interface ObjectType extends TypeTree.Type {

  /**
   * Get the Browse Name of this ObjectType.
   *
   * @return the Browse Name of this ObjectType.
   */
  QualifiedName getBrowseName();

  /**
   * Get the {@link NodeId} of this ObjectType.
   *
   * @return the {@link NodeId} of this ObjectType.
   */
  NodeId getNodeId();

  /**
   * Get whether this ObjectType is abstract.
   *
   * @return {@code true} if this ObjectType is abstract.
   */
  Boolean isAbstract();
}
