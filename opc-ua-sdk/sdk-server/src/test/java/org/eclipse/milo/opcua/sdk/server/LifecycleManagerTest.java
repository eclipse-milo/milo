/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class LifecycleManagerTest {

  // Startup failure must unwind acquired children even though subsequent shutdown is a no-op.
  @ParameterizedTest
  @EnumSource(LifecycleManager.ShutdownOrder.class)
  void failedStartupRollsBackSuccessfulChildren(LifecycleManager.ShutdownOrder order) {
    var manager = new LifecycleManager(order);
    var events = new ArrayList<String>();
    var startupFailure = new IllegalStateException("startup");
    var cleanupFailure = new IllegalStateException("cleanup");
    manager.addLifecycle(recordingLifecycle("a", events, null, null));
    manager.addLifecycle(recordingLifecycle("b", events, null, cleanupFailure));
    manager.addLifecycle(recordingLifecycle("c", events, startupFailure, null));
    manager.addLifecycle(recordingLifecycle("d", events, null, null));

    assertSame(startupFailure, assertThrows(IllegalStateException.class, manager::startup));
    assertEquals(List.of("start-a", "start-b", "start-c", "stop-b", "stop-a"), events);
    assertEquals(List.of(cleanupFailure), List.of(startupFailure.getSuppressed()));
    assertFalse(manager.isRunning());

    manager.shutdown();
    assertEquals(List.of("start-a", "start-b", "start-c", "stop-b", "stop-a"), events);
  }

  private static Lifecycle recordingLifecycle(
      String name,
      List<String> events,
      RuntimeException startupFailure,
      RuntimeException cleanupFailure) {
    return new Lifecycle() {
      @Override
      public void startup() {
        events.add("start-" + name);
        if (startupFailure != null) {
          throw startupFailure;
        }
      }

      @Override
      public void shutdown() {
        events.add("stop-" + name);
        if (cleanupFailure != null) {
          throw cleanupFailure;
        }
      }
    };
  }

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

    assertEquals(2, startupCount.get());
    assertEquals(2, shutdownCount.get());
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

    assertEquals(0, startupCount.get());
    assertEquals(0, shutdownCount.get());
  }

  @Test
  public void testStartupOrder() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger i = new AtomicInteger(0);
    manager.addStartupTask(() -> assertEquals(0, i.getAndIncrement()));
    manager.addStartupTask(() -> assertEquals(1, i.getAndIncrement()));
    manager.addStartupTask(() -> assertEquals(2, i.getAndIncrement()));

    manager.startup();
    manager.shutdown();
  }

  @Test
  public void testShutdownOrderLinear() {
    LifecycleManager manager = new LifecycleManager();

    AtomicInteger i = new AtomicInteger(0);
    manager.addShutdownTask(() -> assertEquals(0, i.getAndIncrement()));
    manager.addShutdownTask(() -> assertEquals(1, i.getAndIncrement()));
    manager.addShutdownTask(() -> assertEquals(2, i.getAndIncrement()));

    manager.startup();
    manager.shutdown();
  }

  @Test
  public void testShutdownOrderInverse() {
    LifecycleManager manager = new LifecycleManager(LifecycleManager.ShutdownOrder.INVERSE);

    AtomicInteger i = new AtomicInteger(0);
    manager.addShutdownTask(() -> assertEquals(2, i.getAndIncrement()));
    manager.addShutdownTask(() -> assertEquals(1, i.getAndIncrement()));
    manager.addShutdownTask(() -> assertEquals(0, i.getAndIncrement()));

    manager.startup();
    manager.shutdown();
  }
}
