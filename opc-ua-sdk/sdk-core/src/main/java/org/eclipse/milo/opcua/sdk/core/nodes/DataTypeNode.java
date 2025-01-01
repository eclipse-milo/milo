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

import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;

public interface DataTypeNode extends Node {

  /**
   * The IsAbstract attribute specifies if the DataType is abstract or not.
   *
   * <p>Abstract DataTypes can be used in the AddressSpace, i.e. Variables and VariableTypes can
   * point with their DataType Attribute to an abstract DataType. However, concrete values can never
   * be of an abstract DataType and shall always be of a concrete subtype of the abstract DataType.
   *
   * @return {@code true} if the DataType is abstract.
   */
  Boolean getIsAbstract();

  /**
   * Set the IsAbstract attribute of this DataType.
   *
   * @param isAbstract {@code true} if this
   */
  void setIsAbstract(Boolean isAbstract);

  /**
   * Get the DataTypeDefinition attribute.
   *
   * <p>The DataTypeDefinition Attribute is used to provide the metadata and encoding information
   * for custom DataTypes.
   *
   * <p>See OPC UA Part 3, section 5.8.3.
   *
   * @return the DataTypeDefinition of this Node.
   */
  DataTypeDefinition getDataTypeDefinition();

  /**
   * Set the DataTypeDefinition attribute.
   *
   * @param dataTypeDefinition the DataTypeDefinition to set.
   * @see #getDataTypeDefinition()
   */
  void setDataTypeDefinition(DataTypeDefinition dataTypeDefinition);
}
