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

import static org.junit.jupiter.api.Assertions.*;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpReverseConnectServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectAttempt;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReverseConnectManagerTest {

  private static final String CLIENT_URL = "opc.tcp://client-host:48060";
  private static final String SECOND_CLIENT_URL = "opc.tcp://second-client-host:48061";
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
  void pendingRegistrationBeforeStartupMaterializesOnStart() throws Exception {
    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL);

    assertEquals("", handle.getEndpointUrl());
    assertEquals(1, manager.registrationCount());
    assertEquals(0, manager.connectionCount());

    startManager(appContext());

    StartedAttempt attempt = stubTransport.pollAttempt();

    assertEquals(ENDPOINT_URL, handle.getEndpointUrl());
    assertEquals(ENDPOINT_URL, attempt.endpointUrl());
    assertEquals(SERVER_URI, attempt.serverUri());
    assertEquals(48060, attempt.clientAddress().getPort());
    assertEquals(1, manager.connectionCount());

    CompletableFuture<Void> stopFuture = manager.stop();
    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
  }

  @Test
  void defaultEndpointSkipsNonTcpEndpoints() throws Exception {
    String nonTcpEndpointUrl = "opc.wss://server-host:443";
    ServerApplicationContext appContext =
        appContext(
            endpointDescription(nonTcpEndpointUrl, TransportProfile.WSS_UASC_UABINARY),
            endpointDescription(ENDPOINT_URL, TransportProfile.TCP_UASC_UABINARY));

    startManager(appContext);

    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    assertEquals(ENDPOINT_URL, handle.getEndpointUrl());
    assertEquals(ENDPOINT_URL, attempt.endpointUrl());

    CompletableFuture<Void> stopFuture = manager.stop();
    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
  }

  @Test
  void defaultEndpointRequiresTcpUascEndpoint() throws Exception {
    manager.addReverseConnect(CLIENT_URL);

    ServerApplicationContext appContext =
        appContext(
            endpointDescription("opc.wss://server-host:443", TransportProfile.WSS_UASC_UABINARY));

    CompletableFuture<Void> startFuture = manager.start(appContext, SERVER_URI);
    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> startFuture.get(5, TimeUnit.SECONDS));

    assertInstanceOf(IllegalStateException.class, exception.getCause());
    assertEquals(
        "No non-discovery TCP UASC endpoints configured", exception.getCause().getMessage());
  }

  @Test
  void rejectsNonTcpClientEndpointUrl() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.addReverseConnect("opc.wss://client-host:48060"));

    assertTrue(exception.getMessage().contains("opc.tcp"));
    assertEquals(0, manager.registrationCount());
  }

  @Test
  void removeBetweenStartPublicationAndTargetStartDoesNotFailStart() throws Exception {
    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    ServerApplicationContext appContext = appContext();
    List<?> targetsToStart = prepareStartLocked(appContext);

    CompletableFuture<Void> removeFuture = manager.removeReverseConnectAsync(handle);
    CompletableFuture<Void> startFuture = finishStart(targetsToStart, appContext);

    startFuture.get(5, TimeUnit.SECONDS);
    removeFuture.get(5, TimeUnit.SECONDS);

    assertEquals(0, manager.connectionCount());
    assertEquals(0, manager.registrationCount());
    stubTransport.assertNoAttempt();

    manager.stop().get(5, TimeUnit.SECONDS);
  }

  @Test
  void repeatedStartWhileRunningDoesNotDuplicateTargets() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    manager.start(appContext, SERVER_URI).get(5, TimeUnit.SECONDS);

    assertEquals(1, manager.connectionCount());
    assertEquals(1, manager.registrationCount());
    stubTransport.assertNoAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
  }

  @Test
  void startQueuesRestartWhileStopIsInProgress() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    CompletableFuture<Void> queuedStart = manager.start(appContext, SERVER_URI);

    assertFalse(queuedStart.isDone());
    stubTransport.assertNoAttempt();

    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
    queuedStart.get(5, TimeUnit.SECONDS);

    StartedAttempt restartedAttempt = stubTransport.pollAttempt();

    assertEquals(1, manager.connectionCount());
    assertEquals(1, manager.registrationCount());

    CompletableFuture<Void> restartedStop = manager.stop();
    restartedAttempt.failTcpConnect();
    restartedStop.get(5, TimeUnit.SECONDS);
  }

  @Test
  void stopCallbackStartDoesNotOrphanQueuedRestart() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    CompletableFuture<Void> queuedStart = manager.start(appContext, SERVER_URI);
    var callbackStart = new AtomicReference<CompletableFuture<Void>>();

    stopFuture.thenRun(() -> callbackStart.set(manager.start(appContext, SERVER_URI)));

    attempt.failTcpConnect();

    stopFuture.get(5, TimeUnit.SECONDS);
    queuedStart.get(5, TimeUnit.SECONDS);
    CompletableFuture<Void> callbackStartFuture = callbackStart.get();
    assertNotNull(callbackStartFuture);
    callbackStartFuture.get(5, TimeUnit.SECONDS);

    StartedAttempt restartedAttempt = stubTransport.pollAttempt();

    assertEquals(1, manager.connectionCount());
    assertEquals(2, stubTransport.attemptCount());
    stubTransport.assertNoAttempt();

    CompletableFuture<Void> restartedStop = manager.stop();
    restartedAttempt.failTcpConnect();
    restartedStop.get(5, TimeUnit.SECONDS);
  }

  @Test
  void startCoalescesCompatibleQueuedRestartWhileStopIsInProgress() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    CompletableFuture<Void> queuedStart1 = manager.start(appContext, SERVER_URI);
    CompletableFuture<Void> queuedStart2 = manager.start(appContext, SERVER_URI);

    assertSame(queuedStart1, queuedStart2);
    assertFalse(queuedStart1.isDone());

    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
    queuedStart1.get(5, TimeUnit.SECONDS);

    StartedAttempt restartedAttempt = stubTransport.pollAttempt();

    CompletableFuture<Void> restartedStop = manager.stop();
    restartedAttempt.failTcpConnect();
    restartedStop.get(5, TimeUnit.SECONDS);
  }

  @Test
  void startFailsIncompatibleQueuedRestartWhileStopIsInProgress() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    CompletableFuture<Void> queuedStart = manager.start(appContext, SERVER_URI);
    CompletableFuture<Void> incompatibleStart = manager.start(appContext(), "urn:test:other");

    assertFalse(queuedStart.isDone());
    assertThrows(ExecutionException.class, () -> incompatibleStart.get(5, TimeUnit.SECONDS));

    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
    queuedStart.get(5, TimeUnit.SECONDS);

    StartedAttempt restartedAttempt = stubTransport.pollAttempt();

    CompletableFuture<Void> restartedStop = manager.stop();
    restartedAttempt.failTcpConnect();
    restartedStop.get(5, TimeUnit.SECONDS);
  }

  @Test
  void addReverseConnectDefersRegistrationWhileStopIsInProgress() throws Exception {
    ServerApplicationContext appContext = appContext();
    startManager(appContext);

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    CompletableFuture<Void> stopFuture = manager.stop();
    ReverseConnectHandle pendingHandle = manager.addReverseConnect(SECOND_CLIENT_URL, ENDPOINT_URL);

    assertNotNull(pendingHandle);
    assertEquals(2, manager.registrationCount());
    assertEquals(0, manager.connectionCount());
    stubTransport.assertNoAttempt();

    attempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);

    startManager(appContext);

    StartedAttempt restartedAttempt1 = stubTransport.pollAttempt();
    StartedAttempt restartedAttempt2 = stubTransport.pollAttempt();

    assertEquals(2, manager.connectionCount());

    CompletableFuture<Void> restartedStop = manager.stop();
    restartedAttempt1.failTcpConnect();
    restartedAttempt2.failTcpConnect();
    restartedStop.get(5, TimeUnit.SECONDS);
  }

  @Test
  void removeReverseConnectStopsActiveAndIdleAttempts() throws Exception {
    startManager(appContext());

    ReverseConnectHandle handle = manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt firstAttempt = stubTransport.pollAttempt();
    var activeChannel = new EmbeddedChannel();

    firstAttempt.connect(activeChannel);
    firstAttempt.openSecureChannel();

    StartedAttempt replacementAttempt = stubTransport.pollAttempt();
    var idleChannel = new EmbeddedChannel();
    replacementAttempt.connect(idleChannel);

    manager.removeReverseConnectAsync(handle).get(5, TimeUnit.SECONDS);

    assertFalse(activeChannel.isOpen());
    assertFalse(idleChannel.isOpen());
    assertEquals(0, manager.connectionCount());
    assertEquals(0, manager.registrationCount());
    stubTransport.assertNoAttempt();

    manager.stop().get(5, TimeUnit.SECONDS);
  }

  @Test
  void activeToIdleReplacementIsOwnedByTargetOwnerThroughManagerPath() throws Exception {
    startManager(appContext());

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt firstAttempt = stubTransport.pollAttempt();
    var activeChannel = new EmbeddedChannel();

    firstAttempt.connect(activeChannel);
    firstAttempt.openSecureChannel();

    StartedAttempt replacementAttempt = stubTransport.pollAttempt();

    assertEquals(2, stubTransport.attemptCount());
    assertEquals(1, manager.connectionCount());

    CompletableFuture<Void> stopFuture = manager.stop();
    replacementAttempt.failTcpConnect();
    stopFuture.get(5, TimeUnit.SECONDS);
  }

  @Test
  void stopDuringRetryStateDoesNotSpawnLateAttempts() throws Exception {
    startManager(appContext());

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();

    attempt.failTcpConnect();

    CompletableFuture<Void> stopFuture = manager.stop();
    stopFuture.get(5, TimeUnit.SECONDS);

    Thread.sleep(300);

    assertEquals(1, stubTransport.attemptCount());
  }

  @Test
  void stopWaitsForOwnedChannelCloseFuture() throws Exception {
    startManager(appContext());

    manager.addReverseConnect(CLIENT_URL, ENDPOINT_URL);
    StartedAttempt attempt = stubTransport.pollAttempt();
    ChannelPromise closePromise =
        new DefaultChannelPromise(new EmbeddedChannel(), ImmediateEventExecutor.INSTANCE);
    Channel channel = Mockito.mock(Channel.class);
    Mockito.when(channel.close()).thenReturn(closePromise);

    attempt.connect(channel);

    CompletableFuture<Void> stopFuture = manager.stop();

    assertFalse(stopFuture.isDone());

    closePromise.setSuccess();
    stopFuture.get(5, TimeUnit.SECONDS);

    Mockito.verify(channel).close();
  }

  private void startManager(ServerApplicationContext appContext) throws Exception {
    manager.start(appContext, SERVER_URI).get(5, TimeUnit.SECONDS);
  }

  private ServerApplicationContext appContext() {
    return appContext(endpointDescription(ENDPOINT_URL, TransportProfile.TCP_UASC_UABINARY));
  }

  private ServerApplicationContext appContext(EndpointDescription... endpoints) {
    List<EndpointDescription> endpointDescriptions = List.of(endpoints);

    ServerApplicationContext appContext = Mockito.mock(ServerApplicationContext.class);
    Mockito.when(appContext.getEndpointDescriptions()).thenReturn(endpointDescriptions);

    return appContext;
  }

  private EndpointDescription endpointDescription(String endpointUrl, TransportProfile profile) {
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(endpoint.getEndpointUrl()).thenReturn(endpointUrl);
    Mockito.when(endpoint.getTransportProfileUri()).thenReturn(profile.getUri());

    return endpoint;
  }

  private List<?> prepareStartLocked(ServerApplicationContext appContext) throws Exception {
    ReentrantLock lock = getManagerLock();

    lock.lock();
    try {
      return (List<?>)
          invokeManagerMethod(
              "prepareStartLocked",
              new Class<?>[] {ServerApplicationContext.class, String.class},
              appContext,
              SERVER_URI);
    } finally {
      lock.unlock();
    }
  }

  @SuppressWarnings("unchecked")
  private CompletableFuture<Void> finishStart(
      List<?> targetsToStart, ServerApplicationContext appContext) throws Exception {

    return (CompletableFuture<Void>)
        invokeManagerMethod(
            "finishStart",
            new Class<?>[] {List.class, ServerApplicationContext.class, String.class},
            targetsToStart,
            appContext,
            SERVER_URI);
  }

  private ReentrantLock getManagerLock() throws Exception {
    Field field = ReverseConnectManager.class.getDeclaredField("lock");
    field.setAccessible(true);

    return (ReentrantLock) field.get(manager);
  }

  private Object invokeManagerMethod(String name, Class<?>[] parameterTypes, Object... args)
      throws Exception {

    Method method = ReverseConnectManager.class.getDeclaredMethod(name, parameterTypes);
    method.setAccessible(true);

    try {
      return method.invoke(manager, args);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();

      if (cause instanceof Exception exception) {
        throw exception;
      }
      if (cause instanceof Error error) {
        throw error;
      }

      throw e;
    }
  }

  private static final class StubTransport extends OpcTcpReverseConnectServerTransport {

    private final List<StartedAttempt> attempts = new CopyOnWriteArrayList<>();
    private final LinkedBlockingQueue<StartedAttempt> attemptQueue = new LinkedBlockingQueue<>();

    private StubTransport() {
      super(null);
    }

    @Override
    protected ReverseConnectAttempt connectAttempt(
        ServerApplicationContext applicationContext,
        InetSocketAddress clientAddress,
        String serverUri,
        String endpointUrl,
        long connectTimeoutMs,
        Consumer<ReverseConnectAttempt.Outcome> outcomeConsumer) {

      var attempt = new ReverseConnectAttempt(outcomeConsumer);
      var startedAttempt = new StartedAttempt(attempt, clientAddress, serverUri, endpointUrl);

      attempts.add(startedAttempt);
      attemptQueue.offer(startedAttempt);

      return attempt;
    }

    int attemptCount() {
      return attempts.size();
    }

    StartedAttempt pollAttempt() throws Exception {
      StartedAttempt attempt = attemptQueue.poll(5, TimeUnit.SECONDS);
      if (attempt != null) {
        return attempt;
      }

      return fail("Timed out waiting for connectAttempt() to be called");
    }

    void assertNoAttempt() throws Exception {
      StartedAttempt attempt = attemptQueue.poll(250, TimeUnit.MILLISECONDS);
      if (attempt != null) {
        fail("connectAttempt() should not be called");
      }
    }
  }

  private static final class StartedAttempt {

    private final ReverseConnectAttempt attempt;
    private final InetSocketAddress clientAddress;
    private final String serverUri;
    private final String endpointUrl;

    private StartedAttempt(
        ReverseConnectAttempt attempt,
        InetSocketAddress clientAddress,
        String serverUri,
        String endpointUrl) {

      this.attempt = attempt;
      this.clientAddress = clientAddress;
      this.serverUri = serverUri;
      this.endpointUrl = endpointUrl;
    }

    InetSocketAddress clientAddress() {
      return clientAddress;
    }

    String serverUri() {
      return serverUri;
    }

    String endpointUrl() {
      return endpointUrl;
    }

    void connect(Channel channel) {
      attempt.channelInitialized(channel);
      attempt.tcpConnectSucceeded(channel);
    }

    void failTcpConnect() {
      attempt.tcpConnectFailed(new Exception("connection refused"));
    }

    void openSecureChannel() {
      attempt.secureChannelOpened(1L);
    }
  }
}
