/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.Arrays;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NonNull;

public class RingBuffer<E> {

  private int read = 0;
  private int write = 0;
  private int count = 0;

  private final E[] buffer;
  private final int maxSize;

  public RingBuffer(int maxSize) {
    this.maxSize = maxSize;

    //noinspection unchecked
    buffer = (E[]) new Object[maxSize];
  }

  /**
   * Get the element at the specified index. This does not affect the next element to be returned.
   *
   * @param index The specified index.
   * @return The element at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of bounds...
   */
  public E get(int index) {
    if (index >= count) {
      throw new IndexOutOfBoundsException("index=" + index);
    } else {
      return buffer[(read + index) % maxSize];
    }
  }

  /**
   * Set the element at the specified index. This does not affect the next element to be returned.
   *
   * @param index The specified index.
   * @param e The element to set.
   * @throws IndexOutOfBoundsException if the index is out of bounds...
   */
  public void set(int index, E e) {
    if (index >= count) {
      throw new IndexOutOfBoundsException("index=" + index);
    } else {
      buffer[(read + index) % maxSize] = e;
    }
  }

  /**
   * Add an element to the buffer, replacing the oldest element in the buffer if full.
   *
   * @param e element to add.
   */
  public void add(@NonNull E e) {
    buffer[write] = e;
    write = (write + 1) % maxSize;

    if (count == maxSize) {
      read = (read + 1) % maxSize;
    } else {
      count += 1;
    }
  }

  /**
   * @return The next (oldest) element in the buffer.
   * @throws NoSuchElementException if the buffer is empty.
   */
  public E remove() {
    if (count <= 0) {
      throw new NoSuchElementException();
    } else {
      final E e = buffer[read];
      buffer[read] = null;
      read = (read + 1) % maxSize;
      count -= 1;
      return e;
    }
  }

  /** Clear the contents of this buffer. */
  public void clear() {
    read = write = count = 0;
    Arrays.fill(buffer, null);
  }

  /**
   * @return {@code true} if the buffer is empty (size == 0).
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return The maximum allowed size (number of elements).
   */
  public int maxSize() {
    return maxSize;
  }

  /**
   * @return The current size (number of elements).
   */
  public int size() {
    return count;
  }
}
