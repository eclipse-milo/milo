/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Thread-safe holder for a lazily-computed value, computed without holding a lock.
 *
 * <p>Unlike {@link Lazy}, no lock is held while the value is computed: {@link #reset()} and {@link
 * #set(Object)} always return immediately, even while a computation is in progress. This makes it
 * safe to reset from threads that must never block, e.g. while another thread's computation
 * performs unbounded network I/O.
 *
 * <p>At most one computation is in progress per generation; concurrent callers wait for the
 * in-progress computation and receive its value, or its exception if it fails. A failed computation
 * is not cached: a subsequent call computes again.
 *
 * <p>{@link #reset()} starts a new generation. If a computation is in progress when {@link
 * #reset()} is called, its result is discarded once complete — though it is still delivered to the
 * caller that computed it and to any callers already waiting on it — and the next call computes a
 * fresh value, possibly overlapping the still-running discarded computation.
 *
 * <p>All callers of a given instance should use a consistent exception type {@code E}: a caller
 * that waits on another thread's in-progress computation rethrows that computation's exception
 * as-is, which may be a checked exception the waiting caller's own signature does not declare.
 *
 * @param <T> the type of value computed and held.
 */
public final class NonBlockingLazy<T> {

  private final AtomicReference<CompletableFuture<T>> ref = new AtomicReference<>();

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
    return this.getOrThrow(supplier::get);
  }

  /**
   * Get the lazily computed value, computing it if necessary using {@code supplier}.
   *
   * <p>{@code null} values returned by the supplier are not held, i.e. the next call will compute
   * the value again.
   *
   * @param supplier a {@link Lazy.ThrowingSupplier} that computes the value if necessary.
   * @return the lazily computed value.
   * @throws E if the supplier throws an exception, or if the in-progress computation this call
   *     waited on threw an exception.
   */
  public <E extends Exception> T getOrThrow(Lazy.ThrowingSupplier<E, T> supplier) throws E {
    while (true) {
      CompletableFuture<T> existing = ref.get();

      if (existing == null) {
        var future = new CompletableFuture<T>();

        if (!ref.compareAndSet(null, future)) {
          continue;
        }

        T value;
        try {
          value = supplier.get();
        } catch (RuntimeException | Error e) {
          ref.compareAndSet(future, null);
          future.completeExceptionally(e);
          throw e;
        } catch (Exception e) {
          ref.compareAndSet(future, null);
          future.completeExceptionally(e);
          @SuppressWarnings("unchecked")
          E checked = (E) e;
          throw checked;
        }

        if (value == null) {
          // null values are not held; clear before completing so waiters
          // that observe null re-attempt computation against a cleared ref.
          ref.compareAndSet(future, null);
        }
        future.complete(value);
        return value;
      } else {
        try {
          T value = existing.join();
          if (value != null) {
            return value;
          }
          // the computing thread got null; not held, try computing again
        } catch (CompletionException e) {
          Throwable cause = e.getCause();
          if (cause instanceof RuntimeException re) {
            throw re;
          }
          if (cause instanceof Error error) {
            throw error;
          }
          @SuppressWarnings("unchecked")
          E checked = (E) cause;
          throw checked;
        }
      }
    }
  }

  /**
   * Set the value.
   *
   * @param value the value to set.
   */
  public void set(T value) {
    ref.set(CompletableFuture.completedFuture(value));
  }

  /**
   * Reset the value to {@code null}.
   *
   * <p>This never blocks, even if a computation is in progress; the in-progress result is discarded
   * once complete.
   */
  public void reset() {
    ref.set(null);
  }
}
