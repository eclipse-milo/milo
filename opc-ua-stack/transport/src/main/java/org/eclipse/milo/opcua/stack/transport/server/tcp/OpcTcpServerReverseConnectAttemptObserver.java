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

/**
 * Receives state transitions from a low-level server reverse-connect attempt.
 *
 * <p>Observers are part of the transport primitive used by higher SDK layers to build target-level
 * snapshots and retry decisions. Implementations should return quickly because transitions may be
 * emitted from a Netty event-loop thread.
 */
@FunctionalInterface
public interface OpcTcpServerReverseConnectAttemptObserver {

  /**
   * Observe an attempt state transition.
   *
   * @param event the transition event.
   */
  void onStateTransition(OpcTcpServerReverseConnectAttemptEvent event);

  /**
   * Get a no-op observer.
   *
   * @return a no-op observer.
   */
  static OpcTcpServerReverseConnectAttemptObserver noop() {
    return event -> {};
  }
}
