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

import static org.testng.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;

public class LifecycleManagerTest {

  @Test
  public void testStartupShutdown() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger startupCount = new AtomicInteger(0);
    AtomicInteger shutdownCount = new AtomicInteger(0);

    Lifecycle lifecycle =
        new Lifecycle() {
          @Override
          public void startup() {
            startupCount.incrementAndGet();
          }

          @Override
          public void shutdown() {
            shutdownCount.incrementAndGet();
          }
        };

    manager.addLifecycle(lifecycle);
    manager.addStartupTask(startupCount::incrementAndGet);
    manager.addShutdownTask(shutdownCount::incrementAndGet);

    manager.startup();
    manager.shutdown();

    assertEquals(startupCount.get(), 2);
    assertEquals(shutdownCount.get(), 2);
  }

  @Test
  public void testRemove() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger startupCount = new AtomicInteger(0);
    AtomicInteger shutdownCount = new AtomicInteger(0);

    Lifecycle lifecycle =
        new Lifecycle() {
          @Override
          public void startup() {
            startupCount.incrementAndGet();
          }

          @Override
          public void shutdown() {
            shutdownCount.incrementAndGet();
          }
        };

    manager.addLifecycle(lifecycle);
    manager.removeLifecycle(lifecycle);

    Lifecycle startup = manager.addStartupTask(startupCount::incrementAndGet);
    manager.removeLifecycle(startup);

    Lifecycle shutdown = manager.addShutdownTask(shutdownCount::incrementAndGet);
    manager.removeLifecycle(shutdown);

    assertEquals(startupCount.get(), 0);
    assertEquals(shutdownCount.get(), 0);
  }

  @Test
  public void testStartupOrder() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger i = new AtomicInteger(0);
    manager.addStartupTask(() -> assertEquals(i.getAndIncrement(), 0));
    manager.addStartupTask(() -> assertEquals(i.getAndIncrement(), 1));
    manager.addStartupTask(() -> assertEquals(i.getAndIncrement(), 2));

    manager.startup();
    manager.shutdown();
  }

  @Test
  public void testShutdownOrderLinear() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger i = new AtomicInteger(0);
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 0));
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 1));
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 2));

    manager.startup();
    manager.shutdown();
  }

  @Test
  public void testShutdownOrderInverse() {
    LifecycleManager manager = new LifecycleManager(LifecycleManager.ShutdownOrder.INVERSE);

    AtomicInteger i = new AtomicInteger(0);
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 2));
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 1));
    manager.addShutdownTask(() -> assertEquals(i.getAndIncrement(), 0));

    manager.startup();
    manager.shutdown();
  }
}
