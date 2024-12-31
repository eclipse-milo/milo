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

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncSemaphore {

  private final ArrayDeque<CompletableFuture<SemaphorePermit>> waitQueue = new ArrayDeque<>();

  private final AtomicInteger availablePermits;

  public AsyncSemaphore(int initialPermits) {
    availablePermits = new AtomicInteger(initialPermits);
  }

  public CompletableFuture<SemaphorePermit> acquire() {
    CompletableFuture<SemaphorePermit> f = new CompletableFuture<>();

    boolean permitAvailable = false;

    synchronized (this) {
      if (availablePermits.get() > 0) {
        availablePermits.decrementAndGet();

        permitAvailable = true;
      } else {
        waitQueue.addLast(f);
      }
    }

    if (permitAvailable) {
      f.complete(new PermitImpl());
    }

    return f;
  }

  public interface SemaphorePermit {

    /** Releases this semaphore permit. */
    void release();
  }

  private final class PermitImpl implements SemaphorePermit {
    @Override
    public void release() {
      CompletableFuture<SemaphorePermit> next;

      synchronized (AsyncSemaphore.this) {
        next = waitQueue.pollFirst();
        if (next == null) availablePermits.incrementAndGet();
      }

      if (next != null) next.complete(new PermitImpl());
    }
  }
}
