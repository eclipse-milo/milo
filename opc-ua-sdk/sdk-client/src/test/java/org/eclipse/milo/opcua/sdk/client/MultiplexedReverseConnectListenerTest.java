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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
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
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry.ChannelConsumer;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm.State;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.server.tcp.RateLimitingHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MultiplexedReverseConnectListenerTest {

  private static final String SERVER_URI = "urn:test:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private MultiplexedReverseConnectListener listener;

  @AfterEach
  void tearDown() throws Exception {
    if (listener != null) {
      listener.stop().get(5, TimeUnit.SECONDS);
      listener = null;
    }
  }

  @Test
  void startBindsAndStopUnbinds() throws Exception {
    listener = createAndStart(defaultConfigBuilder().build());

    int port = getPort();

    // Verify we can connect to the listener
    try (var socket = new Socket("localhost", port)) {
      assertTrue(socket.isConnected());
    }

    // Stop the listener
    listener.stop().get(5, TimeUnit.SECONDS);

    // Verify the port is now free
    try (var ss = new ServerSocket(port)) {
      assertNotNull(ss);
    }

    listener = null;
  }

  @Test
  void validRheDispatchesToRegisteredConsumer() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    ReverseConnectChannelFsm fsm = createTestFsm();

    var transitioned = new CompletableFuture<Void>();
    fsm.addTransitionListener(
        (from, to) -> {
          if (from == State.NotConnected && to == State.Handshaking) {
            transitioned.complete(null);
          }
        });

    listener.register(SERVER_URI, new ChannelConsumer(fsm, null));

    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      // Wait for the FSM to transition to Handshaking, proving dispatch worked
      transitioned.get(5, TimeUnit.SECONDS);
      assertEquals(State.Handshaking, fsm.getState());
    }
  }

  @Test
  void unmatchedRheWithNoResolverClosesChannel() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    // No consumers registered, no resolver configured
    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      socket.setSoTimeout(5000);
      // The server should close the channel — read returns -1
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  @Test
  void duplicateServerUriDispatchesToNeedingConsumer() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    ReverseConnectChannelFsm fsm1 = createTestFsm();
    ReverseConnectChannelFsm fsm2 = createTestFsm();

    // Drive fsm1 into Handshaking via a direct ConnectionAccepted event
    // using an EmbeddedChannel. The embedded channel's event loop defers
    // execution, so fsm1 stays in Handshaking without progressing.
    var fsm1Handshaking = new CompletableFuture<Void>();
    fsm1.addTransitionListener(
        (from, to) -> {
          if (to == State.Handshaking) {
            fsm1Handshaking.complete(null);
          }
        });

    var embedded = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    fsm1.fireEvent(new ReverseConnectChannelFsm.Event.ConnectionAccepted(embedded, rhe));
    fsm1Handshaking.get(5, TimeUnit.SECONDS);
    assertEquals(State.Handshaking, fsm1.getState());

    // Register both consumers — fsm1 is Handshaking, fsm2 is NotConnected
    listener.register(SERVER_URI, new ChannelConsumer(fsm1, null));
    listener.register(SERVER_URI, new ChannelConsumer(fsm2, null));

    // fsm2 should receive the dispatch (first in NotConnected/Reconnecting)
    var fsm2Transitioned = new CompletableFuture<Void>();
    fsm2.addTransitionListener(
        (from, to) -> {
          if (from == State.NotConnected && to == State.Handshaking) {
            fsm2Transitioned.complete(null);
          }
        });

    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      fsm2Transitioned.get(5, TimeUnit.SECONDS);
      assertEquals(State.Handshaking, fsm2.getState());
    }

    embedded.close();
  }

  @Test
  void reverseHelloTimeoutClosesChannel() throws Exception {
    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setReverseHelloTimeout(200)
                .build());

    // Connect but do NOT send RHE
    try (var socket = new Socket("localhost", getPort())) {
      socket.setSoTimeout(5000);
      // The listener should close the channel after the timeout
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  @Test
  void maxConnectionsEnforced() throws Exception {
    listener =
        createAndStart(
            defaultConfigBuilder().setRateLimitingEnabled(false).setMaxConnections(1).build());

    int port = getPort();

    // First connection should be accepted
    try (var socket1 = new Socket("localhost", port)) {
      assertTrue(socket1.isConnected());

      // Give the listener time to register the first connection
      Thread.sleep(100);

      // Second connection should be closed immediately by the server
      try (var socket2 = new Socket("localhost", port)) {
        socket2.setSoTimeout(5000);
        assertEquals(-1, socket2.getInputStream().read());
      }
    }
  }

  @Test
  void deregisterRemovesConsumer() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    ReverseConnectChannelFsm fsm = createTestFsm();
    var consumer = new ChannelConsumer(fsm, null);

    listener.register(SERVER_URI, consumer);
    listener.deregister(SERVER_URI, consumer);

    // After deregister, no consumers are registered. RHE should cause channel close.
    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      socket.setSoTimeout(5000);
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  @Test
  void rateLimitingHandlerInstalled() throws Exception {
    var pipelineFuture = new CompletableFuture<ChannelPipeline>();

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(true)
                .setChannelPipelineCustomizer(pipelineFuture::complete)
                .build());

    // Connect a raw socket to trigger the child channel pipeline initialization
    try (var socket = new Socket("localhost", getPort())) {
      ChannelPipeline pipeline = pipelineFuture.get(5, TimeUnit.SECONDS);
      assertNotNull(pipeline.get(RateLimitingHandler.class));
    }
  }

  @Test
  void stopClosesAllChildChannels() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    var socket = new Socket("localhost", getPort());
    socket.setSoTimeout(5000);
    assertTrue(socket.isConnected());

    // Give the listener time to register the child channel
    Thread.sleep(100);

    // Stop should close all child channels
    listener.stop().get(5, TimeUnit.SECONDS);
    listener = null;

    // The socket should report EOF
    assertEquals(-1, socket.getInputStream().read());
    socket.close();
  }

  // ---------------------------------------------------------------------------
  // EndpointResolver tests (Phase 5)
  // ---------------------------------------------------------------------------

  @Test
  void unmatchedRheWithResolverInvokesResolver() throws Exception {
    var resolverArgs = new CompletableFuture<String[]>();

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) -> {
          resolverArgs.complete(new String[] {serverUri, endpointUrl});
          return CompletableFuture.completedFuture(createEndpoint(endpointUrl));
        };

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setEndpointResolver(resolver)
                .build());

    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      String[] args = resolverArgs.get(5, TimeUnit.SECONDS);
      assertEquals(SERVER_URI, args[0]);
      assertEquals(ENDPOINT_URL, args[1]);
    }
  }

  @Test
  void maxPendingConnectionsRejectsExcess() throws Exception {
    // Slow resolver that never completes — occupies a pending slot indefinitely
    EndpointResolver resolver = (serverUri, endpointUrl, discovery) -> new CompletableFuture<>();

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setMaxPendingConnections(1)
                .setEndpointResolver(resolver)
                .build());

    // First RHE occupies the single pending slot
    var socket1 = connectAndSendRhe(SERVER_URI, ENDPOINT_URL);

    // Give the listener time to process the first RHE and increment pending
    Thread.sleep(200);

    // Second RHE should be rejected because pending limit is reached
    try (var socket2 = connectAndSendRhe("urn:test:server2", ENDPOINT_URL)) {
      socket2.setSoTimeout(5000);
      assertEquals(-1, socket2.getInputStream().read());
    }

    socket1.close();
  }

  @Test
  void resolverFailureClosesChannel() throws Exception {
    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) -> {
          CompletableFuture<EndpointDescription> f = new CompletableFuture<>();
          f.completeExceptionally(new RuntimeException("resolver failed"));
          return f;
        };

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setEndpointResolver(resolver)
                .build());

    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      socket.setSoTimeout(5000);
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  // ---------------------------------------------------------------------------
  // 1-shot channel reuse tests (Phase 2 — channel buffering integration)
  // ---------------------------------------------------------------------------

  @Test
  void oneShotChannelReuse_channelNotClosed() throws Exception {
    var transportFuture = new CompletableFuture<OpcTcpMultiplexedReverseConnectTransport>();

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) ->
            CompletableFuture.completedFuture(createEndpoint(endpointUrl));

    ClientListener clientListener =
        client -> {
          var transport = (OpcTcpMultiplexedReverseConnectTransport) client.getTransport();
          transport.connect(newApplicationContext());
          transportFuture.complete(transport);
        };

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setEndpointResolver(resolver)
                .setClientListener(clientListener)
                .build());

    try (var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL)) {
      var transport = transportFuture.get(5, TimeUnit.SECONDS);

      // The FSM should reach Handshaking using the original channel,
      // proving the channel was offered and consumed without closing.
      awaitFsmState(transport, State.Handshaking);
    }
  }

  @Test
  void oneShotChannelReuse_channelDiesBeforeConnect() throws Exception {
    var transportFuture = new CompletableFuture<OpcTcpMultiplexedReverseConnectTransport>();

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) ->
            CompletableFuture.completedFuture(createEndpoint(endpointUrl));

    // Capture the transport but don't call connect() yet.
    ClientListener clientListener =
        client -> {
          var transport = (OpcTcpMultiplexedReverseConnectTransport) client.getTransport();
          transportFuture.complete(transport);
        };

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setEndpointResolver(resolver)
                .setClientListener(clientListener)
                .build());

    var socket = connectAndSendRhe(SERVER_URI, ENDPOINT_URL);
    var transport = transportFuture.get(5, TimeUnit.SECONDS);

    // Close the socket before calling connect(). The Netty channel
    // close listener should clear the pending channel buffer.
    socket.close();
    Thread.sleep(200);

    // connect() should fall back to the normal path (wait for reconnect).
    transport.connect(newApplicationContext());
    Thread.sleep(200);

    assertEquals(State.NotConnected, transport.getChannelFsm().getState());
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private static EndpointDescription createEndpoint(String endpointUrl) {
    return new EndpointDescription(endpointUrl, null, null, null, null, null, null, null);
  }

  private MultiplexedReverseConnectListenerConfigBuilder defaultConfigBuilder() {
    OpcTcpMultiplexedReverseConnectTransportConfig transportConfig =
        OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder().build();

    return MultiplexedReverseConnectListenerConfig.newBuilder()
        .setListenAddress(new InetSocketAddress("localhost", 0))
        .setTransportConfig(transportConfig);
  }

  private MultiplexedReverseConnectListener createAndStart(
      MultiplexedReverseConnectListenerConfig config) {
    listener = new MultiplexedReverseConnectListener(config);
    listener.start();
    return listener;
  }

  private int getPort() {
    InetSocketAddress addr = listener.getLocalAddress();
    assertNotNull(addr);
    return addr.getPort();
  }

  private ReverseConnectChannelFsm createTestFsm() {
    OpcTcpMultiplexedReverseConnectTransportConfig transportConfig =
        OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder().build();

    var fsmConfig =
        new ReverseConnectChannelFsm.ChannelFsmConfig(
            transportConfig,
            transportConfig.getAllowedServerUris(),
            transportConfig.getReverseHelloTimeout());

    UascResponseHandler handler =
        new UascResponseHandler() {
          @Override
          public void handleResponse(long requestId, UaResponseMessageType responseMessage) {}

          @Override
          public void handleSendFailure(long requestId, UaException exception) {}

          @Override
          public void handleReceiveFailure(long requestId, UaException exception) {}

          @Override
          public void handleChannelError(UaException exception) {}

          @Override
          public void handleChannelInactive() {}
        };

    return ReverseConnectChannelFsm.create(
        fsmConfig, handler, new AtomicLong()::getAndIncrement, Executors.newSingleThreadExecutor());
  }

  private Socket connectAndSendRhe(String serverUri, String endpointUrl) throws Exception {
    Socket socket = new Socket("localhost", getPort());
    socket.setSoTimeout(5000);
    socket.getOutputStream().write(encodeReverseHello(serverUri, endpointUrl));
    socket.getOutputStream().flush();
    return socket;
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

  private static byte[] encodeReverseHello(String serverUri, String endpointUrl) {
    byte[] uriBytes = serverUri.getBytes(StandardCharsets.UTF_8);
    byte[] urlBytes = endpointUrl.getBytes(StandardCharsets.UTF_8);
    int messageLength = 8 + 4 + uriBytes.length + 4 + urlBytes.length;

    ByteBuffer buf = ByteBuffer.allocate(messageLength).order(ByteOrder.LITTLE_ENDIAN);
    // Header: "RHE" message type + 'F' chunk type + message length
    buf.put((byte) 'R');
    buf.put((byte) 'H');
    buf.put((byte) 'E');
    buf.put((byte) 'F');
    buf.putInt(messageLength);
    // Body: ServerUri + EndpointUrl as OPC UA strings (LE int32 length + UTF-8)
    buf.putInt(uriBytes.length);
    buf.put(uriBytes);
    buf.putInt(urlBytes.length);
    buf.put(urlBytes);

    return buf.array();
  }
}
