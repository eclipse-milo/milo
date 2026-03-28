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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectHandle;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultApplicationGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.RsaSha256CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * End-to-end integration tests for the multiplexed {@link MultiplexedReverseConnectListener}.
 *
 * <p>These tests verify that a shared client-side listener can accept inbound OPC UA Reverse
 * Connect connections from one or more servers and dispatch each connection to the correct {@link
 * OpcUaClient} instance.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiplexedReverseConnectListenerTest {

  private static final long TIMEOUT_SECONDS = 30;

  private OpcUaServer server;
  private String serverUri;

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

    OpcTcpServerTransportConfig transportConfig =
        OpcTcpServerTransportConfig.newBuilder().setHelloDeadline(uint(5_000)).build();

    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    server.setReverseConnectManager(manager);
    server.startup().get();

    serverUri = server.getConfig().getApplicationUri();
  }

  @AfterAll
  void tearDown() throws Exception {
    server.shutdown().get(10, TimeUnit.SECONDS);
  }

  /**
   * Verify that a pre-registered client can connect to a server via the shared listener. The client
   * creates a {@link OpcTcpMultiplexedReverseConnectTransport} before the server dials in, so the
   * connection is dispatched directly to the registered consumer.
   */
  @Test
  @Order(1)
  void preRegisteredClientConnectsViaSharedListener() throws Exception {
    var listener = createListener();
    listener.start();

    try {
      OpcTcpMultiplexedReverseConnectTransport transport = listener.createTransport(serverUri);

      var clientConfig =
          OpcUaClientConfig.builder()
              .setEndpoint(getNoneSecurityEndpoint(server))
              .setApplicationName(LocalizedText.english("listener test client"))
              .setApplicationUri("urn:eclipse:milo:test:listener-client")
              .setRequestTimeout(uint(30_000))
              .build();

      var client = new OpcUaClient(clientConfig, transport);

      CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

      InetSocketAddress localAddress = listener.getLocalAddress();
      assertNotNull(localAddress);
      int listenerPort = localAddress.getPort();
      ReverseConnectHandle handle =
          server.addReverseConnect(
              "opc.tcp://localhost:" + listenerPort, getServerEndpointUrl(server));

      try {
        connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        List<DataValue> values =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(values);
        assertNotNull(values.get(0).getValue().getValue());
      } finally {
        server.removeReverseConnect(handle);
        try {
          client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
      }
    } finally {
      listener.stop().get(10, TimeUnit.SECONDS);
    }
  }

  /**
   * Verify that two servers with different ApplicationURIs can dial into the same shared listener
   * and each connection is dispatched to the correct client. This is the core multiplexing test.
   */
  @Test
  @Order(2)
  void multipleServersDispatchToCorrectClients() throws Exception {
    // server (from @BeforeAll) is server1.
    // Create server2 with a different ApplicationUri.
    String server2Uri = "urn:eclipse:milo:test:server-2";
    OpcUaServer server2 = createServerWithUri(server2Uri);

    ReverseConnectConfig rcConfig =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(Duration.ofMillis(500))
            .setConnectTimeout(Duration.ofSeconds(5))
            .setRejectBackoff(Duration.ofMillis(500))
            .setMaxReconnectDelay(Duration.ofSeconds(2))
            .build();
    OpcTcpServerTransportConfig s2TransportConfig =
        OpcTcpServerTransportConfig.newBuilder().setHelloDeadline(uint(5_000)).build();
    var manager2 = new ReverseConnectManager(rcConfig, s2TransportConfig);
    server2.setReverseConnectManager(manager2);
    server2.startup().get();

    var listener = createListener();
    listener.start();

    try {
      // Create one transport per server, both on the same listener.
      OpcTcpMultiplexedReverseConnectTransport transport1 = listener.createTransport(serverUri);
      OpcTcpMultiplexedReverseConnectTransport transport2 = listener.createTransport(server2Uri);

      var client1 =
          new OpcUaClient(
              OpcUaClientConfig.builder()
                  .setEndpoint(getNoneSecurityEndpoint(server))
                  .setApplicationName(LocalizedText.english("listener client 1"))
                  .setApplicationUri("urn:eclipse:milo:test:listener-client-1")
                  .setRequestTimeout(uint(30_000))
                  .build(),
              transport1);

      var client2 =
          new OpcUaClient(
              OpcUaClientConfig.builder()
                  .setEndpoint(getNoneSecurityEndpoint(server2))
                  .setApplicationName(LocalizedText.english("listener client 2"))
                  .setApplicationUri("urn:eclipse:milo:test:listener-client-2")
                  .setRequestTimeout(uint(30_000))
                  .build(),
              transport2);

      CompletableFuture<OpcUaClient> connectFuture1 = client1.connectAsync();
      CompletableFuture<OpcUaClient> connectFuture2 = client2.connectAsync();

      InetSocketAddress localAddress = listener.getLocalAddress();
      assertNotNull(localAddress);
      int listenerPort = localAddress.getPort();
      String listenerUrl = "opc.tcp://localhost:" + listenerPort;

      // Both servers dial the same listener port.
      ReverseConnectHandle handle1 =
          server.addReverseConnect(listenerUrl, getServerEndpointUrl(server));
      ReverseConnectHandle handle2 =
          server2.addReverseConnect(listenerUrl, getServerEndpointUrl(server2));

      try {
        connectFuture1.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        connectFuture2.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(client1.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertNotNull(client2.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        // Both clients can make independent service calls.
        List<DataValue> values1 =
            client1
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(values1);
        assertNotNull(values1.get(0).getValue().getValue());

        List<DataValue> values2 =
            client2
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(values2);
        assertNotNull(values2.get(0).getValue().getValue());
      } finally {
        server.removeReverseConnect(handle1);
        server2.removeReverseConnect(handle2);
        try {
          client1.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
        try {
          client2.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
      }
    } finally {
      listener.stop().get(10, TimeUnit.SECONDS);
      server2.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  /**
   * Verify that after the underlying channel is dropped, the server reconnects and the session is
   * re-established through the shared listener.
   */
  @Test
  @Order(3)
  void reconnectionAfterChannelDrop() throws Exception {
    var listener = createListener();
    listener.start();

    try {
      OpcTcpMultiplexedReverseConnectTransport transport = listener.createTransport(serverUri);

      var clientConfig =
          OpcUaClientConfig.builder()
              .setEndpoint(getNoneSecurityEndpoint(server))
              .setApplicationName(LocalizedText.english("reconnection test client"))
              .setApplicationUri("urn:eclipse:milo:test:reconnection-client")
              .setRequestTimeout(uint(30_000))
              .build();

      var client = new OpcUaClient(clientConfig, transport);

      CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

      InetSocketAddress localAddress = listener.getLocalAddress();
      assertNotNull(localAddress);
      int listenerPort = localAddress.getPort();
      ReverseConnectHandle handle =
          server.addReverseConnect(
              "opc.tcp://localhost:" + listenerPort, getServerEndpointUrl(server));

      try {
        connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        // Register a listener so we can wait for the session to be fully
        // re-activated after the forced channel drop.
        var sessionReactivated = new CompletableFuture<Void>();
        client.addSessionActivityListener(
            new SessionActivityListener() {
              @Override
              public void onSessionActive(UaSession session) {
                sessionReactivated.complete(null);
              }
            });

        // Force-close the underlying channel to simulate connection loss.
        transport.getChannelFsm().getChannel().close().sync();

        // Wait for the server to reconnect and re-establish the session.
        sessionReactivated.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // A successful read proves the session was re-established.
        List<DataValue> values =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(values);
        assertNotNull(values.get(0).getValue().getValue());
      } finally {
        server.removeReverseConnect(handle);
        try {
          client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
      }
    } finally {
      listener.stop().get(10, TimeUnit.SECONDS);
    }
  }

  /**
   * Verify on-demand client creation via the {@link
   * MultiplexedReverseConnectListenerConfig#getEndpointResolver()} path. A server dials into the
   * listener with no pre-registered transport. The resolver returns a cached endpoint (1-shot), the
   * listener creates a transport and builds an {@link OpcUaClient} using the resolved endpoint and
   * the {@link MultiplexedReverseConnectListenerConfig#getClientCustomizer()}, then notifies the
   * application via {@link ClientListener}. The application calls {@code connectAsync()}, and the
   * server's next connection dispatches to the newly registered transport.
   */
  @Test
  @Order(4)
  void onDemandClientCreatedForUnknownServer() throws Exception {
    var clientReady = new CompletableFuture<OpcUaClient>();

    // 1-shot resolver: returns a cached endpoint without calling discovery.
    var listenerConfig =
        MultiplexedReverseConnectListenerConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setTransportConfig(OpcTcpClientTransportConfig.newBuilder().build())
            .setRateLimitingEnabled(false)
            .setEndpointResolver(EndpointResolver.cached(getNoneSecurityEndpoint(server)))
            .setClientCustomizer(
                (serverUri, builder) ->
                    builder
                        .setApplicationName(LocalizedText.english("on-demand test client"))
                        .setApplicationUri("urn:eclipse:milo:test:on-demand-client")
                        .setRequestTimeout(uint(30_000)))
            .setClientListener(
                client ->
                    client
                        .connectAsync()
                        .whenComplete(
                            (c, ex) -> {
                              if (ex != null) {
                                clientReady.completeExceptionally(ex);
                              } else {
                                clientReady.complete(c);
                              }
                            }))
            .build();

    var listener = new MultiplexedReverseConnectListener(listenerConfig);
    listener.start();

    try {
      InetSocketAddress localAddress = listener.getLocalAddress();
      assertNotNull(localAddress);
      int listenerPort = localAddress.getPort();
      ReverseConnectHandle handle =
          server.addReverseConnect(
              "opc.tcp://localhost:" + listenerPort, getServerEndpointUrl(server));

      try {
        // Wait for the on-demand client to be created and connected.
        // Flow: server dials in → resolver → listener builds OpcUaClient →
        // ClientListener calls connectAsync() → server reconnects → session up.
        OpcUaClient client = clientReady.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        List<DataValue> values =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(values);
        assertNotNull(values.get(0).getValue().getValue());

        try {
          client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
      } finally {
        server.removeReverseConnect(handle);
      }
    } finally {
      listener.stop().get(10, TimeUnit.SECONDS);
    }
  }

  /**
   * Verify 2-shot discovery for an unknown server via {@link EndpointResolver#discover(
   * java.util.function.BiFunction)}. A server dials into the listener with no pre-registered
   * transport. The resolver calls {@link EndpointResolver.Discovery#getEndpoints()}, which performs
   * Hello/Ack/OPN/GetEndpoints on the inbound channel (consuming it). The server reconnects, and
   * the session is established on the second connection.
   */
  @Test
  @Order(5)
  void twoShotDiscoveryForUnknownServer() throws Exception {
    var clientReady = new CompletableFuture<OpcUaClient>();

    // 2-shot resolver: performs GetEndpoints discovery on the inbound channel,
    // then selects the None-security endpoint.
    EndpointResolver resolver =
        EndpointResolver.discover(
            (serverUri, endpoints) ->
                endpoints.stream()
                    .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                    .findFirst()
                    .orElseThrow());

    var listenerConfig =
        MultiplexedReverseConnectListenerConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setTransportConfig(OpcTcpClientTransportConfig.newBuilder().build())
            .setRateLimitingEnabled(false)
            .setEndpointResolver(resolver)
            .setClientCustomizer(
                (serverUri, builder) ->
                    builder
                        .setApplicationName(LocalizedText.english("2-shot discovery test client"))
                        .setApplicationUri("urn:eclipse:milo:test:2-shot-discovery-client")
                        .setRequestTimeout(uint(30_000)))
            .setClientListener(
                client ->
                    client
                        .connectAsync()
                        .whenComplete(
                            (c, ex) -> {
                              if (ex != null) {
                                clientReady.completeExceptionally(ex);
                              } else {
                                clientReady.complete(c);
                              }
                            }))
            .build();

    var listener = new MultiplexedReverseConnectListener(listenerConfig);
    listener.start();

    try {
      InetSocketAddress localAddress = listener.getLocalAddress();
      assertNotNull(localAddress);
      int listenerPort = localAddress.getPort();
      ReverseConnectHandle handle =
          server.addReverseConnect(
              "opc.tcp://localhost:" + listenerPort, getServerEndpointUrl(server));

      try {
        // Wait for the on-demand client to be created and connected.
        // Flow: server dials in → resolver calls discovery.getEndpoints() →
        // channel consumed → server reconnects → listener builds OpcUaClient →
        // ClientListener calls connectAsync() → session up.
        OpcUaClient client = clientReady.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        List<DataValue> values =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNotNull(values);
        assertNotNull(values.get(0).getValue().getValue());

        try {
          client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // don't mask the original error
        }
      } finally {
        server.removeReverseConnect(handle);
      }
    } finally {
      listener.stop().get(10, TimeUnit.SECONDS);
    }
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  /**
   * Create a {@link MultiplexedReverseConnectListener} with default configuration bound to an
   * ephemeral port.
   */
  private MultiplexedReverseConnectListener createListener() {
    var config =
        MultiplexedReverseConnectListenerConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setTransportConfig(OpcTcpClientTransportConfig.newBuilder().build())
            .setRateLimitingEnabled(false)
            .build();

    return new MultiplexedReverseConnectListener(config);
  }

  private static String getServerEndpointUrl(OpcUaServer srv) {
    return srv.getConfig().getEndpoints().stream()
        .map(EndpointConfig::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow();
  }

  private static EndpointDescription getNoneSecurityEndpoint(OpcUaServer srv) {
    String endpointUrl = getServerEndpointUrl(srv);

    var serverDescription =
        new ApplicationDescription(
            endpointUrl, null, LocalizedText.NULL_VALUE, ApplicationType.Server, null, null, null);

    return new EndpointDescription(
        endpointUrl,
        serverDescription,
        null,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  /**
   * Create a minimal {@link OpcUaServer} with the given {@code applicationUri}. Uses a fresh
   * self-signed certificate so the server has a unique identity. Only None-security endpoints are
   * configured.
   */
  private static OpcUaServer createServerWithUri(String applicationUri) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("Test Server")
            .setApplicationUri(applicationUri)
            .build();

    var trustListManager = new MemoryTrustListManager();
    var certificateStore = new MemoryCertificateStore();
    var certificateQuarantine = new MemoryCertificateQuarantine();
    var certificateFactory =
        new RsaSha256CertificateFactory() {
          @Override
          protected KeyPair createRsaSha256KeyPair() {
            return keyPair;
          }

          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair kp) {
            return new X509Certificate[] {certificate};
          }
        };
    var certificateValidator =
        new DefaultServerCertificateValidator(trustListManager, certificateQuarantine);
    var defaultGroup =
        DefaultApplicationGroup.createAndInitialize(
            trustListManager, certificateStore, certificateFactory, certificateValidator);
    var certificateManager = new DefaultCertificateManager(certificateQuarantine, defaultGroup);

    int port;
    try (ServerSocket ss = new ServerSocket(0)) {
      port = ss.getLocalPort();
    }

    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    for (String hostname : HostnameUtil.getHostnames("localhost", true)) {
      endpoints.add(
          EndpointConfig.newBuilder()
              .setBindAddress("localhost")
              .setHostname(hostname)
              .setPath("/test")
              .setCertificate(certificate)
              .setSecurityPolicy(SecurityPolicy.None)
              .setSecurityMode(MessageSecurityMode.None)
              .addTokenPolicies(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .setBindPort(port)
              .build());
    }

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri(applicationUri)
            .setApplicationName(LocalizedText.english("Test Server " + applicationUri))
            .setEndpoints(endpoints)
            .setCertificateManager(certificateManager)
            .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
            .setProductUri(applicationUri)
            .build();

    return new OpcUaServer(
        serverConfig,
        tp -> {
          if (tp == TransportProfile.TCP_UASC_UABINARY) {
            return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
          }
          throw new RuntimeException("unexpected TransportProfile: " + tp);
        });
  }
}
