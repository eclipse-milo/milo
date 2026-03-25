/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpReverseConnectServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReverseConnectManagerTest {

  private static final String CLIENT_URL = "opc.tcp://client-host:48060";
  private static final String ENDPOINT_URL = "opc.tcp://server-host:4840";
  private static final String SERVER_URI = "urn:test:server";

  private ExecutorService executor;
  private StubTransport stubTransport;
  private ReverseConnectManager manager;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    stubTransport = new StubTransport();

    ReverseConnectConfig config =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(Duration.ofMillis(100))
            .setConnectTimeout(Duration.ofSeconds(5))
            .setRejectBackoff(Duration.ofMillis(500))
            .setMaxReconnectDelay(Duration.ofMillis(800))
            .build();

    OpcTcpServerTransportConfig transportConfig =
        OpcTcpServerTransportConfig.newBuilder().setExecutor(executor).build();

    manager = new ReverseConnectManager(config, transportConfig, stubTransport);
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  @Test
  void removeReverseConnectDoesNotLeakConcurrentIdleFsm() throws Exception {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    // Add a handle — this creates and starts one FSM
    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);

    // Wait for the FSM's connect() call to appear
    assertNotNull(stubTransport.pollConnectFuture());

    // Use a latch to synchronize concurrent operations
    var removeDone = new CountDownLatch(1);
    var ensureDone = new CountDownLatch(1);

    // Concurrently call removeReverseConnect and ensureIdleConnection
    Thread removeThread =
        new Thread(
            () -> {
              manager.removeReverseConnect(handle);
              removeDone.countDown();
            });

    Thread ensureThread =
        new Thread(
            () -> {
              manager.ensureIdleConnection(CLIENT_URL, ENDPOINT_URL);
              ensureDone.countDown();
            });

    removeThread.start();
    ensureThread.start();

    assertTrue(removeDone.await(5, TimeUnit.SECONDS));
    assertTrue(ensureDone.await(5, TimeUnit.SECONDS));

    // After removal, either ensureIdleConnection saw the removal (handles == null)
    // and did nothing, or it ran first and the removal cleaned up everything.
    // In either case, handlesByClientUrl should be empty.
    assertEquals(0, manager.handleGroupCount(), "handlesByClientUrl should be empty after removal");
  }

  @Test
  void removeReverseConnectStopsAllSiblings() {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    // Add two handles for the same client URL — creates two FSMs sharing
    // the same handlesByClientUrl entry, simulating the original + idle sibling.
    ReverseConnectHandle handle1 = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    assertEquals(2, manager.connectionCount());
    assertEquals(1, manager.handleGroupCount());

    // Remove one handle — should stop both FSMs since removeReverseConnect
    // tears down the entire handlesByClientUrl group for that client URL.
    manager.removeReverseConnect(handle1);

    assertEquals(0, manager.connectionCount(), "all connections should be removed");
    assertEquals(0, manager.handleGroupCount(), "handlesByClientUrl should be empty");
  }

  @Test
  void ensureIdleConnectionNoOpAfterStop() {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    // Add a handle so the handlesByClientUrl entry exists
    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    assertNotNull(handle);
    assertEquals(1, manager.connectionCount());

    // Stop the manager
    manager.stop();

    int countAfterStop = manager.connectionCount();

    // ensureIdleConnection should be a no-op because running == false
    manager.ensureIdleConnection(CLIENT_URL, ENDPOINT_URL);

    assertEquals(
        countAfterStop, manager.connectionCount(), "no new FSMs should be created after stop");
  }

  /** A stub transport that captures connect calls and lets tests control returned futures. */
  static class StubTransport extends OpcTcpReverseConnectServerTransport {

    private final LinkedBlockingQueue<CompletableFuture<Channel>> connectFutures =
        new LinkedBlockingQueue<>();

    StubTransport() {
      super(null);
    }

    @Override
    public CompletableFuture<Channel> connect(
        ServerApplicationContext applicationContext,
        InetSocketAddress clientAddress,
        String serverUri,
        String endpointUrl,
        long connectTimeoutMs) {

      var future = new CompletableFuture<Channel>();
      connectFutures.offer(future);
      return future;
    }

    CompletableFuture<Channel> pollConnectFuture() throws Exception {
      CompletableFuture<Channel> f = connectFutures.poll(5, TimeUnit.SECONDS);
      if (f != null) {
        return f;
      }
      return fail("Timed out waiting for connect() to be called");
    }
  }
}
