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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.ClosedWatchServiceException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class WatchKeyRunnerTest {

  // Interrupting the watcher thread must stop the loop and preserve the thread's interrupt status
  // rather than logging an error and continuing to block on take().
  @Test
  void interruptedTakeRestoresInterruptStatusAndExitsLoop() throws Exception {
    var enteredTake = new CountDownLatch(1);
    var blockForever = new CountDownLatch(1);
    var takeCalls = new AtomicInteger();

    WatchService watchService =
        new StubWatchService() {
          @Override
          public WatchKey take() throws InterruptedException {
            takeCalls.incrementAndGet();
            enteredTake.countDown();
            blockForever.await();
            throw new AssertionError("unreachable");
          }
        };

    var interruptedAfterRun = new AtomicBoolean(false);
    var runner = new WatchKeyRunner(watchService, Map.of());
    var thread =
        new Thread(
            () -> {
              runner.run();
              interruptedAfterRun.set(Thread.currentThread().isInterrupted());
            },
            "watch-key-runner-interrupt-test");

    thread.start();
    try {
      assertTrue(enteredTake.await(5, TimeUnit.SECONDS), "runner did not enter take()");

      thread.interrupt();
      thread.join(5000);

      assertFalse(thread.isAlive(), "runner did not exit after interrupt");
      assertEquals(1, takeCalls.get(), "runner looped after InterruptedException");
      assertTrue(interruptedAfterRun.get(), "runner cleared its interrupt status");
    } finally {
      blockForever.countDown();
      thread.interrupt();
      thread.join(5000);
    }
  }

  // Control: normal shutdown via WatchService.close() exits cleanly without setting interrupt.
  @Test
  void closedWatchServiceExitsCleanlyWithoutInterruptStatus() throws Exception {
    var enteredTake = new CountDownLatch(1);
    var closeSignal = new CountDownLatch(1);

    WatchService watchService =
        new StubWatchService() {
          @Override
          public WatchKey take() throws InterruptedException {
            enteredTake.countDown();
            closeSignal.await();
            throw new ClosedWatchServiceException();
          }

          @Override
          public void close() {
            closeSignal.countDown();
          }
        };

    var interruptedAfterRun = new AtomicBoolean(true);
    var runner = new WatchKeyRunner(watchService, Map.of());
    var thread =
        new Thread(
            () -> {
              runner.run();
              interruptedAfterRun.set(Thread.currentThread().isInterrupted());
            },
            "watch-key-runner-close-test");

    thread.start();
    try {
      assertTrue(enteredTake.await(5, TimeUnit.SECONDS), "runner did not enter take()");

      watchService.close();
      thread.join(5000);

      assertFalse(thread.isAlive(), "runner did not exit after close");
      assertFalse(interruptedAfterRun.get(), "normal close should not set interrupt status");
    } finally {
      closeSignal.countDown();
      thread.interrupt();
      thread.join(5000);
    }
  }

  private abstract static class StubWatchService implements WatchService {

    @Override
    public void close() {}

    @Override
    public WatchKey poll() {
      return null;
    }

    @Override
    public WatchKey poll(long timeout, TimeUnit unit) {
      return null;
    }
  }
}
