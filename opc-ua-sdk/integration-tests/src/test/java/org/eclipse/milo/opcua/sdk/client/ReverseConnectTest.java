/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.channel.Channel;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectHandle;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransportConfig;
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
 * End-to-end integration tests for the OPC UA Reverse Connect mechanism.
 *
 * <p>These tests verify Reverse Connect behavior using a real {@link OpcUaClient} backed by {@link
 * OpcTcpReverseConnectTransport} and a real {@link OpcUaServer} with a {@link
 * ReverseConnectManager}. No stubs or mocks are used.
 *
 * <p>Tests are ordered so that the URI rejection test (which produces a burst of rejected
 * connections) runs after the normal connection scenarios, preventing its connection churn from
 * slowing them down.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReverseConnectTest {

  /**
   * Timeout for operations that include the full reverse-connect lifecycle: server connect
   * interval, ReverseHello handshake, OpenSecureChannel, CreateSession, ActivateSession, and
   * session initializers. 10 seconds is generous for localhost connections.
   */
  private static final long TIMEOUT_SECONDS = 30;

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

    OpcTcpServerTransportConfig transportConfig =
        OpcTcpServerTransportConfig.newBuilder().setHelloDeadline(uint(5_000)).build();

    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    server.setReverseConnectManager(manager);
    server.startup().get();
  }

  @AfterAll
  void tearDown() throws Exception {
    server.shutdown().get(10, TimeUnit.SECONDS);
  }

  @Test
  @Order(1)
  void reverseConnectSession() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setReverseHelloTimeout(5_000)
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);
    EndpointDescription endpoint = getNoneSecurityEndpoint();

    var clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationName(LocalizedText.english("reverse connect test client"))
            .setApplicationUri("urn:eclipse:milo:test:reverse-connect-client")
            .setRequestTimeout(uint(30_000))
            .build();

    var client = new OpcUaClient(clientConfig, transport);

    // connectAsync() synchronously binds the listener before returning the future
    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    int clientPort = getListenPort(transport);
    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort, getServerEndpointUrl());

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
  }

  @Test
  @Order(2)
  void reverseConnectReconnection() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setReverseHelloTimeout(5_000)
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);
    EndpointDescription endpoint = getNoneSecurityEndpoint();

    var clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationName(LocalizedText.english("reverse connect test client"))
            .setApplicationUri("urn:eclipse:milo:test:reverse-connect-client")
            .setRequestTimeout(uint(30_000))
            .build();

    var client = new OpcUaClient(clientConfig, transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    int clientPort = getListenPort(transport);
    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort, getServerEndpointUrl());

    try {
      connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

      // Register a SessionActivityListener so we can wait for the session
      // to be fully re-activated on the new secure channel after reconnection.
      // This avoids a race between the SessionFsm processing ConnectionLost
      // and our readValuesAsync call using a stale session.
      var sessionReactivated = new CompletableFuture<Void>();
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              sessionReactivated.complete(null);
            }
          });

      // Force-close the underlying channel to simulate connection loss
      Channel channel = transport.getActiveChannel();
      assertNotNull(channel);
      channel.close().sync();

      // Wait for the session to be fully re-established on the new channel.
      // This includes: server reconnects, new handshake, ActivateSession,
      // and session initializers.
      sessionReactivated.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      // A successful service call proves the session was re-established.
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
  }

  @Test
  @Order(3)
  void multipleClientsConnectIndependently() throws Exception {
    var transportConfig1 =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setReverseHelloTimeout(5_000)
            .build();
    var transport1 = new OpcTcpReverseConnectTransport(transportConfig1);
    OpcUaClient client1 =
        createReverseConnectClient(
            "reverse connect test client 1",
            "urn:eclipse:milo:test:reverse-connect-client-1",
            transport1);

    var transportConfig2 =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setReverseHelloTimeout(5_000)
            .build();
    var transport2 = new OpcTcpReverseConnectTransport(transportConfig2);
    OpcUaClient client2 =
        createReverseConnectClient(
            "reverse connect test client 2",
            "urn:eclipse:milo:test:reverse-connect-client-2",
            transport2);

    CompletableFuture<OpcUaClient> connectFuture1 = client1.connectAsync();
    CompletableFuture<OpcUaClient> connectFuture2 = client2.connectAsync();

    int clientPort1 = getListenPort(transport1);
    int clientPort2 = getListenPort(transport2);

    String endpointUrl = getServerEndpointUrl();
    ReverseConnectHandle handle1 =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort1, endpointUrl);
    ReverseConnectHandle handle2 =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort2, endpointUrl);

    try {
      connectFuture1.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      connectFuture2.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      // Both clients should have active sessions
      assertNotNull(client1.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));
      assertNotNull(client2.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));

      // Both clients should be able to make service calls independently
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
      server.removeReverseConnect(handle2);
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
  }

  @Test
  @Order(4)
  void createReverseConnectBuffersEarlyPass2UntilConnect() throws Exception {
    int clientPort = getAvailablePort();

    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", clientPort))
            .setReverseHelloTimeout(5_000)
            .setConnectTimeout(5_000)
            .build();

    CompletableFuture<OpcUaClient> clientFuture =
        OpcUaClient.createReverseConnect(
            transportConfig,
            this::selectNoneSecuritySessionEndpoint,
            this::configureCreateReverseConnectClient);

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort, getServerEndpointUrl());

    OpcUaClient client = null;
    try {
      client = clientFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      // Give the server retry loop time to deliver pass 2 before the real client context exists.
      Thread.sleep(1_200);

      client.connectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertNotNull(client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS));
    } finally {
      server.removeReverseConnect(handle);
      disconnectQuietly(client);
    }
  }

  @Test
  @Order(5)
  void createReverseConnectTimesOutWhenPass2DoesNotReconnect() throws Exception {
    int clientPort = getAvailablePort();
    var handleRef = new AtomicReference<ReverseConnectHandle>();

    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", clientPort))
            .setReverseHelloTimeout(5_000)
            .setConnectTimeout(750)
            .build();

    handleRef.set(
        server.addReverseConnect("opc.tcp://localhost:" + clientPort, getServerEndpointUrl()));

    OpcUaClient client = null;
    try {
      client =
          OpcUaClient.createReverseConnect(
                  transportConfig,
                  endpoints -> {
                    removeReverseConnect(handleRef);
                    return selectNoneSecuritySessionEndpoint(endpoints);
                  },
                  this::configureCreateReverseConnectClient)
              .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      OpcUaClient unconnectedClient = client;
      ExecutionException ex =
          assertThrows(
              ExecutionException.class,
              () -> unconnectedClient.connectAsync().get(10, TimeUnit.SECONDS));

      UaException uaException = assertInstanceOf(UaException.class, ex.getCause());
      assertEquals(StatusCodes.Bad_Timeout, uaException.getStatusCode().value());
    } finally {
      removeReverseConnect(handleRef);
      disconnectQuietly(client);
    }
  }

  @Test
  @Order(6)
  void createReverseConnectCleansUpListenerWhenDiscoveryTimesOut() throws Exception {
    int clientPort = getAvailablePort();

    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", clientPort))
            .setReverseHelloTimeout(5_000)
            .setConnectTimeout(500)
            .build();

    ExecutionException ex =
        assertThrows(
            ExecutionException.class,
            () ->
                OpcUaClient.createReverseConnect(
                        transportConfig,
                        this::selectNoneSecuritySessionEndpoint,
                        this::configureCreateReverseConnectClient)
                    .get(10, TimeUnit.SECONDS));

    assertInstanceOf(TimeoutException.class, ex.getCause());
    assertPortAvailable(clientPort);
  }

  @Test
  @Order(7)
  void reverseConnectServerUriRejection() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", 0))
            .setReverseHelloTimeout(5_000)
            .setAllowedServerUris(Set.of("urn:bogus:server:that:does:not:match"))
            .build();

    var transport = new OpcTcpReverseConnectTransport(transportConfig);
    EndpointDescription endpoint = getNoneSecurityEndpoint();

    var clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationName(LocalizedText.english("reverse connect test client"))
            .setApplicationUri("urn:eclipse:milo:test:reverse-connect-client")
            .setRequestTimeout(uint(30_000))
            .build();

    var client = new OpcUaClient(clientConfig, transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    int clientPort = getListenPort(transport);
    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + clientPort, getServerEndpointUrl());

    try {
      // The server's ApplicationUri won't match "urn:bogus:server:that:does:not:match",
      // so the client transport should reject the ReverseHello and fail the connect waiter.
      ExecutionException ex =
          assertThrows(
              ExecutionException.class, () -> connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS));
      UaException uaException = assertInstanceOf(UaException.class, ex.getCause());
      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, uaException.getStatusCode().value());
    } finally {
      server.removeReverseConnect(handle);
      try {
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      } catch (Exception ignored) {
        // may already be disconnected
      }
    }
  }

  /**
   * Verify that {@code OpcUaServer.shutdown()} completes without deadlock even when called from a
   * single-threaded executor (simulating a Netty I/O or FSM executor thread). Before the fix, the
   * blocking {@code .join()} in shutdown could deadlock when the reverse connect manager's FSM
   * needed the same thread to complete its stop transition.
   */
  @Test
  @Order(8)
  void shutdownCompletesWithoutDeadlock() throws Exception {
    // Use an independent server so we don't affect the shared instance.
    TestServer testServer = TestServer.create();
    OpcUaServer localServer = testServer.getServer();

    ReverseConnectConfig rcConfig =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(Duration.ofMillis(500))
            .setConnectTimeout(Duration.ofSeconds(5))
            .build();

    OpcTcpServerTransportConfig transportConfig = OpcTcpServerTransportConfig.newBuilder().build();

    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    localServer.setReverseConnectManager(manager);
    localServer.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    // Submit shutdown() from a single-threaded executor to simulate calling
    // it from an I/O thread. If shutdown() blocks (e.g. via .join()), it
    // will deadlock or exceed the timeout.
    ExecutorService singleThread = Executors.newSingleThreadExecutor();
    try {
      CompletableFuture<OpcUaServer> shutdownFuture =
          CompletableFuture.supplyAsync(localServer::shutdown, singleThread).thenCompose(f -> f);

      shutdownFuture.get(10, TimeUnit.SECONDS);
    } finally {
      singleThread.shutdownNow();
    }
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private OpcUaClient createReverseConnectClient(
      String appName, String appUri, OpcTcpReverseConnectTransport transport) {

    var clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(getNoneSecurityEndpoint())
            .setApplicationName(LocalizedText.english(appName))
            .setApplicationUri(appUri)
            .setRequestTimeout(uint(30_000))
            .build();

    return new OpcUaClient(clientConfig, transport);
  }

  private void configureCreateReverseConnectClient(OpcUaClientConfigBuilder builder) {
    builder
        .setApplicationName(LocalizedText.english("reverse connect factory test client"))
        .setApplicationUri("urn:eclipse:milo:test:reverse-connect-factory-client")
        .setRequestTimeout(uint(30_000));
  }

  private Optional<EndpointDescription> selectNoneSecuritySessionEndpoint(
      List<EndpointDescription> endpoints) {

    return endpoints.stream()
        .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
        .filter(e -> e.getEndpointUrl() != null && !e.getEndpointUrl().endsWith("/discovery"))
        .findFirst();
  }

  private void removeReverseConnect(AtomicReference<ReverseConnectHandle> handleRef) {
    ReverseConnectHandle handle = handleRef.getAndSet(null);
    if (handle != null) {
      server.removeReverseConnect(handle);
    }
  }

  private void disconnectQuietly(OpcUaClient client) {
    if (client == null) {
      return;
    }

    try {
      client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // don't mask the original error
    }
  }

  private int getAvailablePort() throws Exception {
    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("localhost", 0));
      return socket.getLocalPort();
    }
  }

  private void assertPortAvailable(int port) throws Exception {
    try (ServerSocket socket = new ServerSocket()) {
      socket.setReuseAddress(true);
      socket.bind(new InetSocketAddress("localhost", port));
    }
  }

  /**
   * Read the actual port that the transport's ServerBootstrap bound to. The transport calls {@code
   * startListening()} synchronously inside {@code connect()}, so by the time {@code connectAsync()}
   * returns the {@code serverChannel} field is set.
   */
  private int getListenPort(OpcTcpReverseConnectTransport transport) throws Exception {
    Field field = OpcTcpReverseConnectTransport.class.getDeclaredField("serverChannel");
    field.setAccessible(true);

    Channel serverChannel = (Channel) field.get(transport);
    assertNotNull(serverChannel, "serverChannel should be set after connectAsync()");

    return ((InetSocketAddress) serverChannel.localAddress()).getPort();
  }

  private String getServerEndpointUrl() {
    return server.getConfig().getEndpoints().stream()
        .map(EndpointConfig::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow();
  }

  private EndpointDescription getNoneSecurityEndpoint() {
    String endpointUrl = getServerEndpointUrl();

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
}
