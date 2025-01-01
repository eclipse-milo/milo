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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.jspecify.annotations.Nullable;

public interface DataType extends TypeTree.Type {

  /**
   * Get the Browse Name of this DataType.
   *
   * @return the Browse Name of this DataType.
   */
  QualifiedName getBrowseName();

  /**
   * Get the {@link NodeId} of this DataType.
   *
   * @return the {@link NodeId} of this DataType.
   */
  NodeId getNodeId();

  /**
   * Get the {@link NodeId} of the Binary Encoding Node for this DataType, if it exists.
   *
   * <p>Only Structured DataTypes have encoding ids.
   *
   * @return the NodeId of the Binary Encoding Node for this DataType, if it exists.
   */
  @Nullable NodeId getBinaryEncodingId();

  /**
   * Get the {@link NodeId} of the XML Encoding Node for this DataType, if it exists.
   *
   * <p>Only Structured DataTypes have encoding ids.
   *
   * @return the NodeId of the XML Encoding Node for this DataType, if it exists.
   */
  @Nullable NodeId getXmlEncodingId();

  /**
   * Get the {@link NodeId} of the JSON Encoding Node for this DataType, if it exists.
   *
   * <p>Only Structured DataTypes have encoding ids.
   *
   * @return the {@link NodeId} of the JSON Encoding Node for this DataType, if it exists.
   */
  @Nullable NodeId getJsonEncodingId();

  /**
   * Get the {@link DataTypeDefinition} of this DataType.
   *
   * <p>Only Structured and Enumerated DataTypes have a {@link DataTypeDefinition}.
   *
   * @return the {@link DataTypeDefinition} of this DataType.
   */
  @Nullable DataTypeDefinition getDataTypeDefinition();

  /**
   * Get whether this DataType is abstract.
   *
   * @return {@code true} if this DataType is abstract.
   */
  Boolean isAbstract();
}
