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

import static org.eclipse.milo.opcua.stack.transport.TestPortAllocator.allocatePort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OpcTcpServerTransportTest {

  private static final String SERVER_URI = "urn:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  private EventLoopGroup eventLoop;

  @AfterEach
  void tearDown() throws Exception {
    if (eventLoop != null) {
      eventLoop.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS).sync();
    }
  }

  @Test
  void unbindRejectsLaterReverseConnectWithoutOpeningSocket() throws Exception {
    eventLoop = new NioEventLoopGroup(1);

    OpcTcpServerTransportConfig config =
        OpcTcpServerTransportConfig.newBuilder().setEventLoop(eventLoop).build();

    var transport = new OpcTcpServerTransport(config);

    try (ServerSocket listener = new ServerSocket()) {
      listener.bind(new InetSocketAddress("127.0.0.1", 0));
      listener.setSoTimeout(200);

      transport.unbind();

      var parameters =
          OpcTcpServerReverseConnectParameters.fromAddress(
              newServerApplicationContext(),
              new InetSocketAddress("127.0.0.1", listener.getLocalPort()),
              ENDPOINT_URL,
              SERVER_URI,
              1_000,
              event -> {});

      IllegalStateException exception =
          assertThrows(IllegalStateException.class, () -> transport.connectReverse(parameters));

      assertEquals("transport is unbound", exception.getMessage());
      assertThrows(SocketTimeoutException.class, listener::accept);
    }
  }

  // Unbind is the server teardown barrier; returning with accepted channels still open lets one
  // test invocation leak network activity into the next.
  @Test
  void unbindClosesAcceptedChannelsBeforeReturning() throws Exception {
    eventLoop = new NioEventLoopGroup(1);
    var acceptedChannel = new AtomicReference<Channel>();
    var channelAccepted = new CountDownLatch(1);

    OpcTcpServerTransportConfig config =
        OpcTcpServerTransportConfig.newBuilder()
            .setEventLoop(eventLoop)
            .setChannelPipelineCustomizer(
                pipeline -> {
                  acceptedChannel.set(pipeline.channel());
                  channelAccepted.countDown();
                })
            .build();

    var transport = new OpcTcpServerTransport(config);
    var bindAddress = new InetSocketAddress("localhost", allocatePort());

    try (var socket = new Socket()) {
      transport.bind(newServerApplicationContext(), bindAddress);
      socket.connect(bindAddress);

      assertTrue(channelAccepted.await(3, TimeUnit.SECONDS));
      Channel channel = acceptedChannel.get();

      transport.unbind();

      assertFalse(channel.isOpen());
      assertTrue(channel.closeFuture().isDone());
    } finally {
      transport.unbind();
    }
  }

  private static ServerApplicationContext newServerApplicationContext() {
    return new ServerApplicationContext() {

      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of();
      }

      @Override
      public CertificateManager getCertificateManager() {
        throw new UnsupportedOperationException();
      }

      @Override
      public EncodingContext getEncodingContext() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Long getNextSecureChannelId() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        throw new UnsupportedOperationException();
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {

        throw new UnsupportedOperationException();
      }
    };
  }
}
