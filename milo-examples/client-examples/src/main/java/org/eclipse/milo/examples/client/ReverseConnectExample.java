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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.server.ExampleServer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.reverse.DiscoveryFirstReverseConnectClient;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates discovery-first Reverse Connect with Milo on both sides.
 *
 * <p>The client starts only a {@link ReverseConnectManager}; it does not begin with an {@link
 * EndpointDescription}. {@link DiscoveryFirstReverseConnectClient} consumes the first reverse
 * connection for {@code GetEndpoints}, selects a no-security endpoint, and then creates the
 * production {@link OpcUaClient}. The production client waits for the server's later matching
 * reverse connection before opening a normal Session.
 */
public class ReverseConnectExample {

  private static final InetSocketAddress LISTENER_ADDRESS =
      new InetSocketAddress("localhost", 48060);

  private static final Logger logger = LoggerFactory.getLogger(ReverseConnectExample.class);

  public static void main(String[] args) throws Exception {
    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(LISTENER_ADDRESS)
            .setPendingConnectionHoldTime(Duration.ofSeconds(30))
            .build();

    ExampleServer exampleServer = new ExampleServer();

    OpcUaClient client = null;
    ReverseConnectTargetHandle targetHandle = null;

    try {
      manager.startup();
      exampleServer.startup().get();

      CompletableFuture<OpcUaClient> connectedClient =
          DiscoveryFirstReverseConnectClient.builder(manager)
              .setClientConfig(
                  (discovery, endpoint) -> clientConfig(endpoint, discovery.endpoints()))
              .connectAsync();

      targetHandle =
          exampleServer
              .getServer()
              .addReverseConnectTarget(
                  reverseTarget(manager, selectAdvertisedEndpointUrl(exampleServer)));

      client = connectedClient.get(30, TimeUnit.SECONDS);

      logger.info("Connected to {}", client.getConfig().getEndpoint().getEndpointUrl());
      readServerStatus(client);
    } finally {
      if (client != null) {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
      if (targetHandle != null) {
        targetHandle.remove().get(5, TimeUnit.SECONDS);
      }

      manager.shutdown();
      exampleServer.shutdown().get();
      Stack.releaseSharedResources();
    }
  }

  private static ReverseConnectTarget reverseTarget(
      ReverseConnectManager manager, String endpointUrl) {

    return ReverseConnectTarget.builder()
        .setClientListenerUrl(listenerUrl(manager))
        .setEndpointUrl(endpointUrl)
        .setRegistrationPeriod(uint(1_000))
        .setConnectTimeout(uint(5_000))
        .build();
  }

  private static OpcUaClientConfig clientConfig(
      EndpointDescription endpoint, List<EndpointDescription> discoveryEndpoints) {

    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint)
        .setDiscoveryEndpoints(discoveryEndpoints)
        .setApplicationName(LocalizedText.english("eclipse milo reverse-connect example client"))
        .setApplicationUri("urn:eclipse:milo:examples:client:reverse-connect")
        .setRequestTimeout(uint(10_000))
        .build();
  }

  private static String selectAdvertisedEndpointUrl(ExampleServer exampleServer) {
    return exampleServer.getServer().getApplicationContext().getEndpointDescriptions().stream()
        .filter(endpoint -> !endpointUrl(endpoint).endsWith("/discovery"))
        .filter(endpoint -> SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri()))
        .filter(endpoint -> endpoint.getSecurityMode() == MessageSecurityMode.None)
        .map(ReverseConnectExample::endpointUrl)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("no None/None endpoint found"));
  }

  private static void readServerStatus(OpcUaClient client) throws Exception {
    List<NodeId> nodeIds =
        List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime);

    List<DataValue> values =
        client.readValuesAsync(0.0, TimestampsToReturn.Both, nodeIds).get(5, TimeUnit.SECONDS);

    Object state = requireNonNull(values.get(0).value().value());
    logger.info("State={}", ServerState.from((Integer) state));
    logger.info("CurrentTime={}", values.get(1).value().value());
  }

  private static String listenerUrl(ReverseConnectManager manager) {
    SocketAddress boundAddress = manager.snapshot().listeners().get(0).boundAddress();
    if (boundAddress instanceof InetSocketAddress address) {
      return "opc.tcp://" + address.getHostString() + ":" + address.getPort();
    }
    throw new IllegalStateException("listener is not bound to an InetSocketAddress");
  }

  private static String endpointUrl(EndpointDescription endpoint) {
    return requireNonNull(endpoint.getEndpointUrl(), "endpointUrl");
  }
}
