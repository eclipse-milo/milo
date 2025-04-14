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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class LazyTest {

  @Test
  void get() {
    var lazy = new Lazy<>();

    assertEquals("foo", lazy.get(() -> "foo"));
  }

  @Test
  void getOrThrow() throws Exception {
    var lazy = new Lazy<>();

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
  void lazyRetainsNonNullValue() {
    Lazy<Object> lazy = new Lazy<>();

    Object instance = new Object();

    Object o1 = lazy.get(() -> instance);
    assertEquals(instance, o1);

    Object o2 = lazy.get(() -> instance);
    assertEquals(instance, o2);
  }

  @Test
  void lazyRetainsNullValue() {
    Lazy<Object> lazy = new Lazy<>();

    Object o1 = lazy.get(() -> null);

    assertNull(o1);
  }

  @Test
  void lazyOnlyComputesOnce() {
    var lazy = new Lazy<>();

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

    Object o1 = lazy.get(supplier);
    assertEquals(instance, o1);

    Object o2 = lazy.get(supplier);
    assertEquals(instance, o2);
  }

  @Test
  void lazyIsResettable() {
    var lazy = new Lazy<>();

    final Object instance1 = new Object();
    final Object instance2 = new Object();

    Supplier<Object> supplier =
        new Supplier<>() {
          final AtomicInteger count = new AtomicInteger(0);

          @Override
          public Object get() {
            return switch (count.incrementAndGet()) {
              case 1 -> instance1;
              default -> instance2;
            };
          }
        };

    Object o1 = lazy.get(supplier);
    assertEquals(instance1, o1);

    lazy.reset();

    Object o2 = lazy.get(supplier);
    assertEquals(instance2, o2);
  }

  @Test
  void lazySet() {
    var lazy = new Lazy<>();

    assertEquals("foo", lazy.get(() -> "foo"));

    var instance = new Object();
    lazy.set(instance);
    assertEquals(instance, lazy.get(() -> null));
  }
}
