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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionQueueTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionQueueTest.class);

  private final ExecutorService executor = Executors.newCachedThreadPool();

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  // Delivery ownership cannot be lost when a configured bounded executor is temporarily full.
  @Test
  void rejectedSubmissionCompletesInline() {
    ExecutionQueue queue =
        new ExecutionQueue(
            task -> {
              throw new RejectedExecutionException("saturated");
            });
    AtomicInteger completed = new AtomicInteger();
    queue.submit(completed::incrementAndGet);
    queue.submit(completed::incrementAndGet);
    assertEquals(2, completed.get());
  }

  // A worker must finish its backlog even if the executor rejects subsequent submissions.
  @Test
  void executorRejectionWhileDrainingDoesNotLoseQueuedTasks() {
    AtomicReference<Runnable> worker = new AtomicReference<>();
    ExecutionQueue queue =
        new ExecutionQueue(
            task -> {
              if (!worker.compareAndSet(null, task)) {
                throw new RejectedExecutionException("saturated");
              }
            });
    List<Integer> completed = new ArrayList<>();
    queue.submit(() -> completed.add(1));
    queue.submit(() -> completed.add(2));
    queue.submit(() -> completed.add(3));
    worker.get().run();
    queue.submit(() -> completed.add(4));
    assertEquals(List.of(1, 2, 3, 4), completed);
  }

  // Direct executors and nested submissions must not recurse once per queued task.
  @Test
  void reentrantSubmissionWithDirectExecutorDrainsWithoutRecursion() {
    ExecutionQueue queue = new ExecutionQueue(Runnable::run);
    AtomicInteger completed = new AtomicInteger();
    Runnable producer =
        new Runnable() {
          @Override
          public void run() {
            if (completed.incrementAndGet() < 100_000) {
              queue.submit(this);
            }
          }
        };
    queue.submit(producer);
    assertEquals(100_000, completed.get());
  }

  // A busy queue must yield so unrelated tasks sharing its executor can make progress.
  @Test
  void workersYieldToOtherExecutorTasksBetweenBatches() {
    Queue<Runnable> executorTasks = new ArrayDeque<>();
    ExecutionQueue queue = new ExecutionQueue(executorTasks::add);
    List<String> completed = new ArrayList<>();
    queue.submit(() -> completed.add("first"));
    queue.submit(() -> completed.add("second"));
    queue.submit(() -> completed.add("third"));
    executorTasks.add(() -> completed.add("other"));
    while (!executorTasks.isEmpty()) {
      executorTasks.remove().run();
    }
    assertEquals(List.of("first", "second", "other", "third"), completed);
  }

  // Rejection fallback must not hold the queue lock across application callbacks.
  @Test
  void rejectedWorkerDoesNotBlockConcurrentSubmissionWhileCallbackRuns() throws Exception {
    ExecutionQueue queue =
        new ExecutionQueue(
            task -> {
              throw new RejectedExecutionException("saturated");
            });
    ExecutorService submitter = Executors.newSingleThreadExecutor();
    AtomicInteger completed = new AtomicInteger();
    try {
      queue.submit(
          () -> {
            try {
              submitter
                  .submit(() -> queue.submit(completed::incrementAndGet))
                  .get(5, TimeUnit.SECONDS);
              completed.incrementAndGet();
            } catch (Exception e) {
              throw new AssertionError(e);
            }
          });
      assertEquals(2, completed.get());
    } finally {
      submitter.shutdownNow();
    }
  }

  @Test
  public void testSubmitIsLinearWhenConcurrencyIs1() throws Exception {
    ExecutionQueue queue = new ExecutionQueue(executor, 1);

    AtomicBoolean failed = new AtomicBoolean(false);
    AtomicInteger n = new AtomicInteger(0);

    for (int i = 0; i < 1000000; i++) {
      final int ii = i;

      queue.submit(
          () -> {
            int nn = n.getAndIncrement();
            if (ii != nn) {
              LOGGER.debug("n={} i={}", nn, ii);
              failed.set(true);
            }
          });
    }

    CompletableFuture<Void> drained = new CompletableFuture<>();
    queue.submit(() -> drained.complete(null));
    drained.get(30, TimeUnit.SECONDS);
    assertEquals(1_000_000, n.get());
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

    assertTrue(latch.await(30, TimeUnit.SECONDS));
    assertEquals(100000, count.get());
  }
}
