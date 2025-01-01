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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public interface ReferenceType extends TypeTree.Type {

  /**
   * @return the {@link QualifiedName} that identifies this ReferenceType.
   */
  QualifiedName getBrowseName();

  /**
   * @return the inverse name of the reference, i.e. the meaning of the reference as seen from the
   *     target node.
   */
  Optional<String> getInverseName();

  /**
   * @return {@code true} if the reference is the same as seen from the source node and the target
   *     node.
   */
  boolean isSymmetric();

  /**
   * @return {@code true} if the reference is an abstract reference type, i.e. no references of this
   *     type exist, only references of its subtypes.
   */
  boolean isAbstract();

  /**
   * Get the {@link NodeId} of the supertype of this reference type.
   *
   * <p>All references except the root reference type have a supertype.
   *
   * @return the {@link NodeId} of the supertype of this reference type.
   */
  Optional<NodeId> getSupertypeId();
}
