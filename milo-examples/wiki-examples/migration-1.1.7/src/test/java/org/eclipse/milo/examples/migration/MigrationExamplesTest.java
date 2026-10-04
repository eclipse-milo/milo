/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(45)
class MigrationExamplesTest {
  @AfterAll
  static void releaseResources() {
    Stack.releaseSharedResources();
  }

  // Each release-note fragment participates in a real trusted secured Session and model operation.
  @Test
  void migrationFragmentsEstablishTrustExposeFolderAndCleanUp() throws Exception {
    String applicationUri = "urn:eclipse:milo:wiki:migration:client";
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate[] chain = {certificate(keyPair, applicationUri)};
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(chain[0]);
    var store = new MemoryCertificateStore();
    var quarantine = new MemoryCertificateQuarantine();
    var validator = new DefaultServerCertificateValidator(trust, quarantine);
    var factory =
        new RsaSha256CertificateFactory() {
          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair pair)
              throws Exception {
            return new X509Certificate[] {
              certificate(pair, "urn:eclipse:milo:wiki:migration:server")
            };
          }
        };
    // snippet:certificate-group:start
    var group = DefaultApplicationGroup.createAndInitialize(trust, store, factory, validator);
    var manager = new DefaultCertificateManager(quarantine, group);
    // snippet:certificate-group:end
    X509Certificate serverCertificate =
        group.getCertificateChain(NodeIds.RsaSha256ApplicationCertificateType).orElseThrow()[0];
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    var endpointConfig =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("127.0.0.1")
            .setBindPort(port)
            .setPath("/migration")
            .setCertificate(serverCertificate)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();
    var serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:wiki:migration:server")
            .setCertificateManager(manager)
            .setEndpoints(Set.of(endpointConfig))
            .build();
    var server =
        new OpcUaServer(
            serverConfig,
            profile -> new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));
    // This fixture has no data MonitoredItems; 1.1.7 requires explicit no-op callbacks.
    var namespace =
        new ManagedNamespaceWithLifecycle(server, "urn:eclipse:milo:wiki:migration") {
          @Override
          public void onDataItemsCreated(List<DataItem> items) {}

          @Override
          public void onDataItemsModified(List<DataItem> items) {}

          @Override
          public void onDataItemsDeleted(List<DataItem> items) {}

          @Override
          public void onMonitoringModeChanged(List<MonitoredItem> items) {}
        };
    try {
      namespace.startup();
      server.startup().get(10, TimeUnit.SECONDS);
      NodeId instanceId = new NodeId(namespace.getNamespaceIndex(), "Folder");
      QualifiedName browseName = new QualifiedName(namespace.getNamespaceIndex(), "Folder");
      UaObjectNode node =
          createFolder(server, namespace, NodeIds.ObjectsFolder, instanceId, browseName);
      var clientTrust = new MemoryTrustListManager();
      clientTrust.addTrustedCertificate(serverCertificate);
      var clientValidator =
          new DefaultClientCertificateValidator(clientTrust, new MemoryCertificateQuarantine());
      EndpointDescription endpoint =
          DiscoveryClient.getEndpoints(endpointConfig.getEndpointUrl())
              .get(10, TimeUnit.SECONDS)
              .stream()
              .filter(e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
              .findFirst()
              .orElseThrow();
      OpcUaClient client =
          OpcUaClient.create(
              clientConfig(endpoint, keyPair, chain, clientValidator, applicationUri));
      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        assertTrue(
            client
                .readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State)
                .getStatusCode()
                .isGood());
        assertTrue(
            client.getAddressSpace().browseNodes(NodeIds.ObjectsFolder).stream()
                .anyMatch(n -> instanceId.equals(n.getNodeId())));
        assertEquals(browseName, client.getAddressSpace().getNode(instanceId).readBrowseName());
        node.delete();
        assertEquals(
            StatusCodes.Bad_NodeIdUnknown,
            client
                .readValue(0.0, TimestampsToReturn.Neither, instanceId)
                .getStatusCode()
                .getValue());
      } finally {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
        clientTrust.close();
      }
      var rejecting =
          new DefaultClientCertificateValidator(
              new MemoryTrustListManager(), new MemoryCertificateQuarantine());
      UaException rejected =
          assertThrows(
              UaException.class,
              () ->
                  rejecting.validateCertificateChain(
                      List.of(serverCertificate),
                      "urn:eclipse:milo:wiki:migration:server",
                      new String[] {"127.0.0.1"}));
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, rejected.getStatusCode().getValue());
      assertInstanceOf(java.security.InvalidAlgorithmParameterException.class, rejected.getCause());
    } finally {
      try {
        namespace.shutdown();
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
        trust.close();
      }
    }
  }

  static OpcUaClientConfig clientConfig(
      EndpointDescription endpoint,
      KeyPair keyPair,
      X509Certificate[] chain,
      CertificateValidator validator,
      String applicationUri) {
    // snippet:client-identity:start
    var config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationUri(applicationUri)
            .setKeyPair(keyPair)
            .setCertificate(chain[0])
            .setCertificateChain(chain)
            .setCertificateValidator(validator)
            .build();
    // snippet:client-identity:end
    return config;
  }

  static UaObjectNode createFolder(
      OpcUaServer server,
      ManagedAddressSpace space,
      NodeId parentId,
      NodeId instanceId,
      QualifiedName browseName)
      throws Exception {
    // snippet:node-factory:start
    UaObjectNode node =
        (UaObjectNode) space.getNodeFactory().createNode(instanceId, NodeIds.FolderType);
    node.setBrowseName(browseName);
    node.setDisplayName(LocalizedText.english("Folder"));
    space.getNodeManager().addNode(node);
    node.addReference(new Reference(instanceId, NodeIds.Organizes, parentId.expanded(), false));
    // snippet:node-factory:end
    return node;
  }

  private static X509Certificate certificate(KeyPair pair, String uri) throws Exception {
    return new SelfSignedCertificateBuilder(pair)
        .setCommonName("Wiki migration fixture")
        .setApplicationUri(uri)
        .addDnsName("localhost")
        .addIpAddress("127.0.0.1")
        .build();
  }
}
