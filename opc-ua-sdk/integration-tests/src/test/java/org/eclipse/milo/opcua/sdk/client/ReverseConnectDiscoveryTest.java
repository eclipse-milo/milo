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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectHandle;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Integration tests for the {@link DiscoveryClient} static Reverse Connect discovery methods:
 * {@link DiscoveryClient#getEndpoints(OpcTcpReverseConnectTransportConfig)} and {@link
 * DiscoveryClient#findServers(OpcTcpReverseConnectTransportConfig)}.
 *
 * <p>The static methods create and manage the transport internally, so the tests reserve an
 * available listen port before each call and pass that port to the server-side reverse-connect
 * handle. The server's retry loop (500ms interval) quickly finds the client once the listening
 * socket opens.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReverseConnectDiscoveryTest {

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

    OpcTcpServerTransportConfig transportConfig = OpcTcpServerTransportConfig.newBuilder().build();

    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    server.setReverseConnectManager(manager);
    server.startup().get();
  }

  @AfterAll
  void tearDown() throws Exception {
    server.shutdown().get(10, TimeUnit.SECONDS);
  }

  @Test
  void getEndpointsViaReverseConnect() throws Exception {
    int listenPort = getAvailablePort();

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", listenPort))
            .build();

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      List<EndpointDescription> endpoints =
          DiscoveryClient.getEndpoints(config).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertFalse(endpoints.isEmpty(), "expected at least one endpoint");

      String serverAppUri = server.getConfig().getApplicationUri();
      assertTrue(
          endpoints.stream()
              .anyMatch(
                  e ->
                      e.getServer() != null
                          && serverAppUri.equals(e.getServer().getApplicationUri())),
          "expected an endpoint with the server's application URI");
    } finally {
      server.removeReverseConnect(handle);
    }
  }

  @Test
  void findServersViaReverseConnect() throws Exception {
    int listenPort = getAvailablePort();

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", listenPort))
            .build();

    ReverseConnectHandle handle =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      List<ApplicationDescription> servers =
          DiscoveryClient.findServers(config).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      assertFalse(servers.isEmpty(), "expected at least one server");

      String serverAppUri = server.getConfig().getApplicationUri();
      assertTrue(
          servers.stream().anyMatch(s -> serverAppUri.equals(s.getApplicationUri())),
          "expected a server with the server's application URI");
    } finally {
      server.removeReverseConnect(handle);
    }
  }

  @Test
  void getEndpointsCleanup() throws Exception {
    int listenPort = getAvailablePort();

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", listenPort))
            .build();

    // First call
    ReverseConnectHandle handle1 =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      List<EndpointDescription> endpoints1 =
          DiscoveryClient.getEndpoints(config).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      assertFalse(endpoints1.isEmpty());
    } finally {
      server.removeReverseConnect(handle1);
    }

    // Second call on the same port — would fail with a bind exception if the
    // first call did not properly release the listening socket.
    ReverseConnectHandle handle2 =
        server.addReverseConnect("opc.tcp://localhost:" + listenPort, getServerEndpointUrl());

    try {
      List<EndpointDescription> endpoints2 =
          DiscoveryClient.getEndpoints(config).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      assertFalse(endpoints2.isEmpty());
    } finally {
      server.removeReverseConnect(handle2);
    }
  }

  @Test
  void getEndpointsTimesOutWhenNoServerConnects() throws Exception {
    int timeoutPort = getAvailablePort();

    var config =
        OpcTcpReverseConnectTransportConfig.newBuilder()
            .setListenAddress(new InetSocketAddress("localhost", timeoutPort))
            .setConnectTimeout(500)
            .build();

    // No server handle registered for this port — the connect will time out.
    ExecutionException ee =
        assertThrows(
            ExecutionException.class,
            () -> DiscoveryClient.getEndpoints(config).get(10, TimeUnit.SECONDS));

    assertReverseConnectTimeout(ee.getCause());

    // Verify the listening port was released by rebinding it.
    assertPortAvailable(timeoutPort);
  }

  private int getAvailablePort() throws Exception {
    try (var socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("localhost", 0));
      return socket.getLocalPort();
    }
  }

  private void assertPortAvailable(int port) throws Exception {
    try (var socket = new ServerSocket()) {
      socket.setReuseAddress(true);
      socket.bind(new InetSocketAddress("localhost", port));
    }
  }

  private void assertReverseConnectTimeout(Throwable cause) {
    Throwable failure = unwrap(cause);
    if (failure instanceof TimeoutException) {
      return;
    }

    UaException uaException = assertInstanceOf(UaException.class, failure);
    assertEquals(StatusCodes.Bad_Timeout, uaException.getStatusCode().value());
  }

  private Throwable unwrap(Throwable cause) {
    if (cause instanceof CompletionException && cause.getCause() != null) {
      return cause.getCause();
    }

    return cause;
  }

  private String getServerEndpointUrl() {
    return server.getConfig().getEndpoints().stream()
        .map(EndpointConfig::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow();
  }
}
