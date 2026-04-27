/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerReverseHelloHandler;

/**
 * Transport-layer class that creates outbound Netty connections for Reverse Connect and wires the
 * server pipeline onto them.
 *
 * <p>This is <b>not</b> an {@link org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport}
 * — it does not implement {@code bind()}/{@code unbind()}. It is used internally by the Reverse
 * Connect manager to initiate outbound TCP connections to clients.
 */
public class OpcTcpReverseConnectServerTransport {

  private final OpcTcpServerTransportConfig config;

  public OpcTcpReverseConnectServerTransport(OpcTcpServerTransportConfig config) {
    this.config = config;
  }

  /**
   * Create a transport-owned Reverse Connect target lifecycle handle.
   *
   * @param clientEndpointUrl the client's listening endpoint URL.
   * @param endpointUrl the server endpoint URL sent in ReverseHello messages.
   * @param serverUri the server's ApplicationUri sent in ReverseHello messages.
   * @param applicationContext the server application context.
   * @param reverseConnectConfig the Reverse Connect timing configuration.
   * @param executor the executor used to serialize target lifecycle events.
   * @param scheduler the scheduler used for retry timers.
   * @return a target handle that keeps retry and idle-socket policy inside the transport layer.
   */
  public ReverseConnectTarget createTarget(
      String clientEndpointUrl,
      String endpointUrl,
      String serverUri,
      ServerApplicationContext applicationContext,
      ReverseConnectConfig reverseConnectConfig,
      Executor executor,
      ScheduledExecutorService scheduler) {

    return new ReverseConnectTarget(
        new ReverseConnectTargetOwner(
            clientEndpointUrl,
            endpointUrl,
            serverUri,
            this,
            applicationContext,
            reverseConnectConfig,
            executor,
            scheduler));
  }

  /**
   * Initiate a reverse connection to a client.
   *
   * @param applicationContext the server application context.
   * @param clientAddress the client's listening address.
   * @param serverUri the server's ApplicationUri for the ReverseHello.
   * @param endpointUrl the server's endpoint URL for the ReverseHello.
   * @param connectTimeoutMs the TCP connect timeout in milliseconds.
   * @return a future that completes with the connected channel.
   */
  public CompletableFuture<Channel> connect(
      ServerApplicationContext applicationContext,
      InetSocketAddress clientAddress,
      String serverUri,
      String endpointUrl,
      long connectTimeoutMs) {

    return connectAttempt(
            applicationContext,
            clientAddress,
            serverUri,
            endpointUrl,
            connectTimeoutMs,
            outcome -> {})
        .connectedFuture();
  }

  protected ReverseConnectAttempt connectAttempt(
      ServerApplicationContext applicationContext,
      InetSocketAddress clientAddress,
      String serverUri,
      String endpointUrl,
      long connectTimeoutMs,
      Consumer<ReverseConnectAttempt.Outcome> outcomeConsumer) {

    var attempt = new ReverseConnectAttempt(outcomeConsumer);

    var bootstrap = new Bootstrap();
    bootstrap
        .channel(NioSocketChannel.class)
        .group(config.getEventLoop())
        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeoutMs)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) {
                attempt.channelInitialized(ch);

                ch.pipeline()
                    .addLast(new ReverseConnectAttempt.Observer(attempt))
                    .addLast(
                        new UascServerReverseHelloHandler(
                            config,
                            applicationContext,
                            TransportProfile.TCP_UASC_UABINARY,
                            serverUri,
                            endpointUrl,
                            attempt::reverseHelloWriteFailed,
                            attempt::clientRejected));
              }
            });

    bootstrap
        .connect(clientAddress)
        .addListener(
            (ChannelFuture cf) -> {
              if (cf.isSuccess()) {
                attempt.tcpConnectSucceeded(cf.channel());
              } else {
                attempt.tcpConnectFailed(cf.cause());
              }
            });

    return attempt;
  }
}
