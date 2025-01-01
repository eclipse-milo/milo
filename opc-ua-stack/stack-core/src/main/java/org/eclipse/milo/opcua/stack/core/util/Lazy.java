/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.util.function.Supplier;

/**
 * Thread-safe holder for a lazily-computed value.
 *
 * @param <T> the type of value computed and held.
 */
public final class Lazy<T> {

  private volatile T value;

  /**
   * Get the lazily computed value, computing it if necessary using {@code supplier}.
   *
   * <p>{@code null} values returned by the supplier are not held, i.e. the next call will compute
   * the value again.
   *
   * @param supplier a {@link Supplier} that computes the value if necessary.
   * @return the lazily computed value.
   */
  public T get(Supplier<T> supplier) {
    final T v = value;

    if (v == null) {
      synchronized (this) {
        if (value == null) {
          value = supplier.get();
        }
        return value;
      }
    } else {
      return v;
    }
  }

  /**
   * Get the lazily computed value, computing it if necessary using {@code supplier}.
   *
   * <p>{@code null} values returned by the supplier are not held, i.e. the next call will compute
   * the value again.
   *
   * @param supplier a {@link ThrowingSupplier} that computes the value if necessary.
   * @return the lazily computed value.
   * @throws E if the supplier throws an exception.
   */
  public <E extends Exception> T getOrThrow(ThrowingSupplier<E, T> supplier) throws E {
    final T v = value;

    if (v == null) {
      synchronized (this) {
        if (value == null) {
          value = supplier.get();
        }
        return value;
      }
    } else {
      return v;
    }
  }

  /**
   * Set the value.
   *
   * @param value the value to set.
   */
  public synchronized void set(T value) {
    this.value = value;
  }

  /** Reset the value to {@code null}. */
  public synchronized void reset() {
    value = null;
  }

  /**
   * Like {@link Supplier} but it can throw an Exception.
   *
   * @param <T> the type of value supplied.
   */
  public interface ThrowingSupplier<E extends Exception, T> {
    T get() throws E;
  }
}
