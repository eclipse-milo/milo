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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectHandle;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.test.TestServer;
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
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Transport-level integration tests for {@link OpcTcpReverseConnectTransport}.
 *
 * <p>These tests verify the transport layer in isolation from the SDK layer ({@code OpcUaClient},
 * {@code SessionFsm}). A real {@link OpcUaServer} with a {@link ReverseConnectManager} provides the
 * server-side Reverse Connect behavior.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpcTcpReverseConnectTransportTest {

  private OpcUaServer server;

  @BeforeAll
  void setUp() throws Exception {
    TestServer testServer = TestServer.create();
    server = testServer.getServer();

    ReverseConnectConfig rcConfig =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(Duration.ofMillis(500))
            .setConnectTimeout(Duration.ofSeconds(5))
            .setRejectBackoff(Duration.ofMillis(500))
            .setMaxReconnectDelay(Duration.ofSeconds(2))
            .build();

    OpcTcpServerTransportConfig transportConfig = OpcTcpServerTransportConfig.newBuilder().build();

    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    server.setReverseConnectManager(manager);
    server.startup().get();
  }

  @AfterAll
  void tearDown() throws Exception {
    server.shutdown().get(5, TimeUnit.SECONDS);
  }

  @Test
  void connectThenDisconnect() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);

    var clientCtx = newClientApplicationContext();

    CompletableFuture<Unit> connectFuture = transport.connect(clientCtx);

    int listenPort = getListenPort(transport);

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      connectFuture.get(10, TimeUnit.SECONDS);

      io.netty.channel.Channel channel = transport.getChannelFsm().getChannel();
      assertNotNull(channel, "channel should be set after connect");
      assertTrue(channel.isActive(), "channel should be active after connect");
    } finally {
      server.removeReverseConnect(handle);
      transport.disconnect().get(5, TimeUnit.SECONDS);
    }
  }

  @Test
  void serverUriRejection() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setAllowedServerUris(Set.of("urn:bogus:server:that:does:not:match"))
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);

    var clientCtx = newClientApplicationContext();

    CompletableFuture<Unit> connectFuture = transport.connect(clientCtx);

    int listenPort = getListenPort(transport);

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      // The server's ApplicationUri won't match the allowed set, so the client
      // transport should reject every ReverseHello and the future should not complete.
      assertThrows(TimeoutException.class, () -> connectFuture.get(3, TimeUnit.SECONDS));
    } finally {
      server.removeReverseConnect(handle);
      try {
        transport.disconnect().get(5, TimeUnit.SECONDS);
      } catch (Exception ignored) {
        // may already be disconnected
      }
    }
  }

  @Test
  void permissiveModeAcceptsAnyServerUri() throws Exception {
    // Empty allowedServerUris = permissive mode (default)
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);

    var clientCtx = newClientApplicationContext();

    CompletableFuture<Unit> connectFuture = transport.connect(clientCtx);

    int listenPort = getListenPort(transport);

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      connectFuture.get(10, TimeUnit.SECONDS);

      io.netty.channel.Channel channel = transport.getChannelFsm().getChannel();
      assertNotNull(channel);
      assertTrue(channel.isActive());
    } finally {
      server.removeReverseConnect(handle);
      transport.disconnect().get(5, TimeUnit.SECONDS);
    }
  }

  @Test
  void reconnectAfterChannelDrop() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);

    var clientCtx = newClientApplicationContext();

    CompletableFuture<Unit> connectFuture = transport.connect(clientCtx);

    int listenPort = getListenPort(transport);

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      connectFuture.get(10, TimeUnit.SECONDS);

      // Force-close the underlying channel
      io.netty.channel.Channel channel = transport.getChannelFsm().getChannel();
      assertNotNull(channel);
      channel.close().sync();

      // The server should reconnect and the transport FSM should re-establish a channel.
      // getChannel() returns a future that completes when a new channel is available.
      var getChannel = new ReverseConnectChannelFsm.Event.GetChannel(new CompletableFuture<>());
      transport.getChannelFsm().fireEvent(getChannel);

      io.netty.channel.Channel newChannel = getChannel.future().get(15, TimeUnit.SECONDS);
      assertNotNull(newChannel, "new channel should be established after reconnect");
      assertTrue(newChannel.isActive(), "new channel should be active");
    } finally {
      server.removeReverseConnect(handle);
      transport.disconnect().get(5, TimeUnit.SECONDS);
    }
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private int getListenPort(OpcTcpReverseConnectTransport transport) throws Exception {
    Field field = OpcTcpReverseConnectTransport.class.getDeclaredField("serverChannel");
    field.setAccessible(true);

    io.netty.channel.Channel serverChannel = (io.netty.channel.Channel) field.get(transport);
    assertNotNull(serverChannel, "serverChannel should be set after connect()");

    return ((InetSocketAddress) serverChannel.localAddress()).getPort();
  }

  private String getServerEndpointUrl() {
    return server.getConfig().getEndpoints().stream()
        .map(EndpointConfig::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow();
  }

  private ClientApplicationContext newClientApplicationContext() {
    String endpointUrl = getServerEndpointUrl();

    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return new EndpointDescription(
            endpointUrl,
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
