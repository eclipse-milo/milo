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

/**
 * A bounded FIFO buffer that replaces its oldest element once it holds {@link #maxSize()} elements.
 *
 * <p>Storage is allocated as elements arrive, so a buffer with a large maximum size that only ever
 * holds a few elements stays small.
 *
 * <p>Not thread-safe; callers must synchronize access.
 *
 * @param <E> the type of element held by this buffer.
 */
public class RingBuffer<E> {

  private static final int INITIAL_CAPACITY = 8;

  private int read = 0;
  private int write = 0;
  private int count = 0;

  private E[] buffer;
  private final int maxSize;

  /**
   * Create a buffer that holds at most {@code maxSize} elements.
   *
   * @param maxSize the maximum number of elements the buffer holds before {@link #add(Object)}
   *     replaces the oldest one.
   */
  public RingBuffer(int maxSize) {
    this.maxSize = maxSize;

    //noinspection unchecked
    buffer = (E[]) new Object[Math.min(maxSize, INITIAL_CAPACITY)];
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
      return buffer[(read + index) % buffer.length];
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
      buffer[(read + index) % buffer.length] = e;
    }
  }

  /**
   * Add an element to the buffer, replacing the oldest element in the buffer if full.
   *
   * @param e element to add.
   */
  public void add(@NonNull E e) {
    if (count == buffer.length && count < maxSize) {
      grow();
    }

    buffer[write] = e;
    write = (write + 1) % buffer.length;

    if (count == maxSize) {
      read = (read + 1) % buffer.length;
    } else {
      count += 1;
    }
  }

  /** Double the capacity, up to {@code maxSize}, moving the elements to the front in FIFO order. */
  private void grow() {
    int capacity = buffer.length;
    int newCapacity = (int) Math.min((long) capacity * 2, maxSize);

    //noinspection unchecked
    E[] newBuffer = (E[]) new Object[newCapacity];

    // The buffer is full, so the elements run from `read` to the end and then from 0 to `read`.
    int tail = capacity - read;
    System.arraycopy(buffer, read, newBuffer, 0, tail);
    System.arraycopy(buffer, 0, newBuffer, tail, read);

    buffer = newBuffer;
    read = 0;
    write = count;
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
      read = (read + 1) % buffer.length;
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

  /**
   * @return the number of elements the buffer can hold before it allocates more storage.
   */
  int capacity() {
    return buffer.length;
  }
}
