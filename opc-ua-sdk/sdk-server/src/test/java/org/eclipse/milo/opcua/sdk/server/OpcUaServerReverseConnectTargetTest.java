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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptState;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetListener;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetSnapshot;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
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
  void updateUnknownTargetThrows() throws Exception {
    EndpointConfig endpoint = endpoint();
    server = newServer(endpoint, Set.of(), false);
    server.startup().get(5, TimeUnit.SECONDS);

    ReverseConnectTarget target =
        targetBuilder(endpoint, unusedPort(), uint(250)).setId(UUID.randomUUID()).build();

    assertThrows(IllegalArgumentException.class, () -> server.updateReverseConnectTarget(target));
  }

  private OpcUaServer newServer(
      EndpointConfig endpoint, Set<ReverseConnectTarget> targets, boolean failBind) {

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = Executors.newSingleThreadScheduledExecutor();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server:reverse-targets")
            .setProductUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("Reverse Target Test Server"))
            .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
            .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
            .setEndpoints(Set.of(endpoint))
            .setReverseConnectTargets(targets)
            .setExecutor(listenerExecutor)
            .setScheduledExecutor(scheduler)
            .build();

    return new OpcUaServer(
        config,
        transportProfile -> {
          if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
            return new TestTcpServerTransport(
                OpcTcpServerTransportConfig.newBuilder().build(), failBind);
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
    private final List<ReverseConnectAttemptEvent> events = new CopyOnWriteArrayList<>();

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
}
