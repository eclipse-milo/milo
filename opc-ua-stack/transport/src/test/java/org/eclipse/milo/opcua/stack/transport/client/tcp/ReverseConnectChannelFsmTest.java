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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm.Event;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm.State;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReverseConnectChannelFsmTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private NioEventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private final AtomicLong requestId = new AtomicLong(0L);

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully();
    wheelTimer.stop();
  }

  @Test
  void happyPath() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    assertEquals(State.NotConnected, fsm.getState());

    // Connect stores a future chained to KEY_CF
    var connectFuture = new CompletableFuture<Channel>();
    fsm.fireEvent(new Event.Connect(connectFuture));
    Thread.sleep(100);
    assertEquals(State.NotConnected, fsm.getState());
    assertFalse(connectFuture.isDone());

    // Server connects inbound
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel, rhe));
    awaitState(fsm, State.Handshaking);

    // Handshake completes successfully
    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.Connected);

    // Connect future should be completed with the channel
    assertTrue(connectFuture.isDone());
    assertEquals(channel, connectFuture.get(1, TimeUnit.SECONDS));

    // KEY_CHANNEL should be set
    assertEquals(channel, fsm.getChannel());
  }

  @Test
  void handshakeFailureTransitionsToReconnecting() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    // First attempt
    var channel1 = new EmbeddedChannel();
    var rhe1 = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel1, rhe1));
    awaitState(fsm, State.Handshaking);

    // Handshake fails
    fsm.fireEvent(new Event.HandshakeFailure(new Exception("handshake failed")));
    awaitState(fsm, State.Reconnecting);

    assertNull(fsm.getChannel());

    // Server reconnects — should transition back to Handshaking
    var channel2 = new EmbeddedChannel();
    var rhe2 = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel2, rhe2));
    awaitState(fsm, State.Handshaking);
  }

  @Test
  void channelInactiveWhileConnectedTransitionsToReconnecting() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel = new EmbeddedChannel();
    reachConnected(fsm, channel);
    assertEquals(State.Connected, fsm.getState());

    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.Reconnecting);

    assertNull(fsm.getChannel());
  }

  @Test
  void disconnectShelvedDuringHandshaking() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel, rhe));
    awaitState(fsm, State.Handshaking);

    // Disconnect while handshaking — should be shelved
    var disconnectFuture = new CompletableFuture<Unit>();
    fsm.fireEvent(new Event.Disconnect(disconnectFuture));
    Thread.sleep(100);
    assertEquals(State.Handshaking, fsm.getState());
    assertFalse(disconnectFuture.isDone());

    // Handshake completes -> Connected -> shelved Disconnect replayed -> NotConnected
    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.NotConnected);

    assertTrue(disconnectFuture.isDone());
    assertEquals(Unit.VALUE, disconnectFuture.get(1, TimeUnit.SECONDS));
  }

  @Test
  void getChannelCompletesImmediatelyWhenConnected() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel = new EmbeddedChannel();
    reachConnected(fsm, channel);

    var getChannelFuture = new CompletableFuture<Channel>();
    fsm.fireEvent(new Event.GetChannel(getChannelFuture));

    Channel result = getChannelFuture.get(5, TimeUnit.SECONDS);
    assertEquals(channel, result);
  }

  @Test
  void getChannelChainedWhenHandshaking() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel, rhe));
    awaitState(fsm, State.Handshaking);

    // GetChannel while handshaking — chained to KEY_CF, not yet completed
    var getChannelFuture = new CompletableFuture<Channel>();
    fsm.fireEvent(new Event.GetChannel(getChannelFuture));
    Thread.sleep(100);
    assertFalse(getChannelFuture.isDone());

    // Handshake completes — getChannelFuture should now be completed
    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.Connected);

    Channel result = getChannelFuture.get(5, TimeUnit.SECONDS);
    assertEquals(channel, result);
  }

  @Test
  void getChannelChainedWhenReconnecting() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel1 = new EmbeddedChannel();
    reachConnected(fsm, channel1);

    // Channel goes inactive -> Reconnecting
    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.Reconnecting);

    // GetChannel while reconnecting — chained to KEY_CF
    var getChannelFuture = new CompletableFuture<Channel>();
    fsm.fireEvent(new Event.GetChannel(getChannelFuture));
    Thread.sleep(100);
    assertFalse(getChannelFuture.isDone());

    // Server reconnects
    var channel2 = new EmbeddedChannel();
    var rhe2 = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel2, rhe2));
    awaitState(fsm, State.Handshaking);

    // Handshake completes — getChannelFuture should now be completed
    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel2);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.Connected);

    Channel result = getChannelFuture.get(5, TimeUnit.SECONDS);
    assertEquals(channel2, result);
  }

  @Test
  void transitionListenerNotification() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var transitions = new CopyOnWriteArrayList<String>();
    fsm.addTransitionListener((from, to) -> transitions.add(from + "->" + to));

    // NotConnected -> Handshaking
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel, rhe));
    awaitState(fsm, State.Handshaking);

    // Handshaking -> Connected
    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.Connected);

    // Connected -> Reconnecting
    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.Reconnecting);

    // Allow time for listener callbacks to complete
    Thread.sleep(200);

    assertTrue(transitions.contains("NotConnected->Handshaking"));
    assertTrue(transitions.contains("Handshaking->Connected"));
    assertTrue(transitions.contains("Connected->Reconnecting"));
  }

  @Test
  void disconnectFromConnectedTransitionsToNotConnected() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel = new EmbeddedChannel();
    reachConnected(fsm, channel);
    assertEquals(State.Connected, fsm.getState());

    var disconnectFuture = new CompletableFuture<Unit>();
    fsm.fireEvent(new Event.Disconnect(disconnectFuture));
    awaitState(fsm, State.NotConnected);

    assertTrue(disconnectFuture.isDone());
    assertEquals(Unit.VALUE, disconnectFuture.get(1, TimeUnit.SECONDS));
    assertNull(fsm.getChannel());
  }

  @Test
  void connectionAcceptedAfterDisconnectTransitionsToHandshaking() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    var channel1 = new EmbeddedChannel();
    reachConnected(fsm, channel1);

    // Disconnect from Connected -> NotConnected
    var disconnectFuture = new CompletableFuture<Unit>();
    fsm.fireEvent(new Event.Disconnect(disconnectFuture));
    awaitState(fsm, State.NotConnected);

    // Server reconnects -> Handshaking
    var channel2 = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel2, rhe));
    awaitState(fsm, State.Handshaking);
  }

  @Test
  void disconnectFromNotConnectedFailsPendingConnectFuture() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    assertEquals(State.NotConnected, fsm.getState());

    // Connect stores a future chained to KEY_CF
    var connectFuture = new CompletableFuture<Channel>();
    fsm.fireEvent(new Event.Connect(connectFuture));
    Thread.sleep(100);
    assertFalse(connectFuture.isDone());

    // Disconnect while still in NotConnected should fail the connect future
    var disconnectFuture = new CompletableFuture<Unit>();
    fsm.fireEvent(new Event.Disconnect(disconnectFuture));
    Thread.sleep(100);

    assertTrue(disconnectFuture.isDone());
    assertEquals(Unit.VALUE, disconnectFuture.get(1, TimeUnit.SECONDS));

    assertTrue(connectFuture.isDone());
    assertTrue(connectFuture.isCompletedExceptionally());
    try {
      connectFuture.get(1, TimeUnit.SECONDS);
      fail("Expected UaException");
    } catch (ExecutionException e) {
      assertTrue(e.getCause() instanceof UaException);
    }
  }

  @Test
  void disconnectFromNotConnectedWithNoConnectFuture() throws Exception {
    var fsm = newFsm();
    fsm.setApplicationContext(newApplicationContext());

    assertEquals(State.NotConnected, fsm.getState());

    // Disconnect without a prior Connect — should complete normally
    var disconnectFuture = new CompletableFuture<Unit>();
    fsm.fireEvent(new Event.Disconnect(disconnectFuture));
    Thread.sleep(100);

    assertTrue(disconnectFuture.isDone());
    assertEquals(Unit.VALUE, disconnectFuture.get(1, TimeUnit.SECONDS));
    assertEquals(State.NotConnected, fsm.getState());
  }

  // -- helpers --

  private ReverseConnectChannelFsm newFsm() {
    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setWheelTimer(wheelTimer)
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .setEventLoop(eventLoop)
            .build();

    UascResponseHandler responseHandler =
        new UascResponseHandler() {
          @Override
          public void handleResponse(long requestId, UaResponseMessageType msg) {}

          @Override
          public void handleSendFailure(long requestId, UaException ex) {}

          @Override
          public void handleReceiveFailure(long requestId, UaException ex) {}

          @Override
          public void handleChannelError(UaException ex) {}

          @Override
          public void handleChannelInactive() {}
        };

    return ReverseConnectChannelFsm.create(
        config, responseHandler, requestId::getAndIncrement, executor);
  }

  private void reachConnected(ReverseConnectChannelFsm fsm, EmbeddedChannel channel)
      throws Exception {

    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm.fireEvent(new Event.ConnectionAccepted(channel, rhe));
    awaitState(fsm, State.Handshaking);

    var sc = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
    sc.setChannel(channel);
    fsm.fireEvent(new Event.HandshakeSuccess(sc));
    awaitState(fsm, State.Connected);
  }

  private static void awaitState(ReverseConnectChannelFsm fsm, State expected) throws Exception {
    awaitState(fsm, expected, Duration.ofSeconds(5));
  }

  @SuppressWarnings("BusyWait")
  private static void awaitState(ReverseConnectChannelFsm fsm, State expected, Duration timeout)
      throws Exception {

    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      if (fsm.getState() == expected) {
        return;
      }
      Thread.sleep(50);
    }
    fail("Timed out waiting for state " + expected + ", current: " + fsm.getState());
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
