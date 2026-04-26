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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcTcpReverseConnectTransportTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private NioEventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private List<OpcTcpReverseConnectTransport> transports;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
    transports = new ArrayList<>();
  }

  @AfterEach
  void tearDown() throws Exception {
    for (OpcTcpReverseConnectTransport transport : transports) {
      transport.disconnect().get(5, TimeUnit.SECONDS);
    }

    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully().get(5, TimeUnit.SECONDS);
    wheelTimer.stop();
  }

  @Test
  void connectFailsWhenListenAddressIsAlreadyBound() throws Exception {
    InetAddress loopback = InetAddress.getLoopbackAddress();

    try (var serverSocket = new ServerSocket(0, 1, loopback)) {
      var listenAddress = new InetSocketAddress(loopback, serverSocket.getLocalPort());
      var transport = newTransport(listenAddress, new TestHandshakeStarter());

      CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());

      assertThrows(ExecutionException.class, () -> connectFuture.get(5, TimeUnit.SECONDS));
    }
  }

  @Test
  void disconnectWhileWaitingFailsConnectWaiterAndStopsListener() throws Exception {
    var transport = newTransport(new TestHandshakeStarter());

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    Channel listenerChannel = transport.getListenerChannel();

    assertNotNull(listenerChannel);
    assertFalse(connectFuture.isDone());

    assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));

    assertUaStatus(StatusCodes.Bad_ConnectionClosed, connectFuture);
    assertFalse(listenerChannel.isOpen());
  }

  @Test
  void disconnectWhileConnectedClosesActiveChannelAndNotifiesStateListeners() throws Exception {
    var handshakeStarter = new TestHandshakeStarter();
    var transport = newTransport(handshakeStarter);
    var states = new LinkedBlockingQueue<Boolean>();
    transport.addTransitionListener(states::add);

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    Channel listenerChannel = transport.getListenerChannel();

    try (Socket socket = connectAndSendReverseHello(listenerChannel)) {
      Handshake handshake = handshakeStarter.take();
      assertNotNull(handshake);
      handshakeStarter.succeed(handshake);

      assertEquals(Unit.VALUE, connectFuture.get(5, TimeUnit.SECONDS));
      assertSame(handshake.channel(), transport.getActiveChannel());
      assertEquals(Boolean.TRUE, states.poll(5, TimeUnit.SECONDS));

      assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));

      assertEquals(Boolean.FALSE, states.poll(5, TimeUnit.SECONDS));
      assertFalse(handshake.channel().isOpen());
      assertFalse(listenerChannel.isOpen());
      assertSocketCloses(socket);
    }
  }

  @Test
  void getChannelAfterDisconnectFailsWithoutCreatingAWaiter() throws Exception {
    var transport = newTransport(new TestHandshakeStarter());

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));

    assertUaStatus(StatusCodes.Bad_ConnectionClosed, connectFuture);
    assertUaStatus(StatusCodes.Bad_Shutdown, transport.getChannel());
  }

  private OpcTcpReverseConnectTransport newTransport(TestHandshakeStarter handshakeStarter) {
    return newTransport(new InetSocketAddress("localhost", 0), handshakeStarter);
  }

  private OpcTcpReverseConnectTransport newTransport(
      InetSocketAddress listenAddress, TestHandshakeStarter handshakeStarter) {

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(listenAddress)
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .setEventLoop(eventLoop)
            .setWheelTimer(wheelTimer)
            .setReverseHelloTimeout(5_000)
            .setConnectTimeout(100)
            .build();

    var transport = new OpcTcpReverseConnectTransport(config, handshakeStarter);
    transports.add(transport);

    return transport;
  }

  private static Socket connectAndSendReverseHello(Channel listenerChannel) throws Exception {
    var address = (InetSocketAddress) listenerChannel.localAddress();
    var socket = new Socket(address.getAddress(), address.getPort());
    socket.setSoTimeout(5_000);

    ByteBuf buffer = TcpMessageEncoder.encode(reverseHello());
    try {
      var bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);
      socket.getOutputStream().write(bytes);
    } finally {
      buffer.release();
    }

    return socket;
  }

  private static ReverseHelloMessage reverseHello() {
    return new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
  }

  private static void assertSocketCloses(Socket socket) throws Exception {
    while (socket.getInputStream().read() != -1) {
      // Drain any bytes before EOF.
    }
  }

  private static ClientApplicationContext newApplicationContext() {
    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return new EndpointDescription(
            ENDPOINT_URL,
            null,
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            TransportProfile.TCP_UASC_UABINARY.getUri(),
            ubyte(0));
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

  private static void assertUaStatus(long expectedStatus, CompletableFuture<?> future) {
    try {
      future.get(1, TimeUnit.SECONDS);
      fail("Expected UaException");
    } catch (ExecutionException e) {
      UaException uaException = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(expectedStatus, uaException.getStatusCode().value());
    } catch (Exception e) {
      fail("Expected UaException", e);
    }
  }

  private record Handshake(Channel channel, CompletableFuture<ClientSecureChannel> future) {}

  private static final class TestHandshakeStarter
      implements ReverseConnectChannelOwner.HandshakeStarter {

    private final LinkedBlockingQueue<Handshake> handshakes = new LinkedBlockingQueue<>();

    @Override
    public CompletableFuture<ClientSecureChannel> start(
        Channel channel,
        ReverseHelloMessage reverseHello,
        UascClientConfig config,
        ClientApplicationContext application,
        UascResponseHandler responseHandler,
        java.util.function.Supplier<Long> requestIdSupplier,
        Set<String> allowedServerUris,
        long reverseHelloTimeoutMs) {

      var future = new CompletableFuture<ClientSecureChannel>();
      handshakes.add(new Handshake(channel, future));

      return future;
    }

    Handshake take() throws Exception {
      return handshakes.poll(5, TimeUnit.SECONDS);
    }

    void succeed(Handshake handshake) {
      var secureChannel = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
      secureChannel.setChannel(handshake.channel());

      handshake.future().complete(secureChannel);
    }
  }
}
