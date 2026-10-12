/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client.prosys;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.reverse.DiscoveryFirstReverseConnectClient;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectEndpointSelector;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectEndpointSelectors;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates discovery-first OPC UA Reverse Connect with the Prosys OPC UA Simulation Server.
 *
 * <p>Configure Prosys Simulation Server to dial {@code opc.tcp://localhost:48060}: open the server
 * settings for reverse connections, add that client URL, then restart or trigger the server so it
 * opens the first reverse socket. This example uses {@link DiscoveryFirstReverseConnectClient} to
 * consume the first reverse connection for {@code GetEndpoints}, select a no-security anonymous
 * endpoint while preferring the URL from {@code ReverseHello}, then wait for a later matching
 * reverse connection to establish the production Session.
 *
 * <p>The selected endpoint still comes from the {@code GetEndpoints} response. The endpoint URL in
 * {@code ReverseHello} is only a routing hint, so the selector below prefers that URL but still
 * requires an endpoint compatible with this example's no-security anonymous client configuration.
 *
 * <p>The first connection is deliberately not reused for the Session. After endpoint discovery, the
 * production {@link OpcUaClient} registers a selector with the same {@link ReverseConnectManager}
 * and waits for Prosys to open another matching reverse connection.
 */
public class ProsysReverseConnectExample {

  private static final InetSocketAddress LISTENER_ADDRESS =
      new InetSocketAddress("localhost", 48060);

  private static final Logger logger = LoggerFactory.getLogger(ProsysReverseConnectExample.class);

  public static void main(String[] args) throws Exception {
    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(LISTENER_ADDRESS)
            .setPendingConnectionHoldTime(Duration.ofSeconds(60))
            .build();

    OpcUaClient client = null;
    try {
      manager.startup();

      CompletableFuture<OpcUaClient> connectedClient =
          DiscoveryFirstReverseConnectClient.builder(manager)
              // Make the endpoint-selection policy visible for readers of the example.
              .setEndpointSelector(noSecurityAnonymousEndpointSelector())
              .setClientConfig(
                  (discovery, endpoint) -> clientConfig(endpoint, discovery.endpoints()))
              .connectAsync();

      logger.info("Waiting for Prosys to reverse-connect to {}...", LISTENER_ADDRESS);

      client = connectedClient.get(120, TimeUnit.SECONDS);

      logger.info("Connected via Reverse Connect.");
      readServerStatus(client);
    } finally {
      if (client != null) {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
      manager.shutdown();
      Stack.releaseSharedResources();
    }
  }

  private static OpcUaClientConfig clientConfig(
      EndpointDescription endpoint, List<EndpointDescription> discoveryEndpoints) {

    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint)
        .setDiscoveryEndpoints(discoveryEndpoints)
        .setApplicationName(LocalizedText.english("eclipse milo prosys reverse-connect client"))
        .setApplicationUri("urn:eclipse:milo:examples:client:prosys:reverse-connect")
        .setRequestTimeout(uint(10_000))
        .build();
  }

  /**
   * Select the production endpoint after the temporary discovery connection returns the server's
   * endpoint descriptions.
   *
   * <p>The selector first tries to find a discovered endpoint whose URL matches the URL sent in the
   * discovery connection's {@code ReverseHello}. That URL is useful for routing, but it is not
   * trusted as proof of identity, so the discovered endpoint must still be no-security and allow
   * anonymous activation. If the URL does not match any compatible endpoint, the selector falls
   * back to the first discovered endpoint with that same no-security anonymous shape.
   *
   * @return the endpoint selector used by this example.
   */
  private static ReverseConnectEndpointSelector noSecurityAnonymousEndpointSelector() {
    return ReverseConnectEndpointSelectors.preferReverseHelloEndpointUrl(
        ReverseConnectEndpointSelectors::isNoSecurityAndAnonymous);
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
}
