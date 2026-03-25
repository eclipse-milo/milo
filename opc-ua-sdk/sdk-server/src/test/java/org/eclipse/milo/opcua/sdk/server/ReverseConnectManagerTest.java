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

  @Test
  void addReverseConnectDuringStartDoesNotOrphan() throws Exception {
    // Verifies that an addReverseConnect call racing with start() does not
    // orphan the registration. The synchronized(pendingRegistrations) block
    // in both methods ensures mutual exclusion.
    var addDone = new CountDownLatch(1);
    var startDone = new CountDownLatch(1);

    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);

    Thread addThread =
        new Thread(
            () -> {
              manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
              addDone.countDown();
            });

    Thread startThread =
        new Thread(
            () -> {
              manager.start(appContext, SERVER_URI);
              startDone.countDown();
            });

    addThread.start();
    startThread.start();

    assertTrue(addDone.await(5, TimeUnit.SECONDS));
    assertTrue(startDone.await(5, TimeUnit.SECONDS));

    // The registration must appear in the connections map regardless of
    // which thread ran first.
    assertEquals(1, manager.connectionCount(), "registration should not be orphaned");
  }

  @Test
  void removeReverseConnectBeforeStartCleansPendingRegistration() {
    // Add a registration before start, then remove it. When start() runs,
    // the registration should already be gone from pendingRegistrations.
    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);

    manager.removeReverseConnect(handle);

    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    assertEquals(0, manager.connectionCount(), "removed registration should not be materialized");
  }

  @Test
  void restartRebuildsDeadFsms() throws Exception {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    // Add a registration — creates and starts one FSM
    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    assertEquals(1, manager.connectionCount());

    // Drain the connect future from the first start cycle
    assertNotNull(stubTransport.pollConnectFuture());

    // Stop the manager — FSMs transition to Disconnected (terminal)
    manager.stop();

    // Restart — should rebuild the dead FSMs with fresh ones
    manager.start(appContext, SERVER_URI);

    assertEquals(
        1, manager.connectionCount(), "connection count should be preserved after restart");

    // The rebuilt FSM should fire a new connect, proving it is alive
    assertNotNull(stubTransport.pollConnectFuture());
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
