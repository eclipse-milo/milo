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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry.ChannelConsumer;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
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

    var consumer = new TestConsumer();
    listener.register(SERVER_URI, consumer);

    try (Socket ignored = connectAndSendRhe(SERVER_URI)) {
      AcceptedChannel accepted = consumer.accepted.get(5, TimeUnit.SECONDS);
      assertEquals(SERVER_URI, accepted.reverseHello().serverUri());
    }
  }

  @Test
  void unmatchedRheWithNoResolverClosesChannel() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    // No consumers registered, no resolver configured
    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
      socket.setSoTimeout(5000);
      // The server should close the channel — read returns -1
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  @Test
  void duplicateServerUriDispatchesToNeedingConsumer() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    var notNeedingConsumer = new TestConsumer(false, true);
    var needingConsumer = new TestConsumer(true, true);

    listener.register(SERVER_URI, notNeedingConsumer);
    listener.register(SERVER_URI, needingConsumer);

    try (Socket ignored = connectAndSendRhe(SERVER_URI)) {
      AcceptedChannel accepted = needingConsumer.accepted.get(5, TimeUnit.SECONDS);
      assertEquals(SERVER_URI, accepted.reverseHello().serverUri());
    }
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

    var consumer = new TestConsumer();

    listener.register(SERVER_URI, consumer);
    listener.deregister(SERVER_URI, consumer);

    // After deregister, no consumers are registered. RHE should cause channel close.
    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
      socket.setSoTimeout(5000);
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  @Test
  void rateLimitingHandlerInstalled() throws Exception {
    // Capture whether the handler is present inside the customizer callback,
    // which runs during initChannel() — before fireChannelRegistered()
    // propagates and AbstractRemoteAddressFilter removes itself from the
    // pipeline.
    var handlerPresent = new CompletableFuture<Boolean>();

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(true)
                .setChannelPipelineCustomizer(
                    pipeline ->
                        handlerPresent.complete(pipeline.get(RateLimitingHandler.class) != null))
                .build());

    // Connect a raw socket to trigger the child channel pipeline initialization
    try (var ignored = new Socket("localhost", getPort())) {
      assertTrue(handlerPresent.get(5, TimeUnit.SECONDS));
    }
  }

  @Test
  void stopWhileChildChannelsExistClosesThemBeforeCompleting() throws Exception {
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

  @Test
  void stopWhileRegisteredConsumersExistNotifiesWithBadShutdown() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    var consumer = new TestConsumer();
    listener.register(SERVER_URI, consumer);

    listener.stop().get(5, TimeUnit.SECONDS);

    UaException stopCause = assertInstanceOf(UaException.class, consumer.stopCause.get());
    assertEquals(StatusCodes.Bad_Shutdown, stopCause.getStatusCode().value());
  }

  @Test
  void duplicateRegistrationIsIdempotentForSameConsumer() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    var consumer = new TestConsumer();

    listener.register(SERVER_URI, consumer);
    listener.register(SERVER_URI, consumer);

    assertEquals(1, registeredConsumerCount(listener, SERVER_URI));

    listener.deregister(SERVER_URI, consumer);

    assertEquals(0, registeredConsumerCount(listener, SERVER_URI));
  }

  @Test
  void stoppedListenerCannotBeRestarted() throws Exception {
    listener = createAndStart(defaultConfigBuilder().setRateLimitingEnabled(false).build());

    listener.stop().get(5, TimeUnit.SECONDS);

    assertThrows(IllegalStateException.class, listener::start);
  }

  @Test
  void reverseHelloTimeoutCancelledWhenDecoderIsRemoved() throws Exception {
    var decoder = new CompletableFuture<Object>();

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setReverseHelloTimeout(30_000)
                .setChannelPipelineCustomizer(
                    pipeline -> captureReverseHelloDecoder(pipeline, decoder))
                .build());

    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
      socket.setSoTimeout(5000);
      assertEquals(-1, socket.getInputStream().read());
    }

    awaitReverseHelloTimeoutCancelled(decoder.get(5, TimeUnit.SECONDS));
  }

  @Test
  void reverseHelloTimeoutCancelledWhenChannelBecomesInactive() throws Exception {
    var decoder = new CompletableFuture<Object>();

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setReverseHelloTimeout(30_000)
                .setChannelPipelineCustomizer(
                    pipeline -> captureReverseHelloDecoder(pipeline, decoder))
                .build());

    var socket = new Socket("localhost", getPort());
    Object reverseHelloDecoder = decoder.get(5, TimeUnit.SECONDS);

    socket.close();

    awaitReverseHelloTimeoutCancelled(reverseHelloDecoder);
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

    try (Socket ignored = connectAndSendRhe(SERVER_URI)) {
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
    Socket socket1 = connectAndSendRhe(SERVER_URI);

    // Give the listener time to process the first RHE and increment pending
    Thread.sleep(200);

    // Second RHE should be rejected because pending limit is reached
    try (Socket socket2 = connectAndSendRhe("urn:test:server2")) {
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

    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
      socket.setSoTimeout(5000);
      assertEquals(-1, socket.getInputStream().read());
    }
  }

  // A resolved endpoint without a ClientListener must not leave the reverse-connected
  // channel parked in an unconsumed transport.
  @Test
  void resolverSuccessWithoutClientListenerClosesChannel() throws Exception {
    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) ->
            CompletableFuture.completedFuture(createEndpoint(endpointUrl));

    listener =
        createAndStart(
            defaultConfigBuilder()
                .setRateLimitingEnabled(false)
                .setEndpointResolver(resolver)
                .build());

    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
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

    try (Socket socket = connectAndSendRhe(SERVER_URI)) {
      OpcTcpMultiplexedReverseConnectTransport transport = transportFuture.get(5, TimeUnit.SECONDS);

      assertNotNull(transport);
      assertSocketRemainsOpen(socket);
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

    Socket socket = connectAndSendRhe(SERVER_URI);
    OpcTcpMultiplexedReverseConnectTransport transport = transportFuture.get(5, TimeUnit.SECONDS);

    // Close the socket before calling connect(). The owner should withdraw the pending accepted
    // channel instead of trying to handshake a dead socket.
    socket.close();
    Thread.sleep(200);

    // connect() should fall back to the normal path (wait for reconnect).
    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    Thread.sleep(200);

    assertTrue(!connectFuture.isDone(), "connect should wait for a replacement channel");
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

  private static int registeredConsumerCount(
      MultiplexedReverseConnectListener listener, String serverUri) throws Exception {

    Field consumersField = MultiplexedReverseConnectListener.class.getDeclaredField("consumers");
    consumersField.setAccessible(true);

    Map<?, ?> consumers = (Map<?, ?>) consumersField.get(listener);
    Object registeredConsumers = consumers.get(serverUri);
    if (registeredConsumers instanceof List<?> list) {
      return list.size();
    }

    return 0;
  }

  private static void awaitReverseHelloTimeoutCancelled(Object decoder) throws Exception {
    long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
    while (System.nanoTime() < deadline) {
      ScheduledFuture<?> timeoutFuture = reverseHelloTimeoutFuture(decoder);
      if (timeoutFuture != null && timeoutFuture.isCancelled()) {
        return;
      }
      Thread.sleep(10);
    }

    fail("Timed out waiting for ReverseHello timeout future cancellation");
  }

  private static void captureReverseHelloDecoder(
      ChannelPipeline pipeline, CompletableFuture<Object> decoder) {

    pipeline.toMap().values().stream()
        .filter(handler -> handler.getClass().getSimpleName().equals("ReverseHelloDecoder"))
        .findFirst()
        .ifPresent(decoder::complete);
  }

  private static ScheduledFuture<?> reverseHelloTimeoutFuture(Object decoder) throws Exception {
    Field timeoutFutureField = decoder.getClass().getDeclaredField("timeoutFuture");
    timeoutFutureField.setAccessible(true);
    return (ScheduledFuture<?>) timeoutFutureField.get(decoder);
  }

  private int getPort() {
    InetSocketAddress addr = listener.getLocalAddress();
    assertNotNull(addr);
    return addr.getPort();
  }

  private Socket connectAndSendRhe(String serverUri) throws Exception {
    Socket socket = new Socket("localhost", getPort());
    socket.setSoTimeout(5000);
    socket.getOutputStream().write(encodeReverseHello(serverUri, ENDPOINT_URL));
    socket.getOutputStream().flush();
    return socket;
  }

  private static void assertSocketRemainsOpen(Socket socket) throws Exception {
    socket.setSoTimeout(200);
    try {
      assertNotEquals(-1, socket.getInputStream().read());
    } catch (SocketTimeoutException ignored) {
      // A timeout means the channel stayed open without sending data.
    } finally {
      socket.setSoTimeout(5000);
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

  private record AcceptedChannel(Channel channel, ReverseHelloMessage reverseHello) {}

  private static final class TestConsumer implements ChannelConsumer {

    private final boolean needsChannel;
    private final boolean acceptsDuplicateChannel;
    private final CompletableFuture<AcceptedChannel> accepted = new CompletableFuture<>();
    private final CompletableFuture<Throwable> stopCause = new CompletableFuture<>();

    TestConsumer() {
      this(true, true);
    }

    TestConsumer(boolean needsChannel, boolean acceptsDuplicateChannel) {
      this.needsChannel = needsChannel;
      this.acceptsDuplicateChannel = acceptsDuplicateChannel;
    }

    @Override
    public boolean needsChannel() {
      return needsChannel;
    }

    @Override
    public boolean acceptsDuplicateChannel() {
      return acceptsDuplicateChannel;
    }

    @Override
    public void accept(Channel channel, ReverseHelloMessage reverseHello) {
      accepted.complete(new AcceptedChannel(channel, reverseHello));
    }

    @Override
    public CompletableFuture<Unit> listenerStopped(Throwable cause) {
      stopCause.complete(cause);
      return CompletableFuture.completedFuture(Unit.VALUE);
    }
  }
}
