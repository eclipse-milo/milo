/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptState;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectRetryPolicy;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetListener;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetSnapshot;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttempt;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectParameters;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OpcUaServerReverseConnectTargetTest {

  private ExecutorService listenerExecutor;
  private ScheduledExecutorService scheduler;
  private OpcUaServer server;

  @AfterEach
  void tearDown() throws Exception {
    if (server != null) {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
    if (listenerExecutor != null) {
      listenerExecutor.shutdownNow();
    }
    if (scheduler != null) {
      scheduler.shutdownNow();
    }
  }

  @Test
  void reverseOnlyStartupSucceedsWithSchedulableTarget() throws Exception {
    EndpointConfig endpoint = endpoint();
    ReverseConnectTarget target = target(endpoint, unusedPort(), uint(1_000));

    server = newServer(endpoint, Set.of(target), true);

    assertEquals(server, server.startup().get(5, TimeUnit.SECONDS));
    assertTrue(server.getBoundEndpoints().isEmpty());
    assertEquals(1, server.getReverseConnectTargetSnapshots().size());
  }

  @Test
  void startupFailsWithoutPassiveBindOrSchedulableTarget() {
    EndpointConfig endpoint = endpoint();
    server = newServer(endpoint, Set.of(), true);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));

    UaException cause = assertInstanceOf(UaException.class, ex.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
  }

  @Test
  void addTargetNotifiesAddedBeforeZeroDelayScheduleUpdate() throws Exception {
    EndpointConfig endpoint = endpoint();
    listenerExecutor = mock(ExecutorService.class);
    doAnswer(
            invocation -> {
              invocation.getArgument(0, Runnable.class).run();
              return null;
            })
        .when(listenerExecutor)
        .execute(any(Runnable.class));

    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    doAnswer(
            invocation -> {
              invocation.getArgument(0, Runnable.class).run();
              return scheduledFuture;
            })
        .when(scheduler)
        .schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));

    server =
        newServer(
            endpoint,
            Set.of(),
            new TestTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build(), false),
            listenerExecutor,
            scheduler);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTarget target = target(endpoint, unusedPort(), uint(250));
    server.addReverseConnectTarget(target);

    assertTrue(listener.notifications.contains("updated"));
    assertEquals("added", listener.notifications.get(0));
  }

  @Test
  void addPauseResumeTriggerRemoveAndSnapshots() throws Exception {
    EndpointConfig endpoint = endpoint();
    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTarget target = target(endpoint, unusedPort(), uint(250));
    ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

    ReverseConnectAttemptEvent firstFailure =
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTargetSnapshot failedSnapshot = handle.snapshot().orElseThrow();
    assertEquals(target.getId(), failedSnapshot.targetId());
    assertNotNull(failedSnapshot.lastAttemptTime());
    assertNotNull(failedSnapshot.nextAttemptTime());
    assertNotNull(failedSnapshot.lastError());
    assertEquals(0, failedSnapshot.activeChannelCount());
    assertFalse(
        failedSnapshot.nextAttemptTime().isBefore(firstFailure.timestamp().plusMillis(200)));

    handle.trigger().get(5, TimeUnit.SECONDS);

    waitForEvent(
        listener,
        event ->
            event.targetId().equals(target.getId())
                && event.attemptNumber() == 2L
                && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTargetSnapshot paused = handle.pause().get(5, TimeUnit.SECONDS);
    assertTrue(paused.paused());
    assertNull(paused.nextAttemptTime());

    int eventCountAfterPause = listener.events.size();
    Thread.sleep(350);
    assertEquals(eventCountAfterPause, listener.events.size());

    ReverseConnectTargetSnapshot resumed = handle.resume().get(5, TimeUnit.SECONDS);
    assertFalse(resumed.paused());

    waitForEvent(
        listener,
        event ->
            event.targetId().equals(target.getId())
                && event.attemptNumber() == 3L
                && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTargetSnapshot removed = handle.remove().get(5, TimeUnit.SECONDS);
    assertEquals(target.getId(), removed.targetId());
    assertTrue(server.getReverseConnectTargetSnapshots().isEmpty());
    waitUntil(
        () ->
            listener.removed.stream()
                .anyMatch(snapshot -> snapshot.targetId().equals(target.getId())));
  }

  @Test
  void updatePausedTargetToUnpausedSchedulesAttempt() throws Exception {
    EndpointConfig endpoint = endpoint();
    ReverseConnectTarget target =
        targetBuilder(endpoint, unusedPort(), uint(5_000)).setPaused(true).build();

    server = newServer(endpoint, Set.of(target), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    assertTrue(listener.events.isEmpty());

    ReverseConnectTargetSnapshot updated =
        server
            .updateReverseConnectTarget(
                targetBuilder(endpoint, unusedPort(), uint(250)).setId(target.getId()).build())
            .get(5, TimeUnit.SECONDS);

    assertFalse(updated.paused());
    assertNotNull(updated.nextAttemptTime());

    waitForEvent(
        listener,
        event ->
            event.targetId().equals(target.getId())
                && event.attemptNumber() == 1L
                && event.state() == ReverseConnectAttemptState.FAILED);
  }

  @Test
  void updateTargetUsesReplacementForFutureAttempts() throws Exception {
    EndpointConfig endpoint = endpoint();
    ReverseConnectTarget target = target(endpoint, unusedPort(), uint(5_000));

    server = newServer(endpoint, Set.of(target), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    waitForEvent(
        listener,
        event ->
            event.targetId().equals(target.getId())
                && event.attemptNumber() == 1L
                && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTarget replacement =
        targetBuilder(endpoint, unusedPort(), uint(250)).setId(target.getId()).build();

    ReverseConnectTargetSnapshot updated =
        server.updateReverseConnectTarget(replacement).get(5, TimeUnit.SECONDS);

    assertEquals(replacement.getClientListenerUrl(), updated.clientListenerUrl());
    assertEquals(replacement.getRegistrationPeriod(), updated.registrationPeriod());

    ReverseConnectAttemptEvent secondFailure =
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 2L
                    && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTargetSnapshot snapshot =
        server.getReverseConnectTargetSnapshot(target.getId()).orElseThrow();
    Instant nextAttemptTime = snapshot.nextAttemptTime();

    assertNotNull(nextAttemptTime);
    assertFalse(nextAttemptTime.isBefore(secondFailure.timestamp().plusMillis(200)));
  }

  @Test
  void throwingRetryPolicyDoesNotSuppressTerminalNotifications() throws Exception {
    EndpointConfig endpoint = endpoint();
    CountDownLatch policyInvoked = new CountDownLatch(1);
    RuntimeException policyFailure = new RuntimeException("retry policy failure");
    ReverseConnectTarget target =
        targetBuilder(endpoint, unusedPort(), uint(5_000))
            .setRetryPolicy(
                (retryTarget, event) -> {
                  policyInvoked.countDown();
                  throw policyFailure;
                })
            .build();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

    ReverseConnectAttemptEvent failure =
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.FAILED);

    assertTrue(policyInvoked.await(3, TimeUnit.SECONDS));
    waitUntil(
        () ->
            listener.updated.stream()
                .anyMatch(
                    snapshot ->
                        snapshot.targetId().equals(target.getId())
                            && snapshot.lastError() != null
                            && snapshot.nextAttemptTime() != null));

    ReverseConnectTargetSnapshot snapshot = handle.snapshot().orElseThrow();
    assertNotNull(snapshot.lastError());
    assertNotNull(snapshot.nextAttemptTime());
    assertFalse(snapshot.nextAttemptTime().isBefore(failure.timestamp().plusMillis(4_000)));

    ReverseConnectTargetSnapshot paused = handle.pause().get(5, TimeUnit.SECONDS);
    assertTrue(paused.paused());
    assertNull(paused.nextAttemptTime());
  }

  @Test
  void blockingRetryPolicyDoesNotHoldTargetManagerLock() throws Exception {
    EndpointConfig endpoint = endpoint();
    CountDownLatch policyEntered = new CountDownLatch(1);
    CountDownLatch releasePolicy = new CountDownLatch(1);
    ReverseConnectTarget target =
        targetBuilder(endpoint, unusedPort(), uint(5_000))
            .setRetryPolicy(
                (retryTarget, event) -> {
                  policyEntered.countDown();
                  try {
                    assertTrue(releasePolicy.await(5, TimeUnit.SECONDS));
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(e);
                  }
                  return retryTarget.getRegistrationPeriod().longValue();
                })
            .build();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);
    assertTrue(policyEntered.await(5, TimeUnit.SECONDS));

    ExecutorService controlExecutor = Executors.newSingleThreadExecutor();
    try {
      Future<ReverseConnectTargetSnapshot> pauseFuture =
          controlExecutor.submit(() -> handle.pause().get(5, TimeUnit.SECONDS));

      ReverseConnectTargetSnapshot paused = pauseFuture.get(1, TimeUnit.SECONDS);
      assertTrue(paused.paused());
      assertNull(paused.nextAttemptTime());
    } finally {
      releasePolicy.countDown();
      controlExecutor.shutdownNow();
    }

    waitForEvent(
        listener,
        event ->
            event.targetId().equals(target.getId())
                && event.attemptNumber() == 1L
                && event.state() == ReverseConnectAttemptState.FAILED);

    ReverseConnectTargetSnapshot snapshot = handle.snapshot().orElseThrow();
    assertTrue(snapshot.paused());
    assertNull(snapshot.nextAttemptTime());
  }

  @Test
  void pauseDuringHandoffDoesNotCloseHandedOffChannel() throws Exception {
    EndpointConfig endpoint = endpoint();
    BlockingHandoffTcpServerTransport transport =
        new BlockingHandoffTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());

    server = newServer(endpoint, Set.of(), transport);
    server.startup().get(5, TimeUnit.SECONDS);

    try (ServerSocket listener = newListener()) {
      ReverseConnectTarget target = target(endpoint, listener.getLocalPort(), uint(5_000));
      ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

      try (Socket socket = accept(listener)) {
        ReverseHelloMessage reverseHello = readReverseHello(socket);
        assertEquals(endpoint.getEndpointUrl(), reverseHello.getEndpointUrl());

        writeMessage(socket, TcpMessageEncoder.encode(newHelloMessage(endpoint)));

        assertTrue(transport.handoffObserved.await(3, TimeUnit.SECONDS));

        ReverseConnectTargetSnapshot paused = handle.pause().get(5, TimeUnit.SECONDS);
        assertTrue(paused.paused());

        transport.releaseHandoff.countDown();

        AcknowledgeMessage acknowledge = readAcknowledge(socket);
        assertEquals(0L, acknowledge.getProtocolVersion());

        waitUntil(
            () ->
                handle
                    .snapshot()
                    .map(snapshot -> snapshot.activeChannelCount() == 1)
                    .orElse(false));
      }
    }
  }

  @Test
  void triggerDoesNotScheduleAttemptWithActiveChannel() throws Exception {
    EndpointConfig endpoint = endpoint();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    try (ServerSocket clientListener = newListener()) {
      ReverseConnectTarget target = target(endpoint, clientListener.getLocalPort(), uint(5_000));
      ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

      try (Socket ignored = establishActiveChannel(clientListener, endpoint, handle)) {
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.HANDOFF);

        ReverseConnectTargetSnapshot triggered = handle.trigger().get(5, TimeUnit.SECONDS);

        assertEquals(1, triggered.activeChannelCount());
        assertNull(triggered.nextAttemptTime());
        assertEquals(1L, maxAttemptNumber(listener, target.getId()));
      }
    }
  }

  @Test
  void activeChannelCloseSchedulesReconnectWithRetryPolicy() throws Exception {
    EndpointConfig endpoint = endpoint();
    CountDownLatch policyInvoked = new CountDownLatch(1);
    AtomicReference<ReverseConnectAttemptEvent> policyEvent = new AtomicReference<>();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    try (ServerSocket clientListener = newListener()) {
      ReverseConnectTarget target =
          targetBuilder(endpoint, clientListener.getLocalPort(), uint(10_000))
              .setRetryPolicy(
                  (retryTarget, event) -> {
                    policyEvent.set(event);
                    policyInvoked.countDown();
                    return ReverseConnectRetryPolicy.fixedDelay(uint(1_000))
                        .getRetryDelayMillis(retryTarget, event);
                  })
              .build();
      ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

      try (Socket socket = establishActiveChannel(clientListener, endpoint, handle)) {
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.HANDOFF);

        Instant closedAt = Instant.now();
        socket.close();

        assertTrue(policyInvoked.await(3, TimeUnit.SECONDS));
        assertEquals(ReverseConnectAttemptState.CLOSED, policyEvent.get().state());
        assertEquals(1L, policyEvent.get().attemptNumber());

        waitUntil(
            () ->
                handle
                    .snapshot()
                    .map(
                        snapshot ->
                            snapshot.activeChannelCount() == 0
                                && snapshot.nextAttemptTime() != null)
                    .orElse(false));

        ReverseConnectTargetSnapshot snapshot = handle.snapshot().orElseThrow();
        assertTrue(snapshot.nextAttemptTime().isBefore(closedAt.plusMillis(3_000)));
      }
    }
  }

  @Test
  void resumeDoesNotScheduleAttemptWithActiveChannel() throws Exception {
    EndpointConfig endpoint = endpoint();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    try (ServerSocket clientListener = newListener()) {
      ReverseConnectTarget target = target(endpoint, clientListener.getLocalPort(), uint(5_000));
      ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

      try (Socket ignored = establishActiveChannel(clientListener, endpoint, handle)) {
        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.HANDOFF);

        ReverseConnectTargetSnapshot paused = handle.pause().get(5, TimeUnit.SECONDS);
        assertTrue(paused.paused());
        assertEquals(1, paused.activeChannelCount());

        ReverseConnectTargetSnapshot resumed = handle.resume().get(5, TimeUnit.SECONDS);

        assertFalse(resumed.paused());
        assertEquals(1, resumed.activeChannelCount());
        assertNull(resumed.nextAttemptTime());
        assertEquals(1L, maxAttemptNumber(listener, target.getId()));
      }
    }
  }

  @Test
  void pauseEmitsTerminalEventForInvalidatedAttemptGeneration() throws Exception {
    EndpointConfig endpoint = endpoint();

    server = newServer(endpoint, Set.of(), false);

    RecordingListener listener = new RecordingListener();
    server.addReverseConnectTargetListener(listener);
    server.startup().get(5, TimeUnit.SECONDS);

    try (ServerSocket clientListener = newListener()) {
      ReverseConnectTarget target = target(endpoint, clientListener.getLocalPort(), uint(5_000));
      ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);

      try (Socket socket = accept(clientListener)) {
        ReverseHelloMessage reverseHello = readReverseHello(socket);
        assertEquals(endpoint.getEndpointUrl(), reverseHello.getEndpointUrl());

        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.HELLO_HANDLER_INSTALLED);

        ReverseConnectTargetSnapshot paused = handle.pause().get(5, TimeUnit.SECONDS);
        assertTrue(paused.paused());
        assertNull(paused.nextAttemptTime());

        waitForEvent(
            listener,
            event ->
                event.targetId().equals(target.getId())
                    && event.attemptNumber() == 1L
                    && event.state() == ReverseConnectAttemptState.CLOSED);
      }
    }
  }

  @Test
  void updateUnknownTargetReturnsFailedFuture() throws Exception {
    EndpointConfig endpoint = endpoint();
    server = newServer(endpoint, Set.of(), false);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTarget target =
        targetBuilder(endpoint, unusedPort(), uint(250)).setId(UUID.randomUUID()).build();

    CompletableFuture<ReverseConnectTargetSnapshot> future =
        server.updateReverseConnectTarget(target);

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
    assertInstanceOf(IllegalArgumentException.class, exception.getCause());
  }

  private OpcUaServer newServer(
      EndpointConfig endpoint, Set<ReverseConnectTarget> targets, boolean failBind) {

    return newServer(
        endpoint,
        targets,
        new TestTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build(), failBind));
  }

  private OpcUaServer newServer(
      EndpointConfig endpoint, Set<ReverseConnectTarget> targets, OpcTcpServerTransport transport) {

    return newServer(
        endpoint,
        targets,
        transport,
        Executors.newSingleThreadExecutor(),
        Executors.newSingleThreadScheduledExecutor());
  }

  private OpcUaServer newServer(
      EndpointConfig endpoint,
      Set<ReverseConnectTarget> targets,
      OpcTcpServerTransport transport,
      ExecutorService executor,
      ScheduledExecutorService scheduledExecutor) {

    listenerExecutor = executor;
    scheduler = scheduledExecutor;

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server:reverse-targets")
            .setProductUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("Reverse Target Test Server"))
            .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
            .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
            .setEndpoints(Set.of(endpoint))
            .setReverseConnectTargets(targets)
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .build();

    return new OpcUaServer(
        config,
        transportProfile -> {
          if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
            return transport;
          } else {
            throw new IllegalArgumentException("unexpected transport profile: " + transportProfile);
          }
        });
  }

  private static EndpointConfig endpoint() {
    return EndpointConfig.newBuilder()
        .setBindAddress("localhost")
        .setHostname("localhost")
        .setBindPort(0)
        .setPath("/reverse-target-test")
        .build();
  }

  private static ReverseConnectTarget target(
      EndpointConfig endpoint, int listenerPort, UInteger registrationPeriod) {

    return targetBuilder(endpoint, listenerPort, registrationPeriod).build();
  }

  private static ReverseConnectTarget.Builder targetBuilder(
      EndpointConfig endpoint, int listenerPort, UInteger registrationPeriod) {

    return ReverseConnectTarget.builder()
        .setClientListenerUrl("opc.tcp://localhost:" + listenerPort)
        .setEndpointUrl(endpoint.getEndpointUrl())
        .setRegistrationPeriod(registrationPeriod)
        .setConnectTimeout(uint(100));
  }

  private static int unusedPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static ServerSocket newListener() throws IOException {
    ServerSocket listener = new ServerSocket();
    listener.bind(new InetSocketAddress("127.0.0.1", 0));
    listener.setSoTimeout(3_000);
    return listener;
  }

  private static Socket accept(ServerSocket listener) throws IOException {
    Socket socket = listener.accept();
    socket.setSoTimeout(3_000);
    return socket;
  }

  private static ReverseHelloMessage readReverseHello(Socket socket) throws Exception {
    ByteBuf buffer = readMessage(socket);
    try {
      return TcpMessageDecoder.decodeReverseHello(buffer);
    } finally {
      buffer.release();
    }
  }

  private static AcknowledgeMessage readAcknowledge(Socket socket) throws Exception {
    ByteBuf buffer = readMessage(socket);
    try {
      return TcpMessageDecoder.decodeAcknowledge(buffer);
    } finally {
      buffer.release();
    }
  }

  private static ByteBuf readMessage(Socket socket) throws IOException {
    InputStream inputStream = socket.getInputStream();
    byte[] header = inputStream.readNBytes(8);

    assertEquals(8, header.length);

    int messageLength =
        (header[4] & 0xFF)
            | ((header[5] & 0xFF) << 8)
            | ((header[6] & 0xFF) << 16)
            | ((header[7] & 0xFF) << 24);

    byte[] body = inputStream.readNBytes(messageLength - 8);
    assertEquals(messageLength - 8, body.length);

    return Unpooled.wrappedBuffer(header, body);
  }

  private static Socket establishActiveChannel(
      ServerSocket listener, EndpointConfig endpoint, ReverseConnectTargetHandle handle)
      throws Exception {

    Socket socket = accept(listener);
    try {
      ReverseHelloMessage reverseHello = readReverseHello(socket);
      assertEquals(endpoint.getEndpointUrl(), reverseHello.getEndpointUrl());

      writeMessage(socket, TcpMessageEncoder.encode(newHelloMessage(endpoint)));

      AcknowledgeMessage acknowledge = readAcknowledge(socket);
      assertEquals(0L, acknowledge.getProtocolVersion());

      waitUntil(
          () ->
              handle.snapshot().map(snapshot -> snapshot.activeChannelCount() == 1).orElse(false));

      return socket;
    } catch (Throwable t) {
      socket.close();
      throw t;
    }
  }

  private static void writeMessage(Socket socket, ByteBuf buffer) throws Exception {
    try {
      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);

      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
    } finally {
      buffer.release();
    }
  }

  private static HelloMessage newHelloMessage(EndpointConfig endpoint) {
    return new HelloMessage(0L, 8192L, 8192L, 8192L, 8L, endpoint.getEndpointUrl());
  }

  private static ReverseConnectAttemptEvent waitForEvent(
      RecordingListener listener, Predicate<ReverseConnectAttemptEvent> predicate)
      throws InterruptedException {

    Instant deadline = Instant.now().plus(Duration.ofSeconds(5));
    while (Instant.now().isBefore(deadline)) {
      for (ReverseConnectAttemptEvent event : listener.events) {
        if (predicate.test(event)) {
          return event;
        }
      }
      Thread.sleep(10);
    }

    throw new AssertionError("timed out waiting for Reverse Connect attempt event");
  }

  private static long maxAttemptNumber(RecordingListener listener, UUID targetId) {
    return listener.events.stream()
        .filter(event -> event.targetId().equals(targetId))
        .mapToLong(ReverseConnectAttemptEvent::attemptNumber)
        .max()
        .orElse(0L);
  }

  private static void waitUntil(BooleanSupplier condition) throws InterruptedException {
    Instant deadline = Instant.now().plus(Duration.ofSeconds(5));
    while (Instant.now().isBefore(deadline)) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }

    throw new AssertionError("timed out waiting for condition");
  }

  private static final class RecordingListener implements ReverseConnectTargetListener {

    private final List<ReverseConnectTargetSnapshot> removed = new CopyOnWriteArrayList<>();
    private final List<ReverseConnectTargetSnapshot> updated = new CopyOnWriteArrayList<>();
    private final List<ReverseConnectAttemptEvent> events = new CopyOnWriteArrayList<>();
    private final List<String> notifications = new CopyOnWriteArrayList<>();

    @Override
    public void onTargetAdded(@NonNull ReverseConnectTargetSnapshot snapshot) {
      notifications.add("added");
    }

    @Override
    public void onTargetUpdated(@NonNull ReverseConnectTargetSnapshot snapshot) {
      updated.add(snapshot);
      notifications.add("updated");
    }

    @Override
    public void onTargetRemoved(@NonNull ReverseConnectTargetSnapshot snapshot) {
      removed.add(snapshot);
    }

    @Override
    public void onAttemptEvent(@NonNull ReverseConnectAttemptEvent event) {
      events.add(event);
    }
  }

  private static final class TestTcpServerTransport extends OpcTcpServerTransport {

    private final boolean failBind;

    private TestTcpServerTransport(OpcTcpServerTransportConfig config, boolean failBind) {
      super(config);
      this.failBind = failBind;
    }

    @Override
    public synchronized void bind(
        ServerApplicationContext applicationContext, InetSocketAddress bindAddress)
        throws Exception {

      if (failBind) {
        throw new IOException("passive bind disabled for test");
      }

      super.bind(applicationContext, bindAddress);
    }
  }

  private static final class BlockingHandoffTcpServerTransport extends OpcTcpServerTransport {

    private final CountDownLatch handoffObserved = new CountDownLatch(1);
    private final CountDownLatch releaseHandoff = new CountDownLatch(1);

    private BlockingHandoffTcpServerTransport(OpcTcpServerTransportConfig config) {
      super(config);
    }

    @Override
    public OpcTcpServerReverseConnectAttempt connectReverse(
        OpcTcpServerReverseConnectParameters parameters) {

      var wrappedParameters =
          new OpcTcpServerReverseConnectParameters(
              parameters.applicationContext(),
              parameters.clientListenerUrl(),
              parameters.clientListenerAddress(),
              parameters.endpointUrl(),
              parameters.serverUri(),
              parameters.connectTimeoutMillis(),
              event -> onStateTransition(parameters, event));

      return super.connectReverse(wrappedParameters);
    }

    private void onStateTransition(
        OpcTcpServerReverseConnectParameters parameters,
        OpcTcpServerReverseConnectAttemptEvent event) {

      parameters.observer().onStateTransition(event);

      if (event.state() == OpcTcpServerReverseConnectAttemptState.HANDOFF) {
        handoffObserved.countDown();
        try {
          assertTrue(releaseHandoff.await(3, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new AssertionError(e);
        }
      }
    }
  }
}
