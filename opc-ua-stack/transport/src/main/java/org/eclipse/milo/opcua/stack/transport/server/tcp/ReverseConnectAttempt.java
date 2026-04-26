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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.channels.ClosedChannelException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.transport.server.uasc.SecureChannelOpenedEvent;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal bridge for one server-initiated Reverse Connect attempt.
 *
 * <p>The attempt owns only observation of the outbound connection lifecycle. Retry policy and
 * target-level idle-socket policy belong to the caller.
 */
public final class ReverseConnectAttempt {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Object lock = new Object();
  private final Consumer<Outcome> outcomeConsumer;
  private final CompletableFuture<Channel> connectedFuture = new CompletableFuture<>();
  private final CompletableFuture<Outcome> terminalOutcomeFuture = new CompletableFuture<>();

  private @Nullable Channel channel;
  private boolean tcpConnected = false;
  private boolean secureChannelOpened = false;
  private boolean terminalOutcomeReported = false;
  private @Nullable SecureChannelOpened pendingSecureChannelOpened = null;

  public ReverseConnectAttempt(Consumer<Outcome> outcomeConsumer) {
    this.outcomeConsumer = Objects.requireNonNull(outcomeConsumer);
  }

  public CompletableFuture<Channel> connectedFuture() {
    return connectedFuture;
  }

  public CompletableFuture<Outcome> terminalOutcomeFuture() {
    return terminalOutcomeFuture;
  }

  public void channelInitialized(Channel channel) {
    synchronized (lock) {
      if (this.channel == null) {
        this.channel = channel;
      }
    }
  }

  public void tcpConnectSucceeded(Channel channel) {
    SecureChannelOpened pendingOutcome;

    synchronized (lock) {
      if (this.channel == null) {
        this.channel = channel;
      }

      if (terminalOutcomeReported) {
        return;
      }

      tcpConnected = true;
      pendingOutcome = pendingSecureChannelOpened;
      pendingSecureChannelOpened = null;
    }

    connectedFuture.complete(channel);

    if (pendingOutcome != null) {
      emitOutcome(pendingOutcome);
    }
  }

  public void tcpConnectFailed(Throwable cause) {
    Throwable failure = cause != null ? cause : new IllegalStateException("TCP connect failed");
    var outcome = new TcpConnectFailure(failure);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(failure);
    }
  }

  public void reverseHelloWriteFailed(Throwable cause) {
    Throwable failure =
        cause != null ? cause : new IllegalStateException("ReverseHello write failed");
    var outcome = new ReverseHelloWriteFailure(requireChannel(), failure);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(failure);
    }
  }

  public void clientRejected(ErrorMessage errorMessage) {
    var outcome = new ClientRejected(requireChannel(), errorMessage);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(
          new IllegalStateException("Reverse Connect client rejected the attempt"));
    }
  }

  public void secureChannelOpened(long secureChannelId) {
    SecureChannelOpened outcomeToEmit = null;

    synchronized (lock) {
      if (terminalOutcomeReported || secureChannelOpened) {
        return;
      }

      secureChannelOpened = true;

      var outcome = new SecureChannelOpened(requireChannel(), secureChannelId);
      if (tcpConnected) {
        outcomeToEmit = outcome;
      } else {
        pendingSecureChannelOpened = outcome;
      }
    }

    if (outcomeToEmit != null) {
      emitOutcome(outcomeToEmit);
    }
  }

  public void channelInactive() {
    Outcome outcome;

    synchronized (lock) {
      if (terminalOutcomeReported) {
        return;
      }

      if (secureChannelOpened) {
        outcome = new CloseAfterSecureChannel(requireChannel());
      } else {
        outcome = new CloseBeforeSecureChannel(requireChannel());
      }
    }

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(new ClosedChannelException());
    }
  }

  private boolean reportTerminalOutcome(Outcome outcome) {
    synchronized (lock) {
      if (terminalOutcomeReported) {
        return false;
      }

      terminalOutcomeReported = true;
    }

    terminalOutcomeFuture.complete(outcome);
    emitOutcome(outcome);

    return true;
  }

  private Channel requireChannel() {
    synchronized (lock) {
      return Objects.requireNonNull(channel, "channel");
    }
  }

  private void emitOutcome(Outcome outcome) {
    try {
      outcomeConsumer.accept(outcome);
    } catch (Throwable t) {
      logger.warn("Reverse Connect attempt outcome callback failed.", t);
    }
  }

  public sealed interface Outcome
      permits TcpConnectFailure,
          ReverseHelloWriteFailure,
          ClientRejected,
          SecureChannelOpened,
          CloseBeforeSecureChannel,
          CloseAfterSecureChannel {}

  public record TcpConnectFailure(Throwable cause) implements Outcome {}

  public record ReverseHelloWriteFailure(Channel channel, Throwable cause) implements Outcome {}

  public record ClientRejected(Channel channel, ErrorMessage errorMessage) implements Outcome {}

  public record SecureChannelOpened(Channel channel, long secureChannelId) implements Outcome {}

  public record CloseBeforeSecureChannel(Channel channel) implements Outcome {}

  public record CloseAfterSecureChannel(Channel channel) implements Outcome {}

  static final class Observer extends ChannelInboundHandlerAdapter {

    private final ReverseConnectAttempt attempt;

    Observer(ReverseConnectAttempt attempt) {
      this.attempt = attempt;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
      attempt.channelInitialized(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if (evt instanceof SecureChannelOpenedEvent event) {
        attempt.secureChannelOpened(event.secureChannelId());
      }

      ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      attempt.channelInactive();
      ctx.fireChannelInactive();
    }
  }
}
