/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm.State;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcTcpMultiplexedReverseConnectTransportTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private NioEventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;

  private MultiplexedReverseConnectListener listener;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
  }

  @AfterEach
  void tearDown() throws Exception {
    if (listener != null) {
      listener.stop().get(5, TimeUnit.SECONDS);
      listener = null;
    }
    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully();
    wheelTimer.stop();
  }

  @Test
  void connectRegistersWithListener() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    transport.connect(newApplicationContext());

    // Verify registration: send an RHE and check it reaches the transport's FSM
    Thread.sleep(100); // allow registration to complete

    var transitionFuture = new CompletableFuture<State>();
    transport.getChannelFsm().addTransitionListener((from, to) -> transitionFuture.complete(to));

    try (Socket socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      State state = transitionFuture.get(5, TimeUnit.SECONDS);
      assertEquals(State.Handshaking, state);
    }

    transport.disconnect().get(5, TimeUnit.SECONDS);
  }

  @Test
  void disconnectDeregistersFromListener() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    transport.connect(newApplicationContext());
    Thread.sleep(100);
    transport.disconnect().get(5, TimeUnit.SECONDS);

    // After disconnect, an RHE for this ServerUri should be unmatched (channel closed).
    try (Socket socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      int result = socket.getInputStream().read();
      assertEquals(-1, result, "channel should be closed (EOF)");
    }
  }

  @Test
  void connectionAcceptedCompletesConnectFuture() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    CompletableFuture<Void> connectFuture =
        transport.connect(newApplicationContext()).thenApply(u -> null);
    Thread.sleep(100);
    assertFalse(connectFuture.isDone());

    // Simulate a ConnectionAccepted → Handshaking → Connected flow on the FSM
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    transport
        .getChannelFsm()
        .fireEvent(new ReverseConnectChannelFsm.Event.ConnectionAccepted(channel, rhe));
    awaitFsmState(transport, State.Handshaking);

    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    transport.getChannelFsm().fireEvent(new ReverseConnectChannelFsm.Event.HandshakeSuccess(sc));
    awaitFsmState(transport, State.Connected);

    // connect future should now be completed
    connectFuture.get(5, TimeUnit.SECONDS);
  }

  @Test
  void channelInactiveTriggersReconnecting() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    transport.connect(newApplicationContext());
    Thread.sleep(100);

    // Drive to Connected
    var channel = new EmbeddedChannel();
    reachConnected(transport, channel);
    assertEquals(State.Connected, transport.getChannelFsm().getState());

    // Simulate channel inactive
    transport.getChannelFsm().fireEvent(new ReverseConnectChannelFsm.Event.ChannelInactive());
    awaitFsmState(transport, State.Reconnecting);

    // Transport should still be registered (so the next RHE routes back to it)
    var transitionFuture = new CompletableFuture<State>();
    transport.getChannelFsm().addTransitionListener((from, to) -> transitionFuture.complete(to));

    try (Socket socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      State state = transitionFuture.get(5, TimeUnit.SECONDS);
      assertEquals(State.Handshaking, state);
    }

    transport.disconnect().get(5, TimeUnit.SECONDS);
  }

  @Test
  void stateListenerNotifiedOnConnectionChange() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    var connected = new AtomicBoolean(false);
    var disconnected = new AtomicBoolean(false);
    transport.addTransitionListener(
        c -> {
          if (c) connected.set(true);
          else disconnected.set(true);
        });

    transport.connect(newApplicationContext());
    Thread.sleep(100);

    // Drive to Connected
    var channel = new EmbeddedChannel();
    reachConnected(transport, channel);
    Thread.sleep(100);
    assertTrue(connected.get(), "listener should have been notified of connection");

    // Disconnect
    transport.disconnect().get(5, TimeUnit.SECONDS);
    Thread.sleep(100);
    assertTrue(disconnected.get(), "listener should have been notified of disconnection");
  }

  @Test
  void getChannelReturnsActiveChannel() throws Exception {
    listener = createAndStartListener();
    OpcTcpMultiplexedReverseConnectTransport transport =
        listener.createTransport(SERVER_URI, createConfig());

    transport.connect(newApplicationContext());
    Thread.sleep(100);

    // Drive to Connected
    var channel = new EmbeddedChannel();
    reachConnected(transport, channel);

    Channel result = transport.getChannelFsm().getChannel();
    assertNotNull(result);
    assertEquals(channel, result);

    transport.disconnect().get(5, TimeUnit.SECONDS);
  }

  @Test
  void offerChannel_consumedByConnect() throws Exception {
    var transport = createTransportDirect();

    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    transport.offerChannel(channel, rhe);

    transport.connect(newApplicationContext());

    awaitFsmState(transport, State.Handshaking);
    assertTrue(channel.isActive(), "offered channel should still be active");
  }

  @Test
  void offerChannel_channelClosedBeforeConnect() throws Exception {
    var transport = createTransportDirect();

    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    transport.offerChannel(channel, rhe);
    channel.close();

    transport.connect(newApplicationContext());
    Thread.sleep(200);

    assertEquals(State.NotConnected, transport.getChannelFsm().getState());
  }

  @Test
  void offerChannel_notCalledConnectWorksNormally() throws Exception {
    var transport = createTransportDirect();

    transport.connect(newApplicationContext());
    Thread.sleep(200);

    assertEquals(State.NotConnected, transport.getChannelFsm().getState());
  }

  // -- helpers --

  private OpcTcpMultiplexedReverseConnectTransport createTransportDirect() {
    ChannelConsumerRegistry noOpRegistry =
        new ChannelConsumerRegistry() {
          @Override
          public void register(
              String serverUri, ChannelConsumerRegistry.ChannelConsumer consumer) {}

          @Override
          public void deregister(
              String serverUri, ChannelConsumerRegistry.ChannelConsumer consumer) {}
        };
    return new OpcTcpMultiplexedReverseConnectTransport(SERVER_URI, createConfig(), noOpRegistry);
  }

  private MultiplexedReverseConnectListener createAndStartListener() {
    MultiplexedReverseConnectListenerConfig listenerConfig =
        MultiplexedReverseConnectListenerConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setTransportConfig(createConfig())
            .setRateLimitingEnabled(false)
            .setReverseHelloTimeout(5_000)
            .build();

    var l = new MultiplexedReverseConnectListener(listenerConfig);
    l.start();
    return l;
  }

  private OpcTcpMultiplexedReverseConnectTransportConfig createConfig() {
    return OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
        .setExecutor(executor)
        .setScheduledExecutor(scheduledExecutor)
        .setEventLoop(eventLoop)
        .setWheelTimer(wheelTimer)
        .build();
  }

  private int getPort() {
    InetSocketAddress addr = listener.getLocalAddress();
    assertNotNull(addr);
    return addr.getPort();
  }

  private void reachConnected(
      OpcTcpMultiplexedReverseConnectTransport transport, EmbeddedChannel channel)
      throws Exception {

    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    transport
        .getChannelFsm()
        .fireEvent(new ReverseConnectChannelFsm.Event.ConnectionAccepted(channel, rhe));
    awaitFsmState(transport, State.Handshaking);

    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    transport.getChannelFsm().fireEvent(new ReverseConnectChannelFsm.Event.HandshakeSuccess(sc));
    awaitFsmState(transport, State.Connected);
  }

  @SuppressWarnings("BusyWait")
  private static void awaitFsmState(
      OpcTcpMultiplexedReverseConnectTransport transport, State expected) throws Exception {

    long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
    while (System.nanoTime() < deadline) {
      if (transport.getChannelFsm().getState() == expected) {
        return;
      }
      Thread.sleep(50);
    }
    fail(
        "Timed out waiting for state "
            + expected
            + ", current: "
            + transport.getChannelFsm().getState());
  }

  private Socket connectAndSendRhe(String serverUri, String endpointUrl) throws Exception {
    Socket socket = new Socket("localhost", getPort());
    socket.setSoTimeout(5000);
    OutputStream out = socket.getOutputStream();
    out.write(encodeReverseHello(serverUri, endpointUrl));
    out.flush();
    return socket;
  }

  private static byte[] encodeReverseHello(String serverUri, String endpointUrl) {
    byte[] uriBytes = serverUri.getBytes(StandardCharsets.UTF_8);
    byte[] urlBytes = endpointUrl.getBytes(StandardCharsets.UTF_8);
    int messageLength = 8 + 4 + uriBytes.length + 4 + urlBytes.length;

    ByteBuffer buf = ByteBuffer.allocate(messageLength).order(ByteOrder.LITTLE_ENDIAN);
    buf.put((byte) 'R');
    buf.put((byte) 'H');
    buf.put((byte) 'E');
    buf.put((byte) 'F');
    buf.putInt(messageLength);
    buf.putInt(uriBytes.length);
    buf.put(uriBytes);
    buf.putInt(urlBytes.length);
    buf.put(urlBytes);

    return buf.array();
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
}
