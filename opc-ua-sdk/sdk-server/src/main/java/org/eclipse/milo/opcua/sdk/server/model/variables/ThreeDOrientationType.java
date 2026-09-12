/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ThreeDOrientationType extends OrientationType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getANode();

  /** Gets the existing node's local value. */
  @Nullable Double getA();

  /** Sets the existing node's local value. */
  void setA(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBNode();

  /** Gets the existing node's local value. */
  @Nullable Double getB();

  /** Sets the existing node's local value. */
  void setB(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCNode();

  /** Gets the existing node's local value. */
  @Nullable Double getC();

  /** Sets the existing node's local value. */
  void setC(@Nullable Double value);
}
