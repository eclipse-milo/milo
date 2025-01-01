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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TaskQueueTest {

  private final Executor executor = Executors.newCachedThreadPool();

  @Test
  void serialTaskExecution() {
    var taskExecutor =
        TaskQueue.newBuilder().setExecutor(executor).setMaxConcurrentTasks(1).build();

    var task = new TestTask();

    taskExecutor.execute(task);

    task.awaitExecution();
  }

  @Test
  void concurrentTaskExecution() {
    var taskExecutor =
        TaskQueue.newBuilder().setExecutor(executor).setMaxConcurrentTasks(2).build();

    taskExecutor.execute(
        new TestTask() {
          @Override
          public void execute() {
            // block indefinitely
            try {
              Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        });

    // these task should still execute because concurrency > 1
    var tasks = new ArrayList<TestTask>();
    for (int i = 0; i < 10; i++) {
      tasks.add(new TestTask());
    }

    tasks.forEach(taskExecutor::execute);
    tasks.forEach(TestTask::awaitExecution);
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

    executor.execute(
        () -> {
          try {
            Thread.sleep(500);
            triggeredTask.trigger();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });

    long now = System.nanoTime();
    taskExecutor.shutdown(true);
    long deltaMs = TimeUnit.MILLISECONDS.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS);

    // Hacky, but make sure we actually waited what should have been ~500ms for shutdown
    assertTrue(deltaMs > 250 && deltaMs < 750);
  }

  @Test
  void executionCallback() throws ExecutionException, InterruptedException {
    var taskExecutor = new TaskQueue(executor);

    {
      var task = new TestTask();
      CompletionStage<Unit> callback = taskExecutor.submit(task);
      assertNotNull(callback);
      task.awaitExecution();
      callback.toCompletableFuture().get();
      assertTrue(callback.toCompletableFuture().isDone());
    }

    {
      var task = new TriggeredTestTask();
      CompletionStage<Unit> callback = taskExecutor.submit(task);
      assertNotNull(callback);
      assertFalse(callback.toCompletableFuture().isDone());
      task.trigger();
      task.awaitExecution();
      callback.toCompletableFuture().get();
      assertTrue(callback.toCompletableFuture().isDone());
    }
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
      try {
        executed.await();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private class TriggeredTestTask extends TestTask {

    final CountDownLatch trigger = new CountDownLatch(1);

    @Override
    public void execute() {
      try {
        trigger.await();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      super.execute();
    }

    void trigger() {
      trigger.countDown();
    }
  }
}
