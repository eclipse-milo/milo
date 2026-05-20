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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/** Handle for one server-initiated reverse-connect attempt. */
final class OpcTcpServerReverseConnectAttempt implements AutoCloseable {

  private final UUID id = UUID.randomUUID();
  private final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
  private final AtomicBoolean completed = new AtomicBoolean(false);
  private final AtomicReference<OpcTcpServerReverseConnectAttemptState> state =
      new AtomicReference<>(OpcTcpServerReverseConnectAttemptState.CONNECTING);

  private final OpcTcpServerReverseConnector connector;
  private final OpcTcpServerReverseConnectParameters parameters;

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
  UUID id() {
    return id;
  }

  /**
   * Get the current attempt state.
   *
   * @return the current attempt state.
   */
  OpcTcpServerReverseConnectAttemptState state() {
    return state.get();
  }

  /**
   * Get the future completed when this attempt enters the normal server UASC path.
   *
   * @return the channel future.
   */
  CompletableFuture<Channel> channelFuture() {
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

  boolean complete(Channel channel) {
    if (completed.compareAndSet(false, true)) {
      channelFuture.complete(channel);
      return true;
    } else {
      return false;
    }
  }

  boolean fail(Throwable cause) {
    if (completed.compareAndSet(false, true)) {
      channelFuture.completeExceptionally(cause);
      return true;
    } else {
      return false;
    }
  }

  boolean cancelFuture() {
    if (completed.compareAndSet(false, true)) {
      channelFuture.cancel(false);
      return true;
    } else {
      return false;
    }
  }

  void transition(
      OpcTcpServerReverseConnectAttemptState nextState,
      @Nullable StatusCode statusCode,
      @Nullable Throwable exception,
      @Nullable String message) {

    if (isComplete() && !isTerminalState(nextState)) {
      return;
    }

    state.set(nextState);
    connector.emitStateTransition(this, nextState, statusCode, exception, message);
  }

  private static boolean isTerminalState(OpcTcpServerReverseConnectAttemptState state) {
    return switch (state) {
      case HANDOFF, CLIENT_ERROR, FAILED, CANCELLED, CLOSED -> true;
      case CONNECTING, CONNECTED, REVERSE_HELLO_SENT, HELLO_HANDLER_INSTALLED -> false;
    };
  }

  /** Cancel this attempt and close any channel it opened. */
  void cancel() {
    connector.cancel(this);
  }

  /** Close this attempt and close any channel it opened. */
  @Override
  public void close() {
    connector.close(this);
  }
}
