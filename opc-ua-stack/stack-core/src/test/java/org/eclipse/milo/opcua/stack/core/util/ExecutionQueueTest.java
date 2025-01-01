/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class ExecutionQueueTest {

  private final ExecutorService executor = Executors.newCachedThreadPool();

  @Test
  public void testSubmitIsLinearWhenConcurrencyIs1() {
    ExecutionQueue queue = new ExecutionQueue(executor, 1);

    AtomicBoolean failed = new AtomicBoolean(false);
    AtomicInteger n = new AtomicInteger(0);

    for (int i = 0; i < 1000000; i++) {
      final int ii = i;

      queue.submit(
          () -> {
            int nn = n.getAndIncrement();
            if (ii != nn) {
              System.out.println("n=" + nn + " i=" + ii);
              failed.set(true);
            }
          });
    }

    assertFalse(failed.get());
  }

  @Test
  public void testWithConcurrency() throws InterruptedException {
    ExecutionQueue queue = new ExecutionQueue(executor, 4);

    final CountDownLatch latch = new CountDownLatch(100000);
    final AtomicInteger count = new AtomicInteger();

    for (int i = 0; i < 100000; i++) {
      queue.submit(
          () -> {
            count.incrementAndGet();
            latch.countDown();
          });
    }

    latch.await();
    assertEquals(100000, count.get());
  }
}
