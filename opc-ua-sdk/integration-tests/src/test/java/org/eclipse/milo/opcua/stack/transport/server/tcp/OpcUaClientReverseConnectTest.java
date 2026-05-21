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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.reverse.DiscoveryFirstReverseConnectClient;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectAcceptor;
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
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
  void sharedListenerRoutesEndpointSpecificClientsWithoutCrossHandoff() throws Exception {
    OpcUaServer secondServer = null;
    TestNamespace secondNamespace = null;
    OpcTcpServerReverseConnector secondConnector = null;
    OpcUaClient secondClient = null;

    try {
      startServerAndManager();

      TestServer secondTestServer = TestServer.create();
      secondServer = secondTestServer.getServer();
      secondNamespace = new TestNamespace(secondServer);
      secondNamespace.startup();
      secondServer.startup().get(5, TimeUnit.SECONDS);

      EndpointDescription firstEndpoint =
          selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
      EndpointDescription secondEndpoint =
          selectEndpoint(secondServer, SecurityPolicy.None, MessageSecurityMode.None);

      assertNotEquals(endpointUrl(firstEndpoint), endpointUrl(secondEndpoint));

      client =
          OpcUaClient.createReverseConnect(
              clientConfig(firstEndpoint),
              manager,
              ReverseConnectSelector.byEndpointUrl(endpointUrl(firstEndpoint)));

      secondClient =
          OpcUaClient.createReverseConnect(
              clientConfigBuilder(secondEndpoint, false)
                  .setApplicationUri("urn:eclipse:milo:test:reverse:client:anonymous:second")
                  .build(),
              manager,
              ReverseConnectSelector.byEndpointUrl(endpointUrl(secondEndpoint)));

      CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
      CompletableFuture<OpcUaClient> secondConnect = secondClient.connectAsync();

      secondConnector =
          new OpcTcpServerReverseConnector(OpcTcpServerTransportConfig.newBuilder().build());

      OpcTcpServerReverseConnectAttempt secondAttempt =
          startReverseConnect(secondConnector, secondServer, listenerAddress(), secondEndpoint);
      OpcTcpServerReverseConnectAttempt firstAttempt = startReverseConnect(firstEndpoint);

      assertSame(client, firstConnect.get(10, TimeUnit.SECONDS));
      assertSame(secondClient, secondConnect.get(10, TimeUnit.SECONDS));

      assertTrue(firstAttempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());
      assertTrue(secondAttempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());

      assertReadWorks(client);
      assertReadWorks(secondClient);
      assertBrowseWorks(client);
      assertBrowseWorks(secondClient);
      assertClaimedReverseHello(firstEndpoint);
      assertClaimedReverseHello(secondEndpoint);
    } finally {
      if (secondClient != null) {
        secondClient.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
      if (secondConnector != null) {
        secondConnector.close();
      }
      if (secondNamespace != null) {
        secondNamespace.shutdown();
      }
      if (secondServer != null) {
        secondServer.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
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
  void discoveryFirstReverseConnectClientConnectsOnLaterReverseConnection() throws Exception {
    startServerAndManager();

    EndpointDescription advertisedEndpoint =
        selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<OpcUaClient> connectedClient =
        DiscoveryFirstReverseConnectClient.builder(manager).connectAsync();

    ReverseConnectTargetHandle handle =
        server.addReverseConnectTarget(reverseTarget(advertisedEndpoint, uint(100)));

    handle.resume().get(5, TimeUnit.SECONDS);

    client = connectedClient.get(10, TimeUnit.SECONDS);

    assertReadWorks();
    assertFalse(client.getConfig().getDiscoveryEndpoints().isEmpty());
    assertClaimedReverseHello(advertisedEndpoint);
    waitUntil(() -> manager.snapshot().claimedCount() == 2);
  }

  @Test
  void reverseConnectAcceptorClaimsPendingCandidateWhenStarted() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<OpcUaClient> connectedClient = new CompletableFuture<>();

    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setClientListener(
                (discovery, discoveredEndpoint, connected) -> {
                  client = connected;
                  connectedClient.complete(connected);
                })
            .setErrorListener(
                (candidate, failure) -> connectedClient.completeExceptionally(failure))
            .build();

    try (acceptor) {
      startReverseConnect(endpoint);
      waitUntil(() -> !manager.snapshot().pendingCandidates().isEmpty());

      acceptor.start();
      waitUntil(() -> manager.snapshot().claimedCount() == 1);

      OpcTcpServerReverseConnectAttempt productionAttempt = startReverseConnect(endpoint);

      OpcUaClient connected = connectedClient.get(10, TimeUnit.SECONDS);

      assertSame(client, connected);
      assertTrue(productionAttempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());
      assertReadWorks();
      assertClaimedReverseHello(endpoint);
      waitUntil(() -> manager.snapshot().claimedCount() == 2);
    }
  }

  @Test
  void reverseConnectAcceptorReleasesKeyWhenProductionClientDisconnects() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<OpcUaClient> firstConnected = new CompletableFuture<>();
    CompletableFuture<OpcUaClient> secondConnected = new CompletableFuture<>();
    AtomicInteger connectedCount = new AtomicInteger();

    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setClientListener(
                (discovery, discoveredEndpoint, connected) -> {
                  client = connected;
                  int count = connectedCount.incrementAndGet();
                  if (count == 1) {
                    firstConnected.complete(connected);
                  } else if (count == 2) {
                    secondConnected.complete(connected);
                  }
                })
            .setErrorListener(
                (candidate, failure) -> {
                  firstConnected.completeExceptionally(failure);
                  secondConnected.completeExceptionally(failure);
                })
            .build();

    try (acceptor) {
      acceptor.start();

      startReverseConnect(endpoint);
      waitUntil(() -> manager.snapshot().claimedCount() == 1);

      OpcTcpServerReverseConnectAttempt firstProductionAttempt = startReverseConnect(endpoint);
      OpcUaClient firstClient = firstConnected.get(10, TimeUnit.SECONDS);

      assertTrue(firstProductionAttempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());
      assertReadWorks(firstClient);

      firstClient.disconnectAsync().get(5, TimeUnit.SECONDS);
      if (client == firstClient) {
        client = null;
      }

      startReverseConnect(endpoint);
      waitUntil(() -> manager.snapshot().claimedCount() == 3);

      OpcTcpServerReverseConnectAttempt secondProductionAttempt = startReverseConnect(endpoint);
      OpcUaClient secondClient = secondConnected.get(10, TimeUnit.SECONDS);

      assertSame(client, secondClient);
      assertTrue(secondProductionAttempt.channelFuture().get(5, TimeUnit.SECONDS).isActive());
      assertReadWorks(secondClient);
      assertEquals(2, connectedCount.get());
    }
  }

  @Test
  void reverseConnectAcceptorDisconnectsClientWhenClientListenerThrows() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    RuntimeException listenerFailure = new RuntimeException("listener failed");
    CompletableFuture<OpcUaClient> deliveredClient = new CompletableFuture<>();
    CompletableFuture<Throwable> reportedFailure = new CompletableFuture<>();

    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setClientListener(
                (discovery, discoveredEndpoint, connected) -> {
                  client = connected;
                  deliveredClient.complete(connected);
                  throw listenerFailure;
                })
            .setErrorListener((candidate, failure) -> reportedFailure.complete(failure))
            .build();

    try (acceptor) {
      acceptor.start();

      startReverseConnect(endpoint);
      waitUntil(() -> manager.snapshot().claimedCount() == 1);

      OpcTcpServerReverseConnectAttempt productionAttempt = startReverseConnect(endpoint);
      OpcUaClient connected = deliveredClient.get(10, TimeUnit.SECONDS);
      Channel channel = productionAttempt.channelFuture().get(5, TimeUnit.SECONDS);

      assertSame(listenerFailure, reportedFailure.get(10, TimeUnit.SECONDS));
      waitUntil(() -> !channel.isActive());
      assertFalse(channel.isActive());

      if (client == connected) {
        client = null;
      }
    }
  }

  @Test
  void reverseConnectAcceptorDoesNotDeliverClientAfterStopDuringInFlightFlow() throws Exception {
    startServerAndManager();

    EndpointDescription endpoint = selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<Void> endpointSelectionEntered = new CompletableFuture<>();
    CompletableFuture<Void> releaseEndpointSelection = new CompletableFuture<>();
    CompletableFuture<OpcUaClient> deliveredClient = new CompletableFuture<>();
    CompletableFuture<Throwable> reportedFailure = new CompletableFuture<>();

    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setEndpointSelector(
                (candidate, endpoints) -> {
                  endpointSelectionEntered.complete(null);
                  releaseEndpointSelection.orTimeout(5, TimeUnit.SECONDS).join();
                  return Optional.of(selectNoSecurityEndpoint(endpoints));
                })
            .setClientListener(
                (discovery, discoveredEndpoint, connected) -> deliveredClient.complete(connected))
            .setErrorListener((candidate, failure) -> reportedFailure.complete(unwrap(failure)))
            .build();

    try (acceptor) {
      acceptor.start();

      startReverseConnect(endpoint);
      endpointSelectionEntered.get(10, TimeUnit.SECONDS);

      acceptor.stop();
      releaseEndpointSelection.complete(null);

      Throwable failure = reportedFailure.get(10, TimeUnit.SECONDS);
      UaException uaException = assertInstanceOf(UaException.class, failure);
      assertEquals(StatusCodes.Bad_Shutdown, uaException.getStatusCode().value());

      startReverseConnect(endpoint);

      assertThrows(TimeoutException.class, () -> deliveredClient.get(500, TimeUnit.MILLISECONDS));
      assertFalse(deliveredClient.isDone());
    }
  }

  @Test
  void discoveryFirstEndpointSelectionFailureClosesDiscoveryConnection() throws Exception {
    startServerAndManager();

    EndpointDescription advertisedEndpoint =
        selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<Throwable> selectionFailure = new CompletableFuture<>();
    CompletableFuture<ReverseConnectTargetHandle> targetHandle = new CompletableFuture<>();
    AtomicBoolean discoveryStarted = new AtomicBoolean(false);

    manager.addListener(
        new ReverseConnectListener() {
          @Override
          public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
            if (!discoveryStarted.compareAndSet(false, true)) {
              return;
            }

            try {
              ReverseConnectConnection connection = manager.claim(snapshot.id()).orElseThrow();

              DiscoveryClient.getEndpoints(connection)
                  .thenApply(
                      endpoints -> {
                        throw new CompletionException(
                            new UaException(
                                StatusCodes.Bad_ConfigurationError, "no endpoint selected"));
                      })
                  .whenComplete(
                      (endpoint, ex) -> {
                        targetHandle.thenCompose(ReverseConnectTargetHandle::pause);
                        if (ex != null) {
                          selectionFailure.complete(unwrap(ex));
                        } else {
                          selectionFailure.completeExceptionally(
                              new AssertionError("endpoint selection unexpectedly succeeded"));
                        }
                      });
            } catch (Throwable t) {
              selectionFailure.complete(t);
            }
          }
        });

    ReverseConnectTargetHandle handle =
        server.addReverseConnectTarget(reverseTarget(advertisedEndpoint, uint(100)));
    targetHandle.complete(handle);

    handle.resume().get(5, TimeUnit.SECONDS);

    Throwable failure = selectionFailure.get(10, TimeUnit.SECONDS);
    UaException uaException = assertInstanceOf(UaException.class, failure);

    assertEquals(StatusCodes.Bad_ConfigurationError, uaException.getStatusCode().value());
    waitUntil(() -> handle.snapshot().orElseThrow().activeChannelCount() == 0);
    assertEquals(1, manager.snapshot().claimedCount());
  }

  @Test
  void productionReverseClientRemainsPendingWhenNoLaterReverseConnectionArrives() throws Exception {

    startServerAndManager();

    EndpointDescription advertisedEndpoint =
        selectEndpoint(SecurityPolicy.None, MessageSecurityMode.None);
    CompletableFuture<CompletableFuture<OpcUaClient>> pendingConnect = new CompletableFuture<>();
    CompletableFuture<ReverseConnectTargetHandle> targetHandle = new CompletableFuture<>();
    AtomicBoolean discoveryStarted = new AtomicBoolean(false);

    manager.addListener(
        new ReverseConnectListener() {
          @Override
          public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
            if (!discoveryStarted.compareAndSet(false, true)) {
              return;
            }

            try {
              ReverseConnectConnection connection = manager.claim(snapshot.id()).orElseThrow();

              DiscoveryClient.getEndpoints(connection)
                  .thenApply(OpcUaClientReverseConnectTest::selectNoSecurityEndpoint)
                  .thenCompose(
                      endpoint ->
                          targetHandle
                              .thenCompose(ReverseConnectTargetHandle::pause)
                              .thenApply(ignored -> endpoint))
                  .whenComplete(
                      (endpoint, ex) -> {
                        if (ex != null) {
                          pendingConnect.completeExceptionally(unwrap(ex));
                          return;
                        }

                        try {
                          OpcUaClient dynamicClient =
                              OpcUaClient.createReverseConnect(
                                  clientConfig(endpoint),
                                  manager,
                                  ReverseConnectSelector.byServerUriAndEndpointUrl(
                                      requireNonNull(snapshot.serverUri()),
                                      requireNonNull(snapshot.endpointUrl())));

                          client = dynamicClient;
                          pendingConnect.complete(dynamicClient.connectAsync());
                        } catch (Throwable t) {
                          pendingConnect.completeExceptionally(t);
                        }
                      });
            } catch (Throwable t) {
              pendingConnect.completeExceptionally(t);
            }
          }
        });

    ReverseConnectTargetHandle handle =
        server.addReverseConnectTarget(reverseTarget(advertisedEndpoint, uint(100)));
    targetHandle.complete(handle);

    handle.resume().get(5, TimeUnit.SECONDS);

    CompletableFuture<OpcUaClient> connectFuture = pendingConnect.get(10, TimeUnit.SECONDS);

    assertThrows(TimeoutException.class, () -> connectFuture.get(500, TimeUnit.MILLISECONDS));
    assertFalse(connectFuture.isDone());
    assertEquals(1, manager.snapshot().claimedCount());
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

  private OpcUaClient newReverseClient(EndpointDescription endpoint, boolean secure) {

    return newReverseClient(endpoint, secure, transport -> {});
  }

  private OpcUaClient newReverseClient(
      EndpointDescription endpoint,
      boolean secure,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport) {

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
    return startReverseConnect(connector, server, listenerAddress(), endpoint);
  }

  private static OpcTcpServerReverseConnectAttempt startReverseConnect(
      OpcTcpServerReverseConnector connector,
      OpcUaServer server,
      InetSocketAddress listenerAddress,
      EndpointDescription endpoint) {

    OpcTcpServerReverseConnectParameters parameters =
        OpcTcpServerReverseConnectParameters.fromAddress(
            server.getApplicationContext(),
            listenerAddress,
            endpointUrl(endpoint),
            serverUri(endpoint),
            5_000,
            OpcTcpServerReverseConnectAttemptObserver.noop());

    return connector.connect(parameters);
  }

  private ReverseConnectTarget reverseTarget(
      EndpointDescription endpoint, UInteger registrationPeriod) {

    return ReverseConnectTarget.builder()
        .setClientListenerUrl(listenerUrl())
        .setEndpointUrl(endpointUrl(endpoint))
        .setRegistrationPeriod(registrationPeriod)
        .setConnectTimeout(uint(5_000))
        .setPaused(true)
        .build();
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
    return selectEndpoint(server, policy, mode);
  }

  private static EndpointDescription selectEndpoint(
      OpcUaServer server, SecurityPolicy policy, MessageSecurityMode mode) {

    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(endpoint -> !endpointUrl(endpoint).endsWith("/discovery"))
        .filter(endpoint -> policy.getUri().equals(endpoint.getSecurityPolicyUri()))
        .filter(endpoint -> mode == endpoint.getSecurityMode())
        .findFirst()
        .orElseThrow();
  }

  private static EndpointDescription selectNoSecurityEndpoint(List<EndpointDescription> endpoints) {
    return endpoints.stream()
        .filter(endpoint -> !endpointUrl(endpoint).endsWith("/discovery"))
        .filter(endpoint -> SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri()))
        .filter(endpoint -> endpoint.getSecurityMode() == MessageSecurityMode.None)
        .findFirst()
        .orElseThrow();
  }

  private void assertReadWorks() throws Exception {
    assertReadWorks(client);
  }

  private static void assertReadWorks(OpcUaClient client) throws Exception {
    DataValue value =
        client.readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);

    assertTrue(value.statusCode().isGood(), () -> "read failed: " + value.statusCode());
  }

  private static void assertBrowseWorks(OpcUaClient client) throws Exception {
    BrowseDescription browse =
        new BrowseDescription(
            NodeIds.ObjectsFolder,
            BrowseDirection.Forward,
            NodeIds.HierarchicalReferences,
            true,
            uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
            uint(BrowseResultMask.All.getValue()));

    BrowseResult browseResult = client.browse(browse);

    assertTrue(
        browseResult.getStatusCode().isGood(),
        () -> "browse failed: " + browseResult.getStatusCode());
    assertTrue(
        browseResult.getReferences() != null && browseResult.getReferences().length > 0,
        "browse returned no references");
  }

  private void assertClaimedReverseHello(EndpointDescription endpoint) {
    List<ReverseConnectCandidateSnapshot> acceptedCandidates =
        manager.snapshot().acceptedCandidates();

    assertFalse(acceptedCandidates.isEmpty());

    assertTrue(
        acceptedCandidates.stream()
            .anyMatch(
                snapshot ->
                    snapshot.state() == ReverseConnectCandidateState.CLAIMED
                        && serverUri(endpoint).equals(snapshot.serverUri())
                        && endpointUrl(endpoint).equals(snapshot.endpointUrl())));
  }

  private static String endpointUrl(EndpointDescription endpoint) {
    return requireNonNull(endpoint.getEndpointUrl(), "endpointUrl");
  }

  private static String serverUri(EndpointDescription endpoint) {
    return requireNonNull(
        requireNonNull(endpoint.getServer(), "server").getApplicationUri(), "serverUri");
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
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

  private static Throwable unwrap(Throwable ex) {
    Throwable cause = ex;
    while ((cause instanceof CompletionException || cause instanceof ExecutionException)
        && cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }
}
