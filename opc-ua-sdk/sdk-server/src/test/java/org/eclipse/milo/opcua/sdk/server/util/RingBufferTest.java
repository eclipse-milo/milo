/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RingBufferTest {

  /** The largest queue size a MonitoredItem can be given. */
  private static final int MAX_QUEUE_SIZE = 0xFFFF;

  @Nested
  class Storage {

    // A MonitoredItem's queue lives as long as the item does and usually holds one or a few values,
    // so a large requested queue size must not reserve its storage up front.
    @Test
    void largeMaxSizeDoesNotAllocateItsStorageUpFront() {
      var buffer = new RingBuffer<Integer>(MAX_QUEUE_SIZE);

      assertTrue(buffer.capacity() <= 16, "capacity when empty: " + buffer.capacity());

      buffer.add(1);

      assertTrue(buffer.capacity() <= 16, "capacity holding one element: " + buffer.capacity());
      assertEquals(MAX_QUEUE_SIZE, buffer.maxSize());
    }

    // Storage follows the number of elements held, not the maximum, and never exceeds the maximum.
    @Test
    void capacityGrowsWithTheElementsHeldUpToMaxSize() {
      var buffer = new RingBuffer<Integer>(100);

      for (int i = 0; i < 20; i++) {
        buffer.add(i);
      }

      assertTrue(buffer.capacity() >= 20, "capacity holding 20 elements: " + buffer.capacity());
      assertTrue(buffer.capacity() < 100, "capacity holding 20 elements: " + buffer.capacity());

      for (int i = 20; i < 250; i++) {
        buffer.add(i);
      }

      assertEquals(100, buffer.capacity());
    }

    @Test
    void capacityNeverExceedsASmallMaxSize() {
      var buffer = new RingBuffer<Integer>(3);

      for (int i = 0; i < 10; i++) {
        buffer.add(i);
      }

      assertEquals(3, buffer.capacity());
      assertEquals(List.of(7, 8, 9), drain(buffer));
    }
  }

  @Nested
  class Ordering {

    // Growth happens while the oldest element sits in the middle of the storage. Values must still
    // come out in the order they arrived, or notifications would be delivered out of order.
    @Test
    void elementsKeepFifoOrderWhenGrowthHappensAfterWrapAround() {
      var buffer = new RingBuffer<Integer>(1000);

      int next = 0;
      int expectedHead = 0;

      // Advance the read position, then fill past the current capacity. Repeat so that several
      // growth steps each start from a wrapped state.
      for (int round = 0; round < 5; round++) {
        for (int i = 0; i < 5; i++) {
          buffer.add(next++);
        }
        for (int i = 0; i < 3; i++) {
          assertEquals(expectedHead++, buffer.remove());
        }

        // Fill the storage, then add one more to make it grow.
        int capacity = buffer.capacity();
        int untilGrowth = capacity - buffer.size() + 1;
        for (int i = 0; i < untilGrowth; i++) {
          buffer.add(next++);
        }

        assertTrue(buffer.capacity() > capacity, "capacity after round " + round);
      }

      assertEquals(next - expectedHead, buffer.size());

      for (int i = 0; i < buffer.size(); i++) {
        assertEquals(expectedHead + i, buffer.get(i), "element at index " + i);
      }

      List<Integer> remaining = drain(buffer);

      assertEquals(next - expectedHead, remaining.size());
      for (int i = 0; i < remaining.size(); i++) {
        assertEquals(expectedHead + i, remaining.get(i), "element removed at position " + i);
      }
    }

    // maxSize is still the point where the oldest value is discarded, however the storage grew.
    @Test
    void addReplacesTheOldestElementOnceMaxSizeIsReached() {
      var buffer = new RingBuffer<Integer>(50);

      for (int i = 0; i < 120; i++) {
        buffer.add(i);
      }

      assertEquals(50, buffer.size());
      assertEquals(70, buffer.get(0));
      assertEquals(119, buffer.get(49));

      List<Integer> remaining = drain(buffer);

      assertEquals(70, remaining.get(0));
      assertEquals(119, remaining.get(49));
      assertEquals(50, remaining.size());
    }

    // MonitoredItems overwrite the newest queued value, at index maxSize - 1, when the queue is
    // full and DiscardOldest is false.
    @Test
    void setReplacesTheElementAtAnIndexRelativeToTheOldest() {
      var buffer = new RingBuffer<Integer>(20);

      for (int i = 0; i < 25; i++) {
        buffer.add(i);
      }

      buffer.set(buffer.maxSize() - 1, -1);

      List<Integer> remaining = drain(buffer);

      assertEquals(5, remaining.get(0));
      assertEquals(23, remaining.get(18));
      assertEquals(-1, remaining.get(19));
    }

    @Test
    void bufferIsReusableAfterClear() {
      var buffer = new RingBuffer<Integer>(50);

      for (int i = 0; i < 30; i++) {
        buffer.add(i);
      }

      buffer.clear();

      assertTrue(buffer.isEmpty());
      assertThrows(NoSuchElementException.class, buffer::remove);

      for (int i = 0; i < 60; i++) {
        buffer.add(i);
      }

      assertEquals(50, buffer.size());
      assertEquals(10, buffer.remove());
    }
  }

  private static List<Integer> drain(RingBuffer<Integer> buffer) {
    var elements = new ArrayList<Integer>();

    while (!buffer.isEmpty()) {
      elements.add(buffer.remove());
    }

    return elements;
  }
}
