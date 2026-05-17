/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OpcUaServerTest {

  private static final String SERVER_URI = "urn:test:server";

  @Test
  void startupWaitsForReverseConnectManagerStart() throws Exception {
    CompletableFuture<Void> reverseConnectStarted = new CompletableFuture<>();
    ControlledReverseConnectManager reverseConnectManager =
        new ControlledReverseConnectManager(reverseConnectStarted);

    OpcServerTransport transport = Mockito.mock(OpcServerTransport.class);
    OpcServerTransportFactory transportFactory = transportProfile -> transport;

    OpcUaServer server = new OpcUaServer(serverConfig(), transportFactory);
    server.setReverseConnectManager(reverseConnectManager);

    CompletableFuture<OpcUaServer> startupFuture = server.startup();

    // startup() must stay pending until reverse connect has been republished.
    assertFalse(startupFuture.isDone(), "startup should wait for reverse-connect publication");

    reverseConnectStarted.complete(null);

    assertSame(server, startupFuture.get(5, TimeUnit.SECONDS));
    assertEquals(SERVER_URI, reverseConnectManager.startedServerUri);

    server.shutdown().get(5, TimeUnit.SECONDS);
  }

  @Test
  void shutdownWaitsForReverseConnectManagerStop() throws Exception {
    CompletableFuture<Void> reverseConnectStopped = new CompletableFuture<>();
    ControlledReverseConnectManager reverseConnectManager =
        new ControlledReverseConnectManager(
            CompletableFuture.completedFuture(null), reverseConnectStopped);

    OpcServerTransport transport = Mockito.mock(OpcServerTransport.class);
    OpcServerTransportFactory transportFactory = transportProfile -> transport;

    OpcUaServer server = new OpcUaServer(serverConfig(), transportFactory);
    server.setReverseConnectManager(reverseConnectManager);
    server.startup().get(5, TimeUnit.SECONDS);

    CompletableFuture<OpcUaServer> shutdownFuture = server.shutdown();

    assertFalse(shutdownFuture.isDone(), "shutdown should wait for reverse-connect stop");

    reverseConnectStopped.complete(null);

    assertSame(server, shutdownFuture.get(5, TimeUnit.SECONDS));
  }

  @Test
  void startupFailureFromReverseConnectManagerUnbindsTransport() throws Exception {
    RuntimeException startupFailure = new RuntimeException("reverse connect start failed");
    ControlledReverseConnectManager reverseConnectManager =
        new ControlledReverseConnectManager(CompletableFuture.failedFuture(startupFailure));

    OpcServerTransport transport = Mockito.mock(OpcServerTransport.class);
    OpcServerTransportFactory transportFactory = transportProfile -> transport;

    OpcUaServer server = new OpcUaServer(serverConfig(), transportFactory);
    server.setReverseConnectManager(reverseConnectManager);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));

    assertSame(startupFailure, ex.getCause());
    assertTrue(server.getBoundEndpoints().isEmpty());
    assertTrue(reverseConnectManager.stopCalled);
    Mockito.verify(transport).bind(Mockito.any(), Mockito.any());
    Mockito.verify(transport).unbind();
  }

  private static OpcUaServerConfig serverConfig() {
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("localhost")
            .setBindPort(4840)
            .build();

    return OpcUaServerConfig.builder()
        .setApplicationUri(SERVER_URI)
        .setCertificateManager(Mockito.mock(CertificateManager.class))
        .setEndpoints(Set.of(endpoint))
        .build();
  }

  private static final class ControlledReverseConnectManager extends ReverseConnectManager {

    private final CompletableFuture<Void> startFuture;
    private final CompletableFuture<Void> stopFuture;
    private volatile String startedServerUri;
    private volatile boolean stopCalled;

    ControlledReverseConnectManager(CompletableFuture<Void> startFuture) {
      this(startFuture, CompletableFuture.completedFuture(null));
    }

    ControlledReverseConnectManager(
        CompletableFuture<Void> startFuture, CompletableFuture<Void> stopFuture) {
      super(
          ReverseConnectConfig.newBuilder()
              .setConnectInterval(Duration.ofMillis(100))
              .setConnectTimeout(Duration.ofSeconds(5))
              .setRejectBackoff(Duration.ofMillis(500))
              .setMaxReconnectDelay(Duration.ofMillis(800))
              .build(),
          OpcTcpServerTransportConfig.newBuilder().build());

      this.startFuture = startFuture;
      this.stopFuture = stopFuture;
    }

    @Override
    CompletableFuture<Void> start(ServerApplicationContext applicationContext, String serverUri) {
      startedServerUri = serverUri;
      return startFuture;
    }

    @Override
    CompletableFuture<Void> stop() {
      stopCalled = true;
      return stopFuture;
    }
  }
}
