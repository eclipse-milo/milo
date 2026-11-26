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

  /**
   * Get the state of this transport's channel.
   *
   * <p>The client's Session handling reads this when an attempt to open or recover a Session fails.
   * Over a transport that is {@link ChannelState#DISCONNECTED}, it abandons the Session instead of
   * reactivating it, and it does not report the failed attempts to {@code SessionActivityListener}:
   * nothing connects the transport again until the application does.
   *
   * @return the state of this transport's channel.
   */
  ChannelState getChannelState();

  /**
   * The state of a client transport's channel, as {@link #getChannelState()} reports it.
   *
   * <p>Each transport documents how its own connection lifecycle maps onto these states.
   */
  enum ChannelState {

    /**
     * No channel is ready, and the transport makes no attempt to establish one on its own. A
     * transport is disconnected before it is first connected and after the application disconnects
     * it, until it is connected again. A transport that can no longer connect at all is
     * disconnected too.
     */
    DISCONNECTED,

    /** The transport is establishing a channel because it was asked to connect. */
    CONNECTING,

    /** A channel is ready for service. */
    CONNECTED,

    /**
     * No channel is ready, and the transport is establishing one again on its own, after a failed
     * attempt or a lost channel. This includes the wait before its next attempt.
     */
    RECONNECTING,

    /** The application asked the transport to disconnect, and its channel is being closed. */
    DISCONNECTING
  }

  /**
   * Callback notified when a client transport gains or loses a usable channel, and when an attempt
   * to establish one fails.
   */
  @FunctionalInterface
  interface TransitionListener {

    /**
     * Invoked when the transport's connected-channel state changes.
     *
     * @param connected {@code true} when a channel becomes ready for service; {@code false} when
     *     the current channel leaves service.
     */
    void onStateTransition(boolean connected);

    /**
     * Invoked when a connection attempt fails before a channel becomes ready for service.
     *
     * <p>The transport keeps reconnecting on its own schedule; this callback lets the application
     * react to the cause, for example by refreshing cached discovery information after a security
     * failure, before the next attempt reads {@link ClientApplicationContext#getEndpoint()}.
     *
     * <p>Failures during SecureChannel establishment must include a {@link
     * SecureChannelHandshakeException} in the cause chain. Transport connection and
     * Hello/Acknowledge failures must not use that marker. This lets callers refresh discovery
     * without depending on particular server status codes.
     *
     * @param failure the cause of the failed attempt, including the server status when available.
     */
    default void onConnectFailure(Throwable failure) {}
  }
}
