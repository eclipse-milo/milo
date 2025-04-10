/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/** Super-interface for all OPC UA DataTypes. */
public interface UaDataType {

  /**
   * Get the {@link ExpandedNodeId} identifying this DataType.
   *
   * @return the {@link ExpandedNodeId} identifying this DataType.
   */
  ExpandedNodeId getTypeId();

  /**
   * Get the name of this DataType.
   *
   * <p>For most types this will be the simple name of the class, but there are exceptions:
   *
   * <ul>
   *   <li>types defined dynamically at runtime
   *   <li>code generated types that had a SymbolicName defined in the NodeSet, usually because the
   *       BrowseName is not suitable as an identifier
   * </ul>
   *
   * In these cases name of the DataType Class will differ from the actual name of the type.
   *
   * @return the name of this DataType.
   */
  default String getTypeName() {
    return getClass().getSimpleName();
  }
}
