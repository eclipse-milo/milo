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

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Queues up submitted {@link java.lang.Runnable}s and executes on an {@link
 * java.util.concurrent.Executor}, with optional concurrency.
 *
 * <p>When {@code concurrency = 1} (the default) submitted tasks are guaranteed to run serially and
 * in the order submitted.
 *
 * <p>When {@code concurrency > 1} there are no guarantees beyond the fact that tasks are still
 * pulled from a queue to be executed.
 */
public class ExecutionQueue {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Object queueLock = new Object();
  private final ArrayDeque<Runnable> queue = new ArrayDeque<>();

  private int pending = 0;
  private boolean paused = false;

  private final Executor executor;
  private final int concurrencyLimit;

  public ExecutionQueue(Executor executor) {
    this(executor, 1);
  }

  public ExecutionQueue(Executor executor, int concurrencyLimit) {
    this.executor = executor;
    this.concurrencyLimit = concurrencyLimit;
  }

  /**
   * Submit a {@link Runnable} to be executed.
   *
   * @param runnable the {@link Runnable} to be executed.
   */
  public void submit(Runnable runnable) {
    synchronized (queueLock) {
      queue.add(runnable);

      maybePollAndExecute();
    }
  }

  /**
   * Submit a {@link Runnable} to be executed at the head of the queue.
   *
   * @param runnable the {@link Runnable} to be executed.
   */
  public void submitToHead(Runnable runnable) {
    synchronized (queueLock) {
      queue.addFirst(runnable);

      maybePollAndExecute();
    }
  }

  /** Pause execution of queued {@link java.lang.Runnable}s. */
  public void pause() {
    synchronized (queueLock) {
      paused = true;
    }
  }

  /** Resume execution of queued {@link java.lang.Runnable}s. */
  public void resume() {
    synchronized (queueLock) {
      paused = false;

      maybePollAndExecute();
    }
  }

  private void maybePollAndExecute() {
    synchronized (queueLock) {
      if (pending < concurrencyLimit && !paused && !queue.isEmpty()) {
        executor.execute(new Task(queue.poll()));
        pending++;
      }
    }
  }

  private class Task implements Runnable {

    private final Runnable runnable;

    Task(Runnable runnable) {
      Preconditions.checkNotNull(runnable);

      this.runnable = runnable;
    }

    @Override
    public void run() {
      try {
        runnable.run();
      } catch (Throwable throwable) {
        log.warn("Uncaught Throwable during execution.", throwable);
      }

      InlineTask inlineTask = null;

      synchronized (queueLock) {
        if (queue.isEmpty() || paused) {
          pending--;
        } else {
          // pending count remains the same
          inlineTask = new InlineTask(queue.poll());
        }
      }

      if (inlineTask != null) {
        inlineTask.run();
      }
    }
  }

  private class InlineTask implements Runnable {

    private final Runnable runnable;

    InlineTask(Runnable runnable) {
      Preconditions.checkNotNull(runnable);

      this.runnable = runnable;
    }

    @Override
    public void run() {
      try {
        runnable.run();
      } catch (Throwable throwable) {
        log.warn("Uncaught Throwable during execution.", throwable);
      }

      synchronized (queueLock) {
        if (queue.isEmpty() || paused) {
          pending--;
        } else {
          // pending count remains the same
          executor.execute(new Task(queue.poll()));
        }
      }
    }
  }
}
