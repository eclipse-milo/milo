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

import java.net.InetSocketAddress;
import java.security.Security;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.examples.client.ClientExampleRunner;
import org.eclipse.milo.examples.client.KeyStoreLoader;
import org.eclipse.milo.opcua.sdk.client.ClientListener;
import org.eclipse.milo.opcua.sdk.client.MultiplexedReverseConnectListener;
import org.eclipse.milo.opcua.sdk.client.MultiplexedReverseConnectListenerConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example demonstrating <b>multiplexed</b> OPC UA Reverse Connect with the Prosys OPC UA Simulation
 * Server.
 *
 * <p>Unlike the 1-1 {@link ProsysReverseConnectExample}, this example uses a {@link
 * MultiplexedReverseConnectListener} — a shared listener that can accept inbound connections from
 * multiple servers on a single port and create clients on demand.
 *
 * <p>When an unknown server connects, the listener performs 2-shot endpoint discovery (consumes the
 * first connection for {@code GetEndpoints}, then waits for the server to reconnect for the actual
 * session). The listener builds an {@link OpcUaClient} automatically using the resolved endpoint
 * and the configured {@link org.eclipse.milo.opcua.sdk.client.ClientCustomizer}, then notifies the
 * application via the {@link ClientListener} callback.
 *
 * <p>Setup steps for the Prosys OPC UA Simulation Server:
 *
 * <ol>
 *   <li>Open the Prosys OPC UA Simulation Server.
 *   <li>Navigate to the Endpoints tab, Reverse Connections section.
 *   <li>Add a new reverse connection with Client URL: opc.tcp://localhost:48060
 *   <li>Restart the server.
 *   <li>Run this example — it will wait for the server to connect.
 * </ol>
 *
 * <p>This example uses {@link SecurityPolicy#None} for simplicity. For secured connections, see
 * {@link KeyStoreLoader} and the certificate setup in {@link ClientExampleRunner}.
 *
 * @see ProsysReverseConnectExample for the simpler 1-1 reverse connect approach
 */
public class ProsysReverseConnectMultiplexedExample {

  /**
   * The address this client listens on for inbound server connections. Configure the Prosys server
   * to reverse-connect to this address.
   */
  private static final InetSocketAddress LISTEN_ADDRESS = new InetSocketAddress("localhost", 48060);

  private static final Logger logger =
      LoggerFactory.getLogger(ProsysReverseConnectMultiplexedExample.class);

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static void main(String[] args) throws Exception {
    // Completed by the ClientListener callback when a client is connected.
    var clientFuture = new CompletableFuture<OpcUaClient>();

    var transportConfig = OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder().build();

    var listenerConfig =
        MultiplexedReverseConnectListenerConfig.newBuilder()
            .setListenAddress(LISTEN_ADDRESS)
            .setTransportConfig(transportConfig)
            .setEndpointResolver(
                EndpointResolver.discover(
                    (serverUri, endpoints) ->
                        endpoints.stream()
                            .filter(
                                e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                            .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                            .filter(
                                e ->
                                    Stack.TCP_UASC_UABINARY_TRANSPORT_URI.equals(
                                        e.getTransportProfileUri()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalStateException("No None/None endpoint found"))))
            .setClientCustomizer(
                (serverUri, builder) ->
                    builder
                        .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                        .setApplicationUri("urn:eclipse:milo:examples:client"))
            .setClientListener(
                client ->
                    client
                        .connectAsync()
                        .whenComplete(
                            (c, ex) -> {
                              if (ex != null) {
                                clientFuture.completeExceptionally(ex);
                              } else {
                                clientFuture.complete(c);
                              }
                            }))
            .build();

    var listener = new MultiplexedReverseConnectListener(listenerConfig);
    listener.start();

    try {
      logger.info(
          "Waiting for server to reverse-connect to {}... "
              + "(first connection discovers endpoints, second establishes session)",
          LISTEN_ADDRESS);

      OpcUaClient client = clientFuture.get(60, TimeUnit.SECONDS);

      logger.info("Connected via Multiplexed Reverse Connect.");

      // Read server state and current time
      List<NodeId> nodeIds =
          List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime);

      List<DataValue> values =
          client.readValuesAsync(0.0, TimestampsToReturn.Both, nodeIds).get(5, TimeUnit.SECONDS);

      DataValue stateValue = values.get(0);
      DataValue timeValue = values.get(1);

      logger.info(
          "State={}", ServerState.from((Integer) requireNonNull(stateValue.value().value())));
      logger.info("CurrentTime={}", timeValue.value().value());

      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } finally {
      listener.stop().get(5, TimeUnit.SECONDS);
      Stack.releaseSharedResources();
    }
  }
}
