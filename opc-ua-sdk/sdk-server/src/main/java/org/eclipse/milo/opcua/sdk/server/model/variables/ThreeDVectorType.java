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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.22">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.22</a>
 */
public interface ThreeDVectorType extends VectorType {
  BaseDataVariableType getXNode();

  Double getX();

  void setX(Double value);

  BaseDataVariableType getYNode();

  Double getY();

  void setY(Double value);

  BaseDataVariableType getZNode();

  Double getZ();

  void setZ(Double value);
}
