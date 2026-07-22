/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

/**
 * Optional client transport capability for observing connected-channel state transitions.
 *
 * <p>Implementations should publish {@code connected=true} when a transport-owned channel becomes
 * ready for service and {@code connected=false} when that channel leaves service.
 */
public interface ChannelStateObservable {

  /**
   * Register a listener for transitions of the transport's usable channel state.
   *
   * <p>Implementations should keep listener invocation ordered with their own channel lifecycle.
   * Callers should return quickly because the callback may run on a transport executor or
   * event-loop thread.
   *
   * @param listener the listener to invoke when the connected-channel state changes.
   */
  void addTransitionListener(TransitionListener listener);

  /**
   * Remove a previously registered transition listener.
   *
   * <p>Removing a listener prevents future callbacks but does not cancel callbacks already
   * dispatched by the transport.
   *
   * @param listener the listener to remove.
   */
  void removeTransitionListener(TransitionListener listener);

  /** Callback notified when a client transport gains or loses a usable channel. */
  @FunctionalInterface
  interface TransitionListener {

    /**
     * Invoked when the transport's connected-channel state changes.
     *
     * @param connected {@code true} when a channel becomes ready for service; {@code false} when
     *     the current channel leaves service.
     */
    void onStateTransition(boolean connected);
  }
}
