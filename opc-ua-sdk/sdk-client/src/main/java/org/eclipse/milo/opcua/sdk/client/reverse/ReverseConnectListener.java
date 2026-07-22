/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

/**
 * Receives serialized reverse-connect manager events.
 *
 * <p>Callbacks are delivered on the manager's configured executor through a serialized execution
 * queue. Implementations should still avoid long blocking work because they share the manager's
 * callback lane with other listeners.
 */
@SuppressWarnings("unused")
public interface ReverseConnectListener {

  /**
   * A listener socket has successfully bound.
   *
   * @param snapshot the listener state after binding.
   */
  default void onListenerBound(ReverseConnectListenerSnapshot snapshot) {}

  /**
   * A listener socket has been unbound during shutdown.
   *
   * @param snapshot the listener state after unbinding.
   */
  default void onListenerUnbound(ReverseConnectListenerSnapshot snapshot) {}

  /**
   * A server-opened TCP socket was accepted and is waiting for {@code ReverseHello}.
   *
   * @param snapshot the candidate state after socket acceptance.
   */
  default void onCandidateAccepted(ReverseConnectCandidateSnapshot snapshot) {}

  /**
   * A decoded and verified candidate is waiting for a selector to claim it.
   *
   * @param snapshot the pending candidate state.
   */
  default void onCandidatePending(ReverseConnectCandidateSnapshot snapshot) {}

  /**
   * A selector claimed a candidate and channel ownership has been transferred.
   *
   * @param snapshot the claimed candidate state.
   */
  default void onCandidateClaimed(ReverseConnectCandidateSnapshot snapshot) {}

  /**
   * A candidate was rejected before it could be claimed.
   *
   * @param snapshot the rejected candidate state.
   */
  default void onCandidateRejected(ReverseConnectCandidateSnapshot snapshot) {}

  /**
   * A pending candidate exceeded its hold time before any selector claimed it.
   *
   * @param snapshot the expired candidate state.
   */
  default void onCandidateExpired(ReverseConnectCandidateSnapshot snapshot) {}

  /**
   * The manager observed an unexpected listener, verifier, selector, or callback error.
   *
   * @param error the observed error.
   */
  default void onError(Throwable error) {}
}
