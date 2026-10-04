/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;

/** A loopback-only server for the first-client tutorial. */
public final class FirstServer {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:wiki";

  private FirstServer() {}

  /**
   * Create a server that owns its tutorial namespace.
   *
   * @param port the loopback TCP port to bind.
   * @return an unstarted server; its caller must shut it down, including after failed startup.
   */
  public static OpcUaServer create(int port) {
    var endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("127.0.0.1")
            .setBindPort(port)
            .setPath("/wiki")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();

    var config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:wiki:server")
            .setApplicationName(LocalizedText.english("Wiki server"))
            .setProductUri("urn:eclipse:milo:wiki")
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(Set.of(endpoint))
            .build();

    var server =
        new OpcUaServer(
            config,
            profile -> new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));
    server.addLifecycleParticipant(new TutorialNamespace(server));
    return server;
  }

  public static void main(String[] args) throws Exception {
    int port = args.length == 0 ? 12686 : Integer.parseInt(args[0]);
    OpcUaServer server = create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      System.out.println(
          "Listening on opc.tcp://127.0.0.1:" + port + "/wiki; press Enter to stop.");
      System.in.read();
    } finally {
      try {
        server.shutdown().get(10, TimeUnit.SECONDS);
      } finally {
        Stack.releaseSharedResources();
      }
    }
  }

  private static final class TutorialNamespace extends ManagedNamespaceWithLifecycle {
    TutorialNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);

      var temperature =
          UaVariableNode.builder(getNodeContext())
              .setNodeId(newNodeId("Temperature"))
              .setBrowseName(newQualifiedName("Temperature"))
              .setDisplayName(LocalizedText.english("Temperature"))
              .setDataType(NodeIds.Double)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .setAccessLevel(AccessLevel.READ_ONLY)
              .setUserAccessLevel(AccessLevel.READ_ONLY)
              .setValue(new DataValue(new Variant(21.5)))
              .build();
      getNodeManager().addNode(temperature);
      temperature.addReference(
          new Reference(
              temperature.getNodeId(), NodeIds.Organizes, NodeIds.ObjectsFolder.expanded(), false));
    }
  }
}
