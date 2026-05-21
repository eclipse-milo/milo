/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.HashedWheelTimer;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientAcknowledgeHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.junit.jupiter.api.Test;

class OpcTcpClientChannelInitializerTest {

  @Test
  void outboundInitializerUsesApplicationEndpointUrl() throws Exception {
    String endpointUrl = "opc.tcp://localhost:12685/milo";
    EmbeddedChannel channel = new EmbeddedChannel();
    HashedWheelTimer wheelTimer = new HashedWheelTimer();

    try {
      OpcTcpClientChannelInitializer.initializeOutboundChannel(
          channel,
          newClientConfig(wheelTimer),
          newClientApplicationContext(endpointUrl),
          NoopResponseHandler.INSTANCE,
          new AtomicLong(1)::getAndIncrement,
          new CompletableFuture<>());

      assertClientPipelineInitialized(channel);
      assertHelloEndpointUrl(channel, endpointUrl);
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  @Test
  void connectedInitializerUsesExplicitEndpointUrl() throws Exception {
    String applicationEndpointUrl = "opc.tcp://configured.example:12685/milo";
    String reverseHelloEndpointUrl = "opc.tcp://reverse.example:12685/milo";
    EmbeddedChannel channel = new EmbeddedChannel();
    HashedWheelTimer wheelTimer = new HashedWheelTimer();

    try {
      OpcTcpClientChannelInitializer.initializeConnectedChannel(
          channel,
          newClientConfig(wheelTimer),
          newClientApplicationContext(applicationEndpointUrl),
          NoopResponseHandler.INSTANCE,
          new AtomicLong(1)::getAndIncrement,
          new CompletableFuture<>(),
          reverseHelloEndpointUrl);

      assertClientPipelineInitialized(channel);
      assertHelloEndpointUrl(channel, reverseHelloEndpointUrl);
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  @Test
  void connectedInitializerRejectsOffEventLoopCaller() throws Exception {
    String applicationEndpointUrl = "opc.tcp://configured.example:12685/milo";
    String reverseHelloEndpointUrl = "opc.tcp://reverse.example:12685/milo";
    EventLoopGroup eventLoop = new NioEventLoopGroup(1);
    Channel channel = new NioSocketChannel();
    HashedWheelTimer wheelTimer = new HashedWheelTimer();

    try {
      eventLoop.register(channel).sync();

      IllegalStateException exception =
          assertThrows(
              IllegalStateException.class,
              () ->
                  OpcTcpClientChannelInitializer.initializeConnectedChannel(
                      channel,
                      newClientConfig(wheelTimer),
                      newClientApplicationContext(applicationEndpointUrl),
                      NoopResponseHandler.INSTANCE,
                      new AtomicLong(1)::getAndIncrement,
                      new CompletableFuture<>(),
                      reverseHelloEndpointUrl));

      assertEquals(
          "initializeConnectedChannel must be invoked from the channel event loop",
          exception.getMessage());
    } finally {
      channel.close().syncUninterruptibly();
      eventLoop.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS).sync();
      wheelTimer.stop();
    }
  }

  @Test
  void connectedInitializerRunsCustomizerBeforeSendingHello() throws Exception {
    String applicationEndpointUrl = "opc.tcp://configured.example:12685/milo";
    String reverseHelloEndpointUrl = "opc.tcp://reverse.example:12685/milo";
    AtomicBoolean helloObserved = new AtomicBoolean(false);
    EmbeddedChannel channel = new EmbeddedChannel();
    HashedWheelTimer wheelTimer = new HashedWheelTimer();

    try {
      OpcTcpClientTransportConfig config =
          OpcTcpClientTransportConfig.newBuilder()
              .setWheelTimer(wheelTimer)
              .setChannelPipelineCustomizer(
                  pipeline -> pipeline.addLast(new HelloObservingHandler(helloObserved)))
              .build();

      OpcTcpClientChannelInitializer.initializeConnectedChannel(
          channel,
          config,
          newClientApplicationContext(applicationEndpointUrl),
          NoopResponseHandler.INSTANCE,
          new AtomicLong(1)::getAndIncrement,
          new CompletableFuture<>(),
          reverseHelloEndpointUrl);

      assertTrue(helloObserved.get());
      assertHelloEndpointUrl(channel, reverseHelloEndpointUrl);
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  private static OpcTcpClientTransportConfig newClientConfig(HashedWheelTimer wheelTimer) {
    return OpcTcpClientTransportConfig.newBuilder()
        .setWheelTimer(wheelTimer)
        .setChannelPipelineCustomizer(pipeline -> pipeline.addLast(new MarkerHandler()))
        .build();
  }

  private static void assertClientPipelineInitialized(EmbeddedChannel channel) {
    assertNotNull(channel.pipeline().get(DelegatingUascResponseHandler.class));
    assertNotNull(channel.pipeline().get(UascClientAcknowledgeHandler.class));
    assertNotNull(channel.pipeline().get(MarkerHandler.class));
  }

  private static void assertHelloEndpointUrl(EmbeddedChannel channel, String endpointUrl)
      throws Exception {

    ByteBuf messageBuffer = channel.readOutbound();
    assertNotNull(messageBuffer);

    try {
      HelloMessage hello = TcpMessageDecoder.decodeHello(messageBuffer);
      assertEquals(endpointUrl, hello.getEndpointUrl());
    } finally {
      messageBuffer.release();
    }
  }

  private static ClientApplicationContext newClientApplicationContext(String endpointUrl) {
    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return newEndpointDescription(endpointUrl);
      }

      @Override
      public Optional<KeyPair> getKeyPair() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate> getCertificate() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain() {
        return Optional.empty();
      }

      @Override
      public CertificateValidator getCertificateValidator() {
        return new CertificateValidator.InsecureCertificateValidator();
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public UInteger getRequestTimeout() {
        return uint(5_000);
      }
    };
  }

  private static EndpointDescription newEndpointDescription(String endpointUrl) {
    return new EndpointDescription(
        endpointUrl,
        new ApplicationDescription(
            "urn:server",
            "productUri",
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {endpointUrl}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static final class MarkerHandler extends ChannelInboundHandlerAdapter {}

  private static final class HelloObservingHandler extends ChannelOutboundHandlerAdapter {

    private final AtomicBoolean helloObserved;

    private HelloObservingHandler(AtomicBoolean helloObserved) {
      this.helloObserved = helloObserved;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
        throws Exception {

      if (msg instanceof ByteBuf byteBuf) {
        MessageType messageType =
            MessageType.fromMediumInt(byteBuf.getMediumLE(byteBuf.readerIndex()));

        if (messageType == MessageType.Hello) {
          helloObserved.set(true);
        }
      }

      super.write(ctx, msg, promise);
    }
  }

  private enum NoopResponseHandler implements UascResponseHandler {
    INSTANCE;

    @Override
    public void handleResponse(long requestId, UaResponseMessageType responseMessage) {}

    @Override
    public void handleSendFailure(long requestId, UaException e) {}

    @Override
    public void handleReceiveFailure(long requestId, UaException e) {}

    @Override
    public void handleChannelError(UaException exception) {}

    @Override
    public void handleChannelInactive() {}
  }
}
