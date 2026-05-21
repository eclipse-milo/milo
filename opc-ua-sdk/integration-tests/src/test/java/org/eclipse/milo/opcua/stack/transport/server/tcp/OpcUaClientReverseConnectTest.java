/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectCandidateSnapshot;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectCandidateState;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OpcUaClientReverseConnectTest {

  private TestServer testServer;
  private OpcUaServer server;
  private TestNamespace testNamespace;
  private ReverseConnectManager manager;
  private OpcTcpServerReverseConnector connector;
  private OpcUaClient client;

  @AfterEach
  void tearDown() throws Exception {
    if (client != null) {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    }
    if (connector != null) {
      connector.close();
    }
    if (manager != null) {
      manager.shutdown();
    }
    if (testNamespace != null) {
      testNamespace.shutdown();
    }
    if (server != null) {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  @Test
  void connectAsyncOpensSessionOverReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);

    client = newReverseClient(endpoint, false);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
    OpcTcpServerReverseConnectAttempt attempt = startReverseConnect(endpoint);

    assertSame(client, connectFuture.get(10, TimeUnit.SECONDS));
    assertTrue(attempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());

    assertReadWorks();
    assertClaimedReverseHello(endpoint);
  }

  @Test
  void secureConnectAsyncOpensSessionOverReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint =
        selectEndpoint(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    client = newReverseClient(endpoint, true);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
    OpcTcpServerReverseConnectAttempt attempt = startReverseConnect(endpoint);

    assertSame(client, connectFuture.get(10, TimeUnit.SECONDS));
    assertTrue(attempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());

    assertReadWorks();
    assertClaimedReverseHello(endpoint);
  }

  @Test
  void connectAsyncRearmsAfterFailedClaimedReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);

    client =
        newReverseClient(endpoint, false, transport -> transport.setAcknowledgeTimeout(uint(250)));

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    sendReverseHelloAndCloseAfterHello(endpoint);

    OpcTcpServerReverseConnectAttempt attempt = startReverseConnect(endpoint);

    assertSame(client, connectFuture.get(10, TimeUnit.SECONDS));
    assertTrue(attempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());

    assertReadWorks();
    assertClaimedReverseHello(endpoint);
  }

  private void startServerAndManager() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    testNamespace = new TestNamespace(server);
    testNamespace.startup();

    server.startup().get(5, TimeUnit.SECONDS);

    manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("localhost", 0))
            .setFirstMessageTimeout(Duration.ofSeconds(5))
            .setPendingConnectionHoldTime(Duration.ofSeconds(5))
            .build();
    manager.startup();

    connector = new OpcTcpServerReverseConnector(OpcTcpServerTransportConfig.newBuilder().build());
  }

  private OpcUaClient newReverseClient(EndpointDescription endpoint, boolean secure)
      throws Exception {

    return newReverseClient(endpoint, secure, transport -> {});
  }

  private OpcUaClient newReverseClient(
      EndpointDescription endpoint,
      boolean secure,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport)
      throws Exception {

    OpcUaClientConfigBuilder builder =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setApplicationName(LocalizedText.english("eclipse milo reverse test client"))
            .setApplicationUri(clientApplicationUri(secure))
            .setRequestTimeout(uint(5_000));

    if (secure) {
      builder
          .setKeyPair(testServer.getClientKeyPair())
          .setCertificate(testServer.getClientCertificate())
          .setCertificateChain(testServer.getClientCertificateChain())
          .setCertificateValidator(new CertificateValidator.InsecureCertificateValidator());
    }

    OpcUaClientConfig clientConfig = builder.build();

    return OpcUaClient.createReverseConnect(
        clientConfig,
        manager,
        ReverseConnectSelector.byServerUriAndEndpointUrl(
            endpoint.getServer().getApplicationUri(), endpoint.getEndpointUrl()),
        configureTransport);
  }

  private String clientApplicationUri(boolean secure) {
    return secure
        ? "urn:eclipse:milo:test:reverse:client"
        : "urn:eclipse:milo:test:reverse:client:anonymous";
  }

  private void sendReverseHelloAndCloseAfterHello(EndpointDescription endpoint) throws Exception {
    try (Socket socket = new Socket()) {
      socket.setSoTimeout(5_000);
      socket.connect(listenerAddress());

      ByteBuf buffer =
          TcpMessageEncoder.encode(
              new ReverseHelloMessage(
                  endpoint.getServer().getApplicationUri(), endpoint.getEndpointUrl()));
      try {
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();
      } finally {
        buffer.release();
      }

      byte[] header = socket.getInputStream().readNBytes(8);
      assertEquals(8, header.length);
    }
  }

  private OpcTcpServerReverseConnectAttempt startReverseConnect(EndpointDescription endpoint) {
    OpcTcpServerReverseConnectParameters parameters =
        OpcTcpServerReverseConnectParameters.fromAddress(
            server.getApplicationContext(),
            listenerAddress(),
            endpoint.getEndpointUrl(),
            endpoint.getServer().getApplicationUri(),
            5_000,
            OpcTcpServerReverseConnectAttemptObserver.noop());

    return connector.connect(parameters);
  }

  private InetSocketAddress listenerAddress() {
    SocketAddress boundAddress = manager.snapshot().listeners().get(0).boundAddress();

    assertTrue(boundAddress instanceof InetSocketAddress);

    return (InetSocketAddress) boundAddress;
  }

  private EndpointDescription selectEndpoint(SecurityPolicy policy, MessageSecurityMode mode) {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(endpoint -> !endpoint.getEndpointUrl().endsWith("/discovery"))
        .filter(endpoint -> policy.getUri().equals(endpoint.getSecurityPolicyUri()))
        .filter(endpoint -> mode == endpoint.getSecurityMode())
        .findFirst()
        .orElseThrow();
  }

  private void assertReadWorks() throws Exception {
    DataValue value =
        client.readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);

    assertTrue(value.statusCode().isGood(), () -> "read failed: " + value.statusCode());
  }

  private void assertClaimedReverseHello(EndpointDescription endpoint) {
    List<ReverseConnectCandidateSnapshot> acceptedCandidates =
        manager.snapshot().acceptedCandidates();

    assertFalse(acceptedCandidates.isEmpty());

    ReverseConnectCandidateSnapshot snapshot =
        acceptedCandidates.get(acceptedCandidates.size() - 1);

    assertEquals(ReverseConnectCandidateState.CLAIMED, snapshot.state());
    assertEquals(endpoint.getServer().getApplicationUri(), snapshot.serverUri());
    assertEquals(endpoint.getEndpointUrl(), snapshot.endpointUrl());
  }
}
