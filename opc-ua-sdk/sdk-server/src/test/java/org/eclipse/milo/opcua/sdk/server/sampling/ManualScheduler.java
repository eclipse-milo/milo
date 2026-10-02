/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * A scheduler whose tasks run only when a test says so, so a test can step through sampling cycles
 * and initial sample timers deterministically.
 */
public final class ManualScheduler {

  final List<Scheduled> scheduled = new ArrayList<>();

  /** A scheduled task, its delay, and whether it was cancelled. */
  record Scheduled(Runnable task, long delayMillis, AtomicBoolean cancelled) {
    boolean isPending() {
      return !cancelled.get();
    }
  }

  public ScheduledExecutorService executor() {
    return new Executor();
  }

  /** The delays of the tasks that are still pending, in scheduling order. */
  public synchronized List<Long> pendingDelays() {
    return scheduled.stream().filter(Scheduled::isPending).map(Scheduled::delayMillis).toList();
  }

  /** Run and remove every pending task whose delay matches {@code delay}. */
  public void run(Predicate<Long> delay) {
    List<Scheduled> due;
    synchronized (this) {
      due = scheduled.stream().filter(s -> s.isPending() && delay.test(s.delayMillis())).toList();
      scheduled.removeAll(due);
    }
    due.forEach(s -> s.task().run());
  }

  /** Run and remove every pending task. */
  public void runAll() {
    run(delay -> true);
  }

  private final class Executor extends java.util.concurrent.AbstractExecutorService
      implements ScheduledExecutorService {

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      var cancelled = new AtomicBoolean(false);
      synchronized (ManualScheduler.this) {
        scheduled.add(new Scheduled(command, unit.toMillis(delay), cancelled));
      }
      return new Future<>(cancelled);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(
        Runnable command, long initialDelay, long period, TimeUnit unit) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void execute(Runnable command) {
      command.run();
    }

    @Override
    public void shutdown() {}

    @Override
    public List<Runnable> shutdownNow() {
      return List.of();
    }

    @Override
    public boolean isShutdown() {
      return false;
    }

    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return true;
    }
  }

  private static final class Future<V> implements ScheduledFuture<V> {

    private final AtomicBoolean cancelled;

    Future(AtomicBoolean cancelled) {
      this.cancelled = cancelled;
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return 0;
    }

    @Override
    public int compareTo(Delayed o) {
      return 0;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      return cancelled.compareAndSet(false, true);
    }

    @Override
    public boolean isCancelled() {
      return cancelled.get();
    }

    @Override
    public boolean isDone() {
      return cancelled.get();
    }

    @Override
    public V get() {
      throw new UnsupportedOperationException();
    }

    @Override
    public V get(long timeout, TimeUnit unit) {
      throw new UnsupportedOperationException();
    }
  }
}
