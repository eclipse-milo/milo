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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
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
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry.ChannelConsumer;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
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

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
  }

  @AfterEach
  void tearDown() throws Exception {
    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully().get(5, TimeUnit.SECONDS);
    wheelTimer.stop();
  }

  @Test
  void duplicateConnectRegistersOnceAndSharesListenerStopFailure() {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 5_000);

    CompletableFuture<Unit> firstConnect = transport.connect(newApplicationContext());
    CompletableFuture<Unit> duplicateConnect = transport.connect(newApplicationContext());

    assertEquals(1, registry.registerCalls.get());

    registry.stop(new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));

    assertUaStatus(StatusCodes.Bad_Shutdown, firstConnect);
    assertUaStatus(StatusCodes.Bad_Shutdown, duplicateConnect);
  }

  @Test
  void connectTimeoutDeregisters() throws Exception {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 50);

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());

    assertUaStatus(StatusCodes.Bad_Timeout, connectFuture);

    assertEquals(1, registry.deregisterCalls.get());
    assertNull(registry.consumer);

    assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));
    assertEquals(1, registry.deregisterCalls.get());
  }

  @Test
  void listenerStopFailsConnectAndGetChannelWaiters() {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 5_000);

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    CompletableFuture<Channel> getChannelFuture = transport.getChannelForTest();

    registry.stop(new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));

    assertUaStatus(StatusCodes.Bad_Shutdown, connectFuture);
    assertUaStatus(StatusCodes.Bad_Shutdown, getChannelFuture);
  }

  @Test
  void offerChannelRejectsOverwriteAndKeepsOriginal() throws Exception {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 5_000);

    var original = new EmbeddedChannel();
    var overwrite = new EmbeddedChannel();

    transport.offerChannel(original, reverseHello());
    transport.offerChannel(overwrite, reverseHello());

    awaitClosed(overwrite);

    assertTrue(original.isOpen(), "the first pending offer should remain owned by the transport");

    assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));
    awaitClosed(original);
  }

  @Test
  void offerChannelRejectsMismatchedServerUri() throws Exception {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 5_000);
    var channel = new EmbeddedChannel();

    transport.offerChannel(
        channel, new ReverseHelloMessage("urn:eclipse:milo:other", ENDPOINT_URL));

    awaitClosed(channel);

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    channel.runPendingTasks();

    assertNull(channel.readOutbound());
    assertFalse(connectFuture.isDone(), "mismatched offered channel must not start a handshake");

    assertEquals(Unit.VALUE, transport.disconnect().get(5, TimeUnit.SECONDS));
  }

  @Test
  void pendingChannelIsConsumedAfterContextInstall() throws Exception {
    var registry = new RecordingRegistry();
    TestTransport transport = newTestTransport(registry, 5_000);
    var channel = new EmbeddedChannel();

    transport.offerChannel(channel, reverseHello());
    channel.runPendingTasks();

    assertNull(channel.readOutbound(), "offered channel must not handshake before connect()");

    CompletableFuture<Unit> connectFuture = transport.connect(newApplicationContext());
    ByteBuf helloBuffer = awaitOutbound(channel);

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertEquals(ENDPOINT_URL, hello.getEndpointUrl());
    helloBuffer.release();
    assertFalse(
        connectFuture.isDone(), "connect waits for the rest of the secure-channel handshake");

    transport.disconnect().get(5, TimeUnit.SECONDS);
  }

  private TestTransport newTestTransport(RecordingRegistry registry, long connectTimeout) {
    return new TestTransport(SERVER_URI, createConfig(connectTimeout), registry);
  }

  private OpcTcpMultiplexedReverseConnectTransportConfig createConfig(long connectTimeout) {
    return OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
        .setExecutor(executor)
        .setScheduledExecutor(scheduledExecutor)
        .setEventLoop(eventLoop)
        .setWheelTimer(wheelTimer)
        .setConnectTimeout(connectTimeout)
        .build();
  }

  private static ReverseHelloMessage reverseHello() {
    return new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
  }

  private static ByteBuf awaitOutbound(EmbeddedChannel channel) throws Exception {
    long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
    while (System.nanoTime() < deadline) {
      channel.runPendingTasks();

      ByteBuf outbound = channel.readOutbound();
      if (outbound != null) {
        return outbound;
      }

      Thread.sleep(10);
    }

    return fail("Timed out waiting for outbound Hello");
  }

  private static void awaitClosed(Channel channel) throws Exception {
    long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
    while (System.nanoTime() < deadline) {
      if (!channel.isOpen()) {
        return;
      }

      Thread.sleep(10);
    }

    fail("Timed out waiting for channel close");
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
      future.get(5, TimeUnit.SECONDS);
      fail("Expected UaException");
    } catch (ExecutionException e) {
      UaException uaException = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(expectedStatus, uaException.getStatusCode().value());
    } catch (Exception e) {
      fail("Expected UaException", e);
    }
  }

  private static final class TestTransport extends OpcTcpMultiplexedReverseConnectTransport {

    private TestTransport(
        String serverUri,
        OpcTcpMultiplexedReverseConnectTransportConfig config,
        ChannelConsumerRegistry registry) {

      super(serverUri, config, registry);
    }

    private CompletableFuture<Channel> getChannelForTest() {
      return super.getChannel();
    }
  }

  private static final class RecordingRegistry implements ChannelConsumerRegistry {

    private final AtomicInteger registerCalls = new AtomicInteger();
    private final AtomicInteger deregisterCalls = new AtomicInteger();

    private volatile ChannelConsumer consumer;

    @Override
    public void register(String serverUri, ChannelConsumer consumer) {
      assertEquals(SERVER_URI, serverUri);
      registerCalls.incrementAndGet();
      this.consumer = consumer;
    }

    @Override
    public void deregister(String serverUri, ChannelConsumer consumer) {
      assertEquals(SERVER_URI, serverUri);
      deregisterCalls.incrementAndGet();
      if (this.consumer == consumer) {
        this.consumer = null;
      }
    }

    private void stop(Throwable cause) {
      ChannelConsumer registeredConsumer = consumer;
      assertNotNull(registeredConsumer);
      registeredConsumer.listenerStopped(cause);
    }
  }
}
