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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.server.ExampleServer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectAcceptor;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates discovery-first Reverse Connect for multiple servers on one client listener.
 *
 * <p>The application does not preconfigure endpoint descriptions for either server. It identifies
 * each first inbound connection by {@code ServerUri}. {@link ReverseConnectAcceptor} claims one
 * connection per server for {@code GetEndpoints}, builds a production {@link OpcUaClient} from the
 * discovered endpoint, and then waits for each server's later matching reverse connection. The
 * shared {@link ReverseConnectManager} keeps the two server flows separate.
 */
public class ReverseConnectSharedListenerExample {

  private static final String SERVER_URI_1 = "urn:eclipse:milo:examples:server:reverse:1";
  private static final String SERVER_URI_2 = "urn:eclipse:milo:examples:server:reverse:2";

  private static final InetSocketAddress LISTENER_ADDRESS =
      new InetSocketAddress("localhost", 48060);

  private static final Logger logger =
      LoggerFactory.getLogger(ReverseConnectSharedListenerExample.class);

  public static void main(String[] args) throws Exception {
    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(LISTENER_ADDRESS)
            .setPendingConnectionHoldTime(Duration.ofSeconds(30))
            .build();

    ExampleServer server1 = new ExampleServer(12686, builder -> {}, SERVER_URI_1);
    ExampleServer server2 = new ExampleServer(12687, builder -> {}, SERVER_URI_2);

    ConcurrentMap<String, CompletableFuture<OpcUaClient>> clientFutures = new ConcurrentHashMap<>();
    clientFutures.put(SERVER_URI_1, new CompletableFuture<>());
    clientFutures.put(SERVER_URI_2, new CompletableFuture<>());

    ConcurrentMap<String, OpcUaClient> clients = new ConcurrentHashMap<>();

    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setDiscoverySelector(
                candidate ->
                    candidate.serverUri() != null
                        && clientFutures.containsKey(candidate.serverUri()))
            .setClientConfig((discovery, endpoint) -> clientConfig(endpoint, discovery.endpoints()))
            .setClientListener(
                (discovery, endpoint, client) -> {
                  String serverUri = requireNonNull(discovery.serverUri(), "serverUri");
                  clients.put(serverUri, client);
                  requireNonNull(clientFutures.get(serverUri), "clientFuture").complete(client);
                })
            .setErrorListener(
                (candidate, failure) -> {
                  String serverUri = candidate.serverUri();
                  if (serverUri != null) {
                    CompletableFuture<OpcUaClient> clientFuture = clientFutures.get(serverUri);
                    if (clientFuture != null) {
                      clientFuture.completeExceptionally(failure);
                    }
                  }
                  logger.warn("Reverse Connect acceptor failed for {}", candidate, failure);
                })
            .build();

    ReverseConnectTargetHandle target1 = null;
    ReverseConnectTargetHandle target2 = null;

    try {
      manager.startup();
      acceptor.start();
      server1.startup().get();
      server2.startup().get();

      target1 =
          server1
              .getServer()
              .addReverseConnectTarget(
                  reverseTarget(manager, selectAdvertisedEndpointUrl(server1)));
      target2 =
          server2
              .getServer()
              .addReverseConnectTarget(
                  reverseTarget(manager, selectAdvertisedEndpointUrl(server2)));

      OpcUaClient client1 = clientFutures.get(SERVER_URI_1).get(30, TimeUnit.SECONDS);
      OpcUaClient client2 = clientFutures.get(SERVER_URI_2).get(30, TimeUnit.SECONDS);

      readServerStatus(client1);
      readServerStatus(client2);
    } finally {
      acceptor.close();
      for (OpcUaClient client : clients.values()) {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
      if (target1 != null) {
        target1.remove().get(5, TimeUnit.SECONDS);
      }
      if (target2 != null) {
        target2.remove().get(5, TimeUnit.SECONDS);
      }

      manager.shutdown();
      server1.shutdown().get();
      server2.shutdown().get();
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

    String serverUri = serverUri(endpoint);

    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint)
        .setDiscoveryEndpoints(discoveryEndpoints)
        .setApplicationName(LocalizedText.english("eclipse milo reverse client for " + serverUri))
        .setApplicationUri("urn:eclipse:milo:examples:client:reverse-connect:" + serverUri)
        .setRequestTimeout(uint(10_000))
        .build();
  }

  private static String selectAdvertisedEndpointUrl(ExampleServer exampleServer) {
    return exampleServer.getServer().getApplicationContext().getEndpointDescriptions().stream()
        .filter(endpoint -> !endpointUrl(endpoint).endsWith("/discovery"))
        .filter(endpoint -> SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri()))
        .filter(endpoint -> endpoint.getSecurityMode() == MessageSecurityMode.None)
        .map(ReverseConnectSharedListenerExample::endpointUrl)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("no None/None endpoint found"));
  }

  private static void readServerStatus(OpcUaClient client) throws Exception {
    List<DataValue> values =
        client
            .readValuesAsync(
                0.0,
                TimestampsToReturn.Both,
                List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime))
            .get(5, TimeUnit.SECONDS);

    Object state = requireNonNull(values.get(0).value().value());
    String serverUri = serverUri(client.getConfig().getEndpoint());

    logger.info("{} State={}", serverUri, ServerState.from((Integer) state));
    logger.info("{} CurrentTime={}", serverUri, values.get(1).value().value());
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

  private static String serverUri(EndpointDescription endpoint) {
    return requireNonNull(
        requireNonNull(endpoint.getServer(), "server").getApplicationUri(), "serverUri");
  }
}
