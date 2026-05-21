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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectCandidateSnapshot;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectCandidateState;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectConnection;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectListener;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
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
import org.jspecify.annotations.NonNull;
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
  void serverReverseTargetOpensSessionOverReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);

    client = newReverseClient(endpoint, false);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    ReverseConnectTarget target =
        ReverseConnectTarget.builder()
            .setClientListenerUrl(listenerUrl())
            .setEndpointUrl(endpointUrl(endpoint))
            .setRegistrationPeriod(uint(30_000))
            .setConnectTimeout(uint(5_000))
            .build();
    ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

    assertSame(client, connectFuture.get(10, TimeUnit.SECONDS));

    waitUntil(() -> handle.snapshot().orElseThrow().activeChannelCount() > 0);

    assertEquals(
        1,
        server
            .updateReverseConnectTarget(
                ReverseConnectTarget.builder()
                    .setId(target.getId())
                    .setClientListenerUrl(listenerUrl())
                    .setEndpointUrl(endpointUrl(endpoint))
                    .setRegistrationPeriod(uint(60_000))
                    .setConnectTimeout(uint(5_000))
                    .build())
            .get(5, TimeUnit.SECONDS)
            .activeChannelCount());

    assertReadWorks();
    assertClaimedReverseHello(endpoint);
  }

  @Test
  void dynamicClientFactoryClaimsPendingReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<OpcUaClient> connectedClient = new CompletableFuture<>();

    manager.addListener(
        new ReverseConnectListener() {
          @Override
          public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
            try {
              ReverseConnectConnection connection = manager.claim(snapshot.id()).orElseThrow();
              OpcUaClient dynamicClient =
                  OpcUaClient.createReverseConnect(clientConfig(endpoint), connection);

              client = dynamicClient;
              dynamicClient
                  .connectAsync()
                  .whenComplete((c, ex) -> complete(connectedClient, c, ex));
            } catch (Throwable t) {
              connectedClient.completeExceptionally(t);
            }
          }
        });

    server.addReverseConnectTarget(
        ReverseConnectTarget.builder()
            .setClientListenerUrl(listenerUrl())
            .setEndpointUrl(endpointUrl(endpoint))
            .setRegistrationPeriod(uint(30_000))
            .setConnectTimeout(uint(5_000))
            .build());

    client = connectedClient.get(10, TimeUnit.SECONDS);

    assertReadWorks();
    assertClaimedReverseHello(endpoint);
  }

  @Test
  void directReverseConnectionDoesNotRearmAfterFailedHandshake() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<Throwable> failure = new CompletableFuture<>();

    manager.addListener(
        new ReverseConnectListener() {
          @Override
          public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
            if (failure.isDone()) {
              return;
            }

            try {
              ReverseConnectConnection connection = manager.claim(snapshot.id()).orElseThrow();
              OpcUaClient directClient =
                  OpcUaClient.createReverseConnect(
                      clientConfig(endpoint),
                      connection,
                      transport -> transport.setAcknowledgeTimeout(uint(250)));

              client = directClient;
              directClient
                  .connectAsync()
                  .whenComplete(
                      (c, ex) -> {
                        if (ex != null) {
                          failure.complete(ex);
                        }
                      });
            } catch (Throwable t) {
              failure.complete(t);
            }
          }
        });

    sendReverseHelloAndCloseAfterHello(endpoint);

    failure.get(10, TimeUnit.SECONDS);

    OpcTcpServerReverseConnectAttempt attempt = startReverseConnect(endpoint);

    waitUntil(() -> !manager.snapshot().pendingCandidates().isEmpty());
    assertFalse(attempt.channelFuture().isDone());
    assertEquals(1, manager.snapshot().claimedCount());
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

    OpcUaClientConfigBuilder builder = clientConfigBuilder(endpoint, secure);

    OpcUaClientConfig clientConfig = builder.build();

    return OpcUaClient.createReverseConnect(
        clientConfig,
        manager,
        ReverseConnectSelector.byServerUriAndEndpointUrl(
            serverUri(endpoint), endpointUrl(endpoint)),
        configureTransport);
  }

  private OpcUaClientConfig clientConfig(EndpointDescription endpoint) {
    return clientConfigBuilder(endpoint, false).build();
  }

  private OpcUaClientConfigBuilder clientConfigBuilder(
      EndpointDescription endpoint, boolean secure) {
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

    return builder;
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
              new ReverseHelloMessage(serverUri(endpoint), endpointUrl(endpoint)));
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
            endpointUrl(endpoint),
            serverUri(endpoint),
            5_000,
            OpcTcpServerReverseConnectAttemptObserver.noop());

    return connector.connect(parameters);
  }

  private InetSocketAddress listenerAddress() {
    SocketAddress boundAddress = manager.snapshot().listeners().get(0).boundAddress();

    return assertInstanceOf(InetSocketAddress.class, boundAddress);
  }

  private String listenerUrl() {
    InetSocketAddress address = listenerAddress();

    return "opc.tcp://" + address.getHostString() + ":" + address.getPort();
  }

  private EndpointDescription selectEndpoint(SecurityPolicy policy, MessageSecurityMode mode) {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(endpoint -> !endpointUrl(endpoint).endsWith("/discovery"))
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
    assertEquals(serverUri(endpoint), snapshot.serverUri());
    assertEquals(endpointUrl(endpoint), snapshot.endpointUrl());
  }

  private static String endpointUrl(EndpointDescription endpoint) {
    return requireNonNull(endpoint.getEndpointUrl(), "endpointUrl");
  }

  private static String serverUri(EndpointDescription endpoint) {
    return requireNonNull(
        requireNonNull(endpoint.getServer(), "server").getApplicationUri(), "serverUri");
  }

  private static void waitUntil(BooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }

    throw new AssertionError("timed out waiting for condition");
  }

  private static <T> void complete(CompletableFuture<T> future, T value, Throwable ex) {
    if (ex != null) {
      future.completeExceptionally(ex);
    } else {
      future.complete(value);
    }
  }
}
