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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FrameType extends BaseDataVariableType {
  QualifiedProperty<Boolean> CONSTANT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Constant",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> FIXED_BASE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "FixedBase",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getConstant();

  /** Sets the existing node's local value. */
  void setConstant(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConstantNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getFixedBase();

  /** Sets the existing node's local value. */
  void setFixedBase(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getFixedBaseNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  CartesianCoordinatesType getCartesianCoordinatesNode();

  /** Gets the existing node's local value. */
  @Nullable CartesianCoordinates getCartesianCoordinates();

  /** Sets the existing node's local value. */
  void setCartesianCoordinates(@Nullable CartesianCoordinates value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  OrientationType getOrientationNode();

  /** Gets the existing node's local value. */
  @Nullable Orientation getOrientation();

  /** Sets the existing node's local value. */
  void setOrientation(@Nullable Orientation value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getBaseFrameNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getBaseFrame();

  /** Sets the existing node's local value. */
  void setBaseFrame(@Nullable NodeId value);
}
