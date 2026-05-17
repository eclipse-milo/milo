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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
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
import org.eclipse.milo.opcua.stack.transport.server.uasc.SecureChannelOpenedEvent;
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
                                ReferenceCountUtil.release(msg);
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
  void connectAppliesPipelineCustomizer() throws Exception {
    Channel serverChannel = startAcceptingServer();

    try {
      var localAddress = (InetSocketAddress) serverChannel.localAddress();
      var customizerInvoked = new CompletableFuture<Void>();

      OpcTcpServerTransportConfig config =
          OpcTcpServerTransportConfig.newBuilder()
              .setEventLoop(eventLoop)
              .setExecutor(ForkJoinPool.commonPool())
              .setChannelPipelineCustomizer(
                  pipeline -> {
                    pipeline.addLast(
                        "reverse-connect-custom-handler", new ChannelInboundHandlerAdapter());
                    customizerInvoked.complete(null);
                  })
              .build();
      var transport = new OpcTcpReverseConnectServerTransport(config);

      Channel channel =
          transport
              .connect(newApplicationContext(), localAddress, SERVER_URI, ENDPOINT_URL, 5_000)
              .get(5, TimeUnit.SECONDS);

      assertNotNull(channel.pipeline().get("reverse-connect-custom-handler"));
      customizerInvoked.get(5, TimeUnit.SECONDS);

      channel.close().sync();
    } finally {
      serverChannel.close().sync();
    }
  }

  @Test
  void connectAppliesBootstrapChildOptions() throws Exception {
    Channel serverChannel = startAcceptingServer();

    try {
      var localAddress = (InetSocketAddress) serverChannel.localAddress();

      OpcTcpServerTransportConfig config =
          OpcTcpServerTransportConfig.newBuilder()
              .setEventLoop(eventLoop)
              .setExecutor(ForkJoinPool.commonPool())
              .setBootstrapCustomizer(b -> b.childOption(ChannelOption.SO_KEEPALIVE, true))
              .build();
      var transport = new OpcTcpReverseConnectServerTransport(config);

      Channel channel =
          transport
              .connect(newApplicationContext(), localAddress, SERVER_URI, ENDPOINT_URL, 5_000)
              .get(5, TimeUnit.SECONDS);

      assertTrue(((SocketChannel) channel).config().isKeepAlive());

      channel.close().sync();
    } finally {
      serverChannel.close().sync();
    }
  }

  @Test
  void connectDoesNotPropagateServerBootstrapParentState() throws Exception {
    Channel serverChannel = startAcceptingServer();

    AttributeKey<String> parentAttrKey =
        AttributeKey.newInstance("parent-only-attr-" + System.nanoTime());

    try {
      var localAddress = (InetSocketAddress) serverChannel.localAddress();

      OpcTcpServerTransportConfig config =
          OpcTcpServerTransportConfig.newBuilder()
              .setEventLoop(eventLoop)
              .setExecutor(ForkJoinPool.commonPool())
              .setBootstrapCustomizer(
                  b -> {
                    b.option(ChannelOption.SO_BACKLOG, 128);
                    b.attr(parentAttrKey, "parent-only");
                  })
              .build();
      var transport = new OpcTcpReverseConnectServerTransport(config);

      Channel channel =
          transport
              .connect(newApplicationContext(), localAddress, SERVER_URI, ENDPOINT_URL, 5_000)
              .get(5, TimeUnit.SECONDS);

      assertNull(channel.attr(parentAttrKey).get());

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

  @Test
  void connectAttemptReportsTcpConnectFailure() throws Exception {
    OpcTcpServerTransportConfig config = newConfig();
    var transport = new OpcTcpReverseConnectServerTransport(config);
    var outcomes = new LinkedBlockingQueue<ReverseConnectAttempt.Outcome>();

    // Use a port that is not listening.
    var unreachableAddress = new InetSocketAddress("127.0.0.1", 1);

    ReverseConnectAttempt attempt =
        transport.connectAttempt(
            newApplicationContext(),
            unreachableAddress,
            SERVER_URI,
            ENDPOINT_URL,
            5_000,
            outcomes::offer);

    var outcome =
        assertInstanceOf(
            ReverseConnectAttempt.TcpConnectFailure.class,
            attempt.terminalOutcomeFuture().get(10, TimeUnit.SECONDS));

    assertNotNull(outcome.cause());
    assertEquals(outcome, outcomes.poll(10, TimeUnit.SECONDS));
    assertThrows(ExecutionException.class, () -> attempt.connectedFuture().get());
  }

  @Test
  void connectAttemptInstallsSecureChannelObserverBeforeReverseHelloHandler() throws Exception {
    Channel serverChannel =
        new ServerBootstrap()
            .group(eventLoop)
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(
                            new ChannelInboundHandlerAdapter() {
                              @Override
                              public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ReferenceCountUtil.release(msg);
                              }
                            });
                  }
                })
            .bind(0)
            .sync()
            .channel();

    try {
      var outcomes = new LinkedBlockingQueue<ReverseConnectAttempt.Outcome>();
      var transport = new OpcTcpReverseConnectServerTransport(newConfig());

      ReverseConnectAttempt attempt =
          transport.connectAttempt(
              newApplicationContext(),
              (InetSocketAddress) serverChannel.localAddress(),
              SERVER_URI,
              ENDPOINT_URL,
              5_000,
              outcomes::offer);

      Channel channel = attempt.connectedFuture().get(5, TimeUnit.SECONDS);

      assertNotNull(channel.pipeline().get(ReverseConnectAttempt.Observer.class));
      assertNotNull(channel.pipeline().get(UascServerReverseHelloHandler.class));
      String observerName = channel.pipeline().context(ReverseConnectAttempt.Observer.class).name();
      String reverseHelloName =
          channel.pipeline().context(UascServerReverseHelloHandler.class).name();
      assertTrue(
          channel.pipeline().names().indexOf(observerName)
              < channel.pipeline().names().indexOf(reverseHelloName));

      channel.pipeline().fireUserEventTriggered(new SecureChannelOpenedEvent(789L));

      var outcome =
          assertInstanceOf(
              ReverseConnectAttempt.SecureChannelOpened.class, outcomes.poll(5, TimeUnit.SECONDS));
      assertEquals(789L, outcome.secureChannelId());

      channel.close().sync();
    } finally {
      serverChannel.close().sync();
    }
  }

  private OpcTcpServerTransportConfig newConfig() {
    return OpcTcpServerTransportConfig.newBuilder()
        .setEventLoop(eventLoop)
        .setExecutor(ForkJoinPool.commonPool())
        .build();
  }

  private static Channel startAcceptingServer() throws InterruptedException {
    return new ServerBootstrap()
        .group(eventLoop)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                    .addLast(
                        new ChannelInboundHandlerAdapter() {
                          @Override
                          public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            ReferenceCountUtil.release(msg);
                          }
                        });
              }
            })
        .bind(0)
        .sync()
        .channel();
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
