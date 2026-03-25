/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

/**
 * Interface for transport implementations that can notify observers when the underlying channel
 * state changes.
 *
 * <p>Both {@code OpcTcpClientTransport} (forward connect) and {@code OpcTcpReverseConnectTransport}
 * (Reverse Connect) implement this interface, allowing {@code SessionFsm} to observe connection
 * state without depending on a specific transport type.
 */
public interface ChannelStateObservable {

  /** Listener notified when the underlying channel state changes. */
  interface TransitionListener {

    /**
     * Called when the channel transitions between states.
     *
     * @param connected true if the channel is now in a connected/active state.
     */
    void onConnectionStateChange(boolean connected);
  }

  /**
   * Add a listener to be notified of channel state transitions.
   *
   * @param listener the listener to add.
   */
  void addTransitionListener(TransitionListener listener);

  /**
   * Remove a previously added listener.
   *
   * @param listener the listener to remove.
   */
  void removeTransitionListener(TransitionListener listener);
}
