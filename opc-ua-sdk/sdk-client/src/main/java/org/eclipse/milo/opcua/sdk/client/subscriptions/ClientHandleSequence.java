/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

class ClientHandleSequence {

  private final ReentrantLock lock = new ReentrantLock();

  private final AtomicLong clientHandle;

  private final Predicate<UInteger> handleInUse;

  ClientHandleSequence(Predicate<UInteger> handleInUse) {
    this(handleInUse, 0L);
  }

  ClientHandleSequence(Predicate<UInteger> handleInUse, long initialValue) {
    this.handleInUse = handleInUse;

    clientHandle = new AtomicLong(initialValue);
  }

  UInteger nextClientHandle() {
    lock.lock();
    try {
      UInteger original = getAndIncrementWithRollover();
      UInteger next = original;

      while (handleInUse.test(next)) {
        next = getAndIncrementWithRollover();

        if (next.equals(original)) {
          // All client handles are in use... unlikely!
          throw new IllegalStateException("no unused client handles");
        }
      }

      return next;
    } finally {
      lock.unlock();
    }
  }

  private UInteger getAndIncrementWithRollover() {
    long current = clientHandle.get();

    if (current > UInteger.MAX_VALUE) {
      clientHandle.set(0L);
    }

    return uint(clientHandle.getAndIncrement());
  }
}
