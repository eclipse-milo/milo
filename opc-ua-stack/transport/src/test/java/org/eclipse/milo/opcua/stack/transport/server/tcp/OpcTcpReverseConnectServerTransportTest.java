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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerReverseHelloHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OpcTcpReverseConnectServerTransportTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private static EventLoopGroup eventLoop;

  @BeforeAll
  static void beforeAll() {
    eventLoop = new NioEventLoopGroup(1);
  }

  @AfterAll
  static void afterAll() {
    eventLoop.shutdownGracefully();
  }

  @Test
  void connectSucceedsWhenTargetReachable() throws Exception {
    // Start a simple server socket to accept the connection
    Channel serverChannel =
        new ServerBootstrap()
            .group(eventLoop)
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) {
                    // Accept the connection but don't process messages
                    ch.pipeline()
                        .addLast(
                            new ChannelInboundHandlerAdapter() {
                              @Override
                              public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                // discard
                              }
                            });
                  }
                })
            .bind(0)
            .sync()
            .channel();

    try {
      var localAddress = (InetSocketAddress) serverChannel.localAddress();

      OpcTcpServerTransportConfig config = newConfig();
      var transport = new OpcTcpReverseConnectServerTransport(config);

      CompletableFuture<Channel> future =
          transport.connect(newApplicationContext(), localAddress, SERVER_URI, ENDPOINT_URL, 5_000);

      Channel channel = future.get(5, TimeUnit.SECONDS);
      assertNotNull(channel);

      // Verify the pipeline contains UascServerReverseHelloHandler
      assertNotNull(channel.pipeline().get(UascServerReverseHelloHandler.class));

      channel.close().sync();
    } finally {
      serverChannel.close().sync();
    }
  }

  @Test
  void connectFailsWhenTargetUnreachable() {
    OpcTcpServerTransportConfig config = newConfig();
    var transport = new OpcTcpReverseConnectServerTransport(config);

    // Use a port that is not listening
    var unreachableAddress = new InetSocketAddress("127.0.0.1", 1);

    CompletableFuture<Channel> future =
        transport.connect(
            newApplicationContext(), unreachableAddress, SERVER_URI, ENDPOINT_URL, 5_000);

    assertThrows(ExecutionException.class, () -> future.get(10, TimeUnit.SECONDS));
  }

  private OpcTcpServerTransportConfig newConfig() {
    return OpcTcpServerTransportConfig.newBuilder()
        .setEventLoop(eventLoop)
        .setExecutor(ForkJoinPool.commonPool())
        .build();
  }

  private static ServerApplicationContext newApplicationContext() {
    return new ServerApplicationContext() {
      private final AtomicLong secureChannelId = new AtomicLong(0L);
      private final AtomicLong secureChannelTokenId = new AtomicLong(0L);

      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of(
            new EndpointDescription(
                ENDPOINT_URL,
                new ApplicationDescription(
                    SERVER_URI,
                    "productUri",
                    LocalizedText.NULL_VALUE,
                    ApplicationType.Server,
                    null,
                    null,
                    new String[] {ENDPOINT_URL}),
                ByteString.NULL_VALUE,
                MessageSecurityMode.None,
                "http://opcfoundation.org/UA/SecurityPolicy#None",
                new UserTokenPolicy[] {
                  new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
                },
                TransportProfile.TCP_UASC_UABINARY.getUri(),
                ubyte(0)));
      }

      @Override
      public CertificateManager getCertificateManager() {
        throw new UnsupportedOperationException();
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public Long getNextSecureChannelId() {
        return secureChannelId.getAndIncrement();
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        return secureChannelTokenId.getAndIncrement();
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException());
      }
    };
  }
}
