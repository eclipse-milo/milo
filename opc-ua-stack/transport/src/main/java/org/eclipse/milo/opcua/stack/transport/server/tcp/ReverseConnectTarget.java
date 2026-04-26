/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Transport-owned lifecycle handle for one server-side Reverse Connect target.
 *
 * <p>SDK code uses this handle to start or stop a configured target while retry policy, attempt
 * state, and idle-socket ownership remain inside the stack transport.
 */
public final class ReverseConnectTarget {

  private final ReverseConnectTargetOwner owner;

  ReverseConnectTarget(ReverseConnectTargetOwner owner) {
    this.owner = Objects.requireNonNull(owner);
  }

  /**
   * Start this target.
   *
   * @return a future that completes when the target has accepted the start request.
   */
  public CompletableFuture<Void> start() {
    return owner.start();
  }

  /**
   * Stop this target.
   *
   * @return a future that completes after owned attempts, timers, and channel closes are settled.
   */
  public CompletableFuture<Void> stop() {
    return owner.stop();
  }

  /**
   * Remove this target permanently.
   *
   * @return a future that completes after owned attempts, timers, and channel closes are settled.
   */
  public CompletableFuture<Void> remove() {
    return owner.remove();
  }
}
