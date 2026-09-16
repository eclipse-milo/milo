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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TaskQueueTest {

  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final ArrayList<TriggeredTestTask> heldTasks = new ArrayList<>();
  private final ConcurrentLinkedQueue<Throwable> workerFailures = new ConcurrentLinkedQueue<>();

  @AfterEach
  void terminateWorkers() throws InterruptedException {
    heldTasks.forEach(TriggeredTestTask::trigger);
    executor.shutdown();
    try {
      assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "workers must terminate");
    } finally {
      executor.shutdownNow();
      assertTrue(
          executor.awaitTermination(5, TimeUnit.SECONDS), "interrupted workers must terminate");
    }
    assertTrue(workerFailures.isEmpty(), () -> "background task failed: " + workerFailures);
  }

  @Test
  void serialTaskExecution() {
    var taskExecutor =
        TaskQueue.newBuilder().setExecutor(executor).setMaxConcurrentTasks(1).build();

    var heldTask = new TriggeredTestTask();
    taskExecutor.execute(heldTask);
    heldTask.awaitEntered();
    var task = new TestTask();
    var completion = taskExecutor.submit(task);
    assertNotNull(completion);
    try {
      assertThrows(
          TimeoutException.class,
          () -> completion.toCompletableFuture().get(100, TimeUnit.MILLISECONDS));
    } finally {
      heldTask.trigger();
    }
    assertDoesNotThrow(() -> completion.toCompletableFuture().get(5, TimeUnit.SECONDS));
  }

  @Test
  void concurrentTaskExecution() {
    var taskExecutor =
        TaskQueue.newBuilder().setExecutor(executor).setMaxConcurrentTasks(2).build();

    var heldTask = new TriggeredTestTask();
    var heldCompletion = taskExecutor.submit(heldTask);
    assertNotNull(heldCompletion);
    heldTask.awaitEntered();

    // these task should still execute because concurrency > 1
    var tasks = new ArrayList<TestTask>();
    for (int i = 0; i < 10; i++) {
      tasks.add(new TestTask());
    }

    tasks.forEach(taskExecutor::execute);
    tasks.forEach(TestTask::awaitExecution);
    assertFalse(heldCompletion.toCompletableFuture().isDone());
    heldTask.trigger();
    assertDoesNotThrow(() -> heldCompletion.toCompletableFuture().get(5, TimeUnit.SECONDS));
  }

  @Test
  void priorityRatio() {
    var taskExecutor =
        TaskQueue.newBuilder()
            .setExecutor(executor)
            .setMaxConcurrentTasks(1)
            .setPriorityRatio(3)
            .build();

    taskExecutor.pause();

    var regularTasks = new ArrayList<TestTask>();
    for (int i = 0; i < 2; i++) {
      var task = new TestTask();
      taskExecutor.execute(task);
      regularTasks.add(task);
    }

    var elevatedTasks = new ArrayList<TestTask>();
    for (int i = 0; i < 6; i++) {
      var task =
          new TestTask() {
            @Override
            public TaskQueue.TaskPriority getPriority() {
              return TaskQueue.TaskPriority.ELEVATED;
            }
          };
      elevatedTasks.add(task);
      taskExecutor.execute(task);
    }

    taskExecutor.resume();

    regularTasks.forEach(TestTask::awaitExecution);
    elevatedTasks.forEach(TestTask::awaitExecution);

    // ratio 3:1 elevated to regular, in order submitted
    assertEquals(0, elevatedTasks.get(0).seq);
    assertEquals(1, elevatedTasks.get(1).seq);
    assertEquals(2, elevatedTasks.get(2).seq);
    assertEquals(4, elevatedTasks.get(3).seq);
    assertEquals(5, elevatedTasks.get(4).seq);
    assertEquals(6, elevatedTasks.get(5).seq);

    assertEquals(3, regularTasks.get(0).seq);
    assertEquals(7, regularTasks.get(1).seq);
  }

  @Test
  void criticalRequests() {
    var taskExecutor =
        TaskQueue.newBuilder()
            .setExecutor(executor)
            .setMaxConcurrentTasks(1)
            .setPriorityRatio(3)
            .build();
    taskExecutor.pause();

    var regularTasks = new ArrayList<TestTask>();
    for (int i = 0; i < 10; i++) {
      var task = new TestTask();
      taskExecutor.execute(task);
      regularTasks.add(task);
    }

    var criticalTasks = new ArrayList<TestTask>();
    for (int i = 0; i < 100; i++) {
      var task =
          new TestTask() {
            @Override
            public TaskQueue.TaskPriority getPriority() {
              return TaskQueue.TaskPriority.CRITICAL;
            }
          };
      criticalTasks.add(task);
      taskExecutor.execute(task);
    }

    taskExecutor.resume();

    for (int i = 0; i < criticalTasks.size(); i++) {
      TestTask task = criticalTasks.get(i);
      task.awaitExecution();
      assertEquals(i, task.seq);
    }

    for (int i = 0; i < regularTasks.size(); i++) {
      TestTask task = regularTasks.get(i);
      task.awaitExecution();
      assertEquals(criticalTasks.size() + i, task.seq);
    }
  }

  @Test
  void queueSize() {
    var taskExecutor = TaskQueue.newBuilder().setExecutor(executor).setMaxQueueSize(3).build();

    taskExecutor.pause();

    var tasks = new ArrayList<TestTask>();
    for (int i = 0; i < 3; i++) {
      var task = new TestTask();
      tasks.add(task);
      assertTrue(taskExecutor.execute(task));
    }

    assertFalse(taskExecutor.execute(new TestTask()));

    taskExecutor.resume();
    tasks.forEach(TestTask::awaitExecution);

    assertTrue(taskExecutor.execute(new TestTask()));
  }

  @Test
  void shutdown() throws InterruptedException {
    var taskExecutor = new TaskQueue(executor);

    var triggeredTask = new TriggeredTestTask();

    taskExecutor.execute(triggeredTask);
    taskExecutor.execute(new TestTask());

    // 1 task not executed, stuck behind still-executing but un-triggered task
    assertEquals(1, taskExecutor.shutdown(false).size());

    // execute outstanding task
    triggeredTask.trigger();
    triggeredTask.awaitExecution();
    assertEquals(0, triggeredTask.seq);
  }

  @Test
  void shutdownQuiescence() throws InterruptedException {
    var taskExecutor = new TaskQueue(executor);

    var triggeredTask = new TriggeredTestTask();

    taskExecutor.execute(triggeredTask);

    triggeredTask.awaitEntered();
    var shutdownEntered = new CountDownLatch(1);
    var shutdown =
        executor.submit(
            () -> {
              shutdownEntered.countDown();
              return taskExecutor.shutdown(true);
            });
    await(shutdownEntered);
    try {
      assertThrows(
          TimeoutException.class,
          () -> shutdown.get(100, TimeUnit.MILLISECONDS),
          "shutdown must wait while a task is held");
    } finally {
      triggeredTask.trigger();
    }
    assertTrue(assertDoesNotThrow(() -> shutdown.get(5, TimeUnit.SECONDS)).isEmpty());
    triggeredTask.awaitExecution();
  }

  @Test
  void executionCallback() throws Exception {
    var taskExecutor = new TaskQueue(executor);

    {
      var task = new TestTask();
      CompletionStage<Unit> callback = taskExecutor.submit(task);
      assertNotNull(callback);
      task.awaitExecution();
      callback.toCompletableFuture().get(5, TimeUnit.SECONDS);
      assertTrue(callback.toCompletableFuture().isDone());
    }

    {
      var task = new TriggeredTestTask();
      CompletionStage<Unit> callback = taskExecutor.submit(task);
      assertNotNull(callback);
      assertFalse(callback.toCompletableFuture().isDone());
      task.trigger();
      task.awaitExecution();
      callback.toCompletableFuture().get(5, TimeUnit.SECONDS);
      assertTrue(callback.toCompletableFuture().isDone());
    }
  }

  @Test
  void callbackCompletesExceptionallyWhenTaskThrows() throws Exception {
    var taskExecutor = new TaskQueue(executor);

    var task =
        new TestTask() {
          @Override
          public void execute() {
            throw new RuntimeException("Task execution failed");
          }
        };

    CompletionStage<Unit> callback = taskExecutor.submit(task);
    assertNotNull(callback);

    // Expected: callback completes exceptionally when the task throws
    assertThrows(
        ExecutionException.class,
        () -> callback.toCompletableFuture().get(5, TimeUnit.SECONDS),
        "Callback should complete exceptionally when task throws");
  }

  @Test
  void callbackCompletesWhenSchedulingCallbackFails() throws Exception {
    // Executor that runs the first task submission normally but throws on later executions
    Executor flakyExecutor =
        new Executor() {
          final AtomicInteger calls = new AtomicInteger();

          @Override
          public void execute(@NonNull Runnable command) {
            if (calls.getAndIncrement() == 0) {
              // Schedule the TaskQueue's first task execution on the real executor
              executor.execute(command);
            } else {
              // Simulate executor failing to schedule the callback completion runnable
              throw new RejectedExecutionException("simulated execution rejection");
            }
          }
        };

    var taskExecutor = TaskQueue.newBuilder().setExecutor(flakyExecutor).build();

    var task = new TestTask();
    var callback = taskExecutor.submit(task);
    assertNotNull(callback);

    // Ensure the task itself executed successfully
    task.awaitExecution();

    // Expected: even if scheduling the callback completion throws, the callback should be
    // completed.
    assertDoesNotThrow(
        () -> callback.toCompletableFuture().get(5, TimeUnit.SECONDS),
        "Callback should complete even when scheduling the completion throws");
  }

  @Test
  void getQueueSizeAndPending() throws InterruptedException {
    var taskQueue = TaskQueue.newBuilder().setExecutor(executor).setMaxConcurrentTasks(1).build();

    // Initially both should be 0
    assertEquals(0, taskQueue.getQueueSize());
    assertEquals(0, taskQueue.getPending());

    var heldTask = new TriggeredTestTask();
    var completion = taskQueue.submit(heldTask);
    assertNotNull(completion);
    heldTask.awaitEntered();

    // Now pending should be 1, queue should be 0
    assertEquals(0, taskQueue.getQueueSize());
    assertEquals(1, taskQueue.getPending());

    // Add more tasks while the first is blocked
    taskQueue.execute(new TestTask());
    taskQueue.execute(new TestTask());

    // Queue should now have 2, pending still 1
    assertEquals(2, taskQueue.getQueueSize());
    assertEquals(1, taskQueue.getPending());

    // Release the blocking task and shutdown
    heldTask.trigger();
    assertDoesNotThrow(() -> completion.toCompletableFuture().get(5, TimeUnit.SECONDS));
    var shutdown = executor.submit(() -> taskQueue.shutdown(true));
    assertDoesNotThrow(() -> shutdown.get(5, TimeUnit.SECONDS));

    // After shutdown, both should be 0
    assertEquals(0, taskQueue.getQueueSize());
    assertEquals(0, taskQueue.getPending());
  }

  private final AtomicInteger sequence = new AtomicInteger(0);

  private class TestTask implements TaskQueue.Task {

    int seq = -1;

    final CountDownLatch executed = new CountDownLatch(1);

    @Override
    public void execute() {
      seq = sequence.getAndIncrement();

      executed.countDown();
    }

    void awaitExecution() {
      await(executed);
    }
  }

  private static void await(CountDownLatch latch) {
    try {
      assertTrue(latch.await(5, TimeUnit.SECONDS), "task coordination timed out");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AssertionError("task coordination interrupted", e);
    }
  }

  private class TriggeredTestTask extends TestTask {

    final CountDownLatch trigger = new CountDownLatch(1);
    final CountDownLatch entered = new CountDownLatch(1);

    TriggeredTestTask() {
      heldTasks.add(this);
    }

    @Override
    public void execute() {
      entered.countDown();
      try {
        await(trigger);
        super.execute();
      } catch (Throwable failure) {
        workerFailures.add(failure);
        throw failure;
      }
    }

    void awaitEntered() {
      await(entered);
    }

    void trigger() {
      trigger.countDown();
    }
  }
}
