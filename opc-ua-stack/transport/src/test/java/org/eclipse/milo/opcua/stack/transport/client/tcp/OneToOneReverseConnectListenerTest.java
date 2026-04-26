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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OneToOneReverseConnectListenerTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private NioEventLoopGroup eventLoop;
  private List<OneToOneReverseConnectListener> listeners;

  @BeforeEach
  void setUp() {
    eventLoop = new NioEventLoopGroup(1);
    listeners = new ArrayList<>();
  }

  @AfterEach
  void tearDown() throws Exception {
    for (OneToOneReverseConnectListener listener : listeners) {
      listener.stop().get(5, TimeUnit.SECONDS);
    }

    eventLoop.shutdownGracefully().get(5, TimeUnit.SECONDS);
  }

  @Test
  void startBindsAndStopUnbindsListenerChannel() throws Exception {
    var listener = newListener(new CapturingSink());

    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);
    assertTrue(listenerChannel.isActive());

    assertEquals(Unit.VALUE, listener.stop().get(5, TimeUnit.SECONDS));

    assertFalse(listenerChannel.isOpen());
  }

  @Test
  void startFutureFailsWhenListenAddressIsAlreadyBound() throws Exception {
    var firstListener = newListener(new CapturingSink());
    Channel firstChannel = firstListener.start().get(5, TimeUnit.SECONDS);
    var address = (InetSocketAddress) firstChannel.localAddress();

    var secondListener =
        newListener(
            new CapturingSink(),
            new InetSocketAddress(address.getAddress(), address.getPort()),
            30_000,
            pipeline -> {});

    CompletableFuture<Channel> secondStart = secondListener.start();

    assertThrows(ExecutionException.class, () -> secondStart.get(5, TimeUnit.SECONDS));
  }

  @Test
  void reverseHelloIsDecodedAndPostedToSink() throws Exception {
    var sink = new CapturingSink();
    var listener = newListener(sink);
    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);

    try (var socket = connectTo(listenerChannel)) {
      writeReverseHello(socket);

      Accepted accepted = sink.take();
      assertNotNull(accepted);
      assertNotNull(accepted.channel());
      assertTrue(accepted.channel().isActive());
      assertEquals(reverseHello().serverUri(), accepted.reverseHello().serverUri());
      assertEquals(reverseHello().endpointUrl(), accepted.reverseHello().endpointUrl());
    }
  }

  @Test
  void stopWaitsForListenerAndAcceptedChildChannelsToClose() throws Exception {
    var childAccepted = new CountDownLatch(1);
    var listener = newListener(new CapturingSink(), 30_000, pipeline -> childAccepted.countDown());
    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);

    try (var socket = connectTo(listenerChannel)) {
      assertTrue(childAccepted.await(5, TimeUnit.SECONDS));

      assertEquals(Unit.VALUE, listener.stop().get(5, TimeUnit.SECONDS));

      assertFalse(listenerChannel.isOpen());
      assertSocketCloses(socket);
    }
  }

  @Test
  void stopClosesAcceptedChildChannelsWhenListenerCloseFails() throws Exception {
    var childAccepted = new CountDownLatch(1);
    var childCloseAttempted = new CountDownLatch(1);
    var listenerChannelRef = new AtomicReference<Channel>();
    var listenerCloseFailure = new RuntimeException("listener close failed");

    var listener =
        newListenerWithChannelCloser(
            new CapturingSink(),
            30_000,
            pipeline -> childAccepted.countDown(),
            channel -> {
              CompletableFuture<Void> closeFuture = closeChannel(channel);
              if (channel == listenerChannelRef.get()) {
                return closeFuture.thenCompose(
                    v -> CompletableFuture.failedFuture(listenerCloseFailure));
              }

              childCloseAttempted.countDown();
              return closeFuture;
            });

    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);
    listenerChannelRef.set(listenerChannel);

    try (var socket = connectTo(listenerChannel)) {
      assertTrue(childAccepted.await(5, TimeUnit.SECONDS));

      ExecutionException ex =
          assertThrows(ExecutionException.class, () -> listener.stop().get(5, TimeUnit.SECONDS));

      assertSame(listenerCloseFailure, ex.getCause());
      assertTrue(childCloseAttempted.await(5, TimeUnit.SECONDS));
      assertSocketCloses(socket);
    }
  }

  @Test
  void invalidFirstMessageSendsErrorAndDoesNotPostAcceptedChannel() throws Exception {
    var sink = new CapturingSink();
    var listener = newListener(sink);
    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);

    try (var socket = connectTo(listenerChannel)) {
      socket.getOutputStream().write(helloHeader());

      assertNull(sink.poll());
      assertSocketCloses(socket);
    }
  }

  @Test
  void reverseHelloTimeoutClosesIdleAcceptedSocket() throws Exception {
    var listener = newListener(new CapturingSink(), 100, pipeline -> {});
    Channel listenerChannel = listener.start().get(5, TimeUnit.SECONDS);

    try (var socket = connectTo(listenerChannel)) {
      assertSocketCloses(socket);
    }
  }

  private OneToOneReverseConnectListener newListener(CapturingSink sink) {
    return newListener(sink, 30_000, pipeline -> {});
  }

  private OneToOneReverseConnectListener newListener(
      CapturingSink sink, long reverseHelloTimeout, Consumer<ChannelPipeline> customizer) {

    return newListener(
        sink, new InetSocketAddress("localhost", 0), reverseHelloTimeout, customizer);
  }

  private OneToOneReverseConnectListener newListener(
      CapturingSink sink,
      InetSocketAddress listenAddress,
      long reverseHelloTimeout,
      Consumer<ChannelPipeline> customizer) {

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(listenAddress)
            .setEventLoop(eventLoop)
            .setReverseHelloTimeout(reverseHelloTimeout)
            .setChannelPipelineCustomizer(customizer)
            .build();

    var listener = new OneToOneReverseConnectListener(config, sink);
    listeners.add(listener);

    return listener;
  }

  private OneToOneReverseConnectListener newListenerWithChannelCloser(
      CapturingSink sink,
      long reverseHelloTimeout,
      Consumer<ChannelPipeline> customizer,
      Function<Channel, CompletableFuture<Void>> channelCloser) {

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setEventLoop(eventLoop)
            .setReverseHelloTimeout(reverseHelloTimeout)
            .setChannelPipelineCustomizer(customizer)
            .build();

    var listener = new OneToOneReverseConnectListener(config, sink, channelCloser);
    listeners.add(listener);

    return listener;
  }

  private static Socket connectTo(Channel listenerChannel) throws Exception {
    var address = (InetSocketAddress) listenerChannel.localAddress();
    var socket = new Socket(address.getAddress(), address.getPort());
    socket.setSoTimeout(5_000);
    return socket;
  }

  private static CompletableFuture<Void> closeChannel(Channel channel) {
    var future = new CompletableFuture<Void>();
    channel
        .close()
        .addListener(
            f -> {
              if (f.isSuccess()) {
                future.complete(null);
              } else {
                future.completeExceptionally(f.cause());
              }
            });
    return future;
  }

  private static void writeReverseHello(Socket socket) throws Exception {
    ByteBuf buffer = TcpMessageEncoder.encode(reverseHello());
    try {
      socket.getOutputStream().write(toBytes(buffer));
    } finally {
      buffer.release();
    }
  }

  private static byte[] helloHeader() {
    ByteBuf buffer = Unpooled.buffer(8);
    try {
      buffer.writeMediumLE(MessageType.toMediumInt(MessageType.Hello));
      buffer.writeByte('F');
      buffer.writeIntLE(8);

      return toBytes(buffer);
    } finally {
      buffer.release();
    }
  }

  private static byte[] toBytes(ByteBuf buffer) {
    var bytes = new byte[buffer.readableBytes()];
    buffer.readBytes(bytes);
    return bytes;
  }

  private static ReverseHelloMessage reverseHello() {
    return new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
  }

  private static void assertSocketCloses(Socket socket) throws Exception {
    while (socket.getInputStream().read() != -1) {
      // Drain an Error response, if one is written before the listener closes.
    }
  }

  private record Accepted(Channel channel, ReverseHelloMessage reverseHello) {}

  private static final class CapturingSink
      implements OneToOneReverseConnectListener.AcceptedChannelSink {

    private final LinkedBlockingQueue<Accepted> accepted = new LinkedBlockingQueue<>();

    @Override
    public void accepted(Channel channel, ReverseHelloMessage reverseHello) {
      accepted.add(new Accepted(channel, reverseHello));
    }

    Accepted take() throws Exception {
      return accepted.poll(5, TimeUnit.SECONDS);
    }

    Accepted poll() throws Exception {
      return accepted.poll(200, TimeUnit.MILLISECONDS);
    }
  }
}
