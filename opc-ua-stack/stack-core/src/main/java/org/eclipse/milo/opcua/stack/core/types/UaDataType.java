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
   * Get the encoding name of this DataType.
   *
   * <p>DataTypes have names that may be used in the JSON and XML encodings. Consequently, there are
   * some restrictions on the characters that can be used in the name.
   *
   * <p>When a DataType is defined in a UANodeSet it provides a browse name and, optionally, an
   * alternative symbolic name. When the browse name uses characters that cannot be encoded the
   * symbolic name is used as an alternative in the encoding.
   *
   * <p>Defaults to the simple name of the class, implementations should override as necessary.
   *
   * @return the encoding name of this DataType.
   */
  default String getEncodingName() {
    return getClass().getSimpleName();
  }
}
