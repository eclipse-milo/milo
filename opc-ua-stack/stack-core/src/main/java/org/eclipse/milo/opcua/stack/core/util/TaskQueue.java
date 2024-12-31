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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TaskQueue {

  /** Default concurrency limit, 1. Submitted tasks will be executed serially. */
  private static final int DEFAULT_MAX_CONCURRENT_TASKS = 1;

  /**
   * Default priority ratio, 5. Elevated priority tasks will be executed 5:1 against regular
   * priority tasks when the queues are saturated.
   */
  private static final int DEFAULT_PRIORITY_RATIO = 5;

  /** Default max queue size, effectively unlimited. */
  private static final int DEFAULT_MAX_QUEUE_SIZE = Integer.MAX_VALUE;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private int pending = 0;
  private boolean paused = false;
  private boolean shutdown = false;
  private CountDownLatch shutdownLatch;

  private final PrioritizedTaskQueue taskQueue;
  private final Lock taskQueueLock = new ReentrantLock(true);

  private final Executor executor;
  private final int maxConcurrentTasks;
  private final int maxQueueSize;

  /**
   * Create a {@link TaskQueue} that executes {@link Task}s on the provided {@link Executor}.
   *
   * <p>All other parameters are default values.
   *
   * @param executor the {@link Executor} that {@link Task}s will use to execute.
   * @see #DEFAULT_MAX_CONCURRENT_TASKS
   * @see #DEFAULT_MAX_QUEUE_SIZE
   * @see #DEFAULT_PRIORITY_RATIO
   */
  public TaskQueue(Executor executor) {
    this(executor, DEFAULT_MAX_CONCURRENT_TASKS, DEFAULT_MAX_QUEUE_SIZE, DEFAULT_PRIORITY_RATIO);
  }

  /**
   * Create a {@link TaskQueue} that executes {@link Task}s on the provided {@link Executor}.
   *
   * @param executor the {@link Executor} that {@link Task}s will use to execute.
   * @param maxConcurrentTasks the number of concurrently executing tasks allowed. When 1, the
   *     default value, submitted Tasks are guaranteed to execute serially.
   * @param maxQueueSize the maximum number of Tasks that can be queued before backpressure is
   *     applied and {@link #execute(Task)} starts returning false.
   * @param priorityRatio ratio of {@link TaskPriority#ELEVATED} tasks to {@link
   *     TaskPriority#REGULAR} tasks that will be executed when elevated tasks are continually being
   *     dequeued.
   */
  public TaskQueue(Executor executor, int maxConcurrentTasks, int maxQueueSize, int priorityRatio) {
    this.executor = executor;
    this.maxConcurrentTasks = maxConcurrentTasks;
    this.maxQueueSize = maxQueueSize;

    taskQueue = new PrioritizedTaskQueue(priorityRatio);
  }

  /**
   * Queue a {@link Task} to be executed.
   *
   * @param task the {@link Task} to be executed.
   * @return {@code true} if {@code task} was queued for execution, or {@code false} if it was not
   *     queued, either because this {@link TaskQueue} is shut down, or because backpressure is
   *     being applied because the configured max queue size would be exceeded.
   */
  public boolean execute(Task task) {
    taskQueueLock.lock();
    try {
      if (shutdown || taskQueue.size() >= maxQueueSize) {
        return false;
      }

      taskQueue.add(new TaskWrapper(task));

      maybePollAndExecute();

      return true;
    } finally {
      taskQueueLock.unlock();
    }
  }

  /**
   * Queue a {@link Task} to be executed, returning a {@link CompletionStage} that will complete
   * when the task has been executed.
   *
   * <p>The callback completes asynchronously, using the {@link TaskQueue}'s configured {@link
   * Executor}, and does not block execution of any further queued tasks.
   *
   * @param task the {@link Task} to be executed.
   * @return a {@link CompletionStage} that will complete when {@code task} has been executed, or
   *     null if the task was not queued, either because this {@link TaskQueue} is shut down, or
   *     because backpressure is being applied because the configured max queue size would be
   *     exceeded.
   */
  public @Nullable CompletionStage<Unit> submit(Task task) {
    taskQueueLock.lock();
    try {
      if (shutdown || taskQueue.size() >= maxQueueSize) {
        return null;
      }

      var callback = new CompletableFuture<Unit>();
      taskQueue.add(new TaskWrapper(task, callback));

      maybePollAndExecute();

      return callback;
    } finally {
      taskQueueLock.unlock();
    }
  }

  /** Pause execution of queued {@link Task}s. */
  public void pause() {
    taskQueueLock.lock();
    try {
      paused = true;
    } finally {
      taskQueueLock.unlock();
    }
  }

  /** Resume execution of queued {@link Task}s. */
  public void resume() {
    taskQueueLock.lock();
    try {
      paused = false;

      maybePollAndExecute();
    } finally {
      taskQueueLock.unlock();
    }
  }

  /**
   * Shut down this executor, optionally awaiting completion of any currently-executing tasks.
   *
   * @param awaitQuiescence {@code true} if this method should block awaiting completion of any
   *     currently-executing tasks.
   * @return a List of {@link Task}s that were queued and will not be executed.
   * @throws InterruptedException if the current Thread is interrupted while waiting.
   */
  public List<Task> shutdown(boolean awaitQuiescence) throws InterruptedException {
    taskQueueLock.lock();
    try {
      shutdown = true;

      if (taskQueue.isEmpty() && pending == 0) {
        return List.of();
      }

      if (awaitQuiescence) {
        // wait for pending to execute, clear queue and return un-executed tasks afterwards
        shutdownLatch = new CountDownLatch(pending);
      } else {
        // don't wait for pending to execute, just clear queue and return un-executed tasks
        List<Task> tasks =
            taskQueue.getTasks().stream().map(tw -> tw.task).collect(Collectors.toList());
        taskQueue.clear();
        return tasks;
      }
    } finally {
      taskQueueLock.unlock();
    }

    // if we made it this far we have pending tasks to await
    assert shutdownLatch != null;
    shutdownLatch.await();

    taskQueueLock.lock();
    try {
      List<Task> tasks =
          taskQueue.getTasks().stream().map(tw -> tw.task).collect(Collectors.toList());
      taskQueue.clear();
      return tasks;
    } finally {
      taskQueueLock.unlock();
    }
  }

  public boolean isShutdown() {
    taskQueueLock.lock();
    try {
      return shutdown;
    } finally {
      taskQueueLock.unlock();
    }
  }

  private void maybePollAndExecute() {
    taskQueueLock.lock();
    try {
      if (pending < maxConcurrentTasks && !paused && !shutdown && !taskQueue.isEmpty()) {
        executor.execute(Objects.requireNonNull(taskQueue.poll()));
        pending++;
      }
    } finally {
      taskQueueLock.unlock();
    }
  }

  /**
   * @return a new {@link Builder} instance.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  private class TaskWrapper implements Runnable {

    private final Task task;
    private final CompletableFuture<Unit> callback;

    private TaskWrapper(Task task) {
      this(task, null);
    }

    private TaskWrapper(Task task, CompletableFuture<Unit> callback) {
      this.task = task;
      this.callback = callback;
    }

    @Override
    public void run() {
      try {
        task.execute();

        if (callback != null) {
          executor.execute(() -> callback.complete(Unit.VALUE));
        }
      } catch (Throwable throwable) {
        logger.warn("Uncaught Throwable during Task execution.", throwable);
      }

      TaskWrapper inlineTask = null;

      taskQueueLock.lock();
      try {
        if (taskQueue.isEmpty() || paused || shutdown) {
          pending--;

          if (shutdown && shutdownLatch != null) {
            shutdownLatch.countDown();
          }
        } else {
          // pending count remains the same
          inlineTask = taskQueue.poll();
        }
      } finally {
        taskQueueLock.unlock();
      }

      if (inlineTask != null) {
        try {
          inlineTask.task.execute();
          if (inlineTask.callback != null) {
            CompletableFuture<Unit> callback = inlineTask.callback;
            executor.execute(() -> callback.complete(Unit.VALUE));
          }
        } catch (Throwable throwable) {
          logger.warn("Uncaught Throwable during Task execution.", throwable);
        }

        taskQueueLock.lock();
        try {
          if (taskQueue.isEmpty() || paused || shutdown) {
            pending--;

            if (shutdown && shutdownLatch != null) {
              shutdownLatch.countDown();
            }
          } else {
            // pending count remains the same
            executor.execute(Objects.requireNonNull(taskQueue.poll()));
          }
        } finally {
          taskQueueLock.unlock();
        }
      }
    }
  }

  public interface Task {

    /** Execute this {@link Task}. */
    void execute();

    /**
     * Get the {@link TaskPriority} assigned to this {@link Task}.
     *
     * @return the {@link TaskPriority} assigned to this {@link Task}.
     */
    default TaskPriority getPriority() {
      return TaskPriority.REGULAR;
    }
  }

  public enum TaskPriority {

    /** The default priority for a task. No special treatment. */
    REGULAR,

    /**
     * Elevated priority for a task. When present in the queue, elevated requests are favored for
     * execution over regular priority tasks, using the configured priority ratio to prevent
     * "starving" the regular priority tasks.
     */
    ELEVATED,

    /**
     * Critical priority for a task. When present in the queue, always executed ahead of elevated or
     * regular priority tasks. No limit or ratio to prevent "starving" lower priority tasks.
     */
    CRITICAL
  }

  public static final class Builder {

    private Executor executor;
    private int maxConcurrentTasks = DEFAULT_MAX_CONCURRENT_TASKS;
    private int maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;
    private int priorityRatio = DEFAULT_PRIORITY_RATIO;

    public Builder setExecutor(Executor executor) {
      this.executor = executor;
      return this;
    }

    public Builder setMaxConcurrentTasks(int maxConcurrentTasks) {
      this.maxConcurrentTasks = maxConcurrentTasks;
      return this;
    }

    public Builder setMaxQueueSize(int maxQueueSize) {
      this.maxQueueSize = maxQueueSize;
      return this;
    }

    public Builder setPriorityRatio(int priorityRatio) {
      this.priorityRatio = priorityRatio;
      return this;
    }

    /**
     * Build a {@link TaskQueue} using the parameters configured on this {@link Builder}.
     *
     * <p>At a minimum, the {@code executor} parameter must be set. See {@link
     * #setExecutor(Executor)}.
     *
     * @return a new {@link TaskQueue} built using the parameters configured on this {@link
     *     Builder}.
     */
    public TaskQueue build() {
      if (executor == null) {
        throw new NullPointerException("executor must be non-null");
      }
      maxConcurrentTasks = Math.max(1, maxConcurrentTasks);
      maxQueueSize = Math.max(1, maxQueueSize);
      priorityRatio = Math.max(1, priorityRatio);

      return new TaskQueue(executor, maxConcurrentTasks, maxQueueSize, priorityRatio);
    }
  }

  private static final class PrioritizedTaskQueue {

    private final ArrayDeque<TaskWrapper> regular = new ArrayDeque<>();
    private final ArrayDeque<TaskWrapper> elevated = new ArrayDeque<>();
    private final ArrayDeque<TaskWrapper> critical = new ArrayDeque<>();

    private int consecutiveElevatedExecutions = 0;
    private final int priorityRatio;

    private PrioritizedTaskQueue(int priorityRatio) {
      this.priorityRatio = priorityRatio;
    }

    void add(TaskWrapper task) {
      switch (task.task.getPriority()) {
        case REGULAR:
          regular.add(task);
          break;
        case ELEVATED:
          elevated.add(task);
          break;
        case CRITICAL:
          critical.add(task);
          break;
      }
    }

    boolean isEmpty() {
      return regular.isEmpty() && elevated.isEmpty() && critical.isEmpty();
    }

    TaskWrapper poll() {
      if (!critical.isEmpty()) {
        return critical.poll();
      } else if (consecutiveElevatedExecutions >= priorityRatio) {
        if (!regular.isEmpty()) {
          consecutiveElevatedExecutions = 0;
          return regular.poll();
        } else if (!elevated.isEmpty()) {
          consecutiveElevatedExecutions++;
          return elevated.poll();
        } else {
          return null;
        }
      } else {
        if (!elevated.isEmpty()) {
          consecutiveElevatedExecutions++;
          return elevated.poll();
        } else if (!regular.isEmpty()) {
          consecutiveElevatedExecutions = 0;
          return regular.poll();
        } else {
          return null;
        }
      }
    }

    List<TaskWrapper> getTasks() {
      var tasks = new ArrayList<TaskWrapper>();
      tasks.addAll(critical);
      tasks.addAll(elevated);
      tasks.addAll(regular);
      return tasks;
    }

    void clear() {
      critical.clear();
      elevated.clear();
      regular.clear();
    }

    int size() {
      return critical.size() + elevated.size() + regular.size();
    }
  }
}
