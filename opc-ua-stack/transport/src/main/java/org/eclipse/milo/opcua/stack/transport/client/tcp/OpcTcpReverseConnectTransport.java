/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;

/**
 * A client transport that listens for inbound server connections using the OPC UA Reverse Connect
 * mechanism (Part 6, Section 7.1.3).
 *
 * <p>Instead of the client connecting to the server, the client opens a listening socket and waits
 * for the server to connect inbound and send a {@code ReverseHello} message. After the ReverseHello
 * exchange, the protocol continues identically to normal connect.
 */
public class OpcTcpReverseConnectTransport extends AbstractUascClientTransport
    implements ChannelStateObservable {

  /**
   * Maximum size for a ReverseHello message: 8-byte header + (4 + 4096) ServerUri + (4 + 4096)
   * EndpointUrl.
   */
  private static final int MAX_REVERSE_HELLO_MESSAGE_SIZE = 8 + 4 + 4096 + 4 + 4096;

  private final OpcTcpReverseConnectTransportConfig config;
  private final ReverseConnectChannelFsm channelFsm;

  private final CopyOnWriteArrayList<ChannelStateObservable.TransitionListener> stateListeners =
      new CopyOnWriteArrayList<>();

  private volatile ServerBootstrap serverBootstrap;
  private volatile Channel serverChannel;

  /**
   * Create a new {@link OpcTcpReverseConnectTransport}.
   *
   * @param config the Reverse Connect transport configuration.
   */
  public OpcTcpReverseConnectTransport(OpcTcpReverseConnectTransportConfig config) {
    super(config);

    this.config = config;

    var fsmConfig =
        new ReverseConnectChannelFsm.ChannelFsmConfig(
            config, config.getAllowedServerUris(), config.getReverseHelloTimeout());

    this.channelFsm =
        ReverseConnectChannelFsm.create(
            fsmConfig, this, requestId::getAndIncrement, config.getExecutor());

    channelFsm.addTransitionListener(
        (from, to) -> {
          if (from == ReverseConnectChannelFsm.State.Connected
              && to != ReverseConnectChannelFsm.State.Connected) {
            stateListeners.forEach(l -> l.onConnectionStateChange(false));
          } else if (from != ReverseConnectChannelFsm.State.Connected
              && to == ReverseConnectChannelFsm.State.Connected) {
            stateListeners.forEach(l -> l.onConnectionStateChange(true));
          }
        });
  }

  @Override
  public OpcTcpReverseConnectTransportConfig getConfig() {
    return config;
  }

  /**
   * Start listening for inbound server connections and wait for a SecureChannel to be established.
   *
   * @param applicationContext the client application context.
   * @return a future that completes when the first SecureChannel is ready.
   */
  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    channelFsm.setApplicationContext(applicationContext);

    try {
      startListening();
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }

    var connect = new ReverseConnectChannelFsm.Event.Connect(new CompletableFuture<>());
    channelFsm.fireEvent(connect);
    return connect.future().thenApply(ch -> Unit.VALUE);
  }

  /**
   * Disconnect the current SecureChannel and stop listening for inbound connections.
   *
   * @return a future that completes when the disconnect is finished and the listening socket is
   *     closed.
   */
  @Override
  public CompletableFuture<Unit> disconnect() {
    var disconnect = new ReverseConnectChannelFsm.Event.Disconnect(new CompletableFuture<>());
    channelFsm.fireEvent(disconnect);

    // Use thenCompose with an async close to avoid blocking the FSM
    // executor thread with syncUninterruptibly(), which could deadlock
    // if the executor is the Netty event loop.
    return disconnect.future().thenCompose(v -> stopListeningAsync());
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    Channel ch = channelFsm.getChannel();
    if (ch != null && ch.isActive()) {
      return CompletableFuture.completedFuture(ch);
    }

    var get = new ReverseConnectChannelFsm.Event.GetChannel(new CompletableFuture<>());
    channelFsm.fireEvent(get);
    return get.future();
  }

  /**
   * Get the {@link ReverseConnectChannelFsm} used by this transport.
   *
   * @return the {@link ReverseConnectChannelFsm}.
   */
  public ReverseConnectChannelFsm getChannelFsm() {
    return channelFsm;
  }

  @Override
  public void addTransitionListener(ChannelStateObservable.TransitionListener listener) {
    stateListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(ChannelStateObservable.TransitionListener listener) {
    stateListeners.remove(listener);
  }

  private synchronized void startListening() {
    if (serverBootstrap != null) {
      return;
    }

    serverBootstrap =
        new ServerBootstrap()
            .channel(NioServerSocketChannel.class)
            .group(config.getEventLoop())
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ReverseHelloDecoder());

                    config.getChannelPipelineCustomizer().accept(ch.pipeline());
                  }
                });

    config.getServerBootstrapCustomizer().accept(serverBootstrap);

    try {
      serverChannel =
          serverBootstrap.bind(config.getListenAddress()).syncUninterruptibly().channel();
    } catch (Exception e) {
      serverBootstrap = null;
      serverChannel = null;
      throw e;
    }

    logger.info("Listening for reverse connections on {}", serverChannel.localAddress());
  }

  private synchronized CompletableFuture<Unit> stopListeningAsync() {
    if (serverChannel != null && serverChannel.isOpen()) {
      var addr = serverChannel.localAddress();
      Channel ch = serverChannel;
      serverBootstrap = null;
      serverChannel = null;

      var future = new CompletableFuture<Unit>();
      ch.close()
          .addListener(
              f -> {
                logger.info("Stopped listening on {}", addr);
                future.complete(Unit.VALUE);
              });
      return future;
    } else {
      serverBootstrap = null;
      serverChannel = null;
      return CompletableFuture.completedFuture(Unit.VALUE);
    }
  }

  /**
   * A minimal decoder installed on each accepted child channel. Its sole job is to read the first
   * message, verify it is a ReverseHello, decode it, and notify the FSM via a {@link
   * ReverseConnectChannelFsm.Event.ConnectionAccepted} event.
   *
   * <p>After successfully decoding the ReverseHello, this handler removes itself from the pipeline.
   * The FSM's handshake entry action then installs the full {@code UascClientReverseHelloHandler}.
   */
  private class ReverseHelloDecoder extends ByteToMessageDecoder implements HeaderDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception {

      if (in.readableBytes() < HEADER_LENGTH) {
        return;
      }

      int messageTypeInt = in.getMediumLE(in.readerIndex());
      MessageType messageType;
      try {
        messageType = MessageType.fromMediumInt(messageTypeInt);
      } catch (UaException e) {
        ExceptionHandler.sendErrorMessage(ctx, e);
        return;
      }

      if (messageType != MessageType.ReverseHello) {
        ExceptionHandler.sendErrorMessage(
            ctx,
            new UaException(
                StatusCodes.Bad_TcpMessageTypeInvalid,
                "expected ReverseHello, received " + messageType));
        return;
      }

      int messageLength = getMessageLength(in, MAX_REVERSE_HELLO_MESSAGE_SIZE);

      if (in.readableBytes() < messageLength) {
        return;
      }

      ByteBuf messageBuffer = in.readSlice(messageLength);

      ReverseHelloMessage rhe = TcpMessageDecoder.decodeReverseHello(messageBuffer);

      ctx.pipeline().remove(this);

      logger.debug(
          "Received ReverseHello: serverUri={}, endpointUrl={}",
          rhe.serverUri(),
          rhe.endpointUrl());

      channelFsm.fireEvent(
          new ReverseConnectChannelFsm.Event.ConnectionAccepted(ctx.channel(), rhe));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      logger.error("Exception in ReverseHelloDecoder", cause);
      ExceptionHandler.sendErrorMessage(ctx, cause);
    }
  }
}
