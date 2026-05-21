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

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.ConnectException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CancellationException;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opens server-initiated UA-TCP sockets for Reverse Connect and hands them back to the normal
 * server UASC pipeline.
 *
 * <p>This class owns only the pre-{@code Hello} transport step. A caller supplies one {@link
 * OpcTcpServerReverseConnectParameters} value for a selected client reverse listener and server
 * endpoint. The connector opens an outbound TCP socket, sends {@link ReverseHelloMessage}, and then
 * installs {@link OpcTcpServerReverseConnectResponseHandler} plus the normal server {@code Hello}
 * path from {@link OpcTcpServerChannelInitializer}. Once the first client {@code Hello} arrives,
 * the attempt reaches {@link OpcTcpServerReverseConnectAttemptState#HANDOFF}; subsequent {@code
 * Acknowledge}, SecureChannel, and service behavior belongs to the existing server UASC handlers.
 *
 * <p>Each call to {@link #connect(OpcTcpServerReverseConnectParameters)} creates an {@link
 * OpcTcpServerReverseConnectAttempt}. The attempt future is completed when the channel reaches the
 * handoff point, completed exceptionally for connect/protocol failures, or cancelled when callers
 * cancel the attempt. Attempt observers receive a monotonic state stream; once a terminal state is
 * observed, later asynchronous Netty callbacks are ignored rather than regressing the state.
 *
 * <p>The successful attempt state sequence is {@link
 * OpcTcpServerReverseConnectAttemptState#CONNECTING} to {@link
 * OpcTcpServerReverseConnectAttemptState#CONNECTED} to {@link
 * OpcTcpServerReverseConnectAttemptState#REVERSE_HELLO_SENT} to {@link
 * OpcTcpServerReverseConnectAttemptState#HELLO_HANDLER_INSTALLED} to terminal {@link
 * OpcTcpServerReverseConnectAttemptState#HANDOFF}. The terminal error sequence is one of {@link
 * OpcTcpServerReverseConnectAttemptState#CLIENT_ERROR}, {@link
 * OpcTcpServerReverseConnectAttemptState#FAILED}, {@link
 * OpcTcpServerReverseConnectAttemptState#CANCELLED}, or {@link
 * OpcTcpServerReverseConnectAttemptState#CLOSED}. A terminal state completes, fails, or cancels the
 * attempt future and prevents later Netty callbacks from publishing another terminal state.
 *
 * <p>The connector remains responsible for channels it opened until it is closed, even after an
 * attempt reaches handoff. Connector shutdown closes connector-owned reverse sockets without making
 * attempt handles or target lifecycle operations responsible for channels that already entered the
 * server UASC path.
 *
 * <p>Threading is deliberately narrow. Netty writes and pipeline mutations happen on the channel's
 * event loop. The connector lock protects only lifecycle bookkeeping: the closed flag, open
 * attempts, and open channels. Attempt observers are invoked synchronously on the thread that emits
 * the transition, which may be an event-loop thread, so observer implementations should avoid
 * blocking work.
 */
final class OpcTcpServerReverseConnector implements AutoCloseable {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Object lock = new Object();
  private final OpcTcpServerTransportConfig config;
  private final Set<OpcTcpServerReverseConnectAttempt> attempts = new HashSet<>();
  private final Set<Channel> channels = new HashSet<>();

  private boolean closed = false;

  OpcTcpServerReverseConnector(OpcTcpServerTransportConfig config) {
    this.config = config;
  }

  /**
   * Start one asynchronous server-initiated reverse-connect attempt.
   *
   * <p>The attempt uses the configured event loop, allocator, TCP options, reverse-connect
   * bootstrap customizer, and server channel pipeline customizer. The returned handle is the
   * caller's control point for the attempt future, cancellation, and state observation; this method
   * does not block for TCP connection, {@code ReverseHello} flush, or client {@code Hello}.
   *
   * @param parameters the listener destination, advertised endpoint values, timeout, application
   *     context, and observer for this attempt.
   * @return the attempt handle.
   * @throws IllegalStateException if the connector has already been closed.
   */
  OpcTcpServerReverseConnectAttempt connect(OpcTcpServerReverseConnectParameters parameters) {
    var attempt = new OpcTcpServerReverseConnectAttempt(this, parameters);
    Bootstrap bootstrap =
        new Bootstrap()
            .channel(NioSocketChannel.class)
            .group(config.getEventLoop())
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, parameters.connectTimeoutMillis())
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel channel) {}
                });

    config.getReverseConnectBootstrapCustomizer().accept(bootstrap);

    ChannelFuture connectFuture;
    synchronized (lock) {
      if (closed) {
        throw new IllegalStateException("OpcTcpServerReverseConnector is closed");
      }

      attempts.add(attempt);
      connectFuture = bootstrap.connect(parameters.clientListenerAddress());
      attempt.setConnectFuture(connectFuture);
    }

    attempt.transition(
        OpcTcpServerReverseConnectAttemptState.CONNECTING,
        null,
        null,
        "connecting to " + parameters.clientListenerAddress());

    connectFuture.addListener(
        (ChannelFutureListener)
            future -> {
              if (!future.isSuccess()) {
                onConnectFailed(attempt, future.cause());
                return;
              }

              Channel channel = future.channel();
              if (isClosed() || attempt.isComplete()) {
                closeAttempt(
                    attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse connector closed");
                closeChannel(channel);
                return;
              }

              attempt.setChannel(channel);

              addChannel(channel);
              channel.closeFuture().addListener(f -> onChannelClosed(attempt, channel));

              attempt.transition(
                  OpcTcpServerReverseConnectAttemptState.CONNECTED,
                  null,
                  null,
                  "connected to " + parameters.clientListenerAddress());

              channel.eventLoop().execute(() -> writeReverseHello(attempt, channel));
            });

    return attempt;
  }

  /**
   * Complete an attempt when the client's first {@code Hello} has been accepted for handoff.
   *
   * <p>{@link OpcTcpServerReverseConnectResponseHandler} calls this immediately before forwarding
   * the retained {@code Hello} frame to the normal {@code UascServerHelloHandler}. At this point
   * the attempt leaves the pre-{@code Hello} attempt set, but the connector still tracks the
   * channel so connector shutdown can close server-owned reverse sockets.
   */
  void onHelloReceived(OpcTcpServerReverseConnectAttempt attempt, Channel channel) {
    if (attempt.handoff(channel, "client Hello received; channel handed off to server UASC path")) {
      removeAttempt(attempt);
    }
  }

  /**
   * Fail an attempt when the client listener responds with {@code ERR/F} instead of {@code HEL/F}.
   *
   * <p>A client-side reverse listener may reject a reverse connection before the normal UASC
   * handshake starts, for example because the advertised endpoint URL or server URI did not match
   * any pending client interest. That response is a terminal attempt result, not input for the
   * normal server {@code Hello} handler.
   */
  void onClientError(
      OpcTcpServerReverseConnectAttempt attempt, ChannelHandlerContext ctx, ErrorMessage error) {

    var statusCode = error.getError();
    String reason = error.getReason();
    String message =
        reason != null && !reason.isBlank()
            ? "client rejected reverse connection: " + reason
            : "client rejected reverse connection";

    var exception = new UaException(statusCode, message);

    if (attempt.fail(
        OpcTcpServerReverseConnectAttemptState.CLIENT_ERROR, exception, statusCode, message)) {
      removeAttempt(attempt);
    }

    ctx.close();
  }

  /**
   * Fail an attempt when the first post-{@code ReverseHello} message is malformed or unexpected.
   *
   * <p>The reverse response handler owns only the narrow pre-{@code Hello} grammar: final-chunk
   * {@code HEL/F} for handoff or {@code ERR/F} for rejection. Any other message type, chunk type,
   * or invalid frame length is surfaced as an attempt failure and the channel is closed.
   */
  void onFirstMessageInvalid(
      OpcTcpServerReverseConnectAttempt attempt, ChannelHandlerContext ctx, Throwable cause) {

    StatusCode statusCode =
        UaException.extractStatusCode(cause)
            .orElse(new StatusCode(StatusCodes.Bad_TcpMessageTypeInvalid));
    String message = cause.getMessage() != null ? cause.getMessage() : cause.toString();

    failAndClose(attempt, statusCode, cause, message, ctx.channel());
  }

  /**
   * Cancel a caller-owned attempt before it reaches handoff.
   *
   * <p>The attempt future is marked terminal before the Netty connect future is cancelled. Netty
   * can notify listeners inline when cancellation is initiated from the channel event loop, so this
   * ordering prevents the connect listener from reporting a cancelled attempt as a generic connect
   * failure.
   */
  void cancel(OpcTcpServerReverseConnectAttempt attempt) {
    var statusCode = new StatusCode(StatusCodes.Bad_RequestCancelledByClient);
    var exception = new CancellationException("reverse-connect attempt cancelled");
    String message = "reverse-connect attempt cancelled";

    if (attempt.cancelFuture(statusCode, exception, message)) {
      ChannelFuture connectFuture = attempt.connectFuture();
      if (connectFuture != null) {
        connectFuture.cancel(false);
      }

      closeChannel(attempt.channel());
      removeAttempt(attempt);
    } else if (attempt.state() == OpcTcpServerReverseConnectAttemptState.HANDOFF) {
      removeAttempt(attempt);
    }
  }

  /**
   * Close a caller-owned attempt before it reaches handoff.
   *
   * <p>Closing is used by the attempt handle and by connector shutdown. It completes the attempt
   * exceptionally with the supplied shutdown state, cancels any pending connect future, and closes
   * any channel already associated with the attempt.
   */
  void close(OpcTcpServerReverseConnectAttempt attempt) {
    closeAttempt(
        attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse-connect attempt closed");
  }

  /**
   * Close the connector and every reverse-connect channel it still owns.
   *
   * <p>After this method starts, no new attempts can be created. Attempts that have not reached a
   * terminal state are completed as closed, and channels that have reached handoff are still closed
   * because they were opened by this connector. The method is idempotent.
   */
  @Override
  public void close() {
    Set<OpcTcpServerReverseConnectAttempt> attemptsSnapshot;
    Set<Channel> channelsSnapshot;

    synchronized (lock) {
      if (closed) {
        return;
      }

      closed = true;
      attemptsSnapshot = Set.copyOf(attempts);
      channelsSnapshot = Set.copyOf(channels);
      channels.clear();
    }

    attemptsSnapshot.forEach(
        attempt ->
            closeAttempt(
                attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse connector closed"));

    channelsSnapshot.forEach(this::closeChannel);
  }

  /**
   * Emit one attempt state transition to the attempt observer.
   *
   * <p>The observer is invoked synchronously and exceptions are contained so that observability
   * code cannot disrupt the transport state machine. Because transitions may be emitted from Netty
   * event loops, observers should record or enqueue work rather than perform blocking operations.
   */
  void emitStateTransition(
      OpcTcpServerReverseConnectAttempt attempt,
      OpcTcpServerReverseConnectAttemptState state,
      @Nullable StatusCode statusCode,
      @Nullable Throwable exception,
      @Nullable String message) {

    var event =
        new OpcTcpServerReverseConnectAttemptEvent(
            attempt.id(), state, Instant.now(), statusCode, exception, message);

    try {
      attempt.parameters().observer().onStateTransition(event);
    } catch (Throwable t) {
      logger.warn("Reverse-connect attempt observer failed.", t);
    }
  }

  /**
   * Write {@code ReverseHello(serverUri, endpointUrl)} after TCP connection succeeds.
   *
   * <p>This is the only message the server sends before the normal UASC server handshake begins.
   * Handler installation is intentionally delayed until the write future succeeds so that an early
   * client {@code Hello} cannot be processed before the server's {@code ReverseHello} has been
   * flushed.
   */
  private void writeReverseHello(OpcTcpServerReverseConnectAttempt attempt, Channel channel) {
    if (isClosed() || attempt.isComplete()) {
      closeAttempt(attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse connector closed");
      closeChannel(channel);
      return;
    }

    ByteBuf messageBuffer;
    try {
      messageBuffer =
          TcpMessageEncoder.encode(
              new ReverseHelloMessage(
                  attempt.parameters().serverUri(), attempt.parameters().endpointUrl()));
    } catch (Throwable t) {
      failAndClose(
          attempt,
          UaException.extractStatusCode(t).orElse(new StatusCode(StatusCodes.Bad_TcpInternalError)),
          t,
          "failed to encode ReverseHello",
          channel);
      return;
    }

    channel
        .writeAndFlush(messageBuffer)
        .addListener(
            (ChannelFutureListener)
                future -> {
                  if (!future.isSuccess()) {
                    failAndClose(
                        attempt,
                        new StatusCode(StatusCodes.Bad_ConnectionClosed),
                        future.cause(),
                        "failed to write ReverseHello",
                        channel);
                    return;
                  }

                  attempt.transition(
                      OpcTcpServerReverseConnectAttemptState.REVERSE_HELLO_SENT,
                      null,
                      null,
                      "ReverseHello sent");

                  installServerHelloPath(attempt, channel);
                });
  }

  /**
   * Install the pre-{@code Hello} response guard and the reusable server {@code Hello} pipeline.
   *
   * <p>The response guard recognizes client {@code ERR/F} as a reverse-connect rejection and
   * forwards {@code HEL/F} into the normal server handler. {@link
   * OpcTcpServerChannelInitializer#initializeReverseChannel(Channel, OpcTcpServerTransportConfig,
   * org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext)} installs the same
   * {@code UascServerHelloHandler} path used by passive listener channels, minus passive-listener
   * rate limiting.
   */
  private void installServerHelloPath(OpcTcpServerReverseConnectAttempt attempt, Channel channel) {

    if (isClosed() || attempt.isComplete()) {
      closeAttempt(attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse connector closed");
      closeChannel(channel);
      return;
    }

    channel.pipeline().addLast(new OpcTcpServerReverseConnectResponseHandler(this, attempt));
    OpcTcpServerChannelInitializer.initializeReverseChannel(
        channel, config, attempt.parameters().applicationContext());

    attempt.transition(
        OpcTcpServerReverseConnectAttemptState.HELLO_HANDLER_INSTALLED,
        null,
        null,
        "server Hello handler installed");
  }

  /**
   * Convert an outbound TCP connect failure into a terminal attempt failure.
   *
   * <p>If the connector was closed while the connect was in flight, the failure is reported as
   * connector closure instead of a network error. This distinction keeps later scheduling and
   * observability code from retrying shutdown-induced failures as if the client listener rejected
   * the connection.
   */
  private void onConnectFailed(
      OpcTcpServerReverseConnectAttempt attempt, @Nullable Throwable cause) {

    if (attempt.isComplete()) {
      return;
    }

    if (isClosed()) {
      closeAttempt(attempt, new StatusCode(StatusCodes.Bad_Shutdown), "reverse connector closed");
      return;
    }

    StatusCode statusCode = connectFailureStatusCode(cause);
    Throwable exception =
        cause != null
            ? new UaException(statusCode.value(), "reverse-connect TCP connect failed", cause)
            : new UaException(statusCode, "reverse-connect TCP connect failed");

    if (attempt.fail(
        OpcTcpServerReverseConnectAttemptState.FAILED,
        exception,
        statusCode,
        exception.getMessage())) {
      removeAttempt(attempt);
    }
  }

  /**
   * Observe channel closure before handoff and convert it into a terminal attempt state.
   *
   * <p>After handoff or other terminal states, channel closure is ordinary channel cleanup; before
   * handoff it means the reverse listener disappeared or closed the socket without producing a
   * usable {@code Hello} or {@code ErrorMessage}.
   */
  private void onChannelClosed(OpcTcpServerReverseConnectAttempt attempt, Channel channel) {

    removeChannel(channel);

    if (!attempt.isComplete()) {
      failAttempt(
          attempt,
          new StatusCode(StatusCodes.Bad_ConnectionClosed),
          "reverse-connect channel closed before Hello");
    }
  }

  /**
   * Complete an attempt as closed and release any connect/channel resources it owns.
   *
   * <p>The attempt is marked terminal before the Netty connect future is cancelled so inline Netty
   * cancellation callbacks cannot report a later non-terminal or failed state.
   */
  private void closeAttempt(
      OpcTcpServerReverseConnectAttempt attempt, StatusCode statusCode, String message) {

    if (attempt.state() == OpcTcpServerReverseConnectAttemptState.HANDOFF) {
      removeAttempt(attempt);
      return;
    }

    var exception = new UaException(statusCode, message);
    boolean completed =
        attempt.fail(OpcTcpServerReverseConnectAttemptState.CLOSED, exception, statusCode, message);

    if (completed) {
      ChannelFuture connectFuture = attempt.connectFuture();
      if (connectFuture != null) {
        connectFuture.cancel(false);
      }

      closeChannel(attempt.channel());
      removeAttempt(attempt);
    } else if (attempt.state() == OpcTcpServerReverseConnectAttemptState.HANDOFF) {
      removeAttempt(attempt);
    }
  }

  /**
   * Complete an attempt as failed without closing the channel.
   *
   * <p>This helper is used when the channel has already closed before the attempt reached handoff.
   */
  private void failAttempt(
      OpcTcpServerReverseConnectAttempt attempt, StatusCode statusCode, String message) {

    var exception = new UaException(statusCode, message);

    if (attempt.fail(
        OpcTcpServerReverseConnectAttemptState.FAILED, exception, statusCode, message)) {
      removeAttempt(attempt);
    }
  }

  /**
   * Complete an attempt as failed and close its channel.
   *
   * <p>This helper is used for protocol, encoding, and write failures that occur after a channel
   * has already been assigned to the attempt.
   */
  private void failAndClose(
      OpcTcpServerReverseConnectAttempt attempt,
      StatusCode statusCode,
      Throwable cause,
      String message,
      Channel channel) {

    var exception = new UaException(statusCode.value(), message, cause);

    if (attempt.fail(
        OpcTcpServerReverseConnectAttemptState.FAILED, exception, statusCode, message)) {
      closeChannel(channel);
      removeAttempt(attempt);
    } else if (attempt.state() != OpcTcpServerReverseConnectAttemptState.HANDOFF) {
      closeChannel(channel);
      removeAttempt(attempt);
    }
  }

  private boolean isClosed() {
    synchronized (lock) {
      return closed;
    }
  }

  private void addChannel(Channel channel) {
    synchronized (lock) {
      channels.add(channel);
    }
  }

  private void removeChannel(Channel channel) {
    synchronized (lock) {
      channels.remove(channel);
    }
  }

  private void removeAttempt(OpcTcpServerReverseConnectAttempt attempt) {
    synchronized (lock) {
      attempts.remove(attempt);
    }
  }

  private void closeChannel(@Nullable Channel channel) {
    if (channel != null && channel.isOpen()) {
      channel.close();
    }
  }

  private static StatusCode connectFailureStatusCode(@Nullable Throwable cause) {
    if (cause instanceof ConnectTimeoutException) {
      return new StatusCode(StatusCodes.Bad_Timeout);
    } else if (cause instanceof ConnectException) {
      return new StatusCode(StatusCodes.Bad_ConnectionRejected);
    } else {
      return new StatusCode(StatusCodes.Bad_ConnectionClosed);
    }
  }
}
