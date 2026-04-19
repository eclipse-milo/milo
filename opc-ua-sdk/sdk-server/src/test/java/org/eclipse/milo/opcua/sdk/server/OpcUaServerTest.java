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
    private volatile String startedServerUri;

    ControlledReverseConnectManager(CompletableFuture<Void> startFuture) {
      super(
          ReverseConnectConfig.newBuilder()
              .setConnectInterval(Duration.ofMillis(100))
              .setConnectTimeout(Duration.ofSeconds(5))
              .setRejectBackoff(Duration.ofMillis(500))
              .setMaxReconnectDelay(Duration.ofMillis(800))
              .build(),
          OpcTcpServerTransportConfig.newBuilder().build());

      this.startFuture = startFuture;
    }

    @Override
    CompletableFuture<Void> start(ServerApplicationContext applicationContext, String serverUri) {
      startedServerUri = serverUri;
      return startFuture;
    }

    @Override
    CompletableFuture<Void> stop() {
      return CompletableFuture.completedFuture(null);
    }
  }
}
