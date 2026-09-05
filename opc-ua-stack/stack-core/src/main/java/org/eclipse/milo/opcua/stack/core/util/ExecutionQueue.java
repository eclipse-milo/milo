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
 *
 * <p>If the executor throws a runtime exception during dispatch, the submitting thread runs queued
 * tasks synchronously. Callbacks should return promptly because this fallback may run on an I/O or
 * timer thread.
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
    }
    maybePollAndExecute();
  }

  /**
   * Submit a {@link Runnable} to be executed at the head of the queue.
   *
   * @param runnable the {@link Runnable} to be executed.
   */
  public void submitToHead(Runnable runnable) {
    synchronized (queueLock) {
      queue.addFirst(runnable);
    }
    maybePollAndExecute();
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
    }
    maybePollAndExecute();
  }

  private void maybePollAndExecute() {
    synchronized (queueLock) {
      if (pending >= concurrencyLimit || paused || queue.isEmpty()) {
        return;
      }
      // Reserve the worker before dispatch, including when the executor runs it inline.
      pending++;
    }

    Task task = new Task();
    try {
      executor.execute(task);
    } catch (RuntimeException e) {
      task.run();
    }
  }

  private class Task implements Runnable {

    // A continuation can run before execute() returns, either inline or on another worker.
    // In that case the current invocation keeps ownership and drains the next batch itself.
    private boolean running;
    private boolean runAgain;
    private boolean completed;

    @Override
    public void run() {
      synchronized (this) {
        if (completed) {
          return;
        }
        if (running) {
          runAgain = true;
          return;
        }
        running = true;
      }

      while (true) {
        boolean moreTasks = runBatch();
        if (moreTasks) {
          try {
            executor.execute(this);
          } catch (RuntimeException e) {
            synchronized (this) {
              runAgain = true;
            }
          }
        }

        synchronized (this) {
          if (moreTasks && runAgain) {
            runAgain = false;
          } else {
            running = false;
            // A decorating executor can run this worker before reporting a dispatch failure.
            // Its fallback invocation must not release a finished worker's reservation twice.
            completed = !moreTasks;
            return;
          }
        }
      }
    }

    private boolean runBatch() {
      for (int i = 0; i < 2; i++) {
        Runnable runnable;
        synchronized (queueLock) {
          if (paused || queue.isEmpty()) {
            pending--;
            return false;
          }
          runnable = queue.remove();
        }

        try {
          runnable.run();
        } catch (Throwable throwable) {
          log.warn("Uncaught Throwable during execution.", throwable);
        }
      }

      synchronized (queueLock) {
        if (paused || queue.isEmpty()) {
          pending--;
          return false;
        }
      }
      return true;
    }
  }
}
