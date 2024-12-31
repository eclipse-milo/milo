/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.jspecify.annotations.Nullable;

/** Identifies an OPC UA enumerated type. */
public interface UaEnumeratedType extends UaDataType {

  /**
   * Get the symbolic name of this enumerated value.
   *
   * @return the symbolic name of this enumerated value.
   */
  default @Nullable String getName() {
    return toString(); // TODO generate new enum implementations, make this abstract
  }

  /**
   * Get the numeric value associated with this enumerated value.
   *
   * @return the numeric value associated with this enumerated value.
   */
  int getValue();
}
