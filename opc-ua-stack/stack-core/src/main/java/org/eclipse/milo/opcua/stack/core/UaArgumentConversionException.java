/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

/**
 * A Method argument that cannot be converted to its declared Java representation.
 *
 * <p>Shared generated descriptors use this exception without depending on either SDK. Server
 * adapters can translate its zero-based index into an input argument result. Client output
 * conversion retains it separately from the operation status.
 */
public final class UaArgumentConversionException extends UaException {
  private final int argumentIndex;

  /**
   * @param argumentIndex the zero-based position in the argument array.
   * @param cause the conversion failure.
   */
  public UaArgumentConversionException(int argumentIndex, Throwable cause) {
    super(StatusCodes.Bad_TypeMismatch, "Cannot convert Method argument " + argumentIndex, cause);
    if (argumentIndex < 0) throw new IllegalArgumentException("negative argument index");
    this.argumentIndex = argumentIndex;
  }

  /**
   * @return the zero-based argument position.
   */
  public int getArgumentIndex() {
    return argumentIndex;
  }
}
