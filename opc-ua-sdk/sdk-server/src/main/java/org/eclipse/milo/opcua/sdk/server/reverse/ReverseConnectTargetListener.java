/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

/**
 * Listener for server-side Reverse Connect target and attempt lifecycle changes.
 *
 * <p>Callbacks are invoked asynchronously on the server executor configured for the owning server.
 * Implementations should return promptly and hand off expensive work to their own executor.
 */
public interface ReverseConnectTargetListener {

  /**
   * Called after a target is registered.
   *
   * @param snapshot the initial runtime snapshot for the target.
   */
  default void onTargetAdded(ReverseConnectTargetSnapshot snapshot) {}

  /**
   * Called after target scheduling, pause, resume, attempt, or channel state changes.
   *
   * @param snapshot the updated runtime snapshot for the target.
   */
  default void onTargetUpdated(ReverseConnectTargetSnapshot snapshot) {}

  /**
   * Called after a target is removed.
   *
   * @param snapshot the final runtime snapshot for the removed target.
   */
  default void onTargetRemoved(ReverseConnectTargetSnapshot snapshot) {}

  /**
   * Called for each observable state transition emitted by a reverse-connect attempt.
   *
   * @param event the attempt transition event.
   */
  default void onAttemptEvent(ReverseConnectAttemptEvent event) {}
}
