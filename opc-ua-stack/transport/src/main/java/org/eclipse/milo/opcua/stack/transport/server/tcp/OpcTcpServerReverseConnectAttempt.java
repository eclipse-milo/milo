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

import static java.util.Objects.requireNonNull;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Handle for one low-level server-initiated reverse-connect attempt.
 *
 * <p>An attempt owns the TCP connect, the initial {@code ReverseHello} write, and the transition
 * into the normal server-side UASC {@code Hello} handler. Callers use the returned future to learn
 * when handoff succeeds, or use {@link #cancel()} and {@link #close()} to stop work before handoff.
 * After {@link OpcTcpServerReverseConnectAttemptState#HANDOFF}, the channel belongs to the normal
 * server transport path rather than this attempt handle.
 */
public final class OpcTcpServerReverseConnectAttempt {

  private final UUID id = UUID.randomUUID();
  private final Object stateLock = new Object();
  private final Deque<PendingTransition> pendingTransitions = new ArrayDeque<>();
  private final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
  private final AtomicBoolean completed = new AtomicBoolean(false);
  private final AtomicReference<OpcTcpServerReverseConnectAttemptState> state =
      new AtomicReference<>(OpcTcpServerReverseConnectAttemptState.CONNECTING);

  private final OpcTcpServerReverseConnector connector;
  private final OpcTcpServerReverseConnectParameters parameters;

  private boolean emittingTransitions = false;
  private volatile @Nullable Channel channel;
  private volatile @Nullable ChannelFuture connectFuture;

  OpcTcpServerReverseConnectAttempt(
      OpcTcpServerReverseConnector connector, OpcTcpServerReverseConnectParameters parameters) {

    this.connector = connector;
    this.parameters = parameters;
  }

  /**
   * Get the attempt identifier.
   *
   * @return the attempt identifier.
   */
  public UUID id() {
    return id;
  }

  /**
   * Get the current attempt state.
   *
   * @return the current attempt state.
   */
  public OpcTcpServerReverseConnectAttemptState state() {
    return state.get();
  }

  /**
   * Get the future completed when this attempt enters the normal server UASC path.
   *
   * <p>The future completes with the handed-off channel after the client sends {@code Hello}. It is
   * completed exceptionally or cancelled when the attempt fails before handoff, is cancelled, or is
   * closed.
   *
   * @return the channel future.
   */
  public CompletableFuture<Channel> channelFuture() {
    return channelFuture;
  }

  OpcTcpServerReverseConnectParameters parameters() {
    return parameters;
  }

  @Nullable Channel channel() {
    return channel;
  }

  void setChannel(Channel channel) {
    this.channel = requireNonNull(channel, "channel");
  }

  @Nullable ChannelFuture connectFuture() {
    return connectFuture;
  }

  void setConnectFuture(ChannelFuture connectFuture) {
    this.connectFuture = requireNonNull(connectFuture, "connectFuture");
  }

  boolean isComplete() {
    return completed.get();
  }

  boolean handoff(Channel channel) {
    if (completeTerminal(
        OpcTcpServerReverseConnectAttemptState.HANDOFF,
        null,
        null,
        "client Hello received; channel handed off to server UASC path")) {
      emitPendingTransitions();
      channelFuture.complete(channel);
      return true;
    } else {
      return false;
    }
  }

  boolean fail(
      OpcTcpServerReverseConnectAttemptState state,
      Throwable cause,
      @Nullable StatusCode statusCode,
      @Nullable String message) {

    if (completeTerminal(state, statusCode, cause, message)) {
      channelFuture.completeExceptionally(cause);
      emitPendingTransitions();
      return true;
    } else {
      return false;
    }
  }

  boolean cancelFuture(StatusCode statusCode, Throwable cause, String message) {
    if (completeTerminal(
        OpcTcpServerReverseConnectAttemptState.CANCELLED, statusCode, cause, message)) {
      channelFuture.cancel(false);
      emitPendingTransitions();
      return true;
    } else {
      return false;
    }
  }

  void transition(OpcTcpServerReverseConnectAttemptState nextState, @Nullable String message) {

    synchronized (stateLock) {
      while (true) {
        OpcTcpServerReverseConnectAttemptState currentState = state.get();
        if (!isTerminalState(nextState) && isTerminalState(currentState)) {
          return;
        }
        if (state.compareAndSet(currentState, nextState)) {
          pendingTransitions.addLast(new PendingTransition(nextState, null, null, message));
          break;
        }
      }
    }

    emitPendingTransitions();
  }

  private boolean completeTerminal(
      OpcTcpServerReverseConnectAttemptState terminalState,
      @Nullable StatusCode statusCode,
      @Nullable Throwable cause,
      @Nullable String message) {

    if (!isTerminalState(terminalState)) {
      throw new IllegalArgumentException("not a terminal state: " + terminalState);
    }

    synchronized (stateLock) {
      while (true) {
        OpcTcpServerReverseConnectAttemptState currentState = state.get();
        if (isTerminalState(currentState)) {
          return false;
        }
        if (state.compareAndSet(currentState, terminalState)) {
          completed.set(true);
          pendingTransitions.addLast(
              new PendingTransition(terminalState, statusCode, cause, message));
          return true;
        }
      }
    }
  }

  private void emitPendingTransitions() {
    while (true) {
      PendingTransition transition;

      synchronized (stateLock) {
        if (emittingTransitions) {
          return;
        }

        transition = pendingTransitions.pollFirst();
        if (transition == null) {
          return;
        }

        emittingTransitions = true;
      }

      try {
        connector.emitStateTransition(
            this,
            transition.state(),
            transition.statusCode(),
            transition.exception(),
            transition.message());
      } finally {
        synchronized (stateLock) {
          emittingTransitions = false;
        }
      }
    }
  }

  private static boolean isTerminalState(OpcTcpServerReverseConnectAttemptState state) {
    return switch (state) {
      case HANDOFF, CLIENT_ERROR, FAILED, CANCELLED, CLOSED -> true;
      case CONNECTING, CONNECTED, REVERSE_HELLO_SENT, HELLO_HANDLER_INSTALLED -> false;
    };
  }

  /**
   * Cancel this attempt and close any channel it opened before handoff.
   *
   * <p>Cancellation is a lifecycle decision by the caller, for example because a scheduler paused
   * or removed the owning target. It emits a cancelled transition and completes the channel future
   * as cancelled if the attempt has not already completed.
   */
  public void cancel() {
    connector.cancel(this);
  }

  /**
   * Close this attempt and close any channel it opened.
   *
   * <p>Do not use this method as try-with-resources cleanup after a successful handoff; once the
   * attempt reaches {@link OpcTcpServerReverseConnectAttemptState#HANDOFF}, the opened channel is
   * owned by the normal server SecureChannel and Session path.
   */
  public void close() {
    connector.close(this);
  }

  private record PendingTransition(
      OpcTcpServerReverseConnectAttemptState state,
      @Nullable StatusCode statusCode,
      @Nullable Throwable exception,
      @Nullable String message) {}
}
