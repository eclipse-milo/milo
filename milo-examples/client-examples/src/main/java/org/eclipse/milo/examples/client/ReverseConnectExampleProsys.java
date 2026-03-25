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
import java.security.Security;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example demonstrating OPC UA Reverse Connect with the Prosys OPC UA Simulation Server.
 *
 * <p>In Reverse Connect, the client listens for inbound connections from the server. The Prosys
 * Simulation Server must be configured to initiate reverse connections to this client's listen
 * address (default: opc.tcp://localhost:48060).
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
 */
public class ReverseConnectExampleProsys {

  /**
   * The address this client listens on for inbound server connections. Configure the Prosys server
   * to reverse-connect to this address.
   */
  private static final InetSocketAddress LISTEN_ADDRESS = new InetSocketAddress("localhost", 48060);

  private static final Logger logger = LoggerFactory.getLogger(ReverseConnectExampleProsys.class);

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static void main(String[] args) throws Exception {
    OpcUaClient client = createClient();

    try {
      logger.info("Waiting for Prosys server to reverse-connect to {}...", LISTEN_ADDRESS);

      client.connectAsync().get(30, TimeUnit.SECONDS);

      logger.info("Connected via Reverse Connect.");

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
    } finally {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      Stack.releaseSharedResources();
    }
  }

  private static OpcUaClient createClient() throws Exception {
    var transportConfig =
        OpcTcpReverseConnectTransportConfig.newBuilder().setListenAddress(LISTEN_ADDRESS).build();

    return OpcUaClient.createReverseConnect(
            transportConfig,
            endpoints ->
                endpoints.stream()
                    .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                    .findFirst(),
            clientConfig ->
                clientConfig
                    .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                    .setApplicationUri("urn:eclipse:milo:examples:client"))
        .get(60, TimeUnit.SECONDS);
  }
}
