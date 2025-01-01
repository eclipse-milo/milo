/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractLifecycle implements Lifecycle {

  private final AtomicReference<LifecycleState> state = new AtomicReference<>(LifecycleState.NEW);

  /**
   * {@inheritDoc}
   *
   * <p>Subsequent invocations throw {@link IllegalStateException}.
   *
   * @throws IllegalStateException on subsequent invocations.
   */
  @Override
  public final void startup() throws IllegalStateException {
    LifecycleState previous =
        state.getAndUpdate(prev -> prev == LifecycleState.NEW ? LifecycleState.RUNNING : prev);

    if (previous == LifecycleState.NEW) {
      this.onStartup();
    } else {
      throw new IllegalStateException("cannot call startup when state=" + previous);
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>If {@link #startup()} hasn't been called yet {@link IllegalStateException} is thrown.
   *
   * <p>Subsequent invocations have no effect.
   *
   * @throws IllegalStateException if not started yet.
   */
  @Override
  public final void shutdown() throws IllegalStateException {
    LifecycleState previous =
        state.getAndUpdate(prev -> prev == LifecycleState.RUNNING ? LifecycleState.STOPPED : prev);

    if (previous == LifecycleState.RUNNING) {
      this.onShutdown();
    } else if (previous == LifecycleState.NEW) {
      throw new IllegalStateException("Cannot call shutdown, never started.");
    }
  }

  /**
   * True if this lifecycle is running, i.e. after {@link #startup()} is called and before {@link
   * #shutdown()} is called.
   *
   * @return {@code true} if this lifecycle is running.
   */
  public final boolean isRunning() {
    return this.state.get() == LifecycleState.RUNNING;
  }

  /**
   * True if this lifecycle is <b>not</b> running, i.e. before {@link #startup()} is called or after
   * {@link #shutdown()}} is called.
   *
   * @return {@code true} if this lifecycle is <b>not</b> running.
   */
  public final boolean isNotRunning() {
    return !isRunning();
  }

  /** Execute startup logic, if there is any. */
  protected abstract void onStartup();

  /** Execute shutdown logic, if there is any. */
  protected abstract void onShutdown();

  private enum LifecycleState {
    NEW,
    RUNNING,
    STOPPED
  }
}
