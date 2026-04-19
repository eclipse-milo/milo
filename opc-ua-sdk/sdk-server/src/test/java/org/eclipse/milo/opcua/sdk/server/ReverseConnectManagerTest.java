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

import com.digitalpetri.fsm.Fsm;
import com.digitalpetri.fsm.FsmContext;
import io.netty.channel.Channel;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpReverseConnectServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.Event;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.State;
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
  void ensureIdleConnectionSkipsDuplicateSpawnWhenReplacementAppearsAfterSnapshot() {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    ReentrantLock managerLock = getManagerLock();
    Map<ReverseConnectHandle, Fsm<State, Event>> connections = getConnections();
    Map<String, Set<ReverseConnectHandle>> handlesByClientUrl = getHandlesByClientUrl();

    ReverseConnectHandle handle1 = new ReverseConnectHandle(CLIENT_URL, ENDPOINT_URL);
    ReverseConnectHandle handle2 = new ReverseConnectHandle(CLIENT_URL, ENDPOINT_URL);
    ReverseConnectHandle replacementHandle =
        new ReverseConnectHandle(CLIENT_URL, ENDPOINT_URL, true);

    Set<ReverseConnectHandle> handles = new CopyOnWriteArraySet<>();
    handles.add(handle1);
    handles.add(handle2);
    handlesByClientUrl.put(CLIENT_URL, handles);

    AtomicBoolean swapped = new AtomicBoolean();
    Fsm<State, Event> replacementFsm = new StubFsm(State.Idle);

    connections.put(handle1, new StubFsm(State.Active));
    connections.put(
        handle2,
        new StubFsm(
            State.Active,
            () -> {
              if (swapped.compareAndSet(false, true)) {
                managerLock.lock();
                try {
                  handles.remove(handle1);
                  connections.remove(handle1);
                  handles.add(replacementHandle);
                  connections.put(replacementHandle, replacementFsm);
                } finally {
                  managerLock.unlock();
                }
              }
            }));

    // If a remove/add race only compares set size, the manager can create a
    // second idle FSM even though another thread already added the replacement.
    manager.ensureIdleConnection(CLIENT_URL, ENDPOINT_URL);

    assertEquals(2, manager.connectionCount(), "the replacement handle should prevent duplicates");
    assertEquals(
        1,
        handles.stream().filter(handle -> handle.autoSpawned).count(),
        "only one auto-spawned handle should remain");
  }

  @Test
  void removeReverseConnectStopsOnlyTargetHandle() {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    // Add two handles for the same client URL — creates two FSMs sharing
    // the same handlesByClientUrl entry, simulating the original + idle sibling.
    ReverseConnectHandle handle1 = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    ReverseConnectHandle handle2 = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    assertEquals(2, manager.connectionCount());
    assertEquals(1, manager.handleGroupCount());

    // Remove one handle — should stop only that handle's FSM, leaving the
    // sibling intact since removeReverseConnect operates per-handle.
    manager.removeReverseConnect(handle1);

    assertEquals(
        1, manager.connectionCount(), "only the removed handle's connection should be gone");
    assertEquals(1, manager.handleGroupCount(), "handle group should remain for surviving sibling");

    // Remove the second handle — now everything should be cleaned up.
    manager.removeReverseConnect(handle2);

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

  // If a late add publishes after stop() has already won the lifecycle lock,
  // shutdown can complete while a new outbound reverse connection starts.
  @Test
  void addReverseConnectDoesNotPublishFsmAfterStopWinsLifecycleLock() throws Exception {
    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    manager.start(appContext, SERVER_URI);

    ReentrantLock managerLock = getManagerLock();
    AtomicReference<ReverseConnectHandle> handleRef = new AtomicReference<>();
    Thread addThread =
        new Thread(() -> handleRef.set(manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL)));

    managerLock.lock();
    try {
      addThread.start();
      awaitThreadBlockedOnManagerLock(addThread);

      manager.stop().get(5, TimeUnit.SECONDS);
    } finally {
      managerLock.unlock();
    }

    addThread.join(TimeUnit.SECONDS.toMillis(5));

    assertEquals(
        Thread.State.TERMINATED,
        addThread.getState(),
        "add thread should finish after the lifecycle lock releases");
    assertNotNull(handleRef.get(), "addReverseConnect should still return a handle");
    assertEquals(0, manager.connectionCount(), "stop should prevent late FSM publication");
    assertEquals(0, manager.handleGroupCount(), "stop should prevent late handle publication");
    stubTransport.assertNoConnectAttempt();
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

    void assertNoConnectAttempt() throws Exception {
      CompletableFuture<Channel> f = connectFutures.poll(250, TimeUnit.MILLISECONDS);
      if (f != null) {
        fail("connect() should not be called");
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Map<ReverseConnectHandle, Fsm<State, Event>> getConnections() {
    return (Map<ReverseConnectHandle, Fsm<State, Event>>) getField("connections");
  }

  @SuppressWarnings("unchecked")
  private Map<String, Set<ReverseConnectHandle>> getHandlesByClientUrl() {
    return (Map<String, Set<ReverseConnectHandle>>) getField("handlesByClientUrl");
  }

  private ReentrantLock getManagerLock() {
    return (ReentrantLock) getField("lock");
  }

  private Object getField(String name) {
    try {
      Field field = ReverseConnectManager.class.getDeclaredField(name);
      field.setAccessible(true);

      return field.get(manager);
    } catch (ReflectiveOperationException e) {
      throw new AssertionError(e);
    }
  }

  private void awaitThreadBlockedOnManagerLock(Thread thread) throws Exception {
    long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

    while (System.nanoTime() < deadlineNanos) {
      Thread.State state = thread.getState();

      if (state == Thread.State.WAITING || state == Thread.State.BLOCKED) {
        return;
      }
      if (state == Thread.State.TERMINATED) {
        fail("Thread terminated before blocking on ReverseConnectManager.lock");
      }

      Thread.sleep(10);
    }

    fail("Timed out waiting for thread to block on ReverseConnectManager.lock");
  }

  private static final class StubFsm implements Fsm<State, Event> {

    private final State state;
    private final Runnable onGetState;

    private StubFsm(State state) {
      this(state, () -> {});
    }

    private StubFsm(State state, Runnable onGetState) {
      this.state = state;
      this.onGetState = onGetState;
    }

    @Override
    public State getState() {
      onGetState.run();

      return state;
    }

    @Override
    public void fireEvent(Event event) {}

    @Override
    public void fireEvent(Event event, Consumer<State> stateConsumer) {
      stateConsumer.accept(state);
    }

    @Override
    public State fireEventBlocking(Event event) {
      return state;
    }

    @Override
    public <T> T getFromContext(Function<FsmContext<State, Event>, T> get) {
      return null;
    }

    @Override
    public void withContext(Consumer<FsmContext<State, Event>> contextConsumer) {}
  }
}
