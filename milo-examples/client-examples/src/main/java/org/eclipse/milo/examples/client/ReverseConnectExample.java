/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import static java.util.Objects.requireNonNull;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.server.ExampleServer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectHandle;
import org.eclipse.milo.opcua.sdk.server.ReverseConnectManager;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example demonstrating 1-1 OPC UA Reverse Connect using Milo's own client and server.
 *
 * <p>In Reverse Connect, the server initiates the TCP connection to the client. This example starts
 * an {@link ExampleServer} with a {@link ReverseConnectManager}, then creates a client that listens
 * for the inbound server connection. The client performs 2-shot discovery (first connection: {@code
 * GetEndpoints}; second connection: session), reads a few nodes, and shuts down.
 *
 * <p>This example uses {@link SecurityPolicy#None} for simplicity.
 */
public class ReverseConnectExample {

  private static final InetSocketAddress LISTEN_ADDRESS = new InetSocketAddress("localhost", 48060);

  private static final Logger logger = LoggerFactory.getLogger(ReverseConnectExample.class);

  public static void main(String[] args) throws Exception {
    // 1. Create and configure the server with a ReverseConnectManager.
    var exampleServer = new ExampleServer(12686, builder -> {}, "urn:eclipse:milo:examples:server");

    var rcConfig =
        ReverseConnectConfig.newBuilder().setConnectInterval(Duration.ofSeconds(2)).build();
    var transportConfig = OpcTcpServerTransportConfig.newBuilder().build();
    var manager = new ReverseConnectManager(rcConfig, transportConfig);

    exampleServer.getServer().setReverseConnectManager(manager);
    exampleServer.startup().get();

    try {
      // 2. Start the client's listening socket and initiate 2-shot discovery.
      //    createReverseConnect() binds the socket immediately; the returned future
      //    completes with a configured (but not connected) client once the server
      //    dials in and GetEndpoints succeeds.
      var listenConfig =
          OpcTcpReverseConnectTransportConfig.newBuilder().setListenAddress(LISTEN_ADDRESS).build();

      CompletableFuture<OpcUaClient> clientFuture =
          OpcUaClient.createReverseConnect(
              listenConfig,
              endpoints ->
                  endpoints.stream()
                      .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                      .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                      .findFirst(),
              builder ->
                  builder
                      .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                      .setApplicationUri("urn:eclipse:milo:examples:client"));

      // 3. Tell the server to dial the client. The server's first connection
      //    is consumed by GetEndpoints (discovery). After that channel closes,
      //    the manager reconnects for the real session.
      String clientListenUrl = "opc.tcp://localhost:" + LISTEN_ADDRESS.getPort();
      ReverseConnectHandle handle = null;
      OpcUaClient client = null;

      try {
        handle = exampleServer.getServer().addReverseConnect(clientListenUrl);

        // 4. Await discovery, then connect the session on the second connection.
        client = clientFuture.get(60, TimeUnit.SECONDS);
        client.connectAsync().get(30, TimeUnit.SECONDS);

        // 5. Print the server's identity from the negotiated endpoint.
        var server = client.getConfig().getEndpoint().getServer();
        logger.info("ApplicationUri: {}", server.getApplicationUri());
        logger.info("ApplicationName: {}", server.getApplicationName().getText());

        // 6. Read nodes.
        readServerStatus(client);
      } finally {
        if (handle != null) {
          exampleServer.getServer().removeReverseConnect(handle);
        }
        if (client != null) {
          client.disconnectAsync().get(5, TimeUnit.SECONDS);
        }
      }
    } finally {
      Thread.sleep(1000);
      exampleServer.shutdown().get();
      Stack.releaseSharedResources();
    }
  }

  private static void readServerStatus(OpcUaClient client) throws Exception {
    List<NodeId> nodeIds =
        List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime);

    List<DataValue> values =
        client.readValuesAsync(0.0, TimestampsToReturn.Both, nodeIds).get(5, TimeUnit.SECONDS);

    DataValue stateValue = values.get(0);
    DataValue timeValue = values.get(1);

    logger.info("State={}", ServerState.from((Integer) requireNonNull(stateValue.value().value())));
    logger.info("CurrentTime={}", timeValue.value().value());
  }
}
