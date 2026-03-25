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

    var future = new CompletableFuture<Channel>();

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
                ch.pipeline()
                    .addLast(
                        new UascServerReverseHelloHandler(
                            config,
                            applicationContext,
                            TransportProfile.TCP_UASC_UABINARY,
                            serverUri,
                            endpointUrl));
              }
            });

    bootstrap
        .connect(clientAddress)
        .addListener(
            (ChannelFuture cf) -> {
              if (cf.isSuccess()) {
                future.complete(cf.channel());
              } else {
                future.completeExceptionally(cf.cause());
              }
            });

    return future;
  }
}
