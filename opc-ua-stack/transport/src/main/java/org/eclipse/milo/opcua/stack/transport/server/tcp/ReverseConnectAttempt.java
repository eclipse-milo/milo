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
 * Observes one server-initiated Reverse Connect attempt.
 *
 * <p>{@link OpcTcpReverseConnectServerTransport} creates an attempt for each outbound TCP dial and
 * wires its observer into the Netty pipeline. The attempt publishes connection and handshake
 * observations through the supplied outcome consumer while leaving retry policy and idle-socket
 * ownership to the caller.
 *
 * <p>{@link SecureChannelOpened} is an informational outcome that may be followed later by a close
 * outcome for the same channel. All other outcomes are terminal for the attempt, and only the first
 * terminal outcome is reported.
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

  /**
   * Create a new attempt observer.
   *
   * @param outcomeConsumer the consumer notified when attempt outcomes are observed.
   */
  public ReverseConnectAttempt(Consumer<Outcome> outcomeConsumer) {
    this.outcomeConsumer = Objects.requireNonNull(outcomeConsumer);
  }

  /**
   * Get the TCP connection future for this attempt.
   *
   * <p>The future completes with the channel after TCP connect succeeds. It completes exceptionally
   * if TCP connect fails, the ReverseHello write fails, the client rejects the reverse handshake, or
   * the channel closes before TCP connect success is reported.
   *
   * @return the future completed when the outbound TCP connection succeeds.
   */
  public CompletableFuture<Channel> connectedFuture() {
    return connectedFuture;
  }

  /**
   * Get the terminal outcome future for this attempt.
   *
   * <p>This future completes with the first terminal outcome. A {@link SecureChannelOpened} outcome
   * is not terminal and is delivered only to the outcome consumer.
   *
   * @return the future completed with the first terminal attempt outcome.
   */
  public CompletableFuture<Outcome> terminalOutcomeFuture() {
    return terminalOutcomeFuture;
  }

  /**
   * Record the channel associated with this attempt.
   *
   * <p>This is normally called when the observer is added to the Netty pipeline. Later lifecycle
   * callbacks use the recorded channel when publishing outcomes.
   *
   * @param channel the channel opened for this attempt.
   */
  public void channelInitialized(Channel channel) {
    synchronized (lock) {
      if (this.channel == null) {
        this.channel = channel;
      }
    }
  }

  /**
   * Report that the outbound TCP connect completed successfully.
   *
   * <p>If a SecureChannel opened before the TCP connect callback arrived, this method also publishes
   * the buffered {@link SecureChannelOpened} outcome.
   *
   * @param channel the connected channel.
   */
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

  /**
   * Report that the outbound TCP connect failed.
   *
   * @param cause the connect failure, or {@code null} to use a generic failure.
   */
  public void tcpConnectFailed(Throwable cause) {
    Throwable failure = cause != null ? cause : new IllegalStateException("TCP connect failed");
    var outcome = new TcpConnectFailure(failure);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(failure);
    }
  }

  /**
   * Report that the ReverseHello write failed after TCP connect.
   *
   * @param cause the write failure, or {@code null} to use a generic failure.
   */
  public void reverseHelloWriteFailed(Throwable cause) {
    Throwable failure =
        cause != null ? cause : new IllegalStateException("ReverseHello write failed");
    var outcome = new ReverseHelloWriteFailure(requireChannel(), failure);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(failure);
    }
  }

  /**
   * Report that the client rejected the reverse handshake with an OPC UA Error message.
   *
   * @param errorMessage the error message received from the client.
   */
  public void clientRejected(ErrorMessage errorMessage) {
    var outcome = new ClientRejected(requireChannel(), errorMessage);

    if (reportTerminalOutcome(outcome)) {
      connectedFuture.completeExceptionally(
          new IllegalStateException("Reverse Connect client rejected the attempt"));
    }
  }

  /**
   * Report that the reverse-connected channel opened a SecureChannel.
   *
   * <p>This publishes a non-terminal {@link SecureChannelOpened} outcome. If TCP connect success has
   * not yet been reported, the outcome is buffered until {@link #tcpConnectSucceeded(Channel)}
   * arrives so callers see a connected channel before the secure-channel-open observation.
   *
   * @param secureChannelId the server-assigned SecureChannel id.
   */
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

  /**
   * Report that the attempt channel became inactive.
   *
   * <p>If no terminal outcome has already been reported, this publishes either {@link
   * CloseBeforeSecureChannel} or {@link CloseAfterSecureChannel} depending on whether a
   * SecureChannel had opened.
   */
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

  /** Observation published by a Reverse Connect attempt. */
  public sealed interface Outcome
      permits TcpConnectFailure,
          ReverseHelloWriteFailure,
          ClientRejected,
          SecureChannelOpened,
          CloseBeforeSecureChannel,
          CloseAfterSecureChannel {}

  /**
   * Terminal outcome reported when the outbound TCP connect fails.
   *
   * @param cause the connect failure.
   */
  public record TcpConnectFailure(Throwable cause) implements Outcome {}

  /**
   * Terminal outcome reported when the server cannot write ReverseHello.
   *
   * @param channel the connected channel.
   * @param cause the write failure.
   */
  public record ReverseHelloWriteFailure(Channel channel, Throwable cause) implements Outcome {}

  /**
   * Terminal outcome reported when the client sends an OPC UA Error during the reverse handshake.
   *
   * @param channel the connected channel.
   * @param errorMessage the error message received from the client.
   */
  public record ClientRejected(Channel channel, ErrorMessage errorMessage) implements Outcome {}

  /**
   * Non-terminal outcome reported when the reverse-connected channel opens a SecureChannel.
   *
   * @param channel the connected channel.
   * @param secureChannelId the server-assigned SecureChannel id.
   */
  public record SecureChannelOpened(Channel channel, long secureChannelId) implements Outcome {}

  /**
   * Terminal outcome reported when the channel closes before a SecureChannel opens.
   *
   * @param channel the connected channel.
   */
  public record CloseBeforeSecureChannel(Channel channel) implements Outcome {}

  /**
   * Terminal outcome reported when the channel closes after a SecureChannel opens.
   *
   * @param channel the connected channel.
   */
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
