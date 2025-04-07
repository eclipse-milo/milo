/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;

/**
 * Base class for an instance of a "dynamic" type, i.e. one that is defined dynamically at runtime
 * as opposed to statically at compile time.
 */
public abstract sealed class DynamicType implements UaDataType
    permits DynamicEnumType, DynamicOptionSetType, DynamicStructType, DynamicUnionType {

  @Override
  public String getEncodingName() {
    return Objects.requireNonNull(getDataType().getBrowseName().name());
  }

  /**
   * Get the {@link DataType} that defines this type.
   *
   * @return the {@link DataType} that defines this type.
   */
  public abstract DataType getDataType();

  /**
   * Get the {@link DataTypeDefinition} that defines this type.
   *
   * <p>Subclasses may refine the return type to either {@link
   * org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition} or {@link
   * org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition}.
   *
   * @return the {@link DataTypeDefinition} that defines this type.
   */
  public abstract DataTypeDefinition getDataTypeDefinition();
}
