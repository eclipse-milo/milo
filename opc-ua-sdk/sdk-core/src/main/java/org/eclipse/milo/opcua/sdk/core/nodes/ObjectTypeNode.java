/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

public interface ObjectTypeNode extends Node {

  /**
   * The IsAbstract attribute indicates if this ObjectType is abstract or not.
   *
   * @return {@code true} if this ObjectType is abstract.
   */
  Boolean getIsAbstract();

  /**
   * Set the IsAbstract attribute of this ObjectType.
   *
   * @param isAbstract {@code true} if this
   */
  void setIsAbstract(Boolean isAbstract);
}
