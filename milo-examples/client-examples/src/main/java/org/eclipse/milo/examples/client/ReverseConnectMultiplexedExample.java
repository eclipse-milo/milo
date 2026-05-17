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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.server.ExampleServer;
import org.eclipse.milo.opcua.sdk.client.MultiplexedReverseConnectListener;
import org.eclipse.milo.opcua.sdk.client.MultiplexedReverseConnectListenerConfig;
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
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example demonstrating multiplexed OPC UA Reverse Connect with two server instances sharing a
 * single client-side listener.
 *
 * <p>Two {@link ExampleServer} instances (with distinct {@code ApplicationUri}s) each dial the same
 * {@link MultiplexedReverseConnectListener}. The listener dispatches inbound connections by {@code
 * ServerUri}, performs 2-shot discovery per server, creates a client per server, reads from both,
 * and shuts down.
 *
 * <p>This example uses {@link SecurityPolicy#None} for simplicity.
 */
public class ReverseConnectMultiplexedExample {

  private static final String SERVER_URI_1 = "urn:eclipse:milo:examples:server-1";
  private static final String SERVER_URI_2 = "urn:eclipse:milo:examples:server-2";
  private static final InetSocketAddress LISTEN_ADDRESS = new InetSocketAddress("localhost", 48060);

  private static final Logger logger =
      LoggerFactory.getLogger(ReverseConnectMultiplexedExample.class);

  public static void main(String[] args) throws Exception {
    // 1. Create two servers with distinct ApplicationUris.
    var server1 = new ExampleServer(12686, builder -> {}, SERVER_URI_1);
    var server2 = new ExampleServer(12687, builder -> {}, SERVER_URI_2);

    // 2. Attach a ReverseConnectManager to each, then start both.
    attachReverseConnectManager(server1);
    attachReverseConnectManager(server2);
    server1.startup().get();
    server2.startup().get();

    try {
      // 3. Track clients as the listener creates them.
      Map<String, CompletableFuture<OpcUaClient>> clientFutures = new ConcurrentHashMap<>();
      clientFutures.put(SERVER_URI_1, new CompletableFuture<>());
      clientFutures.put(SERVER_URI_2, new CompletableFuture<>());

      // 4. Create and start the multiplexed listener.
      var listenerConfig =
          MultiplexedReverseConnectListenerConfig.newBuilder()
              .setListenAddress(LISTEN_ADDRESS)
              .setTransportConfig(
                  OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder().build())
              .setEndpointResolver(
                  EndpointResolver.discover(
                      (serverUri, endpoints) ->
                          endpoints.stream()
                              .filter(
                                  e ->
                                      SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                              .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                              .filter(
                                  e ->
                                      Stack.TCP_UASC_UABINARY_TRANSPORT_URI.equals(
                                          e.getTransportProfileUri()))
                              .findFirst()
                              .orElseThrow()))
              .setClientCustomizer(
                  (serverUri, builder) ->
                      builder
                          .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                          .setApplicationUri("urn:eclipse:milo:examples:client"))
              .setClientListener(
                  client -> {
                    String uri = client.getConfig().getEndpoint().getServer().getApplicationUri();
                    client
                        .connectAsync()
                        .whenComplete(
                            (c, ex) -> {
                              var f = clientFutures.get(uri);
                              if (f != null) {
                                if (ex != null) {
                                  f.completeExceptionally(ex);
                                } else {
                                  f.complete(client);
                                }
                              }
                            });
                  })
              .build();

      var listener = new MultiplexedReverseConnectListener(listenerConfig);
      listener.start();

      try {
        // 5. Tell each server to dial the listener.
        String listenerUrl = "opc.tcp://localhost:" + LISTEN_ADDRESS.getPort();
        ReverseConnectHandle handle1 = null;
        ReverseConnectHandle handle2 = null;
        OpcUaClient client1 = null;
        OpcUaClient client2 = null;

        try {
          handle1 = server1.getServer().addReverseConnect(listenerUrl);
          handle2 = server2.getServer().addReverseConnect(listenerUrl);

          // 6. Wait for both clients.
          client1 = clientFutures.get(SERVER_URI_1).get(60, TimeUnit.SECONDS);
          client2 = clientFutures.get(SERVER_URI_2).get(60, TimeUnit.SECONDS);

          // 7. Print identities and read from both.
          for (OpcUaClient client : List.of(client1, client2)) {
            var appDesc = client.getConfig().getEndpoint().getServer();
            logger.info("ApplicationUri: {}", appDesc.getApplicationUri());
            logger.info("ApplicationName: {}", appDesc.getApplicationName().getText());
            readServerStatus(client);
          }
        } finally {
          if (handle1 != null) {
            server1.getServer().removeReverseConnect(handle1);
          }
          if (handle2 != null) {
            server2.getServer().removeReverseConnect(handle2);
          }
          if (client1 != null) {
            client1.disconnectAsync().get(5, TimeUnit.SECONDS);
          }
          if (client2 != null) {
            client2.disconnectAsync().get(5, TimeUnit.SECONDS);
          }
        }
      } finally {
        listener.stop().get(5, TimeUnit.SECONDS);
      }
    } finally {
      Thread.sleep(1000);
      server1.shutdown().get();
      server2.shutdown().get();
      Stack.releaseSharedResources();
    }
  }

  private static void attachReverseConnectManager(ExampleServer server) {
    var rcConfig = ReverseConnectConfig.newBuilder().build();
    var transportConfig = OpcTcpServerTransportConfig.newBuilder().build();
    var manager = new ReverseConnectManager(rcConfig, transportConfig);
    server.getServer().setReverseConnectManager(manager);
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
