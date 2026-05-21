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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OpcTcpServerReverseConnectorTest {

  private static final String SERVER_URI = "urn:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  private EventLoopGroup eventLoop;
  private OpcTcpServerReverseConnector connector;

  @AfterEach
  void tearDown() throws Exception {
    if (connector != null) {
      connector.close();
    }
    if (eventLoop != null) {
      eventLoop.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS).sync();
    }
  }

  @Test
  void sendsReverseHelloAndHandsHelloToServerPath() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(1_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), null, observer));

      try (Socket socket = accept(listener)) {
        ReverseHelloMessage reverseHello = readReverseHello(socket);

        assertEquals(SERVER_URI, reverseHello.getServerUri());
        assertEquals(ENDPOINT_URL, reverseHello.getEndpointUrl());

        writeMessage(socket, TcpMessageEncoder.encode(newHelloMessage()));

        Channel channel = attempt.channelFuture().get(3, TimeUnit.SECONDS);

        assertNotNull(channel);

        AcknowledgeMessage acknowledge = readAcknowledge(socket);

        assertEquals(0L, acknowledge.getProtocolVersion());
        assertTrue(observer.states().contains(OpcTcpServerReverseConnectAttemptState.HANDOFF));
      }
    }
  }

  @Test
  void clientErrorResponseFailsAttempt() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(1_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        writeMessage(
            socket,
            TcpMessageEncoder.encode(
                new ErrorMessage(StatusCodes.Bad_TcpEndpointUrlInvalid, "blocked")));

        assertThrows(
            ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
        waitUntil(
            () -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.CLIENT_ERROR));

        assertEquals(OpcTcpServerReverseConnectAttemptState.CLIENT_ERROR, attempt.state());
        var event = observer.last();
        assertNotNull(event.statusCode());
        assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, event.statusCode().value());

        connector.close();

        assertEquals(OpcTcpServerReverseConnectAttemptState.CLIENT_ERROR, observer.last().state());
        assertEquals(OpcTcpServerReverseConnectAttemptState.CLIENT_ERROR, attempt.state());
      }
    }
  }

  @Test
  void connectFailureFailsAttempt() throws Exception {
    RecordingObserver observer = new RecordingObserver();
    InetSocketAddress closedAddress;

    try (ServerSocket listener = newListener()) {
      closedAddress = boundAddress(listener);
    }

    connector = newConnector(1_000);

    OpcTcpServerReverseConnectAttempt attempt =
        connector.connect(newParameters(closedAddress, SERVER_URI, observer));

    assertThrows(ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
    waitUntil(() -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.FAILED));

    assertEquals(OpcTcpServerReverseConnectAttemptState.FAILED, attempt.state());
  }

  @Test
  void peerCloseBeforeHelloFailsAttempt() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        waitUntil(
            () ->
                observer
                    .states()
                    .contains(OpcTcpServerReverseConnectAttemptState.HELLO_HANDLER_INSTALLED));
      }

      assertThrows(
          ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
      waitUntil(() -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.FAILED));

      assertEquals(OpcTcpServerReverseConnectAttemptState.FAILED, attempt.state());
      var event = observer.last();
      assertNotNull(event.statusCode());
      assertEquals(StatusCodes.Bad_ConnectionClosed, event.statusCode().value());
    }
  }

  @Test
  void oversizedHelloHeaderFailsAttemptWithoutWaitingForBody() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        writeMessage(socket, newOversizedHelloHeaderMessage());

        assertThrows(
            ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
        waitUntil(() -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.FAILED));

        assertEquals(-1, socket.getInputStream().read());
        assertEquals(OpcTcpServerReverseConnectAttemptState.FAILED, attempt.state());
        var event = observer.last();
        assertNotNull(event.statusCode());
        assertEquals(StatusCodes.Bad_TcpMessageTooLarge, event.statusCode().value());
      }
    }
  }

  @Test
  void closeBeforeHelloClosesAttemptAndChannel() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        waitUntil(
            () ->
                observer
                    .states()
                    .contains(OpcTcpServerReverseConnectAttemptState.HELLO_HANDLER_INSTALLED));

        attempt.close();

        assertThrows(
            ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
        assertEquals(-1, socket.getInputStream().read());
        assertEquals(OpcTcpServerReverseConnectAttemptState.CLOSED, attempt.state());
      }
    }
  }

  @Test
  void cancelBeforeHelloCancelsAttemptAndChannel() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        waitUntil(
            () ->
                observer
                    .states()
                    .contains(OpcTcpServerReverseConnectAttemptState.HELLO_HANDLER_INSTALLED));

        attempt.cancel();

        assertThrows(
            CancellationException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
        assertEquals(-1, socket.getInputStream().read());
        assertEquals(OpcTcpServerReverseConnectAttemptState.CANCELLED, attempt.state());
      }
    }
  }

  @Test
  void cancelFromEventLoopDoesNotReportFailed() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      eventLoop.next().submit(attempt::cancel).get(3, TimeUnit.SECONDS);

      assertThrows(
          CancellationException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
      waitUntil(() -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.CANCELLED));

      assertFalse(observer.states().contains(OpcTcpServerReverseConnectAttemptState.FAILED));
      assertEquals(OpcTcpServerReverseConnectAttemptState.CANCELLED, attempt.state());
    }
  }

  @Test
  void closeFromEventLoopDoesNotReportFailed() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(5_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      eventLoop.next().submit(attempt::close).get(3, TimeUnit.SECONDS);

      assertThrows(
          ExecutionException.class, () -> attempt.channelFuture().get(3, TimeUnit.SECONDS));
      waitUntil(() -> observer.states().contains(OpcTcpServerReverseConnectAttemptState.CLOSED));

      assertFalse(observer.states().contains(OpcTcpServerReverseConnectAttemptState.FAILED));
      assertEquals(OpcTcpServerReverseConnectAttemptState.CLOSED, attempt.state());
    }
  }

  @Test
  void connectorCloseAfterHandoffDoesNotRegressAttemptState() throws Exception {
    RecordingObserver observer = new RecordingObserver();

    try (ServerSocket listener = newListener()) {
      connector = newConnector(1_000);

      OpcTcpServerReverseConnectAttempt attempt =
          connector.connect(newParameters(boundAddress(listener), SERVER_URI, observer));

      try (Socket socket = accept(listener)) {
        readReverseHello(socket);
        writeMessage(socket, TcpMessageEncoder.encode(newHelloMessage()));

        attempt.channelFuture().get(3, TimeUnit.SECONDS);
        readAcknowledge(socket);

        connector.close();

        assertEquals(-1, socket.getInputStream().read());
        assertEquals(OpcTcpServerReverseConnectAttemptState.HANDOFF, observer.last().state());
        assertEquals(OpcTcpServerReverseConnectAttemptState.HANDOFF, attempt.state());
      }
    }
  }

  @Test
  void unresolvedServerUriFailsFast() {
    connector = newConnector(1_000);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            newParameters(
                new InetSocketAddress("127.0.0.1", 4840),
                "opc.tcp://localhost:12685/unknown",
                null,
                new RecordingObserver()));
  }

  private OpcTcpServerReverseConnector newConnector(int helloDeadlineMs) {
    eventLoop = new NioEventLoopGroup(1);

    OpcTcpServerTransportConfig config =
        OpcTcpServerTransportConfig.newBuilder()
            .setEventLoop(eventLoop)
            .setHelloDeadline(uint(helloDeadlineMs))
            .setChannelPipelineCustomizer(pipeline -> pipeline.addLast(new MarkerHandler()))
            .build();

    return new OpcTcpServerReverseConnector(config);
  }

  private static OpcTcpServerReverseConnectParameters newParameters(
      InetSocketAddress clientListenerAddress,
      String endpointUrl,
      String serverUri,
      RecordingObserver observer) {

    return OpcTcpServerReverseConnectParameters.fromAddress(
        newServerApplicationContext(),
        clientListenerAddress,
        endpointUrl,
        serverUri,
        1_000,
        observer);
  }

  private static OpcTcpServerReverseConnectParameters newParameters(
      InetSocketAddress clientListenerAddress, String serverUri, RecordingObserver observer) {

    return newParameters(clientListenerAddress, ENDPOINT_URL, serverUri, observer);
  }

  private static ServerSocket newListener() throws IOException {
    ServerSocket listener = new ServerSocket();
    listener.bind(new InetSocketAddress("127.0.0.1", 0));
    listener.setSoTimeout(3_000);
    return listener;
  }

  private static Socket accept(ServerSocket listener) throws IOException {
    Socket socket = listener.accept();
    socket.setSoTimeout(3_000);
    return socket;
  }

  private static InetSocketAddress boundAddress(ServerSocket listener) {
    return new InetSocketAddress("127.0.0.1", listener.getLocalPort());
  }

  private static ReverseHelloMessage readReverseHello(Socket socket) throws Exception {
    ByteBuf buffer = readMessage(socket);
    try {
      return TcpMessageDecoder.decodeReverseHello(buffer);
    } finally {
      buffer.release();
    }
  }

  private static AcknowledgeMessage readAcknowledge(Socket socket) throws Exception {
    ByteBuf buffer = readMessage(socket);
    try {
      return TcpMessageDecoder.decodeAcknowledge(buffer);
    } finally {
      buffer.release();
    }
  }

  private static ByteBuf readMessage(Socket socket) throws IOException {
    InputStream inputStream = socket.getInputStream();
    byte[] header = inputStream.readNBytes(8);

    assertEquals(8, header.length);

    int messageLength =
        (header[4] & 0xFF)
            | ((header[5] & 0xFF) << 8)
            | ((header[6] & 0xFF) << 16)
            | ((header[7] & 0xFF) << 24);

    byte[] body = inputStream.readNBytes(messageLength - 8);
    assertEquals(messageLength - 8, body.length);

    return Unpooled.wrappedBuffer(header, body);
  }

  private static void writeMessage(Socket socket, ByteBuf buffer) throws Exception {
    try {
      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);

      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
    } finally {
      buffer.release();
    }
  }

  private static ByteBuf newOversizedHelloHeaderMessage() {
    return Unpooled.buffer(8)
        .writeMediumLE(MessageType.toMediumInt(MessageType.Hello))
        .writeByte('F')
        .writeIntLE(UascServerHelloHandler.MAX_HELLO_MESSAGE_SIZE + 1);
  }

  private static HelloMessage newHelloMessage() {
    return new HelloMessage(0L, 8192L, 8192L, 8192L, 8L, ENDPOINT_URL);
  }

  private static ServerApplicationContext newServerApplicationContext() {
    return new ServerApplicationContext() {

      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of(newEndpointDescription());
      }

      @Override
      public CertificateManager getCertificateManager() {
        return EmptyCertificateManager.INSTANCE;
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public Long getNextSecureChannelId() {
        return 1L;
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        return 1L;
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {

        return CompletableFuture.failedFuture(new UnsupportedOperationException());
      }
    };
  }

  private static EndpointDescription newEndpointDescription() {
    return new EndpointDescription(
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
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }

  private static final class MarkerHandler extends ChannelInboundHandlerAdapter {}

  private static final class RecordingObserver
      implements OpcTcpServerReverseConnectAttemptObserver {

    private final List<OpcTcpServerReverseConnectAttemptEvent> events =
        new CopyOnWriteArrayList<>();

    @Override
    public void onStateTransition(OpcTcpServerReverseConnectAttemptEvent event) {
      events.add(event);
    }

    List<OpcTcpServerReverseConnectAttemptState> states() {
      return events.stream().map(OpcTcpServerReverseConnectAttemptEvent::state).toList();
    }

    OpcTcpServerReverseConnectAttemptEvent last() {
      assertFalse(events.isEmpty());
      return events.get(events.size() - 1);
    }
  }

  private enum EmptyCertificateManager implements CertificateManager {
    INSTANCE;

    private final CertificateQuarantine certificateQuarantine = new MemoryCertificateQuarantine();

    @Override
    public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
      return Optional.empty();
    }

    @Override
    public List<CertificateGroup> getCertificateGroups() {
      return List.of();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      return certificateQuarantine;
    }
  }
}
