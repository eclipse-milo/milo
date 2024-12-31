/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * An "Out" param for an OPC UA method.
 *
 * <p>OPC UA Methods use Out parameters to return zero, one, or multiple values instead of
 * traditional function return values.
 *
 * @param <T> the value type.
 */
public class Out<T> {

  private volatile boolean set = false;
  private volatile T value;

  /**
   * Get the value for this Out param.
   *
   * @return the value for this Out param.
   * @throws UaException if the value has not been set.
   */
  @Nullable
  public T get() throws UaException {
    if (!set) {
      throw new UaException(StatusCodes.Bad_InternalError, "out value not set");
    }
    return value;
  }

  /**
   * Get the value for this Out param.
   *
   * @return the value for this Out param.
   */
  @Nullable
  public T getRaw() {
    return value;
  }

  /**
   * Set the value for this Out param.
   *
   * @param value the value.
   */
  public void set(@Nullable T value) {
    this.value = value;

    set = true;
  }

  /**
   * Return {@code true} if the value has been set, i.e. {@link #set(Object)} has been called.
   *
   * <p>This allows the distinction between a value that hasn't been set versus a value that was set
   * to {@code null}.
   *
   * @return {@code true} if the value has been set.
   */
  public boolean isSet() {
    return set;
  }
}
