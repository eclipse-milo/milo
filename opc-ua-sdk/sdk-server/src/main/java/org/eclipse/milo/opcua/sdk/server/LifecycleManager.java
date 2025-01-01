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

import com.google.common.collect.Lists;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A {@link Lifecycle} instance that manages the startup and shutdown of a collection of
 * sub-Lifecycles.
 *
 * <p>Registered Lifecycles are started when this {@link LifecycleManager} is started and shutdown
 * when this LifecycleManager is shutdown.
 *
 * <p>The order that sub-Lifecycles are shutdown can be controlled with the optional {@link
 * ShutdownOrder} parameter provided in {@link LifecycleManager#LifecycleManager(ShutdownOrder)}.
 */
public final class LifecycleManager extends AbstractLifecycle {

  private final CopyOnWriteArrayList<Lifecycle> lifecycles = new CopyOnWriteArrayList<>();
  private final ShutdownOrder shutdownOrder;

  /**
   * Create a {@link LifecycleManager} that shuts down sub-{@link Lifecycle}s in {@link
   * ShutdownOrder#LINEAR} order.
   */
  public LifecycleManager() {
    this(ShutdownOrder.LINEAR);
  }

  /**
   * Create a {@link LifecycleManager} that shuts down sub-{@link Lifecycle}s in the specified
   * {@link ShutdownOrder}.
   *
   * @param shutdownOrder the {@link ShutdownOrder} to shutdown sub-Lifecycles in.
   */
  public LifecycleManager(ShutdownOrder shutdownOrder) {
    this.shutdownOrder = shutdownOrder;
  }

  @Override
  protected void onStartup() {
    lifecycles.forEach(Lifecycle::startup);
  }

  @Override
  protected void onShutdown() {
    if (shutdownOrder == ShutdownOrder.LINEAR) {
      lifecycles.forEach(Lifecycle::shutdown);
    } else {
      Lists.reverse(lifecycles).forEach(Lifecycle::shutdown);
    }
  }

  /**
   * Add a {@link Lifecycle} to be managed.
   *
   * <p>This Lifecycle will have {@link Lifecycle#startup()} called when this manager is started and
   * {@link Lifecycle#shutdown()} called when this manager is shutdown.
   *
   * @param lifecycle the {@link Lifecycle} to add.
   */
  public void addLifecycle(Lifecycle lifecycle) {
    lifecycles.add(lifecycle);
  }

  /**
   * Remove a previously added {@link Lifecycle}.
   *
   * @param lifecycle the {@link Lifecycle} to remove.
   */
  public void removeLifecycle(Lifecycle lifecycle) {
    lifecycles.remove(lifecycle);
  }

  /**
   * Add logic to run at startup.
   *
   * <p>This is a convenience method for {@link #addLifecycle(Lifecycle)} where {@link
   * Lifecycle#shutdown()} is implied to be a no-op.
   *
   * @param startupTask logic to run at startup.
   * @return a {@link Lifecycle} instance that can be used with {@link #removeLifecycle(Lifecycle)}
   *     if needed.
   */
  public Lifecycle addStartupTask(Runnable startupTask) {
    Lifecycle lifecycle =
        new Lifecycle() {
          @Override
          public void startup() {
            startupTask.run();
          }

          @Override
          public void shutdown() {}
        };

    addLifecycle(lifecycle);

    return lifecycle;
  }

  /**
   * Add logic to run at shutdown.
   *
   * <p>This is a convenience method for {@link #addLifecycle(Lifecycle)} where {@link
   * Lifecycle#startup()} is implied to be a no-op.
   *
   * @param shutdownTask logic to run at shutdown.
   * @return a {@link Lifecycle} instance that can be used with {@link #removeLifecycle(Lifecycle)}
   *     if needed.
   */
  public Lifecycle addShutdownTask(Runnable shutdownTask) {
    Lifecycle lifecycle =
        new Lifecycle() {
          @Override
          public void startup() {}

          @Override
          public void shutdown() {
            shutdownTask.run();
          }
        };

    addLifecycle(lifecycle);

    return lifecycle;
  }

  /** Modes that control the order in which registered {@link Lifecycle}s are shutdown. */
  public enum ShutdownOrder {
    /** Lifecycles are shutdown in the same order they were added. */
    LINEAR,

    /** Lifecycles are shutdown in the reverse order they were added. */
    INVERSE
  }
}
