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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class NonBlockingLazyTest {

  @Test
  void get() {
    var lazy = new NonBlockingLazy<>();

    assertEquals("foo", lazy.get(() -> "foo"));
  }

  @Test
  void getOrThrow() {
    var lazy = new NonBlockingLazy<>();

    assertThrows(
        Exception.class,
        () ->
            lazy.getOrThrow(
                () -> {
                  throw new Exception();
                }));

    assertEquals("foo", lazy.getOrThrow(() -> "foo"));
  }

  @Test
  void retainsNonNullValue() {
    NonBlockingLazy<Object> lazy = new NonBlockingLazy<>();

    Object instance = new Object();

    assertSame(instance, lazy.get(() -> instance));
    assertSame(instance, lazy.get(() -> instance));
  }

  @Test
  void doesNotRetainNullValue() {
    NonBlockingLazy<Object> lazy = new NonBlockingLazy<>();

    assertNull(lazy.get(() -> null));

    Object instance = new Object();
    assertSame(instance, lazy.get(() -> instance));
  }

  @Test
  void onlyComputesOnce() {
    var lazy = new NonBlockingLazy<>();

    final var instance = new Object();

    Supplier<Object> supplier =
        new Supplier<>() {
          final AtomicInteger count = new AtomicInteger(0);

          @Override
          public Object get() {
            if (count.incrementAndGet() != 1) {
              throw new IllegalStateException();
            } else {
              return instance;
            }
          }
        };

    assertSame(instance, lazy.get(supplier));
    assertSame(instance, lazy.get(supplier));
  }

  @Test
  void isResettable() {
    var lazy = new NonBlockingLazy<>();

    final Object instance1 = new Object();
    final Object instance2 = new Object();

    Supplier<Object> supplier =
        new Supplier<>() {
          final AtomicInteger count = new AtomicInteger(0);

          @Override
          public Object get() {
            if (count.incrementAndGet() == 1) {
              return instance1;
            } else {
              return instance2;
            }
          }
        };

    assertSame(instance1, lazy.get(supplier));

    lazy.reset();

    assertSame(instance2, lazy.get(supplier));
  }

  @Test
  void set() {
    var lazy = new NonBlockingLazy<>();

    assertEquals("foo", lazy.get(() -> "foo"));

    var instance = new Object();
    lazy.set(instance);
    assertSame(instance, lazy.get(() -> null));
  }

  @Test
  void failedComputationIsNotCached() {
    var lazy = new NonBlockingLazy<String>();

    assertThrows(
        Exception.class,
        () ->
            lazy.getOrThrow(
                () -> {
                  throw new Exception("boom");
                }));

    assertEquals("foo", lazy.getOrThrow(() -> "foo"));
  }

  @Test
  void resetDoesNotBlockWhileComputationInProgress() throws Exception {
    var lazy = new NonBlockingLazy<String>();

    var computeStarted = new CountDownLatch(1);
    var computeRelease = new CountDownLatch(1);

    ExecutorService executor = Executors.newCachedThreadPool();
    try {
      Future<String> computing =
          executor.submit(
              () ->
                  lazy.get(
                      () -> {
                        computeStarted.countDown();
                        try {
                          computeRelease.await();
                        } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                        }
                        return "stale";
                      }));

      assertTrue(computeStarted.await(5, TimeUnit.SECONDS));

      // reset() must return promptly even though a computation is in progress
      var resetDone = new CountDownLatch(1);
      executor.submit(
          () -> {
            lazy.reset();
            resetDone.countDown();
          });

      assertTrue(
          resetDone.await(1, TimeUnit.SECONDS),
          "reset() blocked behind an in-progress computation");

      computeRelease.countDown();

      // the computing caller still receives the value it computed...
      assertEquals("stale", computing.get(5, TimeUnit.SECONDS));

      // ...but the reset discarded it, so the next call computes fresh
      assertEquals("fresh", lazy.get(() -> "fresh"));
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  void waitersReceiveValueFromInProgressComputation() throws Exception {
    var lazy = new NonBlockingLazy<String>();

    var computeStarted = new CountDownLatch(1);
    var computeRelease = new CountDownLatch(1);
    var supplierInvocations = new AtomicInteger(0);

    ExecutorService executor = Executors.newCachedThreadPool();
    try {
      Future<String> computing =
          executor.submit(
              () ->
                  lazy.get(
                      () -> {
                        supplierInvocations.incrementAndGet();
                        computeStarted.countDown();
                        try {
                          computeRelease.await();
                        } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                        }
                        return "value";
                      }));

      assertTrue(computeStarted.await(5, TimeUnit.SECONDS));

      Future<String> waiting = executor.submit(() -> lazy.get(() -> "unexpected"));

      computeRelease.countDown();

      assertEquals("value", computing.get(5, TimeUnit.SECONDS));
      assertEquals("value", waiting.get(5, TimeUnit.SECONDS));
      assertEquals(1, supplierInvocations.get());
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  void waitersReceiveExceptionFromInProgressComputation() throws Exception {
    var lazy = new NonBlockingLazy<String>();

    var computeStarted = new CountDownLatch(1);
    var computeRelease = new CountDownLatch(1);

    ExecutorService executor = Executors.newCachedThreadPool();
    try {
      Future<?> computing =
          executor.submit(
              () ->
                  lazy.getOrThrow(
                      () -> {
                        computeStarted.countDown();
                        try {
                          computeRelease.await();
                        } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                        }
                        throw new Exception("boom");
                      }));

      assertTrue(computeStarted.await(5, TimeUnit.SECONDS));

      Future<String> waiting =
          executor.submit(() -> lazy.<Exception>getOrThrow(() -> "after failure"));

      computeRelease.countDown();

      assertThrows(Exception.class, () -> computing.get(5, TimeUnit.SECONDS));

      // The waiter either observes the failure, or raced past it and computed (and cached) a
      // fresh value. Either way the failure itself is never cached.
      try {
        assertEquals("after failure", waiting.get(5, TimeUnit.SECONDS));
        assertEquals("after failure", lazy.getOrThrow(() -> "recovered"));
      } catch (ExecutionException e) {
        assertEquals("boom", e.getCause().getMessage());
        assertEquals("recovered", lazy.getOrThrow(() -> "recovered"));
      }
    } finally {
      executor.shutdownNow();
    }
  }
}
