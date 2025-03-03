/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27</a>
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

  Boolean getConstant();

  void setConstant(Boolean value);

  PropertyType getConstantNode();

  Boolean getFixedBase();

  void setFixedBase(Boolean value);

  PropertyType getFixedBaseNode();

  CartesianCoordinatesType getCartesianCoordinatesNode();

  CartesianCoordinates getCartesianCoordinates();

  void setCartesianCoordinates(CartesianCoordinates value);

  OrientationType getOrientationNode();

  Orientation getOrientation();

  void setOrientation(Orientation value);

  BaseDataVariableType getBaseFrameNode();

  NodeId getBaseFrame();

  void setBaseFrame(NodeId value);
}
