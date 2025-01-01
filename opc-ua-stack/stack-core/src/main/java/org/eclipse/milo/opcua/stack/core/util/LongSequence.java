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

import java.util.concurrent.atomic.AtomicLong;

public class LongSequence {

  private final AtomicLong atomic;

  private final long low;
  private final long high;

  public LongSequence(long low, long high) {
    this(low, high, low);
  }

  public LongSequence(long low, long high, long start) {
    this.low = low;
    this.high = high;

    atomic = new AtomicLong(start);
  }

  /**
   * @return the current value in the sequence, followed by an increment.
   */
  public long getAndIncrement() {
    while (true) {
      long current = atomic.get();
      long next = (current >= high ? low : current + 1);
      if (atomic.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  /**
   * @return the current value in the sequence, without incrementing.
   */
  public long get() {
    return atomic.get();
  }
}
