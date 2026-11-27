/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseTcpClientTransport;
import org.eclipse.milo.opcua.sdk.client.session.State;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

/**
 * Integration tests verifying that {@link OpcUaClient#disconnectAsync()} called while a real
 * transport's {@link OpcUaClient#connectAsync()} is still in flight cancels Session opening, leaves
 * {@code SessionFsm} {@link State#Inactive}, and permits a subsequent {@link
 * OpcUaClient#connectAsync()} to establish a usable Session.
 */
class OpcUaClientDisconnectReconnectTest {

  // Regression test for #2213 over OpcTcpClientTransport: disconnecting while the TCP transport is
  // waiting on Acknowledge from a silent peer must abort promptly, fail connectAsync() with
  // Bad_ConnectionClosed, leave SessionFsm Inactive without starting CreateSession retries, and
  // allow a subsequent connectAsync() to a live server to succeed.
  @Test
  void disconnectDuringStalledTcpConnectPreventsSessionRetriesAndAllowsReconnect()
      throws Exception {

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    EndpointDescription serverEndpoint = selectNoSecurityEndpoint(server);
    URI serverUri = URI.create(requireNonNull(serverEndpoint.getEndpointUrl()));

    try (var proxy = new StallingTcpProxy(serverUri.getHost(), serverUri.getPort())) {
      EndpointDescription proxyEndpoint =
          EndpointUtil.updateUrl(serverEndpoint, "127.0.0.1", proxy.port());

      OpcUaClientConfig clientConfig =
          OpcUaClientConfig.builder()
              .setEndpoint(proxyEndpoint)
              .setDiscoveryEndpoints(List.of(proxyEndpoint))
              .setSessionEndpointValidationEnabled(false)
              .setApplicationName(LocalizedText.english("eclipse milo test client"))
              .setApplicationUri("urn:eclipse:milo:test:client")
              .setRequestTimeout(uint(5_000))
              .build();

      var transport = new OpcTcpClientTransport(OpcTcpClientTransportConfig.newBuilder().build());
      var client = new OpcUaClient(clientConfig, transport);

      var sessionAttemptFailures = new AtomicInteger();
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
              sessionAttemptFailures.incrementAndGet();
            }
          });

      try {
        CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
        assertTrue(
            proxy.awaitStalledConnection(5, TimeUnit.SECONDS),
            "OpcTcpClientTransport should open a TCP connection to the stalling proxy");

        long startNanos = System.nanoTime();
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        assertTrue(
            elapsedMillis < 3_000L,
            "disconnectAsync() must return promptly during stalled connect; took "
                + elapsedMillis
                + "ms");

        assertFailedWithStatus(firstConnect, StatusCodes.Bad_ConnectionClosed);
        assertEquals(State.Inactive, client.getSessionFsm().getState());
        assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
        assertEquals(
            0,
            sessionAttemptFailures.get(),
            "SessionFsm must not start or retry CreateSession after disconnectAsync()");
        assertEquals(
            1,
            proxy.acceptedCount(),
            "transport must not initiate additional TCP reconnect attempts after"
                + " disconnectAsync()");

        // Allow connections through to the live server and verify a subsequent connect succeeds.
        proxy.setStallConnections(false);

        assertSame(client, client.connectAsync().get(10, TimeUnit.SECONDS));
        assertEquals(State.Active, client.getSessionFsm().getState());

        DataValue serverState =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(10, TimeUnit.SECONDS)
                .get(0);
        assertTrue(
            serverState.statusCode().isGood(),
            "reconnected Session must be usable for service calls");
      } finally {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // Regression test for #2213 over ReverseTcpClientTransport: disconnecting while waiting for an
  // inbound reverse connection must cancel the pending connect with Bad_ConnectionClosed, keep
  // SessionFsm Inactive (so getSessionAsync() fails immediately with Bad_SessionClosed instead of
  // retrying), and allow a later connectAsync() to succeed when the server connects.
  @Test
  void disconnectDuringPendingReverseConnectPreventsSessionRetriesAndAllowsReconnect()
      throws Exception {

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .setFirstMessageTimeout(Duration.ofSeconds(5))
            .setPendingConnectionHoldTime(Duration.ofSeconds(5))
            .build();
    manager.startup();

    EndpointDescription endpoint = selectNoSecurityEndpoint(server);
    String endpointUrl = requireNonNull(endpoint.getEndpointUrl());
    String serverUri = requireNonNull(requireNonNull(endpoint.getServer()).getApplicationUri());

    OpcUaClientConfig clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setApplicationName(LocalizedText.english("eclipse milo reverse test client"))
            .setApplicationUri("urn:eclipse:milo:test:reverse:client")
            .setRequestTimeout(uint(5_000))
            .build();

    OpcUaClient client =
        OpcUaClient.createReverseConnect(
            clientConfig,
            manager,
            ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl));

    var sessionAttemptFailures = new AtomicInteger();
    client.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
            sessionAttemptFailures.incrementAndGet();
          }
        });

    try {
      CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();

      long startNanos = System.nanoTime();
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
      assertTrue(
          elapsedMillis < 3_000L,
          "disconnectAsync() must return promptly while waiting for reverse connect; took "
              + elapsedMillis
              + "ms");

      assertFailedWithStatus(firstConnect, StatusCodes.Bad_ConnectionClosed);
      assertEquals(State.Inactive, client.getSessionFsm().getState());
      assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
      assertEquals(
          0,
          sessionAttemptFailures.get(),
          "SessionFsm must not start or retry CreateSession after disconnectAsync()");

      // Reconnect and have the live server initiate a reverse connection.
      CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();

      InetSocketAddress listenerAddress =
          assertInstanceOf(
              InetSocketAddress.class, manager.snapshot().listeners().get(0).boundAddress());
      String listenerUrl =
          "opc.tcp://" + listenerAddress.getHostString() + ":" + listenerAddress.getPort();

      server.addReverseConnectTarget(
          ReverseConnectTarget.builder()
              .setClientListenerUrl(listenerUrl)
              .setEndpointUrl(endpointUrl)
              .setRegistrationPeriod(uint(30_000))
              .setConnectTimeout(uint(5_000))
              .build());

      assertSame(client, secondConnect.get(10, TimeUnit.SECONDS));
      assertEquals(State.Active, client.getSessionFsm().getState());

      DataValue serverState =
          client
              .readValuesAsync(
                  0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
              .get(10, TimeUnit.SECONDS)
              .get(0);
      assertTrue(
          serverState.statusCode().isGood(),
          "reconnected reverse-connect Session must be usable for service calls");
    } finally {
      try {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      } finally {
        try {
          manager.shutdown();
        } finally {
          server.shutdown().get(10, TimeUnit.SECONDS);
        }
      }
    }
  }

  // Regression test for ReverseTcpClientTransport.completeHandshake() <-> disconnectAsync()
  // lock ordering: ReverseTcpClientTransport completes its connect future inline while holding
  // its internal lock, while disconnectAsync() calls transport.disconnect() which acquires that
  // same lock. Racing the two concurrently must never deadlock, must leave SessionFsm Inactive
  // without issuing an extra closeSession(), and must allow a subsequent reconnect to succeed.
  @Test
  void disconnectConcurrentWithReverseHandshakeCompletionDoesNotDeadlockAndAllowsReconnect()
      throws Exception {

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .setFirstMessageTimeout(Duration.ofSeconds(5))
            .setPendingConnectionHoldTime(Duration.ofSeconds(5))
            .build();
    manager.startup();

    EndpointDescription endpoint = selectNoSecurityEndpoint(server);
    String endpointUrl = requireNonNull(endpoint.getEndpointUrl());
    String serverUri = requireNonNull(requireNonNull(endpoint.getServer()).getApplicationUri());

    InetSocketAddress listenerAddress =
        assertInstanceOf(
            InetSocketAddress.class, manager.snapshot().listeners().get(0).boundAddress());
    String listenerUrl =
        "opc.tcp://" + listenerAddress.getHostString() + ":" + listenerAddress.getPort();

    var reverseTransport =
        new ReverseTcpClientTransport(
            OpcTcpClientTransportConfig.newBuilder().build(),
            manager,
            ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl));
    var handshakesCompleted = new AtomicInteger();
    var lockContentionRendezvous = new AtomicInteger();
    var onHandshake = new AtomicReference<Runnable>();
    var onDisconnect = new AtomicReference<Runnable>();
    OpcClientTransport racingTransport =
        new OpcClientTransport() {
          @Override
          public OpcClientTransportConfig getConfig() {
            return reverseTransport.getConfig();
          }

          @Override
          public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
            return reverseTransport
                .connect(applicationContext)
                .whenComplete(
                    (unit, ex) -> {
                      if (ex == null) {
                        // Runs synchronously inside ReverseTcpClientTransport.completeHandshake()
                        // while ReverseTcpClientTransport.lock is held.
                        handshakesCompleted.incrementAndGet();
                        Runnable callback = onHandshake.get();
                        if (callback != null) {
                          callback.run();
                        }
                      }
                    });
          }

          @Override
          public CompletableFuture<Unit> disconnect() {
            Runnable callback = onDisconnect.get();
            if (callback != null) {
              callback.run();
            }
            return reverseTransport.disconnect();
          }

          @Override
          public CompletableFuture<UaResponseMessageType> sendRequestMessage(
              UaRequestMessageType requestMessage) {
            return reverseTransport.sendRequestMessage(requestMessage);
          }
        };
    ExecutorService disconnector = Executors.newSingleThreadExecutor();

    OpcUaClientConfig clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setApplicationName(LocalizedText.english("eclipse milo reverse race test client"))
            .setApplicationUri("urn:eclipse:milo:test:reverse:race:client")
            .setRequestTimeout(uint(5_000))
            .build();

    var client = new OpcUaClient(clientConfig, racingTransport);

    try {
      for (int i = 0; i < 5; i++) {
        var handshakeHoldingLock = new CountDownLatch(1);
        var disconnectReachedTransport = new CountDownLatch(1);
        var rendezvousReached = new AtomicBoolean(false);

        onHandshake.set(
            () -> {
              // Signal disconnector that we are inside completeHandshake() holding
              // ReverseTcpClientTransport.lock, and keep holding that lock until
              // disconnectAsync() reaches reverseTransport.disconnect().
              handshakeHoldingLock.countDown();
              try {
                if (disconnectReachedTransport.await(5, TimeUnit.SECONDS)) {
                  rendezvousReached.set(true);
                  lockContentionRendezvous.incrementAndGet();
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            });
        onDisconnect.set(disconnectReachedTransport::countDown);

        CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

        Future<CompletableFuture<OpcUaClient>> disconnectTask =
            disconnector.submit(
                () -> {
                  assertTrue(
                      handshakeHoldingLock.await(5, TimeUnit.SECONDS),
                      "handshake must enter completeHandshake() while holding transport lock");
                  return client.disconnectAsync();
                });

        ReverseConnectTargetHandle handle =
            server.addReverseConnectTarget(
                ReverseConnectTarget.builder()
                    .setClientListenerUrl(listenerUrl)
                    .setEndpointUrl(endpointUrl)
                    .setRegistrationPeriod(uint(30_000))
                    .setConnectTimeout(uint(5_000))
                    .build());

        try {
          disconnectTask.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS);
          connectFuture.handle((c, ex) -> Unit.VALUE).get(5, TimeUnit.SECONDS);

          assertTrue(
              rendezvousReached.get(),
              "iteration "
                  + i
                  + " must hold ReverseTcpClientTransport.lock until"
                  + " reverseTransport.disconnect() is entered");
          awaitState(
              client,
              State.Inactive,
              "iteration " + i + " must settle in State.Inactive without deadlocking");
          assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
        } finally {
          disconnectReachedTransport.countDown();
          onHandshake.set(null);
          onDisconnect.set(null);
          handle.remove().get(5, TimeUnit.SECONDS);
        }
      }

      assertEquals(
          5,
          handshakesCompleted.get(),
          "every raced iteration must actually reach"
              + " ReverseTcpClientTransport.completeHandshake()");
      assertEquals(
          5,
          lockContentionRendezvous.get(),
          "every raced iteration must rendezvous completeHandshake() with"
              + " reverseTransport.disconnect()");

      // Verify subsequent reverse connect establishes an active, usable Session.
      onHandshake.set(null);
      onDisconnect.set(null);
      CompletableFuture<OpcUaClient> reconnectFuture = client.connectAsync();
      ReverseConnectTargetHandle reconnectHandle =
          server.addReverseConnectTarget(
              ReverseConnectTarget.builder()
                  .setClientListenerUrl(listenerUrl)
                  .setEndpointUrl(endpointUrl)
                  .setRegistrationPeriod(uint(30_000))
                  .setConnectTimeout(uint(5_000))
                  .build());

      try {
        assertSame(client, reconnectFuture.get(10, TimeUnit.SECONDS));
        assertEquals(State.Active, client.getSessionFsm().getState());
        assertEquals(6, handshakesCompleted.get());

        DataValue serverState =
            client
                .readValuesAsync(
                    0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
                .get(10, TimeUnit.SECONDS)
                .get(0);
        assertTrue(
            serverState.statusCode().isGood(),
            "reconnected reverse-connect Session after handshake race must be usable");
      } finally {
        reconnectHandle.remove().get(5, TimeUnit.SECONDS);
      }
    } finally {
      try {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      } finally {
        disconnector.shutdownNow();
        try {
          manager.shutdown();
        } finally {
          server.shutdown().get(10, TimeUnit.SECONDS);
        }
      }
    }
  }

  // If disconnectAsync() queues a shelved CloseSession while SessionFsm is in Creating and a newer
  // connectAsync() starts before that close completes, the newer connectAsync() must wait for the
  // shelved CloseSession to finish and then open a fresh Session that stays Active and usable,
  // rather than joining the in-flight Creating attempt and losing its Session immediately after.
  @Test
  void reconnectDuringShelvedCloseInCreatingWaitsAndEstablishesUsableSession() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    var createSessionCalls = new AtomicInteger();
    var firstCreateSessionHeld = new CountDownLatch(1);
    var releaseFirstCreateSession = new CompletableFuture<Void>();

    OpcUaClient client =
        TestClient.createWithTransport(
            server,
            transportConfig -> {
              var delegate = new OpcTcpClientTransport(transportConfig);
              return new OpcClientTransport() {
                @Override
                public OpcClientTransportConfig getConfig() {
                  return delegate.getConfig();
                }

                @Override
                public CompletableFuture<Unit> connect(
                    ClientApplicationContext applicationContext) {
                  return delegate.connect(applicationContext);
                }

                @Override
                public CompletableFuture<Unit> disconnect() {
                  return delegate.disconnect();
                }

                @Override
                public CompletableFuture<UaResponseMessageType> sendRequestMessage(
                    UaRequestMessageType requestMessage) {
                  if (requestMessage instanceof CreateSessionRequest
                      && createSessionCalls.incrementAndGet() == 1) {
                    return delegate
                        .sendRequestMessage(requestMessage)
                        .thenCompose(
                            response -> {
                              firstCreateSessionHeld.countDown();
                              return releaseFirstCreateSession.thenApply(ignored -> response);
                            });
                  }
                  return delegate.sendRequestMessage(requestMessage);
                }
              };
            },
            cfg -> {});

    try {
      CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
      assertTrue(
          firstCreateSessionHeld.await(10, TimeUnit.SECONDS),
          "first connectAsync() should hold in State.Creating awaiting CreateSessionResponse");

      CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();
      CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();

      assertFalse(
          secondConnect.isDone(),
          "second connectAsync() must wait for the earlier shelved CloseSession to finish");
      assertEquals(1, createSessionCalls.get());

      // Release the held CreateSessionResponse so the shelved CloseSession completes to Inactive,
      // and verify the second connectAsync() then opens a fresh Session that remains Active.
      releaseFirstCreateSession.complete(null);

      disconnectFuture.get(10, TimeUnit.SECONDS);
      assertSame(client, secondConnect.get(10, TimeUnit.SECONDS));
      firstConnect.handle((c, ex) -> Unit.VALUE).get(10, TimeUnit.SECONDS);

      assertEquals(
          2,
          createSessionCalls.get(),
          "second connectAsync() must open a fresh Session instead of joining the closing one");
      assertEquals(
          State.Active,
          client.getSessionFsm().getState(),
          "client must remain Active after second connectAsync() settles");
      assertNotNull(client.getSessionAsync().get(5, TimeUnit.SECONDS));

      DataValue serverState =
          client
              .readValuesAsync(
                  0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
              .get(10, TimeUnit.SECONDS)
              .get(0);
      assertTrue(
          serverState.statusCode().isGood(),
          "Session established by second connectAsync() must remain usable for service calls");
    } finally {
      releaseFirstCreateSession.complete(null);
      try {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  // If disconnectAsync() queues a shelved CloseSession while SessionFsm is stalled on a
  // CreateSessionRequest that only fails when transport.disconnect() runs, and a newer
  // connectAsync() starts before disconnectCloseSessionTimeoutMillis elapses, the timed-out
  // disconnect must still tear down the old transport to unblock CloseSession and let the newer
  // connectAsync() reconnect cleanly and establish a usable Active Session.
  @Test
  void reconnectDuringTimedOutShelvedCloseTearsDownTransportAndEstablishesUsableSession()
      throws Exception {

    long savedTimeoutMillis = OpcUaClient.disconnectCloseSessionTimeoutMillis;
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    var createSessionCalls = new AtomicInteger();
    var disconnectCalls = new AtomicInteger();
    var firstCreateSessionStalled = new CountDownLatch(1);
    var stalledCreateSessionFuture =
        new AtomicReference<CompletableFuture<UaResponseMessageType>>();

    OpcUaClient client =
        TestClient.createWithTransport(
            server,
            transportConfig -> {
              var delegate = new OpcTcpClientTransport(transportConfig);
              return new OpcClientTransport() {
                @Override
                public OpcClientTransportConfig getConfig() {
                  return delegate.getConfig();
                }

                @Override
                public CompletableFuture<Unit> connect(
                    ClientApplicationContext applicationContext) {
                  return delegate.connect(applicationContext);
                }

                @Override
                public CompletableFuture<Unit> disconnect() {
                  disconnectCalls.incrementAndGet();
                  CompletableFuture<UaResponseMessageType> stalled =
                      stalledCreateSessionFuture.getAndSet(null);
                  if (stalled != null) {
                    stalled.completeExceptionally(
                        new UaException(StatusCodes.Bad_ConnectionClosed));
                  }
                  return delegate.disconnect();
                }

                @Override
                public CompletableFuture<UaResponseMessageType> sendRequestMessage(
                    UaRequestMessageType requestMessage) {
                  if (requestMessage instanceof CreateSessionRequest
                      && createSessionCalls.incrementAndGet() == 1) {
                    var stalled = new CompletableFuture<UaResponseMessageType>();
                    stalledCreateSessionFuture.set(stalled);
                    firstCreateSessionStalled.countDown();
                    return stalled;
                  }
                  return delegate.sendRequestMessage(requestMessage);
                }
              };
            },
            cfg -> {});

    try {
      CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
      assertTrue(
          firstCreateSessionStalled.await(10, TimeUnit.SECONDS),
          "first connectAsync() should stall in State.Creating awaiting CreateSessionResponse");

      // Neither disconnect nor reconnect manually fails the stalled request; only
      // transport.disconnect() running after the 250ms timeout can unblock CloseSession.
      CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();
      CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();

      disconnectFuture.get(10, TimeUnit.SECONDS);
      assertSame(client, secondConnect.get(10, TimeUnit.SECONDS));
      firstConnect.handle((c, ex) -> Unit.VALUE).get(10, TimeUnit.SECONDS);

      assertEquals(
          1,
          disconnectCalls.get(),
          "timed-out disconnectAsync() must tear down the old transport exactly once");
      assertEquals(
          2,
          createSessionCalls.get(),
          "second connectAsync() must open a fresh Session after old transport teardown");
      assertEquals(
          State.Active,
          client.getSessionFsm().getState(),
          "client must remain Active after timed-out disconnect and reconnect");

      DataValue serverState =
          client
              .readValuesAsync(
                  0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_State))
              .get(10, TimeUnit.SECONDS)
              .get(0);
      assertTrue(
          serverState.statusCode().isGood(),
          "Session established after timed-out disconnect must be usable for service calls");
    } finally {
      OpcUaClient.disconnectCloseSessionTimeoutMillis = savedTimeoutMillis;
      try {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  private static void awaitState(OpcUaClient client, State expectedState, String message) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (System.nanoTime() < deadline) {
      if (client.getSessionFsm().getState() == expectedState) {
        return;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }
    assertEquals(expectedState, client.getSessionFsm().getState(), message);
  }

  private static EndpointDescription selectNoSecurityEndpoint(OpcUaServer server) {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(e -> !requireNonNull(e.getEndpointUrl()).endsWith("/discovery"))
        .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
        .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
        .findFirst()
        .orElseThrow();
  }

  private static void assertFailedWithStatus(CompletableFuture<?> future, long expectedStatusCode) {
    ExecutionException ex =
        assertThrows(
            ExecutionException.class,
            () -> future.get(2, TimeUnit.SECONDS),
            "future should fail promptly with status 0x" + Long.toHexString(expectedStatusCode));

    UaException uaException = assertInstanceOf(UaException.class, ex.getCause());
    assertEquals(expectedStatusCode, uaException.getStatusCode().value());
  }

  /**
   * Minimal local TCP proxy that initially accepts connections silently (never replying to UA-TCP
   * Hello) and later forwards connections transparently to the live {@link OpcUaServer}.
   */
  private static final class StallingTcpProxy implements AutoCloseable {

    private final ServerSocket serverSocket;
    private final String targetHost;
    private final int targetPort;
    private final CountDownLatch stalledConnectionAccepted = new CountDownLatch(1);
    private final AtomicInteger acceptedCount = new AtomicInteger();
    private final List<Socket> openSockets = new CopyOnWriteArrayList<>();
    private volatile boolean stallConnections = true;
    private volatile boolean closed;

    StallingTcpProxy(String targetHost, int targetPort) throws IOException {
      this.targetHost = targetHost;
      this.targetPort = targetPort;
      this.serverSocket = new ServerSocket(0, 50, InetAddress.getByName("127.0.0.1"));

      Thread acceptThread = new Thread(this::acceptLoop, "stalling-tcp-proxy-accept");
      acceptThread.setDaemon(true);
      acceptThread.start();
    }

    int port() {
      return serverSocket.getLocalPort();
    }

    int acceptedCount() {
      return acceptedCount.get();
    }

    void setStallConnections(boolean stallConnections) {
      this.stallConnections = stallConnections;
    }

    boolean awaitStalledConnection(long timeout, TimeUnit unit) throws InterruptedException {
      return stalledConnectionAccepted.await(timeout, unit);
    }

    private void acceptLoop() {
      while (!closed) {
        try {
          Socket clientSocket = serverSocket.accept();
          openSockets.add(clientSocket);
          acceptedCount.incrementAndGet();

          if (stallConnections) {
            stalledConnectionAccepted.countDown();
          } else {
            forward(clientSocket);
          }
        } catch (IOException ignored) {
          if (closed) {
            return;
          }
        }
      }
    }

    private void forward(Socket clientSocket) {
      try {
        Socket upstreamSocket = new Socket(targetHost, targetPort);
        openSockets.add(upstreamSocket);

        startPump(clientSocket, upstreamSocket, "proxy-c2s");
        startPump(upstreamSocket, clientSocket, "proxy-s2c");
      } catch (IOException ignored) {
        closeQuietly(clientSocket);
      }
    }

    private static void startPump(Socket from, Socket to, String name) {
      Thread pump =
          new Thread(
              () -> {
                try (InputStream in = from.getInputStream();
                    OutputStream out = to.getOutputStream()) {
                  in.transferTo(out);
                } catch (IOException ignored) {
                  // Expected when either side closes the socket.
                } finally {
                  closeQuietly(from);
                  closeQuietly(to);
                }
              },
              name);
      pump.setDaemon(true);
      pump.start();
    }

    @Override
    public void close() {
      closed = true;
      try {
        serverSocket.close();
      } catch (IOException ignored) {
        // Best-effort cleanup.
      }
      for (Socket socket : openSockets) {
        closeQuietly(socket);
      }
    }

    private static void closeQuietly(Socket socket) {
      try {
        socket.close();
      } catch (IOException ignored) {
        // Best-effort cleanup.
      }
    }
  }
}
